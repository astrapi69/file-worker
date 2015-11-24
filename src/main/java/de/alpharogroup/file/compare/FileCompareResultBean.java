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
package de.alpharogroup.file.compare;

import java.io.File;

import de.alpharogroup.file.compare.interfaces.IFileCompareResultBean;

/**
 * The Class FileCompareResultBean is pojo for comparing two files.
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public class FileCompareResultBean implements IFileCompareResultBean
{

	/** The source file. */
	protected File source;

	/** The compare file. */
	protected File compare;

	/** The file extension equality. */
	private Boolean fileExtensionEquality = Boolean.FALSE;

	/** The same length. */
	private Boolean lengthEquality = Boolean.FALSE;

	/** The same last modified. */
	private Boolean lastModifiedEquality = Boolean.FALSE;

	/** The same name. */
	private Boolean nameEquality = Boolean.FALSE;

	/** The absolute path equality. */
	private Boolean absolutePathEquality = Boolean.FALSE;

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
		super();
		this.source = source;
		this.compare = compare;
	}

	/**
	 * Returns <code>true</code> if this <code>FileCompareResultBean</code> is the same as the o
	 * argument.
	 *
	 * @param o
	 *            the o
	 * @return <code>true</code> if this <code>FileCompareResultBean</code> is the same as the o
	 *         argument.
	 */
	@Override
	public boolean equals(final Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null)
		{
			return false;
		}
		if (o.getClass() != getClass())
		{
			return false;
		}
		final FileCompareResultBean other = (FileCompareResultBean)o;
		if (this.compare.equals(other.source) && this.source.equals(other.compare))
		{
			return true;
		}
		return (this.source == null ? other.source == null : this.source.equals(other.source))
			&& (this.compare == null ? other.compare == null : this.compare.equals(other.compare))
			&& (this.fileExtensionEquality == null
				? other.fileExtensionEquality == null
				: this.fileExtensionEquality.equals(other.fileExtensionEquality))
			&& (this.lengthEquality == null ? other.lengthEquality == null : this.lengthEquality
				.equals(other.lengthEquality))
			&& (this.lastModifiedEquality == null
				? other.lastModifiedEquality == null
				: this.lastModifiedEquality.equals(other.lastModifiedEquality))
			&& (this.nameEquality == null ? other.nameEquality == null : this.nameEquality
				.equals(other.nameEquality))
			&& (this.absolutePathEquality == null
				? other.absolutePathEquality == null
				: this.absolutePathEquality.equals(other.absolutePathEquality));
	}

	/**
	 * Gets the absolute path equality.
	 *
	 * @return the absolute path equality {@inheritDoc}
	 * @see de.alpharogroup.file.compare.interfaces.IFileCompareResultBean#getAbsolutePathEquality()
	 */
	@Override
	public boolean getAbsolutePathEquality()
	{
		return this.absolutePathEquality;
	}

	/**
	 * Gets the file extension equality.
	 *
	 * @return the file extension equality {@inheritDoc}
	 * @see de.alpharogroup.file.compare.interfaces.IFileCompareResultBean#getFileExtensionEquality()
	 */
	@Override
	public boolean getFileExtensionEquality()
	{
		return this.fileExtensionEquality;
	}

	/**
	 * Gets the file to compare.
	 *
	 * @return the file to compare {@inheritDoc}
	 * @see de.alpharogroup.file.compare.interfaces.IFileCompareResultBean#getFileToCompare()
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
	 * @see de.alpharogroup.file.compare.interfaces.IFileCompareResultBean#getLastModifiedEquality()
	 */
	@Override
	public boolean getLastModifiedEquality()
	{
		return this.lastModifiedEquality;
	}

	/**
	 * Gets the length equality.
	 *
	 * @return the length equality {@inheritDoc}
	 * @see de.alpharogroup.file.compare.interfaces.IFileCompareResultBean#getLengthEquality()
	 */
	@Override
	public boolean getLengthEquality()
	{
		return this.lengthEquality;
	}

	/**
	 * Gets the name equality.
	 *
	 * @return the name equality {@inheritDoc}
	 * @see de.alpharogroup.file.compare.interfaces.IFileCompareResultBean#getNameEquality()
	 */
	@Override
	public boolean getNameEquality()
	{
		return this.nameEquality;
	}

	/**
	 * Gets the source file.
	 *
	 * @return the source file {@inheritDoc}
	 * @see de.alpharogroup.file.compare.interfaces.IFileCompareResultBean#getSourceFile()
	 */
	@Override
	public File getSourceFile()
	{
		return this.source;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.alpharogroup.file.compare.interfaces.IFileCompareResultBean#setAbsolutePathEquality
	 * (java.lang.Boolean)
	 */
	@Override
	public void setAbsolutePathEquality(final Boolean absolutePathEquality)
	{
		this.absolutePathEquality = absolutePathEquality;
	}

	/**
	 * Sets the file extension equality.
	 *
	 * @param fileExtensionEquality
	 *            the new file extension equality {@inheritDoc}
	 * @see de.alpharogroup.file.compare.interfaces.IFileCompareResultBean#setFileExtensionEquality(java.lang.Boolean)
	 */
	@Override
	public void setFileExtensionEquality(final Boolean fileExtensionEquality)
	{
		this.fileExtensionEquality = fileExtensionEquality;
	}

	/**
	 * Sets the last modified equality.
	 *
	 * @param lastModifiedEquality
	 *            the new last modified equality {@inheritDoc}
	 * @see de.alpharogroup.file.compare.interfaces.IFileCompareResultBean#setLastModifiedEquality(java.lang.Boolean)
	 */
	@Override
	public void setLastModifiedEquality(final Boolean lastModifiedEquality)
	{
		this.lastModifiedEquality = lastModifiedEquality;
	}

	/**
	 * Sets the length equality.
	 *
	 * @param lengthEquality
	 *            the new length equality {@inheritDoc}
	 * @see de.alpharogroup.file.compare.interfaces.IFileCompareResultBean#setLengthEquality(java.lang.Boolean)
	 */
	@Override
	public void setLengthEquality(final Boolean lengthEquality)
	{
		this.lengthEquality = lengthEquality;
	}

	/**
	 * Sets the name equality.
	 *
	 * @param nameEquality
	 *            the new name equality {@inheritDoc}
	 * @see de.alpharogroup.file.compare.interfaces.IFileCompareResultBean#setNameEquality(java.lang.Boolean)
	 */
	@Override
	public void setNameEquality(final Boolean nameEquality)
	{
		this.nameEquality = nameEquality;
	}

}
