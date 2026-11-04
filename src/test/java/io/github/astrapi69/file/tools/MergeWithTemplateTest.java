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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.FileTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

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
}
