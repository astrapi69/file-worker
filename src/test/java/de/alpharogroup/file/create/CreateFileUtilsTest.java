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
package de.alpharogroup.file.create;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.testng.AssertJUnit;
import org.testng.annotations.Test;

import de.alpharogroup.file.delete.DeleteFileUtils;
import de.alpharogroup.file.exceptions.DirectoryAllreadyExistsException;

/**
 * The class CreateFileUtilsTest has unit test for the class CreateFileUtils.
 */
public class CreateFileUtilsTest
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
				DeleteFileUtils.delete(dir);
			}
		}
		final boolean created = CreateFileUtils.newDirectories(dirs);
		AssertJUnit.assertTrue("directory should be created.", created);
		for (final File dir : dirs)
		{
			AssertJUnit.assertTrue("directory should exist.", dir.exists());
			AssertJUnit.assertTrue("File object should be a directory.", dir.isDirectory());
		}
		// Finally delete the test directories...
		for (final File dir : dirs)
		{
			if (dir.exists())
			{
				DeleteFileUtils.delete(dir);
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
			DeleteFileUtils.delete(dir);
		}
		final boolean created = CreateFileUtils.newDirectory(dir);
		AssertJUnit.assertTrue("directory should be created.", created);
		AssertJUnit.assertTrue("directory should exist.", dir.exists());
		AssertJUnit.assertTrue("File object should be a directory.", dir.isDirectory());
		// Finally delete the test directory...
		DeleteFileUtils.delete(dir);
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
			DeleteFileUtils.delete(file);
		}
		final boolean created = CreateFileUtils.newFile(file);
		AssertJUnit.assertTrue("File should be created.", created);
		AssertJUnit.assertTrue("File should exist.", file.exists());
		AssertJUnit.assertTrue("File object should be a file.", file.isFile());
		// Finally delete the test file...
		DeleteFileUtils.delete(file);
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
				DeleteFileUtils.delete(file);
			}
		}
		final boolean created = CreateFileUtils.newFiles(files);
		AssertJUnit.assertTrue("files should be created.", created);
		for (final File file : files)
		{
			AssertJUnit.assertTrue("file should exist.", file.exists());
			AssertJUnit.assertTrue("File object should be a file.", file.isFile());
		}
		// Finally delete the test files...
		for (final File file : files)
		{
			if (file.exists())
			{
				DeleteFileUtils.delete(file);
			}
		}
	}

}
