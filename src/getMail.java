/**
 * Created with IntelliJ IDEA.
 * User: KesonLau
 * Date: 14-1-12
 * Time: 下午11:26
 * To change this template use File | Settings | File Templates.
 */
import java.io.*;

import java.security.Security;
import java.text.*;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class getMail{

private MimeMessage mimeMessage = null;

private String saveAttachPath = ""; // 附件下载后的存放目录

private StringBuffer bodytext = new StringBuffer();// 存放邮件内容的StringBuffer对象

private String dateformat = "yy-MM-dd HH:mm"; // 默认的日前显示格式

/** *//**
 * * 構造函数,初始化一个MimeMessage對象
 *
 */

public void gmailRead(MimeMessage mimeMessage) {

        this.mimeMessage = mimeMessage;

System.out.println("create　a　PraseMimeMessage　object  ..");

}

/** *//**
 * * 獲取一个MimeMessage對象
 *
 */

public void setMimeMessage(MimeMessage mimeMessage){

        this.mimeMessage = mimeMessage;

}

/** *//**
 * * 獲得發件人的地址和姓名
 *
 */

public String getFrom() throws Exception {

        InternetAddress address[] = (InternetAddress[]) mimeMessage.getFrom();

String from = address[0].getAddress();

if (from == null)

        from = "";

String personal = address[0].getPersonal();

if (personal == null)

        personal = "";

String fromaddr = personal + "<" + from + ">";

return fromaddr;

}

/** *//**
 *
 * 獲得郵件的收件人，抄送，和密送的地址和姓名，根據所传递的参数的不同如
 *
 * to 獲得邮件的收件人 cc 獲得邮件的抄送人地址 bcc 獲得邮件的密送人地址
 *
 */

public String getMailAddress(String type) throws Exception  {

        String mailaddr = "";

String addtype = type.toUpperCase();

InternetAddress[] address = null;

if (addtype.equals("TO") || addtype.equals("CC")

        || addtype.equals("BCC"))  {

        if (addtype.equals("TO"))  {

        address = (InternetAddress[]) mimeMessage

        .getRecipients(Message.RecipientType.TO);

} else if (addtype.equals("CC"))  {

        address = (InternetAddress[]) mimeMessage

        .getRecipients(Message.RecipientType.CC);

} else  {

        address = (InternetAddress[]) mimeMessage

        .getRecipients(Message.RecipientType.BCC);

}

        if (address != null)  {

        for (int i = 0; i < address.length; i++)  {

        String email = address[i].getAddress();

if (email == null)

        email = "";

else  {

        email = MimeUtility.decodeText(email);

}

        String personal = address[i].getPersonal();

if (personal == null)

        personal = "";

else  {

        personal = MimeUtility.decodeText(personal);

}

        String compositeto = personal + "<" + email + ">";

mailaddr += "," + compositeto;

}

        mailaddr = mailaddr.substring(1);

}

        } else  {

        throw new Exception("Error emailaddr type!");

}

        return mailaddr;

}

/** *//**
 * * 獲得郵件主题
 *
 */

public String getSubject() throws MessagingException  {

        String subject = "";

try  {

        subject = MimeUtility.decodeText(mimeMessage.getSubject());

if (subject == null)

        subject = "";

} catch (Exception exce)  {

        }

        return subject;

}

/** *//**
 * * 獲得郵件的發送日期
 *
 */

public String getSentDate() throws Exception  {

        Date sentdate = mimeMessage.getSentDate();

SimpleDateFormat format = new SimpleDateFormat(dateformat);

return format.format(sentdate);

}

/** *//**
 * * 獲得郵件的正文内容
 *
 */

public String getBodyText()  {

        return bodytext.toString();

}

/** *//**
 *
 * 解析郵件，把得到的郵件内容保存到一个StringBuffer对象中，解析邮件 *
 *
 * 主要是根据MimeType类型的不同执行不同的操作，一步一步的解析
 *
 */

public void getMailContent(Part part) throws Exception  {

        String contenttype = part.getContentType();

int nameindex = contenttype.indexOf("name");

boolean conname = false;

if (nameindex != -1)

        conname = true;

System.out.println("CONTENTTYPE:　" + contenttype);

if (part.isMimeType("text/plain") && !conname)  {

        bodytext.append((String) part.getContent());

} else if (part.isMimeType("text/html") && !conname)  {

        bodytext.append((String) part.getContent());

} else if (part.isMimeType("multipart/*"))  {

        Multipart multipart = (Multipart) part.getContent();

int counts = multipart.getCount();

for (int i = 0; i < counts; i++)  {

        getMailContent(multipart.getBodyPart(i));

}

        } else if (part.isMimeType("message/rfc822"))  {

        getMailContent((Part) part.getContent());

} else  {

        }

        }

/** *//**
 * * 判断此邮件是否需要回执，如果需要回执返回"true",否则返回"false"
 *
 */

public boolean getReplySign() throws MessagingException  {

        boolean replysign = false;

String needreply[] = mimeMessage

        .getHeader("Disposition-Notification-To");

if (needreply != null)  {

        replysign = true;

}

        return replysign;

}

/** *//**
 *
 * *獲得此郵件的Message-ID
 *
 */

public String getMessageId() throws MessagingException  {

        return mimeMessage.getMessageID();

}

/** *//**
 *
 * 判断此郵件是否已读，如果未读返回false,反之返回true
 *
 */

public boolean isNew() throws MessagingException  {

        boolean isnew = false;

Flags flags = ((Message) mimeMessage).getFlags();

// add by wangdong

if (true == flags.contains(Flags.Flag.SEEN))  {
        System.out.println("邮件已读");
}
        else
         {
        System.out.println("邮件未读");
}

        Flags.Flag[] flag = flags.getSystemFlags();

System.out.println("flags's　length:　" + flag.length);

for (int i = 0; i < flag.length; i++)  {

        if (flag[i] == Flags.Flag.SEEN)  {

        isnew = true;

System.out.println("seen　Message  .");

break;

}

        }

        return isnew;

}

/** *//**
 * * 判断此郵件是否包含附件
 *
 */

public boolean isContainAttach(Part part) throws Exception  {

        boolean attachflag = false;

// String contentType = part.getContentType();

if (part.isMimeType("multipart/*"))  {

        Multipart mp = (Multipart) part.getContent();

for (int i = 0; i < mp.getCount(); i++)  {

        BodyPart mpart = mp.getBodyPart(i);

String disposition = mpart.getDisposition();

if ((disposition != null)

        && ((disposition.equals(Part.ATTACHMENT)) || (disposition

        .equals(Part.INLINE))))

        attachflag = true;

else if (mpart.isMimeType("multipart/*"))  {

        attachflag = isContainAttach((Part) mpart);

} else  {

        String contype = mpart.getContentType();

if (contype.toLowerCase().indexOf("application") != -1)

        attachflag = true;

if (contype.toLowerCase().indexOf("name") != -1)

        attachflag = true;

}

        }

        } else if (part.isMimeType("message/rfc822"))  {

        attachflag = isContainAttach((Part) part.getContent());

}

        return attachflag;

}

/** *//**
 *
 * 保存附件
 *
 */

public void saveAttachMent(Part part) throws Exception  {

        String fileName = "";

if (part.isMimeType("multipart/*"))  {

        Multipart mp = (Multipart) part.getContent();

for (int i = 0; i < mp.getCount(); i++)  {

        BodyPart mpart = mp.getBodyPart(i);

String disposition = mpart.getDisposition();

if ((disposition != null)

        && ((disposition.equals(Part.ATTACHMENT)) || (disposition

        .equals(Part.INLINE))))  {

        fileName = mpart.getFileName();

if (fileName.toLowerCase().indexOf("gb2312") != -1)  {

        fileName = MimeUtility.decodeText(fileName);

}

        saveFile(fileName, mpart.getInputStream());

} else if (mpart.isMimeType("multipart/*"))  {

        saveAttachMent(mpart);

} else  {

        fileName = mpart.getFileName();

if ((fileName != null)

        && (fileName.toLowerCase().indexOf("GB2312") != -1))  {

        fileName = MimeUtility.decodeText(fileName);

saveFile(fileName, mpart.getInputStream());

}

        }

        }

        } else if (part.isMimeType("message/rfc822"))  {

        saveAttachMent((Part) part.getContent());

}

        }

/** *//**
 *
 * 设置附件存放路径
 *
 */

public void setAttachPath(String attachpath)  {

        this.saveAttachPath = attachpath;

}

/** *//**
 *
 * 设置日期显示格式
 *
 */

public void setDateFormat(String format) throws Exception  {

        this.dateformat = format;

}

/** *//**
 *
 * 获得附件存放路径
 *
 */

public String getAttachPath()  {

        return saveAttachPath;

}

/** *//**
 * * 保存附件到指定目录里
 *
 */

private void saveFile(String fileName, InputStream in) throws Exception  {

        String osName = System.getProperty("os.name");

String storedir = getAttachPath();

String separator = "";

if (osName == null)

        osName = "";

if (osName.toLowerCase().indexOf("win") != -1)  {

        separator = "/";

if (storedir == null || storedir.equals(""))

        storedir = "c:/tmp";

} else  {

        separator = "/";

storedir = "/tmp";

}

        File storefile = new File(storedir + separator + fileName);

System.out.println("storefile's　path:　" + storefile.toString());

// for(int i=0;storefile.exists();i++){

// storefile = new File(storedir+separator+fileName+i);

// }

BufferedOutputStream bos = null;

BufferedInputStream bis = null;

try  {

        bos = new BufferedOutputStream(new FileOutputStream(storefile));

bis = new BufferedInputStream(in);

int c;

while ((c = bis.read()) != -1)  {

        bos.write(c);

bos.flush();

}

        } catch (Exception exception)  {

        exception.printStackTrace();

throw new Exception("文件保存失败!");

} finally  {

        bos.close();

bis.close();

}

        }

/** *//**
 * * 类测试
 *
 */
}
