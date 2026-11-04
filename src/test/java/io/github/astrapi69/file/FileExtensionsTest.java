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
import static org.junit.jupiter.params.provider.Arguments.argumentSet;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.meanbean.test.BeanTester;

import io.github.astrapi69.file.write.StoreFileExtensions;

/**
 * The unit test class for the class {@link FileExtensions}.
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public class FileExtensionsTest extends FileTestCase
{

	/**
	 * Sets up method will be invoked before every unit test method in this class.
	 *
	 * @throws Exception
	 *             is thrown if an exception occurs
	 */
	@Override
	@BeforeEach
	protected void setUp() throws Exception
	{
		super.setUp();
	}

	/**
	 * Teardown method will be invoked after every unit test method in this class.
	 *
	 * @throws Exception
	 *             is thrown if an exception occurs
	 */
	@Override
	@AfterEach
	protected void tearDown() throws Exception
	{
		super.tearDown();
	}

	/**
	 * Test method for {@link FileExtensions#download(java.net.URI)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testDownload() throws IOException
	{

		final byte[] expected = { -84, -19, 0, 5, 116, 0, 7, 70, 111, 111, 32, 98, 97, 114 };

		final File destination = new File(this.testDir.getAbsoluteFile(), "testDownload.txt");

		StoreFileExtensions.toFile(destination, expected);

		final byte[] compare = FileExtensions.download(destination.toURI());

		for (int i = 0; i < compare.length; i++)
		{
			this.actual = compare[i] == expected[i];
			assertTrue(this.actual);
		}
	}

	/**
	 * Test method for {@link FileExtensions#getAbsolutPathWithoutFilename(File)}.
	 */
	@Test
	public void testGetAbsolutPathWithoutFilename()
	{
		String actual;
		String expected;
		String absolutePath = this.testDir.getAbsolutePath();
		final File srcFile = new File(this.testDir.getAbsoluteFile(), "testMoveFile.txt");
		actual = FileExtensions.getAbsolutPathWithoutFilename(srcFile);
		expected = absolutePath + File.separator;
		assertEquals(actual, expected);
	}

	/**
	 * The files for {@link #testGetAbsolutPathWithoutFilenameBelowTheRoot}, each with the path of
	 * its directory. Nothing is created: the paths are only computed.
	 *
	 * @return the files and the expected paths of their directories
	 */
	static Stream<Arguments> filesBelowTheRoot()
	{
		final File root = File.listRoots()[0];
		final String rootPath = root.getAbsolutePath();
		return Stream.of(
			argumentSet("a file directly in the root", new File(root, "file.txt"), rootPath),
			argumentSet("a file one directory below the root",
				new File(new File(root, "dir"), "file.txt"), rootPath + "dir" + File.separator),
			argumentSet("a file two directories below the root",
				new File(new File(new File(root, "dir"), "sub"), "file.txt"),
				rootPath + "dir" + File.separator + "sub" + File.separator));
	}

	/**
	 * Test method for {@link FileExtensions#getAbsolutPathWithoutFilename(File)}: the path ends
	 * with the separator after the directory, also when the only separator is the one of the root
	 * at the first position of the path.
	 *
	 * @param file
	 *            the file
	 * @param expected
	 *            the expected path of its directory
	 */
	@ParameterizedTest
	@MethodSource("filesBelowTheRoot")
	public void testGetAbsolutPathWithoutFilenameBelowTheRoot(final File file,
		final String expected)
	{
		assertEquals(expected, FileExtensions.getAbsolutPathWithoutFilename(file));
	}

	/**
	 * Test method for {@link FileExtensions#getCurrentAbsolutPathWithoutDotAndSlash()}: the result
	 * is the working directory of the JVM, without the trailing separator and dot.
	 */
	@Test
	public void testGetCurrentAbsolutPathWithoutDotAndSlash()
	{
		final String actual = FileExtensions.getCurrentAbsolutPathWithoutDotAndSlash();
		assertEquals(System.getProperty("user.dir"), actual);
		assertFalse(actual.endsWith("."), actual);
	}

	/**
	 * Test method for {@link FileExtensions#getContentType(File)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	@Test
	public void testGetContentType() throws IOException
	{
		String actual;
		String expected;
		final String filePrefix = "testAppendSystemtimeToFilename";
		final String fileSuffix = ".txt";
		final File testFile1 = new File(this.testDir, filePrefix + fileSuffix);
		actual = FileExtensions.getContentType(testFile1);
		expected = "text/plain";
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link FileExtensions#isOpen(File)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testIsOpen() throws IOException
	{
		boolean actual;
		boolean expected;
		final File srcFile = new File(this.testDir.getAbsoluteFile(), "testMoveFile.txt");
		actual = FileExtensions.isOpen(srcFile);
		expected = false;
		assertEquals(actual, expected);
	}

	/**
	 * Test method for {@link FileExtensions#isOpen(File)}: a file whose lock another process holds
	 * is open, and is no longer open once that process has released the lock and ended.
	 *
	 * @throws Exception
	 *             is thrown if the process that holds the lock cannot be started or awaited
	 */
	@Test
	public void testIsOpenWhileAnotherProcessHoldsTheLock() throws Exception
	{
		final File lockedFile = tempRoot.resolve("locked.txt").toFile();
		Files.writeString(lockedFile.toPath(), "locked by another process");
		// a small program, run by the java launcher from its source, that locks the file until
		// its standard input gets a line; a lock held in this JVM would not do, because tryLock
		// throws an OverlappingFileLockException instead of returning null
		final Path lockHolder = tempRoot.resolve("HoldLock.java");
		Files.writeString(lockHolder, """
			import java.io.RandomAccessFile;
			import java.nio.channels.FileLock;

			public class HoldLock
			{
				public static void main(String[] args) throws Exception
				{
					try (RandomAccessFile file = new RandomAccessFile(args[0], "rw");
						FileLock lock = file.getChannel().lock())
					{
						System.out.println("locked");
						System.out.flush();
						System.in.read();
					}
				}
			}
			""");
		final String java = Path.of(System.getProperty("java.home"), "bin", "java").toString();
		final Process process = new ProcessBuilder(java, lockHolder.toString(),
			lockedFile.getAbsolutePath()).redirectError(ProcessBuilder.Redirect.INHERIT).start();
		try
		{
			try (BufferedReader output = new BufferedReader(
				new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8)))
			{
				assertEquals("locked", output.readLine());
				assertTrue(FileExtensions.isOpen(lockedFile));
				// a line on its standard input lets the process release the lock and end
				try (OutputStream input = process.getOutputStream())
				{
					input.write('\n');
				}
				assertTrue(process.waitFor(30, TimeUnit.SECONDS));
			}
			assertEquals(0, process.exitValue());
			assertFalse(FileExtensions.isOpen(lockedFile));
		}
		finally
		{
			process.destroyForcibly();
		}
	}

	/**
	 * Test method for {@link FileExtensions}
	 */
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(FileExtensions.class);
	}

}
