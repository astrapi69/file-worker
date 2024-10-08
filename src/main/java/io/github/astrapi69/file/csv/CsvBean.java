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

import java.io.Serial;
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
 * The `CsvBean` class represents a structured data model for CSV files. It includes headers, column
 * types, lines of data, and supports operations like cloning, equality comparison, and fluent
 * construction using the builder pattern.
 * 
 * @deprecated Use instead the class with the same name from module 'cvs-worker'. Note: will be
 *             removed with next minor version
 */
@Deprecated
public class CsvBean implements Serializable, Cloneable
{

	/** The Constant serialVersionUID. */
	@Serial
	private static final long serialVersionUID = 1648936246997896598L;
	/** The types of each column. */
	private String[] columnTypes;
	/** The editable types of each column. */
	private String[] columnTypesEdit;
	/** The headers of the CSV. */
	private String[] headers;
	/** The order of lines in the CSV. */
	private Map<Integer, Integer> lineOrder;
	/** The lines of data in the CSV. */
	private List<String[]> lines;

	/**
	 * Default constructor.
	 */
	public CsvBean()
	{
	}

	/**
	 * Constructs a new `CsvBean` object with specified headers, column types, and lines.
	 *
	 * @param headers
	 *            the headers of the CSV
	 * @param columnTypes
	 *            the types of each column
	 * @param lines
	 *            the lines of data in the CSV
	 */
	public CsvBean(final String[] headers, final String[] columnTypes, final List<String[]> lines)
	{
		this.headers = headers;
		this.columnTypes = columnTypes;
		this.lines = lines;
	}

	/**
	 * Constructs a new `CsvBean` object with specified headers, column types, editable types, and
	 * lines.
	 *
	 * @param headers
	 *            the headers of the CSV
	 * @param columnTypes
	 *            the types of each column
	 * @param columnTypesEdit
	 *            the editable types of each column
	 * @param lines
	 *            the lines of data in the CSV
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
	 * Constructs a new `CsvBean` object with specified column types, editable types, headers, line
	 * order, and lines.
	 *
	 * @param columnTypes
	 *            the types of each column
	 * @param columnTypesEdit
	 *            the editable types of each column
	 * @param headers
	 *            the headers of the CSV
	 * @param lineOrder
	 *            the order of lines in the CSV
	 * @param lines
	 *            the lines of data in the CSV
	 */
	public CsvBean(String[] columnTypes, String[] columnTypesEdit, String[] headers,
		Map<Integer, Integer> lineOrder, List<String[]> lines)
	{
		this.columnTypes = columnTypes;
		this.columnTypesEdit = columnTypesEdit;
		this.headers = headers;
		this.lineOrder = lineOrder;
		this.lines = lines;
	}

