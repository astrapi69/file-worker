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
package io.github.astrapi69.file.modify;

import java.util.List;

import io.github.astrapi69.file.modify.api.FileChangeable;

/**
 * The class {@link DeleteLinesByIndexInFile} implements the {@link FileChangeable} interface to
 * delete specific lines in a file based on a list of line indexes.
 */
public class DeleteLinesByIndexInFile implements FileChangeable
{

	private final List<Integer> lineIndexesToDelete;

	/**
	 * Instantiates a new {@link DeleteLinesByIndexInFile} with the given list of line indexes to
	 * delete
	 *
	 * @param lineIndexesToDelete
	 *            the line indexes to delete
	 */
	public DeleteLinesByIndexInFile(final List<Integer> lineIndexesToDelete)
	{
		this.lineIndexesToDelete = lineIndexesToDelete;
	}

	/**
	 * Applies the deletion logic to the specified line. If the current line index is in the list of
	 * indexes to delete, the method returns null, indicating that this line should be skipped.
	 * Otherwise, it returns the line with a newline character.
	 *
	 * @param lineIndex
	 *            the index of the current line
	 * @param line
	 *            the content of the current line
	 * @return the modified line, or null if the line should be deleted
	 */
	@Override
	public String apply(Integer lineIndex, String line)
	{
		// If the current line index is in the list of indexes to delete, return null
		if (lineIndexesToDelete.contains(lineIndex))
		{
			return null;
		}
		return line + System.lineSeparator();
	}

}
