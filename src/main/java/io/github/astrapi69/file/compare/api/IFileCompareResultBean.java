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
package io.github.astrapi69.file.compare.api;

import java.io.File;

/**
 * The Interface IFileCompareResultBean.
 * 
 * @version 1.0
 * @author Asterios Raptis
 */
public interface IFileCompareResultBean
{

	/**
	 * Gets the absolute path equality.
	 * 
	 * @return the absolute path equality
	 */
	boolean getAbsolutePathEquality();

	/**
	 * Sets the absolute path equality.
	 *
	 * @param absolutePathEquality
	 *            the new absolute path equality
	 */
	void setAbsolutePathEquality(final Boolean absolutePathEquality);

	/**
	 * Gets the file extension equality.
	 *
	 * @return the file extension equality
	 */
	boolean getFileExtensionEquality();

	/**
	 * Sets the file extension equality.
	 *
	 * @param fileExtensionEquality
	 *            the new file extension equality
	 */
	void setFileExtensionEquality(final Boolean fileExtensionEquality);

	/**
	 * Gets the file to compare.
	 *
	 * @return Returns the file to compare.
	 */
	File getFileToCompare();

	/**
	 * Gets the last modified equality.
	 *
	 * @return the last modified equality
	 */
	boolean getLastModifiedEquality();

	/**
	 * Sets the last modified equality.
	 *
	 * @param lastModifiedEquality
	 *            the new last modified equality
	 */
	void setLastModifiedEquality(final Boolean lastModifiedEquality);

	/**
	 * Gets the length equality.
	 *
	 * @return the length equality
	 */
	boolean getLengthEquality();

	/**
	 * Sets the length equality.
	 *
	 * @param lengthEquality
	 *            the new length equality
	 */
	void setLengthEquality(final Boolean lengthEquality);

	/**
	 * Gets the name equality.
	 *
	 * @return the name equality
	 */
	boolean getNameEquality();

	/**
	 * Sets the name equality.
	 *
	 * @param nameEquality
	 *            the new name equality
	 */
	void setNameEquality(final Boolean nameEquality);

	/**
	 * Gets the source file.
	 *
	 * @return Returns the source file.
	 */
	File getSourceFile();

}
