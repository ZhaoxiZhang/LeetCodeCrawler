import bean.ProblemBean;
import bean.ProblemDataBean;
import bean.ResultBean;
import bean.SubmissionBean;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.System.out;

public class Problem {
    private static volatile Problem problem;
    private ProblemBean problems;
    private List<ProblemBean.StatStatusPairsBean> acProblemList;
    private Map<Integer, List<String>> submissionLanguageMap;
    private OkHttpHelper okHttpHelperInstance;
    private Config configInstance;
    private int fakeTotalNumProblem;

    private Problem() {
        okHttpHelperInstance = OkHttpHelper.getSingleton();
        configInstance = Config.getSingleton();
        submissionLanguageMap = new ConcurrentHashMap<>();
        fakeTotalNumProblem = -1;
    }

    public static Problem getSingleton() {
        Problem result = problem;
        if (result == null) {
            synchronized (Problem.class) {
                result = problem;
                if (result == null) {
                    result = problem = new Problem();
                }
            }
        }
        return result;
    }

    public ProblemBean getAllProblemsInformation() throws IOException {
        ProblemBean instance = problems;

        while (instance == null) {
            Response response = okHttpHelperInstance.getSync(URL.PROBLEMS);

            if (response.isSuccessful() && response.body() != null) {
                String responseData = response.body().string();
                if (Main.isDebug) out.println(responseData);

                instance = problems = okHttpHelperInstance.fromJson(responseData, ProblemBean.class);

                response.close();
            }
        }

        return instance;
    }

    public List<ProblemBean.StatStatusPairsBean> getAllProblems() throws IOException {
        return getAllProblemsInformation().getStat_status_pairs();
    }

    public List<ProblemBean.StatStatusPairsBean> getAllAcProblems() throws IOException {
        List<ProblemBean.StatStatusPairsBean> instance = acProblemList;
        if (instance == null) {
            List<ProblemBean.StatStatusPairsBean> allProblemList = getAllProblems();
            instance = acProblemList = new ArrayList<>(problems.getNum_solved());
            for (ProblemBean.StatStatusPairsBean statStatusPairsBean : allProblemList) {
                if (statStatusPairsBean.getStatus() != null && statStatusPairsBean.getStatus().equals("ac")) {
                    instance.add(statStatusPairsBean);
                }
            }
        }
        return instance;
    }

    /**
     * 由于LeetCode存在题号不连续的情况，例如510直接跳到513，缺少两题
     * 因此通过JSON获得的数据段"num_total"的值存在小于最新题号值的情况，
     * 例如："num_total" = 999, 最新题号已经1004
     * 因此使用num_total作为总值生成的格式化的字符串ID在某一情况下为：
     * 001  002 ... 1003 1004 长度不都为4，
     * 为了消除这个问题，取最新题号值为题目数量的最大值
     */
    public int getFakeTotalNumProblem() throws IOException {
        if (fakeTotalNumProblem == -1) {
            List<ProblemBean.StatStatusPairsBean> problems = getAllProblems();
            for (ProblemBean.StatStatusPairsBean statStatusPairsBean : problems) {
                fakeTotalNumProblem = Math.max(statStatusPairsBean.getStat().getFrontend_question_id(), fakeTotalNumProblem);
            }
        }
        return fakeTotalNumProblem;
    }

