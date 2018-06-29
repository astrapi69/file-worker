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
package de.alpharogroup.file.rename;

import java.io.File;
import java.io.IOException;

import de.alpharogroup.file.copy.CopyFileExtensions;
import de.alpharogroup.file.delete.DeleteFileExtensions;
import de.alpharogroup.file.exceptions.FileDoesNotExistException;
import de.alpharogroup.file.exceptions.FileIsADirectoryException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * The class {@link RenameFileQuietlyExtensions} helps you to rename files or directories in a
 * quietly manner as the name let presume.
 *
 * @author Asterios Raptis
 * @version 1.0
 */
@UtilityClass
@Slf4j
public final class RenameFileQuietlyExtensions
{

	/**
	 * Moves the given source file to the given destination file.
	 *
	 * @param srcFile
	 *            The source file.
	 * @param destinationFile
	 *            The destination file.
	 *
	 * @return true if the file was moved otherwise false.
	 */
	public static boolean forceToMoveFileQuietly(final File srcFile, final File destinationFile)
	{
		boolean moved = false;
		try
		{
			moved = RenameFileExtensions.forceToMoveFile(srcFile, destinationFile);
		}
		catch (IOException e)
		{
			log.error("forceToMoveFile method failed...\n" + e.getMessage(), e);
		}
		catch (FileIsADirectoryException e)
		{
			log.error("forceToMoveFile method failed...\n" + e.getMessage(), e);
		}
		return moved;
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
	 * @param delete
	 *            If true an attempt to copy the content from the file to rename to the new file and
	 *            then delete the file to rename otherwise not.
	 *
	 * @return 's true if the file was renamed otherwise false.
	 */
	public static boolean renameFileQuietly(final File fileToRename, final File newFileName,
		final boolean delete)
	{
		boolean success = fileToRename.renameTo(newFileName);
		if (!success)
		{
			log.info("The file " + fileToRename.getName() + " was not renamed.");

			if (delete)
			{
				log.info("Try to copy the content into the new file with the new name.");
				try
				{
					final boolean copied = CopyFileExtensions.copyFile(fileToRename, newFileName);
					if (copied)
					{
						log.info("Sucessfully copied the old file " + fileToRename.getName()
							+ " to the new file " + newFileName.getName() + ".");
					}
					else
					{
						log.info("Try to copy file " + fileToRename.getName()
							+ " into the new file " + newFileName.getName() + " failed.");
					}
				}
				catch (final IOException e)
				{
					log.error("Try to copy file " + fileToRename.getName() + " into the new file "
						+ newFileName.getName() + " failed.");
				}
				catch (final FileIsADirectoryException e)
				{
					log.error("Given file " + newFileName.getName() + " is a directory.", e);
				}
				log.info("Try to delete the old file " + fileToRename.getName() + ".");
				try
				{
					DeleteFileExtensions.delete(fileToRename);
					success = true;
				}
				catch (final IOException e)
				{
					log.error("Try to delete the old file " + fileToRename.getName() + " failed.",
						e);
				}
			}
		}
		return success;
	}

	/**
	 * This method renames a given file. For instance if we have a file which we want to rename with
	 * the path "/tmp/test.dat" to "/tmp/renamed.dat" then you call the method as follow:
	 * renameFile(new File("C://tmp//test.dat"), new File("C://tmp//renamed.dat"));
	 *
	 * @param fileToRename
	 *            The file to rename.
	 * @param newFileNameWithoutAbsolutPath
	 *            The new name from the file.
	 * @return 's true if the file was renamed otherwise false.
	 */
	public static boolean renameFileQuietly(final File fileToRename,
		final String newFileNameWithoutAbsolutPath)
	{
		boolean renamed = false;
		try
		{
			renamed = RenameFileExtensions.renameFile(fileToRename, newFileNameWithoutAbsolutPath);
		}
		catch (IOException e)
		{
			log.error(e.getLocalizedMessage(), e);
		}
		catch (FileIsADirectoryException e)
		{
			log.error(e.getLocalizedMessage(), e);
		}
		catch (FileDoesNotExistException e)
		{
			log.error(e.getLocalizedMessage(), e);
		}
		return renamed;
	}

}
