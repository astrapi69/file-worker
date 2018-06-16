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
import static org.testng.AssertJUnit.assertEquals;

import java.util.List;

import org.testng.annotations.Test;

import de.alpharogroup.collections.array.ArrayFactory;
import de.alpharogroup.collections.list.ListFactory;
import de.alpharogroup.evaluate.object.EqualsHashCodeAndToStringEvaluator;

/**
 * The unit test class for the class {@link CsvBean}
 */
public class CsvBeanTest
{

	/**
	 * Test method for {@link CsvBean} constructors and builders
	 */
	@Test
	public final void testConstructors()
	{
		CsvBean model;
		model = newCsvBean();
		assertNotNull(model);
		model = new CsvBean();
		assertNotNull(model);
	}

	/**
	 * Test method for {@link CsvBean#equals(Object)} , {@link CsvBean#hashCode()} and
	 * {@link CsvBean#toString()}
	 */
	@Test(enabled = false)
	public void testEqualsHashcodeAndToString()
	{
		final boolean expected;
		final boolean actual;
		String[] headers;
		String[] columnTypes;
		String[] lineOne;
		String[] lineTwo;
		String[] lineThree;
		List<String[]> lines;
		headers = ArrayFactory.newArray("foo", "bar", "bla");
		columnTypes = ArrayFactory.newArray("integer", "integer", "integer");
		lineOne = ArrayFactory.newArray("1", "23", "3");
		lineTwo = ArrayFactory.newArray("4", "25", "9");
		lineThree = ArrayFactory.newArray("6", "21", "8");
		lines = ListFactory.newArrayList(lineOne, lineTwo, lineThree);

		CsvBean first = newCsvBean();
		CsvBean second = CsvBean.builder().headers(headers).columnTypes(columnTypes).lines(lines).build();
		CsvBean third = newCsvBean();
		CsvBean fourth = newCsvBean();

		actual = EqualsHashCodeAndToStringEvaluator.evaluateEqualsHashcodeAndToString(first, second,
			third, fourth);
		expected = true;
		assertEquals(expected, actual);
	}
	
	/**
	 * Factory method for create {@link CsvBean} for unit tests.
	 *
	 * @return the csv bean
	 */
	public static CsvBean newCsvBean() {
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

		model = CsvBean.builder().headers(headers).columnTypes(columnTypes).lines(lines).build();
		return model;
	}

}
