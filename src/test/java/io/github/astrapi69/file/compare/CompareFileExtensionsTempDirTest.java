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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import io.github.astrapi69.file.compare.api.IFileCompareResultBean;
import io.github.astrapi69.file.compare.api.IFileContentResultBean;

/**
 * Tests for {@link CompareFileExtensions} that build every file they compare in a temporary
 * directory of their own
 */
public class CompareFileExtensionsTempDirTest
{

	/**
	 * A fixed modification time in whole seconds, so that a file system with a coarse clock keeps
	 * it as it is set
	 */
	private static final long LAST_MODIFIED = 1_600_000_000_000L;

	/** A modification time ten seconds after {@link #LAST_MODIFIED} */
	private static final long LATER = LAST_MODIFIED + 10_000L;

	@TempDir
	Path tempDir;

	/**
	 * The properties of a pair of files and the equality that a comparison has to report for them
	 */
	record PropertyCase(String description, String sourcePath, String sourceContent,
		long sourceLastModified, String comparePath, String compareContent,
		long compareLastModified, boolean extensionEquality, boolean lengthEquality,
		boolean lastModifiedEquality, boolean nameEquality) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	static Stream<PropertyCase> propertyCases()
	{
		return Stream.of(
			new PropertyCase("only the directory differs", "a/same.txt", "abc", LAST_MODIFIED,
				"b/same.txt", "abc", LAST_MODIFIED, true, true, true, true),
			new PropertyCase("only the extension differs", "a/same.txt", "abc", LAST_MODIFIED,
				"b/same.csv", "abc", LAST_MODIFIED, false, true, true, true),
			new PropertyCase("only the length differs", "a/same.txt", "abc", LAST_MODIFIED,
				"b/same.txt", "abcd", LAST_MODIFIED, true, false, true, true),
			new PropertyCase("only the modification time differs", "a/same.txt", "abc",
				LAST_MODIFIED, "b/same.txt", "abc", LATER, true, true, false, true),
			new PropertyCase("only the name differs", "a/one.txt", "abc", LAST_MODIFIED,
				"b/two.txt", "abc", LAST_MODIFIED, true, true, true, false),
			new PropertyCase("name and extension differ only in case", "a/Same.TXT", "abc",
				LAST_MODIFIED, "b/same.txt", "abc", LAST_MODIFIED, true, true, true, true),
			new PropertyCase("neither file has an extension", "a/README", "abc", LAST_MODIFIED,
				"b/README", "abc", LAST_MODIFIED, true, true, true, true),
			new PropertyCase("only the file to compare has no extension", "a/README.txt", "abc",
				LAST_MODIFIED, "b/README", "abc", LAST_MODIFIED, false, true, true, true),
			new PropertyCase("only the source has no extension", "a/README", "abc", LAST_MODIFIED,
				"b/README.txt", "abc", LAST_MODIFIED, false, true, true, true));
	}

	static Stream<Arguments> contentComparers()
	{
		return Stream.of(Arguments.of("compareFileContentByBytes",
			(BiFunction<File, File, IFileContentResultBean>)CompareFileExtensions::compareFileContentByBytes),
			Arguments.of("compareFileContentByLines",
				(BiFunction<File, File, IFileContentResultBean>)CompareFileExtensions::compareFileContentByLines));
	}

	/**
	 * Two contents of the same length and the content equality that a comparison has to report for
	 * them
	 */
	record ContentCase(String description, String sourceContent, String compareContent,
		boolean contentEquality) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	static Stream<Arguments> contentComparersWithContentCases()
	{
		final List<ContentCase> contentCases = List.of(
			new ContentCase("every byte differs", "abc", "xyz", false),
			new ContentCase("only the first byte differs", "abc", "xbc", false),
			new ContentCase("only the last byte differs", "abc", "abd", false),
			new ContentCase("only the second line differs", "a\nb\n", "a\nc\n", false),
			new ContentCase("the content is the same", "abc", "abc", true),
			new ContentCase("both files are empty", "", "", true));
		return contentComparers().flatMap(comparer -> contentCases.stream()
			.map(contentCase -> Arguments.of(comparer.get()[0], comparer.get()[1], contentCase)));
	}

