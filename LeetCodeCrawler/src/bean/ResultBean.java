package bean;


import java.util.List;

public class ResultBean {

    /**
     * frontend_id : 1
     * language : ["cpp","java"]
     * translatedTitle : 两数之和
     * topicTags : [{"name":"Array","slug":"array","translatedName":"数组"}]
     */

    private int frontend_id;
    private List<String> language;
    private String translatedTitle;
    private List<ProblemDataBean.DataBean.QuestionBean.TopicTagsBean> topicTags;

    public int getFrontend_id() {
        return frontend_id;
    }

    public void setFrontend_id(int frontend_id) {
        this.frontend_id = frontend_id;
    }

    public List<String> getLanguage() {
        return language;
    }

    public void setLanguage(List<String> language) {
        this.language = language;
    }

    public String getTranslatedTitle() {
        return translatedTitle;
    }

    public void setTranslatedTitle(String translatedTitle) {
        this.translatedTitle = translatedTitle;
    }

    public List<ProblemDataBean.DataBean.QuestionBean.TopicTagsBean> getTopicTags() {
        return topicTags;
    }

    public void setTopicTags(List<ProblemDataBean.DataBean.QuestionBean.TopicTagsBean> topicTags) {
        this.topicTags = topicTags;
    }

}
