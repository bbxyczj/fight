package fight.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Security;
import java.util.Date;
import java.util.Properties;

/**
 * created with IDEA
 *
 * @author chenzhengjian
 * @version 1.0
 * @date 2020/10/15 16:28
 */
public class MailUtil {
    private static final Logger log = LoggerFactory.getLogger(MailUtil.class);

    /**
     * 逗号
     */
    private static final String COMMA = ",";

    private static boolean init(Mail mail) {
        return mail != null && StringUtils.isNotEmpty(mail.getMailServerHost()) && StringUtils.isNotEmpty(mail.getMailServerPort())
                && StringUtils.isNotEmpty(mail.getFromAddress()) && StringUtils.isNotEmpty(mail.getFromPassword())
                && StringUtils.isNotEmpty(mail.getFromName()) && StringUtils.isNotEmpty(mail.getToAddress())
                && StringUtils.isNotEmpty(mail.getMailSubject()) && StringUtils.isNotEmpty(mail.getMailContent());
    }

    /**
     * 发送邮件（支持html和附件）
     *
     * @param mail
     * @return
     * @throws Exception
     */
    public static boolean sendMail(Mail mail) {
        try {
            log.info("发送邮件开始！");
            if (init(mail)) {
                //解决附件名过长解析错误问题(必须在new MimeMultipart()之前)
                System.setProperty("mail.mime.splitlongparameters", "false");

                // 获取系统属性
                Properties properties = System.getProperties();

                // 设置邮件服务器
                properties.setProperty("mail.smtp.host", mail.getMailServerHost());
                properties.setProperty("mail.smtp.port", mail.mailServerPort);

                properties.put("mail.smtp.auth", "true");

                //tsl 加密
                properties.put("mail.smtp.starttls.enable" , "true" );
                //阿里云环境没有开发ssl端口
//                MailSSLSocketFactory sf = new MailSSLSocketFactory();
//                sf.setTrustAllHosts(true);
//                properties.put("mail.smtp.ssl.enable", "true");
//                properties.put("mail.smtp.ssl.socketFactory", sf);

                // 获取默认session对象
                Session session = Session.getDefaultInstance(properties, new Authenticator() {
                    @Override
                    public PasswordAuthentication getPasswordAuthentication() {
                        // 发件人邮件用户名、密码
                        return new PasswordAuthentication(mail.getFromAddress(), mail.getFromPassword());
                    }
                });
                // 创建默认的 MimeMessage 对象
                MimeMessage message = new MimeMessage(session);

                // Set From: 头部头字段
                message.setFrom(new InternetAddress(mail.getFromAddress(), mail.getFromName()));


                // Set To: 接收
                if (mail.getToAddress().contains(COMMA)) {
                    message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(mail.getToAddress()));
                } else {
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(mail.getToAddress()));
                }

                // Set CC: 抄送
                if (StringUtils.isNotEmpty(mail.getCcAddress())) {
                    if (mail.getCcAddress().contains(COMMA)) {
                        message.addRecipients(Message.RecipientType.CC, InternetAddress.parse(mail.getCcAddress()));
                    } else {
                        message.addRecipient(Message.RecipientType.CC, new InternetAddress(mail.getCcAddress()));
                    }
                }

                // Set Subject: 主题文字
                message.setSubject(mail.getMailSubject());

                // 设置发件日期
                message.setSentDate(new Date());

//                // 创建消息部分
//                BodyPart messageBodyPart = new MimeBodyPart();

//                // 文本消息
//                messageBodyPart.setText(mailContent);

                // 创建多重消息
                Multipart multipart = new MimeMultipart();

//                // 设置文本消息
//                multipart.addBodyPart(messageBodyPart);

                // 设置HTML内容
                MimeBodyPart content = createContent(mail.getMailContent());
                multipart.addBodyPart(content);

                // 附件部分
                if (StringUtils.isNotEmpty(mail.getFileName())) {
                    MimeBodyPart messageBodyPart = new MimeBodyPart();
                    // 设置要发送附件的文件路径
                    DataSource source = new FileDataSource(mail.getFileName());
                    messageBodyPart.setDataHandler(new DataHandler(source));
                    // 处理附件名称中文（附带文件路径）乱码问题
                    messageBodyPart.setFileName(MimeUtility.encodeText(mail.getFileName()
                            .substring(mail.getFileName().lastIndexOf("/") + 1)));
                    multipart.addBodyPart(messageBodyPart);
                }
                // 发送完整消息
                message.setContent(multipart);
                // 保存并生成最终的邮件内容
                message.saveChanges();
                // 发送消息
                Transport.send(message);
                log.info("发送邮件结束！");
                return true;
            }else{
                log.info("init()初始化校验邮件参数失败！");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return false;
    }


