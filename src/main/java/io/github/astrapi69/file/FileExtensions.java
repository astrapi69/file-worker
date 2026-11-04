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
import java.nio.channels.OverlappingFileLockException;
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

	/**
	 * Private constructor to prevent instantiation
	 */
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
	 * Checks whether a lock on the given file is held, by this JVM through another channel or,
	 * where the platform enforces it, by another process. The check opens the existing file for
	 * writing, tries an exclusive lock on it and releases that lock again at once; it neither
	 * creates the file nor changes its content. A file that is open without a lock, for example for
	 * reading, is not open in the sense of this check.
	 * <p>
	 * A missing file gives {@code false} and is left missing, also when its directory is missing.
	 * The check looks for the file before it opens it: a file that another thread or process
	 * deletes in between is created again, empty, by the opening.
	 * <p>
	 * Note: on platforms where closing a channel releases all the locks the JVM holds on the file
	 * (see {@link FileLock}), checking a file that this JVM has locked releases that lock as far as
	 * other processes are concerned, although it stays valid in this JVM.
	 *
	 * @param file
	 *            The file to check.
	 * @return true if a lock on the file is held, false if the file is missing or no lock on it is
	 *         held
	 * @throws IOException
	 *             if the file exists but cannot be opened for writing, for example a
	 *             {@link java.io.FileNotFoundException} for a read-only file or a directory, or if
	 *             trying the lock fails
	 */
	public static boolean isOpen(final File file) throws IOException
	{
		if (!file.exists())
		{
			return false;
		}
		try (RandomAccessFile fileAccess = new RandomAccessFile(file.getAbsolutePath(), "rw");
			FileLock lock = fileAccess.getChannel().tryLock())
		{
			return lock == null;
		}
		catch (final OverlappingFileLockException exception)
		{
			return true;
		}
	}

}
