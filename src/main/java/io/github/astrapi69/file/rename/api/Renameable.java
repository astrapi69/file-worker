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
package io.github.astrapi69.file.rename.api;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.github.astrapi69.file.copy.CopyFileExtensions;
import io.github.astrapi69.file.delete.DeleteFileExtensions;
import io.github.astrapi69.file.exception.FileDoesNotExistException;
import io.github.astrapi69.file.exception.FileIsADirectoryException;
import io.github.astrapi69.file.rename.RenameFileExtensions;
import io.github.astrapi69.io.file.FilenameExtensions;

/**
 * Interface {@link Renameable} provides methods for renaming files and directories.
 *
 * @version 1.0
 */
public interface Renameable
{

	/**
	 * Returns the filename from the given file with the system time appended
	 *
	 * @param fileToRename
	 *            the file to rename
	 * @return the filename with the system time appended
	 */
	default String appendSystemTimeToFilename(final File fileToRename)
	{
		return appendSystemTimeToFilename(fileToRename, null);
	}

	/**
	 * Returns the filename from the given file with the specified date appended. The time goes
	 * before the last extension, or at the end of a name without one.
	 *
	 * @param fileToRename
	 *            the file
	 * @param add2Name
	 *            the date to add
	 * @return the filename with the specified date appended
	 */
	default String appendSystemTimeToFilename(final File fileToRename, final Date add2Name)
	{
		final String format = "HHmmssSSS";
		DateFormat df = new SimpleDateFormat(format);
		String sysTime = add2Name != null ? df.format(add2Name) : df.format(new Date());

		String fileName = fileToRename.getName();
		String nameWithoutExtension = FilenameExtensions.getFilenameWithoutExtension(fileName);
		String extension = fileName.substring(nameWithoutExtension.length());
		return nameWithoutExtension + "_" + sysTime + extension;
	}

	/**
	 * Changes all filenames with the specified suffix, in the given directory and below it. A file
	 * whose suffix cannot be changed goes into the returned list, and the batch goes on with the
	 * next file. Delegates to
	 * {@link RenameFileExtensions#changeAllFilenameSuffix(File, String, String, boolean)}
	 *
	 * @param file
	 *            the directory in which to change filenames
	 * @param oldSuffix
	 *            the suffix to replace
	 * @param newSuffix
	 *            the new suffix
	 * @param delete
	 *            if true, a file whose rename the operating system refuses, as it does for a move
	 *            to another file system, is copied to its new name and then deleted; if false, it
	 *            is left as it is
	 * @return the files whose suffix could not be changed, because the rename was refused or, with
	 *         delete, the copy failed; an empty list, never null, if every file with the old suffix
	 *         was renamed
	 * @throws IOException
	 *             if an I/O exception occurs
	 * @throws FileDoesNotExistException
	 *             if the file does not exist
	 * @throws FileIsADirectoryException
	 *             if the file is a directory
	 */
	default List<File> changeAllFilenameSuffix(final File file, final String oldSuffix,
		final String newSuffix, final boolean delete)
		throws IOException, FileDoesNotExistException, FileIsADirectoryException
	{
		return RenameFileExtensions.changeAllFilenameSuffix(file, oldSuffix, newSuffix, delete);
	}

	/**
	 * Changes the suffix of the given file
	 *
	 * @param file
	 *            the file to change
	 * @param newSuffix
	 *            the new suffix
	 * @param delete
	 *            if true, a file whose rename the operating system refuses is copied to its new
	 *            name and then deleted; if false, it is left as it is
	 * @return true if the file was renamed; false only if delete is false and the rename was
	 *         refused. With delete the result is always true: see
	 *         {@link #renameFile(File, File, boolean)}
	 * @throws IOException
	 *             if an I/O exception occurs, with delete for a copy or a delete that fails
	 * @throws FileDoesNotExistException
	 *             if the file does not exist
	 * @throws FileIsADirectoryException
	 *             if the file is a directory
	 */
	default boolean changeFilenameSuffix(final File file, final String newSuffix,
		final boolean delete)
		throws IOException, FileDoesNotExistException, FileIsADirectoryException
	{
		if (!file.exists())
		{
			throw new FileDoesNotExistException("The file " + file + " does not exist.");
		}

		String newFilename = FilenameExtensions.getFilenamePrefix(file) + newSuffix;
		return renameFile(file, new File(newFilename), delete);
	}

	/**
	 * Renames the file, with an option to delete the original
	 *
	 * @param fileToRename
	 *            the file to rename
	 * @param newFileName
	 *            the new filename
	 * @param delete
	 *            if true and the operating system refuses the rename, the file is copied to the new
	 *            filename and the original is then deleted; if false, a refused rename leaves the
	 *            file as it is
	 * @return true if the file was renamed; false only if delete is false and the rename was
	 *         refused. With delete the result is always true: a copy or a delete that fails throws,
	 *         it does not return false. If the copy is made and the delete then throws, as it does
	 *         in a directory that cannot be written, the file is in both places
	 * @throws IOException
	 *             if an I/O exception occurs, with delete for a copy or a delete that fails
	 * @throws IllegalArgumentException
	 *             with delete, if the copy is refused because the new filename is a directory, see
	 *             {@link CopyFileExtensions#copyFile(File, File)}
	 * @throws FileIsADirectoryException
	 *             if the file is a directory
	 */
	default boolean renameFile(final File fileToRename, final File newFileName,
		final boolean delete) throws IOException, FileIsADirectoryException
	{
		boolean success = fileToRename.renameTo(newFileName);
		if (!success && delete)
		{
			CopyFileExtensions.copyFile(fileToRename, newFileName);
			DeleteFileExtensions.delete(fileToRename);
			success = true;
		}
		return success;
	}

