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
package io.github.astrapi69.file.write;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Optional;

import io.github.astrapi69.file.system.SystemPropertiesExtensions;
import io.github.astrapi69.string.CharsetExtensions;

/**
 * The class {@link StoreFileExtensions} provides extension methods for files to store or update
 *
 * @author Asterios Raptis
 */
public final class StoreFileExtensions
{

	/**
	 * Private constructor to prevent instantiation
	 */
	private StoreFileExtensions()
	{
	}

	/**
	 * Stores the given byte array to the given {@link File} object
	 *
	 * @param file
	 *            The {@link File} object to write the given byte array
	 * @param data
	 *            The byte array to be store
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	public static void toFile(final File file, final byte[] data) throws IOException
	{
		Files.write(file.toPath(), data);
	}

	/**
	 * This Method writes the given {@link String} object to the given {@link File} object
	 *
	 * @param file
	 *            The {@link File} object to write the given {@link String} object
	 * @param stringToWrite
	 *            The {@link String} object to write into the given {@link File} object
	 * @return {@code true} if the given {@link String} object was written to the given {@link File}
	 *         object; a failure is reported by an {@link IOException}, never by {@code false}
	 * @throws FileNotFoundException
	 *             is thrown if an attempt to open the file denoted by a specified pathname has
	 *             failed
	 * @throws IOException
	 *             Signals that an I/O exception has occurred, also if a write to the file or its
	 *             close fails
	 */
	public static boolean toFile(final File file, final String stringToWrite)
		throws FileNotFoundException, IOException
	{
		return toFile(file, stringToWrite, (String)null);
	}

	/**
	 * Writes the given lines in the collection into the given file
	 *
	 * @param file
	 *            The {@link File} object to write
	 * @param lines
	 *            The lines to write to file
	 * @return {@code true} if the given lines were written to the given {@link File} object; a
	 *         failure is reported by an {@link IOException}, never by {@code false}
	 * @throws IOException
	 *             Signals that an I/O exception has occurred, also if a write to the file or its
	 *             close fails
	 */
	public static boolean toFile(final File file, final Collection<String> lines) throws IOException
	{
		final String sb = concatenate(lines);
		return StoreFileExtensions.toFile(file, sb);
	}

	private static String concatenate(Collection<String> lines)
	{
		final StringBuilder sb = new StringBuilder();
		final String lineSeparator = SystemPropertiesExtensions.getLineSeparator();
		for (final String line : lines)
		{
			sb.append(line);
			sb.append(lineSeparator);
		}
		return sb.toString();
	}

	/**
	 * Writes the given lines in the collection into the given file
	 *
	 * @param file
	 *            The {@link File} object to write
	 * @param lines
	 *            The lines to write to file
	 * @param encoding
	 *            The encoding from the file as {@link String} object
	 * @return {@code true} if the given lines were written to the given {@link File} object; a
	 *         failure is reported by an {@link IOException}, never by {@code false}
	 * @throws IOException
	 *             Signals that an I/O exception has occurred, also if a write to the file or its
	 *             close fails
	 */
	public static boolean toFile(final File file, final Collection<String> lines,
		final String encoding) throws IOException
	{
		final String sb = concatenate(lines);
		return StoreFileExtensions.toFile(file, sb, encoding);
	}

	/**
	 * Writes the given lines in the collection into the given file
	 *
	 * @param file
	 *            The {@link File} object to write
	 * @param lines
	 *            The lines to write to file
	 * @param charset
	 *            The charset from the file.
	 * @return {@code true} if the given lines were written to the given {@link File} object; a
	 *         failure is reported by an {@link IOException}, never by {@code false}
	 * @throws IOException
	 *             Signals that an I/O exception has occurred, also if a write to the file or its
	 *             close fails
	 */
	public static boolean toFile(final File file, final Collection<String> lines,
		final Charset charset) throws IOException
	{
		final String sb = concatenate(lines);
		return StoreFileExtensions.toFile(file, sb, charset);
	}

	/**
	 * This Method writes the given {@link String} object to the given {@link File} object with the
	 * given encoding as {@link String} object
	 *
	 * @param file
	 *            The {@link File} object to write the given {@link String} object
	 * @param stringToWrite
	 *            The {@link String} object to write into the given {@link File} object
	 * @param encoding
	 *            The encoding from the file as {@link String} object
	 * @return {@code true} if the given {@link String} object was written to the given {@link File}
	 *         object; a failure is reported by an {@link IOException}, never by {@code false}
	 * @throws FileNotFoundException
	 *             is thrown if an attempt to open the file denoted by a specified pathname has
	 *             failed
	 * @throws IOException
	 *             Signals that an I/O exception has occurred, also if a write to the file or its
	 *             close fails
	 */
	public static boolean toFile(final File file, final String stringToWrite, final String encoding)
		throws FileNotFoundException, IOException
	{
		Optional<Charset> optionalCharset = CharsetExtensions.getCharset(encoding);

		return optionalCharset.isPresent()
			? toFile(file, stringToWrite, optionalCharset.get())
			: toFile(file, stringToWrite, (Charset)null);
	}

	/**
	 * This Method writes the given {@link String} object to the given {@link File} object with the
	 * encoding that is encapsulated in the given {@link Charset} object
	 *
	 * @param file
	 *            The {@link File} object to write the given {@link String} object
	 * @param stringToWrite
	 *            The {@link String} object to write into the given {@link File} object
	 * @param charset
	 *            The charset from the file.
	 * @return {@code true} if the given {@link String} object was written to the given {@link File}
	 *         object; a failure is reported by an {@link IOException}, never by {@code false}
	 * @throws FileNotFoundException
	 *             is thrown if an attempt to open the file denoted by a specified pathname has
	 *             failed
	 * @throws IOException
	 *             Signals that an I/O exception has occurred, also if a write to the file or its
	 *             close fails
	 */
	public static boolean toFile(final File file, final String stringToWrite, final Charset charset)
		throws FileNotFoundException, IOException
	{
		try (FileOutputStream fos = new FileOutputStream(file);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			OutputStreamWriter osw = (null == charset)
				? new OutputStreamWriter(bos)
				: new OutputStreamWriter(bos, charset))
		{
			osw.write(stringToWrite);
		}
		return true;
	}

}
