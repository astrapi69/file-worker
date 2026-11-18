/**
 * The MIT License
 *
 * Copyright (C) 2015 Asterios Raptis
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package io.github.astrapi69.file.tools;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.FileTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.meanbean.test.BeanTester;

/**
 * Uses JUnit 5 (`@Test`, `@TempDir`). Invokes `MergeWithTemplate.run(args, configFile)` with
 * explicit `--src_base`, `--dst_base`, `--dirs` and a config file inside the temporary directory
 * (so it does **not** depend on `./config/merge_defaults.json` and never ends the JVM). The config
 * file names default bases inside the temporary directory too, so that nothing falls back to the
 * working directory. Verifies: copy when destination subdir is missing, merge into existing
 * directory (overwrites), dry-run doesn’t change filesystem, multiple directories are handled.
 */
class MergeWithTemplateTest
{

	private static void write(Path file, String content) throws IOException
	{
		Files.createDirectories(file.getParent());
		Files.writeString(file, content, StandardCharsets.UTF_8, StandardOpenOption.CREATE,
			StandardOpenOption.TRUNCATE_EXISTING);
	}

	private static String read(Path file) throws IOException
	{
		return Files.readString(file, StandardCharsets.UTF_8);
	}

	/**
	 * Writes a config file into the given temporary directory. Its default bases point into the
	 * temporary directory as well and it names no default directories, so the defaults neither
	 * replace the explicit arguments nor lead anywhere outside the temporary directory.
	 */
	private static Path config(Path tmp) throws IOException
	{
		Path configFile = tmp.resolve("config/merge_defaults.json");
		String json = "{ \"default_src_base\": \""
			+ tmp.resolve("configSrcBase").toString().replace("\\", "/")
			+ "\", \"default_dst_base\": \""
			+ tmp.resolve("configDstBase").toString().replace("\\", "/") + "\" }";
		write(configFile, json);
		return configFile;
	}

	/**
	 * Writes a config file into the given temporary directory that names the given default files
	 * and, like {@link #config(Path)}, no default directories.
	 */
	private static Path configWithFiles(Path tmp, String... files) throws IOException
	{
		Path configFile = tmp.resolve("config/merge_defaults.json");
		write(configFile, "{ \"default_files\": [\"" + String.join("\", \"", files) + "\"] }");
		return configFile;
	}

	/**
	 * Runs the given action and returns what it printed to the standard output, which is the report
	 * of the command line.
	 */
	private static String stdoutOf(Executable action) throws Throwable
	{
		PrintStream original = System.out;
		ByteArrayOutputStream captured = new ByteArrayOutputStream();
		try (PrintStream stdout = new PrintStream(captured, true, StandardCharsets.UTF_8))
		{
			System.setOut(stdout);
			action.execute();
		}
		finally
		{
			System.setOut(original);
		}
		return captured.toString(StandardCharsets.UTF_8);
	}

	@Test
	void copiesWhenDestinationSubdirDoesNotExist(@TempDir Path tmp) throws Exception
	{
		Path srcBase = tmp.resolve("srcBase");
		Path dstBase = tmp.resolve("dstBase");
		Path srcScripts = srcBase.resolve("scripts");
		Path srcFile = srcScripts.resolve("a.txt");

		write(srcFile, "A");
		assertFalse(Files.exists(dstBase.resolve("scripts")));

		MergeWithTemplate.run(new String[] { "--src_base", srcBase.toString(), "--dst_base",
				dstBase.toString(), "--dirs", "scripts" },
			config(tmp), tmp.resolve("work"));

		Path dstFile = dstBase.resolve("scripts/a.txt");
		assertTrue(Files.exists(dstFile), "Destination file should exist after copy");
		assertEquals("A", read(dstFile));
	}

