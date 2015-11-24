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

import java.io.File;
import java.io.FilenameFilter;
import java.util.zip.ZipFile;

/**
 * The Interface ZipModel.
 * 
 * @version 1.0
 * @author Asterios Raptis
 */
public interface ZipModel
{

	/**
	 * Returns the field <code>compressionMethod</code>.
	 * 
	 * @return The field <code>compressionMethod</code>.
	 */
	public int getCompressionMethod();

	/**
	 * Returns the field <code>directoryToZip</code>.
	 * 
	 * @return The field <code>directoryToZip</code>.
	 */
	public File getDirectoryToZip();

	/**
	 * Returns the field <code>dirToStart</code>.
	 * 
	 * @return The field <code>dirToStart</code>.
	 */
	public String getDirToStart();

	/**
	 * Returns the field <code>fileCounter</code>.
	 * 
	 * @return The field <code>fileCounter</code>.
	 */
	public int getFileCounter();

	/**
	 * Returns the field <code>fileFilter</code>.
	 * 
	 * @return The field <code>fileFilter</code>.
	 */
	public FilenameFilter getFileFilter();

	/**
	 * Returns the field <code>fileLength</code>.
	 * 
	 * @return The field <code>fileLength</code>.
	 */
	public long getFileLength();

	/**
	 * Returns the field <code>zipFile</code>.
	 * 
	 * @return The field <code>zipFile</code>.
	 */
	public File getZipFile();

	/**
	 * Returns the field <code>zipFileComment</code>.
	 * 
	 * @return The field <code>zipFileComment</code>.
	 */
	public String getZipFileComment();

	/**
	 * Returns the field <code>zipFileName</code>.
	 * 
	 * @return The field <code>zipFileName</code>.
	 */
	public String getZipFileName();

	/**
	 * Returns the field <code>zipFileObj</code>.
	 * 
	 * @return The field <code>zipFileObj</code>.
	 */
	public ZipFile getZipFileObj();

	/**
	 * Returns the field <code>zipLevel</code>.
	 * 
	 * @return The field <code>zipLevel</code>.
	 */
	public int getZipLevel();

	/**
	 * Sets the field <code>compressionMethod</code>.
	 * 
	 * @param compressionMethod
	 *            The <code>compressionMethod</code> to set
	 */
	public void setCompressionMethod(final int compressionMethod);

	/**
	 * Sets the field <code>directoryToZip</code>.
	 * 
	 * @param directoryToZip
	 *            The <code>directoryToZip</code> to set
	 */
	public void setDirectoryToZip(final File directoryToZip);

	/**
	 * Sets the field <code>dirToStart</code>.
	 * 
	 * @param dirToStart
	 *            The <code>dirToStart</code> to set
	 */
	public void setDirToStart(final String dirToStart);

	/**
	 * Sets the field <code>fileCounter</code>.
	 * 
	 * @param fileCounter
	 *            The <code>fileCounter</code> to set
	 */
	public void setFileCounter(final int fileCounter);

	/**
	 * Sets the field <code>fileFilter</code>.
	 * 
	 * @param fileFilter
	 *            The <code>fileFilter</code> to set
	 */
	public void setFileFilter(final FilenameFilter fileFilter);

	/**
	 * Sets the field <code>fileLength</code>.
	 * 
	 * @param fileLength
	 *            The <code>fileLength</code> to set
	 */
	public void setFileLength(final long fileLength);

	/**
	 * Sets the field <code>zipFile</code>.
	 * 
	 * @param zipFile
	 *            The <code>zipFile</code> to set
	 */
	public void setZipFile(final File zipFile);

	/**
	 * Sets the field <code>zipFileComment</code>.
	 * 
	 * @param zipFileComment
	 *            The <code>zipFileComment</code> to set
	 */
	public void setZipFileComment(final String zipFileComment);

	/**
	 * Sets the field <code>zipFileName</code>.
	 * 
	 * @param zipFileName
	 *            The <code>zipFileName</code> to set
	 */
	public void setZipFileName(final String zipFileName);

	/**
	 * Sets the field <code>zipFileObj</code>.
	 * 
	 * @param zipFileObj
	 *            The <code>zipFileObj</code> to set
	 */
	public void setZipFileObj(final ZipFile zipFileObj);

	/**
	 * Sets the field <code>zipLevel</code>.
	 * 
	 * @param zipLevel
	 *            The <code>zipLevel</code> to set
	 */
	public void setZipLevel(final int zipLevel);

}