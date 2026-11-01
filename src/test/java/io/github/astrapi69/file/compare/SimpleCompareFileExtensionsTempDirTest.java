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
package io.github.astrapi69.file.compare;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import io.github.astrapi69.crypt.api.algorithm.HashAlgorithm;
import io.github.astrapi69.crypt.api.algorithm.MdAlgorithm;

/**
 * Tests for {@link SimpleCompareFileExtensions} that build every file they compare in a temporary
 * directory of their own
 */
public class SimpleCompareFileExtensionsTempDirTest
{

	/** A fixed modification time in whole seconds */
	private static final long LAST_MODIFIED = 1_600_000_000_000L;

	@TempDir
	Path tempDir;

	/**
	 * A comparison of two files that may fail with an exception
	 */
	@FunctionalInterface
	interface FileComparer
	{
		boolean compare(File source, File compare) throws Exception;
	}

	/**
	 * Two files that differ in exactly one property
	 */
	record DifferentFiles(String sourcePath, String sourceContent, long sourceLastModified,
		String comparePath, String compareContent, long compareLastModified) {
	}

	static Stream<Arguments> checksumComparisons()
	{
		final Stream<Arguments> comparers = Stream.of(
			Arguments.of("Adler32",
				(FileComparer)SimpleCompareFileExtensions::compareFilesByChecksumAdler32),
			Arguments.of("CRC32",
				(FileComparer)SimpleCompareFileExtensions::compareFilesByChecksumCRC32),
			Arguments.of("MD5",
				(FileComparer)(source, compare) -> SimpleCompareFileExtensions
					.compareFilesByChecksum(source, compare, MdAlgorithm.MD5)),
			Arguments.of("SHA-256", (FileComparer)(source, compare) -> SimpleCompareFileExtensions
				.compareFilesByChecksum(source, compare, HashAlgorithm.SHA_256)));
		return comparers.flatMap(comparer -> Stream.of(
			Arguments.of(comparer.get()[0], comparer.get()[1], "abc", "abc", true),
			Arguments.of(comparer.get()[0], comparer.get()[1], "abc", "abd", false),
			Arguments.of(comparer.get()[0], comparer.get()[1], "abc", "cba", false),
			Arguments.of(comparer.get()[0], comparer.get()[1], "abc", "", false),
			Arguments.of(comparer.get()[0], comparer.get()[1], "", "", true)));
	}

	static Stream<Arguments> propertyComparisons()
	{
		return Stream.of(
			Arguments.of("absolute path",
				(BiPredicate<File, File>)SimpleCompareFileExtensions::compareFilesByAbsolutePath,
				new DifferentFiles("a/same.txt", "abc", LAST_MODIFIED, "b/same.txt", "abc",
					LAST_MODIFIED)),
			Arguments.of("content",
				(BiPredicate<File, File>)SimpleCompareFileExtensions::compareFilesByContent,
				new DifferentFiles("a/same.txt", "abc", LAST_MODIFIED, "b/same.txt", "xyz",
					LAST_MODIFIED)),
			Arguments.of("extension",
				(BiPredicate<File, File>)SimpleCompareFileExtensions::compareFilesByExtension,
				new DifferentFiles("a/same.txt", "abc", LAST_MODIFIED, "b/same.csv", "abc",
					LAST_MODIFIED)),
			Arguments.of("modification time",
				(BiPredicate<File, File>)SimpleCompareFileExtensions::compareFilesByLastModified,
				new DifferentFiles("a/same.txt", "abc", LAST_MODIFIED, "b/same.txt", "abc",
					LAST_MODIFIED + 10_000L)),
			Arguments.of("length",
				(BiPredicate<File, File>)SimpleCompareFileExtensions::compareFilesByLength,
				new DifferentFiles("a/same.txt", "abc", LAST_MODIFIED, "b/same.txt", "abcd",
					LAST_MODIFIED)),
			Arguments.of("name",
				(BiPredicate<File, File>)SimpleCompareFileExtensions::compareFilesByName,
				new DifferentFiles("a/one.txt", "abc", LAST_MODIFIED, "b/two.txt", "abc",
					LAST_MODIFIED)));
	}