	@Test
	void mergesIntoExistingDirectory_overwritesTargetFiles(@TempDir Path tmp) throws Exception
	{
		Path srcBase = tmp.resolve("srcBase");
		Path dstBase = tmp.resolve("dstBase");

		Path dstScripts = dstBase.resolve("scripts");
		Path dstFile = dstScripts.resolve("x.txt");
		write(dstFile, "OLD");
		Files.setLastModifiedTime(dstFile, FileTime.fromMillis(1_000L));

		Path srcScripts = srcBase.resolve("scripts");
		Path srcFile = srcScripts.resolve("x.txt");
		write(srcFile, "NEW");
		Files.setLastModifiedTime(srcFile, FileTime.fromMillis(2_000L));

		// Destination subdir exists -> CLI uses MergeDirectoryExtensions.merge()
		MergeWithTemplate.run(new String[] { "--src_base", srcBase.toString(), "--dst_base",
				dstBase.toString(), "--dirs", "scripts" },
			config(tmp), tmp.resolve("work"));

		assertTrue(Files.exists(dstFile));
		assertEquals("NEW", read(dstFile),
			"Merge should overwrite destination with source content");
	}

	@Test
	void dryRunDoesNotChangeFilesystem(@TempDir Path tmp) throws Exception
	{
		Path srcBase = tmp.resolve("srcBase");
		Path dstBase = tmp.resolve("dstBase");

		Path srcScripts = srcBase.resolve("scripts");
		Path f1 = srcScripts.resolve("a.txt");
		write(f1, "A");

		// no scripts/ at destination yet
		assertFalse(Files.exists(dstBase.resolve("scripts")));

		MergeWithTemplate.run(new String[] { "--src_base", srcBase.toString(), "--dst_base",
				dstBase.toString(), "--dirs", "scripts", "--dry-run" },
			config(tmp), tmp.resolve("work"));

		// still no scripts/ created
		assertFalse(Files.exists(dstBase.resolve("scripts")),
			"Dry-run must not create destination directories or copy files");
	}

	@Test
	void copiesMultipleDirs(@TempDir Path tmp) throws Exception
	{
		Path srcBase = tmp.resolve("srcBase");
		Path dstBase = tmp.resolve("dstBase");

		write(srcBase.resolve("scripts/tool.sh"), "#!/bin/sh\necho hi\n");
		write(srcBase.resolve("tests/test.txt"), "T");

		MergeWithTemplate.run(new String[] { "--src_base", srcBase.toString(), "--dst_base",
				dstBase.toString(), "--dirs", "scripts", "tests" },
			config(tmp), tmp.resolve("work"));

		assertTrue(Files.exists(dstBase.resolve("scripts/tool.sh")));
		assertTrue(Files.exists(dstBase.resolve("tests/test.txt")));
	}

	@Test
	void theDestinationBaseIsTheWorkingDirectory_whenNeitherArgumentsNorDefaultsNameIt(
		@TempDir Path tmp) throws Exception
	{
		Path srcBase = tmp.resolve("srcBase");
		Path work = tmp.resolve("work");
		write(srcBase.resolve("scripts/a.txt"), "A");

		// a missing config file means no defaults
		MergeWithTemplate.run(
			new String[] { "--src_base", srcBase.toString(), "--dirs", "scripts" },
			tmp.resolve("no-config.json"), work);

		assertEquals("A", read(work.resolve("scripts/a.txt")));
	}

	@Test
	void theSourceBaseIsTheWorkingDirectory_whenNeitherArgumentsNorDefaultsNameIt(@TempDir Path tmp)
		throws Exception
	{
		Path dstBase = tmp.resolve("dstBase");
		Path work = tmp.resolve("work");
		write(work.resolve("scripts/a.txt"), "A");

		MergeWithTemplate.run(
			new String[] { "--dst_base", dstBase.toString(), "--dirs", "scripts" },
			tmp.resolve("no-config.json"), work);

		assertEquals("A", read(dstBase.resolve("scripts/a.txt")));
	}

