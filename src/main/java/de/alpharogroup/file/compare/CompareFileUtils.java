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
package de.alpharogroup.file.compare;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import de.alpharogroup.file.FileUtils;
import de.alpharogroup.file.checksum.Algorithm;
import de.alpharogroup.file.checksum.ChecksumUtils;
import de.alpharogroup.file.compare.interfaces.IFileCompareResultBean;
import de.alpharogroup.file.compare.interfaces.IFileContentResultBean;
import de.alpharogroup.file.search.FileSearchUtils;
import de.alpharogroup.io.StreamUtils;

/**
 * The Class CompareFileUtils helps you to compare files.
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public final class CompareFileUtils
{


	/**
	 * Sets the flags in the FileCompareResultBean object according to the given boolean flag what
	 * to ignore.
	 *
	 * @param fileCompareResultBean
	 *            The FileCompareResultBean.
	 * @param ignoreAbsolutePathEquality
	 *            If this is true then the absolute path equality will be ignored.
	 * @param ignoreExtensionEquality
	 *            If this is true then the extension equality will be ignored.
	 * @param ignoreLengthEquality
	 *            If this is true then the length equality will be ignored.
	 * @param ignoreLastModified
	 *            If this is true then the last modified equality will be ignored.
	 * @param ignoreNameEquality
	 *            If this is true then the name equality will be ignored.
	 */
	public static void compare(final IFileCompareResultBean fileCompareResultBean,
		final boolean ignoreAbsolutePathEquality, final boolean ignoreExtensionEquality,
		final boolean ignoreLengthEquality, final boolean ignoreLastModified,
		final boolean ignoreNameEquality)
	{
		final File source = fileCompareResultBean.getSourceFile();
		final File compare = fileCompareResultBean.getFileToCompare();
		if (!ignoreAbsolutePathEquality)
		{
			// check the absolute path from the files...
			final String sourceAbsolutePath = source.getAbsolutePath();
			final String compareAbsolutePath = compare.getAbsolutePath();
			final boolean absolutePathEquality = sourceAbsolutePath.equals(compareAbsolutePath);
			fileCompareResultBean.setAbsolutePathEquality(absolutePathEquality);
		}
		else
		{
			fileCompareResultBean.setAbsolutePathEquality(true);
		}
		if (!ignoreExtensionEquality)
		{
			// check the file extension...
			final String sourceFileExtension = FileUtils.getFilenameSuffix(source);
			final String compareFileExtension = FileUtils.getFilenameSuffix(compare);

			final boolean extensionEquality = compareFileExtension
				.equalsIgnoreCase(sourceFileExtension);
			fileCompareResultBean.setFileExtensionEquality(extensionEquality);
		}
		else
		{
			fileCompareResultBean.setFileExtensionEquality(true);
		}

		if (!ignoreLengthEquality)
		{
			// check the file length...
			final boolean length = source.length() == compare.length();
			fileCompareResultBean.setLengthEquality(length);
		}
		else
		{
			fileCompareResultBean.setLengthEquality(true);
		}

		if (!ignoreLastModified)
		{
			// check the last modified date...
			final boolean lastModified = source.lastModified() == compare.lastModified();
			fileCompareResultBean.setLastModifiedEquality(lastModified);
		}
		else
		{
			fileCompareResultBean.setLastModifiedEquality(true);
		}

		if (!ignoreNameEquality)
		{
			// check the filename...
			final String sourceFilename = FileUtils.getFilenameWithoutExtension(source);
			final String compareFilename = FileUtils.getFilenameWithoutExtension(compare);
			final boolean nameEquality = compareFilename.equalsIgnoreCase(sourceFilename);
			fileCompareResultBean.setNameEquality(nameEquality);
		}
		else
		{
			fileCompareResultBean.setNameEquality(true);
		}
	}

	/**
	 * Sets the flags in the FileContentResultBean object according to the given boolean flag what
	 * to ignore.
	 *
	 * @param fileContentResultBean
	 *            The FileContentResultBean.
	 * @param ignoreAbsolutePathEquality
	 *            If this is true then the absolute path equality will be ignored.
	 * @param ignoreExtensionEquality
	 *            If this is true then the extension equality will be ignored.
	 * @param ignoreLengthEquality
	 *            If this is true then the length equality will be ignored.
	 * @param ignoreLastModified
	 *            If this is true then the last modified equality will be ignored.
	 * @param ignoreNameEquality
	 *            If this is true then the name equality will be ignored.
	 * @param ignoreContentEquality
	 *            If this is true then the content equality will be ignored.
	 */
	public static void compare(final IFileContentResultBean fileContentResultBean,
		final boolean ignoreAbsolutePathEquality, final boolean ignoreExtensionEquality,
		final boolean ignoreLengthEquality, final boolean ignoreLastModified,
		final boolean ignoreNameEquality, final boolean ignoreContentEquality)
	{
		compare(fileContentResultBean, ignoreAbsolutePathEquality, ignoreExtensionEquality,
			ignoreLengthEquality, ignoreLastModified, ignoreNameEquality);
		final File source = fileContentResultBean.getSourceFile();
		final File compare = fileContentResultBean.getFileToCompare();
		if (!ignoreContentEquality)
		{
			boolean contentEquality;
			try
			{
				final String sourceChecksum = ChecksumUtils.getChecksum(source,
					Algorithm.SHA_512.getAlgorithm());
				final String compareChecksum = ChecksumUtils.getChecksum(compare,
					Algorithm.SHA_512.getAlgorithm());
				contentEquality = sourceChecksum.equals(compareChecksum);
				fileContentResultBean.setContentEquality(contentEquality);
			}
			catch (final NoSuchAlgorithmException e)
			{
				// if the algorithm is not supported check it with CRC32.
				contentEquality = ChecksumUtils.getCheckSumCRC32(source) == ChecksumUtils
					.getCheckSumCRC32(compare);
				fileContentResultBean.setContentEquality(contentEquality);
			}
		}
		else
		{
			fileContentResultBean.setContentEquality(true);
		}
	}

	/**
	 * Compare file content for every single byte.
	 *
	 * @param sourceFile
	 *            the source file
	 * @param fileToCompare
	 *            the file to compare
	 *
	 * @return the i file content result bean
	 */
	public static IFileContentResultBean compareFileContentByBytes(final File sourceFile,
		final File fileToCompare)
	{
		final IFileContentResultBean fileContentResultBean = new FileContentResultBean(sourceFile,
			fileToCompare);
		completeCompare(fileContentResultBean);
		final boolean simpleEquality = validateEquality(fileContentResultBean);
		boolean contentEquality = true;
		// Compare the content...
		if (simpleEquality)
		{
			InputStream sourceReader = null;
			InputStream compareReader = null;
			try
			{
				sourceReader = StreamUtils.getInputStream(sourceFile);
				compareReader = StreamUtils.getInputStream(fileToCompare);

				final byte[] source = StreamUtils.getByteArray(sourceReader);
				final byte[] compare = StreamUtils.getByteArray(compareReader);

				for (int i = 0; 0 < source.length; i++)
				{
					if (source[i] != compare[i])
					{
						contentEquality = false;
						break;
					}
				}
			}
			catch (final FileNotFoundException e)
			{
				contentEquality = false;
			}
			catch (final IOException e)
			{
				contentEquality = false;
			}
			finally
			{
				StreamUtils.closeInputStream(sourceReader);
				StreamUtils.closeInputStream(compareReader);
			}
		}
		fileContentResultBean.setContentEquality(contentEquality);
		return fileContentResultBean;
	}

	/**
	 * Compare file content by lines.
	 *
	 * @param sourceFile
	 *            the source file
	 * @param fileToCompare
	 *            the file to compare
	 *
	 * @return the i file content result bean
	 */
	public static IFileContentResultBean compareFileContentByLines(final File sourceFile,
		final File fileToCompare)
	{
		final IFileContentResultBean fileContentResultBean = new FileContentResultBean(sourceFile,
			fileToCompare);
		completeCompare(fileContentResultBean);
		final boolean simpleEquality = validateEquality(fileContentResultBean);
		boolean contentEquality = true;
		// Compare the content...
		if (simpleEquality)
		{
			BufferedReader sourceReader = null;
			BufferedReader compareReader = null;
			try
			{
				sourceReader = (BufferedReader)StreamUtils.getReader(sourceFile);
				compareReader = (BufferedReader)StreamUtils.getReader(fileToCompare);
				String sourceLine;
				String compareLine;

				while ((sourceLine = sourceReader.readLine()) != null)
				{
					compareLine = compareReader.readLine();
					if (compareLine == null || !sourceLine.equals(compareLine))
					{
						contentEquality = false;
						break;
					}
				}
			}
			catch (final FileNotFoundException e)
			{
				contentEquality = false;
			}
			catch (final IOException e)
			{
				contentEquality = false;
			}
			finally
			{
				StreamUtils.closeReader(sourceReader);
				StreamUtils.closeReader(compareReader);
			}
		}
		fileContentResultBean.setContentEquality(contentEquality);
		return fileContentResultBean;
	}

	/**
	 * Compare files.
	 *
	 * @param sourceFile
	 *            the source file
	 * @param fileToCompare
	 *            the file to compare
	 *
	 * @return the i file content result bean
	 */
	public static IFileContentResultBean compareFiles(final File sourceFile,
		final File fileToCompare)
	{
		return compareFiles(sourceFile, fileToCompare, true, false, false, true, false, true);
	}

	/**
	 * Compare files.
	 *
	 * @param source
	 *            the source
	 * @param compare
	 *            the compare
	 * @param content
	 *            the content
	 *
	 * @return true, if successful
	 */
	public static boolean compareFiles(final File source, final File compare, final boolean content)
	{
		boolean equal = true;
		// boolean sameLastModified = false;
		// boolean sameAbsolutePath = false;
		boolean sameFilename = false;
		// // Check if the files have the same absolute path.
		// int s = source.compareTo(compare);
		// sameAbsolutePath = s == 0;
		//
		// // Check if the files have same names.
		// sameFilename = source.getName().equals(compare.getName());
		//
		// // Check if the Files have the same lastModified date.
		// sameLastModified = source.lastModified() == compare.lastModified();

		// Check if source file exists.
		if (!source.exists())
		{
			// Check if compare file exists.
			if (!compare.exists())
			{
				// // Check if the files have same names.
				sameFilename = source.getName().equals(compare.getName());
				if (sameFilename)
				{
					return true;
				}
			}
			return false;
		}
		else
		{
			if (!compare.exists())
			{
				return false;
			}
		}

		// Check if the Files have the same lastModified date.
		if (source.length() != compare.length())
		{
			return false;
		}
		// Compare the content?
		if (content)
		{
			BufferedReader sourceReader = null;
			BufferedReader compareReader = null;
			try
			{
				sourceReader = (BufferedReader)StreamUtils.getReader(source);
				compareReader = (BufferedReader)StreamUtils.getReader(compare);
				String sourceLine;
				String compareLine;

				while ((sourceLine = sourceReader.readLine()) != null)
				{
					compareLine = compareReader.readLine();
					if (compareLine == null || !sourceLine.equals(compareLine))
					{
						equal = false;
						break;
					}
				}
			}
			catch (final FileNotFoundException e)
			{
				equal = false;
			}
			catch (final IOException e)
			{
				equal = false;
			}
			finally
			{
				StreamUtils.closeReader(sourceReader);
				StreamUtils.closeReader(compareReader);
			}
		}
		return equal;
	}

	/**
	 * Compare files.
	 *
	 * @param sourceFile
	 *            the source file
	 * @param fileToCompare
	 *            the file to compare
	 * @param ignoreAbsolutePathEquality
	 *            If this is true then the absolute path equality will be ignored.
	 * @param ignoreExtensionEquality
	 *            the ignore extension equality
	 * @param ignoreLengthEquality
	 *            the ignore length equality
	 * @param ignoreLastModified
	 *            the ignore last modified
	 * @param ignoreNameEquality
	 *            the ignore name equality
	 *
	 * @return the i file compare result bean
	 */
	public static IFileCompareResultBean compareFiles(final File sourceFile,
		final File fileToCompare, final boolean ignoreAbsolutePathEquality,
		final boolean ignoreExtensionEquality, final boolean ignoreLengthEquality,
		final boolean ignoreLastModified, final boolean ignoreNameEquality)
	{
		final IFileCompareResultBean fileCompareResultBean = new FileCompareResultBean(sourceFile,
			fileToCompare);
		compare(fileCompareResultBean, ignoreAbsolutePathEquality, ignoreExtensionEquality,
			ignoreLengthEquality, ignoreLastModified, ignoreNameEquality);
		return fileCompareResultBean;
	}

	/**
	 * Compare files.
	 *
	 * @param sourceFile
	 *            the source file
	 * @param fileToCompare
	 *            the file to compare
	 * @param ignoreAbsolutePathEquality
	 *            If this is true then the absolute path equality will be ignored.
	 * @param ignoreExtensionEquality
	 *            the ignore extension equality
	 * @param ignoreLengthEquality
	 *            the ignore length equality
	 * @param ignoreLastModified
	 *            the ignore last modified
	 * @param ignoreNameEquality
	 *            the ignore name equality
	 * @param ignoreContentEquality
	 *            the ignore content equality
	 *
	 * @return the i file content result bean
	 */
	public static IFileContentResultBean compareFiles(final File sourceFile,
		final File fileToCompare, final boolean ignoreAbsolutePathEquality,
		final boolean ignoreExtensionEquality, final boolean ignoreLengthEquality,
		final boolean ignoreLastModified, final boolean ignoreNameEquality,
		final boolean ignoreContentEquality)
	{
		final IFileContentResultBean fileContentResultBean = new FileContentResultBean(sourceFile,
			fileToCompare);
		compare(fileContentResultBean, ignoreAbsolutePathEquality, ignoreExtensionEquality,
			ignoreLengthEquality, ignoreLastModified, ignoreNameEquality, ignoreContentEquality);
		return fileContentResultBean;
	}

	/**
	 * Completes the compare from the files encapsulated in the FileCompareResultBean.
	 *
	 * @param fileCompareResultBean
	 *            the FileCompareResultBean.
	 */
	public static void completeCompare(final IFileCompareResultBean fileCompareResultBean)
	{
		compare(fileCompareResultBean, false, false, false, false, false);
	}

	/**
	 * Find equal files.
	 *
	 * @param dirToSearch
	 *            the dir to search
	 *
	 * @return the list with the result beans
	 */
	public static List<IFileCompareResultBean> findEqualFiles(final File dirToSearch)
	{
		final List<File> allFiles = FileSearchUtils.findFilesRecursive(dirToSearch, "*");
		final List<IFileCompareResultBean> equalFiles = new ArrayList<>();
		for (int i = 0; i < allFiles.size(); i++)
		{
			final File toCompare = allFiles.get(i);
			for (final File file : allFiles)
			{
				if (toCompare.equals(file))
				{
					continue;
				}
				final IFileCompareResultBean compareResultBean = CompareFileUtils
					.simpleCompareFiles(toCompare, file);
				final boolean equal = CompareFileUtils.validateEquality(compareResultBean);
				// if equal is true and the list does not contain the same
				// compareResultBean then add it.
				if (equal && !equalFiles.contains(compareResultBean))
				{
					equalFiles.add(compareResultBean);
				}
			}
		}
		return equalFiles;
	}


	/**
	 * Find equal files.
	 *
	 * @param dirToSearch
	 *            the dir to search
	 * @param ignoreAbsolutePathEquality
	 *            If this is true then the absolute path equality will be ignored.
	 * @param ignoreExtensionEquality
	 *            the ignore extension equality
	 * @param ignoreLengthEquality
	 *            the ignore length equality
	 * @param ignoreLastModified
	 *            the ignore last modified
	 * @param ignoreNameEquality
	 *            the ignore name equality
	 *
	 * @return the list with the result beans
	 */
	public static List<IFileCompareResultBean> findEqualFiles(final File dirToSearch,
		final boolean ignoreAbsolutePathEquality, final boolean ignoreExtensionEquality,
		final boolean ignoreLengthEquality, final boolean ignoreLastModified,
		final boolean ignoreNameEquality)
	{
		final List<File> allFiles = FileSearchUtils.findFilesRecursive(dirToSearch, "*");
		final List<IFileCompareResultBean> equalFiles = new ArrayList<IFileCompareResultBean>();
		for (int i = 0; i < allFiles.size(); i++)
		{
			final File toCompare = allFiles.get(i);
			for (int j = 0; j < allFiles.size(); j++)
			{
				final File file = allFiles.get(j);
				if (toCompare.equals(file))
				{
					continue;
				}
				final IFileCompareResultBean compareResultBean = CompareFileUtils.compareFiles(
					toCompare, file, ignoreAbsolutePathEquality, ignoreExtensionEquality,
					ignoreLengthEquality, ignoreLastModified, ignoreNameEquality);
				final boolean equal = CompareFileUtils.validateEquality(compareResultBean);
				// if equal is true and the list does not contain the same
				// compareResultBean then add it.
				if (equal && !equalFiles.contains(compareResultBean))
				{
					equalFiles.add(compareResultBean);
				}
			}
		}
		return equalFiles;
	}

	/**
	 * Find equal files from the given directories.
	 *
	 * @param source
	 *            the source directory.
	 * @param compare
	 *            the directory compare.
	 *
	 * @return the list with the result beans
	 */
	public static List<IFileCompareResultBean> findEqualFiles(final File source, final File compare)
	{
		final List<File> allSourceFiles = FileSearchUtils.findFilesRecursive(source, "*");
		final List<File> allCompareFiles = FileSearchUtils.findFilesRecursive(compare, "*");
		final List<IFileCompareResultBean> equalFiles = new ArrayList<IFileCompareResultBean>();
		for (int i = 0; i < allSourceFiles.size(); i++)
		{
			final File toCompare = allSourceFiles.get(i);
			for (int j = 0; j < allCompareFiles.size(); j++)
			{
				final File file = allCompareFiles.get(j);
				if (toCompare.equals(file))
				{
					continue;
				}
				final IFileCompareResultBean compareResultBean = CompareFileUtils
					.simpleCompareFiles(toCompare, file);
				final boolean equal = CompareFileUtils.validateEquality(compareResultBean);
				// if equal is true and the list does not contain the same
				// compareResultBean then add it.
				if (equal && !equalFiles.contains(compareResultBean))
				{
					equalFiles.add(compareResultBean);
				}
			}
		}
		return equalFiles;
	}

	/**
	 * Find equal files from the given directories.
	 *
	 * @param source
	 *            the source directory.
	 * @param compare
	 *            the directory compare.
	 * @param ignoreAbsolutePathEquality
	 *            If this is true then the absolute path equality will be ignored.
	 * @param ignoreExtensionEquality
	 *            the ignore extension equality
	 * @param ignoreLengthEquality
	 *            the ignore length equality
	 * @param ignoreLastModified
	 *            the ignore last modified
	 * @param ignoreNameEquality
	 *            the ignore name equality
	 *
	 * @return the list with the result beans
	 */
	public static List<IFileCompareResultBean> findEqualFiles(final File source,
		final File compare, final boolean ignoreAbsolutePathEquality,
		final boolean ignoreExtensionEquality, final boolean ignoreLengthEquality,
		final boolean ignoreLastModified, final boolean ignoreNameEquality)
	{
		final List<File> allSourceFiles = FileSearchUtils.findFilesRecursive(source, "*");
		final List<File> allCompareFiles = FileSearchUtils.findFilesRecursive(compare, "*");
		final List<IFileCompareResultBean> equalFiles = new ArrayList<IFileCompareResultBean>();
		for (int i = 0; i < allSourceFiles.size(); i++)
		{
			final File toCompare = allSourceFiles.get(i);
			for (int j = 0; j < allCompareFiles.size(); j++)
			{
				final File file = allCompareFiles.get(j);
				if (toCompare.equals(file))
				{
					continue;
				}
				final IFileCompareResultBean compareResultBean = CompareFileUtils.compareFiles(
					toCompare, file, ignoreAbsolutePathEquality, ignoreExtensionEquality,
					ignoreLengthEquality, ignoreLastModified, ignoreNameEquality);
				final boolean equal = CompareFileUtils.validateEquality(compareResultBean);
				// if equal is true and the list does not contain the same
				// compareResultBean then add it.
				if (equal && !equalFiles.contains(compareResultBean))
				{
					equalFiles.add(compareResultBean);
				}
			}
		}
		return equalFiles;
	}

	/**
	 * Compare files with the same content.
	 *
	 * @param dirToSearch
	 *            the dir to search
	 *
	 * @return the list with the result beans
	 */
	public static List<IFileContentResultBean> findEqualFilesWithSameContent(final File dirToSearch)
	{
		final List<IFileContentResultBean> equalFiles = new ArrayList<IFileContentResultBean>();

		final List<File> allFiles = FileSearchUtils.findFilesRecursive(dirToSearch, "*");

		for (int i = 0; i < allFiles.size(); i++)
		{
			final File toCompare = allFiles.get(i);
			for (int j = 0; j < allFiles.size(); j++)
			{
				final File file = allFiles.get(j);
				if (toCompare.equals(file))
				{
					continue;
				}
				final IFileContentResultBean contentResultBean = CompareFileUtils.compareFiles(
					toCompare, file);
				final boolean equal = CompareFileUtils.validateEquality(contentResultBean);
				// if equal is true and the list does not contain the same
				// compareResultBean then add it.
				if (equal && !equalFiles.contains(contentResultBean))
				{
					equalFiles.add(contentResultBean);
				}
			}
		}

		return equalFiles;
	}


	/**
	 * Compare files with the same content.
	 *
	 * @param dirToSearch
	 *            the dir to search
	 * @param ignoreAbsolutePathEquality
	 *            If this is true then the absolute path equality will be ignored.
	 * @param ignoreExtensionEquality
	 *            the ignore extension equality
	 * @param ignoreLengthEquality
	 *            the ignore length equality
	 * @param ignoreLastModified
	 *            the ignore last modified
	 * @param ignoreNameEquality
	 *            the ignore name equality
	 * @param ignoreContentEquality
	 *            the ignore content equality
	 * @return the list with the result beans
	 */
	public static List<IFileContentResultBean> findEqualFilesWithSameContent(
		final File dirToSearch, final boolean ignoreAbsolutePathEquality,
		final boolean ignoreExtensionEquality, final boolean ignoreLengthEquality,
		final boolean ignoreLastModified, final boolean ignoreNameEquality,
		final boolean ignoreContentEquality)
	{
		final List<IFileContentResultBean> equalFiles = new ArrayList<IFileContentResultBean>();

		final List<File> allFiles = FileSearchUtils.findFilesRecursive(dirToSearch, "*");

		for (int i = 0; i < allFiles.size(); i++)
		{
			final File toCompare = allFiles.get(i);
			for (int j = 0; j < allFiles.size(); j++)
			{
				final File file = allFiles.get(j);
				if (toCompare.equals(file))
				{
					continue;
				}
				final IFileContentResultBean contentResultBean = CompareFileUtils.compareFiles(
					toCompare, file, ignoreAbsolutePathEquality, ignoreExtensionEquality,
					ignoreLengthEquality, ignoreLastModified, ignoreNameEquality,
					ignoreContentEquality);
				final boolean equal = CompareFileUtils.validateEquality(contentResultBean);
				// if equal is true and the list does not contain the same
				// compareResultBean then add it.
				if (equal && !equalFiles.contains(contentResultBean))
				{
					equalFiles.add(contentResultBean);
				}
			}
		}

		return equalFiles;
	}

	/**
	 * Find equal files from the given directories.
	 *
	 * @param source
	 *            the source directory.
	 * @param compare
	 *            the directory compare.
	 *
	 * @return the list with the result beans
	 */
	public static List<IFileContentResultBean> findEqualFilesWithSameContent(final File source,
		final File compare)
	{
		final List<File> allSourceFiles = FileSearchUtils.findFilesRecursive(source, "*");
		final List<File> allCompareFiles = FileSearchUtils.findFilesRecursive(compare, "*");
		final List<IFileContentResultBean> equalFiles = new ArrayList<IFileContentResultBean>();
		for (int i = 0; i < allSourceFiles.size(); i++)
		{
			final File toCompare = allSourceFiles.get(i);
			for (int j = 0; j < allCompareFiles.size(); j++)
			{
				final File file = allCompareFiles.get(j);
				if (toCompare.equals(file))
				{
					continue;
				}
				final IFileContentResultBean contentResultBean = CompareFileUtils.compareFiles(
					toCompare, file);
				final boolean equal = CompareFileUtils.validateEquality(contentResultBean);
				// if equal is true and the list does not contain the same
				// compareResultBean then add it.
				if (equal && !equalFiles.contains(contentResultBean))
				{
					equalFiles.add(contentResultBean);
				}
			}
		}
		return equalFiles;
	}

	/**
	 * Find equal files from the given directories.
	 *
	 * @param source
	 *            the source directory.
	 * @param compare
	 *            the directory compare.
	 * @param ignoreAbsolutePathEquality
	 *            If this is true then the absolute path equality will be ignored.
	 * @param ignoreExtensionEquality
	 *            the ignore extension equality
	 * @param ignoreLengthEquality
	 *            the ignore length equality
	 * @param ignoreLastModified
	 *            the ignore last modified
	 * @param ignoreNameEquality
	 *            the ignore name equality
	 * @param ignoreContentEquality
	 *            the ignore content equality
	 *
	 * @return the list with the result beans
	 */
	public static List<IFileContentResultBean> findEqualFilesWithSameContent(final File source,
		final File compare, final boolean ignoreAbsolutePathEquality,
		final boolean ignoreExtensionEquality, final boolean ignoreLengthEquality,
		final boolean ignoreLastModified, final boolean ignoreNameEquality,
		final boolean ignoreContentEquality)
	{
		final List<File> allSourceFiles = FileSearchUtils.findFilesRecursive(source, "*");
		final List<File> allCompareFiles = FileSearchUtils.findFilesRecursive(compare, "*");
		final List<IFileContentResultBean> equalFiles = new ArrayList<IFileContentResultBean>();
		for (int i = 0; i < allSourceFiles.size(); i++)
		{
			final File toCompare = allSourceFiles.get(i);
			for (int j = 0; j < allCompareFiles.size(); j++)
			{
				final File file = allCompareFiles.get(j);
				if (toCompare.equals(file))
				{
					continue;
				}
				final IFileContentResultBean contentResultBean = CompareFileUtils.compareFiles(
					toCompare, file, ignoreAbsolutePathEquality, ignoreExtensionEquality,
					ignoreLengthEquality, ignoreLastModified, ignoreNameEquality,
					ignoreContentEquality);
				final boolean equal = CompareFileUtils.validateEquality(contentResultBean);
				// if equal is true and the list does not contain the same
				// compareResultBean then add it.
				if (equal && !equalFiles.contains(contentResultBean))
				{
					equalFiles.add(contentResultBean);
				}
			}
		}
		return equalFiles;
	}

	/**
	 * Simple comparing the given files.
	 *
	 * @param sourceFile
	 *            the source file
	 * @param fileToCompare
	 *            the file to compare
	 *
	 * @return Returns a FileCompareResultBean Object with the results.
	 */
	public static IFileCompareResultBean simpleCompareFiles(final File sourceFile,
		final File fileToCompare)
	{
		return compareFiles(sourceFile, fileToCompare, true, false, false, true, false);
	}

	/**
	 * Validates the files encapsulated in the IFileCompareResultBean for simple equality. This
	 * means like if they have equal file extension, length, last modified, and filenames.
	 *
	 * @param fileCompareResultBean
	 *            the FileCompareResultBean.
	 *
	 * @return true, if successful
	 */
	public static boolean validateEquality(final IFileCompareResultBean fileCompareResultBean)
	{
		return fileCompareResultBean.getFileExtensionEquality()
			&& fileCompareResultBean.getLengthEquality()
			&& fileCompareResultBean.getLastModifiedEquality()
			&& fileCompareResultBean.getNameEquality();
	}

	/**
	 * Validates the files encapsulated in the IFileCompareResultBean for total equality. This means
	 * like if they have equal file extension, length, last modified, filenames and content.
	 *
	 * @param fileContentResultBean
	 *            the IFileContentResultBean.
	 *
	 * @return true, if successful
	 */
	public static boolean validateEquality(final IFileContentResultBean fileContentResultBean)
	{
		return fileContentResultBean.getFileExtensionEquality()
			&& fileContentResultBean.getLengthEquality()
			&& fileContentResultBean.getLastModifiedEquality()
			&& fileContentResultBean.getNameEquality()
			&& fileContentResultBean.getContentEquality();
	}

	private CompareFileUtils()
	{
		super();
	}

}
