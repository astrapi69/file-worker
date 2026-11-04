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
package io.github.astrapi69.file.search;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import io.github.astrapi69.file.search.api.Searchable;

/**
 * Tests {@link FileSearchExtensions} against a directory tree built in a temporary directory, with
 * the edge cases of each search: an empty directory, a regular file and a missing file in place of
 * the directory.
 *
 * <pre>
 * report-1.txt
 * report-2.log
 * notes.txt
 * empty/
 * sub/report-3.txt
 * sub/deeper/report-4.txt
 * </pre>
 */
public class FileSearchExtensionsEdgeCasesTest
{

	private static final Set<String> ALL_FILES = Set.of("report-1.txt", "report-2.log", "notes.txt",
		"report-3.txt", "report-4.txt");

	private static final Set<String> TXT_FILES = Set.of("report-1.txt", "notes.txt", "report-3.txt",
		"report-4.txt");

	/**
	 * Names with characters a regular expression reads as syntax, each beside a name the syntax
	 * would match instead (file-worker#58), and a name whose prefix reaches into its extension
	 * beside one where it does not (file-worker#88)
	 */
	private static final List<String> NAMES_WITH_REGEX_CHARACTERS = List.of("file(1)-a.txt",
		"file1-a.txt", "data.1.txt", "dataX1.txt", "[draft] plan.txt", "d plan.txt", "notes.t.t",
		"notes.txt", "main.c++", "report.txt", "report..txt");

	@TempDir
	Path tempDir;

	static Stream<Arguments> directChildrenCases()
	{
		return Stream.of(Arguments.of("a direct child is found", "", "notes.txt", true),
			Arguments.of("a file of a subdirectory is not a direct child", "", "report-3.txt",
				false),
			Arguments.of("a name that is not there is not found", "", "missing.txt", false),
			Arguments.of("a regular file has no children", "notes.txt", "notes.txt", false),
			Arguments.of("a missing directory has no children", "missing", "notes.txt", false));
	}

	static Stream<Arguments> recursiveContainsCases()
	{
		return Stream.of(
			Arguments.of("a file two levels down", "", "sub/deeper/report-4.txt", true),
			Arguments.of("a direct child", "", "notes.txt", true),
			Arguments.of("a file outside the directory", "sub", "notes.txt", false),
			Arguments.of("an empty directory contains nothing", "empty", "notes.txt", false),
			Arguments.of("a regular file contains nothing", "notes.txt", "notes.txt", false));
	}

	static Stream<Arguments> countCases()
	{
		return Stream.of(Arguments.of("files only", "", 0L, false, 5L),
			Arguments.of("files and directories", "", 0L, true, 8L),
			Arguments.of("the start value is added to", "", 10L, false, 15L),
			Arguments.of("an empty directory returns the start value", "empty", 3L, true, 3L),
			Arguments.of("a regular file returns the start value", "notes.txt", 7L, false, 7L));
	}

	static Stream<Arguments> regexCases()
	{
		return Stream.of(
			Arguments.of("matches in every level", "", "report-\\d\\.txt",
				Set.of("report-1.txt", "report-3.txt", "report-4.txt")),
			Arguments.of("the whole name has to match", "", "report", Set.of()),
			Arguments.of("an empty directory has no matches", "empty", ".*", Set.of()),
			Arguments.of("a regular file has no matches", "notes.txt", ".*", Set.of()));
	}

	/**
	 * The searches of the tree {@code top.txt}, {@code sub/middle.txt} and
	 * {@code sub/deeper/bottom.txt}, with the paths, relative to the searched directory, of the
	 * files each one finds (file-worker#111)
	 */
	static Stream<Arguments> recursionCases()
	{
		return Stream.of(
			Arguments.of("every level is searched", ".*\\.txt",
				Set.of("top.txt", "sub/middle.txt", "sub/deeper/bottom.txt")),
			Arguments.of("a match two levels down is found", "bottom\\.txt",
				Set.of("sub/deeper/bottom.txt")),
			Arguments.of("directories are searched, not returned", ".*",
				Set.of("top.txt", "sub/middle.txt", "sub/deeper/bottom.txt")),
			Arguments.of("a pattern that matches only directory names finds nothing", "sub|deeper",
				Set.of()),
			Arguments.of("the pattern is matched against the name, not the path",
				"sub/middle\\.txt", Set.of()));
	}

