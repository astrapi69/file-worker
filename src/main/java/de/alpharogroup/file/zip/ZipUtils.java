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
package de.alpharogroup.file.zip;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import de.alpharogroup.file.FileConst;
import de.alpharogroup.file.exceptions.FileDoesNotExistException;
import de.alpharogroup.file.search.FileSearchUtils;
import de.alpharogroup.io.StreamUtils;

/**
 * The Class ZipUtils provides functionality for ziping and unzipping files.
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public final class ZipUtils
{

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
		final String absolutePath = file.getAbsolutePath();
		final int index = absolutePath.indexOf(dirToZip.getName());
		final String zipEntryName = absolutePath.substring(index, absolutePath.length());
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
		InputStream is = null;
		BufferedInputStream bis = null;
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;

		try
		{
			final ZipEntry ze = zipFile.getEntry(target.getName());
			is = zipFile.getInputStream(ze);
			bis = new BufferedInputStream(is);

			new File(fileToExtract.getParent()).mkdirs();
			fos = new FileOutputStream(fileToExtract);
			bos = new BufferedOutputStream(fos);
			for (int c; (c = bis.read()) != -1;)
			{
				bos.write((byte)c);
			}
			bos.flush();
			bos.close();
		}
		catch (final IOException e)
		{
			throw e;
		}
		finally
		{
			StreamUtils.closeInputStream(bis);
			StreamUtils.closeInputStream(is);

			StreamUtils.closeOutputStream(fos);
			StreamUtils.closeOutputStream(bos);
		}
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
		for (final String element : FileConst.ZIP_EXTENSIONS)
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
			zipFile.close();
		}
		catch (final IOException e)
		{
			throw e;
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
	 * @throws Exception
	 *             the exception
	 */
	public static void zip(final File fileToZip, final File zipFile) throws Exception
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
	 */
	public static void zip(final File dirToZip, final File zipFile, final FilenameFilter filter)
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
	 */
	public static void zip(final File dirToZip, final File zipFile, final FilenameFilter filter,
		final boolean createFile)
	{
		ZipOutputStream zos = null;
		FileOutputStream fos = null;
		try
		{
			if (!dirToZip.exists())
			{
				throw new IOException("Directory with the name " + dirToZip.getName()
					+ " does not exist.");
			}

			if (!zipFile.exists())
			{
				if (createFile)
				{
					zipFile.createNewFile();
				}
				else
				{
					throw new FileDoesNotExistException("Zipfile with the name "
						+ zipFile.getName() + " does not exist.");
				}
			}
			fos = new FileOutputStream(zipFile);
			zos = new ZipOutputStream(fos);
			zos.setLevel(9);
			zipFiles(dirToZip, dirToZip, zos, filter);
			zos.flush();
			zos.finish();
			zos.close();
			fos.flush();
			fos.close();
			fos = null;
			zos = null;
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			StreamUtils.closeOutputStream(fos);
			StreamUtils.closeOutputStream(zos);
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
			File[] fList;
			List<File> foundedFiles;
			if (null != fileFilter)
			{
				final File[] tmpfList = file.listFiles(fileFilter);
				final List<File> foundedDirs = FileSearchUtils.listDirs(file);
				if (0 < foundedDirs.size())
				{
					final List<File> tmp = Arrays.asList(tmpfList);
					foundedDirs.addAll(tmp);
					foundedFiles = foundedDirs;
				}
				else
				{
					final List<File> tmp = Arrays.asList(tmpfList);
					foundedFiles = tmp;
				}
			}
			else
			{
				fList = file.listFiles();
				final List<File> tmp = Arrays.asList(fList);
				foundedFiles = tmp;
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
	 * Private constructor.
	 */
	private ZipUtils()
	{
		super();
	}

}
