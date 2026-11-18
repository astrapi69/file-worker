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
package io.github.astrapi69.file.tools;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

import io.github.astrapi69.file.merge.MergeDirectoryExtensions;

/**
 * CLI: Merge/copy selected directories from a source base into a destination base.
 *
 * Defaults can be provided in ./config/merge_defaults.json: { "default_src_base":
 * "/path/to/template", "default_dst_base": "/path/to/project", "default_dirs": ["scripts","tests"]
 * }
 *
 * Usage examples: java io.github.astrapi69.tools.MergeWithTemplate --dirs scripts tests java
 * io.github.astrapi69.tools.MergeWithTemplate --src_base /tmp/src --dst_base /tmp/dst --dirs assets
 * docs java io.github.astrapi69.tools.MergeWithTemplate --dry-run --dirs scripts tests
 *
 * <p>
 * A directory that does not exist at the destination yet is copied, one that exists is merged: the
 * files of the source overwrite the files of the same name at the destination, whichever is newer,
 * and the other files at the destination stay. Both keep the modification time of the source files,
 * as the copy of the default files does. A dry run copies, merges and overwrites nothing (it only
 * creates the destination base if it is missing) and lists each file of the source directory with
 * its destination, the path of the file relative to the source directory, also for a symbolic link
 * whose target lies outside the source directory.
 */
public class MergeWithTemplate
{

	private static final String CONFIG_PATH = "./config/merge_defaults.json";

	/** The exit code of {@link #main(String[])} on a usage error. */
	public static final int USAGE_ERROR_EXIT_CODE = 2;

	/**
	 * Creates a new {@link MergeWithTemplate} object; its methods are static, so it needs none
	 */
	public MergeWithTemplate()
	{
	}

	/**
	 * The command line: runs {@link #run(String[])} and ends the JVM with
	 * {@value #USAGE_ERROR_EXIT_CODE} on a usage error. A caller in the same JVM calls {@code run}
	 * instead, which throws (#34).
	 *
	 * @param args
	 *            the arguments, see the class documentation
	 * @throws Exception
	 *             if copying or merging fails
	 */
	public static void main(String[] args) throws Exception
	{
		int exitCode = commandLine(args, Path.of(CONFIG_PATH),
			Path.of(".").toAbsolutePath().normalize());
		if (exitCode != 0)
		{
			System.exit(exitCode);
		}
	}

	/**
	 * What {@link #main(String[])} does, short of ending the JVM: copies and merges as
	 * {@link #run(String[], Path, Path)} does and returns 0, or prints the usage error on standard
	 * error and returns {@value #USAGE_ERROR_EXIT_CODE}, the exit code {@code main} ends the JVM
	 * with. The tests call it in place of {@code main}, whose {@code System.exit} would end the
	 * test JVM.
	 *
	 * @param args
	 *            the arguments, see the class documentation
	 * @param configFile
	 *            the JSON file with the defaults; a missing file means no defaults
	 * @param workingDirectory
	 *            the base for a source or destination that neither the arguments nor the defaults
	 *            name
	 * @return 0, or {@value #USAGE_ERROR_EXIT_CODE} on a usage error
	 * @throws IOException
	 *             if copying or merging fails
	 */
	static int commandLine(String[] args, Path configFile, Path workingDirectory) throws IOException
	{
		try
		{
			run(args, configFile, workingDirectory);
			return 0;
		}
		catch (UsageException usageError)
		{
			System.err.println(usageError.getMessage());
			return USAGE_ERROR_EXIT_CODE;
		}
	}

	/**
	 * Copies and merges as {@link #main(String[])} does, with the defaults of
	 * {@code ./config/merge_defaults.json}, without ending the JVM.
	 *
	 * @param args
	 *            the arguments, see the class documentation
	 * @throws UsageException
	 *             if neither the arguments nor the defaults name a directory
	 * @throws IOException
	 *             if copying or merging fails
	 */
	public static void run(String[] args) throws IOException
	{
		run(args, Path.of(CONFIG_PATH));
	}

	/**
	 * Copies and merges as {@link #main(String[])} does, with the defaults of the given file,
	 * without ending the JVM.
	 *
	 * @param args
	 *            the arguments, see the class documentation
	 * @param configFile
	 *            the JSON file with the defaults; a missing file means no defaults
	 * @throws UsageException
	 *             if neither the arguments nor the defaults name a directory
	 * @throws IOException
	 *             if copying or merging fails
	 */
	public static void run(String[] args, Path configFile) throws IOException
	{
		run(args, configFile, Path.of(".").toAbsolutePath().normalize());
	}

