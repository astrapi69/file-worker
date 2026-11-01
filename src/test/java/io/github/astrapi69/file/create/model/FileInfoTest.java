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
package io.github.astrapi69.file.create.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.argumentSet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import org.apache.commons.lang3.SystemUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.meanbean.test.BeanTester;

import io.github.astrapi69.file.create.FileFactory;
import io.github.astrapi69.file.delete.DeleteFileExtensions;

/**
 * The unit test class for the class {@link FileInfo}
 */
public class FileInfoTest
{

	@TempDir
	Path tempDir;

	/**
	 * Test method for {@link FileInfo} constructors and builders
	 */
	@Test
	public final void testConstructors()
	{
		FileInfo model = new FileInfo();
		assertNotNull(model);
		model = new FileInfo("foo", "bar", false);
		assertNotNull(model);
		model = FileInfo.builder().build();
		assertNotNull(model);
		String toString = model.toString();
		assertNotNull(toString);
		assertTrue(model.equals((Object)FileInfo.builder().build()));
		int hashCode = model.hashCode();
		assertTrue(0 < hashCode);
	}

	/**
	 * Test method for {@link FileInfo#toFileInfo(File)}
	 */
	@Test
	public void testToFileInfo() throws IOException
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
		DeleteFileExtensions.delete(file);
	}

	/**
	 * Test method for {@link FileInfo}
	 */
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(FileInfo.class);
	}

	/**
	 * A {@link FileInfo} of a subclass that refuses to be compared with a plain {@link FileInfo}
	 */
	private static FileInfo refusingComparison(String name, String path, boolean directory)
	{
		return new FileInfo(name, path, directory)
		{
			@Override
			protected boolean canEqual(final Object other)
			{
				return false;
			}
		};
	}

	static Stream<Arguments> fileInfoAndAnotherThatDiffers()
	{
		FileInfo fileInfo = new FileInfo("name.txt", "/some/path", false);
		return Stream.of(
			argumentSet("another name", fileInfo, new FileInfo("other.txt", "/some/path", false)),
			argumentSet("no name on the other side", fileInfo,
				new FileInfo(null, "/some/path", false)),
			argumentSet("no name on this side", new FileInfo(null, "/some/path", false), fileInfo),
			argumentSet("another path", fileInfo, new FileInfo("name.txt", "/other/path", false)),
			argumentSet("no path on the other side", fileInfo,
				new FileInfo("name.txt", null, false)),
			argumentSet("no path on this side", new FileInfo("name.txt", null, false), fileInfo),
			argumentSet("a directory on the other side", fileInfo,
				new FileInfo("name.txt", "/some/path", true)),
			argumentSet("an object of another type", fileInfo, "name.txt"),
			argumentSet("a subclass that refuses the comparison", fileInfo,
				refusingComparison("name.txt", "/some/path", false)));
	}

	static Stream<Arguments> valuesOfEqualFileInfos()
	{
		return Stream.of(argumentSet("a file", "name.txt", "/some/path", false),
			argumentSet("a directory", "directory", "/some/path", true),
			argumentSet("no name and no path", null, null, false));
	}

	/**
	 * Test method for {@link FileInfo#equals(Object)}
	 *
	 * @param fileInfo
	 *            the object to compare
	 * @param other
	 *            the object to compare with, differing in one respect
	 */
	@ParameterizedTest
	@MethodSource("fileInfoAndAnotherThatDiffers")
	public void equals_isFalse_whenTheOtherDiffersInOneRespect(FileInfo fileInfo, Object other)
	{
		assertFalse(fileInfo.equals(other));
	}

	/**
	 * Test method for {@link FileInfo#equals(Object)} and {@link FileInfo#hashCode()}
	 *
	 * @param name
	 *            the name of both objects
	 * @param path
	 *            the path of both objects
	 * @param directory
	 *            the directory flag of both objects
	 */
	@ParameterizedTest
	@MethodSource("valuesOfEqualFileInfos")
	public void equalValues_makeEqualObjectsWithEqualHashCodes(String name, String path,
		boolean directory)
	{
		FileInfo constructed = new FileInfo(name, path, directory);
		FileInfo built = FileInfo.builder().name(name).path(path).directory(directory).build();

		assertTrue(constructed.equals(built));
		assertTrue(built.equals(constructed));
		assertEquals(constructed.hashCode(), built.hashCode());
	}

	/**
	 * Test method for {@link FileInfo#equals(Object)}
	 */
	@Test
	public void equals_isTrue_forTheSameInstance()
	{
		FileInfo fileInfo = new FileInfo("name.txt", "/some/path", false);

		assertTrue(fileInfo.equals(fileInfo));
	}

	/**
	 * Test method for {@link FileInfo#FileInfo(File)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void constructorWithFile_takesNameParentPathAndTheFlagOfAFile() throws IOException
	{
		File file = Files.writeString(tempDir.resolve("data.txt"), "content").toFile();

		FileInfo fileInfo = new FileInfo(file);

		assertEquals("data.txt", fileInfo.getName());
		assertEquals(tempDir.toFile().getAbsolutePath(), fileInfo.getPath());
		assertFalse(fileInfo.isDirectory());
		assertEquals(FileInfo.toFileInfo(file), fileInfo);
	}

	/**
	 * Test method for {@link FileInfo#FileInfo(File)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void constructorWithFile_takesNameParentPathAndTheFlagOfADirectory() throws IOException
	{
		File directory = Files.createDirectory(tempDir.resolve("folder")).toFile();

		FileInfo fileInfo = new FileInfo(directory);

		assertEquals("folder", fileInfo.getName());
		assertEquals(tempDir.toFile().getAbsolutePath(), fileInfo.getPath());
		assertTrue(fileInfo.isDirectory());
		assertEquals(FileInfo.toFileInfo(directory), fileInfo);
	}

	/**
	 * Test method for {@link FileInfo#FileInfo(File)}
	 */
	@Test
	public void constructorWithFile_throwsNullPointerException_whenTheFileIsNull()
	{
		assertThrows(NullPointerException.class, () -> new FileInfo((File)null));
	}

	/**
	 * Test method for {@link FileInfo#toFile()}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void toFile_returnsTheFileTheFileInfoWasMadeFrom() throws IOException
	{
		File file = Files.writeString(tempDir.resolve("data.txt"), "content").toFile();

		File actual = new FileInfo(file).toFile();

		assertEquals(file, actual);
		assertEquals("content", Files.readString(actual.toPath()));
	}

	/**
	 * Test method for {@link FileInfo#toBuilder()}
	 */
	@Test
	public void toBuilder_startsFromTheValuesOfTheFileInfo()
	{
		FileInfo original = new FileInfo("data.txt", "/some/path", true);

		FileInfo copy = original.toBuilder().build();
		FileInfo renamed = original.toBuilder().name("other.txt").build();

		assertEquals(original, copy);
		assertEquals(new FileInfo("other.txt", "/some/path", true), renamed);
		assertEquals("data.txt", original.getName());
	}

	/**
	 * Test method for {@link FileInfo#toString()}
	 */
	@Test
	public void toString_namesEveryField()
	{
		FileInfo fileInfo = new FileInfo("data.txt", "/some/path", true);

		assertEquals("FileInfo{name='data.txt', path='/some/path', directory=true}",
			fileInfo.toString());
	}

	/**
	 * Test method for {@link FileInfo.FileInfoBuilder#toString()}
	 */
	@Test
	public void builderToString_namesTheValuesSetSoFar()
	{
		FileInfo.FileInfoBuilder<?, ?> builder = FileInfo.builder().name("data.txt")
			.path("/some/path").directory(true);

		assertEquals("FileInfoBuilder{name='data.txt', path='/some/path', directory=true}",
			builder.toString());
	}
}
