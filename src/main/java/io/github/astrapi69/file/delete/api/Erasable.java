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
package io.github.astrapi69.file.delete.api;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;

import io.github.astrapi69.file.delete.DeleteFileExtensions;
import io.github.astrapi69.file.exception.*;

/**
 * Interface for delete files or directories
 */
public interface Erasable
{

	/**
	 * Checks if the File is a directory, exists, and is accessible
	 *
	 * @param file
	 *            The File to check
	 * @return Null if all checks pass; otherwise, an appropriate Exception
	 */
	default Exception checkFile(File file)
	{
		return DeleteFileExtensions.checkFile(file);
	}

	/**
	 * Tries to delete all given files in the collection. Caution: This can not be undone.
	 *
	 * @param files
	 *            The files to delete
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	default void delete(Collection<File> files) throws IOException
	{
		DeleteFileExtensions.delete(files);
	}

	/**
	 * Tries to delete the specified file or directory recursively if it is a directory
	 *
	 * @param file
	 *            The file or directory to delete
	 * @return <code>true</code> if deletion is successful; <code>false</code> otherwise
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	default boolean delete(File file) throws IOException
	{
		return DeleteFileExtensions.delete(file);
	}

	/**
	 * Deletes all files and directories recursively within the specified directory
	 *
	 * @param dirPath
	 *            The path of the directory to delete files within
	 * @throws IOException
	 *             if an I/O exception occurs
	 */
	default void deleteAllFiles(Path dirPath) throws IOException
	{
		DeleteFileExtensions.deleteAllFiles(dirPath);
	}

	/**
	 * Deletes the File and if it is a directory it deletes its sub-directories recursively.
	 *
	 * @param file
	 *            The File to delete.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	default void deleteAllFiles(File file) throws IOException
	{
		DeleteFileExtensions.deleteAllFiles(file);
	}

	/**
	 * Deletes all files with the given suffix recursively.
	 *
	 * @param directory
	 *            The directory from where to delete the files with the given suffix.
	 * @param theSuffix
	 *            The suffix from the files to delete.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	default void deleteAllFilesWithSuffix(File directory, String theSuffix) throws IOException
	{
		DeleteFileExtensions.deleteAllFilesWithSuffix(directory, theSuffix);
	}

	/**
	 * Attempts to delete a specific file
	 *
	 * @param fileToDelete
	 *            The file to delete
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	default void deleteFile(File fileToDelete) throws IOException
	{
		DeleteFileExtensions.deleteFile(fileToDelete);
	}

	/**
	 * Tries to delete all files in the Directory.
	 *
	 * @param file
	 *            The Directory to delete files.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	default void deleteFiles(File file) throws IOException
	{
		DeleteFileExtensions.deleteFiles(file);
	}

	/**
	 * Tries to delete all files that match the given includeFileFilter from the given source
	 * directory.
	 *
	 * @param sourceDir
	 *            The source directory.
	 * @param includeFileFilter
	 *            The FileFilter for the files to be deleted. If null all files will be deleted.
	 * @throws FileIsNotADirectoryException
	 *             If the destination file is a directory.
	 * @throws IOException
	 *             If an error occurs by reading or writing.
	 * @throws FileIsSecurityRestrictedException
	 *             If the source file is security restricted.
	 */
	default void deleteFilesWithFileFilter(File sourceDir, FileFilter includeFileFilter)
		throws FileIsNotADirectoryException, IOException, FileIsSecurityRestrictedException
	{
		DeleteFileExtensions.deleteFilesWithFileFilter(sourceDir, includeFileFilter);
	}

	/**
	 * Tries to delete all files that match the given prefix from the given source directory.
	 *
	 * @param sourceDir
	 *            The source directory
	 * @param prefix
	 *            The prefix from the files to delete
	 * @throws FileIsNotADirectoryException
	 *             If the destination file is a directory.
	 * @throws IOException
	 *             If an error occurs by reading or writing.
	 * @throws FileIsSecurityRestrictedException
	 *             If the source file is security restricted.
	 */
	default void deleteAllFilesWithPrefix(File sourceDir, String prefix)
		throws FileIsNotADirectoryException, FileIsSecurityRestrictedException, IOException
	{
		DeleteFileExtensions.deleteAllFilesWithPrefix(sourceDir, prefix);
	}

	/**
	 * Tries to delete all files that match the given includeFilenameFilter from the given source
	 * directory.
	 *
	 * @param source
	 *            The source directory.
	 * @param includeFilenameFilter
	 *            The FilenameFilter for the files to be deleted. If null all files will be deleted.
	 * @throws FileIsNotADirectoryException
	 *             If the destination file is a directory.
	 * @throws IOException
	 *             If an error occurs by reading or writing.
	 * @throws FileIsSecurityRestrictedException
	 *             If the source file is security restricted.
	 */
	default void deleteFilesWithFilenameFilter(File source, FilenameFilter includeFilenameFilter)
		throws FileIsNotADirectoryException, IOException, FileIsSecurityRestrictedException
	{
		DeleteFileExtensions.deleteFilesWithFilenameFilter(source, includeFilenameFilter);
	}
}
