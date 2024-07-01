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

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * The `Unzipper` class provides methods to extract zip entries to a specified directory. It
 * utilizes `ZipExtensions` for performing the extraction operations.
 */
public class Unzipper
{

	/** The directory to extract files to. */
	private File toDir;
	/** The zip file to extract entries from. */
	private ZipFile zipFile;

	/**
	 * Default constructor.
	 */
	public Unzipper()
	{
	}

	/**
	 * Constructs an `Unzipper` with the given target directory and zip file.
	 *
	 * @param toDir
	 *            the directory to extract files to
	 * @param zipFile
	 *            the zip file to extract entries from
	 */
	public Unzipper(File toDir, ZipFile zipFile)
	{
		this.toDir = toDir;
		this.zipFile = zipFile;
	}

	/**
	 * Constructs an `Unzipper` with the given zip file and target directory.
	 *
	 * @param zipFile
	 *            the zip file to extract entries from
	 * @param toDir
	 *            the directory to extract files to
	 */
	public Unzipper(final ZipFile zipFile, final File toDir)
	{
		this.zipFile = zipFile;
		this.toDir = toDir;
	}

	/**
	 * Creates a builder for constructing `Unzipper` objects.
	 *
	 * @return a new `UnzipperBuilder` instance
	 */
	public static UnzipperBuilder builder()
	{
		return new UnzipperBuilder();
	}

	/**
	 * Extracts a specific zip entry to the target directory.
	 *
	 * @param zipFile
	 *            the zip file containing the entry
	 * @param target
	 *            the zip entry to extract
	 * @param toDirectory
	 *            the directory to extract the entry to
	 * @throws IOException
	 *             if an I/O error occurs during extraction
	 */
	public void extractZipEntry(final ZipFile zipFile, final ZipEntry target,
		final File toDirectory) throws IOException
	{
		ZipExtensions.extractZipEntry(zipFile, target, toDirectory);
	}

	/**
	 * Retrieves the current target directory for extraction.
	 *
	 * @return the current target directory
	 */
	public File getToDir()
	{
		return this.toDir;
	}

	/**
	 * Sets the target directory for extraction.
	 *
	 * @param toDir
	 *            the directory to set as the target for extraction
	 */
	public void setToDir(File toDir)
	{
		this.toDir = toDir;
	}

	/**
	 * Retrieves the current zip file being used for extraction.
	 *
	 * @return the current zip file
	 */
	public ZipFile getZipFile()
	{
		return this.zipFile;
	}

	/**
	 * Sets the zip file to use for extraction.
	 *
	 * @param zipFile
	 *            the zip file to set for extraction
	 */
	public void setZipFile(ZipFile zipFile)
	{
		this.zipFile = zipFile;
	}

	/**
	 * Creates a builder initialized with the current `Unzipper` instance's properties.
	 *
	 * @return a new `UnzipperBuilder` initialized with the current instance's properties
	 */
	public UnzipperBuilder toBuilder()
	{
		return new UnzipperBuilder().toDir(this.toDir).zipFile(this.zipFile);
	}

	/**
	 * Returns a string representation of the `Unzipper` object, showing its current target
	 * directory and zip file.
	 *
	 * @return a string representation of the `Unzipper` object
	 */
	@Override
	public String toString()
	{
		return "Unzipper(toDir=" + this.getToDir() + ", zipFile=" + this.getZipFile() + ")";
	}

	/**
	 * Extracts all entries from the current zip file to the current target directory.
	 *
	 * @throws IOException
	 *             if an I/O error occurs during extraction
	 */
	public void unzip() throws IOException
	{
		this.unzip(this.zipFile, this.toDir);
	}

	/**
	 * Extracts all entries from the specified zip file to the specified target directory.
	 *
	 * @param zipFile
	 *            the zip file to extract entries from
	 * @param toDir
	 *            the directory to extract entries to
	 * @throws IOException
	 *             if an I/O error occurs during extraction
	 */
	public void unzip(final ZipFile zipFile, final File toDir) throws IOException
	{
		ZipExtensions.unzip(zipFile, toDir);
	}

	/**
	 * Builder class for constructing `Unzipper` objects.
	 */
	public static class UnzipperBuilder
	{
		private File toDir;
		private ZipFile zipFile;

		/**
		 * Default constructor.
		 */
		UnzipperBuilder()
		{
		}

		/**
		 * Constructs a new `Unzipper` object based on the current builder state.
		 *
		 * @return a new `Unzipper` object
		 */
		public Unzipper build()
		{
			return new Unzipper(toDir, zipFile);
		}

		/**
		 * Sets the target directory for the `Unzipper` object being built.
		 *
		 * @param toDir
		 *            the directory to set as the target for extraction
		 * @return the current `UnzipperBuilder` instance
		 */
		public UnzipperBuilder toDir(File toDir)
		{
			this.toDir = toDir;
			return this;
		}

		/**
		 * Sets the zip file for the `Unzipper` object being built.
		 *
		 * @param zipFile
		 *            the zip file to set for extraction
		 * @return the current `UnzipperBuilder` instance
		 */
		public UnzipperBuilder zipFile(ZipFile zipFile)
		{
			this.zipFile = zipFile;
			return this;
		}

		/**
		 * Returns a string representation of the `UnzipperBuilder` object, showing its current
		 * target directory and zip file.
		 *
		 * @return a string representation of the `UnzipperBuilder` object
		 */
		@Override
		public String toString()
		{
			return "Unzipper.UnzipperBuilder(toDir=" + this.toDir + ", zipFile=" + this.zipFile
				+ ")";
		}
	}
}
