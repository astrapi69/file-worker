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
package io.github.astrapi69.file.copy;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import io.github.astrapi69.file.create.DirectoryFactory;
import io.github.astrapi69.file.exception.DirectoryAlreadyExistsException;
import io.github.astrapi69.file.exception.FileIsADirectoryException;
import io.github.astrapi69.file.exception.FileIsNotADirectoryException;
import io.github.astrapi69.file.exception.FileIsSecurityRestrictedException;
import io.github.astrapi69.io.StreamExtensions;
import io.github.astrapi69.io.file.FileExtension;
import io.github.astrapi69.io.file.FileSize;
import io.github.astrapi69.throwable.RuntimeExceptionDecorator;

/**
 * The class {@link CopyFileExtensions} helps you to copy files or directories.
 */
public final class CopyFileExtensions
{

	/**
	 * Private constructor to prevent instantiation
	 */
	private CopyFileExtensions()
	{
	}

	/**
	 * Copies the given source directory to the given destination directory.
	 *
	 * @param source
	 *            The source directory.
	 * @param destination
	 *            The destination directory.
	 *
	 * @return 's true if the directory is copied, otherwise false.
	 *
	 * @throws FileIsSecurityRestrictedException
	 *             Is thrown if the source file is security restricted.
	 * @throws IOException
	 *             Is thrown if an error occurs by reading or writing.
	 * @throws FileIsADirectoryException
	 *             Is thrown if the destination file is a directory.
	 * @throws FileIsNotADirectoryException
	 *             Is thrown if the source file is not a directory.
	 * @throws DirectoryAlreadyExistsException
	 *             Is thrown if the directory all ready exists.
	 */
	public static boolean copyDirectory(final File source, final File destination)
		throws FileIsSecurityRestrictedException, IOException, FileIsADirectoryException,
		FileIsNotADirectoryException, DirectoryAlreadyExistsException
	{
		return copyDirectory(source, destination, true);
	}

	/**
	 * Copies the given source directory to the given destination directory with the option to set
	 * the lastModified time from the given destination file or directory.
	 *
	 * @param source
	 *            The source directory.
	 * @param destination
	 *            The destination directory.
	 * @param lastModified
	 *            Flag the tells if the attribute lastModified has to be set with the attribute from
	 *            the destination file.
	 *
	 * @return 's true if the directory is copied, otherwise false.
	 *
	 * @throws FileIsSecurityRestrictedException
	 *             Is thrown if the source file is security restricted.
	 * @throws IOException
	 *             Is thrown if an error occurs by reading or writing.
	 * @throws FileIsADirectoryException
	 *             Is thrown if the destination file is a directory.
	 * @throws FileIsNotADirectoryException
	 *             Is thrown if the source file is not a directory.
	 * @throws DirectoryAlreadyExistsException
	 *             Is thrown if the directory all ready exists.
	 */
	public static boolean copyDirectory(final File source, final File destination,
		final boolean lastModified) throws FileIsSecurityRestrictedException, IOException,
		FileIsADirectoryException, FileIsNotADirectoryException, DirectoryAlreadyExistsException
	{
		return copyDirectoryWithFileFilter(source, destination, null, lastModified);
	}

	/**
	 * Copies all files that match to the FileFilter from the given source directory to the given
	 * destination directory with the option to set the lastModified time from the given destination
	 * file or directory.
	 *
	 * @param source
	 *            The source directory.
	 * @param destination
	 *            The destination directory.
	 * @param fileFilter
	 *            The FileFilter for the files to be copied. If null all files will be copied.
	 * @param lastModified
	 *            Flag the tells if the attribute lastModified has to be set with the attribute from
	 *            the destination file.
	 * @return 's true if the directory is copied, otherwise false.
	 * @throws IOException
	 *             Is thrown if an error occurs by reading or writing.
	 * @throws FileIsNotADirectoryException
	 *             Is thrown if the source file is not a directory.
	 * @throws FileIsADirectoryException
	 *             Is thrown if the destination file is a directory.
	 * @throws FileIsSecurityRestrictedException
	 *             Is thrown if the source file is security restricted.
	 * @throws DirectoryAlreadyExistsException
	 *             Is thrown if the directory all ready exists.
	 */
	public static boolean copyDirectoryWithFileFilter(final File source, final File destination,
		final FileFilter fileFilter, final boolean lastModified)
		throws IOException, FileIsNotADirectoryException, FileIsADirectoryException,
		FileIsSecurityRestrictedException, DirectoryAlreadyExistsException
	{
		return copyDirectoryWithFileFilter(source, destination, fileFilter, null, lastModified);
	}

