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
package de.alpharogroup.file;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

import java.io.File;
import java.io.IOException;

import org.meanbean.factories.ObjectCreationException;
import org.meanbean.test.BeanTestException;
import org.meanbean.test.BeanTester;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.alpharogroup.file.write.WriteFileExtensions;

/**
 * The unit test class for the class {@link FileExtensions}.
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public class FileExtensionsTest extends FileTestCase
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
	 * Test method for {@link FileExtensions#download(java.net.URI)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testDownload() throws IOException
	{

		final byte[] expected = { -84, -19, 0, 5, 116, 0, 7, 70, 111, 111, 32, 98, 97, 114 };

		final File destination = new File(this.testDir.getAbsoluteFile(), "testDownload.txt");

		WriteFileExtensions.storeByteArrayToFile(expected, destination);

		final byte[] compare = FileExtensions.download(destination.toURI());

		for (int i = 0; i < compare.length; i++)
		{
			this.actual = compare[i] == expected[i];
			assertTrue("", this.actual);
		}
	}

	/**
	 * Test method for {@link FileExtensions#getAbsolutPathWithoutFilename(File)}.
	 */
	@Test
	public void testGetAbsolutPathWithoutFilename()
	{
		String actual;
		String expected;
		String absolutePath = this.testDir.getAbsolutePath();
		final File srcFile = new File(this.testDir.getAbsoluteFile(), "testMoveFile.txt");
		actual = FileExtensions.getAbsolutPathWithoutFilename(srcFile);
		expected = absolutePath + "/";
		assertEquals(actual, expected);
	}

	/**
	 * Test method for {@link FileExtensions#getFilenamePrefix(File)}.
	 */
	@Test
	public void testGetFilenamePrefix()
	{
		final String filePrefix = "testGetFilenamePrefix";
		final String fileSuffix = ".txt";

		final File testFile1 = new File(this.testDir, filePrefix + fileSuffix);
		final String ap = testFile1.getAbsolutePath();
		final int ext_index = ap.lastIndexOf(".");
		final String fileNamePrefix = ap.substring(0, ext_index);
		final String expected = fileNamePrefix;
		final String compare = FileExtensions.getFilenamePrefix(testFile1);
		this.actual = expected.equals(compare);
		assertTrue("", this.actual);
	}

	/**
	 * Test method for {@link FileExtensions#getFilenameSuffix(File)}.
	 */
	@Test
	public void testGetFilenameSuffix()
	{
		final String filePrefix = "testAppendSystemtimeToFilename";
		final String fileSuffix = ".txt";
		final String expected = fileSuffix;
		final File testFile1 = new File(this.testDir, filePrefix + fileSuffix);
		final String compare = FileExtensions.getFilenameSuffix(testFile1);
		this.actual = expected.equals(compare);
		assertTrue("", this.actual);
	}

	/**
	 * Test method for {@link FileExtensions#getTempDir()}.
	 */
	@Test
	public void testGetTempDir()
	{
		File actual;
		File expected;
		actual = FileExtensions.getTempDir();
		expected = new File(System.getProperty("java.io.tmpdir"));
		assertEquals(actual, expected);
	}

	/**
	 * Test method for {@link FileExtensions#isOpen(File)}.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void testIsOpen() throws IOException
	{
		boolean actual;
		boolean expected;
		final File srcFile = new File(this.testDir.getAbsoluteFile(), "testMoveFile.txt");
		actual = FileExtensions.isOpen(srcFile);
		expected = false;
		assertEquals(actual, expected);
	}

	/**
	 * Test method for {@link FileExtensions}
	 */
	@Test(expectedExceptions = { BeanTestException.class, ObjectCreationException.class })
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(FileExtensions.class);
	}

}
