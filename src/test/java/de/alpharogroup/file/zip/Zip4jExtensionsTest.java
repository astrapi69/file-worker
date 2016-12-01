package de.alpharogroup.file.zip;

import java.io.File;
import java.io.IOException;

import org.testng.AssertJUnit;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.alpharogroup.file.FileTestCase;
import de.alpharogroup.file.write.WriteFileExtensions;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;


/**
 * Test class for the class {@link Zip4jExtensions}.
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public class Zip4jExtensionsTest extends FileTestCase
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
	 * Test for {@link Zip4jExtensions#extract(ZipFile, File, String)}
	 */
	@Test(enabled = true)
	public void testExtract() throws ZipException
	{
		final File zipFile = new File(this.zipDir.getAbsoluteFile(), "Zip4j.zip");

		final ZipFile zip4jZipFile = new ZipFile(zipFile);

		final String file1 = "testZip1.txt";
		final File unzippedFile1 = new File(this.unzipDir, file1);
		final File testFile1 = new File(this.testDir.getAbsoluteFile(), file1);

		WriteFileExtensions.string2File(testFile1, "Its a beautifull day!!!");

		Zip4jExtensions.zipFiles(zip4jZipFile, testFile1);
		// Test to extract...
		Zip4jExtensions.extract(zip4jZipFile, this.unzipDir, null);

		AssertJUnit.assertTrue("File" + unzippedFile1.getName()
			+ " should be extracted.", unzippedFile1.exists());

	}

	/**
	 * Test for {@link Zip4jExtensions#zipFiles(ZipFile, File...)}
	 */
	@Test(enabled = true)
	public void testZipFilesZipFileFileArray() throws ZipException, IOException
	{

		final File zipFile = new File(this.zipDir.getAbsoluteFile(), "Zip4j.zip");

		final ZipFile zip4jZipFile = new ZipFile(zipFile);

		final String file1 = "testZip1.txt";
		final File unzippedFile1 = new File(this.unzipDir, file1);
		final File testFile1 = new File(this.testDir.getAbsoluteFile(), file1);

		final String file2 = "testZip2.tft";
		final File unzippedFile2 = new File(this.unzipDir, file2);
		final File testFile2 = new File(this.testDir.getAbsoluteFile(), file2);

		WriteFileExtensions.string2File(testFile1, "Its a beautifull day!!!");
		WriteFileExtensions.string2File(testFile2, "Its a beautifull evening!!!");

		Zip4jExtensions.zipFiles(zip4jZipFile, testFile1, testFile2);


		Zip4jExtensions.extract(zip4jZipFile, this.unzipDir, null);

		AssertJUnit.assertTrue("File" + unzippedFile1.getName()
			+ " should be extracted.", unzippedFile1.exists());
		AssertJUnit.assertTrue("File" + unzippedFile2.getName()
			+ " should be extracted.", unzippedFile2.exists());
	}

	/**
	 * Test for {@link Zip4jExtensions#zipFiles(ZipFile, int, int, File...)}
	 */
	@Test(enabled = false)
	public void testZipFilesZipFileIntIntFileArray()
	{
//		Zip4jExtensions.zipFiles(zipFile4j, compressionMethod, compressionLevel, toAdd);
	}

	/**
	 * Test for {@link Zip4jExtensions#zipFiles(ZipFile, net.lingala.zip4j.model.ZipParameters, File...)}
	 */
	@Test(enabled = false)
	public void testZipFilesZipFileZipParametersFileArray()
	{
//		Zip4jExtensions.zipFiles(zipFile4j, parameters, toAdd);
	}

}