	/**
	 * Copies and merges as {@link #run(String[], Path)} does, with the given directory in place of
	 * the current working directory: it is the source base and the destination base that neither
	 * the arguments nor the defaults name.
	 *
	 * @param args
	 *            the arguments, see the class documentation
	 * @param configFile
	 *            the JSON file with the defaults; a missing file means no defaults
	 * @param workingDirectory
	 *            the base for a source or destination that neither the arguments nor the defaults
	 *            name
	 * @throws UsageException
	 *             if neither the arguments nor the defaults name a directory
	 * @throws IOException
	 *             if copying or merging fails
	 */
	public static void run(String[] args, Path configFile, Path workingDirectory) throws IOException
	{
		Map<String, Object> cfg = loadDefaults(configFile.toString());

		// Parse CLI
		Map<String, List<String>> cli = parseArgs(args);
		String srcBase = firstOrDefault(cli.get("--src_base"), (String)cfg.get("default_src_base"));
		String dstBase = firstOrDefault(cli.get("--dst_base"), (String)cfg.get("default_dst_base"));
		List<String> dirs = orDefault(cli.get("--dirs"), castList(cfg.get("default_dirs")));
		List<String> files = castList(cfg.get("default_files"));
		boolean dryRun = cli.containsKey("--dry-run");

		if (dirs == null || dirs.isEmpty())
		{
			throw new UsageException(
				"No directories specified. Use --dirs <dir1> <dir2> ... or set default_dirs in "
					+ configFile);
		}
		if (isBlank(srcBase))
			srcBase = workingDirectory.toString();
		if (isBlank(dstBase))
			dstBase = workingDirectory.toString();

		log("🔎 Source base: " + srcBase);
		log("📦 Destination base: " + dstBase);
		log("📂 Dirs to process: " + dirs + (dryRun ? " (dry-run)" : ""));
		if (files != null && !files.isEmpty())
		{
			log("📄 Files to process: " + files + (dryRun ? " (dry-run)" : ""));
		}
		Path dstBasePath = Path.of(dstBase);
		Files.createDirectories(dstBasePath);
		copyFiles(srcBase, dstBase, files, dryRun);

		for (String d : dirs)
		{
			File srcDir = Path.of(srcBase, d).toFile();
			File dstDir = Path.of(dstBase, d).toFile();

			if (!srcDir.isDirectory())
			{
				warn("❌ Not a directory (skip): " + srcDir.getAbsolutePath());
				continue;
			}

			if (dryRun)
			{
				if (dstDir.exists())
				{
					log("🔀 DRY-RUN merge " + srcDir + " -> " + dstDir
						+ " (source overwrites existing files)");
					preview(srcDir, dstDir);
				}
				else
				{
					log("📂 DRY-RUN copy " + srcDir + " -> " + dstDir + " (create)");
					preview(srcDir, dstDir);
				}
				continue;
			}

			if (!dstDir.exists())
			{
				log("📂 Copying " + srcDir + " -> " + dstDir + " (create)");
				copyDirectoryKeepingTimes(srcDir.toPath(), dstDir.toPath());
			}
			else
			{
				log("🔀 Merging " + srcDir + " -> " + dstDir
					+ " (source overwrites existing files)");
				// MergeDirectoryExtensions expects targetDir to exist and merges contents of given
				// source dirs into it
				Files.createDirectories(dstDir.toPath());
				MergeDirectoryExtensions.merge(dstDir, srcDir);
			}
		}
		log("✅ Done.");
	}

	// ---------- helpers ----------

	private static void copyFiles(String srcBase, String dstBase, List<String> files,
		boolean dryRun) throws IOException
	{
		if (files == null || files.isEmpty())
			return;

		for (String fileName : files)
		{
			Path src = Path.of(srcBase, fileName);
			Path dst = Path.of(dstBase, fileName);

			if (!Files.exists(src))
			{
				warn("❌ File not found in template (skip): " + src.toAbsolutePath());
				continue;
			}

			if (dryRun)
			{
				log("📄 DRY-RUN copy file " + src.toAbsolutePath() + " -> " + dst.toAbsolutePath());
				continue;
			}

			Files.createDirectories(dst.getParent()); // falls im Unterordner
			Files.copy(src, dst, java.nio.file.StandardCopyOption.REPLACE_EXISTING,
				java.nio.file.StandardCopyOption.COPY_ATTRIBUTES);
			log("📄 Copied file " + src.toAbsolutePath() + " -> " + dst.toAbsolutePath());
		}
	}

