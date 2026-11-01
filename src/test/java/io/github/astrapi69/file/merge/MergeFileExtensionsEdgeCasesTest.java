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
package io.github.astrapi69.file.merge;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import io.github.astrapi69.file.merge.strategy.MergeFileStrategy;

/**
 * Tests the edge cases of {@link MergeFileExtensions} with files built in a temporary directory:
 * empty files, a missing or changed header, blank lines and lines with fewer columns than the key
 * index.
 */
public class MergeFileExtensionsEdgeCasesTest
{

	@TempDir
	Path tempDir;

	static Stream<Arguments> csvHeaderCases()
	{
		return Stream.of(
			Arguments.of("a repeated header is dropped", List.of("id,name\n1,A", "id,name\n2,B"),
				List.of("id,name", "1,A", "2,B")),
			Arguments.of("a file without the header keeps its first line",
				List.of("id,name\n1,A", "2,B\n3,C"), List.of("id,name", "1,A", "2,B", "3,C")),
			Arguments.of("an empty first file does not set the header",
				List.of("", "id,name\n1,A", "id,name\n2,B"), List.of("id,name", "1,A", "2,B")),
			Arguments.of("the header is compared without surrounding blanks",
				List.of("id,name\n1,A", " id,name \n2,B"), List.of("id,name", "1,A", "2,B")));
	}

	static Stream<Arguments> markdownCases()
	{
		return Stream.of(
			Arguments.of("a rule between two files", List.of("# A", "# B"),
				List.of("# A", "", "---", "", "# B")),
			Arguments.of("an empty file adds no rule", List.of("# A", "", "# B"),
				List.of("# A", "", "---", "", "# B")),
			Arguments.of("an empty first file adds no leading rule", List.of("", "# A\ntext"),
				List.of("# A", "text")),
			Arguments.of("a single file gets no rule", List.of("# A\ntext"),
				List.of("# A", "text")));
	}

	static Stream<Arguments> byKeyCases()
	{
		return Stream.of(
			Arguments.of("the last line of a key wins, in the order of the first",
				List.of("1;Alice;Admin\n2;Bob;User", "1;Alice;Root"), 0, ";",
				List.of("1;Alice;Root", "2;Bob;User")),
			Arguments.of("blank lines are skipped", List.of("a,1\n\n   \nb,2", ""), 0, ",",
				List.of("a,1", "b,2")),
			Arguments.of("a line with fewer columns is its own key",
				List.of("x,y,K1\nsolo", "z,w,K1\nsolo\nother"), 2, ",",
				List.of("z,w,K1", "solo", "other")),
			Arguments.of("the key is compared without surrounding blanks",
				List.of("k1 ,a", " k1,b"), 0, ",", List.of(" k1,b")),
			Arguments.of("a pipe delimiter is a plain string, not a regular expression",
				List.of("1|Alice\n2|Bob"), 1, "|", List.of("1|Alice", "2|Bob")),
			Arguments.of("a dot delimiter is a plain string, not a regular expression",
				List.of("1.Alice\n2.Bob"), 0, ".", List.of("1.Alice", "2.Bob")),
			Arguments.of("a delimiter that is no valid regular expression splits as well",
				List.of("1[Alice\n1[Root\n2[Bob"), 0, "[", List.of("1[Root", "2[Bob")));
	}

	private List<File> writeFiles(List<String> contents) throws IOException
	{
		List<File> files = new ArrayList<>();
		for (int index = 0; index < contents.size(); index++)
		{
			Path file = tempDir.resolve("input-" + index + ".txt");
			Files.writeString(file, contents.get(index));
			files.add(file.toFile());
		}
		return files;
	}

	@Test
	void mergeList_writesTheLinesOfAllFilesInOrder() throws IOException
	{
		List<File> files = writeFiles(List.of("a\nb", "c"));
		File targetFile = tempDir.resolve("merged.txt").toFile();

		MergeFileExtensions.merge(files, targetFile, MergeFileStrategy.APPEND);

		assertEquals(List.of("a", "b", "c"), Files.readAllLines(targetFile.toPath()));
	}

	@Test
	void mergeByKey_throwsIllegalArgumentException_whenTheKeyIndexIsNegative() throws IOException
	{
		List<File> files = writeFiles(List.of("1,a"));
		File targetFile = tempDir.resolve("merged.txt").toFile();

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
			() -> MergeFileExtensions.mergeByKey(files, targetFile, -1, ","));

		assertEquals("keyIndex must be >= 0", exception.getMessage());
		assertFalse(targetFile.exists(), "nothing is written");
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("csvHeaderCases")
	void mergeCsvHeader_keepsTheHeaderOfTheFirstFileOnly(String displayName, List<String> contents,
		List<String> expectedLines) throws IOException
	{
		File targetFile = tempDir.resolve("merged.csv").toFile();

		MergeFileExtensions.mergeAndGet(writeFiles(contents), targetFile,
			MergeFileStrategy.CSV_HEADER_MERGE);

		assertEquals(expectedLines, Files.readAllLines(targetFile.toPath()));
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("markdownCases")
	void mergeMarkdownSections_separatesTheFilesWithARule(String displayName, List<String> contents,
		List<String> expectedLines) throws IOException
	{
		File targetFile = tempDir.resolve("merged.md").toFile();

		MergeFileExtensions.mergeAndGet(writeFiles(contents), targetFile,
			MergeFileStrategy.MARKDOWN_SECTIONS);

		assertEquals(expectedLines, Files.readAllLines(targetFile.toPath()));
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("byKeyCases")
	void mergeByKey_keepsOneLinePerKey(String displayName, List<String> contents, int keyIndex,
		String delimiter, List<String> expectedLines) throws IOException
	{
		File targetFile = tempDir.resolve("merged.txt").toFile();

		File merged = MergeFileExtensions.mergeByKey(writeFiles(contents), targetFile, keyIndex,
			delimiter);

		assertEquals(targetFile, merged);
		assertEquals(expectedLines, Files.readAllLines(targetFile.toPath()));
	}
}
