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
package de.alpharogroup.file.copy;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.AssertJUnit;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.alpharogroup.file.FileTestCase;
import de.alpharogroup.file.create.CreateFileUtils;
import de.alpharogroup.file.exceptions.DirectoryAllreadyExistsException;
import de.alpharogroup.file.exceptions.FileIsADirectoryException;
import de.alpharogroup.file.exceptions.FileIsNotADirectoryException;
import de.alpharogroup.file.exceptions.FileIsSecurityRestrictedException;
import de.alpharogroup.file.filter.MultiplyExtensionsFileFilter;
import de.alpharogroup.file.filter.TxtFileFilter;
import de.alpharogroup.file.namefilter.MultiplyExtensionsFilenameFilter;
import de.alpharogroup.file.namefilter.SimpleFilenameFilter;
import de.alpharogroup.file.read.ReadFileUtils;
import de.alpharogroup.file.write.WriteFileUtils;

/**
 * The Class CopyFileUtilsTest.
 *
 * @version 1.0
 *
 * @author Asterios Raptis
 *
 */
public class CopyFileUtilsTest extends FileTestCase
{

	/**
	 * Sets the up.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Override
	@BeforeMethod
	public void setUp() throws Exception
	{
		super.setUp();
	}

	/**
	 * Tear down.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Override
	@AfterMethod
	public void tearDown() throws Exception
	{
		super.tearDown();
	}

	/**
	 * Test method for
	 * {@link de.alpharogroup.file.copy.CopyFileUtils#copyDirectory(java.io.File, java.io.File)} .
	 *
	 * @throws DirectoryAllreadyExistsException
	 *             Is thrown if the directory all ready exists.
	 * @throws FileIsSecurityRestrictedException
	 *             Is thrown if the source file is security restricted.
	 * @throws IOException
	 *             Is thrown if an error occurs by reading or writing.
	 * @throws FileIsADirectoryException
	 *             Is thrown if the destination file is a directory.
	 * @throws FileIsNotADirectoryException
	 *             Is thrown if the source file is not a directory.
	 */
	@Test
	public void testCopyDirectoryFileFile() throws DirectoryAllreadyExistsException,
		FileIsSecurityRestrictedException, IOException, FileIsADirectoryException,
		FileIsNotADirectoryException
	{
		// Test to copy a directory...
		// Create a test directory to copy.
		final String dirToCopyName = "dirToCopy";
		final File srcDeepDir = new File(this.deepDir, dirToCopyName);

		// Create a destination directory to copy.
		final File destDir = new File(this.deeperDir, dirToCopyName);
		// Create a test file in the directory to copy to check if the file is copied too.
		final String filePrefix = "testCopyFile";
		final String txtSuffix = ".txt";
		final File srcDeepFile = new File(srcDeepDir, filePrefix + txtSuffix);
		// if the testfile does not exist create it.
		if (!srcDeepDir.exists())
		{
			final boolean created = CreateFileUtils.newDirectory(srcDeepDir);
			AssertJUnit.assertTrue("The directory " + srcDeepDir.getAbsolutePath()
				+ " should be created.", created);
			WriteFileUtils.string2File(srcDeepFile, "Its a beautifull day!!!");
		}
		final String deepestDirName = "deepest";
		final File srcDeepestDir = new File(srcDeepDir, deepestDirName);
		final String deepestFilename = "test" + txtSuffix;
		final File srcDeepestFile = new File(srcDeepestDir, deepestFilename);
		if (!srcDeepestDir.exists())
		{
			final boolean created = CreateFileUtils.newDirectory(srcDeepestDir);
			AssertJUnit.assertTrue("The directory " + srcDeepestDir.getAbsolutePath()
				+ " should be created.", created);
			WriteFileUtils.string2File(srcDeepestFile, "Its a beautifull night!!!");
		}

		// Test to copy the source directory to the destination directory.
		this.result = CopyFileUtils.copyDirectory(srcDeepDir, destDir);
		// Check if the destination directory was copied.
		AssertJUnit.assertTrue("Directory " + destDir.getAbsolutePath() + " should be copied.",
			this.result);
		// Check if the directory inside the destination directory was copied.
		final File expectedDeeperDir = new File(this.deeperDir, dirToCopyName);
		AssertJUnit.assertTrue("Directory " + expectedDeeperDir.getAbsolutePath()
			+ " should be copied.", expectedDeeperDir.exists());
		// Check if the file in the directory inside the destination directory was copied.
		final File expectedDeeperFile = new File(expectedDeeperDir, filePrefix + txtSuffix);
		AssertJUnit.assertTrue("File " + expectedDeeperFile.getAbsolutePath()
			+ " should be copied.", expectedDeeperFile.exists());
		// Check the long lastModified from the file that they are equal.
		AssertJUnit.assertTrue("long lastModified was not set.",
			srcDeepFile.lastModified() == expectedDeeperFile.lastModified());
		// Check if the directory in the directory inside the destination directory was copied.
		final File expectedDeepestDir = new File(expectedDeeperDir, deepestDirName);
		AssertJUnit.assertTrue("Directory " + expectedDeepestDir.getAbsolutePath()
			+ " should be copied.", expectedDeepestDir.exists());
		// Check if the file in the deeper directory inside the directory from the destination
		// directory was copied.
		final File expectedDeepestFile = new File(expectedDeepestDir, deepestFilename);
		AssertJUnit.assertTrue("File " + expectedDeepestFile.getAbsolutePath()
			+ " should be copied.", expectedDeepestFile.exists());
		// Check the long lastModified from the file that they are equal.
		AssertJUnit.assertTrue("long lastModified was not set.",
			srcDeepestFile.lastModified() == expectedDeepestFile.lastModified());
	}

