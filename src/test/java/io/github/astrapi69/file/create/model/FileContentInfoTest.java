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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.lang3.SystemUtils;
import org.junit.jupiter.api.Test;

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

}
