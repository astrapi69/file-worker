package io.github.astrapi69.file.tools;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.FileTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Uses JUnit 5 (`@Test`, `@TempDir`). Invokes `MergeWithTemplate.main(...)` with explicit
 * `--src_base`, `--dst_base`, `--dirs` (so it does **not** depend on your JSON defaults). Verifies:
 * copy when destination subdir is missing, merge into existing directory (overwrites), dry-run
 * doesn’t change filesystem, multiple directories are handled.
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

	@Test
	void copiesWhenDestinationSubdirDoesNotExist(@TempDir Path tmp) throws Exception
	{
		Path srcBase = tmp.resolve("srcBase");
		Path dstBase = tmp.resolve("dstBase");
		Path srcScripts = srcBase.resolve("scripts");
		Path srcFile = srcScripts.resolve("a.txt");

		write(srcFile, "A");
		assertFalse(Files.exists(dstBase.resolve("scripts")));

		MergeWithTemplate.main(new String[] { "--src_base", srcBase.toString(), "--dst_base",
				dstBase.toString(), "--dirs", "scripts" });

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
		MergeWithTemplate.main(new String[] { "--src_base", srcBase.toString(), "--dst_base",
				dstBase.toString(), "--dirs", "scripts" });

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

		MergeWithTemplate.main(new String[] { "--src_base", srcBase.toString(), "--dst_base",
				dstBase.toString(), "--dirs", "scripts", "--dry-run" });

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

		MergeWithTemplate.main(new String[] { "--src_base", srcBase.toString(), "--dst_base",
				dstBase.toString(), "--dirs", "scripts", "tests" });

		assertTrue(Files.exists(dstBase.resolve("scripts/tool.sh")));
		assertTrue(Files.exists(dstBase.resolve("tests/test.txt")));
	}
}