	/**
	 * Renames a file with the system time appended
	 *
	 * @param fileToRename
	 *            the file to rename
	 * @return the renamed file with the system time
	 * @throws IOException
	 *             if an I/O exception occurs
	 * @throws FileIsADirectoryException
	 *             if the file is a directory
	 */
	default File renameFileWithSystemtime(final File fileToRename)
		throws IOException, FileIsADirectoryException
	{
		String newFilenameWithSystemtime = appendSystemTimeToFilename(fileToRename);
		File fileWithNewName = new File(fileToRename.getParent(), newFilenameWithSystemtime);
		renameFile(fileToRename, fileWithNewName, true);
		return fileWithNewName;
	}

	/**
	 * Changes the suffix of a filename. Example: test.dat to test.xxx
	 *
	 * @param file
	 *            the file to change
	 * @param newSuffix
	 *            the new suffix, starting with a dot, e.g., ".xxx"
	 * @return true if the file was renamed successfully
	 * @throws IOException
	 *             if an I/O exception occurs
	 * @throws FileDoesNotExistException
	 *             if the file does not exist
	 * @throws FileIsADirectoryException
	 *             if the file is a directory
	 */
	default boolean changeFilenameSuffix(final File file, final String newSuffix)
		throws IOException, FileDoesNotExistException, FileIsADirectoryException
	{
		return changeFilenameSuffix(file, newSuffix, false);
	}

	/**
	 * Moves the specified source file to the given destination file
	 *
	 * @param srcFile
	 *            the source file
	 * @param destinationFile
	 *            the destination file
	 * @throws IOException
	 *             if an I/O exception occurs, for a copy or a delete that fails
	 * @throws FileIsADirectoryException
	 *             if the source or destination is a directory
	 */
	default void forceToMoveFile(final File srcFile, final File destinationFile)
		throws IOException, FileIsADirectoryException
	{
		renameFile(srcFile, destinationFile, true);
	}

	/**
	 * Moves the specified source file to the given destination file. The destination file is the
	 * new path of the source file, as the target of {@code Files.move(source, target)} is, not a
	 * directory to move it into: a move of a file onto an existing directory is refused with an
	 * {@link IllegalArgumentException}, and the file stays where it is. To move a file into a
	 * directory, use {@code FileUtils.moveFileToDirectory(File, File, boolean)} of commons-io
	 *
	 * @param srcFile
	 *            the source file
	 * @param destFile
	 *            the destination file, the new path of the source file
	 * @throws IOException
	 *             if an I/O exception occurs, for a copy or a delete that fails
	 * @throws FileIsADirectoryException
	 *             not thrown by the default methods of this interface, a move of a file onto an
	 *             existing directory is refused with an {@link IllegalArgumentException}
	 */
	default void moveFile(final File srcFile, final File destFile)
		throws IOException, FileIsADirectoryException
	{
		renameFile(srcFile, destFile, true);
	}

	/**
	 * Renames the given file, allowing specification of a new file name without an absolute path
	 *
	 * @param fileToRename
	 *            the file to rename
	 * @param newFileNameWithoutAbsolutePath
	 *            the new name for the file without absolute path
	 * @return true if the file was renamed successfully
	 * @throws IOException
	 *             if an I/O exception occurs
	 * @throws FileIsADirectoryException
	 *             if the file is a directory
	 * @throws FileDoesNotExistException
	 *             if the file does not exist
	 */
	default boolean renameFile(final File fileToRename, final String newFileNameWithoutAbsolutePath)
		throws IOException, FileIsADirectoryException, FileDoesNotExistException
	{
		if (!fileToRename.exists())
		{
			throw new FileDoesNotExistException(
				"File " + fileToRename.getName() + " does not exist!");
		}
		String absolutePathPrefix = fileToRename.getParent();
		File newNameForFile = new File(absolutePathPrefix, newFileNameWithoutAbsolutePath);
		return renameFile(fileToRename, newNameForFile);
	}

	/**
	 * This method renames a given file. For instance if we have a file which we want to rename with
	 * the path "/tmp/test.dat" to "/tmp/renamed.dat" then you call the method as follow:
	 * renameFile(new File("C://tmp//test.dat"), new File("C://tmp//renamed.dat"));
	 *
	 * @param fileToRename
	 *            The file to rename.
	 * @param newFileName
	 *            The new name from the file.
	 * @return 's true if the file was renamed otherwise false.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws FileIsADirectoryException
	 *             the file is A directory exception
	 */
	default boolean renameFile(final File fileToRename, final File newFileName)
		throws IOException, FileIsADirectoryException
	{
		return renameFile(fileToRename, newFileName, false);
	}

}
