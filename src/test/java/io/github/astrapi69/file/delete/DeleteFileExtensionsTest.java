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
package io.github.astrapi69.file.delete;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.meanbean.test.BeanTester;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.github.astrapi69.file.FileTestCase;
import io.github.astrapi69.collections.list.ListFactory;
import io.github.astrapi69.file.create.FileCreationState;
import io.github.astrapi69.file.create.FileFactory;
import io.github.astrapi69.file.exceptions.DirectoryAlreadyExistsException;
import io.github.astrapi69.file.exceptions.FileDoesNotExistException;
import io.github.astrapi69.file.exceptions.FileIsNotADirectoryException;
import io.github.astrapi69.file.exceptions.FileIsSecurityRestrictedException;
import io.github.astrapi69.io.file.filter.MultiplyExtensionsFileFilter;
import io.github.astrapi69.io.file.filter.TxtFileFilter;
import io.github.astrapi69.io.file.namefilter.MultiplyExtensionsFilenameFilter;
import io.github.astrapi69.file.write.WriteFileExtensions;

/**
 * The unit test class for the class {@link DeleteFileExtensions}
 */
public class DeleteFileExtensionsTest extends FileTestCase
{

	/**
	 * Sets up method will be invoked before every unit test method in this class.
	 *
	 * @throws Exception
	 *             is thrown if an exception occurs
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
	 *             is thrown if an exception occurs
	 */
	@Override
	@AfterMethod
	protected void tearDown() throws Exception
	{
		super.tearDown();
	}

	/**
	 * Test method for {@link DeleteFileExtensions#checkFile(File)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 * @throws DirectoryAlreadyExistsException
	 *             is thrown if the directory all ready exists
	 */
	@Test
	public void testCheckFile() throws IOException, DirectoryAlreadyExistsException
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
			final FileCreationState state = FileFactory.newDirectory(this.testDir);
			assertTrue("The directory should be created.", state.equals(FileCreationState.CREATED));
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

	/**
	 * Test method for {@link DeleteFileExtensions#deleteAllFiles(File)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testDeleteAllFiles() throws IOException
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

	/**
	 * Test method for {@link DeleteFileExtensions#deleteAllFilesWithSuffix(File, String)}.
	 */
	@Test
	public void testDeleteAllFilesWithSuffix() throws DirectoryAlreadyExistsException, IOException
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


	/**
	 * Test method for {@link DeleteFileExtensions#deleteAllFilesWithPrefix(File, String)}
	 *
	 * @throws FileIsNotADirectoryException
	 *             Is thrown if the destination file is a directory.
	 * @throws IOException
	 *             Is thrown if an error occurs by reading or writing.
	 * @throws FileIsSecurityRestrictedException
	 *             Is thrown if the source file is security restricted.
	 */
	@Test
	public void testDeleteAllFilesWithPrefix()
		throws FileIsNotADirectoryException, FileIsSecurityRestrictedException, IOException
	{
		final File testFile1 = new File(this.testDir, "testDeleteAllFilesWithPrefix1.txt");
		final File testFile2 = new File(this.testDir, "testDeleteAllFilesWithPrefix2.txt");
		final File testFile3 = new File(this.deepDir, "testDeleteAllFilesWithPrefix3.txt");
		final File testFile4 = new File(this.testDir, "foo-bar4.tft");
		final File testFile5 = new File(this.deepDir, "testDeleteAllFilesWithPrefix5.cvs");
		WriteFileExtensions.string2File(testFile1, "Its a beautifull day!!!");
		WriteFileExtensions.string2File(testFile2, "Its a beautifull evening!!!");
		WriteFileExtensions.string2File(testFile3, "Its a beautifull night!!!");
		WriteFileExtensions.string2File(testFile4, "Its a beautifull morning!!!");
		WriteFileExtensions.string2File(testFile5, "She's a beautifull woman!!!");

		DeleteFileExtensions.deleteAllFilesWithPrefix(this.testDir, "testDeleteAllFilesWithPrefix");

		this.actual = testFile1.exists();
		assertFalse("", this.actual);
		this.actual = testFile2.exists();
		assertFalse("", this.actual);
		this.actual = testFile3.exists();
		assertFalse("", this.actual);
		this.actual = testFile4.exists();
		assertTrue("", this.actual);
		this.actual = testFile5.exists();
		assertFalse("", this.actual);
	}

