/**
 * The MIT License
 *
 * Copyright (C) 2007 Asterios Raptis
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
package de.alpharogroup.file.search;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The Class PathFinder is a helper class for getting source folders from maven projects.
 */
public final class PathFinder
{
	/**
	 * The Constant SOURCE_FOLDER_SRC_MAIN_RESOURCES keeps the relative path for the source folder
	 * 'src/main/resources' in maven projects.
	 * */
	public static final String SOURCE_FOLDER_SRC_MAIN_RESOURCES = "src/main/resources";

	/**
	 * The Constant SOURCE_FOLDER_SRC_MAIN_JAVA keeps the relative path for the source folder
	 * 'src/main/java' in maven projects.
	 * */
	public static final String SOURCE_FOLDER_SRC_MAIN_JAVA = "src/main/java";

	/**
	 * The Constant SOURCE_FOLDER_SRC_TEST_RESOURCES keeps the relative path for the source folder
	 * 'src/test/resources' in maven projects.
	 * */
	public static final String SOURCE_FOLDER_SRC_TEST_RESOURCES = "src/test/resources";

	/**
	 * The Constant SOURCE_FOLDER_SRC_TEST_JAVA keeps the relative path for the source folder
	 * 'src/test/java' in maven projects.
	 * */
	public static final String SOURCE_FOLDER_SRC_TEST_JAVA = "src/test/java";

	/**
	 * Gets the absolute path.
	 *
	 * @param file
	 *            the file
	 * @param removeLastChar
	 *            the remove last char
	 * @return the absolute path
	 */
	public static String getAbsolutePath(final File file, final boolean removeLastChar)
	{
		String absolutePath = file.getAbsolutePath();
		if (removeLastChar)
		{
			absolutePath = absolutePath.substring(0, absolutePath.length() - 1);
		}
		return absolutePath;
	}

	/**
	 * Gets the project directory.
	 *
	 * @return the project directory
	 */
	public static File getProjectDirectory()
	{
		return getProjectDirectory(new File("."));
	}

	/**
	 * Gets the project directory.
	 *
	 * @param currentDir
	 *            the current dir
	 * @return the project directory
	 */
	public static File getProjectDirectory(final File currentDir)
	{
		final String projectPath = PathFinder.getAbsolutePath(currentDir, true);
		final File projectFile = new File(projectPath);
		return projectFile;
	}

	/**
	 * Gets the file or directory from the given parent File object and the relative path given over
	 * the list as String objects.
	 *
	 * @param parent
	 *            The parent directory.
	 * @param folders
	 *            The list with the directories and optional a filename.
	 * @return the resulted file or directory from the given arguments.
	 */
	public static File getRelativePath(final File parent, final String... folders)
	{
		return getRelativePathTo(parent, Arrays.asList(folders));
	}

	/**
	 * Gets the file or directory from the given parent File object and the relative path given over
	 * the list as String objects.
	 *
	 * @param parent
	 *            The parent directory.
	 * @param folders
	 *            The list with the directories and optional a filename.
	 * @return the resulted file or directory from the given arguments.
	 */
	public static File getRelativePathTo(File parent, final List<String> folders)
	{
		for (final String string : folders)
		{
			final File nextFolder = new File(parent, string);
			parent = nextFolder;
		}
		return parent;
	}

	/**
	 * Gets the file or directory from the given parent File object and the relative path given over
	 * the list as String objects.
	 *
	 * @param parent
	 *            The parent directory.
	 * @param separator
	 *            The separator for separate the String folders.
	 * @param folders
	 *            The relative path as a String object separated with the defined separator.
	 * @param filename
	 *            The filename.
	 * @return the resulted file or directory from the given arguments.
	 */
	public static File getRelativePathTo(final File parent, final String separator,
		final String folders, final String filename)
	{
		final List<String> list = new ArrayList<>(Arrays.asList(folders.split(separator)));
		if (filename != null && !filename.isEmpty())
		{
			list.add(filename);
		}
		return getRelativePathTo(parent, list);
	}

	/**
	 * Gets the src main java dir.
	 *
	 * @return the src main java dir
	 */
	public static File getSrcMainJavaDir()
	{
		return new File(getProjectDirectory(), PathFinder.SOURCE_FOLDER_SRC_MAIN_JAVA);
	}

	/**
	 * Gets the src main java dir.
	 *
	 * @param projectDirectory
	 *            the project directory
	 * @return the src main java dir
	 */
	public static File getSrcMainJavaDir(final File projectDirectory)
	{
		return new File(projectDirectory, PathFinder.SOURCE_FOLDER_SRC_MAIN_JAVA);
	}


	/**
	 * Gets the src main resources dir.
	 *
	 * @return the src main resources dir
	 */
	public static File getSrcMainResourcesDir()
	{
		return new File(getProjectDirectory(), PathFinder.SOURCE_FOLDER_SRC_MAIN_RESOURCES);
	}

	/**
	 * Gets the src main resources dir.
	 *
	 * @param projectDirectory
	 *            the project directory
	 * @return the src main resources dir
	 */
	public static File getSrcMainResourcesDir(final File projectDirectory)
	{
		return new File(projectDirectory, PathFinder.SOURCE_FOLDER_SRC_MAIN_RESOURCES);
	}


	/**
	 * Gets the src test java dir.
	 *
	 * @return the src test java dir
	 */
	public static File getSrcTestJavaDir()
	{
		return new File(getProjectDirectory(), PathFinder.SOURCE_FOLDER_SRC_TEST_JAVA);
	}

	/**
	 * Gets the src test java dir.
	 *
	 * @param projectDirectory
	 *            the project directory
	 * @return the src test java dir
	 */
	public static File getSrcTestJavaDir(final File projectDirectory)
	{
		return new File(projectDirectory, PathFinder.SOURCE_FOLDER_SRC_TEST_JAVA);
	}

	/**
	 * Gets the src test resources dir.
	 *
	 * @return the src test resources dir
	 */
	public static File getSrcTestResourcesDir()
	{
		return new File(getProjectDirectory(), PathFinder.SOURCE_FOLDER_SRC_TEST_RESOURCES);
	}

	/**
	 * Gets the src test resources dir.
	 *
	 * @param projectDirectory
	 *            the project directory
	 * @return the src test resources dir
	 */
	public static File getSrcTestResourcesDir(final File projectDirectory)
	{
		return new File(projectDirectory, PathFinder.SOURCE_FOLDER_SRC_TEST_RESOURCES);
	}

	/**
	 * Private constructor.
	 */
	private PathFinder()
	{
		super();
	}

}
