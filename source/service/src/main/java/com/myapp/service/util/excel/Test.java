package com.myapp.service.util.excel;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

import static com.myapp.service.util.common.CommonUtil.fail;
import static com.myapp.service.util.excel.ExcelUtil.getLocalDateTimeCellValue;

public class Test {

    public static void main(String[] args) {
        importExcel();
    }

    public static void importExcel() {
        try {
            File file = new File("C:\\Users\\Admin\\Desktop\\测试\\还款计划表(1).xls");
            try (Workbook workbook = WorkbookFactory.create(file)) {
                Sheet sheet = workbook.getSheetAt(0);
                for (int i = 1, lastRowNum = sheet.getLastRowNum(); i <= lastRowNum; i++) {
                    Row row = sheet.getRow(i);
                    if (row == null) continue;
                    LocalDateTime dateTime = getLocalDateTimeCellValue(row, 6);
                    System.out.println();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            fail("文件导入失败");
        }
    }

}
