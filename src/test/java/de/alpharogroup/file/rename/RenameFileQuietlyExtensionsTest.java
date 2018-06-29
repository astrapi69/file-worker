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
package de.alpharogroup.file.rename;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

import java.io.File;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.alpharogroup.file.FileTestCase;
import de.alpharogroup.file.search.FileSearchExtensions;
import de.alpharogroup.file.write.WriteFileQuietlyExtensions;

/**
 * The unit test class for the class {@link RenameFileQuietlyExtensions}
 */
public class RenameFileQuietlyExtensionsTest extends FileTestCase
{

	/**
	 * Sets up method will be invoked before every unit test method in this class.
	 *
	 * @throws Exception
	 *             is thrown if an exception occurs
	 */
	@Override
	@BeforeMethod
	protected void setUp() throws Exception
	{
		super.setUp();
	}

	/**
	 * Tear down method will be invoked after every unit test method in this class.
	 *
	 * @throws Exception
	 *             is thrown if an exception occurs
	 */
	@Override
	@AfterMethod
	protected void tearDown() throws Exception
	{
		super.tearDown();
	}


	/**
	 * Test method for {@link RenameFileQuietlyExtensions#forceToMoveFileQuietly(File, File)}.
	 */
	@Test
	public void testForceToMoveFileQuietly()
	{
		final File srcFile = new File(this.testDir.getAbsoluteFile(), "testMoveFile.txt");
		final File expectedMovedFile = new File(this.deepDir, "testMovedFile.moved");
		WriteFileQuietlyExtensions.string2File(srcFile, "Its a beautifull day!!!");
		actual = RenameFileQuietlyExtensions.forceToMoveFileQuietly(srcFile, expectedMovedFile);
		expected = true;
		assertEquals(actual, expected);
		assertTrue(expectedMovedFile.exists());
		assertFalse(srcFile.exists());
		expectedMovedFile.deleteOnExit();
	}

	/**
	 * Test method for {@link RenameFileQuietlyExtensions#renameFileQuietly(File, File, boolean)}.
	 */
	@Test
	public void testRenameFileQuietlyFileFileBoolean()
	{
		final File srcFile = new File(this.testDir.getAbsoluteFile(), "testRenameFile.txt");
		final File expectedRenamedFile = new File(this.deepDir, "testRenamedFile.renamed");
		WriteFileQuietlyExtensions.string2File(srcFile, "Its a beautifull day!!!");
		actual = RenameFileQuietlyExtensions.renameFileQuietly(srcFile, expectedRenamedFile, true);
		expected = true;
		assertEquals(actual, expected);
		assertTrue(expectedRenamedFile.exists());
		assertFalse(srcFile.exists());
		expectedRenamedFile.deleteOnExit();
		srcFile.deleteOnExit();

		final String filePrefix1 = "testRenameFileFileFile1";
		final String filePrefix2 = "testRenameFileFileFile2";
		final String oldFileSuffix = ".txt";
		final String newFileSuffix = ".rtf";
		final File testFile1 = new File(this.deepDir, filePrefix1 + oldFileSuffix);
		final File renamedFile1 = new File(this.deepDir, filePrefix2 + newFileSuffix);

		this.actual = RenameFileQuietlyExtensions.renameFileQuietly(testFile1, renamedFile1, false);
		assertFalse("File should not exist in this directory.", this.actual);
		WriteFileQuietlyExtensions.string2File(testFile1, "Its a beautifull day!!!");
		this.actual = RenameFileQuietlyExtensions.renameFileQuietly(testFile1, renamedFile1, false);
		assertTrue("File should be renamed.", this.actual);
		this.actual = FileSearchExtensions.containsFile(this.deepDir, renamedFile1);
		assertTrue("The renamed file should exist in this directory.", this.actual);
		testFile1.deleteOnExit();
		renamedFile1.deleteOnExit();
	}

	/**
	 * Test method for {@link RenameFileQuietlyExtensions#renameFileQuietly(File, String)}.
	 */
	@Test
	public void testRenameFileQuietlyFileString()
	{
		final String filePrefix1 = "testRenameFileFileString1";
		final String filePrefix2 = "testRenameFileFileString2";
		final String oldFileSuffix = ".txt";
		final String newFileSuffix = ".rtf";
		final File testFile1 = new File(this.deepDir, filePrefix1 + oldFileSuffix);
		final File renamedFile1 = new File(this.deepDir, filePrefix2 + newFileSuffix);

		WriteFileQuietlyExtensions.string2File(testFile1, "Its a beautifull day!!!");
		this.actual = RenameFileQuietlyExtensions.renameFileQuietly(testFile1,
			renamedFile1.getName());
		assertTrue("File should be renamed.", this.actual);
		this.actual = FileSearchExtensions.containsFile(this.deepDir, renamedFile1);
		assertTrue("The renamed file should exist in this directory.", this.actual);
	}

}
