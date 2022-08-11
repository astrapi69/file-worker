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
package io.github.astrapi69.file.rename;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.github.astrapi69.file.FileExtensions;
import io.github.astrapi69.file.copy.CopyFileExtensions;
import io.github.astrapi69.file.delete.DeleteFileExtensions;
import io.github.astrapi69.file.exception.FileDoesNotExistException;
import io.github.astrapi69.file.exception.FileIsADirectoryException;
import io.github.astrapi69.file.exception.FileNotRenamedException;
import io.github.astrapi69.file.search.FileSearchExtensions;

/**
 * The class {@link RenameFileExtensions} helps you to rename files or directories.
 *
 * @author Asterios Raptis
 * @version 1.0
 */
public final class RenameFileExtensions
{

	private RenameFileExtensions()
	{
	}

	/**
	 * Returns the filename from the given file with the systemtime.
	 *
	 * @param fileToRename
	 *            The file to rename.
	 *
	 * @return Returns the filename from the given file with the systemtime.
	 */
	public static String appendSystemtimeToFilename(final File fileToRename)
	{
		return appendSystemtimeToFilename(fileToRename, null);
	}

	/**
	 * Returns the filename from the given file with the systemtime.
	 *
	 * @param fileToRename
	 *            The file.
	 * @param add2Name
	 *            Adds the Date to the Filename.
	 *
	 * @return Returns the filename from the given file with the systemtime.
	 */
	public static String appendSystemtimeToFilename(final File fileToRename, final Date add2Name)
	{
		final String format = "HHmmssSSS";
		String sysTime = null;
		if (null != add2Name)
		{
			final DateFormat df = new SimpleDateFormat(format);
			sysTime = df.format(add2Name);
		}
		else
		{
			final DateFormat df = new SimpleDateFormat(format);
			sysTime = df.format(new Date());
		}

		final String fileName = fileToRename.getName();
		final int ext_index = fileName.lastIndexOf(".");
		final String ext = fileName.substring(ext_index);
		String newName = fileName.substring(0, ext_index);
		newName += "_" + sysTime + ext;
		return newName;
	}

	/**
	 * Changes all the Filenames with the new Suffix recursively.
	 *
	 * @param file
	 *            The file where to change the Filename with the new Suffix.
	 * @param oldSuffix
	 *            All files that have the old suffix will be renamed with the new Suffix.
	 * @param newSuffix
	 *            The new suffix.
	 * @return the list of files
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws FileDoesNotExistException
	 *             If the file does not exist.
	 * @throws FileIsADirectoryException
	 *             the file is A directory exception
	 */
	public static List<File> changeAllFilenameSuffix(final File file, final String oldSuffix,
		final String newSuffix)
		throws IOException, FileDoesNotExistException, FileIsADirectoryException
	{
		return changeAllFilenameSuffix(file, oldSuffix, newSuffix, false);
	}

	/**
	 * Changes all the Filenames with the new Suffix recursively. If delete is true its deletes the
	 * existing file with the same name.
	 *
	 * @param file
	 *            The file where to change the Filename with the new Suffix.
	 * @param oldSuffix
	 *            All files that have the old suffix will be renamed with the new Suffix.
	 * @param newSuffix
	 *            The new suffix.
	 * @param delete
	 *            If its true than its deletes the existing file with the same name. But before it
	 *            copys the contents into the new File.
	 * @return the list of files
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws FileDoesNotExistException
	 *             If the file does not exist.
	 * @throws FileIsADirectoryException
	 *             the file is A directory exception
	 */
	public static List<File> changeAllFilenameSuffix(final File file, final String oldSuffix,
		final String newSuffix, final boolean delete)
		throws IOException, FileDoesNotExistException, FileIsADirectoryException
	{
		boolean success;
		List<File> notDeletedFiles = null;
		final String filePath = file.getAbsolutePath();
		final String[] suffix = { oldSuffix };
		final List<File> files = FileSearchExtensions.findFiles(filePath, suffix);
		final int fileCount = files.size();
		for (int i = 0; i < fileCount; i++)
		{
			final File currentFile = files.get(i);
			success = RenameFileExtensions.changeFilenameSuffix(currentFile, newSuffix, delete);
			if (!success)
			{
				if (null != notDeletedFiles)
				{
					notDeletedFiles.add(currentFile);
				}
				else
				{
					notDeletedFiles = new ArrayList<>();
					notDeletedFiles.add(currentFile);
				}
			}
		}
		return notDeletedFiles;
	}

