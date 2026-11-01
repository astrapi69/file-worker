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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import io.github.astrapi69.evaluate.object.evaluator.EqualsHashCodeAndToStringEvaluator;
import io.github.astrapi69.file.search.SearchFileAttributesBean.SearchFileAttributesBeanBuilder;

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


	static Stream<Arguments> singleFlagChanges()
	{
		return Stream.of(
			Arguments.of("ignoreContentEquality",
				(UnaryOperator<SearchFileAttributesBeanBuilder>)builder -> builder
					.ignoreContentEquality(true)),
			Arguments.of("ignoreExtensionEquality",
				(UnaryOperator<SearchFileAttributesBeanBuilder>)builder -> builder
					.ignoreExtensionEquality(true)),
			Arguments.of("ignoreLastModified",
				(UnaryOperator<SearchFileAttributesBeanBuilder>)builder -> builder
					.ignoreLastModified(true)),
			Arguments.of("ignoreLengthEquality",
				(UnaryOperator<SearchFileAttributesBeanBuilder>)builder -> builder
					.ignoreLengthEquality(true)),
			Arguments.of("ignoreNameEquality",
				(UnaryOperator<SearchFileAttributesBeanBuilder>)builder -> builder
					.ignoreNameEquality(true)));
	}

	@ParameterizedTest
	@CsvSource({ "true, false, true, false, true", "false, true, false, true, false" })
	void builder_buildsABeanWithEveryFlagAsSet(boolean content, boolean extension,
		boolean lastModified, boolean length, boolean name)
	{
		SearchFileAttributesBean bean = SearchFileAttributesBean.builder()
			.ignoreContentEquality(content).ignoreExtensionEquality(extension)
			.ignoreLastModified(lastModified).ignoreLengthEquality(length).ignoreNameEquality(name)
			.build();

		assertEquals(content, bean.isIgnoreContentEquality());
		assertEquals(extension, bean.isIgnoreExtensionEquality());
		assertEquals(lastModified, bean.isIgnoreLastModified());
		assertEquals(length, bean.isIgnoreLengthEquality());
		assertEquals(name, bean.isIgnoreNameEquality());
		assertEquals(new SearchFileAttributesBean(content, extension, lastModified, length, name),
			bean);
	}

	@ParameterizedTest
	@CsvSource({ "true, false, true, false, true", "false, true, false, true, false" })
	void toBuilder_buildsAnEqualBean(boolean content, boolean extension, boolean lastModified,
		boolean length, boolean name)
	{
		SearchFileAttributesBean original = new SearchFileAttributesBean(content, extension,
			lastModified, length, name);

		SearchFileAttributesBean copy = original.toBuilder().build();

		assertEquals(original, copy);
		assertEquals(original.hashCode(), copy.hashCode());
		assertEquals(original.toString(), copy.toString());
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("singleFlagChanges")
	void equals_isFalse_whenOnlyOneFlagDiffers(String flag,
		UnaryOperator<SearchFileAttributesBeanBuilder> change)
	{
		SearchFileAttributesBean original = SearchFileAttributesBean.builder().build();

		SearchFileAttributesBean changed = change.apply(original.toBuilder()).build();

		assertNotEquals(original, changed, flag + " differs");
		assertNotEquals(changed, original, flag + " differs");
		assertEquals(changed, change.apply(SearchFileAttributesBean.builder()).build(),
			"the same change gives an equal bean");
	}

	@Test
	void equals_isFalse_whenTheOtherBeanRefusesEquality()
	{
		SearchFileAttributesBean bean = new SearchFileAttributesBean(false, false, false, false,
			false);
		SearchFileAttributesBean refusing = new SearchFileAttributesBean(false, false, false, false,
			false)
		{
			@Override
			protected boolean canEqual(final Object other)
			{
				return false;
			}
		};

		assertNotEquals(bean, refusing);
	}

	@Test
	void builderToString_listsEveryFlag()
	{
		SearchFileAttributesBeanBuilder builder = SearchFileAttributesBean.builder()
			.ignoreExtensionEquality(true).ignoreLengthEquality(true);

		assertEquals(
			"SearchFileAttributesBean.SearchFileAttributesBeanBuilder{"
				+ "ignoreContentEquality=false, ignoreExtensionEquality=true, "
				+ "ignoreLastModified=false, ignoreLengthEquality=true, ignoreNameEquality=false}",
			builder.toString());
	}

}
