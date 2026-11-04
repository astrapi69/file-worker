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
package io.github.astrapi69.file.compare;

import java.io.File;
import java.util.Objects;

import io.github.astrapi69.file.compare.api.IFileCompareResultBean;

/**
 * The class FileCompareResultBean is pojo for comparing two files.
 * <p>
 * The five equality flags are primitive values that start as {@code false}. Their setters take a
 * {@link Boolean}, as {@link IFileCompareResultBean} declares them, and reject {@code null} with a
 * {@link NullPointerException} that names the flag, so a flag always has a value to read, compare,
 * hash and print.
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public class FileCompareResultBean implements IFileCompareResultBean
{

	/** The compare file. */
	protected File compare;
	/** The source file. */
	protected File source;
	/** The absolute path equality. */
	private boolean absolutePathEquality;
	/** The file extension equality. */
	private boolean fileExtensionEquality;
	/** The same last modified. */
	private boolean lastModifiedEquality;
	/** The same length. */
	private boolean lengthEquality;
	/** The same name. */
	private boolean nameEquality;

	/**
	 * Instantiates a new file compare result bean.
	 *
	 * @param source
	 *            the source file.
	 * @param compare
	 *            the compare file.
	 */
	public FileCompareResultBean(final File source, final File compare)
	{
		this.source = source;
		this.compare = compare;
	}

	/**
	 * Internal method for checking if another object can be considered equal to this one.
	 *
	 * @param other
	 *            the object to compare with
	 * @return true if the objects can be considered equal, false otherwise
	 */
	protected boolean canEqual(final Object other)
	{
		return other instanceof FileCompareResultBean;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(final Object o)
	{
		if (o == this)
			return true;
		if (!(o instanceof FileCompareResultBean))
			return false;
		final FileCompareResultBean other = (FileCompareResultBean)o;
		if (!other.canEqual(this))
			return false;
		if (this.getAbsolutePathEquality() != other.getAbsolutePathEquality())
			return false;
		final Object this$compare = this.compare;
		final Object other$compare = other.compare;
		if (this$compare == null ? other$compare != null : !this$compare.equals(other$compare))
			return false;
		if (this.getFileExtensionEquality() != other.getFileExtensionEquality())
			return false;
		if (this.getLastModifiedEquality() != other.getLastModifiedEquality())
			return false;
		if (this.getLengthEquality() != other.getLengthEquality())
			return false;
		if (this.getNameEquality() != other.getNameEquality())
			return false;
		final Object this$source = this.source;
		final Object other$source = other.source;
		return this$source == null ? other$source == null : this$source.equals(other$source);
	}

	/**
	 * Gets the absolute path equality.
	 *
	 * @return the absolute path equality {@inheritDoc}
	 * @see IFileCompareResultBean#getAbsolutePathEquality()
	 */
	@Override
	public boolean getAbsolutePathEquality()
	{
		return this.absolutePathEquality;
	}

	/**
	 * Sets the absolute path equality.
	 *
	 * @param absolutePathEquality
	 *            the new absolute path equality {@inheritDoc}
	 * @throws NullPointerException
	 *             if the given value is {@code null}, with the message {@code absolutePathEquality}
	 * @see IFileCompareResultBean#setAbsolutePathEquality(java.lang.Boolean)
	 */
	@Override
	public void setAbsolutePathEquality(final Boolean absolutePathEquality)
	{
		this.absolutePathEquality = Objects.requireNonNull(absolutePathEquality,
			"absolutePathEquality");
	}

	/**
	 * Gets the file extension equality.
	 *
	 * @return the file extension equality {@inheritDoc}
	 * @see IFileCompareResultBean#getFileExtensionEquality()
	 */
	@Override
	public boolean getFileExtensionEquality()
	{
		return this.fileExtensionEquality;
	}

	/**
	 * Sets the file extension equality.
	 *
	 * @param fileExtensionEquality
	 *            the new file extension equality {@inheritDoc}
	 * @throws NullPointerException
	 *             if the given value is {@code null}, with the message
	 *             {@code fileExtensionEquality}
	 * @see IFileCompareResultBean#setFileExtensionEquality(java.lang.Boolean)
	 */
	@Override
	public void setFileExtensionEquality(final Boolean fileExtensionEquality)
	{
		this.fileExtensionEquality = Objects.requireNonNull(fileExtensionEquality,
			"fileExtensionEquality");
	}

	/**
	 * Gets the file to compare.
	 *
	 * @return the file to compare {@inheritDoc}
	 * @see IFileCompareResultBean#getFileToCompare()
	 */
	@Override
	public File getFileToCompare()
	{
		return this.compare;
	}

	/**
	 * Gets the last modified equality.
	 *
	 * @return the last modified equality {@inheritDoc}
	 * @see IFileCompareResultBean#getLastModifiedEquality()
	 */
	@Override
	public boolean getLastModifiedEquality()
	{
		return this.lastModifiedEquality;
	}

	/**
	 * Sets the last modified equality.
	 *
	 * @param lastModifiedEquality
	 *            the new last modified equality {@inheritDoc}
	 * @throws NullPointerException
	 *             if the given value is {@code null}, with the message {@code lastModifiedEquality}
	 * @see IFileCompareResultBean#setLastModifiedEquality(java.lang.Boolean)
	 */
	@Override
	public void setLastModifiedEquality(final Boolean lastModifiedEquality)
	{
		this.lastModifiedEquality = Objects.requireNonNull(lastModifiedEquality,
			"lastModifiedEquality");
	}

	/**
	 * Gets the length equality.
	 *
	 * @return the length equality {@inheritDoc}
	 * @see IFileCompareResultBean#getLengthEquality()
	 */
	@Override
	public boolean getLengthEquality()
	{
		return this.lengthEquality;
	}

	/**
	 * Sets the length equality.
	 *
	 * @param lengthEquality
	 *            the new length equality {@inheritDoc}
	 * @throws NullPointerException
	 *             if the given value is {@code null}, with the message {@code lengthEquality}
	 * @see IFileCompareResultBean#setLengthEquality(java.lang.Boolean)
	 */
	@Override
	public void setLengthEquality(final Boolean lengthEquality)
	{
		this.lengthEquality = Objects.requireNonNull(lengthEquality, "lengthEquality");
	}

	/**
	 * Gets the name equality.
	 *
	 * @return the name equality {@inheritDoc}
	 * @see IFileCompareResultBean#getNameEquality()
	 */
	@Override
	public boolean getNameEquality()
	{
		return this.nameEquality;
	}

	/**
	 * Sets the name equality.
	 *
	 * @param nameEquality
	 *            the new name equality {@inheritDoc}
	 * @throws NullPointerException
	 *             if the given value is {@code null}, with the message {@code nameEquality}
	 * @see IFileCompareResultBean#setNameEquality(java.lang.Boolean)
	 */
	@Override
	public void setNameEquality(final Boolean nameEquality)
	{
		this.nameEquality = Objects.requireNonNull(nameEquality, "nameEquality");
	}

	/**
	 * Gets the source file.
	 *
	 * @return the source file {@inheritDoc}
	 * @see IFileCompareResultBean#getSourceFile()
	 */
	@Override
	public File getSourceFile()
	{
		return this.source;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
	{
		final int PRIME = 59;
		int result = 1;
		result = result * PRIME + Boolean.hashCode(this.getAbsolutePathEquality());
		final Object $compare = this.compare;
		result = result * PRIME + ($compare == null ? 43 : $compare.hashCode());
		result = result * PRIME + Boolean.hashCode(this.getFileExtensionEquality());
		result = result * PRIME + Boolean.hashCode(this.getLastModifiedEquality());
		result = result * PRIME + Boolean.hashCode(this.getLengthEquality());
		result = result * PRIME + Boolean.hashCode(this.getNameEquality());
		final Object $source = this.source;
		result = result * PRIME + ($source == null ? 43 : $source.hashCode());
		return result;
	}

	/**
	 * Sets the file to compare against.
	 *
	 * @param compare
	 *            the file to compare against
	 */
	public void setCompare(File compare)
	{
		this.compare = compare;
	}

	/**
	 * Sets the source file used as reference for comparison.
	 *
	 * @param source
	 *            the source file
	 */
	public void setSource(File source)
	{
		this.source = source;
	}

	@Override
	public String toString()
	{
		return "FileCompareResultBean(absolutePathEquality=" + this.getAbsolutePathEquality()
			+ ", compare=" + this.compare + ", fileExtensionEquality="
			+ this.getFileExtensionEquality() + ", lastModifiedEquality="
			+ this.getLastModifiedEquality() + ", lengthEquality=" + this.getLengthEquality()
			+ ", nameEquality=" + this.getNameEquality() + ", source=" + this.source + ")";
	}
}
