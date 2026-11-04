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
import java.util.function.BiConsumer;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import io.github.astrapi69.evaluate.object.evaluator.EqualsHashCodeAndToStringEvaluator;
import io.github.astrapi69.file.search.PathFinder;

/**
 * The unit test class for the class {@link FileCompareResultBean}.
 */
public class FileCompareResultBeanTest
{

	/**
	 * Test method for {@link FileCompareResultBean} constructors
	 */
	@Test
	public final void testConstructors()
	{
		FileCompareResultBean model = new FileCompareResultBean(PathFinder.getProjectDirectory(),
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
	 * Test method for {@link FileCompareResultBean#equals(Object)} ,
	 * {@link FileCompareResultBean#hashCode()} and {@link FileCompareResultBean#toString()}
	 */
	@Test
	public void testEqualsHashcodeAndToString()
	{
		final boolean expected;
		final boolean actual;

		FileCompareResultBean first = new FileCompareResultBean(PathFinder.getProjectDirectory(),
			PathFinder.getProjectDirectory());
		FileCompareResultBean second = new FileCompareResultBean(PathFinder.getSrcMainJavaDir(),
			PathFinder.getSrcTestJavaDir());
		FileCompareResultBean third = new FileCompareResultBean(PathFinder.getProjectDirectory(),
			PathFinder.getProjectDirectory());
		FileCompareResultBean fourth = new FileCompareResultBean(PathFinder.getProjectDirectory(),
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

	static Stream<Arguments> flagSetters()
	{
		return Stream.of(Arguments.of("absolute path equality",
			(BiConsumer<FileCompareResultBean, Boolean>)FileCompareResultBean::setAbsolutePathEquality),
			Arguments.of("file extension equality",
				(BiConsumer<FileCompareResultBean, Boolean>)FileCompareResultBean::setFileExtensionEquality),
			Arguments.of("last modified equality",
				(BiConsumer<FileCompareResultBean, Boolean>)FileCompareResultBean::setLastModifiedEquality),
			Arguments.of("length equality",
				(BiConsumer<FileCompareResultBean, Boolean>)FileCompareResultBean::setLengthEquality),
			Arguments.of("name equality",
				(BiConsumer<FileCompareResultBean, Boolean>)FileCompareResultBean::setNameEquality));
	}

	static Stream<Arguments> beansWithDifferentFiles()
	{
		final File other = new File("other.txt");
		return Stream.of(
			Arguments.of("this bean has no source file", new FileCompareResultBean(null, COMPARE),
				new FileCompareResultBean(SOURCE, COMPARE)),
			Arguments.of("the other bean has no source file",
				new FileCompareResultBean(SOURCE, COMPARE),
				new FileCompareResultBean(null, COMPARE)),
			Arguments.of("the source files differ", new FileCompareResultBean(SOURCE, COMPARE),
				new FileCompareResultBean(other, COMPARE)),
			Arguments.of("this bean has no file to compare",
				new FileCompareResultBean(SOURCE, null),
				new FileCompareResultBean(SOURCE, COMPARE)),
			Arguments.of("the other bean has no file to compare",
				new FileCompareResultBean(SOURCE, COMPARE),
				new FileCompareResultBean(SOURCE, null)),
			Arguments.of("the files to compare differ", new FileCompareResultBean(SOURCE, COMPARE),
				new FileCompareResultBean(SOURCE, other)));
	}

	private static FileCompareResultBean newBeanWithEveryFlagSet()
	{
		final FileCompareResultBean bean = new FileCompareResultBean(SOURCE, COMPARE);
		bean.setAbsolutePathEquality(true);
		bean.setFileExtensionEquality(true);
		bean.setLastModifiedEquality(true);
		bean.setLengthEquality(true);
		bean.setNameEquality(true);
		return bean;
	}

	/**
	 * Test method for {@link FileCompareResultBean#equals(Object)}
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("flagSetters")
	public void equals_returnsFalse_whenOnlyOneFlagDiffers(final String flagName,
		final BiConsumer<FileCompareResultBean, Boolean> setter)
	{
		final FileCompareResultBean first = newBeanWithEveryFlagSet();
		final FileCompareResultBean second = newBeanWithEveryFlagSet();
		assertEquals(first, second);

		setter.accept(second, Boolean.FALSE);

		assertNotEquals(first, second);
	}

	/**
	 * Test method for {@link FileCompareResultBean#equals(Object)}
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("beansWithDifferentFiles")
	public void equals_returnsFalse_whenTheFilesDiffer(final String description,
		final FileCompareResultBean thisBean, final FileCompareResultBean otherBean)
	{
		assertNotEquals(thisBean, otherBean);
	}

	/**
	 * Test method for {@link FileCompareResultBean#equals(Object)} and
	 * {@link FileCompareResultBean#hashCode()}
	 */
	@Test
	public void equalsAndHashCode_treatTwoBeansWithoutFilesAsEqual()
	{
		final FileCompareResultBean first = new FileCompareResultBean(null, null);
		final FileCompareResultBean second = new FileCompareResultBean(null, null);

		assertEquals(first, second);
		assertEquals(first.hashCode(), second.hashCode());
	}

	/**
	 * Test method for {@link FileCompareResultBean#equals(Object)}
	 */
	@Test
	public void equals_returnsFalse_whenTheOtherBeanCannotEqualThisOne()
	{
		final FileCompareResultBean bean = new FileCompareResultBean(SOURCE, COMPARE);
		final FileCompareResultBean restrictive = new FileCompareResultBean(SOURCE, COMPARE)
		{
			@Override
			protected boolean canEqual(final Object other)
			{
				return false;
			}
		};

		assertNotEquals(bean, restrictive);
	}

	/**
	 * Test method for {@link FileCompareResultBean#setSource(File)} and
	 * {@link FileCompareResultBean#setCompare(File)}
	 */
	@Test
	public void setSourceAndSetCompare_replaceTheFilesTheBeanHolds()
	{
		final FileCompareResultBean bean = new FileCompareResultBean(SOURCE, COMPARE);
		final File newSource = new File("new-source.txt");
		final File newCompare = new File("new-compare.txt");

		bean.setSource(newSource);
		bean.setCompare(newCompare);

		assertEquals(newSource, bean.getSourceFile());
		assertEquals(newCompare, bean.getFileToCompare());
	}

	/**
	 * Test method for {@link FileCompareResultBean#hashCode()}
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("flagSetters")
	public void hashCode_differs_whenOnlyOneFlagDiffers(final String flagName,
		final BiConsumer<FileCompareResultBean, Boolean> setter)
	{
		final FileCompareResultBean first = newBeanWithEveryFlagSet();
		final FileCompareResultBean second = newBeanWithEveryFlagSet();
		assertEquals(first.hashCode(), second.hashCode());

		setter.accept(second, Boolean.FALSE);

		assertNotEquals(first.hashCode(), second.hashCode());
	}

	/**
	 * Test method for {@link FileCompareResultBean#toString()}
	 */
	@Test
	public void toString_namesEveryFlagWithItsValue_andBothFiles()
	{
		final FileCompareResultBean bean = new FileCompareResultBean(SOURCE, COMPARE);
		bean.setAbsolutePathEquality(true);
		bean.setLengthEquality(true);

		final String text = bean.toString();

		assertTrue(text.startsWith("FileCompareResultBean("), text);
		assertTrue(text.contains("absolutePathEquality=true"), text);
		assertTrue(text.contains("fileExtensionEquality=false"), text);
		assertTrue(text.contains("lastModifiedEquality=false"), text);
		assertTrue(text.contains("lengthEquality=true"), text);
		assertTrue(text.contains("nameEquality=false"), text);
		assertTrue(text.contains("source=" + SOURCE), text);
		assertTrue(text.contains("compare=" + COMPARE), text);
	}

}
