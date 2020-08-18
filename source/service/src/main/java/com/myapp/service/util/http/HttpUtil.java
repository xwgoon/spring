package com.myapp.service.util.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.net.URIBuilder;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpUtil {

    private static final ContentType TEXT_PLAIN_UTF8 = ContentType.create("text/plain", StandardCharsets.UTF_8);

    public static String postForm(String uri, Map<String, Object> params) {
        HttpPost httpPost = new HttpPost(uri);
        if (params != null && params.size() > 0) {
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            params.forEach((key, value) -> {
                if (value instanceof File) {
                    builder.addBinaryBody(key, (File) value);
                } else {
                    String strVal = value == null ? null : value.toString();
                    builder.addTextBody(key, strVal, TEXT_PLAIN_UTF8);
                }
            });
            httpPost.setEntity(builder.build());
        }

        return execute(httpPost);
    }

    public static String postJson(String uri, Map<String, Object> params) {
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setHeader("Content-type", "application/json;charset=UTF-8");
        if (params != null && params.size() > 0) {
            String jsonString = new JSONObject(params).toJSONString();
            StringEntity entity = new StringEntity(jsonString, TEXT_PLAIN_UTF8);
            httpPost.setEntity(entity);
        }

        return execute(httpPost);
    }

    public static String get(String uri, Map<String, Object> params) {
        try {
            URIBuilder builder = new URIBuilder(uri, StandardCharsets.UTF_8);
            if (params != null && params.size() > 0) {
                params.forEach((key, value) -> {
                    String strVal = value == null ? null : value.toString();
                    builder.addParameter(key, strVal);
                });
            }
            HttpGet httpGet = new HttpGet(builder.build());
            return execute(httpGet);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    private static String execute(ClassicHttpRequest request) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(request)) {
            String resString = "";
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                // 即使服务器Controller中方法的返回类型是void，response.getEntity()也不会是null，
                //EntityUtils.toString(response.getEntity())的值是空字符串""。
                resString = EntityUtils.toString(resEntity);
            }
            EntityUtils.consume(resEntity);
            return resString;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }


    public static void main(String[] args) {
        String uri;
        Map<String, Object> params = new HashMap<>();
        String res;


        /*uri = "http://localhost:8888/contextPath/xmlServletPath/controller0/post";
        File file = new File("C:\\Users\\Administrator\\Desktop\\导入模板.xlsx");
        params.put("name", "张三");
        params.put("file", file);
        res = postForm(uri, params);*/

        /*uri = "https://api.eol.cn/gkcx/api/?access_token=&keyword=%E8%AE%A1%E7%AE%97%E6%9C%BA&level1=1&page=1&province_id=&request_type=1&school_type=&signsafe=&size=20&special_id=&type=&uri=apidata/api/gk/schoolSpecial/lists";
        params.put("keyword", "计算机");
        params.put("level1", 1);
        params.put("request_type", 1);
        params.put("page", 1);
        params.put("size", 20);
        params.put("uri", "apidata/api/gk/schoolSpecial/lists");
        res = postJson(uri, params);*/

        uri = "https://static-data.eol.cn/www/2.0/schoolspecialindex/2019/140/51/1/1.json";
        res = get(uri, null);


        JSONObject resJo = JSON.parseObject(res);
        System.out.println(resJo);
    }

}