    /**
     * 创建HTML格式的邮件内容
     *
     * @param body 邮件内容
     * @return 邮件内容实体
     * @throws Exception
     */
    public static MimeBodyPart createContent(String body) throws Exception {

        /* 创建代表组合MIME消息的MimeMultipart对象和该对象保存到的MimeBodyPart对象 */
        MimeBodyPart content = new MimeBodyPart();

        // 创建一个MImeMultipart对象
        MimeMultipart multipart = new MimeMultipart();

        // 创建一个表示HTML正文的MimeBodyPart对象，并将它加入到前面的创建的MimeMultipart对象中
        MimeBodyPart htmlBodyPart = new MimeBodyPart();
        htmlBodyPart.setContent(body, "text/html;charset=UTF-8");
        multipart.addBodyPart(htmlBodyPart);

        // 将MimeMultipart对象保存到MimeBodyPart对象中
        content.setContent(multipart);

        return content;
    }

    /**
     * 发送邮件
     *
     * @param mail
     * @return
     * @throws IOException
     */
    public static boolean sslSend(Mail mail) {
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        final String sslFactory = "javax.net.ssl.SSLSocketFactory";
        // Get a Properties object
        Properties props = new Properties();
        props.setProperty("mail.smtp.host", mail.getMailServerHost());
        props.setProperty("mail.smtp.socketFactory.class", sslFactory);
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.auth", "true");

        final String username = mail.getFromAddress();
        final String password = mail.getFromPassword();
        Session session = Session.getDefaultInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        MimeMessage msg = new MimeMessage(session);
        MimeMultipart mp = new MimeMultipart();
        // 自定义发件人昵称
        String nick = "";
        try {
            nick = MimeUtility.encodeText(mail.getFromName());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            // 设置发件人和收件人
            msg.setFrom(new InternetAddress(mail.getFromAddress(), nick));
            String[] receiver = mail.getToAddress().split(",");
            Address[] to = new InternetAddress[receiver.length];
            for (int i = 0; i < receiver.length; i++) {
                to[i] = new InternetAddress(receiver[i]);
            }
            // 多个收件人地址
            msg.setRecipients(Message.RecipientType.TO, to);
//        msg.setText(mail.getText());
            // 标题
            msg.setSubject(mail.getMailSubject());
            msg.setSentDate(new Date());
            MimeBodyPart bp = new MimeBodyPart();
            bp.setContent(mail.getMailContent(), "text/html;charset=utf-8");
            mp.addBodyPart(bp);
            if (StrUtil.isNotBlank(mail.getFileName())) {
                bp = new MimeBodyPart();
                bp.attachFile(mail.getFileName());
                mp.addBodyPart(bp);
            }
            msg.setContent(mp);
            msg.saveChanges();
            Transport.send(msg);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        log.info("EmailUtil ssl协议邮件发送{}", JSONUtil.toJsonStr(msg));
        return true;
    }

    public static class Mail {
        private String mailServerHost;        // 邮件服务器
        private String mailServerPort;        // 端口
        private String fromAddress;        // 发件人邮箱
        private String fromPassword;        // 发件人邮箱密码
        private String fromName;        // 发件人昵称
        private String toAddress;        // 收件人地址
        private String ccAddress;        // 抄送人地址

        private String mailSubject;//标题
        private String mailContent;//内容
        private String fileName;//附件名（全路径）

        public String getMailServerHost() {
            return mailServerHost;
        }

        public void setMailServerHost(String mailServerHost) {
            this.mailServerHost = mailServerHost;
        }

        public String getMailServerPort() {
            return mailServerPort;
        }

        public void setMailServerPort(String mailServerPort) {
            this.mailServerPort = mailServerPort;
        }

        public String getFromAddress() {
            return fromAddress;
        }

        public void setFromAddress(String fromAddress) {
            this.fromAddress = fromAddress;
        }

        public String getFromPassword() {
            return fromPassword;
        }

        public void setFromPassword(String fromPassword) {
            this.fromPassword = fromPassword;
        }

        public String getFromName() {
            return fromName;
        }

        public void setFromName(String fromName) {
            this.fromName = fromName;
        }

        public String getToAddress() {
            return toAddress;
        }

        public void setToAddress(String toAddress) {
            this.toAddress = toAddress;
        }

        public String getCcAddress() {
            return ccAddress;
        }

        public void setCcAddress(String ccAddress) {
            this.ccAddress = ccAddress;
        }

        public String getMailSubject() {
            return mailSubject;
        }

        public void setMailSubject(String mailSubject) {
            this.mailSubject = mailSubject;
        }

        public String getMailContent() {
            return mailContent;
        }

        public void setMailContent(String mailContent) {
            this.mailContent = mailContent;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }
    }


    public static void main(String[] args) {
        Mail mail = new Mail();
        mail.setMailServerHost("mail.opg.cn");
        mail.setMailServerPort("25");
        mail.setFromName("运维中心");
        mail.setFromAddress("noc@opg.cn");
        mail.setFromPassword("Bestv_123");
        mail.setToAddress("1064376730@qq.com");
        mail.setMailSubject("测试告警邮件发送");
        mail.setMailContent("<span style='color: red;'>\n" + "邮件告警" +
                "</span></br>");
        sendMail(mail);
    }
}
