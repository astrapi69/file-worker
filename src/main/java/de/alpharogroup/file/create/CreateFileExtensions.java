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
package de.alpharogroup.file.create;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.Collection;

import de.alpharogroup.file.exceptions.DirectoryAlreadyExistsException;
import lombok.experimental.UtilityClass;

/**
 * The class {@link CreateFileExtensions} helps you to create files or directories.
 *
 * @author Asterios Raptis
 * @version 1.0
 */
@UtilityClass
public final class CreateFileExtensions
{

	/**
	 * Creates the directories.
	 *
	 * @param directories
	 *            the directories
	 *
	 * @return true, if successful
	 *
	 * @throws DirectoryAlreadyExistsException
	 *             the directory allready exists exception
	 */
	public static FileCreationState newDirectories(final Collection<File> directories)
		throws DirectoryAlreadyExistsException
	{
		FileCreationState created = FileCreationState.PENDING;
		for (final File dir : directories)
		{
			created = CreateFileExtensions.newDirectory(dir);
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
	 * @see <code>Files#createDirectories(Path, FileAttribute...)</code>
	 */
	public static boolean newDirectories(Path dir, FileAttribute<?>... attrs) throws IOException
	{
		Path directory = Files.createDirectories(dir, attrs);
		return Files.exists(directory);
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
	 * @see <code>Files#createDirectory(Path, FileAttribute...)</code>
	 */
	public static boolean newDirectory(Path dir, FileAttribute<?>... attrs) throws IOException
	{
		Path directory = Files.createDirectory(dir, attrs);
		return Files.exists(directory);
	}

	/**
	 * Creates a new directory from the given {@link File} object
	 *
	 * @param dir
	 *            The directory to create
	 *
	 * @return the {@link FileCreationState} with the result
	 */
	public static FileCreationState newDirectory(final File dir)
	{
		FileCreationState fileCreationState = FileCreationState.ALREADY_EXISTS;
		// If the directory does not exists
		if (!dir.exists())
		{ // then
			fileCreationState = FileCreationState.FAILED;
			// create it...
			if (dir.mkdir())
			{
				fileCreationState = FileCreationState.CREATED;
			}
		}
		return fileCreationState;
	}

	/**
	 * Creates an empty file if the File does not exists otherwise it lets the file as it is.
	 *
	 * @param file
	 *            the file.
	 *
	 * @return true, if the file is successful created otherwise false.
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
			newDirectories(file.toPath());
			if (file.createNewFile())
			{
				fileCreationState = FileCreationState.CREATED;
			}
		}
		return fileCreationState;
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
			created = CreateFileExtensions.newFile(file);
		}
		return created;
	}

}
