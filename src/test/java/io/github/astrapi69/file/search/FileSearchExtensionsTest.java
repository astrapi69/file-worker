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
package io.github.astrapi69.file.search;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.meanbean.test.BeanTester;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.github.astrapi69.file.FileTestCase;
import io.github.astrapi69.file.delete.DeleteFileExtensions;
import io.github.astrapi69.file.exceptions.DirectoryAlreadyExistsException;
import io.github.astrapi69.file.write.WriteFileExtensions;

/**
 * The unit test class for the class {@link FileSearchExtensions}.
 */
public class FileSearchExtensionsTest extends FileTestCase
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
	 * Test method for {@link FileSearchExtensions#getRootDirectory(File)}
	 */
	@Test
	public void testGetParentRoot()
	{
		final File parentRoot = FileSearchExtensions
			.getRootDirectory(PathFinder.getProjectDirectory());
		assertNotNull(parentRoot);
	}

	/**
	 * Test method for {@link FileSearchExtensions#containsFile(File, File)}.
	 */
	@Test
	public void testContainsFileFileFile() throws IOException
	{
		final File testFile = new File(this.testDir, "beautifull.txt");
		WriteFileExtensions.string2File(testFile, "Its a beautifull day!!!");
		boolean contains = FileSearchExtensions.containsFile(new File("."), testFile);
		assertFalse("File should not exist in this directory.", contains);
		contains = FileSearchExtensions.containsFile(this.testDir, testFile);
		assertTrue("File should not exist in this directory.", contains);
		testFile.deleteOnExit();
	}

	/**
	 * Test method for {@link FileSearchExtensions#containsFile(File, String)}.
	 */
	@Test
	public void testContainsFileFileString() throws IOException
	{
		final File testFile = new File(this.testDir, "beautifull.txt");
		WriteFileExtensions.string2File(testFile, "Its a beautifull day!!!");
		boolean contains = FileSearchExtensions.containsFile(new File("."), testFile);
		assertFalse("File should not exist in this directory.", contains);
		final String filename = testFile.getName();
		contains = FileSearchExtensions.containsFile(this.testDir, filename);
		assertTrue("File should not exist in this directory.", contains);
	}

	/**
	 * Test method for {@link FileSearchExtensions#containsFileRecursive(File, File)}.
	 */
	@Test
	public void testContainsFileRecursive() throws IOException
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

	/**
	 * Test method for {@link FileSearchExtensions#countAllFilesInDirectory(File, long, boolean)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testCountAllFilesInDirectory() throws IOException
	{
		long actual;
		long expected;
		// initialize files to count...
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
		// this list is kept for clean up...
		final List<File> fileList = new ArrayList<>();
		fileList.add(testFile1);
		fileList.add(testFile2);
		fileList.add(testFile3);
		fileList.add(testFile4);
		// count only files ...
		actual = FileSearchExtensions.countAllFilesInDirectory(this.testDir, 0, false);
		expected = 4;
		assertEquals(actual, expected);
		// count files and directories...
		actual = FileSearchExtensions.countAllFilesInDirectory(this.testDir, 0, true);
		expected = 7;
		assertEquals(actual, expected);
		// clean up...
		DeleteFileExtensions.delete(fileList);
	}

	/**
	 * Test method for {@link FileSearchExtensions#findAllFiles(File, String)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test(enabled = true)
	public void testFindAllFilesFileString() throws IOException
	{
		// 1. initialize expected files to search
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
		// this list is kept for clean up...
		final List<File> fileList = new ArrayList<>();
		fileList.add(testFile1);
		fileList.add(testFile2);
		fileList.add(testFile3);
		fileList.add(testFile4);
		// this list is expected as result...
		final List<File> expected = new ArrayList<>();
		expected.add(testFile1);
		expected.add(testFile4);

		// 2. run the actual method to test
		final long start = System.currentTimeMillis();
		final List<File> actual = FileSearchExtensions.findAllFiles(this.testDir, ".*txt");
		final long end = System.currentTimeMillis();
		final long executionTime = end - start;
		System.out.println("execution:" + executionTime);
		// 3. assert that expected with actual match
		assertTrue("", expected.size() == actual.size());
		for (final File file : expected)
		{
			assertTrue("", actual.contains(file));
		}
		// 4. cleanup all files from this test
		DeleteFileExtensions.delete(fileList);
	}

	/**
	 * Test method for {@link FileSearchExtensions#findFiles(File, String)}
	 *
	 * @throws DirectoryAlreadyExistsException
	 *             the directory allready exists exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testFindFilesFileString() throws DirectoryAlreadyExistsException, IOException
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

	/**
	 * Test method for {@link FileSearchExtensions#findFilesRecursive(File, String)}
	 *
	 * @throws DirectoryAlreadyExistsException
	 *             the directory allready exists exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testFindFilesRecursive() throws DirectoryAlreadyExistsException, IOException
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

	/**
	 * Test method for {@link FileSearchExtensions#findFiles(String, String[])}
	 */
	@Test
	public void testFindFilesStringStringArray() throws IOException
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
	 * Test method for {@link FileSearchExtensions#findFilesWithFilter(File, String...)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test(enabled = true)
	public void testFindFileWithFileFilter() throws IOException
	{
		// 1. initialize expected files to search
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
		// this list is kept for clean up...
		final List<File> fileList = new ArrayList<>();
		fileList.add(testFile1);
		fileList.add(testFile2);
		fileList.add(testFile3);
		fileList.add(testFile4);
		// this list is expected as result...
		final List<File> expected = new ArrayList<>();
		expected.add(testFile1);
		expected.add(testFile4);

		// 2. run the actual method to test
		List<File> actual = FileSearchExtensions.findFilesWithFilter(this.testDir, ".txt");
		// 3. assert that expected with actual match
		assertTrue("", expected.size() == actual.size());
		for (final File file : expected)
		{
			assertTrue("", actual.contains(file));
		}
		actual = FileSearchExtensions.findFilesWithFilter(this.testDir, "tft", "cvs");
		expected.clear();
		expected.add(testFile2);
		expected.add(testFile3);
		assertTrue("", expected.size() == actual.size());
		for (final File file : expected)
		{
			assertTrue("", actual.contains(file));
		}
		// 4. cleanup all files from this test
		DeleteFileExtensions.delete(fileList);
	}

	/**
	 * Test method for {@link FileSearchExtensions#getAllFilesFromDir(File)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testGetAllFilesFromDir() throws IOException
	{
		// initialize files to count...
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
		// this list is kept for clean up...
		final List<File> fileList = new ArrayList<>();
		fileList.add(testFile1);
		fileList.add(testFile2);
		fileList.add(testFile3);
		fileList.add(testFile4);
		List<File> allFilesFromDir = FileSearchExtensions.getAllFilesFromDir(testDir);
		assertNotNull(allFilesFromDir);
		// this list is expected as result...
		final List<File> expected = new ArrayList<>();
		expected.add(testFile1);
		expected.add(testFile2);
		expected.add(testFile3);
		expected.add(testFile4);
		assertTrue("", expected.size() == fileList.size());
		for (final File file : expected)
		{
			assertTrue("", fileList.contains(file));
		}
		// clean up...
		DeleteFileExtensions.delete(fileList);
	}

	/**
	 * Test method for {@link FileSearchExtensions#getAllFilesFromDirRecursive(File)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testGetAllFilesFromDirRecursive() throws IOException
	{
		// initialize files to count...
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
		// this list is kept for clean up...
		final List<File> fileList = new ArrayList<>();
		fileList.add(testFile1);
		fileList.add(testFile2);
		fileList.add(testFile3);
		fileList.add(testFile4);
		List<File> allFilesFromDir = FileSearchExtensions.getAllFilesFromDirRecursive(testDir);
		assertNotNull(allFilesFromDir);
		// this list is expected as result...
		final List<File> expected = new ArrayList<>();
		expected.add(testFile1);
		expected.add(testFile2);
		expected.add(testFile3);
		expected.add(testFile4);
		assertTrue("", expected.size() == fileList.size());
		for (final File file : expected)
		{
			assertTrue("", fileList.contains(file));
		}
		// clean up...
		DeleteFileExtensions.delete(fileList);
	}

	/**
	 * Test method for {@link FileSearchExtensions#getFileLengthInKilobytes(File)}.
	 */
	@Test
	public void testGetFileLengthInKilobytes()
	{
		long actual;
		actual = FileSearchExtensions.getFileLengthInKilobytes(testDir);
		assertTrue(0 < actual);
	}

	/**
	 * Test method for {@link FileSearchExtensions#getFileLengthInMegabytes(File)}.
	 */
	@Test
	public void testGetFileLengthInMegabytes()
	{
		long actual;
		actual = FileSearchExtensions.getFileLengthInMegabytes(testDir);
		assertTrue(0 < actual);
	}

	/**
	 * Test method for {@link FileSearchExtensions#getSearchFilePattern(String[])}.
	 */
	@Test
	public void testGetSearchFilePattern()
	{
		String actual;
		String expected;

		actual = FileSearchExtensions.getSearchFilePattern("txt");
		expected = "([^\\s]+(\\.(?i)(txt))$)";
		assertEquals(actual, expected);

		actual = FileSearchExtensions.getSearchFilePattern("img", "png");
		expected = "([^\\s]+(\\.(?i)(img|png))$)";
		assertEquals(actual, expected);
	}

	/**
	 * Test method for {@link FileSearchExtensions#match(String, String[])}.
	 */
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

	/**
	 * Test method for {@link FileSearchExtensions}
	 */
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(FileSearchExtensions.class);
	}

}
