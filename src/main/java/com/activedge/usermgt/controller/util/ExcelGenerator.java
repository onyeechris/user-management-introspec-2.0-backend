package com.activedge.usermgt.controller.util;

import com.activedge.usermgt.model.CustomHttpTrace;
import com.activedge.usermgt.model.dto.StaffDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

public class ExcelGenerator {

    public static List<String> WRITE_METHODS = Arrays.asList("POST", "PUT", "DELETE");

    static DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");

    public static ByteArrayInputStream generateAuditLogs(Page<CustomHttpTrace> auditLogs) throws IOException {
        String[] COLUMNs = {"Reference", "Description", "Username", "Datetime", "Maker_IP", "Activity", "User/Product Affected", "Value"};
        try(
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
        ){

            Sheet sheet = workbook.createSheet("Customers");

            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.AUTOMATIC.getIndex());

            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);

            // Row for Header
            Row headerRow = sheet.createRow(0);

            // Header
            for (int col = 0; col < COLUMNs.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(COLUMNs[col]);
                cell.setCellStyle(headerCellStyle);
            }

            int rowIdx = 1;
            for (CustomHttpTrace auditLog : auditLogs) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(auditLog.getId());
                row.createCell(1).setCellValue(WRITE_METHODS.contains(auditLog.getMethod()) ? "Write Operation" : "Read Operation");
                row.createCell(2).setCellValue(auditLog.getUsername());
                row.createCell(3).setCellValue(dateFormat.format(auditLog.getTimestamp().getTime()));
                row.createCell(4).setCellValue(auditLog.getSourceIp());
                row.createCell(5).setCellValue(auditLog.getMethod() + " with the following parameters: " + auditLog.getQueryParams());
                row.createCell(6).setCellValue(auditLog.getPath());
                row.createCell(7).setCellValue(auditLog.getPayload());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    public static ByteArrayInputStream generateUserList(Page<StaffDTO> users) throws IOException {
        String[] COLUMNs = {"S/N", "Username", "Name", "Email", "Affiliate", "Role", "Maker", "Maker_Date", "Lock_Status", "Last_Login"};

        return null;
    }

}