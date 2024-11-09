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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;

import io.github.astrapi69.file.FileTestCase;
import io.github.astrapi69.file.create.DirectoryFactory;
import io.github.astrapi69.file.create.model.FileCreationState;
import io.github.astrapi69.file.exception.FileIsNotADirectoryException;
import io.github.astrapi69.file.read.ReadFileExtensions;
import io.github.astrapi69.file.write.StoreFileExtensions;
import io.github.astrapi69.io.file.FileExtension;
import io.github.astrapi69.io.file.filter.MultiplyExtensionsFileFilter;
import io.github.astrapi69.io.file.filter.TxtFileFilter;
import io.github.astrapi69.io.file.namefilter.MultiplyExtensionsFilenameFilter;
import io.github.astrapi69.io.file.namefilter.SimpleFilenameFilter;

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
	 * Teardown method will be invoked after every unit test method in this class
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
	 * Test method for {@link CopyFileExtensions#copyFile(File, File)}
	 *
	 * @throws IOException
	 *             Is thrown if an error occurs by reading or writing.
	 */
	@Test
	public void testCopyFileFileFile() throws IOException
	{
		try
		{
			actual = CopyFileExtensions.copyFile(source, destination);
			assertFalse(actual);
		}
		catch (final Exception fnfe)
		{
			actual = fnfe instanceof FileNotFoundException;
			assertTrue(actual);
		}
		final String inputString = "Its a beautifull day!!!";
		StoreFileExtensions.toFile(source, inputString);

		actual = CopyFileExtensions.copyFile(source, destination);
		assertTrue(actual, "Source file " + source.getName()
			+ " was not copied in the destination file " + destination.getName() + ".");
		final String actual = ReadFileExtensions.fromFile(destination);
		final String expected = inputString;
		assertEquals(actual, expected);
	}

	/**
	 * Test method for {@link CopyFileExtensions#copyFile(File, File, boolean)}
	 *
	 * @throws IOException
	 *             Is thrown if an error occurs by reading or writing.
	 */
	@Test
	public void testCopyFileFileFileBoolean() throws IOException
	{
		try
		{
			actual = CopyFileExtensions.copyFile(source, destination, false);
			assertFalse(actual);
		}
		catch (final Exception fnfe)
		{
			actual = fnfe instanceof FileNotFoundException;
			assertTrue(actual);
		}
		final String inputString = "Its a beautifull day!!!";
		StoreFileExtensions.toFile(source, inputString);

		actual = CopyFileExtensions.copyFile(source, destination, false);
		assertTrue(actual, "Source file " + source.getName()
			+ " was not copied in the destination file " + destination.getName() + ".");
		final String actual = ReadFileExtensions.fromFile(destination);
		final String expected = inputString;
		assertEquals(actual, expected);
	}

	/**
	 * Test method for {@link CopyFileExtensions#copyFileToDirectory(File, File)}
	 *
	 * @throws FileIsNotADirectoryException
	 *             the file is not a directory exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testCopyFileToDirectoryFileFile() throws FileIsNotADirectoryException, IOException
	{
		// Try to copy the file srcFile into the destination directory.
		actual = CopyFileExtensions.copyFileToDirectory(srcFile, srcDeepDir);
		final File expectedCopiedFile = new File(srcDeepDir, filePrefix + txtSuffix);
		assertTrue(expectedCopiedFile.exists(),
			"File " + expectedCopiedFile.getAbsolutePath() + " should be copied.");

		// Check the long lastModified from the file that they are equal.
		assertTrue(srcFile.lastModified() == expectedCopiedFile.lastModified(),
			"long lastModified is not the same.");
	}

	/**
	 * Test method for {@link CopyFileExtensions#copyFileToDirectory(File, File, boolean)}
	 *
	 * @throws FileIsNotADirectoryException
	 *             the file is not a directory exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testCopyFileToDirectoryFileFileBoolean()
		throws FileIsNotADirectoryException, IOException
	{
		// Try to copy the file srcFile into the destination directory.
		actual = CopyFileExtensions.copyFileToDirectory(srcFile, srcDeepDir, false);
		final File expectedCopiedFile = new File(srcDeepDir, filePrefix + txtSuffix);
		assertTrue(expectedCopiedFile.exists(),
			"File " + expectedCopiedFile.getAbsolutePath() + " should be copied.");
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
		File backupFile = CopyFileExtensions.newBackupOf(srcDeepFile, StandardCharsets.UTF_8,
			StandardCharsets.UTF_8);
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
