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
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;

/**
 * The class {@link MergeDirectoryExtensions} provides methods for simple merge of directories
 */
public class MergeDirectoryExtensions
{

	/**
	 * Merge the given directories to merge to the given target directory
	 * 
	 * @param targetDir
	 *            the target directory
	 * @param directoriesToMerge
	 *            the directories to merge in the target directory
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void merge(File targetDir, File... directoriesToMerge) throws IOException
	{
		final Map<String, File> fileStore = new HashMap<>();
		for (File directoryToMerge : directoriesToMerge)
		{
			refreshFileStore(directoryToMerge, fileStore, null);
		}

		for (final Map.Entry<String, File> fileEntry : fileStore.entrySet())
		{
			final String relativeName = fileEntry.getKey();
			final File srcFile = fileEntry.getValue();
			FileUtils.copyFile(srcFile, new File(targetDir, relativeName));
		}
	}

	private static void refreshFileStore(final File baseDirectory,
		final Map<String, File> fileStore, final String relativeName)
	{
		for (final File file : baseDirectory.listFiles())
		{
			final String relativeFileName = getRelativeFileName(relativeName, file.getName());
			if (file.isDirectory())
			{
				refreshFileStore(file, fileStore, relativeFileName);
			}
			else
			{
				final File existingFile = fileStore.get(relativeFileName);
				if (existingFile == null || file.lastModified() > existingFile.lastModified())
				{
					fileStore.put(relativeFileName, file);
				}
			}
		}
	}

	private static String getRelativeFileName(final String baseName, final String fileName)
	{
		return baseName == null ? fileName : baseName + "/" + fileName;
	}

}
