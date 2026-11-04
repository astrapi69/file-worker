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
package io.github.astrapi69.file.create.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import io.github.astrapi69.file.create.model.FileCreationState;

/**
 * The unit test class for the default methods of the interface {@link Createable}
 */
public class CreateableTest
{

	private final Createable createable = new Createable()
	{
	};

	@TempDir
	Path tempDir;

	/**
	 * Test method for {@link Createable#newDirectories(Collection)}
	 */
	@Test
	public void newDirectories_createsEveryDirectoryOfTheCollection()
	{
		List<File> directories = List.of(tempDir.resolve("first").toFile(),
			tempDir.resolve("second").toFile());

		Collection<FileCreationState> states = createable.newDirectories(directories);

		assertEquals(List.of(FileCreationState.CREATED, FileCreationState.CREATED),
			List.copyOf(states));
		directories.forEach(directory -> assertTrue(directory.isDirectory(), directory.toString()));
	}

	/**
	 * Test method for
	 * {@link Createable#newDirectories(Path, java.nio.file.attribute.FileAttribute...)}
	 *
	 * @throws IOException
	 *             Signals an I/O exception occurred
	 */
	@Test
	public void newDirectories_createsTheDirectoryWithEveryMissingParent() throws IOException
	{
		Path nested = tempDir.resolve("a").resolve("b").resolve("c");

		boolean created = createable.newDirectories(nested);

		assertTrue(created);
		assertTrue(Files.isDirectory(nested));
	}

	/**
	 * Test method for
	 * {@link Createable#newDirectory(Path, java.nio.file.attribute.FileAttribute...)}
	 *
	 * @throws IOException
	 *             Signals an I/O exception occurred
	 */
	@Test
	public void newDirectory_createsTheDirectoryOfThePath() throws IOException
	{
		Path directory = tempDir.resolve("single");

		boolean created = createable.newDirectory(directory);

		assertTrue(created);
		assertTrue(Files.isDirectory(directory));
	}

	/**
	 * Test method for
	 * {@link Createable#newDirectory(Path, java.nio.file.attribute.FileAttribute...)}
	 *
	 * @throws IOException
	 *             Signals an I/O exception occurred
	 */
	@Test
	public void newDirectory_throwsFileAlreadyExistsException_whenThePathExists() throws IOException
	{
		Path existing = Files.createDirectory(tempDir.resolve("existing"));

		FileAlreadyExistsException exception = assertThrows(FileAlreadyExistsException.class,
			() -> createable.newDirectory(existing));

		assertEquals(existing.toString(), exception.getFile());
	}

	/**
	 * Test method for {@link Createable#newDirectory(String)}
	 *
	 * @throws IOException
	 *             Signals an I/O exception occurred
	 */
	@Test
	public void newDirectory_createsAndReturnsTheDirectoryOfTheAbsolutePath() throws IOException
	{
		String absolutePath = tempDir.resolve("by-path").toString();

		File directory = createable.newDirectory(absolutePath);

		assertEquals(new File(absolutePath), directory);
		assertTrue(directory.isDirectory());
	}

	/**
	 * Test method for {@link Createable#newDirectory(File)}
	 */
	@Test
	public void newDirectory_reportsCreatedAndThenAlreadyExists()
	{
		File directory = tempDir.resolve("twice").toFile();

		FileCreationState first = createable.newDirectory(directory);
		FileCreationState second = createable.newDirectory(directory);

		assertEquals(FileCreationState.CREATED, first);
		assertEquals(FileCreationState.ALREADY_EXISTS, second);
		assertTrue(directory.isDirectory());
	}

	/**
	 * Test method for {@link Createable#newDirectory(File, String)}
	 */
	@Test
	public void newDirectory_createsTheNamedDirectoryInTheParent()
	{
		File directory = createable.newDirectory(tempDir.toFile(), "child");

		assertEquals(tempDir.resolve("child").toFile(), directory);
		assertTrue(directory.isDirectory());
	}

	/**
	 * Test method for {@link Createable#mkParentDirs(File)}
	 */
	@Test
	public void mkParentDirs_createsTheParentsButNotTheFile()
	{
		File file = tempDir.resolve("x").resolve("y").resolve("file.txt").toFile();

		boolean created = createable.mkParentDirs(file);

		assertTrue(created);
		assertTrue(file.getParentFile().isDirectory());
		assertFalse(file.exists());
	}

