import bean.ProblemBean;
import bean.ProblemDataBean;
import bean.ResultBean;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class MarkdownGenerator {
    public static final String MARKDOWNTITLE = "| # | Title | Solution | Acceptance | Difficulty | Topics\n" +
            "|:--:|:-----:|:---------:|:----:|:----:|:----:|";
    private static volatile MarkdownGenerator instance;
    private final String TITLE_FORM = "[%s](%s)";
    private final String LANGUAG_FORM = "[%s](%s) ";
    private final String TOPIC_FORM = "[%s]()";
    private final String MARKDOWN_FORM = "| %s | %s | %s | %s | %s | %s |";
    private final String SHIELD = "<p align=\"center\"><img width=\"300\" src=\"https://raw.githubusercontent.com/ZhaoxiZhang/LeetCodeCrawler/master/pictures/site-logo.png\"></p>\n\n" +
            "<p align=\"center\">\n" +
            "    <img src=\"https://img.shields.io/badge/%d/%d-Solved/Total-blue.svg\" alt=\"\">\n" +
            "    <img src=\"https://img.shields.io/badge/Easy-%d-green.svg\" alt=\"\">\n" +
            "    <img src=\"https://img.shields.io/badge/Medium-%d-orange.svg\" alt=\"\">\n" +
            "    <img src=\"https://img.shields.io/badge/Hard-%d-red.svg\" alt=\"\">\n" +
            "</p>\n\n";

    private MarkdownGenerator() {
    }

    public static MarkdownGenerator getSingleton() {
        MarkdownGenerator result = instance;
        if (result == null) {
            synchronized (MarkdownGenerator.class) {
                result = instance;
                if (result == null) {
                    result = instance = new MarkdownGenerator();
                }
            }
        }
        return result;
    }

    public String generateMarkdown() throws IOException {
        Result resultInstance = Result.getSingleton();
        Problem problemInstance = Problem.getSingleton();

        StringBuilder markdownContent = new StringBuilder();

        ProblemBean problemBean = problemInstance.getAllProblemsInformation();
        List<ProblemBean.StatStatusPairsBean> acProblemList = problemInstance.getAllAcProblems();
        acProblemList.sort(Comparator.comparingInt(o -> o.getStat().getFrontend_question_id()));
        int fakeTotalNumProblem = Problem.getSingleton().getFakeTotalNumProblem();
        Map<Integer, List<String>> submissionLanguageMap = problemInstance.getSubmissionLanguageMap();

        String shieldString = String.format(SHIELD, problemBean.getNum_solved(), problemBean.getNum_total(), problemBean.getAc_easy(), problemBean.getAc_medium(), problemBean.getAc_hard());
        markdownContent.append(shieldString).append("\n");
        markdownContent.append(MARKDOWNTITLE).append("\n");

        for (int i = 0; i < acProblemList.size(); i++) {
            ProblemBean.StatStatusPairsBean statStatusPairsBean = acProblemList.get(i);
            ProblemBean.StatStatusPairsBean.StatBean statBean = statStatusPairsBean.getStat();
            int frontendId = statBean.getFrontend_question_id();
            String Number = Util.formId(fakeTotalNumProblem, frontendId);

            String problemTranslatedTitle = statBean.getQuestion__translated_title();
            String problemTitle = statBean.getQuestion__title();
            String problemSlug = statBean.getQuestion__title_slug();
            String Title = String.format(TITLE_FORM, problemTitle, "./" + Number + "." + problemSlug + "/" + problemSlug + ".md");

            List<String> languageList = submissionLanguageMap.get(frontendId);
            if (languageList == null || languageList.size() == 0) {
                continue;
            }

            StringBuilder solutionTmp = new StringBuilder();
            for (String language : languageList) {
                String languageSolution = String.format(LANGUAG_FORM, leetCodeName2LanguageName(language), "./" + Number + "." + problemSlug + "/" + problemSlug + "." + Util.languageName2FileTypeName(language));
                solutionTmp.append(languageSolution);
            }
            String Solution = solutionTmp.toString();

            DecimalFormat df = new DecimalFormat("#0.00");
            String Acceptance = df.format((double) statBean.getTotal_acs() / statBean.getTotal_submitted() * 100) + "%";

            int difficulty = statStatusPairsBean.getDifficulty().getLevel();
            String Difficulty = DifficultyLevel2String(difficulty);

            List<ProblemDataBean.DataBean.QuestionBean.TopicTagsBean> topicTags = statBean.getQuestion__topics_tags();

            StringBuilder topicsTmp = new StringBuilder();
            StringBuilder topicsTranslatedTmp = new StringBuilder();
            int notNullTopicCnt = 0;
            for (int j = 0; topicTags != null && j < topicTags.size(); j++) {
                ProblemDataBean.DataBean.QuestionBean.TopicTagsBean topicTag = topicTags.get(j);
                if (topicTag == null) continue;
                notNullTopicCnt++;
                String topic = String.format(TOPIC_FORM, topicTag.getName());
                if (notNullTopicCnt == 1) {
                    topicsTmp.append(topic);
                } else {
                    topicsTmp.append(" &#124; ");
                    topicsTmp.append(topic);
                }
            }
            String Topics = topicsTmp.toString();

            String row = String.format(MARKDOWN_FORM, Number, Title, Solution, Acceptance, Difficulty, Topics);

            markdownContent.append(row).append('\n');

            ResultBean savedResult = new ResultBean();
            savedResult.setFrontend_id(frontendId);
            savedResult.setLanguage(languageList);
            savedResult.setTranslatedTitle(statBean.getQuestion__translated_title());
            savedResult.setTopicTags(statBean.getQuestion__topics_tags());
            resultInstance.addElement2SavedResultList(savedResult);
        }

        return markdownContent.toString();
    }

    private String leetCodeName2LanguageName(String leetcodeName) {
        String res;
        switch (leetcodeName) {
            case "cpp":
                res = "C++";
                break;
            case "java":
                res = "Java";
                break;
            case "c":
                res = "C";
                break;
            case "csharp":
                res = "C#";
                break;
            case "javascript":
                res = "JavaScript";
                break;
            case "python":
                res = "Python";
                break;
            case "python3":
                res = "Python3";
                break;
            case "ruby":
                res = "Ruby";
                break;
            case "swift":
                res = "Swift";
                break;
            case "golang":
                res = "Go";
                break;
            case "scala":
                res = "Scala";
                break;
            case "kotlin":
                res = "Kotlin";
                break;
            default:
                res = "";
        }
        return res;
    }

    private String DifficultyLevel2String(int level) {
        String res;
        switch (level) {
            case 1:
                res = "Easy";
                break;
            case 2:
                res = "Medium";
                break;
            case 3:
                res = "Hard";
                break;
            default:
                res = "";
        }
        return res;
    }
}
