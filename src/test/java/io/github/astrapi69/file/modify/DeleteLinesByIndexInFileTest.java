package io.github.astrapi69.file.modify;

import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

import io.github.astrapi69.file.copy.CopyFileExtensions;
import io.github.astrapi69.file.create.FileFactory;
import io.github.astrapi69.file.delete.DeleteFileExtensions;
import io.github.astrapi69.file.read.ReadFileExtensions;
import io.github.astrapi69.file.search.PathFinder;

/**
 * The class {@link DeleteLinesByIndexInFileTest} provides unit tests for the class
 * {@link DeleteLinesByIndexInFile}
 */
public class DeleteLinesByIndexInFileTest
{

	/**
	 * Test method for {@link DeleteLinesByIndexInFile#apply(Integer, String)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	@Test
	public void testApply() throws IOException
	{
		File inputFile;
		File testFile;
		File parent = PathFinder.getRelativePath(PathFinder.getSrcTestResourcesDir(), "resources");

		inputFile = FileFactory.newFile(parent, "test-csv-data.csv");
		testFile = FileFactory.newFile(parent, "test-delete-lines-data.csv");
		CopyFileExtensions.copyFile(inputFile, testFile);

		Path inputFilePath = testFile.toPath();
		List<Integer> linesToDelete = Arrays.asList(1, 4); // Example line indexes to delete

		DeleteLinesByIndexInFile deleter = new DeleteLinesByIndexInFile(linesToDelete);

		ModifyFileExtensions.modifyFile(inputFilePath, deleter);

		List<String> linesInList = ReadFileExtensions.readLinesInList(inputFile);

		String content = ReadFileExtensions.fromFile(testFile);

		assertFalse(content.contains(linesInList.get(1)));
		assertFalse(content.contains(linesInList.get(4)));

		assertTrue(content.contains(linesInList.get(0)));
		assertTrue(content.contains(linesInList.get(2)));
		assertTrue(content.contains(linesInList.get(3)));

		DeleteFileExtensions.deleteFile(testFile);
	}
}