package com.myapp.service.util.excel;

import com.alibaba.fastjson.JSON;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.myapp.service.util.common.CommonUtil.fail;
import static com.myapp.service.util.excel.ExcelUtil.exportToFile;
import static com.myapp.service.util.excel.ExcelUtil.getLocalDateTimeCellValue;

public class Test {

    public static void main(String[] args) {
//        importExcel();
    }

    public static void importExcel() {
        try {
            File file = new File("C:\\Users\\Admin\\Desktop\\测试\\test.xlsx");
            try (Workbook workbook = WorkbookFactory.create(file)) {
                Sheet sheet = workbook.getSheetAt(0);

                /*List<Integer> removeRegions = new ArrayList<>();
                for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
                    CellRangeAddress range = sheet.getMergedRegion(i);
                    if (range.getFirstRow() >= 20 && range.getLastRow() <= 25 &&
                            range.getFirstColumn() >= 1 && range.getLastColumn() <= 2) {
//                        sheet.removeMergedRegion(i); //不能一边遍历一边删除
                        removeRegions.add(i);
                    }
                }
                sheet.removeMergedRegions(removeRegions);
                sheet.shiftRows(26, sheet.getLastRowNum(), -6);
                sheet.addMergedRegion(new CellRangeAddress(9, 19, 0, 0));*/

                exportToFile(workbook,"C:\\Users\\Admin\\Desktop\\测试\\"+System.currentTimeMillis());
//                for (int i = 1, lastRowNum = sheet.getLastRowNum(); i <= lastRowNum; i++) {
//                    Row row = sheet.getRow(i);
//                    if (row == null) continue;
//                    LocalDateTime dateTime = getLocalDateTimeCellValue(row, 6);
//                    System.out.println();
//                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            fail("文件导入失败");
        }
    }

}
