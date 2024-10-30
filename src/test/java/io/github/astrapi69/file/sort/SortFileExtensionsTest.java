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
package io.github.astrapi69.file.sort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;

import io.github.astrapi69.comparator.object.StringComparator;
import io.github.astrapi69.file.FileExtensions;
import io.github.astrapi69.file.create.DirectoryFactory;
import io.github.astrapi69.file.create.model.FileCreationState;
import io.github.astrapi69.file.read.ReadFileExtensions;
import io.github.astrapi69.file.write.StoreFileExtensions;

/**
 * The unit test class for the class {@link SortFileExtensions}.
 */
public class SortFileExtensionsTest
{

	String absolutePath;
	String actual;
	boolean created;

	String expected;
	File projectPath;
	/** The resources. */
	File resources;
	/** The directory /src/test/resources for test resources. */
	File testResources;

	/**
	 * Sets up method will be invoked before every unit test method in this class.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@BeforeEach
	protected void setUp() throws Exception
	{
		// Get the absolut path from the current project.
		absolutePath = FileExtensions.getCurrentAbsolutPathWithoutDotAndSlash();
		projectPath = new File(absolutePath);
		assertTrue(projectPath.exists(),
			"The directory " + projectPath.getAbsolutePath() + " should be created.");
		testResources = new File(projectPath.getAbsoluteFile(), "/src/test/resources");
		if (!testResources.exists())
		{
			FileCreationState state = DirectoryFactory.newDirectory(testResources);
			assertTrue(state.equals(FileCreationState.CREATED),
				"The directory " + testResources.getAbsolutePath() + " should be created.");
		}
		resources = new File(testResources, "resources");

	}

	/**
	 * Test method for {@link SortFileExtensions#sort(File, java.util.Comparator, String)}.
	 *
	 * @throws FileNotFoundException
	 *             is thrown if the given file is not found
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testSort() throws FileNotFoundException, IOException
	{
		File testFile;
		File epfFile;
		File sortedEpfFile;
		List<String> originalLines;
		List<String> actualSortedLines;
		List<String> expectedSortedLines;

		testFile = new File(testResources, "resources");
		epfFile = new File(testFile, "testEpf.epf");

		originalLines = ReadFileExtensions.readLinesInList(epfFile, false);

		SortFileExtensions.sort(epfFile, StringComparator.of(), "UTF-8");

		actualSortedLines = ReadFileExtensions.readLinesInList(epfFile, false);

		sortedEpfFile = new File(testFile, "testSortedEpf.epf");

		expectedSortedLines = ReadFileExtensions.readLinesInList(sortedEpfFile, false);

		assertEquals(expectedSortedLines.size(), actualSortedLines.size());

		for (int i = 0; i < expectedSortedLines.size(); i++)
		{
			expected = expectedSortedLines.get(i);
			actual = actualSortedLines.get(i);
			assertEquals(expected, actual);
		}
		// create initial state...
		StoreFileExtensions.toFile(epfFile, originalLines, "UTF-8");
	}

	/**
	 * Test method for {@link SortFileExtensions}
	 */
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(SortFileExtensions.class);
	}

}
