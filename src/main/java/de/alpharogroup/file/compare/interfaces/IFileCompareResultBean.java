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
package de.alpharogroup.file.compare.interfaces;

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
	 * Gets the file extension equality.
	 * 
	 * @return the file extension equality
	 */
	boolean getFileExtensionEquality();

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
	 * Gets the length equality.
	 * 
	 * @return the length equality
	 */
	boolean getLengthEquality();

	/**
	 * Gets the name equality.
	 * 
	 * @return the name equality
	 */
	boolean getNameEquality();

	/**
	 * Gets the source file.
	 * 
	 * @return Returns the source file.
	 */
	File getSourceFile();

	/**
	 * Sets the absolute path equality.
	 * 
	 * @param absolutePathEquality
	 *            the new absolute path equality
	 */
	void setAbsolutePathEquality(final Boolean absolutePathEquality);

	/**
	 * Sets the file extension equality.
	 * 
	 * @param fileExtensionEquality
	 *            the new file extension equality
	 */
	void setFileExtensionEquality(final Boolean fileExtensionEquality);

	/**
	 * Sets the last modified equality.
	 * 
	 * @param lastModifiedEquality
	 *            the new last modified equality
	 */
	void setLastModifiedEquality(final Boolean lastModifiedEquality);

	/**
	 * Sets the length equality.
	 * 
	 * @param lengthEquality
	 *            the new length equality
	 */
	void setLengthEquality(final Boolean lengthEquality);

	/**
	 * Sets the name equality.
	 * 
	 * @param nameEquality
	 *            the new name equality
	 */
	void setNameEquality(final Boolean nameEquality);

}
