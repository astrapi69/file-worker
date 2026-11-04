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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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

	/**
	 * Test method for
	 * {@link CopyFileExtensions#copyFile(File, File, java.nio.charset.Charset, java.nio.charset.Charset, boolean)}
	 * (#56)
	 *
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	@Test
	public void copyFile_namesTheSourceInTheMessage_whenTheSourceIsADirectory() throws IOException
	{
		final File source = Files.createDirectories(tempDir.resolve("source-directory")).toFile();
		final File destination = tempDir.resolve("target-name.txt").toFile();

		final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
			() -> CopyFileExtensions.copyFile(source, destination));

		assertTrue(exception.getMessage().contains("source-directory"), exception.getMessage());
		assertFalse(exception.getMessage().contains("target-name.txt"), exception.getMessage());
	}

	/**
	 * A copy onto the file itself is refused and leaves the file as it was (#73): it used to open
	 * the destination for writing first, which emptied the only copy. A hard link has a path of its
	 * own and is still the same file.
	 */
	@ParameterizedTest(name = "{0}, charset {1}")
	@CsvSource(nullValues = "none", value = { "the same File, none", "the same File, UTF-8",
			"another path to the same file, none", "another path to the same file, UTF-8",
			"a hard link to the file, none", "a hard link to the file, UTF-8" })
	void copyFile_refusesAndKeepsTheContent_whenSourceAndDestinationAreTheSameFile(
		final String destinationKind, final String charsetName) throws IOException
	{
		final File source = newFile("dir/same.txt", "abc").toFile();
		final File destination = switch (destinationKind)
		{
			case "the same File" -> source;
			case "another path to the same file" -> tempDir.resolve("dir").resolve(".")
				.resolve("same.txt").toFile();
			default -> Files.createLink(tempDir.resolve("dir").resolve("link.txt"), source.toPath())
				.toFile();
		};
		final Charset charset = charsetName == null ? null : Charset.forName(charsetName);

		final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
			() -> CopyFileExtensions.copyFile(source, destination, charset, charset, false));

		assertTrue(exception.getMessage().contains("same.txt"), exception.getMessage());
		assertEquals("abc", Files.readString(source.toPath(), StandardCharsets.US_ASCII));
	}

	/**
	 * A source that does not exist is the same file as a destination with the same absolute
	 * normalized path (#73): the copy is refused before anything is opened, and no destination is
	 * created
	 */
	@ParameterizedTest(name = "{0}")
	@ValueSource(strings = { "the same File", "another path to the same file" })
	void copyFile_refuses_whenAMissingSourceIsCopiedOntoItsOwnPath(final String destinationKind)
	{
		final File source = tempDir.resolve("dir").resolve("missing.txt").toFile();
		final File destination = destinationKind.equals("the same File")
			? source
			: tempDir.resolve("dir").resolve("..").resolve("dir").resolve("missing.txt").toFile();

		final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
			() -> CopyFileExtensions.copyFile(source, destination, false));

		assertTrue(exception.getMessage().contains("same file"), exception.getMessage());
		assertFalse(source.exists());
	}

	/**
	 * A source that does not exist and a destination with another path are two files: the copy
	 * fails on reading the source and creates no destination
	 */
	@ParameterizedTest(name = "charset {0}")
	@CsvSource(nullValues = "none", value = { "none", "UTF-8" })
	void copyFile_throwsFileNotFound_whenTheSourceIsMissingAndTheDestinationIsAnotherPath(
		final String charsetName)
	{
		final File source = tempDir.resolve("dir").resolve("missing.txt").toFile();
		final File destination = tempDir.resolve("other.txt").toFile();
		final Charset charset = charsetName == null ? null : Charset.forName(charsetName);

		assertThrows(FileNotFoundException.class,
			() -> CopyFileExtensions.copyFile(source, destination, charset, charset, false));

		assertFalse(destination.exists());
	}
}
