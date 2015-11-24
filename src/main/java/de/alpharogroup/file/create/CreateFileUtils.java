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
package de.alpharogroup.file.create;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.log4j.Logger;

import de.alpharogroup.file.exceptions.DirectoryAllreadyExistsException;

/**
 * The Class CreateFileUtils helps you to create files or directories.
 *
 * @author Asterios Raptis
 * @version 1.0
 */
public final class CreateFileUtils
{

	/** The Constant logger. */
	private static final Logger logger = Logger.getLogger(CreateFileUtils.class.getName());

	/**
	 * Creates the directories.
	 *
	 * @param directories
	 *            the directories
	 *
	 * @return true, if successful
	 *
	 * @throws DirectoryAllreadyExistsException
	 *             the directory allready exists exception
	 */
	public static boolean newDirectories(final Collection<File> directories)
		throws DirectoryAllreadyExistsException
	{
		boolean created = false;
		for (final File dir : directories)
		{
			created = CreateFileUtils.newDirectory(dir);
		}
		return created;

	}

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
			created = CreateFileUtils.newDirectoryQuietly(dir);
		}
		return created;
	}

	/**
	 * Creates a new directory.
	 *
	 * @param dir
	 *            The directory to create.
	 *
	 * @return Returns true if the directory was created otherwise false.
	 *
	 * @throws DirectoryAllreadyExistsException
	 *             Thrown if the directory all ready exists.
	 */
	public static boolean newDirectory(final File dir) throws DirectoryAllreadyExistsException
	{
		boolean created = false;
		// If the directory does not exists
		if (!dir.exists())
		{ // then
			// create it...
			created = dir.mkdir();
		}
		else
		{ // otherwise
			// throw a DirectoryAllreadyExistsException.
			throw new DirectoryAllreadyExistsException("Directory allready exists.");
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
			return newDirectory(dir);
		}
		catch (final DirectoryAllreadyExistsException e)
		{
			logger.error("Directory '" + dir.getAbsolutePath() + "' allready exists.", e);
		}
		return false;
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
	public static boolean newFile(final File file) throws IOException
	{
		boolean created = false;
		if (!file.exists())
		{
			newParentDirectories(file);
			created = file.createNewFile();
		}
		else
		{
			created = true;
		}
		return created;
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
			return newFile(file);
		}
		catch (final IOException e)
		{
			logger.error("File '" + file.getAbsolutePath()
				+ "' could not created. For more information see the exception:", e);
		}
		return false;
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
	public static boolean newFiles(final Collection<File> files) throws IOException
	{
		boolean created = false;
		for (final File file : files)
		{
			created = CreateFileUtils.newFile(file);
		}
		return created;
	}

	/**
	 * Creates the parent directories from the given file.
	 *
	 * @param file
	 *            the file
	 */
	public static void newParentDirectories(final File file)
	{
		if (!file.exists())
		{
			final File parent = file.getParentFile();
			if (parent != null && !parent.exists())
			{
				parent.mkdirs();
			}
		}
	}

	/**
	 * Private constructor.
	 */
	private CreateFileUtils()
	{
		super();
	}

}
