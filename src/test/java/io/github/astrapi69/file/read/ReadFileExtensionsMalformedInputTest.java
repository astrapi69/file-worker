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
package io.github.astrapi69.file.read;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.argumentSet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
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

/**
 * One rule for bytes that are not valid in the charset a method of {@link ReadFileExtensions}
 * decodes with: every one of them throws a {@link MalformedInputException}, as
 * {@link ReadFileExtensions#readLinesInList(File)} and
 * {@link ReadFileExtensions#countAllLines(File)} did, where the others replaced such bytes with
 * U+FFFD (#125)
 */
public class ReadFileExtensionsMalformedInputTest
{

	/**
	 * {@code a}, the byte 0xFF, which is never valid in UTF-8, {@code b}, a line break, {@code c}
	 */
	private static final byte[] NOT_UTF_8 = { 'a', (byte)0xFF, 'b', '\n', 'c' };

	@TempDir
	Path tempDir;

	interface Read
	{
		Object from(File file) throws IOException;
	}

	static Stream<Arguments> everyMethodThatDecodes()
	{
		return Stream.of(argumentSet("fromFile(File)", (Read)ReadFileExtensions::fromFile),
			argumentSet("fromFile(File, Charset)",
				(Read)file -> ReadFileExtensions.fromFile(file, StandardCharsets.UTF_8)),
			argumentSet("readHeadLine(String)",
				(Read)file -> ReadFileExtensions.readHeadLine(file.getAbsolutePath())),
			argumentSet("readLine(File, int)", (Read)file -> ReadFileExtensions.readLine(file, 1)),
			argumentSet("readLinesInList(File)", (Read)ReadFileExtensions::readLinesInList),
			argumentSet("readLinesInList(File, boolean)",
				(Read)file -> ReadFileExtensions.readLinesInList(file, true)),
			argumentSet("readLinesInList(File, Charset)",
				(Read)file -> ReadFileExtensions.readLinesInList(file, StandardCharsets.UTF_8)),
			argumentSet("readLinesInList(File, Charset, boolean)",
				(Read)file -> ReadFileExtensions.readLinesInList(file, StandardCharsets.UTF_8,
					false)),
			argumentSet("readLinesInList(InputStream)",
				(Read)file -> ReadFileExtensions.readLinesInList(new FileInputStream(file))),
			argumentSet("readLinesInList(InputStream, boolean)",
				(Read)file -> ReadFileExtensions.readLinesInList(new FileInputStream(file), true)),
			argumentSet("readLinesInList(InputStream, Charset, boolean)",
				(Read)file -> ReadFileExtensions.readLinesInList(new FileInputStream(file),
					StandardCharsets.UTF_8, false)),
			argumentSet("readLinesInList(InputStream, null, boolean), the default charset",
				(Read)file -> ReadFileExtensions.readLinesInList(new FileInputStream(file), null,
					false)),
			argumentSet("countAllLines(File)", (Read)ReadFileExtensions::countAllLines));
	}

	@ParameterizedTest
	@MethodSource("everyMethodThatDecodes")
	public void throwsAMalformedInputException_forBytesThatAreNotValidUtf8(final Read read)
		throws IOException
	{
		final File file = Files.write(tempDir.resolve("not-utf-8.txt"), NOT_UTF_8).toFile();

		assertThrows(MalformedInputException.class, () -> read.from(file));
	}

	@Test
	public void readsTheSameBytes_inACharsetInWhichTheyAreValid() throws IOException
	{
		final File file = Files.write(tempDir.resolve("latin-1.txt"), NOT_UTF_8).toFile();

		assertEquals("aÿb\nc", ReadFileExtensions.fromFile(file, StandardCharsets.ISO_8859_1));
		assertEquals(List.of("aÿb", "c"),
			ReadFileExtensions.readLinesInList(file, StandardCharsets.ISO_8859_1));
		assertEquals(List.of("aÿb", "c"), ReadFileExtensions
			.readLinesInList(new FileInputStream(file), StandardCharsets.ISO_8859_1, true));
	}

	static Stream<Arguments> headLines()
	{
		return Stream.of(argumentSet("a line break", "first\nsecond", "first"),
			argumentSet("a carriage return and a line break", "first\r\nsecond", "first"),
			argumentSet("a carriage return", "first\rsecond", "first"),
			argumentSet("no line break", "only line", "only line"),
			argumentSet("an empty first line", "\nsecond", ""),
			argumentSet("characters outside of ASCII", "Grüße\nzweite", "Grüße"));
	}

	@ParameterizedTest
	@MethodSource("headLines")
	public void readHeadLine_returnsTheFirstLine(final String content, final String expected)
		throws IOException
	{
		final Path file = Files.writeString(tempDir.resolve("head.txt"), content,
			StandardCharsets.UTF_8);

		assertEquals(expected, ReadFileExtensions.readHeadLine(file.toString()));
	}

	@Test
	public void readHeadLine_returnsNull_forAnEmptyFile() throws IOException
	{
		final Path file = Files.write(tempDir.resolve("empty.txt"), new byte[0]);

		assertNull(ReadFileExtensions.readHeadLine(file.toString()));
	}

	@Test
	public void readHeadLine_decodesTheFirstLineOnly() throws IOException
	{
		final byte[] validFirstLine = { 'o', 'k', '\n', (byte)0xFF };
		final Path file = Files.write(tempDir.resolve("valid-first-line.txt"), validFirstLine);

		assertEquals("ok", ReadFileExtensions.readHeadLine(file.toString()));
	}
}
