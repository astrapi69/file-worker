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
package io.github.astrapi69.file.write.api;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import io.github.astrapi69.collection.array.ArrayFactory;
import io.github.astrapi69.collection.list.ListFactory;
import io.github.astrapi69.file.read.ReadFileExtensions;

/**
 * The unit test class for the {@link Storable} interface implementation
 */
public class StorableTest
{

	// Anonymous implementation of Storable interface for testing
	private final Storable storable = new Storable()
	{
	};

	@TempDir
	Path tempDir;

	/**
	 * Test method for {@link Storable#toFile(File, byte[])}
	 *
	 * @throws IOException
	 *             if an I/O exception occurs
	 */
	@Test
	public void testToFileWithByteArray() throws IOException
	{
		byte[] expectedData = ArrayFactory.newByteArray(-84, -19, 0, 5, 116, 0, 7, 70, 111, 111, 32,
			98, 97, 114);
		File destination = new File(tempDir.toFile(), "testStoreByteArrayToFile.txt");

		storable.toFile(destination, expectedData);
		byte[] actualData = ReadFileExtensions.readFileToBytearray(destination);

		assertArrayEquals(expectedData, actualData);
	}

	/**
	 * Test method for {@link Storable#toFile(File, String)}
	 *
	 * @throws IOException
	 *             if an I/O exception occurs
	 */
	@Test
	public void testToFileWithString() throws IOException
	{
		String expectedContent = "foo bar";
		File destination = new File(tempDir.toFile(), "testStoreStringToFile.txt");

		storable.toFile(destination, expectedContent);
		String actualContent = ReadFileExtensions.fromFile(destination);

		assertEquals(expectedContent, actualContent);
	}

	/**
	 * Test method for {@link Storable#toFile(File, String, Charset)}
	 *
	 * @throws IOException
	 *             if an I/O exception occurs
	 */
	@Test
	public void testToFileWithStringAndCharset() throws IOException
	{
		String expectedContent = "foo bar";
		File destination = new File(tempDir.toFile(), "testStoreStringWithCharset.txt");

		storable.toFile(destination, expectedContent, StandardCharsets.UTF_8);
		String actualContent = ReadFileExtensions.fromFile(destination);

		assertEquals(expectedContent, actualContent);
	}

	/**
	 * Test method for {@link Storable#toFile(File, Collection, Charset)}
	 *
	 * @throws IOException
	 *             if an I/O exception occurs
	 */
	@Test
	public void testToFileWithCollectionAndCharset() throws IOException
	{
		List<String> lines = ListFactory.newSortedUniqueList("foo bar", "bla", "fasel");
		File destination = new File(tempDir.toFile(), "testStoreCollectionWithCharset.txt");

		storable.toFile(destination, lines, StandardCharsets.UTF_8);
		String actualContent = ReadFileExtensions.fromFile(destination);
		String expectedContent = String.join(System.lineSeparator(), lines);

		assertEquals(expectedContent, actualContent);
	}

	/**
	 * Test method for {@link Storable#toFile(File, Collection)}
	 *
	 * @throws IOException
	 *             if an I/O exception occurs
	 */
	@Test
	public void testToFileWithCollection() throws IOException
	{
		List<String> lines = ListFactory.newSortedUniqueList("foo bar", "bla", "fasel");
		File destination = new File(tempDir.toFile(), "testStoreCollection.txt");

		storable.toFile(destination, lines);
		String actualContent = ReadFileExtensions.fromFile(destination);
		String expectedContent = String.join(System.lineSeparator(), lines);

		assertEquals(expectedContent, actualContent);
	}
}
