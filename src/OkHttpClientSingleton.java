import okhttp3.OkHttpClient;

public final class OkHttpClientSingleton{
    private static volatile OkHttpClient instance;
    private OkHttpClientSingleton(){

    }

    public static OkHttpClient getOkHttpClientSingleton(){
        OkHttpClient result = instance;
        if (result == null){
            synchronized (OkHttpClientSingleton.class){
                result = instance;
                if (result == null){
                    result = instance = new OkHttpClient().newBuilder()
                            .followRedirects(false)
                            .followSslRedirects(false)
                            .cookieJar(new MyCookieJar())
                            .build();
                }
            }
        }
        return result;
    }
}
