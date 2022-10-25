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
package io.github.astrapi69.file.csv;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.meanbean.test.BeanTester;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.github.astrapi69.collection.CollectionExtensions;
import io.github.astrapi69.collection.array.ArrayFactory;
import io.github.astrapi69.collection.list.ListExtensions;
import io.github.astrapi69.collection.list.ListFactory;
import io.github.astrapi69.collection.map.MapFactory;
import io.github.astrapi69.collection.set.SetFactory;
import io.github.astrapi69.file.FileExtensions;
import io.github.astrapi69.file.create.DirectoryFactory;
import io.github.astrapi69.file.create.FileCreationState;
import io.github.astrapi69.file.delete.DeleteFileExtensions;
import io.github.astrapi69.file.exception.FileDoesNotExistException;
import io.github.astrapi69.file.read.ReadFileExtensions;
import io.github.astrapi69.io.StreamExtensions;

/**
 * The unit test class for the class {@link CsvFileExtensions}.
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public class CsvFileExtensionsTest
{

	File resources;

	/** The test resources. */
	File testResources;

	/**
	 * Sets up method will be invoked before every unit test method in this class.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@BeforeMethod
	protected void setUp() throws Exception
	{
		// Get the absolut path from the current project.
		final String absolutePath = FileExtensions.getCurrentAbsolutPathWithoutDotAndSlash();
		final File projectPath = new File(absolutePath);
		assertTrue("The directory " + projectPath.getAbsolutePath() + " should be created.",
			projectPath.exists());
		testResources = new File(projectPath.getAbsoluteFile(), "/src/test/resources");
		if (!testResources.exists())
		{
			final FileCreationState state = DirectoryFactory.newDirectory(testResources);
			assertTrue("The directory " + testResources.getAbsolutePath() + " should be created.",
				state.equals(FileCreationState.CREATED));
		}
		resources = new File(testResources, "resources");

	}

	/**
	 * Tear down method will be invoked after every unit test method in this class.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@AfterMethod
	protected void tearDown() throws Exception
	{
	}

	/**
	 * Test method for {@link CsvFileExtensions#formatKommaSeperatedFileToList(File, String)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test(enabled = true)
	public void testFormatKommaSeperatedFileToList() throws IOException
	{
		final File input = new File(resources, "testFormatKommaSeperatedFileToList.dat");
		final List<String> testdata = CsvFileExtensions.formatKommaSeperatedFileToList(input, null);
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
		expected.add("test7");
		expected.add("test8");
		expected.add("test9");
		expected.add("bla1");
		expected.add("fasel1");
		expected.add("and1");
		expected.add("so1");
		expected.add("on1");
		expected.add("test10");
		expected.add("test11");
		expected.add("test12");
		expected.add("foo1");
		expected.add("bar1");
		expected.add("sim1");
		expected.add("sala1");
		expected.add("bim1");
		final boolean result = expected.equals(testdata);
		assertTrue("The List testdate should be equal with the expected List.", result);

	}

	/**
	 * Test method for {@link CsvFileExtensions#formatToCSV(File, File, String)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test(enabled = true)
	public void testFormatToCSV() throws IOException
	{
		final File testInputFile = new File(resources, "testFormatToCSVinput.lst");
		final File testOutputFile = new File(testResources, "testFormatToCSVoutput.csf");
		CsvFileExtensions.formatToCSV(testInputFile, testOutputFile, null);
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
		final List<String> testList = CsvFileExtensions
			.formatKommaSeperatedFileToList(testOutputFile, null);
		final boolean result = expected.equals(testList);
		assertTrue("", result);
	}

	/**
	 * Test method for {@link CsvFileExtensions#getCvsAsListMap(File)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testGetCvsAsListMap() throws IOException
	{
		List<Map<String, String>> expected;
		String key;
		String value;
		Map<String, String> map;

		expected = ListFactory.newArrayList();
		map = MapFactory.newLinkedHashMap();
		key = "\"Vorname\",\"Nachname\",\"Email\"";
		value = "\"Jaroslav\",\"Mengele\",\"jaro.meng@gmail.com\"";
		map.put(key, value);
		expected.add(map);

		map = MapFactory.newLinkedHashMap();
		key = "\"Vorname\",\"Nachname\",\"Email\"";
		value = "\"Dimitri\",\"Vladim\",\"dim.vlad@gmail.com\"";
		map.put(key, value);
		expected.add(map);

		map = MapFactory.newLinkedHashMap();
		key = "\"Vorname\",\"Nachname\",\"Email\"";
		value = "\"Jim\",\"Phelps\",\"jim.phelps@gmail.com\"";
		map.put(key, value);
		expected.add(map);

		map = MapFactory.newLinkedHashMap();
		key = "\"Vorname\",\"Nachname\",\"Email\"";
		value = "\"Jürgen\",\"Dößler\",\"juerg.doesl@gmail.com\"";
		map.put(key, value);
		expected.add(map);

		final File res = new File(testResources, "resources");
		final File input = new File(res, "test-csv-data.csv");
		List<Map<String, String>> cvsAsListMap = CsvFileExtensions.getCvsAsListMap(input, "UTF-8");
		for (int i = 0; i < cvsAsListMap.size(); i++)
		{
			Map<String, String> map2 = cvsAsListMap.get(i);
			Map<String, String> map3 = expected.get(i);
			assertEquals(map2.get(key), map3.get(key));
		}
	}

	/**
	 * Test method for {@link CsvFileExtensions#getCvsAsListMap(File, String)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testGetCvsAsListMapFileString() throws IOException
	{
		List<Map<String, String>> expected;
		String key;
		String value;
		String encoding;
		Map<String, String> map;

		expected = ListFactory.newArrayList();
		map = MapFactory.newLinkedHashMap();
		key = "\"Vorname\",\"Nachname\",\"Email\"";
		value = "\"Jaroslav\",\"Mengele\",\"jaro.meng@gmail.com\"";
		map.put(key, value);
		expected.add(map);

		map = MapFactory.newLinkedHashMap();
		key = "\"Vorname\",\"Nachname\",\"Email\"";
		value = "\"Dimitri\",\"Vladim\",\"dim.vlad@gmail.com\"";
		map.put(key, value);
		expected.add(map);

		map = MapFactory.newLinkedHashMap();
		key = "\"Vorname\",\"Nachname\",\"Email\"";
		value = "\"Jim\",\"Phelps\",\"jim.phelps@gmail.com\"";
		map.put(key, value);
		expected.add(map);

		map = MapFactory.newLinkedHashMap();
		key = "\"Vorname\",\"Nachname\",\"Email\"";
		value = "\"Jürgen\",\"Dößler\",\"juerg.doesl@gmail.com\"";
		map.put(key, value);
		expected.add(map);

		final File res = new File(testResources, "resources");
		final File input = new File(res, "test-csv-data.csv");

		encoding = "UTF-8";
		List<Map<String, String>> cvsAsListMap = CsvFileExtensions.getCvsAsListMap(input, encoding);
		for (int i = 0; i < cvsAsListMap.size(); i++)
		{
			Map<String, String> map2 = cvsAsListMap.get(i);
			Map<String, String> map3 = expected.get(i);
			assertEquals(map2.get(key), map3.get(key));
		}
	}

	/**
	 * Test method for {@link CsvFileExtensions#getDataFromLine(String, String)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test(enabled = true)
	public void testGetDataFromLine() throws IOException
	{
		List<String[]> actual;
		List<String[]> expected;
		String[] lineOne;
		String[] lineTwo;

		lineOne = ArrayFactory.newArray("test1", "test2", "test3", "bla", "fasel", "and", "so",
			"on");
		lineTwo = ArrayFactory.newArray("test4", "test5", "test6", "foo", "bar", "sim", "sala",
			"bim");
		expected = ListFactory.newArrayList(lineOne, lineTwo);
		actual = ListFactory.newArrayList();

		final File testFile = new File(testResources, "resources");
		final File file = new File(testFile, "testSortData.lst");
		final List<String> list = ReadFileExtensions.readLinesInList(file);
		for (final String string2 : list)
		{
			final String string = string2;
			final String[] data = CsvFileExtensions.getDataFromLine(string, ",");
			actual.add(data);
		}
		assertTrue(CollectionExtensions.isEqualCollection(actual, expected));
	}

	/**
	 * Test method for {@link CsvFileExtensions#getLineCountFromCsvFile(File)}.
	 *
	 * @throws IOException
	 */
	@Test
	public void testGetLineCountFromCsvFile() throws IOException
	{
		int actual;
		int expected;
		final File res = new File(testResources, "resources");
		File input = new File(res, "test-csv-data.csv");
		actual = CsvFileExtensions.getLineCountFromCsvFile(input);
		expected = 5;
		assertEquals(actual, expected);

		input = new File(res, "testReadDataFromCVSFileToList.csv");
		actual = CsvFileExtensions.getLineCountFromCsvFile(input);
		expected = 6;
		assertEquals(actual, expected);
	}

	/**
	 * Test method for
	 * {@link CsvFileExtensions#readDataFromCVSFileToList(File, int, boolean, String)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 *             {@link CsvFileExtensions#readDataFromCVSFileToList(File, int, boolean, String)}
	 */
	@Test(enabled = true)
	public void testReadDataFromCVSFileToList() throws IOException
	{
		final File res = new File(testResources, "resources");
		final File input = new File(res, "testReadDataFromCVSFileToList.csv");
		System.out.println(input.getAbsolutePath());
		final List<String> output = CsvFileExtensions.readDataFromCVSFileToList(input, 1, false,
			"UTF-8");
		final boolean result = output.size() == 5;
		assertTrue("", result);
	}

	/**
	 * Test method for
	 * {@link CsvFileExtensions#readDataFromCVSFileToList(File, int, boolean, String)}.
	 *
	 * @throws IOException
	 */
	@Test
	public void testReadDataFromCVSFileToListFileIntBooleanString() throws IOException
	{
		final File res = new File(testResources, "resources");
		File input = new File(res, "test-csv-data.csv");

		List<String> actual;
		List<String> expected;
		int position;
		boolean putFirstLine;
		String encoding;
		position = 0;
		putFirstLine = false;
		encoding = "UTF-8";
		actual = CsvFileExtensions.readDataFromCVSFileToList(input, position, putFirstLine,
			encoding);
		expected = ListFactory.newArrayList("Jaroslav", "Dimitri", "Jim", "Jürgen");
		assertTrue(CollectionExtensions.isEqualCollection(actual, expected));
		//
		putFirstLine = true;
		actual = CsvFileExtensions.readDataFromCVSFileToList(input, position, putFirstLine,
			encoding);
		expected = ListFactory.newArrayList("Vorname", "Jaroslav", "Dimitri", "Jim", "Jürgen");
		assertTrue(CollectionExtensions.isEqualCollection(actual, expected));
	}

	/**
	 * Test method for
	 * {@link CsvFileExtensions#readDataFromCVSFileToList(File, int, boolean, String, String)}.
	 *
	 * @throws IOException
	 */
	@Test
	public void testReadDataFromCVSFileToListFileIntBooleanStringString() throws IOException
	{
		final File res = new File(testResources, "resources");
		File input = new File(res, "test-csv-data.csv");

		List<String> actual;
		List<String> expected;
		int position;
		boolean putFirstLine;
		String splitChar;
		String encoding;
		position = 0;
		putFirstLine = false;
		encoding = "UTF-8";
		splitChar = ",";
		actual = CsvFileExtensions.readDataFromCVSFileToList(input, position, putFirstLine,
			splitChar, encoding);
		expected = ListFactory.newArrayList("Jaroslav", "Dimitri", "Jim", "Jürgen");
		assertTrue(CollectionExtensions.isEqualCollection(actual, expected));
		//
		putFirstLine = true;
		actual = CsvFileExtensions.readDataFromCVSFileToList(input, position, putFirstLine,
			splitChar, encoding);
		expected = ListFactory.newArrayList("Vorname", "Jaroslav", "Dimitri", "Jim", "Jürgen");
		assertTrue(CollectionExtensions.isEqualCollection(actual, expected));
	}

	/**
	 * Test method for {@link CsvFileExtensions#readFilelistToProperties(File)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test(enabled = true)
	public void testReadFilelistToProperties() throws IOException
	{
		final String[] expected = { "test1", "test2", "test3", "bla", "fasel", "and", "so", "on",
				"test4", "test5", "test6", "foo", "bar", "sim", "sala", "bim" };
		final Properties expectedProperties = new Properties();
		for (int i = 0; i < expected.length; i++)
		{
			expectedProperties.put(i + "", expected[i]);
		}
		final File testFile = new File(resources, "testReadKommaSeperatedFileToProperties.lst");
		final Properties testProperties = CsvFileExtensions.readFilelistToProperties(testFile);
		final boolean result = expectedProperties.equals(testProperties);
		assertTrue("", result);
	}

	/**
	 * Test method for.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 *             {@link CsvFileExtensions#readFileToList(File, String)} .
	 */
	@Test(enabled = true)
	public void testReadFileToList() throws IOException
	{
		final List<String> expected = new ArrayList<>();
		expected.add("abra");
		expected.add("gada");
		expected.add("bra");
		expected.add("sim");
		expected.add("sala");
		expected.add("bim");
		expected.add("here");
		expected.add("you");
		expected.add("are");
		final File testFileList = new File(resources, "testReadFileToList.dat");
		final List<String> fileList = CsvFileExtensions.readFileToList(testFileList);
		boolean result = expected.size() == fileList.size();
		assertTrue(result);
		result = expected.equals(fileList);
		assertTrue(result);
	}

	/**
	 * Test method for {@link CsvFileExtensions#readFileToList(File, String, String)}.
	 *
	 * @throws IOException
	 */
	@Test
	public void testReadFileToListFileStringString() throws IOException
	{
		List<String[]> actual;
		List<String[]> expected;
		String[] lineOne;
		String[] lineTwo;
		String[] lineThree;
		String[] lineFour;
		String[] lineFive;

		lineOne = ArrayFactory.newArray("\"Vorname\"", "\"Nachname\"", "\"Email\"");
		lineTwo = ArrayFactory.newArray("\"Jaroslav\"", "\"Mengele\"", "\"jaro.meng@gmail.com\"");
		lineThree = ArrayFactory.newArray("\"Dimitri\"", "\"Vladim\"", "\"dim.vlad@gmail.com\"");
		lineFour = ArrayFactory.newArray("\"Jim\"", "\"Phelps\"", "\"jim.phelps@gmail.com\"");
		lineFive = ArrayFactory.newArray("\"Jürgen\"", "\"Dößler\"", "\"juerg.doesl@gmail.com\"");

		expected = ListFactory.newArrayList(lineOne, lineTwo, lineThree, lineFour, lineFive);

		final File res = new File(testResources, "resources");
		File input = new File(res, "test-csv-data.csv");
		actual = CsvFileExtensions.readFileToList(input, ",", "UTF-8");
		assertTrue(ListExtensions.isEqualListOfArrays(actual, expected));
	}

	/**
	 * Test method for {@link CsvFileExtensions#readLinesInList(File, String)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test(enabled = true)
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
		final File testFile = new File(resources, "testReadLinesInList.lst");
		final List<String> testList = CsvFileExtensions.readLinesInList(testFile, null);
		final boolean result = expected.equals(testList);
		assertTrue("", result);

	}

	/**
	 * Test method for {@link CsvFileExtensions#sortData(File, String)}.
	 *
	 * @throws FileNotFoundException
	 *             the file not found exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test(enabled = true)
	public void testSortData() throws FileNotFoundException, IOException
	{
		final String[] expected = { "and", "bar", "bim", "bla", "fasel", "foo", "on", "sala", "sim",
				"so", "test1", "test2", "test3", "test4", "test5", "test6" };
		final File testFile = new File(resources, "testSortData.lst");
		final String[] sortedData = CsvFileExtensions.sortData(testFile, null);
		for (int i = 0; i < sortedData.length; i++)
		{
			final boolean result = expected[i].equals(sortedData[i]);
			assertTrue("", result);
		}
	}

	/**
	 * Test method for {@link CsvFileExtensions#storeFilelistToProperties(File, File, String)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws FileDoesNotExistException
	 *             the file does not exist exception
	 */
	@Test(enabled = true)
	public void testStoreFilelistToProperties() throws IOException, FileDoesNotExistException
	{
		final String[] expected = { "test1", "test2", "test3", "bla", "fasel", "and", "so", "on",
				"test4", "test5", "test6", "foo", "bar", "sim", "sala", "bim" };
		final Properties expectedProperties = new Properties();
		for (int i = 0; i < expected.length; i++)
		{
			expectedProperties.put(i + "", expected[i]);
		}
		final File testFileInput = new File(resources, "testStoreFilelistToProperties.lst");
		final File testFileOutput = new File(resources, "testStoreFilelistToProperties.properties");
		CsvFileExtensions.storeFilelistToProperties(testFileOutput, testFileInput, "Test comment.");
		final Properties testProperties = new Properties();
		testProperties.load(StreamExtensions.getInputStream(testFileOutput, true));
		final boolean result = expectedProperties.equals(testProperties);
		assertTrue("", result);
		try
		{
			DeleteFileExtensions.delete(testFileOutput);
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link CsvFileExtensions}
	 */
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(CsvFileExtensions.class);
	}

	/**
	 * Test method for {@link CsvFileExtensions#writeLines(File, Set, String)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test(enabled = true)
	public void testWriteLines() throws IOException
	{
		final Set<String> expected = SetFactory.newHashSet();
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
		final File testFile = new File(testResources, "testWriteLinesToFile.lst");
		CsvFileExtensions.writeLines(testFile, expected, "UTF-8");
		final Set<String> actual = SetFactory
			.newHashSet(CsvFileExtensions.readLinesInList(testFile, null));
		final boolean result = CollectionExtensions.isEqualCollection(expected, actual);
		assertTrue("", result);

	}

	/**
	 * Test method for {@link CsvFileExtensions#writeLinesToFile(Collection, File, String)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test(enabled = true)
	public void testWriteLinesToFile() throws IOException
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
		final File testFile = new File(testResources, "testWriteLinesToFile.lst");
		CsvFileExtensions.writeLinesToFile(expected, testFile, null);
		final List<String> testList = CsvFileExtensions.readLinesInList(testFile, null);
		final boolean result = expected.equals(testList);
		assertTrue("", result);
	}

}