	/**
	 * Test method for
	 * {@link de.alpharogroup.file.copy.CopyFileUtils#copyDirectory(java.io.File, java.io.File, boolean)}
	 * .
	 * 
	 * @throws FileIsSecurityRestrictedException
	 *             Is thrown if the source file is security restricted.
	 * @throws IOException
	 *             Is thrown if an error occurs by reading or writing.
	 * @throws FileIsADirectoryException
	 *             Is thrown if the destination file is a directory.
	 * @throws FileIsNotADirectoryException
	 *             Is thrown if the source file is not a directory.
	 * @throws DirectoryAllreadyExistsException
	 *             Is thrown if the directory all ready exists.
	 */
	@Test(enabled = true)
	public void testCopyDirectoryFileFileBoolean() throws FileIsSecurityRestrictedException,
		IOException, FileIsADirectoryException, FileIsNotADirectoryException,
		DirectoryAllreadyExistsException
	{

		// Test to copy a directory...
		// Create a test directory to copy.
		final String dirToCopyName = "dirToCopy";
		final File srcDir = new File(this.deepDir, dirToCopyName);

		// Create a destination directory to copy.
		final File destDir = new File(this.deeperDir, dirToCopyName);
		// Create a test file in the directory to copy to check if the file is copied too.
		final String filePrefix = "testCopyFile";
		final String txtSuffix = ".txt";
		final File srcFile = new File(srcDir, filePrefix + txtSuffix);
		// if the testfile does not exist create it.
		if (!srcDir.exists())
		{
			final boolean created = CreateFileUtils.newDirectory(srcDir);
			AssertJUnit.assertTrue("The directory " + srcDir.getAbsolutePath()
				+ " should be created.", created);
			WriteFileUtils.string2File(srcFile, "Its a beautifull day!!!");
		}
		final String deepestDirName = "deepest";
		final File srcDeepestDir = new File(srcDir, deepestDirName);
		final String deepestFilename = "test" + txtSuffix;
		final File srcDeepestFile = new File(srcDeepestDir, deepestFilename);
		if (!srcDeepestDir.exists())
		{
			final boolean created = CreateFileUtils.newDirectory(srcDeepestDir);
			AssertJUnit.assertTrue("The directory " + srcDeepestDir.getAbsolutePath()
				+ " should be created.", created);
			WriteFileUtils.string2File(srcDeepestFile, "Its a beautifull night!!!");
		}

		// Test to copy the source directory to the destination directory.
		this.result = CopyFileUtils.copyDirectory(srcDir, destDir, false);
		// Check if the destination directory was copied.
		AssertJUnit.assertTrue("Directory " + destDir.getAbsolutePath() + " should be copied.",
			this.result);
		// Check if the directory inside the destination directory was copied.
		final File expectedDeeperDir = new File(this.deeperDir, dirToCopyName);
		AssertJUnit.assertTrue("Directory " + expectedDeeperDir.getAbsolutePath()
			+ " should be copied.", expectedDeeperDir.exists());
		// Check if the file in the directory inside the destination directory was copied.
		final File expectedDeeperFile = new File(expectedDeeperDir, filePrefix + txtSuffix);
		AssertJUnit.assertTrue("File " + expectedDeeperFile.getAbsolutePath()
			+ " should be copied.", expectedDeeperFile.exists());
		// Check if the directory in the directory inside the destination directory was copied.
		final File expectedDeepestDir = new File(expectedDeeperDir, deepestDirName);
		AssertJUnit.assertTrue("Directory " + expectedDeepestDir.getAbsolutePath()
			+ " should be copied.", expectedDeepestDir.exists());
		// Check if the file in the deeper directory inside the directory from the destination
		// directory was copied.
		final File expectedDeepestFile = new File(expectedDeepestDir, deepestFilename);
		AssertJUnit.assertTrue("File " + expectedDeepestFile.getAbsolutePath()
			+ " should be copied.", expectedDeepestFile.exists());

	}

