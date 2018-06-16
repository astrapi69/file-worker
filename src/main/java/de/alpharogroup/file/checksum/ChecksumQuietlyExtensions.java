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
package de.alpharogroup.file.checksum;

import java.io.File;
import java.security.NoSuchAlgorithmException;

import de.alpharogroup.crypto.algorithm.Algorithm;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * The class {@link ChecksumQuietlyExtensions} is a utility class for computing checksum from files
 * and byte arrays in a quietly manner as the name let presume.
 */
@UtilityClass
@Slf4j
public final class ChecksumQuietlyExtensions
{

	/**
	 * Gets the checksum quietly from the given byte array with an instance of.
	 *
	 * @param bytes
	 *            the byte array.
	 * @param algorithm
	 *            the algorithm to get the checksum. This could be for instance "MD4", "MD5",
	 *            "SHA-1", "SHA-256", "SHA-384" or "SHA-512".
	 * @return The checksum from the file as a String object.
	 */
	public static String getChecksumQuietly(final byte[] bytes, final Algorithm algorithm)
	{
		try
		{
			return ChecksumExtensions.getChecksum(bytes, algorithm.getAlgorithm());
		}
		catch (final NoSuchAlgorithmException e)
		{
			log.error("getChecksumQuietly failed...\n" + e.getMessage(), e);
		}
		return null;
	}

	/**
	 * Gets the checksum quietly from the given byte array with an instance of.
	 *
	 * @param bytes
	 *            the byte array.
	 * @param algorithm
	 *            the algorithm to get the checksum. This could be for instance "MD4", "MD5",
	 *            "SHA-1", "SHA-256", "SHA-384" or "SHA-512".
	 * @return The checksum from the file as a String object.
	 */
	public static String getChecksumQuietly(final byte[] bytes, final String algorithm)
	{
		try
		{
			return ChecksumExtensions.getChecksum(bytes, algorithm);
		}
		catch (final NoSuchAlgorithmException e)
		{
			log.error("getChecksumQuietly failed...\n" + e.getMessage(), e);
		}
		return algorithm;
	}

	/**
	 * Gets the checksum quietly from the given byte array with an instance of.
	 *
	 * @param bytes
	 *            the Byte object array.
	 * @param algorithm
	 *            the algorithm to get the checksum. This could be for instance "MD4", "MD5",
	 *            "SHA-1", "SHA-256", "SHA-384" or "SHA-512".
	 * @return The checksum from the file as a String object.
	 */
	public static String getChecksumQuietly(final Byte[] bytes, final String algorithm)
	{

		try
		{
			return ChecksumExtensions.getChecksum(bytes, algorithm);
		}
		catch (final NoSuchAlgorithmException e)
		{
			log.error("getChecksumQuietly failed...\n" + e.getMessage(), e);
		}
		return algorithm;
	}

	/**
	 * Gets the checksum quietly from the given file with an instance of the given algorithm.
	 *
	 * @param file
	 *            the file.
	 * @param algorithm
	 *            the algorithm to get the checksum. This could be for instance "MD4", "MD5",
	 *            "SHA-1", "SHA-256", "SHA-384" or "SHA-512".
	 * @return The checksum from the file as a String object or null if a NoSuchAlgorithmException
	 *         will be thrown. exists. {@link java.security.MessageDigest} object.
	 */
	public static String getChecksumQuietly(final File file, final Algorithm algorithm)
	{
		try
		{
			return ChecksumExtensions.getChecksum(file, algorithm.getAlgorithm());
		}
		catch (final NoSuchAlgorithmException e)
		{
			log.error("getChecksumQuietly failed...\n" + e.getMessage(), e);
		}
		return null;
	}
	
}
