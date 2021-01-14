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
package io.github.astrapi69.zip;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.CompressionMethod;

/**
 * The class {@link Zip4jExtensions}.
 */
public final class Zip4jExtensions
{

	private Zip4jExtensions()
	{
	}

	/**
	 * Extract.
	 *
	 * @param zipFile4j
	 *            the zip file4j
	 * @param destination
	 *            the destination
	 * @param password
	 *            the password
	 * @throws ZipException
	 *             the zip exception
	 */
	public static void extract(final ZipFile zipFile4j, final File destination,
		final String password) throws ZipException
	{
		if (zipFile4j.isEncrypted())
		{
			zipFile4j.setPassword(password.toCharArray());
		}
		zipFile4j.extractAll(destination.getAbsolutePath());
	}

	/**
	 * Adds a file or several files to the given zip file with the parameters
	 * Zip4jConstants.COMP_DEFLATE for the compression method and
	 * Zip4jConstants.DEFLATE_LEVEL_NORMAL as the compression level.
	 *
	 * @param zipFile4j
	 *            the zip file4j
	 * @param toAdd
	 *            the to add
	 * @throws ZipException
	 *             the zip exception
	 */
	public static void zipFiles(final ZipFile zipFile4j, final File... toAdd) throws ZipException
	{
		zipFiles(zipFile4j, CompressionMethod.DEFLATE, CompressionLevel.NORMAL, toAdd);
	}

	/**
	 * Adds a file or several files to the given zip file with the given parameters for the
	 * compression method and the compression level.
	 *
	 * @param zipFile4j
	 *            the zip file4j
	 * @param compressionMethod
	 *            The compression method
	 * @param compressionLevel
	 *            the compression level
	 * @param toAdd
	 *            the to add
	 * @throws ZipException
	 *             the zip exception
	 */
	public static void zipFiles(final ZipFile zipFile4j, final CompressionMethod compressionMethod,
		final CompressionLevel compressionLevel, final File... toAdd) throws ZipException
	{
		// Initiate Zip Parameters which define various properties such
		// as compression method, etc.
		final ZipParameters parameters = new ZipParameters();

		// set compression method to store compression
		// Zip4jConstants.COMP_STORE is for no compression
		// Zip4jConstants.COMP_DEFLATE is for compression
		parameters.setCompressionMethod(compressionMethod);

		// Set the compression level
		// DEFLATE_LEVEL_ULTRA = ultra maximum compression
		// DEFLATE_LEVEL_MAXIMUM = maximum compression
		// DEFLATE_LEVEL_NORMAL = normal compression
		// DEFLATE_LEVEL_FAST = fast compression
		// DEFLATE_LEVEL_FASTEST = fastest compression
		parameters.setCompressionLevel(compressionLevel);
		zipFiles(zipFile4j, parameters, toAdd);
	}

	/**
	 * Adds the given file(s) to the given zip file.
	 *
	 * @param zipFile4j
	 *            the zip file4j
	 * @param parameters
	 *            the parameters
	 * @param toAdd
	 *            the list with the files to add in the zip file
	 * @throws ZipException
	 *             the zip exception
	 */
	public static void zipFiles(final ZipFile zipFile4j, final ZipParameters parameters,
		final File... toAdd) throws ZipException
	{
		zipFiles(zipFile4j, parameters, Arrays.asList(toAdd));
	}

	/**
	 * Adds the given file(s) to the given zip file.
	 *
	 * @param zipFile4j
	 *            the zip file4j
	 * @param parameters
	 *            the parameters
	 * @param toAdd
	 *            the list with the files to add in the zip file
	 * @throws ZipException
	 *             the zip exception
	 */
	public static void zipFiles(final ZipFile zipFile4j, final ZipParameters parameters,
		final List<File> toAdd) throws ZipException
	{
		for (final File element : toAdd)
		{
			zipFile4j.addFile(element, parameters);
		}
	}

}
