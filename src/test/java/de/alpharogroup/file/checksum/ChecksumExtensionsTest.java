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

import static org.testng.AssertJUnit.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.lang3.ArrayUtils;
import org.meanbean.factories.ObjectCreationException;
import org.meanbean.test.BeanTestException;
import org.meanbean.test.BeanTester;
import org.testng.annotations.Test;

import de.alpharogroup.crypto.algorithm.Algorithm;
import de.alpharogroup.crypto.algorithm.HashAlgorithm;
import de.alpharogroup.crypto.algorithm.MdAlgorithm;
import de.alpharogroup.file.FileTestCase;
import de.alpharogroup.file.search.PathFinder;

/**
 * The unit test class for the class {@link ChecksumExtensions}.
 */
public class ChecksumExtensionsTest extends FileTestCase
{

	/**
	 * Test method for {@link ChecksumExtensions#getCheckSumAdler32(byte[])}.
	 *
	 * @throws UnsupportedEncodingException
	 *             is thrown if the named charset is not supported.
	 */
	@Test
	public void testGetCheckSumAdler32ByteArray() throws UnsupportedEncodingException
	{
		long expected;
		long actual;
		final String secretMessage = "secret Message";
		final byte[] secretMessageBytes = secretMessage.getBytes("UTF-8");
		actual = ChecksumExtensions.getCheckSumAdler32(secretMessageBytes);
		expected = 685966700l;
		assertEquals(expected, actual);

	}

