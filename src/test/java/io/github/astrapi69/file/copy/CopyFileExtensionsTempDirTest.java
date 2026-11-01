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
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import io.github.astrapi69.file.exception.FileIsNotADirectoryException;

/**
 * Tests for {@link CopyFileExtensions} that build every file they copy in a temporary directory of
 * their own
 */
public class CopyFileExtensionsTempDirTest
{

	/** A fixed modification time in whole seconds, long before any test runs */
	private static final long LAST_MODIFIED = 1_600_000_000_000L;

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

	private static String textOfLength(final int length)
	{
		return "line of text\n".repeat(length / 13 + 1).substring(0, length);
	}

	/**
	 * Test method for {@link CopyFileExtensions#copyFile(Path, Path)}
	 */
	@Test
	public void copyFilePath_writesTheSourceContentToANewDestination() throws IOException
	{
		final Path source = newFile("source.txt", "alpha");
		final Path destination = notCreated("destination.txt");

		assertTrue(CopyFileExtensions.copyFile(source, destination));

		assertEquals("alpha", read(destination));
	}

	/**
	 * Test method for {@link CopyFileExtensions#copyFile(Path, Path)}
	 */
	@Test
	public void copyFilePath_replacesTheContentOfAnExistingDestination() throws IOException
	{
		final Path source = newFile("source.txt", "alpha");
		final Path destination = newFile("destination.txt", "a much longer existing content");

		assertTrue(CopyFileExtensions.copyFile(source, destination));

		assertEquals("alpha", read(destination));
	}

	/**
	 * Test method for {@link CopyFileExtensions#copyFile(Path, Path)}
	 */
	@Test
	public void copyFilePath_throwsIllegalArgumentException_whenTheSourceIsADirectory()
		throws IOException
	{
		final Path source = Files.createDirectories(tempDir.resolve("directory"));
		final Path destination = notCreated("destination.txt");

		assertThrows(IllegalArgumentException.class,
			() -> CopyFileExtensions.copyFile(source, destination));
	}

	/**
	 * Test method for {@link CopyFileExtensions#copyFile(Path, Path)}
	 */
	@Test
	public void copyFilePath_throwsNullPointerExceptionNamingTheArgument_whenAnArgumentIsNull()
		throws IOException
	{
		final Path file = newFile("source.txt", "alpha");

		final NullPointerException noSource = assertThrows(NullPointerException.class,
			() -> CopyFileExtensions.copyFile(null, file));
		final NullPointerException noDestination = assertThrows(NullPointerException.class,
			() -> CopyFileExtensions.copyFile(file, (Path)null));

		assertEquals("source must not be null", noSource.getMessage());
		assertEquals("destination must not be null", noDestination.getMessage());
	}

	/**
	 * Test method for {@link CopyFileExtensions#copyFiles(java.util.Collection, Path)}
	 */
	@Test
	public void copyFilesPath_createsTheDestinationDirectoryAndCopiesEveryFile() throws IOException
	{
		final Path first = newFile("in/first.txt", "alpha");
		final Path second = newFile("in/second.txt", "beta");
		final Path destination = notCreated("out/nested");

		CopyFileExtensions.copyFiles(List.of(first, second), destination);

		assertEquals("alpha", read(destination.resolve("first.txt")));
		assertEquals("beta", read(destination.resolve("second.txt")));
	}

	/**
	 * Test method for {@link CopyFileExtensions#copyFiles(java.util.Collection, Path)}
	 */
	@Test
	public void copyFilesPath_copiesIntoAnExistingDirectory() throws IOException
	{
		final Path first = newFile("in/first.txt", "alpha");
		final Path destination = Files.createDirectories(tempDir.resolve("out"));
		newFile("out/kept.txt", "kept");

		CopyFileExtensions.copyFiles(List.of(first), destination);

		assertEquals("alpha", read(destination.resolve("first.txt")));
		assertEquals("kept", read(destination.resolve("kept.txt")));
	}

	/**
	 * Test method for {@link CopyFileExtensions#newBackupOf(Path)}
	 */
	@Test
	public void newBackupOfPath_writesACopyWithTheBakExtensionNextToTheFile() throws IOException
	{
		final Path file = newFile("data/notes.txt", "alpha");

		final Path backup = CopyFileExtensions.newBackupOf(file);

		assertEquals(file.resolveSibling("notes.txt.bak"), backup);
		assertEquals("alpha", read(backup));
		assertEquals("alpha", read(file));
	}

