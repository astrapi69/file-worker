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
package de.alpharogroup.file.search;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.testng.AssertJUnit;
import org.testng.annotations.Test;

import de.alpharogroup.file.FileTestCase;
import de.alpharogroup.file.delete.DeleteFileUtils;
import de.alpharogroup.file.write.WriteFileUtils;

/**
 * The Class FileSearchUtilsTest.
 */
public class FileSearchUtilsTest extends FileTestCase
{

	@Test(enabled = true)
	public void testFindAllFilesFileString() throws IOException
	{
		// 1. initialize expected files to search
		final File testFile1 = new File(this.testDir.getAbsoluteFile(),
			"testFindFilesRecursive.txt");
		final File testFile2 = new File(this.testDir.getAbsoluteFile(),
			"testFindFilesRecursive.tft");
		final File testFile3 = new File(this.deepDir, "testFindFilesRecursive.cvs");
		final File testFile4 = new File(this.deepDir, "testFindFilesRecursive.txt");
		WriteFileUtils.string2File(testFile1, "Its a beautifull day!!!");
		WriteFileUtils.string2File(testFile2, "Its a beautifull evening!!!");
		WriteFileUtils.string2File(testFile3, "Its a beautifull night!!!");
		WriteFileUtils.string2File(testFile4, "Its a beautifull day!!!");
		// this list is kept for clean up...
		final List<File> fileList = new ArrayList<>();
		fileList.add(testFile1);
		fileList.add(testFile2);
		fileList.add(testFile3);
		fileList.add(testFile4);
		// this list is expected as result...
		final List<File> expected = new ArrayList<>();
		expected.add(testFile1);
		expected.add(testFile4);

		// 2. run the actual method to test
		final long start = System.currentTimeMillis();
		final List<File> actual = FileSearchUtils.findAllFiles(this.testDir, ".*txt");
		final long end = System.currentTimeMillis();
		final long executionTime = end - start;
		System.out.println("execution:" + executionTime);
		// 3. assert that expected with actual match
		AssertJUnit.assertTrue("", expected.size() == actual.size());
		for (final File file : expected)
		{
			AssertJUnit.assertTrue("", actual.contains(file));
		}
		// 4. cleanup all files from this test
		DeleteFileUtils.delete(fileList);
	}

	@Test(enabled = true)
	public void testFindFileWithFileFilter() throws IOException
	{
		// 1. initialize expected files to search
		final File testFile1 = new File(this.testDir.getAbsoluteFile(),
			"testFindFilesRecursive.txt");
		final File testFile2 = new File(this.testDir.getAbsoluteFile(),
			"testFindFilesRecursive.tft");
		final File testFile3 = new File(this.deepDir, "testFindFilesRecursive.cvs");
		final File testFile4 = new File(this.deepDir, "testFindFilesRecursive.txt");
		WriteFileUtils.string2File(testFile1, "Its a beautifull day!!!");
		WriteFileUtils.string2File(testFile2, "Its a beautifull evening!!!");
		WriteFileUtils.string2File(testFile3, "Its a beautifull night!!!");
		WriteFileUtils.string2File(testFile4, "Its a beautifull day!!!");
		// this list is kept for clean up...
		final List<File> fileList = new ArrayList<>();
		fileList.add(testFile1);
		fileList.add(testFile2);
		fileList.add(testFile3);
		fileList.add(testFile4);
		// this list is expected as result...
		final List<File> expected = new ArrayList<>();
		expected.add(testFile1);
		expected.add(testFile4);

		// 2. run the actual method to test
		List<File> actual = FileSearchUtils.findFilesWithFilter(this.testDir, ".txt");
		// 3. assert that expected with actual match
		AssertJUnit.assertTrue("", expected.size() == actual.size());
		for (final File file : expected)
		{
			AssertJUnit.assertTrue("", actual.contains(file));
		}
		actual = FileSearchUtils.findFilesWithFilter(this.testDir, "tft", "cvs");
		expected.clear();
		expected.add(testFile2);
		expected.add(testFile3);
		AssertJUnit.assertTrue("", expected.size() == actual.size());
		for (final File file : expected)
		{
			AssertJUnit.assertTrue("", actual.contains(file));
		}
		// 4. cleanup all files from this test
		DeleteFileUtils.delete(fileList);
	}

}