	@Test
	void copiesTheDefaultFiles_overwritingAndKeepingTheirModificationTime(@TempDir Path tmp)
		throws Throwable
	{
		Path srcBase = tmp.resolve("srcBase");
		Path dstBase = tmp.resolve("dstBase");
		write(srcBase.resolve("scripts/tool.sh"), "S");
		write(srcBase.resolve("a.txt"), "A");
		Files.setLastModifiedTime(srcBase.resolve("a.txt"), FileTime.fromMillis(2_000L));
		write(srcBase.resolve("sub/b.txt"), "B");
		write(dstBase.resolve("a.txt"), "OLD");

		String report = stdoutOf(() -> MergeWithTemplate.run(
			new String[] { "--src_base", srcBase.toString(), "--dst_base", dstBase.toString(),
					"--dirs", "scripts" },
			configWithFiles(tmp, "a.txt", "sub/b.txt", "missing.txt"), tmp.resolve("work")));

		assertEquals("A", read(dstBase.resolve("a.txt")));
		assertEquals(2_000L, Files.getLastModifiedTime(dstBase.resolve("a.txt")).toMillis());
		assertEquals("B", read(dstBase.resolve("sub/b.txt")));
		assertFalse(Files.exists(dstBase.resolve("missing.txt")));
		assertTrue(
			report.contains(
				"Files to process: [a.txt, sub/b.txt, missing.txt]" + System.lineSeparator()),
			report);
		assertTrue(report.contains("Copied file " + srcBase.resolve("a.txt").toAbsolutePath()
			+ " -> " + dstBase.resolve("a.txt").toAbsolutePath()), report);
		assertTrue(report.contains("File not found in template (skip): "
			+ srcBase.resolve("missing.txt").toAbsolutePath()), report);
		assertFalse(
			report.contains("Copied file " + srcBase.resolve("missing.txt").toAbsolutePath()),
			report);
	}

	@Test
	void dryRunDoesNotCopyTheDefaultFiles_butReportsThem(@TempDir Path tmp) throws Throwable
	{
		Path srcBase = tmp.resolve("srcBase");
		Path dstBase = tmp.resolve("dstBase");
		write(srcBase.resolve("scripts/tool.sh"), "S");
		write(srcBase.resolve("a.txt"), "A");

		String report = stdoutOf(() -> MergeWithTemplate.run(
			new String[] { "--src_base", srcBase.toString(), "--dst_base", dstBase.toString(),
					"--dirs", "scripts", "--dry-run" },
			configWithFiles(tmp, "a.txt"), tmp.resolve("work")));

		assertFalse(Files.exists(dstBase.resolve("a.txt")));
		assertTrue(report.contains("Files to process: [a.txt] (dry-run)"), report);
		assertTrue(report.contains("DRY-RUN copy file " + srcBase.resolve("a.txt").toAbsolutePath()
			+ " -> " + dstBase.resolve("a.txt").toAbsolutePath()), report);
		assertFalse(report.contains("Copied file"), report);
	}

	@Test
	void reportsNoFilesToProcess_whenTheConfigNamesNone(@TempDir Path tmp) throws Throwable
	{
		Path srcBase = tmp.resolve("srcBase");
		Path dstBase = tmp.resolve("dstBase");
		write(srcBase.resolve("scripts/tool.sh"), "S");

		String report = stdoutOf(
			() -> MergeWithTemplate
				.run(
					new String[] { "--src_base", srcBase.toString(), "--dst_base",
							dstBase.toString(), "--dirs", "scripts" },
					config(tmp), tmp.resolve("work")));

		assertFalse(report.contains("Files to process"), report);
	}

