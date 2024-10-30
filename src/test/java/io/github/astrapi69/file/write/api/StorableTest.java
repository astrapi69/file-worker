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
