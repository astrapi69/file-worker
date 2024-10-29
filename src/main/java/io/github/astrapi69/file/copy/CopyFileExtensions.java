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
package io.github.astrapi69.file.copy;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;

import io.github.astrapi69.file.create.DirectoryFactory;
import io.github.astrapi69.file.exception.FileIsNotADirectoryException;
import io.github.astrapi69.io.StreamExtensions;
import io.github.astrapi69.io.file.FileExtension;
import io.github.astrapi69.io.file.FileSize;
import io.github.astrapi69.throwable.RuntimeExceptionDecorator;

/**
 * The class {@link CopyFileExtensions} helps you to copy files.
 */
public final class CopyFileExtensions
{

	/**
	 * Private constructor to prevent instantiation
	 */
	private CopyFileExtensions()
	{
	}

	/**
	 * Copies the given source file to the destination file
	 *
	 * @param source
	 *            The source file
	 * @param destination
	 *            The destination file
	 * @return true if the file was successfully copied, otherwise false
	 * @throws IOException
	 *             if an I/O error occurs during the copy
	 */
	public static boolean copyFile(Path source, Path destination) throws IOException
	{
		Objects.requireNonNull(source, "source must not be null");
		Objects.requireNonNull(destination, "destination must not be null");
		if (Files.isDirectory(source))
		{
			throw new IllegalArgumentException("Source must be a file: " + source);
		}
		Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
		return true;
	}

	/**
	 * Copies multiple files to the destination directory
	 *
	 * @param sources
	 *            List of source files to copy
	 * @param destination
	 *            The destination directory
	 * @throws IOException
	 *             if an I/O error occurs during the copy
	 */
	public static void copyFiles(List<Path> sources, Path destination) throws IOException
	{
		Objects.requireNonNull(sources, "sources must not be null");
		Objects.requireNonNull(destination, "destination must not be null");
		if (!Files.isDirectory(destination))
		{
			Files.createDirectories(destination);
		}
		for (Path source : sources)
		{
			copyFile(source, destination.resolve(source.getFileName()));
		}
	}

	/**
	 * Creates a backup file with ".bak" extension in the same directory as the original file
	 *
	 * @param file
	 *            The file to back up
	 * @return the path to the backup file
	 * @throws IOException
	 *             if an I/O error occurs during the copy
	 */
	public static Path newBackupOf(Path file) throws IOException
	{
		Objects.requireNonNull(file, "file must not be null");
		Path backup = Paths.get(file.toString() + ".bak");
		copyFile(file, backup);
		return backup;
	}

	/**
	 * Copies the given source file to the given destination file.
	 *
	 * @param source
	 *            The source file.
	 * @param destination
	 *            The destination file.
	 *
	 * @return 's true if the file is copied, otherwise false.
	 *
	 * @throws IOException
	 *             Is thrown if an error occurs by reading or writing.
	 */
	public static boolean copyFile(final File source, final File destination) throws IOException
	{
		return copyFile(source, destination, true);
	}

	/**
	 * Copies the given source file to the given destination file with the option to set the
	 * lastModified time from the given destination file.
	 *
	 * @param source
	 *            The source file.
	 * @param destination
	 *            The destination file.
	 * @param lastModified
	 *            Flag the tells if the attribute lastModified has to be set with the attribute from
	 *            the destination file.
	 *
	 * @return 's true if the file is copied, otherwise false.
	 *
	 * @throws IOException
	 *             Is thrown if an error occurs by reading or writing.
	 */
	public static boolean copyFile(final File source, final File destination,
		final boolean lastModified) throws IOException
	{
		return copyFile(source, destination, null, null, lastModified);
	}

