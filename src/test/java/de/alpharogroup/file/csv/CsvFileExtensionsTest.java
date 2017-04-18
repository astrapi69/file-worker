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
package de.alpharogroup.file.csv;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.testng.AssertJUnit;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.alpharogroup.file.FileExtensions;
import de.alpharogroup.file.create.CreateFileExtensions;
import de.alpharogroup.file.delete.DeleteFileExtensions;
import de.alpharogroup.file.exceptions.FileDoesNotExistException;
import de.alpharogroup.file.read.ReadFileExtensions;
import de.alpharogroup.file.write.WriteFileExtensions;
import de.alpharogroup.io.StreamExtensions;

/**
 * Test class for the class {@link CsvFileExtensions}.
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public class CsvFileExtensionsTest
{

	/** The test resources. */
	File testResources;

	File resources;

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
		AssertJUnit.assertTrue(
			"The directory " + projectPath.getAbsolutePath() + " should be created.",
			projectPath.exists());
		testResources = new File(projectPath.getAbsoluteFile(), "/src/test/resources");
		if (!testResources.exists())
		{
			final boolean created = CreateFileExtensions.newDirectory(testResources);
			AssertJUnit.assertTrue(
				"The directory " + testResources.getAbsolutePath() + " should be created.",
				created);
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

	@Test(enabled = true)
	public void testCreateSqlInsertFromCsv() throws IOException
	{
		final File testFile = new File(testResources, "languages_iso639_1.csv");
		final File output = new File(testResources, "insert_languages.sql");
		final List<String> list = ReadFileExtensions.readLinesInList(testFile,
			Charset.forName("Windows-1252"));
		int id = 1;
		final int version = 1;
		final StringBuilder sb = new StringBuilder();
		for (final String string2 : list)
		{
			final String string = string2;
			final String[] data = CsvFileExtensions.getDataFromLine(string, ";");
			byte[] destinationBytes = data[0].getBytes("UTF-8");
			final String languageName = new String(destinationBytes);
			destinationBytes = data[1].getBytes("UTF-8");
			final String languageCode = new String(destinationBytes);
			final String insert = "INSERT INTO languages (id, version, name, iso639_1) VALUES ("
				+ id + ", " + version + ", '" + languageName + "', '" + languageCode + "');";
			sb.append(insert);
			sb.append(System.lineSeparator());
			id++;
			System.out.println(insert);
		}
		WriteFileExtensions.writeStringToFile(output, sb.toString(), "UTF-8");
	}

	/**
	 * Test method for.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 *             {@link de.alpharogroup.file.csv.CsvFileExtensions#formatKommaSeperatedFileToList(java.io.File, java.lang.String)}
	 *             .
	 */
	@Test(enabled = false)
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
		AssertJUnit.assertTrue("The List testdate should be equal with the expected List.", result);

	}

	/**
	 * Test method for.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 *             {@link de.alpharogroup.file.csv.CsvFileExtensions#formatToCSV(java.io.File, java.io.File, java.lang.String)}
	 *             .
	 */
	@Test(enabled = false)
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
		AssertJUnit.assertTrue("", result);
	}

	/**
	 * Test get data from line.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test(enabled = false)
	public void testGetDataFromLine() throws IOException
	{
		final File testFile = new File(testResources, "resources");
		final File file = new File(testFile, "testSortData.lst");
		final List<String> list = ReadFileExtensions.readLinesInList(file);
		for (final String string2 : list)
		{
			final String string = string2;
			final String[] data = CsvFileExtensions.getDataFromLine(string, ",");
			System.out.println(data);

		}
	}

	/**
	 * Test method for.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 *             {@link de.alpharogroup.file.csv.CsvFileExtensions#readDataFromCVSFileToList(java.io.File, int, boolean, java.lang.String)}
	 *             .
	 */
	@Test(enabled = false)
	public void testReadDataFromCVSFileToList() throws IOException
	{
		final File res = new File(testResources, "resources");
		final File input = new File(res, "testReadDataFromCVSFileToList.csv");
		System.out.println(input.getAbsolutePath());
		final List<String> output = CsvFileExtensions.readDataFromCVSFileToList(input, 1, true,
			"UTF-8");
		final boolean result = output.size() == 5;
		AssertJUnit.assertTrue("", result);
	}

	/**
	 * Test method for.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 *             {@link de.alpharogroup.file.csv.CsvFileExtensions#readFilelistToProperties(File)}
	 *             .
	 */
	@Test(enabled = false)
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
		AssertJUnit.assertTrue("", result);
	}

	/**
	 * Test method for.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 *             {@link de.alpharogroup.file.csv.CsvFileExtensions#readFileToList(java.io.File, java.lang.String)}
	 *             .
	 */
	@Test(enabled = false)
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
		AssertJUnit.assertTrue(result);
		result = expected.equals(fileList);
		AssertJUnit.assertTrue(result);
	}

	/**
	 * Test method for.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 *             {@link de.alpharogroup.file.csv.CsvFileExtensions#readLinesInList(java.io.File, java.lang.String)}
	 *             .
	 */
	@Test(enabled = false)
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
		AssertJUnit.assertTrue("", result);

	}

	/**
	 * Test method for.
	 *
	 * @throws FileNotFoundException
	 *             the file not found exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 *             {@link de.alpharogroup.file.csv.CsvFileExtensions#sortData(java.io.File, java.lang.String)}
	 *             .
	 */
	@Test(enabled = false)
	public void testSortData() throws FileNotFoundException, IOException
	{
		final String[] expected = { "and", "bar", "bim", "bla", "fasel", "foo", "on", "sala", "sim",
				"so", "test1", "test2", "test3", "test4", "test5", "test6" };
		final File testFile = new File(resources, "testSortData.lst");
		final String[] sortedData = CsvFileExtensions.sortData(testFile, null);
		for (int i = 0; i < sortedData.length; i++)
		{
			final boolean result = expected[i].equals(sortedData[i]);
			AssertJUnit.assertTrue("", result);
		}
	}

	/**
	 * Test method for.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws FileDoesNotExistException
	 *             the file does not exist exception
	 *             {@link de.alpharogroup.file.csv.CsvFileExtensions#storeFilelistToProperties(File, File, String)}
	 *             .
	 */
	@Test(enabled = false)
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
		AssertJUnit.assertTrue("", result);
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
	 * Test method for.
	 *
	 * {@link de.alpharogroup.file.csv.CsvFileExtensions#writeLines(java.io.File, java.util.Set, java.lang.String)}
	 * .
	 */
	@Test(enabled = false)
	public void testWriteLines()
	{
		// See test from
		// CsvFileUtils.writeLinesToFile(Collection, File, String )

	}

	/**
	 * Test method for.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 *             {@link de.alpharogroup.file.csv.CsvFileExtensions#writeLinesToFile(java.util.Collection, java.io.File, java.lang.String)}
	 *             .
	 */
	@Test
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
		AssertJUnit.assertTrue("", result);
	}

}
