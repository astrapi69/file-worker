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
package de.alpharogroup.file.write;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import de.alpharogroup.file.FileConst;
import de.alpharogroup.io.StreamExtensions;
import lombok.experimental.UtilityClass;

/**
 * The class {@link WriteFileExtensions} provides methods for writing in files.
 *
 * @version 1.0
 * @author Asterios Raptis
 */
@UtilityClass
public final class WriteFileExtensions
{

	/** The LOGGER. */
	private static final Logger LOGGER = Logger.getLogger(WriteFileExtensions.class.getName());


	/**
	 * Writes the source file with the best performance to the destination file.
	 *
	 * @param srcfile
	 *            The source file.
	 * @param destFile
	 *            The destination file.
	 * @deprecated use instead the same name method in the class {@code WriteFileQuietlyExtensions}.
	 *             <br>
	 *             <br>
	 *             Note: will be removed in the next minor release.
	 */
	public static void readSourceFileAndWriteDestFile(final String srcfile, final String destFile)
	{
		try (FileInputStream fis = new FileInputStream(srcfile);
			FileOutputStream fos = new FileOutputStream(destFile);
			BufferedInputStream bis = new BufferedInputStream(fis);
			BufferedOutputStream bos = new BufferedOutputStream(fos);)
		{
			final int availableLength = bis.available();
			final byte[] totalBytes = new byte[availableLength];
			bis.read(totalBytes, 0, availableLength);
			bos.write(totalBytes, 0, availableLength);
		}
		catch (final FileNotFoundException e)
		{
			LOGGER.error("readSourceFileAndWriteDestFile failed...\n" + e.getMessage(), e);
		}
		catch (final IOException e)
		{
			LOGGER.error("readSourceFileAndWriteDestFile failed...\n" + e.getMessage(), e);
		}
	}

	/**
	 * Saves a byte array to the given file.
	 *
	 * @param data
	 *            The byte array to be saved.
	 * @param file
	 *            The file to save the byte array.
	 * @deprecated use instead the same name method in the class {@code WriteFileQuietlyExtensions}.
	 *             <br>
	 *             <br>
	 *             Note: will be removed in the next minor release.
	 */
	public static void storeByteArrayToFile(final byte[] data, final File file)
	{
		try (FileOutputStream fos = new FileOutputStream(file);
			BufferedOutputStream bos = new BufferedOutputStream(fos);)
		{
			bos.write(data);
			bos.flush();
		}
		catch (final FileNotFoundException e)
		{
			LOGGER.error("storeByteArrayToFile failed...\n" + e.getMessage(), e);
		}
		catch (final IOException e)
		{
			LOGGER.error("storeByteArrayToFile failed...\n" + e.getMessage(), e);
		}
	}

	/**
	 * The Method string2File(File, String) writes the String to the File.
	 *
	 * @param file
	 *            The File to write the String.
	 * @param string2write
	 *            The String to write into the File.
	 * @return The Method return true if the String was write successfull to the file otherwise
	 *         false.
	 * @deprecated use instead the same name method in the class {@code WriteFileQuietlyExtensions}.
	 *             <br>
	 *             <br>
	 *             Note: will be removed in the next minor release.
	 */
	public static boolean string2File(final File file, final String string2write)
	{
		return writeStringToFile(file, string2write, null);
	}

	/**
	 * The Method string2File(File, String) writes the String to the File.
	 *
	 * @param file
	 *            The File to write the String.
	 * @param string2write
	 *            The String to write into the File.
	 * @param encoding
	 *            the encoding
	 * @return The Method return true if the String was write successfull to the file otherwise
	 *         false.
	 * @deprecated use instead the same name method in the class {@code WriteFileQuietlyExtensions}.
	 *             <br>
	 *             <br>
	 *             Note: will be removed in the next minor release.
	 */
	public static boolean string2File(final File file, final String string2write,
		final String encoding)
	{
		return writeStringToFile(file, string2write, encoding);
	}

