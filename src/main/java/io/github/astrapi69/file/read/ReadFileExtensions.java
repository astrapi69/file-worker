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
package io.github.astrapi69.file.read;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;

import io.github.astrapi69.io.StreamExtensions;
import io.github.astrapi69.io.file.FileSize;

/**
 * The class {@link ReadFileExtensions} helps in reading files.
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public final class ReadFileExtensions
{

	/**
	 * Private constructor to prevent instantiation
	 */
	private ReadFileExtensions()
	{
	}

	/**
	 * Gets the file content as Byte array from the given file
	 *
	 * @param tmpFile
	 *            the temporary file
	 * @return the file content as Byte array object
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static Byte[] getFilecontentAsByteObjectArray(final File tmpFile) throws IOException
	{
		return toObject(toByteArray(tmpFile));
	}

	/**
	 * Reads the given file to a byte array
	 *
	 * @param file
	 *            the file to read
	 * @return byte array of the file content or null
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static byte[] readFileToBytearray(final File file) throws IOException
	{
		return toByteArray(file);
	}

	/**
	 * Reads the given file with UTF-8 encoding and returns the content as String
	 *
	 * @param file
	 *            the file to read
	 * @return String content from the file
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	public static String fromFile(final File file) throws IOException
	{
		return fromFile(file, StandardCharsets.UTF_8);
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
	 *             Signals that an I/O exception has occurred
	 */
	public static String fromFile(final File file, final Charset encoding) throws IOException
	{
		try (InputStream inputStream = StreamExtensions.getInputStream(file);
			InputStreamReader reader = new InputStreamReader(inputStream, encoding))
		{
			final StringBuilder stringBuilder = new StringBuilder();
			final char[] charArray = new char[FileSize.DEFAULT_BLOCK_SIZE.getSize()];
			int tmp;
			while ((tmp = reader.read(charArray)) > 0)
			{
				stringBuilder.append(charArray, 0, tmp);
			}
			return stringBuilder.toString();
		}
	}

	/**
	 * Reads the first line from the specified file
	 *
	 * @param inputFile
	 *            the file path
	 * @return first line from the file
	 * @throws FileNotFoundException
	 *             is thrown if the given file is not found.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static String readHeadLine(final String inputFile)
		throws FileNotFoundException, IOException
	{
		try (BufferedReader reader = new BufferedReader(new FileReader(inputFile)))
		{
			return reader.readLine();
		}
	}

	/**
	 * Reads all lines from the specified file into a list
	 *
	 * @param input
	 *            the file to read
	 * @return list of lines from the file
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static List<String> readLinesInList(final File input) throws IOException
	{
		return Files.readAllLines(input.toPath());
	}

	/**
	 * Reads a line at the specified index from the file, or null if the line does not exist
	 *
	 * @param input
	 *            the file to read
	 * @param index
	 *            The index of the line
	 * @return The line or null if it does not exist
	 * @throws FileNotFoundException
	 *             is thrown if the given file is not found.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static String readLine(final File input, final int index)
		throws FileNotFoundException, IOException
	{
		List<String> linesRead = readLinesInList(input, false);
		if (index < linesRead.size())
		{
			return linesRead.get(index);
		}
		return null;
	}

	/**
	 * Reads every line from the File and puts them to the List.
	 *
	 * @param input
	 *            The File from where the input comes.
	 * @param trim
	 *            the flag trim if the lines shell be trimed.
	 * @return The List with all lines from the file.
	 * @throws FileNotFoundException
	 *             is thrown if the given file is not found.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static List<String> readLinesInList(final File input, final boolean trim)
		throws FileNotFoundException, IOException
	{
		return readLinesInList(new FileInputStream(input), trim);
	}

	/**
	 * Reads every line from the File and puts them to the List.
	 *
	 * @param input
	 *            The File from where the input comes.
	 * @param encoding
	 *            the encoding
	 * @return The List with all lines from the file.
	 * @throws FileNotFoundException
	 *             is thrown if the given file is not found.
	 * @throws IOException
	 *             When a io-problem occurs.
	 */
	public static List<String> readLinesInList(final File input, final Charset encoding)
		throws FileNotFoundException, IOException
	{
		return readLinesInList(input, encoding, false);
	}

	/**
	 * Reads every line from the File and puts them to the List.
	 *
	 * @param input
	 *            The File from where the input comes.
	 * @param encoding
	 *            the charset for read
	 * @param trim
	 *            the flag trim if the lines shell be trimed.
	 * @return The List with all lines from the file.
	 * @throws FileNotFoundException
	 *             is thrown if the given file is not found.
	 * @throws IOException
	 *             When a io-problem occurs.
	 */
	public static List<String> readLinesInList(final File input, final Charset encoding,
		final boolean trim) throws FileNotFoundException, IOException
	{
		return readLinesInList(new FileInputStream(input), encoding, trim);
	}

	/**
	 * Reads every line from the given InputStream and puts them to the List.
	 *
	 * @param input
	 *            The InputStream from where the input comes.
	 * @return The List with all lines from the file.
	 * @throws IOException
	 *             When a io-problem occurs.
	 */
	public static List<String> readLinesInList(final InputStream input) throws IOException
	{
		return readLinesInList(input, false);
	}

	/**
	 * Reads every line from the given InputStream and puts them to the List.
	 *
	 * @param input
	 *            The InputStream from where the input comes.
	 * @param trim
	 *            the flag trim if the lines shell be trimed.
	 * @return The List with all lines from the file.
	 * @throws IOException
	 *             When a io-problem occurs.
	 */
	public static List<String> readLinesInList(final InputStream input, final boolean trim)
		throws IOException
	{
		// return the list with all lines from the file.
		return readLinesInList(input, StandardCharsets.UTF_8, trim);
	}

	/**
	 * Reads every line from the given InputStream and puts them to the List.
	 *
	 * @param input
	 *            The InputStream from where the input comes.
	 * @param encoding
	 *            the charset for read
	 * @param trim
	 *            the flag trim if the lines shell be trimed.
	 * @return The List with all lines from the file.
	 * @throws IOException
	 *             When a io-problem occurs.
	 */
	public static List<String> readLinesInList(final InputStream input, final Charset encoding,
		final boolean trim) throws IOException
	{
		// The List where the lines from the File to save.
		final List<String> output = new ArrayList<>();
		try (
			InputStreamReader isr = encoding == null
				? new InputStreamReader(input)
				: new InputStreamReader(input, encoding);
			BufferedReader reader = new BufferedReader(isr))
		{
			// the line.
			String line;
			// read all lines from the file
			do
			{
				line = reader.readLine();
				// if null break the loop
				if (line == null)
				{
					break;
				}
				if (trim)
				{
					line.trim();
				}
				// add the line to the list
				output.add(line);
			}
			while (true);
		}
		// return the list with all lines from the file.
		return output;
	}

	/**
	 * Reads properties from a properties file
	 *
	 * @param filename
	 *            the properties file path
	 * @return Properties object or null if error occurs
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static Properties readPropertiesFromFile(final String filename) throws IOException
	{
		Properties properties = new Properties();
		try (InputStream inputStream = Files.newInputStream(new File(filename).toPath()))
		{
			properties.load(inputStream);
		}
		return properties;
	}

	/**
	 * Reads file content into a byte array
	 *
	 * @param tmpFile
	 *            the file to read
	 * @return byte array of file content or null
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static byte[] toByteArray(final File tmpFile) throws IOException
	{
		return tmpFile.exists() && !tmpFile.isDirectory()
			? Files.readAllBytes(tmpFile.toPath())
			: null;
	}

	/**
	 * Get a byte array from the given {@code InputStream}.
	 *
	 * @param input
	 *            The {@code InputStream}.
	 * @return the a byte array.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws NullPointerException
	 *             if the input is null
	 */
	public static byte[] toByteArray(final InputStream input) throws IOException
	{
		return IOUtils.toByteArray(input);
	}

	/**
	 * To byte array.
	 *
	 * @param byteArray
	 *            the byte array
	 * @return the byte[]
	 */
	private static Byte[] toObject(final byte[] byteArray)
	{
		return ArrayUtils.toObject(byteArray);
	}

	/**
	 * Counts all lines in the specified file
	 *
	 * @param fileToCountTheLines
	 *            the file to count lines from
	 * @return line count
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static long countAllLines(File fileToCountTheLines) throws IOException
	{
		return Files.lines(fileToCountTheLines.toPath()).count();
	}

}