	/**
	 * Test method for
	 * {@link de.alpharogroup.file.copy.CopyFileUtils#copyDirectoryWithFileFilter(java.io.File, java.io.File, java.io.FileFilter, boolean)}
	 * .
	 * 
	 * @throws FileIsSecurityRestrictedException
	 *             Is thrown if the source file is security restricted.
	 * @throws IOException
	 *             Is thrown if an error occurs by reading or writing.
	 * @throws FileIsADirectoryException
	 *             Is thrown if the destination file is a directory.
	 * @throws FileIsNotADirectoryException
	 *             Is thrown if the source file is not a directory.
	 * @throws DirectoryAllreadyExistsException
	 *             Is thrown if the directory all ready exists.
	 */
	@Test(enabled = true)
	public void testCopyDirectoryWithFileFilter() throws FileIsSecurityRestrictedException,
		IOException, FileIsADirectoryException, FileIsNotADirectoryException,
		DirectoryAllreadyExistsException
	{

		// Test to copy a directory...
		// Create a test directory to copy.
		final String dirToCopyName = "dirToCopy";
		final File srcDir = new File(this.deepDir, dirToCopyName);

		// Create a destination directory to copy.
		final File destDir = new File(this.deeperDir, dirToCopyName);
		// Create a test file in the directory to copy to check if the file is copied too.
		final String filePrefix = "testCopyFile";
		final String txtSuffix = ".txt";
		final String rtfSuffix = ".rtf";
		final File srcFile1 = new File(srcDir, filePrefix + txtSuffix);
		final File srcFile2 = new File(srcDir, filePrefix + rtfSuffix);

		// if the testfile does not exist create it.
		if (!srcDir.exists())
		{
			final boolean created = CreateFileUtils.newDirectory(srcDir);
			AssertJUnit.assertTrue("The directory " + srcDir.getAbsolutePath()
				+ " should be created.", created);
			WriteFileUtils.string2File(srcFile1, "Its a beautifull day!!!");
			WriteFileUtils.string2File(srcFile2, "Its a beautifull night!!!");

		}
		final String deepestDirName = "deepest";
		final File srcDeepestDir = new File(srcDir, deepestDirName);
		final String srcDeepestFileName1 = "test1" + txtSuffix;
		final String srcDeepestFileName2 = "test2" + rtfSuffix;
		final File srcDeepestFile1 = new File(srcDeepestDir, srcDeepestFileName1);
		final File srcDeepestFile2 = new File(srcDeepestDir, srcDeepestFileName2);
		if (!srcDeepestDir.exists())
		{
			final boolean created = CreateFileUtils.newDirectory(srcDeepestDir);
			AssertJUnit.assertTrue("The directory " + srcDeepestDir.getAbsolutePath()
				+ " should be created.", created);
			WriteFileUtils.string2File(srcDeepestFile1, "Its a beautifull day!!!");
			WriteFileUtils.string2File(srcDeepestFile2, "Its a beautifull night!!!");
		}

		// define a filefilter object...
		final FileFilter fileFilter = new TxtFileFilter();
		// Test to copy the source directory to the destination directory.
		this.result = CopyFileUtils.copyDirectoryWithFileFilter(srcDir, destDir, fileFilter, false);
		// Check if the destination directory was copied.
		AssertJUnit.assertTrue("Directory " + destDir.getAbsolutePath() + " should be copied.",
			this.result);
		// Check if the directory inside the destination directory was copied.
		final File expectedDeeperDir = new File(this.deeperDir, dirToCopyName);
		AssertJUnit.assertTrue("Directory " + expectedDeeperDir.getAbsolutePath()
			+ " should be copied.", expectedDeeperDir.exists());
		// Check if the file in the directory inside the destination directory was copied.
		final File expectedDeeperFile = new File(expectedDeeperDir, filePrefix + txtSuffix);
		AssertJUnit.assertTrue("File " + expectedDeeperFile.getAbsolutePath()
			+ " should be copied.", expectedDeeperFile.exists());
		// Check if the file that is not included in the FileFilter was not copied in the
		// destination directory.
		final File notCopied1 = new File(expectedDeeperDir, filePrefix + rtfSuffix);
		AssertJUnit.assertFalse("File " + notCopied1.getAbsolutePath() + " should not be copied.",
			notCopied1.exists());
		// Check if the directory in the directory inside the destination directory was copied.
		final File expectedDeepestDir = new File(expectedDeeperDir, deepestDirName);
		AssertJUnit.assertTrue("Directory " + expectedDeepestDir.getAbsolutePath()
			+ " should be copied.", expectedDeepestDir.exists());
		// Check if the file in the deeper directory inside the directory from the destination
		// directory was copied.
		final File expectedDeepestFile1 = new File(expectedDeepestDir, srcDeepestFileName1);
		AssertJUnit.assertTrue("File " + expectedDeepestFile1.getAbsolutePath()
			+ " should be copied.", expectedDeepestFile1.exists());
		final File notExpectedDeepestFile2 = new File(expectedDeepestDir, srcDeepestFileName2);
		AssertJUnit.assertFalse("File " + notExpectedDeepestFile2.getAbsolutePath()
			+ " should not be copied.", notExpectedDeepestFile2.exists());

	}

