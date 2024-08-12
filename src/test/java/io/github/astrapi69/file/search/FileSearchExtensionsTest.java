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

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;

import io.github.astrapi69.collection.set.SetFactory;
import io.github.astrapi69.file.FileTestCase;
import io.github.astrapi69.file.copy.CopyFileExtensions;
import io.github.astrapi69.file.create.FileFactory;
import io.github.astrapi69.file.delete.DeleteFileExtensions;
import io.github.astrapi69.file.write.StoreFileExtensions;
import io.github.astrapi69.io.file.filter.SuffixFileFilter;

/**
 * The unit test class for the class {@link FileSearchExtensions}.
 */
public class FileSearchExtensionsTest extends FileTestCase
{

	private Path testDirectory;

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
		// Create a temporary directory for testing
		testDirectory = Files.createTempDirectory("fileSearchTestDir");

		// Create some files and directories in the tempDir for testing


		// Create test files
		Files.createFile(testDirectory.resolve("logfile1.txt"));
		Files.createFile(testDirectory.resolve("logfile2.txt"));
		Files.createFile(testDirectory.resolve("logfile1.log"));
		Files.createFile(testDirectory.resolve("otherfile.txt"));

		// Create a subdirectory with files
		Path subDir = Files.createDirectory(testDirectory.resolve("subDir"));
		Files.createFile(subDir.resolve("logfile3.txt"));
		Files.createFile(subDir.resolve("logfile4.log"));
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
		// Clean up
		Files.walk(testDirectory).map(Path::toFile).forEach(File::delete);
	}

	/**
	 * Test method for
	 * {@link FileSearchExtensions#findFilesWithPrefixAndExtensionRecursive(File, String, String)}
	 */
	@Test
	public void testFindFilesWithPrefixAndExtensionRecursive_BasicMatch()
	{
		File dir = testDirectory.toFile();
		String prefix = "logfile";
		String extension = "txt";

		List<File> files = FileSearchExtensions.findFilesWithPrefixAndExtensionRecursive(dir,
			prefix, extension);

		assertEquals(3, files.size());
		assertTrue(files.stream().anyMatch(file -> file.getName().equals("logfile1.txt")));
		assertTrue(files.stream().anyMatch(file -> file.getName().equals("logfile2.txt")));
		assertTrue(files.stream().anyMatch(file -> file.getName().equals("logfile3.txt")));
	}

	/**
	 * Test method for
	 * {@link FileSearchExtensions#findFilesWithPrefixAndExtensionRecursive(File, String, String)}
	 */
	@Test
	public void testFindFilesWithPrefixAndExtensionRecursive_NoMatch()
	{
		File dir = testDirectory.toFile();
		String prefix = "nonexistent";
		String extension = "txt";

		List<File> files = FileSearchExtensions.findFilesWithPrefixAndExtensionRecursive(dir,
			prefix, extension);

		assertTrue(files.isEmpty());
	}

	/**
	 * Test method for
	 * {@link FileSearchExtensions#findFilesWithPrefixAndExtensionRecursive(File, String, String)}
	 */
	@Test
	public void testFindFilesWithPrefixAndExtensionRecursive_DifferentExtension() throws IOException
	{

		// Create a subdirectory with files
		File dir = testDirectory.toFile();
		String prefix = "logfile";
		String extension = "log";

		List<File> files = FileSearchExtensions.findFilesWithPrefixAndExtensionRecursive(dir,
			prefix, extension);

		assertEquals(2, files.size());
		assertTrue(files.stream().anyMatch(file -> file.getName().equals("logfile1.log")));
		assertTrue(files.stream().anyMatch(file -> file.getName().equals("logfile4.log")));
	}

	/**
	 * Test method for
	 * {@link FileSearchExtensions#findFilesWithPrefixAndExtensionRecursive(File, String, String)}
	 */
	@Test
	public void testFindFilesWithPrefixAndExtensionRecursive_EmptyDirectory() throws IOException
	{
		Path emptyDir = Files.createDirectory(testDirectory.resolve("emptyDir"));
		File dir = emptyDir.toFile();
		String prefix = "logfile";
		String extension = "txt";

		List<File> files = FileSearchExtensions.findFilesWithPrefixAndExtensionRecursive(dir,
			prefix, extension);

		assertTrue(files.isEmpty());
	}

	/**
	 * Test method for
	 * {@link FileSearchExtensions#findFilesWithPrefixAndExtensionRecursive(File, String, String)}
	 */
	@Test
	public void testFindFilesWithPrefixAndExtensionRecursive_MixedContent()
	{
		File dir = testDirectory.toFile();
		String prefix = "logfile";
		String extension = "txt";

		List<File> files = FileSearchExtensions.findFilesWithPrefixAndExtensionRecursive(dir,
			prefix, extension);

		assertEquals(3, files.size());
		assertTrue(files.stream().allMatch(file -> file.getName().startsWith("logfile")));
		assertTrue(files.stream().allMatch(file -> file.getName().endsWith(".txt")));
	}

	/**
	 * Test method for
	 * {@link FileSearchExtensions#findFilesWithPrefixAndExtension(File, String, String)}
	 */
	@Test
	void testFindFilesWithPrefixAndExtension_nonRecursive() throws IOException
	{

		Files.createFile(testDirectory.resolve("readme.txt"));
		Files.createFile(testDirectory.resolve("logfile_special.txt"));
		Files.createDirectory(testDirectory.resolve("subdir"));
		Files.createFile(testDirectory.resolve("subdir/logfile4.txt"));
		Files.createFile(testDirectory.resolve("subdir/logfile5.log"));
		// Test for finding files with prefix "logfile" and extension "txt" (non-recursive)
		List<File> files = FileSearchExtensions
			.findFilesWithPrefixAndExtension(testDirectory.toFile(), "logfile", "txt");
		assertEquals(3, files.size());
		assertTrue(files.stream().anyMatch(file -> file.getName().equals("logfile1.txt")));
		assertTrue(files.stream().anyMatch(file -> file.getName().equals("logfile2.txt")));
		assertTrue(files.stream().anyMatch(file -> file.getName().equals("logfile_special.txt")));
	}

	/**
	 * Test method for
	 * {@link FileSearchExtensions#findFilesWithPrefixAndExtension(File, String, String)}
	 */
	@Test
	public void testFindFilesWithPrefixAndExtension_BasicMatch()
	{
		File dir = testDirectory.toFile();
		String prefix = "logfile";
		String extension = "txt";

		List<File> files = FileSearchExtensions.findFilesWithPrefixAndExtension(dir, prefix,
			extension);

		assertEquals(2, files.size());
		assertTrue(files.stream().anyMatch(file -> file.getName().equals("logfile1.txt")));
		assertTrue(files.stream().anyMatch(file -> file.getName().equals("logfile2.txt")));
	}

	/**
	 * Test method for
	 * {@link FileSearchExtensions#findFilesWithPrefixAndExtension(File, String, String)}
	 */
	@Test
	public void testFindFilesWithPrefixAndExtension_NoMatch()
	{
		File dir = testDirectory.toFile();
		String prefix = "nonexistent";
		String extension = "txt";

		List<File> files = FileSearchExtensions.findFilesWithPrefixAndExtension(dir, prefix,
			extension);

		assertTrue(files.isEmpty());
	}

	/**
	 * Test method for
	 * {@link FileSearchExtensions#findFilesWithPrefixAndExtension(File, String, String)}
	 */
	@Test
	public void testFindFilesWithPrefixAndExtension_DifferentExtension()
	{
		File dir = testDirectory.toFile();
		String prefix = "logfile";
		String extension = "log";

		List<File> files = FileSearchExtensions.findFilesWithPrefixAndExtension(dir, prefix,
			extension);

		assertEquals(1, files.size());
		assertEquals("logfile1.log", files.get(0).getName());
	}

	/**
	 * Test method for
	 * {@link FileSearchExtensions#findFilesWithPrefixAndExtension(File, String, String)}
	 */
	@Test
	public void testFindFilesWithPrefixAndExtension_EmptyDirectory() throws IOException
	{
		Path emptyDir = Files.createDirectory(testDirectory.resolve("emptyDir"));
		File dir = emptyDir.toFile();
		String prefix = "logfile";
		String extension = "txt";

		List<File> files = FileSearchExtensions.findFilesWithPrefixAndExtension(dir, prefix,
			extension);

		assertTrue(files.isEmpty());
	}

	/**
	 * Test method for
	 * {@link FileSearchExtensions#findFilesWithPrefixAndExtension(File, String, String)}
	 */
	@Test
	public void testFindFilesWithPrefixAndExtension_DirectoryOnly()
	{
		File dir = testDirectory.toFile();
		String prefix = "subDir";
		String extension = "txt";

		List<File> files = FileSearchExtensions.findFilesWithPrefixAndExtension(dir, prefix,
			extension);

		assertTrue(files.isEmpty()); // subDir is a directory, should not be included
	}

	/**
	 * Test method for
	 * {@link FileSearchExtensions#findFilesWithPrefixAndExtension(File, String, String)}
	 */
	@Test
	public void testFindFilesWithPrefixAndExtension_RecursiveSearch()
	{
		File dir = testDirectory.toFile();
		String prefix = "logfile";
		String extension = "txt";

		List<File> files = FileSearchExtensions.findFilesWithPrefixAndExtension(dir, prefix,
			extension);

		assertEquals(2, files.size()); // Should only find files in the top directory

	}

	/**
	 * Test method for {@link FileSearchExtensions#findLineIndex(File, String)}
	 */
	@Test
	public void testFindLineIndex() throws IOException
	{

		File inputFile;
		File testFile;
		String searchString;
		int actual;
		int expected;
		File parent = PathFinder.getRelativePath(PathFinder.getSrcTestResourcesDir(), "resources");

		inputFile = FileFactory.newFile(parent, "test-csv-data.csv");
		testFile = FileFactory.newFile(parent, "test-find-line-data.csv");
		CopyFileExtensions.copyFile(inputFile, testFile);

		searchString = "\"Dimitri\",\"Vladim\",\"dim.vlad@gmail.com\"";
		actual = FileSearchExtensions.findLineIndex(testFile, searchString);
		expected = 2;
		assertEquals(expected, actual);

		searchString = "\"Dimitri\",\"Vladim\",\"dim.vlad@gmail.com\"";
		actual = FileSearchExtensions.findLineIndex(testFile, searchString);
		assertEquals(expected, actual);

		DeleteFileExtensions.deleteFile(testFile);
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
		StoreFileExtensions.toFile(testFile, "Its a beautifull day!!!");
		boolean contains = FileSearchExtensions.containsFile(new File("."), testFile);
		assertFalse(contains, "File should not exist in this directory.");
		contains = FileSearchExtensions.containsFile(this.testDir, testFile);
		assertTrue(contains, "File should not exist in this directory.");
		testFile.deleteOnExit();
	}

	/**
	 * Test method for {@link FileSearchExtensions#containsFile(File, String)}.
	 */
	@Test
	public void testContainsFileFileString() throws IOException
	{
		final File testFile = new File(this.testDir, "beautifull.txt");
		StoreFileExtensions.toFile(testFile, "Its a beautifull day!!!");
		boolean contains = FileSearchExtensions.containsFile(new File("."), testFile);
		assertFalse(contains, "File should not exist in this directory.");
		final String filename = testFile.getName();
		contains = FileSearchExtensions.containsFile(this.testDir, filename);
		assertTrue(contains, "File should not exist in this directory.");
	}

	/**
	 * Test method for {@link FileSearchExtensions#containsFileRecursive(File, File)}.
	 */
	@Test
	public void testContainsFileRecursive() throws IOException
	{
		final File testFile = new File(this.testDir.getAbsoluteFile(),
			"testContainsFileRecursives.txt");
		StoreFileExtensions.toFile(testFile, "Its a beautifull day!!!");

		final File testFile3 = new File(this.deepDir.getAbsoluteFile(),
			"testContainsFileRecursives.cvs");
		StoreFileExtensions.toFile(testFile3, "Its a beautifull evening!!!");
		final File currentDir = new File(".").getAbsoluteFile();
		boolean contains = FileSearchExtensions.containsFileRecursive(currentDir.getAbsoluteFile(),
			testFile);
		assertFalse(contains, "File should not exist in this directory.");
		contains = FileSearchExtensions.containsFileRecursive(this.testDir, testFile);
		assertTrue(contains, "File should not exist in this directory.");
		this.actual = FileSearchExtensions.containsFileRecursive(this.testDir, testFile3);
		assertTrue(this.actual);
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
		StoreFileExtensions.toFile(testFile1, "Its a beautifull day!!!");
		StoreFileExtensions.toFile(testFile2, "Its a beautifull evening!!!");
		StoreFileExtensions.toFile(testFile3, "Its a beautifull night!!!");
		StoreFileExtensions.toFile(testFile4, "Its a beautifull day!!!");
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
	@Test
	public void testFindAllFilesFileString() throws IOException
	{
		// 1. initialize expected files to search
		final File testFile1 = new File(this.testDir.getAbsoluteFile(),
			"testFindFilesRecursive.txt");
		final File testFile2 = new File(this.testDir.getAbsoluteFile(),
			"testFindFilesRecursive.tft");
		final File testFile3 = new File(this.deepDir, "testFindFilesRecursive.cvs");
		final File testFile4 = new File(this.deepDir, "testFindFilesRecursive.txt");
		StoreFileExtensions.toFile(testFile1, "Its a beautifull day!!!");
		StoreFileExtensions.toFile(testFile2, "Its a beautifull evening!!!");
		StoreFileExtensions.toFile(testFile3, "Its a beautifull night!!!");
		StoreFileExtensions.toFile(testFile4, "Its a beautifull day!!!");
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
		assertEquals(expected.size(), actual.size());
		for (final File file : expected)
		{
			assertTrue(actual.contains(file));
		}
		// 4. cleanup all files from this test
		DeleteFileExtensions.delete(fileList);
	}

	@Test
	public void testFindFilesWithPredicate() throws IOException
	{

		String suffix;
		Set<File> files;

		suffix = ".txt";

		final File testFile1 = new File(this.testDir, "testFindFilesFileString.txt");
		final File testFile2 = new File(this.testDir, "testFindFilesFileString.tft");
		final File testFile3 = new File(this.deepDir, "testFindFilesFileString.cvs");
		StoreFileExtensions.toFile(testFile1, "Its a beautifull day!!!");
		StoreFileExtensions.toFile(testFile2, "Its a beautifull evening!!!");
		StoreFileExtensions.toFile(testFile3, "Its a beautifull night!!!");
		files = FileSearchExtensions.findFiles(this.testDir, (file -> {
			if (file.isDirectory())
			{
				return false;
			}
			FileFilter fileFilter = SuffixFileFilter.of(suffix, false);
			return !fileFilter.accept(file);
		}));
		this.actual = files != null;
		assertTrue(this.actual);
		this.actual = files.size() == 1;
		assertTrue(this.actual);
		files = FileSearchExtensions.findFilesRecursive(this.testDir, (file -> {
			if (file.isDirectory())
			{
				return false;
			}
			FileFilter fileFilter = SuffixFileFilter.of(suffix, false);
			return !fileFilter.accept(file);
		}));
		this.actual = files != null;
		assertTrue(this.actual);
		this.actual = files.size() == 2;
		assertTrue(this.actual);
	}

	@Test
	public void testFindFilesFileFileFilter() throws IOException
	{
		String suffix;
		Set<File> files;

		suffix = ".txt";

		final File testFile1 = new File(this.testDir, "testFindFilesFileString.txt");
		final File testFile2 = new File(this.testDir, "testFindFilesFileString.tft");
		final File testFile3 = new File(this.deepDir, "testFindFilesFileString.cvs");
		StoreFileExtensions.toFile(testFile1, "Its a beautifull day!!!");
		StoreFileExtensions.toFile(testFile2, "Its a beautifull evening!!!");
		StoreFileExtensions.toFile(testFile3, "Its a beautifull night!!!");
		files = SetFactory.newHashSet();
		files = FileSearchExtensions.findFiles(this.testDir, files, SuffixFileFilter.of(suffix));
		this.actual = files.size() == 2;
		assertTrue(this.actual);
	}

	/**
	 * Test method for {@link FileSearchExtensions#findFiles(File, String)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testFindFilesFileString() throws IOException
	{
		final String test = "testFindFilesFileString.t*";

		final File testFile1 = new File(this.testDir, "testFindFilesFileString.txt");
		final File testFile2 = new File(this.testDir, "testFindFilesFileString.tft");
		final File testFile3 = new File(this.deepDir, "testFindFilesFileString.cvs");
		StoreFileExtensions.toFile(testFile1, "Its a beautifull day!!!");
		StoreFileExtensions.toFile(testFile2, "Its a beautifull evening!!!");
		StoreFileExtensions.toFile(testFile3, "Its a beautifull night!!!");
		final List<File> foundedFiles = FileSearchExtensions.findFiles(this.testDir, test);
		this.actual = foundedFiles.size() == 2;
		assertTrue(this.actual);
	}

	/**
	 * Test method for {@link FileSearchExtensions#findFilesRecursive(File, String)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testFindFilesRecursive() throws IOException
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
		StoreFileExtensions.toFile(testFile1, "Its a beautifull day!!!");
		StoreFileExtensions.toFile(testFile2, "Its a beautifull evening!!!");
		StoreFileExtensions.toFile(testFile3, "Its a beautifull night!!!");
		List<File> foundedFiles = FileSearchExtensions.findFilesRecursive(this.testDir, test);
		this.actual = foundedFiles.size() == 2;
		assertTrue(this.actual);
		for (final File expectedFile : expectedFiles)
		{
			this.actual = foundedFiles.contains(expectedFile);
			assertTrue(this.actual);
		}
		final String pattern = "*";

		foundedFiles = FileSearchExtensions.findFilesRecursive(this.testDir, pattern);

		this.actual = foundedFiles.size() == 3;
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

		StoreFileExtensions.toFile(testFile1, "Its a beautifull day!!!");
		StoreFileExtensions.toFile(testFile2, "Its a beautifull evening!!!");
		StoreFileExtensions.toFile(testFile3, "Its a beautifull night!!!");
		StoreFileExtensions.toFile(testFile4, "Its a beautifull morning!!!");
		StoreFileExtensions.toFile(testFile5, "She's a beautifull woman!!!");
		StoreFileExtensions.toFile(testFile6, "Its a beautifull street!!!");
		StoreFileExtensions.toFile(testFile7, "He's a beautifull man!!!");
		StoreFileExtensions.toFile(testFile8, "Its a beautifull city!!!");
		StoreFileExtensions.toFile(testFile9, "He's a beautifull boy!!!");
		expected.add(testFile1);
		expected.add(testFile3);
		expected.add(testFile6);
		expected.add(testFile8);
		final String[] txtExtension = { ".txt" };
		final List<File> compare = FileSearchExtensions.findFiles(this.testDir.getAbsolutePath(),
			txtExtension);
		this.actual = expected.size() == compare.size();
		assertTrue(this.actual);
		for (final File file : compare)
		{
			this.actual = expected.contains(file);
			assertTrue(this.actual);
		}
	}

	/**
	 * Test method for {@link FileSearchExtensions#findFilesWithFilter(File, String...)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testFindFileWithFileFilter() throws IOException
	{
		// 1. initialize expected files to search
		final File testFile1 = new File(this.testDir.getAbsoluteFile(),
			"testFindFilesRecursive.txt");
		final File testFile2 = new File(this.testDir.getAbsoluteFile(),
			"testFindFilesRecursive.tft");
		final File testFile3 = new File(this.deepDir, "testFindFilesRecursive.cvs");
		final File testFile4 = new File(this.deepDir, "testFindFilesRecursive.txt");
		StoreFileExtensions.toFile(testFile1, "Its a beautifull day!!!");
		StoreFileExtensions.toFile(testFile2, "Its a beautifull evening!!!");
		StoreFileExtensions.toFile(testFile3, "Its a beautifull night!!!");
		StoreFileExtensions.toFile(testFile4, "Its a beautifull day!!!");
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
		assertEquals(expected.size(), actual.size());
		for (final File file : expected)
		{
			assertTrue(actual.contains(file));
		}
		actual = FileSearchExtensions.findFilesWithFilter(this.testDir, "tft", "cvs");
		expected.clear();
		expected.add(testFile2);
		expected.add(testFile3);
		assertEquals(expected.size(), actual.size());
		for (final File file : expected)
		{
			assertTrue(actual.contains(file));
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
		StoreFileExtensions.toFile(testFile1, "Its a beautifull day!!!");
		StoreFileExtensions.toFile(testFile2, "Its a beautifull evening!!!");
		StoreFileExtensions.toFile(testFile3, "Its a beautifull night!!!");
		StoreFileExtensions.toFile(testFile4, "Its a beautifull day!!!");
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
		assertEquals(expected.size(), fileList.size());
		for (final File file : expected)
		{
			assertTrue(fileList.contains(file));
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
		StoreFileExtensions.toFile(testFile1, "Its a beautifull day!!!");
		StoreFileExtensions.toFile(testFile2, "Its a beautifull evening!!!");
		StoreFileExtensions.toFile(testFile3, "Its a beautifull night!!!");
		StoreFileExtensions.toFile(testFile4, "Its a beautifull day!!!");
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
		assertEquals(expected.size(), fileList.size());
		for (final File file : expected)
		{
			assertTrue(fileList.contains(file));
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
		assertTrue(this.actual);

		final String[] otherExtensions = { rtfExtension, cvsExtension };
		this.actual = FileSearchExtensions.match(filename, otherExtensions);
		assertFalse(this.actual);
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
