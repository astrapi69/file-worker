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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
	 *             Signals that an I/O exception has occurred. A directory that cannot be read, and
	 *             under {@link MergeStrategy#TARGET_AS_MASTER} a file or an emptied directory that
	 *             cannot be deleted from the source, does not stop the merge: after every source is
	 *             merged, one exception names each of them, with the single failures attached as
	 *             suppressed exceptions. The target holds the copies. A failure that stops the
	 *             merge, such as a copy that fails, carries the failures collected until then as
	 *             suppressed exceptions.
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

		final Map<File, IOException> directoriesNotRead = new LinkedHashMap<>();
		final Map<File, IOException> sourceFilesLeft = new LinkedHashMap<>();
		try
		{
			for (File sourceDir : directoriesToMerge)
			{
				if (sourceDir != null && sourceDir.isDirectory())
				{
					processDirectory(sourceDir, targetDir, "", strategy, directoriesNotRead,
						sourceFilesLeft);
				}
			}
		}
		catch (IOException mergeStopped)
		{
			addSuppressed(mergeStopped, directoriesNotRead, sourceFilesLeft);
			throw mergeStopped;
		}
		if (!directoriesNotRead.isEmpty() || !sourceFilesLeft.isEmpty())
		{
			throw newIncompleteMergeException(targetDir, directoriesNotRead, sourceFilesLeft);
		}
		return targetDir;
	}

	/**
	 * Merge the given directories to the given target directory and returns the target directory. A
	 * directory to merge that is null, does not exist or is no directory is skipped.
	 *
	 * @param targetDir
	 *            the target directory
	 * @param directoriesToMerge
	 *            the directories to merge in the target directory
	 * @return the targetDir
	 * @throws IOException
	 *             Signals that an I/O exception has occurred. A directory that cannot be read does
	 *             not stop the merge: after every other file is merged, one exception names each of
	 *             them, with the single failures attached as suppressed exceptions.
	 */
	public static File mergeAndGet(File targetDir, File... directoriesToMerge) throws IOException
	{
		Objects.requireNonNull(targetDir, "targetDir must not be null");
		Objects.requireNonNull(directoriesToMerge, "directoriesToMerge must not be null");

		final Map<File, IOException> directoriesNotRead = new LinkedHashMap<>();
		newFileMergeStore(directoriesNotRead, directoriesToMerge).entrySet().forEach(fileEntry -> {
			final String relativeName = fileEntry.getKey();
			final File srcFile = fileEntry.getValue();
			RuntimeExceptionDecorator
				.decorate(() -> FileUtils.copyFile(srcFile, new File(targetDir, relativeName)));
		});
		if (!directoriesNotRead.isEmpty())
		{
			throw newIncompleteMergeException(targetDir, directoriesNotRead, Map.of());
		}
		return targetDir;
	}

	/**
	 * Merge the given directories to merge to the given target directory. A directory to merge that
	 * is null, does not exist or is no directory is skipped.
	 *
	 * @param targetDir
	 *            the target directory
	 * @param directoriesToMerge
	 *            the directories to merge in the target directory
	 * @throws IOException
	 *             Signals that an I/O exception has occurred. A directory that cannot be read does
	 *             not stop the merge: after every other file is merged, one exception names each of
	 *             them, with the single failures attached as suppressed exceptions.
	 */
	public static void merge(File targetDir, File... directoriesToMerge) throws IOException
	{
		mergeAndGet(targetDir, directoriesToMerge);
	}

	private static Map<String, File> newFileMergeStore(
		final Map<File, IOException> directoriesNotRead, File... directoriesToMerge)
	{
		final Map<String, File> fileMergeStore = new HashMap<>();
		for (File directoryToMerge : directoriesToMerge)
		{
			if (directoryToMerge != null && directoryToMerge.isDirectory())
			{
				refreshFileStore(directoryToMerge, fileMergeStore, null, directoriesNotRead);
			}
		}
		return fileMergeStore;
	}

	private static void refreshFileStore(final File baseDirectory,
		final Map<String, File> fileStore, final String relativeName,
		final Map<File, IOException> directoriesNotRead)
	{
		File[] files = listFiles(baseDirectory, directoriesNotRead);
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
				refreshFileStore(file, fileStore, relativeFileName, directoriesNotRead);
			}
		}
	}

	private static String getRelativeFileName(final String baseName, final String fileName)
	{
		return baseName == null ? fileName : baseName + "/" + fileName;
	}

	/**
	 * Lists the given directory, and records it with the reason when it cannot be read, so that the
	 * merge goes on and names it at the end (file-worker#76)
	 */
	private static File[] listFiles(File directory, Map<File, IOException> directoriesNotRead)
	{
		try (Stream<Path> entries = Files.list(directory.toPath()))
		{
			return entries.map(Path::toFile).toArray(File[]::new);
		}
		catch (IOException exception)
		{
			directoriesNotRead.put(directory, exception);
			return new File[0];
		}
	}

	/**
	 * Tells whether the given directory can be read and is empty: one that cannot be read is not
	 * known to be empty, so it stays in the source (file-worker#76)
	 */
	private static boolean isEmptyDirectory(File directory)
	{
		String[] entries = directory.list();
		return entries != null && entries.length == 0;
	}

	private static void processDirectory(File sourceDir, File targetDir, String relativePath,
		MergeStrategy strategy, Map<File, IOException> directoriesNotRead,
		Map<File, IOException> sourceFilesLeft) throws IOException
	{
		File[] files = listFiles(sourceDir, directoriesNotRead);
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
				processDirectory(file, targetDir, currentRelPath, strategy, directoriesNotRead,
					sourceFilesLeft);

				// Optional: Leere Verzeichnisse in der Quelle löschen bei TARGET_AS_MASTER
				if (strategy == MergeStrategy.TARGET_AS_MASTER && isEmptyDirectory(file))
				{
					deleteFromSource(file, sourceFilesLeft);
				}
			}
			else
			{
				applyStrategyToFile(file, targetFile, strategy, sourceFilesLeft);
			}
		}
	}

	private static void applyStrategyToFile(File sourceFile, File targetFile,
		MergeStrategy strategy, Map<File, IOException> sourceFilesLeft) throws IOException
	{
		boolean targetExists = targetFile.exists();
		boolean contentEqual = targetExists && FileUtils.contentEquals(sourceFile, targetFile);

		if (!targetExists)
		{
			// exists in source, but not in target -> copy from source to target
			FileUtils.copyFile(sourceFile, targetFile);
			if (strategy == MergeStrategy.TARGET_AS_MASTER)
			{
				deleteFromSource(sourceFile, sourceFilesLeft);
			}
		}
		else if (contentEqual)
		{
			// exists in both, and content is equal -> leave target unchanged
			if (strategy == MergeStrategy.TARGET_AS_MASTER)
			{
				deleteFromSource(sourceFile, sourceFilesLeft);
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
				deleteFromSource(sourceFile, sourceFilesLeft);
			}
		}
	}

	/**
	 * Deletes the given file or empty directory from the source, and records it with the reason
	 * when that fails, so the merge goes on and reports every file left at the end
	 */
	private static void deleteFromSource(File sourceFile, Map<File, IOException> sourceFilesLeft)
	{
		try
		{
			Files.delete(sourceFile.toPath());
		}
		catch (IOException exception)
		{
			sourceFilesLeft.put(sourceFile, exception);
		}
	}

	/**
	 * Builds the one exception of a merge that did not do everything: it names the directories that
	 * could not be read and the source files that could not be deleted, and carries their single
	 * failures as suppressed exceptions
	 */
	private static IOException newIncompleteMergeException(File targetDir,
		Map<File, IOException> directoriesNotRead, Map<File, IOException> sourceFilesLeft)
	{
		final List<String> failures = new ArrayList<>();
		if (!directoriesNotRead.isEmpty())
		{
			failures.add("the directories that could not be read are left out ("
				+ directoriesNotRead.size() + "): " + joinAbsolutePaths(directoriesNotRead));
		}
		if (!sourceFilesLeft.isEmpty())
		{
			failures.add("the source keeps what could not be deleted (" + sourceFilesLeft.size()
				+ "): " + joinAbsolutePaths(sourceFilesLeft));
		}
		IOException exception = new IOException("Merged into " + targetDir.getAbsolutePath()
			+ ", but " + String.join("; and ", failures));
		addSuppressed(exception, directoriesNotRead, sourceFilesLeft);
		return exception;
	}

	/**
	 * Attaches the single failures of the merge to the given exception, so that an exception that
	 * stops the merge does not drop the ones collected until then (file-worker#80)
	 */
	private static void addSuppressed(IOException exception,
		Map<File, IOException> directoriesNotRead, Map<File, IOException> sourceFilesLeft)
	{
		directoriesNotRead.values().forEach(exception::addSuppressed);
		sourceFilesLeft.values().forEach(exception::addSuppressed);
	}

	private static String joinAbsolutePaths(Map<File, IOException> failures)
	{
		return failures.keySet().stream().map(File::getAbsolutePath)
			.collect(Collectors.joining(", "));
	}

}
