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

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.testng.annotations.Test;

import io.github.astrapi69.collection.array.ArrayFactory;
import io.github.astrapi69.collection.list.ListFactory;
import io.github.astrapi69.file.delete.DeleteFileExtensions;
import io.github.astrapi69.file.search.PathFinder;

/**
 * The unit test class for the class {@link DirectoryStructureFactory}
 */
public class DirectoryStructureFactoryTest
{

	/**
	 * Test method for {@link DirectoryStructureFactory#newDirectoryStructure(Collection)}
	 */
	@Test
	public void testNewDirectoryStructureWithContent() throws IOException
	{
		Collection<File> actual;
		Collection<File> expected;
		Collection<FileContentInfo> fileInfos;
		String parentAbsolutePath;
		// new scenario...
		String absolutePath = PathFinder.getSrcTestResourcesDir().getAbsolutePath();
		File parentFile = DirectoryFactory.newDirectory(absolutePath, "app");
		parentAbsolutePath = parentFile.getAbsolutePath();
		fileInfos = DirectoryStructureTestData.newTestData(parentAbsolutePath);
		expected = ListFactory.newArrayList();
		fileInfos.forEach(fileContentInfo1 -> expected.add(FileInfo.toFile(fileContentInfo1)));

		actual = DirectoryStructureFactory.newDirectoryStructure(fileInfos);
		assertEquals(expected, actual);
		// cleanup...
		actual.add(parentFile);
		DeleteFileExtensions.delete(actual);
	}


	/**
	 * Test method for {@link DirectoryStructureFactory#newDirectoryStructure(File, Collection)}
	 */
	@Test
	public void testNewDirectoryStructureFileCollection() throws IOException
	{
		Collection<File> actual;
		Collection<File> expected;
		Collection<String> filePaths;
		File parent;
		// new scenario...
		filePaths = ListFactory.newArrayList("action", "model", "state", "reducer", "reducer/main");
		String absolutePath = PathFinder.getSrcTestResourcesDir().getAbsolutePath();
		parent = DirectoryFactory.newDirectory(absolutePath, "app");
		actual = DirectoryStructureFactory.newDirectoryStructure(parent, filePaths);
		expected = ListFactory.newArrayList(DirectoryFactory.newDirectory(parent, "action"),
			DirectoryFactory.newDirectory(parent, "model"),
			DirectoryFactory.newDirectory(parent, "state"),
			DirectoryFactory.newDirectory(parent, "reducer"),
			DirectoryFactory.newDirectory(parent, "reducer/main"));
		assertEquals(expected, actual);
		DeleteFileExtensions.delete(actual);
	}

	/**
	 * Test method for {@link DirectoryStructureFactory#newDirectoryStructure(String, Collection)}
	 */
	@Test
	public void testNewDirectoryStructureStringCollection() throws IOException
	{
		Collection<File> actual;
		Collection<File> expected;
		Collection<String> filePaths;
		String parentAbsolutePath;
		File parent;
		// new scenario...
		filePaths = ListFactory.newArrayList("action", "model", "state", "reducer", "reducer/main");
		String absolutePath = PathFinder.getSrcTestResourcesDir().getAbsolutePath();
		parent = DirectoryFactory.newDirectory(absolutePath, "app");
		parentAbsolutePath = parent.getAbsolutePath();
		actual = DirectoryStructureFactory.newDirectoryStructure(parentAbsolutePath, filePaths);
		expected = ListFactory.newArrayList(DirectoryFactory.newDirectory(parent, "action"),
			DirectoryFactory.newDirectory(parent, "model"),
			DirectoryFactory.newDirectory(parent, "state"),
			DirectoryFactory.newDirectory(parent, "reducer"),
			DirectoryFactory.newDirectory(parent, "reducer/main"));
		assertEquals(expected, actual);
		DeleteFileExtensions.delete(actual);
	}

	/**
	 * Test method for {@link DirectoryStructureFactory#newDirectoryStructure(String, String...)}
	 */
	@Test
	public void testNewDirectoryStructureStringVararg() throws IOException
	{
		Collection<File> actual;
		Collection<File> expected;
		String[] filePaths;
		String parentAbsolutePath;
		File parent;
		// new scenario...
		filePaths = ArrayFactory.newArray("action", "model", "state", "reducer", "reducer/main");
		String absolutePath = PathFinder.getSrcTestResourcesDir().getAbsolutePath();
		parent = DirectoryFactory.newDirectory(absolutePath, "app");
		parentAbsolutePath = parent.getAbsolutePath();
		actual = DirectoryStructureFactory.newDirectoryStructure(parentAbsolutePath, filePaths);
		expected = ListFactory.newArrayList(DirectoryFactory.newDirectory(parent, "action"),
			DirectoryFactory.newDirectory(parent, "model"),
			DirectoryFactory.newDirectory(parent, "state"),
			DirectoryFactory.newDirectory(parent, "reducer"),
			DirectoryFactory.newDirectory(parent, "reducer/main"));
		assertEquals(expected, actual);
		DeleteFileExtensions.delete(actual);
	}

	/**
	 * Test method for {@link DirectoryStructureFactory#newDirectoryStructure(File, String...)}
	 */
	@Test
	public void testNewDirectoryStructureFileVararg() throws IOException
	{
		Collection<File> actual;
		Collection<File> expected;
		String[] filePaths;
		String parentAbsolutePath;
		File parent;
		// new scenario...
		filePaths = ArrayFactory.newArray("action", "model", "state", "reducer", "reducer/main");
		String absolutePath = PathFinder.getSrcTestResourcesDir().getAbsolutePath();
		parent = DirectoryFactory.newDirectory(absolutePath, "app");
		actual = DirectoryStructureFactory.newDirectoryStructure(parent, filePaths);
		expected = ListFactory.newArrayList(DirectoryFactory.newDirectory(parent, "action"),
			DirectoryFactory.newDirectory(parent, "model"),
			DirectoryFactory.newDirectory(parent, "state"),
			DirectoryFactory.newDirectory(parent, "reducer"),
			DirectoryFactory.newDirectory(parent, "reducer/main"));
		assertEquals(expected, actual);
		DeleteFileExtensions.delete(actual);
	}
}
