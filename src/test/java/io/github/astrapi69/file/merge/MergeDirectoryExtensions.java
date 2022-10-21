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

import io.github.astrapi69.file.create.DirectoryFactory;
import io.github.astrapi69.file.create.DirectoryStructureFactory;
import io.github.astrapi69.file.system.SystemFileExtensions;
import org.apache.commons.io.FileUtils;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

public class MergeDirectoryExtensions
{

	public static void copyDirectoryToDirectory(final File sourceDir, final File destinationDir)
		throws IOException
	{

		FileUtils.copyDirectoryToDirectory(sourceDir, destinationDir);
	}

	@Test
	public void testModifyFile() throws IOException
	{
		File sourceDir;
		File destinationDir;
		File userTmpDir = SystemFileExtensions.getUserTempDir();
		File userTempDir = SystemFileExtensions.getUserTempDir("temp");

		Collection<File> tmpContent = DirectoryStructureFactory.newDirectoryStructure(userTmpDir,
			"content", "content/first", "content/first/deep", "content/second");
		Collection<File> tempContent = DirectoryStructureFactory.newDirectoryStructure(userTempDir,
			"content", "content/first", "content/first/deep", "content/second");
		sourceDir = userTmpDir;
		destinationDir = userTempDir;
		FileUtils.copyDirectoryToDirectory(sourceDir, destinationDir);
	}

}
