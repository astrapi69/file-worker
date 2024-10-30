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
package io.github.astrapi69.file.write;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import io.github.astrapi69.collection.list.ListFactory;

/**
 * The {@code LineAppender} class provides utility methods for appending lines of text to a file.
 * <p>
 * This class cannot be instantiated
 * </p>
 */
public final class LineAppender
{

	/**
	 * Private constructor to prevent instantiation
	 */
	private LineAppender()
	{
	}

	/**
	 * Appends the given lines to the given {@link File} object
	 *
	 * @param file
	 *            The source file
	 * @param lineToAppend
	 *            The lines to append
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void appendLines(File file, String... lineToAppend) throws IOException
	{
		Files.write(file.toPath(), ListFactory.newArrayList(lineToAppend),
			StandardOpenOption.APPEND, StandardOpenOption.CREATE);
	}

}
