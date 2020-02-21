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
package de.alpharogroup.file.compare;

import de.alpharogroup.file.compare.api.IFileContentResultBean;

import java.io.File;

/**
 * Bean that tells if the content from the given files are equal.
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public class FileContentResultBean extends FileCompareResultBean implements IFileContentResultBean
{

	/** The same content. */
	private boolean contentEquality;

	/**
	 * Default constructor.
	 *
	 * @param source
	 *            the source
	 * @param compare
	 *            the compare
	 */
	public FileContentResultBean(final File source, final File compare)
	{
		super(source, compare);
	}

	/**
	 * Gets the content equality.
	 *
	 * @return the content equality {@inheritDoc}
	 * @see de.alpharogroup.file.compare.api.IFileContentResultBean#getContentEquality()
	 */
	@Override
	public boolean getContentEquality()
	{
		return this.contentEquality;
	}

	/**
	 * Sets the content equality.
	 *
	 * @param contentEquality
	 *            the new content equality {@inheritDoc}
	 * @see de.alpharogroup.file.compare.api.IFileContentResultBean#setContentEquality(boolean)
	 */
	@Override
	public void setContentEquality(final boolean contentEquality)
	{
		this.contentEquality = contentEquality;
	}

	public boolean equals(final Object o)
	{
		if (o == this)
			return true;
		if (!(o instanceof FileContentResultBean))
			return false;
		final FileContentResultBean other = (FileContentResultBean)o;
		if (!other.canEqual((Object)this))
			return false;
		if (!super.equals(o))
			return false;
		if (this.contentEquality != other.contentEquality)
			return false;
		return true;
	}

	protected boolean canEqual(final Object other)
	{
		return other instanceof FileContentResultBean;
	}

	public int hashCode()
	{
		final int PRIME = 59;
		int result = super.hashCode();
		result = result * PRIME + (this.contentEquality ? 79 : 97);
		return result;
	}

	public String toString()
	{
		return "FileContentResultBean(super=" + super.toString() + ", contentEquality="
			+ this.contentEquality + ")";
	}
}