	/**
	 * Creates a builder for constructing `CsvBean` objects.
	 *
	 * @return a new `CsvBeanBuilder` instance
	 */
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
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}
		final CsvBean other = (CsvBean)o;
		boolean headersEquality = Arrays.equals(headers, other.headers);
		boolean columnTypesEquality = Arrays.equals(columnTypes, other.columnTypes);
		boolean linesEquality = false;
		if (lines == null)
		{
			if (other.lines != null)
				return false;
		}
		linesEquality = ListExtensions.isEqualListOfArrays(lines, other.lines);
		return headersEquality && columnTypesEquality && linesEquality;
	}

	/**
	 * Gets the types of each column in the CSV.
	 *
	 * @return an array representing the types of each column
	 */
	public String[] getColumnTypes()
	{
		return this.columnTypes;
	}

	/**
	 * Sets the types of each column in the CSV.
	 *
	 * @param columnTypes
	 *            an array representing the types of each column
	 */
	public void setColumnTypes(String[] columnTypes)
	{
		this.columnTypes = columnTypes;
	}

	/**
	 * Gets the editable types of each column in the CSV.
	 *
	 * @return an array representing the editable types of each column
	 */
	public String[] getColumnTypesEdit()
	{
		return this.columnTypesEdit;
	}

	/**
	 * Sets the editable types of each column in the CSV.
	 *
	 * @param columnTypesEdit
	 *            an array representing the editable types of each column
	 */
	public void setColumnTypesEdit(String[] columnTypesEdit)
	{
		this.columnTypesEdit = columnTypesEdit;
	}

	/**
	 * Gets the headers of the CSV.
	 *
	 * @return an array representing the headers of the CSV
	 */
	public String[] getHeaders()
	{
		return this.headers;
	}

	/**
	 * Sets the headers of the CSV.
	 *
	 * @param headers
	 *            an array representing the headers of the CSV
	 */
	public void setHeaders(String[] headers)
	{
		this.headers = headers;
	}

	/**
	 * Gets the order of lines in the CSV.
	 *
	 * @return a map representing the order of lines in the CSV
	 */
	public Map<Integer, Integer> getLineOrder()
	{
		return this.lineOrder;
	}

	/**
	 * Sets the order of lines in the CSV.
	 *
	 * @param lineOrder
	 *            a map representing the order of lines in the CSV
	 */
	public void setLineOrder(Map<Integer, Integer> lineOrder)
	{
		this.lineOrder = lineOrder;
	}

	/**
	 * Gets the lines of data in the CSV.
	 *
	 * @return a list containing arrays representing the lines of data in the CSV
	 */
	public List<String[]> getLines()
	{
		return this.lines;
	}

	/**
	 * Sets the lines of data in the CSV.
	 *
	 * @param lines
	 *            a list containing arrays representing the lines of data in the CSV
	 */
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

	/**
	 * Creates a builder initialized with the current `CsvBean` instance's properties.
	 *
	 * @return a new `CsvBeanBuilder` initialized with the current instance's properties
	 */
	public CsvBeanBuilder toBuilder()
	{
		return new CsvBeanBuilder().columnTypes(this.columnTypes)
			.columnTypesEdit(this.columnTypesEdit).headers(this.headers).lineOrder(this.lineOrder)
			.lines(this.lines);
	}

	/**
	 * Returns a string representation of the `CsvBean` object, showing its current column types,
	 * editable types, headers, line order, and lines of data.
	 *
	 * @return a string representation of the `CsvBean` object
	 */
	@Override
	public String toString()
	{
		return "CsvBean(columnTypes=" + Arrays.deepToString(this.getColumnTypes())
			+ ", columnTypesEdit=" + Arrays.deepToString(this.getColumnTypesEdit()) + ", headers="
			+ Arrays.deepToString(this.getHeaders()) + ", lineOrder=" + this.getLineOrder()
			+ ", lines=" + this.getLines() + ")";
	}

	/**
	 * Builder class for constructing `CsvBean` objects.
	 */
	public static class CsvBeanBuilder
	{
		private String[] columnTypes;
		private String[] columnTypesEdit;
		private String[] headers;
		private Map<Integer, Integer> lineOrder;
		private List<String[]> lines;

		/**
		 * Default constructor.
		 */
		CsvBeanBuilder()
		{
		}

		/**
		 * Constructs a new `CsvBean` object based on the current builder state.
		 *
		 * @return a new `CsvBean` object
		 */
		public CsvBean build()
		{
			return new CsvBean(columnTypes, columnTypesEdit, headers, lineOrder, lines);
		}

		/**
		 * Sets the types of each column for the `CsvBean` object being built.
		 *
		 * @param columnTypes
		 *            an array representing the types of each column
		 * @return the current `CsvBeanBuilder` instance
		 */
		public CsvBeanBuilder columnTypes(String[] columnTypes)
		{
			this.columnTypes = columnTypes;
			return this;
		}

		/**
		 * Sets the editable types of each column for the `CsvBean` object being built.
		 *
		 * @param columnTypesEdit
		 *            an array representing the editable types of each column
		 * @return the current `CsvBeanBuilder` instance
		 */
		public CsvBeanBuilder columnTypesEdit(String[] columnTypesEdit)
		{
			this.columnTypesEdit = columnTypesEdit;
			return this;
		}

		/**
		 * Sets the headers for the `CsvBean` object being built.
		 *
		 * @param headers
		 *            an array representing the headers of the CSV
		 * @return the current `CsvBeanBuilder` instance
		 */
		public CsvBeanBuilder headers(String[] headers)
		{
			this.headers = headers;
			return this;
		}

		/**
		 * Sets the line order for the `CsvBean` object being built.
		 *
		 * @param lineOrder
		 *            a map representing the order of lines in the CSV
		 * @return the current `CsvBeanBuilder` instance
		 */
		public CsvBeanBuilder lineOrder(Map<Integer, Integer> lineOrder)
		{
			this.lineOrder = lineOrder;
			return this;
		}

		/**
		 * Sets the lines of data for the `CsvBean` object being built.
		 *
		 * @param lines
		 *            a list containing arrays representing the lines of data in the CSV
		 * @return the current `CsvBeanBuilder` instance
		 */
		public CsvBeanBuilder lines(List<String[]> lines)
		{
			this.lines = lines;
			return this;
		}

		/**
		 * Returns a string representation of the `CsvBeanBuilder` object, showing its current
		 * column types, editable types, headers, line order, and lines of data.
		 *
		 * @return a string representation of the `CsvBeanBuilder` object
		 */
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
