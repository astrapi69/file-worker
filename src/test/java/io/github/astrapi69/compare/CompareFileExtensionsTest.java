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
package io.github.astrapi69.compare;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.meanbean.test.BeanTester;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.alpharogroup.collections.CollectionExtensions;
import de.alpharogroup.collections.list.ListFactory;
import io.github.astrapi69.FileTestCase;
import io.github.astrapi69.compare.api.IFileCompareResultBean;
import io.github.astrapi69.compare.api.IFileContentResultBean;
import io.github.astrapi69.delete.DeleteFileExtensions;
import io.github.astrapi69.write.WriteFileExtensions;

/**
 * The unit test class for the class {@link CompareFileExtensions}.
 */
public class CompareFileExtensionsTest extends FileTestCase
{

	IFileContentResultBean actual;
	IFileContentResultBean expected;
	File testFile1;
	File testFile2;
	File testFile3;
	File testFile4;

	@Override
	@BeforeMethod
	protected void setUp() throws Exception
	{
		super.setUp();
		testFile1 = new File(this.testDir.getAbsoluteFile(), "testFindFilesRecursive.txt");
		testFile2 = new File(this.testDir.getAbsoluteFile(), "testFindFilesRecursive.tft");

		testFile3 = new File(this.deepDir, "testFindFilesRecursive.cvs");

		testFile4 = new File(this.deepDir, "testFindFilesRecursive.txt");

		WriteFileExtensions.string2File(testFile1, "Its a beautifull day!!!");
		WriteFileExtensions.string2File(testFile2, "Its a beautifull evening!!!");
		WriteFileExtensions.string2File(testFile3, "Its a beautifull day!!!");
		WriteFileExtensions.string2File(testFile4, "Its a beautifull day!!!");
	}

	@Override
	@AfterMethod
	protected void tearDown() throws Exception
	{
		super.tearDown();
		DeleteFileExtensions.delete(testFile1);
		DeleteFileExtensions.delete(testFile2);
		DeleteFileExtensions.delete(testFile3);
		DeleteFileExtensions.delete(testFile4);
	}