	static Stream<Arguments> wildcardCases()
	{
		return Stream.of(
			Arguments.of("'?' stands for one character, '*' for any", "", "report-?.*",
				Set.of("report-1.txt", "report-2.log")),
			Arguments.of("only the top level is searched", "", "report-3.txt", Set.of()),
			Arguments.of("a regular file has no matches", "notes.txt", "*", Set.of()),
			Arguments.of("a missing directory has no matches", "missing", "*", Set.of()));
	}

	static Stream<Arguments> extensionCases()
	{
		return Stream.of(
			Arguments.of("the case of the extension does not matter", "", new String[] { ".TXT" },
				TXT_FILES),
			Arguments.of("several extensions", "", new String[] { ".log", ".txt" }, ALL_FILES),
			Arguments.of("a start that is a regular file finds nothing", "notes.txt",
				new String[] { ".txt" }, Set.of()),
			Arguments.of("a start that does not exist finds nothing", "missing",
				new String[] { ".txt" }, Set.of()));
	}

	static Stream<Arguments> recursiveListingCases()
	{
		Set<String> filesAndDirectories = new HashSet<>(ALL_FILES);
		filesAndDirectories.addAll(Set.of("empty", "sub", "deeper"));
		return Stream.of(Arguments.of("the files of every level", "", false, ALL_FILES),
			Arguments.of("with the directories", "", true, filesAndDirectories),
			Arguments.of("an empty directory has nothing", "empty", true, Set.of()),
			Arguments.of("a regular file has nothing", "notes.txt", true, Set.of()));
	}

	static Stream<Arguments> extensionFilterCases()
	{
		return Stream.of(
			Arguments.of("one extension in every level", "", new String[] { ".txt" }, TXT_FILES),
			Arguments.of("several extensions", "", new String[] { ".log", ".txt" }, ALL_FILES),
			Arguments.of("a regular file has nothing", "notes.txt", new String[] { ".txt" },
				Set.of()));
	}

	static Stream<Arguments> lineIndexCases()
	{
		return Stream.of(Arguments.of("the first line", List.of("alpha", "beta"), "alpha", 0),
			Arguments.of("the start of a later line", List.of("alpha", "beta", "gamma"), "gam", 2),
			Arguments.of("the first of two matching lines", List.of("x", "beta 1", "beta 2"),
				"beta", 1),
			Arguments.of("a line that only contains the string", List.of("alphabet"), "bet", -1),
			Arguments.of("no line", List.of("alpha", "beta"), "delta", -1),
			Arguments.of("an empty file", List.of(), "alpha", -1));
	}

	static Stream<Arguments> topLevelFileCases()
	{
		return Stream.of(
			Arguments.of("the files of the top level, no directories", "",
				Set.of("report-1.txt", "report-2.log", "notes.txt")),
			Arguments.of("a directory with one file", "sub/deeper", Set.of("report-4.txt")),
			Arguments.of("an empty directory has no files", "empty", Set.of()),
			Arguments.of("a regular file has no files", "notes.txt", Set.of()));
	}

	static Stream<Arguments> recursiveFileListingCases()
	{
		return Stream.of(Arguments.of("the files of every level, no directories", "", ALL_FILES),
			Arguments.of("one level down", "sub", Set.of("report-3.txt", "report-4.txt")),
			Arguments.of("an empty directory has nothing", "empty", Set.of()),
			Arguments.of("a regular file has nothing", "notes.txt", Set.of()));
	}

