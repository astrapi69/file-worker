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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import io.github.astrapi69.file.create.DirectoryFactory;
import io.github.astrapi69.file.create.DirectoryStructureFactory;
import io.github.astrapi69.file.create.DirectoryStructureTestData;
import io.github.astrapi69.file.create.FileContentInfo;
import io.github.astrapi69.file.delete.DeleteFileExtensions;
import io.github.astrapi69.file.search.PathFinder;

/**
 * The unit test class for the class {@link MergeDirectoryExtensions}
 */
public class MergeDirectoryExtensionsTest
{

	@Test
	public void testMerge_TwoDirectories_NoConflicts() throws IOException
	{
		// Setup
		File targetDir = Files.createTempDirectory("target").toFile();
		File dir1 = Files.createTempDirectory("dir1").toFile();
		File dir2 = Files.createTempDirectory("dir2").toFile();

		File file1Dir1 = new File(dir1, "file1.txt");
		FileUtils.writeStringToFile(file1Dir1, "content1", "UTF-8");

		File file1Dir2 = new File(dir2, "file2.txt");
		FileUtils.writeStringToFile(file1Dir2, "content2", "UTF-8");

		// Action
		MergeDirectoryExtensions.merge(targetDir, dir1, dir2);

		// Verification
		File expectedFile1 = new File(targetDir, "file1.txt");
		File expectedFile2 = new File(targetDir, "file2.txt");

		assertTrue(expectedFile1.exists(), "File1 should exist in target directory");
		assertTrue(expectedFile2.exists(), "File2 should exist in target directory");
		assertEquals("content1", FileUtils.readFileToString(expectedFile1, "UTF-8"));
		assertEquals("content2", FileUtils.readFileToString(expectedFile2, "UTF-8"));
	}

	@Test
	public void testMerge_TwoDirectories_WithConflicts() throws IOException, InterruptedException
	{
		// Setup
		File targetDir = Files.createTempDirectory("target").toFile();
		File dir1 = Files.createTempDirectory("dir1").toFile();
		File dir2 = Files.createTempDirectory("dir2").toFile();

		File file1Dir1 = new File(dir1, "file.txt");
		FileUtils.writeStringToFile(file1Dir1, "content1", "UTF-8");
		// Wait to ensure different modification times
		Thread.sleep(1000);
		File file1Dir2 = new File(dir2, "file.txt");
		FileUtils.writeStringToFile(file1Dir2, "content2", "UTF-8");

		// Action
		MergeDirectoryExtensions.merge(targetDir, dir1, dir2);

		// Verification
		File expectedFile = new File(targetDir, "file.txt");

		assertTrue(expectedFile.exists(), "File should exist in target directory");
		assertEquals("content2", FileUtils.readFileToString(expectedFile, "UTF-8"));
	}

	@Test
	public void testMerge_EmptyDirectoryArray() throws IOException
	{
		// Setup
		File targetDir = Files.createTempDirectory("target").toFile();

		// Action
		MergeDirectoryExtensions.merge(targetDir);

		// Verification
		assertEquals(0, targetDir.listFiles().length, "Target directory should be empty");
	}

	@Test
	public void testMerge_Directories_RecursiveFiles() throws IOException
	{
		// Setup
		File targetDir = Files.createTempDirectory("target").toFile();
		File dir1 = Files.createTempDirectory("dir1").toFile();

		File subDir1 = new File(dir1, "subdir");
		subDir1.mkdir();

		File file1SubDir1 = new File(subDir1, "file1.txt");
		FileUtils.writeStringToFile(file1SubDir1, "content1", "UTF-8");

		// Action
		MergeDirectoryExtensions.merge(targetDir, dir1);

		// Verification
		File expectedSubDir = new File(targetDir, "subdir");
		File expectedFile1 = new File(expectedSubDir, "file1.txt");

		assertTrue(expectedSubDir.exists(), "Subdirectory should exist in target directory");
		assertTrue(expectedFile1.exists(), "File should exist in subdirectory");
		assertEquals("content1", FileUtils.readFileToString(expectedFile1, "UTF-8"));
	}

	/**
	 * Test method for {@link MergeDirectoryExtensions#merge(File, File...)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 * @throws NoSuchAlgorithmException
	 *             is thrown if the directory all ready exists
	 * @throws NoSuchAlgorithmException
	 *             Is thrown if the algorithm is not supported or does not exist
	 */
	@Test
	public void testMergeDirectory() throws IOException, NoSuchAlgorithmException
	{
		Collection<File> leftSide;
		Collection<File> rightSide;
		Collection<FileContentInfo> fileInfos;
		String parentAbsolutePath;
		// new scenario...
		String absolutePath = PathFinder.getSrcTestResourcesDir().getAbsolutePath();
		File parentLeftFile = DirectoryFactory.newDirectory(absolutePath, "app");
		parentAbsolutePath = parentLeftFile.getAbsolutePath();
		fileInfos = DirectoryStructureTestData.newTestData(parentAbsolutePath);

		leftSide = DirectoryStructureFactory.newDirectoryStructure(fileInfos);

		File parentRightFile = DirectoryFactory.newDirectory(absolutePath, "other");
		parentAbsolutePath = parentRightFile.getAbsolutePath();
		fileInfos = DirectoryStructureTestData.newOtherTestData(parentAbsolutePath);
		rightSide = DirectoryStructureFactory.newDirectoryStructure(fileInfos);

		MergeDirectoryExtensions.merge(parentLeftFile, parentRightFile);

		// cleanup...
		leftSide.add(parentLeftFile);
		DeleteFileExtensions.delete(leftSide);
		rightSide.add(parentRightFile);
		DeleteFileExtensions.delete(rightSide);
	}


	/**
	 * Test method for {@link MergeDirectoryExtensions#merge(File, File...)} research case for merge
	 * strategies for instance: <br>
	 * <br>
	 * TargetMasterMergeStrategy will do: <br>
	 * <br>
	 * if exists in source, but not in target -> copy from source to target and delete from
	 * source<br>
	 * <br>
	 * if exists in target, but not in source -> do nothing<br>
	 * <br>
	 * if exists in both, but content not equal -> try to merge if fail copy from source to target
	 * and delete from source <br>
	 * if exists in both, and content is equal -> leave target unchanged and delete from source
	 *
	 * another strategy can leave source unchanged for historical issues SourceToTargetMergeStrategy
	 * will do: <br>
	 * if exists in source, but not in target -> copy from source to target <br>
	 * if exists in target, but not in source -> do nothing <br>
	 * if exists in both, but content not equal -> try to merge if fail copy from source to target
	 * <br>
	 * if exists in both, and content is equal -> leave target unchanged<br>
	 */
	@Test
	public void testAndSync()
	{

	}

}
