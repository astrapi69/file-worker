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
package io.github.astrapi69.read;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.meanbean.test.BeanTester;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.alpharogroup.collections.array.ArrayFactory;
import io.github.astrapi69.FileTestCase;
import io.github.astrapi69.delete.DeleteFileExtensions;
import io.github.astrapi69.exceptions.DirectoryAlreadyExistsException;
import io.github.astrapi69.exceptions.FileDoesNotExistException;
import io.github.astrapi69.write.WriteFileExtensions;
import de.alpharogroup.io.StreamExtensions;

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
	 * Test method for {@link ReadFileExtensions#inputStream2String(InputStream)}
	 *
	 * @throws DirectoryAlreadyExistsException
	 *             the directory allready exists exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws FileDoesNotExistException
	 *             the file does not exist exception
	 */
	@Test
	public void testInputStream2String()
		throws DirectoryAlreadyExistsException, IOException, FileDoesNotExistException
	{

		final File inputFile = new File(this.testDir, "testInputStream2String.inp");
		inputFile.createNewFile();
		final String inputString = "Its a beautifull day!!!\n" + "This is the second line.\n"
			+ "This is the third line. ";
		WriteFileExtensions.string2File(inputFile, inputString);
		// --------------------------------
		final InputStream is = StreamExtensions.getInputStream(inputFile);
		final String compare = ReadFileExtensions.inputStream2String(is);

		this.actual = inputString.equals(compare);
		assertTrue("", this.actual);
	}

	/**
	 * Test method for {@link ReadFileExtensions#openFileReader(String)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testOpenFileReader() throws IOException
	{
		final File testFile1 = new File(this.testDir, "testOpenFileReader.txt");
		final String inputString = "Its a beautifull day!!!";
		final String expected = inputString;
		final String ap = testFile1.getAbsolutePath();
		WriteFileExtensions.string2File(inputString, ap);

		final Reader reader = ReadFileExtensions.openFileReader(ap);
		final String compare = ReadFileExtensions.reader2String(reader);
		this.actual = expected.equals(compare);
		assertTrue("", this.actual);
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

		final File testFile1 = new File(this.testDir, "testReadFromFile.txt");
		final String inputString = "Its a beautifull day!!!";
		WriteFileExtensions.string2File(testFile1, inputString);
		// --------------------------------

		final String content = ReadFileExtensions.readFromFile(testFile1);
		this.actual = inputString.equals(content);
		assertTrue("", this.actual);
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
	 * Test method for {@link ReadFileExtensions#readHeadLine(String)}.
	 *
	 * @throws DirectoryAlreadyExistsException
	 *             the directory allready exists exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testReadHeadLine() throws DirectoryAlreadyExistsException, IOException
	{
		final File inputFile = new File(this.testDir, "testReadHeadLine.inp");
		inputFile.createNewFile();

		final String inputString = "Its a beautifull day!!!\n This is the second line.\nThis is the third line. ";
		final String expected = "Its a beautifull day!!!";
		WriteFileExtensions.string2File(inputFile, inputString);
		// --------------------------------
		final String compare = ReadFileExtensions.readHeadLine(inputFile.getAbsolutePath());

		this.actual = expected.equals(compare);
		assertTrue("", this.actual);
	}

	/**
	 * Test method for {@link ReadFileExtensions#readLinesInList(File)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testReadLinesInList() throws IOException
	{
		final List<String> expected = new ArrayList<>();
		expected.add("test1");
		expected.add("test2");
		expected.add("test3");
		expected.add("bla");
		expected.add("fasel");
		expected.add("and");
		expected.add("so");
		expected.add("on");
		expected.add("test4");
		expected.add("test5");
		expected.add("test6");
		expected.add("foo");
		expected.add("bar");
		expected.add("sim");
		expected.add("sala");
		expected.add("bim");
		final File testFile = new File(this.testResources, "testReadLinesInList.lst");
		final List<String> testList = ReadFileExtensions.readLinesInList(testFile);
		this.actual = expected.equals(testList);
		assertTrue("", this.actual);
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
	 * Test method for {@link ReadFileExtensions#readPropertiesFromFile(String)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testReadPropertiesFromFile() throws IOException
	{
		final File tp = new File(this.testResources, "testReadPropertiesFromFile.properties");
		final String ap = tp.getAbsolutePath();
		Properties compare = new Properties();
		final Properties properties = new Properties();
		properties.setProperty("testkey1", "testvalue1");
		properties.setProperty("testkey2", "testvalue2");
		properties.setProperty("testkey3", "testvalue3");
		WriteFileExtensions.writeProperties2File(ap, properties);
		compare = ReadFileExtensions.readPropertiesFromFile(ap);
		this.actual = properties.equals(compare);
		assertTrue(this.actual);
		// clean up
		DeleteFileExtensions.delete(tp);
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
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(ReadFileExtensions.class);
	}

}
