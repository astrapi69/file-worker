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
import java.util.Collection;
import java.util.List;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.alpharogroup.file.FileTestCase;
import de.alpharogroup.file.delete.DeleteFileExtensions;

/**
 * The unit test class for the class {@link CreateFileQuietlyExtensions}.
 */
public class CreateFileQuietlyExtensionsTest extends FileTestCase
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
	 * Test method for {@link CreateFileQuietlyExtensions#newDirectoriesQuietly(Collection)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testNewDirectoriesQuietly() throws IOException
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
		final boolean created = CreateFileQuietlyExtensions.newDirectoriesQuietly(dirs);
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
	 * Test method for {@link CreateFileQuietlyExtensions#newDirectoryQuietly(File)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testNewDirectoryQuietly() throws IOException
	{
		final File dir = new File(this.testResources, "testCreateDirectory");
		// if the directory exist delete it to prevent a DirectoryAllreadyExistsException.
		if (dir.exists())
		{
			DeleteFileExtensions.delete(dir);
		}
		final boolean created = CreateFileQuietlyExtensions.newDirectoryQuietly(dir);
		assertTrue("directory should be created.", created);
		assertTrue("directory should exist.", dir.exists());
		assertTrue("File object should be a directory.", dir.isDirectory());
		// Finally delete the test directory...
		DeleteFileExtensions.delete(dir);
	}

	/**
	 * Test method for {@link CreateFileQuietlyExtensions#newFileQuietly(File)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testNewFileQuietly() throws IOException
	{
		final File file = new File("/tmp/foo/bar/test.file");
		// if the file exist delete it to prevent a DirectoryAllreadyExistsException.
		if (file.exists())
		{
			DeleteFileExtensions.delete(file);
		}
		final boolean created = CreateFileQuietlyExtensions.newFileQuietly(file);
		assertTrue("File should be created.", created);
		assertTrue("File should exist.", file.exists());
		assertTrue("File object should be a file.", file.isFile());
		// Finally delete the test file...
		DeleteFileExtensions.delete(file);
	}

}