	@ParameterizedTest(name = "dry-run: {0}, destination exists: {1}")
	@CsvSource({ "false, false, Copying, (create), Merging",
			"false, true, Merging, (source overwrites existing files), Copying",
			"true, false, DRY-RUN copy, (create), DRY-RUN merge",
			"true, true, DRY-RUN merge, (source overwrites existing files), DRY-RUN copy" })
	void reportsTheBasesTheDirectoriesAndWhatItDoesWithEach(boolean dryRun,
		boolean destinationExists, String action, String suffix, String otherAction,
		@TempDir Path tmp) throws Throwable
	{
		Path base = tmp.toRealPath();
		Path srcBase = base.resolve("srcBase");
		Path dstBase = base.resolve("dstBase");
		write(srcBase.resolve("scripts/sub/a.txt"), "A");
		if (destinationExists)
		{
			write(dstBase.resolve("scripts/keep.txt"), "K");
		}
		String[] args = dryRun
			? new String[] { "--src_base", srcBase.toString(), "--dst_base", dstBase.toString(),
					"--dirs", "scripts", "--dry-run" }
			: new String[] { "--src_base", srcBase.toString(), "--dst_base", dstBase.toString(),
					"--dirs", "scripts" };

		String report = stdoutOf(
			() -> MergeWithTemplate.run(args, config(base), base.resolve("work")));

		Path srcDir = srcBase.resolve("scripts");
		Path dstDir = dstBase.resolve("scripts");
		assertTrue(report.contains("Source base: " + srcBase + System.lineSeparator()), report);
		assertTrue(report.contains("Destination base: " + dstBase + System.lineSeparator()),
			report);
		assertTrue(report.contains(
			"Dirs to process: [scripts]" + (dryRun ? " (dry-run)" : "") + System.lineSeparator()),
			report);
		assertTrue(report.contains(action + " " + srcDir + " -> " + dstDir + " " + suffix), report);
		assertFalse(report.contains(otherAction + " "), report);
		String previewLine = srcDir.resolve("sub/a.txt") + "  ->  " + dstDir.resolve("sub/a.txt");
		assertEquals(dryRun, report.contains(previewLine), report);
		assertTrue(report.endsWith("Done." + System.lineSeparator()), report);
		assertEquals(!dryRun, Files.exists(dstDir.resolve("sub/a.txt")));
	}

	@ParameterizedTest(name = "the source entry is a file: {0}")
	@ValueSource(booleans = { false, true })
	void skipsASourceThatIsNotADirectory_andReportsIt_andProcessesTheNext(
		boolean sourceEntryIsAFile, @TempDir Path tmp) throws Throwable
	{
		Path srcBase = tmp.resolve("srcBase");
		Path dstBase = tmp.resolve("dstBase");
		write(srcBase.resolve("scripts/a.txt"), "A");
		if (sourceEntryIsAFile)
		{
			write(srcBase.resolve("docs"), "not a directory");
		}

		String report = stdoutOf(
			() -> MergeWithTemplate
				.run(
					new String[] { "--src_base", srcBase.toString(), "--dst_base",
							dstBase.toString(), "--dirs", "docs", "scripts" },
					config(tmp), tmp.resolve("work")));

		assertFalse(Files.exists(dstBase.resolve("docs")));
		assertEquals("A", read(dstBase.resolve("scripts/a.txt")));
		assertTrue(
			report.contains(
				"Not a directory (skip): " + srcBase.resolve("docs").toFile().getAbsolutePath()),
			report);
		assertFalse(
			report.contains(
				"Not a directory (skip): " + srcBase.resolve("scripts").toFile().getAbsolutePath()),
			report);
	}

