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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.apache.commons.lang3.SystemUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * The unit test class for the class {@link SystemFileExtensions}
 */
public class SystemFileExtensionsTest
{

	/**
	 * Test method for {@link SystemFileExtensions#getJavaHomeDir()}
	 */
	@Test
	public void testGetJavaHomeDir()
	{
		File javaHomeDir = SystemFileExtensions.getJavaHomeDir();
		assertNotNull(javaHomeDir);
	}

	/**
	 * Test method for {@link SystemFileExtensions#getTempDir()}
	 */
	@Test
	public void testGetTempDir()
	{
		File tempDir = SystemFileExtensions.getTempDir();
		assertNotNull(tempDir);
	}

	/**
	 * Test method for {@link SystemFileExtensions#getUserHomeDir()}
	 */
	@Test
	public void testGetUserHomeDir()
	{
		File userHomeDir = SystemFileExtensions.getUserHomeDir();
		assertNotNull(userHomeDir);
	}

	/**
	 * Test method for {@link SystemFileExtensions#getUserDownloadsDir(String)}
	 */
	@Test
	public void testGetUserDownloadsDirWithString()
	{
		File userDownloadsDir = SystemFileExtensions
			.getUserDownloadsDir(SystemFileExtensions.DEFAULT_USER_DOWNLOAD_DIRECTORY_NAME);
		assertNotNull(userDownloadsDir);
	}

	/**
	 * Test method for {@link SystemFileExtensions#getUserTempDir()}
	 */
	@Test
	public void testGetUserTempDir()
	{
		File userHomeDir = SystemFileExtensions.getUserTempDir();
		assertNotNull(userHomeDir);
	}

	/**
	 * Test method for {@link SystemFileExtensions#getUserTempDir(String)}
	 */
	@Test
	public void testGetUserTempDirWithString()
	{
		File userDownloadsDir = SystemFileExtensions
			.getUserTempDir(SystemFileExtensions.DEFAULT_USER_TEMPORARY_DIRECTORY_NAME);
		assertNotNull(userDownloadsDir);
	}

	/**
	 * Test method for {@link SystemFileExtensions#getUserDownloadsDir()}
	 */
	@Test
	public void testGetUserDownloadsDir()
	{
		File userDownloadsDir = SystemFileExtensions.getUserDownloadsDir();
		assertNotNull(userDownloadsDir);
	}

	/**
	 * Test method for {@link SystemFileExtensions#getUserConfigurationDir(String)}
	 */
	@Test
	public void testGetUserConfigurationDirWithString()
	{
		File userConfigurationDir = SystemFileExtensions.getUserConfigurationDir(
			SystemFileExtensions.DEFAULT_USER_CONFIGURATION_DIRECTORY_NAME);
		assertNotNull(userConfigurationDir);
	}

	/**
	 * Test method for {@link SystemFileExtensions#getUserConfigurationDir()}
	 */
	@Test
	public void testGetUserConfigurationDir()
	{
		File userConfigurationDir = SystemFileExtensions.getUserConfigurationDir();
		assertNotNull(userConfigurationDir);
	}

	/**
	 * Test method for {@link SystemFileExtensions#getUserWorkingDir()}
	 */
	@Test
	public void testGetUserWorkingDir()
	{
		File userWorkingDir = SystemFileExtensions.getUserWorkingDir();
		assertNotNull(userWorkingDir);
	}

	/**
	 * Test method for {@link SystemFileExtensions#getRootDir()}
	 */
	@Test
	public void testGetRootDir()
	{
		File actual;
		File expected;
		actual = SystemFileExtensions.getRootDir();
		assertNotNull(actual);
		if (SystemUtils.IS_OS_WINDOWS)
		{
			expected = new File("C:");
			assertEquals(actual, expected);
		}
	}