	/**
	 * Test method for {@link CopyFileExtensions#copyFile(File, File)}
	 *
	 * @param length
	 *            the number of characters of the copied text, around the block size the copy reads
	 *            in
	 */
	@ParameterizedTest(name = "{0} characters")
	@ValueSource(ints = { 0, 1, 8191, 8192, 8193, 3 * 8192 + 7 })
	public void copyFile_copiesTextOfEveryLength(final int length) throws IOException
	{
		final String text = textOfLength(length);
		final Path source = newFile("source.txt", text);
		final File destination = notCreated("destination.txt").toFile();

		assertTrue(CopyFileExtensions.copyFile(source.toFile(), destination));

		assertEquals(text, read(destination.toPath()));
	}

	/**
	 * Test method for {@link CopyFileExtensions#copyFile(File, File, boolean)}
	 */
	@Test
	public void copyFile_replacesTheContentOfALongerExistingDestination() throws IOException
	{
		final Path source = newFile("source.txt", "alpha");
		final Path destination = newFile("destination.txt", "a much longer existing content");

		assertTrue(CopyFileExtensions.copyFile(source.toFile(), destination.toFile(), false));

		assertEquals("alpha", read(destination));
	}

	/**
	 * Test method for {@link CopyFileExtensions#copyFile(File, File, boolean)}
	 */
	@Test
	public void copyFile_givesTheDestinationTheModificationTimeOfTheSource_whenAsked()
		throws IOException
	{
		final File source = newFile("source.txt", "alpha").toFile();
		assertTrue(source.setLastModified(LAST_MODIFIED));
		final File destination = notCreated("destination.txt").toFile();

		CopyFileExtensions.copyFile(source, destination, true);

		assertEquals(LAST_MODIFIED, destination.lastModified());
	}

	/**
	 * Test method for {@link CopyFileExtensions#copyFile(File, File, boolean)}
	 */
	@Test
	public void copyFile_leavesTheDestinationItsOwnModificationTime_whenNotAsked()
		throws IOException
	{
		final File source = newFile("source.txt", "alpha").toFile();
		assertTrue(source.setLastModified(LAST_MODIFIED));
		final File destination = notCreated("destination.txt").toFile();

		CopyFileExtensions.copyFile(source, destination, false);

		assertEquals("alpha", read(destination.toPath()));
		assertNotEquals(LAST_MODIFIED, destination.lastModified());
	}

	/**
	 * Test method for
	 * {@link CopyFileExtensions#copyFile(File, File, java.nio.charset.Charset, java.nio.charset.Charset, boolean)}
	 */
	@Test
	public void copyFile_writesTheTextInTheDestinationEncoding_whenBothEncodingsAreGiven()
		throws IOException
	{
		final String text = "Grüße aus Köln";
		final Path source = tempDir.resolve("latin1.txt");
		Files.write(source, text.getBytes(StandardCharsets.ISO_8859_1));
		final File destination = notCreated("utf8.txt").toFile();

		CopyFileExtensions.copyFile(source.toFile(), destination, StandardCharsets.ISO_8859_1,
			StandardCharsets.UTF_8, false);

		assertArrayEquals(text.getBytes(StandardCharsets.UTF_8),
			Files.readAllBytes(destination.toPath()));
	}

	/**
	 * Test method for
	 * {@link CopyFileExtensions#copyFile(File, File, java.nio.charset.Charset, java.nio.charset.Charset, boolean)}
	 */
	@Test
	public void copyFile_throwsIllegalArgumentException_whenTheSourceIsADirectory()
		throws IOException
	{
		final File source = Files.createDirectories(tempDir.resolve("directory")).toFile();
		final File destination = notCreated("destination.txt").toFile();

		assertThrows(IllegalArgumentException.class,
			() -> CopyFileExtensions.copyFile(source, destination));
	}

	/**
	 * Test method for
	 * {@link CopyFileExtensions#copyFile(File, File, java.nio.charset.Charset, java.nio.charset.Charset, boolean)}
	 */
	@Test
	public void copyFile_throwsIllegalArgumentExceptionNamingTheDestination_whenTheDestinationIsADirectory()
		throws IOException
	{
		final File source = newFile("source.txt", "alpha").toFile();
		final File destination = Files.createDirectories(tempDir.resolve("target-directory"))
			.toFile();

		final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
			() -> CopyFileExtensions.copyFile(source, destination));

