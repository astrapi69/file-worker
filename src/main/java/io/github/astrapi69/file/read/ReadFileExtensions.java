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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
 * The class {@link ReadFileExtensions} helps you reading files.
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
	 * Get a Byte array from the given file.
	 *
	 * @param tmpFile
	 *            the tmp file
	 * @return the filecontent as Byte array object.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static Byte[] getFilecontentAsByteObjectArray(final File tmpFile) throws IOException
	{
		return toObject(toByteArray(tmpFile));
	}

	/**
	 * Get a byte array from the given file.
	 *
	 * @param file
	 *            The file.
	 * @return Returns a byte array or null.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static byte[] readFileToBytearray(final File file) throws IOException
	{
		return toByteArray(file);
	}

	/**
	 * This method reads the given file with the default UTF-8 encoding and returns the content as
	 * an {@link String} object
	 *
	 * @param file
	 *            The file to read to an {@link String} object
	 * @return The {@link String} object from the given file
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	public static String fromFile(final File file) throws IOException
	{
		return fromFile(file, StandardCharsets.UTF_8);
	}

	/**
	 * This method reads the given file with the given encoding and returns the content as an
	 * {@link String} object
	 *
	 * @param file
	 *            The file to read to an {@link String} object
	 * @param encoding
	 *            the encoding for reading the given file
	 * @return The {@link String} object from the given file
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
	 * The Method readHeadLine() opens the File and reads the first line from the file.
	 *
	 * @param inputFile
	 *            The Path to the File and name from the file from where we read.
	 * @return The first line from the file.
	 * @throws FileNotFoundException
	 *             is thrown if the given file is not found.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static String readHeadLine(final String inputFile)
		throws FileNotFoundException, IOException
	{
		String headLine;
		try (BufferedReader reader = new BufferedReader(new FileReader(inputFile)))
		{
			headLine = reader.readLine();
		}
		return headLine;
	}

	/**
	 * Reads every line from the File and puts them to the List.
	 *
	 * @param input
	 *            The File from where the input comes.
	 * @return The List with all lines from the file.
	 * @throws FileNotFoundException
	 *             is thrown if the given file is not found.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static List<String> readLinesInList(final File input)
		throws FileNotFoundException, IOException
	{
		return readLinesInList(input, false);
	}

	/**
	 * Read a line from the given index in the given File and return it, if index greater than null
	 * is returned
	 *
	 * @param input
	 *            The File from where the input comes
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
	 * The Method readFromFile(String) reads from the properties-file all Properties and saves them
	 * into a Properties-Object.
	 *
	 * @param filename
	 *            The Filename from the Properties-file.
	 * @return The Properties or null if an error occurs.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static Properties readPropertiesFromFile(final String filename) throws IOException
	{
		final Properties properties = new Properties();
		try (FileInputStream fis = new FileInputStream(filename))
		{
			properties.load(fis);
		}
		return properties;
	}

	/**
	 * Get a byte array from the given file.
	 *
	 * @param tmpFile
	 *            The file.
	 * @return Returns a byte array or null.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static byte[] toByteArray(final File tmpFile) throws IOException
	{
		byte[] data = null;
		if (tmpFile.exists() && !tmpFile.isDirectory())
		{
			try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(tmpFile));
				ByteArrayOutputStream bos = new ByteArrayOutputStream(FileSize.KILOBYTE.getSize()))
			{
				StreamExtensions.writeInputStreamToOutputStream(bis, bos);
				data = bos.toByteArray();
			}
		}
		return data;
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
	 * Count all lines from the given file
	 *
	 * @param fileToCountTheLines
	 *            The File to count the lines
	 * @return the number of the lines from the given file
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static long countAllLines(File fileToCountTheLines) throws IOException
	{
		return Files.lines(fileToCountTheLines.toPath()).count();
	}

}
