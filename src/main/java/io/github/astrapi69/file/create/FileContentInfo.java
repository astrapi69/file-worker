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
package io.github.astrapi69.file.create;

import java.io.File;

import io.github.astrapi69.checksum.FileChecksumExtensions;
import io.github.astrapi69.crypt.api.algorithm.ChecksumAlgorithm;
import io.github.astrapi69.file.FileExtensions;
import io.github.astrapi69.file.read.ReadFileExtensions;
import io.github.astrapi69.file.write.WriteFileExtensions;
import io.github.astrapi69.throwable.RuntimeExceptionDecorator;

/**
 * The class {@link FileContentInfo}
 */
public class FileContentInfo extends FileInfo
{
	/** The checksum from this resource. */
	private String checksum;
	/** The binary data from this resource. */
	private byte[] content;

	public FileContentInfo(String checksum, byte[] content)
	{
		this.checksum = checksum;
		this.content = content;
	}

	public FileContentInfo()
	{
	}

	protected FileContentInfo(FileContentInfoBuilder<?, ?> b)
	{
		super(b);
		this.checksum = b.checksum;
		this.content = b.content;
	}

	public static FileContentInfo toFileContentInfo(File file)
	{
		if (file.exists())
		{
			return FileContentInfo.builder().name(file.getName())
				.path(FileExtensions.getAbsolutPathWithoutFilename(file))
				.checksum(RuntimeExceptionDecorator.decorate(
					() -> FileChecksumExtensions.getChecksum(file, ChecksumAlgorithm.MD5)))
				.content(RuntimeExceptionDecorator
					.decorate(() -> ReadFileExtensions.readFileToBytearray(file)))
				.build();
		}
		return FileContentInfo.builder().name(file.getName())
			.path(FileExtensions.getAbsolutPathWithoutFilename(file)).build();
	}

	public static File toFile(FileContentInfo fileContentInfo)
	{
		File file = new File(fileContentInfo.getPath(), fileContentInfo.getName());
		if (!fileContentInfo.isDirectory())
		{
			if (!file.exists())
			{
				RuntimeExceptionDecorator.decorate(() -> FileFactory.newFile(file));
			}
			RuntimeExceptionDecorator.decorate(
				() -> WriteFileExtensions.storeByteArrayToFile(fileContentInfo.getContent(), file));
		}
		return file;
	}

	public static FileContentInfoBuilder<?, ?> builder()
	{
		return new FileContentInfoBuilderImpl();
	}

	public String getChecksum()
	{
		return this.checksum;
	}

	public void setChecksum(String checksum)
	{
		this.checksum = checksum;
	}

	public byte[] getContent()
	{
		return this.content;
	}

	public void setContent(byte[] content)
	{
		this.content = content;
	}

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
		if (!java.util.Arrays.equals(this.getContent(), other.getContent()))
			return false;
		return true;
	}

	protected boolean canEqual(final Object other)
	{
		return other instanceof FileContentInfo;
	}

	public int hashCode()
	{
		final int PRIME = 59;
		int result = 1;
		final Object $checksum = this.getChecksum();
		result = result * PRIME + ($checksum == null ? 43 : $checksum.hashCode());
		result = result * PRIME + java.util.Arrays.hashCode(this.getContent());
		return result;
	}

	public String toString()
	{
		return "FileContentInfo(checksum=" + this.getChecksum() + ", content="
			+ java.util.Arrays.toString(this.getContent()) + ")";
	}

	public static abstract class FileContentInfoBuilder<C extends FileContentInfo, B extends FileContentInfoBuilder<C, B>>
		extends
			FileInfoBuilder<C, B>
	{
		private String checksum;
		private byte[] content;

		public B checksum(String checksum)
		{
			this.checksum = checksum;
			return self();
		}

		public B content(byte[] content)
		{
			this.content = content;
			return self();
		}

		protected abstract B self();

		public abstract C build();

		public String toString()
		{
			return "FileContentInfo.FileContentInfoBuilder(super=" + super.toString()
				+ ", checksum=" + this.checksum + ", content="
				+ java.util.Arrays.toString(this.content) + ")";
		}
	}

	private static final class FileContentInfoBuilderImpl
		extends
			FileContentInfoBuilder<FileContentInfo, FileContentInfoBuilderImpl>
	{
		private FileContentInfoBuilderImpl()
		{
		}

		protected FileContentInfoBuilderImpl self()
		{
			return this;
		}

		public FileContentInfo build()
		{
			return new FileContentInfo(this);
		}
	}
}
