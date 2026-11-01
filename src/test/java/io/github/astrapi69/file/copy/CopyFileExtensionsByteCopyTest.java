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
package io.github.astrapi69.file.copy;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import io.github.astrapi69.file.copy.api.Copyable;
import io.github.astrapi69.io.file.FileSize;

/**
 * Tests that every copy without charsets in {@link CopyFileExtensions},
 * {@link CopyDirectoryExtensions} and {@link Copyable} keeps the bytes of the source unchanged
 * (#45)
 */
public class CopyFileExtensionsByteCopyTest
{

	/** Bytes that are no valid text in UTF-8, the measured case of #45 */
	private static final byte[] NOT_TEXT = { (byte)0xFF, (byte)0xFE, 0x00, (byte)0x80, 0x41 };

	/** An implementation that uses every default method as it is */
	private static final Copyable COPYABLE = new Copyable()
	{
	};

	@TempDir
	Path tempDir;

	/**
	 * A copy that takes the source file and the temporary directory and returns the copied file
	 */
	@FunctionalInterface
	interface ByteCopy
	{
		File copy(File source, Path tempDir) throws Exception;
	}

	static Stream<Arguments> contents()
	{
		final byte[] everyByteValue = new byte[256];
		for (int i = 0; i < everyByteValue.length; i++)
		{
			everyByteValue[i] = (byte)i;
		}
		final byte[] moreThanOneBlock = new byte[FileSize.DEFAULT_BLOCK_SIZE.getSize() + 1];
		for (int i = 0; i < moreThanOneBlock.length; i++)
		{
			moreThanOneBlock[i] = (byte)(i * 31);
		}
		return Stream.of(Arguments.of("bytes that are no text in UTF-8", NOT_TEXT),
			Arguments.of("an empty file", new byte[0]),
			Arguments.of("every byte value once", everyByteValue),
			Arguments.of("one byte more than a copy block", moreThanOneBlock));
	}

	static Stream<Arguments> copiesWithoutCharsets()
	{
		return Stream.of(Arguments.of("CopyFileExtensions.copyFile(File, File, boolean)",
			(ByteCopy)(source, dir) -> {
				final File copy = dir.resolve("copy.bin").toFile();
				CopyFileExtensions.copyFile(source, copy, false);
				return copy;
			}), Arguments.of("CopyFileExtensions.copyFileToDirectory(File, File)",
				(ByteCopy)(source, dir) -> {
					final File destinationDir = Files.createDirectories(dir.resolve("out"))
						.toFile();
					CopyFileExtensions.copyFileToDirectory(source, destinationDir);
					return new File(destinationDir, source.getName());
				}),
			Arguments.of(
				"CopyFileExtensions.copyFiles(Collection<File>, File, null, null, boolean)",
				(ByteCopy)(source, dir) -> {
					final File destinationDir = dir.resolve("out").toFile();
					CopyFileExtensions.copyFiles(List.of(source), destinationDir, null, null,
						false);
					return new File(destinationDir, source.getName());
				}),
			Arguments.of("CopyFileExtensions.newBackupOf(File, null, null)",
				(ByteCopy)(source, dir) -> CopyFileExtensions.newBackupOf(source, null, null)),
			Arguments.of("CopyDirectoryExtensions.copyDirectory(File, File)",
				(ByteCopy)(source, dir) -> {
					final File destinationDir = dir.resolve("out").toFile();
					CopyDirectoryExtensions.copyDirectory(source.getParentFile(), destinationDir);
					return new File(destinationDir, source.getName());
				}),
			Arguments.of(
				"CopyDirectoryExtensions.copyDirectoryWithFilenameFilter(File, File, null, boolean)",
				(ByteCopy)(source, dir) -> {
					final File destinationDir = dir.resolve("out").toFile();
					CopyDirectoryExtensions.copyDirectoryWithFilenameFilter(source.getParentFile(),
						destinationDir, null, false);
					return new File(destinationDir, source.getName());
				}),
			Arguments.of("Copyable.copyFile(File, File)", (ByteCopy)(source, dir) -> {
				final File copy = dir.resolve("copy.bin").toFile();
				COPYABLE.copyFile(source, copy);
				return copy;
			}), Arguments.of("Copyable.copyDirectory(File, File)", (ByteCopy)(source, dir) -> {
				final File destinationDir = dir.resolve("out").toFile();
				COPYABLE.copyDirectory(source.getParentFile(), destinationDir);
				return new File(destinationDir, source.getName());
			}), Arguments.of("Copyable.copyFiles(Collection<File>, File, null, null, boolean)",
				(ByteCopy)(source, dir) -> {
					final File destinationDir = dir.resolve("out").toFile();
					COPYABLE.copyFiles(List.of(source), destinationDir, null, null, false);
					return new File(destinationDir, source.getName());
				}),
			Arguments.of("Copyable.newBackupOf(File, null, null)",
				(ByteCopy)(source, dir) -> COPYABLE.newBackupOf(source, null, null)));
	}

	private File newSource(final byte[] content) throws IOException
	{
		final Path sourceDir = Files.createDirectories(tempDir.resolve("in"));
		return Files.write(sourceDir.resolve("source.bin"), content).toFile();
	}

	/**
	 * Test method for {@link CopyFileExtensions#copyFile(File, File)}
	 *
	 * @param description
	 *            what the content is
	 * @param content
	 *            the bytes of the source file
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("contents")
	public void copyFile_keepsEveryByte_whenNoCharsetIsGiven(final String description,
		final byte[] content) throws IOException
	{
		final File source = newSource(content);
		final File destination = tempDir.resolve("copy.bin").toFile();

		assertTrue(CopyFileExtensions.copyFile(source, destination));

		assertArrayEquals(content, Files.readAllBytes(destination.toPath()));
	}

	/**
	 * Every method that reaches {@link CopyFileExtensions#copyFile(File, File, boolean)} or passes
	 * no charsets copies the bytes unchanged
	 *
	 * @param entryPoint
	 *            the method under test
	 * @param byteCopy
	 *            the call of the method under test
	 * @throws Exception
	 *             if the copy fails
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("copiesWithoutCharsets")
	public void copyWithoutCharsets_keepsEveryByte(final String entryPoint, final ByteCopy byteCopy)
		throws Exception
	{
		final File source = newSource(NOT_TEXT);

		final File copy = byteCopy.copy(source, tempDir);

		assertArrayEquals(NOT_TEXT, Files.readAllBytes(copy.toPath()));
	}
}
