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
package de.alpharogroup.file.search;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import de.alpharogroup.file.filter.MultiplyExtensionsFileFilter;
import de.alpharogroup.regex.REUtils;

/**
 * The Class FileSearchUtils provides methods for searching in directories.
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public final class FileSearchUtils
{
	/**
	 * Checks if the given file contains only in the parent file, not in the subdirectories.
	 *
	 * @param parent
	 *            The parent directory to search.
	 * @param search
	 *            The file to search.
	 * @return 's true if the file exists in the parent directory otherwise false.
	 */
	public static boolean containsFile(final File parent, final File search)
	{
		boolean exists = false;
		final String[] children = parent.list();
		if (children == null)
		{
			return false;
		}
		final List<String> fileList = Arrays.asList(children);
		if (fileList.contains(search.getName()))
		{
			exists = true;
		}
		return exists;
	}

	/**
	 * Checks if the given file contains in the parent file.
	 *
	 * @param fileToSearch
	 *            The parent directory to search.
	 * @param pathname
	 *            The file to search.
	 * @return 's true if the file exists in the parent directory otherwise false.
	 */
	public static boolean containsFile(final File fileToSearch, final String pathname)
	{
		final String[] allFiles = fileToSearch.list();
		if (allFiles == null)
		{
			return false;
		}
		final List<String> list = Arrays.asList(allFiles);
		return list.contains(pathname);
	}

	/**
	 * Checks if the given file contains only in the parent file recursively.
	 *
	 * @param parent
	 *            The parent directory to search.
	 * @param search
	 *            The file to search.
	 * @return 's true if the file exists in the parent directory otherwise false.
	 */
	public static boolean containsFileRecursive(final File parent, final File search)
	{
		final File toSearch = search.getAbsoluteFile();
		boolean exists = false;
		final File[] children = parent.getAbsoluteFile().listFiles();
		if (children == null)
		{
			return false;
		}
		final List<File> fileList = Arrays.asList(children);
		for (final File currentFile : fileList)
		{
			if (currentFile.isDirectory())
			{
				exists = FileSearchUtils.containsFileRecursive(currentFile, toSearch);
				if (exists)
				{
					return true;
				}
			}
			if (fileList.contains(toSearch))
			{
				return true;
			}
		}
		return exists;
	}

	/**
	 * Counts all the files in a directory recursively. This includes files in the subdirectories.
	 *
	 * @param dir
	 *            the directory.
	 * @param length
	 *            the current length. By start is this 0.
	 * @param includeDirectories
	 *            If this is true then the directories are in the count too.
	 * @return the total number of files.
	 */
	public static long countAllFilesInDirectory(final File dir, long length,
		final boolean includeDirectories)
	{
		// Get all files
		final File[] children = dir.getAbsoluteFile().listFiles();
		if (children == null || children.length < 1)
		{
			return length;
		}
		for (final File element : children)
		{
			// if the entry is a directory
			if (element.isDirectory())
			{ // then
				// if directories shell be include
				if (includeDirectories)
				{ // then
					// increment the length
					length++;
				}
				// find recursively in the directory the files.
				length = countAllFilesInDirectory(element, length, includeDirectories);
			}
			else
			{
				// increment length...
				length++;
			}
		}
		return length;
	}

	/**
	 * Finds all files that match the search pattern. The search is not recursively.
	 *
	 * @param dir
	 *            The directory to search.
	 * @param fileSearchPattern
	 *            The regex file search pattern.
	 * @return A List with all files that matches the search pattern.
	 */
	public static List<File> findAllFiles(final File dir, final String fileSearchPattern)
	{
		final List<File> foundFiles = new ArrayList<>();
		// Get all files
		final File[] children = dir.getAbsoluteFile().listFiles();
		if (children == null || children.length < 1)
		{
			return foundFiles;
		}
		for (final File element : children)
		{
			// if the entry is a directory
			if (element.isDirectory())
			{ // then
				// find recursively in the directory and put it in a List.
				final List<File> foundedFiles = findAllFiles(element, fileSearchPattern);
				// Put the founded files in the main List.
				foundFiles.addAll(foundedFiles);
			}
			else
			{
				// entry is a file
				final String filename = element.getName();
				if (filename.matches(fileSearchPattern))
				{
					foundFiles.add(element.getAbsoluteFile());
				}
			}
		}
		return foundFiles;
	}

	/**
	 * Finds all files that match the search pattern. The search is not recursively.
	 *
	 * @param dir
	 *            The directory to search.
	 * @param filenameToSearch
	 *            The search pattern. Allowed wildcards are "*" and "?".
	 * @return A List with all files that matches the search pattern.
	 */
	public static List<File> findFiles(final File dir, final String filenameToSearch)
	{
		final List<File> foundedFileList = new ArrayList<>();
		final String regex = REUtils.replaceWildcardsWithRE(filenameToSearch);
		final String[] children = dir.list();
		for (final String filename : children)
		{
			if (filename.matches(regex))
			{
				final File foundedFile = new File(filename);
				foundedFileList.add(foundedFile);
			}
		}
		return foundedFileList;
	}

	/**
	 * Searches for files with the given extensions and adds them to a Vector.
	 *
	 * @param start
	 *            The path to the file.
	 * @param extensions
	 *            The extensions to find.
	 * @return Returns a Vector all founded files.
	 */
	public static List<File> findFiles(final String start, final String[] extensions)
	{
		final List<File> files = new ArrayList<>();
		final Stack<File> dirs = new Stack<>();
		final File startdir = new File(start);
		if (startdir.isDirectory())
		{
			dirs.push(new File(start));
		}
		while (dirs.size() > 0)
		{
			final File dirFiles = dirs.pop();
			final String s[] = dirFiles.list();
			if (s != null)
			{
				for (final String element : s)
				{
					final File file = new File(dirFiles.getAbsolutePath() + File.separator
						+ element);
					if (file.isDirectory())
					{
						dirs.push(file);
					}
					else if (match(element, extensions))
					{
						files.add(file);
					}
				}
			}
		}
		return files;
	}

	/**
	 * Finds all files that match the search pattern. The search is recursively.
	 *
	 * @param dir
	 *            The directory to search.
	 * @param filenameToSearch
	 *            The search pattern. Allowed wildcards are "*" and "?".
	 * @return A List with all files that matches the search pattern.
	 */
	public static List<File> findFilesRecursive(final File dir, final String filenameToSearch)
	{
		final List<File> foundedFileList = new ArrayList<>();
		final String regex = REUtils.replaceWildcardsWithRE(filenameToSearch);
		// Get all files
		final File[] children = dir.getAbsoluteFile().listFiles();
		if (children == null || children.length < 1)
		{
			return foundedFileList;
		}
		for (final File element : children)
		{
			// if the entry is a directory
			if (element.isDirectory())
			{ // then
				// find recursively in the directory and put it in a List.
				final List<File> foundedFiles = findFilesRecursive(element, filenameToSearch);
				// Put the founded files in the main List.
				foundedFileList.addAll(foundedFiles);
			}
			else
			{
				// entry is a file
				final String filename = element.getName();
				if (filename.matches(regex))
				{
					foundedFileList.add(element.getAbsoluteFile());
				}
			}
		}
		return foundedFileList;
	}

	/**
	 * Finds all files that match the given extension. The search is recursively.
	 *
	 * @param dir
	 *            The directory to search.
	 * @param extension
	 *            The extensions to search.
	 * @return A List with all files that matches the search pattern.
	 */
	public static List<File> findFilesWithFilter(final File dir, final String... extension)
	{
		final List<File> foundedFileList = new ArrayList<>();
		final File[] children = dir.listFiles(new MultiplyExtensionsFileFilter(true, extension));
		for (final File element : children)
		{
			// if the entry is a directory
			if (element.isDirectory())
			{ // then
				// find recursively in the directory and put it in a List.
				// Put the founded files in the main List.
				foundedFileList.addAll(findFilesWithFilter(element, extension));
			}
			else
			{
				foundedFileList.add(element.getAbsoluteFile());
			}
		}
		return foundedFileList;
	}

	/**
	 * Gets the all files from directory.
	 *
	 * @param dir
	 *            the dir
	 * @return A list with all files from the given directory.
	 */
	public static List<File> getAllFilesFromDir(final File dir)
	{
		final List<File> foundedFileList = new ArrayList<>();
		final File[] children = dir.getAbsoluteFile().listFiles();
		if (children == null || children.length < 1)
		{
			return foundedFileList;
		}
		for (final File child : children)
		{
			// if the entry is not a directory
			if (!child.isDirectory())
			{
				// then
				// entry is a file
				foundedFileList.add(child.getAbsoluteFile());
			}
		}
		return foundedFileList;
	}


	/**
	 * Gets all the files from directory recursive.
	 *
	 * @param dir
	 *            the directory
	 *
	 * @return the all files from dir recursive
	 */
	public static List<File> getAllFilesFromDirRecursive(final File dir)
	{
		return findFilesRecursive(dir, "*");
	}


	/**
	 * Gets the file length from the given file in Kilobytes.
	 *
	 * @param dir
	 *            the dir
	 *
	 * @return Returns the file length from the given file in Kilobytes.
	 */
	public static long getFileLengthInKilobytes(final File dir)
	{
		final long fileLength = dir.getTotalSpace();
		return fileLength / 1024;
	}

	/**
	 * Gets the file length from the given file in Megabytes.
	 *
	 * @param dir
	 *            the directory
	 *
	 * @return Returns the file length from the given file in Megabytes.
	 */
	public static long getFileLengthInMegabytes(final File dir)
	{
		return getFileLengthInKilobytes(dir) / 1024;
	}


	/**
	 * Gets a regex search file pattern that can be used for searching files with a Matcher.
	 *
	 * @param fileExtensions
	 *            The file extensions that shell exist in the search pattern.
	 * @return a regex search file pattern that can be used for searching files with a Matcher.
	 */
	public static String getSearchFilePattern(final String... fileExtensions)
	{
		if (fileExtensions.length == 0)
		{
			return "";
		}
		final String searchFilePatternPrefix = "([^\\s]+(\\.(?i)(";
		final String searchFilePatternSuffix = "))$)";
		final StringBuilder sb = new StringBuilder();
		int count = 1;
		for (final String fileExtension : fileExtensions)
		{
			if (count < fileExtensions.length)
			{
				sb.append(fileExtension).append("|");
			}
			else
			{
				sb.append(fileExtension);
			}
			count++;
		}
		return searchFilePatternPrefix + sb.toString().trim() + searchFilePatternSuffix;
	}

	/**
	 * List the directories from the given file(directory).
	 *
	 * @param dir
	 *            the directory.
	 * @return the list
	 */
	public static List<File> listDirs(final File dir)
	{
		final List<File> foundedDirs = new ArrayList<>();
		final File[] fileArray = dir.listFiles();
		for (final File element : fileArray)
		{
			if (element.isDirectory())
			{
				foundedDirs.add(element);
			}
		}
		return foundedDirs;

	}

	/**
	 * Checks the given String matches the given suffixes.
	 *
	 * @param stringToMatch
	 *            The string to compare.
	 * @param suffixes
	 *            An array with suffixes.
	 * @return Returns true if matches otherwise false.
	 */
	public static boolean match(final String stringToMatch, final String suffixes[])
	{
		for (final String suffix : suffixes)
		{
			final int suffixesLength = suffix.length();
			final int stringToMatchLength = stringToMatch.length();
			final int result = stringToMatchLength - suffixesLength;
			final String extensionToMatch = stringToMatch.substring(result, stringToMatchLength);
			final boolean equals = extensionToMatch.equalsIgnoreCase(suffix);
			if (stringToMatchLength >= suffixesLength && equals)
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Private constructor.
	 */
	private FileSearchUtils()
	{
		super();
	}

}
