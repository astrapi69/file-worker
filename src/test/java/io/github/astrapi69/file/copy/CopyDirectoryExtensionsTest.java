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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;

import io.github.astrapi69.file.FileTestCase;
import io.github.astrapi69.file.create.DirectoryFactory;
import io.github.astrapi69.file.create.FileCreationState;
import io.github.astrapi69.file.exception.DirectoryAlreadyExistsException;
import io.github.astrapi69.file.exception.FileIsADirectoryException;
import io.github.astrapi69.file.exception.FileIsNotADirectoryException;
import io.github.astrapi69.file.exception.FileIsSecurityRestrictedException;
import io.github.astrapi69.file.write.StoreFileExtensions;
import io.github.astrapi69.io.file.filter.MultiplyExtensionsFileFilter;
import io.github.astrapi69.io.file.filter.TxtFileFilter;
import io.github.astrapi69.io.file.namefilter.MultiplyExtensionsFilenameFilter;
import io.github.astrapi69.io.file.namefilter.SimpleFilenameFilter;

/**
 * The unit test class for the class {@link CopyDirectoryExtensions}.
 *
 * @version 1.0
 *
 * @author Asterios Raptis
 */
public class CopyDirectoryExtensionsTest extends FileTestCase
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
	@BeforeEach
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
			final FileCreationState state = DirectoryFactory.newDirectory(srcDeepDir);
			assertTrue(state.equals(FileCreationState.CREATED),
				"The directory " + srcDeepDir.getAbsolutePath() + " should be created.");
			StoreFileExtensions.toFile(srcDeepFile, "Its a beautifull day!!!");
			StoreFileExtensions.toFile(srcFile2, "Its a beautifull night!!!");
			StoreFileExtensions.toFile(srcFile3, "Its a beautifull exe morning!!!");
			StoreFileExtensions.toFile(srcFile4, "Its a beautifull txt evening!!!");
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
			final FileCreationState state = DirectoryFactory.newDirectory(srcDeepestDir);
			assertTrue(state.equals(FileCreationState.CREATED),
				"The directory " + srcDeepestDir.getAbsolutePath() + " should be created.");
			StoreFileExtensions.toFile(srcDeepestFile, "Its a beautifull night!!!");
			StoreFileExtensions.toFile(srcDeepestFile1, "Its a beautifull day!!!");
			StoreFileExtensions.toFile(srcDeepestFile2, "Its a beautifull night!!!");
			StoreFileExtensions.toFile(srcDeepestFile3, "Its a beautifull exe morning!!!");
		}

		source = new File(this.testDir, "testCopyFileInput.txt");
		destination = new File(this.testDir, "testCopyFileOutput.tft");
		srcFile = new File(this.testDir, filePrefix + txtSuffix);
		if (!testDir.exists())
		{
			final FileCreationState state = DirectoryFactory.newDirectory(srcDeepDir);
			assertTrue(state.equals(FileCreationState.CREATED),
				"The directory " + testDir.getAbsolutePath() + " should be created.");
		}
		StoreFileExtensions.toFile(srcFile, "Its a beautifull day!!!");
		// define a filefilter object...
		fileFilter = new TxtFileFilter();
		// define the include filefilter object...
		includeFileFilter = new MultiplyExtensionsFileFilter(Arrays.asList(".txt", ".rtf"), true);
		// define the exclude filefilter object...
		excludeFileFilter = new MultiplyExtensionsFileFilter(Arrays.asList(".exe"));
		// define a filenamefilter object...
		filenameFilter = new SimpleFilenameFilter(".txt", true);
		// define the include filenamefilter object...
		includeFilenameFilter = new MultiplyExtensionsFilenameFilter(Arrays.asList(".txt", ".rtf"),
			true);
		// define the exclude filenamefilter object...
		excludeFilenameFilter = new MultiplyExtensionsFilenameFilter(Arrays.asList(".exe"));

	}

	/**
	 * Tear down method will be invoked after every unit test method in this class
	 *
	 * @throws Exception
	 *             is thrown if an exception occurs
	 */
	@Override
	@AfterEach
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
	 * Test method for {@link CopyDirectoryExtensions#copyDirectory(File, File)}
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
		actual = CopyDirectoryExtensions.copyDirectory(srcDeepDir, destDir);
		// Check if the destination directory was copied.
		assertTrue(actual, "Directory " + destDir.getAbsolutePath() + " should be copied.");
		// Check if the directory inside the destination directory was copied.
		assertTrue(expectedDeeperDir.exists(),
			"Directory " + expectedDeeperDir.getAbsolutePath() + " should be copied.");
		// Check if the file in the directory inside the destination directory was copied.
		assertTrue(expectedDeeperFile.exists(),
			"File " + expectedDeeperFile.getAbsolutePath() + " should be copied.");
		// Check the long lastModified from the file that they are equal.
		assertTrue(srcDeepFile.lastModified() == expectedDeeperFile.lastModified(),
			"long lastModified was not set.");
		// Check if the directory in the directory inside the destination directory was copied.
		assertTrue(expectedDeepestDir.exists(),
			"Directory " + expectedDeepestDir.getAbsolutePath() + " should be copied.");
		// Check if the file in the deeper directory inside the directory from the destination
		// directory was copied.
		assertTrue(expectedDeepestFile.exists(),
			"File " + expectedDeepestFile.getAbsolutePath() + " should be copied.");
		// Check the long lastModified from the file that they are equal.
		assertTrue(srcDeepestFile.lastModified() == expectedDeepestFile.lastModified(),
			"long lastModified was not set.");
	}

	/**
	 * Test method for {@link CopyDirectoryExtensions#copyDirectory(File, File, boolean)}
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
	@Test
	public void testCopyDirectoryFileFileBoolean()
		throws FileIsSecurityRestrictedException, IOException, FileIsADirectoryException,
		FileIsNotADirectoryException, DirectoryAlreadyExistsException
	{
		// Test to copy the source directory to the destination directory.
		actual = CopyDirectoryExtensions.copyDirectory(srcDeepDir, destDir, false);
		// Check if the destination directory was copied.
		assertTrue(actual, "Directory " + destDir.getAbsolutePath() + " should be copied.");
		// Check if the directory inside the destination directory was copied.
		assertTrue(expectedDeeperDir.exists(),
			"Directory " + expectedDeeperDir.getAbsolutePath() + " should be copied.");
		// Check if the file in the directory inside the destination directory was copied.
		assertTrue(expectedDeeperFile.exists(),
			"File " + expectedDeeperFile.getAbsolutePath() + " should be copied.");
		// Check if the directory in the directory inside the destination directory was copied.
		assertTrue(expectedDeepestDir.exists(),
			"Directory " + expectedDeepestDir.getAbsolutePath() + " should be copied.");
		// Check if the file in the deeper directory inside the directory from the destination
		// directory was copied.
		assertTrue(expectedDeepestFile.exists(),
			"File " + expectedDeepestFile.getAbsolutePath() + " should be copied.");
	}

	/**
	 * Test method for
	 * {@link CopyDirectoryExtensions#copyDirectoryWithFileFilter(File, File, FileFilter, boolean)}
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
	@Test
	public void testCopyDirectoryWithFileFilter()
		throws FileIsSecurityRestrictedException, IOException, FileIsADirectoryException,
		FileIsNotADirectoryException, DirectoryAlreadyExistsException
	{
		// Test to copy the source directory to the destination directory.
		actual = CopyDirectoryExtensions.copyDirectoryWithFileFilter(srcDeepDir, destDir,
			fileFilter, false);
		// Check if the destination directory was copied.
		assertTrue(actual, "Directory " + destDir.getAbsolutePath() + " should be copied.");
		// Check if the directory inside the destination directory was copied.
		assertTrue(expectedDeeperDir.exists(),
			"Directory " + expectedDeeperDir.getAbsolutePath() + " should be copied.");
		// Check if the file in the directory inside the destination directory was copied.
		assertTrue(expectedDeeperFile.exists(),
			"File " + expectedDeeperFile.getAbsolutePath() + " should be copied.");
		// Check if the file that is not included in the FileFilter was not copied in the
		// destination directory.
		final File notCopied1 = new File(expectedDeeperDir, filePrefix + rtfSuffix);
		assertFalse(notCopied1.exists(),
			"File " + notCopied1.getAbsolutePath() + " should not be copied.");
		// Check if the directory in the directory inside the destination directory was copied.
		assertTrue(expectedDeepestDir.exists(),
			"Directory " + expectedDeepestDir.getAbsolutePath() + " should be copied.");
		// Check if the file in the deeper directory inside the directory from the destination
		// directory was copied.
		final File expectedDeepestFile1 = new File(expectedDeepestDir, srcDeepestFileName1);
		assertTrue(expectedDeepestFile1.exists(),
			"File " + expectedDeepestFile1.getAbsolutePath() + " should be copied.");
		final File notExpectedDeepestFile2 = new File(expectedDeepestDir, srcDeepestFileName2);
		assertFalse(notExpectedDeepestFile2.exists(),
			"File " + notExpectedDeepestFile2.getAbsolutePath() + " should not be copied.");
	}

	/**
	 * Test method for
	 * {@link CopyDirectoryExtensions#copyDirectoryWithFileFilter(File, File, FileFilter, FileFilter, boolean)}
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
	@Test
	public void testCopyDirectoryWithFileFilters()
		throws FileIsSecurityRestrictedException, IOException, FileIsADirectoryException,
		FileIsNotADirectoryException, DirectoryAlreadyExistsException
	{
		// Test to copy the source directory to the destination directory.
		actual = CopyDirectoryExtensions.copyDirectoryWithFileFilter(srcDeepDir, destDir,
			includeFileFilter, excludeFileFilter, false);

		// Check if the destination directory was copied.
		assertTrue(actual, "Directory " + destDir.getAbsolutePath() + " should be copied.");
		// Check if the directory inside the destination directory was copied.
		final File expectedDeeperDir = new File(deeperDir, dirToCopyName);
		assertTrue(expectedDeeperDir.exists(),
			"Directory " + expectedDeeperDir.getAbsolutePath() + " should be copied.");
		// Check if the file in the first directory inside the destination directory was copied.
		final File expectedDeeperFile = new File(expectedDeeperDir, filePrefix + txtSuffix);
		assertTrue(expectedDeeperFile.exists(),
			"File " + expectedDeeperFile.getAbsolutePath() + " should be copied.");

		// Check if the file in the second directory inside the destination directory was copied.
		final File expectedDeeperFile2 = new File(expectedDeeperDir, filePrefix + rtfSuffix);
		assertTrue(expectedDeeperFile2.exists(),
			"File " + expectedDeeperFile2.getAbsolutePath() + " should be copied.");
		// Check if the excluded file inside the deeper destination directory was not copied
		final File notExpectedDeeperFile1 = new File(expectedDeeperDir, filePrefix + exeSuffix);
		assertFalse(notExpectedDeeperFile1.exists(),
			"File " + notExpectedDeeperFile1.getAbsolutePath() + " should not be copied.");
		// Check if the directory in the directory inside the destination directory was copied.
		final File expectedDeepestDir = new File(expectedDeeperDir, deepestDirName);
		assertTrue(expectedDeepestDir.exists(),
			"Directory " + expectedDeepestDir.getAbsolutePath() + " should be copied.");
		// Check if the first file in the deeper directory inside the directory from the destination
		// directory was copied.
		final File expectedDeepestFile1 = new File(expectedDeepestDir, srcDeepestFileName1);
		assertTrue(expectedDeepestFile1.exists(),
			"File " + expectedDeepestFile1.getAbsolutePath() + " should be copied.");

		// Check if the second file in the deeper directory inside the directory from the
		// destination directory was copied.
		final File expectedDeepestFile2 = new File(expectedDeepestDir, srcDeepestFileName2);
		assertTrue(expectedDeepestFile2.exists(),
			"File " + expectedDeepestFile2.getAbsolutePath() + " should be copied.");

		// Check if the excluded file inside the deepest destination directory was not copied
		final File notExpectedDeepestFile3 = new File(expectedDeepestDir, srcDeepestFileName3);
		assertFalse(notExpectedDeepestFile3.exists(),
			"File " + notExpectedDeepestFile3.getAbsolutePath() + " should not be copied.");
	}

	/**
	 * Test method for
	 * {@link CopyDirectoryExtensions#copyDirectoryWithFileFilter(File, File, FileFilter, FileFilter, boolean)}
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
	@Test
	public void testCopyDirectoryWithFileFiltersAndExcudedFileList()
		throws FileIsSecurityRestrictedException, IOException, FileIsADirectoryException,
		FileIsNotADirectoryException, DirectoryAlreadyExistsException
	{
		// list with files to exclude...
		final List<File> excludeFiles = new ArrayList<>();
		excludeFiles.add(srcFile4);
		// define the include filefilter object...
		final FileFilter includeFileFilter = new MultiplyExtensionsFileFilter(
			Arrays.asList(".txt", ".rtf"), true);

		// define the exclude filefilter object...
		final FileFilter excludeFileFilter = new MultiplyExtensionsFileFilter(
			Arrays.asList(".exe"));
		// Test to copy the source directory to the destination directory.
		actual = CopyDirectoryExtensions.copyDirectoryWithFileFilter(srcDeepDir, destDir,
			includeFileFilter, excludeFileFilter, excludeFiles, false);
		// Check if the destination directory was copied.
		assertTrue(actual, "Directory " + destDir.getAbsolutePath() + " should be copied.");
		// Check if the directory inside the destination directory was copied.
		final File expectedDeeperDir = new File(deeperDir, dirToCopyName);
		assertTrue(expectedDeeperDir.exists(),
			"Directory " + expectedDeeperDir.getAbsolutePath() + " should be copied.");
		// Check if the file in the first directory inside the destination directory was copied.
		final File expectedDeeperFile = new File(expectedDeeperDir, filePrefix + txtSuffix);
		assertTrue(expectedDeeperFile.exists(),
			"File " + expectedDeeperFile.getAbsolutePath() + " should be copied.");

		// Check if the file in the second directory inside the destination directory was copied.
		final File expectedDeeperFile2 = new File(expectedDeeperDir, filePrefix + rtfSuffix);
		assertTrue(expectedDeeperFile2.exists(),
			"File " + expectedDeeperFile2.getAbsolutePath() + " should be copied.");
		// Check if the excluded file inside the deeper destination directory was not copied
		final File notExpectedDeeperFile1 = new File(expectedDeeperDir, filePrefix + exeSuffix);
		assertFalse(notExpectedDeeperFile1.exists(),
			"File " + notExpectedDeeperFile1.getAbsolutePath() + " should not be copied.");

		// Check if the explicit excluded file inside the deeper destination directory was not
		// copied
		final File notExpectedDeeperFile2 = new File(expectedDeeperDir,
			excludeFilePrefix + exeSuffix);
		assertFalse(notExpectedDeeperFile2.exists(),
			"File " + notExpectedDeeperFile2.getAbsolutePath() + " should not be copied.");

		// Check if the directory in the directory inside the destination directory was copied.
		final File expectedDeepestDir = new File(expectedDeeperDir, deepestDirName);
		assertTrue(expectedDeepestDir.exists(),
			"Directory " + expectedDeepestDir.getAbsolutePath() + " should be copied.");
		// Check if the first file in the deeper directory inside the directory from the destination
		// directory was copied.
		final File expectedDeepestFile1 = new File(expectedDeepestDir, srcDeepestFileName1);
		assertTrue(expectedDeepestFile1.exists(),
			"File " + expectedDeepestFile1.getAbsolutePath() + " should be copied.");

		// Check if the second file in the deeper directory inside the directory from the
		// destination directory was copied.
		final File expectedDeepestFile2 = new File(expectedDeepestDir, srcDeepestFileName2);
		assertTrue(expectedDeepestFile2.exists(),
			"File " + expectedDeepestFile2.getAbsolutePath() + " should be copied.");

		// Check if the excluded file inside the deepest destination directory was not copied
		final File notExpectedDeepestFile3 = new File(expectedDeepestDir, srcDeepestFileName3);
		assertFalse(notExpectedDeepestFile3.exists(),
			"File " + notExpectedDeepestFile3.getAbsolutePath() + " should not be copied.");
	}

	/**
	 * Test method for
	 * {@link CopyDirectoryExtensions#copyDirectoryWithFilenameFilter(File, File, FilenameFilter, boolean)}
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
	@Test
	public void testCopyDirectoryWithFilenameFilter()
		throws IOException, FileIsNotADirectoryException, FileIsADirectoryException,
		FileIsSecurityRestrictedException, DirectoryAlreadyExistsException
	{
		// Test to copy the source directory to the destination directory.
		actual = CopyDirectoryExtensions.copyDirectoryWithFilenameFilter(srcDeepDir, destDir,
			filenameFilter, false);
		// Check if the destination directory was copied.
		assertTrue(actual, "Directory " + destDir.getAbsolutePath() + " should be copied.");
		// Check if the directory inside the destination directory was copied.
		final File expectedDeeperDir = new File(deeperDir, dirToCopyName);
		assertTrue(expectedDeeperDir.exists(),
			"Directory " + expectedDeeperDir.getAbsolutePath() + " should be copied.");
		// Check if the file in the directory inside the destination directory was copied.
		final File expectedDeeperFile = new File(expectedDeeperDir, filePrefix + txtSuffix);
		assertTrue(expectedDeeperFile.exists(),
			"File " + expectedDeeperFile.getAbsolutePath() + " should be copied.");
		final File notCopied1 = new File(expectedDeeperDir, filePrefix + rtfSuffix);
		assertFalse(notCopied1.exists(),
			"File " + notCopied1.getAbsolutePath() + " should not be copied.");
		// Check if the directory in the directory inside the destination directory was copied.
		final File expectedDeepestDir = new File(expectedDeeperDir, deepestDirName);
		assertTrue(expectedDeepestDir.exists(),
			"Directory " + expectedDeepestDir.getAbsolutePath() + " should be copied.");
		// Check if the file in the deeper directory inside the directory from the destination
		// directory was copied.
		final File expectedDeepestFile1 = new File(expectedDeepestDir, srcDeepestFileName1);
		assertTrue(expectedDeepestFile1.exists(),
			"File " + expectedDeepestFile1.getAbsolutePath() + " should be copied.");
		final File notExpectedDeepestFile2 = new File(expectedDeepestDir, srcDeepestFileName2);
		assertFalse(notExpectedDeepestFile2.exists(),
			"File " + notExpectedDeepestFile2.getAbsolutePath() + " should not be copied.");
	}

	/**
	 * Test method for
	 * {@link CopyDirectoryExtensions#copyDirectoryWithFilenameFilter(File, File, FilenameFilter, FilenameFilter, boolean)}
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
	@Test
	public void testCopyDirectoryWithFilenameFilters()
		throws IOException, FileIsNotADirectoryException, FileIsADirectoryException,
		FileIsSecurityRestrictedException, DirectoryAlreadyExistsException
	{
		// Test to copy the source directory to the destination directory.
		actual = CopyDirectoryExtensions.copyDirectoryWithFilenameFilter(srcDeepDir, destDir,
			includeFilenameFilter, excludeFilenameFilter, false);

		// Check if the destination directory was copied.
		assertTrue(actual, "Directory " + destDir.getAbsolutePath() + " should be copied.");
		// Check if the directory inside the destination directory was copied.
		final File expectedDeeperDir = new File(deeperDir, dirToCopyName);
		assertTrue(expectedDeeperDir.exists(),
			"Directory " + expectedDeeperDir.getAbsolutePath() + " should be copied.");
		// Check if the file in the first directory inside the destination directory was copied.
		final File expectedDeeperFile = new File(expectedDeeperDir, filePrefix + txtSuffix);
		assertTrue(expectedDeeperFile.exists(),
			"File " + expectedDeeperFile.getAbsolutePath() + " should be copied.");

		// Check if the file in the second directory inside the destination directory was copied.
		final File expectedDeeperFile2 = new File(expectedDeeperDir, filePrefix + rtfSuffix);
		assertTrue(expectedDeeperFile2.exists(),
			"File " + expectedDeeperFile2.getAbsolutePath() + " should be copied.");
		// Check if the excluded file inside the deeper destination directory was not copied
		final File notExpectedDeeperFile1 = new File(expectedDeeperDir, filePrefix + exeSuffix);
		assertFalse(notExpectedDeeperFile1.exists(),
			"File " + notExpectedDeeperFile1.getAbsolutePath() + " should not be copied.");
		// Check if the directory in the directory inside the destination directory was copied.
		final File expectedDeepestDir = new File(expectedDeeperDir, deepestDirName);
		assertTrue(expectedDeepestDir.exists(),
			"Directory " + expectedDeepestDir.getAbsolutePath() + " should be copied.");
		// Check if the first file in the deeper directory inside the directory from the destination
		// directory was copied.
		final File expectedDeepestFile1 = new File(expectedDeepestDir, srcDeepestFileName1);
		assertTrue(expectedDeepestFile1.exists(),
			"File " + expectedDeepestFile1.getAbsolutePath() + " should be copied.");

		// Check if the second file in the deeper directory inside the directory from the
		// destination directory was copied.
		final File expectedDeepestFile2 = new File(expectedDeepestDir, srcDeepestFileName2);
		assertTrue(expectedDeepestFile2.exists(),
			"File " + expectedDeepestFile2.getAbsolutePath() + " should be copied.");

		// Check if the excluded file inside the deepest destination directory was not copied
		final File notExpectedDeepestFile3 = new File(expectedDeepestDir, srcDeepestFileName3);
		assertFalse(notExpectedDeepestFile3.exists(),
			"File " + notExpectedDeepestFile3.getAbsolutePath() + " should not be copied.");
	}

	/**
	 * Test method for {@link CopyDirectoryExtensions}
	 */
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(CopyDirectoryExtensions.class);
	}

}
