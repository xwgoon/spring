package com.myapp.service.util.http;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpUtil {

    private static final ContentType TEXT_PLAIN_UTF8 = ContentType.create("text/plain", StandardCharsets.UTF_8);

    public static String postForm(String uri) {
        return postForm(uri, null);
    }

    public static String postForm(String uri, Map<String, Object> params) {
        return postForm(uri, params, null);
    }

    public static String postForm(String uri, Map<String, Object> params, Map<String, String> headers) {
        HttpPost httpPost = new HttpPost(uri);
        if (params != null && params.size() > 0) {
            MultipartEntityBuilder builder = MultipartEntityBuilder.create()
                    .setMode(HttpMultipartMode.RFC6532); // 解决文件名乱码问题
            params.forEach((key, value) -> {
                if (value instanceof Iterable) {
                    ((Iterable<?>) value).forEach(it -> addBody(builder, key, it));
                } else if (value.getClass().isArray()) {
                    for (Object it : ((Object[]) value)) {
                        addBody(builder, key, it);
                    }
                } else {
                    addBody(builder, key, value);
                }
            });
            httpPost.setEntity(builder.build());
        }
        if (headers != null && headers.size() > 0) {
            headers.forEach(httpPost::addHeader);
        }
        return execute(httpPost);
    }

    private static void addBody(MultipartEntityBuilder builder, String key, Object value) {
        if (value instanceof File) {
            builder.addBinaryBody(key, (File) value);
        } else {
            String strVal = value == null ? null : value.toString();
            builder.addTextBody(key, strVal, TEXT_PLAIN_UTF8);
        }
    }

    public static String postJson(String uri) {
        return postJson(uri, null);
    }

    public static String postJson(String uri, Map<String, Object> params) {
        return postJson(uri, params, null);
    }

    public static String postJson(String uri, Map<String, Object> params, Map<String, String> headers) {
        HttpPost httpPost = new HttpPost(uri);
        httpPost.addHeader("Content-type", "application/json;charset=UTF-8");
        if (params != null && params.size() > 0) {
            String jsonString = new JSONObject(params).toJSONString();
            StringEntity entity = new StringEntity(jsonString, TEXT_PLAIN_UTF8);
            httpPost.setEntity(entity);
        }
        if (headers != null && headers.size() > 0) {
            headers.forEach(httpPost::addHeader);
        }
        return execute(httpPost);
    }

    public static String get(String uri) {
        return get(uri, null);
    }

    public static String get(String uri, Map<String, Object> params) {
        return get(uri, params, null);
    }

    public static String get(String uri, Map<String, Object> params, Map<String, String> headers) {
        try {
            URIBuilder builder = new URIBuilder(uri, StandardCharsets.UTF_8);
            if (params != null && params.size() > 0) {
                params.forEach((key, value) -> {
                    if (value instanceof Iterable) {
                        ((Iterable<?>) value).forEach(it -> addParameter(builder, key, it));
                    } else if (value.getClass().isArray()) {
                        for (Object it : ((Object[]) value)) {
                            addParameter(builder, key, it);
                        }
                    } else {
                        addParameter(builder, key, value);
                    }
                });
            }
            HttpGet httpGet = new HttpGet(builder.build());
            if (headers != null && headers.size() > 0) {
                headers.forEach(httpGet::addHeader);
            }
            return execute(httpGet);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static void addParameter(URIBuilder builder, String key, Object value) {
        String strVal = value == null ? null : value.toString();
        builder.addParameter(key, strVal);
    }

    private static String execute(HttpRequestBase request) {
        // 设置代理
//        HttpHost proxy = new HttpHost("127.0.0.1", 1087, "http");
//        RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
//        request.setConfig(config);

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
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) throws UnsupportedEncodingException {
        String uri;
        Map<String, Object> params = new HashMap<>();
        String res;

//        params.put("smtpHost", "smtp.qq.com");
//        params.put("smtpPort", "587");
//        params.put("userName", "1510962196@qq.com");
//        params.put("password", "jnixndgfswuohcja");
//        params.put("to", new String[]{"1178124579@qq.com", "xiaowei_wyyx@163.com"});
//        params.put("subject", "测试邮件");
//        params.put("content", "<html><body><a href=\"https://www.baidu.com\">点我</a> 哈哈</body></html>");
//        params.put("files", new File(DIR + "util/mail/测试文件.txt"));
//        params.put("fileUrls", "https://ss0.baidu.com/7Po3dSag_xI4khGko9WTAnF6hhy/zhidao/pic/item/9c16fdfaaf51f3de9ba8ee1194eef01f3a2979a8.jpg");
//        postForm("http://192.168.80.64:9000/api/mail/send", params);

//        params.put("projectName", "ypdb");
//        params.put("type", "11");
//        params.put("file", new File(DIR + "util/mail/测试文件.txt"));
//        System.out.println(postForm("https://fileapi.adas.com/file/PostUploadFile", params));
//        System.out.println(postForm("http://localhost:8080/contextPath/xmlServletPath/controller1/postForm", params));

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

//        uri = "http://api.map.baidu.com/geocoder/v2/?output=json&address=%E5%91%A8%E5%9F%AD%E6%9D%91%E5%A7%94%E4%BC%9A&city=%E6%B3%B0%E5%B7%9E%E5%B8%82&ak=6AebMREDLGyCvC3EAj4QgYVzDOtFVsMp";
//
//        res = URLEncoder.encode(uri, "UTF-8");
//        System.out.println();

//        res = get(uri, null);
//
//
//        JSONObject resJo = JSON.parseObject(res);
//        System.out.println(resJo);
    }

}
