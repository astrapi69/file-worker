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
		FileContentResultBean model = new FileContentResultBean(PathFinder.getProjectDirectory(), PathFinder.getProjectDirectory());
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
	 * Test method for {@link FileContentResultBean#equals(Object)} , {@link FileContentResultBean#hashCode()} and
	 * {@link FileContentResultBean#toString()}
	 */
	@Test
	public void testEqualsHashcodeAndToString()
	{
		final boolean expected;
		final boolean actual;

		FileContentResultBean first = new FileContentResultBean(PathFinder.getProjectDirectory(), PathFinder.getProjectDirectory());
		FileContentResultBean second = new FileContentResultBean(PathFinder.getSrcMainJavaDir(), PathFinder.getSrcTestJavaDir());
		FileContentResultBean third = new FileContentResultBean(PathFinder.getProjectDirectory(), PathFinder.getProjectDirectory());
		FileContentResultBean fourth = new FileContentResultBean(PathFinder.getProjectDirectory(), PathFinder.getProjectDirectory());

		actual = EqualsHashCodeAndToStringEvaluator.evaluateEqualsHashcodeAndToString(first, second,
			third, fourth);
		expected = true;
		assertEquals(expected, actual);
	}
}