	static Stream<Arguments> searchPatternCases()
	{
		return Stream.of(Arguments.of("the case of the extension does not matter", "a.TXT", true),
			Arguments.of("the second extension", "b.log", true),
			Arguments.of("another extension", "c.csv", false),
			Arguments.of("the extension without a name before the dot", "txt", false),
			Arguments.of("the extension has to end the name", "archive.txt.gz", false));
	}

	static Stream<Arguments> listDirsCases()
	{
		return Stream.of(
			Arguments.of("the directories of the top level", "", Set.of("empty", "sub")),
			Arguments.of("one level down", "sub", Set.of("deeper")),
			Arguments.of("an empty directory has none", "empty", Set.of()),
			Arguments.of("a regular file has none", "notes.txt", Set.of()));
	}

	static Stream<Arguments> matchCases()
	{
		return Stream.of(
			Arguments.of("the suffix ends the name", "report.txt", new String[] { ".txt" }, true),
			Arguments.of("the case does not matter", "REPORT.TXT", new String[] { ".txt" }, true),
			Arguments.of("a name exactly as long as the suffix", ".txt", new String[] { ".txt" },
				true),
			Arguments.of("the second suffix ends the name", "report.txt",
				new String[] { ".log", ".txt" }, true),
			Arguments.of("no suffix ends the name", "report.txt", new String[] { ".log", ".csv" },
				false),
			Arguments.of("no suffixes at all", "report.txt", new String[] { }, false),
			Arguments.of("a name shorter than the suffix", "a", new String[] { ".txt" }, false),
			Arguments.of("an empty name", "", new String[] { ".txt" }, false),
			Arguments.of("a name shorter than the first suffix ends with the second", "a.c",
				new String[] { ".txt", ".c" }, true));
	}

	static Stream<Arguments> prefixAndExtensionCases()
	{
		return Stream.of(
			Arguments.of("prefix and extension", "", "report", "txt", Set.of("report-1.txt")),
			Arguments.of("the prefix starts the name", "", "eport", "txt", Set.of()),
			Arguments.of("a directory is not a file", "", "sub", "txt", Set.of()),
			Arguments.of("a regular file has nothing", "notes.txt", "notes", "txt", Set.of()));
	}

	static Stream<Arguments> recursivePrefixAndExtensionCases()
	{
		return Stream.of(
			Arguments.of("prefix and extension in every level", "", "report", "txt",
				Set.of("report-1.txt", "report-3.txt", "report-4.txt")),
			Arguments.of("another extension", "", "report", "log", Set.of("report-2.log")),
			Arguments.of("a regular file has nothing", "notes.txt", "notes", "txt", Set.of()));
	}

	static Stream<Arguments> literalPrefixAndExtensionCases()
	{
		return Stream.of(
			Arguments.of("parentheses in the prefix are characters", "file(1)", "txt",
				Set.of("file(1)-a.txt")),
			Arguments.of("a dot in the prefix is a dot", "data.", "txt", Set.of("data.1.txt")),
			Arguments.of("brackets in the prefix are characters", "[draft]", "txt",
				Set.of("[draft] plan.txt")),
			Arguments.of("an unclosed bracket in the prefix is a character", "[", "txt",
				Set.of("[draft] plan.txt")),
			Arguments.of("a dot in the extension is a dot", "notes", "t.t", Set.of("notes.t.t")),
			Arguments.of("plus signs in the extension are characters", "main", "c++",
				Set.of("main.c++")),
			Arguments.of("the prefix may end with the dot before the extension", "report.", "txt",
				Set.of("report.txt", "report..txt")));
	}

	static Stream<Arguments> fileLengthCases()
	{
		int kilobyte = 1024;
		int megabyte = 1024 * kilobyte;
		return Stream.of(Arguments.of("an empty file", 0, 0L, 0L),
			Arguments.of("one byte less than a kilobyte", kilobyte - 1, 0L, 0L),
			Arguments.of("one kilobyte", kilobyte, 1L, 0L),
			Arguments.of("two kilobytes, the file measured in #57", 2 * kilobyte, 2L, 0L),
			Arguments.of("one byte less than a megabyte", megabyte - 1, 1023L, 0L),
			Arguments.of("one megabyte", megabyte, 1024L, 1L),
			Arguments.of("one byte more than a megabyte", megabyte + 1, 1024L, 1L));
	}

