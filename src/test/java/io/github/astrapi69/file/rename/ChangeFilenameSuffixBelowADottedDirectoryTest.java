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
package io.github.astrapi69.file.rename;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import io.github.astrapi69.file.rename.api.Renameable;

/**
 * A file below a directory whose name holds a dot keeps its directory when its suffix changes
 * (#37): with silly-io 3.6, {@code getFilenamePrefix} looked for the extension's dot in the whole
 * path, so {@code ~/.config/app/data} with the suffix {@code .bak} became {@code ~/.bak}
 * (astrapi69/silly-io#9)
 */
class ChangeFilenameSuffixBelowADottedDirectoryTest
{

	@FunctionalInterface
	interface ChangeFilenameSuffix
	{
		boolean change(File file, String newSuffix) throws Exception;
	}

	@TempDir
	Path tempDir;

	static Stream<Arguments> filesAndCalls()
	{
		ChangeFilenameSuffix renameFileExtensions = RenameFileExtensions::changeFilenameSuffix;
		ChangeFilenameSuffix withDelete = (file, newSuffix) -> RenameFileExtensions
			.changeFilenameSuffix(file, newSuffix, true);
		ChangeFilenameSuffix renameable = (file, newSuffix) -> new Renameable()
		{
		}.changeFilenameSuffix(file, newSuffix, true);
		return Stream.of(".cache/x/data", ".config/app/data.txt", "a.b/c/notes", "x.y/z.w/name.")
			.flatMap(relative -> Stream.of(
				Arguments.argumentSet(relative + ", changeFilenameSuffix(File, String)", relative,
					renameFileExtensions),
				Arguments.argumentSet(relative + ", changeFilenameSuffix(File, String, true)",
					relative, withDelete),
				Arguments.argumentSet(relative + ", Renameable", relative, renameable)));
	}

	@ParameterizedTest
	@MethodSource("filesAndCalls")
	void changeFilenameSuffix_keepsTheFileInItsDirectory_belowADottedDirectory(
		final String relative, final ChangeFilenameSuffix call) throws Exception
	{
		final Path file = tempDir.resolve(relative);
		Files.createDirectories(file.getParent());
		Files.writeString(file, "content");
		final String name = file.getFileName().toString();
		final int dot = name.lastIndexOf('.');
		final String expectedName = (dot == -1 ? name : name.substring(0, dot)) + ".bak";

		assertTrue(call.change(file.toFile(), ".bak"));

		final Path renamed = file.getParent().resolve(expectedName);
		assertEquals("content", Files.readString(renamed), "renamed in its own directory");
		assertFalse(Files.exists(file), "the old name is gone");
		try (Stream<Path> top = Files.list(tempDir))
		{
			assertEquals(1, top.count(), "nothing moved up into the temporary directory");
		}
	}
}
