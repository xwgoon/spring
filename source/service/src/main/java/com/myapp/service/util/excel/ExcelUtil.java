package com.myapp.service.util.excel;

import com.myapp.data.model.User;
import com.myapp.service.util.file.FileUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.Internal;
import org.apache.poi.util.Removal;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.myapp.service.util.common.CommonUtil.checkNotEmpty;
import static com.myapp.service.util.common.CommonUtil.fail;
import static com.myapp.service.util.file.FileUtil.PropConstant_CACHE_IMAGE_ROOT;
import static com.myapp.service.util.file.FileUtil.PropConstant_FILE_DOWNLOAD_URL;

public class ExcelUtil {

    private static final DataFormatter DATA_FORMATTER = new DataFormatter();

    /**
     * Dummy formula evaluator that does nothing.
     * YK: The only reason of having this class is that
     * {@link org.apache.poi.ss.usermodel.DataFormatter#formatCellValue(org.apache.poi.ss.usermodel.Cell)}
     * returns formula string for formula cells. Dummy evaluator makes it to format the cached formula result.
     * <p>
     * See Bugzilla #50021
     */
    private static final FormulaEvaluator DUMMY_EVALUATOR = new FormulaEvaluator() {
        @Override
        public void clearAllCachedResultValues() {
        }

        @Override
        public void notifySetFormula(Cell cell) {
        }

        @Override
        public void notifyDeleteCell(Cell cell) {
        }

        @Override
        public void notifyUpdateCell(Cell cell) {
        }

        @Override
        public CellValue evaluate(Cell cell) {
            return null;
        }

        @Override
        public Cell evaluateInCell(Cell cell) {
            return null;
        }

        @Override
        public void setupReferencedWorkbooks(Map<String, FormulaEvaluator> workbooks) {
        }

        @Override
        public void setDebugEvaluationOutputForNextEval(boolean value) {
        }

        @Override
        public void setIgnoreMissingWorkbooks(boolean ignore) {
        }

        @Override
        public void evaluateAll() {
        }

        @Override
        public CellType evaluateFormulaCell(Cell cell) {
            return cell.getCachedFormulaResultType();
        }

        /**
         * @since POI 3.15 beta 3
         * @deprecated POI 3.15 beta 3. Will be deleted when we make the CellType enum transition. See bug 59791.
         */
        @Deprecated
        @Removal(version = "4.2")
        @Internal(since = "POI 3.15 beta 3")
        @Override
        public CellType evaluateFormulaCellEnum(Cell cell) {
            return evaluateFormulaCell(cell);
        }
    };

