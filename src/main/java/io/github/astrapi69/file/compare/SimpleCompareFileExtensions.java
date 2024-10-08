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
package io.github.astrapi69.file.compare;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import io.github.astrapi69.checksum.FileChecksumExtensions;
import io.github.astrapi69.crypt.api.algorithm.Algorithm;

/**
 * The class {@link SimpleCompareFileExtensions}.
 */
public final class SimpleCompareFileExtensions
{

	/**
	 * Private constructor to prevent instantiation
	 */
	private SimpleCompareFileExtensions()
	{
	}

	/**
	 * Compare files by absolute path.
	 *
	 * @param sourceFile
	 *            the source file
	 * @param fileToCompare
	 *            the file to compare
	 * @return true if the absolute path are equal, otherwise false.
	 */
	public static boolean compareFilesByAbsolutePath(final File sourceFile,
		final File fileToCompare)
	{
		return CompareFileExtensions
			.compareFiles(sourceFile, fileToCompare, false, true, true, true, true, true)
			.getAbsolutePathEquality();
	}

	/**
	 * Compare files by checksum with the algorithm Adler32.
	 *
	 * @param sourceFile
	 *            the source file
	 * @param fileToCompare
	 *            the file to compare
	 * @param algorithm
	 *            the algorithm
	 * @return true if the checksum with the algorithm Adler32 are equal, otherwise false.
	 * @throws NoSuchAlgorithmException
	 *             Is thrown if the algorithm is not supported or does not exists.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static boolean compareFilesByChecksum(final File sourceFile, final File fileToCompare,
		final Algorithm algorithm) throws NoSuchAlgorithmException, IOException
	{
		final String checksumSourceFile = FileChecksumExtensions.getChecksum(sourceFile, algorithm);
		final String checksumFileToCompare = FileChecksumExtensions.getChecksum(fileToCompare,
			algorithm);
		return checksumSourceFile.equals(checksumFileToCompare);
	}

	/**
	 * Compare files by checksum with the algorithm Adler32.
	 *
	 * @param sourceFile
	 *            the source file
	 * @param fileToCompare
	 *            the file to compare
	 * @return true if the checksum with the algorithm Adler32 are equal, otherwise false.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static boolean compareFilesByChecksumAdler32(final File sourceFile,
		final File fileToCompare) throws IOException
	{
		final long checksumSourceFile = FileChecksumExtensions.getCheckSumAdler32(sourceFile);
		final long checksumFileToCompare = FileChecksumExtensions.getCheckSumAdler32(fileToCompare);
		return checksumSourceFile == checksumFileToCompare;
	}

	/**
	 * Compare files by checksum with the algorithm CRC32.
	 *
	 * @param sourceFile
	 *            the source file
	 * @param fileToCompare
	 *            the file to compare
	 * @return true if the checksum with the algorithm CRC32 are equal, otherwise false.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static boolean compareFilesByChecksumCRC32(final File sourceFile,
		final File fileToCompare) throws IOException
	{
		final long checksumSourceFile = FileChecksumExtensions.getCheckSumCRC32(sourceFile);
		final long checksumFileToCompare = FileChecksumExtensions.getCheckSumCRC32(fileToCompare);
		return checksumSourceFile == checksumFileToCompare;
	}

	/**
	 * Compare files by content.
	 *
	 * @param sourceFile
	 *            the source file
	 * @param fileToCompare
	 *            the file to compare
	 *
	 * @return true if the content are equal, otherwise false.
	 */
	public static boolean compareFilesByContent(final File sourceFile, final File fileToCompare)
	{
		return CompareFileExtensions
			.compareFiles(sourceFile, fileToCompare, true, true, true, true, true, false)
			.getContentEquality();
	}

	/**
	 * Compare files by extension.
	 *
	 * @param sourceFile
	 *            the source file
	 * @param fileToCompare
	 *            the file to compare
	 * @return true if the extension are equal, otherwise false.
	 */
	public static boolean compareFilesByExtension(final File sourceFile, final File fileToCompare)
	{
		return CompareFileExtensions
			.compareFiles(sourceFile, fileToCompare, true, false, true, true, true, true)
			.getFileExtensionEquality();
	}

	/**
	 * Compare files by last modified.
	 *
	 * @param sourceFile
	 *            the source file
	 * @param fileToCompare
	 *            the file to compare
	 * @return true if the last modified are equal, otherwise false.
	 */
	public static boolean compareFilesByLastModified(final File sourceFile,
		final File fileToCompare)
	{
		return CompareFileExtensions
			.compareFiles(sourceFile, fileToCompare, true, true, true, false, true, true)
			.getLastModifiedEquality();
	}

	/**
	 * Compare files by length.
	 *
	 * @param sourceFile
	 *            the source file
	 * @param fileToCompare
	 *            the file to compare
	 * @return true if the length are equal, otherwise false.
	 */
	public static boolean compareFilesByLength(final File sourceFile, final File fileToCompare)
	{
		return CompareFileExtensions
			.compareFiles(sourceFile, fileToCompare, true, true, false, true, true, true)
			.getLengthEquality();
	}

	/**
	 * Compare files by name.
	 *
	 * @param sourceFile
	 *            the source file
	 * @param fileToCompare
	 *            the file to compare
	 * @return true if the name are equal, otherwise false.
	 */
	public static boolean compareFilesByName(final File sourceFile, final File fileToCompare)
	{
		return CompareFileExtensions
			.compareFiles(sourceFile, fileToCompare, true, true, true, true, false, true)
			.getNameEquality();
	}

}
