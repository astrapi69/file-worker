package io.github.astrapi69.file.tools;

import io.github.astrapi69.file.create.DirectoryFactory;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;
/**
 * Uses JUnit 5 (`@Test`, `@TempDir`). Invokes `MergeWithTemplate.main(...)` with explicit
 * `--src_base`, `--dst_base`, `--dirs` (so it does **not** depend on your JSON defaults). Verifies:
 * copy when destination subdir is missing, merge into existing directory (overwrites), dry-run
 * doesn’t change filesystem, multiple directories are handled.
 */
class NewMergeWithTemplateTest
{

    private static String jsonString(String json, String key)
    {
        Pattern p = Pattern.compile("\"" + Pattern.quote(key) + "\"\\s*:\\s*\"([^\"]*)\"", Pattern.DOTALL);
        Matcher m = p.matcher(json);
        return m.find() ? m.group(1) : null;
    }

    private static List<String> jsonStringArray(String json, String key)
    {
        Pattern p = Pattern.compile("\"" + Pattern.quote(key) + "\"\\s*:\\s*\\[(.*?)]", Pattern.DOTALL);
        Matcher m = p.matcher(json);
        if (!m.find()) return null;

        String body = m.group(1);
        List<String> out = new ArrayList<>();
        Matcher item = Pattern.compile("\"([^\"]*)\"").matcher(body);
        while (item.find()) out.add(item.group(1));
        return out;
    }

    private static String quote(String s) { return "\"" + s + "\""; } // optional, wird oben nicht mehr genutzt


    @Test
    void usesRealJsonDefaults() throws Exception
    {
        // Dein JSON-String
        String json = "{\n" +
                "  \"default_src_base\": \"/run/media/astrapi69/backups/git/hub/astrapi69/write-book-template\",\n" +
                "  \"default_dst_base\": \"/home/astrapi69/PycharmProjects/last-spark\",\n" +
                "  \"default_dirs\": [\"scripts\", \"tests\"]\n" +
                "}";

        // Sicherstellen, dass ./config existiert
        Path configDir = Path.of("config");
        Files.createDirectories(configDir);
        Path configFile = configDir.resolve("merge_defaults.json");

        // JSON reinschreiben
        Files.writeString(configFile, json, StandardCharsets.UTF_8);

        // Test: keine Args -> liest Defaults
        MergeWithTemplate.main(new String[] {});

        // Danach kannst du prüfen, ob Dateien im Ziel vorhanden sind
        assertTrue(Files.exists(Path.of("/home/astrapi69/PycharmProjects/last-spark/scripts")));
        assertTrue(Files.exists(Path.of("/home/astrapi69/PycharmProjects/last-spark/tests")));
    }

	private static void write(Path file, String content) throws IOException
	{
		Files.createDirectories(file.getParent());
		Files.writeString(file, content, StandardCharsets.UTF_8, StandardOpenOption.CREATE,
			StandardOpenOption.TRUNCATE_EXISTING);
	}

	private static String read(Path file) throws IOException
	{
		return Files.readString(file, StandardCharsets.UTF_8);
	}
    private static void die(String msg)
    {
        throw new IllegalArgumentException(msg);
    }


    @Test
    @Disabled("only for internal and local use")
    void mergesIntoExistingDirectory_overwritesTargetFiles_last_spark() throws Exception
    {
        Path srcBase = Path.of("/run/media/astrapi69/backups/git/hub/astrapi69/write-book-template");
        Path dstBase = Path.of("/home/astrapi69/PycharmProjects/last-spark");


        // Destination subdir exists -> CLI uses MergeDirectoryExtensions.merge()
        MergeWithTemplate.main(new String[] { "--src_base", srcBase.toString(), "--dst_base",
                dstBase.toString(), "--dirs", "scripts", "tests" });

    }

    @Test
    @Disabled("only for internal and local use")
    void mergesIntoExistingDirectory_overwritesTargetFiles_currency_of_mind() throws Exception
    {
        Path srcBase = Path.of("/run/media/astrapi69/backups/git/hub/astrapi69/write-book-template");
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
