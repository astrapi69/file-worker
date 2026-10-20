package io.github.astrapi69.file.tools;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

import io.github.astrapi69.file.copy.CopyDirectoryExtensions;
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
 */
public class MergeWithTemplate
{

	private static final String CONFIG_PATH = "./config/merge_defaults.json";

	public static void main(String[] args) throws Exception
	{
		Map<String, Object> cfg = loadDefaults(CONFIG_PATH);

		// Parse CLI
		Map<String, List<String>> cli = parseArgs(args);
		String srcBase = firstOrDefault(cli.get("--src_base"), (String)cfg.get("default_src_base"));
		String dstBase = firstOrDefault(cli.get("--dst_base"), (String)cfg.get("default_dst_base"));
		List<String> dirs = orDefault(cli.get("--dirs"), castList(cfg.get("default_dirs")));
		List<String> files = castList(cfg.get("default_files"));
		boolean dryRun = cli.containsKey("--dry-run");

		if (dirs == null || dirs.isEmpty())
		{
			die("No directories specified. Use --dirs <dir1> <dir2> ... or set default_dirs in "
				+ CONFIG_PATH);
		}
		if (isBlank(srcBase))
			srcBase = cwd();
		if (isBlank(dstBase))
			dstBase = cwd();

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
					log("🔀 DRY-RUN merge " + srcDir + " -> " + dstDir + " (latest-modified wins)");
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
				CopyDirectoryExtensions.copyDirectory(srcDir.toPath(), dstDir.toPath());
			}
			else
			{
				log("🔀 Merging " + srcDir + " -> " + dstDir + " (latest-modified wins)");
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

	private static void preview(File srcDir, File dstDir) throws IOException
	{
		final Collection<File> files = FileUtils.listFiles(srcDir, null, true);
		String base = srcDir.getCanonicalPath();
		String dstBase = dstDir.getCanonicalPath();
		for (File f : files)
		{
			String rel = f.getCanonicalPath().substring(base.length()).replace(File.separatorChar,
				'/');
			if (rel.startsWith("/"))
				rel = rel.substring(1);
			File to = new File(dstBase, rel);
			log("   📄 " + f.getPath() + "  ->  " + to.getPath());
		}
	}

	@SuppressWarnings("unchecked")
	private static List<String> castList(Object o)
	{
		if (o == null)
			return null;
		if (o instanceof List<?>)
		{
			List<String> out = new ArrayList<>();
			for (Object e : (List<?>)o)
				if (e != null)
					out.add(String.valueOf(e));
			return out;
		}
		return null;
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

	private static String cwd()
	{
		return Path.of(".").toAbsolutePath().normalize().toString();
	}

	private static void die(String msg)
	{
		System.err.println(msg);
		System.exit(2);
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
	// ... keep the rest of your class

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

	private static String quote(String s)
	{
		return "\"" + s + "\"";
	}
}
