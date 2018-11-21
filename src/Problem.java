import bean.ProblemBean;
import bean.ProblemContentBean;
import bean.SubmissionBean;
import com.google.gson.Gson;
import okhttp3.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.System.out;

public class Problem {
    private static volatile Problem problem;
    private static volatile ProblemBean problems;
    private static volatile List<ProblemBean.StatStatusPairsBean> acProblems;
    private static volatile List<String> problemNameList;
    private static volatile List<String> problemFormatNameList;
    private static volatile Map<Integer, List<String>> submissionLanguageMap;

    private Problem() {
    }

    public static Problem getInstance() {
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

    //TODO 写成死循环形式直至获取到数据
    public List<ProblemBean.StatStatusPairsBean> getAllProblems() throws IOException {
        ProblemBean instance = problems;
        if (instance == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();
            Request request = new Request.Builder()
                    .addHeader("Cookie", "__cfduid=" + Login.__cfduid + ";" + "csrftoken=" + Login.csrftoken + ";" + "LEETCODE_SESSION=" + Login.LEETCODE_SESSION)
                    .url(URL.PROBLEMS)
                    .build();

            Response response = okHttpClient.newCall(request).execute();
            if (response.body() != null) {
                String responseData = response.body().string();

                Gson gson = new Gson();
                Problem.problems = gson.fromJson(responseData, ProblemBean.class);

                response.close();

            } else {
                //TODO 输出错误信息
                out.println("没有获取到题目信息");
            }
        }
        return problems.getStat_status_pairs();
    }

    public List<ProblemBean.StatStatusPairsBean> getAllAcProblems() throws IOException {
        List<ProblemBean.StatStatusPairsBean> instance = acProblems;
        if (instance == null) {
            acProblems = new ArrayList<>();
            List<ProblemBean.StatStatusPairsBean> problems = getAllProblems();
            ProblemBean.StatStatusPairsBean problem;
            for (int i = 0; i < problems.size(); i++) {
                problem = problems.get(i);
                if (problem.getStatus() != null && problem.getStatus().equals("ac")) {
                    acProblems.add(problem);
                }
            }
        }

        return acProblems;
    }

    //得到的题目名称格式类似于 001.two-sum
    public List<String> getAllAcProblemsName() throws IOException {
        List<String> instance = problemNameList;
        if (instance == null) {
            int total = getAllProblems().size();
            List<ProblemBean.StatStatusPairsBean> acProblems = getAllAcProblems();

            problemNameList = new ArrayList<>(acProblems.size());
            for (int i = 0; i < acProblems.size(); i++) {
                int id = acProblems.get(i).getStat().getQuestion_id();
                String problemTitle = acProblems.get(i).getStat().getQuestion__title_slug();
                problemNameList.add(formId(total, id) + "." + problemTitle);
            }
        }
        return problemNameList;
    }

    public String formId(int total, int id) {
        int digitCntTotal = (int) Math.log10(total);
        int digitCntId = (int) Math.log10(id);
        int needDigitCnt = digitCntTotal - digitCntId;

        StringBuilder res = new StringBuilder(digitCntTotal);
        while (needDigitCnt-- != 0) {
            res.append(0);
        }
        res.append(id);
        return res.toString();
    }

    public String getProblemDescription(String problemTitle) throws IOException {
        String problemDescriptionString = "";
        String postBody = "query{question(titleSlug:\"" + problemTitle + "\") {content}}\n";
        Request graphqlRequest = new Request.Builder()
                .addHeader("Content-Type", "application/graphql")
                .addHeader("Referer", "https://leetcode.com/problems/" + problemTitle)
                .addHeader("Cookie", "__cfduid=" + Login.__cfduid + ";" + "csrftoken=" + Login.csrftoken)
                .addHeader("x-csrftoken", Login.csrftoken)
                .post(RequestBody.create(MediaType.parse("application/graphql; charset=utf-8"), postBody))
                .url(URL.GRAPHQL)
                .build();
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
        Response graphqlResponse = okHttpClient.newCall(graphqlRequest).execute();
        if (graphqlResponse != null) {
            Gson gson = new Gson();
            out.println(graphqlResponse.body().toString());
            ProblemContentBean problemContentBean = gson.fromJson(graphqlResponse.body().string(), ProblemContentBean.class);
            problemDescriptionString = problemContentBean.getData().getQuestion().getContent();
        }else{
            //TODO
        }
        return problemDescriptionString;
    }

    public String getSubmissionCode(String submissionUrl) throws IOException {
        String url = URL.LEETCODE + submissionUrl;
        out.println(url);
        String codeString = null;

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.MINUTES)
                .readTimeout(3, TimeUnit.MINUTES)
                .writeTimeout(3, TimeUnit.MINUTES)
                .build();
        Request request = new Request.Builder()
                .addHeader("Cookie", "__cfduid=" + Login.__cfduid + ";" + "csrftoken=" + Login.csrftoken + ";" + "LEETCODE_SESSION=" + Login.LEETCODE_SESSION)
                .url(url)
                .build();

        Response response = okHttpClient.newCall(request).execute();

        if (response != null) {
            String htmlString = response.body().string();

            Document document = Jsoup.parse(htmlString);
            Elements elements = document.getElementsByTag("script");
            for (Element element : elements) {
                int indexStart = element.toString().indexOf("submissionCode: '");
                if (indexStart > -1) {
                    int indexTo = element.toString().indexOf("editCodeUrl");
                    codeString = element.toString().substring(indexStart + ("submissionCode: '").length(), indexTo - 5);
                    break;
                }
            }
        } else {
            //TODO 错误信息处理
        }

        codeString = encode(codeString);

        return codeString;
    }