    public String getProblemDescription(ProblemBean.StatStatusPairsBean.StatBean problem) throws IOException {
        String problemDescription = null;

        String postBody = String.format(GraphQL.questionData, problem.getQuestion__title_slug());
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), postBody);
        Headers headers = new Headers.Builder()
                .add("Content-Type", "application/json")
                .add("Referer", String.format(URL.PROBLEM, problem.getQuestion__title_slug()))
                .add("origin", URL.HOME)
                .build();

        Response response;
        while (problemDescription == null) {
            response = okHttpHelperInstance.postSync(URL.GRAPHQL, requestBody, headers);

            if (response.isSuccessful() && response.body() != null) {
                String responseData = response.body().string();
                ProblemDataBean problemDataBean = okHttpHelperInstance.fromJson(responseData, ProblemDataBean.class);
                problemDescription = problemDataBean.getData().getQuestion().getTranslatedContent();
                problem.setQuestion__translated_title(problemDataBean.getData().getQuestion().getTranslatedTitle());
                problem.setQuestion__topics_tags(problemDataBean.getData().getQuestion().getTopicTags());
                response.close();
            }
        }

        return problemDescription;
    }

    public String encode(String s) {
        Pattern reUnicode = Pattern.compile("\\\\u([0-9a-zA-Z]{4})");
        Matcher matcher = reUnicode.matcher(s);
        StringBuffer sb = new StringBuffer(s.length());
        while (matcher.find()) {
            String replace = Character.toString((char) Integer.parseInt(matcher.group(1), 16));
            if (replace.equals("\\")) {
                replace = "\\\\";
            }
            matcher.appendReplacement(sb, replace);
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    public String getSubmissionCode(String submissionUrl) throws IOException, InterruptedException {
        String submissionDetailURL = URL.HOME + submissionUrl;
        String codeContent = "";

        Response response;
        while (codeContent.isEmpty()) {

            response = okHttpHelperInstance.getSync(submissionDetailURL);
            if (response.isSuccessful() && response.body() != null) {
                String htmlContent = response.body().string();

                if (Main.isDebug) {
                    out.println();
                    out.println("getSubmissionCode = " + submissionDetailURL);
                    out.println("message = " + response.message());
                    out.println("code = " + response.code());

                }

                Document document = Jsoup.parse(htmlContent);
                Elements elements = document.getElementsByTag("script");
                for (Element element : elements) {
                    int indexStart = element.toString().indexOf("submissionCode: '");
                    if (indexStart > -1) {
                        int indexTo = element.toString().indexOf("editCodeUrl");
                        codeContent = element.toString().substring(indexStart + ("submissionCode: '").length(), indexTo - 5);
                        break;
                    }
                }

                response.close();
            } else {    //too many requests
                out.println();
                out.println("code : " + response.code() + " message : " + response.message());
                if (response.code() == 429){
                    out.println("The program will run after 40 seconds later");
                    Thread.sleep(40000);
                }
                out.println();
            }
        }

        codeContent = encode(codeContent);

        return codeContent;
    }

    public Map<String, String> getSubmissions(String problemTitleSlug, ResultBean resultBean) throws IOException, InterruptedException {
        Map<String, String> submissionMap = new HashMap<>();

        int offset = 0;
        int limit = 20;
        boolean hasNext = true;
        String lastKey = "";
        List<String> languageList = new ArrayList<>(configInstance.getLanguageList());
        List<String> savedLanguageList = resultBean != null ? resultBean.getLanguage() : new ArrayList<>();
        Iterator<String> iterator = languageList.iterator();
        while (iterator.hasNext()) {
            String language = iterator.next();
            for (String savedLanguage : savedLanguageList) {
                if (savedLanguage.equals(language)) {
                    iterator.remove();
                }
            }
        }

        while (hasNext) {
            String postBody = String.format(GraphQL.questionSubmissions, offset, limit, lastKey, problemTitleSlug);
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), postBody);
            Headers headers = new Headers.Builder()
                    .add("Content-Type", "application/json")
                    .add("Referer", String.format(URL.SUBMISSIONS, problemTitleSlug))
                    .add("origin", URL.HOME)
                    .build();

            String responseData = "";
            while (responseData.isEmpty()) {

                Response response = okHttpHelperInstance.postSync(URL.GRAPHQL, requestBody, headers);

                if (response.isSuccessful() && response.body() != null) {
                    responseData = response.body().string();

                    SubmissionBean submissionBean = okHttpHelperInstance.fromJson(responseData, SubmissionBean.class);
                    List<SubmissionBean.DataBean.SubmissionListBean.SubmissionsBean> submissionsBeanList = submissionBean.getData().getSubmissionList().getSubmissions();

                    if (submissionsBeanList == null) {
                        if (Main.isDebug) {
                            out.println();
                            out.println("problemTitleSlug = " + problemTitleSlug);
                            out.println("response data = " + responseData);
                            out.println("status message = " + response.message());
                            out.println("message code = " + response.code());
                            out.println();
                        }
                        continue;
                    }

                    for (int i = 0; i < submissionsBeanList.size() && languageList.size() != 0; i++) {
                        SubmissionBean.DataBean.SubmissionListBean.SubmissionsBean submission = submissionsBeanList.get(i);
                        if (submission.getStatusDisplay().equals("Accepted")) {
                            String language = submission.getLang();
                            iterator = languageList.iterator();
                            while (iterator.hasNext()) {
                                if (iterator.next().equals(language)) {
                                    submissionMap.put(language, getSubmissionCode(submission.getUrl()));
                                    iterator.remove();
                                    break;
                                }
                            }
                        }
                    }
                    hasNext = submissionBean.getData().getSubmissionList().isHasNext();
                    offset = ((offset / 10) + 1) * limit;
                    lastKey = submissionBean.getData().getSubmissionList().getLastKey();
                }

            }

            if (languageList.size() == 0) {
                break;
            }
        }

        return submissionMap;
    }

    public Map<Integer, List<String>> getSubmissionLanguageMap() {
        return submissionLanguageMap;
    }

    public void putData2SubmissionLanguageMap(Integer problemFrontedId, List<String> languageList) {
        if (Main.isDebug) {
            out.println();
            out.println("putData2SubmissionLanguageMap + " + problemFrontedId + " " + languageList.size());
        }
        submissionLanguageMap.put(problemFrontedId, languageList);
    }

}