		assertTrue(exception.getMessage().contains("target-directory"), exception.getMessage());
	}

	/**
	 * Test method for
	 * {@link CopyFileExtensions#copyFiles(java.util.Collection, File, java.nio.charset.Charset, java.nio.charset.Charset, boolean)}
	 */
	@Test
	public void copyFiles_createsTheDestinationDirectoryAndCopiesEveryFile() throws IOException
	{
		final File first = newFile("in/first.txt", "alpha").toFile();
		final File second = newFile("in/second.txt", "beta").toFile();
		assertTrue(first.setLastModified(LAST_MODIFIED));
		final Path destination = notCreated("out");

		CopyFileExtensions.copyFiles(List.of(first, second), destination.toFile(), null, null,
			true);

		assertEquals("alpha", read(destination.resolve("first.txt")));
		assertEquals("beta", read(destination.resolve("second.txt")));
		assertEquals(LAST_MODIFIED, destination.resolve("first.txt").toFile().lastModified());
	}

	/**
	 * Test method for
	 * {@link CopyFileExtensions#copyFiles(java.util.Collection, File, java.nio.charset.Charset, java.nio.charset.Charset, boolean)}
	 */
	@Test
	public void copyFiles_copiesIntoAnExistingDirectory() throws IOException
	{
		final File first = newFile("in/first.txt", "alpha").toFile();
		final Path destination = Files.createDirectories(tempDir.resolve("out"));
		newFile("out/kept.txt", "kept");

		CopyFileExtensions.copyFiles(List.of(first), destination.toFile(),
			StandardCharsets.US_ASCII, StandardCharsets.US_ASCII, false);

		assertEquals("alpha", read(destination.resolve("first.txt")));
		assertEquals("kept", read(destination.resolve("kept.txt")));
	}

	/**
	 * Test method for
	 * {@link CopyFileExtensions#copyFiles(java.util.Collection, File, java.nio.charset.Charset, java.nio.charset.Charset, boolean)}
	 */
	@Test
	public void copyFiles_throwsRuntimeExceptionCausedByFileNotFoundException_whenASourceIsMissing()
	{
		final File missing = notCreated("in/missing.txt").toFile();
		final File destination = notCreated("out").toFile();

		final RuntimeException exception = assertThrows(RuntimeException.class,
			() -> CopyFileExtensions.copyFiles(List.of(missing), destination, null, null, false));

		assertInstanceOf(FileNotFoundException.class, exception.getCause());
	}

	/**
	 * Test method for {@link CopyFileExtensions#copyFileToDirectory(File, File, boolean)}
	 */
	@Test
	public void copyFileToDirectory_throwsIllegalArgumentException_whenTheDirectoryIsNull()
		throws IOException
	{
		final File source = newFile("source.txt", "alpha").toFile();

		assertThrows(IllegalArgumentException.class,
			() -> CopyFileExtensions.copyFileToDirectory(source, null));
	}

	/**
	 * Test method for {@link CopyFileExtensions#copyFileToDirectory(File, File, boolean)}
	 */
	@Test
	public void copyFileToDirectory_throwsFileIsNotADirectoryException_whenTheDestinationIsAFile()
		throws IOException
	{
		final File source = newFile("source.txt", "alpha").toFile();
		final File file = newFile("plain-file.txt", "beta").toFile();

		assertThrows(FileIsNotADirectoryException.class,
			() -> CopyFileExtensions.copyFileToDirectory(source, file, false));
		assertEquals("beta", read(file.toPath()));
	}

	/**
	 * Test method for {@link CopyFileExtensions#copyFileToDirectory(File, File)}
	 */
	@Test
	public void copyFileToDirectory_copiesTheFileUnderItsOwnNameIntoTheDirectory()
		throws IOException, FileIsNotADirectoryException
	{
		final File source = newFile("in/source.txt", "alpha").toFile();
		final Path directory = Files.createDirectories(tempDir.resolve("out"));

		assertTrue(CopyFileExtensions.copyFileToDirectory(source, directory.toFile()));

		assertEquals("alpha", read(directory.resolve("source.txt")));
	}

	/**
	 * Test method for
	 * {@link CopyFileExtensions#newBackupOf(File, java.nio.charset.Charset, java.nio.charset.Charset)}
	 */
	@Test
	public void newBackupOf_writesACopyWithTheBakExtensionNextToTheFile() throws IOException
	{
		final File file = newFile("data/notes.txt", "alpha").toFile();

		final File backup = CopyFileExtensions.newBackupOf(file, StandardCharsets.US_ASCII, null);

		assertEquals(new File(file.getParentFile(), "notes.txt.bak"), backup);
		assertEquals("alpha", read(backup.toPath()));
	}

}