    public Map<String, String> getSubmissions(String problemTitle) throws IOException {
        Map<String, String> submissionMap = new HashMap<>(12);
        int offset = 0;
        int limit = 10;
        boolean hasNext = true;
        String lastKey = "";

        List<String> languageList = Config.getSingleton().getLanguageList();
        Map<String, Boolean>languageMap = new HashMap<>(12);
        for (int i = 0; i < languageList.size(); i++){
            languageMap.put(languageList.get(i), false);
        }

        while(hasNext){
            String submissionsUrl = String.format(URL.SUBMISSIONS_FORMAT, problemTitle, offset, limit, lastKey);
            out.println("submissionsUrl = " + submissionsUrl);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();
            Request request = new Request.Builder()
                    .addHeader("Cookie", "__cfduid=" + Login.__cfduid + ";" + "csrftoken=" + Login.csrftoken + ";" + "LEETCODE_SESSION=" + Login.LEETCODE_SESSION)
                    .url(submissionsUrl)
                    .build();
            Response response = okHttpClient.newCall(request).execute();

            if (response != null){
                String responseData = response.body().string();

                Gson gson = new Gson();
                SubmissionBean submissionBean = gson.fromJson(responseData, SubmissionBean.class);
                List<SubmissionBean.SubmissionsDumpBean> submissionsDumpList = submissionBean.getSubmissions_dump();

                for (int i = 0; i < submissionsDumpList.size(); i++){
                    SubmissionBean.SubmissionsDumpBean submission = submissionsDumpList.get(i);
                    String language = submission.getLang();
                    if (languageMap.containsKey(language) && languageMap.get(language) == false && submission.getStatus_display().equals("Accepted")){
                        submissionMap.put(language, getSubmissionCode(submission.getUrl()));
                        languageMap.put(language, true);
                    }
                }

                hasNext = submissionBean.isHas_next();
                offset = (++offset) * limit;
                lastKey = submissionBean.getLast_key();

            }else{
                //TODO
            }
        }

        return submissionMap;
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

    public Map<Integer, List<String>>getSubmissionLanguage(){
        if (submissionLanguageMap == null){
            submissionLanguageMap = new HashMap<>();
        }
        return submissionLanguageMap;
    }
}
