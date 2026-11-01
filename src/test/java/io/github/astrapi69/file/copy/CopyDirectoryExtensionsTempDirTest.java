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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.File;
import java.io.IOException;
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

import io.github.astrapi69.file.exception.FileIsNotADirectoryException;
import io.github.astrapi69.file.exception.FileIsSecurityRestrictedException;

/**
 * Tests for {@link CopyDirectoryExtensions} that build every directory they copy in a temporary
 * directory of their own
 */
public class CopyDirectoryExtensionsTempDirTest
{

	@TempDir
	Path tempDir;

	/**
	 * A copy of a source directory into a destination directory that may fail with an exception
	 */
	@FunctionalInterface
	interface DirectoryCopier
	{
		boolean copy(File source, File destination) throws Exception;
	}

	static Stream<Arguments> fileBasedCopiers()
	{
		return Stream.of(
			Arguments.of("copyDirectory(File, File)",
				(DirectoryCopier)CopyDirectoryExtensions::copyDirectory),
			Arguments.of("copyDirectoryWithFileFilter without filters",
				(DirectoryCopier)(source, destination) -> CopyDirectoryExtensions
					.copyDirectoryWithFileFilter(source, destination, null, null, null, false)),
			Arguments.of("copyDirectoryWithFilenameFilter without filters",
				(DirectoryCopier)(source, destination) -> CopyDirectoryExtensions
					.copyDirectoryWithFilenameFilter(source, destination, null, null, false)));
	}

	static Stream<Arguments> copiersThatExcludeNothing()
	{
		return Stream.of(Arguments.of("file filter with an empty exclude list",
			(DirectoryCopier)(source, destination) -> CopyDirectoryExtensions
				.copyDirectoryWithFileFilter(source, destination, null, null, List.of(), false)),
			Arguments.of("file filter with an exclude filter that matches nothing",
				(DirectoryCopier)(source, destination) -> CopyDirectoryExtensions
					.copyDirectoryWithFileFilter(source, destination, null,
						file -> file.getName().endsWith(".exe"), false)),
			Arguments.of("filename filter with an exclude filter that matches nothing",
				(DirectoryCopier)(source, destination) -> CopyDirectoryExtensions
					.copyDirectoryWithFilenameFilter(source, destination, null,
						(directory, name) -> name.endsWith(".exe"), false)));
	}

	private Path newFile(final String relativePath, final String content) throws IOException
	{
		final Path path = tempDir.resolve(relativePath).toAbsolutePath();
		Files.createDirectories(path.getParent());
		Files.write(path, content.getBytes(StandardCharsets.US_ASCII));
		return path;
	}

	private Path notCreated(final String relativePath)
	{
		return tempDir.resolve(relativePath).toAbsolutePath();
	}

	private static String read(final Path path) throws IOException
	{
		return new String(Files.readAllBytes(path), StandardCharsets.US_ASCII);
	}

	/**
	 * Test method for {@link CopyDirectoryExtensions#copyDirectory(Path, Path)}
	 */
	@Test
	public void copyDirectoryPath_copiesEveryFileOfTheTree() throws IOException
	{
		newFile("source/a.txt", "alpha");
		newFile("source/sub/b.txt", "beta");
		final Path destination = notCreated("destination");

		assertTrue(CopyDirectoryExtensions.copyDirectory(tempDir.resolve("source"), destination));

		assertEquals("alpha", read(destination.resolve("a.txt")));
		assertEquals("beta", read(destination.resolve("sub/b.txt")));
	}

	/**
	 * Test method for {@link CopyDirectoryExtensions#copyDirectory(Path, Path)}
	 */
	@Test
	public void copyDirectoryPath_throwsIllegalArgumentException_whenTheSourceIsAFile()
		throws IOException
	{
		final Path source = newFile("source.txt", "alpha");
		final Path destination = notCreated("destination");

		assertThrows(IllegalArgumentException.class,
			() -> CopyDirectoryExtensions.copyDirectory(source, destination));
	}

