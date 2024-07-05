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
 * The {@code ZipErrorCodes} enum represents error codes related to ZIP file operations.
 *
 * @version 1.0
 * @author Asterios Raptis
 * @deprecated all classes from this package has moved to the zip-worker repository, use instead the
 *             classes from that project. Note: will be removed in the next major release.
 */
@Deprecated(forRemoval = true)
public enum ZipErrorCodes
{

	/** Error code indicating that the directory to be zipped does not exist. */
	DIRECTORY_TO_ZIP_DOES_NOT_EXIST,

	/** Error code indicating an I/O error during ZIP file operations. */
	IO_ERROR,

	/** Error code indicating that the ZIP file does not exist. */
	ZIP_FILE_DOES_NOT_EXIST
}
