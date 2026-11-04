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
package io.github.astrapi69.file.delete.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import io.github.astrapi69.file.exception.FileDoesNotExistException;
import io.github.astrapi69.file.exception.FileIsNotADirectoryException;
import io.github.astrapi69.file.exception.FileIsSecurityRestrictedException;

/**
 * Tests the default methods of {@link Erasable} on a tree built in a temporary directory. Every
 * deletion targets a file under {@code source}; {@code outside/sibling.txt} has to survive each of
 * them.
 *
 * <pre>
 * source/tmp-a.txt
 * source/b.log
 * source/keep.txt
 * source/nested/tmp-c.txt
 * source/nested/d.log
 * outside/sibling.txt
 * </pre>
 */
public class ErasableTest
{

	private final Erasable erasable = new Erasable()
	{
	};

	@TempDir
	Path tempDir;

	private Path source;

	private Path nested;

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
		Files.writeString(source.resolve("tmp-a.txt"), "a");
		Files.writeString(source.resolve("b.log"), "b");
		Files.writeString(source.resolve("keep.txt"), "keep");
		nested = Files.createDirectory(source.resolve("nested"));
		Files.writeString(nested.resolve("tmp-c.txt"), "c");
		Files.writeString(nested.resolve("d.log"), "d");
		Files.writeString(Files.createDirectory(tempDir.resolve("outside")).resolve("sibling.txt"),
			"sibling");
	}

	private void assertOutsideUntouched() throws IOException
	{
		assertContent("sibling", tempDir.resolve("outside").resolve("sibling.txt"));
	}

	@Test
	void checkFile_returnsNull_forAnAccessibleDirectory()
	{
		assertNull(erasable.checkFile(source.toFile()));
	}

	@Test
	void checkFile_returnsTheReason_whenTheFileIsNoUsableDirectory()
	{
		assertInstanceOf(FileDoesNotExistException.class,
			erasable.checkFile(source.resolve("missing").toFile()));
		assertInstanceOf(FileIsNotADirectoryException.class,
			erasable.checkFile(source.resolve("keep.txt").toFile()));
	}

	@Test
	void deleteCollection_deletesEveryGivenFile() throws IOException
	{
		erasable.delete(List.of(source.resolve("tmp-a.txt").toFile(), nested.toFile()));

		assertDeleted(source.resolve("tmp-a.txt"));
		assertDeleted(nested);
		assertContent("keep", source.resolve("keep.txt"));
		assertOutsideUntouched();
	}

	@Test
	void deleteFile_deletesARegularFile_andReturnsTrue() throws IOException
	{
		assertTrue(erasable.delete(source.resolve("keep.txt").toFile()));

		assertDeleted(source.resolve("keep.txt"));
		assertContent("a", source.resolve("tmp-a.txt"));
		assertOutsideUntouched();
	}

	@Test
	void deleteFile_returnsFalse_andDeletesNothing_whenTheFileDoesNotExist() throws IOException
	{
		assertFalse(erasable.delete(source.resolve("missing.txt").toFile()));

		assertContent("keep", source.resolve("keep.txt"));
		assertContent("a", source.resolve("tmp-a.txt"));
		assertContent("c", nested.resolve("tmp-c.txt"));
		assertOutsideUntouched();
	}

	@Test
	void deleteFile_deletesTheDirectoryWithItsContent_andReturnsTrue() throws IOException
	{
		boolean reported = erasable.delete(nested.toFile());

		assertDeleted(nested);
		assertContent("keep", source.resolve("keep.txt"));
		assertOutsideUntouched();
		assertTrue(reported, "the deleted directory is reported as deleted");
	}

	@Test
	void deleteAllFilesByPath_deletesTheWholeTree() throws IOException
	{
		erasable.deleteAllFiles(source);

		assertDeleted(source);
		assertOutsideUntouched();
	}

	@Test
	void deleteAllFilesByFile_deletesTheWholeTree() throws IOException
	{
		erasable.deleteAllFiles(source.toFile());

		assertDeleted(source);
		assertOutsideUntouched();
	}

	@Test
	void deleteAllFilesWithSuffix_deletesTheFilesWithTheSuffixInEveryLevel() throws IOException
	{
		erasable.deleteAllFilesWithSuffix(source.toFile(), ".log");

		assertDeleted(source.resolve("b.log"));
		assertDeleted(nested.resolve("d.log"));
		assertContent("a", source.resolve("tmp-a.txt"));
		assertContent("c", nested.resolve("tmp-c.txt"));
		assertOutsideUntouched();
	}

	@Test
	void deleteFile_deletesOnlyTheGivenFile() throws IOException
	{
		erasable.deleteFile(source.resolve("b.log").toFile());

		assertDeleted(source.resolve("b.log"));
		assertContent("a", source.resolve("tmp-a.txt"));
		assertContent("d", nested.resolve("d.log"));
		assertOutsideUntouched();
	}

	@Test
	void deleteFiles_emptiesTheDirectory_andKeepsIt() throws IOException
	{
		erasable.deleteFiles(source.toFile());

		assertTrue(Files.isDirectory(source), "the directory itself stays");
		assertEquals(0, source.toFile().list().length, "the directory is empty");
		assertOutsideUntouched();
	}

	@Test
	void deleteFilesWithFileFilter_deletesTheAcceptedFilesOfTheDirectory()
		throws IOException, FileIsNotADirectoryException, FileIsSecurityRestrictedException
	{
		erasable.deleteFilesWithFileFilter(source.toFile(),
			file -> file.getName().endsWith(".txt"));

		assertDeleted(source.resolve("tmp-a.txt"));
		assertDeleted(source.resolve("keep.txt"));
		assertContent("b", source.resolve("b.log"));
		assertContent("c", nested.resolve("tmp-c.txt"));
		assertOutsideUntouched();
	}

	@Test
	void deleteAllFilesWithPrefix_deletesTheFilesWithThePrefixInEveryLevel()
		throws IOException, FileIsNotADirectoryException, FileIsSecurityRestrictedException
	{
		erasable.deleteAllFilesWithPrefix(source.toFile(), "tmp-");

		assertDeleted(source.resolve("tmp-a.txt"));
		assertDeleted(nested.resolve("tmp-c.txt"));
		assertContent("keep", source.resolve("keep.txt"));
		assertContent("d", nested.resolve("d.log"));
		assertOutsideUntouched();
	}

	@Test
	void deleteFilesWithFilenameFilter_deletesTheAcceptedFilesOfTheDirectory()
		throws IOException, FileIsNotADirectoryException, FileIsSecurityRestrictedException
	{
		erasable.deleteFilesWithFilenameFilter(source.toFile(),
			(directory, name) -> name.endsWith(".log"));

		assertDeleted(source.resolve("b.log"));
		assertContent("d", nested.resolve("d.log"));
		assertContent("a", source.resolve("tmp-a.txt"));
		assertOutsideUntouched();
	}
}
