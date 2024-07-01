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
 * The {@code Zipper} class provides functionality to create ZIP archives from directories or files.
 * It supports various compression methods and allows customization through builder pattern.
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public class Zipper implements ZipModel
{
	/** The logger */
	private static final Logger log = Logger.getLogger(Zipper.class.getName());
	/** The compression method */
	private int compressionMethod;
	/** The directory to zip */
	private File directoryToZip;
	/** The directory to start */
	private String dirToStart;
	/** The file counter. */
	private int fileCounter;
	/** The file filter */
	private FilenameFilter fileFilter;
	/** The file length */
	private long fileLength;
	/** The zip file */
	private File zipFile;
	/** The zip file comment */
	private String zipFileComment;
	/** The zip file name */
	private String zipFileName;
	/** The zip file object */
	private ZipFile zipFileObj;
	/** The zip level */
	private int zipLevel;

	{
		fileLength = 0;
		fileCounter = 0;
		zipLevel = -1;
		compressionMethod = -1;
	}

	/**
	 * Instantiates a new {@link Zipper} object
	 */
	public Zipper()
	{
	}

	/**
	 * Instantiates a new {@link Zipper} object
	 *
	 * @param dirToZip
	 *            the directory to zip
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
	 *            the directory to zip
	 * @param zipFile
	 *            the zip file
	 * @param filter
	 *            the file name filter
	 */
	public Zipper(final File dirToZip, final File zipFile, final FilenameFilter filter)
	{
		this.directoryToZip = dirToZip;
		this.zipFile = zipFile;
		this.setFileFilter(filter);
	}

	/**
	 * Instantiates a new {@link Zipper} object with detailed parameters.
	 *
	 * @param compressionMethod
	 *            the compression method
	 * @param directoryToZip
	 *            the directory to zip
	 * @param dirToStart
	 *            the directory to start
	 * @param fileCounter
	 *            the file counter
	 * @param fileFilter
	 *            the file name filter
	 * @param fileLength
	 *            the total length of files being zipped
	 * @param zipFile
	 *            the zip file
	 * @param zipFileComment
	 *            the zip file comment
	 * @param zipFileName
	 *            the zip file name
	 * @param zipFileObj
	 *            the zip file object
	 * @param zipLevel
	 *            the compression level
	 */
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

	/**
	 * Provides a builder to construct a {@code Zipper} object with desired parameters.
	 *
	 * @return a new {@code ZipperBuilder} instance
	 */
	public static ZipperBuilder builder()
	{
		return new ZipperBuilder();
	}

	/**
	 * Gets the compression method used for zipping.
	 *
	 * @return the compression method
	 */
	@Override
	public int getCompressionMethod()
	{
		return this.compressionMethod;
	}

	/**
	 * Sets the compression method for zipping.
	 *
	 * @param compressionMethod
	 *            the new compression method
	 */
	@Override
	public void setCompressionMethod(int compressionMethod)
	{
		this.compressionMethod = compressionMethod;
	}

	/**
	 * Gets the directory to be zipped.
	 *
	 * @return the directory to zip
	 */
	@Override
	public File getDirectoryToZip()
	{
		return this.directoryToZip;
	}

	/**
	 * Sets the directory to be zipped.
	 *
	 * @param directoryToZip
	 *            the new directory to zip
	 */
	@Override
	public void setDirectoryToZip(File directoryToZip)
	{
		this.directoryToZip = directoryToZip;
	}

	/**
	 * Gets the starting directory within the ZIP archive.
	 *
	 * @return the directory to start zipping
	 */
	@Override
	public String getDirToStart()
	{
		return this.dirToStart;
	}

	/**
	 * Sets the starting directory within the ZIP archive.
	 *
	 * @param dirToStart
	 *            the new directory to start zipping
	 */
	@Override
	public void setDirToStart(String dirToStart)
	{
		this.dirToStart = dirToStart;
	}

	/**
	 * Gets the total number of files zipped.
	 *
	 * @return the file counter
	 */
	@Override
	public int getFileCounter()
	{
		return this.fileCounter;
	}

	/**
	 * Sets the total number of files zipped.
	 *
	 * @param fileCounter
	 *            the new file counter
	 */
	@Override
	public void setFileCounter(int fileCounter)
	{
		this.fileCounter = fileCounter;
	}

	/**
	 * Gets the file name filter used during zipping.
	 *
	 * @return the file name filter
	 */
	@Override
	public FilenameFilter getFileFilter()
	{
		return this.fileFilter;
	}

	/**
	 * Sets the file name filter used during zipping.
	 *
	 * @param fileFilter
	 *            the new file name filter
	 */
	@Override
	public void setFileFilter(FilenameFilter fileFilter)
	{
		this.fileFilter = fileFilter;
	}

	/**
	 * Gets the total length of files being zipped.
	 *
	 * @return the total file length
	 */
	@Override
	public long getFileLength()
	{
		return this.fileLength;
	}

	/**
	 * Sets the total length of files being zipped.
	 *
	 * @param fileLength
	 *            the new total file length
	 */
	@Override
	public void setFileLength(long fileLength)
	{
		this.fileLength = fileLength;
	}

	/**
	 * Gets the target ZIP file object.
	 *
	 * @return the zip file
	 */
	@Override
	public File getZipFile()
	{
		return this.zipFile;
	}

	/**
	 * Sets the target ZIP file object.
	 *
	 * @param zipFile
	 *            the new zip file
	 */
	@Override
	public void setZipFile(File zipFile)
	{
		this.zipFile = zipFile;
	}

	/**
	 * Gets the comment associated with the ZIP file.
	 *
	 * @return the zip file comment
	 */
	@Override
	public String getZipFileComment()
	{
		return this.zipFileComment;
	}

	/**
	 * Sets the comment associated with the ZIP file.
	 *
	 * @param zipFileComment
	 *            the new zip file comment
	 */
	@Override
	public void setZipFileComment(String zipFileComment)
	{
		this.zipFileComment = zipFileComment;
	}

	/**
	 * Gets the name of the ZIP file.
	 *
	 * @return the zip file name
	 */
	@Override
	public String getZipFileName()
	{
		return this.zipFileName;
	}

	/**
	 * Sets the name of the ZIP file.
	 *
	 * @param zipFileName
	 *            the new zip file name
	 */
	@Override
	public void setZipFileName(String zipFileName)
	{
		this.zipFileName = zipFileName;
	}

	/**
	 * Gets the ZIP file object.
	 *
	 * @return the zip file object
	 */
	@Override
	public ZipFile getZipFileObj()
	{
		return this.zipFileObj;
	}

	/**
	 * Sets the ZIP file object.
	 *
	 * @param zipFileObj
	 *            the new zip file object
	 */
	@Override
	public void setZipFileObj(ZipFile zipFileObj)
	{
		this.zipFileObj = zipFileObj;
	}

	/**
	 * Gets the compression level used during zipping.
	 *
	 * @return the compression level
	 */
	@Override
	public int getZipLevel()
	{
		return this.zipLevel;
	}

	/**
	 * Sets the compression level used during zipping.
	 *
	 * @param zipLevel
	 *            the new compression level
	 */
	@Override
	public void setZipLevel(int zipLevel)
	{
		this.zipLevel = zipLevel;
	}

	/**
	 * Creates a new {@code ZipperBuilder} initialized with current object's properties.
	 *
	 * @return a builder instance with current configuration
	 */
	public ZipperBuilder toBuilder()
	{
		return new ZipperBuilder().compressionMethod(this.compressionMethod)
			.directoryToZip(this.directoryToZip).dirToStart(this.dirToStart)
			.fileCounter(this.fileCounter).fileFilter(this.fileFilter).fileLength(this.fileLength)
			.zipFile(this.zipFile).zipFileComment(this.zipFileComment).zipFileName(this.zipFileName)
			.zipFileObj(this.zipFileObj).zipLevel(this.zipLevel);
	}

	/**
	 * Creates a ZIP archive of the specified directory and its contents.
	 *
	 * @return an optional error code if any issue occurs during the operation
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
			if (this.zipLevel > 0)
			{
				zos.setLevel(this.zipLevel);
			}
			else
			{
				zos.setLevel(9);
			}
			if (this.zipFileComment != null)
			{
				zos.setComment(this.zipFileComment);
			}
			if (this.compressionMethod > 0)
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
	 * Recursively adds files or directories to the ZIP output stream.
	 *
	 * @param file
	 *            the current file or directory to add
	 * @param zos
	 *            the ZIP output stream
	 * @throws IOException
	 *             if an I/O error occurs during zipping
	 */
	private void zipFiles(final File file, ZipOutputStream zos) throws IOException
	{
		if (file.isDirectory())
		{
			List<File> filesToZip;
			if (this.fileFilter != null)
			{
				filesToZip = ZipExtensions.getFoundedFiles(file, file.listFiles(this.fileFilter));
			}
			else
			{
				filesToZip = Arrays.asList(file.listFiles());
			}
			for (final File fileToZip : filesToZip)
			{
				this.zipFiles(fileToZip, zos);
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
			final String zipEntryName = absolutePath.substring(index);
			final byte[] buffer = new byte[(int)file.length()];
			final ZipEntry zipEntry = new ZipEntry(zipEntryName);
			zos.putNextEntry(zipEntry);
			zos.write(buffer, 0, (int)file.length());
			zos.closeEntry();
		}
	}

	/**
	 * Builder class for constructing {@code Zipper} objects with desired parameters.
	 */
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

		/**
		 * Constructs a new {@code ZipperBuilder}.
		 */
		ZipperBuilder()
		{
		}

		/**
		 * Builds a new {@code Zipper} object with the configured parameters.
		 *
		 * @return a new {@code Zipper} instance
		 */
		public Zipper build()
		{
			return new Zipper(compressionMethod, directoryToZip, dirToStart, fileCounter,
				fileFilter, fileLength, zipFile, zipFileComment, zipFileName, zipFileObj, zipLevel);
		}

		/**
		 * Sets the compression method for the ZIP archive being created.
		 *
		 * @param compressionMethod
		 *            the compression method
		 * @return this builder instance for method chaining
		 */
		public ZipperBuilder compressionMethod(int compressionMethod)
		{
			this.compressionMethod = compressionMethod;
			return this;
		}

		/**
		 * Sets the directory to be zipped.
		 *
		 * @param directoryToZip
		 *            the directory to zip
		 * @return this builder instance for method chaining
		 */
		public ZipperBuilder directoryToZip(File directoryToZip)
		{
			this.directoryToZip = directoryToZip;
			return this;
		}

		/**
		 * Sets the starting directory within the ZIP archive.
		 *
		 * @param dirToStart
		 *            the directory to start zipping
		 * @return this builder instance for method chaining
		 */
		public ZipperBuilder dirToStart(String dirToStart)
		{
			this.dirToStart = dirToStart;
			return this;
		}

		/**
		 * Sets the file counter for tracking the number of files zipped.
		 *
		 * @param fileCounter
		 *            the file counter
		 * @return this builder instance for method chaining
		 */
		public ZipperBuilder fileCounter(int fileCounter)
		{
			this.fileCounter = fileCounter;
			return this;
		}

		/**
		 * Sets the filename filter used during zipping.
		 *
		 * @param fileFilter
		 *            the file name filter
		 * @return this builder instance for method chaining
		 */
		public ZipperBuilder fileFilter(FilenameFilter fileFilter)
		{
			this.fileFilter = fileFilter;
			return this;
		}

		/**
		 * Sets the total length of files being zipped.
		 *
		 * @param fileLength
		 *            the total file length
		 * @return this builder instance for method chaining
		 */
		public ZipperBuilder fileLength(long fileLength)
		{
			this.fileLength = fileLength;
			return this;
		}

		/**
		 * Sets the target ZIP file object.
		 *
		 * @param zipFile
		 *            the zip file
		 * @return this builder instance for method chaining
		 */
		public ZipperBuilder zipFile(File zipFile)
		{
			this.zipFile = zipFile;
			return this;
		}

		/**
		 * Sets the comment associated with the ZIP file.
		 *
		 * @param zipFileComment
		 *            the zip file comment
		 * @return this builder instance for method chaining
		 */
		public ZipperBuilder zipFileComment(String zipFileComment)
		{
			this.zipFileComment = zipFileComment;
			return this;
		}

		/**
		 * Sets the name of the ZIP file.
		 *
		 * @param zipFileName
		 *            the zip file name
		 * @return this builder instance for method chaining
		 */
		public ZipperBuilder zipFileName(String zipFileName)
		{
			this.zipFileName = zipFileName;
			return this;
		}

		/**
		 * Sets the ZIP file object.
		 *
		 * @param zipFileObj
		 *            the zip file object
		 * @return this builder instance for method chaining
		 */
		public ZipperBuilder zipFileObj(ZipFile zipFileObj)
		{
			this.zipFileObj = zipFileObj;
			return this;
		}

		/**
		 * Sets the compression level used during zipping.
		 *
		 * @param zipLevel
		 *            the compression level
		 * @return this builder instance for method chaining
		 */
		public ZipperBuilder zipLevel(int zipLevel)
		{
			this.zipLevel = zipLevel;
			return this;
		}
	}
}
