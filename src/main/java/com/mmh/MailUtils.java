package com.mmh;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * @author: liujiangbo
 * @description: 邮箱发送工具类
 * @date: 2019/1/24 8:54
 */
@Slf4j
public class MailUtils {

    private static final String PROPERTIES_MAILCONFIG = "mailConfig.properties";
    private static String host;
    private static Integer port;
    private static String userName;
    private static String password;
    private static String emailFrom;
    private static String timeout;
    private static JavaMailSenderImpl mailSender = createMailSender();

    private static void init() {
        Properties properties = new Properties();
        try {
            properties.load(new InputStreamReader(MailUtils.class.getClassLoader().getResourceAsStream(PROPERTIES_MAILCONFIG), "UTF-8"));
            host = properties.getProperty("mailHost");
            port = Integer.parseInt(properties.getProperty("mailPort"));
            userName = properties.getProperty("mailUsername");
            password = properties.getProperty("mailPassword");
            emailFrom = properties.getProperty("mailFrom");
            timeout = properties.getProperty("mailTimeout");
        } catch (IOException e) {
            log.warn("mail init failed ! cause : {}", e.getMessage());
        }
    }

    private static JavaMailSenderImpl createMailSender() {
        init();
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(host);
        sender.setPort(port);
        sender.setUsername(userName);
        sender.setPassword(password);
        sender.setDefaultEncoding("UTF-8");
        Properties p = new Properties();
        p.setProperty("mail.smtp.timeout", timeout);
        p.setProperty("mail.smtp.auth", "false");
        sender.setJavaMailProperties(p);
        return sender;
    }

    /**
     * 发送邮件
     *
     * @param to      接收人(多个的话，以逗号隔开)
     * @param subject 主题
     * @param html    发送内容
     * @throws MessagingException 异常
     */
    public static void sendEmail(String to, String subject, String html) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        // 设置utf-8或GBK编码，否则邮件会有乱码
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        messageHelper.setFrom(emailFrom);
        messageHelper.setTo(InternetAddress.parse(to));
        messageHelper.setSubject(subject);
        messageHelper.setText(html, true);
        mailSender.send(mimeMessage);
    }

    public static void main(String[] args) throws Exception {
        sendEmail("2423528736@qq.com", "测试邮件2", "你好，此封邮件为测试邮件，无需回复！");
    }
}
