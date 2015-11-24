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
package de.alpharogroup.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URI;
import java.nio.channels.FileLock;

import org.apache.log4j.Logger;

import de.alpharogroup.file.read.ReadFileUtils;
import de.alpharogroup.file.rename.RenameFileUtils;

/**
 * Utility class for the use of File object. Most methods are set to deprecated and has gone to the
 * appropriate class.
 * 
 * @version 1.0
 * @author Asterios Raptis
 */
public final class FileUtils
{
	/** The LOGGER. */
	private static final Logger LOGGER = Logger.getLogger(FileUtils.class.getName());

	/**
	 * Downloads Data from the given URI.
	 * 
	 * @param uri
	 *            The URI from where to download.
	 * @return Returns a byte array or null.
	 */
	public static byte[] download(final URI uri)
	{
		final File tmpFile = new File(uri);
		return ReadFileUtils.toByteArray(tmpFile);
	}

	/**
	 * Gets the absolut path without the filename.
	 * 
	 * @param file
	 *            the file.
	 * @return 's the absolut path without filename.
	 */
	public static String getAbsolutPathWithoutFilename(final File file)
	{
		return RenameFileUtils.getAbsolutPathWithoutFilename(file);
	}

	/**
	 * Gets the current absolut path without the dot and slash.
	 * 
	 * @return 's the current absolut path without the dot and slash.
	 */
	public static String getCurrentAbsolutPathWithoutDotAndSlash()
	{
		final File currentAbsolutPath = new File(".");
		return currentAbsolutPath.getAbsolutePath().substring(0,
			currentAbsolutPath.getAbsolutePath().length() - 2);
	}

	/**
	 * Gets the filename with the absolute path prefix.
	 * 
	 * @param file
	 *            the file.
	 * @return the filename prefix.
	 */
	public static String getFilenamePrefix(final File file)
	{
		return FilenameUtils.getFilenamePrefix(file);
	}

	/**
	 * Gets the filename suffix or null if no suffix exists or the given file object is a directory.
	 * 
	 * @param file
	 *            the file.
	 * @return 's the filename suffix or null if no suffix exists or the given file object is a
	 *         directory.
	 */
	public static String getFilenameSuffix(final File file)
	{
		return FilenameUtils.getFilenameSuffix(file);
	}

	/**
	 * Gets the filename without the extension or null if the given file object is a directory.
	 * 
	 * @param file
	 *            the file.
	 * @return the filename without the extension or null if the given file object is a directory.
	 */
	public static String getFilenameWithoutExtension(final File file)
	{
		return FilenameUtils.getFilenameWithoutExtension(file);
	}

	/**
	 * Gets the temporary directory from the system as File object.
	 *
	 * @return the temporary directory from the system.
	 */
	public static File getTempDir()
	{
		return new File(System.getProperty("java.io.tmpdir"));
	}

	/**
	 * Not yet implemented. Checks if the given file is open.
	 * 
	 * @param file
	 *            The file to check.
	 * @return Return true if the file is open otherwise false.
	 */
	public static boolean isOpen(final File file)
	{
		boolean open = false;
		RandomAccessFile fileAccess = null;
		FileLock lock = null;
		try
		{
			fileAccess = new RandomAccessFile(file.getAbsolutePath(), "rw");
			lock = fileAccess.getChannel().tryLock();
			if (lock == null)
			{
				open = true;
			}
			else
			{
				lock.release();
			}
		}
		catch (final FileNotFoundException fnfe)
		{
			open = true;
		}
		catch (final IOException ioe)
		{
			open = true;
		}
		finally
		{
			if (fileAccess != null)
			{
				try
				{
					fileAccess.close();
					fileAccess = null;
				}
				catch (final IOException ioex)
				{
					LOGGER.error(ioex);
				}
			}
		}
		return open;
	}

	/**
	 * Private constructor.
	 */
	private FileUtils()
	{
		super();
	}

}
