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

import lombok.Getter;

/**
 * The enum {@link Protocol} represents protocols from an url.
 * 
 * @deprecated use instead the same name class in the net-extensions project <br>
 *             <br>
 *             Note: This method will be removed in next minor release.
 */
@Deprecated
public enum Protocol
{

	/** The ear protocol. */
	EAR("ear"),
	/** The jar protocol. */
	FILE("file"),
	/** The ftp protocol. */
	FTP("ftp"),
	/** The http protocol. */
	HTTP("http"),
	/** The https protocol. */
	HTTPS("https"),
	/** The jar protocol. */
	JAR("jar"),
	/** The jar protocol. */
	MAILTO("mailto"),
	/** The jar protocol. */
	NEWS("mailto"),
	/** The nntp protocol. */
	NNTP("nntp"),
	/** The urn protocol. */
	URN("urn"),
	/** The war protocol. */
	WAR("war");

	/** The protocol. */

	/**
	 * Gets the protocol.
	 *
	 * @return the protocol
	 */
	@Getter
	private final String protocol;

	/**
	 * Instantiates a new {@link Protocol}.
	 * 
	 * @param protocol
	 *            the protocol
	 */
	private Protocol(final String protocol)
	{
		this.protocol = protocol;
	}

}
