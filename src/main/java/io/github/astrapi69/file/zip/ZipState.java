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
package io.github.astrapi69.file.zip;

/**
 * The enum {@link ZipState} provides flags for the encryption and decryption of zip files.
 * 
 * @deprecated all classes from this package has moved to the zip-worker repository, use instead the
 *             classes from that project. Note: will be removed in the next major release.
 */
@Deprecated(forRemoval = true)
public enum ZipState
{

	/** The COMPRESSE d_ size. */
	COMPRESSED_SIZE,
	/** The DATA. */
	DATA,
	/** The E f_ length. */
	EF_LENGTH,
	/** The FLAGS. */
	FLAGS,
	/** The F n_ length. */
	FN_LENGTH,
	/** The HEADER. */
	HEADER,
	/** The SIGNATURE. */
	SIGNATURE,
	/** The TAIL. */
	TAIL
}
