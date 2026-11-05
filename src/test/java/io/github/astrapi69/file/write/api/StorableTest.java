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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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
		String expectedContent = String.join(System.lineSeparator(), lines)
			+ System.lineSeparator();

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
		String expectedContent = String.join(System.lineSeparator(), lines)
			+ System.lineSeparator();

		assertEquals(expectedContent, actualContent);
	}

	/**
	 * Test method for {@link Storable#toFile(File, String)}: returns {@code true} and writes the
	 * string encoded with the platform default charset
	 *
	 * @throws IOException
	 *             if an I/O exception occurs
	 */
	@Test
	public void testToFileWithStringReturnsTrueAndUsesDefaultCharset() throws IOException
	{
		String content = "foo bar äöü";
		File destination = new File(tempDir.toFile(), "testStoreStringDefaultCharset.txt");

		storable.toFile(destination, content);

		assertArrayEquals(content.getBytes(Charset.defaultCharset()),
			Files.readAllBytes(destination.toPath()));
	}

	/**
	 * Test method for {@link Storable#toFile(File, Collection)}: returns {@code true} and writes
	 * every line ended with the platform line separator, the last one too, encoded with the
	 * platform default charset
	 *
	 * @throws IOException
	 *             if an I/O exception occurs
	 */
	@Test
	public void testToFileWithCollectionReturnsTrueAndUsesDefaultCharset() throws IOException
	{
		List<String> lines = List.of("foo bar", "äöü");
		File destination = new File(tempDir.toFile(), "testStoreCollectionDefaultCharset.txt");
		String expected = "foo bar" + System.lineSeparator() + "äöü" + System.lineSeparator();

		storable.toFile(destination, lines);

		assertArrayEquals(expected.getBytes(Charset.defaultCharset()),
			Files.readAllBytes(destination.toPath()));
	}

	/**
	 * Test method for {@link Storable#toFile(File, String, Charset)}: returns {@code true} and
	 * writes the string encoded with the given charset
	 *
	 * @param charsetName
	 *            the name of the charset to write with
	 * @throws IOException
	 *             if an I/O exception occurs
	 */
	@ParameterizedTest(name = "charset: {0}")
	@ValueSource(strings = { "UTF-8", "UTF-16", "ISO-8859-1" })
	public void testToFileWithStringAndCharsetReturnsTrueAndEncodes(String charsetName)
		throws IOException
	{
		Charset charset = Charset.forName(charsetName);
		String content = "foo bar äöü";
		File destination = new File(tempDir.toFile(), "testStoreString-" + charsetName + ".txt");

		storable.toFile(destination, content, charset);

		assertArrayEquals(content.getBytes(charset), Files.readAllBytes(destination.toPath()));
	}

	/**
	 * Test method for {@link Storable#toFile(File, Collection, Charset)}: returns {@code true} and
	 * writes every line ended with the platform line separator, the last one too, encoded with the
	 * given charset
	 *
	 * @param charsetName
	 *            the name of the charset to write with
	 * @throws IOException
	 *             if an I/O exception occurs
	 */
	@ParameterizedTest(name = "charset: {0}")
	@ValueSource(strings = { "UTF-8", "UTF-16", "ISO-8859-1" })
	public void testToFileWithCollectionAndCharsetReturnsTrueAndEncodes(String charsetName)
		throws IOException
	{
		Charset charset = Charset.forName(charsetName);
		List<String> lines = List.of("foo bar", "äöü");
		File destination = new File(tempDir.toFile(),
			"testStoreCollection-" + charsetName + ".txt");
		String expected = "foo bar" + System.lineSeparator() + "äöü" + System.lineSeparator();

		storable.toFile(destination, lines, charset);

		assertArrayEquals(expected.getBytes(charset), Files.readAllBytes(destination.toPath()));
	}

	/**
	 * Test method for the string and collection overloads of {@link Storable}: a file whose parent
	 * directory does not exist is signalled with a {@link NoSuchFileException} instead of a
	 * {@code false} return value, and no file is created
	 */
	@Test
	public void testToFileFailsWhenParentMissing()
	{
		File destination = tempDir.resolve("missing-parent").resolve("file.txt").toFile();
		List<String> lines = List.of("foo bar");

		assertThrows(NoSuchFileException.class, () -> storable.toFile(destination, "foo bar"));
		assertThrows(NoSuchFileException.class,
			() -> storable.toFile(destination, "foo bar", StandardCharsets.UTF_8));
		assertThrows(NoSuchFileException.class, () -> storable.toFile(destination, lines));
		assertThrows(NoSuchFileException.class,
			() -> storable.toFile(destination, lines, StandardCharsets.UTF_8));
		assertFalse(destination.exists());
	}
}
