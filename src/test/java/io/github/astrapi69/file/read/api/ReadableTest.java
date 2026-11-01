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
package io.github.astrapi69.file.read.api;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.argumentSet;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * The unit test class for the default methods of the interface {@link Readable}
 */
public class ReadableTest
{

	private final Readable readable = new Readable()
	{
	};

	@TempDir
	Path tempDir;

	static Stream<Arguments> contentsAndTheirFirstLine()
	{
		return Stream.of(argumentSet("two lines", "first\nsecond\n", "first"),
			argumentSet("one line without a line break", "only line", "only line"),
			argumentSet("an empty first line", "\nsecond\n", ""));
	}

	static Stream<Arguments> bytesAndTheirObjects()
	{
		return Stream.of(argumentSet("no bytes", new byte[0], new Byte[0]),
			argumentSet("the smallest byte, zero and the largest byte",
				new byte[] { Byte.MIN_VALUE, 0, Byte.MAX_VALUE },
				new Byte[] { Byte.MIN_VALUE, 0, Byte.MAX_VALUE }));
	}

	/**
	 * Test method for {@link Readable#fromFile(File)}
	 *
	 * @throws IOException
	 *             if an I/O exception occurs
	 */
	@Test
	public void fromFile_readsTheWholeFileAsUtf8() throws IOException
	{
		File file = Files.writeString(tempDir.resolve("utf8.txt"), "Umlaute äöüß\nund €\n",
			StandardCharsets.UTF_8).toFile();

		String content = readable.fromFile(file);

		assertEquals("Umlaute äöüß\nund €\n", content);
	}

	/**
	 * Test method for {@link Readable#fromFile(File, Charset)}
	 *
	 * @param charsetName
	 *            the name of the charset the file is written and read with
	 * @throws IOException
	 *             if an I/O exception occurs
	 */
	@ParameterizedTest
	@ValueSource(strings = { "UTF-8", "ISO-8859-1", "UTF-16" })
	public void fromFile_readsTheWholeFileInTheGivenEncoding(String charsetName) throws IOException
	{
		Charset charset = Charset.forName(charsetName);
		File file = Files
			.writeString(tempDir.resolve("encoded.txt"), "Umlaute äöüß\nzweite Zeile", charset)
			.toFile();

		String content = readable.fromFile(file, charset);

		assertEquals("Umlaute äöüß\nzweite Zeile", content);
	}

	/**
	 * Test method for {@link Readable#readHeadLine(String)}
	 *
	 * @param content
	 *            the content of the file
	 * @param expectedHeadLine
	 *            the first line of the content
	 * @throws IOException
	 *             if an I/O exception occurs
	 */
	@ParameterizedTest
	@MethodSource("contentsAndTheirFirstLine")
	public void readHeadLine_returnsTheFirstLineOfTheFile(String content, String expectedHeadLine)
		throws IOException
	{
		Path file = Files.writeString(tempDir.resolve("lines.txt"), content);

		String headLine = readable.readHeadLine(file.toString());

		assertEquals(expectedHeadLine, headLine);
	}

	/**
	 * Test method for {@link Readable#readAllLines(File)}
	 *
	 * @throws IOException
	 *             if an I/O exception occurs
	 */
	@Test
	public void readAllLines_returnsEveryLineWithoutItsLineBreak() throws IOException
	{
		File file = Files.writeString(tempDir.resolve("lines.txt"), "first\nsecond\n\nfourth")
			.toFile();

		List<String> lines = readable.readAllLines(file);

		assertEquals(List.of("first", "second", "", "fourth"), lines);
	}

	/**
	 * Test method for {@link Readable#toByteArray(File)}
	 *
	 * @throws IOException
	 *             if an I/O exception occurs
	 */
	@Test
	public void toByteArray_returnsEveryByteOfTheFile() throws IOException
	{
		byte[] content = { Byte.MIN_VALUE, -1, 0, 1, Byte.MAX_VALUE };
		File file = Files.write(tempDir.resolve("bytes.bin"), content).toFile();

		byte[] actual = readable.toByteArray(file);

		assertArrayEquals(content, actual);
	}

	/**
	 * Test method for {@link Readable#toByteArray(java.io.InputStream)}
	 *
	 * @throws IOException
	 *             if an I/O exception occurs
	 */
	@Test
	public void toByteArray_returnsEveryByteOfTheInputStream() throws IOException
	{
		byte[] content = { Byte.MIN_VALUE, -1, 0, 1, Byte.MAX_VALUE };

		byte[] actual = readable.toByteArray(new ByteArrayInputStream(content));

		assertArrayEquals(content, actual);
	}

	/**
	 * Test method for {@link Readable#toObject(byte[])}
	 *
	 * @param bytes
	 *            the primitive bytes
	 * @param expected
	 *            the same values as objects
	 */
	@ParameterizedTest
	@MethodSource("bytesAndTheirObjects")
	public void toObject_returnsTheSameValuesAsObjects(byte[] bytes, Byte[] expected)
	{
		Byte[] actual = readable.toObject(bytes);

		assertArrayEquals(expected, actual);
	}

}
