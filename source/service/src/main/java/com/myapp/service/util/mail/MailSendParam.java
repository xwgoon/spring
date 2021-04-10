package com.myapp.service.util.mail;

//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;

//@ApiModel("邮件发送参数")
public class MailSendParam {

//    @ApiModelProperty("SMTP主机")
    private String smtpHost;
//    @ApiModelProperty("SMTP端口")
    private Integer smtpPort;
//    @ApiModelProperty("发件人邮箱地址")
    private String userName;
//    @ApiModelProperty("发件人邮箱密码")
    private String password;
//    @ApiModelProperty("发件人昵称")
    private String nickName;
//    @ApiModelProperty("收件人邮箱地址")
    private Collection<String> to;
//    @ApiModelProperty("抄送人邮箱地址")
    private Collection<String> cc;
//    @ApiModelProperty("密送人邮箱地址")
    private Collection<String> bcc;
//    @ApiModelProperty("邮件主题")
    private String subject;
//    @ApiModelProperty("邮件内容")
    private String content;
//    @ApiModelProperty("附件(文件)")
    private Collection<MultipartFile> files;
//    @ApiModelProperty("附件(文件地址)")
    private Collection<String> fileUrls;

    public String getSmtpHost() {
        return smtpHost;
    }

    public void setSmtpHost(String smtpHost) {
        this.smtpHost = smtpHost;
    }

    public Integer getSmtpPort() {
        return smtpPort;
    }

    public void setSmtpPort(Integer smtpPort) {
        this.smtpPort = smtpPort;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Collection<String> getTo() {
        return to;
    }

    public void setTo(Collection<String> to) {
        this.to = to;
    }

    public Collection<String> getCc() {
        return cc;
    }

    public void setCc(Collection<String> cc) {
        this.cc = cc;
    }

    public Collection<String> getBcc() {
        return bcc;
    }

    public void setBcc(Collection<String> bcc) {
        this.bcc = bcc;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Collection<MultipartFile> getFiles() {
        return files;
    }

    public void setFiles(Collection<MultipartFile> files) {
        this.files = files;
    }

    public Collection<String> getFileUrls() {
        return fileUrls;
    }

    public void setFileUrls(Collection<String> fileUrls) {
        this.fileUrls = fileUrls;
    }
}
