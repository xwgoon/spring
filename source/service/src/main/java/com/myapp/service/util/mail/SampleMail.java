package com.myapp.service.util.mail;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class SampleMail {

    private static final String SMTP_HOST = "smtp.qq.com";
    private static final String SMTP_PORT = "587";

    public static void main(String[] args) {
        // 配置发送邮件的环境属性
        final Properties props = new Properties();
        // 表示SMTP发送邮件，需要进行身份验证
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        // 如果使用ssl，则去掉使用25端口的配置，进行如下配置,
        // props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        // props.put("mail.smtp.socketFactory.port", "465");
        // props.put("mail.smtp.port", "465");
        // 发件人的账号
        props.put("mail.user", "1510962196@qq.com");
        // 访问SMTP服务时需要提供的密码
        props.put("mail.password", "jnixndgfswuohcja");
        // 构建授权信息，用于进行SMTP进行身份验证
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // 用户名、密码
                String userName = props.getProperty("mail.user");
                String password = props.getProperty("mail.password");
                return new PasswordAuthentication(userName, password);
            }
        };
        // 使用环境属性和授权信息，创建邮件会话
        Session mailSession = Session.getInstance(props, authenticator);
//        mailSession.setDebug(true);
        //UUID uuid = UUID.randomUUID();
        //final String messageIDValue = "<" + uuid.toString() + ">";
        // 创建邮件消息
        MimeMessage message = new MimeMessage(mailSession) {
            //@Override
            //protected void updateMessageID() throws MessagingException {
            //设置自定义Message-ID值
            //setHeader("Message-ID", messageIDValue);
            //}
        };

        try {
            // 设置发件人邮件地址和名称
            InternetAddress from = new InternetAddress(props.getProperty("mail.user"), "张三");
            message.setFrom(from);

            //可选。设置回信地址
//            Address[] a = new Address[1];
//            a[0] = new InternetAddress("***");
//            message.setReplyTo(a);

            // 设置收件人邮件地址，比如yyy@yyy.com
            InternetAddress to = new InternetAddress("1178124579@qq.com");
            message.setRecipient(MimeMessage.RecipientType.TO, to);
            //如果同时发给多人，才将上面两行替换为如下：
            //InternetAddress[] adds = new InternetAddress[2];
            //adds[0] = new InternetAddress("xxxxx@qq.com");
            //adds[1] = new InternetAddress("xxxxx@qq.com");
            //message.setRecipients(Message.RecipientType.TO, adds);

            // 设置多个抄送地址
//            String ccUser = "抄送邮箱";
//            if(null != ccUser && !ccUser.isEmpty()){
//                @SuppressWarnings("static-access")
//                InternetAddress[] internetAddressCC = new InternetAddress().parse(ccUser);
//                message.setRecipients(Message.RecipientType.CC, internetAddressCC);
//            }

            // 设置多个密送地址
//            String bccUser = "密送邮箱";
//            if(null != bccUser && !bccUser.isEmpty()){
//                @SuppressWarnings("static-access")
//                InternetAddress[] internetAddressBCC = new InternetAddress().parse(bccUser);
//                message.setRecipients(Message.RecipientType.BCC, internetAddressBCC);
//            }

            // 设置邮件标题
            message.setSubject("测试邮件");
            // 设置邮件的内容体
//            message.setContent("<html><body><a href=\"https://www.baidu.com\">点我</a> 哈哈</body></html>", "text/html;charset=UTF-8");

            //若需要开启邮件跟踪服务，请使用以下代码设置跟踪链接头。首先域名需要备案，设置且已正确解析了CNAME配置；其次发信需要打Tag，此Tag在控制台已创建并存在，Tag创建10分钟后方可使用；
            //String tagName = "Test";
            //HashMap<String, String> trace = new HashMap<>();
            //trace.put("OpenTrace", "1");
            //trace.put("TagName", tagName);
            //String jsonTrace = JSON.toJSONString(trace);
            //String base64Trace = new String(Base64.encodeBase64(jsonTrace.getBytes()));
            //设置跟踪链接头
            //message.addHeader("X-AliDM-Trace", base64Trace);

            // 创建多重消息
            Multipart multipart = new MimeMultipart();

            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent("<html><body><a href=\"https://www.baidu.com\">点我</a> 哈哈</body></html>", "text/html;charset=UTF-8");
            multipart.addBodyPart(messageBodyPart);

            messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource("d://test1.txt");
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(MimeUtility.encodeWord("测试文件1.txt"));
            multipart.addBodyPart(messageBodyPart);

            messageBodyPart = new MimeBodyPart();
            source = new FileDataSource("d://test2.txt");
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(MimeUtility.encodeWord("测试文件2.txt"));
            multipart.addBodyPart(messageBodyPart);

            message.setContent(multipart);

            Transport.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            String err = e.getMessage();
            System.out.println(err);
        }
    }

}
