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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Properties;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * The unit test class for the class {@link SystemPropertiesExtensions}
 */
public class SystemPropertiesExtensionsTest
{

	/**
	 * Teardown method will be invoked after every unit test method in this class
	 */
	@AfterEach
	void tearDown()
	{
		// Remove test properties after each test to avoid side effects
		System.clearProperty("test.key1");
		System.clearProperty("test.key2");
	}

	/**
	 * Test method for {@link SystemPropertiesExtensions#setSystemProperties(Properties)}
	 */
	@Test
	void testSetSystemProperties()
	{
		// Arrange
		Properties properties = new Properties();
		properties.setProperty("test.key1", "value1");
		properties.setProperty("test.key2", "value2");

		// Act
		SystemPropertiesExtensions.setSystemProperties(properties);

		// Assert
		assertEquals("value1", System.getProperty("test.key1"));
		assertEquals("value2", System.getProperty("test.key2"));
	}

	/**
	 * Test method for {@link SystemPropertiesExtensions#setSystemProperties(Properties)}
	 */
	@Test
	void testSetEmptySystemProperties()
	{
		// Arrange
		Properties properties = new Properties();

		// Act
		SystemPropertiesExtensions.setSystemProperties(properties);

		// Assert
		assertNull(System.getProperty("test.key1"));
		assertNull(System.getProperty("test.key2"));
	}

	/**
	 * Provides each getter of {@link SystemPropertiesExtensions} together with the key of the
	 * system property it reads
	 *
	 * @return the key and the getter for every system property
	 */
	static Stream<Arguments> propertyGetters()
	{
		return Stream.of(
			Arguments.of(SystemPropertiesExtensions.FILE_SEPARATOR_PROPERTY_KEY,
				(Supplier<String>)SystemPropertiesExtensions::getFileSeparator),
			Arguments.of(SystemPropertiesExtensions.JAVA_IO_TMPDIR_PROPERTY_KEY,
				(Supplier<String>)SystemPropertiesExtensions::getJavaIoTmpdir),
			Arguments.of(SystemPropertiesExtensions.JAVA_HOME_PROPERTY_KEY,
				(Supplier<String>)SystemPropertiesExtensions::getJavaHome),
			Arguments.of(SystemPropertiesExtensions.JAVA_CLASS_PATH_PROPERTY_KEY,
				(Supplier<String>)SystemPropertiesExtensions::getJavaClassPath),
			Arguments.of(SystemPropertiesExtensions.JAVA_VENDOR_PROPERTY_KEY,
				(Supplier<String>)SystemPropertiesExtensions::getJavaVendor),
			Arguments.of(SystemPropertiesExtensions.JAVA_VENDOR_URL_PROPERTY_KEY,
				(Supplier<String>)SystemPropertiesExtensions::getJavaVendorUrl),
			Arguments.of(SystemPropertiesExtensions.JAVA_VERSION_PROPERTY_KEY,
				(Supplier<String>)SystemPropertiesExtensions::getJavaVersion),
			Arguments.of(SystemPropertiesExtensions.LINE_SEPARATOR_PROPERTY_KEY,
				(Supplier<String>)SystemPropertiesExtensions::getLineSeparator),
			Arguments.of(SystemPropertiesExtensions.OS_ARCH_PROPERTY_KEY,
				(Supplier<String>)SystemPropertiesExtensions::getOsArchitecture),
			Arguments.of(SystemPropertiesExtensions.OS_NAME_PROPERTY_KEY,
				(Supplier<String>)SystemPropertiesExtensions::getOsName),
			Arguments.of(SystemPropertiesExtensions.OS_VERSION_PROPERTY_KEY,
				(Supplier<String>)SystemPropertiesExtensions::getOsVersion),
			Arguments.of(SystemPropertiesExtensions.PATH_SEPARATOR_PROPERTY_KEY,
				(Supplier<String>)SystemPropertiesExtensions::getPathSeparator),
			Arguments.of(SystemPropertiesExtensions.USER_HOME_PROPERTY_KEY,
				(Supplier<String>)SystemPropertiesExtensions::getUserHome),
			Arguments.of(SystemPropertiesExtensions.USER_NAME_PROPERTY_KEY,
				(Supplier<String>)SystemPropertiesExtensions::getUserName),
			Arguments.of(SystemPropertiesExtensions.USER_DIR_PROPERTY_KEY,
				(Supplier<String>)SystemPropertiesExtensions::getUserWorkingDirectory));
	}

	/**
	 * Test that every getter returns the current, non empty value of the system property its key
	 * names
	 *
	 * @param key
	 *            the key of the system property
	 * @param getter
	 *            the getter that reads it
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("propertyGetters")
	void testGetterReturnsValueOfItsSystemProperty(final String key, final Supplier<String> getter)
	{
		String actual = getter.get();
		assertNotNull(actual);
		assertFalse(actual.isEmpty());
		assertEquals(System.getProperty(key), actual);
	}

	/**
	 * Test that every getter follows a change of its system property, so it reads the property
	 * named by its key and nothing cached or fixed
	 *
	 * @param key
	 *            the key of the system property
	 * @param getter
	 *            the getter that reads it
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("propertyGetters")
	void testGetterFollowsAChangedSystemProperty(final String key, final Supplier<String> getter)
	{
		String original = System.getProperty(key);
		String marker = "changed-" + key;
		try
		{
			System.setProperty(key, marker);
			assertEquals(marker, getter.get());
		}
		finally
		{
			System.setProperty(key, original);
		}
		assertEquals(original, getter.get());
	}
}
