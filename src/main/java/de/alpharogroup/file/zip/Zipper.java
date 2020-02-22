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
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * The class {@link Zipper}
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public class Zipper implements ZipModel
{

	public static class ZipperBuilder
	{
		private int compressionMethod;
		private File directoryToZip;
		private String dirToStart;
		private int fileCounter;
		private FilenameFilter fileFilter;
		private long fileLength;
		private File zipFile;
		private String zipFileComment;
		private String zipFileName;
		private ZipFile zipFileObj;
		private int zipLevel;

		ZipperBuilder()
		{
		}

		public Zipper build()
		{
			return new Zipper(compressionMethod, directoryToZip, dirToStart, fileCounter,
				fileFilter, fileLength, zipFile, zipFileComment, zipFileName, zipFileObj, zipLevel);
		}

		public Zipper.ZipperBuilder compressionMethod(int compressionMethod)
		{
			this.compressionMethod = compressionMethod;
			return this;
		}

		public Zipper.ZipperBuilder directoryToZip(File directoryToZip)
		{
			this.directoryToZip = directoryToZip;
			return this;
		}

		public Zipper.ZipperBuilder dirToStart(String dirToStart)
		{
			this.dirToStart = dirToStart;
			return this;
		}

		public Zipper.ZipperBuilder fileCounter(int fileCounter)
		{
			this.fileCounter = fileCounter;
			return this;
		}

		public Zipper.ZipperBuilder fileFilter(FilenameFilter fileFilter)
		{
			this.fileFilter = fileFilter;
			return this;
		}

		public Zipper.ZipperBuilder fileLength(long fileLength)
		{
			this.fileLength = fileLength;
			return this;
		}

		@Override
		public String toString()
		{
			return "Zipper.ZipperBuilder(compressionMethod=" + this.compressionMethod
				+ ", directoryToZip=" + this.directoryToZip + ", dirToStart=" + this.dirToStart
				+ ", fileCounter=" + this.fileCounter + ", fileFilter=" + this.fileFilter
				+ ", fileLength=" + this.fileLength + ", zipFile=" + this.zipFile
				+ ", zipFileComment=" + this.zipFileComment + ", zipFileName=" + this.zipFileName
				+ ", zipFileObj=" + this.zipFileObj + ", zipLevel=" + this.zipLevel + ")";
		}

		public Zipper.ZipperBuilder zipFile(File zipFile)
		{
			this.zipFile = zipFile;
			return this;
		}

		public Zipper.ZipperBuilder zipFileComment(String zipFileComment)
		{
			this.zipFileComment = zipFileComment;
			return this;
		}

		public Zipper.ZipperBuilder zipFileName(String zipFileName)
		{
			this.zipFileName = zipFileName;
			return this;
		}

		public Zipper.ZipperBuilder zipFileObj(ZipFile zipFileObj)
		{
			this.zipFileObj = zipFileObj;
			return this;
		}

		public Zipper.ZipperBuilder zipLevel(int zipLevel)
		{
			this.zipLevel = zipLevel;
			return this;
		}
	}

	private static final Logger log = Logger.getLogger(Zipper.class.getName());

	public static ZipperBuilder builder()
	{
		return new ZipperBuilder();
	}

	/** The compression method. */
	private int compressionMethod;

	/** The directory to zip. */
	private File directoryToZip;

	/** The dir to start. */
	private String dirToStart;

	/** The file counter. */
	private int fileCounter;

	/** The file filter. */
	private FilenameFilter fileFilter;

	/** The file length. */
	private long fileLength;

	/** The zip file. */
	private File zipFile;

	/** The zip file comment. */
	private String zipFileComment;

	/** The zip file name. */
	private String zipFileName;

	/** The zip file obj. */
	private ZipFile zipFileObj;

	/** The zip level. */
	private int zipLevel;

	{
		fileLength = 0;
		fileCounter = 0;
		zipLevel = -1;
		compressionMethod = -1;
	}

	public Zipper()
	{
	}

	/**
	 * Instantiates a new {@link Zipper} object
	 *
	 * @param dirToZip
	 *            the dir to zip
	 * @param zipFile
	 *            the zip file
	 */
	public Zipper(final File dirToZip, final File zipFile)
	{
		this(dirToZip, zipFile, null);
	}

	/**
	 * Instantiates a new {@link Zipper} object
	 *
	 * @param dirToZip
	 *            the dir to zip
	 * @param zipFile
	 *            the zip file
	 * @param filter
	 *            the filter
	 */
	public Zipper(final File dirToZip, final File zipFile, final FilenameFilter filter)
	{
		this.directoryToZip = dirToZip;
		this.zipFile = zipFile;
		this.setFileFilter(filter);
	}

	public Zipper(int compressionMethod, File directoryToZip, String dirToStart, int fileCounter,
		FilenameFilter fileFilter, long fileLength, File zipFile, String zipFileComment,
		String zipFileName, ZipFile zipFileObj, int zipLevel)
	{
		this.compressionMethod = compressionMethod;
		this.directoryToZip = directoryToZip;
		this.dirToStart = dirToStart;
		this.fileCounter = fileCounter;
		this.fileFilter = fileFilter;
		this.fileLength = fileLength;
		this.zipFile = zipFile;
		this.zipFileComment = zipFileComment;
		this.zipFileName = zipFileName;
		this.zipFileObj = zipFileObj;
		this.zipLevel = zipLevel;
	}

	@Override
	public int getCompressionMethod()
	{
		return this.compressionMethod;
	}

	@Override
	public File getDirectoryToZip()
	{
		return this.directoryToZip;
	}

	@Override
	public String getDirToStart()
	{
		return this.dirToStart;
	}

	@Override
	public int getFileCounter()
	{
		return this.fileCounter;
	}

	@Override
	public FilenameFilter getFileFilter()
	{
		return this.fileFilter;
	}

	@Override
	public long getFileLength()
	{
		return this.fileLength;
	}

	@Override
	public File getZipFile()
	{
		return this.zipFile;
	}

	@Override
	public String getZipFileComment()
	{
		return this.zipFileComment;
	}

	@Override
	public String getZipFileName()
	{
		return this.zipFileName;
	}

	@Override
	public ZipFile getZipFileObj()
	{
		return this.zipFileObj;
	}

	@Override
	public int getZipLevel()
	{
		return this.zipLevel;
	}

	@Override
	public void setCompressionMethod(int compressionMethod)
	{
		this.compressionMethod = compressionMethod;
	}

	@Override
	public void setDirectoryToZip(File directoryToZip)
	{
		this.directoryToZip = directoryToZip;
	}

	@Override
	public void setDirToStart(String dirToStart)
	{
		this.dirToStart = dirToStart;
	}

	@Override
	public void setFileCounter(int fileCounter)
	{
		this.fileCounter = fileCounter;
	}

	@Override
	public void setFileFilter(FilenameFilter fileFilter)
	{
		this.fileFilter = fileFilter;
	}

	@Override
	public void setFileLength(long fileLength)
	{
		this.fileLength = fileLength;
	}

	@Override
	public void setZipFile(File zipFile)
	{
		this.zipFile = zipFile;
	}

	@Override
	public void setZipFileComment(String zipFileComment)
	{
		this.zipFileComment = zipFileComment;
	}

	@Override
	public void setZipFileName(String zipFileName)
	{
		this.zipFileName = zipFileName;
	}

	@Override
	public void setZipFileObj(ZipFile zipFileObj)
	{
		this.zipFileObj = zipFileObj;
	}

	@Override
	public void setZipLevel(int zipLevel)
	{
		this.zipLevel = zipLevel;
	}

	public ZipperBuilder toBuilder()
	{
		return new ZipperBuilder().compressionMethod(this.compressionMethod)
			.directoryToZip(this.directoryToZip).dirToStart(this.dirToStart)
			.fileCounter(this.fileCounter).fileFilter(this.fileFilter).fileLength(this.fileLength)
			.zipFile(this.zipFile).zipFileComment(this.zipFileComment).zipFileName(this.zipFileName)
			.zipFileObj(this.zipFileObj).zipLevel(this.zipLevel);
	}

	@Override
	public String toString()
	{
		return "Zipper(compressionMethod=" + this.getCompressionMethod() + ", directoryToZip="
			+ this.getDirectoryToZip() + ", dirToStart=" + this.getDirToStart() + ", fileCounter="
			+ this.getFileCounter() + ", fileFilter=" + this.getFileFilter() + ", fileLength="
			+ this.getFileLength() + ", zipFile=" + this.getZipFile() + ", zipFileComment="
			+ this.getZipFileComment() + ", zipFileName=" + this.getZipFileName() + ", zipFileObj="
			+ this.getZipFileObj() + ", zipLevel=" + this.getZipLevel() + ")";
	}

	/**
	 * Zip the given files of this objects
	 *
	 * @return the optional with the possibles errors
	 */
	public Optional<ZipErrorCodes> zip()
	{
		try (FileOutputStream fos = new FileOutputStream(this.zipFile);
			ZipOutputStream zos = new ZipOutputStream(fos))
		{

			if (!this.directoryToZip.exists())
			{
				return Optional.of(ZipErrorCodes.DIRECTORY_TO_ZIP_DOES_NOT_EXIST);
			}
			if (!this.zipFile.exists())
			{
				return Optional.of(ZipErrorCodes.ZIP_FILE_DOES_NOT_EXIST);
			}
			if (0 < this.zipLevel)
			{
				zos.setLevel(this.zipLevel);
			}
			else
			{
				zos.setLevel(9);
			}
			if (null != this.zipFileComment)
			{
				zos.setComment(this.zipFileComment);
			}
			if (0 < this.compressionMethod)
			{
				zos.setMethod(this.compressionMethod);
			}
			this.zipFiles(this.directoryToZip, zos);
			zos.flush();
			zos.finish();
			fos.flush();
		}
		catch (IOException e)
		{
			log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			return Optional.of(ZipErrorCodes.IO_ERROR);
		}
		return Optional.empty();
	}

	/**
	 * Zip files.
	 *
	 * @param file
	 *            the file
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private void zipFiles(final File file, ZipOutputStream zos) throws IOException
	{
		if (file.isDirectory())
		{
			List<File> foundedFiles;
			if (null != this.fileFilter)
			{
				foundedFiles = ZipExtensions.getFoundedFiles(file, file.listFiles(this.fileFilter));
			}
			else
			{
				foundedFiles = Arrays.asList(file.listFiles());
			}
			for (final File foundedFile : foundedFiles)
			{
				this.zipFiles(foundedFile, zos);
			}
		}
		else
		{
			this.fileLength += file.length();
			this.fileCounter++;
			final String absolutePath = file.getAbsolutePath();
			if (this.dirToStart == null)
			{
				this.dirToStart = this.directoryToZip.getName();
			}
			final int index = absolutePath.indexOf(this.dirToStart);
			final String zipEntryName = absolutePath.substring(index, absolutePath.length());
			final byte[] b = new byte[(int)file.length()];
			final ZipEntry cpZipEntry = new ZipEntry(zipEntryName);
			zos.putNextEntry(cpZipEntry);
			zos.write(b, 0, (int)file.length());
			zos.closeEntry();
		}
	}
}
