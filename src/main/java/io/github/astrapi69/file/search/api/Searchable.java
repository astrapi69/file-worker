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
package io.github.astrapi69.file.search.api;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Interface for searching capabilities within files and directories
 */
public interface Searchable
{

	/**
	 * Retrieves the root directory of the given file object
	 *
	 * @param file
	 *            the file to analyze
	 * @return the root directory from the given file
	 */
	default File getRootDirectory(File file)
	{
		File previous = file;
		File parent = previous.getParentFile();
		while (parent != null)
		{
			previous = parent;
			parent = parent.getParentFile();
		}
		return previous;
	}

	/**
	 * Checks if the parent directory contains the specified file by filename
	 *
	 * @param parent
	 *            the directory to search within
	 * @param search
	 *            the file to locate
	 * @return true if the file exists in the parent directory otherwise false
	 */
	default boolean containsFile(File parent, File search)
	{
		final String[] children = parent.list();
		if (children == null)
		{
			return false;
		}
		return Arrays.asList(children).contains(search.getName());
	}

	/**
	 * Checks if the parent directory contains a file matching the specified filename
	 *
	 * @param parent
	 *            the directory to search within
	 * @param pathname
	 *            the filename to search for
	 * @return true if the file exists in the parent directory otherwise false
	 */
	default boolean containsFile(File parent, String pathname)
	{
		final String[] allFiles = parent.list();
		if (allFiles == null)
		{
			return false;
		}
		return Arrays.asList(allFiles).contains(pathname);
	}

	/**
	 * Searches for files in a directory matching a specified predicate
	 *
	 * @param directory
	 *            the directory to search
	 * @param predicate
	 *            the condition to filter files
	 * @return a set of files that match the predicate
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	default Set<File> findFiles(File directory, Predicate<File> predicate) throws IOException
	{
		try (Stream<Path> pathStream = Files.list(directory.toPath()))
		{
			return pathStream.map(Path::toFile).filter(predicate).collect(Collectors.toSet());
		}
	}

	/**
	 * Recursively searches for files in a directory matching a specified predicate
	 *
	 * @param directory
	 *            the directory to search
	 * @param predicate
	 *            the condition to filter files
	 * @return a set of files that match the predicate
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	default Set<File> findFilesRecursive(File directory, Predicate<File> predicate)
		throws IOException
	{
		try (Stream<Path> pathStream = Files.walk(directory.toPath()))
		{
			return pathStream.map(Path::toFile).filter(predicate).collect(Collectors.toSet());
		}
	}

	/**
	 * Counts all files in a directory recursively, optionally including directories in the count
	 *
	 * @param dir
	 *            the directory to count files in
	 * @param length
	 *            the starting count
	 * @param includeDirectories
	 *            flag to include directories in the count if true
	 * @return the total number of files and directories
	 */
	default long countAllFilesInDirectory(File dir, long length, boolean includeDirectories)
	{
		final File[] children = dir.listFiles();
		if (children == null || children.length < 1)
		{
			return length;
		}
		for (File child : children)
		{
			if (child.isDirectory())
			{
				if (includeDirectories)
				{
					length++;
				}
				length = countAllFilesInDirectory(child, length, includeDirectories);
			}
			else
			{
				length++;
			}
		}
		return length;
	}

	/**
	 * Finds files within a directory matching a specified search pattern (regex)
	 *
	 * @param dir
	 *            the directory to search
	 * @param fileSearchPattern
	 *            the regex pattern to match filenames against
	 * @return a list of matching files
	 */
	default List<File> findAllFiles(File dir, String fileSearchPattern)
	{
		final List<File> foundFiles = new ArrayList<>();
		final File[] children = dir.listFiles();
		if (children == null || children.length < 1)
		{
			return foundFiles;
		}
		for (File child : children)
		{
			if (child.isDirectory())
			{
				foundFiles.addAll(findAllFiles(child, fileSearchPattern));
			}
			else if (child.getName().matches(fileSearchPattern))
			{
				foundFiles.add(child);
			}
		}
		return foundFiles;
	}

	/**
	 * Finds all files in a directory with the specified extensions
	 *
	 * @param start
	 *            the starting directory
	 * @param extensions
	 *            array of file extensions to search for
	 * @return a list of files that match the specified extensions
	 */
	default List<File> findFiles(String start, String[] extensions)
	{
		final List<File> files = new ArrayList<>();
		final File startDir = new File(start);
		if (!startDir.isDirectory())
		{
			return files;
		}
		final File[] children = startDir.listFiles();
		if (children != null)
		{
			for (File child : children)
			{
				if (child.isDirectory())
				{
					files.addAll(findFiles(child.getAbsolutePath(), extensions));
				}
				else if (Arrays.stream(extensions).anyMatch(ext -> child.getName().endsWith(ext)))
				{
					files.add(child);
				}
			}
		}
		return files;
	}

	/**
	 * Retrieves all files from a directory
	 *
	 * @param dir
	 *            the directory to retrieve files from
	 * @return a list of files in the directory
	 */
	default List<File> getAllFilesFromDir(File dir)
	{
		final List<File> files = new ArrayList<>();
		final File[] children = dir.listFiles();
		if (children != null)
		{
			for (File child : children)
			{
				if (!child.isDirectory())
				{
					files.add(child);
				}
			}
		}
		return files;
	}

	/**
	 * Retrieves all files from a directory recursively
	 *
	 * @param dir
	 *            the directory to retrieve files from
	 * @return a list of all files in the directory and its subdirectories
	 */
	default List<File> getAllFilesFromDirRecursive(File dir)
	{
		return findAllFiles(dir, ".*");
	}


	/**
	 * Finds all files in the specified directory and subdirectories that match the given prefix and
	 * extension
	 *
	 * @param dir
	 *            the directory in which to search for files
	 * @param prefix
	 *            the prefix that filenames should start with
	 * @param extension
	 *            the file extension that filenames should have
	 * @return a list of files that match the specified prefix and extension
	 */
	default List<File> findFilesWithPrefixAndExtensionRecursive(File dir, String prefix,
		String extension)
	{
		List<File> matchingFiles = new ArrayList<>();
		File[] files = dir.listFiles();

		if (files != null)
		{
			for (File file : files)
			{
				if (file.isDirectory())
				{
					matchingFiles
						.addAll(findFilesWithPrefixAndExtensionRecursive(file, prefix, extension));
				}
				else if (file.getName().startsWith(prefix)
					&& file.getName().endsWith("." + extension))
				{
					matchingFiles.add(file);
				}
			}
		}
		return matchingFiles;
	}
}
