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

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.meanbean.test.BeanTester;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.alpharogroup.file.FileTestCase;
import de.alpharogroup.file.create.FileCreationState;
import de.alpharogroup.file.create.FileFactory;
import de.alpharogroup.file.exceptions.DirectoryAlreadyExistsException;
import de.alpharogroup.file.exceptions.FileIsADirectoryException;
import de.alpharogroup.file.exceptions.FileIsNotADirectoryException;
import de.alpharogroup.file.exceptions.FileIsSecurityRestrictedException;
import de.alpharogroup.file.read.ReadFileExtensions;
import de.alpharogroup.file.write.WriteFileExtensions;
import de.alpharogroup.io.file.FileExtension;
import de.alpharogroup.io.file.filter.MultiplyExtensionsFileFilter;
import de.alpharogroup.io.file.filter.TxtFileFilter;
import de.alpharogroup.io.file.namefilter.MultiplyExtensionsFilenameFilter;
import de.alpharogroup.io.file.namefilter.SimpleFilenameFilter;

/**
 * The unit test class for the class {@link CopyFileExtensions}.
 *
 * @version 1.0
 *
 * @author Asterios Raptis
 */
public class CopyFileExtensionsTest extends FileTestCase
{
	String deepestDirName;
	String deepestFilename;
	File destDir;
	File destination;
	String dirToCopyName;
	FileFilter excludeFileFilter;
	FilenameFilter excludeFilenameFilter;
	String excludeFilePrefix;
	String exeSuffix;
	File expectedDeeperDir;
	File expectedDeeperFile;
	File expectedDeepestDir;
	File expectedDeepestFile;
	FileFilter fileFilter;
	FilenameFilter filenameFilter;
	String filePrefix;
	FileFilter includeFileFilter;
	FilenameFilter includeFilenameFilter;
	String rtfSuffix;
	File source;
	File srcDeepDir;
	File srcDeepestDir;
	File srcDeepestFile;
	File srcDeepestFile1;
	File srcDeepestFile2;
	File srcDeepestFile3;
	String srcDeepestFileName1;
	String srcDeepestFileName2;
	String srcDeepestFileName3;
	File srcDeepFile;
	File srcFile;
	File srcFile2;
	File srcFile3;
	File srcFile4;
	String txtSuffix;

