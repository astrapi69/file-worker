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
package io.github.astrapi69.file;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.argumentSet;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
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

import io.github.astrapi69.file.copy.CopyFileExtensions;
import io.github.astrapi69.file.merge.MergeFileExtensions;
import io.github.astrapi69.file.merge.strategy.MergeFileStrategy;
import io.github.astrapi69.file.modify.ModifyFileExtensions;
import io.github.astrapi69.file.search.FileSearchExtensions;
import io.github.astrapi69.file.write.WriteFileExtensions;

/**
 * The rule of {@link io.github.astrapi69.file.read.ReadFileExtensions} for bytes that are not valid
 * in the charset holds outside of it as well: every method of file-worker that decodes a file
 * throws a {@link MalformedInputException}, where these replaced such bytes with U+FFFD; the two
 * that decoded in the platform charset decode UTF-8 (#147)
 */
public class StrictDecodingTest
{

	/**
	 * {@code a}, the byte 0xFF, which is never valid in UTF-8, {@code b}, a line break, {@code c}
	 */
	private static final byte[] NOT_UTF_8 = { 'a', (byte)0xFF, 'b', '\n', 'c' };

	@TempDir
	Path tempDir;

	interface Decode
	{
		void from(Path file, Path directory) throws IOException;
	}

	@SuppressWarnings("deprecation")
	static Stream<Arguments> everyMethodThatDecodesOutsideOfReadFileExtensions()
	{
		return Stream.of(
			argumentSet("CopyFileExtensions.copyFile with charsets",
				(Decode)(file, directory) -> CopyFileExtensions.copyFile(file.toFile(),
					directory.resolve("copy.txt").toFile(), StandardCharsets.UTF_8,
					StandardCharsets.UTF_8, false)),
			argumentSet("CopyFileExtensions.copyFile with the default charset for the source",
				(Decode)(file, directory) -> CopyFileExtensions.copyFile(file.toFile(),
					directory.resolve("copy.txt").toFile(), null, StandardCharsets.UTF_8, false)),
			argumentSet("MergeFileExtensions.mergeAndGet, APPEND",
				(Decode)(file, directory) -> merge(file, directory, MergeFileStrategy.APPEND)),
			argumentSet("MergeFileExtensions.mergeAndGet, UNIQUE_LINES",
				(Decode)(file, directory) -> merge(file, directory,
					MergeFileStrategy.UNIQUE_LINES)),
			argumentSet("MergeFileExtensions.mergeAndGet, SORTED",
				(Decode)(file, directory) -> merge(file, directory, MergeFileStrategy.SORTED)),
			argumentSet("MergeFileExtensions.mergeByKey",
				(Decode)(file, directory) -> MergeFileExtensions.mergeByKey(List.of(file.toFile()),
					directory.resolve("merged.txt").toFile(), 0, ";")),
			argumentSet("MergeFileExtensions.collectByKeyAdvanced",
				(Decode)(file, directory) -> MergeFileExtensions
					.collectByKeyAdvanced(List.of(file.toFile()), StandardCharsets.UTF_8, 0, ";")),
			argumentSet("ModifyFileExtensions.modifyFileAndGet(Path, Charset, FileChangeable)",
				(Decode)(file, directory) -> ModifyFileExtensions.modifyFileAndGet(file,
					StandardCharsets.UTF_8, (index, line) -> line)),
			argumentSet(
				"ModifyFileExtensions.modifyFileAndGet(Path, Path, Charset, FileChangeable)",
				(Decode)(file, directory) -> ModifyFileExtensions.modifyFileAndGet(file,
					directory.resolve("modified.txt"), StandardCharsets.UTF_8,
					(index, line) -> line)),
			argumentSet("FileSearchExtensions.findLineIndex",
				(Decode)(file, directory) -> FileSearchExtensions.findLineIndex(file.toFile(),
					"c")),
			argumentSet("WriteFileExtensions.write2File(String, Writer)", (Decode)(file,
				directory) -> WriteFileExtensions.write2File(file.toString(), new StringWriter())));
	}

	private static void merge(final Path file, final Path directory,
		final MergeFileStrategy strategy) throws IOException
	{
		MergeFileExtensions.mergeAndGet(List.of(file.toFile()),
			directory.resolve("merged.txt").toFile(), StandardCharsets.UTF_8, strategy);
	}

	@ParameterizedTest
	@MethodSource("everyMethodThatDecodesOutsideOfReadFileExtensions")
	public void throwsAMalformedInputException_forBytesThatAreNotValidUtf8(final Decode decode)
		throws IOException
	{
		final Path file = Files.write(tempDir.resolve("not-utf-8.txt"), NOT_UTF_8);
		final Path directory = Files.createDirectory(tempDir.resolve("out"));

		assertThrows(MalformedInputException.class, () -> decode.from(file, directory));
	}

	@Test
	public void modifyFileAndGet_leavesAFileThatIsNotValidInItsCharsetAsItIs() throws IOException
	{
		final Path file = Files.write(tempDir.resolve("not-utf-8.txt"), NOT_UTF_8);

		assertThrows(MalformedInputException.class, () -> ModifyFileExtensions
			.modifyFileAndGet(file, StandardCharsets.UTF_8, (index, line) -> line));

		assertArrayEquals(NOT_UTF_8, Files.readAllBytes(file));
	}

	@Test
	public void findLineIndex_findsALineWithCharactersOutsideOfAscii() throws IOException
	{
		final File file = Files
			.write(tempDir.resolve("utf-8.txt"), List.of("Größe", "Ärger"), StandardCharsets.UTF_8)
			.toFile();

		org.junit.jupiter.api.Assertions.assertEquals(1,
			FileSearchExtensions.findLineIndex(file, "Ärger"));
	}
}
