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

import java.util.Properties;

/**
 * The {@code SystemPropertiesExtensions} class provides utility methods to retrieve common system
 * properties.
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public final class SystemPropertiesExtensions
{

	/** Key for the OS architecture system property. */
	public static final String OS_ARCH_PROPERTY_KEY = "os.arch";

	/** Key for the OS name system property. */
	public static final String OS_NAME_PROPERTY_KEY = "os.name";

	/** Key for the OS version system property. */
	public static final String OS_VERSION_PROPERTY_KEY = "os.version";

	/** Key for the user home directory system property. */
	public static final String USER_HOME_PROPERTY_KEY = "user.home";

	/** Key for the user name system property. */
	public static final String USER_NAME_PROPERTY_KEY = "user.name";

	/** Key for the user working directory system property. */
	public static final String USER_DIR_PROPERTY_KEY = "user.dir";

	/** Key for the file separator system property. */
	public static final String FILE_SEPARATOR_PROPERTY_KEY = "file.separator";

	/** Key for the line separator system property. */
	public static final String LINE_SEPARATOR_PROPERTY_KEY = "line.separator";

	/** Key for the path separator system property. */
	public static final String PATH_SEPARATOR_PROPERTY_KEY = "path.separator";

	/** Key for the Java temporary directory system property. */
	public static final String JAVA_IO_TMPDIR_PROPERTY_KEY = "java.io.tmpdir";

	/** Key for the Java class path system property. */
	public static final String JAVA_CLASS_PATH_PROPERTY_KEY = "java.class.path";

	/** Key for the Java vendor system property. */
	public static final String JAVA_VENDOR_PROPERTY_KEY = "java.vendor";

	/** Key for the Java home directory system property. */
	public static final String JAVA_HOME_PROPERTY_KEY = "java.home";

	/** Key for the Java version system property. */
	public static final String JAVA_VERSION_PROPERTY_KEY = "java.version";

	/** Key for the Java vendor URL system property. */
	public static final String JAVA_VENDOR_URL_PROPERTY_KEY = "java.vendor.url";

	/**
	 * Private constructor to prevent instantiation
	 */
	private SystemPropertiesExtensions()
	{
	}

	/**
	 * Retrieves the system file separator character.
	 *
	 * @return the system file separator character
	 */
	public static String getFileSeparator()
	{
		return System.getProperty(FILE_SEPARATOR_PROPERTY_KEY);
	}

	/**
	 * Retrieves the path for the Java temporary directory.
	 *
	 * @return the Java temporary directory path
	 */
	public static String getJavaIoTmpdir()
	{
		return System.getProperty(JAVA_IO_TMPDIR_PROPERTY_KEY);
	}

	/**
	 * Retrieves the path of the installed Java home directory.
	 *
	 * @return the Java home directory path
	 */
	public static String getJavaHome()
	{
		return System.getProperty(JAVA_HOME_PROPERTY_KEY);
	}

	/**
	 * Retrieves the Java class path.
	 *
	 * @return the Java class path
	 */
	public static String getJavaClassPath()
	{
		return System.getProperty(JAVA_CLASS_PATH_PROPERTY_KEY);
	}

	/**
	 * Retrieves the name of the JRE vendor.
	 *
	 * @return the name of the JRE vendor
	 */
	public static String getJavaVendor()
	{
		return System.getProperty(JAVA_VENDOR_PROPERTY_KEY);
	}

	/**
	 * Retrieves the URL of the JRE vendor.
	 *
	 * @return the URL of the JRE vendor
	 */
	public static String getJavaVendorUrl()
	{
		return System.getProperty(JAVA_VENDOR_URL_PROPERTY_KEY);
	}

	/**
	 * Retrieves the JRE version.
	 *
	 * @return the JRE version
	 */
	public static String getJavaVersion()
	{
		return System.getProperty(JAVA_VERSION_PROPERTY_KEY);
	}

	/**
	 * Retrieves the system line separator character.
	 *
	 * @return the system line separator character
	 */
	public static String getLineSeparator()
	{
		return System.getProperty(LINE_SEPARATOR_PROPERTY_KEY);
	}

	/**
	 * Retrieves the operating system architecture.
	 *
	 * @return the operating system architecture
	 */
	public static String getOsArchitecture()
	{
		return System.getProperty(OS_ARCH_PROPERTY_KEY);
	}

	/**
	 * Retrieves the operating system name.
	 *
	 * @return the operating system name
	 */
	public static String getOsName()
	{
		return System.getProperty(OS_NAME_PROPERTY_KEY);
	}

	/**
	 * Retrieves the operating system version.
	 *
	 * @return the operating system version
	 */
	public static String getOsVersion()
	{
		return System.getProperty(OS_VERSION_PROPERTY_KEY);
	}

	/**
	 * Retrieves the system path separator character used in java.class.path.
	 *
	 * @return the system path separator character used in java.class.path
	 */
	public static String getPathSeparator()
	{
		return System.getProperty(PATH_SEPARATOR_PROPERTY_KEY);
	}

	/**
	 * Retrieves the user home directory.
	 *
	 * @return the user home directory
	 */
	public static String getUserHome()
	{
		return System.getProperty(USER_HOME_PROPERTY_KEY);
	}

	/**
	 * Retrieves the user name.
	 *
	 * @return the user name
	 */
	public static String getUserName()
	{
		return System.getProperty(USER_NAME_PROPERTY_KEY);
	}

	/**
	 * Retrieves the user working directory.
	 *
	 * @return the user working directory
	 */
	public static String getUserWorkingDirectory()
	{
		return System.getProperty(USER_DIR_PROPERTY_KEY);
	}

	/**
	 * Sets each entry from the specified {@link Properties} as a system property
	 *
	 * @param properties
	 *            the {@link Properties} to set as system properties
	 */
	public static void setSystemProperties(final Properties properties)
	{
		properties.forEach((key, value) -> System.setProperty(key.toString(), value.toString()));
	}
}