	/**
	 * Sets up method will be invoked before every unit test method in this class
	 *
	 * @throws Exception
	 *             is thrown if an exception occurs
	 */
	@Override
	@BeforeMethod
	public void setUp() throws Exception
	{
		super.setUp();
		// Test to copy a directory...
		// Create a test directory to copy.
		dirToCopyName = "dirToCopy";
		srcDeepDir = new File(deepDir, dirToCopyName);

		// Create a destination directory to copy.
		destDir = new File(this.deeperDir, dirToCopyName);
		// Create a test file in the directory to copy to check if the file is copied too.
		filePrefix = "testCopyFile";
		excludeFilePrefix = "excludeFile";
		txtSuffix = ".txt";
		rtfSuffix = ".rtf";
		exeSuffix = ".exe";
		srcDeepFile = new File(srcDeepDir, filePrefix + txtSuffix);
		srcFile2 = new File(srcDeepDir, filePrefix + rtfSuffix);
		srcFile3 = new File(srcDeepDir, filePrefix + exeSuffix);
		srcFile4 = new File(srcDeepDir, excludeFilePrefix + txtSuffix);
		// if the testfile does not exist create it.
		if (!srcDeepDir.exists())
		{
			final FileCreationState state = FileFactory.newDirectory(srcDeepDir);
			assertTrue("The directory " + srcDeepDir.getAbsolutePath() + " should be created.",
				state.equals(FileCreationState.CREATED));
			WriteFileExtensions.string2File(srcDeepFile, "Its a beautifull day!!!");
			WriteFileExtensions.string2File(srcFile2, "Its a beautifull night!!!");
			WriteFileExtensions.string2File(srcFile3, "Its a beautifull exe morning!!!");
			WriteFileExtensions.string2File(srcFile4, "Its a beautifull txt evening!!!");
		}
		deepestDirName = "deepest";
		srcDeepestDir = new File(srcDeepDir, deepestDirName);
		deepestFilename = "test" + txtSuffix;
		srcDeepestFile = new File(srcDeepestDir, deepestFilename);

		srcDeepestFileName1 = "test1" + txtSuffix;
		srcDeepestFileName2 = "test2" + rtfSuffix;
		srcDeepestFileName3 = "test3" + exeSuffix;
		srcDeepestFile1 = new File(srcDeepestDir, srcDeepestFileName1);
		srcDeepestFile2 = new File(srcDeepestDir, srcDeepestFileName2);
		srcDeepestFile3 = new File(srcDeepestDir, srcDeepestFileName3);

		expectedDeeperDir = new File(deeperDir, dirToCopyName);
		expectedDeeperFile = new File(expectedDeeperDir, filePrefix + txtSuffix);
		expectedDeepestDir = new File(expectedDeeperDir, deepestDirName);
		expectedDeepestFile = new File(expectedDeepestDir, deepestFilename);

		if (!srcDeepestDir.exists())
		{
			final FileCreationState state = FileFactory.newDirectory(srcDeepestDir);
			assertTrue("The directory " + srcDeepestDir.getAbsolutePath() + " should be created.",
				state.equals(FileCreationState.CREATED));
			WriteFileExtensions.string2File(srcDeepestFile, "Its a beautifull night!!!");
			WriteFileExtensions.string2File(srcDeepestFile1, "Its a beautifull day!!!");
			WriteFileExtensions.string2File(srcDeepestFile2, "Its a beautifull night!!!");
			WriteFileExtensions.string2File(srcDeepestFile3, "Its a beautifull exe morning!!!");
		}

		source = new File(this.testDir, "testCopyFileInput.txt");
		destination = new File(this.testDir, "testCopyFileOutput.tft");
		srcFile = new File(this.testDir, filePrefix + txtSuffix);
		if (!testDir.exists())
		{
			final FileCreationState state = FileFactory.newDirectory(srcDeepDir);
			assertTrue("The directory " + testDir.getAbsolutePath() + " should be created.",
				state.equals(FileCreationState.CREATED));
		}
		WriteFileExtensions.string2File(srcFile, "Its a beautifull day!!!");
		// define a filefilter object...
		fileFilter = new TxtFileFilter();
		// define the include filefilter object...
		includeFileFilter = new MultiplyExtensionsFileFilter(
			Arrays.asList(new String[] { ".txt", ".rtf" }), true);
		// define the exclude filefilter object...
		excludeFileFilter = new MultiplyExtensionsFileFilter(
			Arrays.asList(new String[] { ".exe" }));
		// define a filenamefilter object...
		filenameFilter = new SimpleFilenameFilter(".txt", true);
		// define the include filenamefilter object...
		includeFilenameFilter = new MultiplyExtensionsFilenameFilter(
			Arrays.asList(new String[] { ".txt", ".rtf" }), true);
		// define the exclude filenamefilter object...
		excludeFilenameFilter = new MultiplyExtensionsFilenameFilter(
			Arrays.asList(new String[] { ".exe" }));

	}

	/**
	 * Tear down method will be invoked after every unit test method in this class
	 *
	 * @throws Exception
	 *             is thrown if an exception occurs
	 */
	@Override
	@AfterMethod
	public void tearDown() throws Exception
	{
		super.tearDown();
		dirToCopyName = null;
		deepestDirName = null;
		deepestFilename = null;
		srcDeepestFileName1 = null;
		srcDeepestFileName2 = null;
		srcDeepestFileName3 = null;
		excludeFilePrefix = null;
		filePrefix = null;
		txtSuffix = null;
		rtfSuffix = null;
		exeSuffix = null;
		srcDeepDir.delete();
		destDir.delete();
		srcDeepFile.delete();
		srcDeepestDir.delete();
		srcDeepestFile.delete();
		srcFile.delete();
		srcFile2.delete();
		srcFile3.delete();
		srcFile4.delete();
		srcDeepestFile1.delete();
		srcDeepestFile2.delete();
		srcDeepestFile3.delete();
		source.delete();
		destination.delete();
		srcDeepDir = null;
		destDir = null;
		srcDeepFile = null;
		srcDeepestDir = null;
		srcDeepestFile = null;
		srcFile = null;
		srcFile2 = null;
		srcFile3 = null;
		srcFile4 = null;
		srcDeepestFile1 = null;
		srcDeepestFile2 = null;
		srcDeepestFile3 = null;
		source = null;
		destination = null;
	}

