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

	// =========================================================================
	// 1. STRATEGY: UNIQUE_LINES
	// =========================================================================

	@Test
	@DisplayName("Test mergeAndGet with UNIQUE_LINES strategy (two files)")
	void testMergeAndGet_UniqueLines() throws IOException
	{
		File file1 = Files.createTempFile("f1", ".txt").toFile();
		File file2 = Files.createTempFile("f2", ".txt").toFile();
		File result = Files.createTempFile("result", ".txt").toFile();

		StoreFileExtensions.toFile(file1, "apple\nbanana\napple");
		StoreFileExtensions.toFile(file2, "banana\ncherry");

		File returned = MergeFileExtensions.mergeAndGet(file1, file2, result,
			MergeFileStrategy.UNIQUE_LINES);

		assertEquals(result, returned);
		List<String> lines = ReadFileExtensions.readLinesInList(result);
		assertEquals(3, lines.size());
		assertEquals("apple", lines.get(0));
		assertEquals("banana", lines.get(1));
		assertEquals("cherry", lines.get(2));

		cleanupFiles(file1, file2, result);
	}

	// =========================================================================
	// 2. STRATEGY: APPEND
	// =========================================================================

	@Test
	@DisplayName("Test mergeAndGet with APPEND strategy")
	void testMergeAndGet_Append() throws IOException
	{
		File file1 = createTempFileWithContent("append1.txt", "Line A\nLine B");
		File file2 = createTempFileWithContent("append2.txt", "Line C");
		File result = Files.createTempFile("appendResult", ".txt").toFile();

		MergeFileExtensions.mergeAndGet(file1, file2, result, MergeFileStrategy.APPEND);

		List<String> lines = ReadFileExtensions.readLinesInList(result);
		assertEquals(3, lines.size());
		assertEquals("Line A", lines.get(0));
		assertEquals("Line B", lines.get(1));
		assertEquals("Line C", lines.get(2));

		cleanupFiles(file1, file2, result);
	}

	// =========================================================================
	// 3. STRATEGY: SORTED & SORTED_UNIQUE
	// =========================================================================

	@Test
	@DisplayName("Test mergeAndGet with SORTED strategy")
	void testMergeAndGet_Sorted() throws IOException
	{
		File file1 = createTempFileWithContent("sort1.txt", "zebra\napple");
		File file2 = createTempFileWithContent("sort2.txt", "banana\napple");
		File result = Files.createTempFile("sortResult", ".txt").toFile();

		MergeFileExtensions.mergeAndGet(file1, file2, result, MergeFileStrategy.SORTED);

		List<String> lines = ReadFileExtensions.readLinesInList(result);
		assertEquals(4, lines.size()); // SORTED keeps duplicates
		assertEquals("apple", lines.get(0));
		assertEquals("apple", lines.get(1));
		assertEquals("banana", lines.get(2));
		assertEquals("zebra", lines.get(3));

		cleanupFiles(file1, file2, result);
	}

	@Test
	@DisplayName("Test mergeAndGet with SORTED_UNIQUE strategy")
	void testMergeAndGet_SortedUnique() throws IOException
	{
		File file1 = createTempFileWithContent("sortU1.txt", "zebra\napple");
		File file2 = createTempFileWithContent("sortU2.txt", "banana\napple");
		File result = Files.createTempFile("sortUResult", ".txt").toFile();

		MergeFileExtensions.mergeAndGet(file1, file2, result, MergeFileStrategy.SORTED_UNIQUE);

		List<String> lines = ReadFileExtensions.readLinesInList(result);
		assertEquals(3, lines.size()); // SORTED_UNIQUE removes duplicates
		assertEquals("apple", lines.get(0));
		assertEquals("banana", lines.get(1));
		assertEquals("zebra", lines.get(2));

		cleanupFiles(file1, file2, result);
	}

	// =========================================================================
	// 4. MULTIPLE FILES & PATH API
	// =========================================================================

	@Test
	@DisplayName("Test mergeAndGet with List of Files")
	void testMergeAndGet_MultipleFiles() throws IOException
	{
		File f1 = createTempFileWithContent("m1.txt", "1\n2");
		File f2 = createTempFileWithContent("m2.txt", "2\n3");
		File f3 = createTempFileWithContent("m3.txt", "3\n4");
		File result = Files.createTempFile("multiResult", ".txt").toFile();

		List<File> files = Arrays.asList(f1, f2, f3);

		File returned = MergeFileExtensions.mergeAndGet(files, result,
			MergeFileStrategy.UNIQUE_LINES);

		assertEquals(result, returned);
		List<String> lines = ReadFileExtensions.readLinesInList(result);
		assertEquals(4, lines.size());
		assertTrue(lines.containsAll(Arrays.asList("1", "2", "3", "4")));

		cleanupFiles(f1, f2, f3, result);
	}

	@Test
	@DisplayName("Test mergeAndGet with NIO Path API (3-arg convenience method)")
	void testMergeAndGet_PathApi() throws IOException
	{
		Path p1 = Files.createTempFile("path1", ".txt");
		Path p2 = Files.createTempFile("path2", ".txt");
		Path resultPath = Files.createTempFile("pathResult", ".txt");

		StoreFileExtensions.toFile(p1.toFile(), "alpha");
		StoreFileExtensions.toFile(p2.toFile(), "beta");

		List<Path> paths = Arrays.asList(p1, p2);

		// Dies nutzt nun die neue 3-Parameter Convenience-Methode für Path
		Path returnedPath = MergeFileExtensions.mergeAndGet(paths, resultPath,
			MergeFileStrategy.APPEND);

		assertEquals(resultPath, returnedPath);
		assertTrue(Files.exists(returnedPath));
		List<String> lines = ReadFileExtensions.readLinesInList(returnedPath.toFile());
		assertEquals(2, lines.size());

		cleanupFiles(p1.toFile(), p2.toFile(), resultPath.toFile());
	}

	// =========================================================================
	// 5. BACKWARD COMPATIBILITY & EDGE CASES
	// =========================================================================

	@Test
	@DisplayName("Test legacy void merge method for backward compatibility")
	void testLegacyVoidMergeMethod() throws IOException
	{
		File file1 = createTempFileWithContent("legacy1.txt", "X");
		File file2 = createTempFileWithContent("legacy2.txt", "Y");
		File result = Files.createTempFile("legacyResult", ".txt").toFile();

		MergeFileExtensions.merge(file1, file2, result, MergeFileStrategy.APPEND);

		assertTrue(result.exists());
		List<String> lines = ReadFileExtensions.readLinesInList(result);
		assertEquals(2, lines.size());

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

		DeleteFileExtensions.deleteFile(result);
	}

	@Test
	@DisplayName("Test mergeAndGet throws NullPointerException for null arguments")
	void testMergeAndGet_NullArgumentsThrowException()
	{
		File dummy = new File("dummy.txt");

		assertThrows(NullPointerException.class, () -> MergeFileExtensions
			.mergeAndGet((List<File>)null, dummy, MergeFileStrategy.APPEND));
		assertThrows(NullPointerException.class,
			() -> MergeFileExtensions.mergeAndGet(List.of(dummy), null, MergeFileStrategy.APPEND));
		assertThrows(NullPointerException.class,
			() -> MergeFileExtensions.mergeAndGet(List.of(dummy), dummy, (MergeFileStrategy)null));
	}

	// =========================================================================
	// 6. SPECIALIZED STRATEGIES
	// =========================================================================

	@Test
	@DisplayName("Test CSV_HEADER_MERGE strategy")
	void testMergeAndGet_CsvHeaderMerge() throws IOException
	{
		File f1 = createTempFileWithContent("csv1.csv", "id,name\n1,Alice\n2,Bob");
		File f2 = createTempFileWithContent("csv2.csv", "id,name\n3,Charlie\n4,Diana");
		File result = Files.createTempFile("csvResult", ".csv").toFile();

		MergeFileExtensions.mergeAndGet(f1, f2, result, MergeFileStrategy.CSV_HEADER_MERGE);

		List<String> lines = ReadFileExtensions.readLinesInList(result);
		assertEquals(5, lines.size()); // 1 Header + 4 Datenzeilen
		assertEquals("id,name", lines.get(0));
		assertEquals("3,Charlie", lines.get(3));

		cleanupFiles(f1, f2, result);
	}

	@Test
	@DisplayName("Test MARKDOWN_SECTIONS strategy")
	void testMergeAndGet_MarkdownSections() throws IOException
	{
		File f1 = createTempFileWithContent("md1.md", "# Title 1\nContent A");
		File f2 = createTempFileWithContent("md2.md", "# Title 2\nContent B");
		File result = Files.createTempFile("mdResult", ".md").toFile();

		MergeFileExtensions.mergeAndGet(f1, f2, result, MergeFileStrategy.MARKDOWN_SECTIONS);

		List<String> lines = ReadFileExtensions.readLinesInList(result);
		assertTrue(lines.contains("---"));

		int title1Index = lines.indexOf("# Title 1");
		int separatorIndex = lines.indexOf("---");
		int title2Index = lines.indexOf("# Title 2");
		assertTrue(title1Index < separatorIndex && separatorIndex < title2Index);

		cleanupFiles(f1, f2, result);
	}

	@Test
	@DisplayName("Test BY_KEY strategy (default comma, index 0)")
	void testMergeAndGet_ByKeyDefault() throws IOException
	{
		File f1 = createTempFileWithContent("data1.csv", "user1,Alice\nuser2,Bob");
		File f2 = createTempFileWithContent("data2.csv", "user1,Alice Updated\nuser3,Charlie");
		File result = Files.createTempFile("keyResult", ".csv").toFile();

		MergeFileExtensions.mergeAndGet(f1, f2, result, MergeFileStrategy.BY_KEY);

		List<String> lines = ReadFileExtensions.readLinesInList(result);
		assertEquals(3, lines.size());
		assertTrue(lines.contains("user1,Alice Updated")); // Letzter Wert gewinnt
		assertTrue(lines.contains("user2,Bob"));
		assertTrue(lines.contains("user3,Charlie"));

		cleanupFiles(f1, f2, result);
	}

	@Test
	@DisplayName("Test advanced mergeByKey method with custom delimiter and index")
	void testMergeByKeyAdvanced() throws IOException
	{
		// Wir lassen den Header weg, da mergeByKey eine generische Funktion ist
		// und keine CSV-spezifische Header-Logik besitzt.
		File f1 = createTempFileWithContent("adv1.txt", "101;Alice;Admin");
		File f2 = createTempFileWithContent("adv2.txt", "102;Bob;User\n101;Alice;SuperAdmin");
		File result = Files.createTempFile("advResult", ".txt").toFile();

		// Schlüssel ist Index 1 (Name), Trennzeichen ist ";"
		MergeFileExtensions.mergeByKey(Arrays.asList(f1, f2), result, 1, ";");

		List<String> lines = ReadFileExtensions.readLinesInList(result);

		// Jetzt erwarten wir korrekt 2 Zeilen: eine für Alice, eine für Bob
		assertEquals(2, lines.size(), "Should contain exactly 2 unique keys (Alice and Bob)");
		assertTrue(lines.stream().anyMatch(line -> line.contains("SuperAdmin")),
			"Last value for Alice should win");
		assertTrue(lines.stream().anyMatch(line -> line.contains("Bob")), "Bob should be present");

		cleanupFiles(f1, f2, result);
	}

	// =========================================================================
	// HELPER METHODS
	// =========================================================================

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