	/**
	 * Test method for {@link ChecksumExtensions#getCheckSumAdler32(File)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testGetCheckSumAdler32File() throws IOException
	{
		long expected;
		long actual;
		final File testFile = new File(PathFinder.getSrcTestResourcesDir(),
			"testReadFileInput.txt");
		actual = ChecksumExtensions.getCheckSumAdler32(testFile);
		expected = 3296728756l;
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link ChecksumExtensions#getChecksum(byte[], Algorithm)}.
	 *
	 * @throws UnsupportedEncodingException
	 *             If the named charset is not supported
	 * @throws NoSuchAlgorithmException
	 *             Is thrown if the algorithm is not supported or does not exists.
	 *             {@link java.security.MessageDigest} object.
	 */
	@Test
	public void testGetChecksumByteArrayAlgorithm()
		throws UnsupportedEncodingException, NoSuchAlgorithmException
	{
		String expected;
		String actual;
		final String secretMessage = "secret Message";
		final byte[] secretMessageBytes = secretMessage.getBytes("UTF-8");
		expected = "5cc16e663491726545c13ec2012f4601";
		actual = ChecksumExtensions.getChecksum(secretMessageBytes, MdAlgorithm.MD2);
		assertEquals(expected, actual);

		expected = "25659bd9db98ecc3c2077d44e69607b8";
		actual = ChecksumExtensions.getChecksum(secretMessageBytes, MdAlgorithm.MD5);
		assertEquals(expected, actual);

		expected = "874026e54b67d4f9aaf87cb14a683fb51de6f9cb";
		actual = ChecksumExtensions.getChecksum(secretMessageBytes, HashAlgorithm.SHA_1);
		assertEquals(expected, actual);

		expected = "8a3b3c92a8b0eb00da917c23201a9407ef7963373464076aec4c54c066e8b7aa";
		actual = ChecksumExtensions.getChecksum(secretMessageBytes, HashAlgorithm.SHA_256);
		assertEquals(expected, actual);

		expected = "b58a362687ab42b9bf0d8af0b4860ed262d1fd128e16ab0082723e7785a862cd129b03577312452cc24aecdb36d5406d";
		actual = ChecksumExtensions.getChecksum(secretMessageBytes, HashAlgorithm.SHA_384);
		assertEquals(expected, actual);

		expected = "ab29b34a26547ca4ce517d776885a5642929d9ed571a990fc764f7d0b854d6546276ca9aa45b3d88db3dc3dbf3c2f2152017d3e3e054ed6cd7a38a1f7925a746";
		actual = ChecksumExtensions.getChecksum(secretMessageBytes, HashAlgorithm.SHA_512);
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link ChecksumExtensions#getChecksum(byte[], String)}.
	 *
	 * @throws UnsupportedEncodingException
	 *             If the named charset is not supported
	 * @throws NoSuchAlgorithmException
	 *             Is thrown if the algorithm is not supported or does not exists.
	 *             {@link java.security.MessageDigest} object.
	 */
	@Test
	public void testGetChecksumByteArrayString()
		throws UnsupportedEncodingException, NoSuchAlgorithmException
	{
		String expected;
		String actual;
		final String secretMessage = "secret Message";
		final byte[] secretMessageBytes = secretMessage.getBytes("UTF-8");
		expected = "5cc16e663491726545c13ec2012f4601";
		actual = ChecksumExtensions.getChecksum(secretMessageBytes, MdAlgorithm.MD2.getAlgorithm());
		assertEquals(expected, actual);

		expected = "25659bd9db98ecc3c2077d44e69607b8";
		actual = ChecksumExtensions.getChecksum(secretMessageBytes, MdAlgorithm.MD5.getAlgorithm());
		assertEquals(expected, actual);

		expected = "874026e54b67d4f9aaf87cb14a683fb51de6f9cb";
		actual = ChecksumExtensions.getChecksum(secretMessageBytes,
			HashAlgorithm.SHA_1.getAlgorithm());
		assertEquals(expected, actual);

		expected = "8a3b3c92a8b0eb00da917c23201a9407ef7963373464076aec4c54c066e8b7aa";
		actual = ChecksumExtensions.getChecksum(secretMessageBytes,
			HashAlgorithm.SHA_256.getAlgorithm());
		assertEquals(expected, actual);

		expected = "b58a362687ab42b9bf0d8af0b4860ed262d1fd128e16ab0082723e7785a862cd129b03577312452cc24aecdb36d5406d";
		actual = ChecksumExtensions.getChecksum(secretMessageBytes,
			HashAlgorithm.SHA_384.getAlgorithm());
		assertEquals(expected, actual);

		expected = "ab29b34a26547ca4ce517d776885a5642929d9ed571a990fc764f7d0b854d6546276ca9aa45b3d88db3dc3dbf3c2f2152017d3e3e054ed6cd7a38a1f7925a746";
		actual = ChecksumExtensions.getChecksum(secretMessageBytes,
			HashAlgorithm.SHA_512.getAlgorithm());
		assertEquals(expected, actual);
	}

	/**
	 * Test for {@link ChecksumExtensions#getChecksum(Byte[], Algorithm)}.
	 *
	 * @throws NoSuchAlgorithmException
	 *             Is thrown if the algorithm is not supported or does not exists.
	 *             {@link java.security.MessageDigest} object.
	 * @throws UnsupportedEncodingException
	 *             If the named charset is not supported
	 */
	@Test
	public void testGetChecksumByteObjectArrayAlgorithm()
		throws NoSuchAlgorithmException, UnsupportedEncodingException
	{
		String expected;
		String actual;
		final String secretMessage = "secret Message";
		final byte[] sbytes = secretMessage.getBytes("UTF-8");
		final Byte[] secretMessageBytes = ArrayUtils.toObject(sbytes);
		expected = "5cc16e663491726545c13ec2012f4601";
		actual = ChecksumExtensions.getChecksum(secretMessageBytes, MdAlgorithm.MD2);
		assertEquals(expected, actual);

		expected = "25659bd9db98ecc3c2077d44e69607b8";
		actual = ChecksumExtensions.getChecksum(secretMessageBytes, MdAlgorithm.MD5);
		assertEquals(expected, actual);

		expected = "874026e54b67d4f9aaf87cb14a683fb51de6f9cb";
		actual = ChecksumExtensions.getChecksum(secretMessageBytes, HashAlgorithm.SHA_1);
		assertEquals(expected, actual);

		expected = "8a3b3c92a8b0eb00da917c23201a9407ef7963373464076aec4c54c066e8b7aa";
		actual = ChecksumExtensions.getChecksum(secretMessageBytes, HashAlgorithm.SHA_256);
		assertEquals(expected, actual);

		expected = "b58a362687ab42b9bf0d8af0b4860ed262d1fd128e16ab0082723e7785a862cd129b03577312452cc24aecdb36d5406d";
		actual = ChecksumExtensions.getChecksum(secretMessageBytes, HashAlgorithm.SHA_384);
		assertEquals(expected, actual);

		expected = "ab29b34a26547ca4ce517d776885a5642929d9ed571a990fc764f7d0b854d6546276ca9aa45b3d88db3dc3dbf3c2f2152017d3e3e054ed6cd7a38a1f7925a746";
		actual = ChecksumExtensions.getChecksum(secretMessageBytes, HashAlgorithm.SHA_512);
		assertEquals(expected, actual);

	}

	/**
	 * Test for {@link ChecksumExtensions#getChecksum(Byte[], String)}.
	 *
	 * @throws UnsupportedEncodingException
	 *             If the named charset is not supported
	 * @throws NoSuchAlgorithmException
	 *             Is thrown if the algorithm is not supported or does not exists.
	 *             {@link java.security.MessageDigest} object.
	 */
	@Test
	public void testGetChecksumByteObjectArrayString()
		throws UnsupportedEncodingException, NoSuchAlgorithmException
	{
		String expected;
		String actual;
		final String secretMessage = "secret Message";
		final byte[] sbytes = secretMessage.getBytes("UTF-8");
		final Byte[] secretMessageBytes = ArrayUtils.toObject(sbytes);

		expected = "5cc16e663491726545c13ec2012f4601";
		actual = ChecksumExtensions.getChecksum(secretMessageBytes, MdAlgorithm.MD2.getAlgorithm());
		assertEquals(expected, actual);

		expected = "25659bd9db98ecc3c2077d44e69607b8";
		actual = ChecksumExtensions.getChecksum(secretMessageBytes, MdAlgorithm.MD5.getAlgorithm());
		assertEquals(expected, actual);

		expected = "874026e54b67d4f9aaf87cb14a683fb51de6f9cb";
		actual = ChecksumExtensions.getChecksum(secretMessageBytes,
			HashAlgorithm.SHA_1.getAlgorithm());
		assertEquals(expected, actual);

		expected = "8a3b3c92a8b0eb00da917c23201a9407ef7963373464076aec4c54c066e8b7aa";
		actual = ChecksumExtensions.getChecksum(secretMessageBytes,
			HashAlgorithm.SHA_256.getAlgorithm());
		assertEquals(expected, actual);

		expected = "b58a362687ab42b9bf0d8af0b4860ed262d1fd128e16ab0082723e7785a862cd129b03577312452cc24aecdb36d5406d";
		actual = ChecksumExtensions.getChecksum(secretMessageBytes,
			HashAlgorithm.SHA_384.getAlgorithm());
		assertEquals(expected, actual);

		expected = "ab29b34a26547ca4ce517d776885a5642929d9ed571a990fc764f7d0b854d6546276ca9aa45b3d88db3dc3dbf3c2f2152017d3e3e054ed6cd7a38a1f7925a746";
		actual = ChecksumExtensions.getChecksum(secretMessageBytes,
			HashAlgorithm.SHA_512.getAlgorithm());
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link ChecksumExtensions#getCheckSumCRC32(byte[])}.
	 * 
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testGetCheckSumCRC32ByteArray() throws UnsupportedEncodingException
	{
		long expected;
		long actual;
		final String secretMessage = "secret Message";
		final byte[] secretMessageBytes = secretMessage.getBytes("UTF-8");
		actual = ChecksumExtensions.getCheckSumCRC32(secretMessageBytes);
		expected = 711998200l;
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link ChecksumExtensions#getCheckSumCRC32(File)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testGetCheckSumCRC32File() throws IOException
	{
		long expected;
		long actual;
		final File testFile = new File(PathFinder.getSrcTestResourcesDir(),
			"testReadFileInput.txt");
		actual = ChecksumExtensions.getCheckSumCRC32(testFile);
		expected = 197057321l;
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link ChecksumExtensions#getChecksum(File, Algorithm)}.
	 *
	 * @throws NoSuchAlgorithmException
	 *             is thrown if instantiation of the SecretKeyFactory object fails.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testGetChecksumFileAlgorithm() throws NoSuchAlgorithmException, IOException
	{
		String expected;
		String actual;

		final File testFile = new File(PathFinder.getSrcTestResourcesDir(),
			"testReadFileInput.txt");

		expected = "f57f8379e8c62db6135f14d93a84ffd3";
		actual = ChecksumExtensions.getChecksum(testFile, MdAlgorithm.MD2);
		assertEquals(expected, actual);

		expected = "3a37a2c10a590785dbfb9ce3b15b0464";
		actual = ChecksumExtensions.getChecksum(testFile, MdAlgorithm.MD5);
		assertEquals(expected, actual);

		expected = "496dfa0ecf50cc6e3eda41fd3258272c2f2f0ff1";
		actual = ChecksumExtensions.getChecksum(testFile, HashAlgorithm.SHA_1);
		assertEquals(expected, actual);

		expected = "94151a5c66422a9adf706937eeb7fafec25032c380b55b0e92695baf297fb747";
		actual = ChecksumExtensions.getChecksum(testFile, HashAlgorithm.SHA_256);
		assertEquals(expected, actual);

		expected = "c1bc0091901a944828ca56f236f068d318086a55b96e045b1e7415df1449eb9c8e54546fec4b759ad2c6f7e3fbab7561";
		actual = ChecksumExtensions.getChecksum(testFile, HashAlgorithm.SHA_384);
		assertEquals(expected, actual);

		expected = "4d0c14f299254e58dcea1f524ca08af5f0776b1f5070919a859b92c2ab350635375862ab0727fd5e34ff35da837bd836a17047544db8df63adc4912211ea7f02";
		actual = ChecksumExtensions.getChecksum(testFile, HashAlgorithm.SHA_512);
		assertEquals(expected, actual);

	}

	/**
	 * Test method for {@link ChecksumExtensions#getChecksum(File, boolean)}.
	 *
	 * @throws FileNotFoundException
	 *             the file not found exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testGetChecksumFileBoolean() throws FileNotFoundException, IOException
	{
		long expected;
		long actual;

		final File testFile = new File(PathFinder.getSrcTestResourcesDir(),
			"testReadFileInput.txt");

		expected = 197057321l;
		actual = ChecksumExtensions.getChecksum(testFile, true);
		assertEquals(expected, actual);

		expected = 3296728756l;
		actual = ChecksumExtensions.getChecksum(testFile, false);
		assertEquals(expected, actual);

	}

	/**
	 * Test method for {@link ChecksumExtensions#getChecksum(File, String)}.
	 *
	 * @throws NoSuchAlgorithmException
	 *             is thrown if instantiation of the SecretKeyFactory object fails.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testGetChecksumFileString() throws NoSuchAlgorithmException, IOException
	{
		String expected;
		String actual;

		final File testFile = new File(PathFinder.getSrcTestResourcesDir(),
			"testReadFileInput.txt");

		expected = "f57f8379e8c62db6135f14d93a84ffd3";
		actual = ChecksumExtensions.getChecksum(testFile, MdAlgorithm.MD2.getAlgorithm());
		assertEquals(expected, actual);

		expected = "3a37a2c10a590785dbfb9ce3b15b0464";
		actual = ChecksumExtensions.getChecksum(testFile, MdAlgorithm.MD5.getAlgorithm());
		assertEquals(expected, actual);

		expected = "496dfa0ecf50cc6e3eda41fd3258272c2f2f0ff1";
		actual = ChecksumExtensions.getChecksum(testFile, HashAlgorithm.SHA_1.getAlgorithm());
		assertEquals(expected, actual);

		expected = "94151a5c66422a9adf706937eeb7fafec25032c380b55b0e92695baf297fb747";
		actual = ChecksumExtensions.getChecksum(testFile, HashAlgorithm.SHA_256.getAlgorithm());
		assertEquals(expected, actual);

		expected = "c1bc0091901a944828ca56f236f068d318086a55b96e045b1e7415df1449eb9c8e54546fec4b759ad2c6f7e3fbab7561";
		actual = ChecksumExtensions.getChecksum(testFile, HashAlgorithm.SHA_384.getAlgorithm());
		assertEquals(expected, actual);

		expected = "4d0c14f299254e58dcea1f524ca08af5f0776b1f5070919a859b92c2ab350635375862ab0727fd5e34ff35da837bd836a17047544db8df63adc4912211ea7f02";
		actual = ChecksumExtensions.getChecksum(testFile, HashAlgorithm.SHA_512.getAlgorithm());
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link ChecksumExtensions#matchesMD5(String)}.
	 */
	@Test
	public void testMatchesMD5()
	{
		boolean expected;
		boolean actual;
		actual = ChecksumExtensions.matchesMD5("3a37a2c10a590785dbfb9ce3b15b0464");
		expected = true;
		assertEquals(expected, actual);

		actual = ChecksumExtensions.matchesMD5("496dfa0ecf50cc6e3eda41fd3258272c2f2f0ff1");
		expected = false;
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link ChecksumExtensions#matchesSHA1(String)}.
	 */
	@Test
	public void testMatchesSHA1()
	{
		boolean expected;
		boolean actual;
		actual = ChecksumExtensions.matchesSHA1("496dfa0ecf50cc6e3eda41fd3258272c2f2f0ff1");
		expected = true;
		assertEquals(expected, actual);

		actual = ChecksumExtensions.matchesSHA1("3a37a2c10a590785dbfb9ce3b15b0464");
		expected = false;
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link ChecksumExtensions#matchesSHA512(String)}.
	 */
	@Test
	public void testMatchesSHA512()
	{
		boolean expected;
		boolean actual;
		actual = ChecksumExtensions.matchesSHA512(
			"4d0c14f299254e58dcea1f524ca08af5f0776b1f5070919a859b92c2ab350635375862ab0727fd5e34ff35da837bd836a17047544db8df63adc4912211ea7f02");
		expected = true;
		assertEquals(expected, actual);

		actual = ChecksumExtensions.matchesSHA512("496dfa0ecf50cc6e3eda41fd3258272c2f2f0ff1");
		expected = false;
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link ChecksumExtensions}
	 */
	@Test(expectedExceptions = { BeanTestException.class, ObjectCreationException.class })
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(ChecksumExtensions.class);
	}

}
