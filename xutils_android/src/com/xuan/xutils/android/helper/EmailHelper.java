/* 
 * @(#)EmailHelper.java    Created on 2013-3-14
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.xutils.android.helper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import android.content.Context;

/**
 * 发送Email工具类
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-3-14 下午8:17:35 $
 */
public class EmailHelper {
    static SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static String mailName;// 发送者邮件账户名
    /**
     * 发送者邮件账户密码
     * */
    private static String mailPassword;
    /**
     * 接收者邮件账户名
     * */
    private static String SEND_TO_MAILLIST[];

    private static String mailHost;
    private static String mailProt;
    private static String mailSProt;

    public static void setMailSetup(String user, String pw, String... sendTo) {
        mailName = user;
        mailPassword = pw;
        SEND_TO_MAILLIST = sendTo;
    }

    public static void setMailSettings(String _mailHost, String port, String sport) {
        mailHost = _mailHost;
        mailProt = port;
        mailSProt = sport;
    }

    public synchronized static void sendMail(String title, String body) {
        Mail mail = new Mail(mailName, mailPassword, SEND_TO_MAILLIST);

        StringBuffer $title = new StringBuffer();
        if (title == null) {
            title = "";
        }
        $title.append(title).append("_").append("[ ");
        $title.append(mDateFormat.format(new Date(System.currentTimeMillis())));

        mail.setTitle($title.toString());
        mail.setBody(body);
        try {
            mail.send();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized static void sendMail(String title, Throwable t) {
        Mail mail = new Mail(mailName, mailPassword, SEND_TO_MAILLIST);

        StringBuffer $title = new StringBuffer();
        if (title == null) {
            title = "";
        }
        $title.append(title).append("_").append("[ ");
        $title.append(mDateFormat.format(new Date(System.currentTimeMillis())));

        mail.setTitle($title.toString());
        StringBuffer body = new StringBuffer();
        body.append(StringHelper.exceptionToString(t));
        mail.setBody(body.toString());
        try {
            mail.send();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized static void sendMail(Context context, String logUser, String title, Throwable t) {
        Mail mail = new Mail(mailName, mailPassword, SEND_TO_MAILLIST);

        StringBuffer $title = new StringBuffer();
        if (title == null) {
            title = "";
        }
        $title.append(title).append("_").append("[ ");
        $title.append(mDateFormat.format(new Date(System.currentTimeMillis())));
        $title.append(" ]_");

        mail.setTitle($title.toString());

        StringBuffer body = new StringBuffer();
        body.append("LOG_USER_NAME: ").append(logUser).append("\n");
        body.append(StringHelper.exceptionToString(t));
        mail.setBody(body.toString());
        try {
            mail.send();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized static void sendMail(Context context, String logUser, String title, String logs, Throwable t) {
        Mail mail = new Mail(mailName, mailPassword, SEND_TO_MAILLIST);

        StringBuffer $title = new StringBuffer();
        if (title == null) {
            title = "";
        }
        $title.append(title).append("_").append("[ ");
        $title.append(mDateFormat.format(new Date(System.currentTimeMillis())));
        $title.append(" ]_");
        $title.append(logUser);

        mail.setTitle($title.toString());

        StringBuffer body = new StringBuffer();
        body.append("LOG_USER_NAME: ").append(logUser).append("\n");
        body.append(logs).append('\n');
        body.append(StringHelper.exceptionToString(t));
        mail.setBody(body.toString());
        try {
            mail.send();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized static void sendMail(Context context, String logUser, String filename) {
        Mail mail = new Mail(mailName, mailPassword, SEND_TO_MAILLIST);

        StringBuffer $title = new StringBuffer();
        $title.append(logUser).append("_").append("[ ");
        $title.append(mDateFormat.format(new Date(System.currentTimeMillis())));
        $title.append(" ]_");
        $title.append(logUser);

        mail.setTitle($title.toString());

        StringBuffer body = new StringBuffer();
        body.append("LOG_USER_NAME: ").append(logUser).append("\n");
        mail.setBody(body.toString());
        try {
            mail.addAttachment(filename);
        }
        catch (Exception e1) {
            e1.printStackTrace();
        }
        try {
            mail.send();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class Mail extends javax.mail.Authenticator {
        private String _user;
        private String _pass;

        private String[] _to;
        private String _from;

        private final String _port;
        private final String _sport;

        private final String _host;

        private String _subject;
        private String _body;

        private final boolean _auth;

        private final boolean _debuggable;

        private final Multipart _multipart;

        public Mail() {

            _host = mailHost; // default smtp server
            _port = mailProt; // default smtp port
            _sport = mailSProt; // default socketfactory port

            _user = ""; // username
            _pass = ""; // password
            _from = ""; // email sent from
            _subject = ""; // email subject
            _body = ""; // email body

            _debuggable = false; // debug mode on or off - default off
            _auth = true; // smtp authentication - default on

            _multipart = new MimeMultipart();

            if (mailName == null || mailPassword == null || SEND_TO_MAILLIST == null || mailHost == null
                    || mailProt == null) {
                throw new NullPointerException("EmailHelper 配置未初始化完成");
            }

            // There is something wrong with MailCap, javamail can not find a
            // handler for the multipart/mixed part, so this bit needs to be
            // added.
            MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
            mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
            mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
            mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
            mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
            mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
            CommandMap.setDefaultCommandMap(mc);
        }

        public Mail(String user, String pass, String to[]) {
            this();
            _subject = "buglists";
            _user = user;
            _from = _user;
            _pass = pass;
            _to = to;
        }

        public void setTitle(String title) {
            _subject = title;
        }

        public boolean send() throws Exception {
            Properties props = _setProperties();

            if (!_user.equals("") && !_pass.equals("") && _to.length > 0 && !_from.equals("") && !_subject.equals("")
                    && !_body.equals("")) {
                Session session = Session.getInstance(props, this);

                MimeMessage msg = new MimeMessage(session);

                msg.setFrom(new InternetAddress(_from));

                InternetAddress[] addressTo = new InternetAddress[_to.length];
                for (int i = 0; i < _to.length; i++) {
                    addressTo[i] = new InternetAddress(_to[i]);
                }
                msg.setRecipients(MimeMessage.RecipientType.TO, addressTo);

                msg.setSubject(_subject);
                msg.setSentDate(new Date());

                // setup message body
                BodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setText(_body);
                _multipart.addBodyPart(messageBodyPart);

                // Put parts in message
                msg.setContent(_multipart);

                // send email
                Transport.send(msg);

                return true;
            }
            else {
                return false;
            }
        }

        public void addAttachment(String filename) throws Exception {
            BodyPart messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filename);

            _multipart.addBodyPart(messageBodyPart);
        }

        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(_user, _pass);
        }

        private Properties _setProperties() {
            Properties props = new Properties();

            props.put("mail.smtp.host", _host);

            if (_debuggable) {
                props.put("mail.debug", "true");
            }

            if (_auth) {
                props.put("mail.smtp.auth", "true");
            }

            props.put("mail.smtp.port", _port);
            props.put("mail.smtp.socketFactory.port", _sport);
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.socketFactory.fallback", "false");

            return props;
        }

        public String getBody() {
            return _body;
        }

        public void setBody(String _body) {
            this._body = _body;
        }
    }

}
