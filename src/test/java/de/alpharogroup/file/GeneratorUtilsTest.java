/**
 * The MIT License
 *
 * Copyright (C) 2007 Asterios Raptis
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
package de.alpharogroup.file;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.alpharogroup.file.csv.CsvFileUtils;

/**
 * Test class for the class GeneratorUtils.
 * 
 * @version 1.0
 * @author Asterios Raptis
 */
public class GeneratorUtilsTest extends FileTestCase
{

	/** The constantlist. */
	File constantlist;

	/**
	 * {@inheritDoc}
	 */
	@Override
	@BeforeMethod
	protected void setUp() throws Exception
	{
		super.setUp();
		constantlist = new File(this.testResources, "constantlist");

	}

	/**
	 * Test method for
	 * {@link de.alpharogroup.file.GeneratorUtils#createStaticArrayVariable(String, List)} .
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	// @Test
	// public void testCreateStaticArrayVariable() {
	// fail("Not yet implemented");
	// }

	/**
	 * Test method for
	 * {@link de.alpharogroup.file.GeneratorUtils#newConstantsFromStringList(List, String, String, boolean)}
	 * .
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testCreateConstantsFromStringList() throws IOException
	{
		final List<String> cl = CsvFileUtils.readFileToList(constantlist);
		final List<String> gcl = GeneratorUtils.newConstantsFromStringList(cl, null, "_key", true);
		for (final String string : gcl)
		{
			System.out.println(string);
		}
	}

}
