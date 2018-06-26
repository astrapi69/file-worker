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
package de.alpharogroup.file.read;

import static org.testng.Assert.assertNotNull;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import org.meanbean.factories.ObjectCreationException;
import org.meanbean.test.BeanTestException;
import org.meanbean.test.BeanTester;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.alpharogroup.collections.array.ArrayFactory;
import de.alpharogroup.file.FileTestCase;
import de.alpharogroup.file.write.WriteFileExtensions;

/**
 * The unit test class for the class {@link ReadFileExtensions}.
 */
public class ReadFileExtensionsTest extends FileTestCase
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
	 * Test method for {@link ReadFileExtensions#getFilecontentAsByteObjectArray(File)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testGetFilecontentAsByteObjectArray() throws IOException
	{
		Byte[] actual;
		Byte[] expected;
		final File tmpFile = new File(this.test, "testReadSmallFileInput.txt");
		actual = ReadFileExtensions.getFilecontentAsByteObjectArray(tmpFile);
		expected = ArrayFactory.newArray(Byte.valueOf("f".getBytes()[0]),
			Byte.valueOf("o".getBytes()[0]), Byte.valueOf("o".getBytes()[0]),
			Byte.valueOf(" ".getBytes()[0]), Byte.valueOf("b".getBytes()[0]),
			Byte.valueOf("a".getBytes()[0]), Byte.valueOf("r".getBytes()[0]),
			Byte.valueOf((byte)10));
		assertTrue(Arrays.deepEquals(actual, expected));
	}

	/**
	 * Test method for {@link ReadFileExtensions#readFromFile(File)}
	 */
	@Test
	public void testReadFromFileFile() throws IOException
	{
		final File source = new File(this.test.getAbsoluteFile(), "testReadFileInput.txt");
		final String sourceContent = ReadFileExtensions.readFromFile(source);
		final File output = new File(this.test.getAbsoluteFile(), "testReadFileOutput.txt");
		WriteFileExtensions.string2File(output, sourceContent);
		final String outputContent = ReadFileExtensions.readFromFile(output);
		assertEquals(sourceContent, outputContent);
	}

	/**
	 * Test method for {@link ReadFileExtensions#readFromFile(File, Charset)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testReadFromFileFileCharset() throws IOException
	{
		String actual;
		String expected;
		final File file = new File(this.test, "testReadSmallFileInput.txt");
		actual = ReadFileExtensions.readFromFile(file, Charset.forName("UTF-8"));
		expected = "foo bar\n";
		assertEquals(actual, expected);
	}

	/**
	 * Test method for {@link ReadFileExtensions#readLinesInList(File, Charset)}.
	 *
	 * @throws FileNotFoundException
	 *             the file not found exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testReadLinesInListFileCharset() throws FileNotFoundException, IOException
	{
		final File testFile = new File(this.test, "languages_iso639_1.csv");
		final List<String> list = ReadFileExtensions.readLinesInList(testFile,
			Charset.forName("Windows-1252"));
		assertNotNull(list);
		actual = list.size() == 185;
		expected = true;
		assertEquals(actual, expected);
	}

	/**
	 * Test method for {@link ReadFileExtensions#readLinesInList(File, Charset, boolean)}.
	 *
	 * @throws FileNotFoundException
	 *             the file not found exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testReadLinesInListFileCharsetBoolean() throws FileNotFoundException, IOException
	{
		final File testFile = new File(this.test, "languages_iso639_1.csv");
		final List<String> list = ReadFileExtensions.readLinesInList(testFile,
			Charset.forName("Windows-1252"), true);
		assertNotNull(list);
		actual = list.size() == 185;
		expected = true;
		assertEquals(actual, expected);
	}

	/**
	 * Test method for {@link ReadFileExtensions#readLinesInList(InputStream)}.
	 *
	 * @throws FileNotFoundException
	 *             the file not found exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testReadLinesInListInputStream() throws FileNotFoundException, IOException
	{
		final File testFile = new File(this.test, "languages_iso639_1.csv");
		final List<String> list = ReadFileExtensions.readLinesInList(new FileInputStream(testFile));
		assertNotNull(list);
		actual = list.size() == 185;
		expected = true;
		assertEquals(actual, expected);
	}

	/**
	 * Test method for {@link ReadFileExtensions#toByteArray(InputStream)}.
	 *
	 * @throws FileNotFoundException
	 *             the file not found exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testToByteArrayInputStream() throws FileNotFoundException, IOException
	{
		byte[] actual;
		byte[] expected;
		final File tmpFile = new File(this.test, "testReadSmallFileInput.txt");
		actual = ReadFileExtensions.toByteArray(new FileInputStream(tmpFile));
		expected = ArrayFactory.newByteArray(Byte.valueOf("f".getBytes()[0]).byteValue(),
			Byte.valueOf("o".getBytes()[0]).byteValue(),
			Byte.valueOf("o".getBytes()[0]).byteValue(),
			Byte.valueOf(" ".getBytes()[0]).byteValue(),
			Byte.valueOf("b".getBytes()[0]).byteValue(),
			Byte.valueOf("a".getBytes()[0]).byteValue(),
			Byte.valueOf("r".getBytes()[0]).byteValue(), Byte.valueOf((byte)10).byteValue());
		assertTrue(Arrays.equals(actual, expected));
	}

	/**
	 * Test method for {@link ReadFileExtensions}
	 */
	@Test(expectedExceptions = { BeanTestException.class, ObjectCreationException.class })
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(ReadFileExtensions.class);
	}

}
