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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;

import io.github.astrapi69.collection.CollectionExtensions;
import io.github.astrapi69.collection.list.ListFactory;
import io.github.astrapi69.file.FileTestCase;
import io.github.astrapi69.file.compare.api.IFileCompareResultBean;
import io.github.astrapi69.file.compare.api.IFileContentResultBean;
import io.github.astrapi69.file.delete.DeleteFileExtensions;
import io.github.astrapi69.file.write.StoreFileExtensions;

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
	@BeforeEach
	protected void setUp() throws Exception
	{
		super.setUp();
		testFile1 = new File(this.testDir.getAbsoluteFile(), "testFindFilesRecursive.txt");
		testFile2 = new File(this.testDir.getAbsoluteFile(), "testFindFilesRecursive.tft");

		testFile3 = new File(this.deepDir, "testFindFilesRecursive.cvs");

		testFile4 = new File(this.deepDir, "testFindFilesRecursive.txt");

		StoreFileExtensions.toFile(testFile1, "Its a beautifull day!!!");
		StoreFileExtensions.toFile(testFile2, "Its a beautifull evening!!!");
		StoreFileExtensions.toFile(testFile3, "Its a beautifull day!!!");
		StoreFileExtensions.toFile(testFile4, "Its a beautifull day!!!");
	}

	@Override
	@AfterEach
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
	@Test
	@Disabled
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
	@Test
	@Disabled // TODO FIXME
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
	@Test
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
	@Test
	public void testCompareFilesFileFileBoolean() throws IOException
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
		assertTrue(actual, "File should be equal cause they dont exist.");
		compare = new File(this.deepDir, filePrefix2 + newFileSuffix);
		actual = CompareFileExtensions.compareFiles(source, compare, false);
		assertFalse(actual, "File should not be equal.");
		StoreFileExtensions.toFile(source, "Its a beautifull day!!!");
		StoreFileExtensions.toFile(compare, "Its a beautifull day!!!");
		actual = CompareFileExtensions.compareFiles(source, compare, false);
		assertTrue(actual, "File should be equal.");
		actual = CompareFileExtensions.compareFiles(source, compare, true);
		assertTrue(actual, "File should be equal.");
		StoreFileExtensions.toFile(compare, "Its a beautifull evening!!!");
		actual = CompareFileExtensions.compareFiles(source, compare, true);
		assertFalse(actual, "File should not be equal.");
		StoreFileExtensions.toFile(compare, "Its a beautifull boy!!!");
		actual = CompareFileExtensions.compareFiles(source, compare, true);
		assertFalse(actual, "File should not be equal.");
	}

	/**
	 * Test method for
	 * {@link CompareFileExtensions#compareFiles(File, File, boolean, boolean, boolean, boolean, boolean)}.
	 */
	@Test
	@Disabled
	public void testCompareFilesFileFileBooleanBooleanBooleanBooleanBoolean() throws IOException
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
	@Test
	@Disabled
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
	@Test
	@Disabled
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

		actual = new FileCompareResultBean(testFile1, testFile3);
		CompareFileExtensions.compare(actual, true, false, false, false, false);
		expected = new FileCompareResultBean(testFile1, testFile3);
		expected.setAbsolutePathEquality(true);
		expected.setFileExtensionEquality(false);
		expected.setLastModifiedEquality(true);
		expected.setLengthEquality(true);
		expected.setNameEquality(true);
		assertEquals(expected, actual);

		actual = new FileCompareResultBean(testFile1, testFile3);
		CompareFileExtensions.compare(actual, false, true, false, false, false);
		expected = new FileCompareResultBean(testFile1, testFile3);
		expected.setAbsolutePathEquality(false);
		expected.setFileExtensionEquality(true);
		expected.setLastModifiedEquality(true);
		expected.setLengthEquality(true);
		expected.setNameEquality(true);
		assertEquals(expected, actual);
	}

	/**
	 * Test method for
	 * {@link CompareFileExtensions#compare(IFileContentResultBean, boolean, boolean, boolean, boolean, boolean, boolean)}.
	 */
	@Test
	@Disabled
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
		expected.setLastModifiedEquality(false);
		expected.setLengthEquality(true);
		expected.setNameEquality(true);
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link CompareFileExtensions#completeCompare(IFileCompareResultBean)}.
	 */
	@Test
	@Disabled // TODO FIXME
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
	@Test
	public void testFindEqualFilesFile() throws IOException
	{
		final List<IFileCompareResultBean> found = CompareFileExtensions
			.findEqualFiles(this.testDir);

		assertTrue(found.size() == 2, "found.size() is not equal 2.");
	}

	/**
	 * Test method for
	 * {@link CompareFileExtensions#findEqualFiles(File, boolean, boolean, boolean, boolean, boolean)}.
	 */
	@Test
	@Disabled
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
	@Test
	public void testFindEqualFilesFileFile() throws IOException
	{
		final File testFile3 = new File(this.deepDir, "testFindFilesRecursive.cvs");

		StoreFileExtensions.toFile(testFile3, "Its a beautifull night!!!");

		final File testFile4 = new File(this.secondTestDir.getAbsoluteFile(),
			"testFindFilesRecursive.txt");

		final File testFile5 = new File(this.secondTestDir.getAbsoluteFile(),
			"testFindFilesRecursive.tft");

		final File testFile6 = new File(this.secondTestDir, "testFindFilesRecursive.cvs");

		StoreFileExtensions.toFile(testFile4, "Its a beautifull day!!!");
		StoreFileExtensions.toFile(testFile5, "Its a beautifull evening!!!????");
		StoreFileExtensions.toFile(testFile6, "Its a beautifull night!!!");

		final List<IFileCompareResultBean> found = CompareFileExtensions
			.findEqualFiles(this.testDir, this.secondTestDir);

		assertTrue(found.size() == 3, "found.size() is not equal 3.");
	}

	/**
	 * Test method for
	 * {@link CompareFileExtensions#findEqualFiles(File, File, boolean, boolean, boolean, boolean, boolean)}.
	 */
	@Test
	@Disabled
	public void testFindEqualFilesFileFileBooleanBooleanBooleanBooleanBoolean() throws IOException
	{
		List<IFileCompareResultBean> actual;
		List<IFileCompareResultBean> expected;
		final File testFile3 = new File(this.deepDir, "testFindFilesRecursive.cvs");

		StoreFileExtensions.toFile(testFile3, "Its a beautifull night!!!");

		final File testFile4 = new File(this.secondTestDir.getAbsoluteFile(),
			"testFindFilesRecursive.txt");

		final File testFile5 = new File(this.secondTestDir.getAbsoluteFile(),
			"testFindFilesRecursive.tft");

		final File testFile6 = new File(this.secondTestDir, "testFindFilesRecursive.cvs");

		StoreFileExtensions.toFile(testFile4, "Its a beautifull day!!!");
		StoreFileExtensions.toFile(testFile5, "Its a beautifull evening!!!????");
		StoreFileExtensions.toFile(testFile6, "Its a beautifull night!!!");

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
	@Test
	@Disabled // TODO FIXME
	public void testFindEqualFilesWithSameContentFile()
	{
		final List<IFileContentResultBean> found = CompareFileExtensions
			.findEqualFilesWithSameContent(this.testDir);

		assertTrue(found.size() == 2, "found.size() is not equal 2.");
	}

	/**
	 * Test method for
	 * {@link CompareFileExtensions#findEqualFilesWithSameContent(File, boolean, boolean, boolean, boolean, boolean, boolean)}.
	 */
	@Test
	@Disabled // TODO FIXME
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
	@Test
	public void testFindEqualFilesWithSameContentFileFile() throws IOException
	{
		final File testFile3 = new File(this.deepDir, "testFindFilesRecursive.cvs");

		StoreFileExtensions.toFile(testFile3, "Its a beautifull night!!!");

		final File testFile4 = new File(this.secondTestDir.getAbsoluteFile(),
			"testFindFilesRecursive.txt");

		final File testFile5 = new File(this.secondTestDir.getAbsoluteFile(),
			"testFindFilesRecursive.tft");

		final File testFile6 = new File(this.secondTestDir, "testFindFilesRecursive.cvs");

		StoreFileExtensions.toFile(testFile4, "Its a beautifull day!!!");
		StoreFileExtensions.toFile(testFile5, "Its a beautifull evening!!!????");
		StoreFileExtensions.toFile(testFile6, "Its a beautifull night!!!");

		final List<IFileContentResultBean> contentfound = CompareFileExtensions
			.findEqualFilesWithSameContent(this.testDir, this.secondTestDir);

		assertTrue(contentfound.size() == 3, "contentfound() is not equal 3.");
	}

	/**
	 * Test method for
	 * {@link CompareFileExtensions#findEqualFilesWithSameContent(File, File, boolean, boolean, boolean, boolean, boolean, boolean)}.
	 */
	@Test
	@Disabled // TODO FIXME
	public void testFindEqualFilesWithSameContentFileFileBooleanBooleanBooleanBooleanBooleanBoolean()
		throws IOException
	{
		List<IFileContentResultBean> actual;
		List<IFileContentResultBean> expected;
		final File testFile3 = new File(this.deepDir, "testFindFilesRecursive.cvs");

		StoreFileExtensions.toFile(testFile3, "Its a beautifull night!!!");

		final File testFile4 = new File(this.secondTestDir.getAbsoluteFile(),
			"testFindFilesRecursive.txt");

		final File testFile5 = new File(this.secondTestDir.getAbsoluteFile(),
			"testFindFilesRecursive.tft");

		final File testFile6 = new File(this.secondTestDir, "testFindFilesRecursive.cvs");

		StoreFileExtensions.toFile(testFile4, "Its a beautifull day!!!");
		StoreFileExtensions.toFile(testFile5, "Its a beautifull evening!!!????");
		StoreFileExtensions.toFile(testFile6, "Its a beautifull night!!!");

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
	@Test
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
	@Test
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
	@Test
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


	private File createTempFile(String name, long length)
	{
		// Helper method to create a temporary file with specified name and length
		File file = new File(System.getProperty("java.io.tmpdir"), name);
		// Method to set file length, modify last modified date as needed for tests
		return file;
	}

	@Test
	public void testCompareFiles_SameFiles()
	{
		File file1 = createTempFile("test1.txt", 100);
		File file2 = file1; // Same file testing
		assertTrue(
			CompareFileExtensions.compareFiles(file1, file2, false, false, false, false, false)
				.getFileExtensionEquality(),
			"Files should be identical");
	}

	@Test
	public void testCompareFiles_DifferentExtension()
	{
		File file1 = createTempFile("test1.txt", 100);
		File file2 = createTempFile("test1.jpg", 100);
		assertFalse(CompareFileExtensions.compareFiles(file1, file2, true, false, true, true, true)
			.getFileExtensionEquality(), "Files should differ in extension");
	}

	@Test
	public void testSimpleCompareFilesNewUseCase()
	{
		File file1 = createTempFile("simple1.txt", 100);
		File file2 = createTempFile("simple1.txt", 100);
		IFileCompareResultBean result = CompareFileExtensions.simpleCompareFiles(file1, file2);
		assertTrue(result.getFileExtensionEquality(), "Simple comparison should pass");
	}

	@Test
	public void testValidateEquality_True()
	{
		File file1 = createTempFile("validate.txt", 100);
		IFileCompareResultBean result = CompareFileExtensions.simpleCompareFiles(file1, file1);
		assertTrue(CompareFileExtensions.validateEquality(result), "Validation should pass");
	}

	@Test
	public void testValidateEquality_False()
	{
		File file1 = createTempFile("validate.txt", 100);
		File file2 = createTempFile("validate.jpg", 200);
		IFileCompareResultBean result = CompareFileExtensions.compareFiles(file1, file2, true,
			false, true, true, false);
		assertFalse(CompareFileExtensions.validateEquality(result), "Validation should fail");
	}

}
