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
package io.github.astrapi69.file;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import io.github.astrapi69.file.compare.CompareFileExtensions;
import io.github.astrapi69.file.compare.api.IFileContentResultBean;
import io.github.astrapi69.file.copy.CopyDirectoryExtensions;
import io.github.astrapi69.file.copy.CopyFileExtensions;
import io.github.astrapi69.file.create.DirectoryFactory;
import io.github.astrapi69.file.create.FileFactory;
import io.github.astrapi69.file.create.model.FileCreationState;
import io.github.astrapi69.file.delete.DeleteFileExtensions;
import io.github.astrapi69.file.merge.MergeFileExtensions;
import io.github.astrapi69.file.merge.strategy.MergeFileStrategy;
import io.github.astrapi69.file.modify.DeleteLinesByIndexInFile;
import io.github.astrapi69.file.modify.ModifyFileExtensions;
import io.github.astrapi69.file.read.ReadFileExtensions;
import io.github.astrapi69.file.rename.RenameFileExtensions;
import io.github.astrapi69.file.search.FileSearchExtensions;
import io.github.astrapi69.file.write.LineAppender;
import io.github.astrapi69.file.write.StoreFileExtensions;
import io.github.astrapi69.io.file.filter.SuffixFileFilter;

/**
 * The examples of the README's "Quick start", one test for each block: the code between the markers
 * is the README's, and the assertions are the values its comments name. A change to either side has
 * to be made on both
 */
class ReadmeExamplesTest
{

	@TempDir
	File directory;

	/**
	 * Writes the given lines, each with the line end of the platform, into the file with the given
	 * path below the temporary directory, and creates its missing parent directories
	 */
	private File file(final String path, final String... lines) throws IOException
	{
		final Path file = directory.toPath().resolve(path);
		Files.createDirectories(file.getParent());
		Files.write(file, List.of(lines), StandardCharsets.UTF_8);
		return file.toFile();
	}

	/**
	 * The paths of the given files relative to the given directory, with "/" as separator, sorted
	 */
	private static List<String> pathsOf(final File base, final Collection<File> files)
	{
		return files.stream().map(file -> base.toPath().relativize(file.toPath()).toString())
			.map(path -> path.replace(File.separatorChar, '/')).sorted().toList();
	}

	/**
	 * The lines of the given file, read as UTF-8
	 */
	private static List<String> linesOf(final File file) throws IOException
	{
		return Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
	}

	@Test
	void creatingFilesAndDirectories() throws IOException
	{
		// README: begin
		File summary = new File(directory, "reports/2026/summary.txt");
		FileCreationState created = FileFactory.newFile(summary); // CREATED, with reports/2026
		FileCreationState again = FileFactory.newFile(summary); // ALREADY_EXISTS
		File archive = new File(directory, "archive");
		FileCreationState made = DirectoryFactory.newDirectory(archive); // CREATED
		// README: end

		assertEquals(FileCreationState.CREATED, created);
		assertTrue(summary.isFile());
		assertTrue(new File(directory, "reports/2026").isDirectory());
		assertEquals(FileCreationState.ALREADY_EXISTS, again);
		assertEquals(FileCreationState.CREATED, made);
		assertTrue(archive.isDirectory());
	}

	@Test
	void writingAndReading() throws IOException
	{
		// README: begin
		File names = new File(directory, "names.txt");
		StoreFileExtensions.toFile(names, List.of("Ada", "Grace"), StandardCharsets.UTF_8);
		LineAppender.appendLines(names, "Linus");
		List<String> lines = ReadFileExtensions.readLinesInList(names); // [Ada, Grace, Linus]
		String second = ReadFileExtensions.readLine(names, 1); // "Grace"
		String text = ReadFileExtensions.fromFile(names); // the whole file
		// README: end

		assertEquals(List.of("Ada", "Grace", "Linus"), lines);
		assertEquals("Grace", second);
		final String lineEnd = System.lineSeparator();
		assertEquals("Ada" + lineEnd + "Grace" + lineEnd + "Linus" + lineEnd, text);
	}

