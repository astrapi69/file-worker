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
import java.util.Collection;

import de.alpharogroup.file.exceptions.DirectoryAllreadyExistsException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * The class {@link CreateFileQuietlyExtensions} helps you to create files or directories in a
 * quietly manner as the name let presume.
 *
 * @author Asterios Raptis
 * @version 1.0
 */
@UtilityClass
@Slf4j
public final class CreateFileQuietlyExtensions
{

	/**
	 * Creates the directories.
	 *
	 * @param directories
	 *            the directories
	 *
	 * @return true, if successful
	 */
	public static boolean newDirectoriesQuietly(final Collection<File> directories)
	{
		boolean created = false;
		for (final File dir : directories)
		{
			created = CreateFileQuietlyExtensions.newDirectoryQuietly(dir);
		}
		return created;
	}

	/**
	 * Creates a new directory quietly.
	 *
	 * @param dir
	 *            The directory to create.
	 *
	 * @return Returns true if the directory was created otherwise false.
	 */
	public static boolean newDirectoryQuietly(final File dir)
	{
		try
		{
			return CreateFileExtensions.newDirectory(dir);
		}
		catch (final DirectoryAllreadyExistsException e)
		{
			log.error("Directory '" + dir.getAbsolutePath() + "' allready exists.", e);
		}
		return false;
	}

	/**
	 * Creates an empty file quietly if the File does not exists otherwise it lets the file as it
	 * is.
	 *
	 * @param file
	 *            the file.
	 *
	 * @return true, if the file is successful created otherwise false.
	 */
	public static boolean newFileQuietly(final File file)
	{
		try
		{
			return CreateFileExtensions.newFile(file);
		}
		catch (final IOException e)
		{
			log.error("File '" + file.getAbsolutePath()
				+ "' could not created. For more information see the exception:", e);
		}
		return false;
	}

}
