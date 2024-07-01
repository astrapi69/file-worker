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
