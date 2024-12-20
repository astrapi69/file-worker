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
package io.github.astrapi69.file.read;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;

import io.github.astrapi69.collection.array.ArrayFactory;
import io.github.astrapi69.collection.properties.PropertiesExtensions;
import io.github.astrapi69.file.FileTestCase;
import io.github.astrapi69.file.delete.DeleteFileExtensions;
import io.github.astrapi69.file.exception.DirectoryAlreadyExistsException;
import io.github.astrapi69.file.write.StoreFileExtensions;

/**
 * The unit test class for the class {@link ReadFileExtensions}.
 */
public class ReadFileExtensionsTest extends FileTestCase
{

	private File testFile;

	/**
	 * Sets up method will be invoked before every unit test method in this class.
	 *
	 * @throws Exception
	 *             is thrown if an exception occurs
	 */
	@Override
	@BeforeEach
	protected void setUp() throws Exception
	{
		super.setUp();
		// Create a temporary file and write some lines to it
		testFile = File.createTempFile("testFile", ".txt");
		try (FileWriter writer = new FileWriter(testFile))
		{
			writer.write("First line\n");
			writer.write("Second line\n");
			writer.write("Third line\n");
		}
	}

	/**
	 * Teardown method will be invoked after every unit test method in this class.
	 *
	 * @throws Exception
	 *             is thrown if an exception occurs
	 */
	@Override
	@AfterEach
	protected void tearDown() throws Exception
	{
		super.tearDown();
		testFile.delete();
	}

	@Test
	public void testReadLineValidIndex() throws IOException
	{
		// Test reading a line within the file
		String line = ReadFileExtensions.readLine(testFile, 1);
		assertEquals("Second line", line);
	}

	@Test
	public void testReadLineInvalidIndex() throws IOException
	{
		// Test reading a line that does not exist
		String line = ReadFileExtensions.readLine(testFile, 10);
		assertNull(line);
	}

	/**
	 * Test method for {@link ReadFileExtensions#countAllLines(File)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testCountAllLines() throws IOException
	{
		long actual;
		long expected;
		File tmpFile;
		// new scenario...
		tmpFile = new File(this.test, "testReadSmallFileInput.txt");
		actual = ReadFileExtensions.countAllLines(tmpFile);
		expected = 1;
		assertEquals(actual, expected);
		// new scenario...
		tmpFile = new File(this.test, "insert_languages.sql");
		actual = ReadFileExtensions.countAllLines(tmpFile);
		expected = 370;
		assertEquals(actual, expected);
		// new scenario...
		tmpFile = new File(this.test, "resultCodeLeft.txt");
		actual = ReadFileExtensions.countAllLines(tmpFile);
		expected = 1936;
		assertEquals(actual, expected);
	}

	/**
	 * Test method for {@link ReadFileExtensions#getFilecontentAsByteObjectArray(File)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testGetFileContentAsByteObjectArray() throws IOException
	{
		Byte[] actual;
		Byte[] expected;
		final File tmpFile = new File(this.test, "testReadSmallFileInput.txt");
		actual = ReadFileExtensions.getFilecontentAsByteObjectArray(tmpFile);
		final String lineSeparator = System.lineSeparator();
		final byte[] bytes = lineSeparator.getBytes();
		expected = ArrayFactory.newArray(Byte.valueOf("f".getBytes()[0]),
			Byte.valueOf("o".getBytes()[0]), Byte.valueOf("o".getBytes()[0]),
			Byte.valueOf(" ".getBytes()[0]), Byte.valueOf("b".getBytes()[0]),
			Byte.valueOf("a".getBytes()[0]), Byte.valueOf("r".getBytes()[0]));
		expected = ArrayFactory.newArray(expected, ArrayUtils.toObject(bytes));
		assertTrue(Arrays.deepEquals(actual, expected));
	}

	/**
	 * Test method for {@link ReadFileExtensions#fromFile(File)}
	 */
	@Test
	public void testReadFromFileFile() throws IOException
	{
		final File source = new File(this.test.getAbsoluteFile(), "testReadFileInput.txt");
		final String sourceContent = ReadFileExtensions.fromFile(source);
		final File output = new File(this.test.getAbsoluteFile(), "testReadFileOutput.txt");
		StoreFileExtensions.toFile(output, sourceContent);
		final String outputContent = ReadFileExtensions.fromFile(output);
		assertEquals(sourceContent, outputContent);

		final File testFile1 = new File(this.testDir, "testReadFromFile.txt");
		final String inputString = "Its a beautifull day!!!";
		StoreFileExtensions.toFile(testFile1, inputString);
		// --------------------------------

		final String content = ReadFileExtensions.fromFile(testFile1);
		this.actual = inputString.equals(content);
		assertTrue(this.actual);
	}

	/**
	 * Test method for {@link ReadFileExtensions#fromFile(File, Charset)}.
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
		actual = ReadFileExtensions.fromFile(file, StandardCharsets.UTF_8);
		final String lineSeparator = System.lineSeparator();
		final byte[] bytes = lineSeparator.getBytes();
		expected = "foo bar" + lineSeparator;
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
		StoreFileExtensions.toFile(inputFile, inputString);
		// --------------------------------
		final String compare = ReadFileExtensions.readHeadLine(inputFile.getAbsolutePath());

		this.actual = expected.equals(compare);
		assertTrue(this.actual);
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
		assertTrue(this.actual);
	}

	/**
	 * Test method for {@link ReadFileExtensions#readLinesInList(File, Charset)}.
	 *
	 * @throws FileNotFoundException
	 *             is thrown if the given file is not found
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
	 *             is thrown if the given file is not found
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
	 *             is thrown if the given file is not found
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
		PropertiesExtensions.export(properties, new FileOutputStream(ap));
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
	 *             is thrown if the given file is not found
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
		final String lineSeparator = System.lineSeparator();
		final byte[] bytes = lineSeparator.getBytes();
		expected = ArrayFactory.newByteArray(Byte.valueOf("f".getBytes()[0]).byteValue(),
			Byte.valueOf("o".getBytes()[0]).byteValue(),
			Byte.valueOf("o".getBytes()[0]).byteValue(),
			Byte.valueOf(" ".getBytes()[0]).byteValue(),
			Byte.valueOf("b".getBytes()[0]).byteValue(),
			Byte.valueOf("a".getBytes()[0]).byteValue(),
			Byte.valueOf("r".getBytes()[0]).byteValue());
		Byte[] expected_Byte_Array = ArrayFactory.newArray(ArrayUtils.toObject(expected),
			ArrayUtils.toObject(bytes));
		expected = ArrayUtils.toPrimitive(expected_Byte_Array);
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
