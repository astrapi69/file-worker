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
import java.util.Collection;

import io.github.astrapi69.file.create.model.FileCreationState;
import io.github.astrapi69.file.create.model.FileInfo;
import io.github.astrapi69.throwable.RuntimeExceptionDecorator;

/**
 * The class {@link FileFactory} helps you to create files
 *
 * @author Asterios Raptis
 * @version 1.0
 */
public final class FileFactory
{

	/**
	 * Private constructor to prevent instantiation
	 */
	private FileFactory()
	{
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
			if (DirectoryFactory.mkParentDirs(file) && file.createNewFile())
			{
				fileCreationState = FileCreationState.CREATED;
			}
		}
		return fileCreationState;
	}

	/**
	 * Factory method that creates a new empty {@link File} if it is not exists, otherwise it lets
	 * the file as it is.
	 *
	 * @param file
	 *            the file.
	 *
	 * @return the appropriate state object that describes what happen
	 */
	public static FileCreationState newFileQuietly(final File file)
	{
		return RuntimeExceptionDecorator.decorate(() -> newFile(file));
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
	 *
	 * @return the file object
	 */
	public static File newFileQuietly(final String absolutePath)
	{
		return RuntimeExceptionDecorator.decorate(() -> newFile(absolutePath));
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
	 * Factory method that creates a new {@link File} object, if the given boolean flag is true a
	 * new empty file will be created on the file system
	 *
	 * @param absolutePath
	 *            the absolute path
	 * @param createIfNotExists
	 *            if this flag is true the file will be created if it does not exists
	 *
	 * @return the file object
	 */
	public static File newFileQuietly(final String absolutePath, boolean createIfNotExists)
	{
		File file = new File(absolutePath);
		if (createIfNotExists)
		{
			newFileQuietly(file);
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
	 * Factory method for creating the new {@link File} if it is not exists.
	 *
	 * @param parentDirectory
	 *            the parent directory
	 * @param filename
	 *            the file name
	 * @return the new {@link File} object
	 */
	public static File newFileQuietly(final File parentDirectory, final String filename)
	{
		return RuntimeExceptionDecorator.decorate(() -> newFile(parentDirectory, filename));
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
	public static File newFile(final String parentDirectory, String filename) throws IOException
	{
		File file = new File(parentDirectory, filename);
		newFile(file);
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
	 */
	public static File newFileQuietly(final String parentDirectory, String filename)
	{
		return RuntimeExceptionDecorator.decorate(() -> newFile(parentDirectory, filename));
	}

	/**
	 * Factory method for creating the new {@link File} from the given {@link FileInfo} object
	 *
	 * @param fileInfo
	 *            the {@link FileInfo} object
	 * @return the new {@link File} object
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static File newFile(final FileInfo fileInfo) throws IOException
	{
		return newFile(fileInfo.getPath(), fileInfo.getName());
	}

	/**
	 * Factory method for creating the new {@link File} from the given {@link FileInfo} object
	 *
	 * @param fileInfo
	 *            the {@link FileInfo} object
	 * @return the new {@link File} object
	 */
	public static File newFileQuietly(final FileInfo fileInfo)
	{
		return RuntimeExceptionDecorator.decorate(() -> newFile(fileInfo));
	}

	/**
	 * Creates all files contained in the collection as empty files if the files does not exists
	 * otherwise it lets the files as they are.
	 *
	 * @param files
	 *            the Collection with the File objects.
	 *
	 * @return the {@link FileCreationState} object that encapsulate the creation result
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
	 * Creates all files contained in the collection as empty files if the files does not exists
	 * otherwise it lets the files as they are.
	 *
	 * @param files
	 *            the Collection with the File objects.
	 *
	 * @return the {@link FileCreationState} object that encapsulate the creation result
	 */
	public static FileCreationState newFilesQuietly(final Collection<File> files)
	{
		return RuntimeExceptionDecorator.decorate(() -> newFiles(files));
	}

}
