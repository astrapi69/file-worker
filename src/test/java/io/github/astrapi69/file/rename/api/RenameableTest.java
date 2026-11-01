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
package io.github.astrapi69.file.rename.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.argumentSet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import io.github.astrapi69.file.exception.FileDoesNotExistException;
import io.github.astrapi69.file.exception.FileIsADirectoryException;
import io.github.astrapi69.file.rename.RenameRefusingFile;

/**
 * The unit test class for the default methods of the interface {@link Renameable}
 * <p>
 * Every file lies below a {@link TempDir}, and every file whose suffix is changed has a dot in its
 * own name: the target of a suffix change is built from the last dot in the absolute path, which is
 * wrong for a file without one below a directory with one (file-worker#37)
 */
public class RenameableTest
{

	private final Renameable renameable = new Renameable()
	{
	};

	@TempDir
	Path tempDir;

	static Stream<Arguments> fileNamesAndTimesWithTheirNewNames()
	{
		return Stream.of(
			argumentSet("a name with one extension", "report.txt",
				LocalTime.of(6, 34, 59, 123_000_000), "report_063459123.txt"),
			argumentSet("a name with two extensions", "archive.tar.gz",
				LocalTime.of(6, 34, 59, 123_000_000), "archive.tar_063459123.gz"),
			argumentSet("a name without an extension", "README", LocalTime.of(6, 34, 59),
				"README_063459000"),
			argumentSet("the first millisecond of the day", "report.txt", LocalTime.MIDNIGHT,
				"report_000000000.txt"),
			argumentSet("the last millisecond of the day", "report.txt",
				LocalTime.of(23, 59, 59, 999_000_000), "report_235959999.txt"));
	}

	private File newFile(String name, String content) throws IOException
	{
		File file = tempDir.resolve(name).toFile();
		Files.writeString(file.toPath(), content);
		return file;
	}

	/**
	 * Test method for {@link Renameable#appendSystemTimeToFilename(File, Date)}
	 *
	 * @param fileName
	 *            the name of the file
	 * @param time
	 *            the time of day to append
	 * @param expectedName
	 *            the name with the time appended
	 */
	@ParameterizedTest
	@MethodSource("fileNamesAndTimesWithTheirNewNames")
	public void appendSystemTimeToFilename_putsTheGivenTimeBeforeTheLastExtension(String fileName,
		LocalTime time, String expectedName)
	{
		Date date = Date.from(
			LocalDate.of(2007, 1, 17).atTime(time).atZone(ZoneId.systemDefault()).toInstant());

		String actual = renameable.appendSystemTimeToFilename(new File(tempDir.toFile(), fileName),
			date);

		assertEquals(expectedName, actual);
	}

	/**
	 * Test method for {@link Renameable#appendSystemTimeToFilename(File)}
	 */
	@Test
	public void appendSystemTimeToFilename_putsTheCurrentTimeBeforeTheExtension()
	{
		String actual = renameable
			.appendSystemTimeToFilename(new File(tempDir.toFile(), "report.txt"));

		assertTrue(actual.matches("report_\\d{9}\\.txt"), actual);
	}

	/**
	 * Test method for {@link Renameable#renameFileWithSystemtime(File)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws FileIsADirectoryException
	 *             the file is a directory exception
	 */
	@Test
	public void renameFileWithSystemtime_movesTheFileToItsNameWithTheTimeInTheSameDirectory()
		throws IOException, FileIsADirectoryException
	{
		File file = newFile("report.txt", "the report");

		File renamed = renameable.renameFileWithSystemtime(file);

		assertEquals(tempDir.toFile(), renamed.getParentFile());
		assertTrue(renamed.getName().matches("report_\\d{9}\\.txt"), renamed.getName());
		assertEquals("the report", Files.readString(renamed.toPath()));
		assertFalse(file.exists());
	}

	/**
	 * Test method for {@link Renameable#renameFileWithSystemtime(File)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws FileIsADirectoryException
	 *             the file is a directory exception
	 */
	@Test
	public void renameFileWithSystemtime_appendsTheTime_whenTheNameHasNoExtension()
		throws IOException, FileIsADirectoryException
	{
		File file = newFile("README", "read me");

		File renamed = renameable.renameFileWithSystemtime(file);

		assertEquals(tempDir.toFile(), renamed.getParentFile());
		assertTrue(renamed.getName().matches("README_\\d{9}"), renamed.getName());
		assertEquals("read me", Files.readString(renamed.toPath()));
		assertFalse(file.exists());
	}

