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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.meanbean.test.BeanTester;

import io.github.astrapi69.file.FileTestCase;
import io.github.astrapi69.file.create.model.FileCreationState;
import io.github.astrapi69.file.delete.DeleteFileExtensions;

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
		assertNotNull(tempFile, "The returned file should not be null");
		assertTrue(tempFile.isDirectory(), "The returned file should be a directory");
		assertTrue(tempFile.getName().startsWith(prefix),
			"Directory name should start with the prefix");

		// Clean up
		tempFile.delete();
	}

	@Test
	public void testNewTempDirectoryWithPathAndStringPrefix() throws IOException
	{
		Path dir = tempRoot;
		String prefix = "test";
		FileAttribute<?>[] attrs = new FileAttribute<?>[0];

		File tempFile = DirectoryFactory.newTempDirectory(dir, prefix, attrs);
		assertNotNull(tempFile, "The returned file should not be null");
		assertTrue(tempFile.isDirectory(), "The returned file should be a directory");
		assertTrue(tempFile.getParent().equals(dir.toString()),
			"Directory should be created in the specified parent directory");
		assertTrue(tempFile.getName().startsWith(prefix),
			"Directory name should start with the prefix");

		// Clean up
		tempFile.delete();
	}

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
	}

	/**
	 * Teardown method will be invoked after every unit test method in this class.
	 *
	 * @throws Exception
	 *             is thrown if an exception occurs
	 */
	@Override
	@AfterEach
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
		// in the temporary directory of FileTestCase, not in the working directory (#99)
		dir1 = new File(this.test, "test1.dir");
		dir2 = new File(this.test, "test2.dir");
		dir3 = new File(this.test, "test3.dir");
		dirs.add(dir1);
		dirs.add(dir2);
		dirs.add(dir3);
		actual = DirectoryFactory.newDirectories(dirs);
		expected = new ArrayList<>();
		expected.add(FileCreationState.CREATED);
		expected.add(FileCreationState.CREATED);
		expected.add(FileCreationState.CREATED);
		assertEquals(expected, actual);

		for (final File dir : dirs)
		{
			assertTrue(dir.exists(), "directory should exist.");
			assertTrue(dir.isDirectory(), "File object should be a directory.");
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
	@Test
	public void testNewDirectoryWithParentFileNotExistsAndDirectoryName() throws IOException
	{

		Assertions.assertThrows(RuntimeException.class, () -> {
			File dir;
			// new scenario...
			dir = new File(this.testResources, "newFooBarDir");
			// if the directory exist delete it
			if (dir.exists())
			{
				DeleteFileExtensions.delete(dir);
			}
			File parentNotExists = new File("tmp", "bla");
			DirectoryFactory.newDirectory(parentNotExists, "newFooBarDir");
		});
	}

	/**
	 * Test method for {@link DirectoryFactory#newDirectory(File, String)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	@Test
	public void testNewDirectoryWithParentFileAndDirectoryNameWhereParentIsNoDirectory()
		throws IOException
	{
		Assertions.assertThrows(RuntimeException.class, () -> {

			File parentDirectory;
			String filename;
			File file;

			File dir;
			// new scenario...
			parentDirectory = tempRoot.toFile();
			filename = "foobar";
			file = FileFactory.newFile(parentDirectory, filename);
			DirectoryFactory.newDirectory(file, "newFooBarDir");
		});
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
	 * Test method for {@link DirectoryFactory#newDirectoryQuietly(String, String)}
	 *
	 * @param existsBefore
	 *            whether the directory exists before the call
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	@ParameterizedTest(name = "existsBefore = {0}")
	@ValueSource(booleans = { true, false })
	public void newDirectoryQuietly_returnsTheNamedDirectoryAndCreatesItOnlyWhenMissing(
		boolean existsBefore) throws IOException
	{
		Path expected = tempRoot.resolve("quiet");
		Path marker = expected.resolve("marker.txt");
		if (existsBefore)
		{
			Files.createDirectory(expected);
			Files.writeString(marker, "kept");
		}

		File directory = DirectoryFactory.newDirectoryQuietly(tempRoot.toString(), "quiet");

		assertEquals(expected.toFile(), directory);
		assertTrue(directory.isDirectory());
		assertEquals(existsBefore, Files.exists(marker));
	}

	/**
	 * Test method for {@link DirectoryFactory#newDirectoryQuietly(String, String)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	@Test
	public void newDirectoryQuietly_throwsRuntimeException_whenTheParentIsARegularFile()
		throws IOException
	{
		Path regularFile = Files.writeString(tempRoot.resolve("regular.txt"), "regular");

		RuntimeException exception = assertThrows(RuntimeException.class,
			() -> DirectoryFactory.newDirectoryQuietly(regularFile.toString(), "quiet"));

		assertInstanceOf(IOException.class, exception.getCause());
		assertTrue(Files.isRegularFile(regularFile));
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
