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

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;

import io.github.astrapi69.file.create.DirectoryFactory;
import io.github.astrapi69.file.create.model.FileCreationState;
import io.github.astrapi69.file.exception.DirectoryAlreadyExistsException;
import io.github.astrapi69.test.base.BaseTestCase;

/**
 * The abstract class FileTestCase is for tests in the package 'io.github.astrapi69'.
 * <p>
 * Every test works in its own temporary directory {@link #tempRoot}: the setup copies the project's
 * {@code src/test/resources}, as it is on disk, to {@code tempRoot/src/test/resources} and creates
 * the test directories below that copy, so that no test writes into the source tree (#99). The
 * scratch directories {@code zipDir}, {@code unzipDir}, {@code resources/testDir} and
 * {@code resources/secondTestDir} hold no committed file and are not copied: they start empty in
 * every test, as they did when tearDown deleted them, even where a file was left behind in the
 * source tree. The project directory {@link #projectPath} may be read, but must not be written.
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public abstract class FileTestCase extends BaseTestCase
{

	/**
	 * The directory that is under the testDir. Current
	 * value:"tempRoot"+"/src/test/resources"+"/resources"+"/testDir"+"/deepDir".
	 */
	protected File deepDir;

	/**
	 * The directory that is under the testDir. Current
	 * value:"tempRoot"+"/src/test/resources"+"/resources"+"/testDir"+"/deepDir2".
	 */
	protected File deepDir2;

	/**
	 * The directory that is under the deepDir. Current value:"tempRoot"+"/src/test/resources"
	 * +"/resources"+"/testDir"+"/deepDir"+"/deeperDir".
	 */
	protected File deeperDir;

	/**
	 * The File object for the current project path. Tests may read from it, but must not write into
	 * it.
	 */
	protected File projectPath;

	/**
	 * The directory for test resources. Current
	 * value:"tempRoot"+"/src/test/resources"+"/resources".
	 */
	protected File resources;

	/**
	 * The directory for resources that after the test are deleted. A second directory for testing.
	 * Current value:"tempRoot"+"/src/test/resources"+"/resources"+"/secondTestDir".
	 */
	protected File secondTestDir;

	/**
	 * The temporary root directory of the current test, created by JUnit for every test and deleted
	 * after it. It holds a copy of the project's {@code src/test/resources} with the same relative
	 * layout, without the scratch directories.
	 */
	@TempDir
	protected Path tempRoot;

	/**
	 * The directories below {@code src/test/resources} that the tests use as scratch space. None of
	 * them holds a committed file, so a file found there was left behind by an earlier run; the
	 * copy leaves them out, so that every test starts with them empty.
	 */
	private static final List<Path> SCRATCH_DIRECTORIES = List.of(Path.of("zipDir"),
		Path.of("unzipDir"), Path.of("resources", "testDir"),
		Path.of("resources", "secondTestDir"));

	/**
	 * The File object for the copy of the source folder for test. Current
	 * value:"tempRoot"+"/src/test/resources".
	 */
	protected File test;

	/**
	 * The directory for resources that after the test are deleted. Current
	 * value:"tempRoot"+"/src/test/resources"+"/resources"+"/testDir".
	 */
	protected File testDir;

	/**
	 * The File object for the test resources. Current
	 * value:"tempRoot"+"/src/test/resources"+"/resources".
	 */
	protected File testResources;

	/**
	 * The directory for unzipping zip files. Current
	 * value:"tempRoot"+"/src/test/resources"+"/unzipDir".
	 */
	protected File unzipDir;

	/** The unzip dir test dir. */
	protected File unzipDirTestDir;

	/** The zip directory. Current value:"tempRoot"+"/src/test/resources"+"/zipDir". */
	protected File zipDir;

	/**
	 * Copies the directory tree below the given source directory into the given target directory.
	 * The copies get the default permissions of new files, not the ones of the originals, so that a
	 * write-protected source tree gives writable copies.
	 *
	 * @param source
	 *            the directory to copy
	 * @param target
	 *            the directory that receives the copy
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	private static void copyDirectoryTree(final Path source, final Path target) throws IOException
	{
		try (Stream<Path> paths = Files.walk(source))
		{
			final Iterator<Path> iterator = paths.iterator();
			while (iterator.hasNext())
			{
				final Path path = iterator.next();
				final Path relative = source.relativize(path);
				if (SCRATCH_DIRECTORIES.stream().anyMatch(relative::startsWith))
				{
					continue;
				}
				final Path destination = target.resolve(relative.toString());
				if (Files.isDirectory(path))
				{
					Files.createDirectories(destination);
				}
				else
				{
					try (InputStream inputStream = Files.newInputStream(path))
					{
						Files.copy(inputStream, destination);
					}
				}
			}
		}
	}

	private void initDirs() throws DirectoryAlreadyExistsException, IOException
	{
		// Get the absolut path from the current project.
		final String absolutePath = FileExtensions.getCurrentAbsolutPathWithoutDotAndSlash();
		projectPath = new File(absolutePath);
		assertTrue(projectPath.exists(),
			"The directory " + this.projectPath.getAbsolutePath() + " should be created.");
		// copy the test resources into the temporary root, so that the tests work on the copy and
		// never write into the source tree
		final Path projectTestResources = projectPath.toPath().resolve("src/test/resources");
		final Path copiedTestResources = tempRoot.resolve("src/test/resources");
		Files.createDirectories(copiedTestResources.getParent());
		if (Files.isDirectory(projectTestResources))
		{
			copyDirectoryTree(projectTestResources, copiedTestResources);
		}
		this.test = copiedTestResources.toFile();
		if (!this.test.exists())
		{
			final FileCreationState state = DirectoryFactory.newDirectory(this.test);
			assertTrue(state.equals(FileCreationState.CREATED),
				"The directory " + this.test.getAbsolutePath() + " should be created.");
		}
		this.zipDir = new File(this.test.getAbsoluteFile(), "zipDir");
		if (!this.zipDir.exists())
		{
			final FileCreationState state = DirectoryFactory.newDirectory(this.zipDir);
			assertTrue(state.equals(FileCreationState.CREATED),
				"The directory " + this.zipDir.getAbsolutePath() + " should be created.");
		}
		this.unzipDir = new File(this.test.getAbsoluteFile(), "unzipDir");
		if (!this.unzipDir.exists())
		{
			final FileCreationState state = DirectoryFactory.newDirectory(this.unzipDir);
			assertTrue(state.equals(FileCreationState.CREATED),
				"The directory " + this.unzipDir.getAbsolutePath() + " should be created.");
		}
		this.resources = new File(this.test.getAbsoluteFile(), "resources");
		if (!this.resources.exists())
		{
			final FileCreationState state = DirectoryFactory.newDirectory(this.resources);
			assertTrue(state.equals(FileCreationState.CREATED),
				"The directory " + this.resources.getAbsolutePath() + " should be created.");
		}
		this.testResources = new File(this.test.getAbsoluteFile(), "resources");
		if (!this.testResources.exists())
		{
			final FileCreationState state = DirectoryFactory.newDirectory(this.testResources);
			assertTrue(

				state.equals(FileCreationState.CREATED),
				"The directory " + this.testResources.getAbsolutePath() + " should be created.");
		}
		this.testDir = new File(this.testResources.getAbsoluteFile(), "testDir");
		if (!this.testDir.exists())
		{
			final FileCreationState state = DirectoryFactory.newDirectory(this.testDir);
			assertTrue(state.equals(FileCreationState.CREATED),
				"The directory " + this.testDir.getAbsolutePath() + " should be created.");
		}
		this.secondTestDir = new File(this.testResources.getAbsoluteFile(), "secondTestDir");
		if (!secondTestDir.exists())
		{
			final FileCreationState state = DirectoryFactory.newDirectory(this.secondTestDir);
			assertTrue(

				state.equals(FileCreationState.CREATED),
				"The directory " + this.secondTestDir.getAbsolutePath() + " should be created.");
		}
		this.deepDir = new File(this.testDir.getAbsoluteFile(), "deepDir");
		if (!this.deepDir.exists())
		{
			final FileCreationState state = DirectoryFactory.newDirectory(this.deepDir);
			assertTrue(state.equals(FileCreationState.CREATED),
				"The directory " + this.deepDir.getAbsolutePath() + " should be created.");

		}
		this.deepDir2 = new File(this.testDir.getAbsoluteFile(), "deepDir2");
		if (!this.deepDir2.exists())
		{
			final FileCreationState state = DirectoryFactory.newDirectory(this.deepDir2);
			assertTrue(state.equals(FileCreationState.CREATED),
				"The directory " + this.deepDir2.getAbsolutePath() + " should be created.");

		}
		this.deeperDir = new File(this.deepDir.getAbsoluteFile(), "deeperDir");
		if (!this.deeperDir.exists())
		{
			final FileCreationState state = DirectoryFactory.newDirectory(this.deeperDir);
			assertTrue(state.equals(FileCreationState.CREATED),
				"The directory " + this.deeperDir.getAbsolutePath() + " should be created.");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@BeforeEach
	protected void setUp() throws Exception
	{
		super.setUp();
		initDirs();
	}

	/**
	 * Teardown method will be invoked after every unit test method in this class. The directories
	 * of the test live below {@link #tempRoot}, which JUnit deletes after the test.
	 *
	 * @throws Exception
	 *             the exception {@inheritDoc}
	 */
	@Override
	@AfterEach
	protected void tearDown() throws Exception
	{
		super.tearDown();
	}
}
