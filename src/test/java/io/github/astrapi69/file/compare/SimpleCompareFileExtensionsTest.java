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
package io.github.astrapi69.file.compare;

import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import org.meanbean.factories.ObjectCreationException;
import org.meanbean.test.BeanTestException;
import org.meanbean.test.BeanTester;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.github.astrapi69.file.FileTestCase;
import io.github.astrapi69.crypto.algorithm.Algorithm;
import io.github.astrapi69.crypto.algorithm.MdAlgorithm;
import io.github.astrapi69.file.delete.DeleteFileExtensions;
import io.github.astrapi69.file.write.WriteFileExtensions;

public class SimpleCompareFileExtensionsTest extends FileTestCase
{

	@Override
	@BeforeMethod
	protected void setUp() throws Exception
	{
		super.setUp();
	}

	@Override
	@AfterMethod
	public void tearDown() throws Exception
	{
		super.tearDown();
	}

	@Test
	public void testCompareFilesByAbsolutePath() throws IOException
	{
		final File testFile1 = new File(this.testDir.getAbsoluteFile(),
			"testFindFilesRecursive.txt");

		final File testFile2 = new File(this.testDir.getAbsoluteFile(),
			"testFindFilesRecursive2.tft");

		WriteFileExtensions.string2File(testFile1, "Its a beautifull day!!!");
		WriteFileExtensions.string2File(testFile2, "Its a beautifull day!!!");
		this.actual = SimpleCompareFileExtensions.compareFilesByAbsolutePath(testFile1, testFile1);
		assertTrue(this.actual);
		this.actual = SimpleCompareFileExtensions.compareFilesByAbsolutePath(testFile1, testFile2);
		assertFalse(this.actual);
		DeleteFileExtensions.delete(testFile2);
		DeleteFileExtensions.delete(testFile1);

	}

	/**
	 * Test method for
	 * {@link SimpleCompareFileExtensions#compareFilesByChecksum(File, File, Algorithm)}
	 */
	@Test
	public void testCompareFilesByChecksum() throws NoSuchAlgorithmException, IOException
	{
		final File testFile1 = new File(this.testDir, "testFindFilesRecursive.txt");
		final File testFile2 = new File(this.deepDir, "testFindFilesRecursive.txt");

		WriteFileExtensions.string2File(testFile1, "Its a beautifull day!!!");
		WriteFileExtensions.string2File(testFile2, "Its a beautifull day!!!");
		this.actual = SimpleCompareFileExtensions.compareFilesByChecksum(testFile1, testFile2,
			MdAlgorithm.MD5);
		assertTrue(this.actual);
		DeleteFileExtensions.delete(testFile2);
		DeleteFileExtensions.delete(testFile1);
	}

	/**
	 * Test method for {@link SimpleCompareFileExtensions#compareFilesByChecksumAdler32(File, File)}
	 */
	@Test
	public void testCompareFilesByChecksumAdler32() throws IOException
	{
		final File testFile1 = new File(this.testDir, "testFindFilesRecursive.txt");
		final File testFile2 = new File(this.deepDir, "testFindFilesRecursive.txt");

		WriteFileExtensions.string2File(testFile1, "Its a beautifull day!!!");
		WriteFileExtensions.string2File(testFile2, "Its a beautifull day!!!");
		this.actual = SimpleCompareFileExtensions.compareFilesByChecksumAdler32(testFile1,
			testFile2);
		assertTrue(this.actual);
		DeleteFileExtensions.delete(testFile2);
		DeleteFileExtensions.delete(testFile1);
	}

	/**
	 * Test method for {@link SimpleCompareFileExtensions#compareFilesByChecksumCRC32(File, File)}
	 */
	@Test
	public void testCompareFilesByChecksumCRC32() throws IOException
	{
		final File testFile1 = new File(this.testDir, "testFindFilesRecursive.txt");
		final File testFile2 = new File(this.deepDir, "testFindFilesRecursive.txt");

		WriteFileExtensions.string2File(testFile1, "Its a beautifull day!!!");
		WriteFileExtensions.string2File(testFile2, "Its a beautifull day!!!");
		this.actual = SimpleCompareFileExtensions.compareFilesByChecksumCRC32(testFile1, testFile2);
		assertTrue(this.actual);
		DeleteFileExtensions.delete(testFile2);
		DeleteFileExtensions.delete(testFile1);
	}

