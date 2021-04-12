package com.myapp.service.util.yanxiu;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.myapp.service.util.http.HttpUtil;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Base {

    /**
     * @param cookie    登录cookie
     * @param xsyxNum   从1开始
     * @param courseNum 从1开始
     */
    protected static void run(String cookie, int xsyxNum, int courseNum) {
        boolean isFinished = false;
        while (!isFinished) {
            try {
                int studySec = 0;
                Map<String, String> headers = new HashMap<>();
                headers.put("Cookie", cookie);

                String xsyxRes = HttpUtil.get("http://2020scxj.yanxiuonline.com/api/v1/activity/assessmentAbility/abilitiesByTeacherRole", null, headers);
                JSONArray xsyxArr = JSON.parseObject(xsyxRes).getJSONArray("rows");
                int xsyxCheckedNum = 0;
                Pattern pattern = Pattern.compile("(?<=data-id=\").+(?=(?:\" class=\"(?:.*)\".+video))");
                for (int k = 0; k < xsyxArr.size(); k++) {
                    JSONObject xsyxItem = xsyxArr.getJSONObject(k);
                    if (xsyxItem.getBoolean("checked")) {
                        xsyxCheckedNum++;
                        if (xsyxCheckedNum < xsyxNum) continue;
                        String ysyxId = xsyxItem.getString("id");
                        String courseRes = HttpUtil.get("http://2020scxj.yanxiuonline.com/api/v1/activity/abilityActivity/teacher/" + ysyxId + "?segmentType=COMPULSORY_COURSE&defaultCurrent=1&pageSize=1000", null, headers);
                        JSONArray courses = JSON.parseObject(courseRes).getJSONArray("rows");
                        for (int i = courseNum - 1; i < courses.size(); i++) {
                            JSONObject course = courses.getJSONObject(i);
                            String id = course.getString("compulsoryCourseId");
                            Map<String, Object> segmentParams = new HashMap<>();
                            segmentParams.put("id", id);
                            segmentParams.put("segmentId", course.getString("id"));
                            Map<String, String> segmentHeaders = new HashMap<>();
                            segmentHeaders.put("Cookie", cookie);
                            segmentHeaders.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
                            String segmentRes = HttpUtil.get("http://2020scxj.yanxiuonline.com/admin/lock/Study", segmentParams, segmentHeaders);
                            Matcher matcher = pattern.matcher(segmentRes);
                            while (matcher.find()) {
                                String segmentId = matcher.group(0);
                                Map<String, Object> videoParams = new HashMap<>();
                                videoParams.put("lessonId", segmentId);
                                String videoRes = HttpUtil.get("http://2020scxj.yanxiuonline.com/api/v1/lecture/video/queryVideo", videoParams, headers);
                                JSONObject videoObject = JSON.parseObject(videoRes);
                                if (videoObject.getBoolean("success")) {
                                    int duration = videoObject.getJSONObject("video").getIntValue("duration");
                                    int minutes = duration / 60 + 1;
                                    for (int j = 0; j < minutes; j++) {
                                        String url = "http://2020scxj.yanxiuonline.com/api/v1/activity/segmentParticipations/" + course.getString("id") + "/setStudyTime";
                                        String res = HttpUtil.postForm(url, null, headers);
                                        String error = JSON.parseObject(res).getString("error");
                                        if ("当前课程学习环节已达到最高时间要求不再计分".equals(error) || "已经提醒".equals(error)) {
                                            break;
                                        }
                                        System.out.println(LocalDateTime.now() + ": " + ++studySec);
                                        Thread.sleep(60000);
                                    }

                                    Map<String, Object> finishParams = new HashMap<>();
                                    finishParams.put("courseInfoId", segmentId);
                                    finishParams.put("courseParentId", id);
                                    finishParams.put("finishState", "FINISH");
                                    finishParams.put("point", duration);
                                    String watchRes = HttpUtil.postJson("http://2020scxj.yanxiuonline.com/api/v1/onlineLearning/studyRecord/trackCourse", finishParams, headers);
                                    String finishRes = HttpUtil.postJson("http://2020scxj.yanxiuonline.com/api/v1/onlineLearning/studyRecord/trackTime", finishParams, headers);
                                }
                            }
                        }
                        courseNum = 1;
                    }
                }
                isFinished = true;
            } catch (Throwable e) {
                e.printStackTrace();
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

}
