package com.example.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ExcelToListConverter {

    public static List<HashMap<String, String>> convertExcelToList(String filePath) throws IOException {
        List<HashMap<String, String>> data = new ArrayList<>();
        FileInputStream file = new FileInputStream(filePath);
        Workbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(0); // Reads the first sheet

        // Assuming the first row contains headers
        Iterator<Row> rowIterator = sheet.iterator();
        Row headerRow = rowIterator.next();
        List<String> headers = new ArrayList<>();

        // Get headers from the first row
        for (Cell cell : headerRow) {
            headers.add(cell.getStringCellValue());
        }

        // Process each remaining row
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            HashMap<String, String> rowData = new HashMap<>();

            for (int i = 0; i < headers.size(); i++) {
                Cell cell = row.getCell(i);
                String cellValue = (cell == null) ? "" : cell.toString(); // Handles null cells
                rowData.put(headers.get(i), cellValue);
            }

            data.add(rowData);
        }

        workbook.close();
        file.close();
        return data;
    }

    public static void main(String[] args) {
        try {
            List<HashMap<String, String>> data = convertExcelToList("path/to/your/excel-file.xlsx");
            System.out.println(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
