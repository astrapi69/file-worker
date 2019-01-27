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
package de.alpharogroup.file.modify;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Path;
import java.util.function.BiFunction;

import de.alpharogroup.file.modify.api.FileChangable;
import lombok.experimental.UtilityClass;

/**
 * The class {@link ModifyFileExtensions} provides methods for modify files
 */
@UtilityClass
public class ModifyFileExtensions
{

	/**
	 * Modifies the input file line by line and writes the modification in the new output file
	 *
	 * @param inFilePath
	 *            the in file path
	 * @param outFilePath
	 *            the out file path
	 * @param modifier
	 *            the modifier {@linkplain BiFunction}
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void modifyFile(Path inFilePath, Path outFilePath, FileChangable modifier)
		throws IOException
	{
		try (
			BufferedReader bufferedReader = new BufferedReader(new FileReader(inFilePath.toFile()));
			Writer writer = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(outFilePath.toFile()), "utf-8")))
		{
			String readLine;
			int counter = 0;
			while ((readLine = bufferedReader.readLine()) != null)
			{
				writer.write(modifier.apply(counter, readLine));
				counter++;
			}
		}
	}
}
