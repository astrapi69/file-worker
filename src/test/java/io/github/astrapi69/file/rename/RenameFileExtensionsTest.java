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
package io.github.astrapi69.file.rename;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;

import io.github.astrapi69.file.FileTestCase;
import io.github.astrapi69.file.create.DirectoryFactory;
import io.github.astrapi69.file.create.FileCreationState;
import io.github.astrapi69.file.exception.DirectoryAlreadyExistsException;
import io.github.astrapi69.file.exception.FileDoesNotExistException;
import io.github.astrapi69.file.exception.FileIsADirectoryException;
import io.github.astrapi69.file.exception.FileNotRenamedException;
import io.github.astrapi69.file.search.FileSearchExtensions;
import io.github.astrapi69.file.write.StoreFileExtensions;

/**
 * The unit test class for the class {@link RenameFileExtensions}.
 */
public class RenameFileExtensionsTest extends FileTestCase
{

	/**
	 * Sets up method will be invoked before every unit test method in this class.
	 *
	 * @throws Exception
	 *             is thrown if an exception occurs
	 */
	@Override
	@BeforeEach
	protected void setUp() throws Exception
	{
		super.setUp();
	}

	/**
	 * Tear down method will be invoked after every unit test method in this class.
	 *
	 * @throws Exception
	 *             is thrown if an exception occurs
	 */
	@Override
	@AfterEach
	protected void tearDown() throws Exception
	{
		super.tearDown();
	}

	/**
	 * Test method for {@link RenameFileExtensions#appendSystemtimeToFilename(File)}.
	 */
	@Test
	public void testAppendSystemtimeToFilenameFile() throws IOException
	{
		String actual;
		final File testFile1 = new File(this.testDir, "testRename.txt");
		StoreFileExtensions.toFile(testFile1, "Its a beautifull day!!!");
		actual = RenameFileExtensions.appendSystemtimeToFilename(testFile1);
		assertNotNull(actual);
		assertTrue(actual.length() == 24);
	}

	/**
	 * Test method for {@link RenameFileExtensions#appendSystemtimeToFilename(File, Date)}.
	 */
	@Test
	public void testAppendSystemtimeToFilenameFileDate() throws IOException
	{
		String actual;
		String expected;
		final File testFile1 = new File(this.testDir, "testRename.txt");
		StoreFileExtensions.toFile(testFile1, "Its a beautifull day!!!");
		LocalDateTime localDateTime = LocalDateTime.parse("2007-11-07T06:34:59");
		Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
		actual = RenameFileExtensions.appendSystemtimeToFilename(testFile1, date);
		assertNotNull(actual);
		assertTrue(actual.length() == 24);
		expected = "testRename_063459000.txt";
		assertEquals(actual, expected);
	}

	/**
	 * Test method for {@link RenameFileExtensions#changeAllFilenameSuffix(File, String, String)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws FileDoesNotExistException
	 *             the file does not exist exception
	 * @throws FileIsADirectoryException
	 *             the file is A directory exception
	 */
	@Test
	public void testChangeAllFilenameSuffixFileStringString()
		throws IOException, FileDoesNotExistException, FileIsADirectoryException
	{
		final String oldFileSuffix = ".txt";
		final String newFileSuffix = ".rtf";
		final List<File> filesWithNewSuffixes = new ArrayList<>();
		final List<File> filesWithOldSuffixes = new ArrayList<>();
		final String filePrefix1 = "testChangeAllFilenameSuffixFileStringString1";
		final File testFile1 = new File(this.deepDir, filePrefix1 + oldFileSuffix);
		filesWithOldSuffixes.add(testFile1);
		final File fileWithNewSuffix1 = new File(this.deepDir, filePrefix1 + newFileSuffix);
		filesWithNewSuffixes.add(fileWithNewSuffix1);
		final String filePrefix2 = "testChangeAllFilenameSuffixFileStringString2";
		final File testFile2 = new File(this.deepDir, filePrefix2 + oldFileSuffix);
		filesWithOldSuffixes.add(testFile2);
		final File fileWithNewSuffix2 = new File(this.deepDir, filePrefix2 + newFileSuffix);
		filesWithNewSuffixes.add(fileWithNewSuffix2);
		final String filePrefix3 = "testChangeAllFilenameSuffixFileStringString3";
		final File testFile3 = new File(this.deeperDir, filePrefix3 + oldFileSuffix);
		filesWithOldSuffixes.add(testFile3);
		final File fileWithNewSuffix3 = new File(this.deeperDir, filePrefix3 + newFileSuffix);
		filesWithNewSuffixes.add(fileWithNewSuffix3);
		final String filePrefix4 = "testChangeAllFilenameSuffixFileStringString4";
		final File testFile4 = new File(this.deeperDir, filePrefix4 + oldFileSuffix);
		filesWithOldSuffixes.add(testFile4);
		final File fileWithNewSuffix4 = new File(this.deeperDir, filePrefix4 + newFileSuffix);
		filesWithNewSuffixes.add(fileWithNewSuffix4);

		List<File> notDeletedFiles = RenameFileExtensions.changeAllFilenameSuffix(this.deepDir,
			oldFileSuffix, newFileSuffix);
		this.actual = null == notDeletedFiles;
		assertTrue(this.actual);
		// ----------------------------------------------------------------
		for (final File file : filesWithOldSuffixes)
		{
			final File currentFile = file;
			currentFile.createNewFile();
		}
		notDeletedFiles = RenameFileExtensions.changeAllFilenameSuffix(this.deepDir, oldFileSuffix,
			newFileSuffix);
		this.actual = null == notDeletedFiles;
		assertTrue(this.actual);

		for (final File file : filesWithNewSuffixes)
		{
			final File currentFile = file;
			this.actual = FileSearchExtensions.containsFileRecursive(this.deepDir, currentFile);
			assertTrue(this.actual);
		}
	}

