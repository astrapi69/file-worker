package io.github.astrapi69.file.modify;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Utility class for retrieving specific file timestamps such as creation, last modified, and last access time
 * from a given {@link File} or {@link Path} and converting them to {@link LocalDateTime}
 */
public class FileTimeExtensions
{
	/**
	 * Gets the specified timestamp of the given File as a LocalDateTime
	 *
	 * @param file
	 *            the file
	 * @param type
	 *            the timestamp type to retrieve
	 * @return the corresponding LocalDateTime
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public static LocalDateTime getTimestamp(File file, FileTimestampType type) throws IOException
	{
		return getTimestamp(file.toPath(), type);
	}

	/**
	 * Gets the specified timestamp of the given Path as a LocalDateTime
	 *
	 * @param path
	 *            the file path
	 * @param type
	 *            the timestamp type to retrieve
	 * @return the corresponding LocalDateTime
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public static LocalDateTime getTimestamp(Path path, FileTimestampType type) throws IOException
	{
		if (path == null || type == null)
		{
			throw new IllegalArgumentException("Path and timestamp type must not be null");
		}

		BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
		Instant instant;

		switch (type)
		{
			case CREATION:
				instant = attrs.creationTime().toInstant();
				break;
			case LAST_MODIFIED:
				instant = attrs.lastModifiedTime().toInstant();
				break;
			case LAST_ACCESS:
				instant = attrs.lastAccessTime().toInstant();
				break;
			default:
				throw new IllegalArgumentException("Unsupported timestamp type: " + type);
		}

		return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
	}

}
