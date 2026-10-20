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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
		concatenateAllAndGet(textFiles, resultTextFile);
	}

	/**
	 * Concatenates the content and returns the result file.
	 *
	 * @param textFiles
	 *            the list of text files to concatenate
	 * @param resultTextFile
	 *            the result text file where the concatenated content will be stored
	 * @return the resultTextFile
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public static File concatenateAllAndGet(List<File> textFiles, File resultTextFile)
		throws IOException
	{
		Objects.requireNonNull(textFiles, "textFiles must not be null");
		Objects.requireNonNull(resultTextFile, "resultTextFile must not be null");
		StringBuilder text = new StringBuilder();
		for (int i = 0; i < textFiles.size(); ++i)
		{
			File textFile = textFiles.get(i);
			String content = ReadFileExtensions.fromFile(textFile);
			text.append(content);
		}
		StoreFileExtensions.toFile(resultTextFile, text.toString());
		return resultTextFile;
	}

	/**
	 * New convenience method: Concatenates files and generates a default result file in the
	 * system's temporary directory.
	 *
	 * @param textFiles
	 *            the list of text files to concatenate
	 * @return the generated result file
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public static File concatenateAll(List<File> textFiles) throws IOException
	{
		Objects.requireNonNull(textFiles, "textFiles must not be null");
		if (textFiles.isEmpty())
		{
			throw new IllegalArgumentException("textFiles must not be empty");
		}
		File resultTextFile = File.createTempFile("concatenated_", ".txt");
		return concatenateAllAndGet(textFiles, resultTextFile);
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
		modifyFileAndGet(inFilePath, charsetOfOutputFile, modifier);
	}

	/**
	 * Modifies the input file line by line and writes the modification in the same file. Returns
	 * the modified Path.
	 *
	 * @param inFilePath
	 *            the in file path
	 * @param charsetOfOutputFile
	 *            the charset of output file
	 * @param modifier
	 *            the modifier {@linkplain java.util.function.BiFunction}
	 * @return the modified inFilePath
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static Path modifyFileAndGet(Path inFilePath, Charset charsetOfOutputFile,
		FileChangeable modifier) throws IOException
	{
		Objects.requireNonNull(inFilePath, "inFilePath must not be null");
		Objects.requireNonNull(charsetOfOutputFile, "charsetOfOutputFile must not be null");
		Objects.requireNonNull(modifier, "modifier must not be null");

		File file = inFilePath.toFile();
		List<String> linesRead = ListFactory.newArrayList();

		try (BufferedReader bufferedReader = new BufferedReader(
			new InputStreamReader(new FileInputStream(file), charsetOfOutputFile)))
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
		return inFilePath;
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
		modifyFileAndGet(inFilePath, outFilePath, charsetOfOutputFile, modifier);
	}

	/**
	 * Modifies the input file line by line and writes the modification in the new output file.
	 * Returns the output Path.
	 *
	 * @param inFilePath
	 *            the in file path
	 * @param outFilePath
	 *            the out file path
	 * @param charsetOfOutputFile
	 *            the charset of output file
	 * @param modifier
	 *            the modifier {@linkplain java.util.function.BiFunction}
	 * @return the outFilePath
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static Path modifyFileAndGet(Path inFilePath, Path outFilePath,
		Charset charsetOfOutputFile, FileChangeable modifier) throws IOException
	{
		Objects.requireNonNull(inFilePath, "inFilePath must not be null");
		Objects.requireNonNull(outFilePath, "outFilePath must not be null");
		Objects.requireNonNull(charsetOfOutputFile, "charsetOfOutputFile must not be null");
		Objects.requireNonNull(modifier, "modifier must not be null");

		try (
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
				new FileInputStream(inFilePath.toFile()), charsetOfOutputFile));
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
		return outFilePath;
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
		modifyFile(inFilePath, outFilePath, StandardCharsets.UTF_8, modifier);
	}

	/**
	 * Concatenates all files with the given extension in the specified directory into a single
	 * result file.
	 *
	 * @param directory
	 *            the directory to search for files
	 * @param extension
	 *            the file extension to filter (e.g., ".md", ".txt")
	 * @param resultTextFile
	 *            the result file where the concatenated content will be stored
	 * @return the resultTextFile
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public static File concatenateFilesWithExtension(File directory, String extension,
		File resultTextFile) throws IOException
	{
		Objects.requireNonNull(directory, "directory must not be null");
		Objects.requireNonNull(extension, "extension must not be null");
		Objects.requireNonNull(resultTextFile, "resultTextFile must not be null");

		if (!directory.isDirectory())
		{
			throw new IllegalArgumentException("The given file is not a directory: " + directory);
		}

		File[] matchingFiles = directory.listFiles((dir, name) -> name.endsWith(extension));
		if (matchingFiles == null || matchingFiles.length == 0)
		{
			throw new IllegalArgumentException(
				"No files with extension '" + extension + "' found in directory: " + directory);
		}

		List<File> fileList = Arrays.asList(matchingFiles);
		Arrays.sort(matchingFiles); // Sortiere für konsistente Reihenfolge
		return concatenateAllAndGet(fileList, resultTextFile);
	}

	/**
	 * Concatenates all files with the given extension in the specified directory into a temporary
	 * result file.
	 *
	 * @param directory
	 *            the directory to search for files
	 * @param extension
	 *            the file extension to filter (e.g., ".md", ".txt")
	 * @return the generated result file
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public static File concatenateFilesWithExtension(File directory, String extension)
		throws IOException
	{
		Objects.requireNonNull(directory, "directory must not be null");
		Objects.requireNonNull(extension, "extension must not be null");

		if (!directory.isDirectory())
		{
			throw new IllegalArgumentException("The given file is not a directory: " + directory);
		}

		File[] matchingFiles = directory.listFiles((dir, name) -> name.endsWith(extension));
		if (matchingFiles == null || matchingFiles.length == 0)
		{
			throw new IllegalArgumentException(
				"No files with extension '" + extension + "' found in directory: " + directory);
		}

		List<File> fileList = Arrays.asList(matchingFiles);
		Arrays.sort(matchingFiles); // Sortiere für konsistente Reihenfolge
		File resultTextFile = File.createTempFile("concatenated_", extension);
		return concatenateAllAndGet(fileList, resultTextFile);
	}

	/**
	 * Concatenates all files with the given extension in the specified directory (recursively) into
	 * a single result file.
	 *
	 * @param directory
	 *            the root directory to search for files
	 * @param extension
	 *            the file extension to filter (e.g., ".md", ".txt")
	 * @param resultTextFile
	 *            the result file where the concatenated content will be stored
	 * @return the resultTextFile
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public static File concatenateFilesWithExtensionRecursive(File directory, String extension,
		File resultTextFile) throws IOException
	{
		Objects.requireNonNull(directory, "directory must not be null");
		Objects.requireNonNull(extension, "extension must not be null");
		Objects.requireNonNull(resultTextFile, "resultTextFile must not be null");

		if (!directory.isDirectory())
		{
			throw new IllegalArgumentException("The given file is not a directory: " + directory);
		}

		List<File> fileList = new ArrayList<>();
		collectFilesRecursive(directory, extension, fileList);

		if (fileList.isEmpty())
		{
			throw new IllegalArgumentException(
				"No files with extension '" + extension + "' found in directory: " + directory);
		}

		Collections.sort(fileList); // Sortiere für konsistente Reihenfolge
		return concatenateAllAndGet(fileList, resultTextFile);
	}

	/**
	 * Concatenates all files with the given extension in the specified directory (recursively) into
	 * a temporary result file.
	 *
	 * @param directory
	 *            the root directory to search for files
	 * @param extension
	 *            the file extension to filter (e.g., ".md", ".txt")
	 * @return the generated result file
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public static File concatenateFilesWithExtensionRecursive(File directory, String extension)
		throws IOException
	{
		Objects.requireNonNull(directory, "directory must not be null");
		Objects.requireNonNull(extension, "extension must not be null");

		if (!directory.isDirectory())
		{
			throw new IllegalArgumentException("The given file is not a directory: " + directory);
		}

		List<File> fileList = new ArrayList<>();
		collectFilesRecursive(directory, extension, fileList);

		if (fileList.isEmpty())
		{
			throw new IllegalArgumentException(
				"No files with extension '" + extension + "' found in directory: " + directory);
		}

		Collections.sort(fileList); // Sortiere für konsistente Reihenfolge
		File resultTextFile = File.createTempFile("concatenated_", extension);
		return concatenateAllAndGet(fileList, resultTextFile);
	}

	/**
	 * Helper method to recursively collect files with a specific extension.
	 */
	private static void collectFilesRecursive(File directory, String extension, List<File> fileList)
	{
		File[] files = directory.listFiles();
		if (files != null)
		{
			for (File file : files)
			{
				if (file.isDirectory())
				{
					collectFilesRecursive(file, extension, fileList);
				}
				else if (file.getName().endsWith(extension))
				{
					fileList.add(file);
				}
			}
		}
	}

}
