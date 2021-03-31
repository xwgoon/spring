package com.myapp.service.util.excel;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.myapp.service.util.common.CommonUtil.fail;
import static com.myapp.service.util.excel.ExcelUtil.*;

public class Test {

    private static CellStyle steadyStyle;
    private static CellStyle riseStyle;
    private static CellStyle fallStyle;

    public static void main(String[] args) {
        Map<String, Student> studentMap = new HashMap<>();

        try {
            File ykFile = new File("C:\\Users\\Admin\\Desktop\\成绩\\上期.xls");
            try (Workbook workbook = WorkbookFactory.create(ykFile)) {
                Sheet sheet = workbook.getSheetAt(0);

                for (int i = 2, lastRowNum = sheet.getLastRowNum(); i <= lastRowNum; i++) {
                    Row row = sheet.getRow(i);
                    if (row == null) continue;

                    Student student = new Student();
                    student.setName(getStringCellValue(row, 3));
//                    student.setYw(getIntegerCellValue(row, 3));
//                    student.setSx(getIntegerCellValue(row, 5));
//                    student.setYy(getIntegerCellValue(row, 7));
//                    student.setWl(getIntegerCellValue(row, 9));
//                    student.setHx(getIntegerCellValue(row, 11));
//                    student.setSw(getIntegerCellValue(row, 13));
//                    student.setZf(getIntegerCellValue(row, 15));
                    student.setZf(getIntegerCellValue(row, 17));

                    studentMap.put(student.name, student);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            fail("文件导入失败");
        }

        File bqFile = new File("C:\\Users\\Admin\\Desktop\\成绩\\下期.xls");
        try (Workbook workbook = WorkbookFactory.create(bqFile)) {
            Sheet sheet = workbook.getSheetAt(0);

            steadyStyle = workbook.createCellStyle();
            steadyStyle.cloneStyleFrom(sheet.getRow(2).getCell(2).getCellStyle());

            riseStyle = workbook.createCellStyle();
            riseStyle.cloneStyleFrom(sheet.getRow(2).getCell(2).getCellStyle());
            Font riseFont = workbook.createFont();
            riseFont.setColor(HSSFColor.HSSFColorPredefined.RED.getIndex());
            riseStyle.setFont(riseFont);

            fallStyle = workbook.createCellStyle();
            fallStyle.cloneStyleFrom(sheet.getRow(2).getCell(2).getCellStyle());
            Font fallFont = workbook.createFont();
            fallFont.setColor(HSSFColor.HSSFColorPredefined.GREEN.getIndex());
            fallStyle.setFont(fallFont);

            for (int i = 1, lastRowNum = sheet.getLastRowNum(); i <= lastRowNum; i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String name = getStringCellValue(row, 3);
                Student student = studentMap.get(name);
                if (student == null) {
                    System.err.println(name);
                } else {
//                    calc(row, 5, student.yw, cellStyle);
//                    calc(row, 8, student.sx, cellStyle);
//                    calc(row, 11, student.yy, cellStyle);
//                    calc(row, 14, student.wl, cellStyle);
//                    calc(row, 17, student.hx, cellStyle);
//                    calc(row, 20, student.sw, cellStyle);
                    calc(row, 17, student.zf);
                }
            }

            exportToFile(workbook, "C:\\Users\\Admin\\Desktop\\成绩\\" + System.currentTimeMillis());
        } catch (IOException e) {
            e.printStackTrace();
            fail("文件导入失败");
        }

    }

    private static void calc(Row row, int cellNum, int ykfs) {
        String result = "-";
        CellStyle cellStyle = steadyStyle;
        String str = getStringCellValue(row, cellNum);
        if ("缺考".equals(str)) {
            result = "缺考";
        } else {
            int diff = Integer.parseInt(str) - ykfs;
            if (diff < 0) {
                result = "↑" + Math.abs(diff);
                cellStyle = riseStyle;
            } else if (diff > 0) {
                result = "↓" + Math.abs(diff);
                cellStyle = fallStyle;
            }
        }

        Cell cell = row.createCell(cellNum + 1);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(result);
    }

    private static class Student {
        private String name;
        private int yw;
        private int sx;
        private int yy;
        private int wl;
        private int hx;
        private int sw;
        private int zf;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getYw() {
            return yw;
        }

        public void setYw(int yw) {
            this.yw = yw;
        }

        public int getSx() {
            return sx;
        }

        public void setSx(int sx) {
            this.sx = sx;
        }

        public int getYy() {
            return yy;
        }

        public void setYy(int yy) {
            this.yy = yy;
        }

        public int getWl() {
            return wl;
        }

        public void setWl(int wl) {
            this.wl = wl;
        }

        public int getHx() {
            return hx;
        }

        public void setHx(int hx) {
            this.hx = hx;
        }

        public int getSw() {
            return sw;
        }

        public void setSw(int sw) {
            this.sw = sw;
        }

        public int getZf() {
            return zf;
        }

        public void setZf(int zf) {
            this.zf = zf;
        }
    }

}
