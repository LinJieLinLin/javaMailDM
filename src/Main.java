import org.json.JSONArray;
import org.json.JSONException;


public class Main {

    public static void main(String[] args) throws JSONException {
        try{
           String u = "l013977449@163.com";
           String p = "lj8716150";
            u =  "linjie29@yeah.net";
            p = "lj8716150";

//            mail a = new mail("linjie29@yeah.net","lj8716150");
//            mail a = new mail("1013977449@qq.com","lj8716150.");
//            mail a = new mail(u,p,0);

//         a.getJson();
//        String b= a.fck();
        System.out.println("SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS");
            mail b = new mail(u,p,12 );

        }catch (Exception e){
            System.out.println(e);
            System.out.println("Hello!");
        }
        String f= "fuck";
        System.out.println("Hello World!"+f.length());

    }
}
