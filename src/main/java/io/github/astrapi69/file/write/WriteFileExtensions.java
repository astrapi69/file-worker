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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import io.github.astrapi69.collection.list.ListFactory;
import io.github.astrapi69.file.copy.CopyFileExtensions;
import io.github.astrapi69.file.create.FileFactory;
import io.github.astrapi69.file.system.SystemPropertiesExtensions;
import io.github.astrapi69.io.StreamExtensions;

/**
 * The class {@link WriteFileExtensions} provides methods for writing in files.
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public final class WriteFileExtensions
{

	private WriteFileExtensions()
	{
	}

	/**
	 * Appends the given lines to the given {@link File} object
	 *
	 * @param file
	 *            The source file
	 * @param lineToAppend
	 *            The lines to append
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void appendLines(File file, String... lineToAppend) throws IOException
	{
		Files.write(file.toPath(), ListFactory.newArrayList(lineToAppend),
			StandardOpenOption.APPEND, StandardOpenOption.CREATE);
	}

	/**
	 * Writes the source file with the best performance to the destination file.
	 *
	 * @param srcfile
	 *            The source file.
	 * @param destFile
	 *            The destination file.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @deprecated Use {@link CopyFileExtensions#copyFile(File, File)} instead.
	 */
	@Deprecated
	public static void readSourceFileAndWriteDestFile(final String srcfile, final String destFile)
		throws IOException
	{
		CopyFileExtensions.copyFile(new File(srcfile), new File(destFile));
	}

	/**
	 * The Method string2File() writes a String to the file.
	 *
	 * @param string2write
	 *            The String to write into the file.
	 * @param nameOfFile
	 *            The path to the file and name from the file from where we want to write the
	 *            String.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @deprecated Use {@link StoreFileExtensions#toFile(File, String)} instead.
	 */
	@Deprecated
	public static void string2File(final String string2write, final String nameOfFile)
		throws IOException
	{
		StoreFileExtensions.toFile(new File(nameOfFile), string2write);
	}

	/**
	 * Writes the given input stream to the output stream.
	 *
	 * @param inputStream
	 *            the input stream
	 * @param outputStream
	 *            the output stream
	 * @throws FileNotFoundException
	 *             is thrown if the given file is not found
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void write(final InputStream inputStream, final OutputStream outputStream)
		throws FileNotFoundException, IOException
	{
		StreamExtensions.writeInputStreamToOutputStream(inputStream, outputStream);
	}

	/**
	 * The Method write2File() reads from an opened Reader and writes it to the opened Writer.
	 *
	 * @param reader
	 *            The opened Reader.
	 * @param writer
	 *            The opened Writer.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void write2File(final Reader reader, final Writer writer) throws IOException
	{
		StreamExtensions.writeReaderToWriter(reader, writer);
	}

	/**
	 * The Method write2File(String, String) copys a file from one filename to another.
	 *
	 * @param inputFile
	 *            The Name from the File to read and copy.
	 * @param outputFile
	 *            The Name from the File to write into.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @deprecated Use {@link CopyFileExtensions#copyFile(File, File)} instead.
	 */
	@Deprecated
	public static void write2File(final String inputFile, final String outputFile)
		throws IOException
	{
		CopyFileExtensions.copyFile(new File(inputFile), new File(outputFile));
	}

	/**
	 * The Method write2File() writes the File into the PrintWriter.
	 *
	 * @param inputFile
	 *            The Name from the File to read and copy.
	 * @param writer
	 *            The PrintWriter to write into.
	 * @throws FileNotFoundException
	 *             is thrown if an attempt to open the file denoted by a specified pathname has
	 *             failed.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void write2File(final String inputFile, final Writer writer)
		throws FileNotFoundException, IOException
	{
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFile)))
		{
			write2File(bufferedReader, writer);
		}
	}

	/**
	 * The Method write2FileWithBuffer() copy the content from one file to another. It use a buffer
	 * as the name says.
	 *
	 * @param inputFile
	 *            The Path to the File and name from the file from where we read.
	 * @param outputFile
	 *            The Path to the File and name from the file from where we want to write.
	 * @throws FileNotFoundException
	 *             is thrown if an attempt to open the file denoted by a specified pathname has
	 *             failed.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void write2FileWithBuffer(final String inputFile, final String outputFile)
		throws FileNotFoundException, IOException
	{
		try (InputStream inputStream = StreamExtensions.getInputStream(new File(inputFile));
			OutputStream outputStream = StreamExtensions.getOutputStream(new File(outputFile)))
		{
			WriteFileExtensions.write(inputStream, outputStream);
		}
	}

	/**
	 * Writes the given byte array to the given file.
	 *
	 * @param file
	 *            The file.
	 * @param byteArray
	 *            The byte array.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void writeByteArrayToFile(final File file, final byte[] byteArray)
		throws IOException
	{
		Files.write(file.toPath(), byteArray);
	}

	/**
	 * Writes the given byte array to a file.
	 *
	 * @param filename
	 *            The filename from the file.
	 * @param byteArray
	 *            The byte array.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void writeByteArrayToFile(final String filename, final byte[] byteArray)
		throws IOException
	{
		writeByteArrayToFile(FileFactory.newFile(filename), byteArray);
	}

	/**
	 * Writes the input from the lines into the file.
	 *
	 * @param lines
	 *            The lines to write to file.
	 * @param output
	 *            The output-file.
	 * @throws FileNotFoundException
	 *             is thrown if an attempt to open the file denoted by a specified pathname has
	 *             failed.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void writeLinesToFile(final Collection<String> lines, final File output)
		throws FileNotFoundException, IOException
	{
		final StringBuilder sb = new StringBuilder();
		for (final String line : lines)
		{
			sb.append(line);
			sb.append("\n");
		}
		StoreFileExtensions.toFile(output, sb.toString());
	}

	/**
	 * Writes the input from the lines into the file.
	 *
	 * @param lines
	 *            The lines to write to file.
	 * @param output
	 *            The output-file.
	 * @param encoding
	 *            the encoding
	 * @throws FileNotFoundException
	 *             is thrown if an attempt to open the file denoted by a specified pathname has
	 *             failed.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void writeLinesToFile(final Collection<String> lines, final File output,
		final String encoding) throws FileNotFoundException, IOException
	{
		final StringBuilder sb = new StringBuilder();
		final String lineSeparator = SystemPropertiesExtensions.getLineSeparator();
		for (final String line : lines)
		{
			sb.append(line);
			sb.append(lineSeparator);
		}
		StoreFileExtensions.toFile(output, sb.toString(), encoding);
	}

	/**
	 * Writes the input from the collection into the file.
	 *
	 * @param output
	 *            The file to write the lines.
	 * @param input
	 *            The list with the input data.
	 * @param encoding
	 *            The encoding.
	 * @throws FileNotFoundException
	 *             is thrown if an attempt to open the file denoted by a specified pathname has
	 *             failed.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void writeLinesToFile(final File output, final List<String> input,
		final String encoding) throws FileNotFoundException, IOException
	{
		final String lineSeparator = SystemPropertiesExtensions.getLineSeparator();
		try (FileOutputStream fos = new FileOutputStream(output);
			OutputStreamWriter osw = (null == encoding)
				? new OutputStreamWriter(fos)
				: new OutputStreamWriter(fos, encoding);
			PrintWriter out = new PrintWriter(osw))
		{
			final int size = input.size();
			final StringBuilder sb = new StringBuilder();
			for (int i = 0; i < size; i++)
			{
				final String entry = input.get(i);
				sb.append(entry).append(lineSeparator);
			}
			out.write(sb.toString());
		}
	}

	/**
	 * The Method writeProperties2File(String, Properties) writes the Properties to the file.
	 *
	 * @param filename
	 *            The filename from the file to write the properties.
	 * @param properties
	 *            The properties.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void writeProperties2File(final String filename, final Properties properties)
		throws IOException
	{
		// Write properties to the file.
		try (FileOutputStream fos = new FileOutputStream(filename))
		{
			properties.store(fos, null);
		}
	}

}
