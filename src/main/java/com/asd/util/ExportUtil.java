package com.asd.util;


import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ExportUtil {

    private ExportUtil() {}


    public static byte[] toExcel(List<String> headers, List<List <String>> data) throws IOException {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Report");

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.size(); i++) {
                headerRow.createCell(i).setCellValue(headers.get(i));
            }

            for (int i = 0; i < data.size(); i++) {
                Row row = sheet.createRow(i + 1);
                for (int j = 0; j < data.get(i).size(); j++) {
                    Cell cell = row.createCell(j);
                    cell.setCellValue(data.get(i).get(j));
                }
            }
            workbook.write(out);
            workbook.close();
            return out.toByteArray();
        }
    }

    public static byte[] toPDF(List<String> headers, List<List <String>> data) throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, out);
            document.open();

            document.add(new Paragraph("Transaction Report"));
            document.add(new Paragraph("Generated on: " + java.time.LocalDate.now()));
            document.add(new Paragraph("\n"));

            PdfPTable table = new PdfPTable(headers.size());
            table.setWidthPercentage(100);

            for (String header : headers) {
                table.addCell(new Phrase(header));
            }

            for (List<String> row : data) {
                for (String value : row) {
                    table.addCell(new Phrase(value));
                }
            }
            document.add(table);
            document.close();
            return out.toByteArray();
        }
    }


    public static byte[] toCSV(List<String> headers, List<List <String>> data) throws IOException {
        try(ByteArrayOutputStream out = new ByteArrayOutputStream()){
            CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), CSVFormat.DEFAULT);
            csvPrinter.printRecord((Object[]) headers.toArray(new String[0]));

            for (List<String> row : data) {
                csvPrinter.printRecord(row);
            }

            csvPrinter.flush();
            return out.toByteArray();
        }
    }
}
