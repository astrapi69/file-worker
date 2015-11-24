/**
 * The MIT License
 *
 * Copyright (C) 2007 Asterios Raptis
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.testng.AssertJUnit;
import org.testng.annotations.Test;

import de.alpharogroup.file.FileTestCase;
import de.alpharogroup.file.compare.interfaces.IFileCompareResultBean;
import de.alpharogroup.file.compare.interfaces.IFileContentResultBean;
import de.alpharogroup.file.write.WriteFileUtils;

/**
 * The Class CompareFileUtilsTest.
 */
public class CompareFileUtilsTest extends FileTestCase
{

	// @Test
	// public void testCompareFileContentByBytes() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testCompareFileContentByLines() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testCompareFiles() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testCompleteCompare() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testSimpleCompareFiles() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testValidateEqualityIFileCompareResultBean() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testValidateEqualityIFileContentResultBean() {
	// fail("Not yet implemented");
	// }
	//
	/**
	 * Test find equal files file.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testFindEqualFilesFile() throws IOException
	{
		final List<File> expectedFiles = new ArrayList<>();
		final File testFile1 = new File(this.testDir.getAbsoluteFile(),
			"testFindFilesRecursive.txt");
		expectedFiles.add(testFile1);
		final File testFile2 = new File(this.testDir.getAbsoluteFile(),
			"testFindFilesRecursive.tft");

		final File testFile3 = new File(this.deepDir, "testFindFilesRecursive.cvs");
		WriteFileUtils.string2File(testFile1, "Its a beautifull day!!!");
		WriteFileUtils.string2File(testFile2, "Its a beautifull evening!!!");
		WriteFileUtils.string2File(testFile3, "Its a beautifull night!!!");

		final File testFile4 = new File(this.deepDir, "testFindFilesRecursive.txt");
		WriteFileUtils.string2File(testFile4, "Its a beautifull day!!!");

		final List<IFileCompareResultBean> found = CompareFileUtils.findEqualFiles(this.testDir);

		AssertJUnit.assertTrue("found.size() is not equal 1.", found.size() == 1);

		final List<IFileContentResultBean> contentfound = CompareFileUtils
			.findEqualFilesWithSameContent(this.testDir);

		AssertJUnit.assertTrue("contentfound() is not equal 1.", contentfound.size() == 1);

	}

	/**
	 * Test find equal files file file.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testFindEqualFilesFileFile() throws IOException
	{
		final List<File> expectedFiles = new ArrayList<>();
		final File testFile1 = new File(this.testDir.getAbsoluteFile(),
			"testFindFilesRecursive.txt");
		expectedFiles.add(testFile1);
		final File testFile2 = new File(this.testDir.getAbsoluteFile(),
			"testFindFilesRecursive.tft");

		final File testFile3 = new File(this.deepDir, "testFindFilesRecursive.cvs");
		WriteFileUtils.string2File(testFile1, "Its a beautifull day!!!");
		WriteFileUtils.string2File(testFile2, "Its a beautifull evening!!!");
		WriteFileUtils.string2File(testFile3, "Its a beautifull night!!!");

		final File testFile4 = new File(this.secondTestDir.getAbsoluteFile(),
			"testFindFilesRecursive.txt");

		final File testFile5 = new File(this.secondTestDir.getAbsoluteFile(),
			"testFindFilesRecursive.tft");

		final File testFile6 = new File(this.secondTestDir, "testFindFilesRecursive.cvs");

		WriteFileUtils.string2File(testFile4, "Its a beautifull day!!!");
		WriteFileUtils.string2File(testFile5, "Its a beautifull evening!!!????");
		WriteFileUtils.string2File(testFile6, "Its a beautifull night!!!");


		final List<IFileCompareResultBean> found = CompareFileUtils.findEqualFiles(this.testDir,
			this.secondTestDir);

		AssertJUnit.assertTrue("found.size() is not equal 3.", found.size() == 2);

		final List<IFileContentResultBean> contentfound = CompareFileUtils
			.findEqualFilesWithSameContent(this.testDir, this.secondTestDir);

		AssertJUnit.assertTrue("contentfound() is not equal 3.", contentfound.size() == 2);

	}
}