	/**
	 * Test method for
	 * {@link de.alpharogroup.file.copy.CopyFileUtils#copyDirectoryWithFileFilter(java.io.File, java.io.File, java.io.FileFilter, java.io.FileFilter, boolean)}
	 * .
	 * 
	 * @throws FileIsSecurityRestrictedException
	 *             Is thrown if the source file is security restricted.
	 * @throws IOException
	 *             Is thrown if an error occurs by reading or writing.
	 * @throws FileIsADirectoryException
	 *             Is thrown if the destination file is a directory.
	 * @throws FileIsNotADirectoryException
	 *             Is thrown if the source file is not a directory.
	 * @throws DirectoryAllreadyExistsException
	 *             Is thrown if the directory all ready exists.
	 */
	@Test(enabled = true)
	public void testCopyDirectoryWithFileFilters() throws FileIsSecurityRestrictedException,
		IOException, FileIsADirectoryException, FileIsNotADirectoryException,
		DirectoryAllreadyExistsException
	{

		// Test to copy a directory...
		// Create a test directory to copy.
		final String dirToCopyName = "dirToCopy";
		final File srcDir = new File(this.deepDir, dirToCopyName);

		// Create a destination directory to copy.
		final File destDir = new File(this.deeperDir, dirToCopyName);
		// Create a test file in the directory to copy to check if the file is copied too.
		final String filePrefix = "testCopyFile";
		final String txtSuffix = ".txt";
		final String rtfSuffix = ".rtf";
		final String exeSuffix = ".exe";
		final File srcFile1 = new File(srcDir, filePrefix + txtSuffix);
		final File srcFile2 = new File(srcDir, filePrefix + rtfSuffix);
		final File srcFile3 = new File(srcDir, filePrefix + exeSuffix);
		// if the testfile does not exist create it.
		if (!srcDir.exists())
		{
			final boolean created = CreateFileUtils.newDirectory(srcDir);
			AssertJUnit.assertTrue("The directory " + srcDir.getAbsolutePath()
				+ " should be created.", created);
			WriteFileUtils.string2File(srcFile1, "Its a beautifull day!!!");
			WriteFileUtils.string2File(srcFile2, "Its a beautifull night!!!");
			WriteFileUtils.string2File(srcFile3, "Its a beautifull exe morning!!!");
		}
		final String deepestDirName = "deepest";
		final File srcDeepestDir = new File(srcDir, deepestDirName);
		final String srcDeepestFileName1 = "test1" + txtSuffix;
		final String srcDeepestFileName2 = "test2" + rtfSuffix;
		final String srcDeepestFileName3 = "test3" + exeSuffix;
		final File srcDeepestFile1 = new File(srcDeepestDir, srcDeepestFileName1);
		final File srcDeepestFile2 = new File(srcDeepestDir, srcDeepestFileName2);
		final File srcDeepestFile3 = new File(srcDeepestDir, srcDeepestFileName3);
		if (!srcDeepestDir.exists())
		{
			final boolean created = CreateFileUtils.newDirectory(srcDeepestDir);
			AssertJUnit.assertTrue("The directory " + srcDeepestDir.getAbsolutePath()
				+ " should be created.", created);
			WriteFileUtils.string2File(srcDeepestFile1, "Its a beautifull day!!!");
			WriteFileUtils.string2File(srcDeepestFile2, "Its a beautifull night!!!");
			WriteFileUtils.string2File(srcDeepestFile3, "Its a beautifull exe morning!!!");
		}

		// define the include filefilter object...
		final FileFilter includeFileFilter = new MultiplyExtensionsFileFilter(
			Arrays.asList(new String[] { ".txt", ".rtf" }), true);

		// define the exclude filefilter object...
		final FileFilter excludeFileFilter = new MultiplyExtensionsFileFilter(
			Arrays.asList(new String[] { ".exe" }));
		// Test to copy the source directory to the destination directory.
		this.result = CopyFileUtils.copyDirectoryWithFileFilter(srcDir, destDir, includeFileFilter,
			excludeFileFilter, false);

		// Check if the destination directory was copied.
		AssertJUnit.assertTrue("Directory " + destDir.getAbsolutePath() + " should be copied.",
			this.result);
		// Check if the directory inside the destination directory was copied.
		final File expectedDeeperDir = new File(this.deeperDir, dirToCopyName);
		AssertJUnit.assertTrue("Directory " + expectedDeeperDir.getAbsolutePath()
			+ " should be copied.", expectedDeeperDir.exists());
		// Check if the file in the first directory inside the destination directory was copied.
		final File expectedDeeperFile = new File(expectedDeeperDir, filePrefix + txtSuffix);
		AssertJUnit.assertTrue("File " + expectedDeeperFile.getAbsolutePath()
			+ " should be copied.", expectedDeeperFile.exists());

		// Check if the file in the second directory inside the destination directory was copied.
		final File expectedDeeperFile2 = new File(expectedDeeperDir, filePrefix + rtfSuffix);
		AssertJUnit.assertTrue("File " + expectedDeeperFile2.getAbsolutePath()
			+ " should be copied.", expectedDeeperFile2.exists());
		// Check if the excluded file inside the deeper destination directory was not copied
		final File notExpectedDeeperFile1 = new File(expectedDeeperDir, filePrefix + exeSuffix);
		AssertJUnit.assertFalse("File " + notExpectedDeeperFile1.getAbsolutePath()
			+ " should not be copied.", notExpectedDeeperFile1.exists());
		// Check if the directory in the directory inside the destination directory was copied.
		final File expectedDeepestDir = new File(expectedDeeperDir, deepestDirName);
		AssertJUnit.assertTrue("Directory " + expectedDeepestDir.getAbsolutePath()
			+ " should be copied.", expectedDeepestDir.exists());
		// Check if the first file in the deeper directory inside the directory from the destination
		// directory was copied.
		final File expectedDeepestFile1 = new File(expectedDeepestDir, srcDeepestFileName1);
		AssertJUnit.assertTrue("File " + expectedDeepestFile1.getAbsolutePath()
			+ " should be copied.", expectedDeepestFile1.exists());

		// Check if the second file in the deeper directory inside the directory from the
		// destination directory was copied.
		final File expectedDeepestFile2 = new File(expectedDeepestDir, srcDeepestFileName2);
		AssertJUnit.assertTrue("File " + expectedDeepestFile2.getAbsolutePath()
			+ " should be copied.", expectedDeepestFile2.exists());

		// Check if the excluded file inside the deepest destination directory was not copied
		final File notExpectedDeepestFile3 = new File(expectedDeepestDir, srcDeepestFileName3);
		AssertJUnit.assertFalse("File " + notExpectedDeepestFile3.getAbsolutePath()
			+ " should not be copied.", notExpectedDeepestFile3.exists());

	}

