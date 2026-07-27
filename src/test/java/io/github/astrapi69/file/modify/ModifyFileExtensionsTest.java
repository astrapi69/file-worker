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
package io.github.astrapi69.file.modify;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;

import io.github.astrapi69.collection.array.ArrayFactory;
import io.github.astrapi69.file.copy.CopyFileExtensions;
import io.github.astrapi69.file.delete.DeleteFileExtensions;
import io.github.astrapi69.file.exception.FileIsADirectoryException;
import io.github.astrapi69.file.modify.api.FileChangeable;
import io.github.astrapi69.file.read.ReadFileExtensions;
import io.github.astrapi69.file.search.PathFinder;
import io.github.astrapi69.file.write.LineAppender;

/**
 * The unit test class for the class {@link ModifyFileExtensions}
 */
public class ModifyFileExtensionsTest
{

	// =========================================================================
	// 1. TESTS FOR CONCATENATE (List based)
	// =========================================================================

	/**
	 * Test method for {@link ModifyFileExtensions#concatenateAll(List, File)}
	 */
	@Test
	@DisplayName("Test concatenateAll with example files")
	void concatenateEmptyFiles() throws IOException
	{
		List<File> textFiles = new ArrayList<>();
		File first = new File(PathFinder.getSrcTestResourcesDir(), "test1.txt");
		File second = new File(PathFinder.getSrcTestResourcesDir(), "test2.txt");
		textFiles.add(first);
		textFiles.add(second);

		String[] linesToAppend = ArrayFactory.newArray("Line 1", "Line 2", "Line 3");
		LineAppender.appendLines(first, linesToAppend);

		linesToAppend = ArrayFactory.newArray("Line 4", "Line 5", "Line 6");
		LineAppender.appendLines(second, linesToAppend);

		File resultTextFile = new File(PathFinder.getSrcTestResourcesDir(), "result.txt");

		ModifyFileExtensions.concatenateAll(textFiles, resultTextFile);

		String expected = "Line 1\n" + "Line 2\n" + "Line 3\n" + "Line 4\n" + "Line 5\n"
			+ "Line 6\n";
		String actual = ReadFileExtensions.fromFile(resultTextFile);
		assertEquals(expected, actual);

		DeleteFileExtensions.deleteFile(resultTextFile);
		DeleteFileExtensions.delete(textFiles);
	}

	/**
	 * Test method for {@link ModifyFileExtensions#concatenateAllAndGet(List, File)}
	 */
	@Test
	@DisplayName("Test concatenateAllAndGet returns the correct result file")
	void testConcatenateAllAndGet() throws IOException
	{
		List<File> textFiles = new ArrayList<>();
		File first = new File(PathFinder.getSrcTestResourcesDir(), "test-get1.txt");
		LineAppender.appendLines(first, ArrayFactory.newArray("Content A"));
		textFiles.add(first);

		File resultTextFile = new File(PathFinder.getSrcTestResourcesDir(), "result-get.txt");

		File returnedFile = ModifyFileExtensions.concatenateAllAndGet(textFiles, resultTextFile);

		assertEquals(resultTextFile, returnedFile);
		assertTrue(returnedFile.exists());

		DeleteFileExtensions.deleteFile(resultTextFile);
		DeleteFileExtensions.delete(textFiles);
	}

	/**
	 * Test method for {@link ModifyFileExtensions#concatenateAll(List)} (Convenience method)
	 */
	@Test
	@DisplayName("Test concatenateAll(List) creates and returns a temporary file")
	void testConcatenateAllConvenienceMethod() throws IOException
	{
		List<File> textFiles = new ArrayList<>();
		File first = new File(PathFinder.getSrcTestResourcesDir(), "test-temp1.txt");
		LineAppender.appendLines(first, ArrayFactory.newArray("Temp Content"));
		textFiles.add(first);

		File returnedFile = ModifyFileExtensions.concatenateAll(textFiles);

		assertTrue(returnedFile.exists());
		assertTrue(returnedFile.getName().startsWith("concatenated_"));
		assertTrue(returnedFile.getName().endsWith(".txt"));

		String actual = ReadFileExtensions.fromFile(returnedFile);
		assertEquals("Temp Content\n", actual);

		// Cleanup the generated temp file and the source file
		DeleteFileExtensions.deleteFile(returnedFile);
		DeleteFileExtensions.delete(textFiles);
	}