	/**
	 * Test method for
	 * {@link RenameFileExtensions#changeAllFilenameSuffix(File, String, String, boolean)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws FileDoesNotExistException
	 *             the file does not exist exception
	 * @throws FileIsADirectoryException
	 *             the file is A directory exception
	 */
	@Test
	public void testChangeAllFilenameSuffixFileStringStringBoolean()
		throws IOException, FileDoesNotExistException, FileIsADirectoryException
	{
		final String oldFileSuffix = ".txt";
		final String newFileSuffix = ".rtf";
		final List<File> filesWithNewSuffixes = new ArrayList<>();
		final List<File> filesWithOldSuffixes = new ArrayList<>();
		final String filePrefix1 = "testChangeAllFilenameSuffixFileStringStringBoolean1";
		final File testFile1 = new File(this.deepDir, filePrefix1 + oldFileSuffix);
		filesWithOldSuffixes.add(testFile1);
		final File fileWithNewSuffix1 = new File(this.deepDir, filePrefix1 + newFileSuffix);
		filesWithNewSuffixes.add(fileWithNewSuffix1);
		final String filePrefix2 = "testChangeAllFilenameSuffixFileStringStringBoolean2";
		final File testFile2 = new File(this.deepDir, filePrefix2 + oldFileSuffix);
		filesWithOldSuffixes.add(testFile2);
		final File fileWithNewSuffix2 = new File(this.deepDir, filePrefix2 + newFileSuffix);
		filesWithNewSuffixes.add(fileWithNewSuffix2);
		final String filePrefix3 = "testChangeAllFilenameSuffixFileStringStringBoolean3";
		final File testFile3 = new File(this.deeperDir, filePrefix3 + oldFileSuffix);
		filesWithOldSuffixes.add(testFile3);
		final File fileWithNewSuffix3 = new File(this.deeperDir, filePrefix3 + newFileSuffix);
		filesWithNewSuffixes.add(fileWithNewSuffix3);
		final String filePrefix4 = "testChangeAllFilenameSuffixFileStringStringBoolean4";
		final File testFile4 = new File(this.deeperDir, filePrefix4 + oldFileSuffix);
		filesWithOldSuffixes.add(testFile4);
		final File fileWithNewSuffix4 = new File(this.deeperDir, filePrefix4 + newFileSuffix);
		filesWithNewSuffixes.add(fileWithNewSuffix4);

		List<File> notDeletedFiles = RenameFileExtensions.changeAllFilenameSuffix(this.deepDir,
			oldFileSuffix, newFileSuffix, true);
		this.actual = null == notDeletedFiles;
		assertTrue(this.actual);
		// ----------------------------------------------------------------
		for (final File file : filesWithOldSuffixes)
		{
			final File currentFile = file;
			currentFile.createNewFile();
		}
		notDeletedFiles = RenameFileExtensions.changeAllFilenameSuffix(this.deepDir, oldFileSuffix,
			newFileSuffix, true);

		for (final File file : filesWithNewSuffixes)
		{
			final File currentFile = file;
			this.actual = FileSearchExtensions.containsFileRecursive(this.deepDir, currentFile);
			assertTrue(this.actual);
		}
	}

