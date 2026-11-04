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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.argumentSet;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.stream.Stream;

import org.apache.commons.lang3.SystemUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import io.github.astrapi69.checksum.ByteArrayChecksumExtensions;
import io.github.astrapi69.checksum.FileChecksumExtensions;
import io.github.astrapi69.crypt.api.algorithm.ChecksumAlgorithm;
import io.github.astrapi69.file.create.FileFactory;
import io.github.astrapi69.file.delete.DeleteFileExtensions;
import io.github.astrapi69.file.read.ReadFileExtensions;
import io.github.astrapi69.throwable.RuntimeExceptionDecorator;

/**
 * The unit test class for the class {@link FileContentInfo}
 */
public class FileContentInfoTest
{

	/** The MD5 checksum of the text "content" */
	private static final String MD5_OF_CONTENT = "9a0364b9e99bb480dd25e1f0284c8555";

	@TempDir
	Path tempDir;

	/**
	 * Test method for {@link FileContentInfo#toFileInfo(File)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testToFileContentInfo() throws IOException
	{
		String absolutePath;
		File file;
		FileContentInfo fileContentInfo;
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
		fileContentInfo = FileContentInfo.builder().path(absoluteFilePath).name(filename).build();
		file = FileFactory.newFile(fileContentInfo);
		assertTrue(file.exists());
		fileContentInfo.setChecksum(RuntimeExceptionDecorator
			.decorate(() -> FileChecksumExtensions.getChecksum(file, ChecksumAlgorithm.MD5)));
		fileContentInfo.setContent(
			RuntimeExceptionDecorator.decorate(() -> ReadFileExtensions.readFileToBytearray(file)));
		assertNotNull(file);
		expected = file.getAbsolutePath();
		assertEquals(expected, actual);

		FileContentInfo anotherFileInfo = FileContentInfo.toFileContentInfo(file);
		assertEquals(anotherFileInfo, fileContentInfo);

		File anotherFile = FileContentInfo.toFile(anotherFileInfo);
		assertEquals(anotherFile, file);
		DeleteFileExtensions.delete(file);
	}

	/**
	 * Test method for {@link FileContentInfo} constructors and builders
	 */
	@Test
	public final void testConstructors() throws NoSuchAlgorithmException
	{
		FileContentInfo model = new FileContentInfo();
		assertNotNull(model);
		byte[] content = { (byte)2, (byte)1, (byte)0 };
		String checksum = ByteArrayChecksumExtensions.getChecksum(content, ChecksumAlgorithm.MD5);
		model = new FileContentInfo(checksum, content);
		assertNotNull(model);
		model = FileContentInfo.builder().name("foo.txt").path("").checksum(checksum)
			.content(content).build();
		assertNotNull(model);
		String toString = model.toString();
		assertNotNull(toString);
		assertEquals(model, FileContentInfo.builder().name("foo.txt").path("").checksum(checksum)
			.content(content).build());
		int hashCode = model.hashCode();
		assertTrue(0 < hashCode);
	}

	/**
	 * The path {@link FileContentInfo#toFileContentInfo(File)} gives a file in the temporary
	 * directory: the absolute path of the directory with a trailing separator
	 */
	private String pathOfTempDir()
	{
		return tempDir.toFile().getAbsolutePath() + File.separator;
	}