	/**
	 * Test method for {@link ModifyFileExtensions#concatenateAll(List, File)}
	 */
	@Test
	@DisplayName("Test concatenateAll with example files (Local)")
	@Disabled("only for local tests")
	void concatenateContentOfAllMarkdownFiles() throws IOException
	{
		List<File> markdownFiles;
		String baseDir = "/run/media/astrapi69/backups/git/hub/astrapi69/chats-with-ai/prompts/";
		String concatFilename = "concat-prompts.md";

		try
		{
			markdownFiles = MarkdownFileCollector.getFilesWithExtension(baseDir, ".md");
			File resultTextFile = new File(PathFinder.getSrcTestResourcesDir(), concatFilename);
			ModifyFileExtensions.concatenateAll(markdownFiles, resultTextFile);
		}
		catch (IOException e)
		{
			System.err.println("Error: " + e.getMessage());
		}
	}

	/**
	 * Test method for {@link ModifyFileExtensions#concatenateAll(List, File)}
	 */
	@Test
	@DisplayName("Test get rules (Local)")
	@Disabled("only for local tests")
	public void testGetRules()
	{
		List<File> markdownFiles;
		String baseDir = "/home/astrapi69/dev/git/hub/astrapi69/adaptive-learner/.claude/rules";
		String concatFilename = "all-rules.md";

		try
		{
			markdownFiles = MarkdownFileCollector.getFilesWithExtension(baseDir, ".md");
			File resultTextFile = new File(PathFinder.getSrcTestResourcesDir(), concatFilename);
			ModifyFileExtensions.concatenateAll(markdownFiles, resultTextFile);
		}
		catch (IOException e)
		{
			System.err.println("Error: " + e.getMessage());
		}
	}

	/**
	 * Test method for {@link ModifyFileExtensions#concatenateAll(List, File)}
	 */
	@Test
	@DisplayName("Test concatenateAll with example files (Local)")
	@Disabled("only for local tests")
	void concatenateContentOfAllFiles() throws IOException
	{
		List<File> textFiles = new ArrayList<>();
		List<String> textFilenames = new ArrayList<>();
		String baseDir = "/run/media/astrapi69/backups/git/hub/astrapi69/chats-with-ai/prompts/zugewinnausgleich/";

		textFilenames.add("README.md");
		textFilenames.add("Parteiprogramm.md");
		textFilenames.add("Satzung.md");
		textFilenames.add("Ziele.md");
		textFilenames.add("FAQ.md");

		for (String text : textFilenames)
		{
			textFiles.add(new File(baseDir + text));
		}

		File resultTextFile = new File(PathFinder.getSrcTestResourcesDir(),
			"concat-partei-docs.txt");
		ModifyFileExtensions.concatenateAll(textFiles, resultTextFile);
	}


	// =========================================================================
	// 2. TESTS FOR CONCATENATE (Extension based)
	// =========================================================================

	/**
	 * Test method for
	 * {@link ModifyFileExtensions#concatenateFilesWithExtension(File, String, File)}.
	 */
	@Test
	@DisplayName("Test concatenateFilesWithExtension with multiple files")
	void testConcatenateFilesWithExtension() throws IOException
	{
		File testDir = new File(PathFinder.getSrcTestResourcesDir(), "test-extension-dir");
		testDir.mkdirs();

		File file1 = new File(testDir, "file1.txt");
		File file2 = new File(testDir, "file2.txt");
		File file3 = new File(testDir, "file3.md"); // Should not be found

		LineAppender.appendLines(file1, ArrayFactory.newArray("Line 1", "Line 2"));
		LineAppender.appendLines(file2, ArrayFactory.newArray("Line 3", "Line 4"));
		LineAppender.appendLines(file3, ArrayFactory.newArray("Should not be included"));

		File resultFile = new File(PathFinder.getSrcTestResourcesDir(), "result-extension.txt");

		File returnedFile = ModifyFileExtensions.concatenateFilesWithExtension(testDir, ".txt",
			resultFile);

		assertEquals(resultFile, returnedFile);
		String actual = ReadFileExtensions.fromFile(resultFile);
		String expected = "Line 1\nLine 2\nLine 3\nLine 4\n";
		assertEquals(expected, actual);

		DeleteFileExtensions.deleteFile(resultFile);
		DeleteFileExtensions.delete(testDir);
	}

