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
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import io.github.astrapi69.file.create.model.FileCreationState;
import io.github.astrapi69.throwable.RuntimeExceptionDecorator;

/**
 * The class {@link DirectoryFactory} helps you to create directories
 *
 * @version 1.0
 */
public final class DirectoryFactory
{

	/**
	 * Private constructor to prevent instantiation
	 */
	private DirectoryFactory()
	{
	}

	/**
	 * Creates the directories.
	 *
	 * @param directories
	 *            the directories
	 * @return collection of {@link FileCreationState} with the result of each directory creation
	 */
	public static Collection<FileCreationState> newDirectories(final Collection<File> directories)
	{
		Collection<FileCreationState> fileCreationStates = new ArrayList<>();
		for (final File dir : directories)
		{
			FileCreationState created = FileCreationState.PENDING;
			created = DirectoryFactory.newDirectory(dir);
			fileCreationStates.add(created);
		}
		return fileCreationStates;
	}

	/**
	 * Creates a new directory from the given {@link Path} object and optional
	 * {@link FileAttribute}, with every missing parent directory
	 *
	 * @param dir
	 *            the directory to create
	 * @param attrs
	 *            optional file attributes to set atomically when creating the directory
	 * @return true if the directory was created; false if it already existed as a directory before
	 *         the call, in which case it is left as it is
	 * @throws java.nio.file.FileAlreadyExistsException
	 *             if the path exists, but is not a directory
	 * @throws IOException
	 *             Signals an I/O exception occurred, for instance a
	 *             {@link java.nio.file.FileSystemException} if one of its parents is not a
	 *             directory; a directory that cannot be created throws, it does not return false
	 * @see Files#createDirectories(Path, FileAttribute...)
	 */
	public static boolean newDirectories(Path dir, FileAttribute<?>... attrs) throws IOException
	{
		final boolean existed = Files.isDirectory(dir);
		Files.createDirectories(dir, attrs);
		return !existed;
	}

	/**
	 * Creates a new directory from the given {@link Path} object, with every missing parent
	 * directory, and throws an unchecked exception instead of the checked {@link IOException}
	 *
	 * @param dir
	 *            the directory to create
	 * @param attrs
	 *            optional file attributes to set atomically when creating the directory
	 * @return true if the directory was created; false if it already existed as a directory before
	 *         the call
	 * @throws RuntimeException
	 *             with the {@link IOException} of {@link #newDirectories(Path, FileAttribute...)}
	 *             as its cause, if the directory cannot be created
	 */
	public static boolean newDirectoriesQuietly(Path dir, FileAttribute<?>... attrs)
	{
		return RuntimeExceptionDecorator.decorate(() -> newDirectories(dir, attrs));
	}

	/**
	 * Creates a new directory from the given {@link Path} object and the optional
	 * {@link FileAttribute}.<br>
	 * <br>
	 * Note: this method decorates the {@link Files#createDirectory(Path, FileAttribute...)}: the
	 * parent directory must exist, and a path that already exists is not created again.
	 *
	 * @param dir
	 *            the dir the directory to create
	 * @param attrs
	 *            an optional list of file attributes to set atomically when creating the directory
	 * @return true, the directory was created; a directory that cannot be created throws, it does
	 *         not return false
	 * @throws java.nio.file.FileAlreadyExistsException
	 *             if the path already exists, as a directory or as a file
	 * @throws IOException
	 *             Signals that an I/O exception has occurred, for instance a missing parent
	 *             directory
	 * @see Files#createDirectory(Path, FileAttribute...)
	 */
	public static boolean newDirectory(Path dir, FileAttribute<?>... attrs) throws IOException
	{
		Path directory = Files.createDirectory(dir, attrs);
		return Files.exists(directory);
	}

	/**
	 * Creates a new directory from the given {@link Path} object and the optional
	 * {@link FileAttribute}, and throws an unchecked exception instead of the checked
	 * {@link IOException}.<br>
	 * <br>
	 * Note: this method decorates the {@link Files#createDirectory(Path, FileAttribute...)}: the
	 * parent directory must exist, and a path that already exists is not created again.
	 *
	 * @param dir
	 *            the dir the directory to create
	 * @param attrs
	 *            an optional list of file attributes to set atomically when creating the directory
	 * @return true, the directory was created; a directory that cannot be created throws, it does
	 *         not return false
	 * @throws RuntimeException
	 *             with the {@link IOException} of {@link #newDirectory(Path, FileAttribute...)} as
	 *             its cause, for instance a {@link java.nio.file.FileAlreadyExistsException} if the
	 *             path already exists
	 * @see Files#createDirectory(Path, FileAttribute...)
	 */
	public static boolean newDirectoryQuietly(Path dir, FileAttribute<?>... attrs)
	{
		return RuntimeExceptionDecorator.decorate(() -> newDirectory(dir, attrs));
	}

