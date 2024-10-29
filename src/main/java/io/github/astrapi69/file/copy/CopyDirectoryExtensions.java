package io.github.astrapi69.file.copy;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import io.github.astrapi69.file.create.DirectoryFactory;
import io.github.astrapi69.file.exception.DirectoryAlreadyExistsException;
import io.github.astrapi69.file.exception.FileIsADirectoryException;
import io.github.astrapi69.file.exception.FileIsNotADirectoryException;
import io.github.astrapi69.file.exception.FileIsSecurityRestrictedException;

/**
 * The class {@link CopyDirectoryExtensions} helps you to copy directories
 */
public final class CopyDirectoryExtensions
{

	/**
	 * Private constructor to prevent instantiation
	 */
	private CopyDirectoryExtensions()
	{
	}

	/**
	 * Copies the given source directory to the destination directory
	 *
	 * @param source
	 *            The source directory
	 * @param destination
	 *            The destination directory
	 * @return true if the directory was successfully copied, otherwise false
	 * @throws IOException
	 *             if an I/O error occurs during the copy
	 */
	public static boolean copyDirectory(Path source, Path destination) throws IOException
	{
		Objects.requireNonNull(source, "source must not be null");
		Objects.requireNonNull(destination, "destination must not be null");
		if (!Files.isDirectory(source))
		{
			throw new IllegalArgumentException("Source must be a directory: " + source);
		}
		Files.walkFileTree(source, new SimpleFileVisitor<>()
		{
			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
				throws IOException
			{
				Path targetDir = destination.resolve(source.relativize(dir));
				Files.createDirectories(targetDir);
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
				throws IOException
			{
				Files.copy(file, destination.resolve(source.relativize(file)),
					StandardCopyOption.REPLACE_EXISTING);
				return FileVisitResult.CONTINUE;
			}
		});
		return true;
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
							copied = CopyFileExtensions.copyFile(currentFile, copy, lastModified);
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
						copied = CopyFileExtensions.copyFile(currentFile, copy, lastModified);
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
							copied = CopyFileExtensions.copyFile(currentFile, copy, lastModified);
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
						copied = CopyFileExtensions.copyFile(currentFile, copy, lastModified);
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

}
