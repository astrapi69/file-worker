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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.junit.jupiter.params.provider.Arguments.argumentSet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.UncheckedIOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.nio.channels.FileLock;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
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
	 * The data the tests of {@link FileExtensions#download(URI)} read
	 */
	private static final byte[] DOWNLOAD_DATA = "hello".getBytes(StandardCharsets.UTF_8);

	/**
	 * Creates a URI in the given directory, for the parameterized tests of
	 * {@link FileExtensions#download(URI)}
	 */
	@FunctionalInterface
	interface UriInDirectory
	{
		URI create(File directory) throws IOException;
	}

	/**
	 * Writes a jar with one entry
	 *
	 * @param jar
	 *            the jar file to write
	 * @param entryName
	 *            the name of the entry
	 * @param content
	 *            the content of the entry
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	private static void writeJar(final Path jar, final String entryName, final byte[] content)
		throws IOException
	{
		try (JarOutputStream out = new JarOutputStream(Files.newOutputStream(jar)))
		{
			out.putNextEntry(new JarEntry(entryName));
			out.write(content);
			out.closeEntry();
		}
	}

	/**
	 * Gets the jar: URI of an entry of the given jar
	 *
	 * @param jar
	 *            the jar file
	 * @param entryName
	 *            the name of the entry
	 * @return the jar: URI of the entry
	 */
	private static URI jarEntryUri(final Path jar, final String entryName)
	{
		return URI.create("jar:" + jar.toUri() + "!/" + entryName);
	}

	/**
	 * Gets a port on the loopback interface that was free a moment before
	 *
	 * @return the port
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	private static int unusedLoopbackPort() throws IOException
	{
		try (ServerSocket socket = new ServerSocket(0, 1, InetAddress.getLoopbackAddress()))
		{
			return socket.getLocalPort();
		}
	}

	/**
	 * Test method for {@link FileExtensions#download(URI)} with the jar: URI of an entry of a jar
	 * in a local directory: the result is the content of the entry.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testDownloadAnEntryOfALocalJar() throws IOException
	{
		final Path jar = this.testDir.toPath().resolve("data.jar");
		writeJar(jar, "data.txt", DOWNLOAD_DATA);

		assertArrayEquals(DOWNLOAD_DATA, FileExtensions.download(jarEntryUri(jar, "data.txt")));
	}

	/**
	 * Test method for {@link FileExtensions#download(URI)} with the jar: URI of an entry of a jar
	 * that was replaced after a first download: the second download reads the new jar, not a copy
	 * of the old one kept open from the first.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testDownloadAnEntryOfALocalJarThatWasReplaced() throws IOException
	{
		final byte[] replaced = "world".getBytes(StandardCharsets.UTF_8);
		final Path jar = this.testDir.toPath().resolve("replaced.jar");
		final URI uri = jarEntryUri(jar, "data.txt");
		writeJar(jar, "data.txt", DOWNLOAD_DATA);
		assertArrayEquals(DOWNLOAD_DATA, FileExtensions.download(uri));

		Files.delete(jar);
		writeJar(jar, "data.txt", replaced);

		assertArrayEquals(replaced, FileExtensions.download(uri));
	}

	/**
	 * Test method for {@link FileExtensions#download(URI)} with an http: URI of a server on the
	 * loopback interface: the result is the body the server sends.
	 *
	 * @throws Exception
	 *             is thrown if an exception occurs
	 */
	@Test
	public void testDownloadWhatAnHttpServerOnTheLoopbackInterfaceSends() throws Exception
	{
		try (ServerSocket server = new ServerSocket(0, 1, InetAddress.getLoopbackAddress()))
		{
			final Thread serving = new Thread(() -> {
				try (Socket client = server.accept())
				{
					final BufferedReader request = new BufferedReader(
						new InputStreamReader(client.getInputStream(), StandardCharsets.US_ASCII));
					String line = request.readLine();
					while (line != null && !line.isEmpty())
					{
						line = request.readLine();
					}
					final OutputStream response = client.getOutputStream();
					response.write(("HTTP/1.1 200 OK\r\nContent-Length: " + DOWNLOAD_DATA.length
						+ "\r\nConnection: close\r\n\r\n").getBytes(StandardCharsets.US_ASCII));
					response.write(DOWNLOAD_DATA);
					response.flush();
				}
				catch (final IOException exception)
				{
					throw new UncheckedIOException(exception);
				}
			});
			serving.start();
			final URI uri = URI.create("http://127.0.0.1:" + server.getLocalPort() + "/data.txt");

			assertArrayEquals(DOWNLOAD_DATA, FileExtensions.download(uri));
			serving.join(TimeUnit.SECONDS.toMillis(10));
		}
	}

	/**
	 * The URIs that name nothing {@link FileExtensions#download(URI)} can read, each created in the
	 * test directory
	 *
	 * @return the URIs
	 */
	static Stream<Arguments> unreadableUris()
	{
		return Stream.of(
			argumentSet("an http: URI to a loopback port that was free a moment before",
				(UriInDirectory)directory -> URI
					.create("http://127.0.0.1:" + unusedLoopbackPort() + "/data.txt")),
			argumentSet("the jar: URI of an entry a local jar does not hold",
				(UriInDirectory)directory -> {
					final Path jar = directory.toPath().resolve("data.jar");
					writeJar(jar, "data.txt", DOWNLOAD_DATA);
					return jarEntryUri(jar, "missing.txt");
				}),
			argumentSet("the jar: URI of an entry of a jar that does not exist",
				(UriInDirectory)directory -> jarEntryUri(directory.toPath().resolve("missing.jar"),
					"data.txt")));
	}

	/**
	 * Test method for {@link FileExtensions#download(URI)} with a URI that cannot be read: the
	 * failure is the declared {@link IOException}.
	 *
	 * @param uriInDirectory
	 *            creates the URI in the test directory
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@ParameterizedTest
	@MethodSource("unreadableUris")
	public void testDownloadAUriThatCannotBeReadThrowsIOException(
		final UriInDirectory uriInDirectory) throws IOException
	{
		final URI uri = uriInDirectory.create(this.testDir);

		assertThrows(IOException.class, () -> FileExtensions.download(uri));
	}

	/**
	 * The file: URIs that name no regular file, each created in the test directory
	 *
	 * @return the URIs
	 */
	static Stream<Arguments> fileUrisOfNoRegularFile()
	{
		return Stream.of(
			argumentSet("a file that does not exist",
				(UriInDirectory)directory -> new File(directory, "missing.txt").toURI()),
			argumentSet("a directory", (UriInDirectory)File::toURI));
	}

	/**
	 * Test method for {@link FileExtensions#download(URI)} with a file: URI that names no regular
	 * file: the result is null.
	 *
	 * @param uriInDirectory
	 *            creates the URI in the test directory
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@ParameterizedTest
	@MethodSource("fileUrisOfNoRegularFile")
	public void testDownloadAFileUriThatNamesNoRegularFileReturnsNull(
		final UriInDirectory uriInDirectory) throws IOException
	{
		assertNull(FileExtensions.download(uriInDirectory.create(this.testDir)));
	}

	/**
	 * Test method for {@link FileExtensions#download(URI)} with a URI that is not absolute: the
	 * failure is an {@link IllegalArgumentException}.
	 */
	@Test
	public void testDownloadAUriThatIsNotAbsoluteThrowsIllegalArgumentException()
	{
		final URI uri = URI.create("data.txt");

		assertThrows(IllegalArgumentException.class, () -> FileExtensions.download(uri));
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
	 * The missing files for {@link #testIsOpenOnAMissingFile}, as paths below the temporary
	 * directory. Nothing is created.
	 *
	 * @return the paths of the missing files, relative to the temporary directory
	 */
	static Stream<Arguments> missingFiles()
	{
		return Stream.of(argumentSet("a missing file in an existing directory", "missing.txt"),
			argumentSet("a missing file in a missing directory",
				"missingDir" + File.separator + "missing.txt"));
	}

	/**
	 * Test method for {@link FileExtensions#isOpen(File)}: a missing file is not open, and the
	 * check leaves it missing.
	 *
	 * @param relativePath
	 *            the path of the missing file, relative to the temporary directory
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@ParameterizedTest
	@MethodSource("missingFiles")
	public void testIsOpenOnAMissingFile(final String relativePath) throws IOException
	{
		final File missing = tempRoot.resolve(relativePath).toFile();
		final Set<String> entriesBefore = Set.of(tempRoot.toFile().list());
		assertFalse(missing.exists());

		assertFalse(FileExtensions.isOpen(missing));
		assertFalse(missing.exists(), "isOpen created " + missing);
		assertEquals(entriesBefore, Set.of(tempRoot.toFile().list()));
	}

	/**
	 * The existing files without a lock for {@link #testIsOpenOnAnExistingFileWithoutALock}, each
	 * with its content and whether this JVM has it open for reading during the check.
	 *
	 * @return the contents and whether the file is open for reading
	 */
	static Stream<Arguments> filesWithoutALock()
	{
		return Stream.of(argumentSet("an empty file", "", false),
			argumentSet("a file with content", "content", false),
			argumentSet("a file this JVM has open for reading", "content", true));
	}

	/**
	 * Test method for {@link FileExtensions#isOpen(File)}: an existing file on which no lock is
	 * held is not open, also when it is open for reading, and the check leaves its content as it
	 * was.
	 *
	 * @param content
	 *            the content of the file
	 * @param openForReading
	 *            whether this JVM has the file open for reading during the check
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@ParameterizedTest
	@MethodSource("filesWithoutALock")
	public void testIsOpenOnAnExistingFileWithoutALock(final String content,
		final boolean openForReading) throws IOException
	{
		final File file = tempRoot.resolve("unlocked.txt").toFile();
		Files.writeString(file.toPath(), content);
		if (openForReading)
		{
			try (FileInputStream input = new FileInputStream(file))
			{
				assertFalse(FileExtensions.isOpen(file));
			}
		}
		else
		{
			assertFalse(FileExtensions.isOpen(file));
		}
		assertEquals(content, Files.readString(file.toPath()));
	}

	/**
	 * Test method for {@link FileExtensions#isOpen(File)}: a file whose lock this JVM holds through
	 * another channel is open, and is no longer open once the lock is released.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testIsOpenWhileThisJvmHoldsTheLock() throws IOException
	{
		final File lockedFile = tempRoot.resolve("locked.txt").toFile();
		Files.writeString(lockedFile.toPath(), "locked by this JVM");
		try (RandomAccessFile access = new RandomAccessFile(lockedFile, "rw");
			FileLock lock = access.getChannel().lock())
		{
			assertTrue(FileExtensions.isOpen(lockedFile));
			assertTrue(lock.isValid());
		}
		assertFalse(FileExtensions.isOpen(lockedFile));
	}

	/**
	 * Test method for {@link FileExtensions#isOpen(File)}: an existing file that this process
	 * cannot open for writing is not answered with {@code false}; the check throws the
	 * {@link FileNotFoundException} of the opening, and leaves the file as it was.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testIsOpenOnAFileThatCannotBeOpenedForWriting() throws IOException
	{
		final Path readOnly = tempRoot.resolve("readOnly.txt");
		Files.writeString(readOnly, "read only");
		assumeTrue(readOnly.toFile().setWritable(false), "the file system takes the permission");
		try
		{
			assumeFalse(Files.isWritable(readOnly), "the test runs without override rights");
			assertThrows(FileNotFoundException.class,
				() -> FileExtensions.isOpen(readOnly.toFile()));
			assertEquals("read only", Files.readString(readOnly));
		}
		finally
		{
			readOnly.toFile().setWritable(true);
		}
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