	/**
	 * Test method for {@link FileContentInfo#toFileContentInfo(File)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void toFileContentInfo_ofAFile_takesNamePathChecksumAndContent() throws IOException
	{
		File file = Files.writeString(tempDir.resolve("data.txt"), "content").toFile();

		FileContentInfo fileContentInfo = FileContentInfo.toFileContentInfo(file);

		assertEquals("data.txt", fileContentInfo.getName());
		assertEquals(pathOfTempDir(), fileContentInfo.getPath());
		assertFalse(fileContentInfo.isDirectory());
		assertEquals(MD5_OF_CONTENT, fileContentInfo.getChecksum());
		assertArrayEquals("content".getBytes(StandardCharsets.UTF_8), fileContentInfo.getContent());
	}

	/**
	 * Test method for {@link FileContentInfo#toFileContentInfo(File)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void toFileContentInfo_ofADirectory_takesNameAndPathButNoChecksumAndNoContent()
		throws IOException
	{
		File directory = Files.createDirectory(tempDir.resolve("folder")).toFile();

		FileContentInfo fileContentInfo = FileContentInfo.toFileContentInfo(directory);

		assertEquals("folder", fileContentInfo.getName());
		assertEquals(pathOfTempDir(), fileContentInfo.getPath());
		assertTrue(fileContentInfo.isDirectory());
		assertNull(fileContentInfo.getChecksum());
		assertNull(fileContentInfo.getContent());
	}

	/**
	 * Test method for {@link FileContentInfo#toFile(FileContentInfo)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void toFile_createsAMissingFileWithItsParentDirectoriesAndWritesTheContent()
		throws IOException
	{
		File parent = tempDir.resolve("missing").resolve("parent").toFile();
		FileContentInfo fileContentInfo = FileContentInfo.builder().path(parent.getAbsolutePath())
			.name("new.txt").content("content".getBytes(StandardCharsets.UTF_8)).build();

		File file = FileContentInfo.toFile(fileContentInfo);

		assertEquals(new File(parent, "new.txt"), file);
		assertTrue(file.isFile());
		assertEquals("content", Files.readString(file.toPath()));
	}

	/**
	 * Test method for {@link FileContentInfo#toFile(FileContentInfo)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void toFile_replacesTheContentOfAnExistingFile() throws IOException
	{
		File existing = Files.writeString(tempDir.resolve("data.txt"), "old content").toFile();
		FileContentInfo fileContentInfo = FileContentInfo.builder()
			.path(tempDir.toFile().getAbsolutePath()).name("data.txt")
			.content("new".getBytes(StandardCharsets.UTF_8)).build();

		File file = FileContentInfo.toFile(fileContentInfo);

		assertEquals(existing, file);
		assertEquals("new", Files.readString(file.toPath()));
	}

	/**
	 * Test method for {@link FileContentInfo#toFile(FileContentInfo)}
	 */
	@Test
	public void toFile_ofADirectory_createsNothing()
	{
		FileContentInfo fileContentInfo = FileContentInfo.builder()
			.path(tempDir.toFile().getAbsolutePath()).name("folder").directory(true)
			.content("content".getBytes(StandardCharsets.UTF_8)).build();

		File file = FileContentInfo.toFile(fileContentInfo);

		assertEquals(tempDir.resolve("folder").toFile(), file);
		assertFalse(file.exists());
	}

	/**
	 * Test method for {@link FileContentInfo#toFileContentInfo(File)} and
	 * {@link FileContentInfo#toFile(FileContentInfo)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void toFile_ofTheFileContentInfoOfAFile_restoresTheFileAfterItWasDeleted()
		throws IOException
	{
		File file = Files.writeString(tempDir.resolve("data.txt"), "content").toFile();
		FileContentInfo fileContentInfo = FileContentInfo.toFileContentInfo(file);
		Files.delete(file.toPath());

		File restored = FileContentInfo.toFile(fileContentInfo);

		assertEquals(file.getAbsoluteFile(), restored.getAbsoluteFile());
		assertEquals("content", Files.readString(restored.toPath()));
	}

	/**
	 * A {@link FileContentInfo} of a subclass that refuses to be compared with a plain
	 * {@link FileContentInfo}
	 */
	private static FileContentInfo refusingComparison(String checksum, byte[] content)
	{
		return new FileContentInfo(checksum, content)
		{
			@Override
			protected boolean canEqual(final Object other)
			{
				return false;
			}
		};
	}

	static Stream<Arguments> fileContentInfoAndAnotherThatDiffers()
	{
		byte[] content = { 1, 2, 3 };
		FileContentInfo fileContentInfo = new FileContentInfo(MD5_OF_CONTENT, content);
		return Stream.of(
			argumentSet("another checksum", fileContentInfo,
				new FileContentInfo("d41d8cd98f00b204e9800998ecf8427e", content)),
			argumentSet("no checksum on the other side", fileContentInfo,
				new FileContentInfo(null, content)),
			argumentSet("no checksum on this side", new FileContentInfo(null, content),
				fileContentInfo),
			argumentSet("another content", fileContentInfo,
				new FileContentInfo(MD5_OF_CONTENT, new byte[] { 1, 2, 4 })),
			argumentSet("no content on the other side", fileContentInfo,
				new FileContentInfo(MD5_OF_CONTENT, null)),
			argumentSet("an object of another type", fileContentInfo, MD5_OF_CONTENT),
			argumentSet("a plain file info", fileContentInfo, new FileInfo()),
			argumentSet("a subclass that refuses the comparison", fileContentInfo,
				refusingComparison(MD5_OF_CONTENT, content)));
	}

