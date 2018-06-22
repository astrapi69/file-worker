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
import java.io.UnsupportedEncodingException;

import org.apache.commons.lang3.ArrayUtils;
import org.meanbean.factories.ObjectCreationException;
import org.meanbean.test.BeanTestException;
import org.meanbean.test.BeanTester;
import org.testng.annotations.Test;

import de.alpharogroup.crypto.algorithm.Algorithm;
import de.alpharogroup.crypto.algorithm.HashAlgorithm;
import de.alpharogroup.crypto.algorithm.MdAlgorithm;
import de.alpharogroup.file.search.PathFinder;

/**
 * The unit test class for the class {@link ChecksumQuietlyExtensions}
 */
public class ChecksumQuietlyExtensionsTest
{

	/**
	 * Test method for {@link ChecksumQuietlyExtensions#getChecksumQuietly(byte[], Algorithm)}.
	 */
	@Test
	public void testGetChecksumQuietlyByteArrayAlgorithm()
	{
		String expected;
		String actual;

		final File testFile = new File(PathFinder.getSrcTestResourcesDir(),
			"testReadFileInput.txt");

		expected = "f57f8379e8c62db6135f14d93a84ffd3";
		actual = ChecksumQuietlyExtensions.getChecksumQuietly(testFile, MdAlgorithm.MD2);
		assertEquals(expected, actual);

		expected = "3a37a2c10a590785dbfb9ce3b15b0464";
		actual = ChecksumQuietlyExtensions.getChecksumQuietly(testFile, MdAlgorithm.MD5);
		assertEquals(expected, actual);

		expected = "496dfa0ecf50cc6e3eda41fd3258272c2f2f0ff1";
		actual = ChecksumQuietlyExtensions.getChecksumQuietly(testFile, HashAlgorithm.SHA_1);
		assertEquals(expected, actual);

		expected = "94151a5c66422a9adf706937eeb7fafec25032c380b55b0e92695baf297fb747";
		actual = ChecksumQuietlyExtensions.getChecksumQuietly(testFile, HashAlgorithm.SHA_256);
		assertEquals(expected, actual);

		expected = "c1bc0091901a944828ca56f236f068d318086a55b96e045b1e7415df1449eb9c8e54546fec4b759ad2c6f7e3fbab7561";
		actual = ChecksumQuietlyExtensions.getChecksumQuietly(testFile, HashAlgorithm.SHA_384);
		assertEquals(expected, actual);

		expected = "4d0c14f299254e58dcea1f524ca08af5f0776b1f5070919a859b92c2ab350635375862ab0727fd5e34ff35da837bd836a17047544db8df63adc4912211ea7f02";
		actual = ChecksumQuietlyExtensions.getChecksumQuietly(testFile, HashAlgorithm.SHA_512);
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link ChecksumQuietlyExtensions#getChecksumQuietly(byte[], String)}.
	 * 
	 * @throws UnsupportedEncodingException
	 *             if the named charset is not supported
	 */
	@Test
	public void testGetChecksumQuietlyByteArrayString() throws UnsupportedEncodingException
	{
		String expected;
		String actual;
		final String secretMessage = "secret Message";
		final byte[] secretMessageBytes = secretMessage.getBytes("UTF-8");
		expected = "5cc16e663491726545c13ec2012f4601";
		actual = ChecksumQuietlyExtensions.getChecksumQuietly(secretMessageBytes,
			MdAlgorithm.MD2.getAlgorithm());
		assertEquals(expected, actual);

		expected = "25659bd9db98ecc3c2077d44e69607b8";
		actual = ChecksumQuietlyExtensions.getChecksumQuietly(secretMessageBytes,
			MdAlgorithm.MD5.getAlgorithm());
		assertEquals(expected, actual);

		expected = "874026e54b67d4f9aaf87cb14a683fb51de6f9cb";
		actual = ChecksumQuietlyExtensions.getChecksumQuietly(secretMessageBytes,
			HashAlgorithm.SHA_1.getAlgorithm());
		assertEquals(expected, actual);

		expected = "8a3b3c92a8b0eb00da917c23201a9407ef7963373464076aec4c54c066e8b7aa";
		actual = ChecksumQuietlyExtensions.getChecksumQuietly(secretMessageBytes,
			HashAlgorithm.SHA_256.getAlgorithm());
		assertEquals(expected, actual);

		expected = "b58a362687ab42b9bf0d8af0b4860ed262d1fd128e16ab0082723e7785a862cd129b03577312452cc24aecdb36d5406d";
		actual = ChecksumQuietlyExtensions.getChecksumQuietly(secretMessageBytes,
			HashAlgorithm.SHA_384.getAlgorithm());
		assertEquals(expected, actual);

		expected = "ab29b34a26547ca4ce517d776885a5642929d9ed571a990fc764f7d0b854d6546276ca9aa45b3d88db3dc3dbf3c2f2152017d3e3e054ed6cd7a38a1f7925a746";
		actual = ChecksumQuietlyExtensions.getChecksumQuietly(secretMessageBytes,
			HashAlgorithm.SHA_512.getAlgorithm());
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link ChecksumQuietlyExtensions#getChecksumQuietly(Byte[], String)}.
	 * 
	 * @throws UnsupportedEncodingException
	 *             if the named charset is not supported
	 */
	@Test
	public void testGetChecksumQuietlyByteArrayString1() throws UnsupportedEncodingException
	{
		String expected;
		String actual;
		final String secretMessage = "secret Message";
		final byte[] sbytes = secretMessage.getBytes("UTF-8");
		final Byte[] secretMessageBytes = ArrayUtils.toObject(sbytes);

		expected = "5cc16e663491726545c13ec2012f4601";
		actual = ChecksumQuietlyExtensions.getChecksumQuietly(secretMessageBytes,
			MdAlgorithm.MD2.getAlgorithm());
		assertEquals(expected, actual);

		expected = "25659bd9db98ecc3c2077d44e69607b8";
		actual = ChecksumQuietlyExtensions.getChecksumQuietly(secretMessageBytes,
			MdAlgorithm.MD5.getAlgorithm());
		assertEquals(expected, actual);

		expected = "874026e54b67d4f9aaf87cb14a683fb51de6f9cb";
		actual = ChecksumQuietlyExtensions.getChecksumQuietly(secretMessageBytes,
			HashAlgorithm.SHA_1.getAlgorithm());
		assertEquals(expected, actual);

		expected = "8a3b3c92a8b0eb00da917c23201a9407ef7963373464076aec4c54c066e8b7aa";
		actual = ChecksumQuietlyExtensions.getChecksumQuietly(secretMessageBytes,
			HashAlgorithm.SHA_256.getAlgorithm());
		assertEquals(expected, actual);

		expected = "b58a362687ab42b9bf0d8af0b4860ed262d1fd128e16ab0082723e7785a862cd129b03577312452cc24aecdb36d5406d";
		actual = ChecksumQuietlyExtensions.getChecksumQuietly(secretMessageBytes,
			HashAlgorithm.SHA_384.getAlgorithm());
		assertEquals(expected, actual);

		expected = "ab29b34a26547ca4ce517d776885a5642929d9ed571a990fc764f7d0b854d6546276ca9aa45b3d88db3dc3dbf3c2f2152017d3e3e054ed6cd7a38a1f7925a746";
		actual = ChecksumQuietlyExtensions.getChecksumQuietly(secretMessageBytes,
			HashAlgorithm.SHA_512.getAlgorithm());
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link ChecksumQuietlyExtensions#getChecksumQuietly(File, Algorithm)}.
	 */
	@Test
	public void testGetChecksumQuietlyFileAlgorithm()
	{
		String expected;
		String actual;

		final File testFile = new File(PathFinder.getSrcTestResourcesDir(),
			"testReadFileInput.txt");

		expected = "f57f8379e8c62db6135f14d93a84ffd3";
		actual = ChecksumQuietlyExtensions.getChecksumQuietly(testFile, MdAlgorithm.MD2);
		assertEquals(expected, actual);

		expected = "3a37a2c10a590785dbfb9ce3b15b0464";
		actual = ChecksumQuietlyExtensions.getChecksumQuietly(testFile, MdAlgorithm.MD5);
		assertEquals(expected, actual);

		expected = "496dfa0ecf50cc6e3eda41fd3258272c2f2f0ff1";
		actual = ChecksumQuietlyExtensions.getChecksumQuietly(testFile, HashAlgorithm.SHA_1);
		assertEquals(expected, actual);

		expected = "94151a5c66422a9adf706937eeb7fafec25032c380b55b0e92695baf297fb747";
		actual = ChecksumQuietlyExtensions.getChecksumQuietly(testFile, HashAlgorithm.SHA_256);
		assertEquals(expected, actual);

		expected = "c1bc0091901a944828ca56f236f068d318086a55b96e045b1e7415df1449eb9c8e54546fec4b759ad2c6f7e3fbab7561";
		actual = ChecksumQuietlyExtensions.getChecksumQuietly(testFile, HashAlgorithm.SHA_384);
		assertEquals(expected, actual);

		expected = "4d0c14f299254e58dcea1f524ca08af5f0776b1f5070919a859b92c2ab350635375862ab0727fd5e34ff35da837bd836a17047544db8df63adc4912211ea7f02";
		actual = ChecksumQuietlyExtensions.getChecksumQuietly(testFile, HashAlgorithm.SHA_512);
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link ChecksumQuietlyExtensions}
	 */
	@Test(expectedExceptions = { BeanTestException.class, ObjectCreationException.class })
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(ChecksumQuietlyExtensions.class);
	}

}