	/**
	 * Test method for {@link ModifyFileExtensions#concatenateFilesWithExtension(File, String)}.
	 */
	@Test
	@DisplayName("Test concatenateFilesWithExtension with temporary result file")
	void testConcatenateFilesWithExtensionTempFile() throws IOException
	{
		File testDir = new File(PathFinder.getSrcTestResourcesDir(), "test-extension-temp-dir");
		testDir.mkdirs();

		File file1 = new File(testDir, "data1.csv");
		File file2 = new File(testDir, "data2.csv");

		LineAppender.appendLines(file1, ArrayFactory.newArray("A,B,C"));
		LineAppender.appendLines(file2, ArrayFactory.newArray("D,E,F"));

		File returnedFile = ModifyFileExtensions.concatenateFilesWithExtension(testDir, ".csv");

		assertTrue(returnedFile.exists());
		assertTrue(returnedFile.getName().startsWith("concatenated_"));
		assertTrue(returnedFile.getName().endsWith(".csv"));

		String actual = ReadFileExtensions.fromFile(returnedFile);
		String expected = "A,B,C\nD,E,F\n";
		assertEquals(expected, actual);

		DeleteFileExtensions.deleteFile(returnedFile);
		DeleteFileExtensions.delete(testDir);
	}

	/**
	 * Test method for
	 * {@link ModifyFileExtensions#concatenateFilesWithExtension(File, String, File)}. Tests
	 * exception when no files are found.
	 */
	@Test
	@DisplayName("Test concatenateFilesWithExtension throws exception when no files found")
	void testConcatenateFilesWithExtensionNoFilesFound() throws IOException
	{
		File testDir = new File(PathFinder.getSrcTestResourcesDir(), "test-empty-dir");
		testDir.mkdirs();
		File resultFile = new File(PathFinder.getSrcTestResourcesDir(), "result.txt");

		assertThrows(IllegalArgumentException.class, () -> {
			ModifyFileExtensions.concatenateFilesWithExtension(testDir, ".xyz", resultFile);
		});

		DeleteFileExtensions.delete(testDir);
	}

	/**
	 * Test method for
	 * {@link ModifyFileExtensions#concatenateFilesWithExtension(File, String, File)}. Tests
	 * exception when path is not a directory.
	 */
	@Test
	@DisplayName("Test concatenateFilesWithExtension throws exception when not a directory")
	void testConcatenateFilesWithExtensionNotADirectory() throws IOException
	{
		File notADirectory = new File(PathFinder.getSrcTestResourcesDir(), "not-a-dir.txt");
		LineAppender.appendLines(notADirectory, ArrayFactory.newArray("test"));
		File resultFile = new File(PathFinder.getSrcTestResourcesDir(), "result.txt");

		assertThrows(IllegalArgumentException.class, () -> {
			ModifyFileExtensions.concatenateFilesWithExtension(notADirectory, ".txt", resultFile);
		});

		DeleteFileExtensions.deleteFile(notADirectory);
	}

