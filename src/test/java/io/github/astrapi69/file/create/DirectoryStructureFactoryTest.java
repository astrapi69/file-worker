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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import io.github.astrapi69.collection.array.ArrayFactory;
import io.github.astrapi69.collection.list.ListFactory;
import io.github.astrapi69.file.create.model.DirectoryStructureTestData;
import io.github.astrapi69.file.create.model.FileContentInfo;
import io.github.astrapi69.file.create.model.FileInfo;
import io.github.astrapi69.file.delete.DeleteFileExtensions;

/**
 * The unit test class for the class {@link DirectoryStructureFactory}
 */
public class DirectoryStructureFactoryTest
{

	/**
	 * The temporary directory in which the directory structures are created
	 */
	@TempDir
	File tempDir;

	/**
	 * Test method for {@link DirectoryStructureFactory#newDirectoryStructure(Collection)}
	 */
	@Test
	public void testNewDirectoryStructureWithContent() throws IOException, NoSuchAlgorithmException
	{
		Collection<File> actual;
		Collection<File> expected;
		Collection<FileContentInfo> fileInfos;
		String parentAbsolutePath;
		// new scenario...
		File parentFile = DirectoryFactory.newDirectory(tempDir, "app");
		parentAbsolutePath = parentFile.getAbsolutePath();
		fileInfos = DirectoryStructureTestData.newTestData(parentAbsolutePath);
		expected = ListFactory.newArrayList();
		fileInfos.forEach(fileContentInfo1 -> expected.add(FileInfo.toFile(fileContentInfo1)));

		actual = DirectoryStructureFactory.newDirectoryStructure(fileInfos);
		assertEquals(expected, actual);
		// cleanup...
		actual.add(parentFile);
		DeleteFileExtensions.delete(actual);
	}


	/**
	 * Test method for {@link DirectoryStructureFactory#newDirectoryStructure(File, Collection)}
	 */
	@Test
	public void testNewDirectoryStructureFileCollection() throws IOException
	{
		Collection<File> actual;
		Collection<File> expected;
		Collection<String> filePaths;
		File parent;
		// new scenario...
		filePaths = ListFactory.newArrayList("action", "model", "state", "reducer", "reducer/main");
		parent = DirectoryFactory.newDirectory(tempDir, "app");
		actual = DirectoryStructureFactory.newDirectoryStructure(parent, filePaths);
		expected = ListFactory.newArrayList(DirectoryFactory.newDirectory(parent, "action"),
			DirectoryFactory.newDirectory(parent, "model"),
			DirectoryFactory.newDirectory(parent, "state"),
			DirectoryFactory.newDirectory(parent, "reducer"),
			DirectoryFactory.newDirectory(parent, "reducer/main"));
		assertEquals(expected, actual);
		DeleteFileExtensions.delete(actual);
	}

	/**
	 * Test method for {@link DirectoryStructureFactory#newDirectoryStructure(String, Collection)}
	 */
	@Test
	public void testNewDirectoryStructureStringCollection() throws IOException
	{
		Collection<File> actual;
		Collection<File> expected;
		Collection<String> filePaths;
		String parentAbsolutePath;
		File parent;
		// new scenario...
		filePaths = ListFactory.newArrayList("action", "model", "state", "reducer", "reducer/main");

		parent = DirectoryFactory.newDirectory(tempDir, "app");
		parentAbsolutePath = parent.getAbsolutePath();
		actual = DirectoryStructureFactory.newDirectoryStructure(parentAbsolutePath, filePaths);
		expected = ListFactory.newArrayList(DirectoryFactory.newDirectory(parent, "action"),
			DirectoryFactory.newDirectory(parent, "model"),
			DirectoryFactory.newDirectory(parent, "state"),
			DirectoryFactory.newDirectory(parent, "reducer"),
			DirectoryFactory.newDirectory(parent, "reducer/main"));
		assertEquals(expected, actual);
		DeleteFileExtensions.delete(actual);
	}

