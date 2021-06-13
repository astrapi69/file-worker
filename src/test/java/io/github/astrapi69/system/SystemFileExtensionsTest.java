package io.github.astrapi69.system;

import io.github.astrapi69.write.WriteFileExtensions;
import org.testng.annotations.Test;

import java.io.File;

import static org.testng.Assert.*;

/**
 * The unit test class for the class {@link SystemFileExtensions}
 */
public class SystemFileExtensionsTest
{

	/**
	 * Test method for {@link SystemFileExtensions#getJavaHomeDir()}
	 */
	@Test public void testGetJavaHomeDir()
	{
		File javaHomeDir = SystemFileExtensions.getJavaHomeDir();
		assertNotNull(javaHomeDir);
	}

	/**
	 * Test method for {@link SystemFileExtensions#getTempDir()}
	 */
	@Test public void testGetTempDir()
	{
		File tempDir = SystemFileExtensions.getTempDir();
		assertNotNull(tempDir);
	}

	/**
	 * Test method for {@link SystemFileExtensions#getUserHomeDir()}
	 */
	@Test public void testGetUserHomeDir()
	{
		File userHomeDir = SystemFileExtensions.getUserHomeDir();
		assertNotNull(userHomeDir);
	}

	/**
	 * Test method for {@link SystemFileExtensions#getUserDownloadsDir(String)}
	 */
	@Test public void testGetUserDownloadsDirWithString()
	{
		File userDownloadsDir = SystemFileExtensions.getUserDownloadsDir(SystemFileExtensions.DEFAULT_USER_CONFIGURATION_DIRECTORY_NAME);
		assertNotNull(userDownloadsDir);
	}

	/**
	 * Test method for {@link SystemFileExtensions#getUserDownloadsDir()}
	 */
	@Test public void testGetUserDownloadsDir()
	{
		File userDownloadsDir = SystemFileExtensions.getUserDownloadsDir();
		assertNotNull(userDownloadsDir);
	}

	/**
	 * Test method for {@link SystemFileExtensions#getUserConfigurationDir(String)}
	 */
	@Test public void testGetUserConfigurationDirWithString()
	{
		File userConfigurationDir = SystemFileExtensions.getUserConfigurationDir(SystemFileExtensions.DEFAULT_USER_CONFIGURATION_DIRECTORY_NAME);
		assertNotNull(userConfigurationDir);
	}

	/**
	 * Test method for {@link SystemFileExtensions#getUserConfigurationDir()}
	 */
	@Test public void testGetUserConfigurationDir()
	{
		File userConfigurationDir = SystemFileExtensions.getUserConfigurationDir();
		assertNotNull(userConfigurationDir);
	}

	/**
	 * Test method for {@link SystemFileExtensions#getUserWorkingDir()}
	 */
	@Test public void testGetUserWorkingDir()
	{
		File userWorkingDir = SystemFileExtensions.getUserWorkingDir();
		assertNotNull(userWorkingDir);
	}
}
