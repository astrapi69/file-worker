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
package de.alpharogroup.file.checksum;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.Adler32;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.Checksum;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;

import de.alpharogroup.file.read.ReadFileUtils;
import de.alpharogroup.io.StreamUtils;

/**
 * The Class ChecksumUtils is a utility class for computing checksum from files and byte arrays.
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public final class ChecksumUtils
{

	/** The LOGGER. */
	protected static final Logger LOGGER = Logger.getLogger(ChecksumUtils.class.getName());


	/**
	 * Utility method for tests.
	 *
	 * @param data
	 *            the byte array
	 *
	 * @throws NoSuchAlgorithmException
	 *             the no such algorithm exception
	 */
	private static void checkWithByteArray(final byte[] data) throws NoSuchAlgorithmException
	{

		final Algorithm[] algorithms = Algorithm.values();
		for (final Algorithm algorithm : algorithms)
		{
			try
			{
				final String result = getChecksum(data, algorithm.getAlgorithm());
				LOGGER.info("getChecksum from " + algorithm + " algorithm:\t\t" + result);
			}
			catch (final NoSuchAlgorithmException e)
			{
				// do nothing
			}
		}
	}

	/**
	 * Utility method for tests.
	 *
	 * @param pom
	 *            the pom
	 *
	 * @throws NoSuchAlgorithmException
	 *             Is thrown if the algorithm is not supported or does not exists.
	 */
	private static void checkWithFile(final File pom) throws NoSuchAlgorithmException
	{
		final Algorithm[] algorithms = Algorithm.values();
		for (final Algorithm algorithm : algorithms)
		{
			try
			{
				final String result = getChecksum(pom, algorithm.getAlgorithm());
				LOGGER.info("getChecksum from " + algorithm + " algorithm:\t\t" + result);
			}
			catch (final NoSuchAlgorithmException e)
			{
				// do nothing
			}
		}
	}

	/**
	 * Gets the checksum from the given byte array with an instance of.
	 *
	 * @param bytes
	 *            the byte array.
	 * @param algorithm
	 *            the algorithm to get the checksum. This could be for instance "MD4", "MD5",
	 *            "SHA-1", "SHA-256", "SHA-384" or "SHA-512".
	 * @return The checksum from the file as a String object.
	 * @throws NoSuchAlgorithmException
	 *             Is thrown if the algorithm is not supported or does not exists.
	 *             {@link java.security.MessageDigest} object.
	 */
	public static String getChecksum(final byte[] bytes, final Algorithm algorithm)
		throws NoSuchAlgorithmException
	{
		return getChecksum(bytes, algorithm.getAlgorithm());
	}

	/**
	 * Gets the checksum from the given byte array with an instance of.
	 *
	 * @param bytes
	 *            the byte array.
	 * @param algorithm
	 *            the algorithm to get the checksum. This could be for instance "MD4", "MD5",
	 *            "SHA-1", "SHA-256", "SHA-384" or "SHA-512".
	 * @return The checksum from the file as a String object.
	 * @throws NoSuchAlgorithmException
	 *             Is thrown if the algorithm is not supported or does not exists.
	 *             {@link java.security.MessageDigest} object.
	 */
	public static String getChecksum(final byte[] bytes, final String algorithm)
		throws NoSuchAlgorithmException
	{
		final MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
		messageDigest.reset();
		messageDigest.update(bytes);
		final byte digest[] = messageDigest.digest();
		final StringBuilder hexView = new StringBuilder();
		for (final byte element : digest)
		{
			final String intAsHex = Integer.toHexString(0xFF & element);
			if (intAsHex.length() == 1)
			{
				hexView.append('0');
			}
			hexView.append(intAsHex);
		}
		return hexView.toString();
	}

	/**
	 * Gets the checksum from the given byte array with an instance of.
	 *
	 * @param bytes
	 *            the Byte object array.
	 * @param algorithm
	 *            the algorithm to get the checksum. This could be for instance "MD4", "MD5",
	 *            "SHA-1", "SHA-256", "SHA-384" or "SHA-512".
	 * @return The checksum from the file as a String object.
	 * @throws NoSuchAlgorithmException
	 *             Is thrown if the algorithm is not supported or does not exists.
	 *             {@link java.security.MessageDigest} object.
	 */
	public static String getChecksum(final Byte[] bytes, final Algorithm algorithm)
		throws NoSuchAlgorithmException
	{
		return getChecksum(bytes, algorithm.getAlgorithm());
	}

	/**
	 * Gets the checksum from the given byte array with an instance of.
	 *
	 * @param bytes
	 *            the Byte object array.
	 * @param algorithm
	 *            the algorithm to get the checksum. This could be for instance "MD4", "MD5",
	 *            "SHA-1", "SHA-256", "SHA-384" or "SHA-512".
	 * @return The checksum from the file as a String object.
	 * @throws NoSuchAlgorithmException
	 *             Is thrown if the algorithm is not supported or does not exists.
	 *             {@link java.security.MessageDigest} object.
	 */
	public static String getChecksum(final Byte[] bytes, final String algorithm)
		throws NoSuchAlgorithmException
	{
		return getChecksum(ArrayUtils.toPrimitive(bytes), algorithm);
	}

	/**
	 * Gets the checksum from the given file with an instance of the given algorithm.
	 *
	 * @param file
	 *            the file.
	 * @param algorithm
	 *            the algorithm to get the checksum. This could be for instance "MD4", "MD5",
	 *            "SHA-1", "SHA-256", "SHA-384" or "SHA-512".
	 * @return The checksum from the file as a String object.
	 * @throws NoSuchAlgorithmException
	 *             Is thrown if the algorithm is not supported or does not exists.
	 *             {@link java.security.MessageDigest} object.
	 */
	public static String getChecksum(final File file, final Algorithm algorithm)
		throws NoSuchAlgorithmException
	{
		return getChecksum(file, algorithm.getAlgorithm());
	}

	/**
	 * Gets the checksum from the given file. If the flag crc is true than the CheckedInputStream is
	 * constructed with an instance of <code>java.util.zip.CRC32</code> otherwise with an instance
	 * of <code>java.util.zip.Adler32</code>.
	 *
	 * @param file
	 *            The file The file from what to get the checksum.
	 * @param crc
	 *            The crc If the flag crc is true than the CheckedInputStream is constructed with an
	 *            instance of {@link java.util.zip.CRC32} object otherwise it is constructed with an
	 *            instance of
	 * @return The checksum from the given file as long.
	 * @throws FileNotFoundException
	 *             Is thrown if the file is not found.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred. {@link java.util.zip.CRC32} object
	 *             otherwise it is constructed with an instance of {@link java.util.zip.Adler32}
	 *             object. {@link java.util.zip.Adler32} object.
	 */
	public static long getChecksum(final File file, final boolean crc)
		throws FileNotFoundException, IOException
	{
		CheckedInputStream cis = null;
		if (crc)
		{
			cis = new CheckedInputStream(new FileInputStream(file), new CRC32());
		}
		else
		{
			cis = new CheckedInputStream(new FileInputStream(file), new Adler32());
		}
		final int length = (int)file.length();
		final byte[] buffer = new byte[length];
		long checksum = 0;
		while (cis.read(buffer) >= 0)
		{
			checksum = cis.getChecksum().getValue();
		}
		checksum = cis.getChecksum().getValue();
		StreamUtils.closeInputStream(cis);
		return checksum;
	}

	/**
	 * Gets the checksum from the given file with an instance of the given algorithm.
	 *
	 * @param file
	 *            the file.
	 * @param algorithm
	 *            the algorithm to get the checksum. This could be for instance "MD4", "MD5",
	 *            "SHA-1", "SHA-256", "SHA-384" or "SHA-512".
	 * @return The checksum from the file as a String object.
	 * @throws NoSuchAlgorithmException
	 *             Is thrown if the algorithm is not supported or does not exists.
	 *             {@link java.security.MessageDigest} object.
	 */
	public static String getChecksum(final File file, final String algorithm)
		throws NoSuchAlgorithmException
	{
		return getChecksum(ReadFileUtils.toByteArray(file), algorithm);
	}

	/**
	 * Gets the checksum from the given byte array with an instance of.
	 *
	 * @param bytes
	 *            The byte array.
	 * @return The checksum from the byte array as long. {@link java.util.zip.Adler32} object.
	 */
	public static long getCheckSumAdler32(final byte[] bytes)
	{
		final Checksum checksum = new Adler32();
		checksum.update(bytes, 0, bytes.length);
		final long cs = checksum.getValue();
		return cs;
	}

	/**
	 * Gets the checksum from the given file with an instance of.
	 *
	 * @param file
	 *            The file.
	 * @return The checksum from the file as long. {@link java.util.zip.Adler32} object.
	 */
	public static long getCheckSumAdler32(final File file)
	{
		return getCheckSumAdler32(ReadFileUtils.toByteArray(file));
	}

	/**
	 * Gets the checksum from the given byte array with an instance of.
	 *
	 * @param bytes
	 *            The byte array.
	 * @return The checksum from the byte array as long. {@link java.util.zip.CRC32} object.
	 */
	public static long getCheckSumCRC32(final byte[] bytes)
	{
		final Checksum checksum = new CRC32();
		checksum.update(bytes, 0, bytes.length);
		final long cs = checksum.getValue();
		return cs;
	}

	/**
	 * Gets the checksum from the given file with an instance of.
	 *
	 * @param file
	 *            The file.
	 * @return The checksum from the file as long. {@link java.util.zip.CRC32} object.
	 */
	public static long getCheckSumCRC32(final File file)
	{
		return getCheckSumCRC32(ReadFileUtils.toByteArray(file));
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
	public static String getChecksumQuietly(final byte[] bytes, final Algorithm algorithm)
	{
		try
		{
			return getChecksum(bytes, algorithm.getAlgorithm());
		}
		catch (final NoSuchAlgorithmException e)
		{
			LOGGER.error("getChecksumQuietly failed...\n" + e.getMessage(), e);
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
			return getChecksum(bytes, algorithm);
		}
		catch (final NoSuchAlgorithmException e)
		{
			LOGGER.error("getChecksumQuietly failed...\n" + e.getMessage(), e);
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
			return getChecksum(bytes, algorithm);
		}
		catch (final NoSuchAlgorithmException e)
		{
			LOGGER.error("getChecksumQuietly failed...\n" + e.getMessage(), e);
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
			return getChecksum(file, algorithm.getAlgorithm());
		}
		catch (final NoSuchAlgorithmException e)
		{
			LOGGER.error("getChecksumQuietly failed...\n" + e.getMessage(), e);
		}
		return null;
	}

	/**
	 * The main method.
	 *
	 * @param args
	 *            The args
	 * @throws FileNotFoundException
	 *             Is thrown if the file is not found.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws NoSuchAlgorithmException
	 *             Is thrown if the algorithm is not supported or does not exists.
	 */
	public static void main(final String[] args) throws FileNotFoundException, IOException,
		NoSuchAlgorithmException
	{

		if (args.length != 1)
		{
			System.err.println("Usage: java ChecksumCRC32 filename");
		}
		else
		{
			final File pom = new File(args[0]);

			checkWithFile(pom);

			long checksum = getChecksum(new File(args[0]), true);
			System.out.println("CRC32 checksum:" + checksum);
			checksum = getChecksum(new File(args[0]), false);
			System.out.println("Adler32 checksum:" + checksum);

			final byte[] ba = ReadFileUtils.readFileToBytearray(pom);

			checkWithByteArray(ba);

		}

	}

	/**
	 * Private constructor.
	 */
	private ChecksumUtils()
	{
	}

}