	/**
	 * Test method for {@link SystemFileExtensions#getWindowsRootDriveDir(char)}
	 */
	@Test
	public void testGetWindowsRootDriveDir()
	{
		Optional<File> actual;
		Optional<File> expected;
		actual = SystemFileExtensions.getWindowsRootDriveDir('C');
		assertNotNull(actual);
		if (SystemUtils.IS_OS_WINDOWS)
		{
			expected = Optional.of(new File("C:"));
			assertEquals(actual, expected);
		}
	}

	/**
	 * Provides each directory getter of {@link SystemFileExtensions} that maps a system property to
	 * a {@link File}, together with the key of that property
	 *
	 * @return the key and the getter for every such directory
	 */
	static Stream<Arguments> propertyDirectoryGetters()
	{
		return Stream.of(
			Arguments.of(SystemPropertiesExtensions.JAVA_HOME_PROPERTY_KEY,
				(Supplier<File>)SystemFileExtensions::getJavaHomeDir),
			Arguments.of(SystemPropertiesExtensions.JAVA_IO_TMPDIR_PROPERTY_KEY,
				(Supplier<File>)SystemFileExtensions::getTempDir),
			Arguments.of(SystemPropertiesExtensions.USER_HOME_PROPERTY_KEY,
				(Supplier<File>)SystemFileExtensions::getUserHomeDir),
			Arguments.of(SystemPropertiesExtensions.USER_DIR_PROPERTY_KEY,
				(Supplier<File>)SystemFileExtensions::getUserWorkingDir));
	}

	/**
	 * Test that every directory getter returns the directory its system property names
	 *
	 * @param key
	 *            the key of the system property
	 * @param getter
	 *            the getter that returns the directory
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("propertyDirectoryGetters")
	void testDirectoryGetterReturnsTheDirectoryOfItsSystemProperty(final String key,
		final Supplier<File> getter)
	{
		assertEquals(new File(System.getProperty(key)), getter.get());
	}

	/**
	 * Provides each user directory getter that takes a directory name, a name to pass and the name
	 * expected below the user home directory: the default one for a null or empty name, the given
	 * one otherwise
	 *
	 * @return the getter, the given name and the expected name
	 */
	static Stream<Arguments> namedUserDirectories()
	{
		Function<String, File> downloads = SystemFileExtensions::getUserDownloadsDir;
		Function<String, File> temp = SystemFileExtensions::getUserTempDir;
		Function<String, File> configuration = SystemFileExtensions::getUserConfigurationDir;
		return Stream.of(
			Arguments.of("downloads", downloads, null,
				SystemFileExtensions.DEFAULT_USER_DOWNLOAD_DIRECTORY_NAME),
			Arguments.of("downloads", downloads, "",
				SystemFileExtensions.DEFAULT_USER_DOWNLOAD_DIRECTORY_NAME),
			Arguments.of("downloads", downloads, "MyDownloads", "MyDownloads"),
			Arguments.of("temp", temp, null,
				SystemFileExtensions.DEFAULT_USER_TEMPORARY_DIRECTORY_NAME),
			Arguments.of("temp", temp, "",
				SystemFileExtensions.DEFAULT_USER_TEMPORARY_DIRECTORY_NAME),
			Arguments.of("temp", temp, "myTemp", "myTemp"),
			Arguments.of("configuration", configuration, null,
				SystemFileExtensions.DEFAULT_USER_CONFIGURATION_DIRECTORY_NAME),
			Arguments.of("configuration", configuration, "",
				SystemFileExtensions.DEFAULT_USER_CONFIGURATION_DIRECTORY_NAME),
			Arguments.of("configuration", configuration, ".myConfig", ".myConfig"));
	}

