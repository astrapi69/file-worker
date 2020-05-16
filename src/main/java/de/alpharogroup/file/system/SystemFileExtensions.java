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
package de.alpharogroup.file.system;

import java.io.File;

/**
 * The class {@link SystemFileExtensions} provide methods for get system or user files.
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public final class SystemFileExtensions
{
	/**
	 * Gets the installation directory for Java Runtime Environment (JRE) from the system as
	 * {@link File} object
	 *
	 * @return the installation directory for Java Runtime Environment (JRE) from the system as
	 *         {@link File} object
	 */
	public static File getJavaHomeDir()
	{
		String userHomePath = System.getProperty("java.home");
		return new File(userHomePath);
	}

	/**
	 * Gets the temporary directory from the system as File object.
	 *
	 * @return the temporary directory from the system.
	 */
	public static File getTempDir()
	{
		String tmpdirPath = System.getProperty("java.io.tmpdir");
		return new File(tmpdirPath);
	}

	/**
	 * Gets the user home directory from the system as {@link File} object
	 *
	 * @return the user home directory from the system as {@link File} object
	 */
	public static File getUserHomeDir()
	{
		String userHomePath = System.getProperty("user.home");
		return new File(userHomePath);
	}

	/**
	 * Gets the user working directory from the system as {@link File} object
	 *
	 * @return the user working directory from the system as {@link File} object
	 */
	public static File getUserWorkingDir()
	{
		String userWorkingPath = System.getProperty("user.dir");
		return new File(userWorkingPath);
	}

	private SystemFileExtensions()
	{
	}

}
