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
package io.github.astrapi69.file.search;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import io.github.astrapi69.evaluate.object.evaluator.EqualsHashCodeAndToStringEvaluator;

/**
 * The unit test class for the class {@link SearchFileAttributesBean}
 */
public class SearchFileAttributesBeanTest
{

	/**
	 * Test method for {@link SearchFileAttributesBean} constructors and builders
	 */
	@Test
	public final void testConstructors()
	{
		SearchFileAttributesBean model = new SearchFileAttributesBean(false, false, false, false,
			false);
		assertNotNull(model);
		model = SearchFileAttributesBean.builder().build();
		assertNotNull(model);
	}

	/**
	 * Test method for {@link SearchFileAttributesBean#equals(Object)} ,
	 * {@link SearchFileAttributesBean#hashCode()} and {@link SearchFileAttributesBean#toString()}
	 */
	@Test
	public void testEqualsHashcodeAndToString()
	{
		final boolean expected;
		final boolean actual;

		SearchFileAttributesBean first = new SearchFileAttributesBean(false, false, false, false,
			false);
		SearchFileAttributesBean second = new SearchFileAttributesBean(true, true, true, true,
			true);
		SearchFileAttributesBean third = SearchFileAttributesBean.builder().build();
		SearchFileAttributesBean fourth = new SearchFileAttributesBean(false, false, false, false,
			false);

		actual = EqualsHashCodeAndToStringEvaluator.evaluateEqualsHashcodeAndToString(first, second,
			third, fourth);
		expected = true;
		assertEquals(expected, actual);
	}

}
