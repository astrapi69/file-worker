package io.github.astrapi69.file.merge;

import io.github.astrapi69.file.create.DirectoryFactory;
import io.github.astrapi69.file.system.SystemFileExtensions;
import org.apache.commons.io.FileUtils;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

public class MergeDirectoryExtensions
{

	public static void copyDirectoryToDirectory(final File sourceDir, final File destinationDir)
		throws IOException
	{

		FileUtils.copyDirectoryToDirectory(sourceDir, destinationDir);
	}

	@Test
	public void testModifyFile() throws IOException
	{
		File sourceDir;
		File destinationDir;
		File userTmpDir = SystemFileExtensions.getUserTempDir();
		File userTempDir = SystemFileExtensions.getUserTempDir("temp");

		Collection<File> tmpContent = DirectoryFactory.newDirectoryStructure(userTmpDir, "content",
			"content/first", "content/first/deep", "content/second");
		Collection<File> tempContent = DirectoryFactory.newDirectoryStructure(userTempDir,
			"content", "content/first", "content/first/deep", "content/second");
		sourceDir = userTmpDir;
		destinationDir = userTempDir;
		FileUtils.copyDirectoryToDirectory(sourceDir, destinationDir);
	}

}
