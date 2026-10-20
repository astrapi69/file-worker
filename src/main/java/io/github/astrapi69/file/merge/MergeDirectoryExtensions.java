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
import java.util.Objects;

import org.apache.commons.io.FileUtils;

import io.github.astrapi69.file.merge.strategy.MergeStrategy;
import io.github.astrapi69.throwable.RuntimeExceptionDecorator;

/**
 * The class {@link MergeDirectoryExtensions} provides methods for simple merge of directories
 */
public final class MergeDirectoryExtensions
{

	/**
	 * Private constructor to prevent instantiation
	 */
	private MergeDirectoryExtensions()
	{
	}

	/**
	 * Merge the given directories to the given target directory using a specific strategy, and
	 * returns the target directory.
	 *
	 * @param targetDir
	 *            the target directory
	 * @param strategy
	 *            the merge strategy to apply
	 * @param directoriesToMerge
	 *            the directories to merge into the target directory
	 * @return the targetDir
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static File mergeAndGet(File targetDir, MergeStrategy strategy,
		File... directoriesToMerge) throws IOException
	{
		Objects.requireNonNull(targetDir, "targetDir must not be null");
		Objects.requireNonNull(strategy, "strategy must not be null");
		Objects.requireNonNull(directoriesToMerge, "directoriesToMerge must not be null");

		if (!targetDir.exists())
		{
			targetDir.mkdirs();
		}

		for (File sourceDir : directoriesToMerge)
		{
			if (sourceDir != null && sourceDir.exists())
			{
				processDirectory(sourceDir, targetDir, "", strategy);
			}
		}
		return targetDir;
	}

	/**
	 * Merge the given directories to the given target directory and returns the target directory.
	 *
	 * @param targetDir
	 *            the target directory
	 * @param directoriesToMerge
	 *            the directories to merge in the target directory
	 * @return the targetDir
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static File mergeAndGet(File targetDir, File... directoriesToMerge) throws IOException
	{
		Objects.requireNonNull(targetDir, "targetDir must not be null");
		Objects.requireNonNull(directoriesToMerge, "directoriesToMerge must not be null");

		newFileMergeStore(directoriesToMerge).entrySet().forEach(fileEntry -> {
			final String relativeName = fileEntry.getKey();
			final File srcFile = fileEntry.getValue();
			RuntimeExceptionDecorator
				.decorate(() -> FileUtils.copyFile(srcFile, new File(targetDir, relativeName)));
		});
		return targetDir;
	}

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
		mergeAndGet(targetDir, directoriesToMerge);
	}

	private static Map<String, File> newFileMergeStore(File... directoriesToMerge)
	{
		final Map<String, File> fileMergeStore = new HashMap<>();
		for (File directoryToMerge : directoriesToMerge)
		{
			refreshFileStore(directoryToMerge, fileMergeStore, null);
		}
		return fileMergeStore;
	}

	private static void refreshFileStore(final File baseDirectory,
		final Map<String, File> fileStore, final String relativeName)
	{
		File[] files = Objects.requireNonNull(baseDirectory.listFiles());
		for (File file : files)
		{
			final String relativeFileName = getRelativeFileName(relativeName, file.getName());
			if (file.isFile())
			{
				final File existingFile = fileStore.get(relativeFileName);
				if (existingFile == null || file.lastModified() > existingFile.lastModified())
				{
					fileStore.put(relativeFileName, file);
				}
			}
			else
			{
				refreshFileStore(file, fileStore, relativeFileName);
			}
		}
	}

	private static String getRelativeFileName(final String baseName, final String fileName)
	{
		return baseName == null ? fileName : baseName + "/" + fileName;
	}

	private static void processDirectory(File sourceDir, File targetDir, String relativePath,
		MergeStrategy strategy) throws IOException
	{
		File[] files = sourceDir.listFiles();
		if (files == null)
		{
			return;
		}

		for (File file : files)
		{
			String currentRelPath = relativePath.isEmpty()
				? file.getName()
				: relativePath + "/" + file.getName();
			File targetFile = new File(targetDir, currentRelPath);

			if (file.isDirectory())
			{
				if (!targetFile.exists())
				{
					targetFile.mkdirs();
				}
				processDirectory(file, targetDir, currentRelPath, strategy);

				// Optional: Leere Verzeichnisse in der Quelle löschen bei TARGET_AS_MASTER
				if (strategy == MergeStrategy.TARGET_AS_MASTER && file.listFiles().length == 0)
				{
					file.delete();
				}
			}
			else
			{
				applyStrategyToFile(file, targetFile, strategy);
			}
		}
	}

	private static void applyStrategyToFile(File sourceFile, File targetFile,
		MergeStrategy strategy) throws IOException
	{
		boolean targetExists = targetFile.exists();
		boolean contentEqual = targetExists && FileUtils.contentEquals(sourceFile, targetFile);

		if (!targetExists)
		{
			// exists in source, but not in target -> copy from source to target
			FileUtils.copyFile(sourceFile, targetFile);
			if (strategy == MergeStrategy.TARGET_AS_MASTER)
			{
				sourceFile.delete(); // and delete from source
			}
		}
		else if (contentEqual)
		{
			// exists in both, and content is equal -> leave target unchanged
			if (strategy == MergeStrategy.TARGET_AS_MASTER)
			{
				sourceFile.delete(); // and delete from source
			}
		}
		else
		{
			// exists in both, but content not equal -> try to merge, if fail copy from source to
			// target
			// Fallback: Overwrite target with source (standard behavior for generic file merge)
			FileUtils.copyFile(sourceFile, targetFile);
			if (strategy == MergeStrategy.TARGET_AS_MASTER)
			{
				sourceFile.delete(); // and delete from source
			}
		}
	}

}
