/**
 * The MIT License
 *
 * Copyright (C) 2007 Asterios Raptis
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
package de.alpharogroup.file.delete;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import de.alpharogroup.file.exceptions.DirectoryHasNoContentException;
import de.alpharogroup.file.exceptions.FileDoesNotExistException;
import de.alpharogroup.file.exceptions.FileIsNotADirectoryException;
import de.alpharogroup.file.exceptions.FileIsSecurityRestrictedException;
import de.alpharogroup.file.search.FileSearchUtils;

/**
 * The Class DeleteFileUtils helps you delete files.
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public final class DeleteFileUtils
{
	/**
	 * Checks the File if it is a directory or if its exists or if it is empty.
	 *
	 * @param file
	 *            The File to check.
	 *
	 * @return Null if nothing is wrong otherwise an Exception.
	 */
	public static Exception checkFile(final File file)
	{
		Exception ex = null;
		String error = null;
		// check if the file does not exists...
		if (!file.exists())
		{
			error = "The " + file + " does not exists.";
			ex = new FileDoesNotExistException(error);
			return ex;
		}
		// check if the file is not a directory...
		if (!file.isDirectory())
		{
			error = "The " + file + " is not a directory.";
			ex = new FileIsNotADirectoryException(error);
			return ex;
		}

		final File[] ff = file.listFiles();
		// If the file is null
		if (ff == null)
		{ // it is security restricted
			error = "The " + file + " could not list the content.";
			ex = new DirectoryHasNoContentException(error);
		}
		return ex;
	}

	/**
	 * Tries to delete all given files in the list. Caution: This can not be undone.
	 *
	 * @param files
	 *            The files to delete.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void delete(final Collection<File> files) throws IOException
	{
		for (final File file : files)
		{
			delete(file);
		}
	}

	/**
	 * Tries to delete a file and if its a directory than its deletes all the sub-directories.
	 *
	 * @param file
	 *            The File to delete.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void delete(final File file) throws IOException
	{
		if (file.isDirectory())
		{
			DeleteFileUtils.deleteAllFiles(file);
		}
		else
		{
			String error = null;
			// If the file is not deleted
			if (!file.delete())
			{
				error = "Cannot delete the File " + file.getAbsolutePath() + ".";

				throw new IOException(error);

			}
		}
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
		String error = null;
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
		DeleteFileUtils.deleteFiles(file);
		if (!file.delete())
		{
			error = "Cannot delete the File " + file.getAbsolutePath() + ".";

			throw new IOException(error);

		}
	}

	/**
	 * Deletes all files with the given suffix recursively.
	 *
	 * @param file
	 *            The directory from where to delete the files wiht the given suffix.
	 * @param theSuffix
	 *            The suffix from the files to delete.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void deleteAllFilesWithSuffix(final File file, final String theSuffix)
		throws IOException
	{
		final String filePath = file.getAbsolutePath();
		final String suffix[] = { theSuffix };
		final List<File> files = FileSearchUtils.findFiles(filePath, suffix);
		final int fileCount = files.size();
		for (int i = 0; i < fileCount; i++)
		{
			DeleteFileUtils.deleteFile(files.get(i));
		}
	}

	/**
	 * Tries to delete the given file.
	 *
	 * @param fileToDelete
	 *            The file to delete.
	 *
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
				DeleteFileUtils.delete(f);
			}
		}
	}

	/**
	 * Tries to delete all files that match to the given includeFileFilter from the given source
	 * directory.
	 *
	 * @param source
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
	public static void deleteFilesWithFileFilter(final File source,
		final FileFilter includeFileFilter) throws FileIsNotADirectoryException, IOException,
		FileIsSecurityRestrictedException
	{
		DeleteFileUtils.deleteFilesWithFileFilter(source, includeFileFilter, null);
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
			throw new FileIsNotADirectoryException("Source file '" + source.getAbsolutePath()
				+ "' is not a directory.");
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
			File[] excludeFilesArray = null;
			List<File> excludeFilesList = null;
			if (null != excludeFileFilter)
			{
				excludeFilesArray = source.listFiles(excludeFileFilter);
				excludeFilesList = Arrays.asList(excludeFilesArray);
			}
			// if excludeFilesList is not null and not empty
			if (null != excludeFilesList && !excludeFilesList.isEmpty())
			{
				for (final File element : includeFilesArray)
				{
					final File currentFile = element;
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
						deleteFilesWithFileFilter(currentFile, includeFileFilter, excludeFileFilter);
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
			throw new FileIsSecurityRestrictedException("File '" + source.getAbsolutePath()
				+ "' is security restricted.");
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
		final FilenameFilter includeFilenameFilter) throws FileIsNotADirectoryException,
		IOException, FileIsSecurityRestrictedException
	{
		DeleteFileUtils.deleteFilesWithFilenameFilter(source, includeFilenameFilter, null);
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
			throw new FileIsNotADirectoryException("Source file '" + source.getAbsolutePath()
				+ "' is not a directory.");
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
			File[] excludeFilesArray = null;
			List<File> excludeFilesList = null;
			if (null != excludeFilenameFilter)
			{
				excludeFilesArray = source.listFiles(excludeFilenameFilter);
				excludeFilesList = Arrays.asList(excludeFilesArray);
			}
			// if excludeFilesList is not null and not empty
			if (null != excludeFilesList && !excludeFilesList.isEmpty())
			{
				for (final File element : includeFilesArray)
				{
					final File currentFile = element;
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
			throw new FileIsSecurityRestrictedException("File '" + source.getAbsolutePath()
				+ "' is security restricted.");
		}
	}

	/**
	 * Private constructor.
	 */
	private DeleteFileUtils()
	{
		super();
	}

}