	/**
	 * Changes the suffix from the Filename. Example: test.dat to test.xxx
	 *
	 * @param file
	 *            The file to change.
	 * @param newSuffix
	 *            The new suffix. You must start with a dot. For instance: .xxx
	 * @return true if the file was renamed.
	 * @throws FileNotRenamedException
	 *             If the file could not renamed.
	 * @throws FileDoesNotExistException
	 *             If the file does not exist.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws FileIsADirectoryException
	 *             the file is A directory exception
	 */
	public static boolean changeFilenameSuffix(final File file, final String newSuffix)
		throws FileNotRenamedException, FileDoesNotExistException, IOException,
		FileIsADirectoryException
	{
		return changeFilenameSuffix(file, newSuffix, false);
	}

	/**
	 * Changes the suffix from the Filename. Example: test.dat to test.xxx
	 *
	 * @param file
	 *            The file to change.
	 * @param newSuffix
	 *            The new suffix. You must start with a dot. For instance: .xxx
	 * @param delete
	 *            If its true than its deletes the existing file with the same name. But before it
	 *            copys the contents into the new File.
	 * @return true, if change filename suffix
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws FileDoesNotExistException
	 *             If the file does not exist.
	 * @throws FileIsADirectoryException
	 *             the file is A directory exception
	 */
	public static boolean changeFilenameSuffix(final File file, final String newSuffix,
		final boolean delete)
		throws IOException, FileDoesNotExistException, FileIsADirectoryException
	{
		if (!file.exists())
		{
			final String error = "The " + file + " does not exists.";
			throw new FileDoesNotExistException(error);
		}
		final String fileNamePrefix = FileExtensions.getFilenamePrefix(file);
		final String newFilename = fileNamePrefix + newSuffix;
		final File file2 = new File(newFilename);
		final boolean success = RenameFileExtensions.renameFile(file, file2, delete);
		return success;
	}

	/**
	 * Moves the given source file to the given destination file.
	 *
	 * @param srcFile
	 *            The source file.
	 * @param destinationFile
	 *            The destination file.
	 * @return true if the file was moved otherwise false.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws FileIsADirectoryException
	 *             the file is A directory exception
	 */
	public static boolean forceToMoveFile(final File srcFile, final File destinationFile)
		throws IOException, FileIsADirectoryException
	{
		boolean moved = RenameFileExtensions.renameFile(srcFile, destinationFile, true);
		return moved;
	}

	/**
	 * Moves the given source file to the destination Directory.
	 *
	 * @param srcFile
	 *            The source file.
	 * @param destDir
	 *            The destination directory.
	 * @return true if the file was moved otherwise false.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws FileIsADirectoryException
	 *             the file is A directory exception
	 */
	public static boolean moveFile(final File srcFile, final File destDir)
		throws IOException, FileIsADirectoryException
	{
		return RenameFileExtensions.renameFile(srcFile, destDir, true);
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
	public static boolean renameFile(final File fileToRename, final File newFileName)
		throws IOException, FileIsADirectoryException
	{
		return renameFile(fileToRename, newFileName, false);
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
	 * @return 's true if the file was renamed otherwise false.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws FileIsADirectoryException
	 *             the file is A directory exception
	 */
	public static boolean renameFile(final File fileToRename, final File newFileName,
		final boolean delete) throws IOException, FileIsADirectoryException
	{
		boolean success = fileToRename.renameTo(newFileName);
		if (!success)
		{
			if (delete)
			{
				CopyFileExtensions.copyFile(fileToRename, newFileName);
				DeleteFileExtensions.delete(fileToRename);
				success = true;
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
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws FileIsADirectoryException
	 *             the file is A directory exception
	 * @throws FileDoesNotExistException
	 *             the file does not exist exception
	 */
	public static boolean renameFile(final File fileToRename,
		final String newFileNameWithoutAbsolutPath)
		throws IOException, FileIsADirectoryException, FileDoesNotExistException
	{
		if (!fileToRename.exists())
		{
			throw new FileDoesNotExistException(
				"File" + fileToRename.getName() + " does not exists!");
		}
		final String fileNameAbsolutPathPrefix = FileExtensions
			.getAbsolutPathWithoutFilename(fileToRename);
		final StringBuffer sb = new StringBuffer();
		sb.append(fileNameAbsolutPathPrefix);
		sb.append(newFileNameWithoutAbsolutPath);
		final File newNameForFile = new File(sb.toString());
		final boolean sucess = renameFile(fileToRename, newNameForFile);
		return sucess;
	}

	/**
	 * Renames the given file and add to the filename the systemtime.
	 *
	 * @param fileToRename
	 *            The file to rename.
	 * @return Returns the renamed file from the given file with the systemtime.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws FileIsADirectoryException
	 *             the file is A directory exception
	 */
	public static File renameFileWithSystemtime(final File fileToRename)
		throws IOException, FileIsADirectoryException
	{
		final String newFilenameWithSystemtime = appendSystemtimeToFilename(fileToRename);
		final File fileWithNewName = new File(fileToRename.getParent(), newFilenameWithSystemtime);
		renameFile(fileToRename, fileWithNewName, true);
		return fileWithNewName;
	}


}
