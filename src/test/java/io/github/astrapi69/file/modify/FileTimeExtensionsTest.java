package io.github.astrapi69.file.modify;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FileTimeExtensionsTest
{
	private File testFile;

	@BeforeEach
	void setUp() throws Exception
	{
		// Create a temporary file and write something to it
		testFile = File.createTempFile("test-", ".txt");
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