	static Stream<Arguments> finders()
	{
		return Stream.of(Arguments.of("findEqualFiles(File)",
			(Function<File, List<? extends IFileCompareResultBean>>)root -> CompareFileExtensions
				.findEqualFiles(root)),
			Arguments.of("findEqualFiles(File, flags)",
				(Function<File, List<? extends IFileCompareResultBean>>)root -> CompareFileExtensions
					.findEqualFiles(root, false, false, false, false, false)),
			Arguments.of("findEqualFilesWithSameContent(File)",
				(Function<File, List<? extends IFileCompareResultBean>>)root -> CompareFileExtensions
					.findEqualFilesWithSameContent(root)),
			Arguments.of("findEqualFilesWithSameContent(File, flags)",
				(Function<File, List<? extends IFileCompareResultBean>>)root -> CompareFileExtensions
					.findEqualFilesWithSameContent(root, false, false, false, false, false,
						false)));
	}

	static Stream<Arguments> twoDirectoryFinders()
	{
		return Stream.of(Arguments.of("findEqualFiles(File, File)",
			(BiFunction<File, File, List<? extends IFileCompareResultBean>>)CompareFileExtensions::findEqualFiles),
			Arguments.of("findEqualFiles(File, File, flags)",
				(BiFunction<File, File, List<? extends IFileCompareResultBean>>)(source,
					compare) -> CompareFileExtensions.findEqualFiles(source, compare, false, false,
						false, false, false)),
			Arguments.of("findEqualFilesWithSameContent(File, File)",
				(BiFunction<File, File, List<? extends IFileCompareResultBean>>)CompareFileExtensions::findEqualFilesWithSameContent),
			Arguments.of("findEqualFilesWithSameContent(File, File, flags)",
				(BiFunction<File, File, List<? extends IFileCompareResultBean>>)(source,
					compare) -> CompareFileExtensions.findEqualFilesWithSameContent(source, compare,
						false, false, false, false, false, false)));
	}