	/**
	 * Test method for {@link DeleteFileExtensions#delete(Collection)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testDeleteCollectionOfFile() throws IOException
	{
		// 1. initialize expected files for deletion
		final File testFile1 = new File(this.testDir.getAbsoluteFile(),
			"testFindFilesRecursive.txt");
		final File testFile2 = new File(this.testDir.getAbsoluteFile(),
			"testFindFilesRecursive.tft");
		final File testFile3 = new File(this.deepDir, "testFindFilesRecursive.cvs");
		final File testFile4 = new File(this.deepDir, "testFindFilesRecursive.txt");
		WriteFileExtensions.string2File(testFile1, "Its a beautifull day!!!");
		WriteFileExtensions.string2File(testFile2, "Its a beautifull evening!!!");
		WriteFileExtensions.string2File(testFile3, "Its a beautifull night!!!");
		WriteFileExtensions.string2File(testFile4, "Its a beautifull day!!!");
		// this list is for deletion...
		final List<File> fileList = new ArrayList<>();
		fileList.add(testFile1);
		fileList.add(testFile2);
		fileList.add(testFile3);
		fileList.add(testFile4);

		DeleteFileExtensions.delete(fileList);

		assertFalse(testFile1.exists());
		assertFalse(testFile2.exists());
		assertFalse(testFile3.exists());
		assertFalse(testFile4.exists());
	}

	/**
	 * Test method for {@link DeleteFileExtensions#delete(File)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testDeleteFile() throws IOException
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
	}

	/**
	 * Test method for {@link DeleteFileExtensions#deleteFile(File)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testDeleteFile1() throws IOException
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
	 * Test method for {@link DeleteFileExtensions#deleteFiles(File)}.
	 *
	 * @throws IOException
	 */
	@Test
	public void testDeleteFiles() throws IOException
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

	/**
	 * Test method for {@link DeleteFileExtensions#deleteFilesWithFileFilter(File, FileFilter)}.
	 *
	 * @throws FileIsSecurityRestrictedException
	 * @throws IOException
	 * @throws FileIsNotADirectoryException
	 */
	@Test
	public void testDeleteFilesWithFileFilterFileFileFilter()
		throws FileIsNotADirectoryException, IOException, FileIsSecurityRestrictedException
	{
		final File source;
		final FileFilter includeFileFilter;

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
		source = this.deepDir;
		includeFileFilter = new TxtFileFilter();
		DeleteFileExtensions.deleteFilesWithFileFilter(source, includeFileFilter);

		actual = testFile1.exists();
		expected = true;
		assertEquals(actual, expected);

		actual = testFile3.exists();
		expected = false;
		assertEquals(actual, expected);

		actual = testFile5.exists();
		expected = true;
		assertEquals(actual, expected);

		// this list for clean up...
		final List<File> fileList = new ArrayList<>();
		fileList.add(testFile1);
		fileList.add(testFile2);
		fileList.add(testFile4);
		fileList.add(testFile5);

		DeleteFileExtensions.delete(fileList);

	}

	/**
	 * Test method for
	 * {@link DeleteFileExtensions#deleteFilesWithFileFilter(File, FileFilter, FileFilter)}.
	 *
	 * @throws FileIsNotADirectoryException
	 *             the file is not A directory exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws FileIsSecurityRestrictedException
	 *             the file is security restricted exception
	 */
	@Test
	public void testDeleteFilesWithFileFilterFileFileFilterFileFilter()
		throws FileIsNotADirectoryException, IOException, FileIsSecurityRestrictedException
	{
		final File source;
		final FileFilter includeFileFilter;
		FileFilter excludeFileFilter;
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
		source = this.testDir;
		includeFileFilter = new MultiplyExtensionsFileFilter(".txt", ".tft");
		excludeFileFilter = new MultiplyExtensionsFileFilter(".cvs", ".tft");
		DeleteFileExtensions.deleteFilesWithFileFilter(source, includeFileFilter,
			excludeFileFilter);

		actual = testFile1.exists();
		expected = false;
		assertEquals(actual, expected);

		actual = testFile2.exists();
		expected = false;
		assertEquals(actual, expected);

		actual = testFile3.exists();
		expected = true;
		assertEquals(actual, expected);

		actual = testFile4.exists();
		expected = true;
		assertEquals(actual, expected);

		actual = testFile5.exists();
		expected = true;
		assertEquals(actual, expected);

		// this list for clean up...
		final List<File> fileList = new ArrayList<>();
		fileList.add(testFile3);
		fileList.add(testFile4);
		fileList.add(testFile5);

		DeleteFileExtensions.delete(fileList);
	}

