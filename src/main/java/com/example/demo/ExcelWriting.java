package com.example.demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.example.demo.beans.Output;

public class ExcelWriting {

	@SuppressWarnings("resource")
	public static void writeToExcel(List<Output> outputs, String ruleSetName) {
		try {
			XSSFWorkbook workbook;
			if (new File("output.xlsx").exists()) {
				FileInputStream file = new FileInputStream(new File("output.xlsx"));
				// Blank workbook
				workbook = new XSSFWorkbook(file);
			} else {
				workbook = new XSSFWorkbook();
			}

			Map<String, Object[]> data = new TreeMap<String, Object[]>();
			
			// Create a blank sheet
			XSSFSheet sheet;
			int rowNum = 0;
			if (workbook.getSheet(ruleSetName) != null) {
				sheet = workbook.getSheet(ruleSetName);
				rowNum = sheet.getLastRowNum() + 1;
			} else {
				sheet = workbook.createSheet(ruleSetName);
				data.put("1", new Object[] { "ID", "URL", "NAME", "Lang", "TYPE", "TAGS", "CATEGORIES", "title",
						"Descrition", "content", "crawl_date", "modified_date", "published_date", "text", "rules" });
			}

			// This data needs to be written (Object[]) 
			int i = 2;
			for (Output output : outputs) {
				data.put(i+ "", new Object[] { output.getId(), output.getUrl(), output.getName(),
						output.getLang(), output.getLang(), output.getType(), output.getTags().toString(),
						output.getCategories().toString(), output.getTitle(), output.getDescription(),
						output.getContent().length() > 32767 ? output.getContent().substring(0, 3276700): output.getContent(), output.getCrawl_date().toString(), output.getModified_date().toString(),
						output.getPublished_date().toString(), output.getText().length() > 3276700 ? output.getText().substring(0, 32767): output.getText(), output.getRules().toString() });
				i++;
			}

			// Iterate over data and write to sheet
			Set<String> keyset = data.keySet();
			for (String key : keyset) {
				Row row = sheet.createRow(rowNum++);
				Object[] objArr = data.get(key);
				int cellnum = 0;
				for (Object obj : objArr) {
					Cell cell = row.createCell(cellnum++);
					if (obj instanceof String)
						cell.setCellValue((String) obj);
					else if (obj instanceof Integer)
						cell.setCellValue((Integer) obj);
				}
			}
			try {
				// Write the workbook in file system
				FileOutputStream out = new FileOutputStream(new File("output.xlsx"));
				workbook.write(out);
				out.close();
				System.out.println("output.xlsx written successfully on disk.");
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
