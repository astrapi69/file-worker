/**
 * The MIT License
 *
 * Copyright (C) 2007 Asterios Raptis
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
package de.alpharogroup.file.read;

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
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;

import de.alpharogroup.file.FileConst;
import de.alpharogroup.io.StreamUtils;

/**
 * The Class ReadFileUtils helps you reading files.
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public final class ReadFileUtils
{
	/** The LOGGER. */
	private static final Logger LOGGER = Logger.getLogger(ReadFileUtils.class.getName());

	/**
	 * Get a Byte array from the given file.
	 *
	 * @param tmpFile
	 *            the tmp file
	 * @return the filecontent as Byte array object.
	 */
	public static Byte[] getFilecontentAsByteObjectArray(final File tmpFile)
	{
		return toObject(toByteArray(tmpFile));
	}

	/**
	 * The Method inputStream2String() reads the data from the InputStream into a String.
	 *
	 * @param inputStream
	 *            The InputStream from where we read.
	 * @return The String that we read from the InputStream.
	 */
	public static String inputStream2String(final InputStream inputStream)
	{
		return inputStream2String(inputStream, Charset.forName("UTF-8"));
	}

	/**
	 * The Method inputStream2String() reads the data from the InputStream into a String.
	 *
	 * @param inputStream
	 *            The InputStream from where we read.
	 * @param encoding
	 *            the encoding
	 * @return The String that we read from the InputStream.
	 */
	public static String inputStream2String(final InputStream inputStream, final Charset encoding)
	{
		return ReadFileUtils.reader2String(new InputStreamReader(inputStream, encoding));
	}

	/**
	 * The Method openFileReader() opens a BufferedReader from the given file.
	 *
	 * @param fileName
	 *            The file from where to read.
	 * @return The opened BufferedReader from the specified file.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static Reader openFileReader(final String fileName) throws IOException
	{
		BufferedReader bufferedReader = null;
		final File file = new File(fileName);
		bufferedReader = (BufferedReader)StreamUtils.getReader(file);
		return bufferedReader;
	}

	/**
	 * The Method reader2String() reads the data from the Reader into a String.
	 *
	 * @param reader
	 *            The Reader from where we read.
	 * @return The String that we read from the Reader.
	 */
	public static String reader2String(final Reader reader)
	{
		final StringBuffer stringBuffer = new StringBuffer();
		final char[] charArray = new char[FileConst.BLOCKSIZE];
		int tmp;
		try
		{
			while ((tmp = reader.read(charArray)) > 0)
			{
				stringBuffer.append(charArray, 0, tmp);
			}
		}
		catch (final IOException e)
		{
			LOGGER.log(Level.SEVERE, "reader2String failed...\n" + e.getMessage(), e);
		}
		finally
		{
			StreamUtils.closeReader(reader);
		}
		return stringBuffer.toString();
	}

	/**
	 * Get a byte array from the given file.
	 *
	 * @param file
	 *            The file.
	 * @return Returns a byte array or null.
	 */
	public static byte[] readFileToBytearray(final File file)
	{
		return toByteArray(file);
	}

	/**
	 * The Method readFromFile() reads the filecontent to a String.
	 *
	 * @param file
	 *            The File to read to a String.
	 * @return The String from the File.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static String readFromFile(final File file) throws IOException
	{
		return inputStream2String(StreamUtils.getInputStream(file));
	}

	public static String readFromFile(final File file, final Charset encoding) throws IOException
	{
		return inputStream2String(StreamUtils.getInputStream(file), encoding);
	}

	/**
	 * The Method readHeadLine() opens the File and reads the first line from the file.
	 *
	 * @param inputFile
	 *            The Path to the File and name from the file from where we read.
	 * @return The first line from the file.
	 */
	public static String readHeadLine(final String inputFile)
	{
		BufferedReader reader = null;
		String headLine = null;
		try
		{
			reader = new BufferedReader(new FileReader(inputFile));
			headLine = reader.readLine();
			reader.close();
		}
		catch (final FileNotFoundException e)
		{
			LOGGER.log(Level.SEVERE, "readHeadLine failed...\n" + e.getMessage(), e);
		}
		catch (final IOException e)
		{
			LOGGER.log(Level.SEVERE, "readHeadLine failed...\n" + e.getMessage(), e);
		}
		finally
		{
			StreamUtils.closeReader(reader);
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
	 *             When a io-problem occurs.
	 */
	public static List<String> readLinesInList(final File input) throws FileNotFoundException,
		IOException
	{
		return readLinesInList(input, false);
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
	 *             When a io-problem occurs.
	 */
	public static List<String> readLinesInList(final File input, final boolean trim)
		throws FileNotFoundException, IOException
	{
		return readLinesInList(new FileInputStream(input), trim);
	}

	/**
	 * Reads every line from the given InputStream and puts them to the List.
	 *
	 * @param input
	 *            The InputStream from where the input comes.
	 * @return The List with all lines from the file.
	 * @throws FileNotFoundException
	 *             is thrown if the given file is not found.
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
		// The List where the lines from the File to save.
		final List<String> output = new ArrayList<>();
		InputStreamReader isr = null;
		BufferedReader reader = null;
		try
		{
			// create the inputstreamreader
			isr = new InputStreamReader(input);
			// create the bufferedreader
			reader = new BufferedReader(isr);
			// the line.
			String line = null;
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
		finally
		{
			StreamUtils.closeReader(isr);
			StreamUtils.closeReader(reader);
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
	 */
	public static Properties readPropertiesFromFile(final String filename)
	{
		final Properties properties = new Properties();
		FileInputStream fis = null;
		try
		{
			fis = new FileInputStream(filename);
			properties.load(fis);
		}
		catch (final IOException e)
		{
			LOGGER.log(Level.SEVERE, "readPropertiesFromFile failed...\n" + e.getMessage(), e);
		}
		finally
		{
			StreamUtils.closeInputStream(fis);
		}
		return properties;
	}

	/**
	 * Get a byte array from the given file.
	 *
	 * @param tmpFile
	 *            The file.
	 * @return Returns a byte array or null.
	 */
	public static byte[] toByteArray(final File tmpFile)
	{
		byte[] data = null;
		BufferedInputStream bis = null;
		ByteArrayOutputStream bos = null;
		if (tmpFile.exists() && !tmpFile.isDirectory())
		{
			try
			{
				bos = new ByteArrayOutputStream(FileConst.KILOBYTE);
				bis = new BufferedInputStream(new FileInputStream(tmpFile));
				StreamUtils.writeInputStreamToOutputStream(bis, bos, false);
				data = bos.toByteArray();
			}
			catch (final IOException e)
			{
				LOGGER.log(Level.SEVERE,
					"transformFilecontentToByteArray failed...\n" + e.getMessage(), e);
			}
			finally
			{
				StreamUtils.closeInputStream(bis);
				StreamUtils.closeOutputStream(bos);
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
	 * @throws NullPointerException
	 *             if the input is null
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
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
	 * Private constructor.
	 */
	private ReadFileUtils()
	{
		super();
	}

}