	/**
	 * Test method for {@link RenameFileExtensions#changeFilenameSuffix(File, String)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws FileNotRenamedException
	 *             the file not renamed exception
	 * @throws FileDoesNotExistException
	 *             the file does not exist exception
	 * @throws FileIsADirectoryException
	 *             the file is A directory exception
	 */
	@Test
	public void testChangeFilenameSuffixFileString() throws IOException, FileNotRenamedException,
		FileDoesNotExistException, FileIsADirectoryException
	{
		final String filePrefix = "testChangeFilenameSuffixFileString";
		final String oldFileSuffix = ".txt";
		final String newFileSuffix = ".rtf";
		final File testFile1 = new File(this.deepDir, filePrefix + oldFileSuffix);
		final File fileWithNewSuffix = new File(this.deepDir, filePrefix + newFileSuffix);
		try
		{
			this.actual = RenameFileExtensions.changeFilenameSuffix(testFile1, newFileSuffix);
		}
		catch (final Exception e)
		{
			this.actual = e instanceof FileDoesNotExistException;
			assertTrue(this.actual);
		}

		testFile1.createNewFile();
		this.actual = RenameFileExtensions.changeFilenameSuffix(testFile1, newFileSuffix);
		assertTrue(this.actual);

		this.actual = FileSearchExtensions.containsFile(this.deepDir, fileWithNewSuffix);
		assertTrue(this.actual);
	}

	/**
	 * Test method for {@link RenameFileExtensions#changeFilenameSuffix(File, String, boolean)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws FileDoesNotExistException
	 *             the file does not exist exception
	 * @throws FileIsADirectoryException
	 *             the file is A directory exception
	 */
	@Test
	public void testChangeFilenameSuffixFileStringBoolean()
		throws IOException, FileDoesNotExistException, FileIsADirectoryException
	{
		final String filePrefix = "testChangeFilenameSuffixFileStringBoolean";
		final String oldFileSuffix = ".txt";
		final String newFileSuffix = ".rtf";
		final File testFile1 = new File(this.deepDir, filePrefix + oldFileSuffix);
		final File fileWithNewSuffix = new File(this.deepDir, filePrefix + newFileSuffix);
		try
		{
			this.actual = RenameFileExtensions.changeFilenameSuffix(testFile1, newFileSuffix, true);
		}
		catch (final Exception e)
		{
			this.actual = e instanceof FileDoesNotExistException;
			assertTrue(this.actual);
		}

		testFile1.createNewFile();
		this.actual = RenameFileExtensions.changeFilenameSuffix(testFile1, newFileSuffix, true);
		assertTrue(this.actual);

		this.actual = FileSearchExtensions.containsFile(this.deepDir, fileWithNewSuffix);
		assertTrue(this.actual);
	}

	/**
	 * Test method for {@link RenameFileExtensions#forceToMoveFile(File, File)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws FileIsADirectoryException
	 *             the file is A directory exception
	 */
	@Test
	public void testForceToMoveFile() throws IOException, FileIsADirectoryException
	{
		final File srcFile = new File(this.testDir.getAbsoluteFile(), "testMoveFile.txt");
		final File expectedMovedFile = new File(this.deepDir, "testMovedFile.moved");
		StoreFileExtensions.toFile(srcFile, "Its a beautifull day!!!");
		actual = RenameFileExtensions.forceToMoveFile(srcFile, expectedMovedFile);
		expected = true;
		assertEquals(actual, expected);
		assertTrue(expectedMovedFile.exists());
		assertFalse(srcFile.exists());
		expectedMovedFile.deleteOnExit();
	}

