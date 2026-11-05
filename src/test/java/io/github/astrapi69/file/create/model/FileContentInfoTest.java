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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.argumentSet;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import io.github.astrapi69.checksum.ByteArrayChecksumExtensions;
import io.github.astrapi69.checksum.FileChecksumExtensions;
import io.github.astrapi69.crypt.api.algorithm.ChecksumAlgorithm;
import io.github.astrapi69.file.create.FileFactory;
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
		String absoluteFilePath = tempDir.resolve("foo").resolve("bar").toString();
		String filename = "foo.txt";
		FileContentInfo fileContentInfo = FileContentInfo.builder().path(absoluteFilePath)
			.name(filename).build();

		File file = FileFactory.newFile(fileContentInfo);

		assertTrue(file.exists());
		assertEquals(new File(absoluteFilePath, filename).getAbsolutePath(),
			file.getAbsolutePath());
		fileContentInfo.setChecksum(RuntimeExceptionDecorator
			.decorate(() -> FileChecksumExtensions.getChecksum(file, ChecksumAlgorithm.MD5)));
		fileContentInfo.setContent(
			RuntimeExceptionDecorator.decorate(() -> ReadFileExtensions.readFileToBytearray(file)));

		FileContentInfo anotherFileInfo = FileContentInfo.toFileContentInfo(file);
		assertEquals(anotherFileInfo, fileContentInfo);

		File anotherFile = FileContentInfo.toFile(anotherFileInfo);
		assertEquals(anotherFile, file);
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
	 * Test method for {@link FileContentInfo#equals(Object)}: the checksum, the content or the type
	 * differs. The name, the path and the directory flag take no part, as equals and hashCode do
	 * not call those of {@link FileInfo} (#114), so no case varies them.
	 *
	 * @param fileContentInfo
	 *            the object to compare
	 * @param other
	 *            the object to compare with, differing in one respect
	 */
	@ParameterizedTest
	@MethodSource("fileContentInfoAndAnotherThatDiffers")
	public void equals_isFalse_whenTheChecksumTheContentOrTheTypeDiffers(
		FileContentInfo fileContentInfo, Object other)
	{
		assertFalse(fileContentInfo.equals(other));
	}

	private static FileContentInfo entry(final String name, final String path,
		final boolean directory)
	{
		return FileContentInfo.builder().name(name).path(path).directory(directory)
			.checksum(MD5_OF_CONTENT).content(new byte[] { 1, 2, 3 }).build();
	}

	static Stream<Arguments> entriesOfTwoFilesWithTheSameContent()
	{
		FileContentInfo entry = entry("a.txt", "/data/", false);
		return Stream.of(argumentSet("another name", entry, entry("b.txt", "/data/", false)),
			argumentSet("another path", entry, entry("a.txt", "/other/", false)),
			argumentSet("a directory of that name", entry, entry("a.txt", "/data/", true)),
			argumentSet("no name on the other side", entry, entry(null, "/data/", false)),
			argumentSet("no path on this side", entry("a.txt", null, false), entry));
	}

	/**
	 * Test method for {@link FileContentInfo#equals(Object)} and
	 * {@link FileContentInfo#hashCode()}: two entries with the same checksum and content are still
	 * two files, when the name, the path or the directory flag of {@link FileInfo} differs (#123)
	 *
	 * @param entry
	 *            the entry to compare
	 * @param other
	 *            an entry with the same checksum and content of another file
	 */
	@ParameterizedTest
	@MethodSource("entriesOfTwoFilesWithTheSameContent")
	public void equals_isFalse_forTwoFilesWithTheSameContent(final FileContentInfo entry,
		final FileContentInfo other)
	{
		assertFalse(entry.equals(other));
		assertFalse(other.equals(entry));
		assertEquals(2, new HashSet<>(List.of(entry, other)).size());
	}

	/**
	 * Test method for {@link FileContentInfo#equals(Object)} and
	 * {@link FileContentInfo#hashCode()}: the same file with the same content is equal, with an
	 * equal hash code (#123)
	 */
	@Test
	public void equals_isTrue_forTheSameFileWithTheSameContent()
	{
		FileContentInfo entry = entry("a.txt", "/data/", false);
		FileContentInfo same = entry("a.txt", "/data/", false);

		assertEquals(entry, same);
		assertEquals(entry.hashCode(), same.hashCode());
		assertEquals(1, new HashSet<>(List.of(entry, same)).size());
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

	static Stream<Arguments> contentsAndHowToStringNamesThem()
	{
		return Stream.of(argumentSet("two bytes", new byte[] { 1, 2 }, "byte[2]"),
			argumentSet("no bytes", new byte[0], "byte[0]"),
			argumentSet("no content", null, "null"));
	}

	/**
	 * Test method for {@link FileContentInfo#toString()}
	 *
	 * @param content
	 *            the content of the entry
	 * @param expectedContent
	 *            how the string representation names the content
	 */
	@ParameterizedTest
	@MethodSource("contentsAndHowToStringNamesThem")
	public void toString_namesNamePathDirectoryChecksumAndTheLengthOfTheContent(byte[] content,
		String expectedContent)
	{
		FileContentInfo fileContentInfo = FileContentInfo.builder().name("data.txt")
			.path("/some/path").directory(true).checksum(MD5_OF_CONTENT).content(content).build();

		assertEquals(
			"FileContentInfo{name='data.txt', path='/some/path', directory=true, checksum='"
				+ MD5_OF_CONTENT + "', content=" + expectedContent + "}",
			fileContentInfo.toString());
	}

	/**
	 * Test method for {@link FileContentInfo.FileContentInfoBuilder#toString()}
	 *
	 * @param content
	 *            the content set on the builder
	 * @param expectedContent
	 *            how the string representation names the content
	 */
	@ParameterizedTest
	@MethodSource("contentsAndHowToStringNamesThem")
	public void builderToString_namesTheValuesSetSoFarAndTheLengthOfTheContent(byte[] content,
		String expectedContent)
	{
		FileContentInfo.FileContentInfoBuilder<?, ?> builder = FileContentInfo.builder()
			.name("data.txt").path("/some/path").checksum(MD5_OF_CONTENT).content(content);

		assertEquals("FileContentInfo.FileContentInfoBuilder(super=FileInfoBuilder{"
			+ "name='data.txt', path='/some/path', directory=false}, checksum='" + MD5_OF_CONTENT
			+ "', content=" + expectedContent + ")", builder.toString());
	}

	/** A content whose bytes no string representation may carry */
	private static final byte[] SENTINEL = "SENTINEL-CONTENT-4711".getBytes(StandardCharsets.UTF_8);

	static Stream<Arguments> stringRepresentationsOfAnEntryWithTheSentinelAsContent()
	{
		Supplier<FileContentInfo.FileContentInfoBuilder<?, ?>> builder = () -> FileContentInfo
			.builder().name("data.txt").path("/some/path").checksum(MD5_OF_CONTENT)
			.content(SENTINEL);
		return Stream.of(
			argumentSet("the object", (Supplier<String>)() -> builder.get().build().toString()),
			argumentSet("the builder", (Supplier<String>)() -> builder.get().toString()));
	}

	/**
	 * Test method for {@link FileContentInfo#toString()} and
	 * {@link FileContentInfo.FileContentInfoBuilder#toString()}: they name name, path, directory
	 * and checksum and the length of the content, not its bytes (#114)
	 *
	 * @param stringRepresentation
	 *            gives the string representation to check
	 */
	@ParameterizedTest
	@MethodSource("stringRepresentationsOfAnEntryWithTheSentinelAsContent")
	public void toString_namesTheLengthOfTheContentButNotItsBytes(
		Supplier<String> stringRepresentation)
	{
		String toString = stringRepresentation.get();

		assertFalse(toString.contains(Arrays.toString(SENTINEL)), toString);
		assertFalse(toString.contains(new String(SENTINEL, StandardCharsets.UTF_8)), toString);
		assertTrue(toString.contains("name='data.txt'"), toString);
		assertTrue(toString.contains("path='/some/path'"), toString);
		assertTrue(toString.contains("directory=false"), toString);
		assertTrue(toString.contains("checksum='" + MD5_OF_CONTENT + "'"), toString);
		assertTrue(toString.contains("content=byte[" + SENTINEL.length + "]"), toString);
	}

	/**
	 * Asserts that the given entry describes the given file as a file that is not a directory and
	 * has neither checksum nor content
	 */
	private void assertNotADirectoryWithoutChecksumAndContent(File file,
		FileContentInfo fileContentInfo)
	{
		assertEquals(file.getName(), fileContentInfo.getName());
		assertFalse(fileContentInfo.isDirectory());
		assertNull(fileContentInfo.getChecksum());
		assertNull(fileContentInfo.getContent());
	}

	/**
	 * Test method for {@link FileContentInfo#toFileContentInfo(File)}: a path that does not exist
	 * is not described as a directory (#114)
	 */
	@Test
	public void toFileContentInfo_ofAMissingPath_isNotADirectory()
	{
		File missing = tempDir.resolve("missing.txt").toFile();

		FileContentInfo fileContentInfo = FileContentInfo.toFileContentInfo(missing);

		assertNotADirectoryWithoutChecksumAndContent(missing, fileContentInfo);
		assertEquals(pathOfTempDir(), fileContentInfo.getPath());
	}

	/**
	 * Test method for {@link FileContentInfo#toFileContentInfo(File)}: a symbolic link to a path
	 * that does not exist is not described as a directory (#114)
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	@DisabledOnOs(OS.WINDOWS)
	public void toFileContentInfo_ofADanglingSymbolicLink_isNotADirectory() throws IOException
	{
		File link = Files
			.createSymbolicLink(tempDir.resolve("link"), tempDir.resolve("missing-target"))
			.toFile();

		FileContentInfo fileContentInfo = FileContentInfo.toFileContentInfo(link);

		assertNotADirectoryWithoutChecksumAndContent(link, fileContentInfo);
		assertEquals(pathOfTempDir(), fileContentInfo.getPath());
	}

	/**
	 * Test method for {@link FileContentInfo#toFileContentInfo(File)}: a device that exists but is
	 * neither a regular file nor a directory is not described as a directory (#114)
	 */
	@Test
	@EnabledOnOs({ OS.LINUX, OS.MAC })
	public void toFileContentInfo_ofADevice_isNotADirectory()
	{
		File device = new File("/dev/null");

		FileContentInfo fileContentInfo = FileContentInfo.toFileContentInfo(device);

		assertNotADirectoryWithoutChecksumAndContent(device, fileContentInfo);
	}

	static Stream<Arguments> namesOfAFileWithoutContent()
	{
		return Stream.of(argumentSet("in an existing directory", "data.txt"),
			argumentSet("in a missing directory", "missing" + File.separator + "data.txt"));
	}

	/**
	 * Test method for {@link FileContentInfo#toFile(FileContentInfo)}: an entry that is not a
	 * directory and has no content is refused before anything is created (#114)
	 *
	 * @param relativeName
	 *            the name of the file, relative to the temporary directory
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@ParameterizedTest
	@MethodSource("namesOfAFileWithoutContent")
	public void toFile_ofAFileWithoutContent_throwsAndCreatesNothing(String relativeName)
		throws IOException
	{
		File file = tempDir.resolve(relativeName).toFile();
		FileContentInfo fileContentInfo = FileContentInfo.builder()
			.path(file.getParentFile().getAbsolutePath()).name(file.getName()).build();

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
			() -> FileContentInfo.toFile(fileContentInfo));

		assertTrue(exception.getMessage().contains(file.getPath()), exception.getMessage());
		assertFalse(file.exists());
		try (Stream<Path> entries = Files.list(tempDir))
		{
			assertEquals(0, entries.count());
		}
	}

	/**
	 * Test method for {@link FileContentInfo#toFile(FileContentInfo)}: a directory entry needs no
	 * content
	 */
	@Test
	public void toFile_ofADirectoryWithoutContent_createsNothingAndDoesNotThrow()
	{
		FileContentInfo fileContentInfo = FileContentInfo.builder()
			.path(tempDir.toFile().getAbsolutePath()).name("folder").directory(true).build();

		File file = FileContentInfo.toFile(fileContentInfo);

		assertEquals(tempDir.resolve("folder").toFile(), file);
		assertFalse(file.exists());
	}

}