	static Stream<Arguments> comparisons()
	{
		return Stream.of(
			Arguments.of("compareFilesByAbsolutePath",
				(FileComparer)SimpleCompareFileExtensions::compareFilesByAbsolutePath),
			Arguments.of("compareFilesByChecksum",
				(FileComparer)(source, compare) -> SimpleCompareFileExtensions
					.compareFilesByChecksum(source, compare, HashAlgorithm.SHA_256)),
			Arguments.of("compareFilesByChecksumAdler32",
				(FileComparer)SimpleCompareFileExtensions::compareFilesByChecksumAdler32),
			Arguments.of("compareFilesByChecksumCRC32",
				(FileComparer)SimpleCompareFileExtensions::compareFilesByChecksumCRC32),
			Arguments.of("compareFilesByContent",
				(FileComparer)SimpleCompareFileExtensions::compareFilesByContent),
			Arguments.of("compareFilesByExtension",
				(FileComparer)SimpleCompareFileExtensions::compareFilesByExtension),
			Arguments.of("compareFilesByLastModified",
				(FileComparer)SimpleCompareFileExtensions::compareFilesByLastModified),
			Arguments.of("compareFilesByLength",
				(FileComparer)SimpleCompareFileExtensions::compareFilesByLength),
			Arguments.of("compareFilesByName",
				(FileComparer)SimpleCompareFileExtensions::compareFilesByName));
	}

	private File newFile(final String relativePath, final String content, final long lastModified)
		throws IOException
	{
		final Path path = tempDir.resolve(relativePath).toAbsolutePath();
		Files.createDirectories(path.getParent());
		Files.write(path, content.getBytes(StandardCharsets.US_ASCII));
		final File file = path.toFile();
		assertTrue(file.setLastModified(lastModified),
			"the modification time of " + file + " could not be set");
		return file;
	}

	private File newDirectory(final String relativePath) throws IOException
	{
		return Files.createDirectories(tempDir.resolve(relativePath).toAbsolutePath()).toFile();
	}

	/**
	 * Test method for every comparison of {@link SimpleCompareFileExtensions}: a directory as the
	 * source file is rejected (file-worker#78)
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("comparisons")
	public void compareFiles_rejectsADirectoryAsTheSourceFile_andNamesIt(
		final String comparisonName, final FileComparer comparer) throws IOException
	{
		final File directory = newDirectory("a/same");
		final File compare = newFile("b/same.txt", "abc", LAST_MODIFIED);

		final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
			() -> comparer.compare(directory, compare));

		assertTrue(exception.getMessage().contains(directory.toString()), exception.getMessage());
	}

	/**
	 * Test method for every comparison of {@link SimpleCompareFileExtensions}: a directory as the
	 * file to compare is rejected (file-worker#78)
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("comparisons")
	public void compareFiles_rejectsADirectoryAsTheFileToCompare_andNamesIt(
		final String comparisonName, final FileComparer comparer) throws IOException
	{
		final File source = newFile("a/same.txt", "abc", LAST_MODIFIED);
		final File directory = newDirectory("b/same");

		final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
			() -> comparer.compare(source, directory));

		assertTrue(exception.getMessage().contains(directory.toString()), exception.getMessage());
	}

	/**
	 * Test method for
	 * {@link SimpleCompareFileExtensions#compareFilesByChecksumAdler32(File, File)},
	 * {@link SimpleCompareFileExtensions#compareFilesByChecksumCRC32(File, File)} and
	 * {@link SimpleCompareFileExtensions#compareFilesByChecksum(File, File, io.github.astrapi69.crypt.api.algorithm.Algorithm)}
	 */
	@ParameterizedTest(name = "{0}: \"{2}\" and \"{3}\" have the same checksum: {4}")
	@MethodSource("checksumComparisons")
	public void compareFilesByChecksum_tellsWhetherTheContentIsEqual(final String algorithmName,
		final FileComparer comparer, final String sourceContent, final String compareContent,
		final boolean expected) throws Exception
	{
		final File source = newFile("a/same.txt", sourceContent, LAST_MODIFIED);
		final File compare = newFile("b/same.txt", compareContent, LAST_MODIFIED);

		assertEquals(expected, comparer.compare(source, compare));
	}

	/**
	 * Test method for the property comparisons of {@link SimpleCompareFileExtensions}
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("propertyComparisons")
	public void compareFilesByProperty_returnsTrueForTheFileItselfAndFalseWhenThePropertyDiffers(
		final String propertyName, final BiPredicate<File, File> comparer,
		final DifferentFiles files) throws IOException
	{
		final File source = newFile(files.sourcePath(), files.sourceContent(),
			files.sourceLastModified());
		final File compare = newFile(files.comparePath(), files.compareContent(),
			files.compareLastModified());

		assertTrue(comparer.test(source, source), "the file compared with itself");
		assertFalse(comparer.test(source, compare), "the file compared with the other one");
	}

}