	/**
	 * Test method for {@link RenameFileExtensions#moveFile(File, File)} for directory.
	 *
	 * @throws DirectoryAlreadyExistsException
	 *             the directory allready exists exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws FileIsADirectoryException
	 *             the file is A directory exception
	 */
	@Test
	public void testMoveDir()
		throws DirectoryAlreadyExistsException, IOException, FileIsADirectoryException
	{
		// Test to move a directory...
		// Create a test directory to move.
		final File srcDir = new File(this.deepDir, "dirToMove");
		// Create a destination directory.
		final File destDir = new File(this.deeperDir, "dirToMove");
		// Create a test file in the directory to move to check if the file is moved too.
		final String filePrefix = "testMoveFile";
		final String fileSuffix = ".txt";
		final File srcFile = new File(srcDir, filePrefix + fileSuffix);
		// if the testfile does not exist create it.
		if (!srcDir.exists())
		{
			final FileCreationState state = DirectoryFactory.newDirectory(srcDir);
			assertTrue(state.equals(FileCreationState.CREATED),
				"The directory " + srcDir.getAbsolutePath() + " should be created.");
			StoreFileExtensions.toFile(srcFile, "Its a beautifull day!!!");
		}
		// Test to move the dir.
		this.actual = RenameFileExtensions.moveFile(srcDir, destDir);
		assertTrue(this.actual, "Directory should be renamed.");

	}

	/**
	 * Test method for {@link RenameFileExtensions#moveFile(File, File)} for file.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws FileIsADirectoryException
	 *             the file is A directory exception
	 */
	@Test
	public void testMoveFile() throws IOException, FileIsADirectoryException
	{
		// Test to move a file....
		final String filePrefix1 = "testMoveFile";
		final String oldFileSuffix = ".txt";
		final File srcFile = new File(this.deepDir, filePrefix1 + oldFileSuffix);
		final File destDir = new File(this.deeperDir, filePrefix1 + oldFileSuffix);

		StoreFileExtensions.toFile(srcFile, "Its a beautifull day!!!");
		this.actual = RenameFileExtensions.moveFile(srcFile, destDir);
		assertTrue(this.actual, "File should be renamed.");
		this.actual = FileSearchExtensions.containsFile(this.deeperDir, destDir);
		assertTrue(this.actual, "The renamed file should exist in this directory.");
	}

	/**
	 * Test method for {@link RenameFileExtensions#moveFile(File, File)} that throws a
	 * {@code FileNotFoundException}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws FileIsADirectoryException
	 *             the file is A directory exception
	 */
	@Test
	public void testMoveFileFileNotFoundException() throws IOException, FileIsADirectoryException
	{
		Assertions.assertThrows(FileNotFoundException.class, () -> {

			// Test to move a file....
			final String filePrefix1 = "testMoveFile";
			final String oldFileSuffix = ".txt";
			final File srcFile = new File(this.deepDir, filePrefix1 + oldFileSuffix);
			final File destDir = new File(this.deeperDir, filePrefix1 + oldFileSuffix);

			RenameFileExtensions.moveFile(srcFile, destDir);
		});
	}

	/**
	 * Test method for {@link RenameFileExtensions#renameFile(File, File)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws FileIsADirectoryException
	 *             the file is A directory exception
	 */
	@Test
	public void testRenameFileFileFile() throws IOException, FileIsADirectoryException
	{
		final File srcFile = new File(this.testDir.getAbsoluteFile(), "testRenameFile.txt");
		final File expectedRenamedFile = new File(this.deepDir, "testRenamedFile.renamed");
		StoreFileExtensions.toFile(srcFile, "Its a beautifull day!!!");
		actual = RenameFileExtensions.renameFile(srcFile, expectedRenamedFile);
		expected = true;
		assertEquals(actual, expected);
		assertTrue(expectedRenamedFile.exists());
		assertFalse(srcFile.exists());


		final String filePrefix1 = "testRenameFileFileFile1";
		final String filePrefix2 = "testRenameFileFileFile2";
		final String oldFileSuffix = ".txt";
		final String newFileSuffix = ".rtf";
		final File testFile1 = new File(this.deepDir, filePrefix1 + oldFileSuffix);
		final File renamedFile1 = new File(this.deepDir, filePrefix2 + newFileSuffix);

		this.actual = RenameFileExtensions.renameFile(testFile1, renamedFile1);
		assertFalse(this.actual, "File should not exist in this directory.");
		StoreFileExtensions.toFile(testFile1, "Its a beautifull day!!!");
		this.actual = RenameFileExtensions.renameFile(testFile1, renamedFile1);
		assertTrue(this.actual, "File should be renamed.");
		this.actual = FileSearchExtensions.containsFile(this.deepDir, renamedFile1);
		assertTrue(this.actual, "The renamed file should exist in this directory.");
		testFile1.deleteOnExit();
		renamedFile1.deleteOnExit();
		expectedRenamedFile.deleteOnExit();
	}

