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
package de.alpharogroup.file;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class GeneratorUtils.
 */
public final class GeneratorUtils
{
	/**
	 * Creates a String list with the String constants from the given String list. For instance:
	 * List data = new ArrayList(); data.add("foo"); data.add("bar");
	 * newConstantsFromStringList(data, false); Result from
	 * list:["public static final String FOO = "foo";", "public static final String BAR = "bar";"]
	 *
	 * @param data
	 *            The data from what to create the contant strings.
	 * @param prefix
	 *            If the constant name needs a prefix.
	 * @param suffix
	 *            If the constant name needs a suffix.
	 * @param withQuotation
	 *            If the strings in the list have already quotation marks then true.
	 * @return A list with constants strings.
	 */
	public static List<String> newConstantsFromStringList(final List<String> data,
		final String prefix, final String suffix, final boolean withQuotation)
	{
		final List<String> returnList = new ArrayList<>();

		final int size = data.size();
		for (int i = 0; i < size; i++)
		{
			final String element = data.get(i);
			final StringBuilder sb = new StringBuilder();
			sb.append("public static final String ");
			if (withQuotation)
			{
				String striped = element.trim().toUpperCase();
				striped = striped.substring(1, striped.length() - 1);
				if (null != prefix)
				{
					sb.append(prefix.toUpperCase());
				}
				sb.append(striped);
				if (null != suffix)
				{
					sb.append(suffix.toUpperCase());
				}
				sb.append(" = ");
				sb.append(element + ";");
			}
			else
			{
				if (null != prefix)
				{
					sb.append(prefix.toUpperCase());
				}
				sb.append(element.trim().toUpperCase());
				if (null != suffix)
				{
					sb.append(suffix.toUpperCase());
				}
				sb.append(" = ");
				sb.append("\"" + element + "\";");
			}
			returnList.add(sb.toString().trim());
		}
		return returnList;

	}

	/**
	 * Creates a String for a constant Stringarray to be inserted in java-file. For instance: List
	 * data = new ArrayList(); data.add("foo"); data.add("bar");
	 * newcreateStaticArrayVariable("test", data);
	 * Resultstring:"public static final String [] TEST = { "foo", "bar"};"
	 *
	 * @param arrayName
	 *            The name from the array.
	 * @param data
	 *            The data what to insert in the generated array.
	 * @return The result.
	 */
	public static String newcreateStaticArrayVariable(final String arrayName,
		final List<String> data)
	{
		final StringBuffer sb = new StringBuffer();
		sb.append("public static final String []");
		sb.append(arrayName.trim().toUpperCase());
		sb.append(" = {");
		final int size = data.size();
		for (int i = 0; i < size; i++)
		{
			final String element = data.get(i);
			if (i < size - 1)
			{
				sb.append("\"" + element + "\", ");
			}
			else
			{
				sb.append("\"" + element + "\"");
			}
		}
		sb.append("};");
		return sb.toString();
	}

	/**
	 * Private constructor.
	 */
	private GeneratorUtils()
	{
		super();
	}

}
