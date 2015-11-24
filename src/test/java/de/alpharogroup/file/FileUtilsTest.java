/**
 * The MIT License
 *
 * Copyright (C) 2007 Asterios Raptis
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

import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.alpharogroup.file.compare.CompareFileUtils;
import de.alpharogroup.file.copy.CopyFileUtils;
import de.alpharogroup.file.create.CreateFileUtils;
import de.alpharogroup.file.delete.DeleteFileUtils;
import de.alpharogroup.file.exceptions.DirectoryAllreadyExistsException;
import de.alpharogroup.file.exceptions.FileDoesNotExistException;
import de.alpharogroup.file.exceptions.FileIsADirectoryException;
import de.alpharogroup.file.exceptions.FileIsNotADirectoryException;
import de.alpharogroup.file.exceptions.FileNotRenamedException;
import de.alpharogroup.file.read.ReadFileUtils;
import de.alpharogroup.file.rename.RenameFileUtils;
import de.alpharogroup.file.search.FileSearchUtils;
import de.alpharogroup.file.write.WriteFileUtils;
import de.alpharogroup.file.zip.ZipUtils;
import de.alpharogroup.io.SerializedObjectUtils;
import de.alpharogroup.io.StreamUtils;
import de.alpharogroup.string.StringUtils;

/**
 * Test class for the class FileUtils.
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public class FileUtilsTest extends FileTestCase
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
		WriteFileUtils.string2File(inputString, ap);

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
		// this.result = DateUtils.isBetween(beforeDate, afterDate, between);
		// assertTrue("", this.result);
	}

	@Test
	public void testChangeAllFilenameSuffixFileStringString() throws IOException,
		FileDoesNotExistException
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

		List<File> notDeletedFiles = RenameFileUtils.changeAllFilenameSuffix(this.deepDir,
			oldFileSuffix, newFileSuffix);
		this.result = null == notDeletedFiles;
		AssertJUnit.assertTrue("", this.result);
		// ----------------------------------------------------------------
		for (final File file : filesWithOldSuffixes)
		{
			final File currentFile = file;
			currentFile.createNewFile();
		}
		notDeletedFiles = RenameFileUtils.changeAllFilenameSuffix(this.deepDir, oldFileSuffix,
			newFileSuffix);
		this.result = null == notDeletedFiles;
		AssertJUnit.assertTrue("", this.result);

		for (final File file : filesWithNewSuffixes)
		{
			final File currentFile = file;
			this.result = FileSearchUtils.containsFileRecursive(this.deepDir, currentFile);
			AssertJUnit.assertTrue("", this.result);
		}
	}

	@Test
	public void testChangeAllFilenameSuffixFileStringStringBoolean() throws IOException,
		FileDoesNotExistException
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

		List<File> notDeletedFiles = RenameFileUtils.changeAllFilenameSuffix(this.deepDir,
			oldFileSuffix, newFileSuffix, true);
		this.result = null == notDeletedFiles;
		AssertJUnit.assertTrue("", this.result);
		// ----------------------------------------------------------------
		for (final File file : filesWithOldSuffixes)
		{
			final File currentFile = file;
			currentFile.createNewFile();
		}
		notDeletedFiles = RenameFileUtils.changeAllFilenameSuffix(this.deepDir, oldFileSuffix,
			newFileSuffix, true);

		for (final File file : filesWithNewSuffixes)
		{
			final File currentFile = file;
			this.result = FileSearchUtils.containsFileRecursive(this.deepDir, currentFile);
			AssertJUnit.assertTrue("", this.result);
		}
	}

	@Test
	public void testChangeFilenameSuffixFileString() throws FileNotRenamedException, IOException,
		FileDoesNotExistException
	{

		final String filePrefix = "testChangeFilenameSuffixFileString";
		final String oldFileSuffix = ".txt";
		final String newFileSuffix = ".rtf";
		final File testFile1 = new File(this.deepDir, filePrefix + oldFileSuffix);
		final File fileWithNewSuffix = new File(this.deepDir, filePrefix + newFileSuffix);
		try
		{
			this.result = RenameFileUtils.changeFilenameSuffix(testFile1, newFileSuffix);
		}
		catch (final Exception e)
		{
			this.result = e instanceof FileDoesNotExistException;
			AssertJUnit.assertTrue("", this.result);
		}

		testFile1.createNewFile();
		this.result = RenameFileUtils.changeFilenameSuffix(testFile1, newFileSuffix);
		AssertJUnit.assertTrue("", this.result);

		this.result = FileSearchUtils.containsFile(this.deepDir, fileWithNewSuffix);
		AssertJUnit.assertTrue("", this.result);

	}

	@Test
	public void testChangeFilenameSuffixFileStringBoolean() throws IOException,
		FileNotRenamedException, FileDoesNotExistException
	{

		final String filePrefix = "testChangeFilenameSuffixFileStringBoolean";
		final String oldFileSuffix = ".txt";
		final String newFileSuffix = ".rtf";
		final File testFile1 = new File(this.deepDir, filePrefix + oldFileSuffix);
		final File fileWithNewSuffix = new File(this.deepDir, filePrefix + newFileSuffix);
		try
		{
			this.result = RenameFileUtils.changeFilenameSuffix(testFile1, newFileSuffix, true);
		}
		catch (final Exception e)
		{
			this.result = e instanceof FileDoesNotExistException;
			AssertJUnit.assertTrue("", this.result);
		}

		testFile1.createNewFile();
		this.result = RenameFileUtils.changeFilenameSuffix(testFile1, newFileSuffix, true);
		AssertJUnit.assertTrue("", this.result);

		this.result = FileSearchUtils.containsFile(this.deepDir, fileWithNewSuffix);
		AssertJUnit.assertTrue("", this.result);
	}

	@Test
	public void testCheckFile() throws DirectoryAllreadyExistsException, IOException
	{
		if (this.testDir.exists())
		{
			DeleteFileUtils.delete(this.testDir);
		}
		this.testDir = new File(this.testResources, "testDir");
		Exception ex = DeleteFileUtils.checkFile(this.testDir);
		this.result = ex != null;
		AssertJUnit.assertTrue("", this.result);
		this.result = ex instanceof FileDoesNotExistException;
		AssertJUnit.assertTrue("", this.result);
		if (!this.testDir.exists())
		{
			final boolean created = CreateFileUtils.newDirectory(this.testDir);
			AssertJUnit.assertTrue("The directory should be created.", created);
		}
		ex = DeleteFileUtils.checkFile(this.testDir);
		this.result = ex == null;
		AssertJUnit.assertTrue("", this.result);

		final File testFile1 = new File(this.testDir, "testCheckFile.txt");
		WriteFileUtils.string2File(testFile1, "Its a beautifull day!!!");
		ex = DeleteFileUtils.checkFile(testFile1);
		this.result = ex != null;
		AssertJUnit.assertTrue("", this.result);
		this.result = ex instanceof FileIsNotADirectoryException;
		AssertJUnit.assertTrue("", this.result);

		final File testFile2 = new File("a");
		ex = DeleteFileUtils.checkFile(testFile2);
		this.result = ex != null;
		AssertJUnit.assertTrue("", this.result);

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

		this.result = CompareFileUtils.compareFiles(source, compare, false);
		AssertJUnit.assertTrue("File should be equal cause they dont exist.", this.result);
		compare = new File(this.deepDir, filePrefix2 + newFileSuffix);
		this.result = CompareFileUtils.compareFiles(source, compare, false);
		AssertJUnit.assertFalse("File should not be equal.", this.result);
		WriteFileUtils.string2File(source, "Its a beautifull day!!!");
		WriteFileUtils.string2File(compare, "Its a beautifull day!!!");
		this.result = CompareFileUtils.compareFiles(source, compare, false);
		AssertJUnit.assertTrue("File should be equal.", this.result);
		this.result = CompareFileUtils.compareFiles(source, compare, true);
		AssertJUnit.assertTrue("File should be equal.", this.result);
		WriteFileUtils.string2File(compare, "Its a beautifull evening!!!");
		this.result = CompareFileUtils.compareFiles(source, compare, true);
		AssertJUnit.assertFalse("File should not be equal.", this.result);
		WriteFileUtils.string2File(compare, "Its a beautifull boy!!!");
		this.result = CompareFileUtils.compareFiles(source, compare, true);
		AssertJUnit.assertFalse("File should not be equal.", this.result);

	}

	@Test
	public void testContainsFileFileFile() throws DirectoryAllreadyExistsException, IOException
	{
		final File testFile = new File(this.testDir, "beautifull.txt");
		WriteFileUtils.string2File(testFile, "Its a beautifull day!!!");
		boolean contains = FileSearchUtils.containsFile(new File("."), testFile);
		AssertJUnit.assertFalse("File should not exist in this directory.", contains);
		contains = FileSearchUtils.containsFile(this.testDir, testFile);
		AssertJUnit.assertTrue("File should not exist in this directory.", contains);
	}

	@Test
	public void testContainsFileFileString()
	{
		final File testFile = new File(this.testDir, "beautifull.txt");
		WriteFileUtils.string2File(testFile, "Its a beautifull day!!!");
		boolean contains = FileSearchUtils.containsFile(new File("."), testFile);
		AssertJUnit.assertFalse("File should not exist in this directory.", contains);
		final String filename = testFile.getName();
		contains = FileSearchUtils.containsFile(this.testDir, filename);
		AssertJUnit.assertTrue("File should not exist in this directory.", contains);
	}

	@Test
	public void testContainsFileRecursive()
	{

		final File testFile = new File(this.testDir.getAbsoluteFile(),
			"testContainsFileRecursives.txt");
		WriteFileUtils.string2File(testFile, "Its a beautifull day!!!");

		final File testFile3 = new File(this.deepDir.getAbsoluteFile(),
			"testContainsFileRecursives.cvs");
		WriteFileUtils.string2File(testFile3, "Its a beautifull evening!!!");
		final File currentDir = new File(".").getAbsoluteFile();
		boolean contains = FileSearchUtils.containsFileRecursive(currentDir.getAbsoluteFile(),
			testFile);
		AssertJUnit.assertFalse("File should not exist in this directory.", contains);
		contains = FileSearchUtils.containsFileRecursive(this.testDir, testFile);
		AssertJUnit.assertTrue("File should not exist in this directory.", contains);
		this.result = FileSearchUtils.containsFileRecursive(this.testDir, testFile3);
		AssertJUnit.assertTrue("", this.result);
	}

	@Test
	public void testConvert2ByteArray() throws IOException
	{
		final byte[] expected = { -84, -19, 0, 5, 116, 0, 7, 70, 111, 111, 32, 98, 97, 114 };
		final String testString = "Foo bar";
		byte[] compare = null;
		compare = SerializedObjectUtils.toByteArray(testString);
		for (int i = 0; i < compare.length; i++)
		{
			this.result = expected[i] == compare[i];
			AssertJUnit.assertTrue("", this.result);
		}
	}

	@Test
	public void testConvert2Object() throws ClassNotFoundException, IOException
	{
		final byte[] testBytearray = { -84, -19, 0, 5, 116, 0, 7, 70, 111, 111, 32, 98, 97, 114 };
		final String expected = "Foo bar";
		final Object obj = SerializedObjectUtils.toObject(testBytearray);
		final String compare = (String)obj;
		this.result = expected.equals(compare);
		AssertJUnit.assertTrue("", this.result);

	}

	@Test
	public void testCopyFile() throws IOException, FileIsADirectoryException
	{
		final File source = new File(this.testDir.getAbsoluteFile(), "testCopyFileInput.txt");
		final File destination = new File(this.testDir.getAbsoluteFile(), "testCopyFileOutput.tft");
		try
		{
			this.result = CopyFileUtils.copyFile(source, destination);
			AssertJUnit.assertFalse("", this.result);
		}
		catch (final Exception fnfe)
		{
			this.result = fnfe instanceof FileNotFoundException;
			AssertJUnit.assertTrue("", this.result);
		}
		final String inputString = "Its a beautifull day!!!";
		final String expected = inputString;
		WriteFileUtils.string2File(source, inputString);

		this.result = CopyFileUtils.copyFile(source, destination);
		AssertJUnit.assertTrue("", this.result);
		final String compare = ReadFileUtils.readFromFile(destination);
		this.result = expected.equals(compare);
		AssertJUnit.assertTrue("", this.result);

	}

	@Test
	public void testCreateDirectory() throws DirectoryAllreadyExistsException, IOException
	{
		final File testing = new File(this.testResources, "testCreateDirectory");
		if (testing.exists())
		{
			DeleteFileUtils.delete(testing);
		}
		final boolean created = CreateFileUtils.newDirectory(testing);
		AssertJUnit.assertTrue("The directory should be created.", created);
		this.result = testing.isDirectory();
		AssertJUnit.assertTrue("Created File should be a directory.", this.result);
		if (testing.exists())
		{
			DeleteFileUtils.delete(testing);
		}
	}

	@Test
	public void testCreateFile() throws IOException
	{
		final File source = new File(this.testDir.getAbsoluteFile(), "testGetOutputStream.txt");

		CreateFileUtils.newFile(source);
		this.result = source.exists();
		AssertJUnit.assertTrue("", this.result);

	}

	@Test
	public void testDeleleFiles() throws IOException, DirectoryAllreadyExistsException
	{

		final File testFile1 = new File(this.testDir, "testDeleleFiles1.txt");
		final File testFile2 = new File(this.testDir, "testDeleleFiles2.txt");
		final File testFile3 = new File(this.deepDir, "testDeleleFiles3.txt");
		final File testFile4 = new File(this.testDir, "testDeleleFiles4.tft");
		final File testFile5 = new File(this.deepDir, "testDeleleFiles5.cvs");
		WriteFileUtils.string2File(testFile1, "Its a beautifull day!!!");
		WriteFileUtils.string2File(testFile2, "Its a beautifull evening!!!");
		WriteFileUtils.string2File(testFile3, "Its a beautifull night!!!");
		WriteFileUtils.string2File(testFile4, "Its a beautifull morning!!!");
		WriteFileUtils.string2File(testFile5, "She's a beautifull woman!!!");
		// --------------------------------
		this.result = this.deepDir.exists();
		AssertJUnit.assertTrue("", this.result);

		this.result = testFile1.exists();
		AssertJUnit.assertTrue("", this.result);

		this.result = testFile2.exists();
		AssertJUnit.assertTrue("", this.result);

		this.result = testFile3.exists();
		AssertJUnit.assertTrue("", this.result);

		this.result = testFile4.exists();
		AssertJUnit.assertTrue("", this.result);

		this.result = testFile5.exists();
		AssertJUnit.assertTrue("", this.result);

		this.result = this.testDir.exists();
		AssertJUnit.assertTrue("", this.result);

		DeleteFileUtils.deleteFiles(this.testDir);

		this.result = this.deepDir.exists();
		AssertJUnit.assertFalse("", this.result);

		this.result = testFile1.exists();
		AssertJUnit.assertFalse("", this.result);

		this.result = testFile2.exists();
		AssertJUnit.assertFalse("", this.result);

		this.result = testFile3.exists();
		AssertJUnit.assertFalse("", this.result);

		this.result = testFile4.exists();
		AssertJUnit.assertFalse("", this.result);

		this.result = testFile5.exists();
		AssertJUnit.assertFalse("", this.result);

	}

	@Test
	public void testDelete() throws DirectoryAllreadyExistsException, IOException
	{

		final File testFile1 = new File(this.testDir, "testDelete1.txt");
		final File testFile2 = new File(this.testDir, "testDelete2.txt");
		final File testFile3 = new File(this.deepDir, "testDelete3.txt");
		final File testFile4 = new File(this.testDir, "testDelete4.tft");
		final File testFile5 = new File(this.deepDir, "testDelete5.cvs");
		WriteFileUtils.string2File(testFile1, "Its a beautifull day!!!");
		WriteFileUtils.string2File(testFile2, "Its a beautifull evening!!!");
		WriteFileUtils.string2File(testFile3, "Its a beautifull night!!!");
		WriteFileUtils.string2File(testFile4, "Its a beautifull morning!!!");
		WriteFileUtils.string2File(testFile5, "She's a beautifull woman!!!");
		// --------------------------------
		this.result = testFile1.exists();
		AssertJUnit.assertTrue("", this.result);

		DeleteFileUtils.delete(testFile1);

		this.result = testFile1.exists();
		AssertJUnit.assertFalse("", this.result);
		// --------------------------------
		this.result = testFile3.exists();
		AssertJUnit.assertTrue("", this.result);

		DeleteFileUtils.delete(testFile3);

		this.result = testFile3.exists();
		AssertJUnit.assertFalse("", this.result);
		// --------------------------------

		this.result = testFile5.exists();
		AssertJUnit.assertTrue("", this.result);

		this.result = this.deepDir.exists();
		AssertJUnit.assertTrue("", this.result);

		DeleteFileUtils.delete(this.deepDir);

		this.result = this.deepDir.exists();
		AssertJUnit.assertFalse("", this.result);

		this.result = testFile5.exists();
		AssertJUnit.assertFalse("", this.result);
		// --------------------------------

		this.result = testFile4.exists();
		AssertJUnit.assertTrue("", this.result);

		this.result = this.testDir.exists();
		AssertJUnit.assertTrue("", this.result);

		DeleteFileUtils.delete(this.testDir);

		this.result = testFile4.exists();
		AssertJUnit.assertFalse("", this.result);

		this.result = this.testDir.exists();
		AssertJUnit.assertFalse("", this.result);
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
		WriteFileUtils.string2File(testFile1, "Its a beautifull day!!!");
		WriteFileUtils.string2File(testFile2, "Its a beautifull evening!!!");
		WriteFileUtils.string2File(testFile3, "Its a beautifull night!!!");
		WriteFileUtils.string2File(testFile4, "Its a beautifull morning!!!");
		WriteFileUtils.string2File(testFile5, "She's a beautifull woman!!!");
		// --------------------------------
		this.result = testFile1.exists();
		AssertJUnit.assertTrue("", this.result);

		this.result = testFile3.exists();
		AssertJUnit.assertTrue("", this.result);

		this.result = testFile5.exists();
		AssertJUnit.assertTrue("", this.result);

		this.result = this.deepDir.exists();
		AssertJUnit.assertTrue("", this.result);

		this.result = testFile4.exists();
		AssertJUnit.assertTrue("", this.result);

		this.result = this.testDir.exists();
		AssertJUnit.assertTrue("", this.result);

		DeleteFileUtils.deleteAllFiles(this.testDir);

		this.result = this.deepDir.exists();
		AssertJUnit.assertFalse("", this.result);

		this.result = this.testDir.exists();
		AssertJUnit.assertFalse("", this.result);

		this.result = testFile1.exists();
		AssertJUnit.assertFalse("", this.result);

		this.result = testFile2.exists();
		AssertJUnit.assertFalse("", this.result);

		this.result = testFile3.exists();
		AssertJUnit.assertFalse("", this.result);

		this.result = testFile4.exists();
		AssertJUnit.assertFalse("", this.result);

		this.result = testFile5.exists();
		AssertJUnit.assertFalse("", this.result);

	}

	@Test
	public void testDeleteAllFilesWithSuffix() throws DirectoryAllreadyExistsException, IOException
	{

		final File testFile1 = new File(this.testDir, "testDeleteAllFilesWithSuffix1.txt");
		final File testFile2 = new File(this.testDir, "testDeleteAllFilesWithSuffix2.txt");
		final File testFile3 = new File(this.deepDir, "testDeleteAllFilesWithSuffix3.txt");
		final File testFile4 = new File(this.testDir, "testDeleteAllFilesWithSuffix4.tft");
		final File testFile5 = new File(this.deepDir, "testDeleteAllFilesWithSuffix5.cvs");
		WriteFileUtils.string2File(testFile1, "Its a beautifull day!!!");
		WriteFileUtils.string2File(testFile2, "Its a beautifull evening!!!");
		WriteFileUtils.string2File(testFile3, "Its a beautifull night!!!");
		WriteFileUtils.string2File(testFile4, "Its a beautifull morning!!!");
		WriteFileUtils.string2File(testFile5, "She's a beautifull woman!!!");

		DeleteFileUtils.deleteAllFilesWithSuffix(this.testDir, ".txt");

		this.result = testFile1.exists();
		AssertJUnit.assertFalse("", this.result);
		this.result = testFile2.exists();
		AssertJUnit.assertFalse("", this.result);
		this.result = testFile3.exists();
		AssertJUnit.assertFalse("", this.result);
		this.result = testFile4.exists();
		AssertJUnit.assertTrue("", this.result);
		this.result = testFile5.exists();
		AssertJUnit.assertTrue("", this.result);

	}

	@Test
	public void testDeleteFile() throws DirectoryAllreadyExistsException, IOException
	{

		final File testFile1 = new File(this.testDir, "testDeleteFile1.txt");
		final File testFile2 = new File(this.testDir, "testDeleteFile2.txt");
		final File testFile3 = new File(this.deepDir, "testDeleteFile3.txt");
		final File testFile4 = new File(this.testDir, "testDeleteFile4.tft");
		final File testFile5 = new File(this.deepDir, "testDeleteFile5.cvs");
		WriteFileUtils.string2File(testFile1, "Its a beautifull day!!!");
		WriteFileUtils.string2File(testFile2, "Its a beautifull evening!!!");
		WriteFileUtils.string2File(testFile3, "Its a beautifull night!!!");
		WriteFileUtils.string2File(testFile4, "Its a beautifull morning!!!");
		WriteFileUtils.string2File(testFile5, "She's a beautifull woman!!!");
		// --------------------------------
		this.result = testFile1.exists();
		AssertJUnit.assertTrue("", this.result);

		DeleteFileUtils.deleteFile(testFile1);

		this.result = testFile1.exists();
		AssertJUnit.assertFalse("", this.result);
		// --------------------------------
		this.result = testFile3.exists();
		AssertJUnit.assertTrue("", this.result);

		DeleteFileUtils.deleteFile(testFile3);

		this.result = testFile3.exists();
		AssertJUnit.assertFalse("", this.result);
		// --------------------------------

		this.result = testFile5.exists();
		AssertJUnit.assertTrue("", this.result);

		this.result = this.deepDir.exists();
		AssertJUnit.assertTrue("", this.result);

		DeleteFileUtils.deleteFile(this.deepDir);

		this.result = this.deepDir.exists();
		AssertJUnit.assertFalse("", this.result);

		this.result = testFile5.exists();
		AssertJUnit.assertFalse("", this.result);
		// --------------------------------

		this.result = testFile4.exists();
		AssertJUnit.assertTrue("", this.result);

		this.result = this.testDir.exists();
		AssertJUnit.assertTrue("", this.result);

		DeleteFileUtils.deleteFile(this.testDir);

		this.result = testFile4.exists();
		AssertJUnit.assertFalse("", this.result);

		this.result = this.testDir.exists();
		AssertJUnit.assertFalse("", this.result);
		// --------------------------------

	}

	/**
	 * Test method for {@link de.alpharogroup.file.FileUtils#download(java.net.URI)}.
	 */
	@Test
	public void testDownload()
	{

		final byte[] expected = { -84, -19, 0, 5, 116, 0, 7, 70, 111, 111, 32, 98, 97, 114 };

		final File destination = new File(this.testDir.getAbsoluteFile(), "testDownload.txt");

		WriteFileUtils.storeByteArrayToFile(expected, destination);

		final byte[] compare = FileUtils.download(destination.toURI());

		for (int i = 0; i < compare.length; i++)
		{
			this.result = compare[i] == expected[i];
			AssertJUnit.assertTrue("", this.result);
		}

	}

	@Test
	public void testFindFilesFileString() throws DirectoryAllreadyExistsException, IOException
	{
		final String test = "testFindFilesFileString.t*";

		final File testFile1 = new File(this.testDir, "testFindFilesFileString.txt");
		final File testFile2 = new File(this.testDir, "testFindFilesFileString.tft");
		final File testFile3 = new File(this.deepDir, "testFindFilesFileString.cvs");
		WriteFileUtils.string2File(testFile1, "Its a beautifull day!!!");
		WriteFileUtils.string2File(testFile2, "Its a beautifull evening!!!");
		WriteFileUtils.string2File(testFile3, "Its a beautifull night!!!");
		final List<File> foundedFiles = FileSearchUtils.findFiles(this.testDir, test);
		this.result = foundedFiles != null;
		AssertJUnit.assertTrue(this.result);
		this.result = foundedFiles.size() == 2;
		AssertJUnit.assertTrue(this.result);
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
		WriteFileUtils.string2File(testFile1, "Its a beautifull day!!!");
		WriteFileUtils.string2File(testFile2, "Its a beautifull evening!!!");
		WriteFileUtils.string2File(testFile3, "Its a beautifull night!!!");
		List<File> foundedFiles = FileSearchUtils.findFilesRecursive(this.testDir, test);
		this.result = foundedFiles != null;
		AssertJUnit.assertTrue(this.result);
		this.result = foundedFiles.size() == 2;
		AssertJUnit.assertTrue(this.result);
		for (final File expectedFile : expectedFiles)
		{
			this.result = foundedFiles.contains(expectedFile);
			AssertJUnit.assertTrue(this.result);
		}
		final String pattern = "*";
		final File testFile4 = new File(this.deepDir, "testFindFilesRecursive2.cvs");
		testFile4.createNewFile();

		foundedFiles = FileSearchUtils.findFilesRecursive(this.testDir, pattern);

		this.result = foundedFiles != null;
		AssertJUnit.assertTrue(this.result);
		this.result = foundedFiles.size() == 4;
		AssertJUnit.assertTrue(this.result);
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

		WriteFileUtils.string2File(testFile1, "Its a beautifull day!!!");
		WriteFileUtils.string2File(testFile2, "Its a beautifull evening!!!");
		WriteFileUtils.string2File(testFile3, "Its a beautifull night!!!");
		WriteFileUtils.string2File(testFile4, "Its a beautifull morning!!!");
		WriteFileUtils.string2File(testFile5, "She's a beautifull woman!!!");
		WriteFileUtils.string2File(testFile6, "Its a beautifull street!!!");
		WriteFileUtils.string2File(testFile7, "He's a beautifull man!!!");
		WriteFileUtils.string2File(testFile8, "Its a beautifull city!!!");
		WriteFileUtils.string2File(testFile9, "He's a beautifull boy!!!");
		expected.add(testFile1);
		expected.add(testFile3);
		expected.add(testFile6);
		expected.add(testFile8);
		final String[] txtExtension = { ".txt" };
		final List<File> compare = FileSearchUtils.findFiles(this.testDir.getAbsolutePath(),
			txtExtension);
		this.result = expected.size() == compare.size();
		AssertJUnit.assertTrue("", this.result);
		for (final File file : compare)
		{
			final File currentFile = file;
			this.result = expected.contains(currentFile);
			AssertJUnit.assertTrue("", this.result);
		}
	}

	/**
	 * Test method for {@link de.alpharogroup.file.FileUtils#getFilenamePrefix(File)}.
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
		final String compare = FileUtils.getFilenamePrefix(testFile1);
		this.result = expected.equals(compare);
		AssertJUnit.assertTrue("", this.result);
	}

	/**
	 * Test method for {@link de.alpharogroup.file.FileUtils#getFilenameSuffix(File)}.
	 */
	@Test
	public void testGetFilenameSuffix()
	{
		final String filePrefix = "testAppendSystemtimeToFilename";
		final String fileSuffix = ".txt";
		final String expected = fileSuffix;
		final File testFile1 = new File(this.testDir, filePrefix + fileSuffix);
		final String compare = FileUtils.getFilenameSuffix(testFile1);
		this.result = expected.equals(compare);
		AssertJUnit.assertTrue("", this.result);
	}

	@Test
	public void testGetInputStream() throws IOException
	{

		final File source = new File(this.testDir.getAbsoluteFile(), "testGetInputStream.txt");

		final String inputString = "Its a beautifull day!!!";
		final String expected = inputString;
		WriteFileUtils.writeStringToFile(source, inputString, null);

		final InputStream is = StreamUtils.getInputStream(source);
		final StringBuffer sb = new StringBuffer();
		int byt;
		while ((byt = is.read()) != -1)
		{
			sb.append((char)byt);
		}
		StreamUtils.closeInputStream(is);
		final String compare = sb.toString();
		this.result = expected.equals(compare);
		AssertJUnit.assertTrue("", this.result);

	}

	@Test
	public void testGetOutputStream() throws IOException
	{

		final File source = new File(this.testDir.getAbsoluteFile(), "testGetOutputStream.txt");

		final File destination = new File(this.testDir.getAbsoluteFile(), "testGetOutputStream.tft");

		final String inputString = "Its a beautifull day!!!";
		final String expected = inputString;
		WriteFileUtils.writeStringToFile(source, inputString, null);

		final OutputStream os = StreamUtils.getOutputStream(destination, true);
		os.write(inputString.getBytes());

		StreamUtils.closeOutputStream(os);
		final String compare = ReadFileUtils.readFromFile(destination);
		this.result = expected.equals(compare);
		AssertJUnit.assertTrue("", this.result);
	}

	@Test
	public void testInputStream2String() throws DirectoryAllreadyExistsException, IOException,
		FileDoesNotExistException
	{

		final File inputFile = new File(this.testDir, "testInputStream2String.inp");
		inputFile.createNewFile();
		final String inputString = "Its a beautifull day!!!\n" + "This is the second line.\n"
			+ "This is the third line. ";
		WriteFileUtils.string2File(inputFile, inputString);
		// --------------------------------
		final InputStream is = StreamUtils.getInputStream(inputFile);
		final String compare = ReadFileUtils.inputStream2String(is);

		this.result = inputString.equals(compare);
		AssertJUnit.assertTrue("", this.result);

	}

	@Test
	public void testIsZip()
	{
		final int length = FileConst.ZIP_EXTENSIONS.length;
		for (int i = 0; i < length; i++)
		{
			final File testIsZip = new File(this.testResources, "testIsZip"
				+ FileConst.ZIP_EXTENSIONS[i]);
			this.result = ZipUtils.isZip(testIsZip.getName());
			AssertJUnit.assertTrue("The file " + testIsZip.getName() + " should be a zipfile.",
				this.result);
		}
		this.result = ZipUtils.isZip(this.testResources.getName());
		AssertJUnit.assertFalse("The file " + this.testResources.getName()
			+ " should not be a zipfile.", this.result);
	}

	@Test
	public void testMatch()
	{
		final String filename = "testMatch.txt";
		final String txtExtension = ".txt";
		final String rtfExtension = ".rtf";
		final String cvsExtension = ".cvs";
		final String[] extensions = { txtExtension };

		this.result = FileSearchUtils.match(filename, extensions);
		AssertJUnit.assertTrue("", this.result);

		final String[] otherExtensions = { rtfExtension, cvsExtension };
		this.result = FileSearchUtils.match(filename, otherExtensions);
		AssertJUnit.assertFalse("", this.result);
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
			final boolean created = CreateFileUtils.newDirectory(srcDir);
			AssertJUnit.assertTrue("The directory " + srcDir.getAbsolutePath()
				+ " should be created.", created);
			WriteFileUtils.string2File(srcFile, "Its a beautifull day!!!");
		}
		System.err.println("-------------------------------------------------");
		System.err.println("srcFile.getAbsolutePath():" + srcFile.getAbsolutePath());
		System.err.println("-------------------------------------------------");
		// Test to move the dir.
		this.result = RenameFileUtils.moveFile(srcDir, destDir);
		AssertJUnit.assertTrue("Directory should be renamed.", this.result);
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

		this.result = RenameFileUtils.moveFile(srcFile, destDir);
		AssertJUnit.assertFalse("File should not exist in this directory.", this.result);
		WriteFileUtils.string2File(srcFile, "Its a beautifull day!!!");
		this.result = RenameFileUtils.moveFile(srcFile, destDir);
		AssertJUnit.assertTrue("File should be renamed.", this.result);
		this.result = FileSearchUtils.containsFile(this.deeperDir, destDir);
		AssertJUnit.assertTrue("The renamed file should exist in this directory.", this.result);


	}

	@Test
	public void testOpenFileReader() throws IOException
	{
		final File testFile1 = new File(this.testDir, "testOpenFileReader.txt");
		final String inputString = "Its a beautifull day!!!";
		final String expected = inputString;
		final String ap = testFile1.getAbsolutePath();
		WriteFileUtils.string2File(inputString, ap);

		final Reader reader = ReadFileUtils.openFileReader(ap);
		final String compare = ReadFileUtils.reader2String(reader);
		this.result = expected.equals(compare);
		AssertJUnit.assertTrue("", this.result);
	}

	@Test
	public void testReader2String() throws IOException, DirectoryAllreadyExistsException
	{

		final File inputFile = new File(this.testDir, "testReader2String.inp");
		inputFile.createNewFile();
		final String inputString = "Its a beautifull day!!!\n" + "This is the second line.\n"
			+ "This is the third line. ";
		WriteFileUtils.string2File(inputFile, inputString);
		// --------------------------------
		final Reader reader = StreamUtils.getReader(inputFile);
		final String compare = ReadFileUtils.reader2String(reader);

		this.result = inputString.equals(compare);
		AssertJUnit.assertTrue("", this.result);

	}

	@Test
	public void testReadFromFile() throws IOException, DirectoryAllreadyExistsException
	{

		final File testFile1 = new File(this.testDir, "testReadFromFile.txt");
		final String inputString = "Its a beautifull day!!!";
		WriteFileUtils.string2File(testFile1, inputString);
		// --------------------------------

		final String content = ReadFileUtils.readFromFile(testFile1);
		this.result = inputString.equals(content);
		AssertJUnit.assertTrue("", this.result);

	}

	@Test
	public void testReadHeadLine() throws DirectoryAllreadyExistsException, IOException
	{

		final File inputFile = new File(this.testDir, "testReadHeadLine.inp");
		inputFile.createNewFile();

		final String inputString = "Its a beautifull day!!!\n This is the second line.\nThis is the third line. ";
		final String expected = "Its a beautifull day!!!";
		WriteFileUtils.string2File(inputFile, inputString);
		// --------------------------------
		final String compare = ReadFileUtils.readHeadLine(inputFile.getAbsolutePath());

		this.result = expected.equals(compare);
		AssertJUnit.assertTrue("", this.result);

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
		final List<String> testList = ReadFileUtils.readLinesInList(testFile);
		this.result = expected.equals(testList);
		AssertJUnit.assertTrue("", this.result);
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
		WriteFileUtils.writeProperties2File(ap, properties);
		compare = ReadFileUtils.readPropertiesFromFile(ap);
		this.result = properties.equals(compare);
		AssertJUnit.assertTrue(this.result);
		// clean up
		DeleteFileUtils.delete(tp);
	}

	@Test
	public void testReadSourceFileAndWriteDestFile() throws IOException
	{

		final File source = new File(this.testDir.getAbsoluteFile(),
			"testReadSourceFileAndWriteDestFileInput.txt");
		final File destination = new File(this.testDir.getAbsoluteFile(),
			"testReadSourceFileAndWriteDestFileOutput.tft");

		WriteFileUtils.string2File(source, "Its a beautifull day!!!");
		WriteFileUtils.string2File(destination, "");
		try
		{
			WriteFileUtils.readSourceFileAndWriteDestFile(source.getAbsolutePath(),
				destination.getAbsolutePath());
		}
		catch (final Exception e)
		{
			this.result = e instanceof FileNotFoundException;
			AssertJUnit.assertTrue("Exception should be of type FileNotFoundException.",
				this.result);
		}

		final String inputString = "Its a beautifull day!!!";
		final String expected = inputString;
		WriteFileUtils.string2File(source, inputString);

		WriteFileUtils.readSourceFileAndWriteDestFile(source.getAbsolutePath(),
			destination.getAbsolutePath());

		final String compare = ReadFileUtils.readFromFile(destination);
		this.result = expected.equals(compare);
		AssertJUnit.assertTrue("", this.result);

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

		this.result = RenameFileUtils.renameFile(testFile1, renamedFile1, false);
		AssertJUnit.assertFalse("File should not exist in this directory.", this.result);
		WriteFileUtils.string2File(testFile1, "Its a beautifull day!!!");
		this.result = RenameFileUtils.renameFile(testFile1, renamedFile1, false);
		AssertJUnit.assertTrue("File should be renamed.", this.result);
		this.result = FileSearchUtils.containsFile(this.deepDir, renamedFile1);
		AssertJUnit.assertTrue("The renamed file should exist in this directory.", this.result);
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

		this.result = RenameFileUtils.renameFile(testFile1, renamedFile1);
		AssertJUnit.assertFalse("File should not exist in this directory.", this.result);
		WriteFileUtils.string2File(testFile1, "Its a beautifull day!!!");
		this.result = RenameFileUtils.renameFile(testFile1, renamedFile1);
		AssertJUnit.assertTrue("File should be renamed.", this.result);
		this.result = FileSearchUtils.containsFile(this.deepDir, renamedFile1);
		AssertJUnit.assertTrue("The renamed file should exist in this directory.", this.result);

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

		WriteFileUtils.string2File(testFile1, "Its a beautifull day!!!");
		this.result = RenameFileUtils.renameFile(testFile1, renamedFile1.getName());
		AssertJUnit.assertTrue("File should be renamed.", this.result);
		this.result = FileSearchUtils.containsFile(this.deepDir, renamedFile1);
		AssertJUnit.assertTrue("The renamed file should exist in this directory.", this.result);

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
		// this.result = DateUtils.isBetween(beforeDate, afterDate, between);
		// assertTrue("", this.result);
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
		// this.result = compare[i] == expected[i];
		// assertTrue("", this.result);
		// }
	}

	@Test
	public void testString2FileFileString() throws DirectoryAllreadyExistsException, IOException
	{

		final File testFile1 = new File(this.testDir, "testString2FileFileString.txt");
		final String inputString = "Its a beautifull day!!!";
		WriteFileUtils.string2File(testFile1, inputString);
		// --------------------------------

		final String content = ReadFileUtils.readFromFile(testFile1);
		this.result = inputString.equals(content);
		AssertJUnit.assertTrue("", this.result);

	}

	@Test
	public void testString2FileStringString() throws IOException
	{

		final File testFile1 = new File(this.testDir, "testString2FileStringString.txt");
		final String inputString = "Its a beautifull day!!!";
		WriteFileUtils.string2File(inputString, testFile1.getAbsolutePath());
		// --------------------------------

		final String content = ReadFileUtils.readFromFile(testFile1);
		this.result = inputString.equals(content);
		AssertJUnit.assertTrue("", this.result);

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
		WriteFileUtils.string2File(inputFile, inputString);
		// --------------------------------
		final InputStream is = StreamUtils.getInputStream(inputFile);
		final OutputStream os = StreamUtils.getOutputStream(outputFile);
		StreamUtils.writeInputStreamToOutputStream(is, os, true);

		final String content = ReadFileUtils.readFromFile(outputFile);
		this.result = inputString.equals(content);
		AssertJUnit.assertTrue("", this.result);

	}

	@Test
	public void testWrite2FileReaderWriterBoolean() throws DirectoryAllreadyExistsException,
		IOException
	{

		final File inputFile = new File(this.testDir, "testWrite2FileReaderWriterBoolean.inp");
		inputFile.createNewFile();
		final File outputFile = new File(this.testDir, "testWrite2FileReaderWriterBoolean.outp");
		outputFile.createNewFile();
		final String inputString = "Its a beautifull day!!!";
		WriteFileUtils.string2File(inputFile, inputString);
		// --------------------------------
		final Reader reader = StreamUtils.getReader(inputFile);
		final Writer writer = StreamUtils.getWriter(outputFile);
		WriteFileUtils.write2File(reader, writer, true);

		final String content = ReadFileUtils.readFromFile(outputFile);
		this.result = inputString.equals(content);
		AssertJUnit.assertTrue("", this.result);

	}

	@Test
	public void testWrite2FileStringPrintWriterBoolean() throws DirectoryAllreadyExistsException,
		IOException
	{

		final File inputFile = new File(this.testDir, "testWrite2FileStringPrintWriterBoolean.inp");
		inputFile.createNewFile();
		final File outputFile = new File(this.testDir,
			"testWrite2FileStringPrintWriterBoolean.outp");
		outputFile.createNewFile();
		final String inputString = "Its a beautifull day!!!";
		WriteFileUtils.string2File(inputFile, inputString);
		// --------------------------------
		final PrintWriter writer = (PrintWriter)StreamUtils.getWriter(outputFile);
		final String path = inputFile.getAbsolutePath();
		WriteFileUtils.write2File(path, writer, true);

		final String content = ReadFileUtils.readFromFile(outputFile);
		this.result = inputString.equals(content);
		AssertJUnit.assertTrue("", this.result);

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
		WriteFileUtils.string2File(inputFile, inputString);
		// --------------------------------
		WriteFileUtils.write2File(inputFile.getAbsolutePath(), outputFile.getAbsolutePath());

		final String content = ReadFileUtils.readFromFile(outputFile);
		this.result = inputString.equals(content);
		AssertJUnit.assertTrue("", this.result);

	}

	@Test
	public void testWrite2FileWithBuffer() throws DirectoryAllreadyExistsException, IOException
	{

		final File inputFile = new File(this.testDir, "testWrite2FileWithBuffer.inp");
		inputFile.createNewFile();
		final File outputFile = new File(this.testDir, "testWrite2FileWithBuffer.outp");
		outputFile.createNewFile();
		final String inputString = "Its a beautifull day!!!";
		WriteFileUtils.string2File(inputFile, inputString);
		// --------------------------------
		WriteFileUtils.write2FileWithBuffer(inputFile.getAbsolutePath(),
			outputFile.getAbsolutePath());

		final String content = ReadFileUtils.readFromFile(outputFile);
		this.result = inputString.equals(content);
		AssertJUnit.assertTrue("", this.result);

	}

	@Test
	public void testWriteByteArrayToFileFileByteArray() throws IOException
	{
		final byte[] expected = { -84, -19, 0, 5, 116, 0, 7, 70, 111, 111, 32, 98, 97, 114 };

		final File destination = new File(this.testDir.getAbsoluteFile(),
			"testStoreByteArrayToFile.txt");

		WriteFileUtils.writeByteArrayToFile(destination, expected);

		final String compareString = ReadFileUtils.readFromFile(destination);
		final byte[] compare = StringUtils.convertToBytearray(compareString.toCharArray());

		for (int i = 0; i < compare.length; i++)
		{
			this.result = compare[i] == expected[i];
			AssertJUnit.assertTrue("", this.result);
		}
	}

	@Test
	public void testWriteByteArrayToFileStringByteArray() throws IOException
	{
		final byte[] expected = { -84, -19, 0, 5, 116, 0, 7, 70, 111, 111, 32, 98, 97, 114 };

		final File destination = new File(this.testDir.getAbsoluteFile(),
			"testStoreByteArrayToFile.txt");

		WriteFileUtils.writeByteArrayToFile(destination.getAbsolutePath(), expected);

		final String compareString = ReadFileUtils.readFromFile(destination);
		final byte[] compare = StringUtils.convertToBytearray(compareString.toCharArray());

		for (int i = 0; i < compare.length; i++)
		{
			this.result = compare[i] == expected[i];
			AssertJUnit.assertTrue("", this.result);
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
		WriteFileUtils.writeLinesToFile(expected, testFile);
		final List<String> testList = ReadFileUtils.readLinesInList(testFile);
		this.result = expected.equals(testList);
		AssertJUnit.assertTrue("", this.result);
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
		WriteFileUtils.writeLinesToFile(testFile, expected, null);
		final List<String> testList = ReadFileUtils.readLinesInList(testFile);
		final boolean result = expected.equals(testList);
		AssertJUnit.assertTrue("", result);
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
		WriteFileUtils.writeProperties2File(ap, properties);
		final Properties compare = ReadFileUtils.readPropertiesFromFile(ap);
		this.result = properties.equals(compare);
		AssertJUnit.assertTrue(this.result);
		DeleteFileUtils.delete(tp);
	}

	@Test
	public void testWriteStringToFile() throws IOException
	{

		final File source = new File(this.testDir.getAbsoluteFile(), "testWriteStringToFile.txt");

		final String inputString = "Its a beautifull day!!!";
		final String expected = inputString;
		WriteFileUtils.writeStringToFile(source, inputString, null);

		final String compare = ReadFileUtils.readFromFile(source);
		this.result = expected.equals(compare);
		AssertJUnit.assertTrue("", this.result);
	}

}
