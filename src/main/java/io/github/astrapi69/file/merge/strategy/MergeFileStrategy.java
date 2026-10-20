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
	SORTED_UNIQUE,

	/**
	 * CSV_HEADER_MERGE: Merges CSV files, keeping the header (first line) only from the very first
	 * file. Subsequent files will have their first line skipped if it matches the initial header.
	 */
	CSV_HEADER_MERGE,

	/**
	 * MARKDOWN_SECTIONS: Merges Markdown files and automatically inserts a horizontal rule ("---")
	 * with surrounding empty lines between the content of different files for clean separation.
	 */
	MARKDOWN_SECTIONS,

	/**
	 * BY_KEY: Merges files based on a unique key in each line. Default behavior: Uses the first
	 * column (index 0) with a comma (",") as delimiter. If a key appears multiple_files, the *last*
	 * encountered line for that key overwrites previous ones (update behavior). For custom
	 * delimiters or key indices, use {@code MergeFileExtensions.mergeByKey(...)}.
	 */
	BY_KEY
}
