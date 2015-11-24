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

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.lang.ArrayUtils;
import org.junit.Assert;
import org.junit.Test;

public class ChecksumUtilsTest
{

	/**
	 * Test for {@link ChecksumUtils#getChecksum(byte[], Algorithm)}
	 * 
	 * @throws UnsupportedEncodingException
	 *             If the named charset is not supported
	 * @throws NoSuchAlgorithmException
	 *             Is thrown if the algorithm is not supported or does not exists.
	 *             {@link java.security.MessageDigest} object.
	 */
	@Test
	public void testGetChecksumByteArrayAlgorithm() throws UnsupportedEncodingException,
		NoSuchAlgorithmException
	{
		final String secretMessage = "secret Message";
		final byte[] secretMessageBytes = secretMessage.getBytes("UTF-8");
		String expected = "5cc16e663491726545c13ec2012f4601";
		String actual = ChecksumUtils.getChecksum(secretMessageBytes, Algorithm.MD2);
		Assert.assertEquals(expected, actual);

		expected = "25659bd9db98ecc3c2077d44e69607b8";
		actual = ChecksumUtils.getChecksum(secretMessageBytes, Algorithm.MD5);
		Assert.assertEquals(expected, actual);

		expected = "874026e54b67d4f9aaf87cb14a683fb51de6f9cb";
		actual = ChecksumUtils.getChecksum(secretMessageBytes, Algorithm.SHA_1);
		Assert.assertEquals(expected, actual);

		expected = "8a3b3c92a8b0eb00da917c23201a9407ef7963373464076aec4c54c066e8b7aa";
		actual = ChecksumUtils.getChecksum(secretMessageBytes, Algorithm.SHA_256);
		Assert.assertEquals(expected, actual);

		expected = "b58a362687ab42b9bf0d8af0b4860ed262d1fd128e16ab0082723e7785a862cd129b03577312452cc24aecdb36d5406d";
		actual = ChecksumUtils.getChecksum(secretMessageBytes, Algorithm.SHA_384);
		Assert.assertEquals(expected, actual);

		expected = "ab29b34a26547ca4ce517d776885a5642929d9ed571a990fc764f7d0b854d6546276ca9aa45b3d88db3dc3dbf3c2f2152017d3e3e054ed6cd7a38a1f7925a746";
		actual = ChecksumUtils.getChecksum(secretMessageBytes, Algorithm.SHA_512);
		Assert.assertEquals(expected, actual);
	}

	/**
	 * Test for {@link ChecksumUtils#getChecksum(byte[], String)}
	 * 
	 * @throws UnsupportedEncodingException
	 *             If the named charset is not supported
	 * @throws NoSuchAlgorithmException
	 *             Is thrown if the algorithm is not supported or does not exists.
	 *             {@link java.security.MessageDigest} object.
	 */
	@Test
	public void testGetChecksumByteArrayString() throws UnsupportedEncodingException,
		NoSuchAlgorithmException
	{
		final String secretMessage = "secret Message";
		final byte[] secretMessageBytes = secretMessage.getBytes("UTF-8");
		String expected = "5cc16e663491726545c13ec2012f4601";
		String actual = ChecksumUtils.getChecksum(secretMessageBytes, Algorithm.MD2.getAlgorithm());
		Assert.assertEquals(expected, actual);

		expected = "25659bd9db98ecc3c2077d44e69607b8";
		actual = ChecksumUtils.getChecksum(secretMessageBytes, Algorithm.MD5.getAlgorithm());
		Assert.assertEquals(expected, actual);

		expected = "874026e54b67d4f9aaf87cb14a683fb51de6f9cb";
		actual = ChecksumUtils.getChecksum(secretMessageBytes, Algorithm.SHA_1.getAlgorithm());
		Assert.assertEquals(expected, actual);

		expected = "8a3b3c92a8b0eb00da917c23201a9407ef7963373464076aec4c54c066e8b7aa";
		actual = ChecksumUtils.getChecksum(secretMessageBytes, Algorithm.SHA_256.getAlgorithm());
		Assert.assertEquals(expected, actual);

		expected = "b58a362687ab42b9bf0d8af0b4860ed262d1fd128e16ab0082723e7785a862cd129b03577312452cc24aecdb36d5406d";
		actual = ChecksumUtils.getChecksum(secretMessageBytes, Algorithm.SHA_384.getAlgorithm());
		Assert.assertEquals(expected, actual);

		expected = "ab29b34a26547ca4ce517d776885a5642929d9ed571a990fc764f7d0b854d6546276ca9aa45b3d88db3dc3dbf3c2f2152017d3e3e054ed6cd7a38a1f7925a746";
		actual = ChecksumUtils.getChecksum(secretMessageBytes, Algorithm.SHA_512.getAlgorithm());
		Assert.assertEquals(expected, actual);
	}

