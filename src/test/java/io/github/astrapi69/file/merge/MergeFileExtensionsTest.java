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
package io.github.astrapi69.file.merge;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.github.astrapi69.file.delete.DeleteFileExtensions;
import io.github.astrapi69.file.merge.strategy.MergeFileStrategy;
import io.github.astrapi69.file.read.ReadFileExtensions;
import io.github.astrapi69.file.write.StoreFileExtensions;

/**
 * The unit test class for the class {@link MergeFileExtensions}
 */
class MergeFileExtensionsTest
{

	@Test
	@DisplayName("Test mergeAndGet with UNIQUE_LINES strategy (two files)")
	void testMergeAndGet_UniqueLines() throws IOException
	{
		// Arrange
		File file1 = Files.createTempFile("f1", ".txt").toFile();
		File file2 = Files.createTempFile("f2", ".txt").toFile();
		File result = Files.createTempFile("result", ".txt").toFile();

		StoreFileExtensions.toFile(file1, "apple\nbanana\napple");
		StoreFileExtensions.toFile(file2, "banana\ncherry");

		// Act
		File returned = MergeFileExtensions.mergeAndGet(file1, file2, result,
			MergeFileStrategy.UNIQUE_LINES);

		// Assert
		assertEquals(result, returned, "Returned file should be the target file");
		assertTrue(returned.exists(), "Result file should exist");

		List<String> lines = ReadFileExtensions.readLinesInList(result);
		assertEquals(3, lines.size(), "Should have exactly 3 unique lines");
		assertEquals("apple", lines.get(0));
		assertEquals("banana", lines.get(1));
		assertEquals("cherry", lines.get(2));

		// Cleanup
		cleanupFiles(file1, file2, result);
	}

	@Test
	@DisplayName("Test mergeAndGet with APPEND strategy")
	void testMergeAndGet_Append() throws IOException
	{
		// Arrange
		File file1 = Files.createTempFile("append1", ".txt").toFile();
		File file2 = Files.createTempFile("append2", ".txt").toFile();
		File result = Files.createTempFile("appendResult", ".txt").toFile();

		StoreFileExtensions.toFile(file1, "Line A\nLine B");
		StoreFileExtensions.toFile(file2, "Line C");

		// Act
		MergeFileExtensions.mergeAndGet(file1, file2, result, MergeFileStrategy.APPEND);

		// Assert
		List<String> lines = ReadFileExtensions.readLinesInList(result);
		assertEquals(3, lines.size());
		assertEquals("Line A", lines.get(0));
		assertEquals("Line B", lines.get(1));
		assertEquals("Line C", lines.get(2));

		// Cleanup
		cleanupFiles(file1, file2, result);
	}

	@Test
	@DisplayName("Test mergeAndGet with SORTED strategy")
	void testMergeAndGet_Sorted() throws IOException
	{
		// Arrange
		File file1 = Files.createTempFile("sort1", ".txt").toFile();
		File file2 = Files.createTempFile("sort2", ".txt").toFile();
		File result = Files.createTempFile("sortResult", ".txt").toFile();

		StoreFileExtensions.toFile(file1, "zebra\napple");
		StoreFileExtensions.toFile(file2, "banana\napple"); // Duplicate 'apple'

		// Act
		MergeFileExtensions.mergeAndGet(file1, file2, result, MergeFileStrategy.SORTED);

		// Assert
		List<String> lines = ReadFileExtensions.readLinesInList(result);
		assertEquals(4, lines.size(), "SORTED keeps duplicates");
		assertEquals("apple", lines.get(0));
		assertEquals("apple", lines.get(1));
		assertEquals("banana", lines.get(2));
		assertEquals("zebra", lines.get(3));

		// Cleanup
		cleanupFiles(file1, file2, result);
	}

	@Test
	@DisplayName("Test mergeAndGet with SORTED_UNIQUE strategy")
	void testMergeAndGet_SortedUnique() throws IOException
	{
		// Arrange
		File file1 = Files.createTempFile("sortU1", ".txt").toFile();
		File file2 = Files.createTempFile("sortU2", ".txt").toFile();
		File result = Files.createTempFile("sortUResult", ".txt").toFile();

		StoreFileExtensions.toFile(file1, "zebra\napple");
		StoreFileExtensions.toFile(file2, "banana\napple");

		// Act
		MergeFileExtensions.mergeAndGet(file1, file2, result, MergeFileStrategy.SORTED_UNIQUE);

		// Assert
		List<String> lines = ReadFileExtensions.readLinesInList(result);
		assertEquals(3, lines.size(), "SORTED_UNIQUE removes duplicates");
		assertEquals("apple", lines.get(0));
		assertEquals("banana", lines.get(1));
		assertEquals("zebra", lines.get(2));

		// Cleanup
		cleanupFiles(file1, file2, result);
	}