	/**
	 * Test method for {@link FileContentInfo#equals(Object)}
	 *
	 * @param fileContentInfo
	 *            the object to compare
	 * @param other
	 *            the object to compare with, differing in one respect
	 */
	@ParameterizedTest
	@MethodSource("fileContentInfoAndAnotherThatDiffers")
	public void equals_isFalse_whenTheOtherDiffersInOneRespect(FileContentInfo fileContentInfo,
		Object other)
	{
		assertFalse(fileContentInfo.equals(other));
	}

	static Stream<Arguments> valuesOfEqualFileContentInfos()
	{
		return Stream.of(argumentSet("checksum and content", MD5_OF_CONTENT, new byte[] { 1, 2 }),
			argumentSet("no checksum", null, new byte[] { 1, 2 }),
			argumentSet("no checksum and no content", null, null));
	}

	/**
	 * Test method for {@link FileContentInfo#equals(Object)} and {@link FileContentInfo#hashCode()}
	 *
	 * @param checksum
	 *            the checksum of both objects
	 * @param content
	 *            the content of both objects
	 */
	@ParameterizedTest
	@MethodSource("valuesOfEqualFileContentInfos")
	public void equalValues_makeEqualObjectsWithEqualHashCodes(String checksum, byte[] content)
	{
		FileContentInfo constructed = new FileContentInfo(checksum,
			content == null ? null : content.clone());
		FileContentInfo built = FileContentInfo.builder().checksum(checksum).content(content)
			.build();

		assertTrue(constructed.equals(built));
		assertTrue(built.equals(constructed));
		assertEquals(constructed.hashCode(), built.hashCode());
	}

	/**
	 * Test method for {@link FileContentInfo#equals(Object)}
	 */
	@Test
	public void equals_isTrue_forTheSameInstance()
	{
		FileContentInfo fileContentInfo = new FileContentInfo(MD5_OF_CONTENT, new byte[] { 1 });

		assertTrue(fileContentInfo.equals(fileContentInfo));
	}

	/**
	 * Test method for {@link FileInfo#equals(Object)} with a {@link FileContentInfo}
	 */
	@Test
	public void plainFileInfo_isNotEqualToAFileContentInfoWithTheSameNamePathAndFlag()
	{
		FileInfo fileInfo = new FileInfo("data.txt", "/some/path", false);
		FileContentInfo fileContentInfo = FileContentInfo.builder().name("data.txt")
			.path("/some/path").build();

		assertFalse(fileInfo.equals(fileContentInfo));
		assertFalse(fileContentInfo.equals(fileInfo));
	}

	static Stream<Arguments> fileContentInfosThatDifferInOneField()
	{
		byte[] content = { 1, 2, 3 };
		return Stream.of(argumentSet("checksums that differ in the last character",
			new FileContentInfo("checksum1", content), new FileContentInfo("checksum2", content)),
			argumentSet("a checksum and none", new FileContentInfo("checksum1", content),
				new FileContentInfo(null, content)),
			argumentSet("contents that differ in the last byte",
				new FileContentInfo("checksum1", content),
				new FileContentInfo("checksum1", new byte[] { 1, 2, 4 })));
	}

	/**
	 * Test method for {@link FileContentInfo#hashCode()}
	 *
	 * @param fileContentInfo
	 *            the one object
	 * @param other
	 *            the other object, differing in one field
	 */
	@ParameterizedTest
	@MethodSource("fileContentInfosThatDifferInOneField")
	public void hashCode_differs_whenOneFieldDiffers(FileContentInfo fileContentInfo,
		FileContentInfo other)
	{
		assertNotEquals(fileContentInfo.hashCode(), other.hashCode());
	}

	/**
	 * Test method for {@link FileContentInfo#toString()}
	 */
	@Test
	public void toString_namesChecksumAndContent()
	{
		FileContentInfo fileContentInfo = new FileContentInfo(MD5_OF_CONTENT, new byte[] { 1, 2 });

		assertEquals("FileContentInfo{checksum='" + MD5_OF_CONTENT + "', content=[1, 2]}",
			fileContentInfo.toString());
	}

	/**
	 * Test method for {@link FileContentInfo.FileContentInfoBuilder#toString()}
	 */
	@Test
	public void builderToString_namesTheValuesSetSoFar()
	{
		FileContentInfo.FileContentInfoBuilder<?, ?> builder = FileContentInfo.builder()
			.name("data.txt").path("/some/path").checksum(MD5_OF_CONTENT)
			.content(new byte[] { 1, 2 });

		assertEquals("FileContentInfo.FileContentInfoBuilder(super=FileInfoBuilder{"
			+ "name='data.txt', path='/some/path', directory=false}, checksum='" + MD5_OF_CONTENT
			+ "', content=[1, 2])", builder.toString());
	}

}
