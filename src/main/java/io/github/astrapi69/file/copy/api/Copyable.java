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
package io.github.astrapi69.file.copy.api;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.Collection;

import io.github.astrapi69.file.copy.CopyDirectoryExtensions;
import io.github.astrapi69.file.copy.CopyFileExtensions;
import io.github.astrapi69.file.exception.DirectoryAlreadyExistsException;
import io.github.astrapi69.file.exception.FileIsADirectoryException;
import io.github.astrapi69.file.exception.FileIsNotADirectoryException;
import io.github.astrapi69.file.exception.FileIsSecurityRestrictedException;

/**
 * Interface for copying files or directories
 */
public interface Copyable
{

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
	default boolean copyDirectory(Path source, Path destination) throws IOException
	{
		return CopyDirectoryExtensions.copyDirectory(source, destination);
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
	 *             Is thrown if the directory already exists.
	 */
	default boolean copyDirectory(final File source, final File destination)
		throws FileIsSecurityRestrictedException, IOException, FileIsADirectoryException,
		FileIsNotADirectoryException, DirectoryAlreadyExistsException
	{
		return CopyDirectoryExtensions.copyDirectory(source, destination);
	}

	/**
	 * Copies the given source file to the destination directory
	 *
	 * @param source
	 *            The source file
	 * @param destination
	 *            The destination directory
	 * @return true if the file was successfully copied, otherwise false
	 * @throws IOException
	 *             if an I/O error occurs during the copy
	 */
	default boolean copyFile(File source, File destination) throws IOException
	{
		return CopyFileExtensions.copyFile(source, destination);
	}

	/**
	 * Copies the given source file to the given destination file with the given source encodings
	 * and destination encodings.
	 *
	 * @param sources
	 *            the files the have to be copied
	 * @param destination
	 *            the destination
	 * @param sourceEncoding
	 *            the source encoding
	 * @param destinationEncoding
	 *            the destination encoding
	 * @param lastModified
	 *            if true, the last modified flag is set
	 */
	default void copyFiles(Collection<File> sources, File destination, final Charset sourceEncoding,
		final Charset destinationEncoding, final boolean lastModified)
	{
		CopyFileExtensions.copyFiles(sources, destination, sourceEncoding, destinationEncoding,
			lastModified);
	}

	/**
	 * Creates a backup file in the same directory with the same name of the given file and with the
	 * extension of '*.bak'.
	 *
	 * @param file
	 *            the file to backup.
	 * @param sourceEncoding
	 *            the source encoding of the file to backup.
	 * @param destinationEncoding
	 *            the destination encoding of the backup file. This can be null.
	 * @return the created backup file.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	default File newBackupOf(File file, final Charset sourceEncoding,
		final Charset destinationEncoding) throws IOException
	{
		return CopyFileExtensions.newBackupOf(file, sourceEncoding, destinationEncoding);
	}

	/**
	 * Copies the given source file to the destination directory path.
	 *
	 * @param source
	 *            The source path
	 * @param destination
	 *            The destination directory path
	 * @return true if the file was successfully copied, otherwise false
	 * @throws IOException
	 *             if an I/O error occurs during the copy
	 */
	default boolean copyFile(Path source, Path destination) throws IOException
	{
		return CopyFileExtensions.copyFile(source, destination);
	}

	/**
	 * Copies multiple files to the destination directory.
	 *
	 * @param sources
	 *            List of source paths to copy
	 * @param destination
	 *            The destination path
	 * @throws IOException
	 *             if an I/O error occurs during the copy
	 */
	default void copyFiles(Collection<Path> sources, Path destination) throws IOException
	{
		CopyFileExtensions.copyFiles(sources, destination);
	}

	/**
	 * Creates a backup file with ".bak" extension in the same directory as the original path.
	 *
	 * @param path
	 *            The path to back up
	 * @return the path to the backup file
	 * @throws IOException
	 *             if an I/O error occurs during the copy
	 */
	default Path newBackupOf(Path path) throws IOException
	{
		return CopyFileExtensions.newBackupOf(path);
	}
}