	/**
	 * Test method for {@link Createable#mkParentDirs(File)}
	 *
	 * @throws IOException
	 *             Signals an I/O exception occurred
	 */
	@Test
	public void mkParentDirs_returnsFalse_whenTheParentCannotBeCreated() throws IOException
	{
		File regularFile = Files.writeString(tempDir.resolve("regular.txt"), "regular").toFile();
		File file = new File(new File(regularFile, "sub"), "file.txt");

		boolean created = createable.mkParentDirs(file);

		assertFalse(created);
		assertFalse(file.getParentFile().exists());
		assertTrue(regularFile.isFile());
	}

	/**
	 * Test method for {@link Createable#newFile(File)}
	 *
	 * @throws IOException
	 *             Signals an I/O exception occurred
	 */
	@Test
	public void newFile_createsTheFileWithItsParents() throws IOException
	{
		File file = tempDir.resolve("parent").resolve("file.txt").toFile();

		FileCreationState state = createable.newFile(file);

		assertEquals(FileCreationState.CREATED, state);
		assertTrue(file.isFile());
	}

	/**
	 * Test method for {@link Createable#newFile(String)}
	 *
	 * @throws IOException
	 *             Signals an I/O exception occurred
	 */
	@Test
	public void newFile_returnsTheFileOfTheAbsolutePathWithoutCreatingIt() throws IOException
	{
		String absolutePath = tempDir.resolve("not-created.txt").toString();

		File file = createable.newFile(absolutePath);

		assertEquals(new File(absolutePath), file);
		assertFalse(file.exists());
	}

	/**
	 * Test method for {@link Createable#newFile(String, boolean)}
	 *
	 * @param createIfNotExists
	 *            the flag whether the file is to be created
	 * @throws IOException
	 *             Signals an I/O exception occurred
	 */
	@ParameterizedTest(name = "createIfNotExists = {0}")
	@ValueSource(booleans = { true, false })
	public void newFile_createsTheFileOfTheAbsolutePathOnlyWhenAsked(boolean createIfNotExists)
		throws IOException
	{
		String absolutePath = tempDir.resolve("maybe.txt").toString();

		File file = createable.newFile(absolutePath, createIfNotExists);

		assertEquals(new File(absolutePath), file);
		assertEquals(createIfNotExists, file.isFile());
	}

	/**
	 * Test method for {@link Createable#newFile(File, String)}
	 *
	 * @throws IOException
	 *             Signals an I/O exception occurred
	 */
	@Test
	public void newFile_createsTheNamedFileInTheParent() throws IOException
	{
		File file = createable.newFile(tempDir.toFile(), "child.txt");

		assertEquals(tempDir.resolve("child.txt").toFile(), file);
		assertTrue(file.isFile());
	}

	/**
	 * Test method for {@link Createable#newFiles(Collection)}
	 *
	 * @throws IOException
	 *             Signals an I/O exception occurred
	 */
	@Test
	public void newFiles_createsEveryFileOfTheCollection() throws IOException
	{
		List<File> files = List.of(tempDir.resolve("first.txt").toFile(),
			tempDir.resolve("second.txt").toFile());

		FileCreationState state = createable.newFiles(files);

		assertEquals(FileCreationState.CREATED, state);
		files.forEach(file -> assertTrue(file.isFile(), file.toString()));
	}

	/**
	 * Test method for {@link Createable#newFiles(Collection)}
	 *
	 * @throws IOException
	 *             Signals an I/O exception occurred
	 */
	@Test
	public void newFiles_reportsFailed_whenAFileBeforeTheLastFails() throws IOException
	{
		File regularFile = Files.writeString(tempDir.resolve("regular.txt"), "regular").toFile();
		File impossible = new File(new File(regularFile, "sub"), "child.txt");
		File fresh = tempDir.resolve("fresh.txt").toFile();

		FileCreationState state = createable.newFiles(List.of(impossible, fresh));

		assertEquals(FileCreationState.FAILED, state);
		assertTrue(fresh.isFile());
	}

}