	/**
	 * Test method for {@link DirectoryStructureFactory#newDirectoryStructure(String, String...)}
	 */
	@Test
	public void testNewDirectoryStructureStringVararg() throws IOException
	{
		Collection<File> actual;
		Collection<File> expected;
		String[] filePaths;
		String parentAbsolutePath;
		File parent;
		// new scenario...
		filePaths = ArrayFactory.newArray("action", "model", "state", "reducer", "reducer/main");

		parent = DirectoryFactory.newDirectory(tempDir, "app");
		parentAbsolutePath = parent.getAbsolutePath();
		actual = DirectoryStructureFactory.newDirectoryStructure(parentAbsolutePath, filePaths);
		expected = ListFactory.newArrayList(DirectoryFactory.newDirectory(parent, "action"),
			DirectoryFactory.newDirectory(parent, "model"),
			DirectoryFactory.newDirectory(parent, "state"),
			DirectoryFactory.newDirectory(parent, "reducer"),
			DirectoryFactory.newDirectory(parent, "reducer/main"));
		assertEquals(expected, actual);
		DeleteFileExtensions.delete(actual);
	}

	/**
	 * Test method for {@link DirectoryStructureFactory#newDirectoryStructure(File, String...)}
	 */
	@Test
	public void testNewDirectoryStructureFileVararg() throws IOException
	{
		Collection<File> actual;
		Collection<File> expected;
		String[] filePaths;
		String parentAbsolutePath;
		File parent;
		// new scenario...
		filePaths = ArrayFactory.newArray("action", "model", "state", "reducer", "reducer/main");

		parent = DirectoryFactory.newDirectory(tempDir, "app");
		actual = DirectoryStructureFactory.newDirectoryStructure(parent, filePaths);
		expected = ListFactory.newArrayList(DirectoryFactory.newDirectory(parent, "action"),
			DirectoryFactory.newDirectory(parent, "model"),
			DirectoryFactory.newDirectory(parent, "state"),
			DirectoryFactory.newDirectory(parent, "reducer"),
			DirectoryFactory.newDirectory(parent, "reducer/main"));
		assertEquals(expected, actual);
		DeleteFileExtensions.delete(actual);
	}

	/**
	 * The overloads that take the parent as {@link File} object, each called with the parent and
	 * the directory names
	 *
	 * @return the name of each overload and the call of it
	 */
	static Stream<Arguments> fileOfTheParentOverloads()
	{
		return Stream.of(
			Arguments.of("File, String...",
				(BiFunction<File, List<String>, Collection<File>>)(parent,
					names) -> DirectoryStructureFactory.newDirectoryStructure(parent,
						names.toArray(new String[0]))),
			Arguments.of("File, Collection",
				(BiFunction<File, List<String>, Collection<File>>)(parent,
					names) -> DirectoryStructureFactory.newDirectoryStructure(parent, names)));
	}

	/**
	 * The overloads that take the absolute path of the parent, each called with the parent and the
	 * directory names
	 *
	 * @return the name of each overload and the call of it
	 */
	static Stream<Arguments> pathOfTheParentOverloads()
	{
		return Stream.of(
			Arguments.of("String, String...",
				(BiFunction<File, List<String>, Collection<File>>)(parent,
					names) -> DirectoryStructureFactory.newDirectoryStructure(
						parent.getAbsolutePath(), names.toArray(new String[0]))),
			Arguments.of("String, Collection",
				(BiFunction<File, List<String>, Collection<File>>)(parent,
					names) -> DirectoryStructureFactory
						.newDirectoryStructure(parent.getAbsolutePath(), names)));
	}

	/**
	 * Every overload that takes directory names
	 *
	 * @return the name of each overload and the call of it
	 */
	static Stream<Arguments> allOverloads()
	{
		return Stream.concat(fileOfTheParentOverloads(), pathOfTheParentOverloads());
	}

