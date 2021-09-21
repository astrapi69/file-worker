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

/**
 * The class {@link SystemPropertiesExtensions} provide methods for get the common system properties
 */
public class SystemPropertiesExtensions
{

	public static final String OS_ARCH_PROPERTY_KEY = "os.arch";
	public static final String OS_NAME_PROPERTY_KEY = "os.name";
	public static final String OS_VERSION_PROPERTY_KEY = "os.version";

	public static final String USER_HOME_PROPERTY_KEY = "user.home";
	public static final String USER_NAME_PROPERTY_KEY = "user.name";
	public static final String USER_DIR_PROPERTY_KEY = "user.dir";

	public static final String FILE_SEPARATOR_PROPERTY_KEY = "file.separator";
	public static final String LINE_SEPARATOR_PROPERTY_KEY = "line.separator";
	public static final String PATH_SEPARATOR_PROPERTY_KEY = "path.separator";

	public static final String JAVA_IO_TMPDIR_PROPERTY_KEY = "java.io.tmpdir";
	public static final String JAVA_CLASS_PATH_PROPERTY_KEY = "java.class.path";
	public static final String JAVA_VENDOR_PROPERTY_KEY = "java.vendor";
	public static final String JAVA_HOME_PROPERTY_KEY = "java.home";
	public static final String JAVA_VERSION_PROPERTY_KEY = "java.version";
	public static final String JAVA_VENDOR_URL_PROPERTY_KEY = "java.vendor.url";

	/**
	 * Gets the system file separator character. In other words the character that separates
	 * components of a file path. This is "/" on UNIX and "\" on Windows.
	 *
	 * @return the system file separator character
	 */
	public static String getFileSeparator()
	{
		String fileSeparator = System.getProperty(FILE_SEPARATOR_PROPERTY_KEY);
		return fileSeparator;
	}

	/**
	 * Gets the path for the temporary directory for java
	 *
	 * @return the java class path
	 */
	public static String getJavaIoTmpdir()
	{
		String fileSeparator = System.getProperty(JAVA_IO_TMPDIR_PROPERTY_KEY);
		return fileSeparator;
	}

	/**
	 * Gets the path of the installed java home
	 *
	 * @return the java home path
	 */
	public static String getJavaHome()
	{
		String userHomePath = System.getProperty(JAVA_HOME_PROPERTY_KEY);
		return userHomePath;
	}

	/**
	 * Gets the Path used to find directories and JAR archives containing class files. Elements of
	 * the class path are separated by a platform-specific character specified in the path.separator
	 * property.
	 *
	 * @return the java class path
	 */
	public static String getJavaClassPath()
	{
		String fileSeparator = System.getProperty(JAVA_CLASS_PATH_PROPERTY_KEY);
		return fileSeparator;
	}

	/**
	 * Gets the name of JRE vendor name
	 *
	 * @return the name of JRE vendor name
	 */
	public static String getJavaVendor()
	{
		String javaVendor = System.getProperty(JAVA_VENDOR_PROPERTY_KEY);
		return javaVendor;
	}

	/**
	 * Gets the name of JRE vendor url
	 *
	 * @return the name of JRE vendor url
	 */
	public static String getJavaVendorUrl()
	{
		String fileSeparator = System.getProperty(JAVA_VENDOR_URL_PROPERTY_KEY);
		return fileSeparator;
	}

	/**
	 * Gets the JRE version
	 *
	 * @return the JRE version
	 */
	public static String getJavaVersion()
	{
		String javaVendor = System.getProperty(JAVA_VERSION_PROPERTY_KEY);
		return javaVendor;
	}

	/**
	 * Gets the system line separator character. In other words the sequence used by operating
	 * system to separate lines in text files
	 *
	 * @return the system line separator character
	 */
	public static String getLineSeparator()
	{
		String fileSeparator = System.getProperty(LINE_SEPARATOR_PROPERTY_KEY);
		return fileSeparator;
	}

	/**
	 * Gets the operating system architecture
	 *
	 * @return the operating system architecture
	 */
	public static String getOsArchitecture()
	{
		String fileSeparator = System.getProperty(OS_ARCH_PROPERTY_KEY);
		return fileSeparator;
	}

	/**
	 * Gets the operating system name
	 *
	 * @return the operating system name
	 */
	public static String getOsName()
	{
		String fileSeparator = System.getProperty(OS_NAME_PROPERTY_KEY);
		return fileSeparator;
	}

	/**
	 * Gets the operating system version
	 *
	 * @return the operating system version
	 */
	public static String getOsVersion()
	{
		String fileSeparator = System.getProperty(OS_VERSION_PROPERTY_KEY);
		return fileSeparator;
	}

	/**
	 * Gets the system path separator character used in java.class.path
	 *
	 * @return the system path separator character used in java.class.path
	 */
	public static String getPathSeparator()
	{
		String fileSeparator = System.getProperty(PATH_SEPARATOR_PROPERTY_KEY);
		return fileSeparator;
	}

	/**
	 * Gets the user home directory
	 *
	 * @return the user home directory
	 */
	public static String getUserHome()
	{
		String userHome = System.getProperty(USER_HOME_PROPERTY_KEY);
		return userHome;
	}

	/**
	 * Gets the user name
	 *
	 * @return the user name
	 */
	public static String getUserName()
	{
		String userHome = System.getProperty(USER_NAME_PROPERTY_KEY);
		return userHome;
	}

	/**
	 * Gets the user working directory
	 *
	 * @return the user working directory
	 */
	public static String getUserWorkingDirectory()
	{
		String userHome = System.getProperty(USER_DIR_PROPERTY_KEY);
		return userHome;
	}

}