	/**
	 * Test method for
	 * {@link de.alpharogroup.file.copy.CopyFileUtils#copyDirectoryWithFileFilter(java.io.File, java.io.File, java.io.FileFilter, java.io.FileFilter, boolean)}
	 * .
	 * 
	 * @throws FileIsSecurityRestrictedException
	 *             Is thrown if the source file is security restricted.
	 * @throws IOException
	 *             Is thrown if an error occurs by reading or writing.
	 * @throws FileIsADirectoryException
	 *             Is thrown if the destination file is a directory.
	 * @throws FileIsNotADirectoryException
	 *             Is thrown if the source file is not a directory.
	 * @throws DirectoryAllreadyExistsException
	 *             Is thrown if the directory all ready exists.
	 */
	@Test(enabled = true)
	public void testCopyDirectoryWithFileFiltersAndExcudedFileList()
		throws FileIsSecurityRestrictedException, IOException, FileIsADirectoryException,
		FileIsNotADirectoryException, DirectoryAllreadyExistsException
	{

		// Test to copy a directory...
		// Create a test directory to copy.
		final String dirToCopyName = "dirToCopy";
		final File srcDir = new File(this.deepDir, dirToCopyName);

		// Create a destination directory to copy.
		final File destDir = new File(this.deeperDir, dirToCopyName);
		// Create a test file in the directory to copy to check if the file is copied too.
		final String filePrefix = "testCopyFile";
		final String excludeFilePrefix = "excludeFile";
		final String txtSuffix = ".txt";
		final String rtfSuffix = ".rtf";
		final String exeSuffix = ".exe";
		final File srcFile1 = new File(srcDir, filePrefix + txtSuffix);
		final File srcFile2 = new File(srcDir, filePrefix + rtfSuffix);
		final File srcFile3 = new File(srcDir, filePrefix + exeSuffix);
		final File srcFile4 = new File(srcDir, excludeFilePrefix + txtSuffix);
		final List<File> excludeFiles = new ArrayList<>();
		excludeFiles.add(srcFile4);
		// if the testfile does not exist create it.
		if (!srcDir.exists())
		{
			final boolean created = CreateFileUtils.newDirectory(srcDir);
			AssertJUnit.assertTrue("The directory " + srcDir.getAbsolutePath()
				+ " should be created.", created);
			WriteFileUtils.string2File(srcFile1, "Its a beautifull day!!!");
			WriteFileUtils.string2File(srcFile2, "Its a beautifull night!!!");
			WriteFileUtils.string2File(srcFile3, "Its a beautifull exe morning!!!");
			WriteFileUtils.string2File(srcFile4, "Its a beautifull txt evening!!!");
		}
		final String deepestDirName = "deepest";
		final File srcDeepestDir = new File(srcDir, deepestDirName);
		final String srcDeepestFileName1 = "test1" + txtSuffix;
		final String srcDeepestFileName2 = "test2" + rtfSuffix;
		final String srcDeepestFileName3 = "test3" + exeSuffix;
		final File srcDeepestFile1 = new File(srcDeepestDir, srcDeepestFileName1);
		final File srcDeepestFile2 = new File(srcDeepestDir, srcDeepestFileName2);
		final File srcDeepestFile3 = new File(srcDeepestDir, srcDeepestFileName3);
		if (!srcDeepestDir.exists())
		{
			final boolean created = CreateFileUtils.newDirectory(srcDeepestDir);
			AssertJUnit.assertTrue("The directory " + srcDeepestDir.getAbsolutePath()
				+ " should be created.", created);
			WriteFileUtils.string2File(srcDeepestFile1, "Its a beautifull day!!!");
			WriteFileUtils.string2File(srcDeepestFile2, "Its a beautifull night!!!");
			WriteFileUtils.string2File(srcDeepestFile3, "Its a beautifull exe morning!!!");
		}

		// define the include filefilter object...
		final FileFilter includeFileFilter = new MultiplyExtensionsFileFilter(
			Arrays.asList(new String[] { ".txt", ".rtf" }), true);

		// define the exclude filefilter object...
		final FileFilter excludeFileFilter = new MultiplyExtensionsFileFilter(
			Arrays.asList(new String[] { ".exe" }));
		// Test to copy the source directory to the destination directory.
		this.result = CopyFileUtils.copyDirectoryWithFileFilter(srcDir, destDir, includeFileFilter,
			excludeFileFilter, excludeFiles, false);
		// Check if the destination directory was copied.
		AssertJUnit.assertTrue("Directory " + destDir.getAbsolutePath() + " should be copied.",
			this.result);
		// Check if the directory inside the destination directory was copied.
		final File expectedDeeperDir = new File(this.deeperDir, dirToCopyName);
		AssertJUnit.assertTrue("Directory " + expectedDeeperDir.getAbsolutePath()
			+ " should be copied.", expectedDeeperDir.exists());
		// Check if the file in the first directory inside the destination directory was copied.
		final File expectedDeeperFile = new File(expectedDeeperDir, filePrefix + txtSuffix);
		AssertJUnit.assertTrue("File " + expectedDeeperFile.getAbsolutePath()
			+ " should be copied.", expectedDeeperFile.exists());

		// Check if the file in the second directory inside the destination directory was copied.
		final File expectedDeeperFile2 = new File(expectedDeeperDir, filePrefix + rtfSuffix);
		AssertJUnit.assertTrue("File " + expectedDeeperFile2.getAbsolutePath()
			+ " should be copied.", expectedDeeperFile2.exists());
		// Check if the excluded file inside the deeper destination directory was not copied
		final File notExpectedDeeperFile1 = new File(expectedDeeperDir, filePrefix + exeSuffix);
		AssertJUnit.assertFalse("File " + notExpectedDeeperFile1.getAbsolutePath()
			+ " should not be copied.", notExpectedDeeperFile1.exists());


		// Check if the explicit excluded file inside the deeper destination directory was not
		// copied
		final File notExpectedDeeperFile2 = new File(expectedDeeperDir, excludeFilePrefix
			+ exeSuffix);
		AssertJUnit.assertFalse("File " + notExpectedDeeperFile2.getAbsolutePath()
			+ " should not be copied.", notExpectedDeeperFile2.exists());

		// Check if the directory in the directory inside the destination directory was copied.
		final File expectedDeepestDir = new File(expectedDeeperDir, deepestDirName);
		AssertJUnit.assertTrue("Directory " + expectedDeepestDir.getAbsolutePath()
			+ " should be copied.", expectedDeepestDir.exists());
		// Check if the first file in the deeper directory inside the directory from the destination
		// directory was copied.
		final File expectedDeepestFile1 = new File(expectedDeepestDir, srcDeepestFileName1);
		AssertJUnit.assertTrue("File " + expectedDeepestFile1.getAbsolutePath()
			+ " should be copied.", expectedDeepestFile1.exists());

		// Check if the second file in the deeper directory inside the directory from the
		// destination directory was copied.
		final File expectedDeepestFile2 = new File(expectedDeepestDir, srcDeepestFileName2);
		AssertJUnit.assertTrue("File " + expectedDeepestFile2.getAbsolutePath()
			+ " should be copied.", expectedDeepestFile2.exists());

		// Check if the excluded file inside the deepest destination directory was not copied
		final File notExpectedDeepestFile3 = new File(expectedDeepestDir, srcDeepestFileName3);
		AssertJUnit.assertFalse("File " + notExpectedDeepestFile3.getAbsolutePath()
			+ " should not be copied.", notExpectedDeepestFile3.exists());

	}

