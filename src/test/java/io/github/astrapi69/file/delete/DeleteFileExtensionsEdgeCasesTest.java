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
package io.github.astrapi69.file.delete;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import io.github.astrapi69.file.exception.DirectoryHasNoContentException;
import io.github.astrapi69.file.exception.FileIsNotADirectoryException;
import io.github.astrapi69.file.exception.FileIsSecurityRestrictedException;

/**
 * Tests the edge cases of {@link DeleteFileExtensions} on a tree built in a temporary directory.
 * Every deletion targets a file under {@code source}; {@code outside/sibling.txt} has to survive
 * each of them.
 *
 * <pre>
 * source/a.txt
 * source/b.log
 * source/keep/c.txt
 * source/nested/d.txt
 * source/nested/e.log
 * outside/sibling.txt
 * </pre>
 */
public class DeleteFileExtensionsEdgeCasesTest
{

	@TempDir
	Path tempDir;

	private Path source;

	/**
	 * A deletion of the files under the given directory
	 */
	@FunctionalInterface
	interface Deletion
	{
		void deleteIn(File directory) throws Exception;
	}

	static Stream<Arguments> deletionsWithoutIncludeFilter()
	{
		return Stream.of(
			Arguments.of("FileFilter",
				(Deletion)directory -> DeleteFileExtensions.deleteFilesWithFileFilter(directory,
					(FileFilter)null)),
			Arguments.of("FilenameFilter", (Deletion)directory -> DeleteFileExtensions
				.deleteFilesWithFilenameFilter(directory, (FilenameFilter)null)));
	}

	static Stream<Arguments> deletionsOfTxtFiles()
	{
		FileFilter txtOrDirectory = file -> file.isDirectory() || file.getName().endsWith(".txt");
		FilenameFilter txtOrDirectoryName = (directory,
			name) -> new File(directory, name).isDirectory() || name.endsWith(".txt");
		return Stream.of(
			Arguments.of("FileFilter without exclude filter",
				(Deletion)directory -> DeleteFileExtensions.deleteFilesWithFileFilter(directory,
					txtOrDirectory)),
			Arguments.of("FileFilter with an exclude filter that matches nothing",
				(Deletion)directory -> DeleteFileExtensions.deleteFilesWithFileFilter(directory,
					txtOrDirectory, file -> file.getName().equals("matches-nothing"))),
			Arguments.of("FilenameFilter without exclude filter",
				(Deletion)directory -> DeleteFileExtensions.deleteFilesWithFilenameFilter(directory,
					txtOrDirectoryName)),
			Arguments.of("FilenameFilter with an exclude filter that matches nothing",
				(Deletion)directory -> DeleteFileExtensions.deleteFilesWithFilenameFilter(directory,
					txtOrDirectoryName, (parent, name) -> name.equals("matches-nothing"))));
	}

	static Stream<Arguments> deletionsOfTxtFilesExceptTheKeepDirectory()
	{
		FileFilter txtOrDirectory = file -> file.isDirectory() || file.getName().endsWith(".txt");
		FilenameFilter txtOrDirectoryName = (directory,
			name) -> new File(directory, name).isDirectory() || name.endsWith(".txt");
		return Stream.of(
			Arguments.of("FileFilter",
				(Deletion)directory -> DeleteFileExtensions.deleteFilesWithFileFilter(directory,
					txtOrDirectory, file -> file.getName().equals("keep"))),
			Arguments.of("FilenameFilter",
				(Deletion)directory -> DeleteFileExtensions.deleteFilesWithFilenameFilter(directory,
					txtOrDirectoryName, (parent, name) -> name.equals("keep"))));
	}

	static Stream<Arguments> revokedPermissions()
	{
		return Stream.of(
			Arguments.of("unreadable",
				(Function<File, Boolean>)directory -> directory.setReadable(false),
				(Predicate<Path>)Files::isReadable),
			Arguments.of("unwritable",
				(Function<File, Boolean>)directory -> directory.setWritable(false),
				(Predicate<Path>)Files::isWritable));
	}

	private static void restorePermissions(Path directory)
	{
		File file = directory.toFile();
		file.setReadable(true);
		file.setWritable(true);
	}

	private static void assertContent(String expected, Path file) throws IOException
	{
		assertEquals(expected, Files.readString(file), file + " keeps its content");
	}

