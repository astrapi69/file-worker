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
package de.alpharogroup.file.zip;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * The class {@link Unzipper}
 *
 * @author Asterios Raptis
 * @version 1.0
 */
public class Unzipper
{

	/** The to dir. */
	private File toDir;

	/** The zip file. */
	private ZipFile zipFile;

	/**
	 * Instantiates a new unzipper.
	 *
	 * @param zipFile
	 *            the zip file
	 * @param toDir
	 *            the to dir
	 */
	public Unzipper(final ZipFile zipFile, final File toDir)
	{
		this.zipFile = zipFile;
		this.toDir = toDir;
	}

	public Unzipper(File toDir, ZipFile zipFile)
	{
		this.toDir = toDir;
		this.zipFile = zipFile;
	}

	public Unzipper()
	{
	}

	public static UnzipperBuilder builder()
	{
		return new UnzipperBuilder();
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
	public void extractZipEntry(final ZipFile zipFile, final ZipEntry target,
		final File toDirectory) throws IOException
	{
		ZipExtensions.extractZipEntry(zipFile, target, toDirectory);
	}

	/**
	 * Unzip.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void unzip() throws IOException
	{
		this.unzip(this.zipFile, this.toDir);
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
	public void unzip(final ZipFile zipFile, final File toDir) throws IOException
	{
		ZipExtensions.unzip(zipFile, toDir);
	}

	public File getToDir()
	{
		return this.toDir;
	}

	public ZipFile getZipFile()
	{
		return this.zipFile;
	}

	public void setToDir(File toDir)
	{
		this.toDir = toDir;
	}

	public void setZipFile(ZipFile zipFile)
	{
		this.zipFile = zipFile;
	}

	public String toString()
	{
		return "Unzipper(toDir=" + this.getToDir() + ", zipFile=" + this.getZipFile() + ")";
	}

	public UnzipperBuilder toBuilder()
	{
		return new UnzipperBuilder().toDir(this.toDir).zipFile(this.zipFile);
	}

	public static class UnzipperBuilder
	{
		private File toDir;
		private ZipFile zipFile;

		UnzipperBuilder()
		{
		}

		public Unzipper.UnzipperBuilder toDir(File toDir)
		{
			this.toDir = toDir;
			return this;
		}

		public Unzipper.UnzipperBuilder zipFile(ZipFile zipFile)
		{
			this.zipFile = zipFile;
			return this;
		}

		public Unzipper build()
		{
			return new Unzipper(toDir, zipFile);
		}

		public String toString()
		{
			return "Unzipper.UnzipperBuilder(toDir=" + this.toDir + ", zipFile=" + this.zipFile
				+ ")";
		}
	}
}
