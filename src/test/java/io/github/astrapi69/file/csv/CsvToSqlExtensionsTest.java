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

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.meanbean.test.BeanTester;
import org.testng.annotations.Test;

import io.github.astrapi69.collection.array.ArrayFactory;
import io.github.astrapi69.collection.list.ListFactory;

/**
 * The unit test class for the class {@link CsvToSqlExtensions}.
 */
public class CsvToSqlExtensionsTest
{

	/**
	 * Test method for {@link CsvToSqlExtensions#extractSqlColumns(String[])}.
	 */
	@Test
	public final void testExtractSqlColumns()
	{
		String expected;
		String actual;
		String[] headers;
		headers = ArrayFactory.newArray("foo", "bar", "bla");
		actual = CsvToSqlExtensions.extractSqlColumns(headers);
		expected = "foo, bar, bla";
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link CsvToSqlExtensions#getCsvFileAsSqlInsertScript(String, CsvBean)}.
	 */
	@Test
	public final void testGetCsvFileAsSqlInsertScriptStringCsvBean()
	{
		String expected;
		String actual;
		String tableName;
		CsvBean csvBean;
		String[] headers;
		String[] columnTypes;
		String[] lineOne;
		String[] lineTwo;
		String[] lineThree;
		List<String[]> lines;
		headers = ArrayFactory.newArray("name", "age", "gender");
		columnTypes = ArrayFactory.newArray("text", "integer", "enum");
		lineOne = ArrayFactory.newArray("John", "23", "male");
		lineTwo = ArrayFactory.newArray("Jim", "25", "male");
		lineThree = ArrayFactory.newArray("Mary", "21", "female");
		lines = ListFactory.newArrayList(lineOne, lineTwo, lineThree);

		tableName = "employees";
		csvBean = CsvBean.builder().headers(headers).columnTypes(columnTypes).lines(lines).build();
		actual = CsvToSqlExtensions.getCsvFileAsSqlInsertScript(tableName, csvBean);
		expected = "INSERT INTO employees ( name, age, gender) VALUES \n"
			+ "(\"John\", 23, male),\n" + "(\"Jim\", 25, male),\n" + "(\"Mary\", 21, female);\n";
		assertEquals(expected, actual);
	}

	/**
	 * Test method for
	 * {@link CsvToSqlExtensions#getCsvFileAsSqlInsertScript(String, CsvBean, boolean, boolean)}.
	 */
	@Test
	public final void testGetCsvFileAsSqlInsertScriptStringCsvBeanBooleanBoolean()
	{
		String expected;
		String actual;
		String tableName;
		CsvBean csvBean;
		boolean withHeader;
		boolean withEndSemicolon;
		String[] headers;
		String[] columnTypes;
		String[] lineOne;
		String[] lineTwo;
		String[] lineThree;
		List<String[]> lines;
		headers = ArrayFactory.newArray("name", "age", "gender");
		columnTypes = ArrayFactory.newArray("text", "integer", "enum");
		lineOne = ArrayFactory.newArray("John", "23", "male");
		lineTwo = ArrayFactory.newArray("Jim", "25", "male");
		lineThree = ArrayFactory.newArray("Mary", "21", "female");
		lines = ListFactory.newArrayList(lineOne, lineTwo, lineThree);

		tableName = "employees";

		csvBean = CsvBean.builder().headers(headers).columnTypes(columnTypes).lines(lines).build();
		withHeader = true;
		withEndSemicolon = true;
		actual = CsvToSqlExtensions.getCsvFileAsSqlInsertScript(tableName, csvBean, withHeader,
			withEndSemicolon);
		expected = "INSERT INTO employees ( name, age, gender) VALUES \n"
			+ "(\"John\", 23, male),\n" + "(\"Jim\", 25, male),\n" + "(\"Mary\", 21, female);\n";
		assertEquals(expected, actual);
		//
		withHeader = true;
		withEndSemicolon = false;
		actual = CsvToSqlExtensions.getCsvFileAsSqlInsertScript(tableName, csvBean, withHeader,
			withEndSemicolon);
		expected = "INSERT INTO employees ( name, age, gender) VALUES \n"
			+ "(\"John\", 23, male),\n" + "(\"Jim\", 25, male),\n" + "(\"Mary\", 21, female),\n";
		assertEquals(expected, actual);
		//
		withHeader = false;
		withEndSemicolon = false;
		actual = CsvToSqlExtensions.getCsvFileAsSqlInsertScript(tableName, csvBean, withHeader,
			withEndSemicolon);
		expected = "(\"John\", 23, male),\n" + "(\"Jim\", 25, male),\n"
			+ "(\"Mary\", 21, female),\n";
		assertEquals(expected, actual);
		//
		withHeader = false;
		withEndSemicolon = true;
		actual = CsvToSqlExtensions.getCsvFileAsSqlInsertScript(tableName, csvBean, withHeader,
			withEndSemicolon);
		expected = "(\"John\", 23, male),\n" + "(\"Jim\", 25, male),\n"
			+ "(\"Mary\", 21, female);\n";
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link CsvToSqlExtensions#getDataFromLine(String, String)}.
	 */
	@Test
	public final void testGetDataFromLine()
	{
		String[] actual;
		String[] expected;
		String line = "test1, test2, test3, bla, fasel, and, so, on";
		String seperator = ",";
		actual = CsvToSqlExtensions.getDataFromLine(line, seperator);
		expected = ArrayFactory.newArray("test1", " test2", " test3", " bla", " fasel", " and",
			" so", " on");
		assertTrue(Arrays.deepEquals(actual, expected));
	}

	/**
	 * Test method for
	 * {@link CsvToSqlExtensions#getSqlData(String[], String[], String[], Map, List, boolean)}.
	 */
	@Test
	public final void testGetSqlData()
	{
		String actual;
		String expected;
		String[] columns;
		String[] columnTypes;
		String[] columnTypesEdit;
		Map<Integer, Integer> lineOrder;
		List<String[]> lines;
		boolean withEndSemicolon;

		String[] lineOne;
		String[] lineTwo;
		String[] lineThree;

		lineOrder = null;
		columns = ArrayFactory.newArray("name", "age", "gender");
		columnTypes = ArrayFactory.newArray("text", "integer", "enum");
		columnTypesEdit = ArrayFactory.newArray("edit,',\",true", "edit,',\",true",
			"edit,',\",true");
		lineOne = ArrayFactory.newArray("John", "23", "male");
		lineTwo = ArrayFactory.newArray("Jim", "25", "male");
		lineThree = ArrayFactory.newArray("Mary", "21", "female");
		lines = ListFactory.newArrayList(lineOne, lineTwo, lineThree);

		withEndSemicolon = false;

		StringBuilder sqlData = CsvToSqlExtensions.getSqlData(columns, columnTypes, columnTypesEdit,
			lineOrder, lines, withEndSemicolon);
		actual = sqlData.toString();
		expected = "(\"john\", \"23\", \"male\"),\n(\"jim\", \"25\", \"male\"),\n(\"mary\", \"21\", \"female\"),\n";
		assertEquals(actual, expected);
	}

	/**
	 * Test method for {@link CsvToSqlExtensions}
	 */
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(CsvToSqlExtensions.class);
	}

}
