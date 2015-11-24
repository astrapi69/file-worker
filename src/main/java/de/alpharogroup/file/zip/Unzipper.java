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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import de.alpharogroup.io.StreamUtils;

/**
 * The Class Unzipper.
 *
 * @author Asterios Raptis
 * @version 1.0
 */
public class Unzipper
{

	/** The zip file. */
	private ZipFile zipFile;

	/** The to dir. */
	private File toDir;

	/** The password. */
	private String password;

	/**
	 * Instantiates a new unzipper.
	 */
	public Unzipper()
	{
	}

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
	public void extractZipEntry(final ZipFile zipFile, final ZipEntry target, final File toDirectory)
		throws IOException
	{
		final File fileToExtract = new File(toDirectory, target.getName());
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		InputStream is = null;
		try
		{
			is = zipFile.getInputStream(target);
			if (is != null)
			{
				bis = new BufferedInputStream(is);

				new File(fileToExtract.getParent()).mkdirs();

				bos = new BufferedOutputStream(new FileOutputStream(fileToExtract));
				for (int c; (c = bis.read()) != -1;)
				{
					bos.write((byte)c);
				}

				bos.close();
			}
		}
		catch (final IOException e)
		{
			throw e;
		}
		finally
		{
			StreamUtils.closeInputStream(is);
			StreamUtils.closeInputStream(bis);
			StreamUtils.closeOutputStream(bos);
		}
	}

	/**
	 * Gets the password.
	 *
	 * @return the password
	 */
	public String getPassword()
	{
		return password;
	}

	/**
	 * Returns the field <code>toDir</code>.
	 * 
	 * @return The field <code>toDir</code>.
	 */
	public File getToDir()
	{
		return this.toDir;
	}

	/**
	 * Returns the field <code>zipFile</code>.
	 * 
	 * @return The field <code>zipFile</code>.
	 */
	public ZipFile getZipFile()
	{
		return this.zipFile;
	}

	/**
	 * Sets the password.
	 *
	 * @param password
	 *            the new password
	 */
	public void setPassword(final String password)
	{
		this.password = password;
	}

	/**
	 * Sets the field <code>toDir</code>.
	 * 
	 * @param toDir
	 *            The <code>toDir</code> to set
	 */
	public void setToDir(final File toDir)
	{
		this.toDir = toDir;
	}

	/**
	 * Sets the field <code>zipFile</code>.
	 * 
	 * @param zipFile
	 *            The <code>zipFile</code> to set
	 */
	public void setZipFile(final ZipFile zipFile)
	{
		this.zipFile = zipFile;
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
	 * @param password
	 *            the password
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void unzip(final File zipFile, final File toDir, final String password)
		throws IOException
	{
		unzip(zipFile, toDir, password, null);
	}

	/**
	 * Unzip.
	 *
	 * @param zipFile
	 *            the zip file
	 * @param toDir
	 *            the to dir
	 * @param password
	 *            the password
	 * @param charsetName
	 *            the charset name
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void unzip(final File zipFile, final File toDir, final String password,
		final Charset charsetName) throws IOException
	{
		final FileInputStream fis = new FileInputStream(zipFile);
		// wrap it in the decrypt stream
		final ZipDecryptInputStream zdis = new ZipDecryptInputStream(fis, password);
		// wrap the decrypt stream by the ZIP input stream
		ZipInputStream zis = null;
		if (charsetName != null)
		{
			zis = new ZipInputStream(zdis, charsetName);
		}
		else
		{
			zis = new ZipInputStream(zdis);
		}
		// read all the zip entries and save them as files
		ZipEntry ze;
		while ((ze = zis.getNextEntry()) != null)
		{
			final String pathToExtract = toDir.getAbsolutePath() + File.separator + ze.getName();
			final FileOutputStream fos = new FileOutputStream(pathToExtract);
			int b;
			while ((b = zis.read()) != -1)
			{
				fos.write(b);
			}
			fos.close();
			zis.closeEntry();
		}
		zis.close();
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
		for (final Enumeration<? extends ZipEntry> e = zipFile.entries(); e.hasMoreElements();)
		{
			final ZipEntry entry = e.nextElement();
			this.extractZipEntry(zipFile, entry, toDir);
		}
		zipFile.close();
	}

}
