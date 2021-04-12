package com.myapp.service.util.excel;

import com.myapp.data.model.User;
import com.myapp.service.util.common.CollectionUtil;
import com.myapp.service.util.common.ResponseConstant;
import com.myapp.service.util.common.ServiceException;
import com.myapp.service.util.file.FileUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.record.crypto.Biff8EncryptionKey;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.crypt.Decryptor;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.crypt.EncryptionMode;
import org.apache.poi.poifs.crypt.Encryptor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.util.IOUtils;
import org.apache.poi.util.Internal;
import org.apache.poi.util.Removal;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.omg.CORBA.IntHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import static com.myapp.service.util.common.CommonUtil.checkNotEmpty;
import static com.myapp.service.util.common.CommonUtil.fail;
import static com.myapp.service.util.file.FileUtil.PropConstant_CACHE_IMAGE_ROOT;
import static com.myapp.service.util.file.FileUtil.PropConstant_FILE_DOWNLOAD_URL;

public class ExcelUtil {

    private static final DataFormatter DATA_FORMATTER = new DataFormatter();

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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
            response.setHeader("content-disposition", "attachment;filename=" + fileName + getExcelExtension(workbook));
            OutputStream out = response.getOutputStream();
            workbook.write(out);
            out.flush();
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServiceException(ResponseConstant.BUSINESS_ERR, "导出失败");
        }
    }

    public static void exportToFile(Workbook workbook, String filePath) {
        String fileFullPath = filePath + getExcelExtension(workbook);
        Path path = Paths.get(fileFullPath);
        try {
            Files.createDirectories(path.getParent());
            Files.createFile(path);
            try (OutputStream out = new FileOutputStream(fileFullPath)) {
                workbook.write(out);
            }
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServiceException(ResponseConstant.BUSINESS_ERR, "导出失败");
        }
    }

    public static void exportToFile(Workbook workbook, String filePath, String password) {
        if (isHSSFWorkbook(workbook)) {
            Biff8EncryptionKey.setCurrentUserPassword(password);
            exportToFile(workbook, filePath);
        } else {
            try (POIFSFileSystem fs = new POIFSFileSystem()) {
                EncryptionInfo info = new EncryptionInfo(EncryptionMode.agile);
                Encryptor enc = info.getEncryptor();
                enc.confirmPassword(password);

                try (OutputStream out = enc.getDataStream(fs)) {
                    workbook.write(out);
                }

                String fileFullPath = filePath + getExcelExtension(workbook);
                Path path = Paths.get(fileFullPath);
                Files.createDirectories(path.getParent());
                Files.createFile(path);
                try (OutputStream out = new FileOutputStream(fileFullPath)) {
                    fs.writeFilesystem(out);
                }

                workbook.close();
            } catch (IOException | GeneralSecurityException e) {
                e.printStackTrace();
                throw new ServiceException(ResponseConstant.BUSINESS_ERR, "导出失败");
            }
        }
    }

    public static Workbook createWorkbook(File file, String password) throws IOException {
        Workbook workbook;
        String extension = StringUtils.substringAfterLast(file.getName(), ".").toLowerCase();
        if ("xls".equals(extension)) {
            Biff8EncryptionKey.setCurrentUserPassword(password);
            workbook = WorkbookFactory.create(file);
        } else {
            POIFSFileSystem fs = new POIFSFileSystem(file);
            EncryptionInfo info = new EncryptionInfo(fs);
            Decryptor d = Decryptor.getInstance(info);
            try {
                if (!d.verifyPassword(password)) {
                    throw new ServiceException(ResponseConstant.BUSINESS_ERR, "Workbook密码错误");
                }
                workbook = WorkbookFactory.create(d.getDataStream(fs));
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
                throw new ServiceException(ResponseConstant.BUSINESS_ERR, "Workbook解密失败");
            }
        }
        return workbook;
    }

    private static boolean isHSSFWorkbook(Workbook workbook) {
        return workbook.getSpreadsheetVersion() == SpreadsheetVersion.EXCEL97;
    }

    private static String getExcelExtension(Workbook workbook) {
        return isHSSFWorkbook(workbook) ? ".xls" : ".xlsx";
    }

    public static void setTitleRowStyle(Sheet sheet) {
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER); //水平居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER); //垂直居中
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex()); //背景色
//        cellStyle.setWrapText(true);  //允许单元格中内容换行

        for (Cell cell : sheet.getRow(0)) {
            cell.setCellStyle(cellStyle);
        }

        sheet.createFreezePane(0, 1); //固定第一行
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

    /**
     * 添加图片
     */
    public static void addPicture(Workbook workbook, CreationHelper creationHelper, Drawing<?> drawing,
                                  int row1, int col1, String pictureUrl) {
        String extension = StringUtils.substringAfterLast(pictureUrl, ".").toUpperCase();
        Integer pictureType = null;
        switch (extension) {
            case "PNG":
                pictureType = Workbook.PICTURE_TYPE_PNG;
                break;
            case "JPG":
            case "JPEG":
                pictureType = Workbook.PICTURE_TYPE_JPEG;
                break;
        }
        if (pictureType == null && !isHSSFWorkbook(workbook)) {
            if ("GIF".equals(extension)) {
                pictureType = XSSFWorkbook.PICTURE_TYPE_GIF;
            }
        }
        if (pictureType == null) {
            throw new ServiceException(ResponseConstant.BUSINESS_ERR, extension + "图片格式不支持");
        }

        ClientAnchor clientAnchor = creationHelper.createClientAnchor();
        clientAnchor.setAnchorType(ClientAnchor.AnchorType.MOVE_AND_RESIZE);
        clientAnchor.setRow1(row1);
        clientAnchor.setCol1(col1);
        clientAnchor.setRow2(row1 + 1);
        clientAnchor.setCol2(col1 + 1);
        try (InputStream inputStream = new URL(pictureUrl).openStream()) {
            int pictureIndex = workbook.addPicture(IOUtils.toByteArray(inputStream), pictureType);
            Picture picture = drawing.createPicture(clientAnchor, pictureIndex);
//        picture.resize(0.2);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServiceException(ResponseConstant.BUSINESS_ERR, "添加图片失败");
        }
    }

    /**
     * 获取指定sheet中的图片，而workbook.getAllPictures()是获取所有sheet中的图片。
     */
    public static void getPictures(Sheet sheet) throws IOException {
        List<? extends Shape> shapes;
        if (sheet instanceof HSSFSheet) {
            HSSFPatriarch patriarch = ((HSSFSheet) sheet).getDrawingPatriarch();
            if (patriarch == null) return;
            shapes = patriarch.getChildren();
        } else {
            XSSFDrawing drawing = ((XSSFSheet) sheet).getDrawingPatriarch();
            if (drawing == null) return;
            shapes = drawing.getShapes();
        }
        for (Shape shape : shapes) {
            Picture picture = (Picture) shape;
            ClientAnchor clientAnchor = picture.getClientAnchor();
            System.out.println("col1: " + clientAnchor.getCol1() + ", col2: " + clientAnchor.getCol2() + ", row1: " + clientAnchor.getRow1() + ", row2: " + clientAnchor.getRow2());
//                System.out.println("x1: " + clientAnchor.getDx1() + ", x2: " + clientAnchor.getDx2() + ", y1: " + clientAnchor.getDy1() + ", y2: " + clientAnchor.getDy2());

            PictureData pictureData = picture.getPictureData();
            byte[] data = pictureData.getData();
            try (FileOutputStream out = new FileOutputStream("/Users/xw/Desktop/测试/" + UUID.randomUUID() + "." + pictureData.suggestFileExtension())) {
                out.write(data);
            }
        }
    }

    public static String formatNull(Object obj) {
        return obj == null ? "" : obj.toString();
    }

    public static String getStringCellValue(Row row, int cellNum, String cellName) {
        Cell cell = row.getCell(cellNum);
        String strVal = DATA_FORMATTER.formatCellValue(cell, DUMMY_EVALUATOR);
        if (cellName != null && StringUtils.isBlank(strVal)) {
            String msg = "导入失败！第" + (row.getRowNum() + 1) + "行的" + cellName + "不能为空";
            throw new ServiceException(ResponseConstant.BUSINESS_ERR, msg);
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

    public static LocalDateTime getLocalDateTimeCellValue(Row row, int cellNum, String cellName) {
        LocalDateTime dateTime;
        Cell cell = row.getCell(cellNum);
        if (cell.getCellType() == CellType.NUMERIC) {
            dateTime = cell.getLocalDateTimeCellValue();
        } else {
            String datetime = getStringCellValue(row, cellNum, cellName);
            dateTime = LocalDateTime.parse(datetime, DATE_TIME_FORMATTER);
        }
        if (cellName != null && dateTime == null) {
            String msg = "导入失败！第" + (row.getRowNum() + 1) + "行的" + cellName + "不能为空";
            throw new RuntimeException(msg);
        }
        return dateTime;
    }

    public static LocalDateTime getLocalDateTimeCellValue(Row row, int cellNum) {
        return getLocalDateTimeCellValue(row, cellNum, null);
    }

    private static <T> T getCellValue(Row row, int cellNum, String cellName, Function<String, T> function) {
        String stringValue = getStringCellValue(row, cellNum, cellName);
        return StringUtils.isBlank(stringValue) ? null : function.apply(stringValue);
    }


    /*↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑通用方法↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑*/


    public static void importExcel(ImportReq req) {
        checkNotEmpty(req.getFilePath(), "文件路径");

        String fileUrl = PropConstant_FILE_DOWNLOAD_URL + req.getFilePath();
        String localPath = PropConstant_CACHE_IMAGE_ROOT + req.getFilePath();
        File file = null;
        try {
            FileUtil.downloadFile(fileUrl, localPath);
            file = new File(localPath);

//            file = new File("/Users/xw/Desktop/测试/1597241828505.xls");
//            file = new File("/Users/xw/Desktop/测试/1597215197413.xlsx");

            List<User> userList = new ArrayList<>();
            try (Workbook workbook = WorkbookFactory.create(file)) {
                Sheet sheet = workbook.getSheetAt(0);

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

                //            getPictures(sheet);
            }

            if (userList.size() == 0) {
                fail("Excel中无数据");
            }

//            CollectionUtil.split(userList).forEach(it -> userMapper.insertAll(it));
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
        /*String pngPic = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1597212959041&di=" +
                "832afef8b1d6450729c2a4765e2099e8&imgtype=0&src=http%3A%2F%2Fpic21.nipic.com%2F20120610" +
                "%2F10296557_193505570000_2.png";
        String jpgPic = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1597212330302&di=" +
                "677d661a3f72609f80567305ef3f44af&imgtype=0&src=http%3A%2F%2Fimg1.imgtn.bdimg.com%2Fit%2Fu" +
                "%3D2991160191%2C2890588101%26fm%3D214%26gp%3D0.jpg";
        String gifPic = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1597213494593&di=" +
                "d6a2f5fea41cca6c9d70f4b049fccf39&imgtype=0&src=http%3A%2F%2Fhasgyy.com%2Fuploads%2Fallimg" +
                "%2F191122%2F1-191122093j6243.gif";*/

        List<User> list = new ArrayList<>();
/*        list.add(new User("张三", 1, 20, pngPic));
        list.add(new User("李四", 2, 25, jpgPic));
        list.add(new User("王五", null, null, gifPic));*/
//        list.add(new User("张三", 1, 20));
//        list.add(new User("李四", 2, 25));
//        list.add(new User("王五", null, null));

        for (int i = 0; i < 110; i++) {
            list.add(new User(String.valueOf(i)));
        }

//        Workbook workbook = new HSSFWorkbook();
//        Workbook workbook = new XSSFWorkbook();
        Workbook workbook = new SXSSFWorkbook();
        Sheet sheet = workbook.createSheet(WorkbookUtil.createSafeSheetName("用户/列表"));

/*        sheet.setDefaultRowHeight((short) (20 * 60));
        sheet.setDefaultColumnWidth(10);

        CreationHelper creationHelper = workbook.getCreationHelper();
        Drawing<?> drawing = sheet.createDrawingPatriarch();*/

/*        Map<Integer, Collection<String>> colValuesMap = new HashMap<>();
        colValuesMap.put(sexCellNum, sexMap.values());
        ExcelUtil.addValidation(sheet, colValuesMap);*/

        int rowNum = 0, cellNum = 0;
        Row row = sheet.createRow(rowNum);
        row.createCell(cellNum).setCellValue("序号");
        row.createCell(++cellNum).setCellValue("姓名");
        row.createCell(++cellNum).setCellValue("性别");
        row.createCell(++cellNum).setCellValue("年龄");
        row.createCell(++cellNum).setCellValue("头像");
        ExcelUtil.setTitleRowStyle(sheet);

        for (User item : list) {
            row = sheet.createRow(++rowNum);
            row.createCell(cellNum = 0).setCellValue(rowNum);
            row.createCell(++cellNum).setCellValue(item.getName());
            row.createCell(++cellNum).setCellValue(sexMap.get(item.getSex()));
            row.createCell(++cellNum).setCellValue(formatNull(item.getAge()));
//            ExcelUtil.addPicture(workbook, creationHelper, drawing, rowNum, ++cellNum, item.getAvatar());
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

/*        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER); //水平居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER); //垂直居中

        Row firstRow=sheet.createRow(0);

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 2));
        Cell cell = firstRow.createCell(1);
        cell.setCellValue("已完成摸底");
        cell.setCellStyle(cellStyle);

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 3, 4));
        Cell cell1 = firstRow.createCell(3);
        cell1.setCellValue("今日新增");
        cell1.setCellStyle(cellStyle);

        CellStyle cellStyle1 = workbook.createCellStyle();
        cellStyle1.setAlignment(HorizontalAlignment.CENTER); //水平居中
        cellStyle1.setVerticalAlignment(VerticalAlignment.CENTER); //垂直居中
        cellStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle1.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex()); //背景色
        for (Cell c : sheet.getRow(1)) {
            c.setCellStyle(cellStyle1);
        }

        sheet.createFreezePane(0, 2); //固定前两行*/

        // 删除某些行，包括合并单元格处理
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

//        ExcelUtil.exportToResponse(workbook, "用户列表");
        ExcelUtil.exportToFile(workbook, "/Users/xw/Desktop/测试/" + System.currentTimeMillis());
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


    public static void exportExcel1(Sheet sheet) {

        System.out.println(sheet.getSheetName() + "开始");

        List<User> list = new ArrayList<>();

        for (int i = 0; i < 1500; i++) {
            list.add(new User(String.valueOf(i)));
        }

        int rowNum = 0, cellNum = 0;
        Row row = sheet.createRow(rowNum);
        row.createCell(cellNum).setCellValue("序号");
        row.createCell(++cellNum).setCellValue("姓名");
        row.createCell(++cellNum).setCellValue("性别");
        row.createCell(++cellNum).setCellValue("年龄");
        row.createCell(++cellNum).setCellValue("头像");
        ExcelUtil.setTitleRowStyle(sheet);

        for (User item : list) {
            row = sheet.createRow(++rowNum);
            row.createCell(cellNum = 0).setCellValue(rowNum);
            row.createCell(++cellNum).setCellValue(item.getName());
            row.createCell(++cellNum).setCellValue(sexMap.get(item.getSex()));
            row.createCell(++cellNum).setCellValue(formatNull(item.getAge()));
//            ExcelUtil.addPicture(workbook, creationHelper, drawing, rowNum, ++cellNum, item.getAvatar());
        }

//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        System.out.println(sheet.getSheetName() + "结束");

    }

    public static void main(String[] args) throws InterruptedException {
//        System.out.println(Runtime.getRuntime().availableProcessors());

//        System.out.println(CellReference.convertNumToColString(26)); //"AA"
//        System.out.println(CellReference.convertColStringToIndex("AA")); //26

//        exportExcel(null);
//        importExcel(new ImportReq());

//        ExcelUtil excelUtil = new ExcelUtil();
//        C1 c1 = excelUtil.new C1();
//        c1.fun2();
//        c1.fun3();

        long start = System.currentTimeMillis();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CompletionService<Boolean> completionService = new ExecutorCompletionService<>(executorService);
//        ExecutorService executorService= Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

//        Workbook workbook = new SXSSFWorkbook();
//        for (int i = 0; i < 1400; i++) {
//            Sheet sheet = workbook.createSheet(i+"-"+WorkbookUtil.createSafeSheetName("用户/列表"));
//            executorService.execute(()->exportExcel1(sheet));
////            exportExcel1(sheet);
//        }

        IntHolder taskNum = new IntHolder(0);

        try {
            completionService.submit(() -> {
                System.out.println("第1个任务开始");
                Thread.sleep(4000);
                throw new RuntimeException("第1个任务抛异常");
//            System.out.println("第1个任务结束");
            });
            taskNum.value++;

            completionService.submit(() -> {
                System.out.println("第2个任务开始");
                Thread.sleep(2000);
                System.out.println("第2个任务结束");
                return true;
            });
            taskNum.value++;

            completionService.submit(() -> {
                System.out.println("第3个任务开始");
                Thread.sleep(8000);
                System.out.println("第3个任务结束");
                return true;
            });
            taskNum.value++;

//            executorService.shutdown();
//            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

            for (int i = 0; i < taskNum.value; i++) {
                try {
                    Future<Boolean> future = completionService.take(); //blocks if none available
                    if (!future.get()) { //若任务抛了异常，调用get方法时就会抛出
                        throw new RuntimeException("任务执行失败");
                    }
                    System.out.println("任务执行成功");
                } catch (Exception e) {
                    executorService.shutdownNow();
                    throw e;
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("耗时：" + (System.currentTimeMillis() - start));

//        ExcelUtil.exportToFile(workbook, "/Users/xw/Desktop/测试/" + System.currentTimeMillis());

    }

    class C1 {
        void fun1(Runnable runnable) {
            runnable.run();
        }

        void fun2() {
            fun1(() -> System.out.println(this));
        }

        void fun3() {
            fun1(new Runnable() {
                @Override
                public void run() {
                    System.out.println(this);
                }
            });
        }

    }

}
