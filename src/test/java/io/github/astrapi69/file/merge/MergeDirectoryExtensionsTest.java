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
package io.github.astrapi69.file.merge;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.testng.annotations.Test;

import io.github.astrapi69.file.create.DirectoryFactory;
import io.github.astrapi69.file.create.DirectoryStructureFactory;
import io.github.astrapi69.file.create.DirectoryStructureTestData;
import io.github.astrapi69.file.create.FileContentInfo;
import io.github.astrapi69.file.delete.DeleteFileExtensions;
import io.github.astrapi69.file.search.PathFinder;

public class MergeDirectoryExtensionsTest
{

	@Test
	public void testMergeDirectory() throws IOException
	{
		Collection<File> leftSide;
		Collection<File> rightSide;
		Collection<FileContentInfo> fileInfos;
		String parentAbsolutePath;
		// new scenario...
		String absolutePath = PathFinder.getSrcTestResourcesDir().getAbsolutePath();
		File parentLeftFile = DirectoryFactory.newDirectory(absolutePath, "app");
		parentAbsolutePath = parentLeftFile.getAbsolutePath();
		fileInfos = DirectoryStructureTestData.newTestData(parentAbsolutePath);

		leftSide = DirectoryStructureFactory.newDirectoryStructure(fileInfos);

		File parentRightFile = DirectoryFactory.newDirectory(absolutePath, "other");
		parentAbsolutePath = parentRightFile.getAbsolutePath();
		fileInfos = DirectoryStructureTestData.newOtherTestData(parentAbsolutePath);
		rightSide = DirectoryStructureFactory.newDirectoryStructure(fileInfos);

		MergeDirectoryExtensions.merge(parentLeftFile, parentRightFile);

		// cleanup...
		leftSide.add(parentLeftFile);
		DeleteFileExtensions.delete(leftSide);
		rightSide.add(parentRightFile);
		DeleteFileExtensions.delete(rightSide);
	}

}
