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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

/**
 * The class {@link Unzipper}
 *
 * @author Asterios Raptis
 * @version 1.0
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Unzipper
{

	/**
	 * The password.
	 * 
	 * @deprecated <br>
	 *             <br>
	 *             Note: will be removed in the next minor version.
	 */
	String password;

	/** The to dir. */
	File toDir;

	/** The zip file. */
	ZipFile zipFile;

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
	 * @param password
	 *            the password
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * 
	 * @deprecated use instead
	 *             {@code Zip4jExtensions#extract(net.lingala.zip4j.core.ZipFile, java.io.File, String)}.
	 *             <br>
	 *             <br>
	 *             Note: will be removed in the next minor version.
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
	 * 
	 * @deprecated use instead
	 *             {@code Zip4jExtensions#extract(net.lingala.zip4j.core.ZipFile, java.io.File, String)}.
	 *             <br>
	 *             <br>
	 *             Note: will be removed in the next minor version.
	 */
	@Deprecated
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
		ZipExtensions.unzip(zipFile, toDir);
	}

}
