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
package de.alpharogroup.file.modify;

import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.meanbean.factories.ObjectCreationException;
import org.meanbean.test.BeanTestException;
import org.meanbean.test.BeanTester;
import org.testng.annotations.Test;

import de.alpharogroup.file.delete.DeleteFileExtensions;
import de.alpharogroup.file.modify.api.FileChangable;
import de.alpharogroup.file.read.ReadFileExtensions;
import de.alpharogroup.file.search.PathFinder;

/**
 * The unit test class for the class {@link ModifyFileExtensions}
 */
public class ModifyFileExtensionsTest
{

	/**
	 * Test method for
	 * {@link ModifyFileExtensions#modifyFile(java.nio.file.Path, java.nio.file.Path, FileChangable)}.
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
		String add = "|#foo-bar#|";
		ModifyFileExtensions.modifyFile(inputFile.toPath(), outputFile.toPath(), (count, input) -> {
			String alteredLine = input + add;
			return alteredLine;
		});
		List<String> linesInList = ReadFileExtensions.readLinesInList(outputFile);
		linesInList.stream().forEach(line -> assertTrue(line.endsWith(add)));

		DeleteFileExtensions.delete(outputFile);

	}

	/**
	 * Test method for {@link ModifyFileExtensions}
	 */
	@Test(expectedExceptions = { BeanTestException.class, ObjectCreationException.class })
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(ModifyFileExtensions.class);
	}

}