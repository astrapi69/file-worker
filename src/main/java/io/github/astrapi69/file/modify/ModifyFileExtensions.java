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
package io.github.astrapi69.file.modify;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

import io.github.astrapi69.collection.list.ListFactory;
import io.github.astrapi69.file.modify.api.FileChangeable;
import io.github.astrapi69.file.read.ReadFileExtensions;
import io.github.astrapi69.file.write.StoreFileExtensions;

/**
 * The class {@link ModifyFileExtensions} provides methods for modifying files
 */
public final class ModifyFileExtensions
{

	/**
	 * Private constructor to prevent instantiation
	 */
	private ModifyFileExtensions()
	{
	}

	/**
	 * Concatenates the content of the given list of text files into a single result text file
	 *
	 * @param textFiles
	 *            the list of text files to concatenate
	 * @param resultTextFile
	 *            the result text file where the concatenated content will be stored
	 * @throws IOException
	 *             if an I/O error occurs during reading from text files or writing to the result
	 *             text file
	 */
	public static void concatenateAll(List<File> textFiles, File resultTextFile) throws IOException
	{
		StringBuilder text = new StringBuilder();
		for (int i = 0; i < textFiles.size(); ++i)
		{
			File textFile = textFiles.get(i);
			String content = ReadFileExtensions.fromFile(textFile);
			text.append(content);
		}
		StoreFileExtensions.toFile(resultTextFile, text.toString());
	}

	/**
	 * Modifies the input file line by line and writes the modification in the same file
	 *
	 * @param inFilePath
	 *            the in file path
	 * @param charsetOfOutputFile
	 *            the charset of output file
	 * @param modifier
	 *            the modifier {@linkplain BiFunction}
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void modifyFile(Path inFilePath, Charset charsetOfOutputFile,
		FileChangeable modifier) throws IOException
	{
		Objects.requireNonNull(inFilePath);
		Objects.requireNonNull(charsetOfOutputFile);
		Objects.requireNonNull(modifier);
		File file = inFilePath.toFile();
		List<String> linesRead = ListFactory.newArrayList();
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file)))
		{
			String currentLine;
			while ((currentLine = bufferedReader.readLine()) != null)
			{
				linesRead.add(currentLine);
			}
		}

		try (Writer writer = new BufferedWriter(
			new OutputStreamWriter(new FileOutputStream(file), charsetOfOutputFile)))
		{
			int counter = 0;
			for (String currentLine : linesRead)
			{
				String modified = modifier.apply(counter, currentLine);
				if (modified != null)
				{
					writer.write(modified);
				}
				counter++;
			}
		}
	}

	/**
	 * Modifies the input file line by line and writes the modification in the same file
	 *
	 * @param inFilePath
	 *            the in file path
	 * @param modifier
	 *            the modifier {@linkplain BiFunction}
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void modifyFile(Path inFilePath, FileChangeable modifier) throws IOException
	{
		Objects.requireNonNull(inFilePath);
		Objects.requireNonNull(modifier);
		modifyFile(inFilePath, StandardCharsets.UTF_8, modifier);
	}

	/**
	 * Modifies the input file line by line and writes the modification in the new output file.
	 *
	 * @param inFilePath
	 *            the in file path
	 * @param outFilePath
	 *            the out file path
	 * @param charsetOfOutputFile
	 *            the charset of output file
	 * @param modifier
	 *            the modifier {@linkplain BiFunction}
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void modifyFile(Path inFilePath, Path outFilePath, Charset charsetOfOutputFile,
		FileChangeable modifier) throws IOException
	{
		Objects.requireNonNull(inFilePath);
		Objects.requireNonNull(outFilePath);
		Objects.requireNonNull(charsetOfOutputFile);
		Objects.requireNonNull(modifier);
		try (
			BufferedReader bufferedReader = new BufferedReader(new FileReader(inFilePath.toFile()));
			Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(outFilePath.toFile()), charsetOfOutputFile)))
		{
			String currentLine;
			int counter = 0;
			while ((currentLine = bufferedReader.readLine()) != null)
			{
				String modified = modifier.apply(counter, currentLine);
				if (modified != null)
				{
					writer.write(modified);
				}
				counter++;
			}
		}
	}

	/**
	 * Modifies the input file line by line and writes the modification in the new output file
	 *
	 * @param inFilePath
	 *            the in file path
	 * @param outFilePath
	 *            the out file path
	 * @param modifier
	 *            the modifier {@linkplain BiFunction}
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void modifyFile(Path inFilePath, Path outFilePath, FileChangeable modifier)
		throws IOException
	{
		Objects.requireNonNull(inFilePath);
		Objects.requireNonNull(outFilePath);
		Objects.requireNonNull(modifier);
		modifyFile(inFilePath, outFilePath, StandardCharsets.UTF_8, modifier);
	}
}
