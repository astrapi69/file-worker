package io.github.astrapi69.file.tools;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.regex.*;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import io.github.astrapi69.file.delete.DeleteFileExtensions;

/**
 * Uses JUnit 5 (`@Test`, `@TempDir`). Invokes `MergeWithTemplate.main(...)` with explicit
 * `--src_base`, `--dst_base`, `--dirs` (so it does **not** depend on your JSON defaults). Verifies:
 * copy when destination subdir is missing, merge into existing directory (overwrites), dry-run
 * doesn’t change filesystem, multiple directories are handled.
 */
class NewMergeWithTemplateTest
{

	private static void write(Path file, String content) throws IOException
	{
		Files.createDirectories(file.getParent());
		Files.writeString(file, content, StandardCharsets.UTF_8, StandardOpenOption.CREATE,
			StandardOpenOption.TRUNCATE_EXISTING);
	}

	@Test
	@Disabled("only for internal and local use")
	void usesRealJsonDefaults() throws Exception
	{
		String bookProjectBaseDir = ProjectPaths.getPyCharmBookProjectBaseDir();
		;
		String bookProjectName = "Shadows-over-New-Eden";
		mergeWithTemplate(bookProjectBaseDir, bookProjectName);
		// mergeWithOtherBookScripts(bookProjectBaseDir, bookProjectName);
	}

	@Test
	@Disabled("only for internal and local use")
	void usesRealJsonDefaultsAI_For_Everyone() throws Exception
	{
		String bookProjectBaseDir = ProjectPaths.getDriveBookProjectBaseDir();
		String bookProjectName = "ai-for-everyone";
		mergeWithTemplate(bookProjectBaseDir, bookProjectName);
	}

	@Test
	@Disabled("only for internal and local use")
	void usesRealJsonDefaultsEternity_Ebook() throws Exception
	{
		String bookProjectBaseDir = ProjectPaths.getDriveBookProjectBaseDir();

		String bookProjectName = "eternity-ebook";
		mergeWithTemplate(bookProjectBaseDir, bookProjectName);
	}

	@Test
	@Disabled("only for internal and local use")
	void usesRealJsonDefaultsRueckkehrOderBefreiung() throws Exception
	{
		String bookProjectBaseDir = ProjectPaths.getDriveBookProjectBaseDir();

		String bookProjectName = "Rueckkehr-oder-Befreiung";
		mergeWithTemplate(bookProjectBaseDir, bookProjectName);
	}

	@Test
	@Disabled("only for internal and local use")
	void usesRealJsonDefaultsPoliticalProfileInternational() throws Exception
	{
		String bookProjectBaseDir = ProjectPaths.getDriveBookProjectBaseDir();

		String bookProjectName = "political-profile-international";
		mergeWithTemplate(bookProjectBaseDir, bookProjectName);
	}

	@Test
	@Disabled("only for internal and local use")
	void usesRealJsonDefaultsGlobaleSouveraenitaet() throws Exception
	{
		String bookProjectBaseDir = ProjectPaths.getDriveBookProjectBaseDir();

		String bookProjectName = "globale-souveraenitaet";
		mergeWithTemplate(bookProjectBaseDir, bookProjectName);
	}

	@Test
	@Disabled("only for internal and local use")
	void usesRealJsonDefaultsAI_DesignsNewWorld() throws Exception
	{
		String bookProjectBaseDir = ProjectPaths.getPyCharmBookProjectBaseDir();
		;
		String bookProjectName = "ai-designs-new-world";
		mergeWithTemplate(bookProjectBaseDir, bookProjectName);
	}


	@Test
	@Disabled("only for internal and local use")
	void usesRealJsonDefaultsCurrencyOfMind() throws Exception
	{
		String bookProjectBaseDir = ProjectPaths.getDriveBookProjectBaseDir();
		String bookProjectName = "currency-of-mind-storybook";
		mergeWithTemplate(bookProjectBaseDir, bookProjectName);
	}

	@Test
	@Disabled("only for internal and local use")
	void usesRealJsonDefaultsNasenbohrerChronicles() throws Exception
	{
		String bookProjectBaseDir = ProjectPaths.getPyCharmBookProjectBaseDir();
		String bookProjectName = "nasenbohrer-chronicles";
		mergeWithTemplate(bookProjectBaseDir, bookProjectName);
	}

