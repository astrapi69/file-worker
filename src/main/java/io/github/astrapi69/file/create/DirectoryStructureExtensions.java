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

import java.io.File;
import java.util.List;

import io.github.astrapi69.collection.list.ListFactory;
import io.github.astrapi69.file.create.model.FileContentInfo;
import io.github.astrapi69.file.search.FileSearchExtensions;

/**
 * The class {@link DirectoryStructureExtensions} provides methods for get File infos from directory
 * structures
 *
 * @author Asterios Raptis
 * @version 1.0
 */
public final class DirectoryStructureExtensions
{

	/**
	 * Private constructor to prevent instantiation
	 */
	private DirectoryStructureExtensions()
	{
	}

	/**
	 * Creates a list of {@link FileContentInfo} objects from the given directory
	 *
	 * @param directory
	 *            the directory
	 * @return a list of {@link FileContentInfo} objects from the given directory
	 */
	public static List<FileContentInfo> getFileContentInfos(final File directory)
	{
		final List<FileContentInfo> fileContentInfos = ListFactory.newArrayList();
		if (directory.isFile())
		{
			return fileContentInfos;
		}
		final List<File> allFilesFromDir = FileSearchExtensions
			.getAllFilesFromDirRecursive(directory, true);
		allFilesFromDir
			.forEach(file -> fileContentInfos.add(FileContentInfo.toFileContentInfo(file)));
		return fileContentInfos;
	}
}
