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

import static org.testng.Assert.assertNotNull;

import org.testng.annotations.Test;

/**
 * The unit test class for the class {@link SystemPropertiesExtensions}
 */
public class SystemPropertiesExtensionsTest
{

	/**
	 * Test method for {@link SystemPropertiesExtensions#getFileSeparator()}
	 */
	@Test
	public void testGetFileSeparator()
	{
		String fileSeparator = SystemPropertiesExtensions.getFileSeparator();
		assertNotNull(fileSeparator);
	}

	/**
	 * Test method for {@link SystemPropertiesExtensions#getJavaClassPath()}
	 */
	@Test
	public void testGetJavaClassPath()
	{
		String javaClassPath = SystemPropertiesExtensions.getJavaClassPath();
		assertNotNull(javaClassPath);
	}

	/**
	 * Test method for {@link SystemPropertiesExtensions#getJavaIoTmpdir()}
	 */
	@Test
	public void testGetJavaIoTmpdir()
	{
		String javaIoTmpdir = SystemPropertiesExtensions.getJavaIoTmpdir();
		assertNotNull(javaIoTmpdir);
	}

	/**
	 * Test method for {@link SystemPropertiesExtensions#getJavaHome()}
	 */
	@Test
	public void testGetJavaHome()
	{
		String javaHome = SystemPropertiesExtensions.getJavaHome();
		assertNotNull(javaHome);
	}

	/**
	 * Test method for {@link SystemPropertiesExtensions#getJavaVendor()}
	 */
	@Test
	public void testGetJavaVendor()
	{
		String javaVendor = SystemPropertiesExtensions.getJavaVendor();
		assertNotNull(javaVendor);
	}

	/**
	 * Test method for {@link SystemPropertiesExtensions#getJavaVendorUrl()}
	 */
	@Test
	public void testGetJavaVendorUrl()
	{
		String javaVendorUrl = SystemPropertiesExtensions.getJavaVendorUrl();
		assertNotNull(javaVendorUrl);
	}

	/**
	 * Test method for {@link SystemPropertiesExtensions#getJavaVersion()}
	 */
	@Test
	public void testGetJavaVersion()
	{
		String javaVersion = SystemPropertiesExtensions.getJavaVersion();
		assertNotNull(javaVersion);
	}

	/**
	 * Test method for {@link SystemPropertiesExtensions#getLineSeparator()}
	 */
	@Test
	public void testGetLineSeparator()
	{
		String lineSeparator = SystemPropertiesExtensions.getLineSeparator();
		assertNotNull(lineSeparator);
	}

	/**
	 * Test method for {@link SystemPropertiesExtensions#getOsArchitecture()}
	 */
	@Test
	public void testGetOsArchitecture()
	{
		String osArchitecture = SystemPropertiesExtensions.getOsArchitecture();
		assertNotNull(osArchitecture);
	}

	/**
	 * Test method for {@link SystemPropertiesExtensions#getOsName()}
	 */
	@Test
	public void testGetOsName()
	{
		String osName = SystemPropertiesExtensions.getOsName();
		assertNotNull(osName);
	}

	/**
	 * Test method for {@link SystemPropertiesExtensions#getOsVersion()}
	 */
	@Test
	public void testGetOsVersion()
	{
		String osVersion = SystemPropertiesExtensions.getOsVersion();
		assertNotNull(osVersion);
	}

	/**
	 * Test method for {@link SystemPropertiesExtensions#getPathSeparator()}
	 */
	@Test
	public void testGetPathSeparator()
	{
		String pathSeparator = SystemPropertiesExtensions.getPathSeparator();
		assertNotNull(pathSeparator);
	}

	/**
	 * Test method for {@link SystemPropertiesExtensions#getUserHome()}
	 */
	@Test
	public void testGetUserHome()
	{
		String userHome = SystemPropertiesExtensions.getUserHome();
		assertNotNull(userHome);
	}

	/**
	 * Test method for {@link SystemPropertiesExtensions#getUserName()}
	 */
	@Test
	public void testGetUserName()
	{
		String userName = SystemPropertiesExtensions.getUserName();
		assertNotNull(userName);
	}

	/**
	 * Test method for {@link SystemPropertiesExtensions#getUserWorkingDirectory()}
	 */
	@Test
	public void testGetUserWorkingDirectory()
	{
		String userWorkingDirectory = SystemPropertiesExtensions.getUserWorkingDirectory();
		assertNotNull(userWorkingDirectory);
	}
}
