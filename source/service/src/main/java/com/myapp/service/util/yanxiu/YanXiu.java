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

public class YanXiu {

    public static void main(String[] args) throws Exception {
        String cookie = "p_h5_u=51D5212B-CAEB-4267-A4C3-6F65F27FE608; UM_distinctid=1787daf0e9f35c-04d0e884abac3b-5771133-1fa400-1787daf0ea0428; Authorization=eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzaWNodWFuXzEwNDk5MiIsImF1ZGllbmNlIjoid2ViIiwiYXV0aCI6WyJST0xFX1RFQUNIRVJfVVNFUiJdLCJjcmVhdGVkIjoxNjE3MDIxOTQzMTM0LCJzdWJqZWN0SWQiOiIyYzk4MjhjYzc4MjY4NmRhMDE3ODNlNWFhYmNjNjdjNSIsInN1YklkIjoiZmY4MDgwODE3ODNiNWU5NzAxNzgzYmFmMzc4YjI2ZGEiLCJhcmVhQ29kZSI6IjUxMzIyMSIsImNsYXNzSWQiOiJmZjgwODA4MTc4MzZmNGI2MDE3ODNiYWMwM2ZkMTM1YiIsIm1hbmFnZXJDb2RlIjpudWxsLCJzY2hvb2xJZCI6ImZmODA4MDgxNzc0NmRkZmQwMTc3NDZlMDE3M2MwMTJjIiwidGVuYW50SWQiOjEsImV4cCI6MTYxNzYyNjc0MywicHJvamVjdElkIjoiMjAifQ.shS2ac5iWUQ7IjRR_Ak2K-zhK4KEhEHbxxCYnmgm2BxnE_Ldxw1jsO00dYZakHOfo4xE7I0p-1FSQpQuAofFOA; Authorization_token=%7B%22name%22%3A%22%E5%90%91%E6%B4%81%22%2C%22avatar%22%3A%22https%3A%2F%2Fgw.alipayobjects.com%2Fzos%2Frmsportal%2FBiazfanxmamNRoxxVxka.png%22%2C%22userid%22%3A%22ff808081783b5e9701783baf378b26da%22%2C%22areaCode%22%3A%22513221%22%2C%22areaName%22%3Anull%2C%22tenantId%22%3Anull%2C%22managerCode%22%3Anull%2C%22notifyCount%22%3A%220%22%2C%22proSubName%22%3Anull%2C%22impersonate%22%3Afalse%2C%22originIdentityUserId%22%3Anull%2C%22areaLevel%22%3Anull%2C%22tenantType%22%3Anull%2C%22schoolId%22%3A%22ff8080817746ddfd017746e0173c012c%22%2C%22schoolName%22%3Anull%2C%22projectId%22%3A%2220%22%2C%22projectName%22%3A%22%E5%9B%9B%E5%B7%9D%E7%9C%81%E6%95%B4%E6%A0%A1%E6%8E%A8%E8%BF%9B%E8%AF%95%E7%82%B9%E9%A1%B9%E7%9B%AE%22%2C%22classId%22%3A%22ff8080817836f4b601783bac03fd135b%22%2C%22subjectId%22%3A%222c9828cc782686da01783e5aabcc67c5%22%2C%22studentId%22%3A%22ff8080817836f4b601783baf3800142e%22%2C%22identityCode%22%3Anull%2C%22projectType%22%3A%22INFOMATIONPROJECT%22%2C%22roles%22%3A%5B%22ROLE_TEACHER_USER%22%5D%2C%22projectStartTime%22%3A%222021-03-01%22%2C%22projectEndTime%22%3A%222021-12-31%22%2C%22planningState%22%3Anull%2C%22testPaperState%22%3Anull%2C%22groupType%22%3Anull%2C%22groupId%22%3Anull%2C%22token%22%3A%22eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzaWNodWFuXzEwNDk5MiIsImF1ZGllbmNlIjoid2ViIiwiYXV0aCI6WyJST0xFX1RFQUNIRVJfVVNFUiJdLCJjcmVhdGVkIjoxNjE3MDIxOTQzMTM0LCJzdWJqZWN0SWQiOiIyYzk4MjhjYzc4MjY4NmRhMDE3ODNlNWFhYmNjNjdjNSIsInN1YklkIjoiZmY4MDgwODE3ODNiNWU5NzAxNzgzYmFmMzc4YjI2ZGEiLCJhcmVhQ29kZSI6IjUxMzIyMSIsImNsYXNzSWQiOiJmZjgwODA4MTc4MzZmNGI2MDE3ODNiYWMwM2ZkMTM1YiIsIm1hbmFnZXJDb2RlIjpudWxsLCJzY2hvb2xJZCI6ImZmODA4MDgxNzc0NmRkZmQwMTc3NDZlMDE3M2MwMTJjIiwidGVuYW50SWQiOjEsImV4cCI6MTYxNzYyNjc0MywicHJvamVjdElkIjoiMjAifQ.shS2ac5iWUQ7IjRR_Ak2K-zhK4KEhEHbxxCYnmgm2BxnE_Ldxw1jsO00dYZakHOfo4xE7I0p-1FSQpQuAofFOA%22%7D; CNZZDATA1277803829=1585787343-1617016103-http%253A%252F%252F2020scxj.yanxiuonline.com%252F%7C1617032306";

        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", cookie);

//        String courseRes = HttpUtil.get("http://2020scxj.yanxiuonline.com/api/v1/activity/abilityActivity/teacher/2c9828cc6ec0eaa8016ec0ed9ae30006?segmentType=COMPULSORY_COURSE&defaultCurrent=1&pageSize=1000", null, headers);
        String courseRes = HttpUtil.get("http://2020scxj.yanxiuonline.com/api/v1/activity/abilityActivity/teacher/2c9828cc6ec0eaa8016ec0fd1fef002d?segmentType=COMPULSORY_COURSE&defaultCurrent=1&pageSize=1000", null, headers);
        JSONArray courses = JSON.parseObject(courseRes).getJSONArray("rows");
        Pattern pattern = Pattern.compile("(?<=data-id=\").+(?=(?:\" class=\"(?:.*)\".+video))");

        int n = 0;
        for (int i = 0; i < courses.size(); ) {
            try {
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
                    int duration = JSON.parseObject(videoRes).getJSONObject("video").getIntValue("duration");
                    int minutes = duration / 60 + 1;
                    for (int j = 0; j < minutes; j++) {
                        String url = "http://2020scxj.yanxiuonline.com/api/v1/activity/segmentParticipations/" + course.getString("id") + "/setStudyTime";
                        String res = HttpUtil.postForm(url, null, headers);
                        if ("当前课程学习环节已达到最高时间要求不再计分".equals(JSON.parseObject(res).getString("error"))) {
                            break;
                        }
                        System.out.println(LocalDateTime.now() + ": " + ++n);
                        Thread.sleep(60000);
                    }

                    Map<String, Object> finishParams = new HashMap<>();
                    videoParams.put("courseInfoId", segmentId);
                    videoParams.put("courseParentId", id);
                    videoParams.put("finishState", "FINISH");
                    videoParams.put("point", duration);
                    String finishRes = HttpUtil.postJson("http://2020scxj.yanxiuonline.com/api/v1/onlineLearning/studyRecord/trackTime", finishParams, headers);
                }
                i++;
            } catch (Throwable e) {
                e.printStackTrace();
                Thread.sleep(3000);
            }
        }

    }


}
