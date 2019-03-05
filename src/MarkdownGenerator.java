import bean.ProblemBean;
import bean.ResultBean;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static java.lang.System.out;

public class MarkdownGenerator {
    public static final String MARKDOWNTITLE = "| # | Title | Solution | Acceptance | Difficulty | Paid-Only\n" +
            "|:--:|:-----:|:---------:|:----:|:----:|:----:|";
    public static final String TITLE_FORM = "[%s](%s)";
    public static final String LANGUAG_FORM = "[%s](%s) ";
    public static final String MARKDOWN_FORM = "| %s | %s | %s | %s | %s | %s |";
    public static final String SHIELD = "<p align=\"center\"><img width=\"300\" src=\"https://raw.githubusercontent.com/ZhaoxiZhang/LeetCodeCrawler/master/pictures/site-logo.png\"></p>\n\n" +
            "<p align=\"center\">\n" +
            "    <img src=\"https://img.shields.io/badge/%d/%d-Solved/Total-blue.svg\" alt=\"\">\n" +
            "    <img src=\"https://img.shields.io/badge/Easy-%d-green.svg\" alt=\"\">\n" +
            "    <img src=\"https://img.shields.io/badge/Medium-%d-orange.svg\" alt=\"\">\n" +
            "    <img src=\"https://img.shields.io/badge/Hard-%d-red.svg\" alt=\"\">\n" +
            "</p>\n\n";


    public String generateMarkdown() throws IOException {
        Result result = Result.getSingleton();
        StringBuilder markdownString = new StringBuilder();
        int easy = 0;
        int medium = 0;
        int hard = 0;
        Problem problemInstance = Problem.getSingleton();
        List<ProblemBean.StatStatusPairsBean> acProblems = problemInstance.getAllAcProblems();
        Collections.sort(acProblems, Comparator.comparingInt(o -> o.getStat().getFrontend_question_id()));
        Map<Integer, List<String>> submissionLanguageMap = problemInstance.getSubmissionLanguage();
        int totalProblems = problemInstance.getFakeTotalNumProblem();

        markdownString.append(MARKDOWNTITLE + "\n");
        for (int i = 0; i < acProblems.size(); i++) {
            ProblemBean.StatStatusPairsBean problem = acProblems.get(i);
            int Id = problem.getStat().getFrontend_question_id();
            String Number = problemInstance.formId(totalProblems, Id);
            String problemTitle = problem.getStat().getQuestion__title();
            String problemSlug = problem.getStat().getQuestion__title_slug();
            String Title = String.format(MarkdownGenerator.TITLE_FORM, problemTitle, Storage.outputDir + "/" + Number + "." + problemSlug + "/" + problemSlug + ".md");

            StringBuilder SolutionTmp = new StringBuilder();
            List<String> languageList = submissionLanguageMap.get(Id);

            if (languageList == null){
                if (Main.isDebug)   out.println("list is null id = " + Id + " title = " + problemTitle);
                continue;
            }

            for (int j = 0; j < languageList.size(); j++) {
                String language = languageList.get(j);
                String languageSolution = String.format(MarkdownGenerator.LANGUAG_FORM, leetCodeName2LanguageName(language), Storage.outputDir + "/" + Number + "." + problemSlug + "/" + problemSlug + "." + Util.languageName2FileTypeName(language));
                SolutionTmp.append(languageSolution);
            }
            String Solution = SolutionTmp.toString();

            DecimalFormat df = new DecimalFormat("#0.00");
            String Acceptance = df.format((double) problem.getStat().getTotal_acs() / problem.getStat().getTotal_submitted() * 100) + "%";

            int difficulty = problem.getDifficulty().getLevel();
            switch (difficulty){
                case 1:
                    easy++;
                    break;
                case 2:
                    medium++;
                    break;
                case 3:
                    hard++;
                    break;
            }
            String Difficulty = DifficultyLevel2String(difficulty);

            String Paid_only = problem.isPaid_only() ? "Yes" : " ";

            String row = String.format(MARKDOWN_FORM, Number, Title, Solution, Acceptance, Difficulty, Paid_only);

            if (Main.isDebug)   out.println(row);

            markdownString.append(row + "\n");

            ResultBean savedResult = new ResultBean();
            savedResult.setId(Id);
            savedResult.setLanguage(languageList);
            result.addElement2SavedResultList(savedResult);
        }

        String shieldString = String.format(SHIELD, easy + medium + hard, totalProblems, easy, medium, hard);

        markdownString.insert(0, shieldString);
        return markdownString.toString();
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
