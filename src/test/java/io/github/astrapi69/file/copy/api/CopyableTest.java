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
package io.github.astrapi69.file.copy.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * The unit test class for the default methods of the {@link Copyable} interface
 */
public class CopyableTest
{

	/** An implementation that uses every default method as it is */
	private final Copyable copyable = new Copyable()
	{
	};

	@TempDir
	Path tempDir;

	private Path newFile(final String relativePath, final String content) throws IOException
	{
		final Path path = tempDir.resolve(relativePath).toAbsolutePath();
		Files.createDirectories(path.getParent());
		Files.write(path, content.getBytes(StandardCharsets.US_ASCII));
		return path;
	}

	private Path notCreated(final String relativePath)
	{
		return tempDir.resolve(relativePath).toAbsolutePath();
	}

	private static String read(final Path path) throws IOException
	{
		return new String(Files.readAllBytes(path), StandardCharsets.US_ASCII);
	}

	/**
	 * Test method for {@link Copyable#copyDirectory(Path, Path)}
	 */
	@Test
	public void copyDirectoryPath_copiesEveryFileOfTheTree() throws IOException
	{
		newFile("source/a.txt", "alpha");
		newFile("source/sub/b.txt", "beta");
		final Path destination = notCreated("destination");

		assertTrue(copyable.copyDirectory(tempDir.resolve("source"), destination));

		assertEquals("alpha", read(destination.resolve("a.txt")));
		assertEquals("beta", read(destination.resolve("sub/b.txt")));
	}

	/**
	 * Test method for {@link Copyable#copyDirectory(File, File)}
	 */
	@Test
	public void copyDirectory_copiesEveryFileOfTheTree() throws Exception
	{
		newFile("source/a.txt", "alpha");
		newFile("source/sub/b.txt", "beta");
		final Path destination = notCreated("destination");

		assertTrue(
			copyable.copyDirectory(tempDir.resolve("source").toFile(), destination.toFile()));

		assertEquals("alpha", read(destination.resolve("a.txt")));
		assertEquals("beta", read(destination.resolve("sub/b.txt")));
	}

	/**
	 * Test method for {@link Copyable#copyDirectory(File, File)}: a regular file as the destination
	 * is no copy of a directory, so the copy reports false
	 */
	@Test
	public void copyDirectory_returnsFalse_whenTheDestinationIsARegularFile() throws Exception
	{
		final File source = Files.createDirectories(tempDir.resolve("source")).toFile();
		final Path destination = newFile("destination.txt", "alpha");

		assertFalse(copyable.copyDirectory(source, destination.toFile()));

		assertEquals("alpha", read(destination));
	}

	/**
	 * Test method for {@link Copyable#copyFile(File, File)}
	 */
	@Test
	public void copyFile_writesTheSourceContentToTheDestinationFile() throws IOException
	{
		final Path source = newFile("source.txt", "alpha");
		final Path destination = notCreated("destination.txt");

		assertTrue(copyable.copyFile(source.toFile(), destination.toFile()));

		assertEquals("alpha", read(destination));
	}

	/**
	 * Test method for {@link Copyable#copyFile(Path, Path)}
	 */
	@Test
	public void copyFilePath_writesTheSourceContentToTheDestinationFile() throws IOException
	{
		final Path source = newFile("source.txt", "alpha");
		final Path destination = notCreated("destination.txt");

		assertTrue(copyable.copyFile(source, destination));

		assertEquals("alpha", read(destination));
	}

	/**
	 * Test method for {@link Copyable#copyFiles(Collection, File, Charset, Charset, boolean)}
	 */
	@Test
	public void copyFiles_copiesEveryFileIntoTheDestinationDirectory() throws IOException
	{
		final File first = newFile("in/first.txt", "alpha").toFile();
		final File second = newFile("in/second.txt", "beta").toFile();
		final Path destination = notCreated("out");

		copyable.copyFiles(List.of(first, second), destination.toFile(), StandardCharsets.US_ASCII,
			StandardCharsets.US_ASCII, false);

		assertEquals("alpha", read(destination.resolve("first.txt")));
		assertEquals("beta", read(destination.resolve("second.txt")));
	}

	/**
	 * Test method for {@link Copyable#copyFiles(Collection, Path)}
	 */
	@Test
	public void copyFilesPath_copiesEveryFileIntoTheDestinationDirectory() throws IOException
	{
		final Path first = newFile("in/first.txt", "alpha");
		final Path second = newFile("in/second.txt", "beta");
		final Path destination = notCreated("out");

		copyable.copyFiles(List.of(first, second), destination);

		assertEquals("alpha", read(destination.resolve("first.txt")));
		assertEquals("beta", read(destination.resolve("second.txt")));
	}

	/**
	 * Test method for {@link Copyable#newBackupOf(File, Charset, Charset)}
	 */
	@Test
	public void newBackupOf_writesACopyWithTheBakExtensionNextToTheFile() throws IOException
	{
		final File file = newFile("data/notes.txt", "alpha").toFile();

		final File backup = copyable.newBackupOf(file, StandardCharsets.US_ASCII,
			StandardCharsets.US_ASCII);

		assertEquals(new File(file.getParentFile(), "notes.txt.bak"), backup);
		assertEquals("alpha", read(backup.toPath()));
	}

	/**
	 * Test method for {@link Copyable#newBackupOf(Path)}
	 */
	@Test
	public void newBackupOfPath_writesACopyWithTheBakExtensionNextToTheFile() throws IOException
	{
		final Path file = newFile("data/notes.txt", "alpha");

		final Path backup = copyable.newBackupOf(file);

		assertEquals(file.resolveSibling("notes.txt.bak"), backup);
		assertEquals("alpha", read(backup));
	}

}