	private File newFile(final String relativePath, final String content) throws IOException
	{
		return newFile(relativePath, content, LAST_MODIFIED);
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

	private File notCreated(final String relativePath)
	{
		return tempDir.resolve(relativePath).toAbsolutePath().toFile();
	}

	private static Set<List<File>> pairsOf(final List<? extends IFileCompareResultBean> results)
	{
		return results.stream()
			.map(result -> List.of(result.getSourceFile(), result.getFileToCompare()))
			.collect(Collectors.toSet());
	}

	private static FileCompareResultBean newCompareResult(final File source, final File compare,
		final boolean absolutePathEquality)
	{
		final FileCompareResultBean bean = new FileCompareResultBean(source, compare);
		bean.setAbsolutePathEquality(absolutePathEquality);
		bean.setFileExtensionEquality(true);
		bean.setLengthEquality(true);
		bean.setLastModifiedEquality(true);
		bean.setNameEquality(true);
		return bean;
	}

	/**
	 * Test method for
	 * {@link CompareFileExtensions#compare(IFileCompareResultBean, boolean, boolean, boolean, boolean, boolean)}
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("propertyCases")
	public void compare_reportsEachPropertyOfTheTwoFiles_whenNothingIsIgnored(
		final PropertyCase propertyCase) throws IOException
	{
		final File source = newFile(propertyCase.sourcePath(), propertyCase.sourceContent(),
			propertyCase.sourceLastModified());
		final File compare = newFile(propertyCase.comparePath(), propertyCase.compareContent(),
			propertyCase.compareLastModified());
		final IFileCompareResultBean result = new FileCompareResultBean(source, compare);

		CompareFileExtensions.compare(result, false, false, false, false, false);

		assertFalse(result.getAbsolutePathEquality(), "absolute path");
		assertEquals(propertyCase.extensionEquality(), result.getFileExtensionEquality(),
			"extension");
		assertEquals(propertyCase.lengthEquality(), result.getLengthEquality(), "length");
		assertEquals(propertyCase.lastModifiedEquality(), result.getLastModifiedEquality(),
			"modification time");
		assertEquals(propertyCase.nameEquality(), result.getNameEquality(), "name");
	}

	/**
	 * Test method for
	 * {@link CompareFileExtensions#compareFiles(File, File, boolean, boolean, boolean, boolean, boolean)}
	 */
	@Test
	public void compareFiles_reportsEveryIgnoredPropertyAsEqual_whenTheFilesDifferInEverything()
		throws IOException
	{
		final File source = newFile("a/one.txt", "abc", LAST_MODIFIED);
		final File compare = newFile("b/two.csv", "abcdef", LATER);

		final IFileCompareResultBean result = CompareFileExtensions.compareFiles(source, compare,
			true, true, true, true, true);

		assertEquals(source, result.getSourceFile());
		assertEquals(compare, result.getFileToCompare());
		assertTrue(result.getAbsolutePathEquality(), "absolute path");
		assertTrue(result.getFileExtensionEquality(), "extension");
		assertTrue(result.getLengthEquality(), "length");
		assertTrue(result.getLastModifiedEquality(), "modification time");
		assertTrue(result.getNameEquality(), "name");
	}

	/**
	 * Test method for {@link CompareFileExtensions#completeCompare(IFileCompareResultBean)}
	 */
	@Test
	public void completeCompare_reportsTheAbsolutePathAsEqual_onlyForTheSameFile()
		throws IOException
	{
		final File source = newFile("a/same.txt", "abc");
		final File copy = newFile("b/same.txt", "abc");
		final IFileCompareResultBean withItself = new FileCompareResultBean(source, source);
		final IFileCompareResultBean withCopy = new FileCompareResultBean(source, copy);

		CompareFileExtensions.completeCompare(withItself);
		CompareFileExtensions.completeCompare(withCopy);

		assertEquals(newCompareResult(source, source, true), withItself);
		assertEquals(newCompareResult(source, copy, false), withCopy);
	}

	/**
	 * Test method for
	 * {@link CompareFileExtensions#compare(IFileContentResultBean, boolean, boolean, boolean, boolean, boolean, boolean)}
	 */
	@ParameterizedTest(name = "\"{0}\" and \"{1}\" have equal content: {2}")
	@CsvSource({ "abc, abc, true", "abc, abd, false", "abc, abcd, false", "abc, '', false",
			"'', '', true" })
	public void compareFiles_reportsTheContentEquality_whenTheContentIsNotIgnored(
		final String sourceContent, final String compareContent, final boolean expected)
		throws IOException
	{
		final File source = newFile("a/same.txt", sourceContent);
		final File compare = newFile("b/same.txt", compareContent);

		final IFileContentResultBean result = CompareFileExtensions.compareFiles(source, compare,
			true, true, true, true, true, false);

		assertEquals(expected, result.getContentEquality());
	}

	/**
	 * Test method for
	 * {@link CompareFileExtensions#compare(IFileContentResultBean, boolean, boolean, boolean, boolean, boolean, boolean)}
	 */
	@Test
	public void compareFiles_reportsTheContentAsEqual_whenTheContentIsIgnored() throws IOException
	{
		final File source = newFile("a/same.txt", "abc");
		final File compare = newFile("b/same.txt", "xyz");

		final IFileContentResultBean result = CompareFileExtensions.compareFiles(source, compare,
			true, true, true, true, true, true);

		assertTrue(result.getContentEquality());
	}

	/**
	 * Test method for
	 * {@link CompareFileExtensions#compare(IFileContentResultBean, boolean, boolean, boolean, boolean, boolean, boolean)}
	 */
	@Test
	public void compareFiles_reportsTheContentAsNotEqual_whenTheSourceFileIsMissing()
		throws IOException
	{
		final File missing = notCreated("a/same.txt");
		final File compare = newFile("b/same.txt", "abc");

		final IFileContentResultBean result = CompareFileExtensions.compareFiles(missing, compare,
			true, true, true, true, true, false);

		assertFalse(result.getContentEquality());
	}

	/**
	 * Test method for {@link CompareFileExtensions#compareFileContentByBytes(File, File)} and
	 * {@link CompareFileExtensions#compareFileContentByLines(File, File)}
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("contentComparers")
	public void compareFileContent_reportsThePropertiesOfBothFiles_whenTheNamesDiffer(
		final String comparerName, final BiFunction<File, File, IFileContentResultBean> comparer)
		throws IOException
	{
		final File source = newFile("a/one.txt", "abc");
		final File compare = newFile("b/two.csv", "abc");

		final IFileContentResultBean result = comparer.apply(source, compare);

		assertEquals(source, result.getSourceFile());
		assertEquals(compare, result.getFileToCompare());
		assertFalse(result.getAbsolutePathEquality(), "absolute path");
		assertFalse(result.getFileExtensionEquality(), "extension");
		assertTrue(result.getLengthEquality(), "length");
		assertTrue(result.getLastModifiedEquality(), "modification time");
		assertFalse(result.getNameEquality(), "name");
		assertFalse(CompareFileExtensions.validateEquality(result));
	}

	/**
	 * Test method for {@link CompareFileExtensions#compareFileContentByBytes(File, File)} and
	 * {@link CompareFileExtensions#compareFileContentByLines(File, File)}
	 */
	@ParameterizedTest(name = "{0}: {2}")
	@MethodSource("contentComparersWithContentCases")
	public void compareFileContent_reportsTheContentEquality_whenEveryOtherPropertyIsEqual(
		final String comparerName, final BiFunction<File, File, IFileContentResultBean> comparer,
		final ContentCase contentCase) throws IOException
	{
		final File source = newFile("a/same.txt", contentCase.sourceContent());
		final File compare = newFile("b/same.txt", contentCase.compareContent());

		final IFileContentResultBean result = comparer.apply(source, compare);

		assertTrue(CompareFileExtensions.validateEquality((IFileCompareResultBean)result),
			"extension, length, modification time and name");
		assertEquals(contentCase.contentEquality(), result.getContentEquality(), "content");
		assertEquals(contentCase.contentEquality(), CompareFileExtensions.validateEquality(result),
			"total equality");
	}

	/**
	 * Test method for {@link CompareFileExtensions#compareFileContentByBytes(File, File)} and
	 * {@link CompareFileExtensions#compareFileContentByLines(File, File)}
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("contentComparers")
	public void compareFileContent_reportsTheContentAsNotEqual_whenBothFilesAreMissing(
		final String comparerName, final BiFunction<File, File, IFileContentResultBean> comparer)
	{
		final IFileContentResultBean result = comparer.apply(notCreated("a/same.txt"),
			notCreated("b/same.txt"));

		assertFalse(result.getContentEquality());
	}

	/**
	 * Test method for {@link CompareFileExtensions#compareFiles(File, File, boolean)}
	 */
	@Test
	public void compareFilesWithContentFlag_returnsFalse_whenOnlyTheSourceIsMissing()
		throws IOException
	{
		final File missing = notCreated("a/same.txt");
		final File compare = newFile("b/same.txt", "abc");

		assertFalse(CompareFileExtensions.compareFiles(missing, compare, false));
	}

	/**
	 * Test method for {@link CompareFileExtensions#compareFiles(File, File, boolean)}
	 */
	@Test
	public void compareFilesWithContentFlag_returnsFalse_whenOnlyTheFileToCompareIsMissing()
		throws IOException
	{
		final File source = newFile("a/same.txt", "abc");
		final File missing = notCreated("b/same.txt");

		assertFalse(CompareFileExtensions.compareFiles(source, missing, false));
	}

	/**
	 * Test method for {@link CompareFileExtensions#compareFiles(File, File, boolean)}
	 */
	@Test
	public void compareFilesWithContentFlag_returnsFalse_whenTheSourceHasALineMoreThanTheFileToCompare()
		throws IOException
	{
		final File source = newFile("a/same.txt", "x\n\n");
		final File compare = newFile("b/same.txt", "x\r\n");
		assertEquals(source.length(), compare.length());

		assertFalse(CompareFileExtensions.compareFiles(source, compare, true));
	}

	/**
	 * Test method for {@link CompareFileExtensions#compareFiles(File, File, boolean)}
	 */
	@Test
	public void compareFilesWithContentFlag_returnsFalse_whenTheContentOfTwoDirectoriesIsCompared()
		throws IOException
	{
		final File source = newDirectory("a/same");
		final File compare = newDirectory("b/same");
		assumeTrue(source.length() == compare.length(),
			"the file system gives two empty directories different lengths");

		assertFalse(CompareFileExtensions.compareFiles(source, compare, true));
	}

	/**
	 * Test method for {@link CompareFileExtensions#findEqualFiles(File)},
	 * {@link CompareFileExtensions#findEqualFiles(File, boolean, boolean, boolean, boolean, boolean)},
	 * {@link CompareFileExtensions#findEqualFilesWithSameContent(File)} and
	 * {@link CompareFileExtensions#findEqualFilesWithSameContent(File, boolean, boolean, boolean, boolean, boolean, boolean)}
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("finders")
	public void findEqualFiles_findsEveryPairOfEqualFilesInBothOrders(final String finderName,
		final Function<File, List<? extends IFileCompareResultBean>> finder) throws IOException
	{
		final File first = newFile("root/a/same.txt", "abc");
		final File second = newFile("root/b/same.txt", "abc");
		newFile("root/c/other.txt", "abc");
		newFile("root/d/same.txt", "abcd");
		newFile("root/e/same.csv", "abc");

		final List<? extends IFileCompareResultBean> found = finder
			.apply(tempDir.resolve("root").toFile());

		assertEquals(2, found.size());
		assertEquals(Set.of(List.of(first, second), List.of(second, first)), pairsOf(found));
	}

	/**
	 * Test method for {@link CompareFileExtensions#findEqualFiles(File)},
	 * {@link CompareFileExtensions#findEqualFiles(File, boolean, boolean, boolean, boolean, boolean)},
	 * {@link CompareFileExtensions#findEqualFilesWithSameContent(File)} and
	 * {@link CompareFileExtensions#findEqualFilesWithSameContent(File, boolean, boolean, boolean, boolean, boolean, boolean)}
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("finders")
	public void findEqualFiles_pairsTheFilesWithoutAnExtension_besideAFileWithOne(
		final String finderName,
		final Function<File, List<? extends IFileCompareResultBean>> finder) throws IOException
	{
		final File first = newFile("root/a/README", "abc");
		final File second = newFile("root/b/README", "abc");
		newFile("root/c/README.txt", "abc");

		final List<? extends IFileCompareResultBean> found = finder
			.apply(tempDir.resolve("root").toFile());

		assertEquals(Set.of(List.of(first, second), List.of(second, first)), pairsOf(found));
	}

	/**
	 * Test method for {@link CompareFileExtensions#findEqualFiles(File, File)},
	 * {@link CompareFileExtensions#findEqualFiles(File, File, boolean, boolean, boolean, boolean, boolean)},
	 * {@link CompareFileExtensions#findEqualFilesWithSameContent(File, File)} and
	 * {@link CompareFileExtensions#findEqualFilesWithSameContent(File, File, boolean, boolean, boolean, boolean, boolean, boolean)}
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("twoDirectoryFinders")
	public void findEqualFilesInTwoDirectories_pairsTheFilesWithoutAnExtension_besideAFileWithOne(
		final String finderName,
		final BiFunction<File, File, List<? extends IFileCompareResultBean>> finder)
		throws IOException
	{
		final File source = newFile("source/README", "abc");
		newFile("source/README.txt", "abc");
		final File copy = newFile("compare/README", "abc");

		final List<? extends IFileCompareResultBean> found = finder
			.apply(tempDir.resolve("source").toFile(), tempDir.resolve("compare").toFile());

		assertEquals(Set.of(List.of(source, copy)), pairsOf(found));
	}

	/**
	 * Test method for {@link CompareFileExtensions#findEqualFiles(File)}
	 */
	@Test
	public void findEqualFiles_reportsTheSimpleComparisonOfEachPair() throws IOException
	{
		final File first = newFile("root/a/same.txt", "abc", LAST_MODIFIED);
		final File second = newFile("root/b/same.txt", "abc", LATER);

		final List<IFileCompareResultBean> found = CompareFileExtensions
			.findEqualFiles(tempDir.resolve("root").toFile());

		assertEquals(2, found.size());
		assertEquals(
			Set.of(newCompareResult(first, second, true), newCompareResult(second, first, true)),
			new HashSet<>(found));
	}

	/**
	 * Test method for
	 * {@link CompareFileExtensions#findEqualFiles(File, boolean, boolean, boolean, boolean, boolean)}
	 */
	@Test
	public void findEqualFilesWithFlags_comparesTheModificationTime_unlessItIsIgnored()
		throws IOException
	{
		final File first = newFile("root/a/same.txt", "abc", LAST_MODIFIED);
		final File second = newFile("root/b/same.txt", "abc", LAST_MODIFIED);
		final File later = newFile("root/c/same.txt", "abc", LATER);
		final File root = tempDir.resolve("root").toFile();

		final List<IFileCompareResultBean> comparingTime = CompareFileExtensions
			.findEqualFiles(root, false, false, false, false, false);
		final List<IFileCompareResultBean> ignoringTime = CompareFileExtensions.findEqualFiles(root,
			false, false, false, true, false);

		assertEquals(
			Set.of(newCompareResult(first, second, false), newCompareResult(second, first, false)),
			new HashSet<>(comparingTime));
		assertEquals(
			Set.of(List.of(first, second), List.of(second, first), List.of(first, later),
				List.of(later, first), List.of(second, later), List.of(later, second)),
			pairsOf(ignoringTime));
	}

	/**
	 * Test method for {@link CompareFileExtensions#findEqualFilesWithSameContent(File)}
	 */
	@Test
	public void findEqualFilesWithSameContent_leavesOutAPairWithDifferentContent_andIgnoresTheModificationTime()
		throws IOException
	{
		final File first = newFile("root/a/same.txt", "abc");
		newFile("root/b/same.txt", "xyz");
		final File later = newFile("root/c/same.txt", "abc", LATER);

		final List<IFileContentResultBean> found = CompareFileExtensions
			.findEqualFilesWithSameContent(tempDir.resolve("root").toFile());

		assertEquals(Set.of(List.of(first, later), List.of(later, first)), pairsOf(found));
		assertTrue(found.stream().allMatch(IFileContentResultBean::getContentEquality));
	}

	/**
	 * Test method for
	 * {@link CompareFileExtensions#findEqualFilesWithSameContent(File, boolean, boolean, boolean, boolean, boolean, boolean)}
	 */
	@Test
	public void findEqualFilesWithSameContentWithFlags_leavesOutAPairWithDifferentContent()
		throws IOException
	{
		final File first = newFile("root/a/same.txt", "abc");
		newFile("root/b/same.txt", "xyz");
		final File third = newFile("root/c/same.txt", "abc");

		final List<IFileContentResultBean> found = CompareFileExtensions
			.findEqualFilesWithSameContent(tempDir.resolve("root").toFile(), false, false, false,
				false, false, false);

		assertEquals(Set.of(List.of(first, third), List.of(third, first)), pairsOf(found));
		assertTrue(found.stream().allMatch(IFileContentResultBean::getContentEquality));
	}

	/**
	 * Test method for {@link CompareFileExtensions#findEqualFiles(File, File)}
	 */
	@Test
	public void findEqualFilesInTwoDirectories_pairsEachSourceFileWithItsEqualInTheOtherTree()
		throws IOException
	{
		final File same = newFile("source/same.txt", "abc");
		final File other = newFile("source/other.txt", "abc");
		final File late = newFile("source/late.txt", "abc", LAST_MODIFIED);
		final File sameCopy = newFile("compare/same.txt", "abc");
		final File otherCopy = newFile("compare/sub/other.txt", "abc");
		final File lateCopy = newFile("compare/late.txt", "abc", LATER);
		newFile("compare/same.csv", "abc");

		final List<IFileCompareResultBean> found = CompareFileExtensions.findEqualFiles(
			tempDir.resolve("source").toFile(), tempDir.resolve("compare").toFile());

		assertEquals(Set.of(newCompareResult(same, sameCopy, true),
			newCompareResult(other, otherCopy, true), newCompareResult(late, lateCopy, true)),
			new HashSet<>(found));
	}

	/**
	 * Test method for
	 * {@link CompareFileExtensions#findEqualFiles(File, File, boolean, boolean, boolean, boolean, boolean)}
	 */
	@Test
	public void findEqualFilesInTwoDirectoriesWithFlags_leavesOutAPairWithDifferentModificationTimes()
		throws IOException
	{
		final File same = newFile("source/same.txt", "abc");
		final File other = newFile("source/other.txt", "abc");
		newFile("source/late.txt", "abc", LAST_MODIFIED);
		final File sameCopy = newFile("compare/same.txt", "abc");
		final File otherCopy = newFile("compare/sub/other.txt", "abc");
		newFile("compare/late.txt", "abc", LATER);

		final List<IFileCompareResultBean> found = CompareFileExtensions.findEqualFiles(
			tempDir.resolve("source").toFile(), tempDir.resolve("compare").toFile(), false, false,
			false, false, false);

		assertEquals(Set.of(newCompareResult(same, sameCopy, false),
			newCompareResult(other, otherCopy, false)), new HashSet<>(found));
	}

	/**
	 * Test method for {@link CompareFileExtensions#findEqualFilesWithSameContent(File, File)}
	 */
	@Test
	public void findEqualFilesWithSameContentInTwoDirectories_pairsEachSourceFileWithItsCopy()
		throws IOException
	{
		final File same = newFile("source/same.txt", "abc");
		final File other = newFile("source/other.txt", "abc");
		final File sameCopy = newFile("compare/same.txt", "abc");
		final File otherCopy = newFile("compare/sub/other.txt", "abc");
		newFile("compare/same.csv", "abc");

		final List<IFileContentResultBean> found = CompareFileExtensions
			.findEqualFilesWithSameContent(tempDir.resolve("source").toFile(),
				tempDir.resolve("compare").toFile());

		assertEquals(Set.of(List.of(same, sameCopy), List.of(other, otherCopy)), pairsOf(found));
	}

	/**
	 * Test method for {@link CompareFileExtensions#findEqualFilesWithSameContent(File, File)}
	 */
	@Test
	public void findEqualFilesWithSameContentInTwoDirectories_leavesOutAPairWithDifferentContent_andIgnoresTheModificationTime()
		throws IOException
	{
		final File same = newFile("source/same.txt", "abc");
		newFile("source/changed.txt", "abc");
		final File laterCopy = newFile("compare/same.txt", "abc", LATER);
		newFile("compare/changed.txt", "xyz");

		final List<IFileContentResultBean> found = CompareFileExtensions
			.findEqualFilesWithSameContent(tempDir.resolve("source").toFile(),
				tempDir.resolve("compare").toFile());

		assertEquals(Set.of(List.of(same, laterCopy)), pairsOf(found));
		assertTrue(found.get(0).getContentEquality());
	}

	/**
	 * Test method for
	 * {@link CompareFileExtensions#findEqualFilesWithSameContent(File, File, boolean, boolean, boolean, boolean, boolean, boolean)}
	 */
	@Test
	public void findEqualFilesWithSameContentInTwoDirectoriesWithFlags_leavesOutAPairWithDifferentContent()
		throws IOException
	{
		final File same = newFile("source/same.txt", "abc");
		newFile("source/changed.txt", "abc");
		final File sameCopy = newFile("compare/same.txt", "abc");
		newFile("compare/changed.txt", "xyz");

		final List<IFileContentResultBean> found = CompareFileExtensions
			.findEqualFilesWithSameContent(tempDir.resolve("source").toFile(),
				tempDir.resolve("compare").toFile(), false, false, false, false, false, false);

		assertEquals(Set.of(List.of(same, sameCopy)), pairsOf(found));
		assertTrue(found.get(0).getContentEquality());
		assertFalse(found.get(0).getAbsolutePathEquality());
	}

	/**
	 * Test method for {@link CompareFileExtensions#findEqualFiles(File, File)},
	 * {@link CompareFileExtensions#findEqualFiles(File, File, boolean, boolean, boolean, boolean, boolean)},
	 * {@link CompareFileExtensions#findEqualFilesWithSameContent(File, File)} and
	 * {@link CompareFileExtensions#findEqualFilesWithSameContent(File, File, boolean, boolean, boolean, boolean, boolean, boolean)}
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("twoDirectoryFinders")
	public void findEqualFilesInTwoDirectories_neverPairsAFileWithItself_whenBothDirectoriesAreTheSame(
		final String finderName,
		final BiFunction<File, File, List<? extends IFileCompareResultBean>> finder)
		throws IOException
	{
		final File first = newFile("root/a/same.txt", "abc");
		final File second = newFile("root/b/same.txt", "abc");
		final File root = tempDir.resolve("root").toFile();

		final List<? extends IFileCompareResultBean> found = finder.apply(root, root);

		assertEquals(2, found.size());
		assertEquals(Set.of(List.of(first, second), List.of(second, first)), pairsOf(found));
	}

}