	/**
	 * The Method string2File() writes a String to the file.
	 *
	 * @param string2write
	 *            The String to write into the file.
	 * @param nameOfFile
	 *            The path to the file and name from the file from where we want to write the
	 *            String.
	 * @deprecated use instead the same name method in the class {@code WriteFileQuietlyExtensions}.
	 *             <br>
	 *             <br>
	 *             Note: will be removed in the next minor release.
	 */
	public static void string2File(final String string2write, final String nameOfFile)
	{
		try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(nameOfFile)))
		{
			bufferedWriter.write(string2write);
			bufferedWriter.flush();
		}
		catch (final IOException e)
		{
			LOGGER.error("string2File failed...\n" + e.getMessage(), e);
		}
	}

	/**
	 * Writes the given input stream to the output stream.
	 *
	 * @param inputStream
	 *            the input stream
	 * @param outputStream
	 *            the output stream
	 * @throws FileNotFoundException
	 *             the file not found exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void write(final InputStream inputStream, final OutputStream outputStream)
		throws FileNotFoundException, IOException
	{
		int counter;
		final byte byteArray[] = new byte[FileConst.BLOCKSIZE];
		while ((counter = inputStream.read(byteArray)) != -1)
		{
			outputStream.write(byteArray, 0, counter);
		}
	}

	/**
	 * The Method write2File() reads from an opened Reader and writes it to the opened Writer. <br>
	 * <br>
	 * Note: Stream will not be closed even if the flag is set to true!!!
	 *
	 * @param reader
	 *            The opened Reader.
	 * @param writer
	 *            The opened Writer.
	 * @param closeStream
	 *            not in use.
	 * @deprecated use instead the same name method without the closeStream flag.
	 */
	public static void write2File(final Reader reader, final Writer writer,
		final boolean closeStream)
	{
		write2File(reader, writer);
	}

	/**
	 * The Method write2File() reads from an opened Reader and writes it to the opened Writer.
	 *
	 * @param reader
	 *            The opened Reader.
	 * @param writer
	 *            The opened Writer.
	 * @deprecated use instead the same name method in the class {@code WriteFileQuietlyExtensions}.
	 *             <br>
	 *             <br>
	 *             Note: will be removed in the next minor release.
	 */
	public static void write2File(final Reader reader, final Writer writer)
	{
		int byt;
		try
		{
			while ((byt = reader.read()) != -1)
			{
				writer.write(byt);
			}
		}
		catch (final IOException e)
		{
			LOGGER.error("write2File failed...\n" + e.getMessage(), e);
		}
	}

	/**
	 * The Method write2File(String, String) copys a file from one filename to another.
	 *
	 * @param inputFile
	 *            The Name from the File to read and copy.
	 * @param outputFile
	 *            The Name from the File to write into.
	 * @deprecated use instead the same name method in the class {@code WriteFileQuietlyExtensions}.
	 *             <br>
	 *             <br>
	 *             Note: will be removed in the next minor release.
	 */
	public static void write2File(final String inputFile, final String outputFile)
	{
		try (FileInputStream fis = new FileInputStream(inputFile);
			FileOutputStream fos = new FileOutputStream(outputFile);
			BufferedInputStream bis = new BufferedInputStream(fis);
			BufferedOutputStream bos = new BufferedOutputStream(fos);)
		{
			StreamExtensions.writeInputStreamToOutputStream(bis, bos);
		}
		catch (final FileNotFoundException e)
		{
			LOGGER.error("write2File failed...\n" + e.getMessage(), e);
		}
		catch (final IOException e)
		{
			LOGGER.error("write2File failed...\n" + e.getMessage(), e);
		}
	}

	/**
	 * The Method write2File() writes the File into the PrintWriter.
	 *
	 * @param inputFile
	 *            The Name from the File to read and copy.
	 * @param writer
	 *            The PrintWriter to write into.
	 * @param closeWriter
	 *            If true then close the outputStream otherwise keep open.
	 * @deprecated use instead the same name method without the closeWriter flag. <br>
	 *             <br>
	 *             Note: will be removed in the next minor release.
	 */
	public static void write2File(final String inputFile, final Writer writer,
		final boolean closeWriter)
	{
		write2File(inputFile, writer);
	}

	/**
	 * The Method write2File() writes the File into the PrintWriter.
	 *
	 * @param inputFile
	 *            The Name from the File to read and copy.
	 * @param writer
	 *            The PrintWriter to write into.
	 * @deprecated use instead the same name method in the class {@code WriteFileQuietlyExtensions}.
	 *             <br>
	 *             <br>
	 *             Note: will be removed in the next minor release.
	 */
	public static void write2File(final String inputFile, final Writer writer)
	{
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFile));)
		{
			write2File(bufferedReader, writer);
		}
		catch (final FileNotFoundException e)
		{
			LOGGER.error("write2File failed...\n" + e.getMessage(), e);
		}
		catch (IOException e)
		{
			LOGGER.error("write2File failed...\n" + e.getMessage(), e);
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
	 * @deprecated use instead the same name method in the class {@code WriteFileQuietlyExtensions}.
	 *             <br>
	 *             <br>
	 *             Note: will be removed in the next minor release.
	 */
	public static void write2FileWithBuffer(final String inputFile, final String outputFile)
	{
		try (InputStream inputStream = StreamExtensions.getInputStream(new File(inputFile));
			OutputStream outputStream = StreamExtensions.getOutputStream(new File(outputFile));)
		{
			write(inputStream, outputStream);
		}
		catch (final FileNotFoundException e)
		{
			LOGGER.error("write2FileWithBuffer failed...\n" + e.getMessage(), e);
		}
		catch (final IOException e)
		{
			LOGGER.error("write2FileWithBuffer failed...\n" + e.getMessage(), e);
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
		try (FileOutputStream fos = new FileOutputStream(file);
			BufferedOutputStream bos = new BufferedOutputStream(fos))
		{
			bos.write(byteArray);
		}
		catch (final FileNotFoundException ex)
		{
			throw ex;
		}
		catch (final IOException ex)
		{
			throw ex;
		}
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
		final File file = new File(filename);
		writeByteArrayToFile(file, byteArray);
	}

	/**
	 * Writes the input from the collection into the file.
	 *
	 * @param collection
	 *            The collection to write to file.
	 * @param output
	 *            The output-file.
	 * @deprecated use instead the same name method in the class {@code WriteFileQuietlyExtensions}.
	 *             <br>
	 *             <br>
	 *             Note: will be removed in the next minor release.
	 */
	public static void writeLinesToFile(final Collection<String> collection, final File output)
	{
		final StringBuffer sb = new StringBuffer();
		for (final String element : collection)
		{
			sb.append(element);
			sb.append("\n");
		}
		string2File(output, sb.toString());
	}

	/**
	 * Writes the input from the collection into the file.
	 *
	 * @param collection
	 *            The collection to write to file.
	 * @param output
	 *            The output-file.
	 * @param encoding
	 *            the encoding
	 * @deprecated use instead the same name method in the class {@code WriteFileQuietlyExtensions}.
	 *             <br>
	 *             <br>
	 *             Note: will be removed in the next minor release.
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
		string2File(output, sb.toString(), encoding);
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
	 * @deprecated use instead the same name method in the class {@code WriteFileQuietlyExtensions}.
	 *             <br>
	 *             <br>
	 *             Note: will be removed in the next minor release.
	 */
	public static void writeLinesToFile(final File output, final List<String> input,
		final String encoding)
	{
		final String lineSeparator = System.getProperty("line.separator");
		try (FileOutputStream fos = new FileOutputStream(output);
			OutputStreamWriter osw = (null == encoding)
				? new OutputStreamWriter(fos)
				: new OutputStreamWriter(fos, encoding);
			PrintWriter out = new PrintWriter(osw);)
		{
			final int size = input.size();
			final StringBuffer sb = new StringBuffer();
			for (int i = 0; i < size; i++)
			{
				final String entry = input.get(i);
				sb.append(entry).append(lineSeparator);
			}
			out.write(sb.toString());
		}
		catch (final UnsupportedEncodingException e)
		{
			LOGGER.error("writeLinesToFile failed...\n" + e.getMessage(), e);
		}
		catch (final FileNotFoundException e)
		{
			LOGGER.error("writeLinesToFile failed...\n" + e.getMessage(), e);
		}
		catch (final IOException e)
		{
			LOGGER.error("writeLinesToFile failed...\n" + e.getMessage(), e);
		}
	}

	/**
	 * The Method writeProperties2File(String, Properties) writes the Properties to the file.
	 *
	 * @param filename
	 *            The filename from the file to write the properties.
	 * @param properties
	 *            The properties.
	 * @deprecated use instead the same name method in the class {@code WriteFileQuietlyExtensions}.
	 *             <br>
	 *             <br>
	 *             Note: will be removed in the next minor release.
	 */
	public static void writeProperties2File(final String filename, final Properties properties)
	{
		// Write properties to the file.
		try (FileOutputStream fos = new FileOutputStream(filename))
		{
			properties.store(fos, null);
		}
		catch (final IOException e)
		{
			LOGGER.error("writeProperties2File failed...\n" + e.getMessage(), e);
		}
	}

	/**
	 * The Method writeStringToFile(File, String, String) writes the String to the File.
	 *
	 * @param file
	 *            The File to write the String.
	 * @param string2write
	 *            The String to write into the File.
	 * @param encoding
	 *            The encoding from the file.
	 * @return The Method return true if the String was write successfull to the file otherwise
	 *         false.
	 * @deprecated use instead the same name method in the class {@code WriteFileQuietlyExtensions}.
	 *             <br>
	 *             <br>
	 *             Note: will be removed in the next minor release.
	 */
	public static boolean writeStringToFile(final File file, final String string2write,
		final String encoding)
	{
		boolean iswritten = true;
		try (FileOutputStream fos = new FileOutputStream(file);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			OutputStreamWriter osw = (null == encoding)
				? new OutputStreamWriter(bos)
				: new OutputStreamWriter(bos, encoding);
			PrintWriter printWriter = new PrintWriter(osw);)
		{
			printWriter.write(string2write);
		}
		catch (final FileNotFoundException e)
		{
			LOGGER.error("writeStringToFile failed...\n" + e.getMessage(), e);
			iswritten = false;
		}
		catch (final IOException e)
		{
			LOGGER.error("writeStringToFile failed...\n" + e.getMessage(), e);
		}
		return iswritten;
	}

}
