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

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.meanbean.test.BeanTester;

import io.github.astrapi69.collection.array.ArrayFactory;
import io.github.astrapi69.collection.list.ListFactory;
import io.github.astrapi69.file.FileTestCase;
import io.github.astrapi69.file.create.FileFactory;
import io.github.astrapi69.file.read.ReadFileExtensions;
import io.github.astrapi69.file.search.PathFinder;

/**
 * Unit tests for {@link StoreFileExtensions}.
 *
 * <p>
 * These tests verify writing byte arrays and strings to files using different overloads, with and
 * without explicit character encodings, as well as writing collections of lines. They also cover
 * edge cases such as Unicode content, invalid encodings, empty inputs, large payloads, and missing
 * parent directories. A MeanBean {@link BeanTester} is used to validate basic bean semantics.
 * </p>
 *
 * <p>
 * Temporary filesystem state is managed via JUnit 5's {@link TempDir} where appropriate, and the
 * legacy tests keep using {@link PathFinder#getSrcTestResourcesDir()} to maintain compatibility
 * with existing project conventions.
 * </p>
 */
public class StoreFileExtensionsTest extends FileTestCase
{
	/**
	 * Verifies that writing random bytes with {@link StoreFileExtensions#toFile(File, byte[])}
	 * results in a byte-for-byte identical file.
	 *
	 * @param tmp
	 *            a per-test temporary directory injected by JUnit
	 * @throws IOException
	 *             if the write or read operation fails
	 */
	@Test
	void toFile_bytes_roundtrip_random(@TempDir Path tmp) throws IOException
	{
		File destination = tmp.resolve("random.bin").toFile();

		byte[] expected = new byte[8 * 1024]; // 8 KB
		new Random(42).nextBytes(expected);

		StoreFileExtensions.toFile(destination, expected);
		byte[] actual = io.github.astrapi69.file.read.ReadFileExtensions
			.readFileToBytearray(destination);

		assertArrayEquals(expected, actual);
		assertTrue(destination.length() > 0);
	}

	/**
	 * Verifies that Unicode characters are preserved when writing with an explicit UTF-8 charset.
	 *
	 * @param tmp
	 *            a per-test temporary directory injected by JUnit
	 * @throws IOException
	 *             if the write or read operation fails
	 */
	@Test
	void toFile_string_utf8_unicode(@TempDir Path tmp) throws IOException
	{
		File destination = tmp.resolve("unicode.txt").toFile();

		String expected = "hello äöü ÄÖÜ ß — π 😀";
		StoreFileExtensions.toFile(destination, expected, StandardCharsets.UTF_8);

		String actual = io.github.astrapi69.file.read.ReadFileExtensions.fromFile(destination,
			StandardCharsets.UTF_8);
		assertEquals(expected, actual);
	}

	/**
	 * Ensures that an invalid encoding name triggers fallback behavior: ASCII-only content remains
	 * intact when the platform default charset is used.
	 *
	 * @param tmp
	 *            a per-test temporary directory injected by JUnit
	 * @throws IOException
	 *             if the write or read operation fails
	 */
	@Test
	void toFile_string_invalid_encoding_falls_back_to_default(@TempDir Path tmp) throws IOException
	{
		File destination = tmp.resolve("fallback.txt").toFile();

		// ASCII-only content survives regardless of charset choice.
		String expected = "fallback uses default charset for invalid name";
		StoreFileExtensions.toFile(destination, expected, "definitely-not-a-charset");

		// Read using platform default to match OutputStreamWriter default.
		String actual = io.github.astrapi69.file.read.ReadFileExtensions.fromFile(destination,
			Charset.defaultCharset());
		assertEquals(expected, actual);
	}

	/**
	 * Verifies that writing an empty collection of lines produces an empty file and returns
	 * {@code true}.
	 *
	 * @param tmp
	 *            a per-test temporary directory injected by JUnit
	 * @throws IOException
	 *             if the write or read operation fails
	 */
	@Test
	void toFile_collection_empty_writes_empty_file(@TempDir Path tmp) throws IOException
	{
		File destination = tmp.resolve("empty.txt").toFile();

		List<String> lines = List.of();
		boolean ok = StoreFileExtensions.toFile(destination, lines);

		assertTrue(ok);
		String actual = io.github.astrapi69.file.read.ReadFileExtensions.fromFile(destination,
			StandardCharsets.UTF_8);
		assertEquals("", actual);
	}

	/**
	 * Smoke test for large content: ensures that a sizable UTF-8 string is written and read back
	 * exactly as written.
	 *
	 * @param tmp
	 *            a per-test temporary directory injected by JUnit
	 * @throws IOException
	 *             if the write or read operation fails
	 */
	@Test
	void toFile_large_string_content(@TempDir Path tmp) throws IOException
	{
		File destination = tmp.resolve("large.txt").toFile();

		String unit = "lorem ipsum dolor sit amet\n";
		StringBuilder sb = new StringBuilder(1024 * 256);
		for (int i = 0; i < 10_000; i++)
			sb.append(unit); // ~280 KB

		String expected = sb.toString();
		boolean ok = StoreFileExtensions.toFile(destination, expected, StandardCharsets.UTF_8);
		assertTrue(ok);

		String actual = io.github.astrapi69.file.read.ReadFileExtensions.fromFile(destination,
			StandardCharsets.UTF_8);
		assertEquals(expected, actual);
	}

