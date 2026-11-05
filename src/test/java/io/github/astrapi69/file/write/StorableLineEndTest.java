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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnmappableCharacterException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import io.github.astrapi69.file.write.api.Storable;

/**
 * Tests that {@link StoreFileExtensions} and its interface form {@link Storable} write the same
 * file for the same lines, every line ended with the line separator, the last one too, and that a
 * write that fails throws an {@link IOException} instead of returning {@code true} (#107)
 */
class StorableLineEndTest
{
	/** The line separator spelled out, as {@link #content(File)} shows it */
	private static final String EOL = System.lineSeparator().replace("\r", "\\r").replace("\n",
		"\\n");

	/** Linux: every write to this device fails with "No space left on device" */
	private static final File FULL = new File("/dev/full");

	private static final List<String> LINES = List.of("a", "b");

	private final Storable storable = new Storable()
	{
	};

	@TempDir
	Path tempDir;

	/** The content of the file with its line ends spelled out, so that a failure shows them */
	private static String content(File file) throws IOException
	{
		return Files.readString(file.toPath()).replace("\r", "\\r").replace("\n", "\\n");
	}

	/**
	 * A write of one of the overloads to the given file
	 */
	@FunctionalInterface
	interface Write
	{
		void to(File file) throws IOException;
	}

	/**
	 * Every overload of {@link StoreFileExtensions} and {@link Storable} that returns a
	 * {@code boolean}, each writing {@link #LINES}
	 *
	 * @return the name of the overload and the write
	 */
	static Stream<Arguments> writes()
	{
		Storable storable = new Storable()
		{
		};
		String text = "a" + System.lineSeparator() + "b" + System.lineSeparator();
		return Stream.of(
			Arguments.of("StoreFileExtensions.toFile(File, String)",
				(Write)file -> StoreFileExtensions.toFile(file, text)),
			Arguments.of("StoreFileExtensions.toFile(File, String, String)",
				(Write)file -> StoreFileExtensions.toFile(file, text, "UTF-8")),
			Arguments.of("StoreFileExtensions.toFile(File, String, Charset)",
				(Write)file -> StoreFileExtensions.toFile(file, text, StandardCharsets.UTF_8)),
			Arguments.of("StoreFileExtensions.toFile(File, Collection)",
				(Write)file -> StoreFileExtensions.toFile(file, LINES)),
			Arguments.of("StoreFileExtensions.toFile(File, Collection, String)",
				(Write)file -> StoreFileExtensions.toFile(file, LINES, "UTF-8")),
			Arguments.of("StoreFileExtensions.toFile(File, Collection, Charset)",
				(Write)file -> StoreFileExtensions.toFile(file, LINES, StandardCharsets.UTF_8)),
			Arguments.of("Storable.toFile(File, String)",
				(Write)file -> storable.toFile(file, text)),
			Arguments.of("Storable.toFile(File, String, Charset)",
				(Write)file -> storable.toFile(file, text, StandardCharsets.UTF_8)),
			Arguments.of("Storable.toFile(File, Collection)",
				(Write)file -> storable.toFile(file, LINES)),
			Arguments.of("Storable.toFile(File, Collection, Charset)",
				(Write)file -> storable.toFile(file, LINES, StandardCharsets.UTF_8)));
	}

	@Test
	void collection() throws IOException
	{
		File byClass = tempDir.resolve("class.txt").toFile();
		File byInterface = tempDir.resolve("interface.txt").toFile();

		StoreFileExtensions.toFile(byClass, LINES);
		storable.toFile(byInterface, LINES);

		assertEquals("a" + EOL + "b" + EOL, content(byClass));
		assertEquals(content(byClass), content(byInterface));
	}

	@Test
	void collectionAndCharset() throws IOException
	{
		File byClass = tempDir.resolve("class.txt").toFile();
		File byInterface = tempDir.resolve("interface.txt").toFile();

		StoreFileExtensions.toFile(byClass, LINES, StandardCharsets.UTF_8);
		storable.toFile(byInterface, LINES, StandardCharsets.UTF_8);

		assertEquals("a" + EOL + "b" + EOL, content(byClass));
		assertEquals(content(byClass), content(byInterface));
	}

	/** An empty collection writes an empty file, from both, without a line end */
	@Test
	void anEmptyCollection() throws IOException
	{
		File byClass = tempDir.resolve("class.txt").toFile();
		File byInterface = tempDir.resolve("interface.txt").toFile();
		Collection<String> none = List.of();

		StoreFileExtensions.toFile(byClass, none, StandardCharsets.UTF_8);
		storable.toFile(byInterface, none, StandardCharsets.UTF_8);

		assertEquals("", content(byClass));
		assertEquals("", content(byInterface));
	}

	/** Linux: every write to /dev/full fails with "No space left on device" */
	@Test
	@EnabledOnOs(OS.LINUX)
	void aWriteThatFails()
	{
		assumeTrue(FULL.exists(), "/dev/full does not exist");

		assertThrows(IOException.class, () -> storable.toFile(FULL, LINES));
		assertThrows(IOException.class, () -> StoreFileExtensions.toFile(FULL, LINES),
			"StoreFileExtensions.toFile returned instead of throwing for a write that failed");
	}

	/**
	 * Each overload throws an {@link IOException} for a write that fails
	 *
	 * @param name
	 *            the name of the overload
	 * @param write
	 *            the write
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("writes")
	@EnabledOnOs(OS.LINUX)
	void eachOverloadThrowsForAWriteThatFails(String name, Write write)
	{
		assumeTrue(FULL.exists(), "/dev/full does not exist");

		assertThrows(IOException.class, () -> write.to(FULL),
			name + " returned instead of throwing for a write that failed");
	}

	/**
	 * Each overload returns {@code true} for a write that succeeds and writes every line with its
	 * line end
	 *
	 * @param name
	 *            the name of the overload
	 * @param write
	 *            the write
	 * @throws IOException
	 *             if an I/O exception occurs
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("writes")
	void eachOverloadReturnsTrueForAWriteThatSucceeds(String name, Write write) throws IOException
	{
		File file = tempDir.resolve("written.txt").toFile();

		write.to(file);
		assertEquals("a" + EOL + "b" + EOL, content(file), name);
	}

	/** today's behaviour, kept: the class writes '?', the interface throws */
	@Test
	void aCharacterTheCharsetCannotEncode() throws IOException
	{
		File byClass = tempDir.resolve("class.txt").toFile();
		File byInterface = tempDir.resolve("interface.txt").toFile();

		StoreFileExtensions.toFile(byClass, List.of("ä"), StandardCharsets.US_ASCII);
		assertEquals("?" + EOL, content(byClass));
		assertThrows(UnmappableCharacterException.class,
			() -> storable.toFile(byInterface, List.of("ä"), StandardCharsets.US_ASCII));
	}
}
