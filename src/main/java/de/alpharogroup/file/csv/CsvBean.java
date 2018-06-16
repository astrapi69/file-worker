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
package de.alpharogroup.file.csv;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

/**
 * The class {@link CsvBean}.
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CsvBean implements Serializable, Cloneable
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5428554488510583277L;


	/** The headers. */
	private String[] headers;

	/** The column types. */
	private String[] columnTypes;

	/** The lines. */
	private List<String[]> lines;

	/** The column types edit. */
	private String[] columnTypesEdit;


	/** The line order. */
	private Map<Integer, Integer> lineOrder;

	/**
	 * Instantiates a new {@link CsvBean} object.
	 *
	 * @param headers
	 *            the headers
	 * @param columnTypes
	 *            the column types
	 * @param lines
	 *            the lines
	 */
	public CsvBean(final String[] headers, final String[] columnTypes, final List<String[]> lines)
	{
		this.headers = headers;
		this.columnTypes = columnTypes;
		this.lines = lines;
	}

	/**
	 * Instantiates a new {@link CsvBean} object.
	 *
	 * @param headers
	 *            the headers
	 * @param columnTypes
	 *            the column types
	 * @param columnTypesEdit
	 *            the column types edit
	 * @param lines
	 *            the lines
	 */
	public CsvBean(final String[] headers, final String[] columnTypes,
		final String[] columnTypesEdit, final List<String[]> lines)
	{
		this.headers = headers;
		this.columnTypes = columnTypes;
		this.columnTypesEdit = columnTypesEdit;
		this.lines = lines;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object clone()
	{
		final CsvBean inst = new CsvBean();
		if (headers != null)
		{
			inst.headers = new String[headers.length];
			for (int i0 = 0; i0 < headers.length; i0++)
			{
				inst.headers[i0] = headers[i0] == null ? null : new String(headers[i0]);
			}
		}
		else
		{
			inst.headers = null;
		}
		if (columnTypes != null)
		{
			inst.columnTypes = new String[columnTypes.length];
			for (int i0 = 0; i0 < columnTypes.length; i0++)
			{
				inst.columnTypes[i0] = columnTypes[i0] == null ? null : new String(columnTypes[i0]);
			}
		}
		else
		{
			inst.columnTypes = null;
		}

		inst.lines = lines;
		return inst;
	}

}
