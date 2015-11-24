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
package de.alpharogroup.file.csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;

import lombok.experimental.ExtensionMethod;
import de.alpharogroup.file.write.WriteFileUtils;
import de.alpharogroup.io.StreamUtils;
import de.alpharogroup.string.StringExtensions;

/**
 * Utility class for the use of cvs-files.
 *
 * @version 1.0
 * @author Asterios Raptis
 */
@ExtensionMethod(StringExtensions.class)
public final class CsvFileUtils
{
	/**
	 * Reads every line from the File splits the data through a comma and puts them to the List.
	 *
	 * @param input
	 *            The File from where the input comes.
	 * @param encoding
	 *            The encoding from the file.
	 * @return The List with all lines from the file.
	 */
	public static List<String> formatKommaSeperatedFileToList(final File input,
		final String encoding)
	{
		// The List where the data from every line from the File to put.
		final List<String> output = new ArrayList<>();
		BufferedReader reader = null;
		try
		{
			// create the bufferedreader
			reader = (BufferedReader)StreamUtils.getReader(input, encoding, false);
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
				// Split the line
				final String[] splittedData = line.split(",");
				// Iterate throuh the array
				for (final String element : splittedData)
				{
					// add the line to the list
					output.add(element.trim());
				}
			}
			while (true);
		}
		catch (final UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		catch (final FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			StreamUtils.closeReader(reader);
		}

		// return the list with all lines from the file.
		return output;

	}

	/**
	 * Formats the List that contains String-object to a csv-file wich is plus-minus 100 characters
	 * in every line.
	 *
	 * @param list
	 *            The List with the Strings.
	 * @return The String produced from the List.
	 */
	private static String formatListToString(final List<String> list)
	{
		int lineLength = 0;
		final StringBuffer sb = new StringBuffer();
		for (final String str : list)
		{
			final int length = str.length();
			lineLength = length + lineLength;
			sb.append(str);
			sb.append(", ");
			if (100 < lineLength)
			{
				sb.append("\n");
				lineLength = 0;
			}
		}
		return sb.toString().trim();
	}

	/**
	 * Formats a file that has in every line one input-data into a csv-file.
	 *
	 * @param input
	 *            The input-file to format. The current format from the file is every data in a
	 *            line.
	 * @param output
	 *            The file where the formatted data should be inserted.
	 * @param encoding
	 *            the encoding
	 * @throws IOException
	 *             When an io error occurs.
	 */
	public static void formatToCSV(final File input, final File output, final String encoding)
		throws IOException
	{
		final List<String> list = readLinesInList(input, "UTF-8");
		final String sb = formatListToString(list);
		WriteFileUtils.writeStringToFile(output, sb, encoding);
	}

	/**
	 * Gets the given cvs file as list of maps. Every map has as key the header from the column and
	 * the corresponding value for this line.
	 *
	 * @param input
	 *            the input
	 * @return the cvs as list map
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static List<Map<String, String>> getCvsAsListMap(final File input) throws IOException
	{
		final List<String[]> lines = CsvFileUtils.readFileToList(input, ";", "UTF-8");
		final List<Map<String, String>> data = new ArrayList<>();
		final String[] headline = lines.remove(0);
		for (final String[] line : lines)
		{
			final Map<String, String> context = new LinkedHashMap<>();
			final String[] currentLine = line;
			for (int j = 0; j < currentLine.length; j++)
			{
				context.put(headline[j], currentLine[j]);
			}
			data.add(context);
		}
		return data;
	}


	/**
	 * Gets the data from line.
	 *
	 * @param line
	 *            the line
	 * @param seperator
	 *            the seperator
	 * @return the data from line
	 */
	public static String[] getDataFromLine(final String line, final String seperator)
	{
		return getDataFromLine(line, seperator, false);
	}

	/**
	 * Gets the data from line.
	 *
	 * @param line
	 *            the line
	 * @param seperator
	 *            the seperator
	 * @param trim
	 *            the trim
	 * @return the data from line
	 */
	public static String[] getDataFromLine(final String line, final String seperator,
		final boolean trim)
	{
		final StringTokenizer tokenizer = new StringTokenizer(line, seperator);
		final List<String> data = new ArrayList<>();
		while (tokenizer.hasMoreElements())
		{
			final String string = tokenizer.nextToken();
			if (trim)
			{
				data.add(string.trim());
			}
			else
			{
				data.add(string);
			}

		}
		final String[] splittedLine = data.toArray(new String[0]);
		return splittedLine;
	}

