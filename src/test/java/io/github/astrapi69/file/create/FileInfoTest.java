package io.github.astrapi69.file.create;

import io.github.astrapi69.test.object.Company;
import org.meanbean.test.BeanTester;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

/**
 * The unit test class for the class {@link FileInfo}
 */
public class FileInfoTest
{

	/**
	 * Test method for {@link FileInfo} constructors and builders
	 */
	@Test
	public final void testConstructors()
	{
		FileInfo model = new FileInfo();
		assertNotNull(model);
		model = new FileInfo("foo", "bar");
		assertNotNull(model);
		model = FileInfo.builder().build();
		assertNotNull(model);
		String toString = model.toString();
		assertNotNull(toString);
		assertTrue(model.equals((Object)FileInfo.builder().build()));
		int hashCode = model.hashCode();
		assertTrue(0 < hashCode);
	}

	/**
	 * Test method for {@link FileInfo}
	 */
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(FileInfo.class);
	}
}
