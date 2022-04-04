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
package io.github.astrapi69.file.zip;

import static org.testng.AssertJUnit.assertTrue;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.zip.ZipFile;

import org.meanbean.lang.Factory;
import org.meanbean.test.BeanTester;
import org.meanbean.test.Configuration;
import org.meanbean.test.ConfigurationBuilder;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.github.astrapi69.collections.list.ListFactory;
import io.github.astrapi69.file.search.FileSearchExtensions;
import io.github.astrapi69.file.write.WriteFileExtensions;
import io.github.astrapi69.io.file.namefilter.MultiplyExtensionsFilenameFilter;
import io.github.astrapi69.io.file.namefilter.SimpleFilenameFilter;
import io.github.astrapi69.meanbean.factories.FileFactory;

/**
 * The unit test class for the class Zipper.
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
	 * Test method for {@link Zipper}
	 */
	@Test(enabled = false)
	public void testWithBeanTester()
	{
		Configuration configuration = new ConfigurationBuilder()
			.overrideFactory("fileFilter", new Factory<FilenameFilter>()
			{

				@Override
				public FilenameFilter create()
				{
					return new MultiplyExtensionsFilenameFilter(ListFactory.newArrayList("txt"));
				}
			}).overrideFactory("zipFileObj", new Factory<ZipFile>()
			{

				@Override
				public ZipFile create()
				{
					ZipFile zipFile = null;
					try
					{
						zipFile = new ZipFile(
							new File(ZipperTest.this.resources.getAbsoluteFile(), "test.zip"));
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
					return zipFile;
				}
			}).overrideFactory("zipFile", new FileFactory())
			.overrideFactory("directoryToZip", new FileFactory()).build();
		final BeanTester beanTester = new BeanTester();
		beanTester.addCustomConfiguration(Zipper.class, configuration);
		beanTester.testBean(Zipper.class);
	}

	/**
	 * Test method for {@link Zipper#zip()}
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
		this.actual = length == 0;
		assertTrue("", this.actual);
		final File testFile1 = new File(this.testDir.getAbsoluteFile(), "testZip1.txt");
		final File testFile2 = new File(this.testDir.getAbsoluteFile(), "testZip2.tft");
		final File testFile3 = new File(this.testDir.getAbsoluteFile(), "testZip3.txt");

		final File testFile4 = new File(this.deepDir.getAbsoluteFile(), "testZip4.tft");
		final File testFile5 = new File(this.deepDir.getAbsoluteFile(), "testZip5.cvs");

		final File testFile6 = new File(this.deepDir2.getAbsoluteFile(), "testZip6.txt");
		final File testFile7 = new File(this.deepDir2.getAbsoluteFile(), "testZip7.cvs");

		final File testFile8 = new File(this.deeperDir.getAbsoluteFile(), "testZip8.txt");
		final File testFile9 = new File(this.deeperDir.getAbsoluteFile(), "testZip9.cvs");

		WriteFileExtensions.string2File(testFile1, "Its a beautifull day!!!");
		WriteFileExtensions.string2File(testFile2, "Its a beautifull evening!!!");
		WriteFileExtensions.string2File(testFile3, "Its a beautifull night!!!");
		WriteFileExtensions.string2File(testFile4, "Its a beautifull morning!!!");
		WriteFileExtensions.string2File(testFile5, "She's a beautifull woman!!!");
		WriteFileExtensions.string2File(testFile6, "Its a beautifull street!!!");
		WriteFileExtensions.string2File(testFile7, "He's a beautifull man!!!");
		WriteFileExtensions.string2File(testFile8, "Its a beautifull city!!!");
		WriteFileExtensions.string2File(testFile9, "He's a beautifull boy!!!");

		// ZipUtils.zip( testDir, zipFile );

		final Zipper zipper = new Zipper(this.testDir, zipFile);

		zipper.zip();

		final long currentLength = zipFile.length();
		this.actual = 0 < currentLength;
		assertTrue("", this.actual);

		this.actual = zipper.getFileCounter() == 9;
		assertTrue("", this.actual);

		this.actual = zipper.getZipFile().equals(zipFile);
		assertTrue("", this.actual);

		this.actual = FileSearchExtensions.containsFileRecursive(this.zipDir, zipFile);
		assertTrue("", this.actual);

	}

	/**
	 * Test method for {@link Zipper#zip()}
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
		this.actual = length == 0;
		assertTrue("", this.actual);
		final File testFile1 = new File(this.testDir.getAbsoluteFile(), "testZip1.txt");
		final File testFile2 = new File(this.testDir.getAbsoluteFile(), "testZip2.tft");
		final File testFile3 = new File(this.testDir.getAbsoluteFile(), "testZip3.txt");

		final File testFile4 = new File(this.deepDir.getAbsoluteFile(), "testZip4.tft");
		final File testFile5 = new File(this.deepDir.getAbsoluteFile(), "testZip5.cvs");

		final File testFile6 = new File(this.deepDir2.getAbsoluteFile(), "testZip6.txt");
		final File testFile7 = new File(this.deepDir2.getAbsoluteFile(), "testZip7.cvs");

		final File testFile8 = new File(this.deeperDir.getAbsoluteFile(), "testZip8.txt");
		final File testFile9 = new File(this.deeperDir.getAbsoluteFile(), "testZip9.cvs");

		WriteFileExtensions.string2File(testFile1, "Its a beautifull day!!!");
		WriteFileExtensions.string2File(testFile2, "Its a beautifull evening!!!");
		WriteFileExtensions.string2File(testFile3, "Its a beautifull night!!!");
		WriteFileExtensions.string2File(testFile4, "Its a beautifull morning!!!");
		WriteFileExtensions.string2File(testFile5, "She's a beautifull woman!!!");
		WriteFileExtensions.string2File(testFile6, "Its a beautifull street!!!");
		WriteFileExtensions.string2File(testFile7, "He's a beautifull man!!!");
		WriteFileExtensions.string2File(testFile8, "Its a beautifull city!!!");
		WriteFileExtensions.string2File(testFile9, "He's a beautifull boy!!!");

		final SimpleFilenameFilter txtFilenameFilter = new SimpleFilenameFilter(".txt", false);

		final Zipper zipper = Zipper.builder().directoryToZip(this.testDir).zipFile(zipFile)
			.fileFilter(txtFilenameFilter).build(); // the same as new Zipper(this.testDir, zipFile,
													// txtFilenameFilter);
		// execute the zip process ...
		zipper.zip();

		final long currentLength = zipFile.length();
		this.actual = 0 < currentLength;
		assertTrue("", this.actual);

		this.actual = zipper.getFileCounter() == 4;
		assertTrue("", this.actual);

		this.actual = zipper.getZipFile().equals(zipFile);
		assertTrue("", this.actual);

		this.actual = FileSearchExtensions.containsFileRecursive(this.zipDir, zipFile);

		assertTrue("", this.actual);

	}

}