	/**
	 * Test method for {@link CompareFileExtensions#compareFileContentByBytes(File, File)}.
	 */
	@Test(enabled = true)
	public void testCompareFileContentByBytes()
	{

		actual = CompareFileExtensions.compareFileContentByBytes(testFile1, testFile2);
		expected = new FileContentResultBean(testFile1, testFile2);
		expected.setAbsolutePathEquality(false);
		expected.setContentEquality(true);
		expected.setFileExtensionEquality(false);
		expected.setLastModifiedEquality(true);
		expected.setLengthEquality(false);
		expected.setNameEquality(true);
		assertEquals(expected, actual);

		actual = CompareFileExtensions.compareFileContentByBytes(testFile1, testFile3);
		expected = new FileContentResultBean(testFile1, testFile3);
		expected.setAbsolutePathEquality(false);
		expected.setContentEquality(true);
		expected.setFileExtensionEquality(false);
		expected.setLastModifiedEquality(true);
		expected.setLengthEquality(true);
		expected.setNameEquality(true);
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link CompareFileExtensions#compareFileContentByLines(File, File)}.
	 */
	@Test(enabled = false) // TODO FIXME
	public void testCompareFileContentByLines()
	{
		actual = CompareFileExtensions.compareFileContentByLines(testFile1, testFile2);
		expected = new FileContentResultBean(testFile1, testFile2);
		expected.setAbsolutePathEquality(false);
		expected.setContentEquality(true);
		expected.setFileExtensionEquality(false);
		expected.setLastModifiedEquality(true);
		expected.setLengthEquality(false);
		expected.setNameEquality(true);
		assertEquals(expected, actual);

		actual = CompareFileExtensions.compareFileContentByLines(testFile1, testFile3);
		expected = new FileContentResultBean(testFile1, testFile3);
		expected.setAbsolutePathEquality(false);
		expected.setContentEquality(true);
		expected.setFileExtensionEquality(false);
		expected.setLastModifiedEquality(true);
		expected.setLengthEquality(true);
		expected.setNameEquality(true);
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link CompareFileExtensions#compareFiles(File, File)}.
	 */
	@Test(enabled = true)
	public void testCompareFilesFileFile()
	{
		actual = CompareFileExtensions.compareFiles(testFile1, testFile2);
		expected = new FileContentResultBean(testFile1, testFile2);
		expected.setAbsolutePathEquality(true);
		expected.setContentEquality(true);
		expected.setFileExtensionEquality(false);
		expected.setLastModifiedEquality(true);
		expected.setLengthEquality(false);
		expected.setNameEquality(true);
		assertEquals(expected, actual);

		actual = CompareFileExtensions.compareFiles(testFile1, testFile3);
		expected = new FileContentResultBean(testFile1, testFile3);
		expected.setAbsolutePathEquality(true);
		expected.setContentEquality(true);
		expected.setFileExtensionEquality(false);
		expected.setLastModifiedEquality(true);
		expected.setLengthEquality(true);
		expected.setNameEquality(true);
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link CompareFileExtensions#compareFiles(File, File, boolean)}.
	 */
	@Test(enabled = true)
	public void testCompareFilesFileFileBoolean() throws FileNotFoundException, IOException
	{
		boolean actual;
		boolean expected;

		actual = CompareFileExtensions.compareFiles(testFile1, testFile2, false);
		expected = false;
		assertEquals(expected, actual);

		actual = CompareFileExtensions.compareFiles(testFile1, testFile3, true);
		expected = true;
		assertEquals(expected, actual);

		final String filePrefix1 = "testCompareFiles1";
		final String filePrefix2 = "testCompareFiles2";
		final String oldFileSuffix = ".txt";
		final String newFileSuffix = ".rtf";
		final File source = new File(this.deepDir, filePrefix1 + oldFileSuffix);
		File compare = new File(this.deepDir, filePrefix1 + oldFileSuffix);

		actual = CompareFileExtensions.compareFiles(source, compare, false);
		assertTrue("File should be equal cause they dont exist.", actual);
		compare = new File(this.deepDir, filePrefix2 + newFileSuffix);
		actual = CompareFileExtensions.compareFiles(source, compare, false);
		assertFalse("File should not be equal.", actual);
		WriteFileExtensions.string2File(source, "Its a beautifull day!!!");
		WriteFileExtensions.string2File(compare, "Its a beautifull day!!!");
		actual = CompareFileExtensions.compareFiles(source, compare, false);
		assertTrue("File should be equal.", actual);
		actual = CompareFileExtensions.compareFiles(source, compare, true);
		assertTrue("File should be equal.", actual);
		WriteFileExtensions.string2File(compare, "Its a beautifull evening!!!");
		actual = CompareFileExtensions.compareFiles(source, compare, true);
		assertFalse("File should not be equal.", actual);
		WriteFileExtensions.string2File(compare, "Its a beautifull boy!!!");
		actual = CompareFileExtensions.compareFiles(source, compare, true);
		assertFalse("File should not be equal.", actual);
	}

	/**
	 * Test method for
	 * {@link CompareFileExtensions#compareFiles(File, File, boolean, boolean, boolean, boolean, boolean)}.
	 */
	@Test(enabled = false)
	public void testCompareFilesFileFileBooleanBooleanBooleanBooleanBoolean()
		throws FileNotFoundException, IOException
	{
		IFileCompareResultBean actual;
		IFileCompareResultBean expected;
		actual = CompareFileExtensions.compareFiles(testFile1, testFile2, false, false, false,
			false, false);
		expected = new FileCompareResultBean(testFile1, testFile2);
		expected.setAbsolutePathEquality(false);
		expected.setFileExtensionEquality(false);
		expected.setLastModifiedEquality(true);
		expected.setLengthEquality(false);
		expected.setNameEquality(true);
		assertEquals(expected, actual);

		actual = CompareFileExtensions.compareFiles(testFile1, testFile3, false, false, false,
			false, false);
		expected = new FileCompareResultBean(testFile1, testFile3);
		expected.setAbsolutePathEquality(false);
		expected.setFileExtensionEquality(false);
		expected.setLastModifiedEquality(true);
		expected.setLengthEquality(true);
		expected.setNameEquality(true);
		assertEquals(expected, actual);
	}

	/**
	 * Test method for
	 * {@link CompareFileExtensions#compareFiles(File, File, boolean, boolean, boolean, boolean, boolean, boolean)}.
	 */
	@Test(enabled = false)
	public void testCompareFilesFileFileBooleanBooleanBooleanBooleanBooleanBoolean()
	{
		actual = CompareFileExtensions.compareFiles(testFile1, testFile2, false, false, false,
			false, false, false);
		expected = new FileContentResultBean(testFile1, testFile2);
		expected.setAbsolutePathEquality(false);
		expected.setContentEquality(false);
		expected.setFileExtensionEquality(false);
		expected.setLastModifiedEquality(true);
		expected.setLengthEquality(false);
		expected.setNameEquality(true);
		assertEquals(expected, actual);

		actual = CompareFileExtensions.compareFiles(testFile1, testFile3, false, false, false,
			false, false, false);
		expected = new FileContentResultBean(testFile1, testFile3);
		expected.setAbsolutePathEquality(false);
		expected.setContentEquality(true);
		expected.setFileExtensionEquality(false);
		expected.setLastModifiedEquality(true);
		expected.setLengthEquality(true);
		expected.setNameEquality(true);
		assertEquals(expected, actual);
	}

	/**
	 * Test method for
	 * {@link CompareFileExtensions#compare(IFileCompareResultBean, boolean, boolean, boolean, boolean, boolean)}.
	 */
	@Test(enabled = true)
	public void testCompareIFileCompareResultBeanBooleanBooleanBooleanBooleanBoolean()
	{
		IFileCompareResultBean actual;
		IFileCompareResultBean expected;
		actual = new FileCompareResultBean(testFile1, testFile2);
		CompareFileExtensions.compare(actual, false, false, false, false, false);
		expected = new FileCompareResultBean(testFile1, testFile2);
		expected.setAbsolutePathEquality(false);
		expected.setFileExtensionEquality(false);
		expected.setLastModifiedEquality(true);
		expected.setLengthEquality(false);
		expected.setNameEquality(true);
		assertEquals(expected, actual);

		actual = new FileCompareResultBean(testFile1, testFile3);
		CompareFileExtensions.compare(actual, false, false, false, false, false);
		expected = new FileCompareResultBean(testFile1, testFile3);
		expected.setAbsolutePathEquality(false);
		expected.setFileExtensionEquality(false);
		expected.setLastModifiedEquality(true);
		expected.setLengthEquality(true);
		expected.setNameEquality(true);
		assertEquals(expected, actual);
	}

	/**
	 * Test method for
	 * {@link CompareFileExtensions#compare(IFileContentResultBean, boolean, boolean, boolean, boolean, boolean, boolean)}.
	 */
	@Test(enabled = true)
	public void testCompareIFileContentResultBeanBooleanBooleanBooleanBooleanBooleanBoolean()
	{
		actual = new FileContentResultBean(testFile1, testFile2);
		CompareFileExtensions.compare(actual, false, false, false, false, false, false);
		expected = new FileContentResultBean(testFile1, testFile2);
		expected.setAbsolutePathEquality(false);
		expected.setContentEquality(false);
		expected.setFileExtensionEquality(false);
		expected.setLastModifiedEquality(true);
		expected.setLengthEquality(false);
		expected.setNameEquality(true);
		assertEquals(expected, actual);

		actual = new FileContentResultBean(testFile1, testFile3);
		CompareFileExtensions.compare(actual, false, false, false, false, false, false);
		expected = new FileContentResultBean(testFile1, testFile3);
		expected.setAbsolutePathEquality(false);
		expected.setContentEquality(true);
		expected.setFileExtensionEquality(false);
		expected.setLastModifiedEquality(true);
		expected.setLengthEquality(true);
		expected.setNameEquality(true);
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link CompareFileExtensions#completeCompare(IFileCompareResultBean)}.
	 */
	@Test(enabled = false) // TODO FIXME
	public void testCompleteCompare()
	{
		IFileCompareResultBean actual;
		IFileCompareResultBean expected;
		actual = new FileCompareResultBean(testFile1, testFile2);
		CompareFileExtensions.completeCompare(actual);
		expected = new FileCompareResultBean(testFile1, testFile2);
		expected.setAbsolutePathEquality(false);
		expected.setFileExtensionEquality(false);
		expected.setLastModifiedEquality(true);
		expected.setLengthEquality(false);
		expected.setNameEquality(true);
		assertEquals(expected, actual);

		actual = new FileCompareResultBean(testFile1, testFile3);
		CompareFileExtensions.completeCompare(actual);
		expected = new FileCompareResultBean(testFile1, testFile3);
		expected.setAbsolutePathEquality(false);
		expected.setFileExtensionEquality(false);
		expected.setLastModifiedEquality(true);
		expected.setLengthEquality(true);
		expected.setNameEquality(true);
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link CompareFileExtensions#findEqualFiles(File)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test(enabled = true)
	public void testFindEqualFilesFile() throws IOException
	{
		final List<IFileCompareResultBean> found = CompareFileExtensions
			.findEqualFiles(this.testDir);

		assertTrue("found.size() is not equal 2.", found.size() == 2);
	}

	/**
	 * Test method for
	 * {@link CompareFileExtensions#findEqualFiles(File, boolean, boolean, boolean, boolean, boolean)}.
	 */
	@Test(enabled = false)
	public void testFindEqualFilesFileBooleanBooleanBooleanBooleanBoolean()
	{
		List<IFileCompareResultBean> actual;
		List<IFileCompareResultBean> expected;
		actual = CompareFileExtensions.findEqualFiles(this.testDir, false, false, false, false,
			false);
		FileCompareResultBean one = new FileCompareResultBean(testFile4, testFile1);
		one.setAbsolutePathEquality(false);
		one.setFileExtensionEquality(true);
		one.setLastModifiedEquality(true);
		one.setLengthEquality(true);
		one.setNameEquality(true);
		FileCompareResultBean two = new FileCompareResultBean(testFile1, testFile4);
		two.setAbsolutePathEquality(false);
		two.setFileExtensionEquality(true);
		two.setLastModifiedEquality(true);
		two.setLengthEquality(true);
		two.setNameEquality(true);
		expected = ListFactory.newArrayList(one, two);
		assertTrue(CollectionExtensions.isEqualCollection(actual, expected));
	}

	/**
	 * Test method for {@link CompareFileExtensions#findEqualFiles(File, File)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test(enabled = true)
	public void testFindEqualFilesFileFile() throws IOException
	{
		final File testFile3 = new File(this.deepDir, "testFindFilesRecursive.cvs");

		WriteFileExtensions.string2File(testFile3, "Its a beautifull night!!!");

		final File testFile4 = new File(this.secondTestDir.getAbsoluteFile(),
			"testFindFilesRecursive.txt");

		final File testFile5 = new File(this.secondTestDir.getAbsoluteFile(),
			"testFindFilesRecursive.tft");

		final File testFile6 = new File(this.secondTestDir, "testFindFilesRecursive.cvs");

		WriteFileExtensions.string2File(testFile4, "Its a beautifull day!!!");
		WriteFileExtensions.string2File(testFile5, "Its a beautifull evening!!!????");
		WriteFileExtensions.string2File(testFile6, "Its a beautifull night!!!");

		final List<IFileCompareResultBean> found = CompareFileExtensions
			.findEqualFiles(this.testDir, this.secondTestDir);

		assertTrue("found.size() is not equal 3.", found.size() == 3);
	}

	/**
	 * Test method for
	 * {@link CompareFileExtensions#findEqualFiles(File, File, boolean, boolean, boolean, boolean, boolean)}.
	 */
	@Test(enabled = false)
	public void testFindEqualFilesFileFileBooleanBooleanBooleanBooleanBoolean()
		throws FileNotFoundException, IOException
	{
		List<IFileCompareResultBean> actual;
		List<IFileCompareResultBean> expected;
		final File testFile3 = new File(this.deepDir, "testFindFilesRecursive.cvs");

		WriteFileExtensions.string2File(testFile3, "Its a beautifull night!!!");

		final File testFile4 = new File(this.secondTestDir.getAbsoluteFile(),
			"testFindFilesRecursive.txt");

		final File testFile5 = new File(this.secondTestDir.getAbsoluteFile(),
			"testFindFilesRecursive.tft");

		final File testFile6 = new File(this.secondTestDir, "testFindFilesRecursive.cvs");

		WriteFileExtensions.string2File(testFile4, "Its a beautifull day!!!");
		WriteFileExtensions.string2File(testFile5, "Its a beautifull evening!!!????");
		WriteFileExtensions.string2File(testFile6, "Its a beautifull night!!!");

		actual = CompareFileExtensions.findEqualFiles(this.secondTestDir, this.testDir, false,
			false, false, true, false);
		FileCompareResultBean one = new FileCompareResultBean(testFile4, this.testFile4);
		one.setAbsolutePathEquality(false);
		one.setFileExtensionEquality(true);
		one.setLastModifiedEquality(true);
		one.setLengthEquality(true);
		one.setNameEquality(true);
		FileCompareResultBean two = new FileCompareResultBean(testFile4, testFile1);
		two.setAbsolutePathEquality(false);
		two.setFileExtensionEquality(true);
		two.setLastModifiedEquality(true);
		two.setLengthEquality(true);
		two.setNameEquality(true);
		FileCompareResultBean three = new FileCompareResultBean(testFile6, testFile3);
		three.setAbsolutePathEquality(false);
		three.setFileExtensionEquality(true);
		three.setLastModifiedEquality(true);
		three.setLengthEquality(true);
		three.setNameEquality(true);
		expected = ListFactory.newArrayList(one, two, three);
		assertTrue(CollectionExtensions.isEqualCollection(actual, expected));
	}

	/**
	 * Test method for {@link CompareFileExtensions#findEqualFilesWithSameContent(File)}.
	 */
	@Test(enabled = false) // TODO FIXME
	public void testFindEqualFilesWithSameContentFile()
	{
		final List<IFileContentResultBean> found = CompareFileExtensions
			.findEqualFilesWithSameContent(this.testDir);

		assertTrue("found.size() is not equal 2.", found.size() == 2);
	}

	/**
	 * Test method for
	 * {@link CompareFileExtensions#findEqualFilesWithSameContent(File, boolean, boolean, boolean, boolean, boolean, boolean)}.
	 */
	@Test(enabled = false) // TODO FIXME
	public void testFindEqualFilesWithSameContentFileBooleanBooleanBooleanBooleanBooleanBoolean()
	{
		List<IFileContentResultBean> actual;
		List<IFileContentResultBean> expected;
		actual = CompareFileExtensions.findEqualFilesWithSameContent(this.testDir, false, false,
			false, false, false, false);
		FileContentResultBean one = new FileContentResultBean(testFile4, testFile1);
		one.setAbsolutePathEquality(false);
		one.setContentEquality(false);
		one.setFileExtensionEquality(true);
		one.setLastModifiedEquality(true);
		one.setLengthEquality(true);
		one.setNameEquality(true);
		FileContentResultBean two = new FileContentResultBean(testFile1, testFile4);
		two.setAbsolutePathEquality(false);
		two.setContentEquality(false);
		two.setFileExtensionEquality(true);
		two.setLastModifiedEquality(true);
		two.setLengthEquality(true);
		two.setNameEquality(true);
		expected = ListFactory.newArrayList(one, two);
		assertTrue(CollectionExtensions.isEqualCollection(actual, expected));
	}

	/**
	 * Test method for {@link CompareFileExtensions#findEqualFilesWithSameContent(File, File)}.
	 */
	@Test(enabled = true)
	public void testFindEqualFilesWithSameContentFileFile()
		throws FileNotFoundException, IOException
	{
		final File testFile3 = new File(this.deepDir, "testFindFilesRecursive.cvs");

		WriteFileExtensions.string2File(testFile3, "Its a beautifull night!!!");

		final File testFile4 = new File(this.secondTestDir.getAbsoluteFile(),
			"testFindFilesRecursive.txt");

		final File testFile5 = new File(this.secondTestDir.getAbsoluteFile(),
			"testFindFilesRecursive.tft");

		final File testFile6 = new File(this.secondTestDir, "testFindFilesRecursive.cvs");

		WriteFileExtensions.string2File(testFile4, "Its a beautifull day!!!");
		WriteFileExtensions.string2File(testFile5, "Its a beautifull evening!!!????");
		WriteFileExtensions.string2File(testFile6, "Its a beautifull night!!!");

		final List<IFileContentResultBean> contentfound = CompareFileExtensions
			.findEqualFilesWithSameContent(this.testDir, this.secondTestDir);

		assertTrue("contentfound() is not equal 3.", contentfound.size() == 3);
	}

	/**
	 * Test method for
	 * {@link CompareFileExtensions#findEqualFilesWithSameContent(File, File, boolean, boolean, boolean, boolean, boolean, boolean)}.
	 */
	@Test(enabled = false) // TODO FIXME
	public void testFindEqualFilesWithSameContentFileFileBooleanBooleanBooleanBooleanBooleanBoolean()
		throws FileNotFoundException, IOException
	{
		List<IFileContentResultBean> actual;
		List<IFileContentResultBean> expected;
		final File testFile3 = new File(this.deepDir, "testFindFilesRecursive.cvs");

		WriteFileExtensions.string2File(testFile3, "Its a beautifull night!!!");

		final File testFile4 = new File(this.secondTestDir.getAbsoluteFile(),
			"testFindFilesRecursive.txt");

		final File testFile5 = new File(this.secondTestDir.getAbsoluteFile(),
			"testFindFilesRecursive.tft");

		final File testFile6 = new File(this.secondTestDir, "testFindFilesRecursive.cvs");

		WriteFileExtensions.string2File(testFile4, "Its a beautifull day!!!");
		WriteFileExtensions.string2File(testFile5, "Its a beautifull evening!!!????");
		WriteFileExtensions.string2File(testFile6, "Its a beautifull night!!!");

		actual = CompareFileExtensions.findEqualFilesWithSameContent(this.secondTestDir,
			this.testDir, false, false, false, false, false, false);
		FileContentResultBean one = new FileContentResultBean(testFile4, this.testFile4);
		one.setAbsolutePathEquality(false);
		one.setContentEquality(false);
		one.setFileExtensionEquality(true);
		one.setLastModifiedEquality(true);
		one.setLengthEquality(true);
		one.setNameEquality(true);
		FileContentResultBean two = new FileContentResultBean(testFile4, testFile1);
		two.setAbsolutePathEquality(false);
		two.setContentEquality(false);
		two.setFileExtensionEquality(true);
		two.setLastModifiedEquality(true);
		two.setLengthEquality(true);
		two.setNameEquality(true);
		FileContentResultBean three = new FileContentResultBean(testFile6, testFile3);
		three.setAbsolutePathEquality(false);
		three.setContentEquality(false);
		three.setFileExtensionEquality(true);
		three.setLastModifiedEquality(true);
		three.setLengthEquality(true);
		three.setNameEquality(true);
		expected = ListFactory.newArrayList(one, two, three);
		assertTrue(CollectionExtensions.isEqualCollection(actual, expected));
	}

	/**
	 * Test method for {@link CompareFileExtensions#simpleCompareFiles(File, File)}.
	 */
	@Test(enabled = true)
	public void testSimpleCompareFiles()
	{
		IFileCompareResultBean actual;
		IFileCompareResultBean expected;
		actual = CompareFileExtensions.simpleCompareFiles(testFile1, testFile2);
		expected = new FileCompareResultBean(testFile1, testFile2);
		expected.setAbsolutePathEquality(true);
		expected.setFileExtensionEquality(false);
		expected.setLastModifiedEquality(true);
		expected.setLengthEquality(false);
		expected.setNameEquality(true);
		assertEquals(expected, actual);

		actual = CompareFileExtensions.simpleCompareFiles(testFile1, testFile3);
		expected = new FileCompareResultBean(testFile1, testFile3);
		expected.setAbsolutePathEquality(true);
		expected.setFileExtensionEquality(false);
		expected.setLastModifiedEquality(true);
		expected.setLengthEquality(true);
		expected.setNameEquality(true);
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link CompareFileExtensions#validateEquality(IFileCompareResultBean)}.
	 */
	@Test(enabled = true)
	public void testValidateEqualityIFileCompareResultBean()
	{
		boolean actual;
		boolean expected;

		FileCompareResultBean one = new FileCompareResultBean(testFile4, this.testFile4);
		one.setAbsolutePathEquality(false);

		one.setFileExtensionEquality(true);
		one.setLastModifiedEquality(true);
		one.setLengthEquality(true);
		one.setNameEquality(true);
		actual = CompareFileExtensions.validateEquality(one);
		expected = true;
		assertEquals(expected, actual);

		one.setFileExtensionEquality(false);
		one.setLastModifiedEquality(true);
		one.setLengthEquality(true);
		one.setNameEquality(true);
		actual = CompareFileExtensions.validateEquality(one);
		expected = false;
		assertEquals(expected, actual);

		one.setFileExtensionEquality(true);
		one.setLastModifiedEquality(false);
		one.setLengthEquality(true);
		one.setNameEquality(true);
		actual = CompareFileExtensions.validateEquality(one);
		expected = false;
		assertEquals(expected, actual);

		one.setFileExtensionEquality(true);
		one.setLastModifiedEquality(true);
		one.setLengthEquality(false);
		one.setNameEquality(true);
		actual = CompareFileExtensions.validateEquality(one);
		expected = false;
		assertEquals(expected, actual);

		one.setFileExtensionEquality(true);
		one.setLastModifiedEquality(true);
		one.setLengthEquality(true);
		one.setNameEquality(false);
		actual = CompareFileExtensions.validateEquality(one);
		expected = false;
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link CompareFileExtensions#validateEquality(IFileContentResultBean)}.
	 */
	@Test(enabled = true)
	public void testValidateEqualityIFileContentResultBean()
	{
		boolean actual;
		boolean expected;

		FileContentResultBean one = new FileContentResultBean(testFile4, this.testFile4);
		one.setAbsolutePathEquality(false);
		one.setContentEquality(true);
		one.setFileExtensionEquality(true);
		one.setLastModifiedEquality(true);
		one.setLengthEquality(true);
		one.setNameEquality(true);
		actual = CompareFileExtensions.validateEquality(one);
		expected = true;
		assertEquals(expected, actual);

		one.setFileExtensionEquality(false);
		one.setContentEquality(true);
		one.setLastModifiedEquality(true);
		one.setLengthEquality(true);
		one.setNameEquality(true);
		actual = CompareFileExtensions.validateEquality(one);
		expected = false;
		assertEquals(expected, actual);

		one.setFileExtensionEquality(true);
		one.setContentEquality(true);
		one.setLastModifiedEquality(false);
		one.setLengthEquality(true);
		one.setNameEquality(true);
		actual = CompareFileExtensions.validateEquality(one);
		expected = false;
		assertEquals(expected, actual);

		one.setFileExtensionEquality(true);
		one.setContentEquality(true);
		one.setLastModifiedEquality(true);
		one.setLengthEquality(false);
		one.setNameEquality(true);
		actual = CompareFileExtensions.validateEquality(one);
		expected = false;
		assertEquals(expected, actual);

		one.setFileExtensionEquality(true);
		one.setContentEquality(true);
		one.setLastModifiedEquality(true);
		one.setLengthEquality(true);
		one.setNameEquality(false);
		actual = CompareFileExtensions.validateEquality(one);
		expected = false;
		assertEquals(expected, actual);

		one.setFileExtensionEquality(true);
		one.setContentEquality(false);
		one.setLastModifiedEquality(true);
		one.setLengthEquality(true);
		one.setNameEquality(true);
		actual = CompareFileExtensions.validateEquality(one);
		expected = false;
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link CompareFileExtensions}
	 */
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(CompareFileExtensions.class);
	}

}
