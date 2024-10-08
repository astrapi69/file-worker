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
package io.github.astrapi69.file.sort;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

import io.github.astrapi69.file.read.ReadFileExtensions;
import io.github.astrapi69.file.write.StoreFileExtensions;

/**
 * The class {@link SortFileExtensions} provides algorithms for sort file content.
 */
public final class SortFileExtensions
{

	/**
	 * Private constructor to prevent instantiation
	 */
	private SortFileExtensions()
	{
	}

	/**
	 * Sort the file content from the given {@link File}.
	 *
	 * @param file
	 *            the file
	 * @param comparator
	 *            the comparator
	 * @param encoding
	 *            the encoding
	 * @throws FileNotFoundException
	 *             is thrown if the given file is not found.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void sort(File file, Comparator<String> comparator, final String encoding)
		throws FileNotFoundException, IOException
	{
		List<String> lines = ReadFileExtensions.readLinesInList(file);
		lines.sort(comparator);
		StoreFileExtensions.toFile(file, lines, encoding);
	}

}
