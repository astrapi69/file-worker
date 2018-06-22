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
package de.alpharogroup.file.copy;

import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.meanbean.factories.ObjectCreationException;
import org.meanbean.test.BeanTestException;
import org.meanbean.test.BeanTester;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.alpharogroup.file.FileTestCase;
import de.alpharogroup.file.create.CreateFileExtensions;
import de.alpharogroup.file.exceptions.DirectoryAllreadyExistsException;
import de.alpharogroup.file.exceptions.FileIsADirectoryException;
import de.alpharogroup.file.exceptions.FileIsNotADirectoryException;
import de.alpharogroup.file.exceptions.FileIsSecurityRestrictedException;
import de.alpharogroup.file.filter.MultiplyExtensionsFileFilter;
import de.alpharogroup.file.filter.TxtFileFilter;
import de.alpharogroup.file.namefilter.MultiplyExtensionsFilenameFilter;
import de.alpharogroup.file.namefilter.SimpleFilenameFilter;
import de.alpharogroup.file.read.ReadFileExtensions;
import de.alpharogroup.file.write.WriteFileExtensions;

/**
 * The unit test class for the class {@link CopyFileExtensions}.
 *
 * @version 1.0
 *
 * @author Asterios Raptis
 */
public class CopyFileExtensionsTest extends FileTestCase
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
	 * {@link de.alpharogroup.file.copy.CopyFileExtensions#copyDirectory(java.io.File, java.io.File)}
	 * .
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
	public void testCopyDirectoryFileFile()
		throws DirectoryAllreadyExistsException, FileIsSecurityRestrictedException, IOException,
		FileIsADirectoryException, FileIsNotADirectoryException
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
			final boolean created = CreateFileExtensions.newDirectory(srcDeepDir);
			assertTrue("The directory " + srcDeepDir.getAbsolutePath() + " should be created.",
				created);
			WriteFileExtensions.string2File(srcDeepFile, "Its a beautifull day!!!");
		}
		final String deepestDirName = "deepest";
		final File srcDeepestDir = new File(srcDeepDir, deepestDirName);
		final String deepestFilename = "test" + txtSuffix;
		final File srcDeepestFile = new File(srcDeepestDir, deepestFilename);
		if (!srcDeepestDir.exists())
		{
			final boolean created = CreateFileExtensions.newDirectory(srcDeepestDir);
			assertTrue("The directory " + srcDeepestDir.getAbsolutePath() + " should be created.",
				created);
			WriteFileExtensions.string2File(srcDeepestFile, "Its a beautifull night!!!");
		}

		// Test to copy the source directory to the destination directory.
		this.actual = CopyFileExtensions.copyDirectory(srcDeepDir, destDir);
		// Check if the destination directory was copied.
		assertTrue("Directory " + destDir.getAbsolutePath() + " should be copied.", this.actual);
		// Check if the directory inside the destination directory was copied.
		final File expectedDeeperDir = new File(this.deeperDir, dirToCopyName);
		assertTrue("Directory " + expectedDeeperDir.getAbsolutePath() + " should be copied.",
			expectedDeeperDir.exists());
		// Check if the file in the directory inside the destination directory was copied.
		final File expectedDeeperFile = new File(expectedDeeperDir, filePrefix + txtSuffix);
		assertTrue("File " + expectedDeeperFile.getAbsolutePath() + " should be copied.",
			expectedDeeperFile.exists());
		// Check the long lastModified from the file that they are equal.
		assertTrue("long lastModified was not set.",
			srcDeepFile.lastModified() == expectedDeeperFile.lastModified());
		// Check if the directory in the directory inside the destination directory was copied.
		final File expectedDeepestDir = new File(expectedDeeperDir, deepestDirName);
		assertTrue("Directory " + expectedDeepestDir.getAbsolutePath() + " should be copied.",
			expectedDeepestDir.exists());
		// Check if the file in the deeper directory inside the directory from the destination
		// directory was copied.
		final File expectedDeepestFile = new File(expectedDeepestDir, deepestFilename);
		assertTrue("File " + expectedDeepestFile.getAbsolutePath() + " should be copied.",
			expectedDeepestFile.exists());
		// Check the long lastModified from the file that they are equal.
		assertTrue("long lastModified was not set.",
			srcDeepestFile.lastModified() == expectedDeepestFile.lastModified());
	}

	/**
	 * Test method for
	 * {@link de.alpharogroup.file.copy.CopyFileExtensions#copyDirectory(java.io.File, java.io.File, boolean)}
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
	public void testCopyDirectoryFileFileBoolean()
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
		final String txtSuffix = ".txt";
		final File srcFile = new File(srcDir, filePrefix + txtSuffix);
		// if the testfile does not exist create it.
		if (!srcDir.exists())
		{
			final boolean created = CreateFileExtensions.newDirectory(srcDir);
			assertTrue("The directory " + srcDir.getAbsolutePath() + " should be created.",
				created);
			WriteFileExtensions.string2File(srcFile, "Its a beautifull day!!!");
		}
		final String deepestDirName = "deepest";
		final File srcDeepestDir = new File(srcDir, deepestDirName);
		final String deepestFilename = "test" + txtSuffix;
		final File srcDeepestFile = new File(srcDeepestDir, deepestFilename);
		if (!srcDeepestDir.exists())
		{
			final boolean created = CreateFileExtensions.newDirectory(srcDeepestDir);
			assertTrue("The directory " + srcDeepestDir.getAbsolutePath() + " should be created.",
				created);
			WriteFileExtensions.string2File(srcDeepestFile, "Its a beautifull night!!!");
		}

		// Test to copy the source directory to the destination directory.
		this.actual = CopyFileExtensions.copyDirectory(srcDir, destDir, false);
		// Check if the destination directory was copied.
		assertTrue("Directory " + destDir.getAbsolutePath() + " should be copied.", this.actual);
		// Check if the directory inside the destination directory was copied.
		final File expectedDeeperDir = new File(this.deeperDir, dirToCopyName);
		assertTrue("Directory " + expectedDeeperDir.getAbsolutePath() + " should be copied.",
			expectedDeeperDir.exists());
		// Check if the file in the directory inside the destination directory was copied.
		final File expectedDeeperFile = new File(expectedDeeperDir, filePrefix + txtSuffix);
		assertTrue("File " + expectedDeeperFile.getAbsolutePath() + " should be copied.",
			expectedDeeperFile.exists());
		// Check if the directory in the directory inside the destination directory was copied.
		final File expectedDeepestDir = new File(expectedDeeperDir, deepestDirName);
		assertTrue("Directory " + expectedDeepestDir.getAbsolutePath() + " should be copied.",
			expectedDeepestDir.exists());
		// Check if the file in the deeper directory inside the directory from the destination
		// directory was copied.
		final File expectedDeepestFile = new File(expectedDeepestDir, deepestFilename);
		assertTrue("File " + expectedDeepestFile.getAbsolutePath() + " should be copied.",
			expectedDeepestFile.exists());

	}

	/**
	 * Test method for
	 * {@link de.alpharogroup.file.copy.CopyFileExtensions#copyDirectoryWithFileFilter(java.io.File, java.io.File, java.io.FileFilter, boolean)}
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
	public void testCopyDirectoryWithFileFilter()
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
		final String txtSuffix = ".txt";
		final String rtfSuffix = ".rtf";
		final File srcFile1 = new File(srcDir, filePrefix + txtSuffix);
		final File srcFile2 = new File(srcDir, filePrefix + rtfSuffix);

		// if the testfile does not exist create it.
		if (!srcDir.exists())
		{
			final boolean created = CreateFileExtensions.newDirectory(srcDir);
			assertTrue("The directory " + srcDir.getAbsolutePath() + " should be created.",
				created);
			WriteFileExtensions.string2File(srcFile1, "Its a beautifull day!!!");
			WriteFileExtensions.string2File(srcFile2, "Its a beautifull night!!!");

		}
		final String deepestDirName = "deepest";
		final File srcDeepestDir = new File(srcDir, deepestDirName);
		final String srcDeepestFileName1 = "test1" + txtSuffix;
		final String srcDeepestFileName2 = "test2" + rtfSuffix;
		final File srcDeepestFile1 = new File(srcDeepestDir, srcDeepestFileName1);
		final File srcDeepestFile2 = new File(srcDeepestDir, srcDeepestFileName2);
		if (!srcDeepestDir.exists())
		{
			final boolean created = CreateFileExtensions.newDirectory(srcDeepestDir);
			assertTrue("The directory " + srcDeepestDir.getAbsolutePath() + " should be created.",
				created);
			WriteFileExtensions.string2File(srcDeepestFile1, "Its a beautifull day!!!");
			WriteFileExtensions.string2File(srcDeepestFile2, "Its a beautifull night!!!");
		}

		// define a filefilter object...
		final FileFilter fileFilter = new TxtFileFilter();
		// Test to copy the source directory to the destination directory.
		this.actual = CopyFileExtensions.copyDirectoryWithFileFilter(srcDir, destDir, fileFilter,
			false);
		// Check if the destination directory was copied.
		assertTrue("Directory " + destDir.getAbsolutePath() + " should be copied.", this.actual);
		// Check if the directory inside the destination directory was copied.
		final File expectedDeeperDir = new File(this.deeperDir, dirToCopyName);
		assertTrue("Directory " + expectedDeeperDir.getAbsolutePath() + " should be copied.",
			expectedDeeperDir.exists());
		// Check if the file in the directory inside the destination directory was copied.
		final File expectedDeeperFile = new File(expectedDeeperDir, filePrefix + txtSuffix);
		assertTrue("File " + expectedDeeperFile.getAbsolutePath() + " should be copied.",
			expectedDeeperFile.exists());
		// Check if the file that is not included in the FileFilter was not copied in the
		// destination directory.
		final File notCopied1 = new File(expectedDeeperDir, filePrefix + rtfSuffix);
		assertFalse("File " + notCopied1.getAbsolutePath() + " should not be copied.",
			notCopied1.exists());
		// Check if the directory in the directory inside the destination directory was copied.
		final File expectedDeepestDir = new File(expectedDeeperDir, deepestDirName);
		assertTrue("Directory " + expectedDeepestDir.getAbsolutePath() + " should be copied.",
			expectedDeepestDir.exists());
		// Check if the file in the deeper directory inside the directory from the destination
		// directory was copied.
		final File expectedDeepestFile1 = new File(expectedDeepestDir, srcDeepestFileName1);
		assertTrue("File " + expectedDeepestFile1.getAbsolutePath() + " should be copied.",
			expectedDeepestFile1.exists());
		final File notExpectedDeepestFile2 = new File(expectedDeepestDir, srcDeepestFileName2);
		assertFalse("File " + notExpectedDeepestFile2.getAbsolutePath() + " should not be copied.",
			notExpectedDeepestFile2.exists());

	}

	/**
	 * Test method for
	 * {@link de.alpharogroup.file.copy.CopyFileExtensions#copyDirectoryWithFileFilter(java.io.File, java.io.File, java.io.FileFilter, java.io.FileFilter, boolean)}
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
	public void testCopyDirectoryWithFileFilters()
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
		final String txtSuffix = ".txt";
		final String rtfSuffix = ".rtf";
		final String exeSuffix = ".exe";
		final File srcFile1 = new File(srcDir, filePrefix + txtSuffix);
		final File srcFile2 = new File(srcDir, filePrefix + rtfSuffix);
		final File srcFile3 = new File(srcDir, filePrefix + exeSuffix);
		// if the testfile does not exist create it.
		if (!srcDir.exists())
		{
			final boolean created = CreateFileExtensions.newDirectory(srcDir);
			assertTrue("The directory " + srcDir.getAbsolutePath() + " should be created.",
				created);
			WriteFileExtensions.string2File(srcFile1, "Its a beautifull day!!!");
			WriteFileExtensions.string2File(srcFile2, "Its a beautifull night!!!");
			WriteFileExtensions.string2File(srcFile3, "Its a beautifull exe morning!!!");
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
			final boolean created = CreateFileExtensions.newDirectory(srcDeepestDir);
			assertTrue("The directory " + srcDeepestDir.getAbsolutePath() + " should be created.",
				created);
			WriteFileExtensions.string2File(srcDeepestFile1, "Its a beautifull day!!!");
			WriteFileExtensions.string2File(srcDeepestFile2, "Its a beautifull night!!!");
			WriteFileExtensions.string2File(srcDeepestFile3, "Its a beautifull exe morning!!!");
		}

		// define the include filefilter object...
		final FileFilter includeFileFilter = new MultiplyExtensionsFileFilter(
			Arrays.asList(new String[] { ".txt", ".rtf" }), true);

		// define the exclude filefilter object...
		final FileFilter excludeFileFilter = new MultiplyExtensionsFileFilter(
			Arrays.asList(new String[] { ".exe" }));
		// Test to copy the source directory to the destination directory.
		this.actual = CopyFileExtensions.copyDirectoryWithFileFilter(srcDir, destDir,
			includeFileFilter, excludeFileFilter, false);

		// Check if the destination directory was copied.
		assertTrue("Directory " + destDir.getAbsolutePath() + " should be copied.", this.actual);
		// Check if the directory inside the destination directory was copied.
		final File expectedDeeperDir = new File(this.deeperDir, dirToCopyName);
		assertTrue("Directory " + expectedDeeperDir.getAbsolutePath() + " should be copied.",
			expectedDeeperDir.exists());
		// Check if the file in the first directory inside the destination directory was copied.
		final File expectedDeeperFile = new File(expectedDeeperDir, filePrefix + txtSuffix);
		assertTrue("File " + expectedDeeperFile.getAbsolutePath() + " should be copied.",
			expectedDeeperFile.exists());

		// Check if the file in the second directory inside the destination directory was copied.
		final File expectedDeeperFile2 = new File(expectedDeeperDir, filePrefix + rtfSuffix);
		assertTrue("File " + expectedDeeperFile2.getAbsolutePath() + " should be copied.",
			expectedDeeperFile2.exists());
		// Check if the excluded file inside the deeper destination directory was not copied
		final File notExpectedDeeperFile1 = new File(expectedDeeperDir, filePrefix + exeSuffix);
		assertFalse("File " + notExpectedDeeperFile1.getAbsolutePath() + " should not be copied.",
			notExpectedDeeperFile1.exists());
		// Check if the directory in the directory inside the destination directory was copied.
		final File expectedDeepestDir = new File(expectedDeeperDir, deepestDirName);
		assertTrue("Directory " + expectedDeepestDir.getAbsolutePath() + " should be copied.",
			expectedDeepestDir.exists());
		// Check if the first file in the deeper directory inside the directory from the destination
		// directory was copied.
		final File expectedDeepestFile1 = new File(expectedDeepestDir, srcDeepestFileName1);
		assertTrue("File " + expectedDeepestFile1.getAbsolutePath() + " should be copied.",
			expectedDeepestFile1.exists());

		// Check if the second file in the deeper directory inside the directory from the
		// destination directory was copied.
		final File expectedDeepestFile2 = new File(expectedDeepestDir, srcDeepestFileName2);
		assertTrue("File " + expectedDeepestFile2.getAbsolutePath() + " should be copied.",
			expectedDeepestFile2.exists());

		// Check if the excluded file inside the deepest destination directory was not copied
		final File notExpectedDeepestFile3 = new File(expectedDeepestDir, srcDeepestFileName3);
		assertFalse("File " + notExpectedDeepestFile3.getAbsolutePath() + " should not be copied.",
			notExpectedDeepestFile3.exists());

	}

	/**
	 * Test method for
	 * {@link de.alpharogroup.file.copy.CopyFileExtensions#copyDirectoryWithFileFilter(java.io.File, java.io.File, java.io.FileFilter, java.io.FileFilter, boolean)}
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
			final boolean created = CreateFileExtensions.newDirectory(srcDir);
			assertTrue("The directory " + srcDir.getAbsolutePath() + " should be created.",
				created);
			WriteFileExtensions.string2File(srcFile1, "Its a beautifull day!!!");
			WriteFileExtensions.string2File(srcFile2, "Its a beautifull night!!!");
			WriteFileExtensions.string2File(srcFile3, "Its a beautifull exe morning!!!");
			WriteFileExtensions.string2File(srcFile4, "Its a beautifull txt evening!!!");
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
			final boolean created = CreateFileExtensions.newDirectory(srcDeepestDir);
			assertTrue("The directory " + srcDeepestDir.getAbsolutePath() + " should be created.",
				created);
			WriteFileExtensions.string2File(srcDeepestFile1, "Its a beautifull day!!!");
			WriteFileExtensions.string2File(srcDeepestFile2, "Its a beautifull night!!!");
			WriteFileExtensions.string2File(srcDeepestFile3, "Its a beautifull exe morning!!!");
		}

		// define the include filefilter object...
		final FileFilter includeFileFilter = new MultiplyExtensionsFileFilter(
			Arrays.asList(new String[] { ".txt", ".rtf" }), true);

		// define the exclude filefilter object...
		final FileFilter excludeFileFilter = new MultiplyExtensionsFileFilter(
			Arrays.asList(new String[] { ".exe" }));
		// Test to copy the source directory to the destination directory.
		this.actual = CopyFileExtensions.copyDirectoryWithFileFilter(srcDir, destDir,
			includeFileFilter, excludeFileFilter, excludeFiles, false);
		// Check if the destination directory was copied.
		assertTrue("Directory " + destDir.getAbsolutePath() + " should be copied.", this.actual);
		// Check if the directory inside the destination directory was copied.
		final File expectedDeeperDir = new File(this.deeperDir, dirToCopyName);
		assertTrue("Directory " + expectedDeeperDir.getAbsolutePath() + " should be copied.",
			expectedDeeperDir.exists());
		// Check if the file in the first directory inside the destination directory was copied.
		final File expectedDeeperFile = new File(expectedDeeperDir, filePrefix + txtSuffix);
		assertTrue("File " + expectedDeeperFile.getAbsolutePath() + " should be copied.",
			expectedDeeperFile.exists());

		// Check if the file in the second directory inside the destination directory was copied.
		final File expectedDeeperFile2 = new File(expectedDeeperDir, filePrefix + rtfSuffix);
		assertTrue("File " + expectedDeeperFile2.getAbsolutePath() + " should be copied.",
			expectedDeeperFile2.exists());
		// Check if the excluded file inside the deeper destination directory was not copied
		final File notExpectedDeeperFile1 = new File(expectedDeeperDir, filePrefix + exeSuffix);
		assertFalse("File " + notExpectedDeeperFile1.getAbsolutePath() + " should not be copied.",
			notExpectedDeeperFile1.exists());


		// Check if the explicit excluded file inside the deeper destination directory was not
		// copied
		final File notExpectedDeeperFile2 = new File(expectedDeeperDir,
			excludeFilePrefix + exeSuffix);
		assertFalse("File " + notExpectedDeeperFile2.getAbsolutePath() + " should not be copied.",
			notExpectedDeeperFile2.exists());

		// Check if the directory in the directory inside the destination directory was copied.
		final File expectedDeepestDir = new File(expectedDeeperDir, deepestDirName);
		assertTrue("Directory " + expectedDeepestDir.getAbsolutePath() + " should be copied.",
			expectedDeepestDir.exists());
		// Check if the first file in the deeper directory inside the directory from the destination
		// directory was copied.
		final File expectedDeepestFile1 = new File(expectedDeepestDir, srcDeepestFileName1);
		assertTrue("File " + expectedDeepestFile1.getAbsolutePath() + " should be copied.",
			expectedDeepestFile1.exists());

		// Check if the second file in the deeper directory inside the directory from the
		// destination directory was copied.
		final File expectedDeepestFile2 = new File(expectedDeepestDir, srcDeepestFileName2);
		assertTrue("File " + expectedDeepestFile2.getAbsolutePath() + " should be copied.",
			expectedDeepestFile2.exists());

		// Check if the excluded file inside the deepest destination directory was not copied
		final File notExpectedDeepestFile3 = new File(expectedDeepestDir, srcDeepestFileName3);
		assertFalse("File " + notExpectedDeepestFile3.getAbsolutePath() + " should not be copied.",
			notExpectedDeepestFile3.exists());

	}

	/**
	 * Test method for
	 * {@link de.alpharogroup.file.copy.CopyFileExtensions#copyDirectoryWithFilenameFilter(java.io.File, java.io.File, java.io.FilenameFilter, boolean)}
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
	public void testCopyDirectoryWithFilenameFilter()
		throws IOException, FileIsNotADirectoryException, FileIsADirectoryException,
		FileIsSecurityRestrictedException, DirectoryAllreadyExistsException
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
			final boolean created = CreateFileExtensions.newDirectory(srcDir);
			assertTrue("The directory " + srcDir.getAbsolutePath() + " should be created.",
				created);
			WriteFileExtensions.string2File(srcFile1, "Its a beautifull day!!!");
			WriteFileExtensions.string2File(srcFile2, "Its a beautifull night!!!");
		}
		final String deepestDirName = "deepest";
		final File srcDeepestDir = new File(srcDir, deepestDirName);
		final String srcDeepestFileName1 = "test1" + txtSuffix;
		final String srcDeepestFileName2 = "test2" + rtfSuffix;
		final File srcDeepestFile1 = new File(srcDeepestDir, srcDeepestFileName1);
		final File srcDeepestFile2 = new File(srcDeepestDir, srcDeepestFileName2);
		if (!srcDeepestDir.exists())
		{
			final boolean created = CreateFileExtensions.newDirectory(srcDeepestDir);
			assertTrue("The directory " + srcDeepestDir.getAbsolutePath() + " should be created.",
				created);
			WriteFileExtensions.string2File(srcDeepestFile1, "Its a beautifull day!!!");
			WriteFileExtensions.string2File(srcDeepestFile2, "Its a beautifull night!!!");
		}

		// define a filefilter object...
		final FilenameFilter fileFilter = new SimpleFilenameFilter(".txt", true);
		// Test to copy the source directory to the destination directory.
		this.actual = CopyFileExtensions.copyDirectoryWithFilenameFilter(srcDir, destDir,
			fileFilter, false);
		// Check if the destination directory was copied.
		assertTrue("Directory " + destDir.getAbsolutePath() + " should be copied.", this.actual);
		// Check if the directory inside the destination directory was copied.
		final File expectedDeeperDir = new File(this.deeperDir, dirToCopyName);
		assertTrue("Directory " + expectedDeeperDir.getAbsolutePath() + " should be copied.",
			expectedDeeperDir.exists());
		// Check if the file in the directory inside the destination directory was copied.
		final File expectedDeeperFile = new File(expectedDeeperDir, filePrefix + txtSuffix);
		assertTrue("File " + expectedDeeperFile.getAbsolutePath() + " should be copied.",
			expectedDeeperFile.exists());
		final File notCopied1 = new File(expectedDeeperDir, filePrefix + rtfSuffix);
		assertFalse("File " + notCopied1.getAbsolutePath() + " should not be copied.",
			notCopied1.exists());
		// Check if the directory in the directory inside the destination directory was copied.
		final File expectedDeepestDir = new File(expectedDeeperDir, deepestDirName);
		assertTrue("Directory " + expectedDeepestDir.getAbsolutePath() + " should be copied.",
			expectedDeepestDir.exists());
		// Check if the file in the deeper directory inside the directory from the destination
		// directory was copied.
		final File expectedDeepestFile1 = new File(expectedDeepestDir, srcDeepestFileName1);
		assertTrue("File " + expectedDeepestFile1.getAbsolutePath() + " should be copied.",
			expectedDeepestFile1.exists());
		final File notExpectedDeepestFile2 = new File(expectedDeepestDir, srcDeepestFileName2);
		assertFalse("File " + notExpectedDeepestFile2.getAbsolutePath() + " should not be copied.",
			notExpectedDeepestFile2.exists());


	}


	/**
	 * Test method for
	 * {@link de.alpharogroup.file.copy.CopyFileExtensions#copyDirectoryWithFilenameFilter(java.io.File, java.io.File, java.io.FilenameFilter, java.io.FilenameFilter, boolean)}
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
	public void testCopyDirectoryWithFilenameFilters()
		throws IOException, FileIsNotADirectoryException, FileIsADirectoryException,
		FileIsSecurityRestrictedException, DirectoryAllreadyExistsException
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
			final boolean created = CreateFileExtensions.newDirectory(srcDir);
			assertTrue("The directory " + srcDir.getAbsolutePath() + " should be created.",
				created);
			WriteFileExtensions.string2File(srcFile1, "Its a beautifull day!!!");
			WriteFileExtensions.string2File(srcFile2, "Its a beautifull night!!!");
			WriteFileExtensions.string2File(srcFile3, "Its a beautifull exe morning!!!");
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
			final boolean created = CreateFileExtensions.newDirectory(srcDeepestDir);
			assertTrue("The directory " + srcDeepestDir.getAbsolutePath() + " should be created.",
				created);
			WriteFileExtensions.string2File(srcDeepestFile1, "Its a beautifull day!!!");
			WriteFileExtensions.string2File(srcDeepestFile2, "Its a beautifull night!!!");
			WriteFileExtensions.string2File(srcDeepestFile3, "Its a beautifull exe morning!!!");
		}

		// define the include filefilter object...
		final FilenameFilter includeFilenameFilter = new MultiplyExtensionsFilenameFilter(
			Arrays.asList(new String[] { ".txt", ".rtf" }), true);

		// define the exclude filefilter object...
		final FilenameFilter excludeFilenameFilter = new MultiplyExtensionsFilenameFilter(
			Arrays.asList(new String[] { ".exe" }));
		// Test to copy the source directory to the destination directory.
		this.actual = CopyFileExtensions.copyDirectoryWithFilenameFilter(srcDir, destDir,
			includeFilenameFilter, excludeFilenameFilter, false);

		// Check if the destination directory was copied.
		assertTrue("Directory " + destDir.getAbsolutePath() + " should be copied.", this.actual);
		// Check if the directory inside the destination directory was copied.
		final File expectedDeeperDir = new File(this.deeperDir, dirToCopyName);
		assertTrue("Directory " + expectedDeeperDir.getAbsolutePath() + " should be copied.",
			expectedDeeperDir.exists());
		// Check if the file in the first directory inside the destination directory was copied.
		final File expectedDeeperFile = new File(expectedDeeperDir, filePrefix + txtSuffix);
		assertTrue("File " + expectedDeeperFile.getAbsolutePath() + " should be copied.",
			expectedDeeperFile.exists());

		// Check if the file in the second directory inside the destination directory was copied.
		final File expectedDeeperFile2 = new File(expectedDeeperDir, filePrefix + rtfSuffix);
		assertTrue("File " + expectedDeeperFile2.getAbsolutePath() + " should be copied.",
			expectedDeeperFile2.exists());
		// Check if the excluded file inside the deeper destination directory was not copied
		final File notExpectedDeeperFile1 = new File(expectedDeeperDir, filePrefix + exeSuffix);
		assertFalse("File " + notExpectedDeeperFile1.getAbsolutePath() + " should not be copied.",
			notExpectedDeeperFile1.exists());
		// Check if the directory in the directory inside the destination directory was copied.
		final File expectedDeepestDir = new File(expectedDeeperDir, deepestDirName);
		assertTrue("Directory " + expectedDeepestDir.getAbsolutePath() + " should be copied.",
			expectedDeepestDir.exists());
		// Check if the first file in the deeper directory inside the directory from the destination
		// directory was copied.
		final File expectedDeepestFile1 = new File(expectedDeepestDir, srcDeepestFileName1);
		assertTrue("File " + expectedDeepestFile1.getAbsolutePath() + " should be copied.",
			expectedDeepestFile1.exists());

		// Check if the second file in the deeper directory inside the directory from the
		// destination directory was copied.
		final File expectedDeepestFile2 = new File(expectedDeepestDir, srcDeepestFileName2);
		assertTrue("File " + expectedDeepestFile2.getAbsolutePath() + " should be copied.",
			expectedDeepestFile2.exists());

		// Check if the excluded file inside the deepest destination directory was not copied
		final File notExpectedDeepestFile3 = new File(expectedDeepestDir, srcDeepestFileName3);
		assertFalse("File " + notExpectedDeepestFile3.getAbsolutePath() + " should not be copied.",
			notExpectedDeepestFile3.exists());
	}

	/**
	 * Test method for
	 * {@link de.alpharogroup.file.copy.CopyFileExtensions#copyFile(java.io.File, java.io.File)}.
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
		assertTrue("Source file " + source.getName() + " was not copied in the destination file "
			+ destination.getName() + ".", this.actual);
		final String actual = ReadFileExtensions.readFromFile(destination);
		this.actual = expected.equals(actual);
		assertTrue(
			"The content from the source file " + source.getName()
				+ " is not the same as the destination file " + destination.getName() + ".",
			this.actual);

	}

	/**
	 * Test method for
	 * {@link de.alpharogroup.file.copy.CopyFileExtensions#copyFile(java.io.File, java.io.File, boolean)}
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
			this.actual = CopyFileExtensions.copyFile(source, destination, false);
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

		this.actual = CopyFileExtensions.copyFile(source, destination, false);
		assertTrue("Source file " + source.getName() + " was not copied in the destination file "
			+ destination.getName() + ".", this.actual);
		final String compare = ReadFileExtensions.readFromFile(destination);
		this.actual = expected.equals(compare);
		assertTrue(
			"The content from the source file " + source.getName()
				+ " is not the same as the destination file " + destination.getName() + ".",
			this.actual);

	}

	/**
	 * Test method for
	 * {@link de.alpharogroup.file.copy.CopyFileExtensions#copyFileToDirectory(java.io.File, java.io.File)}
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
		WriteFileExtensions.string2File(srcFile, "Its a beautifull day!!!");
		// if the testfile does not exist create it.
		if (!srcDir.exists())
		{
			final boolean created = CreateFileExtensions.newDirectory(srcDir);
			assertTrue("The directory " + srcDir.getAbsolutePath() + " should be created.",
				created);

		}

		// Try to copy the file srcFile into the destination directory.
		this.actual = CopyFileExtensions.copyFileToDirectory(srcFile, srcDir);
		final File expectedCopiedFile = new File(srcDir, filePrefix + txtSuffix);
		assertTrue("File " + expectedCopiedFile.getAbsolutePath() + " should be copied.",
			expectedCopiedFile.exists());

		// Check the long lastModified from the file that they are equal.
		assertTrue("long lastModified is not the same.",
			srcFile.lastModified() == expectedCopiedFile.lastModified());

	}

	/**
	 * Test method for
	 * {@link de.alpharogroup.file.copy.CopyFileExtensions#copyFileToDirectory(java.io.File, java.io.File, boolean)}
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
		WriteFileExtensions.string2File(srcFile, "Its a beautifull day!!!");
		// if the testfile does not exist create it.
		if (!srcDir.exists())
		{
			final boolean created = CreateFileExtensions.newDirectory(srcDir);
			assertTrue("The directory " + srcDir.getAbsolutePath() + " should be created.",
				created);

		}
		// Try to copy the file srcFile into the destination directory.
		this.actual = CopyFileExtensions.copyFileToDirectory(srcFile, srcDir, false);
		final File expectedCopiedFile = new File(srcDir, filePrefix + txtSuffix);
		assertTrue("File " + expectedCopiedFile.getAbsolutePath() + " should be copied.",
			expectedCopiedFile.exists());
	}

	/**
	 * Test method for {@link CopyFileExtensions}
	 */
	@Test(expectedExceptions = { BeanTestException.class, ObjectCreationException.class })
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(CopyFileExtensions.class);
	}

}
