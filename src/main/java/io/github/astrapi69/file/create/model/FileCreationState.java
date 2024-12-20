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
package io.github.astrapi69.file.create.model;

import java.io.File;

/**
 * The enum {@link FileCreationState} provides constants for the file creation state
 */
public enum FileCreationState
{

	/** This state signals that the file already exists */
	ALREADY_EXISTS,

	/** This state signals that the file was created */
	CREATED,

	/** This state signals that the file creation failed */
	FAILED,

	/** This state signals that the file creation is pending */
	PENDING;

	File file;

	/**
	 * Gets file
	 *
	 * @return the file
	 */
	public File getFile()
	{
		return file;
	}

	/**
	 * Sets file
	 *
	 * @param file
	 *            the file
	 * @return the file
	 */
	public FileCreationState setFile(File file)
	{
		this.file = file;
		return this;
	}

}
