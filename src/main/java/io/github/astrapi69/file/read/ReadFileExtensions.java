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
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Stream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;

import io.github.astrapi69.io.StreamExtensions;
import io.github.astrapi69.io.file.FileSize;

/**
 * The class {@link ReadFileExtensions} helps in reading files.
 * <p>
 * Every method that decodes bytes into characters decodes strictly: a byte sequence that is not
 * valid in the charset gives a {@link java.nio.charset.MalformedInputException}, as
 * {@link Files#readAllLines(java.nio.file.Path)} does, and no method replaces it with the
 * replacement character U+FFFD. Read a file whose charset is not known with
 * {@link #readFileToBytearray(File)}, or with a charset in which every byte is valid, such as
 * ISO-8859-1.
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
	 * @throws java.nio.charset.MalformedInputException
	 *             if the file is not valid UTF-8
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
	 * @throws java.nio.charset.MalformedInputException
	 *             if the file is not valid in the given encoding
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	public static String fromFile(final File file, final Charset encoding) throws IOException
	{
		try (InputStream inputStream = StreamExtensions.getInputStream(file);
			Reader reader = newStrictReader(inputStream, encoding))
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
	 * Reads the first line from the specified file with UTF-8 encoding, as {@link #fromFile(File)}
	 * and {@link #readLinesInList(File)} do, whatever the default charset of the platform is. The
	 * first line ends at the first line feed or carriage return. Only that line is decoded: bytes
	 * in it that are not valid UTF-8 give a {@link java.nio.charset.MalformedInputException}, the
	 * bytes after it are not read.
	 *
	 * @param inputFile
	 *            the file path
	 * @return first line from the file, or null if the file is empty
	 * @throws java.nio.charset.MalformedInputException
	 *             if the first line is not valid UTF-8
	 * @throws FileNotFoundException
	 *             is thrown if the given file is not found.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static String readHeadLine(final String inputFile)
		throws FileNotFoundException, IOException
	{
		try (InputStream input = new BufferedInputStream(new FileInputStream(inputFile)))
		{
			int read = input.read();
			if (read == -1)
			{
				return null;
			}
			// a line feed or a carriage return is never part of a multi-byte sequence in UTF-8
			final ByteArrayOutputStream firstLine = new ByteArrayOutputStream();
			while (read != -1 && read != '\n' && read != '\r')
			{
				firstLine.write(read);
				read = input.read();
			}
			return newStrictDecoder(StandardCharsets.UTF_8)
				.decode(ByteBuffer.wrap(firstLine.toByteArray())).toString();
		}
	}

	/**
	 * Reads all lines from the specified file into a list. The file is decoded strictly as UTF-8:
	 * bytes that are not valid UTF-8 give a {@link java.nio.charset.MalformedInputException}, as in
	 * every other method of this class that decodes.
	 *
	 * @param input
	 *            the file to read
	 * @return list of lines from the file
	 * @throws java.nio.charset.MalformedInputException
	 *             if the file is not valid UTF-8
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
	 * @return The line or null if it does not exist, that is if the index is negative or not less
	 *         than the number of lines
	 * @throws java.nio.charset.MalformedInputException
	 *             if the file is not valid UTF-8
	 * @throws FileNotFoundException
	 *             is thrown if the given file is not found.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static String readLine(final File input, final int index)
		throws FileNotFoundException, IOException
	{
		List<String> linesRead = readLinesInList(input, false);
		if (0 <= index && index < linesRead.size())
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
	 *            if true, every line is returned without its leading and trailing whitespace, as by
	 *            {@link String#trim()}, so that a line of whitespace only is returned as an empty
	 *            line; if false, every line is returned as it is.
	 * @return The List with all lines from the file.
	 * @throws java.nio.charset.MalformedInputException
	 *             if the file is not valid UTF-8
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
	 * @throws java.nio.charset.MalformedInputException
	 *             if the file is not valid in the given encoding
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
	 *            if true, every line is returned without its leading and trailing whitespace, as by
	 *            {@link String#trim()}, so that a line of whitespace only is returned as an empty
	 *            line; if false, every line is returned as it is.
	 * @return The List with all lines from the file.
	 * @throws java.nio.charset.MalformedInputException
	 *             if the file is not valid in the given encoding
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
	 * @throws java.nio.charset.MalformedInputException
	 *             if the stream is not valid UTF-8
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
	 *            if true, every line is returned without its leading and trailing whitespace, as by
	 *            {@link String#trim()}, so that a line of whitespace only is returned as an empty
	 *            line; if false, every line is returned as it is.
	 * @return The List with all lines from the file.
	 * @throws java.nio.charset.MalformedInputException
	 *             if the stream is not valid UTF-8
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
	 *            if true, every line is returned without its leading and trailing whitespace, as by
	 *            {@link String#trim()}, so that a line of whitespace only is returned as an empty
	 *            line; if false, every line is returned as it is.
	 * @return The List with all lines from the file.
	 * @throws java.nio.charset.MalformedInputException
	 *             if the stream is not valid in the given encoding, or in the default charset of
	 *             the platform if it is null
	 * @throws IOException
	 *             When a io-problem occurs.
	 */
	public static List<String> readLinesInList(final InputStream input, final Charset encoding,
		final boolean trim) throws IOException
	{
		// The List where the lines from the File to save.
		final List<String> output = new ArrayList<>();
		try (
			Reader isr = newStrictReader(input,
				encoding == null ? Charset.defaultCharset() : encoding);
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
					line = line.trim();
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
	 * Returns a reader that decodes the given stream strictly in the given charset: a byte sequence
	 * that is not valid in it gives a {@link java.nio.charset.MalformedInputException}, a character
	 * it cannot map an {@link java.nio.charset.UnmappableCharacterException}, instead of the
	 * replacement character U+FFFD that an {@link InputStreamReader} of a {@link Charset} writes
	 *
	 * @param input
	 *            the stream to decode
	 * @param charset
	 *            the charset to decode with
	 * @return the reader
	 */
	private static Reader newStrictReader(final InputStream input, final Charset charset)
	{
		return new InputStreamReader(input, newStrictDecoder(charset));
	}

	/**
	 * Returns a decoder of the given charset that reports a malformed input and an unmappable
	 * character instead of replacing them
	 *
	 * @param charset
	 *            the charset to decode with
	 * @return the decoder
	 */
	private static CharsetDecoder newStrictDecoder(final Charset charset)
	{
		return charset.newDecoder().onMalformedInput(CodingErrorAction.REPORT)
			.onUnmappableCharacter(CodingErrorAction.REPORT);
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
	 * Counts all lines in the specified file, which is read as UTF-8 and closed again before this
	 * method returns
	 *
	 * @param fileToCountTheLines
	 *            the file to count lines from
	 * @return line count
	 * @throws java.nio.charset.MalformedInputException
	 *             if the file is not valid UTF-8
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static long countAllLines(File fileToCountTheLines) throws IOException
	{
		try (Stream<String> lines = Files.lines(fileToCountTheLines.toPath()))
		{
			return lines.count();
		}
		catch (final UncheckedIOException e)
		{
			// Files.lines reports an error that occurs while it reads, such as a malformed byte,
			// as an UncheckedIOException around the IOException this method declares
			throw e.getCause();
		}
	}

}