	@Test
	void copyingFilesAndDirectoryTrees() throws Exception
	{
		File project = new File(directory, "project");
		file("project/README.md", "# project");
		file("project/src/Main.java", "class Main {}");
		file("project/src/Main.class", "not really a class file");

		// README: begin
		File copy = new File(directory, "copy");
		boolean copied = CopyDirectoryExtensions.copyDirectoryWithFileFilter(project, copy, null,
			SuffixFileFilter.of(".class", false), true); // true: README.md and src/Main.java
		Path backup = CopyFileExtensions.newBackupOf(project.toPath().resolve("README.md"));
		// README: end

		assertTrue(copied);
		assertEquals(List.of("README.md", "src/Main.java"),
			pathsOf(copy, FileSearchExtensions.getAllFilesFromDirRecursive(copy)));
		assertEquals(project.toPath().resolve("README.md.bak"), backup);
		assertEquals(linesOf(new File(project, "README.md")), linesOf(backup.toFile()));
	}

	@Test
	void renamingMovingAndDeleting() throws Exception
	{
		File notes = new File(directory, "notes");
		file("notes/a.txt", "a");
		file("notes/old/b.txt", "b");

		// README: begin
		List<File> failed = RenameFileExtensions.changeAllFilenameSuffix(notes, ".txt", ".md"); // []
		// README: end

		assertEquals(List.of(), failed);
		assertEquals(List.of("a.md", "old/b.md"),
			pathsOf(notes, FileSearchExtensions.getAllFilesFromDirRecursive(notes)));

		// README: begin
		File a = new File(notes, "a.md");
		RenameFileExtensions.moveFile(a, new File(notes, "old/a.md")); // old/a.md
		boolean deleted = DeleteFileExtensions.delete(new File(notes, "old")); // true
		// README: end

		assertTrue(deleted);
		assertFalse(new File(notes, "old").exists());
		assertEquals(List.of(), List.of(notes.list()));
	}

	@Test
	void searching() throws IOException
	{
		file("report.txt", "r");
		file("report.log", "l");
		file("archive/report-2025.txt", "a");

		// README: begin
		List<File> texts = FileSearchExtensions.findFilesRecursive(directory, "*.txt");
		List<File> reports = FileSearchExtensions.findFiles(directory, "report.*");
		Set<File> logs = FileSearchExtensions.findFilesRecursive(directory,
			file -> file.getName().endsWith(".log"));
		// README: end

		assertEquals(List.of("archive/report-2025.txt", "report.txt"), pathsOf(directory, texts));
		assertEquals(List.of("report.log", "report.txt"), pathsOf(directory, reports));
		assertEquals(List.of("report.log"), pathsOf(directory, logs));
	}

	@Test
	void comparing() throws IOException
	{
		File a = file("a.txt", "Ada", "Grace");
		File b = file("b.txt", "Ada", "Grace");

		// README: begin
		IFileContentResultBean result = CompareFileExtensions.compareFileContentByBytes(a, b);
		boolean sameContent = result.getContentEquality(); // true
		boolean sameName = result.getNameEquality(); // false
		boolean equal = CompareFileExtensions.compareFiles(a, b, true); // true
		// README: end

		assertTrue(sameContent);
		assertFalse(sameName);
		assertTrue(equal);
	}

	@Test
	void merging() throws IOException
	{
		File en = file("en.txt", "yes", "hello");
		File de = file("de.txt", "yes", "hallo");
		File y2025 = file("2025.csv", "id,name", "1,Ada");
		File y2026 = file("2026.csv", "id,name", "2,Grace");

		// README: begin
		File words = new File(directory, "words.txt");
		MergeFileExtensions.merge(List.of(en, de), words, MergeFileStrategy.SORTED_UNIQUE);
		File table = new File(directory, "all.csv");
		MergeFileExtensions.merge(List.of(y2025, y2026), table, MergeFileStrategy.CSV_HEADER_MERGE);
		// README: end

		assertEquals(List.of("hallo", "hello", "yes"), linesOf(words));
		assertEquals(List.of("id,name", "1,Ada", "2,Grace"), linesOf(table));
	}

	@Test
	void changingAFileLineByLine() throws IOException
	{
		Path todo = file("todo.txt", "one", "two", "three").toPath();

		// README: begin
		ModifyFileExtensions.modifyFile(todo, new DeleteLinesByIndexInFile(List.of(1)));
		// README: end

		assertEquals(List.of("one", "three"), linesOf(todo.toFile()));

		// README: begin
		ModifyFileExtensions.modifyFile(todo,
			(index, line) -> line.toUpperCase() + System.lineSeparator());
		// README: end

		assertEquals(List.of("ONE", "THREE"), linesOf(todo.toFile()));
	}
}