	/**
	 * Test method for
	 * {@link ModifyFileExtensions#concatenateFilesWithExtensionRecursive(File, String, File)}.
	 */
	@Test
	@DisplayName("Test concatenateFilesWithExtensionRecursive with subdirectories")
	void testConcatenateFilesWithExtensionRecursive() throws IOException
	{
		File testDir = new File(PathFinder.getSrcTestResourcesDir(), "test-recursive-dir");
		testDir.mkdirs();

		File subDir1 = new File(testDir, "subdir1");
		File subDir2 = new File(testDir, "subdir2");
		subDir1.mkdirs();
		subDir2.mkdirs();

		File file1 = new File(testDir, "root.txt");
		File file2 = new File(subDir1, "sub1.txt");
		File file3 = new File(subDir2, "sub2.txt");
		File file4 = new File(subDir1, "ignore.md"); // Should not be found

		LineAppender.appendLines(file1, ArrayFactory.newArray("Root"));
		LineAppender.appendLines(file2, ArrayFactory.newArray("Sub1"));
		LineAppender.appendLines(file3, ArrayFactory.newArray("Sub2"));
		LineAppender.appendLines(file4, ArrayFactory.newArray("Ignore"));

		File resultFile = new File(PathFinder.getSrcTestResourcesDir(), "result-recursive.txt");

		File returnedFile = ModifyFileExtensions.concatenateFilesWithExtensionRecursive(testDir,
			".txt", resultFile);

		assertEquals(resultFile, returnedFile);
		String actual = ReadFileExtensions.fromFile(resultFile);
		// Files should be sorted alphabetically by path: root.txt, sub1.txt, sub2.txt
		String expected = "Root\nSub1\nSub2\n";
		assertEquals(expected, actual);

		DeleteFileExtensions.deleteFile(resultFile);
		DeleteFileExtensions.delete(testDir);
	}

	/**
	 * Test method for
	 * {@link ModifyFileExtensions#concatenateFilesWithExtensionRecursive(File, String)}.
	 */
	@Test
	@DisplayName("Test concatenateFilesWithExtensionRecursive with temporary result file")
	void testConcatenateFilesWithExtensionRecursiveTempFile() throws IOException
	{
		File testDir = new File(PathFinder.getSrcTestResourcesDir(), "test-recursive-temp-dir");
		testDir.mkdirs();

		File subDir = new File(testDir, "nested");
		subDir.mkdirs();

		File file1 = new File(testDir, "a.json");
		File file2 = new File(subDir, "b.json");

		LineAppender.appendLines(file1, ArrayFactory.newArray("{\"key\": \"value1\"}"));
		LineAppender.appendLines(file2, ArrayFactory.newArray("{\"key\": \"value2\"}"));

		File returnedFile = ModifyFileExtensions.concatenateFilesWithExtensionRecursive(testDir,
			".json");

		assertTrue(returnedFile.exists());
		String actual = ReadFileExtensions.fromFile(returnedFile);
		String expected = "{\"key\": \"value1\"}\n{\"key\": \"value2\"}\n";
		assertEquals(expected, actual);

		DeleteFileExtensions.deleteFile(returnedFile);
		DeleteFileExtensions.delete(testDir);
	}


	// =========================================================================
	// 3. TESTS FOR MODIFY FILE
	// =========================================================================

	/**
	 * Test method for {@link ModifyFileExtensions#modifyFile(Path, Path, FileChangeable)}.
	 */
	@Test
	public void testModifyFile() throws IOException
	{
		File inputFile = new File(
			PathFinder.getRelativePath(PathFinder.getSrcTestResourcesDir(), "resources"),
			"test-csv-data.csv");
		File outputFile = new File(
			PathFinder.getRelativePath(PathFinder.getSrcTestResourcesDir(), "resources"),
			"modified-test-csv-data.csv");
		Path inFilePath = inputFile.toPath();
		Path outFilePath = outputFile.toPath();

		String add = "|#foo-bar#|";
		ModifyFileExtensions.modifyFile(inFilePath, outFilePath, (count, input) -> input + add);

		List<String> linesInList = ReadFileExtensions.readLinesInList(outputFile);
		linesInList.forEach(line -> assertTrue(line.endsWith(add)));

		String pipe = "|";
		ModifyFileExtensions.modifyFile(inFilePath, outFilePath,
			((count, input) -> input + pipe + System.lineSeparator()));

		linesInList = ReadFileExtensions.readLinesInList(outputFile);
		linesInList.forEach(line -> assertTrue(line.endsWith(pipe)));

		DeleteFileExtensions.delete(outputFile);
	}