	/**
	 * Test method for
	 * {@link DeleteFileExtensions#deleteFilesWithFilenameFilter(File, FilenameFilter)}.
	 *
	 * @throws FileIsSecurityRestrictedException
	 * @throws IOException
	 * @throws FileIsNotADirectoryException
	 */
	@Test
	public void testDeleteFilesWithFilenameFilterFileFilenameFilter()
		throws FileIsNotADirectoryException, IOException, FileIsSecurityRestrictedException
	{
		final File source;
		final FilenameFilter includeFileFilter;
		boolean acceptDir;
		Collection<String> fileExtensions;


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
		source = this.deepDir;

		acceptDir = false;
		fileExtensions = ListFactory.newArrayList(".txt");
		includeFileFilter = new MultiplyExtensionsFilenameFilter(fileExtensions, acceptDir);

		DeleteFileExtensions.deleteFilesWithFilenameFilter(source, includeFileFilter);

		actual = testFile1.exists();
		expected = true;
		assertEquals(actual, expected);

		actual = testFile3.exists();
		expected = false;
		assertEquals(actual, expected);

		actual = testFile5.exists();
		expected = true;
		assertEquals(actual, expected);

		// this list for clean up...
		final List<File> fileList = new ArrayList<>();
		fileList.add(testFile1);
		fileList.add(testFile2);
		fileList.add(testFile4);
		fileList.add(testFile5);

		DeleteFileExtensions.delete(fileList);
	}

	/**
	 * Test method for
	 * {@link DeleteFileExtensions#deleteFilesWithFilenameFilter(File, FilenameFilter, FilenameFilter)}.
	 *
	 * @throws FileIsNotADirectoryException
	 *             the file is not A directory exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws FileIsSecurityRestrictedException
	 *             the file is security restricted exception
	 */
	@Test
	public void testDeleteFilesWithFilenameFilterFileFilenameFilterFilenameFilter()
		throws FileIsNotADirectoryException, IOException, FileIsSecurityRestrictedException
	{
		final File source;
		final FilenameFilter includeFileFilter;
		FilenameFilter excludeFileFilter;
		boolean acceptDir;

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
		source = this.testDir;
		acceptDir = true;
		includeFileFilter = new MultiplyExtensionsFilenameFilter(ListFactory.newArrayList(".txt"),
			acceptDir);

		excludeFileFilter = new MultiplyExtensionsFilenameFilter(
			ListFactory.newArrayList(".cvs", ".tft"), acceptDir);
		DeleteFileExtensions.deleteFilesWithFilenameFilter(source, includeFileFilter,
			excludeFileFilter);

		actual = testFile1.exists();
		expected = false;
		assertEquals(actual, expected);

		actual = testFile2.exists();
		expected = false;
		assertEquals(actual, expected);

		actual = testFile3.exists();
		expected = true;
		assertEquals(actual, expected);

		actual = testFile4.exists();
		expected = true;
		assertEquals(actual, expected);

		actual = testFile5.exists();
		expected = true;
		assertEquals(actual, expected);

		// this list for clean up...
		final List<File> fileList = new ArrayList<>();
		fileList.add(testFile3);
		fileList.add(testFile4);
		fileList.add(testFile5);

		DeleteFileExtensions.delete(fileList);
	}

	/**
	 * Test method for {@link DeleteFileExtensions}
	 */
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(DeleteFileExtensions.class);
	}

}
