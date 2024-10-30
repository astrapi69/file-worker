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
package io.github.astrapi69.file.process;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import io.github.astrapi69.file.create.DirectoryFactory;
import io.github.astrapi69.file.create.model.FileCreationState;
import io.github.astrapi69.file.system.SystemFileExtensions;
import io.github.astrapi69.io.file.FileExtension;
import io.github.astrapi69.zip4j.Zip4jExtensions;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public class ProcessTakeOutDirectoryTest
{


	/**
	 * Test method for process the 'takeout-*' folders from google ## Process flow
	 *
	 * 1. create temp folder for the output from the downloaded files if not exists create one. 2.
	 * Download zip file from google drive 2. run unit test for process 3. in the unit test extract
	 * the zip file in the temp folder 4. Remove all duplicated files and google related
	 * configuration files 5. pack all files again in zip files 6. upload all zip files to google
	 * drive
	 *
	 * # course of action
	 */
	@Test
	@Disabled
	public void testProcess() throws ZipException
	{
		File takeOutDir;
		File outputDir;
		List<File> actual;
		String fileSearchPattern;
		String zipFileNameWithExtension;
		String zipFileName;
		File userDownloadsDir;
		File zipFile;
		String zipExtension;

		userDownloadsDir = SystemFileExtensions.getUserDownloadsDir();
		if (!SystemFileExtensions.getUserTempDir().exists())
		{
			FileCreationState fileCreationState = DirectoryFactory
				.newDirectory(SystemFileExtensions.getUserTempDir());
			System.out.println(fileCreationState);
		}
		outputDir = new File(SystemFileExtensions.getUserTempDir(), "output");
		if (!outputDir.exists())
		{
			FileCreationState fileCreationState = DirectoryFactory.newDirectory(outputDir);
			System.out.println(fileCreationState);
		}

		zipExtension = FileExtension.ZIP.getExtension();
		zipFileName = "takeout-20230616T214913Z-016";
		zipFileNameWithExtension = zipFileName + zipExtension;
		System.err.println("extracting " + zipFileNameWithExtension);

		zipFile = new File(userDownloadsDir, zipFileNameWithExtension);

		final ZipFile zip4jZipFile = new ZipFile(zipFile);

		// Test to extract...
		Zip4jExtensions.extract(zip4jZipFile, outputDir, null);
	}
}
