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
package io.github.astrapi69.file.copy;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Regression tests for the File-based methods of {@link CopyFileExtensions}
 */
public class CopyFileExtensionsRegressionTest
{

	@TempDir
	Path tempDir;

	private Path newFile(final String relativePath, final String content) throws IOException
	{
		final Path path = tempDir.resolve(relativePath);
		Files.createDirectories(path.getParent());
		return Files.write(path, content.getBytes(StandardCharsets.US_ASCII));
	}

	/**
	 * Test method for
	 * {@link CopyFileExtensions#copyFiles(java.util.Collection, File, java.nio.charset.Charset, java.nio.charset.Charset, boolean)}
	 * (#55)
	 *
	 * @param destinationPath
	 *            the destination, relative to the temporary directory, whose parent is missing
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	@ParameterizedTest(name = "{0}")
	@ValueSource(strings = { "out/nested", "out/deeper/nested" })
	public void copyFiles_createsTheMissingParentsOfTheDestination(final String destinationPath)
		throws IOException
	{
		final File source = newFile("in/source.txt", "alpha").toFile();
		final Path destination = tempDir.resolve(destinationPath);

		CopyFileExtensions.copyFiles(List.of(source), destination.toFile(), null, null, false);

		assertArrayEquals("alpha".getBytes(StandardCharsets.US_ASCII),
			Files.readAllBytes(destination.resolve("source.txt")));
	}
}
