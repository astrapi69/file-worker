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
package io.github.astrapi69.file.modify;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class FileTimeExtensionsTest
{
	@TempDir
	Path tempDir;

	private File testFile;

	@BeforeEach
	void setUp() throws Exception
	{
		// Create a temporary file and write something to it
		testFile = Files.createTempFile(tempDir, "test-", ".txt").toFile();
		try (FileWriter writer = new FileWriter(testFile))
		{
			writer.write("Hello, world!");
		}
		// Ensure the file exists
		assertTrue(testFile.exists());
	}

	@Test
	void testGetCreationTime() throws Exception
	{
		LocalDateTime creationTime = FileTimeExtensions.getTimestamp(testFile,
			FileTimestampType.CREATION);
		assertNotNull(creationTime);
		System.out.println("Creation time: " + creationTime);
	}

	@Test
	void testGetLastModifiedTime() throws Exception
	{
		LocalDateTime modifiedTime = FileTimeExtensions.getTimestamp(testFile,
			FileTimestampType.LAST_MODIFIED);
		assertNotNull(modifiedTime);
		System.out.println("Last modified time: " + modifiedTime);
	}

	@Test
	void testGetLastAccessTime() throws Exception
	{
		LocalDateTime accessTime = FileTimeExtensions.getTimestamp(testFile,
			FileTimestampType.LAST_ACCESS);
		assertNotNull(accessTime);
		System.out.println("Last access time: " + accessTime);
	}

	@Test
	void testGetTimestampWithPath() throws Exception
	{
		Path path = testFile.toPath();
		LocalDateTime timestamp = FileTimeExtensions.getTimestamp(path,
			FileTimestampType.LAST_MODIFIED);
		assertNotNull(timestamp);
	}

	@Test
	void testInvalidTypeThrowsException() throws Exception
	{
		assertThrows(IllegalArgumentException.class, () -> {
			// Simulate an invalid enum (null for demonstration)
			FileTimeExtensions.getTimestamp(testFile, null);
		});
	}
}