	/**
	 * Test method for {@link CopyDirectoryExtensions#copyDirectory(File, File)},
	 * {@link CopyDirectoryExtensions#copyDirectoryWithFileFilter(File, File, java.io.FileFilter, java.io.FileFilter, java.util.Collection, boolean)}
	 * and
	 * {@link CopyDirectoryExtensions#copyDirectoryWithFilenameFilter(File, File, java.io.FilenameFilter, java.io.FilenameFilter, boolean)}
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("fileBasedCopiers")
	public void copy_throwsFileIsNotADirectoryException_whenTheSourceIsAFile(
		final String copierName, final DirectoryCopier copier) throws IOException
	{
		final File source = newFile("source.txt", "alpha").toFile();
		final File destination = notCreated("destination").toFile();

		assertThrows(FileIsNotADirectoryException.class, () -> copier.copy(source, destination));
	}

	/**
	 * Test method for {@link CopyDirectoryExtensions#copyDirectory(File, File)},
	 * {@link CopyDirectoryExtensions#copyDirectoryWithFileFilter(File, File, java.io.FileFilter, java.io.FileFilter, java.util.Collection, boolean)}
	 * and
	 * {@link CopyDirectoryExtensions#copyDirectoryWithFilenameFilter(File, File, java.io.FilenameFilter, java.io.FilenameFilter, boolean)}
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("fileBasedCopiers")
	public void copy_copiesTheTreeIntoAnExistingDestinationDirectory(final String copierName,
		final DirectoryCopier copier) throws Exception
	{
		newFile("source/a.txt", "alpha");
		newFile("source/sub/b.txt", "beta");
		final Path destination = Files.createDirectories(tempDir.resolve("destination"));
		newFile("destination/kept.txt", "kept");

		assertTrue(copier.copy(tempDir.resolve("source").toFile(), destination.toFile()));

		assertEquals("alpha", read(destination.resolve("a.txt")));
		assertEquals("beta", read(destination.resolve("sub/b.txt")));
		assertEquals("kept", read(destination.resolve("kept.txt")));
	}

	/**
	 * Test method for {@link CopyDirectoryExtensions#copyDirectory(File, File)},
	 * {@link CopyDirectoryExtensions#copyDirectoryWithFileFilter(File, File, java.io.FileFilter, java.io.FileFilter, java.util.Collection, boolean)}
	 * and
	 * {@link CopyDirectoryExtensions#copyDirectoryWithFilenameFilter(File, File, java.io.FilenameFilter, java.io.FilenameFilter, boolean)}
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("fileBasedCopiers")
	public void copy_throwsFileIsSecurityRestrictedException_whenTheSourceCannotBeListed(
		final String copierName, final DirectoryCopier copier) throws IOException
	{
		newFile("locked/a.txt", "alpha");
		final File source = tempDir.resolve("locked").toFile();
		final File destination = notCreated("destination").toFile();
		assumeTrue(source.setReadable(false),
			"the file system does not take the read permission away");
		try
		{
			assumeTrue(source.listFiles() == null,
				"a directory without read permission can still be listed, for example by root");

			assertThrows(FileIsSecurityRestrictedException.class,
				() -> copier.copy(source, destination));
		}
		finally
		{
			assertTrue(source.setReadable(true));
		}
	}

	/**
	 * Test method for
	 * {@link CopyDirectoryExtensions#copyDirectoryWithFileFilter(File, File, java.io.FileFilter, java.io.FileFilter, java.util.Collection, boolean)}
	 * and
	 * {@link CopyDirectoryExtensions#copyDirectoryWithFilenameFilter(File, File, java.io.FilenameFilter, java.io.FilenameFilter, boolean)}
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("copiersThatExcludeNothing")
	public void copy_copiesEveryFile_whenNothingIsExcluded(final String copierName,
		final DirectoryCopier copier) throws Exception
	{
		newFile("source/a.txt", "alpha");
		newFile("source/sub/b.txt", "beta");
		final Path destination = notCreated("destination");

		assertTrue(copier.copy(tempDir.resolve("source").toFile(), destination.toFile()));

		assertEquals("alpha", read(destination.resolve("a.txt")));
		assertEquals("beta", read(destination.resolve("sub/b.txt")));
	}

	/**
	 * Test method for
	 * {@link CopyDirectoryExtensions#copyDirectoryWithFileFilter(File, File, java.io.FileFilter, java.io.FileFilter, java.util.Collection, boolean)}
	 */
	@Test
	public void copyDirectoryWithFileFilter_leavesOutTheListedFiles_whenOnlyAnExcludeListIsGiven()
		throws Exception
	{
		newFile("source/a.txt", "alpha");
		final Path excluded = newFile("source/b.txt", "beta");
		newFile("source/c.txt", "gamma");
		final Path destination = notCreated("destination");

		CopyDirectoryExtensions.copyDirectoryWithFileFilter(tempDir.resolve("source").toFile(),
			destination.toFile(), null, null, List.of(excluded.toFile()), false);

		assertEquals("alpha", read(destination.resolve("a.txt")));
		assertEquals("gamma", read(destination.resolve("c.txt")));
		assertFalse(Files.exists(destination.resolve("b.txt")));
	}

	/**
	 * Test method for
	 * {@link CopyDirectoryExtensions#copyDirectoryWithFilenameFilter(File, File, java.io.FilenameFilter, java.io.FilenameFilter, boolean)}
	 */
	@Test
	public void copyDirectoryWithFilenameFilter_leavesOutTheExcludedNamesInEveryDirectory_whenNoIncludeFilterIsGiven()
		throws Exception
	{
		newFile("source/a.txt", "alpha");
		newFile("source/b.exe", "beta");
		newFile("source/sub/c.txt", "gamma");
		newFile("source/sub/d.exe", "delta");
		final Path destination = notCreated("destination");

		CopyDirectoryExtensions.copyDirectoryWithFilenameFilter(tempDir.resolve("source").toFile(),
			destination.toFile(), null, (directory, name) -> name.endsWith(".exe"), false);

		assertEquals("alpha", read(destination.resolve("a.txt")));
		assertEquals("gamma", read(destination.resolve("sub/c.txt")));
		assertFalse(Files.exists(destination.resolve("b.exe")));
		assertFalse(Files.exists(destination.resolve("sub/d.exe")));
	}

}