	private static Path createLevels(Path directory) throws IOException
	{
		Path deeper = Files.createDirectories(directory.resolve("sub").resolve("deeper"));
		Files.writeString(directory.resolve("top.txt"), "top");
		Files.writeString(directory.resolve("sub").resolve("middle.txt"), "middle");
		Files.writeString(deeper.resolve("bottom.txt"), "bottom");
		return directory;
	}

	private static Set<File> filesIn(Path directory, Set<String> relativePaths)
	{
		return relativePaths.stream().map(directory::resolve).map(Path::toFile)
			.collect(Collectors.toSet());
	}

	private static Set<String> namesOf(Collection<File> files)
	{
		return files.stream().map(File::getName).collect(Collectors.toSet());
	}

	private static void assertFoundExactly(Set<String> expectedNames, Collection<File> found)
	{
		assertEquals(expectedNames, namesOf(found));
		assertEquals(expectedNames.size(), found.size(), "nothing is found twice");
		assertTrue(found.stream().allMatch(File::exists), "every found file exists: " + found);
	}

	private File inTempDir(String relativePath)
	{
		return tempDir.resolve(relativePath).toFile();
	}

	private static void createNamesWithRegexCharacters(Path directory) throws IOException
	{
		Files.createDirectories(directory);
		for (String name : NAMES_WITH_REGEX_CHARACTERS)
		{
			Files.writeString(directory.resolve(name), name);
		}
	}

	@BeforeEach
	void createTree() throws IOException
	{
		Files.writeString(tempDir.resolve("report-1.txt"), "first report");
		Files.writeString(tempDir.resolve("report-2.log"), "second report");
		Files.writeString(tempDir.resolve("notes.txt"), "some notes");
		Files.createDirectory(tempDir.resolve("empty"));
		Path deeper = Files.createDirectories(tempDir.resolve("sub").resolve("deeper"));
		Files.writeString(tempDir.resolve("sub").resolve("report-3.txt"), "third report");
		Files.writeString(deeper.resolve("report-4.txt"), "fourth report");
	}

