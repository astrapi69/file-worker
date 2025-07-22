package io.github.astrapi69.file.modify;

import io.github.astrapi69.file.create.FileFactory;
import io.github.astrapi69.file.search.PathFinder;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for {@link FileTimeExtensions}
 */
class FileTimeExtensionsIntegrationTest
{
	/**
	 * Verifies that the last modified timestamp changes after calling {@link BasicFileAttributeView#setTimes}
	 * and {@link Files#setLastModifiedTime(Path, FileTime)} on a temporary copy of the foo.pdf test resource
	 *
	 * @param tempDir
	 *            JUnit-provided temporary directory (unique per test run)
	 */
	@Test
	void testChangeTimestamps(@TempDir Path tempDir) throws Exception
	{
		// --- Arrange ---------------------------------------------------------
		// Locate the test resource foo.pdf (same as in your example)
		File parent = PathFinder.getRelativePath(PathFinder.getSrcTestResourcesDir(), "resources");
		File sourceFile = FileFactory.newFile(parent, "foo.pdf");
		assertTrue(sourceFile.exists(), "Expected foo.pdf test resource to exist");

		// Copy to temp so we can mutate safely
		Path targetPath = tempDir.resolve("foo-copy.pdf");
		Files.copy(sourceFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);

		// Ensure the FS supports BasicFileAttributeView
		BasicFileAttributeView view = Files.getFileAttributeView(targetPath, BasicFileAttributeView.class);
		Assumptions.assumeTrue(view != null, "BasicFileAttributeView not supported on this filesystem; skipping test");

		// Read original timestamp
		LocalDateTime originalLastModified = FileTimeExtensions.getTimestamp(targetPath, FileTimestampType.LAST_MODIFIED);

		// Prepare new timestamp (same as your example)
		LocalDateTime newDateTime = LocalDateTime.of(2016, 4, 16, 14, 23);
		FileTime newFileTime = FileTime.from(newDateTime.atZone(ZoneId.systemDefault()).toInstant());

		// --- Act -------------------------------------------------------------
		// NOTE: order is (lastModifiedTime, lastAccessTime, createTime)
		view.setTimes(newFileTime, newFileTime, newFileTime);

		// Also explicitly set last modified (extra safety)
		Files.setLastModifiedTime(targetPath, newFileTime);

		// --- Assert ----------------------------------------------------------
		LocalDateTime changedLastModified = FileTimeExtensions.getTimestamp(targetPath, FileTimestampType.LAST_MODIFIED);
		System.out.println("Original last modified: " + originalLastModified);
		System.out.println("Changed  last modified: " + changedLastModified);

		// Because some file systems store coarse timestamps (seconds), allow a difference check
		long expectedMillis = newFileTime.toMillis();
		long actualMillis = Files.getLastModifiedTime(targetPath).toMillis();
		long diff = Math.abs(expectedMillis - actualMillis);

		// Accept difference <= 1000ms (coarse FS) but assert changed value from original
		assertNotEquals(originalLastModified, changedLastModified, "Last modified timestamp should have changed");
		assertTrue(diff <= 1000 || changedLastModified.isEqual(newDateTime), "Timestamp not updated as expected");
	}

	/**
	 * Quick smoke test: ensure we can read all three timestamps from the copied resource file
	 *
	 * @param tempDir
	 *            JUnit-provided temporary directory
	 */
	@Test
	void testReadAllTimestamps(@TempDir Path tempDir) throws IOException
	{
		File parent = PathFinder.getRelativePath(PathFinder.getSrcTestResourcesDir(), "resources");
		File sourceFile = FileFactory.newFile(parent, "foo.pdf");
		assertTrue(sourceFile.exists(), "Expected foo.pdf test resource to exist");

		Path copy = tempDir.resolve("foo-copy.pdf");
		Files.copy(sourceFile.toPath(), copy, StandardCopyOption.REPLACE_EXISTING);

		BasicFileAttributes attrs = Files.readAttributes(copy, BasicFileAttributes.class);
		assertNotNull(attrs);

		assertNotNull(FileTimeExtensions.getTimestamp(copy, FileTimestampType.CREATION));
		assertNotNull(FileTimeExtensions.getTimestamp(copy, FileTimestampType.LAST_MODIFIED));
		assertNotNull(FileTimeExtensions.getTimestamp(copy, FileTimestampType.LAST_ACCESS));

		Path testFile = tempDir.resolve("sample.txt");
		Files.writeString(testFile, "Hello test");

		for (FileTimestampType type : FileTimestampType.values())
		{
			LocalDateTime timestamp = FileTimeExtensions.getTimestamp(testFile, type);
			assertNotNull(timestamp, "Timestamp should not be null for type: " + type);
		}
	}

	/**
	 * Verifies that the last modified timestamp changes after setting a new timestamp using BasicFileAttributeView
	 *
	 * @param tempDir
	 *            the temporary directory provided by JUnit
	 */
	@Test
	void testChangeLastModifiedTime(@TempDir Path tempDir) throws Exception
	{
		// --- Arrange --------------------------------------------------------
		Path testFile = tempDir.resolve("test-file.txt");
		Files.writeString(testFile, "Initial content");

		// Ensure FS supports BasicFileAttributeView
		BasicFileAttributeView view = Files.getFileAttributeView(testFile, BasicFileAttributeView.class);
		Assumptions.assumeTrue(view != null, "BasicFileAttributeView not supported; skipping test");

		LocalDateTime original = FileTimeExtensions.getTimestamp(testFile, FileTimestampType.LAST_MODIFIED);

		LocalDateTime newDateTime = LocalDateTime.of(2016, 4, 16, 14, 23);
		FileTime newFileTime = FileTime.from(newDateTime.atZone(ZoneId.systemDefault()).toInstant());

		// --- Act ------------------------------------------------------------
		view.setTimes(newFileTime, newFileTime, newFileTime);
		Files.setLastModifiedTime(testFile, newFileTime);

		LocalDateTime updated = FileTimeExtensions.getTimestamp(testFile, FileTimestampType.LAST_MODIFIED);

		// --- Assert ---------------------------------------------------------
		assertNotEquals(original, updated);
		long diff = Math.abs(updated.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
				- newFileTime.toMillis());
		assertTrue(diff <= 1000, "Modified time difference too large: " + diff + " ms");
	}

}
