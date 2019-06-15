import okhttp3.*;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.List;

import static java.lang.System.out;

public class Login {

    public static final String boundary = "----WebKitFormBoundaryMIlRKhiheAsBA5xM";
    public static final MediaType MULTIPART = MediaType.parse("multipart/form-data; boundary=" + boundary);
    public static String csrftoken;
    public static String __cfduid;
    public static String LEETCODE_SESSION;

    private OkHttpHelper okHttpHelerInstance;

    private String usrname;
    private String passwd;

    public Login(String usrname, String passwd) {
        this.usrname = usrname;
        this.passwd = passwd;
        okHttpHelerInstance = OkHttpHelper.getSingleton();
    }


    /**
     * 模拟登陆 LeetCodo，登陆过程分析见：https://www.cnblogs.com/ZhaoxiCheung/p/9302510.html
     */
    public boolean doLogin() throws IOException {
        boolean success;
        Connection.Response response = Jsoup.connect(URL.LOGIN)
                .method(Connection.Method.GET)
                .execute();
        csrftoken = response.cookie("csrftoken");
        __cfduid = response.cookie("__cfduid");

        OkHttpClient client = okHttpHelerInstance.getOkHttpClient().newBuilder()
                .followRedirects(false)
                .followSslRedirects(false)
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

        Headers headers = new Headers.Builder()
                .add("Content-Type", "multipart/form-data; boundary=" + boundary)
                .add("Connection", "keep-alive")
                .add("Accept", "*/*")
                .add("Origin", URL.HOME)
                .add("Referer", URL.LOGIN)
                .add("Cookie", "__cfduid=" + __cfduid + ";" + "csrftoken=" + csrftoken)
                .build();

        Response loginResponse = okHttpHelerInstance.postSync(URL.LOGIN, requestBody, headers, client);

        if (Main.isDebug) out.println(loginResponse.message());

        headers = loginResponse.headers();
        List<String> cookies = headers.values("Set-Cookie");
        for (String cookie : cookies) {
            int found = cookie.indexOf("LEETCODE_SESSION");
            if (found > -1) {
                if (Main.isDebug) out.println(cookie);
                int last = cookie.indexOf(";");
                LEETCODE_SESSION = cookie.substring("LEETCODE_SESSION".length() + 1, last);
                if (Main.isDebug) out.println(LEETCODE_SESSION);
            }
        }


        if (LEETCODE_SESSION != null) {
            success = true;
            out.println("Login Successfully");
        } else {
            success = false;
            out.println("Login Unsuccessfully");
        }
        loginResponse.close();

        return success;
    }
}
