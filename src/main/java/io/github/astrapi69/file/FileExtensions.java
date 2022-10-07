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
package io.github.astrapi69.file;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URI;
import java.nio.channels.FileLock;
import java.nio.file.Files;

import io.github.astrapi69.file.read.ReadFileExtensions;

/**
 * Utility class for the use with File object
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public final class FileExtensions
{

	private FileExtensions()
	{
	}

	/**
	 * Downloads Data from the given URI.
	 *
	 * @param uri
	 *            The URI from where to download.
	 * @return Returns a byte array or null.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static byte[] download(final URI uri) throws IOException
	{
		final File tmpFile = new File(uri);
		return ReadFileExtensions.toByteArray(tmpFile);
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
		final String absolutePath = file.getAbsolutePath();
		int lastSlash_index = absolutePath.lastIndexOf("/");
		if (lastSlash_index < 0)
		{
			lastSlash_index = absolutePath.lastIndexOf("\\");
		}
		return absolutePath.substring(0, lastSlash_index + 1);
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
	 * Gets the content type from the given file object
	 *
	 * @param file
	 *            the file
	 * @return 's the content type from the given file object
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	public static String getContentType(final File file) throws IOException
	{
		return Files.probeContentType(file.toPath());
	}

	/**
	 * Not yet implemented. Checks if the given file is open.
	 *
	 * @param file
	 *            The file to check.
	 * @return Return true if the file is open otherwise false.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static boolean isOpen(final File file) throws IOException
	{
		boolean open = false;
		try (RandomAccessFile fileAccess = new RandomAccessFile(file.getAbsolutePath(), "rw"))
		{
			FileLock lock = fileAccess.getChannel().tryLock();
			if (lock == null)
			{
				open = true;
			}
			else
			{
				lock.release();
			}
		}
		return open;
	}

}
