package de.alpharogroup.file.modify;

import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.meanbean.factories.ObjectCreationException;
import org.meanbean.test.BeanTestException;
import org.meanbean.test.BeanTester;
import org.testng.annotations.Test;

import de.alpharogroup.file.delete.DeleteFileExtensions;
import de.alpharogroup.file.modify.api.FileChangable;
import de.alpharogroup.file.read.ReadFileExtensions;
import de.alpharogroup.file.search.PathFinder;

/**
 * The unit test class for the class {@link ModifyFileExtensions}
 */
public class ModifyFileExtensionsTest
{

	/**
	 * Test method for
	 * {@link ModifyFileExtensions#modifyFile(java.nio.file.Path, java.nio.file.Path, FileChangable)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testModifyFile() throws IOException
	{
		File inputFile;
		File outputFile;

		inputFile = new File(
			PathFinder.getRelativePath(PathFinder.getSrcTestResourcesDir(), "resources"),
			"test-csv-data.csv");
		outputFile = new File(
			PathFinder.getRelativePath(PathFinder.getSrcTestResourcesDir(), "resources"),
			"modified-test-csv-data.csv");
		String add = "|#foo-bar#|";
		ModifyFileExtensions.modifyFile(inputFile.toPath(), outputFile.toPath(), (count, input) -> {
			String alteredLine = input + add;
			return alteredLine;
		});
		List<String> linesInList = ReadFileExtensions.readLinesInList(outputFile);
		linesInList.stream().forEach(line -> assertTrue(line.endsWith(add)));

		DeleteFileExtensions.delete(outputFile);

	}

	/**
	 * Test method for {@link ModifyFileExtensions}
	 */
	@Test(expectedExceptions = { BeanTestException.class, ObjectCreationException.class })
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(ModifyFileExtensions.class);
	}

}