	/**
	 * Copies all files that match to the given includeFileFilter and does not copy all the files
	 * that match the excludeFileFilter from the given source directory to the given destination
	 * directory with the option to set the lastModified time from the given destination file or
	 * directory.
	 *
	 * @param source
	 *            The source directory.
	 * @param destination
	 *            The destination directory.
	 * @param includeFileFilter
	 *            The FileFilter for the files to be copied. If null all files will be copied.
	 * @param excludeFileFilter
	 *            The FileFilter for the files to be not copied. If null no files will be excluded
	 *            by copy process.
	 * @param lastModified
	 *            Flag the tells if the attribute lastModified has to be set with the attribute from
	 *            the destination file.
	 * @return 's true if the directory is copied, otherwise false.
	 * @throws IOException
	 *             Is thrown if an error occurs by reading or writing.
	 * @throws FileIsNotADirectoryException
	 *             Is thrown if the source file is not a directory.
	 * @throws FileIsADirectoryException
	 *             Is thrown if the source or destination file is a directory.
	 * @throws FileIsSecurityRestrictedException
	 *             Is thrown if the source file is security restricted.
	 * @throws DirectoryAlreadyExistsException
	 *             Is thrown if the directory all ready exists.
	 */
	public static boolean copyDirectoryWithFileFilter(final File source, final File destination,
		final FileFilter includeFileFilter, final FileFilter excludeFileFilter,
		final boolean lastModified)
		throws IOException, FileIsNotADirectoryException, FileIsADirectoryException,
		FileIsSecurityRestrictedException, DirectoryAlreadyExistsException
	{
		return copyDirectoryWithFileFilter(source, destination, includeFileFilter,
			excludeFileFilter, null, lastModified);
	}

