package bean;


import java.util.List;

public class ResultBean {


    /**
     * id : 1
     * language : ["cpp","java"]
     */

    private int id;
    private List<String> language;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public List<String> getLanguage() {
        return language;
    }

    public void setLanguage(List<String> language) {
        this.language = language;
    }
}
