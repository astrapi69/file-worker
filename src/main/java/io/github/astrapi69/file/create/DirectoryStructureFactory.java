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
package io.github.astrapi69.file.create;

import java.io.File;
import java.util.Collection;

import io.github.astrapi69.collection.array.ArrayExtensions;
import io.github.astrapi69.collection.list.ListFactory;
import io.github.astrapi69.file.create.model.FileContentInfo;

/**
 * The class {@link DirectoryStructureFactory} helps you to create directory structures
 *
 * @author Asterios Raptis
 * @version 1.0
 */
public final class DirectoryStructureFactory
{

	/**
	 * Private constructor to prevent instantiation
	 */
	private DirectoryStructureFactory()
	{
	}

	/**
	 * Factory method for create an initial directory structure that can contain also files with
	 * content
	 *
	 * @param fileInfos
	 *            the collection with the file infos
	 * @return the collection with the created directory {@link File} objects
	 */
	public static Collection<File> newDirectoryStructure(
		final Collection<FileContentInfo> fileInfos)
	{
		Collection<File> files = ListFactory.newArrayList();
		for (FileContentInfo fileContentInfo : fileInfos)
		{
			files.add(FileContentInfo.toFile(fileContentInfo));
		}
		return files;
	}

	/**
	 * Factory method for create an initial directory structure. Each directory path is resolved
	 * against the parent directory and created with its missing parent directories, so the path
	 * {@code "src/main/java"} creates {@code src}, {@code src/main} and {@code src/main/java}.
	 * Directories that already exist are left as they are. A parent directory that does not exist
	 * gives an empty collection, and nothing is created
	 *
	 * @param parentDirectory
	 *            the parent directory
	 * @param filePaths
	 *            the collection with the directory paths, relative to the parent directory
	 * @return the collection with the created directory {@link File} objects
	 * @throws RuntimeException
	 *             a {@link RuntimeException} itself, not a subclass, if a directory path cannot be
	 *             created; its cause is the {@link java.io.IOException} of
	 *             {@link java.nio.file.Files#createDirectories}: a
	 *             {@link java.nio.file.FileAlreadyExistsException} if a regular file exists at the
	 *             path, a {@link java.nio.file.FileSystemException} if one exists at a parent path
	 */
	public static Collection<File> newDirectoryStructure(final File parentDirectory,
		final Collection<String> filePaths)
	{
		Collection<File> directories = ListFactory.newArrayList();
		if (parentDirectory.isDirectory())
		{
			for (String filePath : filePaths)
			{
				File directory = new File(parentDirectory, filePath);
				DirectoryFactory.newDirectoriesQuietly(directory.toPath());
				directories.add(directory);
			}
		}
		return directories;
	}

	/**
	 * Factory method for create an initial directory structure. Each directory path is resolved
	 * against the parent directory and created with its missing parent directories, so the path
	 * {@code "src/main/java"} creates {@code src}, {@code src/main} and {@code src/main/java}.
	 * Directories that already exist are left as they are. A parent directory that does not exist
	 * gives an empty collection, and nothing is created
	 *
	 * @param parentAbsolutePath
	 *            the absolute path from the parent
	 * @param filePaths
	 *            the collection with the directory paths, relative to the parent directory
	 * @return the collection with the created directory {@link File} objects
	 * @throws RuntimeException
	 *             a {@link RuntimeException} itself, not a subclass, if a directory path cannot be
	 *             created; its cause is the {@link java.io.IOException} of
	 *             {@link java.nio.file.Files#createDirectories}: a
	 *             {@link java.nio.file.FileAlreadyExistsException} if a regular file exists at the
	 *             path, a {@link java.nio.file.FileSystemException} if one exists at a parent path
	 */
	public static Collection<File> newDirectoryStructure(final String parentAbsolutePath,
		final Collection<String> filePaths)
	{
		return newDirectoryStructure(DirectoryFactory.newDirectoryQuietly(parentAbsolutePath),
			filePaths);
	}

	/**
	 * Factory method for create an initial directory structure. Each directory path is resolved
	 * against the parent directory and created with its missing parent directories, so the path
	 * {@code "src/main/java"} creates {@code src}, {@code src/main} and {@code src/main/java}.
	 * Directories that already exist are left as they are. A parent directory that does not exist
	 * gives an empty collection, and nothing is created
	 *
	 * @param parentDirectory
	 *            the parent directory
	 * @param filePaths
	 *            the vararg with the directory paths, relative to the parent directory
	 * @return the collection with the created directory {@link File} objects
	 * @throws RuntimeException
	 *             a {@link RuntimeException} itself, not a subclass, if a directory path cannot be
	 *             created; its cause is the {@link java.io.IOException} of
	 *             {@link java.nio.file.Files#createDirectories}: a
	 *             {@link java.nio.file.FileAlreadyExistsException} if a regular file exists at the
	 *             path, a {@link java.nio.file.FileSystemException} if one exists at a parent path
	 */
	public static Collection<File> newDirectoryStructure(final File parentDirectory,
		String... filePaths)
	{
		return newDirectoryStructure(parentDirectory, ArrayExtensions.toList(filePaths));
	}

	/**
	 * Factory method for create an initial directory structure. Each directory path is resolved
	 * against the parent directory and created with its missing parent directories, so the path
	 * {@code "src/main/java"} creates {@code src}, {@code src/main} and {@code src/main/java}.
	 * Directories that already exist are left as they are. A parent directory that does not exist
	 * gives an empty collection, and nothing is created
	 *
	 * @param parentAbsolutePath
	 *            the absolute path from the parent
	 * @param filePaths
	 *            the vararg with the directory paths, relative to the parent directory
	 * @return the collection with the created directory {@link File} objects
	 * @throws RuntimeException
	 *             a {@link RuntimeException} itself, not a subclass, if a directory path cannot be
	 *             created; its cause is the {@link java.io.IOException} of
	 *             {@link java.nio.file.Files#createDirectories}: a
	 *             {@link java.nio.file.FileAlreadyExistsException} if a regular file exists at the
	 *             path, a {@link java.nio.file.FileSystemException} if one exists at a parent path
	 */
	public static Collection<File> newDirectoryStructure(final String parentAbsolutePath,
		final String... filePaths)
	{
		return newDirectoryStructure(DirectoryFactory.newDirectoryQuietly(parentAbsolutePath),
			ArrayExtensions.toList(filePaths));
	}

}
