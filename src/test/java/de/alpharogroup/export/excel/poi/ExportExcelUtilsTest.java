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
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

import de.alpharogroup.file.delete.DeleteFileUtils;
import de.alpharogroup.file.search.PathFinder;
import de.alpharogroup.io.StreamUtils;


public class ExportExcelUtilsTest
{

	final String twoDimArray[][] = { { "1", "a", "!" }, { "2", "b", "?" }, { "3", "c", "%" } };

	private File createWorkbookWithContent() throws IOException
	{
		final File emptyWorkbook = new File(PathFinder.getSrcTestResourcesDir(),
			"emptyWorkbook.xls");
		final Workbook workbook = ExcelPoiFactory.newHSSFWorkbook(emptyWorkbook);
		final Sheet sheet = ExcelPoiFactory.newSheet(workbook, "first sheet");
		int rownum = 0;
		Row row = sheet.createRow(rownum);
		Cell cell0 = row.createCell(0);
		cell0.setCellValue("1");
		Cell cell1 = row.createCell(1);
		cell1.setCellValue("a");
		Cell cell2 = row.createCell(2);
		cell2.setCellValue("!");
		rownum++;
		row = sheet.createRow(rownum);
		cell0 = row.createCell(0);
		cell0.setCellValue("2");
		cell1 = row.createCell(1);
		cell1.setCellValue("b");
		cell2 = row.createCell(2);
		cell2.setCellValue("?");
		rownum++;
		row = sheet.createRow(rownum);
		cell0 = row.createCell(0);
		cell0.setCellValue("3");
		cell1 = row.createCell(1);
		cell1.setCellValue("c");
		cell2 = row.createCell(2);
		cell2.setCellValue("%");

		try
		{
			final OutputStream outputStream = StreamUtils.getOutputStream(emptyWorkbook);
			workbook.write(outputStream);
			outputStream.close();
		}
		catch (final IOException e)
		{
			throw e;
		}
		return emptyWorkbook;
	}

	@Test
	public void testExportWorkbook() throws URISyntaxException, IOException
	{
		final File emptyWorkbook = createWorkbookWithContent();
		final List<String[][]> sheetList = ExportExcelExtensions.exportWorkbook(emptyWorkbook);
		for (int i = 0; i < sheetList.size(); i++)
		{
			final String[][] sheetEntry = sheetList.get(i);

			for (int j = 0; j < sheetEntry.length; j++)
			{
				for (int y = 0; y < sheetEntry[j].length; y++)
				{
					AssertJUnit.assertEquals(twoDimArray[j][y], sheetEntry[j][y]);
				}
			}
		}
		DeleteFileUtils.delete(emptyWorkbook);
	}

	@Test
	public void testExportWorkbookAsStringList() throws IOException
	{
		final File emptyWorkbook = createWorkbookWithContent();
		final List<List<List<String>>> sheetList = ExportExcelExtensions
			.exportWorkbookAsStringList(emptyWorkbook);
		for (int i = 0; i < sheetList.size(); i++)
		{
			final List<List<String>> sheetEntry = sheetList.get(i);

			for (int j = 0; j < sheetEntry.size(); j++)
			{
				final List<String> list = sheetEntry.get(j);
				for (int y = 0; y < list.size(); y++)
				{
					AssertJUnit.assertEquals(twoDimArray[j][y], list.get(y));
				}
			}
		}
		DeleteFileUtils.delete(emptyWorkbook);
	}

	@Test
	public void testReplaceNullCellsIntoEmptyCells()
	{
	}

}
