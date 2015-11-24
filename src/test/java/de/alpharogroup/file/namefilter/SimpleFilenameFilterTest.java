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
package de.alpharogroup.file.namefilter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.testng.AssertJUnit;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.alpharogroup.file.FileTestCase;
import de.alpharogroup.file.write.WriteFileUtils;

/**
 * Test class for the class SimpleFilenameFilter.
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public class SimpleFilenameFilterTest extends FileTestCase
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
	 * Test method for.
	 *
	 * {@link de.alpharogroup.file.namefilter.SimpleFilenameFilter#accept(java.io.File, java.lang.String)}
	 * .
	 */
	@Test
	public void testAccept()
	{
		final List<File> expectedfiles = new ArrayList<>();
		final String file1 = "testZip1.txt";
		final File testFile1 = new File(this.testDir.getAbsoluteFile(), file1);
		expectedfiles.add(testFile1);
		final String file2 = "testZip2.tft";
		final File testFile2 = new File(this.testDir.getAbsoluteFile(), file2);
		final String file3 = "testZip3.txt";
		final File testFile3 = new File(this.testDir.getAbsoluteFile(), file3);
		expectedfiles.add(testFile3);

		WriteFileUtils.string2File(testFile1, "Its a beautifull day!!!");
		WriteFileUtils.string2File(testFile2, "Its a beautifull evening!!!");
		WriteFileUtils.string2File(testFile3, "Its a beautifull night!!!");
		final SimpleFilenameFilter txtFilenameFilter = new SimpleFilenameFilter(".txt", false);
		final File[] txtFiles = this.testDir.listFiles(txtFilenameFilter);
		this.result = txtFiles.length == 2;
		AssertJUnit.assertTrue("", this.result);
		for (final File txtFile : txtFiles)
		{
			this.result = expectedfiles.contains(txtFile);
			AssertJUnit.assertTrue("", this.result);
		}

	}

}