	private static void assertDeleted(Path file)
	{
		assertFalse(Files.exists(file), file + " is deleted");
	}

	@BeforeEach
	void createTree() throws IOException
	{
		source = Files.createDirectory(tempDir.resolve("source"));
		Files.writeString(source.resolve("a.txt"), "a");
		Files.writeString(source.resolve("b.log"), "b");
		Files.writeString(Files.createDirectory(source.resolve("keep")).resolve("c.txt"), "c");
		Path nested = Files.createDirectory(source.resolve("nested"));
		Files.writeString(nested.resolve("d.txt"), "d");
		Files.writeString(nested.resolve("e.log"), "e");
		Files.writeString(Files.createDirectory(tempDir.resolve("outside")).resolve("sibling.txt"),
			"sibling");
	}

	private void assertOutsideUntouched() throws IOException
	{
		assertContent("sibling", tempDir.resolve("outside").resolve("sibling.txt"));
	}

	private void assertSourceUntouched() throws IOException
	{
		assertContent("a", source.resolve("a.txt"));
		assertContent("b", source.resolve("b.log"));
		assertContent("c", source.resolve("keep").resolve("c.txt"));
		assertContent("d", source.resolve("nested").resolve("d.txt"));
		assertContent("e", source.resolve("nested").resolve("e.log"));
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("revokedPermissions")
	void checkFile_returnsDirectoryHasNoContentException_whenDirectoryIsNotAccessible(
		String displayName, Function<File, Boolean> revoke, Predicate<Path> stillPermitted)
	{
		Path directory = source.resolve("nested");
		try
		{
			assumeTrue(revoke.apply(directory.toFile()), "the file system takes the permission");
			assumeFalse(stillPermitted.test(directory), "the test runs without override rights");

			Exception exception = DeleteFileExtensions.checkFile(directory.toFile());

			assertInstanceOf(DirectoryHasNoContentException.class, exception);
			assertTrue(exception.getMessage().contains(directory.toString()),
				exception.getMessage());
		}
		finally
		{
			restorePermissions(directory);
		}
	}

	@ParameterizedTest
	@NullAndEmptySource
	void deleteCollection_deletesNothing_whenThereAreNoFiles(List<File> files) throws IOException
	{
		DeleteFileExtensions.delete(files);

		assertSourceUntouched();
		assertOutsideUntouched();
	}

	@Test
	void deletePath_deletesARegularFile_andReturnsTrue() throws IOException
	{
		Path regularFile = source.resolve("a.txt");

		assertTrue(DeleteFileExtensions.delete(regularFile));

		assertDeleted(regularFile);
		assertContent("b", source.resolve("b.log"));
		assertOutsideUntouched();
	}

	@ParameterizedTest
	@ValueSource(strings = { "source", "source/nested" })
	void deletePath_deletesTheDirectoryWithItsWholeContent(String directory) throws IOException
	{
		Path toDelete = tempDir.resolve(directory);

		DeleteFileExtensions.delete(toDelete);

		assertDeleted(toDelete);
		assertOutsideUntouched();
	}

	@Test
	void deletePath_returnsFalse_whenThePathDoesNotExist() throws IOException
	{
		assertFalse(DeleteFileExtensions.delete(source.resolve("missing.txt")));

		assertSourceUntouched();
	}

	@Test
	void deleteAllFiles_doesNothing_whenTheFileDoesNotExist() throws IOException
	{
		File missing = source.resolve("missing").toFile();

		assertDoesNotThrow(() -> DeleteFileExtensions.deleteAllFiles(missing));

		assertSourceUntouched();
		assertOutsideUntouched();
	}

	@Test
	void deleteAllFiles_deletesARegularFile() throws IOException
	{
		Path regularFile = source.resolve("a.txt");

		DeleteFileExtensions.deleteAllFiles(regularFile.toFile());

		assertDeleted(regularFile);
		assertContent("b", source.resolve("b.log"));
		assertOutsideUntouched();
	}

	@Test
	void deleteAllFiles_throwsIOException_whenTheDirectoryCannotBeDeleted() throws IOException
	{
		Path nested = source.resolve("nested");
		try
		{
			assumeTrue(source.toFile().setWritable(false), "the file system takes the permission");
			assumeFalse(Files.isWritable(source), "the test runs without override rights");

			IOException exception = assertThrows(IOException.class,
				() -> DeleteFileExtensions.deleteAllFiles(nested.toFile()));

			assertTrue(exception.getMessage().contains(nested.toString()), exception.getMessage());
			assertTrue(Files.isDirectory(nested), "the directory is still there");
		}
		finally
		{
			restorePermissions(source);
		}
		assertContent("a", source.resolve("a.txt"));
	}

	@Test
	void deleteFiles_keepsARegularFile() throws IOException
	{
		DeleteFileExtensions.deleteFiles(source.resolve("a.txt").toFile());

		assertSourceUntouched();
	}

	@ParameterizedTest
	@ValueSource(strings = { "a.txt", "missing" })
	void deleteFilesWithFileFilter_throwsFileIsNotADirectoryException_whenSourceIsNoDirectory(
		String notADirectory) throws IOException
	{
		File file = source.resolve(notADirectory).toFile();

		FileIsNotADirectoryException exception = assertThrows(FileIsNotADirectoryException.class,
			() -> DeleteFileExtensions.deleteFilesWithFileFilter(file, null, null));

		assertTrue(exception.getMessage().contains(file.getAbsolutePath()), exception.getMessage());
		assertSourceUntouched();
	}

	@ParameterizedTest
	@ValueSource(strings = { "a.txt", "missing" })
	void deleteFilesWithFilenameFilter_throwsFileIsNotADirectoryException_whenSourceIsNoDirectory(
		String notADirectory) throws IOException
	{
		File file = source.resolve(notADirectory).toFile();

		FileIsNotADirectoryException exception = assertThrows(FileIsNotADirectoryException.class,
			() -> DeleteFileExtensions.deleteFilesWithFilenameFilter(file, null, null));

		assertTrue(exception.getMessage().contains(file.getAbsolutePath()), exception.getMessage());
		assertSourceUntouched();
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("deletionsWithoutIncludeFilter")
	void deleteFilesWithFilter_deletesEveryFileOfEveryLevel_whenNoIncludeFilterIsGiven(
		String displayName, Deletion deletion) throws Exception
	{
		deletion.deleteIn(source.toFile());

		assertDeleted(source.resolve("a.txt"));
		assertDeleted(source.resolve("b.log"));
		assertDeleted(source.resolve("keep").resolve("c.txt"));
		assertDeleted(source.resolve("nested").resolve("d.txt"));
		assertDeleted(source.resolve("nested").resolve("e.log"));
		assertOutsideUntouched();
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("deletionsOfTxtFiles")
	void deleteFilesWithFilter_deletesTheIncludedFilesOfEveryLevel(String displayName,
		Deletion deletion) throws Exception
	{
		deletion.deleteIn(source.toFile());

		assertDeleted(source.resolve("a.txt"));
		assertDeleted(source.resolve("keep").resolve("c.txt"));
		assertDeleted(source.resolve("nested").resolve("d.txt"));
		assertContent("b", source.resolve("b.log"));
		assertContent("e", source.resolve("nested").resolve("e.log"));
		assertOutsideUntouched();
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("deletionsOfTxtFilesExceptTheKeepDirectory")
	void deleteFilesWithFilter_keepsTheExcludedDirectory(String displayName, Deletion deletion)
		throws Exception
	{
		deletion.deleteIn(source.toFile());

		assertDeleted(source.resolve("a.txt"));
		assertDeleted(source.resolve("nested").resolve("d.txt"));
		assertContent("c", source.resolve("keep").resolve("c.txt"));
		assertContent("b", source.resolve("b.log"));
		assertContent("e", source.resolve("nested").resolve("e.log"));
		assertOutsideUntouched();
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("deletionsWithoutIncludeFilter")
	void deleteFilesWithFilter_throwsFileIsSecurityRestrictedException_whenSourceIsUnreadable(
		String displayName, Deletion deletion) throws IOException
	{
		try
		{
			assumeTrue(source.toFile().setReadable(false), "the file system takes the permission");
			assumeFalse(Files.isReadable(source), "the test runs without override rights");

			FileIsSecurityRestrictedException exception = assertThrows(
				FileIsSecurityRestrictedException.class, () -> deletion.deleteIn(source.toFile()));

			assertTrue(exception.getMessage().contains(source.toString()), exception.getMessage());
		}
		finally
		{
			restorePermissions(source);
		}
		assertSourceUntouched();
	}
}
