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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.argumentSet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.lang3.SystemUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.meanbean.test.BeanTester;

import io.github.astrapi69.file.FileTestCase;
import io.github.astrapi69.file.create.model.FileCreationState;
import io.github.astrapi69.file.create.model.FileInfo;
import io.github.astrapi69.file.delete.DeleteFileExtensions;

/**
 * The unit test class for the class {@link FileFactory}
 */
public class FileFactoryTest extends FileTestCase
{

	@TempDir
	Path tempDir;

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
	 * Test method for {@link FileFactory#newFile(File, String)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	@Test
	public void testNewFileWithParentAndFileNameWhereParentIsNoDirectory() throws IOException
	{
		Assertions.assertThrows(RuntimeException.class, () -> {

			File parentDirectory;
			String filename;
			File file;
			// ...
			// new scenario...
			parentDirectory = tempDir.toFile();
			filename = "foobar";
			file = FileFactory.newFile(parentDirectory, filename);
			FileFactory.newFile(file, "newFooBarDir");
		});
	}

	/**
	 * Test method for {@link FileFactory#newFile(File, String)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	@Test
	public void testNewFileWithParentAndFileNameWhereParentDoesntExist() throws IOException
	{
		String newDirectoryName;
		File newDirectory;
		File newFile;
		// new scenario...
		newDirectoryName = "foobar";
		// the copy of src/test/resources in the temporary directory of FileTestCase (#99)
		newDirectory = new File(this.test, newDirectoryName);
		assertFalse(newDirectory.exists());
		newFile = FileFactory.newFile(newDirectory, "newFooBarFile");
		assertTrue(newFile.exists());
		// clean up
		DeleteFileExtensions.delete(newFile);
		DeleteFileExtensions.delete(newDirectory);
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

		parentDirectory = tempDir.toFile();
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
		DirectoryFactory.newDirectory(newParent);
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
		String actual;
		String expected;
		String rootPath;
		String absoluteFilePath;
		String filename;

		rootPath = SystemUtils.IS_OS_WINDOWS ? "C:\\" : "/";

		absoluteFilePath = SystemUtils.IS_OS_WINDOWS
			? rootPath + "tmp\\foo\\bar"
			: rootPath + "tmp/foo/bar";
		filename = "foo.txt";
		actual = SystemUtils.IS_OS_WINDOWS ? "C:\\tmp\\foo\\bar\\foo.txt" : "/tmp/foo/bar/foo.txt";
		fileInfo = FileInfo.builder().path(absoluteFilePath).name(filename).build();
		file = FileFactory.newFile(fileInfo);
		assertTrue(file.exists());
		assertNotNull(file);
		expected = file.getAbsolutePath();
		assertEquals(expected, actual);
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
		String rootPath;

		rootPath = SystemUtils.IS_OS_WINDOWS ? "C:\\" : "/";

		absolutePath = rootPath + "tmp/foo/bar/bla/fasel/test.file";
		file = new File(absolutePath);

		DirectoryFactory.mkParentDirs(file);
		assertNotNull(file);
		// clean up...
		DeleteFileExtensions.delete(file);
		DeleteFileExtensions.delete(new File(rootPath + "/tmp/foo/bar/bla/fasel"));
		DeleteFileExtensions.delete(new File(rootPath + "/tmp/foo/bar/bla"));
		DeleteFileExtensions.delete(new File(rootPath + "/tmp/foo/bar"));
		DeleteFileExtensions.delete(new File(rootPath + "/tmp/foo"));
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

		parentDirectory = tempDir.toFile();
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
		assertEquals(DirectoryFactory.newDirectory(newParent), FileCreationState.CREATED);
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
		// in the temporary directory, not in the project directory (#99)
		file1 = tempDir.resolve("test1.file").toFile();
		file2 = tempDir.resolve("test2.file").toFile();
		file3 = tempDir.resolve("test3.file").toFile();
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
			assertTrue(file.exists(), "file should exist.");
			assertTrue(file.isFile(), "File object should be a file.");
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

	/**
	 * A creation of the file with the given name in the given directory, as the quiet factory
	 * methods of {@link FileFactory} offer it
	 */
	@FunctionalInterface
	interface QuietCreation
	{
		File create(File directory, String name);
	}

	/**
	 * A factory method that returns the file object for a path without creating the file
	 */
	@FunctionalInterface
	interface QuietFileObject
	{
		File newFile(String absolutePath);
	}

	static Stream<Named<QuietCreation>> quietCreations()
	{
		return Stream.of(
			Named.<QuietCreation> of("newFileQuietly(String, true)",
				(directory, name) -> FileFactory
					.newFileQuietly(new File(directory, name).getAbsolutePath(), true)),
			Named.<QuietCreation> of("newFileQuietly(File, String)", FileFactory::newFileQuietly),
			Named.<QuietCreation> of("newFileQuietly(String, String)",
				(directory, name) -> FileFactory.newFileQuietly(directory.getAbsolutePath(), name)),
			Named.<QuietCreation> of("newFileQuietly(FileInfo)",
				(directory, name) -> FileFactory.newFileQuietly(
					FileInfo.builder().path(directory.getAbsolutePath()).name(name).build())));
	}

	static Stream<Named<QuietFileObject>> quietFileObjectsWithoutCreation()
	{
		return Stream.of(
			Named.<QuietFileObject> of("newFileQuietly(String)", FileFactory::newFileQuietly),
			Named.<QuietFileObject> of("newFileQuietly(String, false)",
				absolutePath -> FileFactory.newFileQuietly(absolutePath, false)));
	}

	private File newRegularFile(String name) throws IOException
	{
		return Files.writeString(tempDir.resolve(name), "a regular file").toFile();
	}

	/**
	 * The kind of a file given to {@link FileFactory#newFiles(java.util.Collection)}
	 */
	enum FileKind
	{
		/** a file that exists already */
		EXISTING {
			@Override
			File in(Path directory, String name) throws IOException
			{
				return Files.writeString(directory.resolve(name), "existing").toFile();
			}
		},
		/** a file that does not exist and can be created */
		MISSING {
			@Override
			File in(Path directory, String name)
			{
				return directory.resolve(name).toFile();
			}
		},
		/** a file that cannot be created, because a regular file stands where its parent belongs */
		IMPOSSIBLE {
			@Override
			File in(Path directory, String name) throws IOException
			{
				File regularFile = Files.writeString(directory.resolve(name), "regular").toFile();
				return new File(new File(regularFile, "sub"), "child.txt");
			}
		};

		abstract File in(Path directory, String name) throws IOException;
	}

	private List<File> filesOfKinds(List<FileKind> kinds) throws IOException
	{
		List<File> files = new ArrayList<>();
		for (int index = 0; index < kinds.size(); index++)
		{
			files.add(kinds.get(index).in(tempDir, "file" + index + ".txt"));
		}
		return files;
	}

	static Stream<Arguments> fileKindsWithTheOverallState()
	{
		return Stream.of(
			argumentSet("one fails, the next is created",
				List.of(FileKind.IMPOSSIBLE, FileKind.MISSING), FileCreationState.FAILED),
			argumentSet("one exists, one fails, one exists",
				List.of(FileKind.EXISTING, FileKind.IMPOSSIBLE, FileKind.EXISTING),
				FileCreationState.FAILED),
			argumentSet("one is created, the next exists",
				List.of(FileKind.MISSING, FileKind.EXISTING), FileCreationState.CREATED),
			argumentSet("every file is created", List.of(FileKind.MISSING, FileKind.MISSING),
				FileCreationState.CREATED),
			argumentSet("every file exists", List.of(FileKind.EXISTING, FileKind.EXISTING),
				FileCreationState.ALREADY_EXISTS),
			argumentSet("an empty collection", List.of(), FileCreationState.ALREADY_EXISTS));
	}

	/**
	 * Test method for {@link FileFactory#newFiles(java.util.Collection)}
	 *
	 * @param kinds
	 *            the kinds of the files in the collection, in their order
	 * @param expected
	 *            the overall state
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	@ParameterizedTest
	@MethodSource("fileKindsWithTheOverallState")
	public void newFiles_reportsTheOverallState_failedBeforeCreatedBeforeAlreadyExists(
		List<FileKind> kinds, FileCreationState expected) throws IOException
	{
		List<File> files = filesOfKinds(kinds);

		FileCreationState state = FileFactory.newFiles(files);

		assertEquals(expected, state);
		for (int index = 0; index < kinds.size(); index++)
		{
			assertEquals(kinds.get(index) != FileKind.IMPOSSIBLE, files.get(index).isFile(),
				files.get(index).toString());
		}
	}

	/**
	 * Test method for {@link FileFactory#newFilesQuietly(java.util.Collection)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	@Test
	public void newFilesQuietly_reportsFailed_whenAFileBeforeTheLastFails() throws IOException
	{
		List<File> files = filesOfKinds(List.of(FileKind.IMPOSSIBLE, FileKind.MISSING));

		FileCreationState state = FileFactory.newFilesQuietly(files);

		assertEquals(FileCreationState.FAILED, state);
		assertTrue(files.get(1).isFile());
	}

	/**
	 * Test method for {@link FileFactory#newFileQuietly(String, boolean)},
	 * {@link FileFactory#newFileQuietly(File, String)},
	 * {@link FileFactory#newFileQuietly(String, String)} and
	 * {@link FileFactory#newFileQuietly(FileInfo)}
	 *
	 * @param quietCreation
	 *            the factory method under test
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("quietCreations")
	public void quietFactoryMethods_createTheFileAndReturnIt(QuietCreation quietCreation)
	{
		File file = quietCreation.create(tempDir.toFile(), "created.txt");

		assertEquals(tempDir.resolve("created.txt").toFile(), file);
		assertTrue(file.isFile());
	}

	/**
	 * Test method for {@link FileFactory#newFileQuietly(String)} and
	 * {@link FileFactory#newFileQuietly(String, boolean)}
	 *
	 * @param quietFileObject
	 *            the factory method under test
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("quietFileObjectsWithoutCreation")
	public void quietFactoryMethods_returnTheFileWithoutCreatingIt_whenNotAskedTo(
		QuietFileObject quietFileObject)
	{
		String absolutePath = tempDir.resolve("not-created.txt").toString();

		File file = quietFileObject.newFile(absolutePath);

		assertEquals(new File(absolutePath), file);
		assertFalse(file.exists());
	}

	/**
	 * Test method for {@link FileFactory#newFileQuietly(File)}
	 */
	@Test
	public void newFileQuietly_createsAMissingFileWithItsParents()
	{
		File file = tempDir.resolve("parent").resolve("created.txt").toFile();

		FileCreationState state = FileFactory.newFileQuietly(file);

		assertEquals(FileCreationState.CREATED, state);
		assertTrue(file.isFile());
	}

	/**
	 * Test method for {@link FileFactory#newFileQuietly(File)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	@Test
	public void newFileQuietly_leavesAnExistingFileAsItIs() throws IOException
	{
		File file = newRegularFile("existing.txt");

		FileCreationState state = FileFactory.newFileQuietly(file);

		assertEquals(FileCreationState.ALREADY_EXISTS, state);
		assertEquals("a regular file", Files.readString(file.toPath()));
	}

	/**
	 * Test method for {@link FileFactory#newFile(File)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	@Test
	public void newFile_reportsFailed_whenTheParentDirectoriesCannotBeCreated() throws IOException
	{
		File regularFile = newRegularFile("regular.txt");
		File file = new File(new File(regularFile, "sub"), "child.txt");

		FileCreationState state = FileFactory.newFile(file);

		assertEquals(FileCreationState.FAILED, state);
		assertEquals("a regular file", Files.readString(regularFile.toPath()));
	}

	/**
	 * Test method for {@link FileFactory#newFileQuietly(File)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	@Test
	public void newFileQuietly_wrapsTheIOException_whenTheParentIsARegularFile() throws IOException
	{
		File file = new File(newRegularFile("regular.txt"), "child.txt");

		RuntimeException exception = assertThrows(RuntimeException.class,
			() -> FileFactory.newFileQuietly(file));

		assertInstanceOf(IOException.class, exception.getCause());
	}

	/**
	 * Test method for {@link FileFactory#newFileQuietly(File, String)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	@Test
	public void newFileQuietlyInParent_wrapsTheRefusal_whenTheParentIsARegularFile()
		throws IOException
	{
		File regularFile = newRegularFile("regular.txt");

		RuntimeException exception = assertThrows(RuntimeException.class,
			() -> FileFactory.newFileQuietly(regularFile, "child.txt"));

		assertEquals("Given parent file is not a directory", exception.getCause().getMessage());
	}

	/**
	 * Test method for {@link FileFactory#newFilesQuietly(java.util.Collection)}
	 */
	@Test
	public void newFilesQuietly_createsEveryFileAndReportsCreated()
	{
		List<File> files = List.of(tempDir.resolve("first.txt").toFile(),
			tempDir.resolve("sub").resolve("second.txt").toFile());

		FileCreationState state = FileFactory.newFilesQuietly(files);

		assertEquals(FileCreationState.CREATED, state);
		files.forEach(file -> assertTrue(file.isFile(), file.toString()));
	}

	/**
	 * Test method for {@link FileFactory#newFilesQuietly(java.util.Collection)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	@Test
	public void newFilesQuietly_reportsAlreadyExists_whenEveryFileExists() throws IOException
	{
		List<File> files = List.of(newRegularFile("first.txt"), newRegularFile("second.txt"));

		FileCreationState state = FileFactory.newFilesQuietly(files);

		assertEquals(FileCreationState.ALREADY_EXISTS, state);
		for (File file : files)
		{
			assertEquals("a regular file", Files.readString(file.toPath()));
		}
	}
}
