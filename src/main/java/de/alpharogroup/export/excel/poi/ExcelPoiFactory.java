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
package de.alpharogroup.export.excel.poi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import de.alpharogroup.check.Check;

/**
 * Factory class for create poi objects.
 */
public class ExcelPoiFactory
{

	/**
	 * Creates a new CellStyle from the given parameters.
	 *
	 * @param workbook
	 *            the workbook
	 * @param fontName
	 *            the font name
	 * @param boldweight
	 *            the boldweight
	 * @param height
	 *            the height
	 * @return the cell style
	 */
	public static CellStyle newCellStyle(final Workbook workbook, final String fontName,
		final short boldweight, final short height)
	{
		final CellStyle boldFontCellStyle = workbook.createCellStyle();
		boldFontCellStyle.setFont(newFont(workbook, fontName, boldweight, height));
		return boldFontCellStyle;
	}

	/**
	 * Creates a new CellStyle with the given date format.
	 *
	 * @param workbook
	 *            the workbook
	 * @param dateFormat
	 *            the date format
	 * @return the cell style
	 */
	public static CellStyle newDateCellStyle(final Workbook workbook, final String dateFormat)
	{
		final CellStyle dateCellStyle = workbook.createCellStyle();
		dateCellStyle.setDataFormat(workbook.getCreationHelper().createDataFormat()
			.getFormat(dateFormat));
		return dateCellStyle;
	}

	/**
	 * Creates a new font from the given parameters.
	 *
	 * @param workbook
	 *            the workbook
	 * @param fontName
	 *            the font name
	 * @param boldweight
	 *            the boldweight
	 * @param height
	 *            the height
	 * @return the font
	 */
	public static Font newFont(final Workbook workbook, final String fontName,
		final short boldweight, final short height)
	{
		final Font font = workbook.createFont();
		font.setFontName(fontName);
		font.setBoldweight(boldweight);
		font.setFontHeightInPoints(height);
		return font;
	}

	/**
	 * Creates a new HSSFWorkbook from the given file.
	 *
	 * @param file
	 *            the file name
	 * @return the Workbook
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static Workbook newHSSFWorkbook(final File file) throws IOException
	{
		return writeWorkbook(new HSSFWorkbook(), file);
	}

	/**
	 * Creates a new HSSFWorkbook from the given file name.
	 *
	 * @param pathname
	 *            the file name
	 * @return the Workbook
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static Workbook newHSSFWorkbook(final String pathname) throws IOException
	{
		Check.get().notNull(pathname, "pathname");
		return newHSSFWorkbook(new File(pathname));
	}

	/**
	 * Creates a new Sheet with the given name.
	 * <p>
	 * Note that sheet name is Excel must not exceed 31 characters and must not contain any of the
	 * any of the following characters:
	 * <ul>
	 * <li>0x0000</li>
	 * <li>0x0003</li>
	 * <li>colon (:)</li>
	 * <li>backslash (\)</li>
	 * <li>asterisk (*)</li>
	 * <li>question mark (?)</li>
	 * <li>forward slash (/)</li>
	 * <li>opening square bracket ([)</li>
	 * <li>closing square bracket (])</li>
	 * </ul>
	 * 
	 * @param workbook
	 *            the workbook
	 * @param name
	 *            the name
	 * @return the Sheet
	 */
	public static Sheet newSheet(final Workbook workbook, final String name)
	{
		return workbook.createSheet(WorkbookUtil.createSafeSheetName(name));
	}

	/**
	 * Creates a new HSSFWorkbook from the given file.
	 *
	 * @param file
	 *            the file name
	 * @return the Workbook
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static Workbook newXSSFWorkbook(final File file) throws IOException
	{
		return writeWorkbook(new XSSFWorkbook(), file);
	}

	/**
	 * Writes the given Workbook to the given file.
	 *
	 * @param workbook
	 *            the workbook
	 * @param file
	 *            the file
	 * @return the Workbook
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static Workbook writeWorkbook(final Workbook workbook, final File file)
		throws IOException
	{
		final FileOutputStream fileOutputStream = new FileOutputStream(file);
		workbook.write(fileOutputStream);
		fileOutputStream.close();
		return workbook;
	}

}