	/**
	 * Copies the given source file to the given destination file with the given source encodings
	 * and destination encodings.
	 *
	 * @param source
	 *            the source
	 * @param destination
	 *            the destination
	 * @param sourceEncoding
	 *            the source encoding
	 * @param destinationEncoding
	 *            the destination encoding
	 * @param lastModified
	 *            if true the last modified flag is set.
	 * @return true if the given file is copied otherwise false.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static boolean copyFile(final File source, final File destination,
		final Charset sourceEncoding, final Charset destinationEncoding, final boolean lastModified)
		throws IOException
	{
		if (source.isDirectory())
		{
			throw new IllegalArgumentException("The source File " + destination.getName()
				+ " should be a File but is a Directory.");
		}
		if (destination.isDirectory())
		{
			throw new IllegalArgumentException("The destination File " + destination.getName()
				+ " should be a File but is a Directory.");
		}
		boolean copied;
		try (InputStream inputStream = StreamExtensions.getInputStream(source);
			InputStreamReader reader = sourceEncoding != null
				? new InputStreamReader(inputStream, sourceEncoding)
				: new InputStreamReader(inputStream);
			OutputStream outputStream = StreamExtensions.getOutputStream(destination,
				!destination.exists());
			BufferedOutputStream bos = new BufferedOutputStream(outputStream);
			OutputStreamWriter writer = destinationEncoding != null
				? new OutputStreamWriter(bos, destinationEncoding)
				: new OutputStreamWriter(bos))
		{
			int tmp;
			final char[] charArray = new char[FileSize.DEFAULT_BLOCK_SIZE.getSize()];
			while ((tmp = reader.read(charArray)) > 0)
			{
				writer.write(charArray, 0, tmp);
			}
			copied = true;
		}
		if (lastModified)
		{
			destination.setLastModified(source.lastModified());
		}
		return copied;
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
	 *            if true the last modified flag is set
	 */
	public static void copyFiles(final List<File> sources, final File destination,
		final Charset sourceEncoding, final Charset destinationEncoding, final boolean lastModified)
	{
		if (!destination.exists())
		{
			DirectoryFactory.newDirectory(destination);
		}
		sources.forEach(RuntimeExceptionDecorator.decorate(file -> {
			File destinationFile = new File(destination, file.getName());
			CopyFileExtensions.copyFile(file, destinationFile, sourceEncoding, destinationEncoding,
				lastModified);
		}));
	}

	/**
	 * Copies the given source file to the given destination directory.
	 *
	 * @param source
	 *            The source file to copy in the destination directory.
	 * @param destinationDir
	 *            The destination directory.
	 *
	 * @return 's true if the file is copied to the destination directory, otherwise false.
	 *
	 * @throws FileIsNotADirectoryException
	 *             Is thrown if the source file is not a directory.
	 * @throws IOException
	 *             Is thrown if an error occurs by reading or writing.
	 */
	public static boolean copyFileToDirectory(final File source, final File destinationDir)
		throws FileIsNotADirectoryException, IOException
	{
		return copyFileToDirectory(source, destinationDir, true);
	}

	/**
	 * Copies the given source file to the given destination directory with the option to set the
	 * lastModified time from the given destination directory.
	 *
	 * @param source
	 *            The source directory.
	 * @param destinationDir
	 *            The destination directory.
	 * @param lastModified
	 *            Flag the tells if the attribute lastModified has to be set with the attribute from
	 *            the destination directory.
	 *
	 * @return 's true if the file is copied to the given directory, otherwise false.
	 *
	 * @throws FileIsNotADirectoryException
	 *             Is thrown if the source file is not a directory.
	 * @throws IOException
	 *             Is thrown if an error occurs by reading or writing.
	 */
	public static boolean copyFileToDirectory(final File source, final File destinationDir,
		final boolean lastModified) throws FileIsNotADirectoryException, IOException
	{
		if (null == destinationDir)
		{
			throw new IllegalArgumentException("Destination must not be null");
		}

		if (!destinationDir.isDirectory())
		{
			throw new FileIsNotADirectoryException("Destination File-object '"
				+ destinationDir.getAbsolutePath() + "' is not a directory.");
		}
		final File destinationFile = new File(destinationDir, source.getName());

		return copyFile(source, destinationFile, lastModified);
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
	public static File newBackupOf(final File file, final Charset sourceEncoding,
		final Charset destinationEncoding) throws IOException
	{
		final File backup = new File(file.getAbsolutePath() + FileExtension.BACKUP.getExtension());
		CopyFileExtensions.copyFile(file, backup, sourceEncoding, destinationEncoding, true);
		return backup;
	}

}
