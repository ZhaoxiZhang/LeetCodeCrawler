import bean.ProblemBean;
import bean.ProblemContentBean;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.System.out;

public class Problem {
    private static volatile Problem problem;
    private ProblemBean problems;
    private List<ProblemBean.StatStatusPairsBean> acProblems;
    private List<String> problemNameList;
    private List<String> problemFormatNameList;
    private Map<Integer, List<String>> submissionLanguageMap;
    private OkHttpHelper okHttpHelper;

    private Problem() {
        okHttpHelper = OkHttpHelper.getSingleton();
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

            Headers headers = new Headers.Builder()
                    .add("Cookie", "__cfduid=" + Login.__cfduid + ";" + "csrftoken=" + Login.csrftoken + ";" + "LEETCODE_SESSION=" + Login.LEETCODE_SESSION)
                    .build();
            Response response = okHttpHelper.get(URL.PROBLEMS, headers);

            if (response.body() != null) {
                String responseData = response.body().string();

                instance = problems = okHttpHelper.fromJson(responseData, ProblemBean.class);

                response.close();

            } else {
                //TODO 输出错误信息
                out.println("没有获取到题目信息");
            }
        }
        return instance.getStat_status_pairs();
    }

    public List<ProblemBean.StatStatusPairsBean> getAllAcProblems() throws IOException {
        List<ProblemBean.StatStatusPairsBean> instance = acProblems;
        if (instance == null) {
            instance = acProblems = new ArrayList<>();
            List<ProblemBean.StatStatusPairsBean> problems = getAllProblems();
            ProblemBean.StatStatusPairsBean problem;
            for (int i = 0; i < problems.size(); i++) {
                problem = problems.get(i);
                if (problem.getStatus() != null && problem.getStatus().equals("ac")) {
                    instance.add(problem);
                }
            }
        }

        return instance;
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
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/graphql; charset=utf-8"), postBody);
        Headers headers = new Headers.Builder()
                .add("Content-Type", "application/graphql")
                .add("Referer", "https://leetcode.com/problems/" + problemTitle)
                .add("Cookie", "__cfduid=" + Login.__cfduid + ";" + "csrftoken=" + Login.csrftoken + ";" + "LEETCODE_SESSION=" + Login.LEETCODE_SESSION)
                .add("x-csrftoken", Login.csrftoken)
                .build();

        Response graphqlResponse = okHttpHelper.post(URL.GRAPHQL, requestBody, headers);

        if (graphqlResponse != null) {
            ProblemContentBean problemContentBean = okHttpHelper.fromJson(graphqlResponse.body().string(), ProblemContentBean.class);
            problemDescriptionString = problemContentBean.getData().getQuestion().getContent();

            graphqlResponse.close();
        }else{
            //TODO 输出错误信息
        }
        return problemDescriptionString;
    }

    public String getSubmissionCode(String submissionUrl) throws IOException {
        String url = URL.LEETCODE + submissionUrl;
        out.println(url);
        String codeString = null;

        Headers headers = new Headers.Builder()
                .add("Cookie", "__cfduid=" + Login.__cfduid + ";" + "csrftoken=" + Login.csrftoken + ";" + "LEETCODE_SESSION=" + Login.LEETCODE_SESSION)
                .build();

        Response response = okHttpHelper.get(url, headers);

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

            response.close();
        } else {
            //TODO 错误信息处理
        }

        codeString = encode(codeString);

        return codeString;
    }


    /**
     * 在获取提交的代码的时候服务器可能返回403，一直重试可成功，原因进一步查找中
     * @param problemTitle
     * @return 某个题目对于 config 文件指定的语言提交的代码
     * @throws IOException
     */
    public Map<String, String> getSubmissions(String problemTitle) throws IOException {
        out.println("pre problemTitle = " + problemTitle);
        //保存语言对应的提交代码
        Map<String, String> submissionMap = new HashMap<>(12);
        int offset = 0;
        int limit = 10;
        boolean hasNext = true;
        String lastKey = "";

        List<String> languageList = Config.getSingleton().getLanguageList();

        //保存某个语言的代码是否已经抓取
        Map<String, Boolean>languageMap = new HashMap<>(12);
        for (int i = 0; i < languageList.size(); i++){
            languageMap.put(languageList.get(i), false);
        }

        while(hasNext){
            String submissionsUrl = String.format(URL.SUBMISSIONS_FORMAT, problemTitle, offset, limit, lastKey);

            Headers headers = new Headers.Builder()
                    .add("Cookie", "__cfduid=" + Login.__cfduid + ";" + "csrftoken=" + Login.csrftoken + ";" + "LEETCODE_SESSION=" + Login.LEETCODE_SESSION)
                    .build();

            Response response = okHttpHelper.get(submissionsUrl, headers);

            if (response != null){
                String responseData = response.body().string();

                SubmissionBean submissionBean = okHttpHelper.fromJson(responseData, SubmissionBean.class);
                List<SubmissionBean.SubmissionsDumpBean> submissionsDumpList = submissionBean.getSubmissions_dump();

                if (submissionsDumpList == null){
                    out.println("submissionsUrl = " + submissionsUrl);
                    out.println("problemTitle = " + problemTitle);
                    out.println("responseData = " + responseData);
                    out.println("status message = " + response.message());
                    out.println("message code = " + response.code());
                    /*
                     * 当获取不到提交记录时休眠一小段时间后进行重复尝试,服务器返回如下信息
                     * responseData = {"detail":"You do not have permission to perform this action."}
                     * status message = Forbidden
                     * message code = 403
                     */
                    continue;
                }

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

                response.close();
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


    /**
     * 在 Storage 类中的 writeSubmissions2Disk 方法会对 submissionLanguageMap 填充数据
     * 因此需要在 writeSubmissions2Disk 方法执行后调用方有效
     * @return submissionLanguageMap 某道题对于 config 文件中指定的语言中真实提交的语言
     */
    public Map<Integer, List<String>>getSubmissionLanguage(){
        if (submissionLanguageMap == null){
            submissionLanguageMap = new ConcurrentHashMap<>();
        }
        return submissionLanguageMap;
    }
}