	/**
	 * Test method for {@link CopyFileExtensions#copyDirectory(File, File)}
	 *
	 * @throws DirectoryAlreadyExistsException
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
		throws DirectoryAlreadyExistsException, FileIsSecurityRestrictedException, IOException,
		FileIsADirectoryException, FileIsNotADirectoryException
	{
		// Test to copy the source directory to the destination directory.
		actual = CopyFileExtensions.copyDirectory(srcDeepDir, destDir);
		// Check if the destination directory was copied.
		assertTrue("Directory " + destDir.getAbsolutePath() + " should be copied.", actual);
		// Check if the directory inside the destination directory was copied.
		assertTrue("Directory " + expectedDeeperDir.getAbsolutePath() + " should be copied.",
			expectedDeeperDir.exists());
		// Check if the file in the directory inside the destination directory was copied.
		assertTrue("File " + expectedDeeperFile.getAbsolutePath() + " should be copied.",
			expectedDeeperFile.exists());
		// Check the long lastModified from the file that they are equal.
		assertTrue("long lastModified was not set.",
			srcDeepFile.lastModified() == expectedDeeperFile.lastModified());
		// Check if the directory in the directory inside the destination directory was copied.
		assertTrue("Directory " + expectedDeepestDir.getAbsolutePath() + " should be copied.",
			expectedDeepestDir.exists());
		// Check if the file in the deeper directory inside the directory from the destination
		// directory was copied.
		assertTrue("File " + expectedDeepestFile.getAbsolutePath() + " should be copied.",
			expectedDeepestFile.exists());
		// Check the long lastModified from the file that they are equal.
		assertTrue("long lastModified was not set.",
			srcDeepestFile.lastModified() == expectedDeepestFile.lastModified());
	}

	/**
	 * Test method for {@link CopyFileExtensions#copyDirectory(File, File, boolean)}
	 *
	 * @throws FileIsSecurityRestrictedException
	 *             Is thrown if the source file is security restricted.
	 * @throws IOException
	 *             Is thrown if an error occurs by reading or writing.
	 * @throws FileIsADirectoryException
	 *             Is thrown if the destination file is a directory.
	 * @throws FileIsNotADirectoryException
	 *             Is thrown if the source file is not a directory.
	 * @throws DirectoryAlreadyExistsException
	 *             Is thrown if the directory all ready exists.
	 */
	@Test(enabled = true)
	public void testCopyDirectoryFileFileBoolean()
		throws FileIsSecurityRestrictedException, IOException, FileIsADirectoryException,
		FileIsNotADirectoryException, DirectoryAlreadyExistsException
	{
		// Test to copy the source directory to the destination directory.
		actual = CopyFileExtensions.copyDirectory(srcDeepDir, destDir, false);
		// Check if the destination directory was copied.
		assertTrue("Directory " + destDir.getAbsolutePath() + " should be copied.", actual);
		// Check if the directory inside the destination directory was copied.
		assertTrue("Directory " + expectedDeeperDir.getAbsolutePath() + " should be copied.",
			expectedDeeperDir.exists());
		// Check if the file in the directory inside the destination directory was copied.
		assertTrue("File " + expectedDeeperFile.getAbsolutePath() + " should be copied.",
			expectedDeeperFile.exists());
		// Check if the directory in the directory inside the destination directory was copied.
		assertTrue("Directory " + expectedDeepestDir.getAbsolutePath() + " should be copied.",
			expectedDeepestDir.exists());
		// Check if the file in the deeper directory inside the directory from the destination
		// directory was copied.
		assertTrue("File " + expectedDeepestFile.getAbsolutePath() + " should be copied.",
			expectedDeepestFile.exists());
	}

