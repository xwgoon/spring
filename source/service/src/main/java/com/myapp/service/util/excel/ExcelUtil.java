package com.myapp.service.util.excel;

import com.myapp.data.model.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.util.Internal;
import org.apache.poi.util.Removal;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;

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

    /**
     * 调用此方法的Controller方法，返回类型必须为void。
     */
    public static void exportToResponse(Workbook workbook, String fileName) {
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
            String extension = workbook.getSpreadsheetVersion() == SpreadsheetVersion.EXCEL97 ? "xls" : "xlsx";
            response.setHeader("content-disposition", "attachment;filename=" + fileName + "." + extension);
            OutputStream out = response.getOutputStream();
            workbook.write(out);
            out.flush();
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
            fail("导出失败");
//            throw new ServiceException(ResponseConstant.BUSSINESS_ERR, "导出失败");
        }
    }

    public static void exportToFile(Workbook workbook, String filePath) {
        try {
            Path path = Paths.get(filePath);
            Files.createDirectories(path.getParent());
            Files.createFile(path);
            try (FileOutputStream out = new FileOutputStream(filePath)) {
                workbook.write(out);
            }
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
            fail("导出失败");
//            throw new ServiceException(ResponseConstant.BUSSINESS_ERR, "导出失败");
        }
    }

    public static void setTitleStyle(Workbook workbook) {
        CellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setAlignment(HorizontalAlignment.CENTER); //水平居中
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER); //垂直居中
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        titleStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex()); //背景色
//        titleStyle.setWrapText(true);  //允许单元格中内容换行

        for (Sheet sheet : workbook) {
            Row titleRow = sheet.getRow(0);
            if (titleRow == null) continue;
            for (Cell cell : titleRow) {
                cell.setCellStyle(titleStyle);
            }
            sheet.createFreezePane(0, 1, 0, 1); //固定第一行
        }
    }

    public static void addValidation(Sheet sheet, Map<Integer, Collection<String>> colValuesMap) {
        DataValidationHelper dvHelper = sheet.getDataValidationHelper();
        int lastRowIndex = sheet.getWorkbook().getSpreadsheetVersion().getLastRowIndex();
        colValuesMap.forEach((col, values) -> {
            DataValidationConstraint dvConstraint = dvHelper.createExplicitListConstraint(values.toArray(new String[0]));
            CellRangeAddressList cellRange = new CellRangeAddressList(1, lastRowIndex, col, col);
            DataValidation dv = dvHelper.createValidation(dvConstraint, cellRange);

//        dv.setSuppressDropDownArrow(false);

//        dv.createPromptBox("Valid Values", "当前值合法");
//        dv.setShowPromptBox(true);

//        dv.setErrorStyle(DataValidation.ErrorStyle.STOP);
//        dv.createErrorBox("Validation Error", "当前值非法");
            dv.setShowErrorBox(true);

            sheet.addValidationData(dv);
        });
    }

    public static String formatNull(Object obj) {
        return obj == null ? "" : obj.toString();
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
        return getCellValue(row, cellNum, cellName, Integer::valueOf);
    }

    public static Integer getIntegerCellValue(Row row, int cellNum) {
        return getIntegerCellValue(row, cellNum, null);
    }

    public static Long getLongCellValue(Row row, int cellNum, String cellName) {
        return getCellValue(row, cellNum, cellName, Long::valueOf);
    }

    public static Long getLongCellValue(Row row, int cellNum) {
        return getLongCellValue(row, cellNum, null);
    }

    public static Double getDoubleCellValue(Row row, int cellNum, String cellName) {
        return getCellValue(row, cellNum, cellName, Double::valueOf);
    }

    public static Double getDoubleCellValue(Row row, int cellNum) {
        return getDoubleCellValue(row, cellNum, null);
    }

    public static BigDecimal getBigDecimalCellValue(Row row, int cellNum, String cellName) {
        return getCellValue(row, cellNum, cellName, BigDecimal::new);
    }

    public static BigDecimal getBigDecimalCellValue(Row row, int cellNum) {
        return getBigDecimalCellValue(row, cellNum, null);
    }

    private static <T> T getCellValue(Row row, int cellNum, String cellName, Function<String, T> function) {
        String stringValue = getStringCellValue(row, cellNum, cellName);
        return StringUtils.isBlank(stringValue) ? null : function.apply(stringValue);
    }


    /*↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑通用方法↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑*/


    public static void importExcel(ImportReq req) {
//        checkNotEmpty(req.getFilePath(), "文件路径");

        String fileUrl = PropConstant_FILE_DOWNLOAD_URL + req.getFilePath();
        String localPath = PropConstant_CACHE_IMAGE_ROOT + req.getFilePath();
        File file = null;
        Workbook workbook = null;
        try {
//            FileUtil.downloadFile(fileUrl, localPath);
//            file = new File(localPath);

            file = new File("C:\\Users\\Administrator\\Desktop\\1596437442109.xlsx");

            workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheetAt(0);

            List<User> userList = new ArrayList<>();
//            short lastCellNum = sheet.getRow(0).getLastCellNum();
            for (int i = 1, lastRowNum = sheet.getLastRowNum(); i <= lastRowNum; i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

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
                user.setSex(parseType(row, sexCellNum, sexMap));
                user.setAge(getIntegerCellValue(row, 3));

                userList.add(user);
            }

            if (userList.size() == 0) {
                fail("Excel中无数据");
            }

            /*List<List<User>> userSplitList = CollectionUtil.split(userList);
            userSplitList.forEach(it -> userMapper.insertAll(it));*/
        } catch (IOException e) {
            e.printStackTrace();
            fail("文件导入失败");
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (file != null && file.exists()) {
//                if (!file.delete()) {
//                    fail("文件删除失败");
//                }
            }
        }
    }

    public static void exportExcel(ExportReq req) {
        Workbook workbook = new SXSSFWorkbook();
        Sheet sheet = workbook.createSheet(WorkbookUtil.createSafeSheetName("用户/列表"));

        Map<Integer, Collection<String>> colValuesMap = new HashMap<>();
        colValuesMap.put(sexCellNum, sexMap.values());
        addValidation(sheet, colValuesMap);

        int rowNum = 0, cellNum = 0;
        Row row = sheet.createRow(rowNum);
        row.createCell(cellNum).setCellValue("序号");
        row.createCell(++cellNum).setCellValue("姓名");
        row.createCell(++cellNum).setCellValue("性别");
        row.createCell(++cellNum).setCellValue("年龄");
        setTitleStyle(workbook);

        List<User> list = new ArrayList<>();
        list.add(new User("张三", 1, 20));
        list.add(new User("李四", 2, 25));
        list.add(new User("王五", null, null));

        for (User item : list) {
            row = sheet.createRow(++rowNum);
            row.createCell(cellNum = 0).setCellValue(rowNum);
            row.createCell(++cellNum).setCellValue(item.getName());
            row.createCell(++cellNum).setCellValue(sexMap.get(item.getSex()));
            row.createCell(++cellNum).setCellValue(formatNull(item.getAge()));
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

        ExcelUtil.exportToResponse(workbook, "用户列表");
//        ExcelUtil.exportToFile(workbook, "/Users/xw/Desktop/测试/" + System.currentTimeMillis() + ".xlsx");
    }

    private static final int sexCellNum = 2;
    private static final Map<Integer, String> sexMap = new LinkedHashMap<Integer, String>() {
        {
            put(1, "男");
            put(2, "女");
        }
    };

    private static final Map<Integer, String> hasMap = new LinkedHashMap<Integer, String>() {
        {
            put(1, "有");
            put(0, "无");
        }
    };

    private static final Map<Integer, String> isMap = new LinkedHashMap<Integer, String>() {
        {
            put(1, "是");
            put(0, "否");
        }
    };

    private static Integer parseType(Row row, int cellNum, Map<Integer, String> map) {
        String value = getStringCellValue(row, cellNum, null);
        if (StringUtils.isBlank(value)) return null;

        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) return entry.getKey();
        }

//        fail("导入失败！第" + (row.getRowNum() + 1) + "行的" + cellName + "[" + value + "]值非法");
        return -1;
    }


    public static void main(String[] args) {
//        System.out.println(CellReference.convertNumToColString(26)); //"AA"
//        System.out.println(CellReference.convertColStringToIndex("AA")); //26

//        exportExcel(null);
        importExcel(new ImportReq());

    }

}