	@Test
	void ignoresAConfigThatCannotBeRead_andReportsIt(@TempDir Path tmp) throws Throwable
	{
		Path srcBase = tmp.resolve("srcBase");
		Path dstBase = tmp.resolve("dstBase");
		write(srcBase.resolve("scripts/a.txt"), "A");
		write(srcBase.resolve("a.txt"), "A");
		Path configFile = tmp.resolve("config/merge_defaults.json");
		// the default_files entry in front of bytes that are no UTF-8
		Files.createDirectories(configFile.getParent());
		byte[] json = "{ \"default_files\": [\"a.txt\"] }".getBytes(StandardCharsets.UTF_8);
		byte[] content = new byte[json.length + 2];
		System.arraycopy(json, 0, content, 0, json.length);
		content[json.length] = (byte)0xFF;
		content[json.length + 1] = (byte)0xFE;
		Files.write(configFile, content);

		String report = stdoutOf(
			() -> MergeWithTemplate
				.run(
					new String[] { "--src_base", srcBase.toString(), "--dst_base",
							dstBase.toString(), "--dirs", "scripts" },
					configFile, tmp.resolve("work")));

		assertEquals("A", read(dstBase.resolve("scripts/a.txt")));
		assertFalse(Files.exists(dstBase.resolve("a.txt")), "no default from the unread config");
		assertTrue(report.contains("Could not load defaults from " + configFile + ": "), report);
	}

	@Test
	void reportsNothingAboutTheDefaults_whenTheConfigIsRead(@TempDir Path tmp) throws Throwable
	{
		Path srcBase = tmp.resolve("srcBase");
		Path dstBase = tmp.resolve("dstBase");
		write(srcBase.resolve("scripts/a.txt"), "A");

		String report = stdoutOf(
			() -> MergeWithTemplate
				.run(
					new String[] { "--src_base", srcBase.toString(), "--dst_base",
							dstBase.toString(), "--dirs", "scripts" },
					config(tmp), tmp.resolve("work")));

		assertFalse(report.contains("Could not load defaults"), report);
	}

	@Test
	void mergeKeepsTheModificationTimeOfTheSourceFile(@TempDir Path tmp) throws Exception
	{
		Path srcBase = tmp.resolve("srcBase");
		Path dstBase = tmp.resolve("dstBase");
		write(dstBase.resolve("scripts/keep.txt"), "K");
		Path srcFile = srcBase.resolve("scripts/x.txt");
		write(srcFile, "NEW");
		Files.setLastModifiedTime(srcFile, FileTime.fromMillis(2_000L));

		MergeWithTemplate.run(new String[] { "--src_base", srcBase.toString(), "--dst_base",
				dstBase.toString(), "--dirs", "scripts" },
			config(tmp), tmp.resolve("work"));

		assertEquals("NEW", read(dstBase.resolve("scripts/x.txt")));
		assertEquals(2_000L,
			Files.getLastModifiedTime(dstBase.resolve("scripts/x.txt")).toMillis());
		assertEquals("K", read(dstBase.resolve("scripts/keep.txt")));
	}

	@ParameterizedTest(name = "dry-run: {0}")
	@ValueSource(booleans = { false, true })
	void reportsThatTheSourceOverwritesExistingFiles_whenItMerges(boolean dryRun, @TempDir Path tmp)
		throws Throwable
	{
		Path base = tmp.toRealPath();
		Path srcBase = base.resolve("srcBase");
		Path dstBase = base.resolve("dstBase");
		Path srcFile = srcBase.resolve("scripts/a.txt");
		write(srcFile, "OLD SOURCE");
		Files.setLastModifiedTime(srcFile, FileTime.fromMillis(1_000L));
		Path dstFile = dstBase.resolve("scripts/a.txt");
		write(dstFile, "NEW DEST");
		Files.setLastModifiedTime(dstFile, FileTime.fromMillis(2_000_000L));
		String[] args = dryRun
			? new String[] { "--src_base", srcBase.toString(), "--dst_base", dstBase.toString(),
					"--dirs", "scripts", "--dry-run" }
			: new String[] { "--src_base", srcBase.toString(), "--dst_base", dstBase.toString(),
					"--dirs", "scripts" };

		String report = stdoutOf(
			() -> MergeWithTemplate.run(args, config(base), base.resolve("work")));

		String action = dryRun ? "DRY-RUN merge " : "Merging ";
		assertTrue(report.contains(action + srcBase.resolve("scripts") + " -> "
			+ dstBase.resolve("scripts") + " (source overwrites existing files)"), report);
		assertFalse(report.contains("latest-modified"), report);
		// the older source overwrites the newer destination file, unless it is a dry run
		assertEquals(dryRun ? "NEW DEST" : "OLD SOURCE", read(dstFile));
	}

