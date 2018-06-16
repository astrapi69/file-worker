package de.alpharogroup.file.zip;

/**
 * The enum {@link ZipState} provides flags for the encryption and decryption of zip files.
 */
public enum ZipState
{

	/** The SIGNATURE. */
	SIGNATURE,
	/** The FLAGS. */
	FLAGS,
	/** The COMPRESSE d_ size. */
	COMPRESSED_SIZE,
	/** The F n_ length. */
	FN_LENGTH,
	/** The E f_ length. */
	EF_LENGTH,
	/** The HEADER. */
	HEADER,
	/** The DATA. */
	DATA,
	/** The TAIL. */
	TAIL
}