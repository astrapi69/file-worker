package io.github.astrapi69.file.modify;

/**
 * Enum representing the types of timestamps available for files Can be used to retrieve creation
 * time, last modified time, or last access time from a file
 */
public enum FileTimestampType
{
	/**
	 * The timestamp when the file was originally created
	 */
	CREATION,

	/**
	 * The timestamp when the file was last modified
	 */
	LAST_MODIFIED,

	/**
	 * The timestamp when the file was last accessed
	 */
	LAST_ACCESS
}
