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
package io.github.astrapi69.file.create;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.io.TempDir;

import io.github.astrapi69.collection.CollectionExtensions;
import io.github.astrapi69.file.create.model.DirectoryStructureTestData;
import io.github.astrapi69.file.create.model.FileContentInfo;
import io.github.astrapi69.file.delete.DeleteFileExtensions;

/**
 * The unit test class for the class {@link DirectoryStructureExtensions}
 */
public class DirectoryStructureExtensionsTest
{

	/**
	 * The temporary directory in which the directory structure is created
	 */
	@TempDir
	File tempDir;

	/**
	 * Test method for {@link DirectoryStructureExtensions#getFileContentInfos(File)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testGetFileContentInfos() throws IOException, NoSuchAlgorithmException
	{
		List<FileContentInfo> actual;
		List<FileContentInfo> expected;
		String parentAbsolutePath;
		// new scenario...

		File parentFile = DirectoryFactory.newDirectory(tempDir, "app");
		parentAbsolutePath = parentFile.getAbsolutePath();
		actual = DirectoryStructureTestData.newTestData(parentAbsolutePath);
		Collection<File> files = DirectoryStructureFactory.newDirectoryStructure(actual);
		expected = DirectoryStructureExtensions.getFileContentInfos(parentFile);
		assertTrue(CollectionExtensions.isEqualCollection(actual, expected));
		DeleteFileExtensions.delete(files);
	}

	/**
	 * Test method for {@link DirectoryStructureExtensions#getFileContentInfos(File)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void getFileContentInfos_returnsAnEmptyListTheCallerCanAddTo_whenGivenARegularFile()
		throws IOException
	{
		File regularFile = Files.writeString(tempDir.toPath().resolve("regular.txt"), "regular")
			.toFile();
		FileContentInfo added = FileContentInfo.builder().name("added.txt").build();

		List<FileContentInfo> fileContentInfos = DirectoryStructureExtensions
			.getFileContentInfos(regularFile);

		assertEquals(List.of(), fileContentInfos);
		fileContentInfos.add(added);
		assertEquals(List.of(added), fileContentInfos);
	}

	/**
	 * Test method for {@link DirectoryStructureExtensions#getFileContentInfos(File)} and
	 * {@link DirectoryStructureFactory#newDirectoryStructure(Collection)}: a dangling symbolic link
	 * is neither a regular file nor a directory, so the walk lists no entry for it, while the
	 * regular files and the directories keep theirs, and the tree can be created again from the
	 * entries in another directory
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	@DisabledOnOs(OS.WINDOWS)
	public void getFileContentInfos_listsNoEntryForADanglingLink_andTheTreeCanBeCreatedAgain()
		throws IOException
	{
		File source = DirectoryFactory.newDirectory(tempDir, "source");
		File target = DirectoryFactory.newDirectory(tempDir, "target");
		Files.writeString(source.toPath().resolve("a.txt"), "a");
		File subdirectory = DirectoryFactory.newDirectory(source, "sub");
		Files.writeString(subdirectory.toPath().resolve("b.txt"), "b");
		Files.createSymbolicLink(source.toPath().resolve("dangling"),
			source.toPath().resolve("missing-target"));

		List<FileContentInfo> fileContentInfos = DirectoryStructureExtensions
			.getFileContentInfos(source);

		Map<String, Boolean> directoryByName = fileContentInfos.stream()
			.collect(Collectors.toMap(FileContentInfo::getName, FileContentInfo::isDirectory));
		assertEquals(Map.of("a.txt", false, "sub", true, "b.txt", false), directoryByName);

		fileContentInfos.forEach(fileContentInfo -> fileContentInfo.setPath(
			fileContentInfo.getPath().replace(source.getAbsolutePath(), target.getAbsolutePath())));
		DirectoryStructureFactory.newDirectoryStructure(fileContentInfos);

		assertEquals("a", Files.readString(target.toPath().resolve("a.txt")));
		assertEquals("b", Files.readString(target.toPath().resolve("sub").resolve("b.txt")));
		assertFalse(Files.exists(target.toPath().resolve("dangling"), LinkOption.NOFOLLOW_LINKS));
	}
}