	/**
	 * Test method for
	 * {@link de.alpharogroup.file.copy.CopyFileUtils#copyDirectoryWithFilenameFilter(java.io.File, java.io.File, java.io.FilenameFilter, boolean)}
	 * .
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws FileIsNotADirectoryException
	 *             the file is not a directory exception
	 * @throws FileIsADirectoryException
	 *             the file is a directory exception
	 * @throws FileIsSecurityRestrictedException
	 *             the file is security restricted exception
	 * @throws DirectoryAllreadyExistsException
	 *             the directory allready exists exception
	 */
	@Test(enabled = true)
	public void testCopyDirectoryWithFilenameFilter() throws IOException,
		FileIsNotADirectoryException, FileIsADirectoryException, FileIsSecurityRestrictedException,
		DirectoryAllreadyExistsException
	{

		// Test to copy a directory...
		// Create a test directory to copy.
		final String dirToCopyName = "dirToCopy";
		final File srcDir = new File(this.deepDir, dirToCopyName);

		// Create a destination directory to copy.
		final File destDir = new File(this.deeperDir, dirToCopyName);
		// Create a test file in the directory to copy to check if the file is copied too.
		final String filePrefix = "testCopyFile";
		final String txtSuffix = ".txt";
		final String rtfSuffix = ".rtf";
		final File srcFile1 = new File(srcDir, filePrefix + txtSuffix);
		final File srcFile2 = new File(srcDir, filePrefix + rtfSuffix);
		// if the testfile does not exist create it.
		if (!srcDir.exists())
		{
			final boolean created = CreateFileUtils.newDirectory(srcDir);
			AssertJUnit.assertTrue("The directory " + srcDir.getAbsolutePath()
				+ " should be created.", created);
			WriteFileUtils.string2File(srcFile1, "Its a beautifull day!!!");
			WriteFileUtils.string2File(srcFile2, "Its a beautifull night!!!");
		}
		final String deepestDirName = "deepest";
		final File srcDeepestDir = new File(srcDir, deepestDirName);
		final String srcDeepestFileName1 = "test1" + txtSuffix;
		final String srcDeepestFileName2 = "test2" + rtfSuffix;
		final File srcDeepestFile1 = new File(srcDeepestDir, srcDeepestFileName1);
		final File srcDeepestFile2 = new File(srcDeepestDir, srcDeepestFileName2);
		if (!srcDeepestDir.exists())
		{
			final boolean created = CreateFileUtils.newDirectory(srcDeepestDir);
			AssertJUnit.assertTrue("The directory " + srcDeepestDir.getAbsolutePath()
				+ " should be created.", created);
			WriteFileUtils.string2File(srcDeepestFile1, "Its a beautifull day!!!");
			WriteFileUtils.string2File(srcDeepestFile2, "Its a beautifull night!!!");
		}

		// define a filefilter object...
		final FilenameFilter fileFilter = new SimpleFilenameFilter(".txt", true);
		// Test to copy the source directory to the destination directory.
		this.result = CopyFileUtils.copyDirectoryWithFilenameFilter(srcDir, destDir, fileFilter,
			false);
		// Check if the destination directory was copied.
		AssertJUnit.assertTrue("Directory " + destDir.getAbsolutePath() + " should be copied.",
			this.result);
		// Check if the directory inside the destination directory was copied.
		final File expectedDeeperDir = new File(this.deeperDir, dirToCopyName);
		AssertJUnit.assertTrue("Directory " + expectedDeeperDir.getAbsolutePath()
			+ " should be copied.", expectedDeeperDir.exists());
		// Check if the file in the directory inside the destination directory was copied.
		final File expectedDeeperFile = new File(expectedDeeperDir, filePrefix + txtSuffix);
		AssertJUnit.assertTrue("File " + expectedDeeperFile.getAbsolutePath()
			+ " should be copied.", expectedDeeperFile.exists());
		final File notCopied1 = new File(expectedDeeperDir, filePrefix + rtfSuffix);
		AssertJUnit.assertFalse("File " + notCopied1.getAbsolutePath() + " should not be copied.",
			notCopied1.exists());
		// Check if the directory in the directory inside the destination directory was copied.
		final File expectedDeepestDir = new File(expectedDeeperDir, deepestDirName);
		AssertJUnit.assertTrue("Directory " + expectedDeepestDir.getAbsolutePath()
			+ " should be copied.", expectedDeepestDir.exists());
		// Check if the file in the deeper directory inside the directory from the destination
		// directory was copied.
		final File expectedDeepestFile1 = new File(expectedDeepestDir, srcDeepestFileName1);
		AssertJUnit.assertTrue("File " + expectedDeepestFile1.getAbsolutePath()
			+ " should be copied.", expectedDeepestFile1.exists());
		final File notExpectedDeepestFile2 = new File(expectedDeepestDir, srcDeepestFileName2);
		AssertJUnit.assertFalse("File " + notExpectedDeepestFile2.getAbsolutePath()
			+ " should not be copied.", notExpectedDeepestFile2.exists());


	}


