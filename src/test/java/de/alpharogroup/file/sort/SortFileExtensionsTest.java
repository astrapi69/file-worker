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
package de.alpharogroup.file.sort;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.meanbean.factories.ObjectCreationException;
import org.meanbean.test.BeanTestException;
import org.meanbean.test.BeanTester;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.alpharogroup.comparators.StringComparator;
import de.alpharogroup.file.FileExtensions;
import de.alpharogroup.file.create.CreateFileExtensions;
import de.alpharogroup.file.read.ReadFileExtensions;
import de.alpharogroup.file.write.WriteFileExtensions;
import lombok.extern.slf4j.Slf4j;

/**
 * The unit test class for the class {@link SortFileExtensions}.
 */
@Slf4j
public class SortFileExtensionsTest
{
	/** The directory /src/test/resources for test resources. */
	File testResources;

	/** The resources. */
	File resources;

	/**
	 * Sets up method will be invoked before every unit test method in this class.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@BeforeMethod
	protected void setUp() throws Exception
	{
		// Get the absolut path from the current project.
		final String absolutePath = FileExtensions.getCurrentAbsolutPathWithoutDotAndSlash();
		final File projectPath = new File(absolutePath);
		assertTrue("The directory " + projectPath.getAbsolutePath() + " should be created.",
			projectPath.exists());
		log.debug("The directory " + projectPath.getAbsolutePath() + " exists or is created.");
		testResources = new File(projectPath.getAbsoluteFile(), "/src/test/resources");
		if (!testResources.exists())
		{
			final boolean created = CreateFileExtensions.newDirectory(testResources);
			assertTrue("The directory " + testResources.getAbsolutePath() + " should be created.",
				created);
		}
		resources = new File(testResources, "resources");

	}

	/**
	 * Test method for {@link SortFileExtensions#sort(File, java.util.Comparator, String)}.
	 *
	 * @throws FileNotFoundException
	 *             the file not found exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testSort() throws FileNotFoundException, IOException
	{
		final File testFile = new File(testResources, "resources");
		final File epfFile = new File(testFile, "testEpf.epf");

		List<String> originalLines = ReadFileExtensions.readLinesInList(epfFile, false);

		SortFileExtensions.sort(epfFile, StringComparator.of(), "UTF-8");

		List<String> actualSortedLines = ReadFileExtensions.readLinesInList(epfFile, false);

		final File sortedEpfFile = new File(testFile, "testSortedEpf.epf");

		List<String> expectedSortedLines = ReadFileExtensions.readLinesInList(sortedEpfFile, false);

		assertEquals(expectedSortedLines.size(), actualSortedLines.size());

		Object expected;
		Object actual;
		for (int i = 0; i < expectedSortedLines.size(); i++)
		{
			expected = expectedSortedLines.get(i);
			actual = actualSortedLines.get(i);
			assertEquals(expected, actual);
		}
		// create initial state...
		WriteFileExtensions.writeLinesToFile(epfFile, originalLines, "UTF-8");
	}

	/**
	 * Test method for {@link SortFileExtensions}
	 */
	@Test(expectedExceptions = { BeanTestException.class, ObjectCreationException.class })
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(SortFileExtensions.class);
	}

}