	/**
	 * Test for {@link ChecksumUtils#getChecksum(Byte[], Algorithm)}
	 * 
	 * @throws UnsupportedEncodingException
	 *             If the named charset is not supported
	 * @throws NoSuchAlgorithmException
	 *             Is thrown if the algorithm is not supported or does not exists.
	 *             {@link java.security.MessageDigest} object.
	 */
	@Test
	public void testGetChecksumByteObjectArrayAlgorithm() throws NoSuchAlgorithmException,
		UnsupportedEncodingException
	{
		final String secretMessage = "secret Message";
		final byte[] sbytes = secretMessage.getBytes("UTF-8");
		final Byte[] secretMessageBytes = ArrayUtils.toObject(sbytes);
		String expected = "5cc16e663491726545c13ec2012f4601";
		String actual = ChecksumUtils.getChecksum(secretMessageBytes, Algorithm.MD2);
		Assert.assertEquals(expected, actual);

		expected = "25659bd9db98ecc3c2077d44e69607b8";
		actual = ChecksumUtils.getChecksum(secretMessageBytes, Algorithm.MD5);
		Assert.assertEquals(expected, actual);

		expected = "874026e54b67d4f9aaf87cb14a683fb51de6f9cb";
		actual = ChecksumUtils.getChecksum(secretMessageBytes, Algorithm.SHA_1);
		Assert.assertEquals(expected, actual);

		expected = "8a3b3c92a8b0eb00da917c23201a9407ef7963373464076aec4c54c066e8b7aa";
		actual = ChecksumUtils.getChecksum(secretMessageBytes, Algorithm.SHA_256);
		Assert.assertEquals(expected, actual);

		expected = "b58a362687ab42b9bf0d8af0b4860ed262d1fd128e16ab0082723e7785a862cd129b03577312452cc24aecdb36d5406d";
		actual = ChecksumUtils.getChecksum(secretMessageBytes, Algorithm.SHA_384);
		Assert.assertEquals(expected, actual);

		expected = "ab29b34a26547ca4ce517d776885a5642929d9ed571a990fc764f7d0b854d6546276ca9aa45b3d88db3dc3dbf3c2f2152017d3e3e054ed6cd7a38a1f7925a746";
		actual = ChecksumUtils.getChecksum(secretMessageBytes, Algorithm.SHA_512);
		Assert.assertEquals(expected, actual);

	}

	/**
	 * Test for {@link ChecksumUtils#getChecksum(Byte[], String)}
	 * 
	 * @throws UnsupportedEncodingException
	 *             If the named charset is not supported
	 * @throws NoSuchAlgorithmException
	 *             Is thrown if the algorithm is not supported or does not exists.
	 *             {@link java.security.MessageDigest} object.
	 */
	@Test
	public void testGetChecksumByteObjectArrayString() throws UnsupportedEncodingException,
		NoSuchAlgorithmException
	{

		final String secretMessage = "secret Message";
		final byte[] sbytes = secretMessage.getBytes("UTF-8");
		final Byte[] secretMessageBytes = ArrayUtils.toObject(sbytes);
		String expected = "5cc16e663491726545c13ec2012f4601";
		String actual = ChecksumUtils.getChecksum(secretMessageBytes, Algorithm.MD2.getAlgorithm());
		Assert.assertEquals(expected, actual);

		expected = "25659bd9db98ecc3c2077d44e69607b8";
		actual = ChecksumUtils.getChecksum(secretMessageBytes, Algorithm.MD5.getAlgorithm());
		Assert.assertEquals(expected, actual);

		expected = "874026e54b67d4f9aaf87cb14a683fb51de6f9cb";
		actual = ChecksumUtils.getChecksum(secretMessageBytes, Algorithm.SHA_1.getAlgorithm());
		Assert.assertEquals(expected, actual);

		expected = "8a3b3c92a8b0eb00da917c23201a9407ef7963373464076aec4c54c066e8b7aa";
		actual = ChecksumUtils.getChecksum(secretMessageBytes, Algorithm.SHA_256.getAlgorithm());
		Assert.assertEquals(expected, actual);

		expected = "b58a362687ab42b9bf0d8af0b4860ed262d1fd128e16ab0082723e7785a862cd129b03577312452cc24aecdb36d5406d";
		actual = ChecksumUtils.getChecksum(secretMessageBytes, Algorithm.SHA_384.getAlgorithm());
		Assert.assertEquals(expected, actual);

		expected = "ab29b34a26547ca4ce517d776885a5642929d9ed571a990fc764f7d0b854d6546276ca9aa45b3d88db3dc3dbf3c2f2152017d3e3e054ed6cd7a38a1f7925a746";
		actual = ChecksumUtils.getChecksum(secretMessageBytes, Algorithm.SHA_512.getAlgorithm());
		Assert.assertEquals(expected, actual);
	}

}