    public static void importExcel(ImportReq req) {
        checkNotEmpty(req.getFilePath(), "文件路径");

        String fileUrl = PropConstant_FILE_DOWNLOAD_URL + req.getFilePath();
        String localPath = PropConstant_CACHE_IMAGE_ROOT + req.getFilePath();
        File file = null;
        try {
            FileUtil.downloadFile(fileUrl, localPath);
            file = new File(localPath);

            Workbook workbook;
            try (InputStream in = new FileInputStream(file)) {
                workbook = WorkbookFactory.create(in);
            }

            Sheet sheet = workbook.getSheetAt(0);
            if (sheet.getLastRowNum() < 1) {
                fail("Excel中无数据");
            }

            List<User> userList = new ArrayList<>();
//            short lastCellNum = sheet.getRow(0).getLastCellNum();
            for (int i = 1, lastRowNum = sheet.getLastRowNum(); i <= lastRowNum; i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }

                /*boolean skip = true;
                for (short j = 0; j <= lastCellNum; j++) {
                    if (StringUtils.isNotBlank(getCellStringValue(row, j))) {
                        skip = false;
                        break;
                    }
                }
                if (skip) {
                    continue;
                }*/

                User user = new User();
                user.setName(getStringCellValue(row, 1, "姓名"));
                user.setSex(getIntegerCellValue(row, 2));

                userList.add(user);
            }

            if (userList.size() == 0) {
                fail("Excel中无数据");
            }

//            userMapper.insertAll(userList);
        } catch (IOException e) {
            e.printStackTrace();
            fail("文件导入失败");
        } finally {
            if (file != null && file.exists()) {
                if (!file.delete()) {
                    fail("文件删除失败");
                }
            }
        }
    }

    public static void exportExcel(ExportReq req) {
        List<User> userList = new ArrayList<>();
        userList.add(new User("张三", 0));
        userList.add(new User("李四", (Integer) null));

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("用户列表");

        int rowNum = 0, cellNum = 0;
        Row row = sheet.createRow(rowNum);
        row.createCell(cellNum).setCellValue("序号");
        row.createCell(++cellNum).setCellValue("姓名");
        row.createCell(++cellNum).setCellValue("性别");

        for (User user : userList) {
            row = sheet.createRow(++rowNum);
            row.createCell(cellNum = 0).setCellValue(rowNum);
            row.createCell(++cellNum).setCellValue(user.getName());
            row.createCell(++cellNum).setCellValue(formatNull(user.getSex()));
        }

        //合并单元格
        /*sheet.addMergedRegion(new CellRangeAddress(++rowNum, ++rowNum, 0, 2));
        Cell cell = sheet.createRow(rowNum - 1).createCell(0);
        cell.setCellValue("合并单元格");
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER); //水平居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER); //垂直居中
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex()); //背景色
        cell.setCellStyle(cellStyle);*/

//        ExcelUtil.exportToResponse(workbook, "用户列表");
        ExcelUtil.exportToFile(workbook, "E:\\用户列表.xlsx");
    }

    private static String formatNull(Object obj) {
        return obj == null ? "" : obj.toString();
    }

    public static void exportToResponse(Workbook workbook, String fileName) {
        exportToResponse(workbook, fileName, true);
    }

    public static void exportToResponse(Workbook workbook, String fileName, boolean useTitleStyle) {
        setWorkbookStyle(workbook, useTitleStyle);
        writeToResponse(workbook, fileName);
    }

    public static void exportToFile(Workbook workbook, String filePath) {
        exportToFile(workbook, filePath, true);
    }

    public static void exportToFile(Workbook workbook, String filePath, boolean useTitleStyle) {
        setWorkbookStyle(workbook, useTitleStyle);
        writeToFile(workbook, filePath);
    }

    private static void setWorkbookStyle(Workbook workbook, boolean useTitleStyle) {
/*        CellStyle cellStyle = workbook.createCellStyle();
//        cellStyle.setAlignment(CellStyle.ALIGN_CENTER); //水平居中
//        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); //垂直居中
        cellStyle.setAlignment(CellStyle.ALIGN_LEFT); //水平靠左
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_TOP); //垂直靠上
        cellStyle.setWrapText(true);  //允许单元格中内容换行*/

        CellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setAlignment(HorizontalAlignment.CENTER); //水平居中
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER); //垂直居中
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        titleStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex()); //背景色

        for (int i = 0, sheetNum = workbook.getNumberOfSheets(); i < sheetNum; i++) {
            Sheet sheet = workbook.getSheetAt(i);
            Row titleRow = sheet.getRow(0);
            if (titleRow == null) continue;
            for (int k = 0, titleRowCellNum = titleRow.getPhysicalNumberOfCells(); k <= titleRowCellNum; k++) {
                sheet.autoSizeColumn(k, true);
                int columnWidth = (int) (sheet.getColumnWidth(k) * 1.3);
                sheet.setColumnWidth(k, Math.min(columnWidth, 65280));
                Cell cell;
                if (useTitleStyle && (cell = titleRow.getCell(k)) != null) {
                    cell.setCellStyle(titleStyle);
                }
            }

/*            for (int j = 0, rowNum = sheet.getPhysicalNumberOfRows(); j < rowNum; j++) {
                Row row = sheet.getRow(j);
                for (int k = 0; k <= titleCellNum; k++) {
                    sheet.autoSizeColumn(k, true);
                    int columnWidth = (int) (sheet.getColumnWidth(k) * 1.3);
                    sheet.setColumnWidth(k, columnWidth > 65280 ? 65280 : columnWidth);
                    Cell cell = row.getCell(k);
                    if (cell != null) {
                        if (cell.getCellStyle().getFillForegroundColor() == IndexedColors.GREY_25_PERCENT.getIndex() || (useTitleStyle && j == 0)) {
                            cell.setCellStyle(titleStyle);
                        } else {
                            cell.setCellStyle(cellStyle);
                        }
                    }
                }
            }*/

            if (useTitleStyle) {
                sheet.createFreezePane(0, 1, 0, 1); //固定第一行
            }
        }
    }

    private static void writeToResponse(Workbook workbook, String fileName) {
        ServletRequestAttributes attributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        HttpServletResponse response = attributes.getResponse();
        assert response != null;
        response.setContentType("application/vnd.ms-excel");
        try {
            String agent = request.getHeader("USER-AGENT").toLowerCase();
            if (agent.contains("firefox")) {
                response.setCharacterEncoding("UTF-8");
                fileName = new String(fileName.getBytes(), "ISO8859-1");
            } else {
                fileName = URLEncoder.encode(fileName, "UTF-8");
            }
            response.setHeader("content-disposition", "attachment;filename=" + fileName + ".xlsx");
            OutputStream out = response.getOutputStream();
            workbook.write(out);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
            fail("导出失败");
//            throw new ServiceException(ResponseConstant.BUSSINESS_ERR, "导出失败");
        }
    }

    private static void writeToFile(Workbook book, String filePath) {
        try {
            Path path = Paths.get(filePath);
            Files.createDirectories(path.getParent());
            Files.createFile(path);
            try (FileOutputStream out = new FileOutputStream(path.toFile())) {
                book.write(out);
            }
        } catch (IOException e) {
            e.printStackTrace();
            fail("导出失败");
//            throw new ServiceException(ResponseConstant.BUSSINESS_ERR, "导出失败");
        }
    }

    public static String getStringCellValue(Row row, int cellNum, String cellName) {
        Cell cell = row.getCell(cellNum);
        String strVal = DATA_FORMATTER.formatCellValue(cell, DUMMY_EVALUATOR);
        if (cellName != null && StringUtils.isBlank(strVal)) {
            String msg = "导入失败！第" + (row.getRowNum() + 1) + "行的" + cellName + "不能为空";
            fail(msg);
//                throw new ServiceException(ResponseConstant.BUSSINESS_ERR, msg);
        }
        return strVal;
    }

    public static String getStringCellValue(Row row, int cellNum) {
        return getStringCellValue(row, cellNum, null);
    }

    public static Integer getIntegerCellValue(Row row, int cellNum, String cellName) {
        String stringValue = getStringCellValue(row, cellNum, cellName);
        return StringUtils.isBlank(stringValue) ? null : Integer.valueOf(stringValue);
    }

    public static Integer getIntegerCellValue(Row row, int cellNum) {
        return getIntegerCellValue(row, cellNum, null);
    }


    public static void main(String[] args) {

    }

}
