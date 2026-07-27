package io.github.astrapi69.file.merge.strategy;

/**
 * This enum class defines strategies for merging file contents
 */
public enum MergeFileStrategy
{

	/**
	 * APPEND: Simply appends the content of the second file to the first file. No deduplication or
	 * sorting is performed.
	 */
	APPEND,

	/**
	 * UNIQUE_LINES: Merges the files and removes duplicate lines. The order of first occurrence is
	 * preserved.
	 */
	UNIQUE_LINES,

	/**
	 * SORTED: Merges the files and sorts all lines alphabetically.
	 */
	SORTED,

	/**
	 * SORTED_UNIQUE: Merges the files, removes duplicates, and sorts the result alphabetically.
	 */
	SORTED_UNIQUE
}