	@Test
	void getRootDirectory_returnsTheFileSystemRoot_whenFileIsAbsolute()
	{
		File rootDirectory = FileSearchExtensions
			.getRootDirectory(inTempDir("sub/deeper/report-4.txt"));

		assertEquals(tempDir.getRoot().toFile(), rootDirectory);
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("directChildrenCases")
	void containsFileByName_findsOnlyDirectChildren(String displayName, String parent, String name,
		boolean expected)
	{
		assertEquals(expected, FileSearchExtensions.containsFile(inTempDir(parent), name));
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("directChildrenCases")
	void containsFileByFile_findsOnlyDirectChildren(String displayName, String parent, String name,
		boolean expected)
	{
		File search = tempDir.resolve("elsewhere").resolve(name).toFile();

		assertEquals(expected, FileSearchExtensions.containsFile(inTempDir(parent), search));
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("recursiveContainsCases")
	void containsFileRecursive_findsTheFileInEveryLevel(String displayName, String parent,
		String search, boolean expected)
	{
		assertEquals(expected,
			FileSearchExtensions.containsFileRecursive(inTempDir(parent), inTempDir(search)));
	}

	@Test
	void findFilesWithExcludeFilter_createsTheResultSet_whenNoneIsGiven()
	{
		Set<File> found = FileSearchExtensions.findFiles(tempDir.toFile(), null,
			file -> file.getName().endsWith(".log"));

		assertFoundExactly(TXT_FILES, found);
	}

	@ParameterizedTest
	@ValueSource(strings = { "empty", "notes.txt", "missing" })
	void findFilesWithExcludeFilter_returnsTheGivenSetUnchanged_whenThereAreNoChildren(
		String directory)
	{
		File alreadyFound = inTempDir("report-1.txt");
		Set<File> given = new HashSet<>(Set.of(alreadyFound));

		Set<File> found = FileSearchExtensions.findFiles(inTempDir(directory), given);

		assertSame(given, found);
		assertEquals(Set.of(alreadyFound), found);
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("countCases")
	void countAllFilesInDirectory_countsRecursively(String displayName, String directory,
		long start, boolean includeDirectories, long expected)
	{
		assertEquals(expected, FileSearchExtensions.countAllFilesInDirectory(inTempDir(directory),
			start, includeDirectories));
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("regexCases")
	void findAllFiles_returnsFilesWhoseNameMatchesTheRegex(String displayName, String directory,
		String regex, Set<String> expectedNames)
	{
		List<File> found = FileSearchExtensions.findAllFiles(inTempDir(directory), regex);

		assertFoundExactly(expectedNames, found);
		assertTrue(found.stream().allMatch(File::isAbsolute), "absolute files: " + found);
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("recursionCases")
	void findAllFiles_searchesEverySubdirectory(String displayName, String regex,
		Set<String> expectedPaths) throws IOException
	{
		Path levels = createLevels(tempDir.resolve("levels"));

		List<File> found = FileSearchExtensions.findAllFiles(levels.toFile(), regex);

		assertEquals(filesIn(levels, expectedPaths), Set.copyOf(found));
		assertEquals(expectedPaths.size(), found.size(), "nothing is found twice");
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("wildcardCases")
	void findFilesWithWildcards_returnsTheFilesMatchingThePattern(String displayName,
		String directory, String pattern, Set<String> expectedNames)
	{
		List<File> found = FileSearchExtensions.findFiles(inTempDir(directory), pattern);

		assertFoundExactly(expectedNames, found);
	}

	@Test
	void findFilesWithWildcards_returnsTheFilesOfTheSearchedDirectory() throws IOException
	{
		Files.writeString(tempDir.resolve("sub").resolve("found.txt"), "x");

		List<File> found = FileSearchExtensions.findFiles(inTempDir("sub"), "found.*");

		assertEquals(List.of(tempDir.resolve("sub").resolve("found.txt").toFile()), found);
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("extensionCases")
	void findFilesWithExtensions_returnsFilesEndingWithOneOfThem(String displayName, String start,
		String[] extensions, Set<String> expectedNames)
	{
		List<File> found = FileSearchExtensions.findFiles(inTempDir(start).getPath(), extensions);

		assertFoundExactly(expectedNames, found);
	}

	@Test
	void findFilesWithExtensions_findsEveryMatch_whenANameIsShorterThanTheSuffix()
		throws IOException
	{
		Files.writeString(tempDir.resolve("ab"), "a name shorter than the suffix");
		Files.writeString(tempDir.resolve("sub").resolve("c"), "another one, one level down");

		List<File> found = FileSearchExtensions.findFiles(tempDir.toString(),
			new String[] { ".txt" });

		assertFoundExactly(TXT_FILES, found);
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("recursiveListingCases")
	void getAllFilesFromDirRecursive_listsEveryLevel(String displayName, String directory,
		boolean includeDirectories, Set<String> expectedNames)
	{
		List<File> found = FileSearchExtensions.getAllFilesFromDirRecursive(inTempDir(directory),
			includeDirectories);

		assertFoundExactly(expectedNames, found);
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("recursiveFileListingCases")
	void getAllFilesFromDirRecursive_withoutTheFlag_listsTheFilesOfEveryLevel(String displayName,
		String directory, Set<String> expectedNames)
	{
		List<File> found = FileSearchExtensions.getAllFilesFromDirRecursive(inTempDir(directory));

		assertFoundExactly(expectedNames, found);
		assertTrue(found.stream().allMatch(File::isFile), "files only: " + found);
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("extensionFilterCases")
	void findFilesWithFilter_returnsFilesWithTheExtensionsInEveryLevel(String displayName,
		String directory, String[] extensions, Set<String> expectedNames)
	{
		List<File> found = FileSearchExtensions.findFilesWithFilter(inTempDir(directory),
			extensions);

		assertFoundExactly(expectedNames, found);
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("lineIndexCases")
	void findLineIndex_returnsTheIndexOfTheFirstLineStartingWithTheString(String displayName,
		List<String> lines, String search, int expected) throws IOException
	{
		File file = Files.write(tempDir.resolve("lines.txt"), lines).toFile();

		assertEquals(expected, FileSearchExtensions.findLineIndex(file, search));
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("topLevelFileCases")
	void getAllFilesFromDir_returnsTheRegularFilesOfTheTopLevelOnly(String displayName,
		String directory, Set<String> expectedNames)
	{
		List<File> found = FileSearchExtensions.getAllFilesFromDir(inTempDir(directory));

		assertFoundExactly(expectedNames, found);
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("fileLengthCases")
	void getFileLength_returnsTheLengthOfTheFileInWholeKilobytesAndMegabytes(String displayName,
		int bytes, long expectedKilobytes, long expectedMegabytes) throws IOException
	{
		File file = Files.write(tempDir.resolve("sized.bin"), new byte[bytes]).toFile();

		assertEquals(expectedKilobytes, FileSearchExtensions.getFileLengthInKilobytes(file));
		assertEquals(expectedMegabytes, FileSearchExtensions.getFileLengthInMegabytes(file));
	}

	@Test
	void getFileLength_returnsZero_whenTheFileDoesNotExist()
	{
		File missing = inTempDir("missing.bin");

		assertEquals(0L, FileSearchExtensions.getFileLengthInKilobytes(missing));
		assertEquals(0L, FileSearchExtensions.getFileLengthInMegabytes(missing));
	}

	@Test
	void getSearchFilePattern_returnsAnEmptyPattern_whenNoExtensionIsGiven()
	{
		assertEquals("", FileSearchExtensions.getSearchFilePattern());
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("searchPatternCases")
	void getSearchFilePattern_matchesNamesWithOneOfTheExtensions(String displayName,
		String filename, boolean expected)
	{
		String pattern = FileSearchExtensions.getSearchFilePattern("txt", "log");

		assertEquals(expected, filename.matches(pattern));
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("listDirsCases")
	void listDirs_returnsTheDirectSubdirectories(String displayName, String directory,
		Set<String> expectedNames)
	{
		List<File> found = FileSearchExtensions.listDirs(inTempDir(directory));

		assertEquals(expectedNames, namesOf(found));
		assertTrue(found.stream().allMatch(File::isDirectory), "directories only: " + found);
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("matchCases")
	void match_tellsWhetherOneOfTheSuffixesEndsTheString(String displayName, String name,
		String[] suffixes, boolean expected)
	{
		assertEquals(expected, FileSearchExtensions.match(name, suffixes));
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("prefixAndExtensionCases")
	void findFilesWithPrefixAndExtension_searchesTheTopLevelOnly(String displayName,
		String directory, String prefix, String extension, Set<String> expectedNames)
	{
		List<File> found = FileSearchExtensions
			.findFilesWithPrefixAndExtension(inTempDir(directory), prefix, extension);

		assertFoundExactly(expectedNames, found);
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("recursivePrefixAndExtensionCases")
	void findFilesWithPrefixAndExtensionRecursive_searchesEveryLevel(String displayName,
		String directory, String prefix, String extension, Set<String> expectedNames)
	{
		List<File> found = FileSearchExtensions
			.findFilesWithPrefixAndExtensionRecursive(inTempDir(directory), prefix, extension);

		assertFoundExactly(expectedNames, found);
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("literalPrefixAndExtensionCases")
	void findFilesWithPrefixAndExtension_readsPrefixAndExtensionAsText(String displayName,
		String prefix, String extension, Set<String> expectedNames) throws IOException
	{
		createNamesWithRegexCharacters(tempDir.resolve("literal"));

		List<File> found = FileSearchExtensions
			.findFilesWithPrefixAndExtension(inTempDir("literal"), prefix, extension);

		assertFoundExactly(expectedNames, found);
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("literalPrefixAndExtensionCases")
	void findFilesWithPrefixAndExtensionRecursive_readsPrefixAndExtensionAsText(String displayName,
		String prefix, String extension, Set<String> expectedNames) throws IOException
	{
		createNamesWithRegexCharacters(tempDir.resolve("literal").resolve("deeper"));

		List<File> found = FileSearchExtensions
			.findFilesWithPrefixAndExtensionRecursive(inTempDir("literal"), prefix, extension);

		assertFoundExactly(expectedNames, found);
	}

	/**
	 * {@link FileSearchExtensions} and {@link Searchable} answer alike, from one definition of a
	 * match (file-worker#88)
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("literalPrefixAndExtensionCases")
	void findFilesWithPrefixAndExtension_answersAsSearchable(String displayName, String prefix,
		String extension, Set<String> expectedNames) throws IOException
	{
		createNamesWithRegexCharacters(tempDir.resolve("literal"));
		createNamesWithRegexCharacters(tempDir.resolve("literal").resolve("deeper"));
		File directory = inTempDir("literal");
		Searchable searchable = new Searchable()
		{
		};

		assertEquals(
			Set.copyOf(searchable.findFilesWithPrefixAndExtension(directory, prefix, extension)),
			Set.copyOf(
				FileSearchExtensions.findFilesWithPrefixAndExtension(directory, prefix, extension)),
			"the top level");
		assertEquals(
			Set.copyOf(
				searchable.findFilesWithPrefixAndExtensionRecursive(directory, prefix, extension)),
			Set.copyOf(FileSearchExtensions.findFilesWithPrefixAndExtensionRecursive(directory,
				prefix, extension)),
			"every level");
	}

	@Test
	void findFilesWithExtensions_skipsASubdirectoryThatCannotBeListed()
	{
		File sub = tempDir.resolve("sub").toFile();
		try
		{
			assumeTrue(sub.setReadable(false), "the file system takes the permission");
			assumeFalse(Files.isReadable(sub.toPath()), "the test runs without override rights");

			List<File> found = FileSearchExtensions.findFiles(tempDir.toString(),
				new String[] { ".txt" });

			assertFoundExactly(Set.of("report-1.txt", "notes.txt"), found);
		}
		finally
		{
			sub.setReadable(true);
		}
	}

	static Stream<Arguments> searchesOfADirectoryWithoutFiles()
	{
		Function<File, List<File>> findAllFiles = dir -> FileSearchExtensions.findAllFiles(dir,
			"*");
		Function<File, List<File>> findFilesRecursive = dir -> FileSearchExtensions
			.findFilesRecursive(dir, true, "*");
		Function<File, List<File>> getAllFilesFromDir = FileSearchExtensions::getAllFilesFromDir;
		return Stream.of(Arguments.of("findAllFiles", "empty", findAllFiles),
			Arguments.of("findAllFiles", "missing", findAllFiles),
			Arguments.of("findFilesRecursive", "empty", findFilesRecursive),
			Arguments.of("findFilesRecursive", "missing", findFilesRecursive),
			Arguments.of("getAllFilesFromDir", "empty", getAllFilesFromDir),
			Arguments.of("getAllFilesFromDir", "missing", getAllFilesFromDir));
	}

	/**
	 * A search of a directory without files gives the same kind of list as a search that finds
	 * some: an empty one the caller can add to, not an unmodifiable one
	 */
	@ParameterizedTest(name = "{0} of the {1} directory")
	@MethodSource("searchesOfADirectoryWithoutFiles")
	void search_ofADirectoryWithoutFiles_isAnEmptyListTheCallerCanAddTo(final String search,
		final String directory, final Function<File, List<File>> searchOf)
	{
		File withoutFiles = tempDir.resolve(directory).toFile();

		List<File> found = searchOf.apply(withoutFiles);

		assertEquals(List.of(), found);
		found.add(withoutFiles);
		assertEquals(List.of(withoutFiles), found);
	}
}
