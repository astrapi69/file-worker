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
package io.github.astrapi69.file.search;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.List;

import org.apache.commons.lang3.SystemUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;

import io.github.astrapi69.collection.list.ListFactory;
import io.github.astrapi69.file.FileTestCase;

/**
 * The unit test class for the class {@link PathFinder}.
 */
public class PathFinderTest extends FileTestCase
{

	/**
	 * Sets up method will be invoked before every unit test method in this class.
	 *
	 * @throws Exception
	 *             is thrown if an exception occurs
	 */
	@Override
	@BeforeEach
	protected void setUp() throws Exception
	{
		super.setUp();
	}

	/**
	 * Teardown method will be invoked after every unit test method in this class.
	 *
	 * @throws Exception
	 *             is thrown if an exception occurs
	 */
	@Override
	@AfterEach
	protected void tearDown() throws Exception
	{
		super.tearDown();
	}

	/**
	 * Test method for {@link PathFinder#getAbsolutePath(File, boolean)}.
	 */
	@Test
	public void testGetAbsolutePath()
	{
		String actual;
		String expected;
		actual = PathFinder.getAbsolutePath(testDir, false);
		expected = SystemUtils.IS_OS_WINDOWS
			? "file-worker\\src\\test\\resources\\resources\\testDir"
			: "file-worker/src/test/resources/resources/testDir";
		assertTrue(actual.endsWith(expected));
	}

	/**
	 * Test method for {@link PathFinder#getProjectDirectory()}.
	 */
	@Test
	public void testGetProjectDirectory()
	{
		File actual;
		File expected;
		final File pp = new File(".");
		actual = PathFinder.getProjectDirectory();
		expected = pp.getAbsoluteFile();
		String expectedPath = expected.getPath();
		expectedPath = expectedPath.substring(0, expectedPath.length() - 2);
		String actualPath = actual.getPath();
		assertEquals(actualPath, expectedPath);
	}


	/**
	 * Test method for {@link PathFinder#getCurrentDirectory()}.
	 */
	@Test
	public void testGetCurrentDirectory()
	{
		File actual;
		File expected;
		final File pp = new File(".");
		actual = PathFinder.getCurrentDirectory();
		assertTrue(actual.isDirectory());
		expected = pp.getAbsoluteFile();
		String expectedPath = expected.getPath();
		expectedPath = expectedPath.substring(0, expectedPath.length() - 2);
		String actualPath = actual.getPath();
		assertEquals(actualPath, expectedPath);
	}

	/**
	 * Test method for {@link PathFinder#getProjectDirectory(File)}.
	 */
	@Test
	public void testGetProjectDirectoryFile()
	{
		File actual;
		File expected;
		final File pp = new File(".");
		actual = PathFinder.getProjectDirectory(pp);
		expected = pp.getAbsoluteFile();
		String expectedPath = expected.getPath();
		expectedPath = expectedPath.substring(0, expectedPath.length() - 2);
		String actualPath = actual.getPath();
		assertEquals(actualPath, expectedPath);
	}

	/**
	 * Test method for {@link PathFinder#getRelativePath(File, String[])}.
	 */
	@Test
	public void testGetRelativePath()
	{
		File actual;
		File expected;

		actual = PathFinder.getRelativePath(testDir, "foo", "bar");
		expected = new File(testDir, "foo");
		expected = new File(expected, "bar");
		assertEquals(actual, expected);

		actual = PathFinder.getRelativePath(testDir, "foo", "bar", "bla");
		expected = new File(testDir, "foo");
		expected = new File(expected, "bar");
		expected = new File(expected, "bla");
		assertEquals(actual, expected);
	}

	/**
	 * Test method for {@link PathFinder#getRelativePathTo(File, List)}.
	 */
	@Test
	public void testGetRelativePathToFileListOfString()
	{
		File actual;
		File expected;
		List<String> folders;
		folders = ListFactory.newArrayList("foo", "bar");
		actual = PathFinder.getRelativePathTo(testDir, folders);
		expected = new File(testDir, "foo");
		expected = new File(expected, "bar");
		assertEquals(actual, expected);

		folders = ListFactory.newArrayList("foo", "bar", "bla");
		actual = PathFinder.getRelativePathTo(testDir, folders);
		expected = new File(testDir, "foo");
		expected = new File(expected, "bar");
		expected = new File(expected, "bla");
		assertEquals(actual, expected);

		folders = ListFactory.newArrayList("foo", "bar", "bla", "test.txt");
		actual = PathFinder.getRelativePathTo(testDir, folders);
		expected = new File(testDir, "foo");
		expected = new File(expected, "bar");
		expected = new File(expected, "bla");
		expected = new File(expected, "test.txt");
		assertEquals(actual, expected);
	}

