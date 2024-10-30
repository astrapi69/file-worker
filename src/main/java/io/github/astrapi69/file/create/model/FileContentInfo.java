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
package io.github.astrapi69.file.create.model;

import java.io.File;
import java.util.Arrays;

import io.github.astrapi69.checksum.FileChecksumExtensions;
import io.github.astrapi69.crypt.api.algorithm.ChecksumAlgorithm;
import io.github.astrapi69.file.FileExtensions;
import io.github.astrapi69.file.create.FileFactory;
import io.github.astrapi69.file.read.ReadFileExtensions;
import io.github.astrapi69.file.write.StoreFileExtensions;
import io.github.astrapi69.throwable.RuntimeExceptionDecorator;

/**
 * The class {@link FileContentInfo} represents information about file content, including checksum
 * and binary data.
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public class FileContentInfo extends FileInfo
{

	/** The checksum of the file content. */
	private String checksum;

	/** The binary content of the file. */
	private byte[] content;

	/**
	 * Constructs a new `FileContentInfo` with the given checksum and content.
	 *
	 * @param checksum
	 *            the checksum of the file content
	 * @param content
	 *            the binary content of the file
	 */
	public FileContentInfo(String checksum, byte[] content)
	{
		this.checksum = checksum;
		this.content = content;
	}

	/**
	 * Constructs a new empty `FileContentInfo`.
	 */
	public FileContentInfo()
	{
	}

	/**
	 * Constructs a new `FileContentInfo` from the builder.
	 *
	 * @param b
	 *            the builder used to construct the `FileContentInfo`
	 */
	protected FileContentInfo(FileContentInfoBuilder<?, ?> b)
	{
		super(b);
		this.checksum = b.checksum;
		this.content = b.content;
	}

	/**
	 * Converts a `File` into a `FileContentInfo`, extracting its name, path, checksum, and content.
	 *
	 * @param file
	 *            the file to convert
	 * @return the `FileContentInfo` representation of the file
	 */
	public static FileContentInfo toFileContentInfo(File file)
	{
		if (file.exists() && file.isFile())
		{
			return FileContentInfo.builder().name(file.getName())
				.path(FileExtensions.getAbsolutPathWithoutFilename(file))
				.checksum(RuntimeExceptionDecorator.decorate(
					() -> FileChecksumExtensions.getChecksum(file, ChecksumAlgorithm.MD5)))
				.content(RuntimeExceptionDecorator
					.decorate(() -> ReadFileExtensions.readFileToBytearray(file)))
				.build();
		}
		return FileContentInfo.builder().name(file.getName()).directory(true)
			.path(FileExtensions.getAbsolutPathWithoutFilename(file)).build();
	}

	/**
	 * Converts a `FileContentInfo` into a `File`.
	 *
	 * @param fileContentInfo
	 *            the `FileContentInfo` to convert
	 * @return the `File` representation of the `FileContentInfo`
	 */
	public static File toFile(FileContentInfo fileContentInfo)
	{
		File file = new File(fileContentInfo.getPath(), fileContentInfo.getName());
		if (!fileContentInfo.isDirectory())
		{
			if (!file.exists())
			{
				RuntimeExceptionDecorator.decorate(() -> FileFactory.newFile(file));
			}
			RuntimeExceptionDecorator
				.decorate(() -> StoreFileExtensions.toFile(file, fileContentInfo.getContent()));
		}
		return file;
	}

	/**
	 * Returns a builder for constructing `FileContentInfo` objects.
	 *
	 * @return a new instance of `FileContentInfoBuilder`
	 */
	public static FileContentInfoBuilder<?, ?> builder()
	{
		return new FileContentInfoBuilderImpl();
	}

	/**
	 * Retrieves the checksum of the file content.
	 *
	 * @return the checksum of the file content
	 */
	public String getChecksum()
	{
		return this.checksum;
	}

	/**
	 * Sets the checksum of the file content.
	 *
	 * @param checksum
	 *            the new checksum to set
	 */
	public void setChecksum(String checksum)
	{
		this.checksum = checksum;
	}

	/**
	 * Retrieves the binary content of the file.
	 *
	 * @return the binary content of the file
	 */
	public byte[] getContent()
	{
		return this.content;
	}

	/**
	 * Sets the binary content of the file.
	 *
	 * @param content
	 *            the new binary content to set
	 */
	public void setContent(byte[] content)
	{
		this.content = content;
	}

	/**
	 * Indicates whether some other object is "equal to" this one.
	 *
	 * @param o
	 *            the object to compare
	 * @return true if this object is the same as the o argument; false otherwise
	 */
	public boolean equals(final Object o)
	{
		if (o == this)
			return true;
		if (!(o instanceof FileContentInfo))
			return false;
		final FileContentInfo other = (FileContentInfo)o;
		if (!other.canEqual((Object)this))
			return false;
		final Object this$checksum = this.getChecksum();
		final Object other$checksum = other.getChecksum();
		if (this$checksum == null ? other$checksum != null : !this$checksum.equals(other$checksum))
			return false;
		if (!Arrays.equals(this.getContent(), other.getContent()))
			return false;
		return true;
	}

	/**
	 * Internal method for checking equality with another object.
	 *
	 * @param other
	 *            the object to compare with
	 * @return true if the objects can be considered equal, false otherwise
	 */
	protected boolean canEqual(final Object other)
	{
		return other instanceof FileContentInfo;
	}

	/**
	 * Returns a hash code value for the object.
	 *
	 * @return a hash code value for this object
	 */
	public int hashCode()
	{
		final int PRIME = 59;
		int result = 1;
		final Object $checksum = this.getChecksum();
		result = result * PRIME + ($checksum == null ? 43 : $checksum.hashCode());
		result = result * PRIME + Arrays.hashCode(this.getContent());
		return result;
	}

	/**
	 * Returns a string representation of the object.
	 *
	 * @return a string representation of the object
	 */
	public String toString()
	{
		return "FileContentInfo{" + "checksum='" + this.getChecksum() + '\'' + ", content="
			+ Arrays.toString(this.getContent()) + '}';
	}

	/**
	 * The builder class for constructing `FileContentInfo` objects.
	 *
	 * @param <C>
	 *            the type of `FileContentInfo` to build
	 * @param <B>
	 *            the type of builder for `FileContentInfo`
	 */
	public static abstract class FileContentInfoBuilder<C extends FileContentInfo, B extends FileContentInfoBuilder<C, B>>
		extends
			FileInfoBuilder<C, B>
	{
		private String checksum;
		private byte[] content;

		/**
		 * Sets the checksum of the file content.
		 *
		 * @param checksum
		 *            the checksum to set
		 * @return this builder instance
		 */
		public B checksum(String checksum)
		{
			this.checksum = checksum;
			return self();
		}

		/**
		 * Sets the binary content of the file.
		 *
		 * @param content
		 *            the binary content to set
		 * @return this builder instance
		 */
		public B content(byte[] content)
		{
			this.content = content;
			return self();
		}

		/**
		 * Returns this builder instance.
		 *
		 * @return this builder instance
		 */
		protected abstract B self();

		/**
		 * Builds the `FileContentInfo` object.
		 *
		 * @return the constructed `FileContentInfo` object
		 */
		public abstract C build();

		/**
		 * Returns a string representation of the builder.
		 *
		 * @return a string representation of the builder
		 */
		public String toString()
		{
			return "FileContentInfo.FileContentInfoBuilder(super=" + super.toString()
				+ ", checksum='" + this.checksum + '\'' + ", content="
				+ Arrays.toString(this.content) + ")";
		}
	}

	/**
	 * The concrete implementation of the `FileContentInfoBuilder`.
	 */
	private static final class FileContentInfoBuilderImpl
		extends
			FileContentInfoBuilder<FileContentInfo, FileContentInfoBuilderImpl>
	{

		/**
		 * Constructs a new `FileContentInfoBuilderImpl`.
		 */
		private FileContentInfoBuilderImpl()
		{
		}

		/**
		 * Returns this builder instance.
		 *
		 * @return this builder instance
		 */
		protected FileContentInfoBuilderImpl self()
		{
			return this;
		}

		/**
		 * Builds the `FileContentInfo` object.
		 *
		 * @return the constructed `FileContentInfo` object
		 */
		public FileContentInfo build()
		{
			return new FileContentInfo(this);
		}
	}
}
