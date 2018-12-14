import com.google.gson.Gson;
import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public final class OkHttpHelper {
    private static volatile OkHttpHelper okHttpHelperInstance;
    private OkHttpClient okHttpClient;
    private Gson gson;


    private OkHttpHelper(){
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();

        gson = new Gson();
    }

    public static OkHttpHelper getSingleton(){
        OkHttpHelper result = okHttpHelperInstance;
        if (result == null){
            synchronized (OkHttpHelper.class){
                result = okHttpHelperInstance;
                if (result == null){
                    result = okHttpHelperInstance = new OkHttpHelper();
                }
            }
        }
        return result;
    }

    private Response getInternal(String url, Headers headers) throws IOException {
        Request request = new Request.Builder()
                .headers(headers)
                .url(url)
                .build();
        Call call = okHttpClient.newCall(request);
        return call.execute();
    }

    private Response postInternal(String url, RequestBody requestBody, Headers headers) throws IOException {
        Request request = new Request.Builder()
                .headers(headers)
                .post(requestBody)
                .url(url)
                .build();
        Call call = okHttpClient.newCall(request);
        return call.execute();
    }

    public Response get(String url, Headers headers) throws IOException {
        return getInternal(url, headers);
    }

    public Response post(String url, RequestBody requestBody, Headers headers) throws IOException {
        return postInternal(url, requestBody, headers);
    }


    public <T> T fromJson(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }

}
