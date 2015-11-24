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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;

import de.alpharogroup.lang.ClassExtensions;

/**
 * The Class ExportExcelExtensions.
 */
public final class ExportExcelExtensions
{

	/**
	 * Exportiert die übergebene excel-Datei in eine Liste mit zweidimensionalen Arrays für jeweils
	 * ein sheet in der excel-Datei.
	 *
	 * @param excelSheet
	 *            Die excel-Datei.
	 * @return Gibt eine Liste mit zweidimensionalen Arrays für jeweils ein sheet in der excel-Datei
	 *         zurück.
	 * @throws IOException
	 *             Fals ein Fehler beim Lesen aufgetreten ist.
	 * @throws FileNotFoundException
	 *             Fals die excel-Datei nicht gefunden wurde.
	 */
	public static List<String[][]> exportWorkbook(final File excelSheet) throws IOException,
		FileNotFoundException
	{
		final POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(excelSheet));
		final HSSFWorkbook wb = new HSSFWorkbook(fs);

		final int numberOfSheets = wb.getNumberOfSheets();
		final List<String[][]> sheetList = new ArrayList<>();
		for (int sheetNumber = 0; sheetNumber < numberOfSheets; sheetNumber++)
		{
			HSSFSheet sheet = null;
			sheet = wb.getSheetAt(sheetNumber);
			final int rows = sheet.getLastRowNum();

			final int columns = sheet.getRow(0).getLastCellNum();
			String[][] excelSheetInTDArray = null;
			excelSheetInTDArray = new String[rows + 1][columns];
			for (int i = 0; i <= rows; i++)
			{
				final HSSFRow row = sheet.getRow(i);
				if (null != row)
				{
					for (int j = 0; j < columns; j++)
					{
						final HSSFCell cell = row.getCell(j);
						if (null == cell)
						{
							excelSheetInTDArray[i][j] = "";
						}
						else
						{
							final int cellType = cell.getCellType();
							if (cellType == Cell.CELL_TYPE_BLANK)
							{
								excelSheetInTDArray[i][j] = "";
							}
							else if (cellType == Cell.CELL_TYPE_BOOLEAN)
							{
								excelSheetInTDArray[i][j] = Boolean.toString(cell
									.getBooleanCellValue());
							}
							else if (cellType == Cell.CELL_TYPE_ERROR)
							{
								excelSheetInTDArray[i][j] = "";
							}
							else if (cellType == Cell.CELL_TYPE_FORMULA)
							{
								excelSheetInTDArray[i][j] = cell.getCellFormula();
							}
							else if (cellType == Cell.CELL_TYPE_NUMERIC)
							{
								excelSheetInTDArray[i][j] = Double.toString(cell
									.getNumericCellValue());
							}
							else if (cellType == Cell.CELL_TYPE_STRING)
							{
								excelSheetInTDArray[i][j] = cell.getRichStringCellValue()
									.getString();
							}
						}
					}
				}
			}
			sheetList.add(excelSheetInTDArray);
		}
		wb.close();
		return sheetList;
	}


	/**
	 * Exportiert die übergebene excel-Datei in eine geschachtelte Liste mit Listen von sheets und
	 * Listen von den Zeilen der sheets von der excel-Datei.
	 *
	 * @param excelSheet
	 *            Die excel-Datei.
	 * @return Gibt eine Liste mit Listen von den sheets in der excel-Datei zurück. Die Listen mit
	 *         den sheets beinhalten weitere Listen mit String die jeweils eine Zeile
	 *         repräsentieren.
	 * @throws IOException
	 *             Fals ein Fehler beim Lesen aufgetreten ist.
	 * @throws FileNotFoundException
	 *             Fals die excel-Datei nicht gefunden wurde.
	 */
	public static List<List<List<String>>> exportWorkbookAsStringList(final File excelSheet)
		throws IOException, FileNotFoundException
	{
		final POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(excelSheet));
		final HSSFWorkbook wb = new HSSFWorkbook(fs);
		final int numberOfSheets = wb.getNumberOfSheets();
		final List<List<List<String>>> sl = new ArrayList<>();
		for (int sheetNumber = 0; sheetNumber < numberOfSheets; sheetNumber++)
		{
			HSSFSheet sheet = null;
			sheet = wb.getSheetAt(sheetNumber);
			final int rows = sheet.getLastRowNum();
			final int columns = sheet.getRow(0).getLastCellNum();
			final List<List<String>> excelSheetList = new ArrayList<>();
			for (int i = 0; i <= rows; i++)
			{
				final HSSFRow row = sheet.getRow(i);
				if (null != row)
				{
					final List<String> reihe = new ArrayList<>();
					for (int j = 0; j < columns; j++)
					{
						final HSSFCell cell = row.getCell(j);
						if (null == cell)
						{
							reihe.add("");
						}
						else
						{
							final int cellType = cell.getCellType();
							if (cellType == Cell.CELL_TYPE_BLANK)
							{
								reihe.add("");
							}
							else if (cellType == Cell.CELL_TYPE_BOOLEAN)
							{
								reihe.add(Boolean.toString(cell.getBooleanCellValue()));
							}
							else if (cellType == Cell.CELL_TYPE_ERROR)
							{
								reihe.add("");
							}
							else if (cellType == Cell.CELL_TYPE_FORMULA)
							{
								reihe.add(cell.getCellFormula());
							}
							else if (cellType == Cell.CELL_TYPE_NUMERIC)
							{
								reihe.add(Double.toString(cell.getNumericCellValue()));
							}
							else if (cellType == Cell.CELL_TYPE_STRING)
							{
								reihe.add(cell.getRichStringCellValue().getString());
							}
						}
					}
					excelSheetList.add(reihe);
				}
			}
			sl.add(excelSheetList);
		}
		wb.close();
		return sl;
	}


	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 * @throws FileNotFoundException
	 *             the file not found exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws URISyntaxException
	 *             is thrown if a string could not be parsed as a URI reference.
	 */
	public static void main(final String[] args) throws FileNotFoundException, IOException,
		URISyntaxException
	{
		final String filename = "test.xls";
		final URL url = ClassExtensions.getResource(filename);
		final File excelSheet = new File(url.toURI());
		System.out.println(excelSheet.exists());
		final List<String[][]> sheetList = exportWorkbook(excelSheet);
		System.out.println(sheetList);
		final List<List<List<String>>> excelSheetList = exportWorkbookAsStringList(excelSheet);
		System.out.println(excelSheetList);
	}


	/**
	 * Replace null cells into empty cells.
	 *
	 * @param excelSheet
	 *            the excel sheet
	 * @return the HSSF workbook
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws FileNotFoundException
	 *             the file not found exception
	 */
	public static HSSFWorkbook replaceNullCellsIntoEmptyCells(final File excelSheet)
		throws IOException, FileNotFoundException
	{
		final POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(excelSheet));
		final HSSFWorkbook wb = new HSSFWorkbook(fs);
		final int numberOfSheets = wb.getNumberOfSheets();
		for (int sheetNumber = 0; sheetNumber < numberOfSheets; sheetNumber++)
		{
			HSSFSheet sheet = null;
			sheet = wb.getSheetAt(sheetNumber);
			final int rows = sheet.getLastRowNum();
			final int columns = sheet.getRow(0).getLastCellNum();
			for (int i = 0; i <= rows; i++)
			{
				final HSSFRow row = sheet.getRow(i);
				if (null != row)
				{
					for (int j = 0; j < columns; j++)
					{
						HSSFCell cell = row.getCell(j);
						if (cell == null)
						{
							cell = row.createCell(j, Cell.CELL_TYPE_BLANK);
						}
					}
				}
			}
		}
		return wb;
	}


	/**
	 * Privater Konstruktor damit keine Instanzen erzeugt werden können.
	 */
	private ExportExcelExtensions()
	{
	}
}
