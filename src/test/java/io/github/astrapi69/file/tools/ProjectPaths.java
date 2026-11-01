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
package io.github.astrapi69.file.tools;

import io.github.astrapi69.io.shell.OS;

/**
 * Enum for OS-specific project directory paths
 */
public enum ProjectPaths
{

	MANJARO("/home/astrapi69/dev/git/hub", "/run/media/astrapi69/backups/git/hub/astrapi69",
		"/home/astrapi69/PycharmProjects"),

	UBUNTU("/home/astrapi69/dev/git/hub", "/media/astrapi69/T7_Shield/dev/git/hub",
		"/home/astrapi69/PycharmProjects"),

	WINDOWS("C:\\Users\\astrapi69\\dev\\git\\hub", "D:\\backups\\git\\hub\\astrapi69",
		"C:\\Users\\astrapi69\\PycharmProjects"),

	MAC("/Users/astrapi69/dev/git/hub", "/Volumes/backups/git/hub/astrapi69",
		"/Users/astrapi69/PycharmProjects");

	private final String homeGithubBookProjectBaseDir;
	private final String driveBookProjectBaseDir;
	private final String pyCharmBookProjectBaseDir;

	/**
	 * Constructor
	 *
	 * @param homeGithubBookProjectBaseDir
	 *            the home GitHub project base directory
	 * @param driveBookProjectBaseDir
	 *            the drive backup project base directory
	 * @param pyCharmBookProjectBaseDir
	 *            the PyCharm projects base directory
	 */
	ProjectPaths(String homeGithubBookProjectBaseDir, String driveBookProjectBaseDir,
		String pyCharmBookProjectBaseDir)
	{
		this.homeGithubBookProjectBaseDir = homeGithubBookProjectBaseDir;
		this.driveBookProjectBaseDir = driveBookProjectBaseDir;
		this.pyCharmBookProjectBaseDir = pyCharmBookProjectBaseDir;
	}

	/**
	 * Gets the home GitHub project base directory for this enum instance
	 *
	 * @return the home GitHub project base directory
	 */
	public String homeGithubBookProjectBaseDir()
	{
		return homeGithubBookProjectBaseDir;
	}

	/**
	 * Gets the drive backup project base directory for this enum instance
	 *
	 * @return the drive backup project base directory
	 */
	public String driveBookProjectBaseDir()
	{
		return driveBookProjectBaseDir;
	}

	/**
	 * Gets the PyCharm projects base directory for this enum instance
	 *
	 * @return the PyCharm projects base directory
	 */
	public String pyCharmBookProjectBaseDir()
	{
		return pyCharmBookProjectBaseDir;
	}

	/**
	 * Gets the home GitHub project base directory for the current OS
	 *
	 * @return the home GitHub project base directory
	 */
	public static String getHomeGithubBookProjectBaseDir()
	{
		return getCurrent().homeGithubBookProjectBaseDir;
	}

	/**
	 * Gets the drive backup project base directory for the current OS
	 *
	 * @return the drive backup project base directory
	 */
	public static String getDriveBookProjectBaseDir()
	{
		return getCurrent().driveBookProjectBaseDir;
	}

	/**
	 * Gets the PyCharm projects base directory for the current OS
	 *
	 * @return the PyCharm projects base directory
	 */
	public static String getPyCharmBookProjectBaseDir()
	{
		return getCurrent().pyCharmBookProjectBaseDir;
	}

	/**
	 * Gets the ProjectPaths for the current operating system
	 *
	 * @return the ProjectPaths enum constant for the current OS
	 * @throws IllegalStateException
	 *             if the OS is not supported
	 */
	public static ProjectPaths getCurrent()
	{
		OS currentOS = OS.get();
		return forOS(currentOS);
	}

	/**
	 * Gets the ProjectPaths for a specific operating system
	 *
	 * @param os
	 *            the operating system
	 * @return the ProjectPaths enum constant for the specified OS
	 * @throws IllegalStateException
	 *             if the OS is not supported
	 */
	public static ProjectPaths forOS(OS os)
	{
		switch (os)
		{
			case LINUX :
			case UNIX :
				return detectLinuxDistribution();
			case WINDOWS :
				return WINDOWS;
			case MAC :
				return MAC;
			default :
				throw new IllegalStateException("Unsupported operating system: " + os);
		}
	}

	/**
	 * Detects the specific Linux distribution
	 *
	 * @return MANJARO if Manjaro/Arch is detected, otherwise UBUNTU as default
	 */
	private static ProjectPaths detectLinuxDistribution()
	{
		try
		{
			// Check /etc/os-release file
			java.nio.file.Path osRelease = java.nio.file.Paths.get("/etc/os-release");
			if (java.nio.file.Files.exists(osRelease))
			{
				String content = new String(java.nio.file.Files.readAllBytes(osRelease))
					.toLowerCase();
				if (content.contains("manjaro") || content.contains("arch"))
				{
					return MANJARO;
				}
				if (content.contains("ubuntu"))
				{
					return UBUNTU;
				}
			}
		}
		catch (Exception e)
		{
			// Fall through to default
		}

		// Default to Ubuntu for other Linux distributions
		return UBUNTU;
	}
}