	@Test
	@Disabled("only for internal and local use")
	void usesRealJsonDefaultsFipsAbenteuer() throws Exception
	{
		String bookProjectBaseDir = ProjectPaths.getPyCharmBookProjectBaseDir();
		String bookProjectName = "fips-abenteuer";
		mergeWithTemplate(bookProjectBaseDir, bookProjectName);
	}

	@Test
	@Disabled("only for internal and local use")
	void usesRealJsonDefaultsLebendeStimmrecht() throws Exception
	{
		String bookProjectBaseDir = ProjectPaths.getPyCharmBookProjectBaseDir();
		String bookProjectName = "lebendes-stimmrecht";
		mergeWithTemplate(bookProjectBaseDir, bookProjectName);
	}

	@Test
	@Disabled("only for internal and local use")
	void usesRealJsonDefaultsMergeDieSouveraeneZivilisation() throws Exception
	{
		String bookProjectBaseDir = ProjectPaths.getDriveBookProjectBaseDir();

		String bookProjectName = "die-souveraene-zivilisation";
		mergeWithTemplate(bookProjectBaseDir, bookProjectName);
	}

	@Test
	@Disabled("only for internal and local use")
	void usesRealJsonDefaultsMerge_AI_For_EveryoneWithMitDenAugenEinesVaters() throws Exception
	{
		String bookProjectBaseDir = ProjectPaths.getDriveBookProjectBaseDir();

		String bookProjectName = "mit-den-augen-eines-vaters";
		mergeWithTemplate(bookProjectBaseDir, bookProjectName);
	}

	private static void mergeWithAI_For_Everyone(String bookProjectBaseDir, String bookProjectName)
		throws Exception
	{
		String templateProjectBaseDir = ProjectPaths.getDriveBookProjectBaseDir();
		String templateProjectName = "ai-for-everyone";
		mergeWithTemplate(bookProjectBaseDir, bookProjectName, templateProjectBaseDir,
			templateProjectName);
	}

	private static void mergeWithTemplate(String bookProjectBaseDir, String bookProjectName)
		throws Exception
	{
		String templateProjectBaseDir = ProjectPaths.getDriveBookProjectBaseDir();
		String templateProjectName = "write-book-template";
		mergeWithTemplate(bookProjectBaseDir, bookProjectName, templateProjectBaseDir,
			templateProjectName);
	}

	private static void mergeWithLastSpark(String bookProjectBaseDir, String bookProjectName)
		throws Exception
	{
		String templateProjectBaseDir = ProjectPaths.getPyCharmBookProjectBaseDir();
		;
		String templateProjectName = "last-spark";
		mergeWithTemplate(bookProjectBaseDir, bookProjectName, templateProjectBaseDir,
			templateProjectName);
	}

