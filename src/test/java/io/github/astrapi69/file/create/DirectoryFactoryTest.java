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
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.meanbean.test.BeanTester;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.github.astrapi69.file.FileTestCase;
import io.github.astrapi69.file.delete.DeleteFileExtensions;
import io.github.astrapi69.file.system.SystemFileExtensions;

/**
 * The unit test class for the class {@link DirectoryFactory}
 */
public class DirectoryFactoryTest extends FileTestCase
{

	@Test
	public void testNewTempDirectoryWithStringPrefix() throws IOException
	{
		String prefix = "test";
		FileAttribute<?>[] attrs = new FileAttribute<?>[0];

		File tempFile = DirectoryFactory.newTempDirectory(prefix, attrs);
		assertNotNull("The returned file should not be null", tempFile);
		assertTrue("The returned file should be a directory", tempFile.isDirectory());
		assertTrue("Directory name should start with the prefix",
			tempFile.getName().startsWith(prefix));

		// Clean up
		tempFile.delete();
	}

	@Test
	public void testNewTempDirectoryWithPathAndStringPrefix() throws IOException
	{
		Path dir = Paths.get(System.getProperty("java.io.tmpdir"));
		String prefix = "test";
		FileAttribute<?>[] attrs = new FileAttribute<?>[0];

		File tempFile = DirectoryFactory.newTempDirectory(dir, prefix, attrs);
		assertNotNull("The returned file should not be null", tempFile);
		assertTrue("The returned file should be a directory", tempFile.isDirectory());
		assertTrue("Directory should be created in the specified parent directory",
			tempFile.getParent().equals(dir.toString()));
		assertTrue("Directory name should start with the prefix",
			tempFile.getName().startsWith(prefix));

		// Clean up
		tempFile.delete();
	}

	@Test
	public void testNewTempDirWithStringPrefix() throws IOException
	{
		String prefix = "test";
		FileAttribute<?>[] attrs = new FileAttribute<?>[0];

		FileCreationState result = DirectoryFactory.newTempDir(prefix, attrs);
		assertNotNull("The result should not be null", result);
		assertEquals("The result should indicate success", FileCreationState.ALREADY_EXISTS,
			result);
		assertNotNull("The file in the result should not be null", result.getFile());
		assertTrue("The file should be a directory", result.getFile().isDirectory());

		// Clean up
		result.getFile().delete();
	}

	@Test
	public void testNewTempDirWithPathAndStringPrefix() throws IOException
	{
		Path dir = Paths.get(System.getProperty("java.io.tmpdir"));
		String prefix = "test";
		FileAttribute<?>[] attrs = new FileAttribute<?>[0];

		FileCreationState result = DirectoryFactory.newTempDir(dir, prefix, attrs);
		assertNotNull("The result should not be null", result);
		assertEquals("The result should indicate success", FileCreationState.ALREADY_EXISTS,
			result);
		assertNotNull("The file in the result should not be null", result.getFile());
		assertTrue("The file should be a directory", result.getFile().isDirectory());
		assertTrue("Directory should be created in the specified parent directory",
			result.getFile().getParent().equals(dir.toString()));

		// Clean up
		result.getFile().delete();
	}

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
	 * Test method for {@link DirectoryFactory#newDirectories(Collection)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	@Test
	public void testNewDirectories() throws IOException
	{
		Collection<FileCreationState> actual;
		Collection<FileCreationState> expected;
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
		actual = DirectoryFactory.newDirectories(dirs);
		expected = new ArrayList<>();
		final FileCreationState fileCreationState = FileCreationState.CREATED.setFile(dir3);
		expected.add(fileCreationState);
		expected.add(fileCreationState);
		expected.add(fileCreationState);
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
	 * {@link DirectoryFactory#newDirectories(Path, java.nio.file.attribute.FileAttribute...)}
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
		actual = DirectoryFactory.newDirectories(dirPath);
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
	 * Test method for
	 * {@link DirectoryFactory#newDirectories(Path, java.nio.file.attribute.FileAttribute...)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	@Test
	public void testNewDirectoriesQuietlyWithPath() throws IOException
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
		actual = DirectoryFactory.newDirectoriesQuietly(dirPath);
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
	 * Test method for {@link DirectoryFactory#newDirectory(File, String)}
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
		actual = DirectoryFactory.newDirectory(this.testResources, "newFooBarDir");
		expected = dir;
		assertEquals(actual, expected);
		assertTrue(actual.exists());
		// Finally delete the test directory...
		DeleteFileExtensions.delete(dir);
	}

	/**
	 * Test method for {@link DirectoryFactory#newDirectory(File, String)}
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
		actual = DirectoryFactory.newDirectory(parentNotExists, "newFooBarDir");
	}

	/**
	 * Test method for {@link DirectoryFactory#newDirectory(File, String)}
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
		DirectoryFactory.newDirectory(file, "newFooBarDir");
	}


	/**
	 * Test method for {@link DirectoryFactory#newDirectory(String, String)}
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
		actual = DirectoryFactory.newDirectory(this.testResources.getAbsolutePath(),
			"newFooBarDir");
		expected = dir;
		assertEquals(actual, expected);
		assertTrue(actual.exists());
		// Finally delete the test directory...
		DeleteFileExtensions.delete(dir);
	}

	/**
	 * Test method for {@link DirectoryFactory#newDirectory(File)}.
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
		state = DirectoryFactory.newDirectory(dir);
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
	 * Test method for {@link DirectoryFactory#newDirectory(File)}.
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
		directory = DirectoryFactory.newDirectory(absolutePath);

		assertTrue(directory.exists());
		assertTrue(directory.isDirectory());

		// Finally delete the test directories...
		DeleteFileExtensions.delete(dir);
		DeleteFileExtensions.delete(directory);
	}

	/**
	 * Test method for
	 * {@link DirectoryFactory#newDirectory(Path, java.nio.file.attribute.FileAttribute...)}
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
		actual = DirectoryFactory.newDirectory(dirPath);
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
	 * Test method for
	 * {@link DirectoryFactory#newDirectoryQuietly(Path, java.nio.file.attribute.FileAttribute...)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	@Test
	public void testNewDirectoryQuietlyWithPath() throws IOException
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
		actual = DirectoryFactory.newDirectoryQuietly(dirPath);
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
	 * Test method for {@link DirectoryFactory}
	 */
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(DirectoryFactory.class);
	}
}
