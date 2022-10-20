package io.github.astrapi69.file.create;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.Collection;
import java.util.Objects;

import io.github.astrapi69.collection.array.ArrayExtensions;
import io.github.astrapi69.collection.list.ListFactory;
import io.github.astrapi69.throwable.RuntimeExceptionDecorator;

/**
 * The class {@link DirectoryFactory} helps you to create directories
 *
 * @author Asterios Raptis
 * @version 1.0
 */
public class DirectoryFactory
{

	private DirectoryFactory()
	{
	}

	/**
	 * Creates the directories.
	 *
	 * @param directories
	 *            the directories
	 *
	 * @return true, if successful
	 */
	public static FileCreationState newDirectories(final Collection<File> directories)
	{
		FileCreationState created = FileCreationState.PENDING;
		for (final File dir : directories)
		{
			created = DirectoryFactory.newDirectory(dir);
		}
		return created;

	}

	/**
	 * Creates a new directory from the given {@link Path} object and the optional
	 * {@link FileAttribute}.<br>
	 * <br>
	 * Note: this method decorates the {@link Files#createDirectories(Path, FileAttribute...)} and
	 * returns if the directory is created or not.
	 *
	 * @param dir
	 *            the dir the directory to create
	 * @param attrs
	 *            an optional list of file attributes to set atomically when creating the directory
	 * @return Returns true if the directory was created otherwise false.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @see Files#createDirectories(Path, FileAttribute...)
	 */
	public static boolean newDirectories(Path dir, FileAttribute<?>... attrs) throws IOException
	{
		Path directory = Files.createDirectories(dir, attrs);
		return Files.exists(directory);
	}

	/**
	 * Creates a new directory from the given {@link Path} object and the optional
	 * {@link FileAttribute}.<br>
	 * <br>
	 * Note: this method decorates the {@link Files#createDirectories(Path, FileAttribute...)} and
	 * returns if the directory is created or not.
	 *
	 * @param dir
	 *            the dir the directory to create
	 * @param attrs
	 *            an optional list of file attributes to set atomically when creating the directory
	 * @return Returns true if the directory was created otherwise false.
	 * @see Files#createDirectories(Path, FileAttribute...)
	 */
	public static boolean newDirectoriesQuietly(Path dir, FileAttribute<?>... attrs)
	{
		return RuntimeExceptionDecorator.decorate(() -> newDirectories(dir, attrs));
	}

	/**
	 * Creates a new directory from the given {@link Path} object and the optional
	 * {@link FileAttribute}.<br>
	 * <br>
	 * Note: this method decorates the {@link Files#createDirectory(Path, FileAttribute...)} and
	 * returns if the directory is created or not.
	 *
	 * @param dir
	 *            the dir the directory to create
	 * @param attrs
	 *            an optional list of file attributes to set atomically when creating the directory
	 * @return Returns true if the directory was created otherwise false.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @see Files#createDirectory(Path, FileAttribute...)
	 */
	public static boolean newDirectory(Path dir, FileAttribute<?>... attrs) throws IOException
	{
		Path directory = Files.createDirectory(dir, attrs);
		return Files.exists(directory);
	}

	/**
	 * Creates a new directory from the given {@link Path} object and the optional
	 * {@link FileAttribute}.<br>
	 * <br>
	 * Note: this method decorates the {@link Files#createDirectory(Path, FileAttribute...)} and
	 * returns if the directory is created or not.
	 *
	 * @param dir
	 *            the dir the directory to create
	 * @param attrs
	 *            an optional list of file attributes to set atomically when creating the directory
	 * @return Returns true if the directory was created otherwise false.
	 * @see Files#createDirectory(Path, FileAttribute...)
	 */
	public static boolean newDirectoryQuietly(Path dir, FileAttribute<?>... attrs)
	{
		return RuntimeExceptionDecorator.decorate(() -> newDirectory(dir, attrs));
	}

	/**
	 * Factory method that creates a new {@link File} object, if the given boolean flag is true a
	 * new empty file will be created on the file system
	 *
	 * @param absolutePath
	 *            the absolute path
	 *
	 * @return the file object
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static File newDirectory(final String absolutePath) throws IOException
	{
		Path dir = Paths.get(absolutePath);
		File directory = dir.toFile();
		if (!directory.exists())
		{
			newDirectory(dir);
		}
		return directory;
	}

	/**
	 * Factory method that creates a new {@link File} object, if the given boolean flag is true a
	 * new empty file will be created on the file system
	 *
	 * @param absolutePath
	 *            the absolute path
	 *
	 * @return the file object
	 */
	public static File newDirectoryQuietly(final String absolutePath)
	{
		Path dir = Paths.get(absolutePath);
		File directory = dir.toFile();
		if (!directory.exists())
		{
			newDirectoryQuietly(dir);
		}
		return directory;
	}

