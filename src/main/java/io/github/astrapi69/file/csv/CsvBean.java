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
package io.github.astrapi69.file.csv;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.github.astrapi69.collection.CollectionExtensions;
import io.github.astrapi69.collection.array.ArrayExtensions;
import io.github.astrapi69.collection.list.ListExtensions;
import io.github.astrapi69.collection.list.ListFactory;

/**
 * The class {@link CsvBean}.
 */
public class CsvBean implements Serializable, Cloneable
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1648936246997896598L;
	/** The column types. */
	private String[] columnTypes;
	/** The column types edit. */
	private String[] columnTypesEdit;
	/** The headers. */
	private String[] headers;
	/** The line order. */
	private Map<Integer, Integer> lineOrder;
	/** The lines. */
	private List<String[]> lines;

	public CsvBean()
	{
	}

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

	public CsvBean(String[] columnTypes, String[] columnTypesEdit, String[] headers,
		Map<Integer, Integer> lineOrder, List<String[]> lines)
	{
		this.columnTypes = columnTypes;
		this.columnTypesEdit = columnTypesEdit;
		this.headers = headers;
		this.lineOrder = lineOrder;
		this.lines = lines;
	}

	public static CsvBeanBuilder builder()
	{
		return new CsvBeanBuilder();
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
		linesEquality = ListExtensions.isEqualListOfArrays(lines, other.lines);
		return headersEquality && columnTypesEquality && linesEquality;
	}

	public String[] getColumnTypes()
	{
		return this.columnTypes;
	}

	public void setColumnTypes(String[] columnTypes)
	{
		this.columnTypes = columnTypes;
	}

	public String[] getColumnTypesEdit()
	{
		return this.columnTypesEdit;
	}

	public void setColumnTypesEdit(String[] columnTypesEdit)
	{
		this.columnTypesEdit = columnTypesEdit;
	}

	public String[] getHeaders()
	{
		return this.headers;
	}

	public void setHeaders(String[] headers)
	{
		this.headers = headers;
	}

	public Map<Integer, Integer> getLineOrder()
	{
		return this.lineOrder;
	}

	public void setLineOrder(Map<Integer, Integer> lineOrder)
	{
		this.lineOrder = lineOrder;
	}

	public List<String[]> getLines()
	{
		return this.lines;
	}

	public void setLines(List<String[]> lines)
	{
		this.lines = lines;
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

	public CsvBeanBuilder toBuilder()
	{
		return new CsvBeanBuilder().columnTypes(this.columnTypes)
			.columnTypesEdit(this.columnTypesEdit).headers(this.headers).lineOrder(this.lineOrder)
			.lines(this.lines);
	}

	@Override
	public String toString()
	{
		return "CsvBean(columnTypes=" + Arrays.deepToString(this.getColumnTypes())
			+ ", columnTypesEdit=" + Arrays.deepToString(this.getColumnTypesEdit()) + ", headers="
			+ Arrays.deepToString(this.getHeaders()) + ", lineOrder=" + this.getLineOrder()
			+ ", lines=" + this.getLines() + ")";
	}

	public static class CsvBeanBuilder
	{
		private String[] columnTypes;
		private String[] columnTypesEdit;
		private String[] headers;
		private Map<Integer, Integer> lineOrder;
		private List<String[]> lines;

		CsvBeanBuilder()
		{
		}

		public CsvBean build()
		{
			return new CsvBean(columnTypes, columnTypesEdit, headers, lineOrder, lines);
		}

		public CsvBean.CsvBeanBuilder columnTypes(String[] columnTypes)
		{
			this.columnTypes = columnTypes;
			return this;
		}

		public CsvBean.CsvBeanBuilder columnTypesEdit(String[] columnTypesEdit)
		{
			this.columnTypesEdit = columnTypesEdit;
			return this;
		}

		public CsvBean.CsvBeanBuilder headers(String[] headers)
		{
			this.headers = headers;
			return this;
		}

		public CsvBean.CsvBeanBuilder lineOrder(Map<Integer, Integer> lineOrder)
		{
			this.lineOrder = lineOrder;
			return this;
		}

		public CsvBean.CsvBeanBuilder lines(List<String[]> lines)
		{
			this.lines = lines;
			return this;
		}

		@Override
		public String toString()
		{
			return "CsvBean.CsvBeanBuilder(columnTypes=" + Arrays.deepToString(this.columnTypes)
				+ ", columnTypesEdit=" + Arrays.deepToString(this.columnTypesEdit) + ", headers="
				+ Arrays.deepToString(this.headers) + ", lineOrder=" + this.lineOrder + ", lines="
				+ this.lines + ")";
		}
	}
}