	/**
	 * Test method for
	 * {@link CopyFileExtensions#copyDirectoryWithFileFilter(File, File, FileFilter, boolean)}
	 *
	 * @throws FileIsSecurityRestrictedException
	 *             Is thrown if the source file is security restricted.
	 * @throws IOException
	 *             Is thrown if an error occurs by reading or writing.
	 * @throws FileIsADirectoryException
	 *             Is thrown if the destination file is a directory.
	 * @throws FileIsNotADirectoryException
	 *             Is thrown if the source file is not a directory.
	 * @throws DirectoryAlreadyExistsException
	 *             Is thrown if the directory all ready exists.
	 */
	@Test(enabled = true)
	public void testCopyDirectoryWithFileFilter()
		throws FileIsSecurityRestrictedException, IOException, FileIsADirectoryException,
		FileIsNotADirectoryException, DirectoryAlreadyExistsException
	{
		// Test to copy the source directory to the destination directory.
		actual = CopyFileExtensions.copyDirectoryWithFileFilter(srcDeepDir, destDir, fileFilter,
			false);
		// Check if the destination directory was copied.
		assertTrue("Directory " + destDir.getAbsolutePath() + " should be copied.", actual);
		// Check if the directory inside the destination directory was copied.
		assertTrue("Directory " + expectedDeeperDir.getAbsolutePath() + " should be copied.",
			expectedDeeperDir.exists());
		// Check if the file in the directory inside the destination directory was copied.
		assertTrue("File " + expectedDeeperFile.getAbsolutePath() + " should be copied.",
			expectedDeeperFile.exists());
		// Check if the file that is not included in the FileFilter was not copied in the
		// destination directory.
		final File notCopied1 = new File(expectedDeeperDir, filePrefix + rtfSuffix);
		assertFalse("File " + notCopied1.getAbsolutePath() + " should not be copied.",
			notCopied1.exists());
		// Check if the directory in the directory inside the destination directory was copied.
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
	 * {@link CopyFileExtensions#copyDirectoryWithFileFilter(File, File, FileFilter, FileFilter, boolean)}
	 *
	 * @throws FileIsSecurityRestrictedException
	 *             Is thrown if the source file is security restricted.
	 * @throws IOException
	 *             Is thrown if an error occurs by reading or writing.
	 * @throws FileIsADirectoryException
	 *             Is thrown if the destination file is a directory.
	 * @throws FileIsNotADirectoryException
	 *             Is thrown if the source file is not a directory.
	 * @throws DirectoryAlreadyExistsException
	 *             Is thrown if the directory all ready exists.
	 */
	@Test(enabled = true)
	public void testCopyDirectoryWithFileFilters()
		throws FileIsSecurityRestrictedException, IOException, FileIsADirectoryException,
		FileIsNotADirectoryException, DirectoryAlreadyExistsException
	{
		// Test to copy the source directory to the destination directory.
		actual = CopyFileExtensions.copyDirectoryWithFileFilter(srcDeepDir, destDir,
			includeFileFilter, excludeFileFilter, false);

		// Check if the destination directory was copied.
		assertTrue("Directory " + destDir.getAbsolutePath() + " should be copied.", actual);
		// Check if the directory inside the destination directory was copied.
		final File expectedDeeperDir = new File(deeperDir, dirToCopyName);
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
	 * {@link CopyFileExtensions#copyDirectoryWithFileFilter(File, File, FileFilter, FileFilter, boolean)}
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
	 * @throws DirectoryAlreadyExistsException
	 *             Is thrown if the directory all ready exists.
	 */
	@Test(enabled = true)
	public void testCopyDirectoryWithFileFiltersAndExcudedFileList()
		throws FileIsSecurityRestrictedException, IOException, FileIsADirectoryException,
		FileIsNotADirectoryException, DirectoryAlreadyExistsException
	{
		// list with files to exclude...
		final List<File> excludeFiles = new ArrayList<>();
		excludeFiles.add(srcFile4);
		// define the include filefilter object...
		final FileFilter includeFileFilter = new MultiplyExtensionsFileFilter(
			Arrays.asList(new String[] { ".txt", ".rtf" }), true);

		// define the exclude filefilter object...
		final FileFilter excludeFileFilter = new MultiplyExtensionsFileFilter(
			Arrays.asList(new String[] { ".exe" }));
		// Test to copy the source directory to the destination directory.
		actual = CopyFileExtensions.copyDirectoryWithFileFilter(srcDeepDir, destDir,
			includeFileFilter, excludeFileFilter, excludeFiles, false);
		// Check if the destination directory was copied.
		assertTrue("Directory " + destDir.getAbsolutePath() + " should be copied.", actual);
		// Check if the directory inside the destination directory was copied.
		final File expectedDeeperDir = new File(deeperDir, dirToCopyName);
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
	 * {@link CopyFileExtensions#copyDirectoryWithFilenameFilter(File, File, FilenameFilter, boolean)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws FileIsNotADirectoryException
	 *             the file is not a directory exception
	 * @throws FileIsADirectoryException
	 *             the file is a directory exception
	 * @throws FileIsSecurityRestrictedException
	 *             the file is security restricted exception
	 * @throws DirectoryAlreadyExistsException
	 *             the directory allready exists exception
	 */
	@Test(enabled = true)
	public void testCopyDirectoryWithFilenameFilter()
		throws IOException, FileIsNotADirectoryException, FileIsADirectoryException,
		FileIsSecurityRestrictedException, DirectoryAlreadyExistsException
	{
		// Test to copy the source directory to the destination directory.
		actual = CopyFileExtensions.copyDirectoryWithFilenameFilter(srcDeepDir, destDir,
			filenameFilter, false);
		// Check if the destination directory was copied.
		assertTrue("Directory " + destDir.getAbsolutePath() + " should be copied.", actual);
		// Check if the directory inside the destination directory was copied.
		final File expectedDeeperDir = new File(deeperDir, dirToCopyName);
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
	 * {@link CopyFileExtensions#copyDirectoryWithFilenameFilter(File, File, FilenameFilter, FilenameFilter, boolean)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws FileIsNotADirectoryException
	 *             the file is not a directory exception
	 * @throws FileIsADirectoryException
	 *             the file is a directory exception
	 * @throws FileIsSecurityRestrictedException
	 *             the file is security restricted exception
	 * @throws DirectoryAlreadyExistsException
	 *             the directory allready exists exception
	 */
	@Test(enabled = true)
	public void testCopyDirectoryWithFilenameFilters()
		throws IOException, FileIsNotADirectoryException, FileIsADirectoryException,
		FileIsSecurityRestrictedException, DirectoryAlreadyExistsException
	{
		// Test to copy the source directory to the destination directory.
		actual = CopyFileExtensions.copyDirectoryWithFilenameFilter(srcDeepDir, destDir,
			includeFilenameFilter, excludeFilenameFilter, false);

		// Check if the destination directory was copied.
		assertTrue("Directory " + destDir.getAbsolutePath() + " should be copied.", actual);
		// Check if the directory inside the destination directory was copied.
		final File expectedDeeperDir = new File(deeperDir, dirToCopyName);
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
	 * Test method for {@link CopyFileExtensions#copyFile(File, File)}
	 *
	 * @throws IOException
	 *             Is thrown if an error occurs by reading or writing.
	 * @throws FileIsADirectoryException
	 *             Is thrown if the destination file is a directory.
	 */
	@Test
	public void testCopyFileFileFile() throws IOException, FileIsADirectoryException
	{
		try
		{
			actual = CopyFileExtensions.copyFile(source, destination);
			assertFalse("", actual);
		}
		catch (final Exception fnfe)
		{
			actual = fnfe instanceof FileNotFoundException;
			assertTrue("", actual);
		}
		final String inputString = "Its a beautifull day!!!";
		WriteFileExtensions.string2File(source, inputString);

		actual = CopyFileExtensions.copyFile(source, destination);
		assertTrue("Source file " + source.getName() + " was not copied in the destination file "
			+ destination.getName() + ".", actual);
		final String actual = ReadFileExtensions.readFromFile(destination);
		final String expected = inputString;
		assertEquals(actual, expected);
	}

	/**
	 * Test method for {@link CopyFileExtensions#copyFile(File, File, boolean)}
	 *
	 * @throws IOException
	 *             Is thrown if an error occurs by reading or writing.
	 * @throws FileIsADirectoryException
	 *             Is thrown if the destination file is a directory.
	 */
	@Test
	public void testCopyFileFileFileBoolean() throws IOException, FileIsADirectoryException
	{
		try
		{
			actual = CopyFileExtensions.copyFile(source, destination, false);
			assertFalse("", actual);
		}
		catch (final Exception fnfe)
		{
			actual = fnfe instanceof FileNotFoundException;
			assertTrue("", actual);
		}
		final String inputString = "Its a beautifull day!!!";
		WriteFileExtensions.string2File(source, inputString);

		actual = CopyFileExtensions.copyFile(source, destination, false);
		assertTrue("Source file " + source.getName() + " was not copied in the destination file "
			+ destination.getName() + ".", actual);
		final String actual = ReadFileExtensions.readFromFile(destination);
		final String expected = inputString;
		assertEquals(actual, expected);
	}

	/**
	 * Test method for {@link CopyFileExtensions#copyFileToDirectory(File, File)}
	 *
	 * @throws DirectoryAlreadyExistsException
	 *             the directory allready exists exception
	 * @throws FileIsNotADirectoryException
	 *             the file is not a directory exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws FileIsADirectoryException
	 *             the file is a directory exception
	 */
	@Test
	public void testCopyFileToDirectoryFileFile() throws DirectoryAlreadyExistsException,
		FileIsNotADirectoryException, IOException, FileIsADirectoryException
	{
		// Try to copy the file srcFile into the destination directory.
		actual = CopyFileExtensions.copyFileToDirectory(srcFile, srcDeepDir);
		final File expectedCopiedFile = new File(srcDeepDir, filePrefix + txtSuffix);
		assertTrue("File " + expectedCopiedFile.getAbsolutePath() + " should be copied.",
			expectedCopiedFile.exists());

		// Check the long lastModified from the file that they are equal.
		assertTrue("long lastModified is not the same.",
			srcFile.lastModified() == expectedCopiedFile.lastModified());
	}

	/**
	 * Test method for {@link CopyFileExtensions#copyFileToDirectory(File, File, boolean)}
	 *
	 * @throws FileIsNotADirectoryException
	 *             the file is not a directory exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws FileIsADirectoryException
	 *             the file is a directory exception
	 * @throws DirectoryAlreadyExistsException
	 *             the directory allready exists exception
	 */
	@Test(enabled = true)
	public void testCopyFileToDirectoryFileFileBoolean() throws FileIsNotADirectoryException,
		IOException, FileIsADirectoryException, DirectoryAlreadyExistsException
	{
		// Try to copy the file srcFile into the destination directory.
		actual = CopyFileExtensions.copyFileToDirectory(srcFile, srcDeepDir, false);
		final File expectedCopiedFile = new File(srcDeepDir, filePrefix + txtSuffix);
		assertTrue("File " + expectedCopiedFile.getAbsolutePath() + " should be copied.",
			expectedCopiedFile.exists());
	}

	/**
	 * Test method for {@link CopyFileExtensions#newBackupOf(File, Charset, Charset)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testNewBackupOf() throws IOException
	{
		File backupFile = CopyFileExtensions.newBackupOf(srcDeepFile, Charset.forName("UTF-8"),
			Charset.forName("UTF-8"));
		assertTrue(backupFile.exists());
		assertTrue(backupFile.getName().endsWith(FileExtension.BACKUP.getExtension()));
		assertTrue(backupFile.getName()
			.startsWith(backupFile.getName().substring(0, backupFile.getName().length() - 4)));
		backupFile.deleteOnExit();
	}

	/**
	 * Test method for {@link CopyFileExtensions}
	 */
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(CopyFileExtensions.class);
	}

}
