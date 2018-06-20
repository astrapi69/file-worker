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

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.meanbean.test.BeanTester;
import org.meanbean.test.Configuration;
import org.meanbean.test.ConfigurationBuilder;
import org.testng.annotations.Test;

import de.alpharogroup.collections.array.ArrayFactory;
import de.alpharogroup.collections.list.ListFactory;
import de.alpharogroup.evaluate.object.EqualsHashCodeAndToStringEvaluator;
import de.alpharogroup.meanbean.factories.ListStringArrayFactory;
import de.alpharogroup.meanbean.factories.StringArrayFactory;

/**
 * The unit test class for the class {@link CsvBean}
 */
public class CsvBeanTest
{

	/**
	 * Factory method for create {@link CsvBean} with builder for unit tests.
	 *
	 * @return the csv bean
	 */
	public static CsvBean newCsvBeanWithBuilder()
	{
		CsvBean model;
		String[] headers;
		String[] columnTypes;
		String[] columnTypesEdit;
		String[] lineOne;
		String[] lineTwo;
		String[] lineThree;
		List<String[]> lines;
		columnTypesEdit = ArrayFactory.newArray("edit", "'", "\"", "true");
		headers = ArrayFactory.newArray("name", "age", "gender");
		columnTypes = ArrayFactory.newArray("text", "integer", "enum");
		lineOne = ArrayFactory.newArray("John", "23", "male");
		lineTwo = ArrayFactory.newArray("Jim", "25", "male");
		lineThree = ArrayFactory.newArray("Mary", "21", "female");
		lines = ListFactory.newArrayList(lineOne, lineTwo, lineThree);

		model = CsvBean.builder().headers(headers).columnTypes(columnTypes)
			.columnTypesEdit(columnTypesEdit).lines(lines).build();
		return model;
	}

	/**
	 * Factory method for create {@link CsvBean} with constructor for unit tests.
	 *
	 * @return the csv bean
	 */
	public static CsvBean newCsvBeanWithConstructor()
	{
		CsvBean model;
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

		model = new CsvBean(headers, columnTypes, lines);
		return model;
	}

	/**
	 * Test method for {@link CsvBean#clone()}.
	 */
	@Test(enabled = true)
	public void testClone()
	{
		CsvBean actual;
		CsvBean expected;
		actual = newCsvBeanWithBuilder();
		expected = (CsvBean)actual.clone();
		assertTrue(Arrays.deepEquals(expected.getColumnTypes(), actual.getColumnTypes()));
		assertTrue(Arrays.deepEquals(expected.getColumnTypesEdit(), actual.getColumnTypesEdit()));
		assertTrue(Arrays.deepEquals(expected.getHeaders(), actual.getHeaders()));
		assertEquals(expected.getLineOrder(), actual.getLineOrder());
	}

	/**
	 * Test method for {@link CsvBean} constructors and builders
	 */
	@Test
	public final void testConstructors()
	{
		CsvBean model;
		model = newCsvBeanWithBuilder();
		assertNotNull(model);
		model = new CsvBean();
		assertNotNull(model);
		model = newCsvBeanWithConstructor();
		assertNotNull(model);
	}

	/**
	 * Test method for {@link CsvBean#equals(Object)} , {@link CsvBean#hashCode()} and
	 * {@link CsvBean#toString()}
	 */
	@Test(enabled = true)
	public void testEqualsHashcodeAndToString()
	{
		boolean expected;
		boolean actual;
		String[] headers;
		String[] columnTypes;
		String[] columnTypesEdit;
		String[] lineOne;
		String[] lineTwo;
		String[] lineThree;
		List<String[]> lines;
		headers = ArrayFactory.newArray("foo", "bar", "bla");
		columnTypes = ArrayFactory.newArray("integer", "integer", "integer");
		columnTypesEdit = ArrayFactory.newArray("autoincrement", "1");
		lineOne = ArrayFactory.newArray("1", "23", "3");
		lineTwo = ArrayFactory.newArray("4", "25", "9");
		lineThree = ArrayFactory.newArray("6", "21", "8");
		lines = ListFactory.newArrayList(lineOne, lineTwo, lineThree);

		CsvBean first = newCsvBeanWithBuilder();
		CsvBean second = new CsvBean(headers, columnTypes, columnTypesEdit, lines);
		CsvBean third = newCsvBeanWithBuilder();
		CsvBean fourth = newCsvBeanWithBuilder();

		actual = EqualsHashCodeAndToStringEvaluator.evaluateEqualsHashcodeAndToString(first, second,
			third, fourth);
		expected = true;
		assertEquals(expected, actual);

		actual = first.equals("foo");
		expected = false;
		assertEquals(expected, actual);

		first.setLines(null);
		actual = first.equals(third);
		expected = false;
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link CsvBean}
	 */
	@Test
	public void testWithBeanTester()
	{
		Configuration configuration = new ConfigurationBuilder()
			.overrideFactory("headers", new StringArrayFactory())
			.overrideFactory("columnTypes", new StringArrayFactory())
			.overrideFactory("columnTypesEdit", new StringArrayFactory())
			.overrideFactory("lines", new ListStringArrayFactory()).build();
		final BeanTester beanTester = new BeanTester();
		beanTester.addCustomConfiguration(CsvBean.class, configuration);
		beanTester.testBean(CsvBean.class);
	}

}
