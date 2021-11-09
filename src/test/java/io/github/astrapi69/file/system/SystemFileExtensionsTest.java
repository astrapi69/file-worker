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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.io.File;

import org.apache.commons.lang3.SystemUtils;
import org.testng.annotations.Test;

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
			.getUserDownloadsDir(SystemFileExtensions.DEFAULT_USER_CONFIGURATION_DIRECTORY_NAME);
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
		File rootDir = SystemFileExtensions.getRootDir();
		assertNotNull(rootDir);
		if(SystemUtils.IS_OS_WINDOWS) {
			assertEquals(rootDir.getAbsolutePath(), "C:");
		}
	}
}
