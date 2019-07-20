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

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import lombok.experimental.UtilityClass;

/**
 * The class {@link CsvToSqlExtensions}.
 */
@UtilityClass
public final class CsvToSqlExtensions
{

	/**
	 * Extract sql columns.
	 *
	 * @param headers
	 *            the headers
	 * @return the string
	 */
	public static String extractSqlColumns(final String[] headers)
	{
		final StringBuffer sqlColumns = new StringBuffer();
		sqlColumns.append("");
		for (int i = 0; i < headers.length; i++)
		{
			sqlColumns.append(headers[i]);
			if (i < headers.length - 1)
			{
				sqlColumns.append(", ");
			}
			else
			{
				sqlColumns.append("");
			}
		}
		return sqlColumns.toString();
	}

	/**
	 * Gets the csv file as sql insert script.
	 *
	 * @param tableName
	 *            the table name
	 * @param csvBean
	 *            the csv bean
	 * @return the csv file as sql insert script
	 */
	public static String getCsvFileAsSqlInsertScript(final String tableName, final CsvBean csvBean)
	{
		return getCsvFileAsSqlInsertScript(tableName, csvBean, true, true);
	}

	/**
	 * Gets the csv file as sql insert script.
	 *
	 * @param tableName
	 *            the table name
	 * @param csvBean
	 *            the csv bean
	 * @param withHeader
	 *            the with header
	 * @param withEndSemicolon
	 *            the with end semicolon
	 * @return the csv file as sql insert script
	 */
	public static String getCsvFileAsSqlInsertScript(final String tableName, final CsvBean csvBean,
		final boolean withHeader, final boolean withEndSemicolon)
	{
		final StringBuffer sb = new StringBuffer();
		if (withHeader)
		{
			final String sqlColumns = extractSqlColumns(csvBean.getHeaders());
			sb.append("INSERT INTO " + tableName + " ( " + sqlColumns + ") VALUES \n");
		}
		final String[] columnTypesEdit = csvBean.getColumnTypesEdit();
		if (columnTypesEdit != null)
		{
			final StringBuffer sqlData = getSqlData(csvBean.getHeaders(), csvBean.getColumnTypes(),
				columnTypesEdit, csvBean.getLineOrder(), csvBean.getLines(), withEndSemicolon);
			sb.append(sqlData.toString());
		}
		else
		{
			final StringBuffer sqlData = getSqlData(csvBean.getHeaders(), csvBean.getColumnTypes(),
				null, null, csvBean.getLines(), withEndSemicolon);
			sb.append(sqlData.toString());
		}
		return sb.toString();
	}


	/**
	 * Gets the csv file as sql insert script.
	 *
	 * @param tableName
	 *            the table name
	 * @param headers
	 *            the headers
	 * @param columnTypes
	 *            the column types
	 * @param lines
	 *            the lines
	 * @return the csv file as sql insert script
	 */
	public static String getCsvFileAsSqlInsertScript(final String tableName, final String[] headers,
		final String[] columnTypes, final List<String[]> lines)
	{
		return getCsvFileAsSqlInsertScript(tableName, new CsvBean(headers, columnTypes, lines));
	}

	/**
	 * Gets the csv file as sql insert script.
	 *
	 * @param tableName
	 *            the table name
	 * @param headers
	 *            the headers
	 * @param columnTypes
	 *            the column types
	 * @param columnTypesEdit
	 *            the column types edit
	 * @param lines
	 *            the lines
	 * @return the csv file as sql insert script
	 */
	public static String getCsvFileAsSqlInsertScript(final String tableName, final String[] headers,
		final String[] columnTypes, final String[] columnTypesEdit, final List<String[]> lines)
	{
		return getCsvFileAsSqlInsertScript(tableName,
			new CsvBean(headers, columnTypes, columnTypesEdit, lines));
	}

	/**
	 * Gets the data from line.
	 *
	 * @param line
	 *            the line
	 * @param seperator
	 *            the seperator
	 * @return the data from line
	 */
	public static String[] getDataFromLine(final String line, final String seperator)
	{
		final String[] splittedLine = line.split(seperator);
		return splittedLine;
	}

	/**
	 * Gets the sql data.
	 *
	 * @param columns
	 *            the columns
	 * @param columnTypes
	 *            the column types
	 * @param columnTypesEdit
	 *            the column types edit
	 * @param lineOrder
	 *            the line order
	 * @param lines
	 *            the lines
	 * @param withEndSemicolon
	 *            the with end semicolon
	 * @return the sql data
	 */
	public static StringBuffer getSqlData(final String[] columns, final String[] columnTypes,
		final String[] columnTypesEdit, final Map<Integer, Integer> lineOrder,
		final List<String[]> lines, final boolean withEndSemicolon)
	{
		final StringBuffer sb = new StringBuffer();
		int autoincrement = 0;
		for (final Iterator<String[]> iterator = lines.iterator(); iterator.hasNext();)
		{
			String[] line;
			if (lineOrder != null)
			{
				final String[] trueLine = iterator.next();
				final String newLine[] = new String[columnTypes.length];
				for (final Integer index : lineOrder.keySet())
				{
					newLine[lineOrder.get(index)] = trueLine[index];
				}
				line = newLine;
			}
			else
			{
				line = iterator.next();
			}
			sb.append("(");
			for (int i = 0; i < line.length; i++)
			{
				String lineItem = line[i];
				if (columnTypesEdit != null)
				{
					final String columTypeEdit = columnTypesEdit[i];
					final String[] editTypeData = columTypeEdit.split(",");
					final String editType = editTypeData[0];
					if (editType.equals("edit"))
					{
						if (3 < editTypeData.length)
						{
							lineItem = lineItem.replace(editTypeData[1], editTypeData[2]);
							final Boolean lc = new Boolean(editTypeData[3]);
							if (lc)
							{
								final String tlc = lineItem.toLowerCase();
								sb.append("\"" + tlc + "\"");
							}
							else
							{
								sb.append("\"" + lineItem + "\"");
							}
						}
						else
						{
							sb.append("\"" + lineItem + "\"");
						}
					}
					else if (editType.equals("autoincrement"))
					{
						final int startCount = Integer.parseInt(editTypeData[1]);
						if (i == 0 && autoincrement == 0)
						{
							autoincrement = startCount;
						}
						sb.append(autoincrement);
						autoincrement++;
					}
					else if (editType.equals("constant"))
					{
						final String type = editTypeData[1];
						if (type.equals("text"))
						{
							sb.append("\"" + editTypeData[2] + "\"");
						}
						else
						{
							sb.append(editTypeData[2]);
						}
					}
				}
				else
				{
					if (lineItem != null)
					{
						if (columnTypes[i].endsWith("text"))
						{
							sb.append("\"" + lineItem + "\"");
						}
						else
						{
							sb.append(lineItem);
						}
					}
					else
					{
						sb.append(lineItem);
					}

				}
				if (i < columns.length - 1)
				{
					sb.append(", ");
				}
			}
			sb.append(")");
			if (iterator.hasNext())
			{
				sb.append(",\n");
			}
			else
			{
				if (withEndSemicolon)
				{
					sb.append(";\n");
				}
				else
				{
					sb.append(",\n");
				}
			}
		}
		return sb;
	}

}