	/**
	 * Copies all files that match to the given includeFileFilter and does not copy all the files
	 * that match the excludeFileFilter from the given source directory to the given destination
	 * directory with the option to set the lastModified time from the given destination file or
	 * directory.
	 *
	 * @param source
	 *            The source directory.
	 * @param destination
	 *            The destination directory.
	 * @param includeFileFilter
	 *            The FileFilter for the files to be copied. If null all files will be copied.
	 * @param excludeFileFilter
	 *            The FileFilter for the files to be not copied. If null no files will be excluded
	 *            by copy process.
	 * @param excludeFiles
	 *            A list of files that should be not copied. If null no files will be excluded by
	 *            copy process.
	 * @param lastModified
	 *            Flag the tells if the attribute lastModified has to be set with the attribute from
	 *            the destination file.
	 * @return 's true if the directory is copied, otherwise false.
	 * @throws IOException
	 *             Is thrown if an error occurs by reading or writing.
	 * @throws FileIsNotADirectoryException
	 *             Is thrown if the source file is not a directory.
	 * @throws FileIsADirectoryException
	 *             Is thrown if the source or destination file is a directory.
	 * @throws FileIsSecurityRestrictedException
	 *             Is thrown if the source file is security restricted.
	 * @throws DirectoryAlreadyExistsException
	 *             Is thrown if the directory all ready exists.
	 */
	public static boolean copyDirectoryWithFileFilter(final File source, final File destination,
		final FileFilter includeFileFilter, final FileFilter excludeFileFilter,
		final Collection<File> excludeFiles, final boolean lastModified)
		throws IOException, FileIsNotADirectoryException, FileIsADirectoryException,
		FileIsSecurityRestrictedException, DirectoryAlreadyExistsException
	{
		if (!source.isDirectory())
		{
			throw new FileIsNotADirectoryException(
				"Source file '" + source.getAbsolutePath() + "' is not a directory.");
		}
		if (!destination.exists())
		{
			DirectoryFactory.newDirectory(destination);
		}
		boolean copied = false;
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
			List<File> allExcludeFilesList = null;
			List<File> excludeFileFilterList;
			if (null != excludeFileFilter)
			{
				excludeFilesArray = source.listFiles(excludeFileFilter);
				excludeFileFilterList = Arrays.asList(excludeFilesArray);
				allExcludeFilesList = new ArrayList<>(excludeFileFilterList);
			}
			if (excludeFiles != null && !excludeFiles.isEmpty())
			{
				if (allExcludeFilesList != null)
				{
					allExcludeFilesList.addAll(excludeFiles);
				}
				else
				{
					allExcludeFilesList = new ArrayList<>(excludeFiles);
				}
			}
			// if excludeFilesList is not null and not empty
			if (null != allExcludeFilesList && !allExcludeFilesList.isEmpty())
			{

				for (final File currentFile : includeFilesArray)
				{
					// if the excludeFilesList does not contain the current file do copy...
					if (!allExcludeFilesList.contains(currentFile))
					{
						final File copy = new File(destination, currentFile.getName());
						if (currentFile.isDirectory())
						{
							// copy directory recursive...
							copied = copyDirectoryWithFileFilter(currentFile, copy,
								includeFileFilter, excludeFileFilter, lastModified);
						}
						else
						{
							copied = copyFile(currentFile, copy, lastModified);
						}
					} // otherwise do not copy the current file...
				}
			}
			else
			{ // otherwise copy all files and directories
				for (final File currentFile : includeFilesArray)
				{
					final File copy = new File(destination, currentFile.getName());
					if (currentFile.isDirectory())
					{
						// copy directory recursive...
						copied = copyDirectoryWithFileFilter(currentFile, copy, includeFileFilter,
							excludeFileFilter, lastModified);
					}
					else
					{
						copied = copyFile(currentFile, copy, lastModified);
					}
				}
			}
		}
		else
		{
			throw new FileIsSecurityRestrictedException(
				"File '" + source.getAbsolutePath() + "' is security restricted.");
		}
		return copied;
	}

	/**
	 * Copies all files that match to the FilenameFilter from the given source directory to the
	 * given destination directory with the option to set the lastModified time from the given
	 * destination file or directory.
	 *
	 * @param source
	 *            The source directory.
	 * @param destination
	 *            The destination directory.
	 * @param filenameFilter
	 *            The FilenameFilter for the files to be copied. If null all files will be copied.
	 * @param lastModified
	 *            Flag the tells if the attribute lastModified has to be set with the attribute from
	 *            the destination file.
	 * @return 's true if the directory is copied, otherwise false.
	 * @throws IOException
	 *             Is thrown if an error occurs by reading or writing.
	 * @throws FileIsNotADirectoryException
	 *             Is thrown if the source file is not a directory.
	 * @throws FileIsSecurityRestrictedException
	 *             Is thrown if the source file is security restricted.
	 */
	public static boolean copyDirectoryWithFilenameFilter(final File source, final File destination,
		final FilenameFilter filenameFilter, final boolean lastModified)
		throws IOException, FileIsNotADirectoryException, FileIsSecurityRestrictedException
	{
		return copyDirectoryWithFilenameFilter(source, destination, filenameFilter, null,
			lastModified);
	}

	/**
	 * Copies all files that match to the given includeFilenameFilter and does not copy all the
	 * files that match the excludeFilenameFilter from the given source directory to the given
	 * destination directory with the option to set the lastModified time from the given destination
	 * file or directory.
	 *
	 * @param source
	 *            The source directory.
	 * @param destination
	 *            The destination directory.
	 * @param includeFilenameFilter
	 *            The FilenameFilter for the files to be copied. If null all files will be copied.
	 * @param excludeFilenameFilter
	 *            The FilenameFilter for the files to be not copied. If null no files will be
	 *            excluded by copy process.
	 * @param lastModified
	 *            Flag the tells if the attribute lastModified has to be set with the attribute from
	 *            the destination file.
	 * @return 's true if the directory is copied, otherwise false.
	 * @throws IOException
	 *             Is thrown if an error occurs by reading or writing.
	 * @throws FileIsNotADirectoryException
	 *             Is thrown if the source file is not a directory.
	 * @throws FileIsSecurityRestrictedException
	 *             Is thrown if the source file is security restricted.
	 */
	public static boolean copyDirectoryWithFilenameFilter(final File source, final File destination,
		final FilenameFilter includeFilenameFilter, final FilenameFilter excludeFilenameFilter,
		final boolean lastModified)
		throws IOException, FileIsNotADirectoryException, FileIsSecurityRestrictedException
	{
		if (!source.isDirectory())
		{
			throw new FileIsNotADirectoryException(
				"Source file '" + source.getAbsolutePath() + "' is not a directory.");
		}
		if (!destination.exists())
		{
			DirectoryFactory.newDirectory(destination);
		}
		boolean copied = false;
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
						final File copy = new File(destination, currentFile.getName());
						if (currentFile.isDirectory())
						{
							// copy directory recursive...
							copied = copyDirectoryWithFilenameFilter(currentFile, copy,
								includeFilenameFilter, excludeFilenameFilter, lastModified);
						}
						else
						{
							copied = copyFile(currentFile, copy, lastModified);
						}
					} // otherwise do not copy the current file...
				}
			}
			else
			{
				for (final File currentFile : includeFilesArray)
				{
					final File copy = new File(destination, currentFile.getName());
					if (currentFile.isDirectory())
					{
						// copy directory recursive...
						copied = copyDirectoryWithFilenameFilter(currentFile, copy,
							includeFilenameFilter, excludeFilenameFilter, lastModified);
					}
					else
					{
						copied = copyFile(currentFile, copy, lastModified);
					}
				}
			}
		}
		else
		{
			throw new FileIsSecurityRestrictedException(
				"File '" + source.getAbsolutePath() + "' is security restricted.");
		}
		return copied;
	}

	/**
	 * Copies the given source file to the given destination file.
	 *
	 * @param source
	 *            The source file.
	 * @param destination
	 *            The destination file.
	 *
	 * @return 's true if the file is copied, otherwise false.
	 *
	 * @throws IOException
	 *             Is thrown if an error occurs by reading or writing.
	 */
	public static boolean copyFile(final File source, final File destination) throws IOException
	{
		return copyFile(source, destination, true);
	}

	/**
	 * Copies the given source file to the given destination file with the option to set the
	 * lastModified time from the given destination file.
	 *
	 * @param source
	 *            The source file.
	 * @param destination
	 *            The destination file.
	 * @param lastModified
	 *            Flag the tells if the attribute lastModified has to be set with the attribute from
	 *            the destination file.
	 *
	 * @return 's true if the file is copied, otherwise false.
	 *
	 * @throws IOException
	 *             Is thrown if an error occurs by reading or writing.
	 */
	public static boolean copyFile(final File source, final File destination,
		final boolean lastModified) throws IOException
	{
		return copyFile(source, destination, null, null, lastModified);
	}

	/**
	 * Copies the given source file to the given destination file with the given source encodings
	 * and destination encodings.
	 *
	 * @param source
	 *            the source
	 * @param destination
	 *            the destination
	 * @param sourceEncoding
	 *            the source encoding
	 * @param destinationEncoding
	 *            the destination encoding
	 * @param lastModified
	 *            if true the last modified flag is set.
	 * @return true if the given file is copied otherwise false.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static boolean copyFile(final File source, final File destination,
		final Charset sourceEncoding, final Charset destinationEncoding, final boolean lastModified)
		throws IOException
	{
		if (source.isDirectory())
		{
			throw new IllegalArgumentException("The source File " + destination.getName()
				+ " should be a File but is a Directory.");
		}
		if (destination.isDirectory())
		{
			throw new IllegalArgumentException("The destination File " + destination.getName()
				+ " should be a File but is a Directory.");
		}
		boolean copied;
		try (InputStream inputStream = StreamExtensions.getInputStream(source);
			InputStreamReader reader = sourceEncoding != null
				? new InputStreamReader(inputStream, sourceEncoding)
				: new InputStreamReader(inputStream);
			OutputStream outputStream = StreamExtensions.getOutputStream(destination,
				!destination.exists());
			BufferedOutputStream bos = new BufferedOutputStream(outputStream);
			OutputStreamWriter writer = destinationEncoding != null
				? new OutputStreamWriter(bos, destinationEncoding)
				: new OutputStreamWriter(bos))
		{
			int tmp;
			final char[] charArray = new char[FileSize.DEFAULT_BLOCK_SIZE.getSize()];
			while ((tmp = reader.read(charArray)) > 0)
			{
				writer.write(charArray, 0, tmp);
			}
			copied = true;
		}
		if (lastModified)
		{
			destination.setLastModified(source.lastModified());
		}
		return copied;
	}

	/**
	 * Copies the given source file to the given destination file with the given source encodings
	 * and destination encodings.
	 *
	 * @param sources
	 *            the files the have to be copied
	 * @param destination
	 *            the destination
	 * @param sourceEncoding
	 *            the source encoding
	 * @param destinationEncoding
	 *            the destination encoding
	 * @param lastModified
	 *            if true the last modified flag is set
	 */
	public static void copyFiles(final List<File> sources, final File destination,
		final Charset sourceEncoding, final Charset destinationEncoding, final boolean lastModified)
	{
		if (!destination.exists())
		{
			DirectoryFactory.newDirectory(destination);
		}
		sources.forEach(RuntimeExceptionDecorator.decorate(file -> {
			File destinationFile = new File(destination, file.getName());
			CopyFileExtensions.copyFile(file, destinationFile, sourceEncoding, destinationEncoding,
				lastModified);
		}));
	}

	/**
	 * Copies the given source file to the given destination directory.
	 *
	 * @param source
	 *            The source file to copy in the destination directory.
	 * @param destinationDir
	 *            The destination directory.
	 *
	 * @return 's true if the file is copied to the destination directory, otherwise false.
	 *
	 * @throws FileIsNotADirectoryException
	 *             Is thrown if the source file is not a directory.
	 * @throws IOException
	 *             Is thrown if an error occurs by reading or writing.
	 */
	public static boolean copyFileToDirectory(final File source, final File destinationDir)
		throws FileIsNotADirectoryException, IOException
	{
		return copyFileToDirectory(source, destinationDir, true);
	}

	/**
	 * Copies the given source file to the given destination directory with the option to set the
	 * lastModified time from the given destination directory.
	 *
	 * @param source
	 *            The source directory.
	 * @param destinationDir
	 *            The destination directory.
	 * @param lastModified
	 *            Flag the tells if the attribute lastModified has to be set with the attribute from
	 *            the destination directory.
	 *
	 * @return 's true if the file is copied to the given directory, otherwise false.
	 *
	 * @throws FileIsNotADirectoryException
	 *             Is thrown if the source file is not a directory.
	 * @throws IOException
	 *             Is thrown if an error occurs by reading or writing.
	 */
	public static boolean copyFileToDirectory(final File source, final File destinationDir,
		final boolean lastModified) throws FileIsNotADirectoryException, IOException
	{
		if (null == destinationDir)
		{
			throw new IllegalArgumentException("Destination must not be null");
		}

		if (!destinationDir.isDirectory())
		{
			throw new FileIsNotADirectoryException("Destination File-object '"
				+ destinationDir.getAbsolutePath() + "' is not a directory.");
		}
		final File destinationFile = new File(destinationDir, source.getName());

		return copyFile(source, destinationFile, lastModified);
	}

	/**
	 * Creates a backup file in the same directory with the same name of the given file and with the
	 * extension of '*.bak'.
	 *
	 * @param file
	 *            the file to backup.
	 * @param sourceEncoding
	 *            the source encoding of the file to backup.
	 * @param destinationEncoding
	 *            the destination encoding of the backup file. This can be null.
	 * @return the created backup file.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static File newBackupOf(final File file, final Charset sourceEncoding,
		final Charset destinationEncoding) throws IOException
	{
		final File backup = new File(file.getAbsolutePath() + FileExtension.BACKUP.getExtension());
		CopyFileExtensions.copyFile(file, backup, sourceEncoding, destinationEncoding, true);
		return backup;
	}

}
