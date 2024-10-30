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
package io.github.astrapi69.file.delete;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;

import io.github.astrapi69.file.exception.DirectoryHasNoContentException;
import io.github.astrapi69.file.exception.FileDoesNotExistException;
import io.github.astrapi69.file.exception.FileIsNotADirectoryException;
import io.github.astrapi69.file.exception.FileIsSecurityRestrictedException;
import io.github.astrapi69.file.search.FileSearchExtensions;
import io.github.astrapi69.io.file.filter.PrefixFileFilter;

/**
 * The class {@link DeleteFileExtensions} helps delete files, leveraging new functionalities from
 * java.nio.file introduced in JDK 17 for optimized handling
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public final class DeleteFileExtensions
{

	/**
	 * Private constructor to prevent instantiation
	 */
	private DeleteFileExtensions()
	{
	}

	/**
	 * Checks if the File is a directory, exists, and is accessible
	 *
	 * @param file
	 *            The File to check
	 * @return Null if all checks pass; otherwise, an appropriate Exception
	 */
	public static Exception checkFile(final File file)
	{
		Exception ex = null;
		String error;
		// check if the file does not exist...
		if (!file.exists())
		{
			error = "The directory " + file + " does not exist";
			ex = new FileDoesNotExistException(error);
			return ex;
		}
		// check if the file is not a directory...
		if (!file.isDirectory())
		{
			error = "The given file '" + file + "' is not a directory";
			ex = new FileIsNotADirectoryException(error);
			return ex;
		}
		if (!Files.isReadable(file.toPath()) || !Files.isWritable(file.toPath()))
		{
			error = "The directory " + file + " is empty or security restricted";
			ex = new DirectoryHasNoContentException(error);
		}
		return ex;
	}

	/**
	 * Tries to delete all given files in the collection. Caution: This can not be undone.
	 *
	 * @param files
	 *            The files to delete
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void delete(final Collection<File> files) throws IOException
	{
		if (files != null && !files.isEmpty())
		{
			for (File file : files)
			{
				delete(file);
			}
		}
	}

	/**
	 * Tries to delete the specified file or directory recursively if it is a directory
	 *
	 * @param file
	 *            The file or directory to delete
	 * @return <code>true</code> if deletion is successful; <code>false</code> otherwise
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static boolean delete(final File file) throws IOException
	{
		Objects.requireNonNull(file);
		if (file.isDirectory())
		{
			deleteAllFiles(file.toPath());
			return Files.deleteIfExists(file.toPath());
		}
		else
		{
			return Files.deleteIfExists(file.toPath());
		}
	}

	/**
	 * Deletes all files and directories recursively within the specified directory
	 *
	 * @param dirPath
	 *            The path of the directory to delete files within
	 * @throws IOException
	 *             if an I/O exception occurs
	 */
	public static void deleteAllFiles(Path dirPath) throws IOException
	{
		Files.walkFileTree(dirPath, EnumSet.noneOf(FileVisitOption.class), Integer.MAX_VALUE,
			new SimpleFileVisitor<Path>()
			{
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
					throws IOException
				{
					Files.delete(file);
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult postVisitDirectory(Path dir, IOException exc)
					throws IOException
				{
					Files.delete(dir);
					return FileVisitResult.CONTINUE;
				}
			});
	}

	/**
	 * Deletes the File and if it is an directory it deletes his sub-directories recursively.
	 *
	 * @param file
	 *            The File to delete.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void deleteAllFiles(final File file) throws IOException
	{
		if (!file.exists())
		{
			return;
		}
		final Exception ex = checkFile(file);
		if (ex != null)
		{
			try
			{
				throw ex;
			}
			catch (final Exception e)
			{
				e.printStackTrace();
			}
		}
		DeleteFileExtensions.deleteFiles(file);
		if (!file.delete())
		{
			throw new IOException("Cannot delete the File " + file.getAbsolutePath() + ".");
		}
	}

	/**
	 * Deletes all files with the given suffix recursively.
	 *
	 * @param directory
	 *            The directory from where to delete the files with the given suffix.
	 * @param theSuffix
	 *            The suffix from the files to delete.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void deleteAllFilesWithSuffix(final File directory, final String theSuffix)
		throws IOException
	{
		final List<File> files = FileSearchExtensions.findFiles(directory.getAbsolutePath(),
			new String[] { theSuffix });
		for (File file : files)
		{
			deleteFile(file);
		}
	}

	/**
	 * Attempts to delete a specific file
	 *
	 * @param fileToDelete
	 *            The file to delete
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void deleteFile(final File fileToDelete) throws IOException
	{
		delete(fileToDelete);
	}

	/**
	 * Tries to delete all files in the Directory.
	 *
	 * @param file
	 *            The Directory to delete files.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void deleteFiles(final File file) throws IOException
	{
		final File[] ff = file.listFiles();
		if (ff != null)
		{
			for (final File f : ff)
			{
				DeleteFileExtensions.delete(f);
			}
		}
	}

	/**
	 * Tries to delete all files that match to the given includeFileFilter from the given source
	 * directory.
	 *
	 * @param sourceDir
	 *            The source directory.
	 * @param includeFileFilter
	 *            The FileFilter for the files to be deleted. If null all files will be deleted.
	 *
	 * @throws FileIsNotADirectoryException
	 *             Is thrown if the destination file is a directory.
	 * @throws IOException
	 *             Is thrown if an error occurs by reading or writing.
	 * @throws FileIsSecurityRestrictedException
	 *             Is thrown if the source file is security restricted.
	 */
	public static void deleteFilesWithFileFilter(final File sourceDir,
		final FileFilter includeFileFilter)
		throws FileIsNotADirectoryException, IOException, FileIsSecurityRestrictedException
	{
		DeleteFileExtensions.deleteFilesWithFileFilter(sourceDir, includeFileFilter, null);
	}

	/**
	 * Tries to delete all files that match to the given prefix from the given source directory.
	 *
	 *
	 * @param sourceDir
	 *            The source directory
	 * @param prefix
	 *            The prefix from the files to delete
	 *
	 * @throws FileIsNotADirectoryException
	 *             Is thrown if the destination file is a directory.
	 * @throws IOException
	 *             Is thrown if an error occurs by reading or writing.
	 * @throws FileIsSecurityRestrictedException
	 *             Is thrown if the source file is security restricted.
	 */
	public static void deleteAllFilesWithPrefix(final File sourceDir, final String prefix)
		throws FileIsNotADirectoryException, FileIsSecurityRestrictedException, IOException
	{
		DeleteFileExtensions.deleteFilesWithFileFilter(sourceDir, new PrefixFileFilter(prefix));
	}

	/**
	 * Tries to delete all files that match to the given includeFileFilter and does not delete the
	 * files that match the excludeFileFilter from the given source directory.
	 *
	 * @param source
	 *            The source directory.
	 * @param includeFileFilter
	 *            The FileFilter for the files to be deleted. If null all files will be deleted.
	 * @param excludeFileFilter
	 *            The FileFilter for the files to be not deleted. If null no files will be excluded
	 *            by delete process.
	 *
	 * @throws FileIsNotADirectoryException
	 *             Is thrown if the destination file is a directory.
	 * @throws IOException
	 *             Is thrown if an error occurs by reading or writing.
	 * @throws FileIsSecurityRestrictedException
	 *             Is thrown if the source file is security restricted.
	 */
	public static void deleteFilesWithFileFilter(final File source,
		final FileFilter includeFileFilter, final FileFilter excludeFileFilter)
		throws FileIsNotADirectoryException, IOException, FileIsSecurityRestrictedException
	{
		if (!source.isDirectory())
		{
			throw new FileIsNotADirectoryException(
				"Source file '" + source.getAbsolutePath() + "' is not a directory.");
		}
		File[] includeFilesArray;

		if (null != includeFileFilter)
		{
			includeFilesArray = source.listFiles(includeFileFilter);
		}
		else
		{
			includeFilesArray = source.listFiles();
		}

		if (null != includeFilesArray)
		{
			File[] excludeFilesArray;
			List<File> excludeFilesList = null;
			if (null != excludeFileFilter)
			{
				excludeFilesArray = source.listFiles(excludeFileFilter);
				excludeFilesList = Arrays.asList(excludeFilesArray);
			}
			// if excludeFilesList is not null and not empty
			if (null != excludeFilesList && !excludeFilesList.isEmpty())
			{
				for (final File currentFile : includeFilesArray)
				{
					// if the excludeFilesList does not contain the current file do copy...
					if (!excludeFilesList.contains(currentFile))
					{
						if (currentFile.isDirectory())
						{
							// delete directory recursive...
							deleteFilesWithFileFilter(currentFile, includeFileFilter,
								excludeFileFilter);
						}
						else
						{ // delete file
							deleteFile(currentFile);
						}
					} // otherwise do not delete the current file...
				}
			}
			else
			{ // otherwise delete all files and directories
				for (final File currentFile : includeFilesArray)
				{
					if (currentFile.isDirectory())
					{
						// delete directory recursive...
						deleteFilesWithFileFilter(currentFile, includeFileFilter,
							excludeFileFilter);
					}
					else
					{ // delete file
						deleteFile(currentFile);
					}
				}
			}
		}
		else
		{
			throw new FileIsSecurityRestrictedException(
				"File '" + source.getAbsolutePath() + "' is security restricted.");
		}
	}

	/**
	 * Tries to delete all files that match to the given includeFilenameFilter from the given source
	 * directory.
	 *
	 * @param source
	 *            The source directory.
	 * @param includeFilenameFilter
	 *            The FilenameFilter for the files to be deleted. If null all files will be deleted.
	 *
	 * @throws FileIsNotADirectoryException
	 *             Is thrown if the destination file is a directory.
	 * @throws IOException
	 *             Is thrown if an error occurs by reading or writing.
	 * @throws FileIsSecurityRestrictedException
	 *             Is thrown if the source file is security restricted.
	 */
	public static void deleteFilesWithFilenameFilter(final File source,
		final FilenameFilter includeFilenameFilter)
		throws FileIsNotADirectoryException, IOException, FileIsSecurityRestrictedException
	{
		DeleteFileExtensions.deleteFilesWithFilenameFilter(source, includeFilenameFilter, null);
	}

	/**
	 * Tries to delete all files that match to the given includeFilenameFilter and does not delete
	 * the files that match the excludeFilenameFilter from the given source directory.
	 *
	 * @param source
	 *            The source directory.
	 * @param includeFilenameFilter
	 *            The FilenameFilter for the files to be deleted. If null all files will be deleted.
	 * @param excludeFilenameFilter
	 *            The FilenameFilter for the files to be not deleted. If null no files will be
	 *            excluded by delete process.
	 *
	 * @throws FileIsNotADirectoryException
	 *             Is thrown if the destination file is a directory.
	 * @throws IOException
	 *             Is thrown if an error occurs by reading or writing.
	 * @throws FileIsSecurityRestrictedException
	 *             Is thrown if the source file is security restricted.
	 */
	public static void deleteFilesWithFilenameFilter(final File source,
		final FilenameFilter includeFilenameFilter, final FilenameFilter excludeFilenameFilter)
		throws FileIsNotADirectoryException, IOException, FileIsSecurityRestrictedException
	{
		if (!source.isDirectory())
		{
			throw new FileIsNotADirectoryException(
				"Source file '" + source.getAbsolutePath() + "' is not a directory.");
		}
		File[] includeFilesArray;

		if (null != includeFilenameFilter)
		{
			includeFilesArray = source.listFiles(includeFilenameFilter);
		}
		else
		{
			includeFilesArray = source.listFiles();
		}

		if (null != includeFilesArray)
		{
			File[] excludeFilesArray;
			List<File> excludeFilesList = null;
			if (null != excludeFilenameFilter)
			{
				excludeFilesArray = source.listFiles(excludeFilenameFilter);
				excludeFilesList = Arrays.asList(excludeFilesArray);
			}
			// if excludeFilesList is not null and not empty
			if (null != excludeFilesList && !excludeFilesList.isEmpty())
			{
				for (final File currentFile : includeFilesArray)
				{
					// if the excludeFilesList does not contain the current file do copy...
					if (!excludeFilesList.contains(currentFile))
					{
						if (currentFile.isDirectory())
						{
							// delete directory recursive...
							deleteFilesWithFilenameFilter(currentFile, includeFilenameFilter,
								excludeFilenameFilter);
						}
						else
						{ // delete file
							deleteFile(currentFile);
						}
					} // otherwise do not delete the current file...
				}
			}
			else
			{ // otherwise delete all files and directories
				for (final File currentFile : includeFilesArray)
				{
					if (currentFile.isDirectory())
					{
						// delete directory recursive...
						deleteFilesWithFilenameFilter(currentFile, includeFilenameFilter,
							excludeFilenameFilter);
					}
					else
					{ // delete file
						deleteFile(currentFile);
					}
				}
			}
		}
		else
		{
			throw new FileIsSecurityRestrictedException(
				"File '" + source.getAbsolutePath() + "' is security restricted.");
		}
	}

}
