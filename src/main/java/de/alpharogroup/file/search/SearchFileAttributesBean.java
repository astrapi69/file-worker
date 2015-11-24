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
package de.alpharogroup.file.search;

/**
 * The Class SearchFileAttributesBean provides file attribute flags to ignore or not.
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public class SearchFileAttributesBean
{

	/** The ignore extension equality. */
	private final boolean ignoreExtensionEquality;

	/** The ignore length equality. */
	private final boolean ignoreLengthEquality;

	/** The ignore last modified. */
	private final boolean ignoreLastModified;

	/** The ignore name equality. */
	private final boolean ignoreNameEquality;

	/** The ignore content equality. */
	private final boolean ignoreContentEquality;

	/**
	 * Instantiates a new search file attributes bean.
	 *
	 * @param ignoreExtensionEquality
	 *            the ignore extension equality
	 * @param ignoreLengthEquality
	 *            the ignore length equality
	 * @param ignoreLastModified
	 *            the ignore last modified
	 * @param ignoreNameEquality
	 *            the ignore name equality
	 * @param ignoreContentEquality
	 *            the ignore content equality
	 */
	public SearchFileAttributesBean(final boolean ignoreExtensionEquality,
		final boolean ignoreLengthEquality, final boolean ignoreLastModified,
		final boolean ignoreNameEquality, final boolean ignoreContentEquality)
	{
		super();
		this.ignoreExtensionEquality = ignoreExtensionEquality;
		this.ignoreLengthEquality = ignoreLengthEquality;
		this.ignoreLastModified = ignoreLastModified;
		this.ignoreNameEquality = ignoreNameEquality;
		this.ignoreContentEquality = ignoreContentEquality;
	}


	/**
	 * Checks if is ignore content equality.
	 *
	 * @return true, if is ignore content equality
	 */
	public boolean isIgnoreContentEquality()
	{
		return ignoreContentEquality;
	}

	/**
	 * Checks if is ignore extension equality.
	 *
	 * @return true, if is ignore extension equality
	 */
	public boolean isIgnoreExtensionEquality()
	{
		return ignoreExtensionEquality;
	}

	/**
	 * Checks if is ignore last modified.
	 *
	 * @return true, if is ignore last modified
	 */
	public boolean isIgnoreLastModified()
	{
		return ignoreLastModified;
	}

	/**
	 * Checks if is ignore length equality.
	 *
	 * @return true, if is ignore length equality
	 */
	public boolean isIgnoreLengthEquality()
	{
		return ignoreLengthEquality;
	}

	/**
	 * Checks if is ignore name equality.
	 *
	 * @return true, if is ignore name equality
	 */
	public boolean isIgnoreNameEquality()
	{
		return ignoreNameEquality;
	}

}
