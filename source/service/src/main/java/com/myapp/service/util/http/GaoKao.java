package com.myapp.service.util.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.myapp.service.util.excel.ExcelUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

public class GaoKao {

    private static JSONArray getSchool(String specialKeyword, int page) {
        String encodedKw;
        try {
            encodedKw = URLEncoder.encode(specialKeyword, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
//        String provinceId = "51";
        String provinceId = "";
        String uri = "https://api.eol.cn/gkcx/api/?access_token=&keyword=" + encodedKw + "&level1=1&page=" + page
                + "&province_id=" + provinceId + "&request_type=1&school_type=&signsafe=&size=20&special_id=&type=&uri=apidata/api/gk/schoolSpecial/lists";
        Map<String, Object> params = new HashMap<>();
        params.put("keyword", specialKeyword);
        params.put("province_id", provinceId);
        params.put("level1", 1);
        params.put("request_type", 1);
        params.put("page", page);
        params.put("size", 20);
        params.put("uri", "apidata/api/gk/schoolSpecial/lists");
        String res = HttpUtil.postJson(uri, params);
        System.out.println(res);
        return JSON.parseObject(res).getJSONObject("data").getJSONArray("item");
    }

    private static JSONObject getSpecial(int year, int schoolId, int page) {
        String uri = "https://static-data.eol.cn/www/2.0/schoolspecialindex/" + year + "/" + schoolId + "/51/1/" + page + ".json";
        String res = HttpUtil.get(uri, null);
        System.out.println(res);
        return "\"\"".equals(res) ? null : JSON.parseObject(res);
    }

    private static String formatIs(Integer val) {
        return val == null ? "" : val == 1 ? "是" : "否";
    }

    private static List<SpecialVo> getSpecialList(String specialKeyword, int year, Set<String> schoolSpecialSet) {
        List<SpecialVo> specialVoList = new ArrayList<>();
        int schoolPage = 1;

        for (; ; ) {
            JSONArray schoolArray = getSchool(specialKeyword, schoolPage++);
            if (schoolArray.size() == 0) break;

            for (int i = 0, in = schoolArray.size(); i < in; i++) {
                System.out.println((schoolPage - 1) + "_" + (i + 1));

                JSONObject school = schoolArray.getJSONObject(i);
                Integer schoolId = school.getInteger("school_id");
                String schoolName = school.getString("name");
                Integer f985 = school.getInteger("f985");
                Integer f211 = school.getInteger("f211");
                String specialName = school.getString("spname");

                if (!schoolSpecialSet.add(schoolName + "@_#" + specialName)) continue;

                int specialPage = 1;
                for (; ; ) {
                    JSONObject specialJo = getSpecial(year, schoolId, specialPage++);
                    if (specialJo == null) break;

                    JSONObject data = specialJo.getJSONObject("data");
                    JSONArray specialArray = data.getJSONArray("item");
                    for (int j = 0, jn = specialArray.size(); j < jn; j++) {
                        JSONObject special = specialArray.getJSONObject(j);
                        String specialCategory = special.getString("spname");
                        if (specialCategory != null &&
                                (specialCategory.contains("计算机") || specialCategory.contains(specialKeyword))) {
//                        if (specialCategory != null &&
//                                (specialCategory.contains(specialKeyword))) {
                            SpecialVo specialVo = new SpecialVo();
                            specialVo.setSchoolName(schoolName);
                            specialVo.setF985(formatIs(f985));
                            specialVo.setF211(formatIs(f211));
                            specialVo.setSpecialName(specialName);
                            specialVo.setMax(special.getString("max"));
                            specialVo.setAverage(special.getString("average"));
                            specialVo.setMin(special.getString("min"));
                            specialVo.setMinSection(special.getString("min_section"));
                            specialVo.setLocalBatchName(special.getString("local_batch_name"));
                            specialVo.setSpecialCategory(specialCategory);

                            specialVoList.add(specialVo);
                        }
                    }
                }
            }

//            break;
        }

        return specialVoList;
    }

    private static String calcDiff(String val1, int val2) {
        return "-".equals(val1) ? "-" : String.valueOf((int) (Double.parseDouble(val1) - val2));
    }

    private static String formatDouble(String val1) {
        return "-".equals(val1) ? "-" : String.valueOf(Double.valueOf(val1).intValue());
    }

    public static void main(String[] args) {
        List<SpecialVo> specialVoList;
        Set<String> schoolSpecialSet = new HashSet<>();

        int year = 2019;
        int erBenLine = 459;

        List<SpecialVo> computerList = getSpecialList("计算机", year, schoolSpecialSet);
        specialVoList = new ArrayList<>(computerList);
        List<SpecialVo> softwareList = getSpecialList("软件", year, schoolSpecialSet);
        specialVoList.addAll(softwareList);

//        List<SpecialVo> computerList = getSpecialList("电气", year, schoolSpecialSet);
//        specialVoList = new ArrayList<>(computerList);

        specialVoList.sort((s1, s2) -> s2.getMin().compareTo(s1.getMin()));

        Workbook workbook = new XSSFWorkbook();
//        String name = "电气工程及其自动化_四川_理科_" + year;
        String name = "计算机&软件_四川_理科_" + year + "_全国高校";
        Sheet sheet = workbook.createSheet(name);

        int rowNum = 0, cellNum = 0;
        Row row = sheet.createRow(rowNum);
        row.createCell(cellNum).setCellValue("序号");
        row.createCell(++cellNum).setCellValue("学校名称");
        row.createCell(++cellNum).setCellValue("985");
        row.createCell(++cellNum).setCellValue("211");
        row.createCell(++cellNum).setCellValue("专业名称");
        row.createCell(++cellNum).setCellValue("最高分");
        row.createCell(++cellNum).setCellValue("平均分");
        row.createCell(++cellNum).setCellValue("最低分");
        row.createCell(++cellNum).setCellValue("最低分与二本线分差");
        row.createCell(++cellNum).setCellValue("最低位次");
        row.createCell(++cellNum).setCellValue("录取批次");
        row.createCell(++cellNum).setCellValue("专业类别");

        for (SpecialVo specialVo : specialVoList) {
            row = sheet.createRow(++rowNum);
            row.createCell(cellNum = 0).setCellValue(rowNum);
            row.createCell(++cellNum).setCellValue(specialVo.getSchoolName());
            row.createCell(++cellNum).setCellValue(specialVo.getF985());
            row.createCell(++cellNum).setCellValue(specialVo.getF211());
            row.createCell(++cellNum).setCellValue(specialVo.getSpecialName());
            row.createCell(++cellNum).setCellValue(formatDouble(specialVo.getMax()));
            row.createCell(++cellNum).setCellValue(formatDouble(specialVo.getAverage()));
            row.createCell(++cellNum).setCellValue(formatDouble(specialVo.getMin()));
            row.createCell(++cellNum).setCellValue(calcDiff(specialVo.getMin(), erBenLine));
            row.createCell(++cellNum).setCellValue(specialVo.getMinSection());
            row.createCell(++cellNum).setCellValue(specialVo.getLocalBatchName());
            row.createCell(++cellNum).setCellValue(specialVo.getSpecialCategory());
        }

        ExcelUtil.exportToFile(workbook, "/Users/xw/Desktop/高考/" + System.currentTimeMillis() + "/" + name + ".xlsx");
    }

}
