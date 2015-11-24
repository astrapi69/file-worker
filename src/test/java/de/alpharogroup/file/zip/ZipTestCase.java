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
package de.alpharogroup.file.zip;

import java.io.File;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import de.alpharogroup.file.FileTestCase;

/**
 * The abstract class ZipTestCase is for tests in the package 'de.alpharogroup.file.zip'.
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public abstract class ZipTestCase extends FileTestCase
{

	/**
	 * The directory for unzipping zip files. Current
	 * value:"projectpath"+"/src/test/resources"+"/unzipDir"+"/testDir".
	 */
	protected File unzipDirTestDir;

	/**
	 * The directory for unzipping zip files. Current
	 * value:"projectpath"+"/src/test/resources"+"/unzipDir"+"/testDir"+"/deepDir".
	 */
	protected File unzipDirDeepDir;

	/**
	 * The directory for unzipping zip files. Current
	 * value:"projectpath"+"/src/test/resources"+"/unzipDir"+"/testDir"+"/deepDir2".
	 */
	protected File unzipDirDeepDir2;

	/**
	 * The directory for unzipping zip files. Current value:"projectpath"+"/src/test/resources"
	 * +"/unzipDir"+"/testDir"+"/deepDir"+"/deeperDir".
	 */
	protected File unzipDirDeeperDir;

	/**
	 * Sets the up.
	 *
	 * @throws Exception
	 *             the exception {@inheritDoc}
	 * @see de.alpharogroup.file.FileTestCase#setUp()
	 */
	@Override
	@BeforeMethod
	protected void setUp() throws Exception
	{
		super.setUp();

		this.unzipDirTestDir = new File(this.unzipDir, "testDir");
		this.unzipDirDeepDir = new File(this.unzipDirTestDir, "deepDir");
		this.unzipDirDeepDir2 = new File(this.unzipDirTestDir, "deepDir2");
		this.unzipDirDeeperDir = new File(this.unzipDirDeepDir, "deeperDir");
	}

	/**
	 * Tear down.
	 *
	 * @throws Exception
	 *             the exception {@inheritDoc}
	 * @see de.alpharogroup.file.FileTestCase#tearDown()
	 */
	@Override
	@AfterMethod
	protected void tearDown() throws Exception
	{
		super.tearDown();
	}

}
