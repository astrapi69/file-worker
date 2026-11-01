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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
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
}