	/**
	 * Creates a new directory from the given {@link File} object
	 *
	 * @param directory
	 *            The directory to create
	 *
	 * @return the {@link FileCreationState} with the result
	 */
	public static FileCreationState newDirectory(final File directory)
	{
		FileCreationState fileCreationState = FileCreationState.ALREADY_EXISTS;
		// If the directory does not exists
		if (!directory.exists())
		{ // then
			fileCreationState = FileCreationState.FAILED;
			// create it...
			if (directory.mkdir())
			{
				fileCreationState = FileCreationState.CREATED;
			}
		}
		return fileCreationState;
	}

	/**
	 * Factory method for creating the new directory as {@link File} objects if it is not exists.
	 *
	 * @param parentDirectory
	 *            the parent directory
	 * @param directoryName
	 *            the directory name
	 * @return the new directory as {@link File} object
	 */
	public static File newDirectory(final File parentDirectory, final String directoryName)
	{
		Objects.requireNonNull(parentDirectory);
		Objects.requireNonNull(directoryName);
		if (!parentDirectory.exists())
		{
			throw new RuntimeException("Given parent directory does not exist");
		}
		if (!parentDirectory.isDirectory())
		{
			throw new RuntimeException("Given parent file is not a directory");
		}
		File directory = new File(parentDirectory, directoryName);
		newDirectory(directory);
		return directory;
	}

	/**
	 * Factory method for creating the new directory as {@link File} objects if it is not exists.
	 *
	 * @param parentDirectory
	 *            the parent directory
	 * @param directoryName
	 *            the directory name
	 * @return the new directory as {@link File} object
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @see Files#createDirectory(Path, FileAttribute...)
	 */
	public static File newDirectory(final String parentDirectory, final String directoryName)
		throws IOException
	{
		Path dir = Paths.get(parentDirectory, directoryName);
		File directory = dir.toFile();
		if (!directory.exists())
		{
			newDirectory(dir);
		}
		return directory;
	}

	/**
	 * Factory method for creating the new directory as {@link File} objects if it is not exists.
	 *
	 * @param parentDirectory
	 *            the parent directory
	 * @param directoryName
	 *            the directory name
	 * @return the new directory as {@link File} object
	 * @see Files#createDirectory(Path, FileAttribute...)
	 */
	public static File newDirectoryQuietly(final String parentDirectory, final String directoryName)
	{
		Path dir = Paths.get(parentDirectory, directoryName);
		File directory = dir.toFile();
		if (!directory.exists())
		{
			newDirectoryQuietly(dir);
		}
		return directory;
	}

	/**
	 * Creates the parent directories from the given file.
	 *
	 * @param file
	 *            the file
	 * @return true, if successful
	 */
	public static boolean mkParentDirs(final File file)
	{
		if (!file.exists())
		{
			final File parent = file.getParentFile();
			if (parent != null && !parent.exists())
			{
				return parent.mkdirs();
			}
		}
		return true;
	}

	/**
	 * Factory method for create an initial directory structure
	 *
	 * @param parentDirectory
	 *            the parent directory
	 * @param filePaths
	 *            the collection with the directory names
	 * @return the collection with the created directory {@link File} objects
	 */
	public static Collection<File> newDirectoryStructure(final File parentDirectory,
		final Collection<String> filePaths)
	{
		Collection<File> directories = ListFactory.newArrayList();
		if (parentDirectory.isDirectory())
		{
			for (String filePath : filePaths)
			{
				directories.add(DirectoryFactory.newDirectory(parentDirectory, filePath));
			}
		}
		return directories;
	}

	/**
	 * Factory method for create an initial directory structure
	 *
	 * @param parentAbsolutePath
	 *            the absolute path from the parent
	 * @param filePaths
	 *            the collection with the directory names
	 * @return the collection with the created directory {@link File} objects
	 */
	public static Collection<File> newDirectoryStructure(final String parentAbsolutePath,
		final Collection<String> filePaths)
	{
		return newDirectoryStructure(DirectoryFactory.newDirectoryQuietly(parentAbsolutePath),
			filePaths);
	}

	/**
	 * Factory method for create an initial directory structure
	 *
	 * @param parentDirectory
	 *            the parent directory
	 * @param filePaths
	 *            the vararg with the directory names
	 * @return the collection with the created directory {@link File} objects
	 */
	public static Collection<File> newDirectoryStructure(final File parentDirectory,
		String... filePaths)
	{
		return newDirectoryStructure(parentDirectory, ArrayExtensions.toList(filePaths));
	}

	/**
	 * Factory method for create an initial directory structure
	 *
	 * @param parentAbsolutePath
	 *            the absolute path from the parent
	 * @param filePaths
	 *            the vararg with the directory names
	 * @return the collection with the created directory {@link File} objects
	 */
	public static Collection<File> newDirectoryStructure(final String parentAbsolutePath,
		final String... filePaths)
	{
		return newDirectoryStructure(DirectoryFactory.newDirectoryQuietly(parentAbsolutePath),
			ArrayExtensions.toList(filePaths));
	}

}
