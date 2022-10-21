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
import java.util.Objects;

/**
 * The class {@link FileInfo} provides data for create a {@link File} Object
 */
public class FileInfo
{

	/** The name of this file */
	private String name;
	/** The path of this file */
	private String path;
	/** The flag if this file is a directory */
	private boolean directory;

	public FileInfo(String name, String path, boolean directory)
	{
		this.name = name;
		this.path = path;
		this.directory = directory;
	}

	public FileInfo()
	{
	}

	public FileInfo(final File file)
	{
		Objects.requireNonNull(file);
		this.path = file.getParentFile().getAbsolutePath();
		this.name = file.getName();
		this.directory = file.isDirectory();
	}

	protected FileInfo(FileInfoBuilder<?, ?> b)
	{
		this.name = b.name;
		this.path = b.path;
		this.directory = b.directory;
	}

	/**
	 * Factory method for creating only the new {@link File} object from the given {@link FileInfo}
	 * object. This means that if the file does not exist it will not be created, if you want this
	 * behavior use the {@link FileFactory#newFile(FileInfo)} or
	 * {@link FileFactory#newFileQuietly(FileInfo)} method
	 *
	 * @param fileInfo
	 *            the {@link FileInfo} object
	 * @return the new {@link File} object
	 */
	public static File toFile(FileInfo fileInfo)
	{
		return new File(fileInfo.getPath(), fileInfo.getName());
	}

	/**
	 * Converts the given {@link File} object to a {@link FileInfo} object
	 *
	 * @param file
	 *            the {@link File} object to convert to a {@link FileInfo} object
	 * @return a new {@link FileInfo} object from the given {@link File} object
	 */
	public static FileInfo toFileInfo(final File file)
	{
		Objects.requireNonNull(file);
		return FileInfo.builder().path(file.getParentFile().getAbsolutePath()).name(file.getName())
			.directory(file.isDirectory()).build();
	}

	public static FileInfoBuilder<?, ?> builder()
	{
		return new FileInfoBuilderImpl();
	}

	public String getName()
	{
		return this.name;
	}

	public String getPath()
	{
		return this.path;
	}

	public boolean isDirectory()
	{
		return this.directory;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setPath(String path)
	{
		this.path = path;
	}

	public void setDirectory(boolean directory)
	{
		this.directory = directory;
	}

	public boolean equals(final Object o)
	{
		if (o == this)
			return true;
		if (!(o instanceof FileInfo))
			return false;
		final FileInfo other = (FileInfo)o;
		if (!other.canEqual((Object)this))
			return false;
		final Object this$name = this.getName();
		final Object other$name = other.getName();
		if (this$name == null ? other$name != null : !this$name.equals(other$name))
			return false;
		final Object this$path = this.getPath();
		final Object other$path = other.getPath();
		if (this$path == null ? other$path != null : !this$path.equals(other$path))
			return false;
		if (this.isDirectory() != other.isDirectory())
			return false;
		return true;
	}

	protected boolean canEqual(final Object other)
	{
		return other instanceof FileInfo;
	}

	public int hashCode()
	{
		final int PRIME = 59;
		int result = 1;
		final Object $name = this.getName();
		result = result * PRIME + ($name == null ? 43 : $name.hashCode());
		final Object $path = this.getPath();
		result = result * PRIME + ($path == null ? 43 : $path.hashCode());
		result = result * PRIME + (this.isDirectory() ? 79 : 97);
		return result;
	}

	public String toString()
	{
		return "FileInfo(name=" + this.getName() + ", path=" + this.getPath() + ", directory="
			+ this.isDirectory() + ")";
	}

	public FileInfoBuilder<?, ?> toBuilder()
	{
		return new FileInfoBuilderImpl().$fillValuesFrom(this);
	}

	public static abstract class FileInfoBuilder<C extends FileInfo, B extends FileInfoBuilder<C, B>>
	{
		private String name;
		private String path;
		private boolean directory;

		private static void $fillValuesFromInstanceIntoBuilder(FileInfo instance,
			FileInfoBuilder<?, ?> b)
		{
			b.name(instance.name);
			b.path(instance.path);
			b.directory(instance.directory);
		}

		public B name(String name)
		{
			this.name = name;
			return self();
		}

		public B path(String path)
		{
			this.path = path;
			return self();
		}

		public B directory(boolean directory)
		{
			this.directory = directory;
			return self();
		}

		protected B $fillValuesFrom(C instance)
		{
			FileInfoBuilder.$fillValuesFromInstanceIntoBuilder(instance, this);
			return self();
		}

		protected abstract B self();

		public abstract C build();

		public String toString()
		{
			return "FileInfo.FileInfoBuilder(name=" + this.name + ", path=" + this.path
				+ ", directory=" + this.directory + ")";
		}
	}

	private static final class FileInfoBuilderImpl
		extends
			FileInfoBuilder<FileInfo, FileInfoBuilderImpl>
	{
		private FileInfoBuilderImpl()
		{
		}

		protected FileInfoBuilderImpl self()
		{
			return this;
		}

		public FileInfo build()
		{
			return new FileInfo(this);
		}
	}
}