	/**
	 * Test method for {@link Renameable#changeFilenameSuffix(File, String)}
	 *
	 * @throws Exception
	 *             is thrown if an exception occurs
	 */
	@Test
	public void changeFilenameSuffix_renamesTheFileToTheNewSuffix() throws Exception
	{
		File file = newFile("notes.txt", "my notes");
		File expected = tempDir.resolve("notes.rtf").toFile();

		boolean renamed = renameable.changeFilenameSuffix(file, ".rtf");

		assertTrue(renamed);
		assertEquals("my notes", Files.readString(expected.toPath()));
		assertFalse(file.exists());
	}

	/**
	 * Test method for {@link Renameable#changeFilenameSuffix(File, String, boolean)}
	 *
	 * @param delete
	 *            the flag whether a refused rename falls back to copy and delete
	 * @throws Exception
	 *             is thrown if an exception occurs
	 */
	@ParameterizedTest(name = "delete = {0}")
	@ValueSource(booleans = { true, false })
	public void changeFilenameSuffix_renamesTheFileToTheNewSuffix_withEitherDeleteFlag(
		boolean delete) throws Exception
	{
		File file = newFile("notes.txt", "my notes");
		File expected = tempDir.resolve("notes.md").toFile();

		boolean renamed = renameable.changeFilenameSuffix(file, ".md", delete);

		assertTrue(renamed);
		assertEquals("my notes", Files.readString(expected.toPath()));
		assertFalse(file.exists());
	}

	/**
	 * Test method for {@link Renameable#changeFilenameSuffix(File, String, boolean)}
	 */
	@Test
	public void changeFilenameSuffix_throwsFileDoesNotExistException_whenTheFileIsMissing()
	{
		File missing = tempDir.resolve("missing.txt").toFile();

		FileDoesNotExistException exception = assertThrows(FileDoesNotExistException.class,
			() -> renameable.changeFilenameSuffix(missing, ".rtf", true));

		assertEquals("The file " + missing + " does not exist.", exception.getMessage());
	}

	/**
	 * Test method for {@link Renameable#changeAllFilenameSuffix(File, String, String, boolean)}
	 *
	 * @throws Exception
	 *             is thrown if an exception occurs
	 */
	@Test
	public void changeAllFilenameSuffix_returnsTheFilesWhoseRenameWasRefused() throws Exception
	{
		File renamable = newFile("one.txt", "first");
		File blockedFirst = newFile("two.txt", "second");
		File blockedSecond = newFile("three.txt", "third");
		Files.createDirectory(tempDir.resolve("two.rtf"));
		Files.createDirectory(tempDir.resolve("three.rtf"));

		List<File> notRenamed = renameable.changeAllFilenameSuffix(tempDir.toFile(), ".txt", ".rtf",
			false);

		assertEquals(Set.of(blockedFirst, blockedSecond), Set.copyOf(notRenamed));
		assertEquals(2, notRenamed.size());
		assertEquals("first", Files.readString(tempDir.resolve("one.rtf")));
		assertFalse(renamable.exists());
		assertEquals("second", Files.readString(blockedFirst.toPath()));
		assertEquals("third", Files.readString(blockedSecond.toPath()));
	}

	/**
	 * Test method for {@link Renameable#renameFile(File, File)}
	 *
	 * @throws Exception
	 *             is thrown if an exception occurs
	 */
	@Test
	public void renameFile_movesTheFileToTheNewFile() throws Exception
	{
		File file = newFile("old.txt", "content");
		File newFile = tempDir.resolve("new.txt").toFile();

		boolean renamed = renameable.renameFile(file, newFile);

		assertTrue(renamed);
		assertEquals("content", Files.readString(newFile.toPath()));
		assertFalse(file.exists());
	}

