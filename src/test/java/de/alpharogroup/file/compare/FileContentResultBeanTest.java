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
package de.alpharogroup.file.compare;

import static org.testng.Assert.assertNotNull;
import static org.testng.AssertJUnit.assertEquals;

import org.testng.annotations.Test;

import de.alpharogroup.evaluate.object.EqualsHashCodeAndToStringEvaluator;
import de.alpharogroup.file.search.PathFinder;

/**
 * The unit test class for the class {@link FileContentResultBean}.
 */
public class FileContentResultBeanTest
{

	/**
	 * Test method for {@link FileContentResultBean} constructors
	 */
	@Test
	public final void testConstructors()
	{
		FileContentResultBean model = new FileContentResultBean(PathFinder.getProjectDirectory(),
			PathFinder.getProjectDirectory());
		assertNotNull(model);
		model.setAbsolutePathEquality(true);
		model.setCompare(PathFinder.getSrcMainJavaDir());
		model.setFileExtensionEquality(true);
		model.setLastModifiedEquality(true);
		model.setLengthEquality(true);
		model.setNameEquality(true);
		model.setSource(PathFinder.getSrcTestJavaDir());
	}

	/**
	 * Test method for {@link FileContentResultBean#equals(Object)} ,
	 * {@link FileContentResultBean#hashCode()} and {@link FileContentResultBean#toString()}
	 */
	@Test
	public void testEqualsHashcodeAndToString()
	{
		final boolean expected;
		final boolean actual;

		FileContentResultBean first = new FileContentResultBean(PathFinder.getProjectDirectory(),
			PathFinder.getProjectDirectory());
		FileContentResultBean second = new FileContentResultBean(PathFinder.getSrcMainJavaDir(),
			PathFinder.getSrcTestJavaDir());
		FileContentResultBean third = new FileContentResultBean(PathFinder.getProjectDirectory(),
			PathFinder.getProjectDirectory());
		FileContentResultBean fourth = new FileContentResultBean(PathFinder.getProjectDirectory(),
			PathFinder.getProjectDirectory());

		actual = EqualsHashCodeAndToStringEvaluator.evaluateEqualsHashcodeAndToString(first, second,
			third, fourth);
		expected = true;
		assertEquals(expected, actual);
	}
}
