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
package de.alpharogroup.file.write;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.meanbean.factories.ObjectCreationException;
import org.meanbean.test.BeanTestException;
import org.meanbean.test.BeanTester;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.alpharogroup.file.FileExtensions;
import de.alpharogroup.file.FileTestCase;
import de.alpharogroup.file.checksum.ChecksumExtensions;
import de.alpharogroup.file.read.ReadFileExtensions;
import de.alpharogroup.file.search.PathFinder;
import de.alpharogroup.io.StreamExtensions;

/**
 * The unit test class for the class {@link WriteFileExtensions}
 */
public class WriteFileExtensionsTest extends FileTestCase
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
	 * Test method for {@link WriteFileExtensions#readSourceFileAndWriteDestFile(String, String)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testReadSourceFileAndWriteDestFile() throws IOException
	{
		final File source = new File(this.testDir.getAbsoluteFile(),
			"testReadSourceFileAndWriteDestFileInput.txt");
		final File destination = new File(this.testDir.getAbsoluteFile(),
			"testReadSourceFileAndWriteDestFileOutput.tft");

		WriteFileExtensions.string2File(source, "Its a beautifull day!!!");
		WriteFileExtensions.string2File(destination, "");
		try
		{
			WriteFileExtensions.readSourceFileAndWriteDestFile(source.getAbsolutePath(),
				destination.getAbsolutePath());
		}
		catch (final Exception e)
		{
			this.actual = e instanceof FileNotFoundException;
			assertTrue("Exception should be of type FileNotFoundException.", this.actual);
		}

		final String inputString = "Its a beautifull day!!!";
		final String expected = inputString;
		WriteFileExtensions.string2File(source, inputString);

		WriteFileExtensions.readSourceFileAndWriteDestFile(source.getAbsolutePath(),
			destination.getAbsolutePath());

		final String compare = ReadFileExtensions.readFromFile(destination);
		this.actual = expected.equals(compare);
		assertTrue("", this.actual);
	}

	/**
	 * Test method for {@link WriteFileExtensions#storeByteArrayToFile(byte[], File)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testStoreByteArrayToFile() throws IOException
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
	 * Test method for {@link WriteFileExtensions#string2File(File, String)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testString2FileFileString() throws IOException
	{
		final File testFile1 = new File(this.testDir, "testString2FileFileString.txt");
		final String inputString = "Its a beautifull day!!!";
		WriteFileExtensions.string2File(testFile1, inputString);
		// --------------------------------

		final String content = ReadFileExtensions.readFromFile(testFile1);
		this.actual = inputString.equals(content);
		assertTrue("", this.actual);
	}

	/**
	 * Test method for {@link WriteFileExtensions#string2File(File, String, String)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testString2FileFileStringString() throws IOException
	{
		final File testFile1 = new File(this.testDir, "testString2FileFileStringString.txt");
		final String inputString = "Its a beautifull day!!!";
		WriteFileExtensions.string2File(testFile1, inputString, "UTF-8");
		// --------------------------------

		final String content = ReadFileExtensions.readFromFile(testFile1);
		this.actual = inputString.equals(content);
		assertTrue("", this.actual);
	}

	/**
	 * Test method for {@link WriteFileExtensions#string2File(String, String)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testString2FileStringString() throws IOException
	{
		final File testFile1 = new File(this.testDir, "testString2FileFileString.txt");
		final String inputString = "Its a beautifull day!!!";
		WriteFileExtensions.string2File(inputString, testFile1.getAbsolutePath());
		// --------------------------------

		final String content = ReadFileExtensions.readFromFile(testFile1);
		this.actual = inputString.equals(content);
		assertTrue("", this.actual);
	}

	/**
	 * Test method for {@link WriteFileExtensions}
	 */
	@Test(expectedExceptions = { BeanTestException.class, ObjectCreationException.class })
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(WriteFileExtensions.class);
	}

	/**
	 * Test method for {@link WriteFileExtensions#write(InputStream, OutputStream)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testWrite() throws IOException
	{
		long actual;
		long expected;

		final File testFile = new File(PathFinder.getSrcTestResourcesDir(),
			"testReadFileInput.txt");
		File fileout = new File(PathFinder.getSrcTestResourcesDir(),
			"testWriteFileExtensionsWrite.out");
		try (InputStream inputStream = StreamExtensions.getInputStream(testFile, true);
			OutputStream outputStream = StreamExtensions.getOutputStream(fileout, true);)
		{
			WriteFileExtensions.write(inputStream, outputStream);
		}

		actual = ChecksumExtensions.getCheckSumAdler32(testFile);
		expected = ChecksumExtensions.getCheckSumAdler32(fileout);
		assertEquals(expected, actual);
		fileout.deleteOnExit();
	}

	/**
	 * Test method for {@link WriteFileExtensions#write2File(Reader, Writer)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testWrite2FileReaderWriter() throws IOException
	{
		final File inputFile = new File(this.testDir, "testWrite2FileReaderWriterBoolean.inp");
		inputFile.createNewFile();
		final File outputFile = new File(this.testDir, "testWrite2FileReaderWriterBoolean.outp");
		outputFile.createNewFile();
		final String inputString = "Its a beautifull day!!!";
		WriteFileExtensions.string2File(inputFile, inputString);
		// --------------------------------
		try (final Reader reader = StreamExtensions.getReader(inputFile);
			final Writer writer = StreamExtensions.getWriter(outputFile);)
		{
			WriteFileExtensions.write2File(reader, writer);
		}

		final String content = ReadFileExtensions.readFromFile(outputFile);
		this.actual = inputString.equals(content);
		assertTrue("", this.actual);
	}

	/**
	 * Test method for {@link WriteFileExtensions#write2File(Reader, Writer, boolean)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@SuppressWarnings("deprecation")
	@Test
	public void testWrite2FileReaderWriterBoolean() throws IOException
	{
		final File inputFile = new File(this.testDir, "testWrite2FileReaderWriterBoolean.inp");
		inputFile.createNewFile();
		final File outputFile = new File(this.testDir, "testWrite2FileReaderWriterBoolean.outp");
		outputFile.createNewFile();
		final String inputString = "Its a beautifull day!!!";
		WriteFileExtensions.string2File(inputFile, inputString);
		// --------------------------------
		try (final Reader reader = StreamExtensions.getReader(inputFile);
			final Writer writer = StreamExtensions.getWriter(outputFile);)
		{
			WriteFileExtensions.write2File(reader, writer, false);
		}

		final String content = ReadFileExtensions.readFromFile(outputFile);
		this.actual = inputString.equals(content);
		assertTrue("", this.actual);
	}

	/**
	 * Test method for {@link WriteFileExtensions#write2File(String, String)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testWrite2FileStringString() throws IOException
	{
		boolean created;
		final File inputFile = new File(this.testDir, "testWrite2FileStringString.inp");
		created = inputFile.createNewFile();
		if (!created)
		{
			Assert.fail("Fail to create inputFile.");
		}
		final File outputFile = new File(this.testDir, "testWrite2FileStringString.outp");
		outputFile.createNewFile();
		final String inputString = "Its a beautifull day!!!";
		WriteFileExtensions.string2File(inputFile, inputString);
		// --------------------------------
		WriteFileExtensions.write2File(inputFile.getAbsolutePath(), outputFile.getAbsolutePath());

		final String content = ReadFileExtensions.readFromFile(outputFile);
		this.actual = inputString.equals(content);
		assertTrue("", this.actual);
	}

	/**
	 * Test method for {@link WriteFileExtensions#write2File(String, Writer)}.
	 */
	@Test
	public void testWrite2FileStringWriter()
	{
		// TODO implement unit test cases...
	}

	/**
	 * Test method for {@link WriteFileExtensions#write2File(String, Writer, boolean)}.
	 */
	@Test
	public void testWrite2FileStringWriterBoolean()
	{
		// TODO implement unit test cases...
	}

	/**
	 * Test method for {@link WriteFileExtensions#write2FileWithBuffer(String, String)}.
	 */
	@Test
	public void testWrite2FileWithBuffer()
	{
		// TODO implement unit test cases...
	}

	/**
	 * Test method for {@link WriteFileExtensions#writeByteArrayToFile(File, byte[])}.
	 */
	@Test
	public void testWriteByteArrayToFileFileByteArray()
	{
		// TODO implement unit test cases...
	}

	/**
	 * Test method for {@link WriteFileExtensions#writeByteArrayToFile(String, byte[])}.
	 */
	@Test
	public void testWriteByteArrayToFileStringByteArray()
	{
		// TODO implement unit test cases...
	}

	/**
	 * Test method for {@link WriteFileExtensions#writeLinesToFile(Collection, File)}.
	 */
	@Test
	public void testWriteLinesToFileCollectionOfStringFile()
	{
		// TODO implement unit test cases...
	}

	/**
	 * Test method for {@link WriteFileExtensions#writeLinesToFile(Collection, File, String)}.
	 */
	@Test
	public void testWriteLinesToFileCollectionOfStringFileString()
	{
		// TODO implement unit test cases...
	}

	/**
	 * Test method for {@link WriteFileExtensions#writeLinesToFile(File, List, String)}.
	 */
	@Test
	public void testWriteLinesToFileFileListOfStringString()
	{
		// TODO implement unit test cases...
	}

	/**
	 * Test method for {@link WriteFileExtensions#writeProperties2File(String, Properties)}.
	 */
	@Test
	public void testWriteProperties2File()
	{
		// TODO implement unit test cases...
	}

	/**
	 * Test method for {@link WriteFileExtensions#writeStringToFile(File, String, String)}.
	 */
	@Test
	public void testWriteStringToFile()
	{
		// TODO implement unit test cases...
	}

}
