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
package io.github.astrapi69.file.write.api;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Collection;

import io.github.astrapi69.file.system.SystemPropertiesExtensions;

/**
 * The interface {@link Storable} provides methods for storing or updating file contents
 */
public interface Storable
{

	/**
	 * Stores the given byte array to the specified {@link File} object
	 *
	 * @param file
	 *            the {@link File} object to write the byte array
	 * @param data
	 *            the byte array to be stored
	 * @throws IOException
	 *             if an I/O exception occurs
	 */
	default void toFile(final File file, final byte[] data) throws IOException
	{
		Files.write(file.toPath(), data);
	}

	/**
	 * Writes a {@link String} to the specified {@link File} object
	 *
	 * @param file
	 *            the {@link File} to write the string
	 * @param stringToWrite
	 *            the string to write
	 * @return true if the string was successfully written, otherwise false
	 * @throws IOException
	 *             if an I/O exception occurs
	 */
	default boolean toFile(final File file, final String stringToWrite) throws IOException
	{
		return toFile(file, stringToWrite, (Charset)null);
	}

	/**
	 * Writes lines from a collection to the specified file
	 *
	 * @param file
	 *            the file to write
	 * @param lines
	 *            the lines to write
	 * @return true if lines were written successfully, otherwise false
	 * @throws IOException
	 *             if an I/O exception occurs
	 */
	default boolean toFile(final File file, final Collection<String> lines) throws IOException
	{
		final String content = String.join(SystemPropertiesExtensions.getLineSeparator(), lines);
		return toFile(file, content);
	}

	/**
	 * Writes lines to the specified file with the given encoding
	 *
	 * @param file
	 *            the file to write
	 * @param lines
	 *            the lines to write
	 * @param charset
	 *            the charset to use
	 * @return true if lines were written successfully, otherwise false
	 * @throws IOException
	 *             if an I/O exception occurs
	 */
	default boolean toFile(final File file, final Collection<String> lines, final Charset charset)
		throws IOException
	{
		final String content = String.join(SystemPropertiesExtensions.getLineSeparator(), lines);
		return toFile(file, content, charset);
	}

	/**
	 * Writes a string to the specified file with the given encoding
	 *
	 * @param file
	 *            the file to write
	 * @param stringToWrite
	 *            the string to write
	 * @param charset
	 *            the charset to use
	 * @return true if the string was successfully written, otherwise false
	 * @throws IOException
	 *             if an I/O exception occurs
	 */
	default boolean toFile(final File file, final String stringToWrite, final Charset charset)
		throws IOException
	{
		Files.writeString(file.toPath(), stringToWrite,
			charset != null ? charset : Charset.defaultCharset());
		return true;
	}

}