	/**
	 * Test method for
	 * {@link ModifyFileExtensions#modifyFileAndGet(Path, Path, java.nio.charset.Charset, FileChangeable)}.
	 */
	@Test
	public void testModifyFileAndGet() throws IOException
	{
		File inputFile = new File(
			PathFinder.getRelativePath(PathFinder.getSrcTestResourcesDir(), "resources"),
			"test-csv-data-get.csv");
		File outputFile = new File(
			PathFinder.getRelativePath(PathFinder.getSrcTestResourcesDir(), "resources"),
			"modified-test-csv-data-get.csv");

		// Ensure input file exists for the test
		CopyFileExtensions.copyFile(
			new File(PathFinder.getRelativePath(PathFinder.getSrcTestResourcesDir(), "resources"),
				"test-csv-data.csv"),
			inputFile, StandardCharsets.UTF_8, StandardCharsets.UTF_8, true);

		Path inFilePath = inputFile.toPath();
		Path outFilePath = outputFile.toPath();

		Path returnedPath = ModifyFileExtensions.modifyFileAndGet(inFilePath, outFilePath,
			StandardCharsets.UTF_8, (count, input) -> input + "MODIFIED");

		assertEquals(outFilePath, returnedPath);
		assertTrue(returnedPath.toFile().exists());

		DeleteFileExtensions.delete(inputFile);
		DeleteFileExtensions.delete(outputFile);
	}

	/**
	 * Test method for {@link ModifyFileExtensions#modifyFile(Path, FileChangeable)}.
	 */
	@Test
	public void testModifyFileSameFile() throws IOException, FileIsADirectoryException
	{
		File inputFile = new File(
			PathFinder.getRelativePath(PathFinder.getSrcTestResourcesDir(), "resources"),
			"test-csv-data-samefile.csv");
		Path inFilePath = inputFile.toPath();

		String add = "|#foo-bar#|";
		ModifyFileExtensions.modifyFile(inFilePath,
			(count, input) -> input + add + System.lineSeparator());

		List<String> linesInList = ReadFileExtensions.readLinesInList(inputFile);
		linesInList.forEach(line -> assertTrue(line.endsWith(add)));

		String find = "Jaroslav";
		String replaceWith = "Wilhelm";
		ModifyFileExtensions.modifyFile(inFilePath,
			(count, input) -> input.replaceAll(find, replaceWith) + System.lineSeparator());

		String originalContent = ReadFileExtensions.fromFile(inputFile);

		assertFalse(originalContent.contains(find));
		assertTrue(originalContent.contains(replaceWith));

		// Restore original state
		CopyFileExtensions.copyFile(
			new File(PathFinder.getRelativePath(PathFinder.getSrcTestResourcesDir(), "resources"),
				"test-csv-data.csv"),
			inputFile, StandardCharsets.UTF_8, StandardCharsets.UTF_8, true);
	}

	/**
	 * Test method for
	 * {@link ModifyFileExtensions#modifyFileAndGet(Path, java.nio.charset.Charset, FileChangeable)}.
	 */
	@Test
	public void testModifyFileSameFileAndGet() throws IOException, FileIsADirectoryException
	{
		File inputFile = new File(
			PathFinder.getRelativePath(PathFinder.getSrcTestResourcesDir(), "resources"),
			"test-csv-data-samefile-get.csv");

		CopyFileExtensions.copyFile(
			new File(PathFinder.getRelativePath(PathFinder.getSrcTestResourcesDir(), "resources"),
				"test-csv-data.csv"),
			inputFile, StandardCharsets.UTF_8, StandardCharsets.UTF_8, true);

		Path inFilePath = inputFile.toPath();

		Path returnedPath = ModifyFileExtensions.modifyFileAndGet(inFilePath,
			StandardCharsets.UTF_8, (count, input) -> input + "IN_PLACE");

		assertEquals(inFilePath, returnedPath);

		List<String> linesInList = ReadFileExtensions.readLinesInList(inputFile);
		linesInList.forEach(line -> assertTrue(line.endsWith("IN_PLACE")));

		// Restore original state
		CopyFileExtensions.copyFile(
			new File(PathFinder.getRelativePath(PathFinder.getSrcTestResourcesDir(), "resources"),
				"test-csv-data.csv"),
			inputFile, StandardCharsets.UTF_8, StandardCharsets.UTF_8, true);
	}


	// =========================================================================
	// 4. UTILITY TESTS
	// =========================================================================

	/**
	 * Test method for {@link ModifyFileExtensions}
	 */
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(ModifyFileExtensions.class);
	}
}
