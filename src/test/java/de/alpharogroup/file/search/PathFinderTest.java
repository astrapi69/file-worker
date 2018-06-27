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
package de.alpharogroup.file.search;

import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertEquals;

import java.io.File;
import java.util.List;

import org.meanbean.factories.ObjectCreationException;
import org.meanbean.test.BeanTestException;
import org.meanbean.test.BeanTester;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.alpharogroup.file.FileTestCase;

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
	@BeforeMethod
	protected void setUp() throws Exception
	{
		super.setUp();
	}

	/**
	 * Tear down method will be invoked after every unit test method in this class.
	 *
	 * @throws Exception
	 *             is thrown if an exception occurs
	 */
	@Override
	@AfterMethod
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
		expected = "file-worker/src/test/resources/resources/testDir";
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
		expectedPath = expectedPath.substring(0, expectedPath.length()-2);
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
		expectedPath = expectedPath.substring(0, expectedPath.length()-2);
		String actualPath = actual.getPath();
		assertEquals(actualPath, expectedPath);
	}

	/**
	 * Test method for {@link PathFinder#getRelativePath(File, String[])}.
	 */
	@Test
	public void testGetRelativePath()
	{
		// TODO implement unit test cases...
		File actual;
		File expected;
		actual = PathFinder.getRelativePath(testDir, "foo", "bar");
	}

	/**
	 * Test method for {@link PathFinder#getRelativePathTo(File, List)}.
	 */
	@Test
	public void testGetRelativePathToFileListOfString()
	{
		// TODO implement unit test cases...
	}

	/**
	 * Test method for {@link PathFinder#getRelativePathTo(File, String, String, String)}.
	 */
	@Test
	public void testGetRelativePathToFileStringStringString()
	{
		// TODO implement unit test cases...
	}

	/**
	 * Test method for {@link PathFinder#getSrcMainJavaDir()}.
	 */
	@Test
	public void testGetSrcMainJavaDir()
	{
		// TODO implement unit test cases...
	}

	/**
	 * Test method for {@link PathFinder#getSrcMainJavaDir(File)}.
	 */
	@Test
	public void testGetSrcMainJavaDirFile()
	{
		// TODO implement unit test cases...
	}

	/**
	 * Test method for {@link PathFinder#getSrcMainResourcesDir()}.
	 */
	@Test
	public void testGetSrcMainResourcesDir()
	{
		// TODO implement unit test cases...
	}

	/**
	 * Test method for {@link PathFinder#getSrcMainResourcesDir(File)}.
	 */
	@Test
	public void testGetSrcMainResourcesDirFile()
	{
		// TODO implement unit test cases...
	}

	/**
	 * Test method for {@link PathFinder#getSrcTestJavaDir()}.
	 */
	@Test
	public void testGetSrcTestJavaDir()
	{
		// TODO implement unit test cases...
	}

	/**
	 * Test method for {@link PathFinder#getSrcTestJavaDir(File)}.
	 */
	@Test
	public void testGetSrcTestJavaDirFile()
	{
		// TODO implement unit test cases...
	}

	/**
	 * Test method for {@link PathFinder#getSrcTestResourcesDir()}.
	 */
	@Test
	public void testGetSrcTestResourcesDir()
	{
		// TODO implement unit test cases...
	}

	/**
	 * Test method for {@link PathFinder#getSrcTestResourcesDir(File)}.
	 */
	@Test
	public void testGetSrcTestResourcesDirFile()
	{
		// TODO implement unit test cases...
	}

	/**
	 * Test method for {@link PathFinder}
	 */
	@Test(expectedExceptions = { BeanTestException.class, ObjectCreationException.class })
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(PathFinder.class);
	}

}