	/**
	 * Test method for {@link PathFinder#getRelativePathTo(File, String, String, String)}.
	 */
	@Test
	public void testGetRelativePathToFileStringStringString()
	{
		File actual;
		File expected;

		actual = PathFinder.getRelativePathTo(testDir, ",", "foo,bar", "test.txt");
		expected = new File(testDir, "foo");
		expected = new File(expected, "bar");
		expected = new File(expected, "test.txt");
		assertEquals(actual, expected);

		actual = PathFinder.getRelativePathTo(testDir, ",", "foo,bar,bla", "test.txt");
		expected = new File(testDir, "foo");
		expected = new File(expected, "bar");
		expected = new File(expected, "bla");
		expected = new File(expected, "test.txt");
		assertEquals(actual, expected);
	}

	/**
	 * Test method for {@link PathFinder#getSrcMainJavaDir()}.
	 */
	@Test
	public void testGetSrcMainJavaDir()
	{
		File actual;
		File expected;
		actual = PathFinder.getSrcMainJavaDir();
		expected = new File(PathFinder.getProjectDirectory(),
			PathFinder.SOURCE_FOLDER_SRC_MAIN_JAVA);
		assertEquals(actual, expected);
	}

	/**
	 * Test method for {@link PathFinder#getSrcMainJavaDir(File)}.
	 */
	@Test
	public void testGetSrcMainJavaDirFile()
	{
		File actual;
		File expected;
		actual = PathFinder.getSrcMainJavaDir(PathFinder.getProjectDirectory());
		expected = new File(PathFinder.getProjectDirectory(),
			PathFinder.SOURCE_FOLDER_SRC_MAIN_JAVA);
		assertEquals(actual, expected);
	}

	/**
	 * Test method for {@link PathFinder#getSrcMainResourcesDir()}.
	 */
	@Test
	public void testGetSrcMainResourcesDir()
	{
		File actual;
		File expected;
		actual = PathFinder.getSrcMainResourcesDir();
		expected = new File(PathFinder.getProjectDirectory(),
			PathFinder.SOURCE_FOLDER_SRC_MAIN_RESOURCES);
		assertEquals(actual, expected);
	}

	/**
	 * Test method for {@link PathFinder#getSrcMainResourcesDir(File)}.
	 */
	@Test
	public void testGetSrcMainResourcesDirFile()
	{
		File actual;
		File expected;
		actual = PathFinder.getSrcMainResourcesDir(PathFinder.getProjectDirectory());
		expected = new File(PathFinder.getProjectDirectory(),
			PathFinder.SOURCE_FOLDER_SRC_MAIN_RESOURCES);
		assertEquals(actual, expected);
	}

	/**
	 * Test method for {@link PathFinder#getSrcTestJavaDir()}.
	 */
	@Test
	public void testGetSrcTestJavaDir()
	{
		File actual;
		File expected;
		actual = PathFinder.getSrcTestJavaDir();
		expected = new File(PathFinder.getProjectDirectory(),
			PathFinder.SOURCE_FOLDER_SRC_TEST_JAVA);
		assertEquals(actual, expected);
	}

	/**
	 * Test method for {@link PathFinder#getSrcTestJavaDir(File)}.
	 */
	@Test
	public void testGetSrcTestJavaDirFile()
	{
		File actual;
		File expected;
		actual = PathFinder.getSrcTestJavaDir(PathFinder.getProjectDirectory());
		expected = new File(PathFinder.getProjectDirectory(),
			PathFinder.SOURCE_FOLDER_SRC_TEST_JAVA);
		assertEquals(actual, expected);
	}

	/**
	 * Test method for {@link PathFinder#getSrcTestResourcesDir()}.
	 */
	@Test
	public void testGetSrcTestResourcesDir()
	{
		File actual;
		File expected;
		actual = PathFinder.getSrcTestResourcesDir();
		expected = new File(PathFinder.getProjectDirectory(),
			PathFinder.SOURCE_FOLDER_SRC_TEST_RESOURCES);
		assertEquals(actual, expected);
	}

	/**
	 * Test method for {@link PathFinder#getSrcTestResourcesDir(File)}.
	 */
	@Test
	public void testGetSrcTestResourcesDirFile()
	{
		File actual;
		File expected;
		actual = PathFinder.getSrcTestResourcesDir(PathFinder.getProjectDirectory());
		expected = new File(PathFinder.getProjectDirectory(),
			PathFinder.SOURCE_FOLDER_SRC_TEST_RESOURCES);
		assertEquals(actual, expected);
	}

	/**
	 * Test method for {@link PathFinder}
	 */
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(PathFinder.class);
	}

}
