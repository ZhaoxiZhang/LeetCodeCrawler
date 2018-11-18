import okhttp3.*;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

import static java.lang.System.out;

public class Login {

    public static final String boundary = "----WebKitFormBoundaryMIlRKhiheAsBA5xM";
    public static final MediaType MULTIPART = MediaType.parse("multipart/form-data; boundary=" + boundary);

    private String usrname;
    private String passwd;
    public Login(String usrname, String passwd){
        this.usrname = usrname;
        this.passwd = passwd;
    }

    public void doLogin() throws IOException {
        Connection.Response response = Jsoup.connect(URL.LOGIN)
                .method(Connection.Method.GET)
                .execute();

        String csrftoken = response.cookie("csrftoken");
        String __cfduid = response.cookie("__cfduid");

        OkHttpClient client = OkHttpClientSingleton.getOkHttpClientSingleton();

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

        out.println(loginResponse.headers());
        out.println(loginResponse.message());

        loginResponse.close();
    }
}
