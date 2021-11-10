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
package io.github.astrapi69.file.system;

import java.io.File;
import java.util.Optional;

import org.apache.commons.lang3.SystemUtils;

/**
 * The class {@link SystemFileExtensions} provide methods for get system or user files.
 *
 * @author Asterios Raptis
 * @version 1.0
 */
public final class SystemFileExtensions
{

	/**
	 * Constant for the default configuration directory from the current user. current
	 * value:".config"
	 */
	public static final String DEFAULT_USER_CONFIGURATION_DIRECTORY_NAME = ".config";

	/**
	 * Constant for the default download directory from the current user. current value:"Downloads"
	 */
	public static final String DEFAULT_USER_DOWNLOAD_DIRECTORY_NAME = "Downloads";

	private SystemFileExtensions()
	{
	}

	/**
	 * Gets the installation directory for Java Runtime Environment (JRE) from the system as
	 * {@link File} object
	 *
	 * @return the installation directory for Java Runtime Environment (JRE) from the system as
	 *         {@link File} object
	 */
	public static File getJavaHomeDir()
	{
		return new File(SystemPropertiesExtensions.getJavaHome());
	}

	/**
	 * Gets the temporary directory from the system as File object.
	 *
	 * @return the temporary directory from the system.
	 */
	public static File getTempDir()
	{
		return new File(SystemPropertiesExtensions.getJavaIoTmpdir());
	}

	/**
	 * Gets the user home directory from the system as {@link File} object
	 *
	 * @return the user home directory from the system as {@link File} object
	 */
	public static File getUserHomeDir()
	{
		return new File(SystemPropertiesExtensions.getUserHome());
	}

	/**
	 * Gets the users downloads directory from the system as {@link File} object
	 *
	 * @return the users downloads directory from the system as {@link File} object
	 */
	public static File getUserDownloadsDir()
	{
		return getUserDownloadsDir("");
	}

	/**
	 * Gets the users downloads directory from the system as {@link File} object
	 *
	 * @param downloadsDirname
	 *            The name of the downloads directory, if null or empty the default value
	 *            'Downloads' will be taken
	 * @return the users downloads directory from the system as {@link File} object
	 */
	public static File getUserDownloadsDir(String downloadsDirname)
	{
		String ddn = downloadsDirname;
		if (downloadsDirname == null || downloadsDirname.isEmpty())
		{
			ddn = DEFAULT_USER_DOWNLOAD_DIRECTORY_NAME;
		}
		String userHomePath = SystemPropertiesExtensions.getUserHome();
		return new File(userHomePath + File.separator + ddn);
	}

	/**
	 * Gets the users configuration directory from the system as {@link File} object
	 *
	 * @param configurationDirname
	 *            The name of the configuration directory, if null or empty the default value
	 *            '.config' will be taken
	 * @return the users configuration directory from the system as {@link File} object
	 */
	public static File getUserConfigurationDir(String configurationDirname)
	{
		String ddn = configurationDirname;
		if (configurationDirname == null || configurationDirname.isEmpty())
		{
			ddn = DEFAULT_USER_CONFIGURATION_DIRECTORY_NAME;
		}
		String userHomePath = SystemPropertiesExtensions.getUserHome();
		return new File(userHomePath + File.separator + ddn);
	}

	/**
	 * Gets the users configuration directory from the system as {@link File} object
	 *
	 * @return the users configuration directory from the system as {@link File} object
	 */
	public static File getUserConfigurationDir()
	{
		return getUserConfigurationDir("");
	}

	/**
	 * Gets the user working directory from the system as {@link File} object
	 *
	 * @return the user working directory from the system as {@link File} object
	 */
	public static File getUserWorkingDir()
	{
		String userWorkingPath = SystemPropertiesExtensions.getUserWorkingDirectory();
		return new File(userWorkingPath);
	}

	/**
	 * Gets the root directory from the system as {@link File} object
	 *
	 * @return the root directory from the system as {@link File} object
	 */
	public static File getRootDir()
	{
		if (SystemUtils.IS_OS_WINDOWS)
		{
			String systemDrive = System.getenv("SystemDrive");
			return new File(systemDrive);
		}
		return new File("/");
	}

	/**
	 * Gets the drive directory from the given drive letter as {@link Optional} of {@link File}
	 * object. If not found an empty {@link Optional} will be returned
	 *
	 * @param driveLetter
	 *            the drive letter
	 * 
	 * @return the drive directory from the given drive letter as {@link File} object or an empty
	 *         {@link Optional} if not found
	 */
	public static Optional<File> getWindowsRootDriveDir(char driveLetter)
	{
		if (SystemUtils.IS_OS_WINDOWS)
		{
			String driveName = driveLetter + ":";
			return Optional.of(new File(driveName));
		}
		return Optional.empty();
	}

}
