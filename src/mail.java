//package com.shopping.test;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
//import java.util.Date;
import android.text.Html;
import android.util.Log;
import org.json.*;
//import org.json.JSONException;

import java.text.*;
import java.util.Iterator;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;

//Title: 使用JavaMail接收邮件
//Description: 实例JavaMail包接收邮件，本实例没有实现接收邮件的附件。
//Copyright: Copyright (c) 2003
//Filename: POPMail.java
//@version 1.0
public class mail {

    // 方法说明：主方法，接收用户输入的邮箱服务器、用户名和密码输入参数： 返回类型：
    public String user = "";// 邮箱的用户名
    String password = ""; // 邮箱的密码
    IMAPStore store;
    IMAPFolder folder;
    private static StringBuffer bodytext = new StringBuffer();// 存放邮件内容的StringBuffer对象

    public mail(String argUser, String argPwd,Integer arg_aaa) throws Exception {


        try {
            user = argUser;
            password = argPwd;
            String fu = "SSS";
            Properties prop = System.getProperties();
            prop.put("mail.store.protocol", "imap");
            String serve = user.split("@")[1];
            serve = "imap." + serve;
            prop.put("mail.imap.host", serve);
            prop.setProperty("mail.imap.auth.PLAIN.disable", "true");
            prop.setProperty("mail.imap.auth.login.disable","true");
            Session session = Session.getInstance(prop);
//            session.setDebug(true);
            int total = 0;
            store = (IMAPStore) session.getStore("imap"); // 使用imap会话机制，连接服务器
            store.connect(user, password); //登陆
            folder = (IMAPFolder) store.getFolder("INBOX"); // 收件箱
            folder.open(Folder.READ_WRITE);
            // 获取总邮件数
            total = folder.getMessageCount();
            System.out.println("共有邮件：" + total + " 封--------------");
            // 得到收件箱文件夹信息，获取邮件列表
            System.out.println("未读邮件数：" + folder.getUnreadMessageCount());
            Message[] messages = folder.getMessages();
            int messageNumber = arg_aaa;
            for (Message message : messages) {
                Flags flags = message.getFlags();
                if (!flags.contains(Flags.Flag.SEEN)) {
                    System.out.println("未读邮件");
                    SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String date = d.format(message.getSentDate());
                    System.out.println("发送时间：" + date.split(" ")[0]);
                    System.out.println("主题：" + message.getSubject());
//                System.out.println(message.getFrom()[0]+"!@!"+message.getFrom()[1]);
                    InternetAddress address[] = (InternetAddress[]) message.getFrom();

                    String from = address[0].getAddress();
                    String personal = address[0].getPersonal();

                    System.out.println(from + "!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+personal);
//                parseMailContent(message.getContent());
//                System.out.println("内容：" + message.getContent() );
//                    messageNumber = message.getMessageNumber();//每封邮件都有一个MessageNumber，可以通过邮件的MessageNumber在收件箱里面取得该邮件
                    System.out.println("========================================================"+messageNumber);
                }
            }

            System.out.println(messageNumber + "^^^^^^^^^^^^^^^^^^^^^^^^");
            if (messageNumber > 0) {
                Message message = folder.getMessage(messageNumber);
//            getMail b = new getMail(message);
                if (message.isMimeType("text/*")) {
                    System.out.println("<");
                    System.out.println("<");
                    System.out.println(message.getContent());
                }
                System.out.println("<" + messageNumber);
                parseMailContent(message.getContent());
                System.out.println(message.getContent() + ":^^^^^^^^^^^^^^^^^^^^^^^^" + message.getContentType());
                System.out.println(">");
            }

            // 释放资源
            if (folder != null)
                folder.close(true);
            if (store != null)
                store.close();
        } catch (Exception e) {
            System.out.println(e.toString()+"CCCCCCCCCCCCCC");
            e.printStackTrace();
        }
        System.out.println("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB");

    }

    public static void parseMailContent(Object content) {
        try {
            if(content instanceof Html){
                System.out.println(content.toString());
                System.out.println("1111111111111111111");
            }
            System.out.println("1111111111111111111");
            if (content instanceof Multipart) {
                Multipart mPart = (MimeMultipart) content;
                System.out.println(mPart.getCount()+"FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF");
                for (int i = 0; i < mPart.getCount(); i++) {
//                    extractPart((MimeBodyPart) mPart.getBodyPart(i));
                    System.out.println("第N个啊啊啊啊啊啊啊"+i);
                    getMailContent(mPart.getBodyPart(i));
                }
            }
        } catch (Exception e) {
//            log.severe("读取邮件内容异常");
            e.printStackTrace();
        }
    }
    public static void extractPart(MimeBodyPart part) {
        try {
            String disposition = part.getDisposition();

            if (disposition != null && (disposition.equalsIgnoreCase(Part.ATTACHMENT)
                    || disposition.equalsIgnoreCase(Part.INLINE))) ;
            else {// 正文
                if (part.getContent() instanceof String) {//接收到的纯文本
                    String s = part.getContent().toString();
                    System.out.println(s + "FUCK");
                }
            }
        } catch (Exception e) {

        }
    }

    public static void getMailContent(Part part) throws Exception  {
        System.out.println("STAR------------------------------------------------------------------");

        String contenttype = part.getContentType();

        int nameindex = contenttype.indexOf("name");
        System.out.println(contenttype+"i个PART"+nameindex);
        boolean conname = false;

        if (nameindex != -1)

            conname = true;

        System.out.println("CONTENTTYPE:　" + contenttype);

        if (part.isMimeType("text/plain") || part.isMimeType("text/html")&& !conname)  {
            System.out.println("text----------------");
            bodytext.append((String) part.getContent());
            System.out.println(bodytext);

        } else if (part.isMimeType("multipart/*"))  {
            System.out.println("----------------------------------------multipart");

            Multipart multipart = (Multipart) part.getContent();

            int counts = multipart.getCount();

            for (int i = 0; i < counts; i++)  {

                System.out.println("！"+i);
                getMailContent(multipart.getBodyPart(i));

            }

        } else if (part.isMimeType("message/rfc822"))  {

            getMailContent((Part) part.getContent());

        } else  {
            System.out.println("没有了！！！！！！！！！！！！");
        }

    }

    public static String getBody(Part part, String userName) {
        StringBuffer sb = new StringBuffer();
        sb.append(new String(""));
        try {
            /**
             * 纯文本或者html格式的,可以直接解析掉
             */
            if (part.isMimeType("text/plain") || part.isMimeType("text/html")) {
                sb.append(part.getContent());
            } else if (part.isMimeType("multipart/*")) {
                /**
                 * 可供选择的,一般情况下第一个是plain,第二个是html格式的
                 */
                if (part.isMimeType("multipart/alternative")) {
                    Multipart mp = (Multipart) part.getContent();
                    int index = 0;// 兼容不正确的格式,返回第一个部分
                    if (mp.getCount() > 1)
                        index = 1;// 第2个部分为html格式的哦~
                    /**
                     * 已经根据情况进行了判断,就算不符合标准格式也不怕了.
                     */
                    Part tmp = mp.getBodyPart(index);
                    sb.append(tmp.getContent());
                } else if (part.isMimeType("multipart/related")) {
                    /**
                     * related格式的,那么第一个部分包含了body,里面含有内嵌的内容的链接.
                     */
                    Multipart mp = (Multipart) part.getContent();
                    Part tmp = mp.getBodyPart(0);
//                    String body = LmlMessage.getBody(tmp, userName);
                    int count = mp.getCount();
                    /**
                     * 要把那些可能的内嵌对象都先读出来放在服务器上,然后在替换相对地址为绝对地址
                     */
                    for (int k = 1; count > 1 && k < count; k++) {
                        Part att = mp.getBodyPart(k);
                        String attname = att.getFileName();
                        attname = MimeUtility.decodeText(attname);
                        try {
//                            File attFile = new File(
//                                    Constants.tomcat_AttHome_Key, userName
//                                    .concat(attname));
//                            FileOutputStream fileoutput = new FileOutputStream(
//                                    attFile);
//                            InputStream is = att.getInputStream();
//                            BufferedOutputStream outs = new BufferedOutputStream(
//                                    fileoutput);
//                            byte b[] = new byte[att.getSize()];
//                            is.read(b);
//                            outs.write(b);
//                            outs.close();
                        } catch (Exception e) {
//                            logger
//                                    .error("Error occurred when to get the photos from server");
                        }
                        String Content_ID[] = att.getHeader("Content-ID");
                        if (Content_ID != null && Content_ID.length > 0) {
                            String cid_name = Content_ID[0].replaceAll("<", "")
                                    .replaceAll(">", "");
//                            body = body.replaceAll("cid:" + cid_name,
//                                    Constants.server_attHome_Key.concat("/")
//                                            .concat(userName.concat(attname)));
                        }
                    }
                    return sb.toString();
                } else {
                    /**
                     * 其他multipart/*格式的如mixed格式,那么第一个部分包含了body,用递归解析第一个部分就可以了
                     */
                    Multipart mp = (Multipart) part.getContent();
                    Part tmp = mp.getBodyPart(0);
//                    return LmlMessage.getBody(tmp, userName);
                }
            } else if (part.isMimeType("message/rfc822")) {
//                return LmlMessage
//                        .getBody((Message) part.getContent(), userName);
            } else {
                /**
                 * 否则的话,死马当成活马医,直接解析第一部分,呜呜~
                 */
                Object obj = part.getContent();
                if (obj instanceof String) {
                    sb.append(obj);
                } else {
                    Multipart mp = (Multipart) obj;
                    Part tmp = mp.getBodyPart(0);
//                    return LmlMessage.getBody(tmp, userName);
                }
            }
        } catch (Exception e) {
            return "解析正文错误!";
        }
        return sb.toString();
    }



}