	/**
	 * Verifies that writing to a file whose parent directory does not exist fails with an
	 * {@link IOException}. Accepts both {@link FileNotFoundException} (classic IO) and
	 * {@link NoSuchFileException} (NIO) depending on the underlying implementation.
	 *
	 * @param tmp
	 *            a per-test temporary directory injected by JUnit
	 */
	@Test
	void toFile_fails_when_parent_missing(@TempDir Path tmp)
	{
		File destination = tmp.resolve("missing-parent").resolve("file.txt").toFile();

		IOException thrown = assertThrows(IOException.class,
			() -> StoreFileExtensions.toFile(destination, "content", StandardCharsets.UTF_8));

		assertTrue(
			thrown instanceof FileNotFoundException || thrown instanceof NoSuchFileException
				|| thrown.getCause() instanceof NoSuchFileException,
			"Expected FileNotFoundException or NoSuchFileException, but was: " + thrown);
	}

	/**
	 * Ensures that writing a single-line collection appends exactly one platform line separator at
	 * the end (matching {@link System#lineSeparator()}), and returns {@code true}.
	 *
	 * @param tmp
	 *            a per-test temporary directory injected by JUnit
	 * @throws IOException
	 *             if the write or read operation fails
	 */
	@Test
	void toFile_collection_single_line_has_trailing_separator(@TempDir Path tmp) throws IOException
	{
		File destination = tmp.resolve("one-line.txt").toFile();

		List<String> lines = new ArrayList<>();
		lines.add("only-one");
		boolean ok = StoreFileExtensions.toFile(destination, lines, StandardCharsets.UTF_8);

		assertTrue(ok);
		String actual = io.github.astrapi69.file.read.ReadFileExtensions.fromFile(destination,
			StandardCharsets.UTF_8);
		assertEquals("only-one" + System.lineSeparator(), actual);
	}

	/**
	 * Lifecycle hook executed before each test. Delegates to {@link FileTestCase#setUp()} to
	 * preserve base class behavior.
	 *
	 * @throws Exception
	 *             if superclass setup fails
	 */
	@Override
	@BeforeEach
	protected void setUp() throws Exception
	{
		super.setUp();
	}

	/**
	 * Lifecycle hook executed after each test. Delegates to {@link FileTestCase#tearDown()} to
	 * preserve base class behavior.
	 *
	 * @throws Exception
	 *             if superclass teardown fails
	 */
	@Override
	@AfterEach
	protected void tearDown() throws Exception
	{
		super.tearDown();
	}

	/**
	 * Validates bean semantics for {@link StoreFileExtensions} using {@link BeanTester}. This
	 * primarily ensures that the class adheres to expected utility/bean conventions.
	 */
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(StoreFileExtensions.class);
	}

	/**
	 * Verifies that writing a fixed byte array produces identical content when read back.
	 *
	 * @throws IOException
	 *             if the write or read operation fails
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
	 * Verifies that writing a simple string with platform default encoding can be read back
	 * unchanged via {@link ReadFileExtensions#fromFile(File)}.
	 *
	 * @throws IOException
	 *             if the write or read operation fails
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
	 * Verifies that writing a string with a named encoding (e.g. {@code "UTF-8"}) round-trips
	 * correctly.
	 *
	 * @throws IOException
	 *             if the write or read operation fails
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
	 * Verifies that writing a string with an explicit {@link Charset} (UTF-8) round-trips
	 * correctly.
	 *
	 * @throws IOException
	 *             if the write or read operation fails
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

	/**
	 * Verifies that writing a collection of lines with a given {@link Charset} creates the expected
	 * line-separated content (ending with a trailing line separator).
	 *
	 * @throws IOException
	 *             if the write or read operation fails
	 */
	@Test
	public void testTestToFileWithCollectionLinesAndCharset() throws IOException
	{
		String actual;
		String expected;
		File destination;

		destination = FileFactory.newFileQuietly(PathFinder.getSrcTestResourcesDir(),
			"testStoreStringToFile.txt");
		String line = "foo bar";
		List<String> lines = ListFactory.newSortedUniqueList(line, "bla", "fasel");
		StoreFileExtensions.toFile(destination, lines, StandardCharsets.UTF_8);
		actual = ReadFileExtensions.fromFile(destination);
		String lineSeperator = System.lineSeparator();
		expected = line + lineSeperator + "bla" + lineSeperator + "fasel" + lineSeperator;
		assertEquals(actual, expected);
		destination.deleteOnExit();
	}

	/**
	 * Verifies that writing a collection of lines with platform default encoding creates the
	 * expected line-separated content (ending with a trailing line separator).
	 *
	 * @throws IOException
	 *             if the write or read operation fails
	 */
	@Test
	public void testTestToFileWithCollectionLines() throws IOException
	{
		String actual;
		String expected;
		File destination;

		destination = FileFactory.newFileQuietly(PathFinder.getSrcTestResourcesDir(),
			"testStoreStringToFile.txt");
		String line = "foo bar";
		List<String> lines = ListFactory.newSortedUniqueList(line, "bla", "fasel");
		StoreFileExtensions.toFile(destination, lines);
		actual = ReadFileExtensions.fromFile(destination);
		String lineSeperator = System.lineSeparator();
		expected = line + lineSeperator + "bla" + lineSeperator + "fasel" + lineSeperator;
		assertEquals(actual, expected);
		destination.deleteOnExit();
	}
}
