import okhttp3.*;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static java.lang.System.out;

public class Login {

    public static final String boundary = "----WebKitFormBoundaryMIlRKhiheAsBA5xM";
    public static final MediaType MULTIPART = MediaType.parse("multipart/form-data; boundary=" + boundary);
    public static String csrftoken;
    public static String __cfduid;
    public static String LEETCODE_SESSION;

    private String usrname;
    private String passwd;
    public Login(String usrname, String passwd){
        this.usrname = usrname;
        this.passwd = passwd;
    }

    public boolean doLogin() throws IOException {
        boolean success = false;
        Connection.Response response = Jsoup.connect(URL.LOGIN)
                .method(Connection.Method.GET)
                .execute();

        csrftoken = response.cookie("csrftoken");
        __cfduid = response.cookie("__cfduid");

        OkHttpClient client = new OkHttpClient.Builder()
                .followRedirects(false)
                .followSslRedirects(false)
                .cookieJar(new MyCookieJar())
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        String form_data = "--" + boundary + "\r\n"
                + "Content-Disposition: form-data; name=\"csrfmiddlewaretoken\"" + "\r\n\r\n"
                + csrftoken + "\r\n"
                + "--" + boundary + "\r\n"
                + "Content-Disposition: form-data; name=\"login\"" + "\r\n\r\n"
                + usrname + "\r\n"
                + "--" + boundary + "\r\n"
                + "Content-Disposition: form-data; name=\"password\"" + "\r\n\r\n"
                + passwd + "\r\n"
                + "--" + boundary + "\r\n"
                + "Content-Disposition: form-data; name=\"next\"" + "\r\n\r\n"
                + "/problems" + "\r\n"
                + "--" + boundary + "--";

        RequestBody requestBody = RequestBody.create(MULTIPART, form_data);

        Request request = new Request.Builder()
                .addHeader("Content-Type", "multipart/form-data; boundary=" + boundary)
                .addHeader("Connection","keep-alive")
                .addHeader("Accept","*/*")
                .addHeader("Origin","https://leetcode.com")
                .addHeader("Referer",URL.LOGIN)
                .addHeader("Cookie","__cfduid=" + __cfduid + ";" + "csrftoken=" + csrftoken)
                .post(requestBody)
                .url(URL.LOGIN)
                .build();

        Response loginResponse = client.newCall(request).execute();

        out.println(loginResponse.message());

        Headers headers = loginResponse.headers();
        List<String>cookies = headers.values("Set-Cookie");
        for (String cookie : cookies){
            int found = cookie.indexOf("LEETCODE_SESSION");
            if (found > -1){
                out.println(cookie);
                int last = cookie.indexOf(";");
                LEETCODE_SESSION = cookie.substring("LEETCODE_SESSION".length() + 1, last);
                out.println(LEETCODE_SESSION);
            }
        }

        /*
        Set<String>names = headers.names();
        for (String val : names){
            out.println(val);
            List<String>values = headers.values(val);
            for (String value : values){
                out.println(value);
            }
            out.println("------------------------------------");
        }
        */

        if (LEETCODE_SESSION != null){
            success = true;
            out.println("Login Successfully");
        }else{
            success = false;
            out.println("Login Unsuccessfully");
        }
        loginResponse.close();

        return success;
    }
}