	/**
	 * Test that a nested path is created with its missing parents when the parent is given as
	 * {@link File} object
	 *
	 * @param overload
	 *            the name of the overload
	 * @param newDirectoryStructure
	 *            the call of the overload
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("fileOfTheParentOverloads")
	void fileOfTheParent(final String overload,
		final BiFunction<File, List<String>, Collection<File>> newDirectoryStructure)
	{
		File java = new File(tempDir, "src/main/java");

		Collection<File> created = newDirectoryStructure.apply(tempDir, List.of("src/main/java"));

		assertEquals(List.of(java), List.copyOf(created));
		assertTrue(java.isDirectory(), "src/main/java created: " + java.isDirectory()
			+ ", src created: " + new File(tempDir, "src").exists());
	}

	/**
	 * Test that a nested path is created with its missing parents when the parent is given as
	 * absolute path
	 *
	 * @param overload
	 *            the name of the overload
	 * @param newDirectoryStructure
	 *            the call of the overload
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("pathOfTheParentOverloads")
	void pathOfTheParent(final String overload,
		final BiFunction<File, List<String>, Collection<File>> newDirectoryStructure)
	{
		File java = new File(tempDir, "src/main/java");

		Collection<File> created = newDirectoryStructure.apply(tempDir, List.of("src/main/java"));

		assertEquals(List.of(java), List.copyOf(created));
		assertTrue(java.isDirectory(), "src/main/java created: " + java.isDirectory()
			+ ", src created: " + new File(tempDir, "src").exists());
	}

	/**
	 * Test that naming every level from the top creates every level, as it did before the missing
	 * parents were created
	 *
	 * @param overload
	 *            the name of the overload
	 * @param newDirectoryStructure
	 *            the call of the overload
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("allOverloads")
	void eachLevelNamedFromTheTop(final String overload,
		final BiFunction<File, List<String>, Collection<File>> newDirectoryStructure)
	{
		List<String> names = List.of("src", "src/main", "src/main/java");

		Collection<File> created = newDirectoryStructure.apply(tempDir, names);

		assertEquals(List.of(new File(tempDir, "src"), new File(tempDir, "src/main"),
			new File(tempDir, "src/main/java")), List.copyOf(created));
		created.forEach(directory -> assertTrue(directory.isDirectory(), directory.getName()));
	}

	/**
	 * Test that a path below a regular file, which cannot be created, makes the method throw
	 * instead of returning a {@link File} object for a directory that does not exist
	 *
	 * @param overload
	 *            the name of the overload
	 * @param newDirectoryStructure
	 *            the call of the overload
	 * @throws IOException
	 *             if the regular file cannot be written
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("allOverloads")
	void pathBelowARegularFileThrows(final String overload,
		final BiFunction<File, List<String>, Collection<File>> newDirectoryStructure)
		throws IOException
	{
		File regularFile = Files.writeString(tempDir.toPath().resolve("src"), "content").toFile();

		assertThrows(RuntimeException.class,
			() -> newDirectoryStructure.apply(tempDir, List.of("src/main/java")));

		assertTrue(regularFile.isFile());
		assertFalse(new File(tempDir, "src/main").exists());
	}

	/**
	 * Test that a regular file at the path itself makes the method throw instead of returning a
	 * {@link File} object for a directory that does not exist
	 *
	 * @param overload
	 *            the name of the overload
	 * @param newDirectoryStructure
	 *            the call of the overload
	 * @throws IOException
	 *             if the regular file cannot be written
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("allOverloads")
	void regularFileAtThePathThrows(final String overload,
		final BiFunction<File, List<String>, Collection<File>> newDirectoryStructure)
		throws IOException
	{
		File regularFile = Files.writeString(tempDir.toPath().resolve("src"), "content").toFile();

		assertThrows(RuntimeException.class,
			() -> newDirectoryStructure.apply(tempDir, List.of("src")));

		assertTrue(regularFile.isFile());
	}
}
