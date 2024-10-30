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

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link Searchable} methods.
 */
public class SearchableTest
{

	private Searchable searchable;
	private Path testDirectory;

	@BeforeEach
	public void setUp() throws Exception
	{
		// Create an instance of Searchable for testing
		searchable = new Searchable()
		{
		}; // Assuming FileSearchExtensions implements Searchable
		testDirectory = Files.createTempDirectory("fileSearchTestDir");

		// Create test files and directories
		Files.createFile(testDirectory.resolve("logfile1.txt"));
		Files.createFile(testDirectory.resolve("logfile2.txt"));
		Files.createFile(testDirectory.resolve("logfile1.log"));
		Files.createFile(testDirectory.resolve("otherfile.txt"));

		Path subDir = Files.createDirectory(testDirectory.resolve("subDir"));
		Files.createFile(subDir.resolve("logfile3.txt"));
		Files.createFile(subDir.resolve("logfile4.log"));
	}

	@AfterEach
	public void tearDown() throws Exception
	{
		// Cleanup
		Files.walk(testDirectory).map(Path::toFile).forEach(File::delete);
	}

	@Test
	public void testFindFilesWithPrefixAndExtensionRecursive()
	{
		File dir = testDirectory.toFile();
		String prefix = "logfile";
		String extension = "txt";

		List<File> files = searchable.findFilesWithPrefixAndExtensionRecursive(dir, prefix,
			extension);

		assertAll("Recursive prefix and extension match", () -> assertEquals(3, files.size()),
			() -> assertTrue(
				files.stream().anyMatch(file -> file.getName().equals("logfile1.txt"))),
			() -> assertTrue(
				files.stream().anyMatch(file -> file.getName().equals("logfile2.txt"))),
			() -> assertTrue(
				files.stream().anyMatch(file -> file.getName().equals("logfile3.txt"))));
	}

	@Test
	public void testFindFilesWithNoMatch()
	{
		File dir = testDirectory.toFile();
		String prefix = "nonexistent";
		String extension = "txt";

		List<File> files = searchable.findFilesWithPrefixAndExtensionRecursive(dir, prefix,
			extension);

		assertTrue(files.isEmpty(), "Should find no matching files");
	}

	@Test
	public void testContainsFileByFileReference() throws IOException
	{
		File testFile = new File(testDirectory.toFile(), "sample.txt");
		Files.createFile(testFile.toPath());

		boolean fileExists = searchable.containsFile(testDirectory.toFile(), testFile);
		assertTrue(fileExists, "File should exist in the directory");

		Files.delete(testFile.toPath());
	}

	@Test
	public void testFindFilesUsingPredicate() throws IOException
	{
		Predicate<File> txtFileFilter = file -> file.getName().endsWith(".txt");

		Set<File> foundFiles = searchable.findFiles(testDirectory.toFile(), txtFileFilter);

		assertAll("Predicate search for .txt files",
			() -> assertFalse(foundFiles.isEmpty(), "Should find .txt files"), () -> assertTrue(
				foundFiles.stream().allMatch(file -> file.getName().endsWith(".txt"))));
	}

	@Test
	public void testCountAllFilesInDirectory()
	{
		long fileCount = searchable.countAllFilesInDirectory(testDirectory.toFile(), 0, true);
		assertTrue(fileCount > 0, "Should count files and directories");
	}
}
