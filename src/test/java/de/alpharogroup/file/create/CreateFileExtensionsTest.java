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
package de.alpharogroup.file.create;

import static org.testng.AssertJUnit.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.meanbean.factories.ObjectCreationException;
import org.meanbean.test.BeanTestException;
import org.meanbean.test.BeanTester;
import org.testng.annotations.Test;

import de.alpharogroup.file.delete.DeleteFileExtensions;
import de.alpharogroup.file.exceptions.DirectoryAllreadyExistsException;

/**
 * The class {@link CreateFileExtensionsTest} has unit test for the class
 * {@link CreateFileExtensions}.
 */
public class CreateFileExtensionsTest
{

	/**
	 * Test create directories.
	 *
	 * @throws IOException
	 *             the IO exception
	 * @throws DirectoryAllreadyExistsException
	 *             the directory allready exists exception
	 */
	@Test(enabled = true)
	public void testNewDirectories() throws IOException, DirectoryAllreadyExistsException
	{
		final List<File> dirs = new ArrayList<>();
		final File dir1 = new File("test1.dir");
		final File dir2 = new File("test2.dir");
		final File dir3 = new File("test3.dir");
		dirs.add(dir1);
		dirs.add(dir2);
		dirs.add(dir3);
		// if the directories exist delete them to prevent a DirectoryAllreadyExistsException.
		for (final File dir : dirs)
		{
			if (dir.exists())
			{
				DeleteFileExtensions.delete(dir);
			}
		}
		final boolean created = CreateFileExtensions.newDirectories(dirs);
		assertTrue("directory should be created.", created);
		for (final File dir : dirs)
		{
			assertTrue("directory should exist.", dir.exists());
			assertTrue("File object should be a directory.", dir.isDirectory());
		}
		// Finally delete the test directories...
		for (final File dir : dirs)
		{
			if (dir.exists())
			{
				DeleteFileExtensions.delete(dir);
			}
		}
	}

	/**
	 * Test create directory.
	 *
	 * @throws DirectoryAllreadyExistsException
	 *             the directory allready exists exception
	 * @throws IOException
	 *             the IO exception
	 */
	@Test(enabled = true)
	public void testNewDirectory() throws DirectoryAllreadyExistsException, IOException
	{
		final File dir = new File("test.dir");
		// if the directory exist delete it to prevent a DirectoryAllreadyExistsException.
		if (dir.exists())
		{
			DeleteFileExtensions.delete(dir);
		}
		final boolean created = CreateFileExtensions.newDirectory(dir);
		assertTrue("directory should be created.", created);
		assertTrue("directory should exist.", dir.exists());
		assertTrue("File object should be a directory.", dir.isDirectory());
		// Finally delete the test directory...
		DeleteFileExtensions.delete(dir);
	}

	/**
	 * Test create file.
	 *
	 * @throws IOException
	 *             the IO exception
	 * @throws DirectoryAllreadyExistsException
	 *             the directory allready exists exception
	 */
	@Test(enabled = true)
	public void testNewFile() throws IOException, DirectoryAllreadyExistsException
	{
		final File file = new File("/tmp/foo/bar/test.file");
		// if the file exist delete it to prevent a DirectoryAllreadyExistsException.
		if (file.exists())
		{
			DeleteFileExtensions.delete(file);
		}
		final boolean created = CreateFileExtensions.newFile(file);
		assertTrue("File should be created.", created);
		assertTrue("File should exist.", file.exists());
		assertTrue("File object should be a file.", file.isFile());
		// Finally delete the test file...
		DeleteFileExtensions.delete(file);
	}

	/**
	 * Test create files.
	 *
	 * @throws IOException
	 *             the IO exception
	 * @throws DirectoryAllreadyExistsException
	 *             the directory allready exists exception
	 */
	@Test(enabled = true)
	public void testNewFiles() throws IOException, DirectoryAllreadyExistsException
	{
		final List<File> files = new ArrayList<>();
		final File file1 = new File("test1.file");
		final File file2 = new File("test2.file");
		final File file3 = new File("test3.file");
		files.add(file1);
		files.add(file2);
		files.add(file3);
		// if the files exist delete them to prevent a DirectoryAllreadyExistsException.
		for (final File file : files)
		{
			if (file.exists())
			{
				DeleteFileExtensions.delete(file);
			}
		}
		final boolean created = CreateFileExtensions.newFiles(files);
		assertTrue("files should be created.", created);
		for (final File file : files)
		{
			assertTrue("file should exist.", file.exists());
			assertTrue("File object should be a file.", file.isFile());
		}
		// Finally delete the test files...
		for (final File file : files)
		{
			if (file.exists())
			{
				DeleteFileExtensions.delete(file);
			}
		}
	}

	/**
	 * Test method for {@link CreateFileExtensions}
	 */
	@Test(expectedExceptions = { BeanTestException.class, ObjectCreationException.class })
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(CreateFileExtensions.class);
	}

}
