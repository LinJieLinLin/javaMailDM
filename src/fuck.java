import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;
import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.FileWriter;
import java.util.Properties;

public class fuck {
    public static String aim="";
    public static String insert_aim="";
    public fuck() throws Exception {
        Thread.sleep(5000);
        try{
            String user = "993353454@qq.com";// 邮箱的用户名
            String password = "ljzmq."; // 邮箱的密码

            Properties prop = System.getProperties();
            prop.put("mail.store.protocol", "imap");
            prop.put("mail.imap.host", "imap.qq.com");

            Session session = Session.getInstance(prop);

            int total=0;
            IMAPStore store = (IMAPStore) session.getStore("imap"); // 使用imap会话机制，连接服务器
            store.connect(user, password);

            IMAPFolder folder = (IMAPFolder) store.getFolder("INBOX"); // 收件箱
            folder.open(Folder.READ_WRITE);
            // 获取总邮件数
            total = folder.getMessageCount();
            System.out.println("-----------------共有邮件：" + total
                    + " 封--------------");
            // 得到收件箱文件夹信息，获取邮件列表
            System.out.println("未读邮件数：" + folder.getUnreadMessageCount());
            Message[] messages = folder.getMessages();
            int messageNumber = 0;
            for (Message message : messages) {

                Flags flags = message.getFlags();
                if(flags.contains(Flags.Flag.SEEN));

                else {
                    System.out.println("未读邮件");
                    System.out.println("发送时间：" + message.getSentDate());
                    System.out.println("主题：" + message.getSubject());
                    parseMailContent(message.getContent());
//                System.out.println("内容：" + message.getContent() instanceof String);
                    System.out
                            .println("========================================================");
                    System.out
                            .println("========================================================");
                }

                //每封邮件都有一个MessageNumber，可以通过邮件的MessageNumber在收件箱里面取得该邮件
                messageNumber = message.getMessageNumber();
            }
            Message message = folder.getMessage(messageNumber);
            System.out.println(message.getContent()+message.getContentType());
            // 释放资源
            if(folder!= null)
                folder.close(true);
            if(store!= null)
                store.close();
        }catch(Exception e){
        }
    }


    public static void parseMailContent(Object content) {
        try {
            if (content instanceof Multipart) {
                Multipart mPart = (MimeMultipart) content;
                for (int i = 0; i < mPart.getCount(); i++) {
                    extractPart((MimeBodyPart) mPart.getBodyPart(i));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     *
     * @param part 抽取内容
     */
    public static void extractPart(MimeBodyPart part) {
        try {
            String disposition = part.getDisposition();

            if(disposition != null
                    && (disposition.equalsIgnoreCase(Part.ATTACHMENT) || disposition.equalsIgnoreCase(Part.INLINE)));
            else {// 正文
                if(part.getContent() instanceof String){//接收到的纯文本
                    String s = part.getContent().toString();
                    String[]partsCollArr = s.split("\r\n\r\n");
                    for(int i=0;i<partsCollArr.length;i++)
                    {
                        int L= partsCollArr[i].length();
                        System.out.println(partsCollArr[i]);
                        String ss=partsCollArr[i].substring(L-1);
                        if(ss.equals("=")) {
                            aim= partsCollArr[i];
                        }
                    }
                }
            }
            if(!aim.equals("")){
                String path = "../OA_Service/src/key.xml";
                File filename = new File(path);
                filename.createNewFile();
                FileWriter fw = new FileWriter("../OA_Service/src/key.xml");
                insert_aim="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                        "<key>"+aim+"</key>\t";
                fw.write(insert_aim,0,insert_aim.length());
                fw.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}