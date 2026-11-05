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
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.Objects;

import io.github.astrapi69.file.create.DirectoryFactory;
import io.github.astrapi69.file.exception.FileIsNotADirectoryException;
import io.github.astrapi69.file.read.ReadFileExtensions;
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
	 *            The destination file, replaced if it exists
	 * @throws IllegalArgumentException
	 *             if the source is a directory
	 * @throws IOException
	 *             if an I/O error occurs during the copy, for instance a missing source file
	 * @see Files#copy(Path, Path, java.nio.file.CopyOption...)
	 */
	public static void copyFile(Path source, Path destination) throws IOException
	{
		Objects.requireNonNull(source, "source must not be null");
		Objects.requireNonNull(destination, "destination must not be null");
		if (Files.isDirectory(source))
		{
			throw new IllegalArgumentException("Source must be a file: " + source);
		}
		Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
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
	public static void copyFiles(Collection<Path> sources, Path destination) throws IOException
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
	 * Copies the given source file to the given destination file byte for byte.
	 *
	 * @param source
	 *            The source file.
	 * @param destination
	 *            The destination file.
	 *
	 * @throws IllegalArgumentException
	 *             if the source or the destination is a directory, or both are the same file
	 * @throws IOException
	 *             Is thrown if an error occurs by reading or writing.
	 */
	public static void copyFile(final File source, final File destination) throws IOException
	{
		copyFile(source, destination, true);
	}

	/**
	 * Copies the given source file to the given destination file byte for byte with the option to
	 * set the lastModified time from the given destination file.
	 *
	 * @param source
	 *            The source file.
	 * @param destination
	 *            The destination file.
	 * @param lastModified
	 *            Flag the tells if the attribute lastModified has to be set with the attribute from
	 *            the destination file.
	 *
	 * @throws IllegalArgumentException
	 *             if the source or the destination is a directory, or both are the same file
	 * @throws IOException
	 *             Is thrown if an error occurs by reading or writing.
	 */
	public static void copyFile(final File source, final File destination,
		final boolean lastModified) throws IOException
	{
		copyFile(source, destination, null, null, lastModified);
	}

	/**
	 * Copies the given source file to the given destination file with the given source encodings
	 * and destination encodings. If both encodings are null, the bytes are copied unchanged;
	 * otherwise the content is read as text in the source encoding and written in the destination
	 * encoding, a null encoding standing for the default charset.
	 *
	 * @param source
	 *            the source
	 * @param destination
	 *            the destination
	 * @param sourceEncoding
	 *            the source encoding, or null
	 * @param destinationEncoding
	 *            the destination encoding, or null
	 * @param lastModified
	 *            if true the last modified time of the source is set on the destination; whether
	 *            that worked is not checked
	 * @throws java.nio.charset.MalformedInputException
	 *             if a source encoding is given and the source is not valid in it, or not valid in
	 *             the default charset if only the destination encoding is given; the destination
	 *             may then hold what was copied before
	 * @throws IllegalArgumentException
	 *             if the source or the destination is a directory, or both are the same file
	 * @throws IOException
	 *             Signals that an I/O exception has occurred, for instance a
	 *             {@link java.io.FileNotFoundException} for a missing source file
	 */
	public static void copyFile(final File source, final File destination,
		final Charset sourceEncoding, final Charset destinationEncoding, final boolean lastModified)
		throws IOException
	{
		if (source.isDirectory())
		{
			throw new IllegalArgumentException(
				"The source File " + source.getName() + " should be a File but is a Directory.");
		}
		if (destination.isDirectory())
		{
			throw new IllegalArgumentException("The destination File " + destination.getName()
				+ " should be a File but is a Directory.");
		}
		if (isSameFile(source, destination))
		{
			throw new IllegalArgumentException("The source File " + source.getName()
				+ " and the destination are the same file, which a copy would empty.");
		}
		if (sourceEncoding == null && destinationEncoding == null)
		{
			copyBytes(source, destination);
		}
		else
		{
			copyText(source, destination, sourceEncoding, destinationEncoding);
		}
		if (lastModified)
		{
			destination.setLastModified(source.lastModified());
		}
	}

	/**
	 * Tells whether the two files are one: the same file where both exist, else the same absolute
	 * normalized path (#73)
	 */
	private static boolean isSameFile(final File source, final File destination) throws IOException
	{
		if (source.exists() && destination.exists())
		{
			return Files.isSameFile(source.toPath(), destination.toPath());
		}
		return source.toPath().toAbsolutePath().normalize()
			.equals(destination.toPath().toAbsolutePath().normalize());
	}

	private static void copyBytes(final File source, final File destination) throws IOException
	{
		try (InputStream inputStream = StreamExtensions.getInputStream(source);
			OutputStream outputStream = StreamExtensions.getOutputStream(destination,
				!destination.exists()))
		{
			inputStream.transferTo(outputStream);
		}
	}

	private static void copyText(final File source, final File destination,
		final Charset sourceEncoding, final Charset destinationEncoding) throws IOException
	{
		try (InputStream inputStream = StreamExtensions.getInputStream(source);
			Reader reader = ReadFileExtensions.newStrictReader(inputStream,
				sourceEncoding != null ? sourceEncoding : Charset.defaultCharset());
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
		}
	}

	/**
	 * Copies the given source file to the given destination file with the given source encodings
	 * and destination encodings. If both encodings are null, the bytes are copied unchanged.
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
	public static void copyFiles(final Collection<File> sources, final File destination,
		final Charset sourceEncoding, final Charset destinationEncoding, final boolean lastModified)
	{
		if (!destination.exists())
		{
			DirectoryFactory.newDirectoriesQuietly(destination.toPath());
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
	 * @throws FileIsNotADirectoryException
	 *             Is thrown if the destination directory is not a directory.
	 * @throws IOException
	 *             Is thrown if an error occurs by reading or writing.
	 */
	public static void copyFileToDirectory(final File source, final File destinationDir)
		throws FileIsNotADirectoryException, IOException
	{
		copyFileToDirectory(source, destinationDir, true);
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
	 * @throws IllegalArgumentException
	 *             if the destination directory is null, or the source or the entry of its name in
	 *             the destination directory is a directory
	 * @throws FileIsNotADirectoryException
	 *             Is thrown if the destination directory is not a directory.
	 * @throws IOException
	 *             Is thrown if an error occurs by reading or writing.
	 */
	public static void copyFileToDirectory(final File source, final File destinationDir,
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

		copyFile(source, destinationFile, lastModified);
	}

	/**
	 * Creates a backup file in the same directory with the same name of the given file and with the
	 * extension of '*.bak'. If both encodings are null, the bytes are copied unchanged.
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
