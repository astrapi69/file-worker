package de.alpharogroup.file.compare;

import static org.testng.Assert.assertNotNull;
import static org.testng.AssertJUnit.assertEquals;

import org.testng.annotations.Test;

import de.alpharogroup.evaluate.object.EqualsHashCodeAndToStringEvaluator;
import de.alpharogroup.file.search.PathFinder;

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
		FileCompareResultBean model = new FileCompareResultBean(PathFinder.getProjectDirectory(), PathFinder.getProjectDirectory());
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
	 * Test method for {@link FileCompareResultBean#equals(Object)} , {@link FileCompareResultBean#hashCode()} and
	 * {@link FileCompareResultBean#toString()}
	 */
	@Test
	public void testEqualsHashcodeAndToString()
	{
		final boolean expected;
		final boolean actual;

		FileCompareResultBean first = new FileCompareResultBean(PathFinder.getProjectDirectory(), PathFinder.getProjectDirectory());
		FileCompareResultBean second = new FileCompareResultBean(PathFinder.getSrcMainJavaDir(), PathFinder.getSrcTestJavaDir());
		FileCompareResultBean third = new FileCompareResultBean(PathFinder.getProjectDirectory(), PathFinder.getProjectDirectory());
		FileCompareResultBean fourth = new FileCompareResultBean(PathFinder.getProjectDirectory(), PathFinder.getProjectDirectory());

		actual = EqualsHashCodeAndToStringEvaluator.evaluateEqualsHashcodeAndToString(first, second,
			third, fourth);
		expected = true;
		assertEquals(expected, actual);
	}

}
