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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.github.astrapi69.file.copy.CopyFileExtensions;
import io.github.astrapi69.file.create.FileFactory;
import io.github.astrapi69.file.delete.DeleteFileExtensions;
import io.github.astrapi69.file.read.ReadFileExtensions;
import io.github.astrapi69.file.search.PathFinder;

/**
 * The class {@link DeleteLinesByIndexInFileTest} provides unit tests for the class
 * {@link DeleteLinesByIndexInFile}
 */
public class DeleteLinesByIndexInFileTest
{

	/**
	 * Test method for {@link DeleteLinesByIndexInFile#apply(Integer, String)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	@Test
	public void testApply() throws IOException
	{
		File inputFile;
		File testFile;
		File parent = PathFinder.getRelativePath(PathFinder.getSrcTestResourcesDir(), "resources");

		inputFile = FileFactory.newFile(parent, "test-csv-data.csv");
		testFile = FileFactory.newFile(parent, "test-delete-lines-data.csv");
		CopyFileExtensions.copyFile(inputFile, testFile);

		Path inputFilePath = testFile.toPath();
		List<Integer> linesToDelete = Arrays.asList(1, 4); // Example line indexes to delete

		DeleteLinesByIndexInFile deleter = new DeleteLinesByIndexInFile(linesToDelete);

		ModifyFileExtensions.modifyFile(inputFilePath, deleter);

		List<String> linesInList = ReadFileExtensions.readLinesInList(inputFile);

		String content = ReadFileExtensions.fromFile(testFile);

		assertFalse(content.contains(linesInList.get(1)));
		assertFalse(content.contains(linesInList.get(4)));

		assertTrue(content.contains(linesInList.get(0)));
		assertTrue(content.contains(linesInList.get(2)));
		assertTrue(content.contains(linesInList.get(3)));

		DeleteFileExtensions.deleteFile(testFile);
	}
}