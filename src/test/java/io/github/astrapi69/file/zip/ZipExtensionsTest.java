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

import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.meanbean.test.BeanTester;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.github.astrapi69.file.create.DirectoryFactory;
import io.github.astrapi69.file.create.FileFactory;
import io.github.astrapi69.file.search.FileSearchExtensions;
import io.github.astrapi69.file.write.StoreFileExtensions;
import io.github.astrapi69.io.file.FileConstants;
import io.github.astrapi69.io.file.filter.SuffixFileFilter;
import io.github.astrapi69.io.file.namefilter.SimpleFilenameFilter;

/**
 * The unit test class for the class {@link ZipExtensions}.
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public class ZipExtensionsTest extends ZipTestCase
{


	/**
	 * {@inheritDoc}
	 */
	@Override
	@BeforeMethod
	protected void setUp() throws Exception
	{
		super.setUp();
		this.unzipDirTestDir = new File(this.unzipDir, "testDir");
		this.unzipDirDeepDir = new File(this.unzipDirTestDir, "deepDir");
		this.unzipDirDeepDir2 = new File(this.unzipDirTestDir, "deepDir2");
		this.unzipDirDeeperDir = new File(this.unzipDirDeepDir, "deeperDir");
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
	 * Test method for {@link ZipExtensions#extractZipEntry(ZipFile, ZipEntry, File)}
	 *
	 * @throws Exception
	 *             catch all exception and throw
	 */
	@Test
	public void testExtractZipEntry() throws Exception
	{

		final File zipFile = new File(this.zipDir.getAbsoluteFile(), "testZip.zip");

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

		final String file8 = "testZip8.txt";
		final File testFile8 = new File(this.deeperDir.getAbsoluteFile(), file8);
		final File unzippedFile8 = new File(this.unzipDirDeeperDir, file8);
		final String file9 = "testZip9.cvs";
		final File testFile9 = new File(this.deeperDir.getAbsoluteFile(), file9);
		final File unzippedFile9 = new File(this.unzipDirDeeperDir, file9);

		StoreFileExtensions.toFile(testFile1, "Its a beautifull day!!!");
		StoreFileExtensions.toFile(testFile2, "Its a beautifull evening!!!");
		StoreFileExtensions.toFile(testFile3, "Its a beautifull night!!!");
		StoreFileExtensions.toFile(testFile4, "Its a beautifull morning!!!");
		StoreFileExtensions.toFile(testFile5, "She's a beautifull woman!!!");
		StoreFileExtensions.toFile(testFile6, "Its a beautifull street!!!");
		StoreFileExtensions.toFile(testFile7, "He's a beautifull man!!!");
		StoreFileExtensions.toFile(testFile8, "Its a beautifull city!!!");
		StoreFileExtensions.toFile(testFile9, "He's a beautifull boy!!!");

		ZipExtensions.zip(this.testDir, zipFile);

		final long currentLength = zipFile.length();
		this.actual = 0 < currentLength;
		assertTrue("", this.actual);

		final ZipEntry zipEntry = new ZipEntry("testDir" + File.separator + "deepDir"
			+ File.separator + "deeperDir" + File.separator + file8);
		if (!this.unzipDirTestDir.exists())
		{
			DirectoryFactory.newDirectory(this.unzipDirTestDir);
		}
		ZipFile zf = new ZipFile(zipFile);
		ZipExtensions.extractZipEntry(zf, zipEntry, this.unzipDir);
		zf.close();
		this.actual = FileSearchExtensions.containsFileRecursive(this.unzipDir, unzippedFile8);
		assertTrue("", this.actual);

		final ZipEntry zipEntry2 = new ZipEntry("testDir" + File.separator + "deepDir"
			+ File.separator + "deeperDir" + File.separator + file9);
		zf = new ZipFile(zipFile);
		ZipExtensions.extractZipEntry(zf, zipEntry2, this.unzipDir);
		zf.close();
		this.actual = FileSearchExtensions.containsFileRecursive(this.unzipDir, unzippedFile9);
		assertTrue("", this.actual);

	}

	/**
	 * Test method for {@link ZipExtensions#isZip(String)}
	 */
	@Test
	public void testIsZip()
	{
		final int length = FileConstants.ZIP_EXTENSIONS.length;
		for (int i = 0; i < length; i++)
		{
			final File testIsZip = new File(this.testResources,
				"testIsZip" + FileConstants.ZIP_EXTENSIONS[i]);
			this.actual = ZipExtensions.isZip(testIsZip.getName());
			assertTrue("The file " + testIsZip.getName() + " should be a zipfile.", this.actual);
		}
		this.actual = ZipExtensions.isZip(this.testResources.getName());
		assertFalse("The file " + this.testResources.getName() + " should not be a zipfile.",
			this.actual);
	}

	/**
	 * Test method for.
	 *
	 * @throws Exception
	 *             the exception {@link ZipExtensions#unzip(ZipFile, File)} .
	 */
	@Test
	public void testUnzip() throws Exception
	{

		final File zipFile = new File(this.zipDir.getAbsoluteFile(), "testZip.zip");

		final long length = zipFile.length();
		this.actual = length == 0;
		assertTrue("", this.actual);
		final String file1 = "testZip1.txt";
		final File testFile1 = new File(this.testDir.getAbsoluteFile(), file1);
		final File unzippedFile1 = new File(this.unzipDirTestDir, file1);
		final String file2 = "testZip2.tft";
		final File testFile2 = new File(this.testDir.getAbsoluteFile(), file2);
		final File unzippedFile2 = new File(this.unzipDirTestDir, file2);
		final String file3 = "testZip3.txt";
		final File testFile3 = new File(this.testDir.getAbsoluteFile(), file3);
		final File unzippedFile3 = new File(this.unzipDirTestDir, file3);
		final String file4 = "testZip4.tft";
		final File testFile4 = new File(this.deepDir.getAbsoluteFile(), file4);
		final File unzippedFile4 = new File(this.unzipDirDeepDir, file4);
		final String file5 = "testZip5.cvs";
		final File testFile5 = new File(this.deepDir.getAbsoluteFile(), file5);
		final File unzippedFile5 = new File(this.unzipDirDeepDir, file5);
		final String file6 = "testZip6.txt";
		final File testFile6 = new File(this.deepDir2.getAbsoluteFile(), file6);
		final File unzippedFile6 = new File(this.unzipDirDeepDir2, file6);
		final String file7 = "testZip7.cvs";
		final File testFile7 = new File(this.deepDir2.getAbsoluteFile(), file7);
		final File unzippedFile7 = new File(this.unzipDirDeepDir2, file7);
		final String file8 = "testZip8.txt";
		final File testFile8 = new File(this.deeperDir.getAbsoluteFile(), file8);
		final File unzippedFile8 = new File(this.unzipDirDeeperDir, file8);
		final String file9 = "testZip9.cvs";
		final File testFile9 = new File(this.deeperDir.getAbsoluteFile(), file9);
		final File unzippedFile9 = new File(this.unzipDirDeeperDir, file9);

		StoreFileExtensions.toFile(testFile1, "Its a beautifull day!!!");
		StoreFileExtensions.toFile(testFile2, "Its a beautifull evening!!!");
		StoreFileExtensions.toFile(testFile3, "Its a beautifull night!!!");
		StoreFileExtensions.toFile(testFile4, "Its a beautifull morning!!!");
		StoreFileExtensions.toFile(testFile5, "She's a beautifull woman!!!");
		StoreFileExtensions.toFile(testFile6, "Its a beautifull street!!!");
		StoreFileExtensions.toFile(testFile7, "He's a beautifull man!!!");
		StoreFileExtensions.toFile(testFile8, "Its a beautifull city!!!");
		StoreFileExtensions.toFile(testFile9, "He's a beautifull boy!!!");

		ZipExtensions.zip(this.testDir, zipFile);

		final long currentLength = zipFile.length();
		this.actual = 0 < currentLength;
		assertTrue("", this.actual);

		this.actual = FileSearchExtensions.containsFileRecursive(this.zipDir, zipFile);

		assertTrue("", this.actual);

		ZipExtensions.unzip(new ZipFile(zipFile), this.unzipDir);

		this.actual = FileSearchExtensions.containsFileRecursive(this.unzipDir, unzippedFile1);
		assertTrue("", this.actual);

		this.actual = FileSearchExtensions.containsFileRecursive(this.unzipDir, unzippedFile2);
		assertTrue("", this.actual);

		this.actual = FileSearchExtensions.containsFileRecursive(this.unzipDir, unzippedFile3);
		assertTrue("", this.actual);

		this.actual = FileSearchExtensions.containsFileRecursive(this.unzipDir, unzippedFile4);
		assertTrue("", this.actual);

		this.actual = FileSearchExtensions.containsFileRecursive(this.unzipDir, unzippedFile5);
		assertTrue("", this.actual);

		this.actual = FileSearchExtensions.containsFileRecursive(this.unzipDir, unzippedFile6);
		assertTrue("", this.actual);

		this.actual = FileSearchExtensions.containsFileRecursive(this.unzipDir, unzippedFile7);
		assertTrue("", this.actual);

		this.actual = FileSearchExtensions.containsFileRecursive(this.unzipDir, unzippedFile8);
		assertTrue("", this.actual);

		this.actual = FileSearchExtensions.containsFileRecursive(this.unzipDir, unzippedFile9);
		assertTrue("", this.actual);
	}

	/**
	 * Test method for {@link ZipExtensions}
	 */
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(ZipExtensions.class);
	}

	@Test
	public void testZipFiles() throws IOException
	{
		String suffix;
		Set<File> files;

		suffix = ".txt";
		final File zipFile = new File(this.zipDir.getAbsoluteFile(), "testZip.zip");

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

		StoreFileExtensions.toFile(testFile1, "Its a beautifull day!!!");
		StoreFileExtensions.toFile(testFile2, "Its a beautifull evening!!!");
		StoreFileExtensions.toFile(testFile3, "Its a beautifull night!!!");
		StoreFileExtensions.toFile(testFile4, "Its a beautifull morning!!!");
		StoreFileExtensions.toFile(testFile5, "She's a beautifull woman!!!");
		StoreFileExtensions.toFile(testFile6, "Its a beautifull street!!!");
		StoreFileExtensions.toFile(testFile7, "He's a beautifull man!!!");
		StoreFileExtensions.toFile(testFile8, "Its a beautifull city!!!");
		StoreFileExtensions.toFile(testFile9, "He's a beautifull boy!!!");
		ZipExtensions.zipFiles(this.testDir, zipFile, SuffixFileFilter.of(suffix));
		System.out.println(zipFile);
	}

	/**
	 * Test method for.
	 *
	 * @throws Exception
	 *             the exception {@link ZipExtensions#zip(File, File)}.
	 */
	@Test
	public void testZipFileFile() throws Exception
	{

		final File zipFile = new File(this.zipDir.getAbsoluteFile(), "testZip.zip");

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

		StoreFileExtensions.toFile(testFile1, "Its a beautifull day!!!");
		StoreFileExtensions.toFile(testFile2, "Its a beautifull evening!!!");
		StoreFileExtensions.toFile(testFile3, "Its a beautifull night!!!");
		StoreFileExtensions.toFile(testFile4, "Its a beautifull morning!!!");
		StoreFileExtensions.toFile(testFile5, "She's a beautifull woman!!!");
		StoreFileExtensions.toFile(testFile6, "Its a beautifull street!!!");
		StoreFileExtensions.toFile(testFile7, "He's a beautifull man!!!");
		StoreFileExtensions.toFile(testFile8, "Its a beautifull city!!!");
		StoreFileExtensions.toFile(testFile9, "He's a beautifull boy!!!");

		ZipExtensions.zip(this.testDir, zipFile);

		final long currentLength = zipFile.length();
		this.actual = 0 < currentLength;
		assertTrue("", this.actual);

		this.actual = FileSearchExtensions.containsFileRecursive(this.zipDir, zipFile);

		assertTrue("", this.actual);

	}

	/**
	 * Test method for.
	 *
	 * @throws Exception
	 *             the exception {@link ZipExtensions#zip(File, File, FilenameFilter)} .
	 */
	@Test
	public void testZipFileFileFilenameFilter() throws Exception
	{

		final File zipFile = new File(this.zipDir.getAbsoluteFile(), "testZip.zip");
		if (!zipFile.exists())
		{
			FileFactory.newFile(zipFile);
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

		StoreFileExtensions.toFile(testFile1, "Its a beautifull day!!!");
		StoreFileExtensions.toFile(testFile2, "Its a beautifull evening!!!");
		StoreFileExtensions.toFile(testFile3, "Its a beautifull night!!!");
		StoreFileExtensions.toFile(testFile4, "Its a beautifull morning!!!");
		StoreFileExtensions.toFile(testFile5, "She's a beautifull woman!!!");
		StoreFileExtensions.toFile(testFile6, "Its a beautifull street!!!");
		StoreFileExtensions.toFile(testFile7, "He's a beautifull man!!!");
		StoreFileExtensions.toFile(testFile8, "Its a beautifull city!!!");
		StoreFileExtensions.toFile(testFile9, "He's a beautifull boy!!!");

		final SimpleFilenameFilter txtFilenameFilter = new SimpleFilenameFilter(".txt", false);

		ZipExtensions.zip(this.testDir, zipFile, txtFilenameFilter);

		final long currentLength = zipFile.length();
		this.actual = 0 < currentLength;
		assertTrue("", this.actual);

		// result = FileUtils.containsFileRecursive( zipDir, zipFile );

		assertTrue("", this.actual);

	}

}