	/**
	 * Test method for {@link SimpleCompareFileExtensions#compareFilesByContent(File, File)}
	 */
	@Test
	public void testCompareFilesByContent() throws IOException
	{
		final File testFile1 = new File(this.testDir.getAbsoluteFile(),
			"testFindFilesRecursive.txt");

		final File testFile2 = new File(this.testDir.getAbsoluteFile(),
			"testFindFilesRecursive2.tft");

		WriteFileExtensions.string2File(testFile1, "Its a beautifull day!!!");
		WriteFileExtensions.string2File(testFile2, "Its a beautifull day!!!");
		this.actual = SimpleCompareFileExtensions.compareFilesByContent(testFile1, testFile2);
		assertTrue(this.actual);
		DeleteFileExtensions.delete(testFile2);
		DeleteFileExtensions.delete(testFile1);
	}

	/**
	 * Test method for {@link SimpleCompareFileExtensions#compareFilesByExtension(File, File)}
	 */
	@Test
	public void testCompareFilesByExtension() throws IOException
	{
		final File testFile1 = new File(this.testDir.getAbsoluteFile(),
			"testFindFilesRecursive.txt");

		final File testFile2 = new File(this.testDir.getAbsoluteFile(),
			"testFindFilesRecursive2.txt");

		WriteFileExtensions.string2File(testFile1, "Its a beautifull day!!!");
		WriteFileExtensions.string2File(testFile2, "Its a beautifull day!!!");
		this.actual = SimpleCompareFileExtensions.compareFilesByExtension(testFile1, testFile2);
		assertTrue(this.actual);
		DeleteFileExtensions.delete(testFile2);
		DeleteFileExtensions.delete(testFile1);
	}

	/**
	 * Test method for {@link SimpleCompareFileExtensions#compareFilesByLastModified(File, File)}
	 */
	@Test(enabled = true)
	public void testCompareFilesByLastModified() throws IOException
	{
		final File testFile1 = new File(this.testDir.getAbsoluteFile(),
			"testFindFilesRecursive.txt");

		final File testFile2 = new File(this.testDir.getAbsoluteFile(),
			"testFindFilesRecursive2.txt");

		WriteFileExtensions.string2File(testFile1, "Its a beautifull day!!!");
		WriteFileExtensions.string2File(testFile2, "Its a beautifull day!!!");
		final Date now = new Date(System.currentTimeMillis());
		final long tenSeconds = 1000 * 10;
		// simulate that first file is created 10 seconds before the second file...
		testFile1.setLastModified(now.getTime() - tenSeconds);
		testFile2.setLastModified(now.getTime());
		this.actual = SimpleCompareFileExtensions.compareFilesByLastModified(testFile1, testFile2);
		assertFalse(this.actual);
		DeleteFileExtensions.delete(testFile2);
		DeleteFileExtensions.delete(testFile1);
	}

	/**
	 * Test method for {@link SimpleCompareFileExtensions#compareFilesByLength(File, File)}
	 */
	@Test
	public void testCompareFilesByLength() throws IOException
	{
		final File testFile1 = new File(this.testDir.getAbsoluteFile(),
			"testFindFilesRecursive.txt");

		final File testFile2 = new File(this.testDir.getAbsoluteFile(),
			"testFindFilesRecursive2.txt");

		WriteFileExtensions.string2File(testFile1, "Its a beautifull day!!!");
		WriteFileExtensions.string2File(testFile2, "Its a beautifull day!!!");
		this.actual = SimpleCompareFileExtensions.compareFilesByLength(testFile1, testFile2);
		assertTrue(this.actual);
		DeleteFileExtensions.delete(testFile2);
		DeleteFileExtensions.delete(testFile1);
	}

	/**
	 * Test method for {@link SimpleCompareFileExtensions#compareFilesByName(File, File)}
	 */
	@Test
	public void testCompareFilesByName() throws IOException
	{
		final File testFile1 = new File(this.testDir, "testFindFilesRecursive.txt");
		final File testFile2 = new File(this.deepDir, "testFindFilesRecursive.txt");

		WriteFileExtensions.string2File(testFile1, "Its a beautifull day!!!");
		WriteFileExtensions.string2File(testFile2, "Its a beautifull day!!!");
		this.actual = SimpleCompareFileExtensions.compareFilesByName(testFile1, testFile2);
		assertTrue(this.actual);
		DeleteFileExtensions.delete(testFile2);
		DeleteFileExtensions.delete(testFile1);
	}

	/**
	 * Test method for {@link SimpleCompareFileExtensions}
	 */
	@Test(expectedExceptions = { BeanTestException.class, ObjectCreationException.class })
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(SimpleCompareFileExtensions.class);
	}

}
