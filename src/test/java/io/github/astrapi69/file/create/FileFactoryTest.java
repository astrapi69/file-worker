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
package io.github.astrapi69.file.create;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.meanbean.test.BeanTester;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.github.astrapi69.file.FileTestCase;
import io.github.astrapi69.file.delete.DeleteFileExtensions;
import io.github.astrapi69.file.system.SystemFileExtensions;

/**
 * The unit test class for the class {@link FileFactory}
 */
public class FileFactoryTest extends FileTestCase
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
	 * Test method for {@link FileFactory#newDirectories(java.util.Collection)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	@Test
	public void testNewDirectories() throws IOException
	{
		FileCreationState actual;
		FileCreationState expected;
		List<File> dirs;
		File dir1;
		File dir2;
		File dir3;
		// new scenario...
		dirs = new ArrayList<>();
		dir1 = new File("test1.dir");
		dir2 = new File("test2.dir");
		dir3 = new File("test3.dir");
		dirs.add(dir1);
		dirs.add(dir2);
		dirs.add(dir3);
		// if the directories exist delete them to prevent a DirectoryAlreadyExistsException
		for (final File dir : dirs)
		{
			if (dir.exists())
			{
				DeleteFileExtensions.delete(dir);
			}
		}
		actual = FileFactory.newDirectories(dirs);
		expected = FileCreationState.CREATED;
		assertEquals(expected, actual);

		for (final File dir : dirs)
		{
			assertTrue("directory should exist.", dir.exists());
			assertTrue("File object should be a directory.", dir.isDirectory());
		}
		// Finally delete the test directories...
		for (final File dir : dirs)
		{
			if (dir.exists())
			{
				DeleteFileExtensions.delete(dir);
			}
		}
	}


	/**
	 * Test method for
	 * {@link FileFactory#newDirectories(Path, java.nio.file.attribute.FileAttribute...)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	@Test
	public void testNewDirectoriesWithPath() throws IOException
	{
		boolean actual;
		boolean expected;
		Path dirPath;
		// new scenario...
		dirPath = Paths.get(this.testResources.getAbsolutePath(), "test", "dir", "last");
		new File(this.testResources, "testCreateDirectory");
		// if the directory exist delete it to prevent a DirectoryAlreadyExistsException.
		if (Files.exists(dirPath))
		{
			DeleteFileExtensions.delete(dirPath.toFile());
		}
		actual = FileFactory.newDirectories(dirPath);
		expected = true;
		assertEquals(expected, actual);

		actual = Files.exists(dirPath);
		assertEquals(expected, actual);

		actual = dirPath.toFile().isDirectory();
		assertEquals(expected, actual);

		// Finally delete the test directory...
		DeleteFileExtensions.delete(dirPath.toFile());
	}

	/**
	 * Test method for {@link FileFactory#newDirectory(File, String)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	@Test
	public void testNewDirectoryWithParentFileAndDirectoryName() throws IOException
	{

		File actual;
		File expected;
		File dir;
		// new scenario...
		dir = new File(this.testResources, "newFooBarDir");
		// if the directory exist delete it
		if (dir.exists())
		{
			DeleteFileExtensions.delete(dir);
		}
		actual = FileFactory.newDirectory(this.testResources, "newFooBarDir");
		expected = dir;
		assertEquals(actual, expected);
		assertTrue(actual.exists());
		// Finally delete the test directory...
		DeleteFileExtensions.delete(dir);
	}

	/**
	 * Test method for {@link FileFactory#newDirectory(File, String)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	@Test(expectedExceptions = RuntimeException.class)
	public void testNewDirectoryWithParentFileNotExistsAndDirectoryName() throws IOException
	{

		File actual;
		File expected;
		File dir;
		// new scenario...
		dir = new File(this.testResources, "newFooBarDir");
		// if the directory exist delete it
		if (dir.exists())
		{
			DeleteFileExtensions.delete(dir);
		}
		File parentNotExists = new File("tmp", "bla");
		actual = FileFactory.newDirectory(parentNotExists, "newFooBarDir");
	}

	/**
	 * Test method for {@link FileFactory#newDirectory(File, String)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	@Test(expectedExceptions = RuntimeException.class)
	public void testNewDirectoryWithParentFileAndDirectoryNameWhereParentIsNoDirectory()
		throws IOException
	{
		File parentDirectory;
		String filename;
		File file;

		File dir;
		// new scenario...
		parentDirectory = SystemFileExtensions.getTempDir();
		filename = "foobar";
		file = FileFactory.newFile(parentDirectory, filename);
		FileFactory.newDirectory(file, "newFooBarDir");
	}


	/**
	 * Test method for {@link FileFactory#newDirectory(String, String)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	@Test
	public void testNewDirectoryWithParentDirectoryAsStringAndDirectoryName() throws IOException
	{

		File actual;
		File expected;
		File dir;
		// new scenario...
		dir = new File(this.testResources, "newFooBarDir");
		// if the directory exist delete it
		if (dir.exists())
		{
			DeleteFileExtensions.delete(dir);
		}
		actual = FileFactory.newDirectory(this.testResources.getAbsolutePath(), "newFooBarDir");
		expected = dir;
		assertEquals(actual, expected);
		assertTrue(actual.exists());
		// Finally delete the test directory...
		DeleteFileExtensions.delete(dir);
	}

	/**
	 * Test method for {@link FileFactory#newDirectory(File)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	@Test
	public void testNewDirectory() throws IOException
	{
		FileCreationState state;
		boolean actual;
		boolean expected;
		File dir;
		// new scenario...
		dir = new File(this.testResources, "testCreateDirectory");
		// if the directory exist delete it
		if (dir.exists())
		{
			DeleteFileExtensions.delete(dir);
		}
		state = FileFactory.newDirectory(dir);
		assertEquals(state, FileCreationState.CREATED);

		actual = dir.exists();
		expected = true;
		assertEquals(expected, actual);

		actual = dir.isDirectory();
		assertEquals(expected, actual);

		// Finally delete the test directory...
		DeleteFileExtensions.delete(dir);
	}


	/**
	 * Test method for {@link FileFactory#newDirectory(File)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	@Test
	public void testNewDirectoryWithAbsolutePath() throws IOException
	{
		FileCreationState state;
		boolean actual;
		boolean expected;
		File dir;
		File directory;
		String absolutePath;
		// new scenario...
		dir = new File(this.testResources, "testCreateDirectory");
		absolutePath = dir.getAbsolutePath();
		// if the directory exist delete it
		if (dir.exists())
		{
			DeleteFileExtensions.delete(dir);
		}
		directory = FileFactory.newDirectory(absolutePath);

		assertTrue(directory.exists());
		assertTrue(directory.isDirectory());

		// Finally delete the test directories...
		DeleteFileExtensions.delete(dir);
		DeleteFileExtensions.delete(directory);
	}

	/**
	 * Test method for
	 * {@link FileFactory#newDirectory(Path, java.nio.file.attribute.FileAttribute...)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	@Test
	public void testNewDirectoryWithPath() throws IOException
	{
		boolean actual;
		boolean expected;
		Path dirPath;
		// new scenario...
		dirPath = Paths.get(this.testResources.getAbsolutePath(), "testCreateDirectory");
		new File(this.testResources, "testCreateDirectory");
		// if the directory exist delete it to prevent a DirectoryAlreadyExistsException.
		if (Files.exists(dirPath))
		{
			DeleteFileExtensions.delete(dirPath.toFile());
		}
		actual = FileFactory.newDirectory(dirPath);
		expected = true;
		assertEquals(expected, actual);

		actual = Files.exists(dirPath);
		assertEquals(expected, actual);

		actual = dirPath.toFile().isDirectory();
		assertEquals(expected, actual);

		// Finally delete the test directory...
		DeleteFileExtensions.delete(dirPath.toFile());
	}


	/**
	 * Test method for {@link FileFactory#newDirectory(File, String)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	@Test(expectedExceptions = RuntimeException.class)
	public void testNewFileWithParentAndFileNameWhereParentIsNoDirectory() throws IOException
	{
		File parentDirectory;
		String filename;
		File file;

		File dir;
		// new scenario...
		parentDirectory = SystemFileExtensions.getTempDir();
		filename = "foobar";
		file = FileFactory.newFile(parentDirectory, filename);
		FileFactory.newFile(file, "newFooBarDir");
	}

	/**
	 * Test method for {@link FileFactory#newFile(File, String)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	@Test
	public void testNewFileWithParentAndFileName() throws IOException
	{
		boolean actual;
		boolean expected;
		File parentDirectory;
		String filename;
		File file;

		parentDirectory = SystemFileExtensions.getTempDir();
		filename = "foo.txt";
		file = FileFactory.newFile(parentDirectory, filename);

		actual = file.exists();
		expected = true;
		assertEquals(expected, actual);
		// clean up
		if (file.exists())
		{
			DeleteFileExtensions.delete(file);
		}

		File newParent = new File(parentDirectory, "tmp");
		FileFactory.newDirectory(newParent);
		file = FileFactory.newFile(newParent, filename);

		actual = newParent.exists();
		assertEquals(expected, actual);

		actual = file.exists();
		assertEquals(expected, actual);
		// clean up
		if (file.exists())
		{
			DeleteFileExtensions.delete(file);
			DeleteFileExtensions.delete(newParent);
		}
	}


	/**
	 * Test method for {@link FileFactory#newFile(String, boolean)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	@Test
	public void testNewFileStringBoolean() throws IOException
	{
		String absolutePath;
		boolean createIfNotExists;
		File file;

		absolutePath = "/tmp/foo/bar/test.file";
		createIfNotExists = false;
		file = FileFactory.newFile(absolutePath, createIfNotExists);
		assertFalse(file.exists());

		createIfNotExists = true;
		file = FileFactory.newFile(absolutePath, createIfNotExists);
		assertTrue(file.exists());

		createIfNotExists = false;
		file = FileFactory.newFile(absolutePath, createIfNotExists);
		assertTrue(file.exists());
		// clean up
		DeleteFileExtensions.delete(file);
	}

	/**
	 * Test method for {@link FileFactory#newFile(String)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	@Test
	public void testNewFileString() throws IOException
	{
		String absolutePath;
		File file;

		absolutePath = "/tmp/foo/bar/test.file";
		file = FileFactory.newFile(absolutePath);
		assertFalse(file.exists());
		assertNotNull(file);
	}

	/**
	 * Test method for {@link FileFactory#newFile(FileInfo)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	@Test
	public void testNewFileFileInfo() throws IOException
	{
		String absolutePath;
		File file;
		FileInfo fileInfo;

		fileInfo = FileInfo.builder().path("/tmp/foo/bar").name("foo.txt").build();
		file = FileFactory.newFile(fileInfo);
		assertTrue(file.exists());
		assertNotNull(file);
		absolutePath = file.getAbsolutePath();
		assertEquals(absolutePath, "/tmp/foo/bar/foo.txt");
		FileInfo anotherFileInfo = FileInfo.toFileInfo(file);
		assertEquals(anotherFileInfo, fileInfo);
	}

	/**
	 * Test method for {@link FileFactory#newFile(String)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	@Test
	public void testMkParentDirs() throws IOException
	{
		String absolutePath;
		File file;

		absolutePath = "/tmp/foo/bar/bla/fasel/test.file";
		file = new File(absolutePath);

		FileFactory.mkParentDirs(file);
		assertNotNull(file);
		// clean up...
		DeleteFileExtensions.delete(file);
		DeleteFileExtensions.delete(new File("/tmp/foo/bar/bla/fasel"));
		DeleteFileExtensions.delete(new File("/tmp/foo/bar/bla"));
		DeleteFileExtensions.delete(new File("/tmp/foo/bar"));
		DeleteFileExtensions.delete(new File("/tmp/foo"));
	}

	/**
	 * Test method for {@link FileFactory#newFile(String, String)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	@Test
	public void testNewFileWithParentChild() throws IOException
	{
		boolean actual;
		boolean expected;
		File parentDirectory;
		String parentDirectoryPath;
		String filename;
		File file;

		parentDirectory = SystemFileExtensions.getTempDir();
		parentDirectoryPath = parentDirectory.getAbsolutePath();
		filename = "foo.txt";
		file = FileFactory.newFile(parentDirectoryPath, filename);

		actual = file.exists();
		expected = true;
		assertEquals(expected, actual);
		// clean up
		if (file.exists())
		{
			DeleteFileExtensions.delete(file);
		}

		File newParent = new File(parentDirectory, "tmp");
		assertEquals(FileFactory.newDirectory(newParent), FileCreationState.CREATED);
		file = FileFactory.newFile(newParent.getAbsolutePath(), filename);

		actual = newParent.exists();
		assertEquals(expected, actual);

		actual = file.exists();
		assertEquals(expected, actual);
		// clean up
		if (file.exists())
		{
			DeleteFileExtensions.delete(file);
			DeleteFileExtensions.delete(newParent);
		}
	}

	/**
	 * Test method for {@link FileFactory#newFile(File)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	@Test
	public void testNewFile() throws IOException
	{
		boolean actual;
		boolean expected;
		File file;

		file = new File("/tmp/foo/bar/test.file");
		// if the file exist delete it to prevent a DirectoryAlreadyExistsException.
		if (file.exists())
		{
			DeleteFileExtensions.delete(file);
		}

		FileCreationState state = FileFactory.newFile(file);
		assertEquals(state, FileCreationState.CREATED);

		actual = file.exists();
		expected = true;
		assertEquals(expected, actual);

		actual = file.isFile();
		assertEquals(expected, actual);
		// Finally delete the test file...
		DeleteFileExtensions.delete(file);
	}

	/**
	 * Test method for {@link FileFactory#newFiles(java.util.Collection)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	@Test
	public void testNewFiles() throws IOException
	{
		FileCreationState actual;
		FileCreationState expected;
		List<File> files;
		File file1;
		File file2;
		File file3;

		files = new ArrayList<>();
		file1 = new File("test1.file");
		file2 = new File("test2.file");
		file3 = new File("test3.file");
		files.add(file1);
		files.add(file2);
		files.add(file3);
		// if the files exist delete them to prevent a DirectoryAlreadyExistsException.
		for (final File file : files)
		{
			if (file.exists())
			{
				DeleteFileExtensions.delete(file);
			}
		}
		actual = FileFactory.newFiles(files);
		expected = FileCreationState.CREATED;
		assertEquals(actual, expected);
		for (final File file : files)
		{
			assertTrue("file should exist.", file.exists());
			assertTrue("File object should be a file.", file.isFile());
		}
		// Finally delete the test files...
		for (final File file : files)
		{
			if (file.exists())
			{
				DeleteFileExtensions.delete(file);
			}
		}
	}

	/**
	 * Test method for {@link FileFactory}
	 */
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(FileFactory.class);
	}
}
