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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * A usage error of {@link MergeWithTemplate} reaches a caller in the same JVM as an exception, not
 * as the end of the process (#34): before, {@code main} called {@code System.exit(2)}, which in a
 * test JVM ends the run while Gradle reports it green. The command line keeps its exit code 2
 * through {@code main}, which hands on what {@code commandLine} returns.
 */
class MergeWithTemplateUsageTest
{

	@TempDir
	Path tmp;

	/** What {@code commandLine} returned and what it printed on standard error. */
	private record Outcome(int exitCode, String standardError) {
	}

	private static Outcome commandLine(final String[] args, final Path configFile,
		final Path workingDirectory) throws IOException
	{
		ByteArrayOutputStream standardError = new ByteArrayOutputStream();
		PrintStream original = System.err;
		System.setErr(new PrintStream(standardError, true, StandardCharsets.UTF_8));
		try
		{
			int exitCode = MergeWithTemplate.commandLine(args, configFile, workingDirectory);
			return new Outcome(exitCode, standardError.toString(StandardCharsets.UTF_8));
		}
		finally
		{
			System.setErr(original);
		}
	}

	@Test
	void commandLine_printsTheUsageErrorAndReturnsItsExitCode_whenNoDirectoryIsNamed()
		throws IOException
	{
		Path configFile = tmp.resolve("merge_defaults.json");
		Path workingDirectory = tmp.resolve("work");

		Outcome outcome = commandLine(new String[] { }, configFile, workingDirectory);

		assertEquals(MergeWithTemplate.USAGE_ERROR_EXIT_CODE, outcome.exitCode());
		assertEquals(
			"No directories specified. Use --dirs <dir1> <dir2> ... or set default_dirs in "
				+ configFile + System.lineSeparator(),
			outcome.standardError());
		assertFalse(Files.exists(workingDirectory));
	}

	@Test
	void commandLine_copiesAndReturnsZero_whenTheArgumentsNameADirectory() throws IOException
	{
		Path src = tmp.resolve("template");
		Path dst = tmp.resolve("project");
		Files.createDirectories(src.resolve("scripts"));
		Files.writeString(src.resolve("scripts/build.sh"), "echo build", StandardCharsets.UTF_8);

		Outcome outcome = commandLine(new String[] { "--src_base", src.toString(), "--dst_base",
				dst.toString(), "--dirs", "scripts" },
			tmp.resolve("no-config.json"), tmp.resolve("work"));

		assertEquals(0, outcome.exitCode());
		assertEquals("", outcome.standardError());
		assertEquals("echo build",
			Files.readString(dst.resolve("scripts/build.sh"), StandardCharsets.UTF_8));
	}

	@ParameterizedTest(name = "config: {0}")
	@CsvSource(nullValues = "none", delimiter = '|', value = { "none",
			"{ \"default_src_base\": \"src\", \"default_files\": [\"a.txt\"] }" })
	void run_throwsAUsageError_whenNeitherTheArgumentsNorTheConfigNameADirectory(
		final String config) throws IOException
	{
		Path configFile = tmp.resolve("merge_defaults.json");
		if (config != null)
		{
			Files.writeString(configFile, config, StandardCharsets.UTF_8);
		}

		MergeWithTemplate.UsageException usageError = assertThrows(
			MergeWithTemplate.UsageException.class,
			() -> MergeWithTemplate.run(new String[] { }, configFile));

		assertTrue(usageError.getMessage().startsWith("No directories specified."),
			usageError.getMessage());
		assertTrue(usageError.getMessage().endsWith(configFile.toString()),
			usageError.getMessage());
		assertEquals(2, MergeWithTemplate.USAGE_ERROR_EXIT_CODE);
	}

	@Test
	void run_copiesTheDirectoriesTheConfigNames_whenNoArgumentIsGiven() throws IOException
	{
		Path src = tmp.resolve("template");
		Path dst = tmp.resolve("project");
		Files.createDirectories(src.resolve("scripts"));
		Files.writeString(src.resolve("scripts/build.sh"), "echo build", StandardCharsets.UTF_8);
		Path configFile = tmp.resolve("merge_defaults.json");
		Files.writeString(configFile, "{ \"default_src_base\": \"" + src
			+ "\", \"default_dst_base\": \"" + dst + "\", \"default_dirs\": [\"scripts\"] }",
			StandardCharsets.UTF_8);

		MergeWithTemplate.run(new String[] { }, configFile);

		assertEquals("echo build",
			Files.readString(dst.resolve("scripts/build.sh"), StandardCharsets.UTF_8));
	}
}
