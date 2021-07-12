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
package io.github.astrapi69;

import static org.testng.AssertJUnit.assertTrue;

import java.io.File;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import io.github.astrapi69.create.FileCreationState;
import io.github.astrapi69.create.FileFactory;
import io.github.astrapi69.delete.DeleteFileExtensions;
import io.github.astrapi69.exceptions.DirectoryAlreadyExistsException;

/**
 * The abstract class FileTestCase is for tests in the package 'io.github.astrapi69'.
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public abstract class FileTestCase extends BaseTestCase
{

	/**
	 * The directory that is under the testDir. Current
	 * value:"projectpath"+"/src/test/resources"+"/resources"+"/testDir"+"/deepDir".
	 */
	protected File deepDir;

	/**
	 * The directory that is under the testDir. Current
	 * value:"projectpath"+"/src/test/resources"+"/resources"+"/testDir"+"/deepDir2".
	 */
	protected File deepDir2;

	/**
	 * The directory that is under the deepDir. Current value:"projectpath"+"/src/test/resources"
	 * +"/resources"+"/testDir"+"/deepDir"+"/deeperDir".
	 */
	protected File deeperDir;

	/** The File object for the current project path. */
	protected File projectPath;

	/**
	 * The directory for test resources. Current
	 * value:"projectpath"+"/src/test/resources"+"/resources".
	 */
	protected File resources;

	/**
	 * The directory for resources that after the test are deleted. A second directory for testing.
	 * Current value:"projectpath"+"/src/test/resources"+"/resources"+"/secondTestDir".
	 */
	protected File secondTestDir;

	/**
	 * The File object for the source folder for test. Current
	 * value:"projectpath"+"/src/test/resources".
	 */
	protected File test;

	/**
	 * The directory for resources that after the test are deleted. Current
	 * value:"projectpath"+"/src/test/resources"+"/resources"+"/testDir".
	 */
	protected File testDir;

	/** The File object for the test resources. */
	protected File testResources;

	/**
	 * The directory for unzipping zip files. Current
	 * value:"projectpath"+"/src/test/resources"+"/unzipDir".
	 */
	protected File unzipDir;

	/** The unzip dir test dir. */
	protected File unzipDirTestDir;

	/** The zip directory. Current value:"projectpath"+"/src/test/resources"+"/zipDir". */
	protected File zipDir;

	private void initDirs() throws DirectoryAlreadyExistsException
	{
		// Get the absolut path from the current project.
		final String absolutePath = FileExtensions.getCurrentAbsolutPathWithoutDotAndSlash();
		projectPath = new File(absolutePath);
		assertTrue("The directory " + this.projectPath.getAbsolutePath() + " should be created.",
			projectPath.exists());
		this.test = new File(projectPath.getAbsoluteFile(), "/src/test/resources");
		if (!this.test.exists())
		{
			final FileCreationState state = FileFactory.newDirectory(this.test);
			assertTrue("The directory " + this.test.getAbsolutePath() + " should be created.",
				state.equals(FileCreationState.CREATED));
		}
		this.zipDir = new File(this.test.getAbsoluteFile(), "zipDir");
		if (!this.zipDir.exists())
		{
			final FileCreationState state = FileFactory.newDirectory(this.zipDir);
			assertTrue("The directory " + this.zipDir.getAbsolutePath() + " should be created.",
				state.equals(FileCreationState.CREATED));
		}
		this.unzipDir = new File(this.test.getAbsoluteFile(), "unzipDir");
		if (!this.unzipDir.exists())
		{
			final FileCreationState state = FileFactory.newDirectory(this.unzipDir);
			assertTrue("The directory " + this.unzipDir.getAbsolutePath() + " should be created.",
				state.equals(FileCreationState.CREATED));
		}
		this.resources = new File(this.test.getAbsoluteFile(), "resources");
		if (!this.resources.exists())
		{
			final FileCreationState state = FileFactory.newDirectory(this.resources);
			assertTrue("The directory " + this.resources.getAbsolutePath() + " should be created.",
				state.equals(FileCreationState.CREATED));
		}
		this.testResources = new File(this.test.getAbsoluteFile(), "resources");
		if (!this.testResources.exists())
		{
			final FileCreationState state = FileFactory.newDirectory(this.testResources);
			assertTrue(
				"The directory " + this.testResources.getAbsolutePath() + " should be created.",
				state.equals(FileCreationState.CREATED));
		}
		this.testDir = new File(this.testResources.getAbsoluteFile(), "testDir");
		if (!this.testDir.exists())
		{
			final FileCreationState state = FileFactory.newDirectory(this.testDir);
			assertTrue("The directory " + this.testDir.getAbsolutePath() + " should be created.",
				state.equals(FileCreationState.CREATED));
		}
		this.secondTestDir = new File(this.testResources.getAbsoluteFile(), "secondTestDir");
		if (!secondTestDir.exists())
		{
			final FileCreationState state = FileFactory.newDirectory(this.secondTestDir);
			assertTrue(
				"The directory " + this.secondTestDir.getAbsolutePath() + " should be created.",
				state.equals(FileCreationState.CREATED));
		}
		this.deepDir = new File(this.testDir.getAbsoluteFile(), "deepDir");
		if (!this.deepDir.exists())
		{
			final FileCreationState state = FileFactory.newDirectory(this.deepDir);
			assertTrue("The directory " + this.deepDir.getAbsolutePath() + " should be created.",
				state.equals(FileCreationState.CREATED));

		}
		this.deepDir2 = new File(this.testDir.getAbsoluteFile(), "deepDir2");
		if (!this.deepDir2.exists())
		{
			final FileCreationState state = FileFactory.newDirectory(this.deepDir2);
			assertTrue("The directory " + this.deepDir2.getAbsolutePath() + " should be created.",
				state.equals(FileCreationState.CREATED));

		}
		this.deeperDir = new File(this.deepDir.getAbsoluteFile(), "deeperDir");
		if (!this.deeperDir.exists())
		{
			final FileCreationState state = FileFactory.newDirectory(this.deeperDir);
			assertTrue("The directory " + this.deeperDir.getAbsolutePath() + " should be created.",
				state.equals(FileCreationState.CREATED));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@BeforeMethod
	protected void setUp() throws Exception
	{
		super.setUp();
		initDirs();
	}

	/**
	 * Tear down.
	 *
	 * @throws Exception
	 *             the exception {@inheritDoc}
	 */
	@Override
	@AfterMethod
	protected void tearDown() throws Exception
	{
		super.tearDown();
		if (this.testDir.exists())
		{
			DeleteFileExtensions.delete(this.testDir);
		}
		if (this.secondTestDir.exists())
		{
			DeleteFileExtensions.delete(this.secondTestDir);
		}
		if (this.zipDir.exists())
		{
			DeleteFileExtensions.delete(this.zipDir);
		}
		if (this.unzipDir.exists())
		{
			DeleteFileExtensions.delete(this.unzipDir);
		}
	}
}