	/**
	 * Test method for
	 * {@link de.alpharogroup.file.copy.CopyFileUtils#copyDirectoryWithFilenameFilter(java.io.File, java.io.File, java.io.FilenameFilter, java.io.FilenameFilter, boolean)}
	 * .
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws FileIsNotADirectoryException
	 *             the file is not a directory exception
	 * @throws FileIsADirectoryException
	 *             the file is a directory exception
	 * @throws FileIsSecurityRestrictedException
	 *             the file is security restricted exception
	 * @throws DirectoryAllreadyExistsException
	 *             the directory allready exists exception
	 */
	@Test(enabled = true)
	public void testCopyDirectoryWithFilenameFilters() throws IOException,
		FileIsNotADirectoryException, FileIsADirectoryException, FileIsSecurityRestrictedException,
		DirectoryAllreadyExistsException
	{
		// Test to copy a directory...
		// Create a test directory to copy.
		final String dirToCopyName = "dirToCopy";
		final File srcDir = new File(this.deepDir, dirToCopyName);

		// Create a destination directory to copy.
		final File destDir = new File(this.deeperDir, dirToCopyName);
		// Create a test file in the directory to copy to check if the file is copied too.
		final String filePrefix = "testCopyFile";
		final String txtSuffix = ".txt";
		final String rtfSuffix = ".rtf";
		final String exeSuffix = ".exe";
		final File srcFile1 = new File(srcDir, filePrefix + txtSuffix);
		final File srcFile2 = new File(srcDir, filePrefix + rtfSuffix);
		final File srcFile3 = new File(srcDir, filePrefix + exeSuffix);
		// if the testfile does not exist create it.
		if (!srcDir.exists())
		{
			final boolean created = CreateFileUtils.newDirectory(srcDir);
			AssertJUnit.assertTrue("The directory " + srcDir.getAbsolutePath()
				+ " should be created.", created);
			WriteFileUtils.string2File(srcFile1, "Its a beautifull day!!!");
			WriteFileUtils.string2File(srcFile2, "Its a beautifull night!!!");
			WriteFileUtils.string2File(srcFile3, "Its a beautifull exe morning!!!");
		}
		final String deepestDirName = "deepest";
		final File srcDeepestDir = new File(srcDir, deepestDirName);
		final String srcDeepestFileName1 = "test1" + txtSuffix;
		final String srcDeepestFileName2 = "test2" + rtfSuffix;
		final String srcDeepestFileName3 = "test3" + exeSuffix;
		final File srcDeepestFile1 = new File(srcDeepestDir, srcDeepestFileName1);
		final File srcDeepestFile2 = new File(srcDeepestDir, srcDeepestFileName2);
		final File srcDeepestFile3 = new File(srcDeepestDir, srcDeepestFileName3);
		if (!srcDeepestDir.exists())
		{
			final boolean created = CreateFileUtils.newDirectory(srcDeepestDir);
			AssertJUnit.assertTrue("The directory " + srcDeepestDir.getAbsolutePath()
				+ " should be created.", created);
			WriteFileUtils.string2File(srcDeepestFile1, "Its a beautifull day!!!");
			WriteFileUtils.string2File(srcDeepestFile2, "Its a beautifull night!!!");
			WriteFileUtils.string2File(srcDeepestFile3, "Its a beautifull exe morning!!!");
		}

		// define the include filefilter object...
		final FilenameFilter includeFilenameFilter = new MultiplyExtensionsFilenameFilter(
			Arrays.asList(new String[] { ".txt", ".rtf" }), true);

		// define the exclude filefilter object...
		final FilenameFilter excludeFilenameFilter = new MultiplyExtensionsFilenameFilter(
			Arrays.asList(new String[] { ".exe" }));
		// Test to copy the source directory to the destination directory.
		this.result = CopyFileUtils.copyDirectoryWithFilenameFilter(srcDir, destDir,
			includeFilenameFilter, excludeFilenameFilter, false);

		// Check if the destination directory was copied.
		AssertJUnit.assertTrue("Directory " + destDir.getAbsolutePath() + " should be copied.",
			this.result);
		// Check if the directory inside the destination directory was copied.
		final File expectedDeeperDir = new File(this.deeperDir, dirToCopyName);
		AssertJUnit.assertTrue("Directory " + expectedDeeperDir.getAbsolutePath()
			+ " should be copied.", expectedDeeperDir.exists());
		// Check if the file in the first directory inside the destination directory was copied.
		final File expectedDeeperFile = new File(expectedDeeperDir, filePrefix + txtSuffix);
		AssertJUnit.assertTrue("File " + expectedDeeperFile.getAbsolutePath()
			+ " should be copied.", expectedDeeperFile.exists());

		// Check if the file in the second directory inside the destination directory was copied.
		final File expectedDeeperFile2 = new File(expectedDeeperDir, filePrefix + rtfSuffix);
		AssertJUnit.assertTrue("File " + expectedDeeperFile2.getAbsolutePath()
			+ " should be copied.", expectedDeeperFile2.exists());
		// Check if the excluded file inside the deeper destination directory was not copied
		final File notExpectedDeeperFile1 = new File(expectedDeeperDir, filePrefix + exeSuffix);
		AssertJUnit.assertFalse("File " + notExpectedDeeperFile1.getAbsolutePath()
			+ " should not be copied.", notExpectedDeeperFile1.exists());
		// Check if the directory in the directory inside the destination directory was copied.
		final File expectedDeepestDir = new File(expectedDeeperDir, deepestDirName);
		AssertJUnit.assertTrue("Directory " + expectedDeepestDir.getAbsolutePath()
			+ " should be copied.", expectedDeepestDir.exists());
		// Check if the first file in the deeper directory inside the directory from the destination
		// directory was copied.
		final File expectedDeepestFile1 = new File(expectedDeepestDir, srcDeepestFileName1);
		AssertJUnit.assertTrue("File " + expectedDeepestFile1.getAbsolutePath()
			+ " should be copied.", expectedDeepestFile1.exists());

		// Check if the second file in the deeper directory inside the directory from the
		// destination directory was copied.
		final File expectedDeepestFile2 = new File(expectedDeepestDir, srcDeepestFileName2);
		AssertJUnit.assertTrue("File " + expectedDeepestFile2.getAbsolutePath()
			+ " should be copied.", expectedDeepestFile2.exists());

		// Check if the excluded file inside the deepest destination directory was not copied
		final File notExpectedDeepestFile3 = new File(expectedDeepestDir, srcDeepestFileName3);
		AssertJUnit.assertFalse("File " + notExpectedDeepestFile3.getAbsolutePath()
			+ " should not be copied.", notExpectedDeepestFile3.exists());
	}

