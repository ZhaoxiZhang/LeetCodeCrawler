import bean.ConfigBean;
import com.google.gson.Gson;

import java.io.*;
import java.util.List;

public final class Config {
    private static volatile Config instance;

    private ConfigBean configBean;

    private String username;
    private String password;
    private String outputDir;
    private List<String> languageList;

    private Config(){}

    public static Config getSingleton(){
        Config result = instance;
        if (result == null){
            synchronized (Config.class){
                result = instance;
                if (result == null){
                    result = instance = new Config();
                }
            }
        }
        return result;
    }


    /**
     * 获取 config.json 文件的数据
     */
    private ConfigBean getConfigBean() throws IOException {
        if (configBean == null){
            StringBuilder configString = new StringBuilder();
            File file = new File("./config.json");
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader reader = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            while ((line = bufferedReader.readLine()) != null){
                configString.append(line);
            }
            bufferedReader.close();
            Gson gson = new Gson();
            configBean = gson.fromJson(configString.toString(), ConfigBean.class);
        }
        return configBean;
    }

    public String getUsername() throws IOException {
        if (username == null){
            username = getConfigBean().getUsername();
        }
        return username;
    }

    public String getPassword() throws IOException{
        if (password == null){
            password = getConfigBean().getPassword();
        }
        return password;
    }

    public List<String> getLanguageList() throws IOException{
        if (languageList == null){
            languageList = getConfigBean().getLanguage();
        }
        return languageList;
    }

    public String getOutputDir() throws IOException{
        if (outputDir == null){
            outputDir = getConfigBean().getOutputDir();
        }
        return outputDir;
    }
}
