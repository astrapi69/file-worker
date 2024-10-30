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
package io.github.astrapi69.file.read.api;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;

import io.github.astrapi69.file.read.ReadFileExtensions;

/**
 * The interface {@link Readable} provides methods for reading file contents
 *
 * @version 1.0
 */
public interface Readable
{

	/**
	 * Reads the given file with UTF-8 encoding and returns the content as String
	 *
	 * @param file
	 *            the file to read
	 * @return String content from the file
	 * @throws IOException
	 *             if an I/O exception occurs
	 */
	default String fromFile(final File file) throws IOException
	{
		return ReadFileExtensions.fromFile(file, StandardCharsets.UTF_8);
	}

	/**
	 * Reads the given file with the specified encoding and returns the content as String
	 *
	 * @param file
	 *            the file to read
	 * @param encoding
	 *            the encoding to use
	 * @return String content from the file
	 * @throws IOException
	 *             if an I/O exception occurs
	 */
	default String fromFile(final File file, final Charset encoding) throws IOException
	{
		return ReadFileExtensions.fromFile(file, encoding);
	}

	/**
	 * Reads the first line from the specified file
	 *
	 * @param inputFile
	 *            the file path
	 * @return first line from the file
	 * @throws FileNotFoundException
	 *             if the file is not found
	 * @throws IOException
	 *             if an I/O exception occurs
	 */
	default String readHeadLine(final String inputFile) throws FileNotFoundException, IOException
	{
		return ReadFileExtensions.readHeadLine(inputFile);
	}

	/**
	 * Reads all lines from the specified file into a list
	 *
	 * @param input
	 *            the file to read
	 * @return list of lines from the file
	 * @throws IOException
	 *             if an I/O exception occurs
	 */
	default List<String> readAllLines(final File input) throws IOException
	{
		return Files.readAllLines(input.toPath());
	}

	/**
	 * Reads file content into a byte array
	 *
	 * @param tmpFile
	 *            the file to read
	 * @return byte array of file content or null
	 * @throws IOException
	 *             if an I/O exception occurs
	 */
	default byte[] toByteArray(final File tmpFile) throws IOException
	{
		return ReadFileExtensions.toByteArray(tmpFile);
	}

	/**
	 * Gets a byte array from the given {@code InputStream}.
	 *
	 * @param input
	 *            the {@code InputStream}
	 * @return a byte array
	 * @throws IOException
	 *             if an I/O exception occurs
	 */
	default byte[] toByteArray(final InputStream input) throws IOException
	{
		return IOUtils.toByteArray(input);
	}

	/**
	 * Converts a primitive byte array to a Byte array
	 *
	 * @param byteArray
	 *            the byte array
	 * @return Byte array
	 */
	default Byte[] toObject(final byte[] byteArray)
	{
		return ArrayUtils.toObject(byteArray);
	}

}
