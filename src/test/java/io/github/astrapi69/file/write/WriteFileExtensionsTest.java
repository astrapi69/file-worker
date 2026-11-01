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
package io.github.astrapi69.file.write;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.argumentSet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.meanbean.test.BeanTester;

import io.github.astrapi69.checksum.FileChecksumExtensions;
import io.github.astrapi69.file.FileTestCase;
import io.github.astrapi69.file.read.ReadFileExtensions;
import io.github.astrapi69.file.search.PathFinder;
import io.github.astrapi69.io.StreamExtensions;

/**
 * The unit test class for the class {@link WriteFileExtensions}
 */
public class WriteFileExtensionsTest extends FileTestCase
{

	private File tempFile;

	@TempDir
	Path tempDir;

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
		// Create a temporary file for testing
		tempFile = File.createTempFile("test-file", ".txt");
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
	 * Test method for {@link WriteFileExtensions}
	 */
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(WriteFileExtensions.class);
	}

	/**
	 * Test method for {@link WriteFileExtensions#write(InputStream, OutputStream)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testWrite() throws IOException
	{
		long actual;
		long expected;

		final File testFile = new File(PathFinder.getSrcTestResourcesDir(),
			"testReadFileInput.txt");
		File fileout = new File(PathFinder.getSrcTestResourcesDir(),
			"testWriteFileExtensionsWrite.out");
		try (InputStream inputStream = StreamExtensions.getInputStream(testFile, true);
			OutputStream outputStream = StreamExtensions.getOutputStream(fileout, true))
		{
			StreamExtensions.writeInputStreamToOutputStream(inputStream, outputStream);
		}

		actual = FileChecksumExtensions.getCheckSumAdler32(testFile);
		expected = FileChecksumExtensions.getCheckSumAdler32(fileout);
		assertEquals(expected, actual);
		fileout.deleteOnExit();
	}

	/**
	 * Test method for {@link StoreFileExtensions#toFile(File, byte[])}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testWriteByteArrayToFileFileByteArray() throws IOException
	{
		final byte[] expected = { -84, -19, 0, 5, 116, 0, 7, 70, 111, 111, 32, 98, 97, 114 };

		final File destination = new File(this.testDir.getAbsoluteFile(),
			"testStoreByteArrayToFile.txt");
		StoreFileExtensions.toFile(destination, expected);

		final byte[] compare = ReadFileExtensions.readFileToBytearray(destination);

		for (int i = 0; i < compare.length; i++)
		{
			this.actual = compare[i] == expected[i];
			assertTrue(this.actual);
		}
		destination.deleteOnExit();
	}

	/**
	 * Test method for {@link StoreFileExtensions#toFile(File, byte[])}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testWriteByteArrayToFileStringByteArray() throws IOException
	{
		final byte[] expected = { -84, -19, 0, 5, 116, 0, 7, 70, 111, 111, 32, 98, 97, 114 };

		final File destination = new File(this.testDir.getAbsoluteFile(),
			"testStoreByteArrayToFile.txt");
		StoreFileExtensions.toFile(destination, expected);


		final byte[] compare = ReadFileExtensions.readFileToBytearray(destination);

		for (int i = 0; i < compare.length; i++)
		{
			this.actual = compare[i] == expected[i];
			assertTrue(this.actual);
		}
		destination.deleteOnExit();
	}

	/**
	 * A copy between two files given by their path names, as the deprecated copy methods of
	 * {@link WriteFileExtensions} offer it
	 */
	@FunctionalInterface
	interface FileCopy
	{
		void copy(String source, String destination) throws IOException;
	}

	/**
	 * A write of a byte array to a file, as both overloads of
	 * {@link WriteFileExtensions#writeByteArrayToFile} offer it
	 */
	@FunctionalInterface
	interface ByteArrayWrite
	{
		void write(File file, byte[] byteArray) throws IOException;
	}

	static Stream<Named<FileCopy>> copyMethods()
	{
		return Stream.of(
			Named.<FileCopy> of("readSourceFileAndWriteDestFile(String, String)",
				WriteFileExtensions::readSourceFileAndWriteDestFile),
			Named.<FileCopy> of("write2File(String, String)", WriteFileExtensions::write2File),
			Named.<FileCopy> of("write2FileWithBuffer(String, String)",
				WriteFileExtensions::write2FileWithBuffer));
	}

	private static byte[] everyByteValue()
	{
		byte[] everyByteValue = new byte[256];
		for (int i = 0; i < everyByteValue.length; i++)
		{
			everyByteValue[i] = (byte)(Byte.MIN_VALUE + i);
		}
		return everyByteValue;
	}

	static Stream<Named<byte[]>> byteContents()
	{
		return Stream.of(Named.of("no bytes", new byte[0]), Named.of("one byte", new byte[] { 42 }),
			Named.of("every byte value from -128 to 127", everyByteValue()));
	}

	static Stream<Named<ByteArrayWrite>> byteArrayWrites()
	{
		return Stream.of(
			Named.<ByteArrayWrite> of("writeByteArrayToFile(File, byte[])",
				WriteFileExtensions::writeByteArrayToFile),
			Named.<ByteArrayWrite> of("writeByteArrayToFile(String, byte[])",
				(file, byteArray) -> WriteFileExtensions
					.writeByteArrayToFile(file.getAbsolutePath(), byteArray)));
	}

	static Stream<Arguments> linesAndTheirTextWithNewlines()
	{
		return Stream.of(argumentSet("no lines", List.of(), ""),
			argumentSet("one line", List.of("only"), "only\n"),
			argumentSet("an empty line between two lines", List.of("first", "", "third"),
				"first\n\nthird\n"));
	}

	static Stream<Arguments> namedEncodings()
	{
		return Stream.of(
			argumentSet("UTF-8", "UTF-8", StandardCharsets.UTF_8,
				List.of("Umlaute äöüß", "Euro €")),
			argumentSet("ISO-8859-1", "ISO-8859-1", StandardCharsets.ISO_8859_1,
				List.of("Umlaute äöüß", "Grad °")),
			argumentSet("UTF-8 without lines", "UTF-8", StandardCharsets.UTF_8, List.of()));
	}

	static Stream<Arguments> namedEncodingsAndThePlatformDefault()
	{
		return Stream.concat(namedEncodings(), Stream.of(argumentSet("no encoding given (null)",
			null, Charset.defaultCharset(), List.of("plain ascii", "second line"))));
	}

	private static String withLineSeparators(List<String> lines)
	{
		return lines.stream().map(line -> line + System.lineSeparator())
			.collect(Collectors.joining());
	}

	/**
	 * Test method for {@link WriteFileExtensions#appendLines(File, String...)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void appendLines_addsEachLineAfterTheExistingContent() throws IOException
	{
		File file = tempDir.resolve("append.txt").toFile();
		Files.write(file.toPath(), List.of("first"));

		WriteFileExtensions.appendLines(file, "second", "third");

		assertEquals(List.of("first", "second", "third"), Files.readAllLines(file.toPath()));
	}

	/**
	 * Test method for {@link WriteFileExtensions#appendLines(File, String...)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void appendLines_createsTheFileWhenItDoesNotExist() throws IOException
	{
		File file = tempDir.resolve("created.txt").toFile();

		WriteFileExtensions.appendLines(file, "only line");

		assertEquals(List.of("only line"), Files.readAllLines(file.toPath()));
	}

	/**
	 * Test method for {@link WriteFileExtensions#readSourceFileAndWriteDestFile(String, String)},
	 * {@link WriteFileExtensions#write2File(String, String)} and
	 * {@link WriteFileExtensions#write2FileWithBuffer(String, String)}
	 *
	 * @param fileCopy
	 *            the copy method under test
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("copyMethods")
	public void copyMethods_replaceTheContentOfAnExistingDestination(FileCopy fileCopy)
		throws IOException
	{
		File source = tempDir.resolve("source.txt").toFile();
		File destination = tempDir.resolve("destination.txt").toFile();
		Files.writeString(source.toPath(), "new content\nsecond line\n");
		Files.writeString(destination.toPath(), "old content, longer than the new content\n");

		fileCopy.copy(source.getAbsolutePath(), destination.getAbsolutePath());

		assertEquals("new content\nsecond line\n", Files.readString(destination.toPath()));
	}

	/**
	 * Test method for {@link WriteFileExtensions#readSourceFileAndWriteDestFile(String, String)},
	 * {@link WriteFileExtensions#write2File(String, String)} and
	 * {@link WriteFileExtensions#write2FileWithBuffer(String, String)}
	 *
	 * @param fileCopy
	 *            the copy method under test
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("copyMethods")
	public void copyMethods_createTheDestinationWhenItDoesNotExist(FileCopy fileCopy)
		throws IOException
	{
		File source = tempDir.resolve("source.txt").toFile();
		File destination = tempDir.resolve("new-destination.txt").toFile();
		Files.writeString(source.toPath(), "copied content\n");

		fileCopy.copy(source.getAbsolutePath(), destination.getAbsolutePath());

		assertEquals("copied content\n", Files.readString(destination.toPath()));
	}

	/**
	 * Test method for {@link WriteFileExtensions#string2File(String, String)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void string2File_writesTheStringIntoTheNamedFile() throws IOException
	{
		File file = tempDir.resolve("string.txt").toFile();

		WriteFileExtensions.string2File("Its a beautiful day\nand a second line",
			file.getAbsolutePath());

		assertEquals("Its a beautiful day\nand a second line", Files.readString(file.toPath()));
	}

	/**
	 * Test method for {@link WriteFileExtensions#write(InputStream, OutputStream)}
	 *
	 * @param content
	 *            the bytes in the input stream
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("byteContents")
	public void write_copiesEveryByteOfTheInputStreamToTheOutputStream(byte[] content)
		throws IOException
	{
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		WriteFileExtensions.write(new ByteArrayInputStream(content), outputStream);

		assertArrayEquals(content, outputStream.toByteArray());
	}

	/**
	 * Test method for {@link WriteFileExtensions#write2File(java.io.Reader, java.io.Writer)}
	 *
	 * @param content
	 *            the characters in the reader
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@ParameterizedTest
	@ValueSource(strings = { "", "a", "two\nlines\n", "Umlaute äöüß und €" })
	public void write2File_copiesEveryCharacterOfTheReaderToTheWriter(String content)
		throws IOException
	{
		StringWriter writer = new StringWriter();

		WriteFileExtensions.write2File(new StringReader(content), writer);

		assertEquals(content, writer.toString());
	}

	/**
	 * Test method for {@link WriteFileExtensions#write2File(String, java.io.Writer)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void write2File_writesTheContentOfTheNamedFileToTheWriter() throws IOException
	{
		File source = tempDir.resolve("source.txt").toFile();
		Files.writeString(source.toPath(), "first line\nsecond line\n");
		StringWriter writer = new StringWriter();

		WriteFileExtensions.write2File(source.getAbsolutePath(), writer);

		assertEquals("first line\nsecond line\n", writer.toString());
	}

	/**
	 * Test method for {@link WriteFileExtensions#write2File(String, java.io.Writer)}
	 */
	@Test
	public void write2File_throwsFileNotFoundException_whenTheNamedFileDoesNotExist()
	{
		File missing = tempDir.resolve("missing.txt").toFile();
		StringWriter writer = new StringWriter();

		FileNotFoundException exception = assertThrows(FileNotFoundException.class,
			() -> WriteFileExtensions.write2File(missing.getAbsolutePath(), writer));

		assertTrue(exception.getMessage().contains(missing.getAbsolutePath()),
			exception.getMessage());
	}

	/**
	 * Test method for {@link WriteFileExtensions#writeByteArrayToFile(File, byte[])} and
	 * {@link WriteFileExtensions#writeByteArrayToFile(String, byte[])}
	 *
	 * @param byteArrayWrite
	 *            the overload under test
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("byteArrayWrites")
	public void writeByteArrayToFile_writesEveryByteIntoTheFile(ByteArrayWrite byteArrayWrite)
		throws IOException
	{
		byte[] content = everyByteValue();
		File file = tempDir.resolve("bytes.bin").toFile();

		byteArrayWrite.write(file, content);

		assertArrayEquals(content, Files.readAllBytes(file.toPath()));
	}

	/**
	 * Test method for {@link WriteFileExtensions#writeLinesToFile(java.util.Collection, File)}
	 *
	 * @param lines
	 *            the lines to write
	 * @param expectedContent
	 *            the content the file must hold afterwards
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@ParameterizedTest
	@MethodSource("linesAndTheirTextWithNewlines")
	public void writeLinesToFile_endsEveryLineWithANewline(List<String> lines,
		String expectedContent) throws IOException
	{
		File output = tempDir.resolve("lines.txt").toFile();

		WriteFileExtensions.writeLinesToFile(lines, output);

		assertEquals(expectedContent, Files.readString(output.toPath()));
	}

	/**
	 * Test method for
	 * {@link WriteFileExtensions#writeLinesToFile(java.util.Collection, File, String)}
	 *
	 * @param encoding
	 *            the name of the encoding to write with
	 * @param charset
	 *            the charset to read the file back with
	 * @param lines
	 *            the lines to write
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@ParameterizedTest
	@MethodSource("namedEncodings")
	public void writeLinesToFile_writesEveryLineInTheGivenEncoding(String encoding, Charset charset,
		List<String> lines) throws IOException
	{
		File output = tempDir.resolve("encoded.txt").toFile();

		WriteFileExtensions.writeLinesToFile(lines, output, encoding);

		assertEquals(withLineSeparators(lines), Files.readString(output.toPath(), charset));
	}

	/**
	 * Test method for {@link WriteFileExtensions#writeLinesToFile(File, List, String)}
	 *
	 * @param encoding
	 *            the name of the encoding to write with, or null for the platform default
	 * @param charset
	 *            the charset to read the file back with
	 * @param lines
	 *            the lines to write
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@ParameterizedTest
	@MethodSource("namedEncodingsAndThePlatformDefault")
	public void writeLinesToFile_writesTheListInTheGivenEncodingOrThePlatformDefault(
		String encoding, Charset charset, List<String> lines) throws IOException
	{
		File output = tempDir.resolve("list.txt").toFile();

		WriteFileExtensions.writeLinesToFile(output, lines, encoding);

		assertEquals(withLineSeparators(lines), Files.readString(output.toPath(), charset));
	}

	/**
	 * Test method for {@link WriteFileExtensions#writeLinesToFile(File, List, String)}
	 */
	@Test
	public void writeLinesToFile_throwsUnsupportedEncodingException_whenTheEncodingIsUnknown()
	{
		File output = tempDir.resolve("unknown-encoding.txt").toFile();

		UnsupportedEncodingException exception = assertThrows(UnsupportedEncodingException.class,
			() -> WriteFileExtensions.writeLinesToFile(output, List.of("line"), "no-such-charset"));

		assertEquals("no-such-charset", exception.getMessage());
	}

	/**
	 * Test method for {@link WriteFileExtensions#writeProperties2File(String, Properties)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void writeProperties2File_writesPropertiesThatLoadBackEqual() throws IOException
	{
		Properties properties = new Properties();
		properties.setProperty("name", "file-worker");
		properties.setProperty("greeting", "Grüße = Umlaute: äöü");
		File file = tempDir.resolve("written.properties").toFile();

		WriteFileExtensions.writeProperties2File(file.getAbsolutePath(), properties);

		Properties loaded = new Properties();
		try (InputStream inputStream = Files.newInputStream(file.toPath()))
		{
			loaded.load(inputStream);
		}
		assertEquals(properties, loaded);
	}

}
