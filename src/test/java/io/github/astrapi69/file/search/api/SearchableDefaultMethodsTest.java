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
package io.github.astrapi69.file.search.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests the default methods of {@link Searchable} against a directory tree built in a temporary
 * directory:
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
public class SearchableDefaultMethodsTest
{

	private final Searchable searchable = new Searchable()
	{
	};

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

	static Stream<Arguments> extensionCases()
	{
		return Stream.of(
			Arguments.of("one extension in every level", "", new String[] { ".txt" },
				Set.of("report-1.txt", "notes.txt", "report-3.txt", "report-4.txt")),
			Arguments.of("several extensions", "", new String[] { ".log", ".txt" },
				Set.of("report-1.txt", "report-2.log", "notes.txt", "report-3.txt",
					"report-4.txt")),
			Arguments.of("a start that is a regular file finds nothing", "notes.txt",
				new String[] { ".txt" }, Set.of()),
			Arguments.of("a start that does not exist finds nothing", "missing",
				new String[] { ".txt" }, Set.of()));
	}

	static Stream<Arguments> topLevelFileCases()
	{
		return Stream.of(
			Arguments.of("the files of the top level, no directories", "",
				Set.of("report-1.txt", "report-2.log", "notes.txt")),
			Arguments.of("an empty directory has no files", "empty", Set.of()),
			Arguments.of("a regular file has no files", "notes.txt", Set.of()));
	}

	static Stream<Arguments> prefixAndExtensionCases()
	{
		return Stream.of(
			Arguments.of("prefix and extension in every level", "", "report", "txt",
				Set.of("report-1.txt", "report-3.txt", "report-4.txt")),
			Arguments.of("the prefix is a prefix, not a part", "", "eport", "txt", Set.of()),
			Arguments.of("the extension follows a dot", "", "report", "og", Set.of()),
			Arguments.of("a regular file finds nothing", "notes.txt", "notes", "txt", Set.of()));
	}

	private static Set<String> namesOf(Collection<File> files)
	{
		return files.stream().map(File::getName).collect(Collectors.toSet());
	}

	private static void assertFoundExactly(Set<String> expectedNames, Collection<File> found)
	{
		assertEquals(expectedNames, namesOf(found));
		assertEquals(expectedNames.size(), found.size(), "no file is found twice");
		assertTrue(found.stream().allMatch(File::isFile),
			"every found file exists as a regular file: " + found);
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
		File deepFile = tempDir.resolve("sub").resolve("deeper").resolve("report-4.txt").toFile();

		File rootDirectory = searchable.getRootDirectory(deepFile);

		assertEquals(tempDir.getRoot().toFile(), rootDirectory);
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("directChildrenCases")
	void containsFileByName_findsOnlyDirectChildren(String displayName, String parent, String name,
		boolean expected)
	{
		assertEquals(expected, searchable.containsFile(tempDir.resolve(parent).toFile(), name));
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("directChildrenCases")
	void containsFileByFile_findsOnlyDirectChildren(String displayName, String parent, String name,
		boolean expected)
	{
		File search = tempDir.resolve("elsewhere").resolve(name).toFile();

		assertEquals(expected, searchable.containsFile(tempDir.resolve(parent).toFile(), search));
	}

	@Test
	void findFilesWithPredicate_returnsOnlyMatchingDirectChildren() throws IOException
	{
		Set<File> found = searchable.findFiles(tempDir.toFile(),
			file -> file.getName().endsWith(".txt"));

		assertFoundExactly(Set.of("report-1.txt", "notes.txt"), found);
	}

	@Test
	void findFilesRecursiveWithPredicate_returnsMatchingFilesOfEveryLevel() throws IOException
	{
		Set<File> found = searchable.findFilesRecursive(tempDir.toFile(),
			file -> file.isFile() && file.getName().endsWith(".txt"));

		assertFoundExactly(Set.of("report-1.txt", "notes.txt", "report-3.txt", "report-4.txt"),
			found);
	}

	@Test
	void findFilesRecursiveWithPredicate_includesDirectories_whenPredicateAcceptsThem()
		throws IOException
	{
		Set<File> found = searchable.findFilesRecursive(tempDir.toFile(), File::isDirectory);

		assertEquals(Set.of(tempDir.getFileName().toString(), "empty", "sub", "deeper"),
			namesOf(found));
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("countCases")
	void countAllFilesInDirectory_countsRecursively(String displayName, String directory,
		long start, boolean includeDirectories, long expected)
	{
		long count = searchable.countAllFilesInDirectory(tempDir.resolve(directory).toFile(), start,
			includeDirectories);

		assertEquals(expected, count);
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("regexCases")
	void findAllFiles_returnsFilesWhoseNameMatchesTheRegex(String displayName, String directory,
		String regex, Set<String> expectedNames)
	{
		List<File> found = searchable.findAllFiles(tempDir.resolve(directory).toFile(), regex);

		assertFoundExactly(expectedNames, found);
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("extensionCases")
	void findFilesWithExtensions_returnsFilesEndingWithOneOfThem(String displayName, String start,
		String[] extensions, Set<String> expectedNames)
	{
		List<File> found = searchable.findFiles(tempDir.resolve(start).toString(), extensions);

		assertFoundExactly(expectedNames, found);
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("topLevelFileCases")
	void getAllFilesFromDir_returnsTheRegularFilesOfTheTopLevelOnly(String displayName,
		String directory, Set<String> expectedNames)
	{
		List<File> found = searchable.getAllFilesFromDir(tempDir.resolve(directory).toFile());

		assertFoundExactly(expectedNames, found);
	}

	@Test
	void getAllFilesFromDirRecursive_returnsTheRegularFilesOfEveryLevel()
	{
		List<File> found = searchable.getAllFilesFromDirRecursive(tempDir.toFile());

		assertFoundExactly(
			Set.of("report-1.txt", "report-2.log", "notes.txt", "report-3.txt", "report-4.txt"),
			found);
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("prefixAndExtensionCases")
	void findFilesWithPrefixAndExtensionRecursive_matchesStartAndEndOfTheName(String displayName,
		String directory, String prefix, String extension, Set<String> expectedNames)
	{
		List<File> found = searchable.findFilesWithPrefixAndExtensionRecursive(
			tempDir.resolve(directory).toFile(), prefix, extension);

		assertFoundExactly(expectedNames, found);
	}

	@Test
	void findFilesWithExtensions_skipsASubdirectoryThatCannotBeListed()
	{
		File sub = tempDir.resolve("sub").toFile();
		try
		{
			assumeTrue(sub.setReadable(false), "the file system takes the permission");
			assumeFalse(Files.isReadable(sub.toPath()), "the test runs without override rights");

			List<File> found = searchable.findFiles(tempDir.toString(), new String[] { ".txt" });

			assertFoundExactly(Set.of("report-1.txt", "notes.txt"), found);
		}
		finally
		{
			sub.setReadable(true);
		}
	}
}
