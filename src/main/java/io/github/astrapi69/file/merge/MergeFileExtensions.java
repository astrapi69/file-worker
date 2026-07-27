package io.github.astrapi69.file.merge;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import io.github.astrapi69.file.merge.strategy.MergeFileStrategy;

/**
 * The class {@link MergeFileExtensions} provides methods for intelligently merging file contents.
 * Unlike concatenation, merge operations may deduplicate, sort, or otherwise process the content.
 * Methods ending with "AndGet" return the resulting File/Path for fluent API usage.
 */
public final class MergeFileExtensions
{

	private MergeFileExtensions()
	{
	}

	/**
	 * Merges multiple files into a target path using the specified strategy (UTF-8 default).
	 *
	 * @param files
	 *            the list of file paths to merge
	 * @param targetPath
	 *            the target path
	 * @param strategy
	 *            the merge strategy
	 * @return the targetPath
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public static Path mergeAndGet(List<Path> files, Path targetPath, MergeFileStrategy strategy)
		throws IOException
	{
		return mergeAndGet(files, targetPath, java.nio.charset.StandardCharsets.UTF_8, strategy);
	}

	// =========================================================================
	// MERGE TWO FILES
	// =========================================================================

	/**
	 * Merges two files into a target file using the specified strategy.
	 *
	 * @param file1
	 *            the first file
	 * @param file2
	 *            the second file
	 * @param targetFile
	 *            the target file
	 * @param charset
	 *            the charset to use
	 * @param strategy
	 *            the merge strategy
	 * @return the targetFile
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public static File mergeAndGet(File file1, File file2, File targetFile, Charset charset,
		MergeFileStrategy strategy) throws IOException
	{
		return mergeAndGet(List.of(file1, file2), targetFile, charset, strategy);
	}

	/**
	 * Merges two files into a target file using the specified strategy (UTF-8 default).
	 */
	public static File mergeAndGet(File file1, File file2, File targetFile,
		MergeFileStrategy strategy) throws IOException
	{
		return mergeAndGet(file1, file2, targetFile, StandardCharsets.UTF_8, strategy);
	}

	/**
	 * Existing void method for backward compatibility.
	 */
	public static void merge(File file1, File file2, File targetFile, MergeFileStrategy strategy)
		throws IOException
	{
		mergeAndGet(file1, file2, targetFile, strategy);
	}

	// =========================================================================
	// MERGE MULTIPLE FILES
	// =========================================================================

	/**
	 * Merges multiple files into a target file using the specified strategy.
	 *
	 * @param files
	 *            the list of files to merge
	 * @param targetFile
	 *            the target file
	 * @param charset
	 *            the charset to use
	 * @param strategy
	 *            the merge strategy
	 * @return the targetFile
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public static File mergeAndGet(List<File> files, File targetFile, Charset charset,
		MergeFileStrategy strategy) throws IOException
	{
		Objects.requireNonNull(files, "files must not be null");
		Objects.requireNonNull(targetFile, "targetFile must not be null");
		Objects.requireNonNull(charset, "charset must not be null");
		Objects.requireNonNull(strategy, "strategy must not be null");

		if (files.isEmpty())
		{
			throw new IllegalArgumentException("files must not be empty");
		}

		List<String> mergedLines = collectAndMerge(files, charset, strategy);
		writeLines(targetFile, mergedLines, charset);
		return targetFile;
	}

	/**
	 * Merges multiple files into a target file (UTF-8 default).
	 */
	public static File mergeAndGet(List<File> files, File targetFile, MergeFileStrategy strategy)
		throws IOException
	{
		return mergeAndGet(files, targetFile, StandardCharsets.UTF_8, strategy);
	}

	/**
	 * Existing void method for backward compatibility.
	 */
	public static void merge(List<File> files, File targetFile, MergeFileStrategy strategy)
		throws IOException
	{
		mergeAndGet(files, targetFile, strategy);
	}

	// =========================================================================
	// MERGE WITH PATH API
	// =========================================================================

	/**
	 * Merges multiple files into a target path using the specified strategy.
	 *
	 * @param files
	 *            the list of file paths to merge
	 * @param targetPath
	 *            the target path
	 * @param charset
	 *            the charset to use
	 * @param strategy
	 *            the merge strategy
	 * @return the targetPath
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public static Path mergeAndGet(List<Path> files, Path targetPath, Charset charset,
		MergeFileStrategy strategy) throws IOException
	{
		Objects.requireNonNull(files, "files must not be null");
		Objects.requireNonNull(targetPath, "targetPath must not be null");

		List<File> asFiles = new ArrayList<>();
		for (Path p : files)
		{
			asFiles.add(p.toFile());
		}
		mergeAndGet(asFiles, targetPath.toFile(), charset, strategy);
		return targetPath;
	}

	// =========================================================================
	// CORE MERGE LOGIC
	// =========================================================================

	private static List<String> collectAndMerge(List<File> files, Charset charset,
		MergeFileStrategy strategy) throws IOException
	{

		switch (strategy)
		{
			case APPEND :
				return collectAppend(files, charset);
			case UNIQUE_LINES :
				return collectUnique(files, charset, false);
			case SORTED :
				return collectSorted(files, charset, false);
			case SORTED_UNIQUE :
				return collectSorted(files, charset, true);
			default :
				throw new IllegalArgumentException("Unknown strategy: " + strategy);
		}
	}

	private static List<String> collectAppend(List<File> files, Charset charset) throws IOException
	{
		List<String> result = new ArrayList<>();
		for (File file : files)
		{
			result.addAll(readLines(file, charset));
		}
		return result;
	}

	private static List<String> collectUnique(List<File> files, Charset charset, boolean sort)
		throws IOException
	{
		// LinkedHashSet preserves insertion order while removing duplicates
		Set<String> unique = new LinkedHashSet<>();
		for (File file : files)
		{
			unique.addAll(readLines(file, charset));
		}
		List<String> result = new ArrayList<>(unique);
		if (sort)
		{
			Collections.sort(result);
		}
		return result;
	}

	private static List<String> collectSorted(List<File> files, Charset charset, boolean unique)
		throws IOException
	{
		List<String> all = new ArrayList<>();
		for (File file : files)
		{
			all.addAll(readLines(file, charset));
		}
		if (unique)
		{
			all = new ArrayList<>(new LinkedHashSet<>(all));
		}
		Collections.sort(all);
		return all;
	}

	private static List<String> readLines(File file, Charset charset) throws IOException
	{
		List<String> lines = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(
			new InputStreamReader(new FileInputStream(file), charset)))
		{
			String line;
			while ((line = reader.readLine()) != null)
			{
				lines.add(line);
			}
		}
		return lines;
	}

	private static void writeLines(File targetFile, List<String> lines, Charset charset)
		throws IOException
	{
		try (Writer writer = new BufferedWriter(
			new OutputStreamWriter(new FileOutputStream(targetFile), charset)))
		{
			for (int i = 0; i < lines.size(); i++)
			{
				writer.write(lines.get(i));
				if (i < lines.size() - 1)
				{
					writer.write(System.lineSeparator());
				}
			}
			writer.write(System.lineSeparator());
		}
	}
}
