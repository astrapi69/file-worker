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

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.github.astrapi69.collection.CollectionExtensions;
import io.github.astrapi69.file.delete.DeleteFileExtensions;
import io.github.astrapi69.file.search.PathFinder;

/**
 * The unit test class for the class {@link DirectoryStructureExtensions}
 */
public class DirectoryStructureExtensionsTest
{

	/**
	 * Test method for {@link DirectoryStructureExtensions#getFileContentInfos(File)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testGetFileContentInfos() throws IOException, NoSuchAlgorithmException
	{
		List<FileContentInfo> actual;
		List<FileContentInfo> expected;
		String parentAbsolutePath;
		// new scenario...
		String absolutePath = PathFinder.getSrcTestResourcesDir().getAbsolutePath();
		File parentFile = DirectoryFactory.newDirectory(absolutePath, "app");
		parentAbsolutePath = parentFile.getAbsolutePath();
		actual = DirectoryStructureTestData.newTestData(parentAbsolutePath);
		Collection<File> files = DirectoryStructureFactory.newDirectoryStructure(actual);
		expected = DirectoryStructureExtensions.getFileContentInfos(parentFile);
		assertTrue(CollectionExtensions.isEqualCollection(actual, expected));
		DeleteFileExtensions.delete(files);
	}
}
