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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * {@link MergeWithTemplate#main(String[])} and {@link MergeWithTemplate#run(String[])} with
 * arguments that name the source, the destination and the directories, so neither the defaults in
 * {@code ./config/merge_defaults.json} nor the working directory decide where anything goes. The
 * usage error and its exit code are tested through {@code commandLine} in
 * {@link MergeWithTemplateUsageTest}.
 * <p>
 * PIT leaves this class out ({@code excludedTestClasses} in gradle/mutation-testing.gradle): a
 * mutant that turns these arguments into a usage error would make {@code main} call
 * {@code System.exit}, which ends the PIT minion.
 */
class MergeWithTemplateMainTest
{

	@TempDir
	Path tmp;

	private String[] arguments(final Path src, final Path dst) throws IOException
	{
		Files.createDirectories(src.resolve("scripts"));
		Files.writeString(src.resolve("scripts/build.sh"), "echo build", StandardCharsets.UTF_8);
		return new String[] { "--src_base", src.toString(), "--dst_base", dst.toString(), "--dirs",
				"scripts" };
	}

	@Test
	void main_copiesTheNamedDirectories_whenTheArgumentsNameThem() throws Exception
	{
		Path dst = tmp.resolve("project");

		MergeWithTemplate.main(arguments(tmp.resolve("template"), dst));

		assertEquals("echo build",
			Files.readString(dst.resolve("scripts/build.sh"), StandardCharsets.UTF_8));
	}

	@Test
	void run_copiesTheNamedDirectories_whenTheArgumentsNameThem() throws IOException
	{
		Path dst = tmp.resolve("project");

		MergeWithTemplate.run(arguments(tmp.resolve("template"), dst));

		assertEquals("echo build",
			Files.readString(dst.resolve("scripts/build.sh"), StandardCharsets.UTF_8));
	}
}
