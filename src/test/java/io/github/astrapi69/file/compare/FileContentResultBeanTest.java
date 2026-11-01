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
package io.github.astrapi69.file.compare;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.junit.jupiter.api.Test;

import io.github.astrapi69.evaluate.object.evaluator.EqualsHashCodeAndToStringEvaluator;
import io.github.astrapi69.file.search.PathFinder;

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

	/** A file that the bean only holds, the file system is never touched */
	private static final File SOURCE = new File("source.txt");

	/** A second file that the bean only holds, the file system is never touched */
	private static final File COMPARE = new File("compare.txt");

	private static FileContentResultBean newBeanWithEqualContent()
	{
		final FileContentResultBean bean = new FileContentResultBean(SOURCE, COMPARE);
		bean.setNameEquality(true);
		bean.setContentEquality(true);
		return bean;
	}

	/**
	 * Test method for {@link FileContentResultBean#equals(Object)} and
	 * {@link FileContentResultBean#hashCode()}
	 */
	@Test
	public void equalsAndHashCode_agree_whenBothBeansReportEqualContent()
	{
		final FileContentResultBean first = newBeanWithEqualContent();
		final FileContentResultBean second = newBeanWithEqualContent();

		assertEquals(first, second);
		assertEquals(first.hashCode(), second.hashCode());
		assertTrue(first.getContentEquality());
	}

	/**
	 * Test method for {@link FileContentResultBean#equals(Object)}
	 */
	@Test
	public void equals_returnsFalse_whenOnlyTheContentEqualityDiffers()
	{
		final FileContentResultBean first = newBeanWithEqualContent();
		final FileContentResultBean second = newBeanWithEqualContent();

		second.setContentEquality(false);

		assertNotEquals(first, second);
	}

	/**
	 * Test method for {@link FileContentResultBean#equals(Object)}
	 */
	@Test
	public void equals_returnsFalse_whenOnlyAnInheritedFlagDiffers()
	{
		final FileContentResultBean first = newBeanWithEqualContent();
		final FileContentResultBean second = newBeanWithEqualContent();

		second.setNameEquality(false);

		assertNotEquals(first, second);
	}

	/**
	 * Test method for {@link FileContentResultBean#equals(Object)}
	 */
	@Test
	public void equals_returnsFalse_whenTheOtherBeanCannotEqualThisOne()
	{
		final FileContentResultBean bean = newBeanWithEqualContent();
		final FileContentResultBean restrictive = new FileContentResultBean(SOURCE, COMPARE)
		{
			@Override
			protected boolean canEqual(final Object other)
			{
				return false;
			}
		};
		restrictive.setNameEquality(true);
		restrictive.setContentEquality(true);

		assertNotEquals(bean, restrictive);
	}

	/**
	 * Test method for {@link FileContentResultBean#equals(Object)}
	 */
	@Test
	public void equals_returnsFalse_whenTheOtherObjectIsOnlyAFileCompareResultBean()
	{
		final FileContentResultBean bean = new FileContentResultBean(SOURCE, COMPARE);
		final FileCompareResultBean compareOnly = new FileCompareResultBean(SOURCE, COMPARE);

		assertNotEquals(bean, compareOnly);
	}

}