	/**
	 * Factory method that creates a new {@link File} object from the given absolute path as
	 * {@link String} object
	 *
	 * @param absolutePath
	 *            the absolute path
	 *
	 * @return the file object
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static File newDirectory(final String absolutePath) throws IOException
	{
		Path dir = Paths.get(absolutePath);
		File directory = dir.toFile();
		if (!directory.exists())
		{
			newDirectory(dir);
		}
		return directory;
	}

	/**
	 * Factory method that creates a new {@link File} object from the given absolute path as
	 * {@link String} object, and creates the directory if the path does not exist; it throws an
	 * unchecked exception instead of the checked {@link IOException}
	 *
	 * @param absolutePath
	 *            the absolute path
	 *
	 * @return the file object
	 * @throws RuntimeException
	 *             with the {@link IOException} of {@link #newDirectory(Path, FileAttribute...)} as
	 *             its cause, for instance a {@link java.nio.file.NoSuchFileException} if the parent
	 *             directory does not exist
	 */
	public static File newDirectoryQuietly(final String absolutePath)
	{
		Path dir = Paths.get(absolutePath);
		File directory = dir.toFile();
		if (!directory.exists())
		{
			newDirectoryQuietly(dir);
		}
		return directory;
	}

	/**
	 * Creates a new directory from the given {@link File} object
	 *
	 * @param directory
	 *            the directory to create
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
	 * Factory method for creating the new directory as {@link File} objects if it does not exist
	 *
	 * @param parentDirectory
	 *            the parent directory
	 * @param directoryName
	 *            the directory name
	 * @return the new directory as {@link File} object
	 */
	public static File newDirectory(final File parentDirectory, final String directoryName)
	{
		Objects.requireNonNull(parentDirectory);
		Objects.requireNonNull(directoryName);
		if (!parentDirectory.exists())
		{
			throw new RuntimeException("Given parent directory does not exist");
		}
		if (!parentDirectory.isDirectory())
		{
			throw new RuntimeException("Given parent file is not a directory");
		}
		File directory = new File(parentDirectory, directoryName);
		newDirectory(directory);
		return directory;
	}

	/**
	 * Creates a new directory under the given parent directory and directory name
	 *
	 * @param parentDirectory
	 *            the parent directory
	 * @param directoryName
	 *            the name of the directory to create
	 * @return the new {@link File} directory object
	 * @throws IOException
	 *             Signals an I/O exception occurred
	 * @see Files#createDirectory(Path, FileAttribute...)
	 */
	public static File newDirectory(final String parentDirectory, final String directoryName)
		throws IOException
	{
		Path dir = Paths.get(parentDirectory, directoryName);
		File directory = dir.toFile();
		if (!directory.exists())
		{
			newDirectory(dir);
		}
		return directory;
	}

	/**
	 * Factory method for creating the new directory as {@link File} objects if it is not exists,
	 * and throws an unchecked exception instead of the checked {@link IOException}
	 *
	 * @param parentDirectory
	 *            the parent directory
	 * @param directoryName
	 *            the directory name
	 * @return the new directory as {@link File} object
	 * @throws RuntimeException
	 *             with the {@link IOException} of {@link #newDirectory(Path, FileAttribute...)} as
	 *             its cause, for instance a {@link java.nio.file.NoSuchFileException} if the parent
	 *             directory does not exist
	 * @see Files#createDirectory(Path, FileAttribute...)
	 */
	public static File newDirectoryQuietly(final String parentDirectory, final String directoryName)
	{
		Path dir = Paths.get(parentDirectory, directoryName);
		File directory = dir.toFile();
		if (!directory.exists())
		{
			newDirectoryQuietly(dir);
		}
		return directory;
	}

	/**
	 * Creates a new parent directories if not existing for the given file
	 *
	 * @param file
	 *            the file for which parent directories need to be created
	 * @return true if successful, false otherwise
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

	/**
	 * Creates a new temporary directory from the given {@link String} object and the optional file
	 * attributes
	 *
	 * @param prefix
	 *            the prefix string to be used in generating the directory's name; may be
	 *            {@code null}
	 * @param attrs
	 *            an optional list of file attributes to set atomically when creating the directory
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 *
	 * @return the new temporary directory
	 */
	public static File newTempDirectory(String prefix, FileAttribute<?>... attrs) throws IOException
	{
		return Files.createTempDirectory(prefix, attrs).toFile();
	}

	/**
	 * Creates a new temporary directory from the given {@link Path} object, the given
	 * {@link String} object and the optional file attributes
	 *
	 *
	 * @param dir
	 *            the path to directory in which to create the directory
	 * @param prefix
	 *            the prefix string to be used in generating the directory's name; may be
	 *            {@code null}
	 * @param attrs
	 *            an optional list of file attributes to set atomically when creating the directory
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 *
	 * @return the new temporary directory
	 */
	public static File newTempDirectory(Path dir, String prefix, FileAttribute<?>... attrs)
		throws IOException
	{
		return Files.createTempDirectory(dir, prefix, attrs).toFile();
	}

}
