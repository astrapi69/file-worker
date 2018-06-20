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
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.alpharogroup.collections.CollectionExtensions;
import de.alpharogroup.collections.array.ArrayExtensions;
import de.alpharogroup.collections.list.ListFactory;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CsvBean implements Serializable, Cloneable
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1648936246997896598L;

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
		final CsvBean inst = CsvBean.builder()
			.columnTypes(ArrayExtensions.arraycopyWithSystem(columnTypes,
				columnTypes == null ? null : new String[columnTypes.length]))
			.columnTypesEdit(ArrayExtensions.arraycopyWithSystem(columnTypesEdit,
				columnTypesEdit == null ? null : new String[columnTypesEdit.length]))
			.headers(ArrayExtensions.arraycopyWithSystem(headers,
				headers == null ? null : new String[headers.length]))
			.lineOrder(lineOrder == null ? null : new LinkedHashMap<>(lineOrder))
			.lines(lines == null ? null : ListFactory.newArrayList(lines)).build();
		return inst;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(final Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null)
		{
			return false;
		}
		if (o.getClass() != getClass())
		{
			return false;
		}
		final CsvBean other = (CsvBean)o;
		boolean headersEquality = java.util.Arrays.equals(headers, other.headers);
		boolean columnTypesEquality = java.util.Arrays.equals(columnTypes, other.columnTypes);
		boolean linesEquality = false;
		if (lines == null)
		{
			if (other.lines != null)
				return false;
		}
		linesEquality = CollectionExtensions.isEqualCollection(lines, other.lines);
		return headersEquality && columnTypesEquality && linesEquality;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
	{
		int hashCode = 1;
		final int prime = 31;
		hashCode = prime * hashCode + Arrays.hashCode(columnTypes);
		hashCode = prime * hashCode + Arrays.hashCode(headers);

		hashCode = prime * hashCode * CollectionExtensions.hashCode(lines);
		return hashCode;
	}

}