	/**
	 * Test method for {@link RenameFileExtensions#renameFile(File, File, boolean)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws FileIsADirectoryException
	 *             the file is A directory exception
	 */
	@Test
	public void testRenameFileFileFileBoolean() throws IOException, FileIsADirectoryException
	{
		final File srcFile = new File(this.testDir.getAbsoluteFile(), "testRenameFile.txt");
		final File expectedRenamedFile = new File(this.deepDir, "testRenamedFile.renamed");
		StoreFileExtensions.toFile(srcFile, "Its a beautifull day!!!");
		actual = RenameFileExtensions.renameFile(srcFile, expectedRenamedFile, true);
		expected = true;
		assertEquals(actual, expected);
		assertTrue(expectedRenamedFile.exists());
		assertFalse(srcFile.exists());
		expectedRenamedFile.deleteOnExit();
		srcFile.deleteOnExit();

		final String filePrefix1 = "testRenameFileFileFile1";
		final String filePrefix2 = "testRenameFileFileFile2";
		final String oldFileSuffix = ".txt";
		final String newFileSuffix = ".rtf";
		final File testFile1 = new File(this.deepDir, filePrefix1 + oldFileSuffix);
		final File renamedFile1 = new File(this.deepDir, filePrefix2 + newFileSuffix);

		this.actual = RenameFileExtensions.renameFile(testFile1, renamedFile1, false);
		assertFalse(this.actual, "File should not exist in this directory.");
		StoreFileExtensions.toFile(testFile1, "Its a beautifull day!!!");
		this.actual = RenameFileExtensions.renameFile(testFile1, renamedFile1, false);
		assertTrue(this.actual, "File should be renamed.");
		this.actual = FileSearchExtensions.containsFile(this.deepDir, renamedFile1);
		assertTrue(this.actual, "The renamed file should exist in this directory.");
		testFile1.deleteOnExit();
		renamedFile1.deleteOnExit();
	}

	/**
	 * Test method for {@link RenameFileExtensions#renameFile(File, String)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws FileIsADirectoryException
	 *             the file is A directory exception
	 * @throws FileDoesNotExistException
	 *             the file does not exist exception
	 */
	@Test
	public void testRenameFileFileString()
		throws IOException, FileIsADirectoryException, FileDoesNotExistException
	{
		final String filePrefix1 = "testRenameFileFileString1";
		final String filePrefix2 = "testRenameFileFileString2";
		final String oldFileSuffix = ".txt";
		final String newFileSuffix = ".rtf";
		final File testFile1 = new File(this.deepDir, filePrefix1 + oldFileSuffix);
		final File renamedFile1 = new File(this.deepDir, filePrefix2 + newFileSuffix);

		StoreFileExtensions.toFile(testFile1, "Its a beautifull day!!!");
		this.actual = RenameFileExtensions.renameFile(testFile1, renamedFile1.getName());
		assertTrue(this.actual, "File should be renamed.");
		this.actual = FileSearchExtensions.containsFile(this.deepDir, renamedFile1);
		assertTrue(this.actual, "The renamed file should exist in this directory.");
	}

	/**
	 * Test method for {@link RenameFileExtensions#renameFileWithSystemtime(File)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws FileIsADirectoryException
	 *             the file is A directory exception
	 */
	@Test
	public void testRenameFileWithSystemtime() throws IOException, FileIsADirectoryException
	{
		final File srcFile = new File(this.testDir.getAbsoluteFile(),
			"testRenameFileWithSystemtime.txt");// 32 length
		StoreFileExtensions.toFile(srcFile, "Its a beautifull day!!!");
		File renameFileWithSystemtime = RenameFileExtensions.renameFileWithSystemtime(srcFile);
		assertNotNull(renameFileWithSystemtime);
		String name = renameFileWithSystemtime.getName();
		assertTrue(name.length() == 42);
	}

	/**
	 * Test method for {@link RenameFileExtensions}.
	 */
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(RenameFileExtensions.class);
	}


}
