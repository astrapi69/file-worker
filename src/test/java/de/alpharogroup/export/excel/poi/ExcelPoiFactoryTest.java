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
package de.alpharogroup.export.excel.poi;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;

import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.meanbean.factories.ObjectCreationException;
import org.meanbean.test.BeanTestException;
import org.meanbean.test.BeanTester;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.alpharogroup.file.delete.DeleteFileExtensions;
import de.alpharogroup.file.search.PathFinder;

/**
 * The unit test class for the class {@link ExcelPoiFactory}.
 */
public class ExcelPoiFactoryTest
{
	File emptyWorkbook;
	File emptyWorkbookXlsx;
	File testWorkbookXlsx;
	Workbook workbook;
	Workbook workbookXlsx;
	Workbook xssfWorkbook;

	/**
	 * Sets up method will be invoked before every unit test method
	 *
	 * @throws Exception
	 *             is thrown if an exception occurs
	 */
	@BeforeMethod
	protected void setUp() throws Exception
	{
		emptyWorkbook = new File(PathFinder.getSrcTestResourcesDir(), "emptyWorkbook.xls");
		workbook = ExcelPoiFactory.newHSSFWorkbook(emptyWorkbook);

		emptyWorkbookXlsx = new File(PathFinder.getSrcTestResourcesDir(),
			"emptyWorkbook-xlsx.xlsx");
		emptyWorkbookXlsx.createNewFile();
		workbookXlsx = ExcelPoiFactory.newXSSFWorkbook(emptyWorkbookXlsx);

		testWorkbookXlsx = new File(PathFinder.getSrcTestResourcesDir(),
			"TestWorkbook-xlsx.xlsx");
		testWorkbookXlsx.createNewFile();
		xssfWorkbook = new XSSFWorkbook();
	}

	/**
	 * Tear down method will be invoked after every unit test method
	 *
	 * @throws Exception
	 *             is thrown if an exception occurs
	 */
	@AfterMethod
	protected void tearDown() throws Exception
	{
		DeleteFileExtensions.delete(emptyWorkbook);
		DeleteFileExtensions.delete(emptyWorkbookXlsx);
		DeleteFileExtensions.delete(testWorkbookXlsx);
	}

	/**
	 * Test method for {@link ExcelPoiFactory#newCellStyle(Workbook, String, boolean, short)}
	 */
	@Test
	public final void testNewCellStyle()
	{
		String fontName = "Arial";
		boolean bold = true;
		short height = 1;
		CellStyle cellStyle = ExcelPoiFactory.newCellStyle(workbook, fontName, bold, height);
		assertNotNull(cellStyle);
	}

	/**
	 * Test method for {@link ExcelPoiFactory#newDateCellStyle(Workbook, String)}
	 */
	@Test
	public final void testNewDateCellStyle()
	{
		String dateFormat = BuiltinFormats.getBuiltinFormat(14);
		CellStyle cellStyle = ExcelPoiFactory.newDateCellStyle(workbook, dateFormat);
		String formatString = cellStyle.getDataFormatString();
		assertEquals(dateFormat, formatString);
	}

	/**
	 * Test method for {@link ExcelPoiFactory#newFont(Workbook, String, boolean, short)}
	 */
	@Test
	public final void testNewFont()
	{
		String fontName = "Arial";
		boolean bold = true;
		short height = 1;
		Font font = ExcelPoiFactory.newFont(workbook, fontName, bold, height);
		assertNotNull(font);

	}

	/**
	 * Test method for {@link ExcelPoiFactory#newHSSFWorkbook(File)}.
	 */
	@Test
	public final void testNewHSSFWorkbookFile()
	{
		assertNotNull(workbook);
	}

	/**
	 * Test method for {@link ExcelPoiFactory#newHSSFWorkbook(String)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public final void testNewHSSFWorkbookString() throws IOException
	{
		File emptyWorkbookTmp = new File(PathFinder.getSrcTestResourcesDir(),
			"emptyWorkbook-tmp.xls");
		emptyWorkbookTmp.createNewFile();
		String absolutePath = emptyWorkbookTmp.getAbsolutePath();
		Workbook workbookTmp = ExcelPoiFactory.newHSSFWorkbook(absolutePath);
		assertNotNull(workbookTmp);
		DeleteFileExtensions.delete(emptyWorkbookTmp);
	}

	/**
	 * Test method for {@link ExcelPoiFactory#newSheet(Workbook, String)}.
	 */
	@Test
	public final void testNewSheet()
	{
		String sheetName = "foo";
		Sheet sheet = ExcelPoiFactory.newSheet(workbook, sheetName);
		assertNotNull(sheet);
	}

	/**
	 * Test method for {@link ExcelPoiFactory#newXSSFWorkbook(File)}.
	 */
	@Test
	public final void testNewXSSFWorkbook()
	{
		assertNotNull(workbookXlsx);
	}

	/**
	 * Test method for {@link ExcelPoiFactory#writeWorkbook(Workbook, File)}.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public final void testWriteWorkbook() throws IOException
	{
		Workbook newWorkbook = ExcelPoiFactory.writeWorkbook(xssfWorkbook, testWorkbookXlsx);
		assertNotNull(newWorkbook);
	}

	/**
	 * Test method for {@link ExcelPoiFactory}
	 */
	@Test(expectedExceptions = { BeanTestException.class, ObjectCreationException.class })
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(ExcelPoiFactory.class);
	}
	
}