	@ParameterizedTest(name = "link target: {0}, destination exists: {1}")
	@CsvSource({ "r.txt, false", "r.txt, true", "outside/deep/real.txt, false",
			"outside/deep/real.txt, true" })
	void dryRunPreviewsALinkToATargetOutsideTheSource_underTheNameOfTheLink(String target,
		boolean destinationExists, @TempDir Path tmp) throws Throwable
	{
		Path base = tmp.toRealPath();
		Path srcBase = base.resolve("srcBase");
		Path dstBase = base.resolve("dstBase");
		Path srcDir = srcBase.resolve("scripts");
		Path dstDir = dstBase.resolve("scripts");
		write(srcDir.resolve("plain.txt"), "P");
		Path targetFile = base.resolve(target);
		write(targetFile, "T");
		Path link = srcDir.resolve("link.txt");
		try
		{
			Files.createSymbolicLink(link, targetFile);
		}
		catch (IOException | UnsupportedOperationException exception)
		{
			assumeTrue(false, "symbolic links cannot be created here: " + exception);
		}
		if (destinationExists)
		{
			write(dstDir.resolve("keep.txt"), "K");
		}

		String report = stdoutOf(
			() -> MergeWithTemplate
				.run(
					new String[] { "--src_base", srcBase.toString(), "--dst_base",
							dstBase.toString(), "--dirs", "scripts", "--dry-run" },
					config(base), base.resolve("work")));

		assertTrue(
			report.contains(link + "  ->  " + dstDir.resolve("link.txt") + System.lineSeparator()),
			report);
		assertTrue(report.contains(srcDir.resolve("plain.txt") + "  ->  "
			+ dstDir.resolve("plain.txt") + System.lineSeparator()), report);
		assertEquals(2, report.split("  ->  ", -1).length - 1, report);
		assertFalse(Files.exists(dstDir.resolve("link.txt")));
		assertTrue(report.endsWith("Done." + System.lineSeparator()), report);
	}

	@ParameterizedTest(name = "destination exists: {0}")
	@ValueSource(booleans = { false, true })
	void keepsTheModificationTimeOfTheSourceFiles_whetherTheDestinationExistsOrNot(
		boolean destinationExists, @TempDir Path tmp) throws Exception
	{
		Path srcBase = tmp.resolve("srcBase");
		Path dstBase = tmp.resolve("dstBase");
		Path srcFile = srcBase.resolve("scripts/x.txt");
		write(srcFile, "X");
		Files.setLastModifiedTime(srcFile, FileTime.fromMillis(2_000L));
		Path srcNested = srcBase.resolve("scripts/sub/y.txt");
		write(srcNested, "Y");
		Files.setLastModifiedTime(srcNested, FileTime.fromMillis(3_000L));
		if (destinationExists)
		{
			write(dstBase.resolve("scripts/keep.txt"), "K");
		}

		MergeWithTemplate.run(new String[] { "--src_base", srcBase.toString(), "--dst_base",
				dstBase.toString(), "--dirs", "scripts" },
			config(tmp), tmp.resolve("work"));

		assertEquals("X", read(dstBase.resolve("scripts/x.txt")));
		assertEquals(2_000L,
			Files.getLastModifiedTime(dstBase.resolve("scripts/x.txt")).toMillis());
		assertEquals("Y", read(dstBase.resolve("scripts/sub/y.txt")));
		assertEquals(3_000L,
			Files.getLastModifiedTime(dstBase.resolve("scripts/sub/y.txt")).toMillis());
		assertEquals(destinationExists, Files.exists(dstBase.resolve("scripts/keep.txt")));
	}

	/**
	 * Test method for {@link MergeWithTemplate}
	 */
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(MergeWithTemplate.class);
	}
}