	/**
	 * Copies the given source directory into the given destination directory, which does not exist
	 * yet, and keeps the modification time of the files, as the merge does
	 * ({@code CopyDirectoryExtensions.copyDirectory(Path, Path)} gives the copies the current
	 * time).
	 */
	private static void copyDirectoryKeepingTimes(Path source, Path destination) throws IOException
	{
		Files.walkFileTree(source, new SimpleFileVisitor<>()
		{
			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
				throws IOException
			{
				Files.createDirectories(destination.resolve(source.relativize(dir)));
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
				throws IOException
			{
				Files.copy(file, destination.resolve(source.relativize(file)),
					StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
				return FileVisitResult.CONTINUE;
			}
		});
	}

	/**
	 * Lists each file of the source directory with its destination: the path of the file relative
	 * to the source directory, not its canonical path, so a symbolic link keeps its own name.
	 */
	private static void preview(File srcDir, File dstDir) throws IOException
	{
		final Collection<File> files = FileUtils.listFiles(srcDir, null, true);
		Path source = srcDir.toPath();
		String dstBase = dstDir.getCanonicalPath();
		for (File f : files)
		{
			File to = new File(dstBase, source.relativize(f.toPath()).toString());
			log("   📄 " + f.getPath() + "  ->  " + to.getPath());
		}
	}

	/**
	 * Casts a list of the defaults, which {@link #loadDefaults(String)} fills only with the results
	 * of {@link #jsonStringArray(String, String)}: a list of strings or {@code null}.
	 */
	@SuppressWarnings("unchecked")
	private static List<String> castList(Object o)
	{
		return (List<String>)o;
	}

	private static String firstOrDefault(List<String> values, String def)
	{
		return (values != null && !values.isEmpty()) ? values.get(0) : def;
	}

	private static List<String> orDefault(List<String> v, List<String> def)
	{
		return (v != null && !v.isEmpty()) ? v : def;
	}

	private static boolean isBlank(String s)
	{
		return s == null || s.trim().isEmpty();
	}

	private static void log(String s)
	{
		System.out.println(s);
	}

	private static void warn(String s)
	{
		System.out.println(s);
	}

	/**
	 * Minimal arg parser: --flag -> present --key val1 val2 ... (until next --key)
	 */
	private static Map<String, List<String>> parseArgs(String[] args)
	{
		Map<String, List<String>> map = new LinkedHashMap<>();
		String current = null;
		for (String a : args)
		{
			if (a.startsWith("--"))
			{
				current = a;
				map.putIfAbsent(current, new ArrayList<>());
			}
			else if (current != null)
			{
				map.get(current).add(a);
			}
			else
			{
				// ignore stray positional
			}
		}
		return map;
	}

	/**
	 * Tiny JSON reader for exactly our config shape (no external deps). Accepts keys:
	 * default_src_base (string), default_dst_base (string), default_dirs (array of strings)
	 */
	private static Map<String, Object> loadDefaults(String path)
	{
		Map<String, Object> out = new HashMap<>();
		File f = new File(path);
		if (!f.isFile())
			return out;
		try
		{
			String s = Files.readString(f.toPath());
			out.put("default_src_base", jsonString(s, "default_src_base"));
			out.put("default_dst_base", jsonString(s, "default_dst_base"));
			out.put("default_dirs", jsonStringArray(s, "default_dirs"));
			out.put("default_files", jsonStringArray(s, "default_files"));
		}
		catch (Exception e)
		{
			warn("⚠️ Could not load defaults from " + path + ": " + e.getMessage());
		}
		return out;
	}

	// --------- ultra-light JSON extractors (sufficient for simple config) ---------

	private static String jsonString(String json, String key)
	{
		// Matches: "key" : "value"
		Pattern p = Pattern.compile("\"" + Pattern.quote(key) + "\"\\s*:\\s*\"([^\"]*)\"",
			Pattern.DOTALL);
		Matcher m = p.matcher(json);
		return m.find() ? m.group(1) : null;
	}

	private static List<String> jsonStringArray(String json, String key)
	{
		// Matches: "key" : [ ...anything until closing bracket... ]
		Pattern p = Pattern.compile("\"" + Pattern.quote(key) + "\"\\s*:\\s*\\[(.*?)\\]",
			Pattern.DOTALL);
		Matcher m = p.matcher(json);
		if (!m.find())
			return null;

		String body = m.group(1);
		// Extract each "string" element inside the array (simple, no escaped quotes support)
		Matcher q = Pattern.compile("\"([^\"]*)\"").matcher(body);

		List<String> out = new ArrayList<>();
		while (q.find())
			out.add(q.group(1));
		return out;
	}

	/**
	 * A usage error of the command line: {@link #run(String[], Path)} throws it, and
	 * {@link #main(String[])} reports it with the exit code {@value #USAGE_ERROR_EXIT_CODE}.
	 */
	public static class UsageException extends IllegalArgumentException
	{
		private static final long serialVersionUID = 1L;

		UsageException(String message)
		{
			super(message);
		}
	}
}
