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
package de.alpharogroup.file.csv;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * The Class CsvBean.
 */
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
	 * Instantiates a new csv bean.
	 */
	public CsvBean()
	{
		super();
	}

	/**
	 * Instantiates a new csv bean.
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
		this();
		this.headers = headers;
		this.columnTypes = columnTypes;
		this.lines = lines;
	}

	/**
	 * Instantiates a new csv bean.
	 *
	 * @param headers
	 *            the headers
	 * @param columnTypes
	 *            the column types
	 * @param lines
	 *            the lines
	 * @param columnTypesEdit
	 *            the column types edit
	 * @param lineOrder
	 *            the line order
	 */
	public CsvBean(final String[] headers, final String[] columnTypes, final List<String[]> lines,
		final String[] columnTypesEdit, final Map<Integer, Integer> lineOrder)
	{
		super();
		this.headers = headers;
		this.columnTypes = columnTypes;
		this.lines = lines;
		this.columnTypesEdit = columnTypesEdit;
		this.lineOrder = lineOrder;
	}

	/**
	 * Instantiates a new csv bean.
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
		this();
		this.headers = headers;
		this.columnTypes = columnTypes;
		this.columnTypesEdit = columnTypesEdit;
		this.lines = lines;
	}

	/**
	 * (non-Javadoc).
	 *
	 * @return the object
	 * @see java.lang.Object#clone()
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

	/**
	 * Returns <code>true</code> if this <code>CsvBean</code> is the same as the o argument.
	 *
	 * @param o
	 *            the o
	 * @return is the same as the o argument.
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
		final CsvBean castedObj = (CsvBean)o;
		return java.util.Arrays.equals(headers, castedObj.headers)
			&& java.util.Arrays.equals(columnTypes, castedObj.columnTypes)
			&& (lines == null ? castedObj.lines == null : lines.equals(castedObj.lines));
	}

	/**
	 * Gets the column types.
	 *
	 * @return the column types
	 */
	public String[] getColumnTypes()
	{
		return columnTypes;
	}

	/**
	 * Gets the column types edit.
	 *
	 * @return the column types edit
	 */
	public String[] getColumnTypesEdit()
	{
		return columnTypesEdit;
	}


	/**
	 * Gets the headers.
	 *
	 * @return the headers
	 */
	public String[] getHeaders()
	{
		return headers;
	}

	/**
	 * Gets the line order.
	 *
	 * @return the line order
	 */
	public Map<Integer, Integer> getLineOrder()
	{
		return lineOrder;
	}

	/**
	 * Gets the lines.
	 *
	 * @return the lines
	 */
	public List<String[]> getLines()
	{
		return lines;
	}

	/**
	 * Override hashCode.
	 *
	 * @return the Objects hashcode.
	 */
	@Override
	public int hashCode()
	{
		int hashCode = 1;
		hashCode = 31 * hashCode + (int)(+serialVersionUID ^ serialVersionUID >>> 32);
		for (int i0 = 0; headers != null && i0 < headers.length; i0++)
		{
			hashCode = 31 * hashCode + (headers == null ? 0 : headers[i0].hashCode());
		}
		for (int i0 = 0; columnTypes != null && i0 < columnTypes.length; i0++)
		{
			hashCode = 31 * hashCode + (columnTypes == null ? 0 : columnTypes[i0].hashCode());
		}
		hashCode = 31 * hashCode + (lines == null ? 0 : lines.hashCode());
		return hashCode;
	}

	/**
	 * Sets the column types.
	 *
	 * @param columnTypes
	 *            the new column types
	 */
	public void setColumnTypes(final String[] columnTypes)
	{
		this.columnTypes = columnTypes;
	}

	/**
	 * Sets the column types edit.
	 *
	 * @param columnTypesEdit
	 *            the new column types edit
	 */
	public void setColumnTypesEdit(final String[] columnTypesEdit)
	{
		this.columnTypesEdit = columnTypesEdit;
	}

	/**
	 * Sets the headers.
	 *
	 * @param headers
	 *            the new headers
	 */
	public void setHeaders(final String[] headers)
	{
		this.headers = headers;
	}

	/**
	 * Sets the line order.
	 *
	 * @param lineOrder
	 *            the line order
	 */
	public void setLineOrder(final Map<Integer, Integer> lineOrder)
	{
		this.lineOrder = lineOrder;
	}

	/**
	 * Sets the lines.
	 *
	 * @param lines
	 *            the new lines
	 */
	public void setLines(final List<String[]> lines)
	{
		this.lines = lines;
	}

	/**
	 * (non-Javadoc).
	 *
	 * @return the string
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		final StringBuilder buffer = new StringBuilder();
		buffer.append("[CsvBean:");
		buffer.append(" { ");
		for (int i0 = 0; headers != null && i0 < headers.length; i0++)
		{
			buffer.append(" headers[" + i0 + "]: ");
			buffer.append(headers[i0]);
		}
		buffer.append(" } ");
		buffer.append(" { ");
		for (int i0 = 0; columnTypes != null && i0 < columnTypes.length; i0++)
		{
			buffer.append(" columnTypes[" + i0 + "]: ");
			buffer.append(columnTypes[i0]);
		}
		buffer.append(" } ");
		buffer.append(" lines: ");
		buffer.append(lines);
		buffer.append("]");
		return buffer.toString();
	}

}
