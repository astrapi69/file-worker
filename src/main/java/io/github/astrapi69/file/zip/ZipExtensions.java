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
package io.github.astrapi69.file.zip;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import io.github.astrapi69.collection.set.SetFactory;
import io.github.astrapi69.file.create.DirectoryFactory;
import io.github.astrapi69.file.create.FileFactory;
import io.github.astrapi69.file.exception.FileDoesNotExistException;
import io.github.astrapi69.file.search.FileSearchExtensions;
import io.github.astrapi69.io.file.FileConstants;

/**
 * The class {@link ZipExtensions} provides functionality for ziping and unzipping files.
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public final class ZipExtensions
{

	private ZipExtensions()
	{
	}

	/**
	 * Adds the file.
	 *
	 * @param file
	 *            the file
	 * @param dirToZip
	 *            the dir to zip
	 * @param zos
	 *            the zos
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private static void addFile(final File file, final File dirToZip, final ZipOutputStream zos)
		throws IOException
	{
		final String zipEntryName = getZipEntryName(file, dirToZip);
		final byte[] b = new byte[(int)file.length()];
		final ZipEntry cpZipEntry = new ZipEntry(zipEntryName);
		zos.putNextEntry(cpZipEntry);
		zos.write(b, 0, (int)file.length());
		zos.closeEntry();
	}

	/**
	 * Extract zip entry.
	 *
	 * @param zipFile
	 *            the zip file
	 * @param target
	 *            the target
	 * @param toDirectory
	 *            the to directory
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void extractZipEntry(final ZipFile zipFile, final ZipEntry target,
		final File toDirectory) throws IOException
	{
		final File fileToExtract = new File(toDirectory, target.getName());
		DirectoryFactory.mkParentDirs(fileToExtract);
		try (InputStream is = zipFile.getInputStream(target);
			BufferedInputStream bis = new BufferedInputStream(is);
			FileOutputStream fos = new FileOutputStream(fileToExtract);
			BufferedOutputStream bos = new BufferedOutputStream(fos))
		{
			for (int c; (c = bis.read()) != -1;)
			{
				bos.write((byte)c);
			}
			bos.flush();
		}
	}

	static List<File> getFoundedFiles(File file, File[] tmpfList)
	{
		List<File> foundedFiles;
		final List<File> foundedDirs = FileSearchExtensions.listDirs(file);
		if (0 < foundedDirs.size())
		{
			foundedDirs.addAll(Arrays.asList(tmpfList));
			foundedFiles = foundedDirs;
		}
		else
		{
			foundedFiles = Arrays.asList(tmpfList);
		}
		return foundedFiles;
	}

	private static String getZipEntryName(File file, File dirToZip)
	{
		final String absolutePath = file.getAbsolutePath();
		String name = dirToZip.getName();
		final int index = absolutePath.indexOf(name);
		return absolutePath.substring(index);
	}

	/**
	 * Checks if the given filename is a zip-file.
	 *
	 * @param filename
	 *            The filename to check.
	 * @return True if the filename is a zip-file otherwise false.
	 */
	public static boolean isZip(final String filename)
	{
		for (final String element : FileConstants.ZIP_EXTENSIONS)
		{
			if (filename.endsWith(element))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Unzip.
	 *
	 * @param zipFile
	 *            the zip file
	 * @param toDir
	 *            the to dir
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void unzip(final ZipFile zipFile, final File toDir) throws IOException
	{
		try
		{
			for (final Enumeration<? extends ZipEntry> e = zipFile.entries(); e.hasMoreElements();)
			{
				final ZipEntry entry = e.nextElement();
				extractZipEntry(zipFile, entry, toDir);
			}
		}
		finally
		{
			zipFile.close();
		}
	}

	/**
	 * Zip the given file.
	 *
	 * @param fileToZip
	 *            the file to zip.
	 * @param zipFile
	 *            the zip file
	 * @throws FileNotFoundException
	 *             is thrown if the given file is not found
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws FileDoesNotExistException
	 *             the file does not exist exception
	 */
	public static void zip(final File fileToZip, final File zipFile)
		throws FileNotFoundException, IOException, FileDoesNotExistException
	{
		zip(fileToZip, zipFile, null);
	}

	/**
	 * Zip the given file.
	 *
	 * @param dirToZip
	 *            the dir to zip
	 * @param zipFile
	 *            the zip file
	 * @param filter
	 *            the filter
	 * @throws FileNotFoundException
	 *             is thrown if the given file is not found
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws FileDoesNotExistException
	 *             the file does not exist exception
	 */
	public static void zip(final File dirToZip, final File zipFile, final FilenameFilter filter)
		throws FileNotFoundException, IOException, FileDoesNotExistException
	{
		zip(dirToZip, zipFile, filter, true);
	}

	/**
	 * Zip the given file.
	 *
	 * @param dirToZip
	 *            the dir to zip
	 * @param zipFile
	 *            the zip file
	 * @param filter
	 *            the filter
	 * @param createFile
	 *            the create file
	 * @throws FileNotFoundException
	 *             is thrown if the given file is not found
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws FileDoesNotExistException
	 *             the file does not exist exception
	 */
	public static void zip(final File dirToZip, final File zipFile, final FilenameFilter filter,
		final boolean createFile)
		throws FileNotFoundException, IOException, FileDoesNotExistException
	{
		try (FileOutputStream fos = new FileOutputStream(zipFile);
			ZipOutputStream zos = new ZipOutputStream(fos))
		{
			newZipFile(dirToZip, zipFile, createFile);
			zos.setLevel(9);
			zipFiles(dirToZip, dirToZip, zos, filter);
			zos.flush();
			zos.finish();
			fos.flush();
		}
	}

	private static void newZipFile(File dirToZip, File zipFile, boolean createFile)
		throws IOException, FileDoesNotExistException
	{
		if (!dirToZip.exists())
		{
			throw new IOException(
				"Directory with the name " + dirToZip.getName() + " does not exist.");
		}

		if (!zipFile.exists())
		{
			if (createFile)
			{
				FileFactory.newFile(zipFile);
			}
			else
			{
				throw new FileDoesNotExistException(
					"Zipfile with the name " + zipFile.getName() + " does not exist.");
			}
		}
	}

	/**
	 * Zip files.
	 *
	 * @param file
	 *            the file
	 * @param dirToZip
	 *            the dir to zip
	 * @param zos
	 *            the zos
	 * @param fileFilter
	 *            the file filter
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private static void zipFiles(final File file, final File dirToZip, final ZipOutputStream zos,
		final FilenameFilter fileFilter) throws IOException
	{
		if (file.isDirectory())
		{
			List<File> foundedFiles;
			if (null != fileFilter)
			{
				foundedFiles = getFoundedFiles(file, file.listFiles(fileFilter));
			}
			else
			{
				File[] files = file.listFiles();
				if (files != null)
				{
					foundedFiles = Arrays.asList(files);
				}
				else
				{
					foundedFiles = new ArrayList<>();
				}
			}
			for (int i = 0; i < foundedFiles.size(); i++)
			{
				zipFiles(foundedFiles.get(i), dirToZip, zos, fileFilter);
			}
		}
		else
		{
			addFile(file, dirToZip, zos);
		}
	}

	/**
	 * Zip files.
	 *
	 * @param file
	 *            the file
	 * @param dirToZip
	 *            the dir to zip
	 * @param excludeFileFilter
	 *            the file filter for files to exclude
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void zipFiles(final File file, final File dirToZip,
		final FileFilter... excludeFileFilter) throws IOException
	{
		try (FileOutputStream fos = new FileOutputStream(dirToZip);
			ZipOutputStream zos = new ZipOutputStream(fos))
		{
			Set<File> fileSet = FileSearchExtensions.findFiles(file, SetFactory.newHashSet(),
				excludeFileFilter);
			for (File foundedFile : fileSet)
			{
				final byte[] b = new byte[(int)foundedFile.length()];
				final ZipEntry cpZipEntry = new ZipEntry(foundedFile.getAbsolutePath());
				zos.putNextEntry(cpZipEntry);
				zos.write(b, 0, (int)foundedFile.length());
			}
		}
	}
}
