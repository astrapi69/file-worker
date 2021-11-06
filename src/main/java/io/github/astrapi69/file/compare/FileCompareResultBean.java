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

import io.github.astrapi69.file.compare.api.IFileCompareResultBean;

/**
 * The class FileCompareResultBean is pojo for comparing two files.
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
	private Boolean absolutePathEquality = Boolean.FALSE;
	/** The file extension equality. */
	private Boolean fileExtensionEquality = Boolean.FALSE;
	/** The same last modified. */
	private Boolean lastModifiedEquality = Boolean.FALSE;
	/** The same length. */
	private Boolean lengthEquality = Boolean.FALSE;
	/** The same name. */
	private Boolean nameEquality = Boolean.FALSE;

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

	protected boolean canEqual(final Object other)
	{
		return other instanceof FileCompareResultBean;
	}

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
		final Object this$absolutePathEquality = this.getAbsolutePathEquality();
		final Object other$absolutePathEquality = other.getAbsolutePathEquality();
		if (this$absolutePathEquality == null
			? other$absolutePathEquality != null
			: !this$absolutePathEquality.equals(other$absolutePathEquality))
			return false;
		final Object this$compare = this.compare;
		final Object other$compare = other.compare;
		if (this$compare == null ? other$compare != null : !this$compare.equals(other$compare))
			return false;
		final Object this$fileExtensionEquality = this.getFileExtensionEquality();
		final Object other$fileExtensionEquality = other.getFileExtensionEquality();
		if (this$fileExtensionEquality == null
			? other$fileExtensionEquality != null
			: !this$fileExtensionEquality.equals(other$fileExtensionEquality))
			return false;
		final Object this$lastModifiedEquality = this.getLastModifiedEquality();
		final Object other$lastModifiedEquality = other.getLastModifiedEquality();
		if (this$lastModifiedEquality == null
			? other$lastModifiedEquality != null
			: !this$lastModifiedEquality.equals(other$lastModifiedEquality))
			return false;
		final Object this$lengthEquality = this.getLengthEquality();
		final Object other$lengthEquality = other.getLengthEquality();
		if (this$lengthEquality == null
			? other$lengthEquality != null
			: !this$lengthEquality.equals(other$lengthEquality))
			return false;
		final Object this$nameEquality = this.getNameEquality();
		final Object other$nameEquality = other.getNameEquality();
		if (this$nameEquality == null
			? other$nameEquality != null
			: !this$nameEquality.equals(other$nameEquality))
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

	/*
	 * (non-Javadoc)
	 *
	 * @see io.github.astrapi69.compare.interfaces.IFileCompareResultBean#setAbsolutePathEquality
	 * (java.lang.Boolean)
	 */
	@Override
	public void setAbsolutePathEquality(final Boolean absolutePathEquality)
	{
		this.absolutePathEquality = absolutePathEquality;
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
	 * @see IFileCompareResultBean#setFileExtensionEquality(java.lang.Boolean)
	 */
	@Override
	public void setFileExtensionEquality(final Boolean fileExtensionEquality)
	{
		this.fileExtensionEquality = fileExtensionEquality;
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
	 * @see IFileCompareResultBean#setLastModifiedEquality(java.lang.Boolean)
	 */
	@Override
	public void setLastModifiedEquality(final Boolean lastModifiedEquality)
	{
		this.lastModifiedEquality = lastModifiedEquality;
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
	 * @see IFileCompareResultBean#setLengthEquality(java.lang.Boolean)
	 */
	@Override
	public void setLengthEquality(final Boolean lengthEquality)
	{
		this.lengthEquality = lengthEquality;
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
	 * @see IFileCompareResultBean#setNameEquality(java.lang.Boolean)
	 */
	@Override
	public void setNameEquality(final Boolean nameEquality)
	{
		this.nameEquality = nameEquality;
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

	@Override
	public int hashCode()
	{
		final int PRIME = 59;
		int result = 1;
		final Object $absolutePathEquality = this.getAbsolutePathEquality();
		result = result * PRIME
			+ ($absolutePathEquality == null ? 43 : $absolutePathEquality.hashCode());
		final Object $compare = this.compare;
		result = result * PRIME + ($compare == null ? 43 : $compare.hashCode());
		final Object $fileExtensionEquality = this.getFileExtensionEquality();
		result = result * PRIME
			+ ($fileExtensionEquality == null ? 43 : $fileExtensionEquality.hashCode());
		final Object $lastModifiedEquality = this.getLastModifiedEquality();
		result = result * PRIME
			+ ($lastModifiedEquality == null ? 43 : $lastModifiedEquality.hashCode());
		final Object $lengthEquality = this.getLengthEquality();
		result = result * PRIME + ($lengthEquality == null ? 43 : $lengthEquality.hashCode());
		final Object $nameEquality = this.getNameEquality();
		result = result * PRIME + ($nameEquality == null ? 43 : $nameEquality.hashCode());
		final Object $source = this.source;
		result = result * PRIME + ($source == null ? 43 : $source.hashCode());
		return result;
	}

	public void setCompare(File compare)
	{
		this.compare = compare;
	}

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
