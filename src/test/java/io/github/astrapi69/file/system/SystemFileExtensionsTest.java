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

import org.apache.commons.lang3.SystemUtils;
import org.junit.jupiter.api.Test;

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
}
