package bean;

import java.util.List;

public class ConfigBean {

    /**
     * username : xxx
     * password : xxx
     * language : ["cpp","java"]
     * outputDir : ./Problems
     */
    private String username;
    private String password;
    private String outputDir;
    private List<String> language;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOutputDir() {
        return outputDir;
    }

    public void setOutputDir(String outputDir) {
        this.outputDir = outputDir;
    }

    public List<String> getLanguage() {
        return language;
    }

    public void setLanguage(List<String> language) {
        this.language = language;
    }
}