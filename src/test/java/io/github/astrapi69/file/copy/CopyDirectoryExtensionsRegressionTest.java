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
package io.github.astrapi69.file.copy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Regression tests for the File-based methods of {@link CopyDirectoryExtensions}
 */
public class CopyDirectoryExtensionsRegressionTest
{

	/** The files of the source tree, relative to the source directory */
	private static final List<String> SOURCE_FILES = List.of("top.txt", "sub/kept.txt", "sub/x.txt",
		"sub/deeper/kept.txt", "sub/deeper/x.txt", "sub/skipped/inner.txt");

	@TempDir
	Path tempDir;

	/**
	 * A File-based directory copy of {@link CopyDirectoryExtensions} and its result
	 */
	@FunctionalInterface
	interface DirectoryCopy
	{
		boolean copy(File source, File destination) throws Exception;
	}

	static Stream<Arguments> directoryCopies()
	{
		return Stream.of(
			Arguments.of("copyDirectory(File, File)",
				(DirectoryCopy)CopyDirectoryExtensions::copyDirectory),
			Arguments.of("copyDirectoryWithFilenameFilter(File, File, null, boolean)",
				(DirectoryCopy)(source, destination) -> CopyDirectoryExtensions
					.copyDirectoryWithFilenameFilter(source, destination, null, false)));
	}

	static Stream<Arguments> directoryCopiesOfTrees()
	{
		final Map<String, List<String>> trees = new LinkedHashMap<>();
		trees.put("an empty source", List.of());
		trees.put("only an empty subdirectory", List.of("empty/"));
		trees.put("only an empty subdirectory below a subdirectory", List.of("sub/", "sub/empty/"));
		trees.put("a file and an empty subdirectory", List.of("a.txt", "empty/"));
		return directoryCopies().flatMap(directoryCopy -> trees.entrySet().stream()
			.map(tree -> Arguments.of(directoryCopy.get()[0] + " of " + tree.getKey(),
				directoryCopy.get()[1], tree.getValue())));
	}

	private Path newTree(final String name, final List<String> entries) throws IOException
	{
		final Path root = Files.createDirectories(tempDir.resolve(name));
		for (final String entry : entries)
		{
			final Path path = root.resolve(entry);
			if (entry.endsWith("/"))
			{
				Files.createDirectories(path);
				continue;
			}
			Files.createDirectories(path.getParent());
			Files.write(path, entry.getBytes(StandardCharsets.US_ASCII));
		}
		return root;
	}

	private static Set<String> entriesUnder(final Path directory) throws IOException
	{
		final Function<Path, String> relative = path -> directory.relativize(path).toString()
			+ (Files.isDirectory(path) ? "/" : "");
		try (Stream<Path> paths = Files.walk(directory))
		{
			return paths.filter(path -> !path.equals(directory)).map(relative)
				.collect(Collectors.toCollection(TreeSet::new));
		}
	}

	static Stream<Arguments> excludedEntries()
	{
		return Stream.of(Arguments.of("a file one level down", "sub/x.txt"),
			Arguments.of("a file two levels down", "sub/deeper/x.txt"),
			Arguments.of("a directory one level down", "sub/skipped"));
	}

	private Path newSourceTree() throws IOException
	{
		final Path source = tempDir.resolve("src");
		for (final String relativePath : SOURCE_FILES)
		{
			final Path file = source.resolve(relativePath);
			Files.createDirectories(file.getParent());
			Files.write(file, relativePath.getBytes(StandardCharsets.US_ASCII));
		}
		return source;
	}

	private static Set<String> regularFilesUnder(final Path directory) throws IOException
	{
		try (Stream<Path> paths = Files.walk(directory))
		{
			return paths.filter(Files::isRegularFile)
				.map(path -> directory.relativize(path).toString())
				.collect(Collectors.toCollection(TreeSet::new));
		}
	}

	/**
	 * Test method for
	 * {@link CopyDirectoryExtensions#copyDirectoryWithFileFilter(java.io.File, java.io.File, java.io.FileFilter, java.io.FileFilter, java.util.Collection, boolean)}
	 * (#53)
	 *
	 * @param description
	 *            where the excluded entry is
	 * @param excludedPath
	 *            the excluded entry, relative to the source directory
	 * @throws Exception
	 *             if the copy fails
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("excludedEntries")
	public void copyDirectoryWithFileFilter_leavesOutAListedEntry_belowTheTopLevel(
		final String description, final String excludedPath) throws Exception
	{
		final Path source = newSourceTree();
		final Path excluded = source.resolve(excludedPath);
		final Path destination = tempDir.resolve("dst");
		final Set<String> expected = SOURCE_FILES.stream()
			.filter(relativePath -> !source.resolve(relativePath).startsWith(excluded))
			.collect(Collectors.toCollection(TreeSet::new));

		CopyDirectoryExtensions.copyDirectoryWithFileFilter(source.toFile(), destination.toFile(),
			null, null, List.of(excluded.toFile()), false);

		assertFalse(Files.exists(destination.resolve(excludedPath)));
		assertEquals(expected, regularFilesUnder(destination));
	}

	/**
	 * The File-based directory copies return true when the whole tree was copied, whatever the last
	 * entry of the listing is (#54)
	 *
	 * @param description
	 *            the method under test and the source tree
	 * @param directoryCopy
	 *            the call of the method under test
	 * @param entries
	 *            the entries of the source tree, a directory ending with a slash
	 * @throws Exception
	 *             if the copy fails
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("directoryCopiesOfTrees")
	public void copy_returnsTrue_whenTheWholeTreeIsCopied(final String description,
		final DirectoryCopy directoryCopy, final List<String> entries) throws Exception
	{
		final Path source = newTree("src", entries);
		final Path destination = tempDir.resolve("dst");

		final boolean copied = directoryCopy.copy(source.toFile(), destination.toFile());

		assertTrue(copied);
		assertEquals(entriesUnder(source), entriesUnder(destination));
	}

	/**
	 * The File-based directory copies do not claim a copy when the destination is a regular file
	 * (#54)
	 *
	 * @param description
	 *            the method under test
	 * @param directoryCopy
	 *            the call of the method under test
	 * @throws Exception
	 *             if the copy fails
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("directoryCopies")
	public void copy_returnsFalse_whenTheDestinationIsARegularFileAndTheSourceIsEmpty(
		final String description, final DirectoryCopy directoryCopy) throws Exception
	{
		final Path source = newTree("src", List.of());
		final Path destination = Files.write(tempDir.resolve("dst"), new byte[] { 1 });

		assertFalse(directoryCopy.copy(source.toFile(), destination.toFile()));
	}

	/**
	 * The File-based directory copies create the missing parents of the destination, as
	 * {@link CopyDirectoryExtensions#copyDirectory(Path, Path)} does (#55)
	 *
	 * @param description
	 *            the method under test
	 * @param directoryCopy
	 *            the call of the method under test
	 * @throws Exception
	 *             if the copy fails
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("directoryCopies")
	public void copy_createsTheMissingParentsOfTheDestination(final String description,
		final DirectoryCopy directoryCopy) throws Exception
	{
		final Path source = newTree("src", List.of("a.txt", "sub/b.txt", "empty/"));
		final Path destination = tempDir.resolve("out/nested");

		assertTrue(directoryCopy.copy(source.toFile(), destination.toFile()));

		assertEquals(entriesUnder(source), entriesUnder(destination));
	}
}