	/**
	 * Test that a user directory getter resolves the given name below the user home directory and
	 * falls back to its default name for a null or empty name
	 *
	 * @param directory
	 *            the kind of directory, for the test name
	 * @param getter
	 *            the getter that takes the directory name
	 * @param dirname
	 *            the directory name to pass
	 * @param expectedName
	 *            the name expected below the user home directory
	 */
	@ParameterizedTest(name = "{0} directory with name [{2}] is {3}")
	@MethodSource("namedUserDirectories")
	void testUserDirectoryWithName(final String directory, final Function<String, File> getter,
		final String dirname, final String expectedName)
	{
		File expected = new File(
			System.getProperty(SystemPropertiesExtensions.USER_HOME_PROPERTY_KEY), expectedName);
		assertEquals(expected, getter.apply(dirname));
	}

	/**
	 * Provides each user directory getter without arguments together with the default name it must
	 * use
	 *
	 * @return the getter and its default directory name
	 */
	static Stream<Arguments> defaultUserDirectories()
	{
		return Stream.of(
			Arguments.of(SystemFileExtensions.DEFAULT_USER_DOWNLOAD_DIRECTORY_NAME,
				(Supplier<File>)SystemFileExtensions::getUserDownloadsDir),
			Arguments.of(SystemFileExtensions.DEFAULT_USER_TEMPORARY_DIRECTORY_NAME,
				(Supplier<File>)SystemFileExtensions::getUserTempDir),
			Arguments.of(SystemFileExtensions.DEFAULT_USER_CONFIGURATION_DIRECTORY_NAME,
				(Supplier<File>)SystemFileExtensions::getUserConfigurationDir));
	}

	/**
	 * Test that a user directory getter without arguments returns its default directory below the
	 * user home directory
	 *
	 * @param expectedName
	 *            the default directory name
	 * @param getter
	 *            the getter without arguments
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("defaultUserDirectories")
	void testUserDirectoryWithDefaultName(final String expectedName, final Supplier<File> getter)
	{
		File expected = new File(
			System.getProperty(SystemPropertiesExtensions.USER_HOME_PROPERTY_KEY), expectedName);
		assertEquals(expected, getter.get());
	}

	/**
	 * Test that {@link SystemFileExtensions#getRootDir()} returns the file system root outside of
	 * Windows
	 */
	@Test
	@DisabledOnOs(OS.WINDOWS)
	void testGetRootDirIsTheFileSystemRoot()
	{
		assertEquals(new File("/"), SystemFileExtensions.getRootDir());
	}

	/**
	 * Test that {@link SystemFileExtensions#getRootDir()} returns the system drive on Windows
	 */
	@Test
	@EnabledOnOs(OS.WINDOWS)
	void testGetRootDirIsTheSystemDriveOnWindows()
	{
		assertEquals(new File(System.getenv("SystemDrive")), SystemFileExtensions.getRootDir());
	}

	/**
	 * Test that {@link SystemFileExtensions#getWindowsRootDriveDir(char)} finds no drive outside of
	 * Windows, whatever the letter
	 *
	 * @param driveLetter
	 *            the drive letter
	 */
	@ParameterizedTest
	@ValueSource(chars = { 'A', 'C', 'D', 'z' })
	@DisabledOnOs(OS.WINDOWS)
	void testGetWindowsRootDriveDirIsEmptyOutsideOfWindows(final char driveLetter)
	{
		assertEquals(Optional.empty(), SystemFileExtensions.getWindowsRootDriveDir(driveLetter));
	}

	/**
	 * Test that {@link SystemFileExtensions#getWindowsRootDriveDir(char)} returns the drive of the
	 * given letter on Windows
	 *
	 * @param driveLetter
	 *            the drive letter
	 */
	@ParameterizedTest
	@ValueSource(chars = { 'A', 'C', 'D', 'z' })
	@EnabledOnOs(OS.WINDOWS)
	void testGetWindowsRootDriveDirIsTheDriveOfTheLetterOnWindows(final char driveLetter)
	{
		assertEquals(Optional.of(new File(driveLetter + ":")),
			SystemFileExtensions.getWindowsRootDriveDir(driveLetter));
	}
}
