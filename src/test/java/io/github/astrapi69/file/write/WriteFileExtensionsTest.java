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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;

import io.github.astrapi69.checksum.FileChecksumExtensions;
import io.github.astrapi69.file.FileTestCase;
import io.github.astrapi69.file.read.ReadFileExtensions;
import io.github.astrapi69.file.search.PathFinder;
import io.github.astrapi69.io.StreamExtensions;

/**
 * The unit test class for the class {@link WriteFileExtensions}
 */
public class WriteFileExtensionsTest extends FileTestCase
{

	private File tempFile;

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
		// Create a temporary file for testing
		tempFile = File.createTempFile("test-file", ".txt");
	}

	/**
	 * Teardown method will be invoked after every unit test method in this class.
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
	 * Test method for {@link WriteFileExtensions}
	 */
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(WriteFileExtensions.class);
	}

	/**
	 * Test method for {@link WriteFileExtensions#write(InputStream, OutputStream)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testWrite() throws IOException
	{
		long actual;
		long expected;

		final File testFile = new File(PathFinder.getSrcTestResourcesDir(),
			"testReadFileInput.txt");
		File fileout = new File(PathFinder.getSrcTestResourcesDir(),
			"testWriteFileExtensionsWrite.out");
		try (InputStream inputStream = StreamExtensions.getInputStream(testFile, true);
			OutputStream outputStream = StreamExtensions.getOutputStream(fileout, true))
		{
			StreamExtensions.writeInputStreamToOutputStream(inputStream, outputStream);
		}

		actual = FileChecksumExtensions.getCheckSumAdler32(testFile);
		expected = FileChecksumExtensions.getCheckSumAdler32(fileout);
		assertEquals(expected, actual);
		fileout.deleteOnExit();
	}

	/**
	 * Test method for {@link StoreFileExtensions#toFile(File, byte[])}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testWriteByteArrayToFileFileByteArray() throws IOException
	{
		final byte[] expected = { -84, -19, 0, 5, 116, 0, 7, 70, 111, 111, 32, 98, 97, 114 };

		final File destination = new File(this.testDir.getAbsoluteFile(),
			"testStoreByteArrayToFile.txt");
		StoreFileExtensions.toFile(destination, expected);

		final byte[] compare = ReadFileExtensions.readFileToBytearray(destination);

		for (int i = 0; i < compare.length; i++)
		{
			this.actual = compare[i] == expected[i];
			assertTrue(this.actual);
		}
		destination.deleteOnExit();
	}

	/**
	 * Test method for {@link StoreFileExtensions#toFile(File, byte[])}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testWriteByteArrayToFileStringByteArray() throws IOException
	{
		final byte[] expected = { -84, -19, 0, 5, 116, 0, 7, 70, 111, 111, 32, 98, 97, 114 };

		final File destination = new File(this.testDir.getAbsoluteFile(),
			"testStoreByteArrayToFile.txt");
		StoreFileExtensions.toFile(destination, expected);


		final byte[] compare = ReadFileExtensions.readFileToBytearray(destination);

		for (int i = 0; i < compare.length; i++)
		{
			this.actual = compare[i] == expected[i];
			assertTrue(this.actual);
		}
		destination.deleteOnExit();
	}

}
