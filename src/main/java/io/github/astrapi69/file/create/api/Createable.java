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
package io.github.astrapi69.file.create.api;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.Collection;

import io.github.astrapi69.file.create.DirectoryFactory;
import io.github.astrapi69.file.create.FileFactory;
import io.github.astrapi69.file.create.model.FileCreationState;

/**
 * The {@code Createable} interface provides methods for creating files and directories with a
 * variety of configurations and options
 */
public interface Createable
{

	/**
	 * Creates the directories
	 *
	 * @param directories
	 *            the directories
	 * @return collection of {@link FileCreationState} with the result of each directory creation
	 */
	default Collection<FileCreationState> newDirectories(Collection<File> directories)
	{
		return DirectoryFactory.newDirectories(directories);
	}

	/**
	 * Creates new directories from the given path and optional file attributes
	 *
	 * @param dir
	 *            the directory to create
	 * @param attrs
	 *            optional file attributes to set atomically when creating the directory
	 * @return true if the directories were created; false otherwise
	 * @throws IOException
	 *             Signals an I/O exception occurred
	 */
	default boolean newDirectories(Path dir, FileAttribute<?>... attrs) throws IOException
	{
		return DirectoryFactory.newDirectories(dir, attrs);
	}

	/**
	 * Creates a new directory from the given path and optional file attributes
	 *
	 * @param dir
	 *            the directory to create
	 * @param attrs
	 *            optional file attributes to set atomically when creating the directory
	 * @return true if the directory was created; false otherwise
	 * @throws IOException
	 *             Signals an I/O exception occurred
	 */
	default boolean newDirectory(Path dir, FileAttribute<?>... attrs) throws IOException
	{
		return DirectoryFactory.newDirectory(dir, attrs);
	}

	/**
	 * Creates a new directory from the specified absolute path
	 *
	 * @param absolutePath
	 *            the absolute path
	 * @return the created directory as a {@link File} object
	 * @throws IOException
	 *             Signals an I/O exception occurred
	 */
	default File newDirectory(String absolutePath) throws IOException
	{
		return DirectoryFactory.newDirectory(absolutePath);
	}

	/**
	 * Creates a new directory from the given {@link File} object
	 *
	 * @param directory
	 *            the directory to create
	 * @return the {@link FileCreationState} with the result
	 */
	default FileCreationState newDirectory(File directory)
	{
		return DirectoryFactory.newDirectory(directory);
	}

	/**
	 * Factory method for creating the new directory as {@link File} objects if it does not exist
	 *
	 * @param parentDirectory
	 *            the parent directory
	 * @param directoryName
	 *            the directory name
	 * @return the new directory as {@link File} object
	 */
	default File newDirectory(File parentDirectory, String directoryName)
	{
		return DirectoryFactory.newDirectory(parentDirectory, directoryName);
	}

	/**
	 * Creates parent directories for the given file if they do not already exist
	 *
	 * @param file
	 *            the file for which parent directories need to be created
	 * @return true if the parent directories were created successfully; false otherwise
	 */
	default boolean mkParentDirs(File file)
	{
		return DirectoryFactory.mkParentDirs(file);
	}

	/**
	 * Factory method that creates a new empty {@link File} if it does not exist, otherwise it
	 * leaves the file as it is
	 *
	 * @param file
	 *            the file.
	 *
	 * @return the state describing the creation outcome
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	default FileCreationState newFile(File file) throws IOException
	{
		return FileFactory.newFile(file);
	}

	/**
	 * Creates a new file from the specified absolute path
	 *
	 * @param absolutePath
	 *            the absolute path
	 * @return the created file as a {@link File} object
	 * @throws IOException
	 *             Signals an I/O exception occurred
	 */
	default File newFile(String absolutePath) throws IOException
	{
		return FileFactory.newFile(absolutePath);
	}

	/**
	 * Creates a new file from the specified absolute path, creating it if it does not exist
	 *
	 * @param absolutePath
	 *            the absolute path
	 * @param createIfNotExists
	 *            flag indicating if the file should be created if it does not exist
	 * @return the created file as a {@link File} object
	 * @throws IOException
	 *             Signals an I/O exception occurred
	 */
	default File newFile(String absolutePath, boolean createIfNotExists) throws IOException
	{
		return FileFactory.newFile(absolutePath, createIfNotExists);
	}

	/**
	 * Creates a new file in the specified parent directory with the given filename
	 *
	 * @param parentDirectory
	 *            the parent directory
	 * @param filename
	 *            the file name
	 * @return the created file as a {@link File} object
	 * @throws IOException
	 *             Signals an I/O exception occurred
	 */
	default File newFile(File parentDirectory, String filename) throws IOException
	{
		return FileFactory.newFile(parentDirectory, filename);
	}

	/**
	 * Creates all files in the collection as empty files if they do not exist, otherwise leaves
	 * them as they are
	 *
	 * @param files
	 *            the collection of {@link File} objects
	 *
	 * @return the {@link FileCreationState} object representing the overall creation result
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	default FileCreationState newFiles(Collection<File> files) throws IOException
	{
		return FileFactory.newFiles(files);
	}

}
