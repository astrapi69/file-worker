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
package io.github.astrapi69.file;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

import java.util.List;

import io.github.astrapi69.file.GeneratorExtensions;
import org.meanbean.test.BeanTester;
import org.testng.annotations.Test;

import io.github.astrapi69.collections.CollectionExtensions;
import io.github.astrapi69.collections.list.ListFactory;

/**
 * The unit test class for the class {@link GeneratorExtensions}.
 * 
 * @version 1.0
 * @author Asterios Raptis
 */
public class GeneratorExtensionsTest
{

	/**
	 * Test method for
	 * {@link GeneratorExtensions#newConstantsFromStringList(List, String, String, boolean)}.
	 */
	@Test
	public void testNewConstantsFromStringList()
	{
		List<String> actual;
		List<String> expected;
		List<String> data;
		String prefix;
		String suffix;
		boolean withQuotation;

		data = ListFactory.newArrayList("fooDoo", "bar");
		prefix = "pre";
		suffix = "fix";
		withQuotation = false;
		actual = GeneratorExtensions.newConstantsFromStringList(data, prefix, suffix,
			withQuotation);
		expected = ListFactory.newArrayList("public static final String PREFOODOOFIX = \"fooDoo\";",
			"public static final String PREBARFIX = \"bar\";");
		assertTrue(CollectionExtensions.isEqualCollection(actual, expected));

		data = ListFactory.newArrayList("fooDoo", "bar");
		withQuotation = true;
		actual = GeneratorExtensions.newConstantsFromStringList(data, prefix, suffix,
			withQuotation);
		expected = ListFactory.newArrayList("public static final String PREFOODOOFIX = fooDoo;",
			"public static final String PREBARFIX = bar;");
		assertTrue(CollectionExtensions.isEqualCollection(actual, expected));
	}

	/**
	 * Test method for {@link GeneratorExtensions#newStaticArrayVariable(String, List)}
	 */
	@Test
	public void testNewStaticArrayVariable()
	{
		String actual;
		String expected;
		List<String> data;
		String arrayName;
		arrayName = "fooArray";
		data = ListFactory.newArrayList("foo", "bar");
		actual = GeneratorExtensions.newStaticArrayVariable(arrayName, data);
		expected = "public static final String []FOOARRAY = {\"foo\", \"bar\"};";
		assertEquals(actual, expected);
	}

	/**
	 * Test method for {@link GeneratorExtensions}
	 */
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(GeneratorExtensions.class);
	}

}
