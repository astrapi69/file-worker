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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

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

	/**
	 * Test method for {@link ModifyFileExtensions#concatenateAll(List, File)} Simple test case to
	 * ensure that the method executes without throwing an exception
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
		String expected;
		String actual;

		String[] linesToAppend = ArrayFactory.newArray("Line 1", "Line 2", "Line 3");

		LineAppender.appendLines(first, linesToAppend);
		linesToAppend = ArrayFactory.newArray("Line 4", "Line 5", "Line 6");

		LineAppender.appendLines(second, linesToAppend);

		File resultTextFile = new File(PathFinder.getSrcTestResourcesDir(), "result.txt");

		ModifyFileExtensions.concatenateAll(textFiles, resultTextFile);
		expected = "Line 1\n" + "Line 2\n" + "Line 3\n" + "Line 4\n" + "Line 5\n" + "Line 6\n";
		actual = ReadFileExtensions.fromFile(resultTextFile);
		assertEquals(expected, actual);
		DeleteFileExtensions.deleteFile(resultTextFile);
		DeleteFileExtensions.delete(textFiles);
	}

	/**
	 * Test method for
	 * {@link ModifyFileExtensions#modifyFile(java.nio.file.Path, java.nio.file.Path, FileChangeable)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testModifyFile() throws IOException
	{
		File inputFile;
		File outputFile;

		inputFile = new File(
			PathFinder.getRelativePath(PathFinder.getSrcTestResourcesDir(), "resources"),
			"test-csv-data.csv");
		outputFile = new File(
			PathFinder.getRelativePath(PathFinder.getSrcTestResourcesDir(), "resources"),
			"modified-test-csv-data.csv");
		Path inFilePath = inputFile.toPath();
		Path outFilePath = outputFile.toPath();

		String add = "|#foo-bar#|";
		ModifyFileExtensions.modifyFile(inFilePath, outFilePath, (count, input) -> {
			String alteredLine = input + add;
			return alteredLine;
		});
		List<String> linesInList = ReadFileExtensions.readLinesInList(outputFile);
		linesInList.stream().forEach(line -> assertTrue(line.endsWith(add)));
		String pipe = "|";
		ModifyFileExtensions.modifyFile(inFilePath, outFilePath, ((count, input) -> {
			String alteredLine = input + pipe + System.lineSeparator();
			return alteredLine;
		}));
		linesInList = ReadFileExtensions.readLinesInList(outputFile);
		linesInList.stream().forEach(line -> assertTrue(line.endsWith(pipe)));

		DeleteFileExtensions.delete(outputFile);
	}

	/**
	 * Test method for
	 * {@link ModifyFileExtensions#modifyFile(java.nio.file.Path, java.nio.file.Path, FileChangeable)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 * @throws FileIsADirectoryException
	 *             Is thrown if the destination file is a directory
	 */
	@Test
	public void testModifyFileSameFile() throws IOException, FileIsADirectoryException
	{
		File inputFile;
		Path inFilePath;
		String originalContent;
		String add;
		List<String> linesInList;

		inputFile = new File(
			PathFinder.getRelativePath(PathFinder.getSrcTestResourcesDir(), "resources"),
			"test-csv-data-samefile.csv");

		inFilePath = inputFile.toPath();

		add = "|#foo-bar#|";
		ModifyFileExtensions.modifyFile(inFilePath, (count, input) -> {
			String alteredLine = input + add + System.lineSeparator();
			return alteredLine;
		});
		linesInList = ReadFileExtensions.readLinesInList(inputFile);
		linesInList.stream().forEach(line -> assertTrue(line.endsWith(add)));


		String find = "Jaroslav";
		String replaceWith = "Wilhelm";
		ModifyFileExtensions.modifyFile(inFilePath, (count, input) -> {
			String alteredLine = input.replaceAll(find, replaceWith) + System.lineSeparator();
			return alteredLine;
		});

		originalContent = ReadFileExtensions.fromFile(inputFile);

		assertFalse(originalContent.contains(find));
		assertTrue(originalContent.contains(replaceWith));

		CopyFileExtensions.copyFile(
			new File(PathFinder.getRelativePath(PathFinder.getSrcTestResourcesDir(), "resources"),
				"test-csv-data.csv"),
			inputFile, StandardCharsets.UTF_8, StandardCharsets.UTF_8, true);

	}

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
