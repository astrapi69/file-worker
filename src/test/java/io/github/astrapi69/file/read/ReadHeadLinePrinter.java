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
package io.github.astrapi69.file.read;

import java.io.IOException;
import java.util.stream.Collectors;

/**
 * A small program for {@link ReadFileExtensionsTest}: it prints the code points of
 * {@link ReadFileExtensions#readHeadLine(String)} of the file given as its first argument,
 * separated by a space, so that the test can start it in a second JVM with another default charset
 * and read its output whatever the encoding of standard output is.
 */
public final class ReadHeadLinePrinter
{

	private ReadHeadLinePrinter()
	{
	}

	/**
	 * Prints the code points of the first line of the file given as the first argument
	 *
	 * @param args
	 *            the path of the file to read
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void main(final String[] args) throws IOException
	{
		final String headLine = ReadFileExtensions.readHeadLine(args[0]);
		System.out.println(
			headLine.codePoints().mapToObj(Integer::toString).collect(Collectors.joining(" ")));
	}

}
