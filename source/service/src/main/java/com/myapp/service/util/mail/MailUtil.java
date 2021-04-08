package com.myapp.service.util.mail;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Objects;
import java.util.Properties;

import static com.myapp.service.util.common.CommonUtil.fail;

public class MailUtil {

    public static void sendMail(MailSendParam param) {
        // 配置发送邮件的环境属性
        Properties props = new Properties();
        // 表示SMTP发送邮件，需要进行身份验证
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", param.getSmtpHost());
        props.put("mail.smtp.port", param.getSmtpPort());
        // 发件人邮箱地址
        props.put("mail.user", param.getUserName());
        // 发件人邮箱密码
        props.put("mail.password", param.getPassword());
        // 构建授权信息，用于进行SMTP身份验证
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                String userName = props.getProperty("mail.user");
                String password = props.getProperty("mail.password");
                return new PasswordAuthentication(userName, password);
            }
        };
        // 使用环境属性和授权信息，创建邮件会话
        Session mailSession = Session.getInstance(props, authenticator);
        // 创建邮件消息
        MimeMessage message = new MimeMessage(mailSession);

        try {
            // 设置发件人邮箱地址和昵称
            InternetAddress from = new InternetAddress(props.getProperty("mail.user"), param.getNickName());
            message.setFrom(from);

            // 设置收件人邮箱地址
            message.setRecipients(Message.RecipientType.TO, parseToArray(param.getTo()));
            // 设置抄送人邮箱地址
            message.setRecipients(Message.RecipientType.CC, parseToArray(param.getCc()));
            // 设置密送人邮箱地址
            message.setRecipients(Message.RecipientType.BCC, parseToArray(param.getBcc()));

            // 设置邮件标题
            message.setSubject(param.getSubject());

            // 创建多重消息
            Multipart multipart = new MimeMultipart();

            BodyPart bodyPart = new MimeBodyPart();
            bodyPart.setContent(param.getContent(), "text/html;charset=UTF-8");
            multipart.addBodyPart(bodyPart);

            if (CollectionUtils.isNotEmpty(param.getFiles())) {
                for (MultipartFile file : param.getFiles()) {
                    bodyPart = new MimeBodyPart();
                    DataSource source = new ByteArrayDataSource(file.getBytes(), file.getContentType());
                    bodyPart.setDataHandler(new DataHandler(source));
                    String fileName = Objects.requireNonNull(file.getOriginalFilename());
                    bodyPart.setFileName(MimeUtility.encodeWord(fileName));
                    multipart.addBodyPart(bodyPart);
                }
            }

            if (CollectionUtils.isNotEmpty(param.getFileUrls())) {
                for (String str : param.getFileUrls()) {
                    bodyPart = new MimeBodyPart();
                    URL url = new URL(str);
                    bodyPart.setDataHandler(new DataHandler(url));
                    String fileName = StringUtils.substringAfterLast(url.getPath(), "/");
                    bodyPart.setFileName(MimeUtility.encodeWord(fileName));
                    multipart.addBodyPart(bodyPart);
                }
            }

            message.setContent(multipart);

            Transport.send(message);
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    private static InternetAddress[] parseToArray(Collection<String> addresses) throws AddressException {
        if (CollectionUtils.isEmpty(addresses)) {
            return null;
        }

        InternetAddress[] addressArr = new InternetAddress[addresses.size()];
        int i = 0;
        for (String address : addresses) {
            addressArr[i++] = new InternetAddress(address);
        }
        return addressArr;
    }

}
