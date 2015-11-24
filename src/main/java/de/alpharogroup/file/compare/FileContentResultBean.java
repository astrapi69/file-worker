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

import de.alpharogroup.file.compare.interfaces.IFileContentResultBean;

/**
 * Bean that tells if the content from the given files are equal.
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public class FileContentResultBean extends FileCompareResultBean implements IFileContentResultBean
{

	/** The same content. */
	private Boolean contentEquality;

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
	 * Returns <code>true</code> if this <code>FileContentResultBean</code> is the same as the o
	 * argument.
	 *
	 * @param o
	 *            the o
	 * @return <code>true</code> if this <code>FileContentResultBean</code> is the same as the o
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
		final FileContentResultBean castedObj = (FileContentResultBean)o;
		final FileContentResultBean other = (FileContentResultBean)o;
		if (this.compare.equals(other.source) && this.source.equals(other.compare))
		{
			return true;
		}
		if (!super.equals(o))
		{
			return false;
		}
		return this.contentEquality == null
			? castedObj.contentEquality == null
			: this.contentEquality.equals(castedObj.contentEquality);
	}

	/**
	 * Gets the content equality.
	 *
	 * @return the content equality {@inheritDoc}
	 * @see de.alpharogroup.file.compare.interfaces.IFileContentResultBean#getContentEquality()
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
	 * @see de.alpharogroup.file.compare.interfaces.IFileContentResultBean#setContentEquality(boolean)
	 */
	@Override
	public void setContentEquality(final boolean contentEquality)
	{
		this.contentEquality = contentEquality;
	}

}