	/**
	 * Test method for {@link Renameable#renameFile(File, File)}
	 *
	 * @throws Exception
	 *             is thrown if an exception occurs
	 */
	@Test
	public void renameFile_returnsFalseAndKeepsTheFile_whenTheRenameIsRefused() throws Exception
	{
		File file = newFile("old.txt", "content");
		File existingDirectory = Files.createDirectory(tempDir.resolve("taken.txt")).toFile();

		boolean renamed = renameable.renameFile(file, existingDirectory);

		assertFalse(renamed);
		assertEquals("content", Files.readString(file.toPath()));
		assertTrue(existingDirectory.isDirectory());
	}

	/**
	 * Test method for {@link Renameable#renameFile(File, File, boolean)}
	 *
	 * @throws Exception
	 *             is thrown if an exception occurs
	 */
	@Test
	public void renameFile_copiesAndDeletes_whenTheRenameIsRefusedAndDeleteIsSet() throws Exception
	{
		File file = newFile("old.txt", "content");
		File newFile = tempDir.resolve("new.txt").toFile();

		boolean renamed = renameable.renameFile(new RenameRefusingFile(file), newFile, true);

		assertTrue(renamed);
		assertEquals("content", Files.readString(newFile.toPath()));
		assertFalse(file.exists());
	}

	/**
	 * Test method for {@link Renameable#renameFile(File, File, boolean)}
	 *
	 * @throws Exception
	 *             is thrown if an exception occurs
	 */
	@Test
	public void renameFile_returnsFalseAndKeepsTheFile_whenTheRenameIsRefusedAndDeleteIsNotSet()
		throws Exception
	{
		File file = newFile("old.txt", "content");
		File newFile = tempDir.resolve("new.txt").toFile();

		boolean renamed = renameable.renameFile(new RenameRefusingFile(file), newFile, false);

		assertFalse(renamed);
		assertEquals("content", Files.readString(file.toPath()));
		assertFalse(newFile.exists());
	}

	/**
	 * Test method for {@link Renameable#renameFile(File, String)}
	 *
	 * @throws Exception
	 *             is thrown if an exception occurs
	 */
	@Test
	public void renameFile_givesTheFileTheNewNameInItsOwnDirectory() throws Exception
	{
		File file = newFile("old.txt", "content");

		boolean renamed = renameable.renameFile(file, "new.txt");

		assertTrue(renamed);
		assertEquals("content", Files.readString(tempDir.resolve("new.txt")));
		assertFalse(file.exists());
	}

	/**
	 * Test method for {@link Renameable#renameFile(File, String)}
	 */
	@Test
	public void renameFile_throwsFileDoesNotExistException_whenTheFileToRenameIsMissing()
	{
		File missing = tempDir.resolve("missing.txt").toFile();

		FileDoesNotExistException exception = assertThrows(FileDoesNotExistException.class,
			() -> renameable.renameFile(missing, "new.txt"));

		assertEquals("File missing.txt does not exist!", exception.getMessage());
	}

	/**
	 * Test method for {@link Renameable#forceToMoveFile(File, File)}
	 *
	 * @throws Exception
	 *             is thrown if an exception occurs
	 */
	@Test
	public void forceToMoveFile_movesTheFileIntoAnotherDirectory() throws Exception
	{
		File file = newFile("moved.txt", "content");
		Path otherDirectory = Files.createDirectory(tempDir.resolve("other"));
		File destination = otherDirectory.resolve("arrived.txt").toFile();

		boolean moved = renameable.forceToMoveFile(file, destination);

		assertTrue(moved);
		assertEquals("content", Files.readString(destination.toPath()));
		assertFalse(file.exists());
	}

	/**
	 * Test method for {@link Renameable#moveFile(File, File)}
	 *
	 * @throws Exception
	 *             is thrown if an exception occurs
	 */
	@Test
	public void moveFile_movesADirectoryWithItsContentToTheDestinationDirectory() throws Exception
	{
		Path sourceDirectory = Files.createDirectory(tempDir.resolve("source"));
		Files.writeString(sourceDirectory.resolve("inside.txt"), "inside");
		Path destinationParent = Files.createDirectory(tempDir.resolve("parent"));
		File destinationDirectory = destinationParent.resolve("destination").toFile();

		boolean moved = renameable.moveFile(sourceDirectory.toFile(), destinationDirectory);

		assertTrue(moved);
		assertEquals("inside",
			Files.readString(destinationDirectory.toPath().resolve("inside.txt")));
		assertFalse(Files.exists(sourceDirectory));
	}

}