	@Test
	@DisplayName("Test mergeAndGet with List of Files")
	void testMergeAndGet_MultipleFiles() throws IOException
	{
		// Arrange
		File f1 = createTempFileWithContent("m1.txt", "1\n2");
		File f2 = createTempFileWithContent("m2.txt", "2\n3");
		File f3 = createTempFileWithContent("m3.txt", "3\n4");
		File result = Files.createTempFile("multiResult", ".txt").toFile();

		List<File> files = Arrays.asList(f1, f2, f3);

		// Act
		File returned = MergeFileExtensions.mergeAndGet(files, result,
			MergeFileStrategy.UNIQUE_LINES);

		// Assert
		assertEquals(result, returned);
		List<String> lines = ReadFileExtensions.readLinesInList(result);
		assertEquals(4, lines.size());
		assertTrue(lines.containsAll(Arrays.asList("1", "2", "3", "4")));

		// Cleanup
		cleanupFiles(f1, f2, f3, result);
	}

	@Test
	@DisplayName("Test mergeAndGet with NIO Path API")
	void testMergeAndGet_PathApi() throws IOException
	{
		// Arrange
		Path p1 = Files.createTempFile("path1", ".txt");
		Path p2 = Files.createTempFile("path2", ".txt");
		Path resultPath = Files.createTempFile("pathResult", ".txt");

		StoreFileExtensions.toFile(p1.toFile(), "alpha");
		StoreFileExtensions.toFile(p2.toFile(), "beta");

		List<Path> paths = Arrays.asList(p1, p2);

		// Act
		Path returnedPath = MergeFileExtensions.mergeAndGet(paths, resultPath,
			MergeFileStrategy.APPEND);

		// Assert
		assertEquals(resultPath, returnedPath);
		assertTrue(Files.exists(returnedPath));
		List<String> lines = ReadFileExtensions.readLinesInList(returnedPath.toFile());
		assertEquals(2, lines.size());

		// Cleanup
		cleanupFiles(p1.toFile(), p2.toFile(), resultPath.toFile());
	}

	@Test
	@DisplayName("Test legacy void merge method for backward compatibility")
	void testLegacyVoidMergeMethod() throws IOException
	{
		// Arrange
		File file1 = createTempFileWithContent("legacy1.txt", "X");
		File file2 = createTempFileWithContent("legacy2.txt", "Y");
		File result = Files.createTempFile("legacyResult", ".txt").toFile();

		// Act (Calling the void method)
		MergeFileExtensions.merge(file1, file2, result, MergeFileStrategy.APPEND);

		// Assert
		assertTrue(result.exists());
		List<String> lines = ReadFileExtensions.readLinesInList(result);
		assertEquals(2, lines.size());

		// Cleanup
		cleanupFiles(file1, file2, result);
	}

	@Test
	@DisplayName("Test mergeAndGet throws IllegalArgumentException for empty file list")
	void testMergeAndGet_EmptyListThrowsException() throws IOException
	{
		File result = Files.createTempFile("emptyResult", ".txt").toFile();

		assertThrows(IllegalArgumentException.class, () -> {
			MergeFileExtensions.mergeAndGet(List.of(), result, MergeFileStrategy.APPEND);
		});

		// Cleanup
		DeleteFileExtensions.deleteFile(result);
	}

	@Test
	@DisplayName("Test mergeAndGet throws NullPointerException for null arguments")
	void testMergeAndGet_NullArgumentsThrowException()
	{
		File dummy = new File("dummy.txt");

		assertThrows(NullPointerException.class, () -> {
			MergeFileExtensions.mergeAndGet((List<File>)null, dummy, MergeFileStrategy.APPEND);
		});

		assertThrows(NullPointerException.class, () -> {
			MergeFileExtensions.mergeAndGet(List.of(dummy), null, MergeFileStrategy.APPEND);
		});

		assertThrows(NullPointerException.class, () -> {
			MergeFileExtensions.mergeAndGet(List.of(dummy), dummy, null);
		});
	}

	private File createTempFileWithContent(String prefix, String content) throws IOException
	{
		File file = Files.createTempFile(prefix, ".txt").toFile();
		StoreFileExtensions.toFile(file, content);
		return file;
	}

	private void cleanupFiles(File... files) throws IOException
	{
		for (File file : files)
		{
			if (file != null && file.exists())
			{
				DeleteFileExtensions.deleteFile(file);
			}
		}
	}
}
