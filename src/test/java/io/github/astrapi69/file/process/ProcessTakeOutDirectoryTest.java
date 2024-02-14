package io.github.astrapi69.file.process;

import io.github.astrapi69.file.create.DirectoryFactory;
import io.github.astrapi69.file.create.FileCreationState;
import io.github.astrapi69.file.search.FileSearchExtensions;
import io.github.astrapi69.file.system.SystemFileExtensions;
import io.github.astrapi69.zip4j.Zip4jExtensions;
import net.lingala.zip4j.exception.ZipException;
import org.testng.annotations.Test;

import java.io.File;
import java.util.List;
import net.lingala.zip4j.ZipFile;

public class ProcessTakeOutDirectoryTest
{


	/**
	 * Test method for process the 'takeout-*' folders from google
	 */
	@Test(enabled = false)
	public void testProcess() throws ZipException {
		File takeOutDir;
		File outputDir;
		List<File> actual;
		String fileSearchPattern;

		if(!SystemFileExtensions.getUserTempDir().exists()) {
			FileCreationState fileCreationState = DirectoryFactory.newDirectory(SystemFileExtensions.getUserTempDir());
			System.out.println(fileCreationState);
		}
		outputDir = new File(SystemFileExtensions.getUserTempDir(), "output");
		FileCreationState fileCreationState = DirectoryFactory.newDirectory(outputDir);
		System.out.println(fileCreationState);
//		takeOutDir = new File(SystemFileExtensions.getUserDownloadsDir(), "takeout-20230618T035040Z-201");
//		actual = FileSearchExtensions.findAllFiles(takeOutDir, ".*json");
//		System.out.println(actual);

		final File zipFile = new File(SystemFileExtensions.getUserDownloadsDir(), "takeout-20230618T035040Z-097.zip");

		final ZipFile zip4jZipFile = new ZipFile(zipFile);

		// Test to extract...
		Zip4jExtensions.extract(zip4jZipFile, outputDir, null);
	}
}
