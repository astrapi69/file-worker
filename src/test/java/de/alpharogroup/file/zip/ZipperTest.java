/**
 * The MIT License
 *
 * Copyright (C) 2007 Asterios Raptis
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
package de.alpharogroup.file.zip;

import java.io.File;
import java.io.IOException;

import org.testng.AssertJUnit;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.alpharogroup.file.namefilter.SimpleFilenameFilter;
import de.alpharogroup.file.search.FileSearchUtils;
import de.alpharogroup.file.write.WriteFileUtils;

/**
 * Test class for the class Zipper.
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public class ZipperTest extends ZipTestCase
{

	/**
	 * {@inheritDoc}
	 */
	@Override
	@BeforeMethod
	protected void setUp() throws Exception
	{
		super.setUp();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@AfterMethod
	protected void tearDown() throws Exception
	{
		super.tearDown();
	}

	/**
	 * Test method for {@link de.alpharogroup.file.zip.Zipper#zip()}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testZip() throws IOException
	{

		final File zipFile = new File(this.zipDir.getAbsoluteFile(), "testZip.zip");
		if (!zipFile.exists())
		{
			zipFile.createNewFile();
		}
		final long length = zipFile.length();
		this.result = length == 0;
		AssertJUnit.assertTrue("", this.result);
		final File testFile1 = new File(this.testDir.getAbsoluteFile(), "testZip1.txt");
		final File testFile2 = new File(this.testDir.getAbsoluteFile(), "testZip2.tft");
		final File testFile3 = new File(this.testDir.getAbsoluteFile(), "testZip3.txt");

		final File testFile4 = new File(this.deepDir.getAbsoluteFile(), "testZip4.tft");
		final File testFile5 = new File(this.deepDir.getAbsoluteFile(), "testZip5.cvs");

		final File testFile6 = new File(this.deepDir2.getAbsoluteFile(), "testZip6.txt");
		final File testFile7 = new File(this.deepDir2.getAbsoluteFile(), "testZip7.cvs");

		final File testFile8 = new File(this.deeperDir.getAbsoluteFile(), "testZip8.txt");
		final File testFile9 = new File(this.deeperDir.getAbsoluteFile(), "testZip9.cvs");

		WriteFileUtils.string2File(testFile1, "Its a beautifull day!!!");
		WriteFileUtils.string2File(testFile2, "Its a beautifull evening!!!");
		WriteFileUtils.string2File(testFile3, "Its a beautifull night!!!");
		WriteFileUtils.string2File(testFile4, "Its a beautifull morning!!!");
		WriteFileUtils.string2File(testFile5, "She's a beautifull woman!!!");
		WriteFileUtils.string2File(testFile6, "Its a beautifull street!!!");
		WriteFileUtils.string2File(testFile7, "He's a beautifull man!!!");
		WriteFileUtils.string2File(testFile8, "Its a beautifull city!!!");
		WriteFileUtils.string2File(testFile9, "He's a beautifull boy!!!");

		// ZipUtils.zip( testDir, zipFile );

		final Zipper zipper = new Zipper(this.testDir, zipFile);

		zipper.zip();

		final long currentLength = zipFile.length();
		this.result = 0 < currentLength;
		AssertJUnit.assertTrue("", this.result);

		this.result = zipper.getFileCounter() == 9;
		AssertJUnit.assertTrue("", this.result);

		this.result = zipper.getZipFile().equals(zipFile);
		AssertJUnit.assertTrue("", this.result);

		this.result = FileSearchUtils.containsFileRecursive(this.zipDir, zipFile);
		AssertJUnit.assertTrue("", this.result);

	}

	/**
	 * Test method for {@link de.alpharogroup.file.zip.Zipper#zip()}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testZipWithFilter() throws IOException
	{

		final File zipFile = new File(this.zipDir.getAbsoluteFile(), "testZip.zip");
		if (!zipFile.exists())
		{
			zipFile.createNewFile();
		}
		final long length = zipFile.length();
		this.result = length == 0;
		AssertJUnit.assertTrue("", this.result);
		final File testFile1 = new File(this.testDir.getAbsoluteFile(), "testZip1.txt");
		final File testFile2 = new File(this.testDir.getAbsoluteFile(), "testZip2.tft");
		final File testFile3 = new File(this.testDir.getAbsoluteFile(), "testZip3.txt");

		final File testFile4 = new File(this.deepDir.getAbsoluteFile(), "testZip4.tft");
		final File testFile5 = new File(this.deepDir.getAbsoluteFile(), "testZip5.cvs");

		final File testFile6 = new File(this.deepDir2.getAbsoluteFile(), "testZip6.txt");
		final File testFile7 = new File(this.deepDir2.getAbsoluteFile(), "testZip7.cvs");

		final File testFile8 = new File(this.deeperDir.getAbsoluteFile(), "testZip8.txt");
		final File testFile9 = new File(this.deeperDir.getAbsoluteFile(), "testZip9.cvs");

		WriteFileUtils.string2File(testFile1, "Its a beautifull day!!!");
		WriteFileUtils.string2File(testFile2, "Its a beautifull evening!!!");
		WriteFileUtils.string2File(testFile3, "Its a beautifull night!!!");
		WriteFileUtils.string2File(testFile4, "Its a beautifull morning!!!");
		WriteFileUtils.string2File(testFile5, "She's a beautifull woman!!!");
		WriteFileUtils.string2File(testFile6, "Its a beautifull street!!!");
		WriteFileUtils.string2File(testFile7, "He's a beautifull man!!!");
		WriteFileUtils.string2File(testFile8, "Its a beautifull city!!!");
		WriteFileUtils.string2File(testFile9, "He's a beautifull boy!!!");

		// ZipUtils.zip( testDir, zipFile );

		final SimpleFilenameFilter txtFilenameFilter = new SimpleFilenameFilter(".txt", false);

		final Zipper zipper = new Zipper(this.testDir, zipFile, txtFilenameFilter);

		zipper.zip();

		final long currentLength = zipFile.length();
		this.result = 0 < currentLength;
		AssertJUnit.assertTrue("", this.result);

		this.result = zipper.getFileCounter() == 4;
		AssertJUnit.assertTrue("", this.result);

		this.result = zipper.getZipFile().equals(zipFile);
		AssertJUnit.assertTrue("", this.result);

		this.result = FileSearchUtils.containsFileRecursive(this.zipDir, zipFile);

		AssertJUnit.assertTrue("", this.result);

	}

}
