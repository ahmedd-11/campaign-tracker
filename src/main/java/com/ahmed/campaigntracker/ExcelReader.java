package com.ahmed.campaigntracker;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.DataFormatter;

public class ExcelReader {
    public List<Campaign> readFile(InputStream inputStream) throws IOException {
        try (Workbook workbook = WorkbookFactory.create(inputStream)) {
            DataFormatter formatter = new DataFormatter();
            if (workbook.getNumberOfSheets() == 0) {
                throw new IOException("Excel file has no sheets");
            }
            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                throw new IOException("Excel file has no header row");
            }
            int spendColumn = -1;
            int clicksColumn = -1;
            int ctrColumn = -1;
            int conversionsColumn = -1;
            int campaignNameColumn = -1;
            int impressionsColumn = -1;
            int cpmColumn = -1;
            int cpcColumn = -1;
            List<Campaign> campaigns = new ArrayList<>();
            for (Cell cell : headerRow) {
                if (cell.getStringCellValue().equals("Spend")) {
                    spendColumn = cell.getColumnIndex();
                }
                if (cell.getStringCellValue().equals("Clicks (destination)")) {
                    clicksColumn = cell.getColumnIndex();
                }
                if (cell.getStringCellValue().equals("CTR (destination)")) {
                    ctrColumn = cell.getColumnIndex();
                }
                if (cell.getStringCellValue().equals("Conversions")) {
                    conversionsColumn = cell.getColumnIndex();
                }
                if (cell.getStringCellValue().equals("Campaign name")) {
                    campaignNameColumn = cell.getColumnIndex();
                }
                if (cell.getStringCellValue().equals("Impressions")) {
                    impressionsColumn = cell.getColumnIndex();
                }

                if (cell.getStringCellValue().equals("CPM")) {
                    cpmColumn = cell.getColumnIndex();
                }
                if (cell.getStringCellValue().equals("CPC (destination)")) {
                    cpcColumn = cell.getColumnIndex();
                }
            }
            if (campaignNameColumn == -1
                    || spendColumn == -1
                    || clicksColumn == -1
                    || ctrColumn == -1
                    || conversionsColumn == -1
                    || impressionsColumn == -1
                    || cpmColumn == -1
                    || cpcColumn == -1) {

                throw new IOException("Required columns are missing");
            }
            for (int rowNumber = 1; rowNumber <= sheet.getLastRowNum(); rowNumber++) {

                Row campaignRow = sheet.getRow(rowNumber);

                if (campaignRow == null) {
                    continue;
                }

                Cell nameCell = campaignRow.getCell(campaignNameColumn);
                if (nameCell == null) {
                    continue;
                }
                String name = nameCell.getStringCellValue();
                if (name.isBlank()) {
                    continue;
                }
                if (name.startsWith("Total")) {
                    continue;
                }

                Cell spendCell = campaignRow.getCell(spendColumn);
                if (spendCell == null) {
                    continue;
                }
                String spendText = formatter.formatCellValue(spendCell);
                if (spendText.isBlank()) {
                    continue;
                }
                double spend = Double.parseDouble(spendText);
                Cell clickCell = campaignRow.getCell(clicksColumn);
                int clicks;
                if (clickCell == null) {
                    clicks = 0;
                } else {
                    clicks = (int) clickCell.getNumericCellValue();
                }

                Cell ctrCell = campaignRow.getCell(ctrColumn);
                double ctr;
                if (ctrCell == null) {
                    ctr = 0;
                } else {
                    double ctrValue = ctrCell.getNumericCellValue();
                    ctr = ctrValue * 100;
                }

                Cell conversionsCell = campaignRow.getCell(conversionsColumn);
                int conversions;

                if (conversionsCell == null) {
                    conversions = 0;
                } else {
                    conversions = (int) conversionsCell.getNumericCellValue();
                }

                Cell impressionsCell = campaignRow.getCell(impressionsColumn);
                int impressions;
                if (impressionsCell == null) {
                    impressions = 0;
                } else {
                    impressions = (int) impressionsCell.getNumericCellValue();
                }

                Cell cpmCell = campaignRow.getCell(cpmColumn);
                double cpm;
                if (cpmCell == null) {
                    cpm = 0;
                } else {
                    String cpmText = formatter.formatCellValue(cpmCell);
                    if (cpmText.isBlank()) {
                        cpm = 0;
                    } else {
                        cpm = Double.parseDouble(cpmText);
                    }
                }

                Cell cpcCell = campaignRow.getCell(cpcColumn);
                double cpc;
                if (cpcCell == null) {
                    cpc = 0;
                } else {
                    String cpcText = formatter.formatCellValue(cpcCell);
                    if (cpcText.isBlank()) {
                        cpc = 0;
                    } else {
                        cpc = Double.parseDouble(cpcText);
                    }
                }

                Campaign campaign = new Campaign(
                        name,
                        null,
                        clicks,
                        spend,
                        ctr,
                        conversions,
                        impressions,
                        cpm,
                        cpc
                );
                campaigns.add(campaign);


            }
            return campaigns;
        }

    }
}