	private static void mergeWithTemplate(String bookProjectBaseDir, String bookProjectName,
		String templateProjectBaseDir, String templateProjectName) throws Exception
	{
		String defaultSrcBase = templateProjectBaseDir + "/" + templateProjectName;
		String defaultDstBase = bookProjectBaseDir + "/" + bookProjectName;
		// Dein JSON-String
		String json = "{\n" + "  \"default_src_base\": \"" + defaultSrcBase + "\",\n"
			+ "  \"default_dst_base\": \"" + defaultDstBase + "\",\n"
			+ "  \"default_dirs\": [\"scripts\", \"tests\"],\n"
			+ "  \"default_files\": [\".gitignore\", \"pyproject.toml\", \"Makefile\"]\n" + "}";

		// Sicherstellen, dass ./config existiert
		Path configDir = Path.of("config");
		Files.createDirectories(configDir);
		Path configFile = configDir.resolve("merge_defaults.json");

		// JSON reinschreiben
		Files.writeString(configFile, json, StandardCharsets.UTF_8);

		// Test: keine Args -> liest Defaults
		MergeWithTemplate.main(new String[] { });

		// overwrite .gitignore
		String gitignore = ".gitignore";
		String tmplGitignore = defaultSrcBase + "/" + gitignore;
		String bookGitignore = defaultDstBase + "/" + gitignore;
		Files.copy(Path.of(tmplGitignore), Path.of(bookGitignore),
			StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);

		// overwrite pyproject.toml
		String pyprojectToml = "pyproject.toml";
		String tmplPyprojectToml = defaultSrcBase + "/" + pyprojectToml;
		String bookPyprojectToml = defaultDstBase + "/" + pyprojectToml;
		Files.copy(Path.of(tmplPyprojectToml), Path.of(bookPyprojectToml),
			StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
		// overwrite makefile
		String makefile = "Makefile";
		String tmplMakefile = defaultSrcBase + "/" + makefile;
		String bookMakefile = defaultDstBase + "/" + makefile;
		Files.copy(Path.of(tmplMakefile), Path.of(bookMakefile),
			StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);

		// Danach kannst du prüfen, ob Dateien im Ziel vorhanden sind
		assertTrue(Files.exists(Path.of(defaultDstBase + "/scripts")));
		assertTrue(Files.exists(Path.of(defaultDstBase + "/tests")));
		assertTrue(Files.exists(Path.of(defaultDstBase + "/Makefile")));
		assertTrue(Files.exists(Path.of(defaultDstBase + "/.gitignore")));
		assertTrue(Files.exists(Path.of(defaultDstBase + "/pyproject.toml")));
		DeleteFileExtensions.delete(configFile);
	}

	@Test
	@Disabled("only for internal and local use")
	void mergesIntoExistingDirectory_overwritesTargetFiles_last_spark() throws Exception
	{
		Path srcBase = Path
			.of("/run/media/astrapi69/backups/git/hub/astrapi69/write-book-template");
		Path dstBase = Path.of("/home/astrapi69/PycharmProjects/last-spark");


		// Destination subdir exists -> CLI uses MergeDirectoryExtensions.merge()
		MergeWithTemplate.main(new String[] { "--src_base", srcBase.toString(), "--dst_base",
				dstBase.toString(), "--dirs", "scripts", "tests" });

	}

	@Test
	@Disabled("only for internal and local use")
	void mergesIntoExistingDirectory_overwritesTargetFiles_currency_of_mind() throws Exception
	{
		Path srcBase = Path
			.of("/run/media/astrapi69/backups/git/hub/astrapi69/write-book-template");
		Path dstBase = Path.of("/home/astrapi69/dev/git/hub/currency-of-mind-storybook");


		// Destination subdir exists -> CLI uses MergeDirectoryExtensions.merge()
		MergeWithTemplate.main(new String[] { "--src_base", srcBase.toString(), "--dst_base",
				dstBase.toString(), "--dirs", "scripts", "tests" });

	}

	@Test
	void dryRunDoesNotChangeFilesystem(@TempDir Path tmp) throws Exception
	{
		Path srcBase = tmp.resolve("srcBase");
		Path dstBase = tmp.resolve("dstBase");

		Path srcScripts = srcBase.resolve("scripts");
		Path f1 = srcScripts.resolve("a.txt");
		write(f1, "A");

		// no scripts/ at destination yet
		assertFalse(Files.exists(dstBase.resolve("scripts")));

		MergeWithTemplate.main(new String[] { "--src_base", srcBase.toString(), "--dst_base",
				dstBase.toString(), "--dirs", "scripts", "--dry-run" });

		// still no scripts/ created
		assertFalse(Files.exists(dstBase.resolve("scripts")),
			"Dry-run must not create destination directories or copy files");
	}

	@Test
	void copiesMultipleDirs(@TempDir Path tmp) throws Exception
	{
		Path srcBase = tmp.resolve("srcBase");
		Path dstBase = tmp.resolve("dstBase");

		write(srcBase.resolve("scripts/tool.sh"), "#!/bin/sh\necho hi\n");
		write(srcBase.resolve("tests/test.txt"), "T");

		MergeWithTemplate.main(new String[] { "--src_base", srcBase.toString(), "--dst_base",
				dstBase.toString(), "--dirs", "scripts", "tests" });

		assertTrue(Files.exists(dstBase.resolve("scripts/tool.sh")));
		assertTrue(Files.exists(dstBase.resolve("tests/test.txt")));
	}
}
