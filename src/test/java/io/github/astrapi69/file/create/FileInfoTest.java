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
package io.github.astrapi69.file.create;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang3.SystemUtils;
import org.meanbean.test.BeanTester;
import org.testng.annotations.Test;

import io.github.astrapi69.file.delete.DeleteFileExtensions;

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
		model = new FileInfo("foo", "bar", false);
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
	 * Test method for {@link FileInfo#toFileInfo(File)}
	 */
	@Test
	public void testToFileInfo() throws IOException
	{
		String absolutePath;
		File file;
		FileInfo fileInfo;
		String actual;
		String expected;
		String rootPath;
		String absoluteFilePath;
		String filename;

		rootPath = SystemUtils.IS_OS_WINDOWS ? "C:\\" : "/";

		absoluteFilePath = SystemUtils.IS_OS_WINDOWS
			? rootPath + "tmp\\foo\\bar"
			: rootPath + "tmp/foo/bar";
		filename = "foo.txt";
		actual = SystemUtils.IS_OS_WINDOWS ? "C:\\tmp\\foo\\bar\\foo.txt" : "/tmp/foo/bar/foo.txt";
		fileInfo = FileInfo.builder().path(absoluteFilePath).name(filename).build();
		file = FileFactory.newFile(fileInfo);
		assertTrue(file.exists());
		assertNotNull(file);
		expected = file.getAbsolutePath();
		assertEquals(expected, actual);
		FileInfo anotherFileInfo = FileInfo.toFileInfo(file);
		assertEquals(anotherFileInfo, fileInfo);
		DeleteFileExtensions.delete(file);
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
