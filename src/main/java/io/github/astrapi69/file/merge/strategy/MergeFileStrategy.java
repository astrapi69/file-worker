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
