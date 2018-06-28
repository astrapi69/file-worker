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
package de.alpharogroup.file.url;


import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;

import org.testng.annotations.Test;

import de.alpharogroup.file.zip.ZipTestCase;

/**
 * The unit test class for the class {@link URLExtensions}.
 */
public class URLExtensionsTest extends ZipTestCase
{

	/**
	 * Test method for {@link URLExtensions#isJar(URL)}
	 *
	 * @throws MalformedURLException
	 *             is thrown if no protocol is specified, or an unknown protocol is found, or
	 *             {@code spec} is {@code null}.
	 * @see java.net.URL#URL(java.net.URL, java.lang.String)
	 */
	@SuppressWarnings("deprecation")
	@Test
	public void testIsJar() throws MalformedURLException
	{
		final URL myURL = new URL("http://example.com/");
		assertFalse(URLExtensions.isJar(myURL));
		final URL jarUrl = new URL("jar:file:/home/root/jdk/rt.jar!/test.xml");
		assertTrue(URLExtensions.isJar(jarUrl));
	}

}