	/**
	 * Test method for
	 * {@link de.alpharogroup.file.copy.CopyFileUtils#copyFile(java.io.File, java.io.File)}.
	 *
	 * @throws IOException
	 *             Is thrown if an error occurs by reading or writing.
	 * @throws FileIsADirectoryException
	 *             Is thrown if the destination file is a directory.
	 */
	@Test
	public void testCopyFileFileFile() throws IOException, FileIsADirectoryException
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
		AssertJUnit
			.assertTrue("Source file " + source.getName()
				+ " was not copied in the destination file " + destination.getName() + ".",
				this.result);
		final String actual = ReadFileUtils.readFromFile(destination);
		this.result = expected.equals(actual);
		AssertJUnit.assertTrue("The content from the source file " + source.getName()
			+ " is not the same as the destination file " + destination.getName() + ".",
			this.result);

	}

	/**
	 * Test method for
	 * {@link de.alpharogroup.file.copy.CopyFileUtils#copyFile(java.io.File, java.io.File, boolean)}
	 * .
	 * 
	 * @throws IOException
	 *             Is thrown if an error occurs by reading or writing.
	 * @throws FileIsADirectoryException
	 *             Is thrown if the destination file is a directory.
	 */
	@Test
	public void testCopyFileFileFileBoolean() throws IOException, FileIsADirectoryException
	{
		final File source = new File(this.testDir.getAbsoluteFile(), "testCopyFileInput.txt");
		final File destination = new File(this.testDir.getAbsoluteFile(), "testCopyFileOutput.tft");
		try
		{
			this.result = CopyFileUtils.copyFile(source, destination, false);
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

		this.result = CopyFileUtils.copyFile(source, destination, false);
		AssertJUnit
			.assertTrue("Source file " + source.getName()
				+ " was not copied in the destination file " + destination.getName() + ".",
				this.result);
		final String compare = ReadFileUtils.readFromFile(destination);
		this.result = expected.equals(compare);
		AssertJUnit.assertTrue("The content from the source file " + source.getName()
			+ " is not the same as the destination file " + destination.getName() + ".",
			this.result);

	}

	/**
	 * Test method for
	 * {@link de.alpharogroup.file.copy.CopyFileUtils#copyFileToDirectory(java.io.File, java.io.File)}
	 * .
	 *
	 * @throws DirectoryAllreadyExistsException
	 *             the directory allready exists exception
	 * @throws FileIsNotADirectoryException
	 *             the file is not a directory exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws FileIsADirectoryException
	 *             the file is a directory exception
	 */
	@Test
	public void testCopyFileToDirectoryFileFile() throws DirectoryAllreadyExistsException,
		FileIsNotADirectoryException, IOException, FileIsADirectoryException
	{

		// Test to copy a file into a directory...
		// Create a test directory to copy.
		final String dirToCopyName = "dirToCopy";
		final File srcDir = new File(this.deepDir, dirToCopyName);

		// Create a test file in the directory to copy to check if the file is copied too.
		final String filePrefix = "testCopyFile";
		final String txtSuffix = ".txt";
		final File srcFile = new File(this.testDir, filePrefix + txtSuffix);
		WriteFileUtils.string2File(srcFile, "Its a beautifull day!!!");
		// if the testfile does not exist create it.
		if (!srcDir.exists())
		{
			final boolean created = CreateFileUtils.newDirectory(srcDir);
			AssertJUnit.assertTrue("The directory " + srcDir.getAbsolutePath()
				+ " should be created.", created);

		}

		// Try to copy the file srcFile into the destination directory.
		this.result = CopyFileUtils.copyFileToDirectory(srcFile, srcDir);
		final File expectedCopiedFile = new File(srcDir, filePrefix + txtSuffix);
		AssertJUnit.assertTrue("File " + expectedCopiedFile.getAbsolutePath()
			+ " should be copied.", expectedCopiedFile.exists());

		// Check the long lastModified from the file that they are equal.
		AssertJUnit.assertTrue("long lastModified is not the same.",
			srcFile.lastModified() == expectedCopiedFile.lastModified());

	}

	/**
	 * Test method for
	 * {@link de.alpharogroup.file.copy.CopyFileUtils#copyFileToDirectory(java.io.File, java.io.File, boolean)}
	 * .
	 *
	 * @throws FileIsNotADirectoryException
	 *             the file is not a directory exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws FileIsADirectoryException
	 *             the file is a directory exception
	 * @throws DirectoryAllreadyExistsException
	 *             the directory allready exists exception
	 */
	@Test(enabled = true)
	public void testCopyFileToDirectoryFileFileBoolean() throws FileIsNotADirectoryException,
		IOException, FileIsADirectoryException, DirectoryAllreadyExistsException
	{
		// Test to copy a file into a directory...
		// Create a test directory to copy.
		final String dirToCopyName = "dirToCopy";
		final File srcDir = new File(this.deepDir, dirToCopyName);

		// Create a test file in the directory to copy to check if the file is copied too.
		final String filePrefix = "testCopyFile";
		final String txtSuffix = ".txt";
		final File srcFile = new File(this.testDir, filePrefix + txtSuffix);
		WriteFileUtils.string2File(srcFile, "Its a beautifull day!!!");
		// if the testfile does not exist create it.
		if (!srcDir.exists())
		{
			final boolean created = CreateFileUtils.newDirectory(srcDir);
			AssertJUnit.assertTrue("The directory " + srcDir.getAbsolutePath()
				+ " should be created.", created);

		}
		// Try to copy the file srcFile into the destination directory.
		this.result = CopyFileUtils.copyFileToDirectory(srcFile, srcDir, false);
		final File expectedCopiedFile = new File(srcDir, filePrefix + txtSuffix);
		AssertJUnit.assertTrue("File " + expectedCopiedFile.getAbsolutePath()
			+ " should be copied.", expectedCopiedFile.exists());
	}

}
