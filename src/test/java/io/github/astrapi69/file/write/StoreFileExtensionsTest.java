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
package io.github.astrapi69.file.write;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;

import io.github.astrapi69.collection.array.ArrayFactory;
import io.github.astrapi69.file.FileTestCase;
import io.github.astrapi69.file.create.FileFactory;
import io.github.astrapi69.file.read.ReadFileExtensions;
import io.github.astrapi69.file.search.PathFinder;

/**
 * The unit test class for the class {@link StoreFileExtensions}
 */
public class StoreFileExtensionsTest extends FileTestCase
{

	/**
	 * Sets up method will be invoked before every unit test method in this class.
	 *
	 * @throws Exception
	 *             is thrown if an exception occurs
	 */
	@Override
	@BeforeEach
	protected void setUp() throws Exception
	{
		super.setUp();
	}

	/**
	 * Tear down method will be invoked after every unit test method in this class.
	 *
	 * @throws Exception
	 *             is thrown if an exception occurs
	 */
	@Override
	@AfterEach
	protected void tearDown() throws Exception
	{
		super.tearDown();
	}

	/**
	 * Test method for {@link StoreFileExtensions}
	 */
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(StoreFileExtensions.class);
	}

	/**
	 * Test method for {@link StoreFileExtensions#toFile(File, byte[])}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	@Test
	public void testToFile() throws IOException
	{
		byte[] expected;
		File destination;
		byte[] compare;

		expected = ArrayFactory.newByteArray(-84, -19, 0, 5, 116, 0, 7, 70, 111, 111, 32, 98, 97,
			114);

		destination = FileFactory.newFileQuietly(PathFinder.getSrcTestResourcesDir(),
			"testStoreByteArrayToFile.txt");

		StoreFileExtensions.toFile(destination, expected);

		compare = ReadFileExtensions.readFileToBytearray(destination);

		for (int i = 0; i < compare.length; i++)
		{
			assertEquals(compare[i], expected[i]);
		}
		destination.deleteOnExit();
	}

	/**
	 * Test method for {@link StoreFileExtensions#toFile(File, String)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	@Test
	public void testTestToFile() throws IOException
	{
		String actual;
		String expected;
		File destination;

		destination = FileFactory.newFileQuietly(PathFinder.getSrcTestResourcesDir(),
			"testStoreStringToFile.txt");
		expected = "foo bar";
		StoreFileExtensions.toFile(destination, expected);

		actual = ReadFileExtensions.fromFile(destination);

		assertEquals(actual, expected);
		destination.deleteOnExit();
	}

	/**
	 * Test method for {@link StoreFileExtensions#toFile(File, String, String)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	@Test
	public void testTestToFileWithEncoding() throws IOException
	{
		String actual;
		String expected;
		File destination;

		destination = FileFactory.newFileQuietly(PathFinder.getSrcTestResourcesDir(),
			"testStoreStringToFile.txt");
		expected = "foo bar";
		StoreFileExtensions.toFile(destination, expected, "UTF-8");

		actual = ReadFileExtensions.fromFile(destination);

		assertEquals(actual, expected);
		destination.deleteOnExit();
	}

	/**
	 * Test method for {@link StoreFileExtensions#toFile(File, String, String)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	@Test
	public void testTestToFile2() throws IOException
	{
		String actual;
		String expected;
		File destination;

		destination = FileFactory.newFileQuietly(PathFinder.getSrcTestResourcesDir(),
			"testStoreStringToFile.txt");
		expected = "foo bar";
		StoreFileExtensions.toFile(destination, expected, StandardCharsets.UTF_8);
		actual = ReadFileExtensions.fromFile(destination);
		assertEquals(actual, expected);
		destination.deleteOnExit();
	}
}