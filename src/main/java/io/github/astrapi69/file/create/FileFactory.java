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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.Collection;

/**
 * The class {@link FileFactory} helps you to create files or directories
 *
 * @author Asterios Raptis
 * @version 1.0
 */
public final class FileFactory
{

	private FileFactory()
	{
	}

	/**
	 * Creates the directories.
	 *
	 * @param directories
	 *            the directories
	 *
	 * @return true, if successful
	 */
	public static FileCreationState newDirectories(final Collection<File> directories)
	{
		FileCreationState created = FileCreationState.PENDING;
		for (final File dir : directories)
		{
			created = FileFactory.newDirectory(dir);
		}
		return created;

	}

	/**
	 * Creates a new directory from the given {@link Path} object and the optional
	 * {@link FileAttribute}.<br>
	 * <br>
	 * Note: this method decorates the {@link Files#createDirectories(Path, FileAttribute...)} and
	 * returns if the directory is created or not.
	 *
	 * @param dir
	 *            the dir the directory to create
	 * @param attrs
	 *            an optional list of file attributes to set atomically when creating the directory
	 * @return Returns true if the directory was created otherwise false.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @see Files#createDirectories(Path, FileAttribute...)
	 */
	public static boolean newDirectories(Path dir, FileAttribute<?>... attrs) throws IOException
	{
		Path directory = Files.createDirectories(dir, attrs);
		return Files.exists(directory);
	}

	/**
	 * Creates a new directory from the given {@link File} object
	 *
	 * @param directory
	 *            The directory to create
	 *
	 * @return the {@link FileCreationState} with the result
	 */
	public static FileCreationState newDirectory(final File directory)
	{
		FileCreationState fileCreationState = FileCreationState.ALREADY_EXISTS;
		// If the directory does not exists
		if (!directory.exists())
		{ // then
			fileCreationState = FileCreationState.FAILED;
			// create it...
			if (directory.mkdir())
			{
				fileCreationState = FileCreationState.CREATED;
			}
		}
		return fileCreationState;
	}

	/**
	 * Factory method for creating the new directory as {@link File} objects if it is not exists.
	 *
	 * @param parentDirectory
	 *            the parent directory
	 * @param directoryName
	 *            the directory name
	 * @return the new directory as {@link File} object
	 */
	public static File newDirectory(final File parentDirectory, final String directoryName)
	{
		if (!parentDirectory.isDirectory())
		{
			throw new RuntimeException("Given parent file is not a directory");
		}
		File directory = new File(parentDirectory, directoryName);
		newDirectory(directory);
		return directory;
	}

	/**
	 * Creates a new directory from the given {@link Path} object and the optional
	 * {@link FileAttribute}.<br>
	 * <br>
	 * Note: this method decorates the {@link Files#createDirectory(Path, FileAttribute...)} and
	 * returns if the directory is created or not.
	 *
	 * @param dir
	 *            the dir the directory to create
	 * @param attrs
	 *            an optional list of file attributes to set atomically when creating the directory
	 * @return Returns true if the directory was created otherwise false.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @see Files#createDirectory(Path, FileAttribute...)
	 */
	public static boolean newDirectory(Path dir, FileAttribute<?>... attrs) throws IOException
	{
		Path directory = Files.createDirectory(dir, attrs);
		return Files.exists(directory);
	}

	/**
	 * Factory method that creates a new empty {@link File} if it is not exists, otherwise it lets
	 * the file as it is.
	 *
	 * @param file
	 *            the file.
	 *
	 * @return the appropriate state object that describes what happen
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static FileCreationState newFile(final File file) throws IOException
	{
		FileCreationState fileCreationState = FileCreationState.ALREADY_EXISTS;
		if (!file.exists())
		{
			fileCreationState = FileCreationState.FAILED;
			if (mkParentDirs(file) && file.createNewFile())
			{
				fileCreationState = FileCreationState.CREATED;
			}
		}
		return fileCreationState;
	}

	/**
	 * Factory method that creates a new {@link File} object, if the given boolean flag is true a
	 * new empty file will be created on the file system
	 *
	 * @param absolutePath
	 *            the absolute path
	 *
	 * @return the file object
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static File newFile(final String absolutePath) throws IOException
	{
		return newFile(absolutePath, false);
	}

	/**
	 * Factory method that creates a new {@link File} object, if the given boolean flag is true a
	 * new empty file will be created on the file system
	 *
	 * @param absolutePath
	 *            the absolute path
	 * @param createIfNotExists
	 *            if this flag is true the file will be created if it does not exists
	 *
	 * @return the file object
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static File newFile(final String absolutePath, boolean createIfNotExists)
		throws IOException
	{
		File file = new File(absolutePath);
		if (createIfNotExists)
		{
			newFile(file);
		}
		return file;
	}

	/**
	 * Factory method for creating the new {@link File} if it is not exists.
	 *
	 * @param parentDirectory
	 *            the parent directory
	 * @param filename
	 *            the file name
	 * @return the new {@link File} object
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static File newFile(final File parentDirectory, final String filename) throws IOException
	{
		if (!parentDirectory.isDirectory())
		{
			throw new RuntimeException("Given parent file is not a directory");
		}
		File file = new File(parentDirectory, filename);
		newFile(file);
		return file;
	}

	/**
	 * Creates all files contained in the collection as empty files if the files does not exists
	 * otherwise it lets the files as they are.
	 *
	 * @param files
	 *            the Collection with the File objects.
	 *
	 * @return true, if successful
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static FileCreationState newFiles(final Collection<File> files) throws IOException
	{
		FileCreationState created = FileCreationState.PENDING;
		for (final File file : files)
		{
			created = FileFactory.newFile(file);
		}
		return created;
	}

	/**
	 * Creates the parent directories from the given file.
	 *
	 * @param file
	 *            the file
	 * @return true, if successful
	 */
	public static boolean mkParentDirs(final File file)
	{
		if (!file.exists())
		{
			final File parent = file.getParentFile();
			if (parent != null && !parent.exists())
			{
				return parent.mkdirs();
			}
		}
		return true;
	}

}
