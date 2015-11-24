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
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.testng.AssertJUnit;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.alpharogroup.file.search.FileSearchUtils;
import de.alpharogroup.file.write.WriteFileUtils;

/**
 * Test class for the class Unzipper.
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public class UnzipperTest extends ZipTestCase
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
	 * @throws Exception
	 *             the exception
	 *             {@link de.alpharogroup.file.zip.Unzipper#extractZipEntry(java.util.zip.ZipFile, java.util.zip.ZipEntry, java.io.File)}
	 *             .
	 */
	@Test(enabled = true)
	public void testExtractZipEntry() throws Exception
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

		final String file8 = "testZip8.txt";
		final File testFile8 = new File(this.deeperDir.getAbsoluteFile(), file8);
		final File unzippedFile8 = new File(this.unzipDirDeeperDir, file8);
		final String file9 = "testZip9.cvs";
		final File testFile9 = new File(this.deeperDir.getAbsoluteFile(), file9);
		final File unzippedFile9 = new File(this.unzipDirDeeperDir, file9);

		WriteFileUtils.string2File(testFile1, "Its a beautifull day!!!");
		WriteFileUtils.string2File(testFile2, "Its a beautifull evening!!!");
		WriteFileUtils.string2File(testFile3, "Its a beautifull night!!!");
		WriteFileUtils.string2File(testFile4, "Its a beautifull morning!!!");
		WriteFileUtils.string2File(testFile5, "She's a beautifull woman!!!");
		WriteFileUtils.string2File(testFile6, "Its a beautifull street!!!");
		WriteFileUtils.string2File(testFile7, "He's a beautifull man!!!");
		WriteFileUtils.string2File(testFile8, "Its a beautifull city!!!");
		WriteFileUtils.string2File(testFile9, "He's a beautifull boy!!!");

		ZipUtils.zip(this.testDir, zipFile);

		final long currentLength = zipFile.length();
		this.result = 0 < currentLength;
		AssertJUnit.assertTrue("", this.result);

		final ZipEntry zipEntry = new ZipEntry("testDir" + File.separator + "deepDir"
			+ File.separator + "deeperDir" + File.separator + "testZip8.txt");
		if (!this.unzipDirTestDir.exists())
		{
			this.unzipDirTestDir.mkdir();
		}

		ZipFile zf = new ZipFile(zipFile);
		final Unzipper unzipper = new Unzipper(zf, this.unzipDir);
		unzipper.extractZipEntry(zf, zipEntry, this.unzipDir);
		zf.close();
		this.result = FileSearchUtils.containsFileRecursive(this.unzipDir, unzippedFile8);
		AssertJUnit.assertTrue("", this.result);

		final ZipEntry zipEntry2 = new ZipEntry("testDir" + File.separator + "deepDir"
			+ File.separator + "deeperDir" + File.separator + "testZip9.cvs");
		zf = new ZipFile(zipFile);
		unzipper.extractZipEntry(zf, zipEntry2, this.unzipDir);
		zf.close();
		this.result = FileSearchUtils.containsFileRecursive(this.unzipDir, unzippedFile9);
		AssertJUnit.assertTrue("", this.result);

	}

	/**
	 * Test method for {@link de.alpharogroup.file.zip.Unzipper#unzip()}.
	 *
	 * @throws ZipException
	 *             the zip exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testUnzip() throws ZipException, IOException
	{

		final File zipFile = new File(this.zipDir.getAbsoluteFile(), "testZip.zip");
		if (!zipFile.exists())
		{
			zipFile.createNewFile();
		}

		final long length = zipFile.length();
		this.result = length == 0;
		AssertJUnit.assertTrue("", this.result);
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

		WriteFileUtils.string2File(testFile1, "Its a beautifull day!!!");
		WriteFileUtils.string2File(testFile2, "Its a beautifull evening!!!");
		WriteFileUtils.string2File(testFile3, "Its a beautifull night!!!");
		WriteFileUtils.string2File(testFile4, "Its a beautifull morning!!!");
		WriteFileUtils.string2File(testFile5, "She's a beautifull woman!!!");
		WriteFileUtils.string2File(testFile6, "Its a beautifull street!!!");
		WriteFileUtils.string2File(testFile7, "He's a beautifull man!!!");
		WriteFileUtils.string2File(testFile8, "Its a beautifull city!!!");
		WriteFileUtils.string2File(testFile9, "He's a beautifull boy!!!");

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
		final ZipFile zf = new ZipFile(zipFile);
		final Unzipper unzipper = new Unzipper(zf, this.unzipDir);
		unzipper.unzip();

		this.result = FileSearchUtils.containsFileRecursive(this.unzipDir, unzippedFile1);
		AssertJUnit.assertTrue("", this.result);

		this.result = FileSearchUtils.containsFileRecursive(this.unzipDir, unzippedFile2);
		AssertJUnit.assertTrue("", this.result);

		this.result = FileSearchUtils.containsFileRecursive(this.unzipDir, unzippedFile3);
		AssertJUnit.assertTrue("", this.result);

		this.result = FileSearchUtils.containsFileRecursive(this.unzipDir, unzippedFile4);
		AssertJUnit.assertTrue("", this.result);

		this.result = FileSearchUtils.containsFileRecursive(this.unzipDir, unzippedFile5);
		AssertJUnit.assertTrue("", this.result);

		this.result = FileSearchUtils.containsFileRecursive(this.unzipDir, unzippedFile6);
		AssertJUnit.assertTrue("", this.result);

		this.result = FileSearchUtils.containsFileRecursive(this.unzipDir, unzippedFile7);
		AssertJUnit.assertTrue("", this.result);

		this.result = FileSearchUtils.containsFileRecursive(this.unzipDir, unzippedFile8);
		AssertJUnit.assertTrue("", this.result);

		this.result = FileSearchUtils.containsFileRecursive(this.unzipDir, unzippedFile9);
		AssertJUnit.assertTrue("", this.result);

	}

	/**
	 * Test unzip with password.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test(enabled = false)
	public void testUnzipWithPassword() throws IOException
	{
		// password-protected zip file I need to read
		final File zipFileWithPassword = new File(this.testResources, "autotextWithPassword.zip");
		final String password = "Hallo";
		final Unzipper unzipper = new Unzipper();
		unzipper.unzip(zipFileWithPassword, this.unzipDir, password, Charset.forName("UTF-8"));
		final String encryptedFilename = "autotext";
		final File encryptedFile = new File(this.unzipDir, encryptedFilename);
		this.result = FileSearchUtils.containsFileRecursive(this.unzipDir, encryptedFile);
		AssertJUnit.assertTrue("", this.result);
	}

	/**
	 * Test method for.
	 *
	 * @throws ZipException
	 *             the zip exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 *             {@link de.alpharogroup.file.zip.Unzipper#unzip(java.util.zip.ZipFile, java.io.File)}
	 *             .
	 */
	@Test
	public void testUnzipZipFileFile() throws ZipException, IOException
	{

		final File zipFile = new File(this.zipDir.getAbsoluteFile(), "testZip.zip");
		if (!zipFile.exists())
		{
			zipFile.createNewFile();
		}

		final long length = zipFile.length();
		this.result = length == 0;
		AssertJUnit.assertTrue("", this.result);
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

		WriteFileUtils.string2File(testFile1, "Its a beautifull day!!!");
		WriteFileUtils.string2File(testFile2, "Its a beautifull evening!!!");
		WriteFileUtils.string2File(testFile3, "Its a beautifull night!!!");
		WriteFileUtils.string2File(testFile4, "Its a beautifull morning!!!");
		WriteFileUtils.string2File(testFile5, "She's a beautifull woman!!!");
		WriteFileUtils.string2File(testFile6, "Its a beautifull street!!!");
		WriteFileUtils.string2File(testFile7, "He's a beautifull man!!!");
		WriteFileUtils.string2File(testFile8, "Its a beautifull city!!!");
		WriteFileUtils.string2File(testFile9, "He's a beautifull boy!!!");

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

		final Unzipper unzipper = new Unzipper();
		final ZipFile zf = new ZipFile(zipFile);
		unzipper.unzip(zf, this.unzipDir);

		this.result = FileSearchUtils.containsFileRecursive(this.unzipDir, unzippedFile1);
		AssertJUnit.assertTrue("", this.result);

		this.result = FileSearchUtils.containsFileRecursive(this.unzipDir, unzippedFile2);
		AssertJUnit.assertTrue("", this.result);

		this.result = FileSearchUtils.containsFileRecursive(this.unzipDir, unzippedFile3);
		AssertJUnit.assertTrue("", this.result);

		this.result = FileSearchUtils.containsFileRecursive(this.unzipDir, unzippedFile4);
		AssertJUnit.assertTrue("", this.result);

		this.result = FileSearchUtils.containsFileRecursive(this.unzipDir, unzippedFile5);
		AssertJUnit.assertTrue("", this.result);

		this.result = FileSearchUtils.containsFileRecursive(this.unzipDir, unzippedFile6);
		AssertJUnit.assertTrue("", this.result);

		this.result = FileSearchUtils.containsFileRecursive(this.unzipDir, unzippedFile7);
		AssertJUnit.assertTrue("", this.result);

		this.result = FileSearchUtils.containsFileRecursive(this.unzipDir, unzippedFile8);
		AssertJUnit.assertTrue("", this.result);

		this.result = FileSearchUtils.containsFileRecursive(this.unzipDir, unzippedFile9);
		AssertJUnit.assertTrue("", this.result);

	}

}