	/**
	 * Gets the line count from csv file.
	 *
	 * @param file
	 *            the file
	 * @return the line count from csv file
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static int getLineCountFromCsvFile(final File file) throws IOException
	{
		return readFileToList(file).size();
	}

	/**
	 * Reads from a csv-file the field from the given position and puts them to the List.
	 *
	 * @param input
	 *            The input-file.
	 * @param position
	 *            The position from the field.
	 * @param putFirstLine
	 *            Flag that tells if the first line will be put in the list.
	 * @param encoding
	 *            the encoding
	 * @return The list with the data.
	 * @throws IOException
	 *             When a io-problem occurs.
	 */
	public static List<String> readDataFromCVSFileToList(final File input, final int position,
		final boolean putFirstLine, final String encoding) throws IOException
	{
		final List<String> output = new ArrayList<>();
		BufferedReader reader = null;
		try
		{
			// create the bufferedreader
			reader = (BufferedReader)StreamUtils.getReader(input, encoding, false);
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
				// Split the line
				final String[] splittedData = line.split(",");
				// get the data with the index
				if (position < splittedData.length - 1)
				{
					final String s = splittedData[position].removeQuotationMarks();
					output.add(s);
				}
				else
				{
					break;
				}

			}
			while (true);
		}
		catch (final IOException e)
		{
			throw e;
		}
		finally
		{
			StreamUtils.closeReader(reader);
		}
		// return the list with all lines from the file.
		if (putFirstLine)
		{
			output.remove(0);
		}
		return output;
	}

	/**
	 * Reads from a csv-file the field from the given position and puts them to the List.
	 *
	 * @param input
	 *            The input-file.
	 * @param position
	 *            The position from the field.
	 * @param putFirstLine
	 *            Flag that tells if the first line will be put in the list.
	 * @param splitChar
	 *            the split char
	 * @param encoding
	 *            the encoding
	 * @return The list with the data.
	 * @throws IOException
	 *             When a io-problem occurs.
	 */
	public static List<String> readDataFromCVSFileToList(final File input, final int position,
		final boolean putFirstLine, final String splitChar, final String encoding)
		throws IOException
	{
		final List<String> output = new ArrayList<>();
		BufferedReader reader = null;
		try
		{
			// create the bufferedreader
			reader = (BufferedReader)StreamUtils.getReader(input, encoding, false);
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
				// Split the line
				final String[] splittedData = line.split(splitChar);
				// get the data with the index
				if (position <= splittedData.length - 1)
				{
					final String s = splittedData[position].removeQuotationMarks();
					output.add(s);
				}
			}
			while (true);
		}
		catch (final IOException e)
		{
			throw e;
		}
		finally
		{
			StreamUtils.closeReader(reader);
		}
		// return the list with all lines from the file.
		if (putFirstLine)
		{
			output.remove(0);
		}
		return output;
	}

	/**
	 * Read filelist to properties.
	 *
	 * @param input
	 *            the input
	 * @return the properties
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static Properties readFilelistToProperties(final File input) throws IOException
	{
		final List<String> list = readLinesInList(input, null);
		final Properties prop = new Properties();
		for (int i = 0; i < list.size(); i++)
		{
			final String element = list.get(i);
			prop.put(i + "", element);
		}
		return prop;
	}

	/**
	 * Reads every line from the given File into a List and returns the List.
	 *
	 * @param file
	 *            The file from where to read.
	 * @return The List with all lines from the File.
	 * @throws IOException
	 *             When a io-problem occurs.
	 */
	public static List<String> readFileToList(final File file) throws IOException
	{
		return readFileToList(file, null);
	}


	/**
	 * Reads every line from the given File into a List and returns the List.
	 *
	 * @param file
	 *            The file from where to read.
	 * @param encoding
	 *            The encoding to read.
	 * @return The List with all lines from the File.
	 * @throws IOException
	 *             When a io-problem occurs.
	 */
	public static List<String> readFileToList(final File file, final String encoding)
		throws IOException
	{
		final List<String> fn = new ArrayList<>();
		BufferedReader reader = null;
		try
		{
			// create a reader
			reader = (BufferedReader)StreamUtils.getReader(file, encoding, false);
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
				fn.add(line);

			}
			while (true);
		}
		catch (final IOException e)
		{
			throw e;
		}
		finally
		{
			StreamUtils.closeReader(reader);
		}

		return fn;

	}


	/**
	 * Reads every line from the given File into a List of String arrays and returns the List.
	 *
	 * @param file
	 *            The file from where to read.
	 * @param seperator
	 *            the seperator
	 * @param encoding
	 *            The encoding to read.
	 * @return The List with all lines from the File.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static List<String[]> readFileToList(final File file, final String seperator,
		final String encoding) throws IOException
	{
		final List<String[]> fn = new ArrayList<>();
		BufferedReader reader = null;
		try
		{
			// create a reader
			reader = (BufferedReader)StreamUtils.getReader(file, encoding, false);
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
				final String[] splittedLine = getDataFromLine(line, seperator);

				fn.add(splittedLine);

			}
			while (true);
		}
		catch (final IOException e)
		{
			throw e;
		}
		finally
		{
			StreamUtils.closeReader(reader);
		}
		return fn;
	}

	/**
	 * Reads every line from the File and puts them to the List.
	 *
	 * @param input
	 *            The File from where the input comes.
	 * @param encoding
	 *            The encoding from the file.
	 * @return The List with all lines from the file.
	 * @throws IOException
	 *             When a io-problem occurs.
	 */
	public static List<String> readLinesInList(final File input, final String encoding)
		throws IOException
	{
		// The List where the lines from the File to put.
		final List<String> output = new ArrayList<>();
		BufferedReader reader = null;
		try
		{
			// create the bufferedreader
			reader = (BufferedReader)StreamUtils.getReader(input, encoding, false);
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
				// add the line to the list
				output.add(line);
			}
			while (true);
		}
		catch (final IOException e)
		{
			throw e;
		}
		finally
		{
			StreamUtils.closeReader(reader);
		}
		// return the list with all lines from the file.
		return output;
	}

	/**
	 * Read an csv-file and puts them in a String-array.
	 *
	 * @param csvData
	 *            The csv-file with the data.
	 * @param encoding
	 *            The encoding to read.
	 * @return The data from the csv-file as a String-array.
	 * @throws FileNotFoundException
	 *             the file not found exception
	 * @throws IOException
	 *             When an io-error occurs.
	 */
	public static String[] sortData(final File csvData, final String encoding)
		throws FileNotFoundException, IOException
	{
		final List<String> fn = new ArrayList<>();
		BufferedReader reader = null;
		try
		{
			// create a reader
			reader = (BufferedReader)StreamUtils.getReader(csvData, encoding, false);
			// the line.
			String line = null;
			int index, last;
			// read all lines from the file
			do
			{
				line = reader.readLine();
				// if null break the loop
				if (line == null)
				{
					break;
				}
				// initialize the last
				last = 0;
				// get the index from the comma
				index = line.indexOf(',');

				while (index != -1)
				{
					// get the next firstname and remove the whitespaces.
					final String firstname = line.substring(last, index).trim();
					// added to the list
					fn.add(firstname);
					// set last to the next position
					last = index + 1;
					// get the next index from the comma in the line
					index = line.indexOf(',', last);
				}
			}
			while (true);
		}
		catch (final IOException e)
		{
			throw e;
		}
		finally
		{
			StreamUtils.closeReader(reader);
		}
		// convert the list to a String array.
		final String data[] = fn.toArray(new String[fn.size()]);
		// and sort the array.
		Arrays.sort(data);
		return data;
	}

	/**
	 * Stores a komma seperated file to a properties object. As key is the number from the counter.
	 *
	 * @param output
	 *            the output
	 * @param input
	 *            the input
	 * @param comments
	 *            the comments
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void storeFilelistToProperties(final File output, final File input,
		final String comments) throws IOException
	{
		final Properties prop = readFilelistToProperties(input);
		final OutputStream out = StreamUtils.getOutputStream(output, true);
		prop.store(out, comments);
		StreamUtils.closeOutputStream(out);
	}

	/**
	 * Writes the toString() value of each item in a collection to the specified File line by line.
	 * The default VM encoding and the default line ending will be used.
	 *
	 * @param output
	 *            The File where the values to save.
	 * @param values
	 *            The Set with the values to put into the file.
	 * @param encoding
	 *            The encoding from the file.
	 * @throws IOException
	 *             When a io-problem occurs.
	 */
	public static void writeLines(final File output, final Set<String> values, final String encoding)
		throws IOException
	{
		writeLinesToFile(values, output, encoding);
	}

	/**
	 * Writes all the String-object in the collection into the given file.
	 *
	 * @param collection
	 *            The collection with the String-object.
	 * @param output
	 *            The file where the String-object will be writing.
	 * @param encoding
	 *            The encoding from the file.
	 */
	public static void writeLinesToFile(final Collection<String> collection, final File output,
		final String encoding)
	{
		final StringBuffer sb = new StringBuffer();
		for (final String element : collection)
		{
			sb.append(element);
			sb.append("\n");
		}
		WriteFileUtils.writeStringToFile(output, sb.toString(), encoding);
	}

	/**
	 * Private constructor.
	 */
	private CsvFileUtils()
	{
		super();
	}

}
