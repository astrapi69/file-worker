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
package de.alpharogroup.file;

import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.meanbean.factories.ObjectCreationException;
import org.meanbean.test.BeanTestException;
import org.meanbean.test.BeanTester;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.alpharogroup.file.compare.CompareFileExtensions;
import de.alpharogroup.file.copy.CopyFileExtensions;
import de.alpharogroup.file.create.CreateFileExtensions;
import de.alpharogroup.file.delete.DeleteFileExtensions;
import de.alpharogroup.file.exceptions.DirectoryAllreadyExistsException;
import de.alpharogroup.file.exceptions.FileDoesNotExistException;
import de.alpharogroup.file.exceptions.FileIsADirectoryException;
import de.alpharogroup.file.exceptions.FileIsNotADirectoryException;
import de.alpharogroup.file.exceptions.FileNotRenamedException;
import de.alpharogroup.file.read.ReadFileExtensions;
import de.alpharogroup.file.rename.RenameFileExtensions;
import de.alpharogroup.file.search.FileSearchExtensions;
import de.alpharogroup.file.write.WriteFileExtensions;
import de.alpharogroup.file.zip.ZipExtensions;
import de.alpharogroup.io.StreamExtensions;

/**
 * The unit test class for the class {@link FileExtensions}.
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public class FileExtensionsTest extends FileTestCase
{

	/**
	 * Sets up method will be invoked before every unit test method in this class.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Override
	@BeforeMethod
	protected void setUp() throws Exception
	{
		super.setUp();
	}

	/**
	 * Tear down method will be invoked after every unit test method in this class.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Override
	@AfterMethod
	protected void tearDown() throws Exception
	{
		super.tearDown();
	}

	@Test
	public void testAppendSystemtimeToFilename()
	{
		// final String format = "HHmmssSSS";
		final String filePrefix = "testAppendSystemtimeToFilename";
		final String fileSuffix = ".txt";
		final File testFile1 = new File(this.testDir, filePrefix + fileSuffix);
		final String inputString = "Its a beautifull day!!!";

		final String ap = testFile1.getAbsolutePath();
		WriteFileExtensions.string2File(inputString, ap);

		// final Date before = new Date();
		// final String compare = RenameFileUtils.appendSystemtimeToFilename(testFile1);
		// final Date after = new Date();
		// final int start = compare.indexOf("_");
		// final int end = compare.indexOf(fileSuffix);
		// final String sysDateFromFile = compare.substring(start + 1, end);
		// final String sysTimeBefore = de.alpharogroup.date.DateUtils.parseToString(before,
		// format);
		// final String sysTimeAfter = DateUtils.parseToString(after, format);
		// final Date between = DateUtils.parseToDate(sysDateFromFile, format);
		// final Date beforeDate = DateUtils.parseToDate(sysTimeBefore, format);
		// final Date afterDate = DateUtils.parseToDate(sysTimeAfter, format);
		// this.actual = DateUtils.isBetween(beforeDate, afterDate, between);
		// assertTrue("", this.actual);
	}

	@Test
	public void testChangeAllFilenameSuffixFileStringString()
		throws IOException, FileDoesNotExistException
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
		assertTrue("", this.actual);
		// ----------------------------------------------------------------
		for (final File file : filesWithOldSuffixes)
		{
			final File currentFile = file;
			currentFile.createNewFile();
		}
		notDeletedFiles = RenameFileExtensions.changeAllFilenameSuffix(this.deepDir, oldFileSuffix,
			newFileSuffix);
		this.actual = null == notDeletedFiles;
		assertTrue("", this.actual);

		for (final File file : filesWithNewSuffixes)
		{
			final File currentFile = file;
			this.actual = FileSearchExtensions.containsFileRecursive(this.deepDir, currentFile);
			assertTrue("", this.actual);
		}
	}

	@Test
	public void testChangeAllFilenameSuffixFileStringStringBoolean()
		throws IOException, FileDoesNotExistException
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
		assertTrue("", this.actual);
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
			assertTrue("", this.actual);
		}
	}

	@Test
	public void testChangeFilenameSuffixFileString()
		throws FileNotRenamedException, IOException, FileDoesNotExistException
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
			assertTrue("", this.actual);
		}

		testFile1.createNewFile();
		this.actual = RenameFileExtensions.changeFilenameSuffix(testFile1, newFileSuffix);
		assertTrue("", this.actual);

		this.actual = FileSearchExtensions.containsFile(this.deepDir, fileWithNewSuffix);
		assertTrue("", this.actual);

	}

	@Test
	public void testChangeFilenameSuffixFileStringBoolean()
		throws IOException, FileNotRenamedException, FileDoesNotExistException
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
			assertTrue("", this.actual);
		}

		testFile1.createNewFile();
		this.actual = RenameFileExtensions.changeFilenameSuffix(testFile1, newFileSuffix, true);
		assertTrue("", this.actual);

		this.actual = FileSearchExtensions.containsFile(this.deepDir, fileWithNewSuffix);
		assertTrue("", this.actual);
	}

	@Test
	public void testCheckFile() throws DirectoryAllreadyExistsException, IOException
	{
		if (this.testDir.exists())
		{
			DeleteFileExtensions.delete(this.testDir);
		}
		this.testDir = new File(this.testResources, "testDir");
		Exception ex = DeleteFileExtensions.checkFile(this.testDir);
		this.actual = ex != null;
		assertTrue("", this.actual);
		this.actual = ex instanceof FileDoesNotExistException;
		assertTrue("", this.actual);
		if (!this.testDir.exists())
		{
			final boolean created = CreateFileExtensions.newDirectory(this.testDir);
			assertTrue("The directory should be created.", created);
		}
		ex = DeleteFileExtensions.checkFile(this.testDir);
		this.actual = ex == null;
		assertTrue("", this.actual);

		final File testFile1 = new File(this.testDir, "testCheckFile.txt");
		WriteFileExtensions.string2File(testFile1, "Its a beautifull day!!!");
		ex = DeleteFileExtensions.checkFile(testFile1);
		this.actual = ex != null;
		assertTrue("", this.actual);
		this.actual = ex instanceof FileIsNotADirectoryException;
		assertTrue("", this.actual);

		final File testFile2 = new File("a");
		ex = DeleteFileExtensions.checkFile(testFile2);
		this.actual = ex != null;
		assertTrue("", this.actual);

	}

	@Test
	public void testCompareFiles()
	{
		final String filePrefix1 = "testCompareFiles1";
		final String filePrefix2 = "testCompareFiles2";
		final String oldFileSuffix = ".txt";
		final String newFileSuffix = ".rtf";
		final File source = new File(this.deepDir, filePrefix1 + oldFileSuffix);
		File compare = new File(this.deepDir, filePrefix1 + oldFileSuffix);

		this.actual = CompareFileExtensions.compareFiles(source, compare, false);
		assertTrue("File should be equal cause they dont exist.", this.actual);
		compare = new File(this.deepDir, filePrefix2 + newFileSuffix);
		this.actual = CompareFileExtensions.compareFiles(source, compare, false);
		assertFalse("File should not be equal.", this.actual);
		WriteFileExtensions.string2File(source, "Its a beautifull day!!!");
		WriteFileExtensions.string2File(compare, "Its a beautifull day!!!");
		this.actual = CompareFileExtensions.compareFiles(source, compare, false);
		assertTrue("File should be equal.", this.actual);
		this.actual = CompareFileExtensions.compareFiles(source, compare, true);
		assertTrue("File should be equal.", this.actual);
		WriteFileExtensions.string2File(compare, "Its a beautifull evening!!!");
		this.actual = CompareFileExtensions.compareFiles(source, compare, true);
		assertFalse("File should not be equal.", this.actual);
		WriteFileExtensions.string2File(compare, "Its a beautifull boy!!!");
		this.actual = CompareFileExtensions.compareFiles(source, compare, true);
		assertFalse("File should not be equal.", this.actual);

	}

	@Test
	public void testContainsFileFileFile() throws DirectoryAllreadyExistsException, IOException
	{
		final File testFile = new File(this.testDir, "beautifull.txt");
		WriteFileExtensions.string2File(testFile, "Its a beautifull day!!!");
		boolean contains = FileSearchExtensions.containsFile(new File("."), testFile);
		assertFalse("File should not exist in this directory.", contains);
		contains = FileSearchExtensions.containsFile(this.testDir, testFile);
		assertTrue("File should not exist in this directory.", contains);
	}

	@Test
	public void testContainsFileFileString()
	{
		final File testFile = new File(this.testDir, "beautifull.txt");
		WriteFileExtensions.string2File(testFile, "Its a beautifull day!!!");
		boolean contains = FileSearchExtensions.containsFile(new File("."), testFile);
		assertFalse("File should not exist in this directory.", contains);
		final String filename = testFile.getName();
		contains = FileSearchExtensions.containsFile(this.testDir, filename);
		assertTrue("File should not exist in this directory.", contains);
	}

	@Test
	public void testContainsFileRecursive()
	{

		final File testFile = new File(this.testDir.getAbsoluteFile(),
			"testContainsFileRecursives.txt");
		WriteFileExtensions.string2File(testFile, "Its a beautifull day!!!");

		final File testFile3 = new File(this.deepDir.getAbsoluteFile(),
			"testContainsFileRecursives.cvs");
		WriteFileExtensions.string2File(testFile3, "Its a beautifull evening!!!");
		final File currentDir = new File(".").getAbsoluteFile();
		boolean contains = FileSearchExtensions.containsFileRecursive(currentDir.getAbsoluteFile(),
			testFile);
		assertFalse("File should not exist in this directory.", contains);
		contains = FileSearchExtensions.containsFileRecursive(this.testDir, testFile);
		assertTrue("File should not exist in this directory.", contains);
		this.actual = FileSearchExtensions.containsFileRecursive(this.testDir, testFile3);
		assertTrue("", this.actual);
	}

	@Test
	public void testCopyFile() throws IOException, FileIsADirectoryException
	{
		final File source = new File(this.testDir.getAbsoluteFile(), "testCopyFileInput.txt");
		final File destination = new File(this.testDir.getAbsoluteFile(), "testCopyFileOutput.tft");
		try
		{
			this.actual = CopyFileExtensions.copyFile(source, destination);
			assertFalse("", this.actual);
		}
		catch (final Exception fnfe)
		{
			this.actual = fnfe instanceof FileNotFoundException;
			assertTrue("", this.actual);
		}
		final String inputString = "Its a beautifull day!!!";
		final String expected = inputString;
		WriteFileExtensions.string2File(source, inputString);

		this.actual = CopyFileExtensions.copyFile(source, destination);
		assertTrue("", this.actual);
		final String compare = ReadFileExtensions.readFromFile(destination);
		this.actual = expected.equals(compare);
		assertTrue("", this.actual);

	}

	@Test
	public void testCreateDirectory() throws DirectoryAllreadyExistsException, IOException
	{
		final File testing = new File(this.testResources, "testCreateDirectory");
		if (testing.exists())
		{
			DeleteFileExtensions.delete(testing);
		}
		final boolean created = CreateFileExtensions.newDirectory(testing);
		assertTrue("The directory should be created.", created);
		this.actual = testing.isDirectory();
		assertTrue("Created File should be a directory.", this.actual);
		if (testing.exists())
		{
			DeleteFileExtensions.delete(testing);
		}
	}

	@Test
	public void testCreateFile() throws IOException
	{
		final File source = new File(this.testDir.getAbsoluteFile(), "testGetOutputStream.txt");

		CreateFileExtensions.newFile(source);
		this.actual = source.exists();
		assertTrue("", this.actual);

	}

	@Test
	public void testDeleleFiles() throws IOException, DirectoryAllreadyExistsException
	{

		final File testFile1 = new File(this.testDir, "testDeleleFiles1.txt");
		final File testFile2 = new File(this.testDir, "testDeleleFiles2.txt");
		final File testFile3 = new File(this.deepDir, "testDeleleFiles3.txt");
		final File testFile4 = new File(this.testDir, "testDeleleFiles4.tft");
		final File testFile5 = new File(this.deepDir, "testDeleleFiles5.cvs");
		WriteFileExtensions.string2File(testFile1, "Its a beautifull day!!!");
		WriteFileExtensions.string2File(testFile2, "Its a beautifull evening!!!");
		WriteFileExtensions.string2File(testFile3, "Its a beautifull night!!!");
		WriteFileExtensions.string2File(testFile4, "Its a beautifull morning!!!");
		WriteFileExtensions.string2File(testFile5, "She's a beautifull woman!!!");
		// --------------------------------
		this.actual = this.deepDir.exists();
		assertTrue("", this.actual);

		this.actual = testFile1.exists();
		assertTrue("", this.actual);

		this.actual = testFile2.exists();
		assertTrue("", this.actual);

		this.actual = testFile3.exists();
		assertTrue("", this.actual);

		this.actual = testFile4.exists();
		assertTrue("", this.actual);

		this.actual = testFile5.exists();
		assertTrue("", this.actual);

		this.actual = this.testDir.exists();
		assertTrue("", this.actual);

		DeleteFileExtensions.deleteFiles(this.testDir);

		this.actual = this.deepDir.exists();
		assertFalse("", this.actual);

		this.actual = testFile1.exists();
		assertFalse("", this.actual);

		this.actual = testFile2.exists();
		assertFalse("", this.actual);

		this.actual = testFile3.exists();
		assertFalse("", this.actual);

		this.actual = testFile4.exists();
		assertFalse("", this.actual);

		this.actual = testFile5.exists();
		assertFalse("", this.actual);

	}

	@Test
	public void testDelete() throws DirectoryAllreadyExistsException, IOException
	{

		final File testFile1 = new File(this.testDir, "testDelete1.txt");
		final File testFile2 = new File(this.testDir, "testDelete2.txt");
		final File testFile3 = new File(this.deepDir, "testDelete3.txt");
		final File testFile4 = new File(this.testDir, "testDelete4.tft");
		final File testFile5 = new File(this.deepDir, "testDelete5.cvs");
		WriteFileExtensions.string2File(testFile1, "Its a beautifull day!!!");
		WriteFileExtensions.string2File(testFile2, "Its a beautifull evening!!!");
		WriteFileExtensions.string2File(testFile3, "Its a beautifull night!!!");
		WriteFileExtensions.string2File(testFile4, "Its a beautifull morning!!!");
		WriteFileExtensions.string2File(testFile5, "She's a beautifull woman!!!");
		// --------------------------------
		this.actual = testFile1.exists();
		assertTrue("", this.actual);

		DeleteFileExtensions.delete(testFile1);

		this.actual = testFile1.exists();
		assertFalse("", this.actual);
		// --------------------------------
		this.actual = testFile3.exists();
		assertTrue("", this.actual);

		DeleteFileExtensions.delete(testFile3);

		this.actual = testFile3.exists();
		assertFalse("", this.actual);
		// --------------------------------

		this.actual = testFile5.exists();
		assertTrue("", this.actual);

		this.actual = this.deepDir.exists();
		assertTrue("", this.actual);

		DeleteFileExtensions.delete(this.deepDir);

		this.actual = this.deepDir.exists();
		assertFalse("", this.actual);

		this.actual = testFile5.exists();
		assertFalse("", this.actual);
		// --------------------------------

		this.actual = testFile4.exists();
		assertTrue("", this.actual);

		this.actual = this.testDir.exists();
		assertTrue("", this.actual);

		DeleteFileExtensions.delete(this.testDir);

		this.actual = testFile4.exists();
		assertFalse("", this.actual);

		this.actual = this.testDir.exists();
		assertFalse("", this.actual);
		// --------------------------------

	}

	@Test
	public void testDeleteAllFiles() throws DirectoryAllreadyExistsException, IOException
	{

		final File testFile1 = new File(this.testDir, "testDeleteAllFiles1.txt");
		final File testFile2 = new File(this.testDir, "testDeleteAllFiles2.txt");
		final File testFile3 = new File(this.deepDir, "testDeleteAllFiles3.txt");
		final File testFile4 = new File(this.testDir, "testDeleteAllFiles4.tft");
		final File testFile5 = new File(this.deepDir, "testDeleteAllFiles5.cvs");
		WriteFileExtensions.string2File(testFile1, "Its a beautifull day!!!");
		WriteFileExtensions.string2File(testFile2, "Its a beautifull evening!!!");
		WriteFileExtensions.string2File(testFile3, "Its a beautifull night!!!");
		WriteFileExtensions.string2File(testFile4, "Its a beautifull morning!!!");
		WriteFileExtensions.string2File(testFile5, "She's a beautifull woman!!!");
		// --------------------------------
		this.actual = testFile1.exists();
		assertTrue("", this.actual);

		this.actual = testFile3.exists();
		assertTrue("", this.actual);

		this.actual = testFile5.exists();
		assertTrue("", this.actual);

		this.actual = this.deepDir.exists();
		assertTrue("", this.actual);

		this.actual = testFile4.exists();
		assertTrue("", this.actual);

		this.actual = this.testDir.exists();
		assertTrue("", this.actual);

		DeleteFileExtensions.deleteAllFiles(this.testDir);

		this.actual = this.deepDir.exists();
		assertFalse("", this.actual);

		this.actual = this.testDir.exists();
		assertFalse("", this.actual);

		this.actual = testFile1.exists();
		assertFalse("", this.actual);

		this.actual = testFile2.exists();
		assertFalse("", this.actual);

		this.actual = testFile3.exists();
		assertFalse("", this.actual);

		this.actual = testFile4.exists();
		assertFalse("", this.actual);

		this.actual = testFile5.exists();
		assertFalse("", this.actual);

	}

	@Test
	public void testDeleteAllFilesWithSuffix() throws DirectoryAllreadyExistsException, IOException
	{

		final File testFile1 = new File(this.testDir, "testDeleteAllFilesWithSuffix1.txt");
		final File testFile2 = new File(this.testDir, "testDeleteAllFilesWithSuffix2.txt");
		final File testFile3 = new File(this.deepDir, "testDeleteAllFilesWithSuffix3.txt");
		final File testFile4 = new File(this.testDir, "testDeleteAllFilesWithSuffix4.tft");
		final File testFile5 = new File(this.deepDir, "testDeleteAllFilesWithSuffix5.cvs");
		WriteFileExtensions.string2File(testFile1, "Its a beautifull day!!!");
		WriteFileExtensions.string2File(testFile2, "Its a beautifull evening!!!");
		WriteFileExtensions.string2File(testFile3, "Its a beautifull night!!!");
		WriteFileExtensions.string2File(testFile4, "Its a beautifull morning!!!");
		WriteFileExtensions.string2File(testFile5, "She's a beautifull woman!!!");

		DeleteFileExtensions.deleteAllFilesWithSuffix(this.testDir, ".txt");

		this.actual = testFile1.exists();
		assertFalse("", this.actual);
		this.actual = testFile2.exists();
		assertFalse("", this.actual);
		this.actual = testFile3.exists();
		assertFalse("", this.actual);
		this.actual = testFile4.exists();
		assertTrue("", this.actual);
		this.actual = testFile5.exists();
		assertTrue("", this.actual);

	}

	@Test
	public void testDeleteFile() throws DirectoryAllreadyExistsException, IOException
	{

		final File testFile1 = new File(this.testDir, "testDeleteFile1.txt");
		final File testFile2 = new File(this.testDir, "testDeleteFile2.txt");
		final File testFile3 = new File(this.deepDir, "testDeleteFile3.txt");
		final File testFile4 = new File(this.testDir, "testDeleteFile4.tft");
		final File testFile5 = new File(this.deepDir, "testDeleteFile5.cvs");
		WriteFileExtensions.string2File(testFile1, "Its a beautifull day!!!");
		WriteFileExtensions.string2File(testFile2, "Its a beautifull evening!!!");
		WriteFileExtensions.string2File(testFile3, "Its a beautifull night!!!");
		WriteFileExtensions.string2File(testFile4, "Its a beautifull morning!!!");
		WriteFileExtensions.string2File(testFile5, "She's a beautifull woman!!!");
		// --------------------------------
		this.actual = testFile1.exists();
		assertTrue("", this.actual);

		DeleteFileExtensions.deleteFile(testFile1);

		this.actual = testFile1.exists();
		assertFalse("", this.actual);
		// --------------------------------
		this.actual = testFile3.exists();
		assertTrue("", this.actual);

		DeleteFileExtensions.deleteFile(testFile3);

		this.actual = testFile3.exists();
		assertFalse("", this.actual);
		// --------------------------------

		this.actual = testFile5.exists();
		assertTrue("", this.actual);

		this.actual = this.deepDir.exists();
		assertTrue("", this.actual);

		DeleteFileExtensions.deleteFile(this.deepDir);

		this.actual = this.deepDir.exists();
		assertFalse("", this.actual);

		this.actual = testFile5.exists();
		assertFalse("", this.actual);
		// --------------------------------

		this.actual = testFile4.exists();
		assertTrue("", this.actual);

		this.actual = this.testDir.exists();
		assertTrue("", this.actual);

		DeleteFileExtensions.deleteFile(this.testDir);

		this.actual = testFile4.exists();
		assertFalse("", this.actual);

		this.actual = this.testDir.exists();
		assertFalse("", this.actual);
		// --------------------------------

	}

	/**
	 * Test method for {@link de.alpharogroup.file.FileExtensions#download(java.net.URI)}.
	 */
	@Test
	public void testDownload()
	{

		final byte[] expected = { -84, -19, 0, 5, 116, 0, 7, 70, 111, 111, 32, 98, 97, 114 };

		final File destination = new File(this.testDir.getAbsoluteFile(), "testDownload.txt");

		WriteFileExtensions.storeByteArrayToFile(expected, destination);

		final byte[] compare = FileExtensions.download(destination.toURI());

		for (int i = 0; i < compare.length; i++)
		{
			this.actual = compare[i] == expected[i];
			assertTrue("", this.actual);
		}

	}

	@Test
	public void testFindFilesFileString() throws DirectoryAllreadyExistsException, IOException
	{
		final String test = "testFindFilesFileString.t*";

		final File testFile1 = new File(this.testDir, "testFindFilesFileString.txt");
		final File testFile2 = new File(this.testDir, "testFindFilesFileString.tft");
		final File testFile3 = new File(this.deepDir, "testFindFilesFileString.cvs");
		WriteFileExtensions.string2File(testFile1, "Its a beautifull day!!!");
		WriteFileExtensions.string2File(testFile2, "Its a beautifull evening!!!");
		WriteFileExtensions.string2File(testFile3, "Its a beautifull night!!!");
		final List<File> foundedFiles = FileSearchExtensions.findFiles(this.testDir, test);
		this.actual = foundedFiles != null;
		assertTrue(this.actual);
		this.actual = foundedFiles.size() == 2;
		assertTrue(this.actual);
	}

	@Test
	public void testFindFilesRecursive() throws DirectoryAllreadyExistsException, IOException
	{
		final String test = "testFindFilesRecursive.t*";
		final List<File> expectedFiles = new ArrayList<>();
		final File testFile1 = new File(this.testDir.getAbsoluteFile(),
			"testFindFilesRecursive.txt");
		expectedFiles.add(testFile1);
		final File testFile2 = new File(this.testDir.getAbsoluteFile(),
			"testFindFilesRecursive.tft");
		expectedFiles.add(testFile2);
		final File testFile3 = new File(this.deepDir, "testFindFilesRecursive.cvs");
		WriteFileExtensions.string2File(testFile1, "Its a beautifull day!!!");
		WriteFileExtensions.string2File(testFile2, "Its a beautifull evening!!!");
		WriteFileExtensions.string2File(testFile3, "Its a beautifull night!!!");
		List<File> foundedFiles = FileSearchExtensions.findFilesRecursive(this.testDir, test);
		this.actual = foundedFiles != null;
		assertTrue(this.actual);
		this.actual = foundedFiles.size() == 2;
		assertTrue(this.actual);
		for (final File expectedFile : expectedFiles)
		{
			this.actual = foundedFiles.contains(expectedFile);
			assertTrue(this.actual);
		}
		final String pattern = "*";
		final File testFile4 = new File(this.deepDir, "testFindFilesRecursive2.cvs");
		testFile4.createNewFile();

		foundedFiles = FileSearchExtensions.findFilesRecursive(this.testDir, pattern);

		this.actual = foundedFiles != null;
		assertTrue(this.actual);
		this.actual = foundedFiles.size() == 4;
		assertTrue(this.actual);
	}

	@Test
	public void testFindFilesStringStringArray()
	{
		final List<File> expected = new ArrayList<>();
		final File testFile1 = new File(this.testDir.getAbsoluteFile(),
			"testFindFilesStringStringArray1.txt");
		final File testFile2 = new File(this.testDir.getAbsoluteFile(),
			"testFindFilesStringStringArray2.tft");
		final File testFile3 = new File(this.testDir.getAbsoluteFile(),
			"testFindFilesStringStringArray3.txt");

		final File testFile4 = new File(this.deepDir.getAbsoluteFile(),
			"testFindFilesStringStringArray4.tft");
		final File testFile5 = new File(this.deepDir.getAbsoluteFile(),
			"testFindFilesStringStringArray5.cvs");

		final File testFile6 = new File(this.deepDir2.getAbsoluteFile(),
			"testFindFilesStringStringArray6.txt");
		final File testFile7 = new File(this.deepDir2.getAbsoluteFile(),
			"testFindFilesStringStringArray7.cvs");

		final File testFile8 = new File(this.deeperDir.getAbsoluteFile(),
			"testFindFilesStringStringArray8.txt");
		final File testFile9 = new File(this.deeperDir.getAbsoluteFile(),
			"testFindFilesStringStringArray9.cvs");

		WriteFileExtensions.string2File(testFile1, "Its a beautifull day!!!");
		WriteFileExtensions.string2File(testFile2, "Its a beautifull evening!!!");
		WriteFileExtensions.string2File(testFile3, "Its a beautifull night!!!");
		WriteFileExtensions.string2File(testFile4, "Its a beautifull morning!!!");
		WriteFileExtensions.string2File(testFile5, "She's a beautifull woman!!!");
		WriteFileExtensions.string2File(testFile6, "Its a beautifull street!!!");
		WriteFileExtensions.string2File(testFile7, "He's a beautifull man!!!");
		WriteFileExtensions.string2File(testFile8, "Its a beautifull city!!!");
		WriteFileExtensions.string2File(testFile9, "He's a beautifull boy!!!");
		expected.add(testFile1);
		expected.add(testFile3);
		expected.add(testFile6);
		expected.add(testFile8);
		final String[] txtExtension = { ".txt" };
		final List<File> compare = FileSearchExtensions.findFiles(this.testDir.getAbsolutePath(),
			txtExtension);
		this.actual = expected.size() == compare.size();
		assertTrue("", this.actual);
		for (final File file : compare)
		{
			final File currentFile = file;
			this.actual = expected.contains(currentFile);
			assertTrue("", this.actual);
		}
	}

	/**
	 * Test method for {@link de.alpharogroup.file.FileExtensions#getFilenamePrefix(File)}.
	 */
	@Test
	public void testGetFilenamePrefix()
	{
		final String filePrefix = "testGetFilenamePrefix";
		final String fileSuffix = ".txt";

		final File testFile1 = new File(this.testDir, filePrefix + fileSuffix);
		final String ap = testFile1.getAbsolutePath();
		final int ext_index = ap.lastIndexOf(".");
		final String fileNamePrefix = ap.substring(0, ext_index);
		final String expected = fileNamePrefix;
		final String compare = FileExtensions.getFilenamePrefix(testFile1);
		this.actual = expected.equals(compare);
		assertTrue("", this.actual);
	}

	/**
	 * Test method for {@link de.alpharogroup.file.FileExtensions#getFilenameSuffix(File)}.
	 */
	@Test
	public void testGetFilenameSuffix()
	{
		final String filePrefix = "testAppendSystemtimeToFilename";
		final String fileSuffix = ".txt";
		final String expected = fileSuffix;
		final File testFile1 = new File(this.testDir, filePrefix + fileSuffix);
		final String compare = FileExtensions.getFilenameSuffix(testFile1);
		this.actual = expected.equals(compare);
		assertTrue("", this.actual);
	}

	@Test
	public void testGetInputStream() throws IOException
	{

		final File source = new File(this.testDir.getAbsoluteFile(), "testGetInputStream.txt");

		final String inputString = "Its a beautifull day!!!";
		final String expected = inputString;
		WriteFileExtensions.writeStringToFile(source, inputString, null);

		try (final InputStream is = StreamExtensions.getInputStream(source))
		{
			final StringBuffer sb = new StringBuffer();
			int byt;
			while ((byt = is.read()) != -1)
			{
				sb.append((char)byt);
			}
			final String compare = sb.toString();
			this.actual = expected.equals(compare);
			assertTrue("", this.actual);
		}
	}

	@Test
	public void testGetOutputStream() throws IOException
	{

		final File source = new File(this.testDir.getAbsoluteFile(), "testGetOutputStream.txt");

		final File destination = new File(this.testDir.getAbsoluteFile(),
			"testGetOutputStream.tft");

		final String inputString = "Its a beautifull day!!!";
		final String expected = inputString;
		WriteFileExtensions.writeStringToFile(source, inputString, null);

		try (final OutputStream os = StreamExtensions.getOutputStream(destination, true))
		{
			os.write(inputString.getBytes());
		}

		final String compare = ReadFileExtensions.readFromFile(destination);
		this.actual = expected.equals(compare);
		assertTrue("", this.actual);
	}

	@Test
	public void testInputStream2String()
		throws DirectoryAllreadyExistsException, IOException, FileDoesNotExistException
	{

		final File inputFile = new File(this.testDir, "testInputStream2String.inp");
		inputFile.createNewFile();
		final String inputString = "Its a beautifull day!!!\n" + "This is the second line.\n"
			+ "This is the third line. ";
		WriteFileExtensions.string2File(inputFile, inputString);
		// --------------------------------
		final InputStream is = StreamExtensions.getInputStream(inputFile);
		final String compare = ReadFileExtensions.inputStream2String(is);

		this.actual = inputString.equals(compare);
		assertTrue("", this.actual);

	}

	@Test
	public void testIsZip()
	{
		final int length = FileConst.ZIP_EXTENSIONS.length;
		for (int i = 0; i < length; i++)
		{
			final File testIsZip = new File(this.testResources,
				"testIsZip" + FileConst.ZIP_EXTENSIONS[i]);
			this.actual = ZipExtensions.isZip(testIsZip.getName());
			assertTrue("The file " + testIsZip.getName() + " should be a zipfile.", this.actual);
		}
		this.actual = ZipExtensions.isZip(this.testResources.getName());
		assertFalse("The file " + this.testResources.getName() + " should not be a zipfile.",
			this.actual);
	}

	@Test
	public void testMatch()
	{
		final String filename = "testMatch.txt";
		final String txtExtension = ".txt";
		final String rtfExtension = ".rtf";
		final String cvsExtension = ".cvs";
		final String[] extensions = { txtExtension };

		this.actual = FileSearchExtensions.match(filename, extensions);
		assertTrue("", this.actual);

		final String[] otherExtensions = { rtfExtension, cvsExtension };
		this.actual = FileSearchExtensions.match(filename, otherExtensions);
		assertFalse("", this.actual);
	}

	@Test
	public void testMoveDir() throws DirectoryAllreadyExistsException
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
			final boolean created = CreateFileExtensions.newDirectory(srcDir);
			assertTrue("The directory " + srcDir.getAbsolutePath() + " should be created.",
				created);
			WriteFileExtensions.string2File(srcFile, "Its a beautifull day!!!");
		}
		System.err.println("-------------------------------------------------");
		System.err.println("srcFile.getAbsolutePath():" + srcFile.getAbsolutePath());
		System.err.println("-------------------------------------------------");
		// Test to move the dir.
		this.actual = RenameFileExtensions.moveFile(srcDir, destDir);
		assertTrue("Directory should be renamed.", this.actual);
		System.err.println("-------------------------------------------------");
		System.err.println("srcFile.getAbsolutePath():" + srcFile.getAbsolutePath());
		System.err.println("-------------------------------------------------");

	}

	@Test
	public void testMoveFile() throws DirectoryAllreadyExistsException
	{
		// Test to move a file....
		final String filePrefix1 = "testMoveFile";
		final String oldFileSuffix = ".txt";
		final File srcFile = new File(this.deepDir, filePrefix1 + oldFileSuffix);
		final File destDir = new File(this.deeperDir, filePrefix1 + oldFileSuffix);

		this.actual = RenameFileExtensions.moveFile(srcFile, destDir);
		assertFalse("File should not exist in this directory.", this.actual);
		WriteFileExtensions.string2File(srcFile, "Its a beautifull day!!!");
		this.actual = RenameFileExtensions.moveFile(srcFile, destDir);
		assertTrue("File should be renamed.", this.actual);
		this.actual = FileSearchExtensions.containsFile(this.deeperDir, destDir);
		assertTrue("The renamed file should exist in this directory.", this.actual);


	}

	@Test
	public void testOpenFileReader() throws IOException
	{
		final File testFile1 = new File(this.testDir, "testOpenFileReader.txt");
		final String inputString = "Its a beautifull day!!!";
		final String expected = inputString;
		final String ap = testFile1.getAbsolutePath();
		WriteFileExtensions.string2File(inputString, ap);

		final Reader reader = ReadFileExtensions.openFileReader(ap);
		final String compare = ReadFileExtensions.reader2String(reader);
		this.actual = expected.equals(compare);
		assertTrue("", this.actual);
	}

	@Test
	public void testReader2String() throws IOException, DirectoryAllreadyExistsException
	{

		final File inputFile = new File(this.testDir, "testReader2String.inp");
		inputFile.createNewFile();
		final String inputString = "Its a beautifull day!!!\n" + "This is the second line.\n"
			+ "This is the third line. ";
		WriteFileExtensions.string2File(inputFile, inputString);
		// --------------------------------
		final Reader reader = StreamExtensions.getReader(inputFile);
		final String compare = ReadFileExtensions.reader2String(reader);

		this.actual = inputString.equals(compare);
		assertTrue("", this.actual);

	}

	@Test
	public void testReadFromFile() throws IOException, DirectoryAllreadyExistsException
	{

		final File testFile1 = new File(this.testDir, "testReadFromFile.txt");
		final String inputString = "Its a beautifull day!!!";
		WriteFileExtensions.string2File(testFile1, inputString);
		// --------------------------------

		final String content = ReadFileExtensions.readFromFile(testFile1);
		this.actual = inputString.equals(content);
		assertTrue("", this.actual);

	}

	@Test
	public void testReadHeadLine() throws DirectoryAllreadyExistsException, IOException
	{

		final File inputFile = new File(this.testDir, "testReadHeadLine.inp");
		inputFile.createNewFile();

		final String inputString = "Its a beautifull day!!!\n This is the second line.\nThis is the third line. ";
		final String expected = "Its a beautifull day!!!";
		WriteFileExtensions.string2File(inputFile, inputString);
		// --------------------------------
		final String compare = ReadFileExtensions.readHeadLine(inputFile.getAbsolutePath());

		this.actual = expected.equals(compare);
		assertTrue("", this.actual);

	}

	@Test
	public void testReadLinesInList() throws IOException
	{
		final List<String> expected = new ArrayList<>();
		expected.add("test1");
		expected.add("test2");
		expected.add("test3");
		expected.add("bla");
		expected.add("fasel");
		expected.add("and");
		expected.add("so");
		expected.add("on");
		expected.add("test4");
		expected.add("test5");
		expected.add("test6");
		expected.add("foo");
		expected.add("bar");
		expected.add("sim");
		expected.add("sala");
		expected.add("bim");
		final File testFile = new File(this.testResources, "testReadLinesInList.lst");
		final List<String> testList = ReadFileExtensions.readLinesInList(testFile);
		this.actual = expected.equals(testList);
		assertTrue("", this.actual);
	}

	@Test
	public void testReadPropertiesFromFile() throws IOException
	{
		final File tp = new File(this.testResources, "testReadPropertiesFromFile.properties");
		final String ap = tp.getAbsolutePath();
		Properties compare = new Properties();
		final Properties properties = new Properties();
		properties.setProperty("testkey1", "testvalue1");
		properties.setProperty("testkey2", "testvalue2");
		properties.setProperty("testkey3", "testvalue3");
		WriteFileExtensions.writeProperties2File(ap, properties);
		compare = ReadFileExtensions.readPropertiesFromFile(ap);
		this.actual = properties.equals(compare);
		assertTrue(this.actual);
		// clean up
		DeleteFileExtensions.delete(tp);
	}

	@Test
	public void testReadSourceFileAndWriteDestFile() throws IOException
	{

		final File source = new File(this.testDir.getAbsoluteFile(),
			"testReadSourceFileAndWriteDestFileInput.txt");
		final File destination = new File(this.testDir.getAbsoluteFile(),
			"testReadSourceFileAndWriteDestFileOutput.tft");

		WriteFileExtensions.string2File(source, "Its a beautifull day!!!");
		WriteFileExtensions.string2File(destination, "");
		try
		{
			WriteFileExtensions.readSourceFileAndWriteDestFile(source.getAbsolutePath(),
				destination.getAbsolutePath());
		}
		catch (final Exception e)
		{
			this.actual = e instanceof FileNotFoundException;
			assertTrue("Exception should be of type FileNotFoundException.", this.actual);
		}

		final String inputString = "Its a beautifull day!!!";
		final String expected = inputString;
		WriteFileExtensions.string2File(source, inputString);

		WriteFileExtensions.readSourceFileAndWriteDestFile(source.getAbsolutePath(),
			destination.getAbsolutePath());

		final String compare = ReadFileExtensions.readFromFile(destination);
		this.actual = expected.equals(compare);
		assertTrue("", this.actual);

	}

	@Test
	public void testRenameFile()
	{
		final String filePrefix1 = "testRenameFileFileFile1";
		final String filePrefix2 = "testRenameFileFileFile2";
		final String oldFileSuffix = ".txt";
		final String newFileSuffix = ".rtf";
		final File testFile1 = new File(this.deepDir, filePrefix1 + oldFileSuffix);
		final File renamedFile1 = new File(this.deepDir, filePrefix2 + newFileSuffix);

		this.actual = RenameFileExtensions.renameFile(testFile1, renamedFile1, false);
		assertFalse("File should not exist in this directory.", this.actual);
		WriteFileExtensions.string2File(testFile1, "Its a beautifull day!!!");
		this.actual = RenameFileExtensions.renameFile(testFile1, renamedFile1, false);
		assertTrue("File should be renamed.", this.actual);
		this.actual = FileSearchExtensions.containsFile(this.deepDir, renamedFile1);
		assertTrue("The renamed file should exist in this directory.", this.actual);
	}

	@Test
	public void testRenameFileFileFile()
	{

		final String filePrefix1 = "testRenameFileFileFile1";
		final String filePrefix2 = "testRenameFileFileFile2";
		final String oldFileSuffix = ".txt";
		final String newFileSuffix = ".rtf";
		final File testFile1 = new File(this.deepDir, filePrefix1 + oldFileSuffix);
		final File renamedFile1 = new File(this.deepDir, filePrefix2 + newFileSuffix);

		this.actual = RenameFileExtensions.renameFile(testFile1, renamedFile1);
		assertFalse("File should not exist in this directory.", this.actual);
		WriteFileExtensions.string2File(testFile1, "Its a beautifull day!!!");
		this.actual = RenameFileExtensions.renameFile(testFile1, renamedFile1);
		assertTrue("File should be renamed.", this.actual);
		this.actual = FileSearchExtensions.containsFile(this.deepDir, renamedFile1);
		assertTrue("The renamed file should exist in this directory.", this.actual);

	}

	@Test
	public void testRenameFileFileString()
	{

		final String filePrefix1 = "testRenameFileFileString1";
		final String filePrefix2 = "testRenameFileFileString2";
		final String oldFileSuffix = ".txt";
		final String newFileSuffix = ".rtf";
		final File testFile1 = new File(this.deepDir, filePrefix1 + oldFileSuffix);
		final File renamedFile1 = new File(this.deepDir, filePrefix2 + newFileSuffix);

		WriteFileExtensions.string2File(testFile1, "Its a beautifull day!!!");
		this.actual = RenameFileExtensions.renameFile(testFile1, renamedFile1.getName());
		assertTrue("File should be renamed.", this.actual);
		this.actual = FileSearchExtensions.containsFile(this.deepDir, renamedFile1);
		assertTrue("The renamed file should exist in this directory.", this.actual);

	}

	@Test
	public void testRenameFileWithSystemtime()
	{
		// final String format = "HHmmssSSS";
		// final String filePrefix = "testRenameFileWithSystemtime";
		// final String fileSuffix = ".txt";
		// final File testFile1 = new File(this.testDir, filePrefix + fileSuffix);
		// final String inputString = "Its a beautifull day!!!";
		//
		// final String ap = testFile1.getAbsolutePath();
		// WriteFileUtils.string2File(inputString, ap);
		//
		// final Date before = new Date();
		//
		// final File compareFile = RenameFileUtils.renameFileWithSystemtime(testFile1);
		// final String newFilenameWithSystemtime = compareFile.getName();
		// final Date after = new Date();
		// final int start = newFilenameWithSystemtime.indexOf("_");
		// final int end = newFilenameWithSystemtime.indexOf(fileSuffix);
		// final String sysDateFromFile = newFilenameWithSystemtime.substring(start + 1, end);
		// final String sysTimeBefore = DateUtils.parseToString(before, format);
		// final String sysTimeAfter = DateUtils.parseToString(after, format);
		// final Date between = DateUtils.parseToDate(sysDateFromFile, format);
		// final Date beforeDate = DateUtils.parseToDate(sysTimeBefore, format);
		// final Date afterDate = DateUtils.parseToDate(sysTimeAfter, format);
		// this.actual = DateUtils.isBetween(beforeDate, afterDate, between);
		// assertTrue("", this.actual);
	}

	@Test
	public void testStoreByteArrayToFile()
	{
		// final byte[] expected = { -84, -19, 0, 5, 116, 0, 7, 70, 111, 111, 32, 98, 97, 114 };
		//
		// final File destination = new File(this.testDir.getAbsoluteFile(),
		// "testStoreByteArrayToFile.txt");
		//
		// WriteFileUtils.storeByteArrayToFile(expected, destination);
		//
		// final String compareString = ReadFileUtils.readFromFile(destination);
		// final byte[] compare = StringUtils.convertToBytearray(compareString.toCharArray());
		//
		// for (int i = 0; i < compare.length; i++) {
		// this.actual = compare[i] == expected[i];
		// assertTrue("", this.actual);
		// }
	}

	@Test
	public void testString2FileFileString() throws DirectoryAllreadyExistsException, IOException
	{

		final File testFile1 = new File(this.testDir, "testString2FileFileString.txt");
		final String inputString = "Its a beautifull day!!!";
		WriteFileExtensions.string2File(testFile1, inputString);
		// --------------------------------

		final String content = ReadFileExtensions.readFromFile(testFile1);
		this.actual = inputString.equals(content);
		assertTrue("", this.actual);

	}

	@Test
	public void testString2FileStringString() throws IOException
	{

		final File testFile1 = new File(this.testDir, "testString2FileStringString.txt");
		final String inputString = "Its a beautifull day!!!";
		WriteFileExtensions.string2File(inputString, testFile1.getAbsolutePath());
		// --------------------------------

		final String content = ReadFileExtensions.readFromFile(testFile1);
		this.actual = inputString.equals(content);
		assertTrue("", this.actual);

	}

	/**
	 * Test method for {@link FileExtensions}
	 */
	@Test(expectedExceptions = { BeanTestException.class, ObjectCreationException.class })
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(FileExtensions.class);
	}

	@Test
	public void testWrite2FileInputStreamOutputStreamBoolean()
		throws DirectoryAllreadyExistsException, IOException, FileDoesNotExistException
	{

		final File inputFile = new File(this.testDir,
			"testWrite2FileInputStreamOutputStreamBoolean.inp");
		inputFile.createNewFile();
		final File outputFile = new File(this.testDir,
			"testWrite2FileInputStreamOutputStreamBoolean.outp");
		outputFile.createNewFile();
		final String inputString = "Its a beautifull day!!!";
		WriteFileExtensions.string2File(inputFile, inputString);

		try (final InputStream is = StreamExtensions.getInputStream(inputFile);
			final OutputStream os = StreamExtensions.getOutputStream(outputFile);)
		{
			StreamExtensions.writeInputStreamToOutputStream(is, os);
		}

		final String content = ReadFileExtensions.readFromFile(outputFile);
		this.actual = inputString.equals(content);
		assertTrue("", this.actual);

	}

	@Test
	public void testWrite2FileReaderWriterBoolean()
		throws DirectoryAllreadyExistsException, IOException
	{

		final File inputFile = new File(this.testDir, "testWrite2FileReaderWriterBoolean.inp");
		inputFile.createNewFile();
		final File outputFile = new File(this.testDir, "testWrite2FileReaderWriterBoolean.outp");
		outputFile.createNewFile();
		final String inputString = "Its a beautifull day!!!";
		WriteFileExtensions.string2File(inputFile, inputString);
		// --------------------------------
		try (final Reader reader = StreamExtensions.getReader(inputFile);
			final Writer writer = StreamExtensions.getWriter(outputFile);)
		{
			WriteFileExtensions.write2File(reader, writer);
		}

		final String content = ReadFileExtensions.readFromFile(outputFile);
		this.actual = inputString.equals(content);
		assertTrue("", this.actual);

	}

	@Test
	public void testWrite2FileStringPrintWriterBoolean()
		throws DirectoryAllreadyExistsException, IOException
	{

		final File inputFile = new File(this.testDir, "testWrite2FileStringPrintWriterBoolean.inp");
		inputFile.createNewFile();
		final File outputFile = new File(this.testDir,
			"testWrite2FileStringPrintWriterBoolean.outp");
		outputFile.createNewFile();
		final String inputString = "Its a beautifull day!!!";
		WriteFileExtensions.string2File(inputFile, inputString);
		// --------------------------------
		try (final PrintWriter writer = (PrintWriter)StreamExtensions.getWriter(outputFile);)
		{
			final String path = inputFile.getAbsolutePath();
			WriteFileExtensions.write2File(path, writer);
		}

		final String content = ReadFileExtensions.readFromFile(outputFile);
		this.actual = inputString.equals(content);
		assertTrue("", this.actual);

	}

	@Test
	public void testWrite2FileStringString() throws DirectoryAllreadyExistsException, IOException
	{
		boolean created;
		final File inputFile = new File(this.testDir, "testWrite2FileStringString.inp");
		created = inputFile.createNewFile();
		if (!created)
		{
			Assert.fail("Fail to create inputFile.");
		}
		final File outputFile = new File(this.testDir, "testWrite2FileStringString.outp");
		outputFile.createNewFile();
		final String inputString = "Its a beautifull day!!!";
		WriteFileExtensions.string2File(inputFile, inputString);
		// --------------------------------
		WriteFileExtensions.write2File(inputFile.getAbsolutePath(), outputFile.getAbsolutePath());

		final String content = ReadFileExtensions.readFromFile(outputFile);
		this.actual = inputString.equals(content);
		assertTrue("", this.actual);

	}

	@Test
	public void testWrite2FileWithBuffer() throws DirectoryAllreadyExistsException, IOException
	{

		final File inputFile = new File(this.testDir, "testWrite2FileWithBuffer.inp");
		inputFile.createNewFile();
		final File outputFile = new File(this.testDir, "testWrite2FileWithBuffer.outp");
		outputFile.createNewFile();
		final String inputString = "Its a beautifull day!!!";
		WriteFileExtensions.string2File(inputFile, inputString);
		// --------------------------------
		WriteFileExtensions.write2FileWithBuffer(inputFile.getAbsolutePath(),
			outputFile.getAbsolutePath());

		final String content = ReadFileExtensions.readFromFile(outputFile);
		this.actual = inputString.equals(content);
		assertTrue("", this.actual);

	}

	@Test
	public void testWriteByteArrayToFileFileByteArray() throws IOException
	{
		final byte[] expected = { -84, -19, 0, 5, 116, 0, 7, 70, 111, 111, 32, 98, 97, 114 };

		final File destination = new File(this.testDir.getAbsoluteFile(),
			"testStoreByteArrayToFile.txt");

		WriteFileExtensions.writeByteArrayToFile(destination, expected);

		final byte[] compare = ReadFileExtensions.readFileToBytearray(destination);

		for (int i = 0; i < compare.length; i++)
		{
			this.actual = compare[i] == expected[i];
			assertTrue("", this.actual);
		}
	}

	@Test
	public void testWriteByteArrayToFileStringByteArray() throws IOException
	{
		final byte[] expected = { -84, -19, 0, 5, 116, 0, 7, 70, 111, 111, 32, 98, 97, 114 };

		final File destination = new File(this.testDir.getAbsoluteFile(),
			"testStoreByteArrayToFile.txt");

		WriteFileExtensions.writeByteArrayToFile(destination.getAbsolutePath(), expected);

		final byte[] compare = ReadFileExtensions.readFileToBytearray(destination);

		for (int i = 0; i < compare.length; i++)
		{
			this.actual = compare[i] == expected[i];
			assertTrue("", this.actual);
		}
	}

	@Test
	public void testWriteLinesToFileCollectionFile() throws IOException
	{

		final List<String> expected = new ArrayList<>();
		expected.add("test1");
		expected.add("test2");
		expected.add("test3");
		expected.add("bla");
		expected.add("fasel");
		expected.add("and");
		expected.add("so");
		expected.add("on");
		expected.add("test4");
		expected.add("test5");
		expected.add("test6");
		expected.add("foo");
		expected.add("bar");
		expected.add("sim");
		expected.add("sala");
		expected.add("bim");
		final File testFile = new File(this.testResources, "testWriteLinesToFile.lst");
		WriteFileExtensions.writeLinesToFile(expected, testFile);
		final List<String> testList = ReadFileExtensions.readLinesInList(testFile);
		this.actual = expected.equals(testList);
		assertTrue("", this.actual);
	}

	@Test
	public void testWriteLinesToFileFileListString() throws IOException
	{
		final List<String> expected = new ArrayList<>();
		expected.add("test1");
		expected.add("test2");
		expected.add("test3");
		expected.add("bla");
		expected.add("fasel");
		expected.add("and");
		expected.add("so");
		expected.add("on");
		expected.add("test4");
		expected.add("test5");
		expected.add("test6");
		expected.add("foo");
		expected.add("bar");
		expected.add("sim");
		expected.add("sala");
		expected.add("bim");
		final File testFile = new File(this.testResources, "testWriteLinesToFile.lst");
		WriteFileExtensions.writeLinesToFile(testFile, expected, null);
		final List<String> testList = ReadFileExtensions.readLinesInList(testFile);
		final boolean result = expected.equals(testList);
		assertTrue("", result);
	}

	@Test
	public void testWriteProperties2File() throws IOException
	{
		final File tp = new File(this.testResources, "testWriteProperties2File.properties");
		final String ap = tp.getAbsolutePath();

		final Properties properties = new Properties();
		properties.setProperty("testkey1", "testvalue1");
		properties.setProperty("testkey2", "testvalue2");
		properties.setProperty("testkey3", "testvalue3");
		WriteFileExtensions.writeProperties2File(ap, properties);
		final Properties compare = ReadFileExtensions.readPropertiesFromFile(ap);
		this.actual = properties.equals(compare);
		assertTrue(this.actual);
		DeleteFileExtensions.delete(tp);
	}

	@Test
	public void testWriteStringToFile() throws IOException
	{

		final File source = new File(this.testDir.getAbsoluteFile(), "testWriteStringToFile.txt");

		final String inputString = "Its a beautifull day!!!";
		final String expected = inputString;
		WriteFileExtensions.writeStringToFile(source, inputString, null);

		final String compare = ReadFileExtensions.readFromFile(source);
		this.actual = expected.equals(compare);
		assertTrue("", this.actual);
	}

}
