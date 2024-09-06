package io.github.astrapi69.file.write;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * The unit test class for the class {@link LineAppender}
 */
class LineAppenderTest
{

	private File tempFile;

	/**
	 * Sets up method will be invoked before every unit test method in this class.
	 *
	 * @throws Exception
	 *             is thrown if an exception occurs
	 */
	@BeforeEach
	protected void setUp() throws Exception
	{
		// Create a temporary file for testing
		tempFile = File.createTempFile("test-file", ".txt");
	}

	/**
	 * Test method for {@link LineAppender#appendLines(File, String...)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	@DisplayName("Test appending lines to a file")
	public void testAppendLines() throws IOException
	{
		// Arrange
		String[] linesToAppend = { "Line 1", "Line 2", "Line 3" };

		// Act
		LineAppender.appendLines(tempFile, linesToAppend);

		// Assert
		List<String> lines = Files.readAllLines(tempFile.toPath());
		assertEquals(3, lines.size(), "Number of lines should be 3");
		assertEquals("Line 1", lines.get(0));
		assertEquals("Line 2", lines.get(1));
		assertEquals("Line 3", lines.get(2));
	}

}