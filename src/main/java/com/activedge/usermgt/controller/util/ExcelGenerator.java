package com.activedge.usermgt.controller.util;

import com.activedge.usermgt.model.CustomHttpTrace;
import com.activedge.usermgt.model.dto.StaffDTO;

import io.vavr.Function1;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

public class ExcelGenerator {
    static String groups;
    public static List<String> WRITE_METHODS = Arrays.asList("POST", "PUT", "DELETE");

    static DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");

//    public static ByteArrayInputStream generateAuditLogs(Page<CustomHttpTrace> auditLogs) throws IOException {
//        String[] COLUMNs = {"Reference", "Description", "Username", "Datetime", "Maker_IP", "Activity", "User/Product Affected", "Value"};
//        try(
//                Workbook workbook = new XSSFWorkbook();
//                ByteArrayOutputStream out = new ByteArrayOutputStream();
//        ){
//
//            Sheet sheet = workbook.createSheet("Customers");
//
//            Font headerFont = workbook.createFont();
//            headerFont.setBold(true);
//            headerFont.setColor(IndexedColors.AUTOMATIC.getIndex());
//
//            CellStyle headerCellStyle = workbook.createCellStyle();
//            headerCellStyle.setFont(headerFont);
//
//            // Row for Header
//            Row headerRow = sheet.createRow(0);
//
//            // Header
//            for (int col = 0; col < COLUMNs.length; col++) {
//                Cell cell = headerRow.createCell(col);
//                cell.setCellValue(COLUMNs[col]);
//                cell.setCellStyle(headerCellStyle);
//            }
//
//            int rowIdx = 1;
//            for (CustomHttpTrace auditLog : auditLogs) {
//                Row row = sheet.createRow(rowIdx++);
//
//                row.createCell(0).setCellValue(auditLog.getId());
//                row.createCell(1).setCellValue(WRITE_METHODS.contains(auditLog.getMethod()) ? "Write Operation" : "Read Operation");
//                row.createCell(2).setCellValue(auditLog.getUsername());
//                row.createCell(3).setCellValue(dateFormat.format(auditLog.getTimestamp().getTime()));
//                row.createCell(4).setCellValue(auditLog.getSourceIp());
//                row.createCell(5).setCellValue(auditLog.getMethod() + " with the following parameters: " + auditLog.getQueryParams());
//                row.createCell(6).setCellValue(auditLog.getPath());
//                row.createCell(7).setCellValue(auditLog.getPayload());
//            }
//
//            workbook.write(out);
//            return new ByteArrayInputStream(out.toByteArray());
//        }
//    }
public static ByteArrayInputStream generateAuditLogsCSV(Page<CustomHttpTrace> auditLogs) throws IOException {
    String[] COLUMNs = {"Description", "Username", "Datetime", "Maker_IP", "Activity", "User/Product Affected", "Value", "Severity"};
    try (ByteArrayOutputStream out = new ByteArrayOutputStream();
         CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), CSVFormat.DEFAULT.withHeader(COLUMNs))) {

        for (CustomHttpTrace auditLog : auditLogs) {
            csvPrinter.printRecord(
                    getDescriptionForMethod(auditLog.getMethod()), // Adjust this method to return a description
                    auditLog.getUsername(),
                    dateFormat.format(auditLog.getTimestamp().getTime()),
                    auditLog.getSourceIp(),
                    auditLog.getQueryParams(),
                    auditLog.getPath(),
                    auditLog.getPayload(),
                    auditLog.getSeverity().name()
            );
        }
        csvPrinter.flush();
        return new ByteArrayInputStream(out.toByteArray());
    }
}
    public static ByteArrayInputStream generateAuditLogs(Function1<Pageable, Page<CustomHttpTrace>> partialFunction) throws IOException {
        String[] COLUMNs = {"Description", "Username", "Datetime", "Maker_IP", "Activity", "User/Product Affected", "Severity"};
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), CSVFormat.DEFAULT.withHeader(COLUMNs))) {

            Pageable pageable = PageRequest.of(0,1000);
            Page<CustomHttpTrace> pagedLogs = null;
            do {
                pagedLogs = partialFunction.apply(pageable);
                List<CustomHttpTrace> logs = pagedLogs.getContent();
                System.out.println("logs size "+logs.size());
                for (CustomHttpTrace auditLog : logs) {
                    csvPrinter.printRecord(
                            getDescriptionForMethod(auditLog.getMethod()), // Adjust this method to return a description
                            auditLog.getUsername(),
                            dateFormat.format(auditLog.getTimestamp()),
                            auditLog.getSourceIp(),
                            auditLog.getQueryParams(),
                            auditLog.getPath(),
                            auditLog.getSeverity().name()
                    );
                }
                pageable = pagedLogs.nextPageable();
            }while (pagedLogs.hasNext());

            csvPrinter.flush();
            return new ByteArrayInputStream(out.toByteArray());
        }
    }
    private static String getDescriptionForMethod(String method) {
        return WRITE_METHODS.contains(method) ? "Write Operation" : "Read Operation";
    }

    public static ByteArrayInputStream generateUserList(List<StaffDTO> users) throws IOException {
        String[] COLUMNs = {"Firstname", "Lastname", "Email", "Username", "Role", "Group", "Last_Login"};
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), CSVFormat.DEFAULT.withHeader(COLUMNs))) {

            for (StaffDTO user : users) {
                csvPrinter.printRecord(
                        user.getFirst_name(),
                        user.getLast_name(),
                        user.getEmail(),
                        user.getUsername(),
                        user.getUser_type(),
                        groups = String.join(",", user.getGroupNames()),
                        user.getLast_login()
                );
            }
            csvPrinter.flush();
            return new ByteArrayInputStream(out.toByteArray());
        }
    }
}