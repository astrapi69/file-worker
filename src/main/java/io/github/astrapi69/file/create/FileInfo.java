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
 * The class {@link FileInfo} represents file information, including name, path, and directory flag.
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public class FileInfo
{

	/** The name of this file. */
	private String name;

	/** The path of this file. */
	private String path;

	/** Flag indicating if this file is a directory. */
	private boolean directory;

	/**
	 * Constructs a new `FileInfo` with the given name, path, and directory flag.
	 *
	 * @param name
	 *            the name of the file
	 * @param path
	 *            the path of the file
	 * @param directory
	 *            true if the file is a directory, false otherwise
	 */
	public FileInfo(String name, String path, boolean directory)
	{
		this.name = name;
		this.path = path;
		this.directory = directory;
	}

	/**
	 * Constructs a new empty `FileInfo`.
	 */
	public FileInfo()
	{
	}

	/**
	 * Constructs a new `FileInfo` from the given {@link File} object.
	 *
	 * @param file
	 *            the file from which to construct the `FileInfo`
	 * @throws NullPointerException
	 *             if the file is null
	 */
	public FileInfo(final File file)
	{
		Objects.requireNonNull(file);
		this.path = file.getParentFile().getAbsolutePath();
		this.name = file.getName();
		this.directory = file.isDirectory();
	}

	/**
	 * Constructs a new `FileInfo` from the builder.
	 *
	 * @param b
	 *            the builder used to construct the `FileInfo`
	 */
	protected FileInfo(FileInfoBuilder<?, ?> b)
	{
		this.name = b.name;
		this.path = b.path;
		this.directory = b.directory;
	}

	/**
	 * Converts the given {@link File} object to a `FileInfo` object.
	 *
	 * @param file
	 *            the file to convert to `FileInfo`
	 * @return a new `FileInfo` object from the given file
	 * @throws NullPointerException
	 *             if the file is null
	 */
	public static FileInfo toFileInfo(final File file)
	{
		Objects.requireNonNull(file);
		return FileInfo.builder().path(file.getParentFile().getAbsolutePath()).name(file.getName())
			.directory(file.isDirectory()).build();
	}

	/**
	 * Converts this `FileInfo` object to a {@link File} object.
	 *
	 * @param fileInfo
	 *            the `FileInfo` object to convert to `File`
	 * @return the new `File` object from this `FileInfo`
	 */
	public static File toFile(FileInfo fileInfo)
	{
		return new File(fileInfo.getPath(), fileInfo.getName());
	}

	/**
	 * Returns a builder for constructing `FileInfo` objects.
	 *
	 * @return a new instance of `FileInfoBuilder`
	 */
	public static FileInfoBuilder<?, ?> builder()
	{
		return new FileInfoBuilderImpl();
	}

	/**
	 * Retrieves the name of the file.
	 *
	 * @return the name of the file
	 */
	public String getName()
	{
		return this.name;
	}

	/**
	 * Sets the name of the file.
	 *
	 * @param name
	 *            the new name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * Retrieves the path of the file.
	 *
	 * @return the path of the file
	 */
	public String getPath()
	{
		return this.path;
	}

	/**
	 * Sets the path of the file.
	 *
	 * @param path
	 *            the new path to set
	 */
	public void setPath(String path)
	{
		this.path = path;
	}

	/**
	 * Checks if the file is a directory.
	 *
	 * @return true if the file is a directory, false otherwise
	 */
	public boolean isDirectory()
	{
		return this.directory;
	}

	/**
	 * Sets whether the file is a directory.
	 *
	 * @param directory
	 *            true if the file is a directory, false otherwise
	 */
	public void setDirectory(boolean directory)
	{
		this.directory = directory;
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
		return this.isDirectory() == other.isDirectory();
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
		return other instanceof FileInfo;
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
		final Object $name = this.getName();
		result = result * PRIME + ($name == null ? 43 : $name.hashCode());
		final Object $path = this.getPath();
		result = result * PRIME + ($path == null ? 43 : $path.hashCode());
		result = result * PRIME + (this.isDirectory() ? 79 : 97);
		return result;
	}

	/**
	 * Returns a string representation of the object.
	 *
	 * @return a string representation of the object
	 */
	public String toString()
	{
		return "FileInfo{" + "name='" + this.getName() + '\'' + ", path='" + this.getPath() + '\''
			+ ", directory=" + this.isDirectory() + '}';
	}

	/**
	 * Returns a builder initialized with the values of this `FileInfo` object.
	 *
	 * @return a new `FileInfoBuilder` initialized with this `FileInfo` values
	 */
	public FileInfoBuilder<?, ?> toBuilder()
	{
		return new FileInfoBuilderImpl().$fillValuesFrom(this);
	}

	/**
	 * The builder class for constructing `FileInfo` objects.
	 *
	 * @param <C>
	 *            the type of `FileInfo` to build
	 * @param <B>
	 *            the type of builder for `FileInfo`
	 */
	public static abstract class FileInfoBuilder<C extends FileInfo, B extends FileInfoBuilder<C, B>>
	{

		/** The name of the file. */
		private String name;

		/** The path of the file. */
		private String path;

		/** Flag indicating if the file is a directory. */
		private boolean directory;

		/**
		 * Internal method to fill the builder with values from an existing `FileInfo` instance.
		 *
		 * @param instance
		 *            the `FileInfo` instance to copy values from
		 */
		private static void $fillValuesFromInstanceIntoBuilder(FileInfo instance,
			FileInfoBuilder<?, ?> b)
		{
			b.name(instance.name);
			b.path(instance.path);
			b.directory(instance.directory);
		}

		/**
		 * Sets the name of the file.
		 *
		 * @param name
		 *            the name of the file
		 * @return this builder instance
		 */
		public B name(String name)
		{
			this.name = name;
			return self();
		}

		/**
		 * Sets the path of the file.
		 *
		 * @param path
		 *            the path of the file
		 * @return this builder instance
		 */
		public B path(String path)
		{
			this.path = path;
			return self();
		}

		/**
		 * Sets whether the file is a directory.
		 *
		 * @param directory
		 *            true if the file is a directory, false otherwise
		 * @return this builder instance
		 */
		public B directory(boolean directory)
		{
			this.directory = directory;
			return self();
		}

		/**
		 * Internal method to fill the builder with values from an existing `FileInfo` instance.
		 *
		 * @param instance
		 *            the `FileInfo` instance to copy values from
		 */
		protected B $fillValuesFrom(C instance)
		{
			FileInfoBuilder.$fillValuesFromInstanceIntoBuilder(instance, this);
			return self();
		}

		/**
		 * Returns this builder instance.
		 *
		 * @return this builder instance
		 */
		protected abstract B self();

		/**
		 * Builds the `FileInfo` object.
		 *
		 * @return the constructed `FileInfo` object
		 */
		public abstract C build();

		/**
		 * Returns a string representation of the builder.
		 *
		 * @return a string representation of the builder
		 */
		public String toString()
		{
			return "FileInfoBuilder{" + "name='" + this.name + '\'' + ", path='" + this.path + '\''
				+ ", directory=" + this.directory + '}';
		}
	}

	/**
	 * The concrete builder class for constructing `FileInfo` objects.
	 */
	private static final class FileInfoBuilderImpl
		extends
			FileInfoBuilder<FileInfo, FileInfoBuilderImpl>
	{

		/**
		 * Constructs a new `FileInfoBuilderImpl`.
		 */
		private FileInfoBuilderImpl()
		{
		}

		/**
		 * Returns this builder instance.
		 *
		 * @return this builder instance
		 */
		protected FileInfoBuilderImpl self()
		{
			return this;
		}

		/**
		 * Builds the `FileInfo` object.
		 *
		 * @return the constructed `FileInfo` object
		 */
		public FileInfo build()
		{
			return new FileInfo(this);
		}
	}
}
