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
import static org.junit.jupiter.params.provider.Arguments.argumentSet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * The unit test class for the {@code trim} flag of the four overloads
 * {@link ReadFileExtensions#readLinesInList(File, boolean)},
 * {@link ReadFileExtensions#readLinesInList(File, java.nio.charset.Charset, boolean)},
 * {@link ReadFileExtensions#readLinesInList(InputStream, boolean)} and
 * {@link ReadFileExtensions#readLinesInList(InputStream, java.nio.charset.Charset, boolean)}
 */
public class ReadLinesInListTrimTest
{

	/**
	 * Lines with leading and trailing spaces and tabs, one line of whitespace only, and one line
	 * that tells {@link String#trim()} from {@link String#strip()}: it starts with the control
	 * character U+0001, which {@code trim()} removes and {@code strip()} keeps, and ends with the
	 * em space U+2003, which {@code trim()} keeps and {@code strip()} removes
	 */
	private static final String CONTENT = "  first  \n\tsecond\t\n third\n \t \n\u0001x\u2003\n";

	/**
	 * The temporary directory for the file the lines are read from
	 */
	@TempDir
	Path tempDir;

	/**
	 * One of the overloads of {@code readLinesInList} with a {@code trim} flag
	 */
	@FunctionalInterface
	interface ReadLinesWithTrim
	{
		List<String> read(File file, boolean trim) throws IOException;
	}

	static Stream<Arguments> overloads()
	{
		return Stream.of(
			argumentSet("(File, boolean)",
				(ReadLinesWithTrim)(file, trim) -> ReadFileExtensions.readLinesInList(file, trim)),
			argumentSet("(File, Charset, boolean)",
				(ReadLinesWithTrim)(file, trim) -> ReadFileExtensions.readLinesInList(file,
					StandardCharsets.UTF_8, trim)),
			argumentSet("(InputStream, boolean)", (ReadLinesWithTrim)(file, trim) -> {
				// closed here, so that JUnit can delete the temporary directory, also on Windows
				try (InputStream inputStream = new FileInputStream(file))
				{
					return ReadFileExtensions.readLinesInList(inputStream, trim);
				}
			}), argumentSet("(InputStream, Charset, boolean)", (ReadLinesWithTrim)(file, trim) -> {
				try (InputStream inputStream = new FileInputStream(file))
				{
					return ReadFileExtensions.readLinesInList(inputStream, StandardCharsets.UTF_8,
						trim);
				}
			}));
	}

	private File writeContent() throws IOException
	{
		return Files.writeString(tempDir.resolve("lines.txt"), CONTENT, StandardCharsets.UTF_8)
			.toFile();
	}

	/**
	 * With {@code trim = true} every line comes back without its leading and trailing whitespace,
	 * as by {@link String#trim()}, not by {@link String#strip()}, and a line of whitespace only
	 * comes back as an empty line, not dropped
	 *
	 * @param overload
	 *            the overload to read with
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	@ParameterizedTest
	@MethodSource("overloads")
	public void withTrimTrueEveryLineIsTrimmed(final ReadLinesWithTrim overload) throws IOException
	{
		assertEquals(List.of("first", "second", "third", "", "x\u2003"),
			overload.read(writeContent(), true));
	}

	/**
	 * With {@code trim = false} every line comes back as it is in the file
	 *
	 * @param overload
	 *            the overload to read with
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	@ParameterizedTest
	@MethodSource("overloads")
	public void withTrimFalseTheLinesAreKeptAsTheyAre(final ReadLinesWithTrim overload)
		throws IOException
	{
		assertEquals(List.of("  first  ", "\tsecond\t", " third", " \t ", "\u0001x\u2003"),
			overload.read(writeContent(), false));
	}
}
