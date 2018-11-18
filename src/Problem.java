import bean.ProblemBean;
import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;

public class Problem {
    private static volatile ProblemBean problems;
    private static volatile List<ProblemBean.StatStatusPairsBean>acProblems;
    private static volatile List<String>problemNameList;

    //TODO 写成死循环形式直至获取到数据
    public List<ProblemBean.StatStatusPairsBean> getAllProblems() throws IOException {
        ProblemBean instance = problems;
        if (instance == null){
            synchronized (Problem.class){
                instance = problems;
                if (instance == null){
                    OkHttpClient client = OkHttpClientSingleton.getOkHttpClientSingleton();
                    Request request = new Request.Builder()
                            .url(URL.PROBLEMS)
                            .build();

                    Response response = client.newCall(request).execute();
                    if (response.body() != null){
                        String responseData = response.body().string();
                        Gson gson = new Gson();
                        Problem.problems = gson.fromJson(responseData, ProblemBean.class);

                        response.close();

                    }else{
                        //TODO 输出错误信息
                        out.println("没有获取到题目信息");
                    }
                }
            }
        }
        return problems.getStat_status_pairs();
    }

    public List<ProblemBean.StatStatusPairsBean> getAllAcProblems() throws IOException {
        List<ProblemBean.StatStatusPairsBean>result = acProblems;
        if (result == null){
            synchronized (Problem.class){
                result = acProblems;
                if (result == null){
                    acProblems = new ArrayList<>();
                    List<ProblemBean.StatStatusPairsBean>problems = getAllProblems();
                    ProblemBean.StatStatusPairsBean problem;
                    for (int i = 0; i < problems.size(); i++){
                        problem = problems.get(i);
                        if (problem.getStatus() != null && problem.getStatus().equals("ac")){
                            acProblems.add(problem);
                        }
                    }
                }
            }
        }

        return acProblems;
    }

    //得到的题目名称格式类似于 001.two-sum
    public List<String> getAllAcProblemsName() throws IOException {
        List<String>result = problemNameList;

        if (result == null){
            synchronized (Problem.class){
                result = problemNameList;
                if (result == null){
                    int total = getAllProblems().size();
                    List<ProblemBean.StatStatusPairsBean>acProblems = getAllAcProblems();

                    problemNameList = new ArrayList<>(acProblems.size());
                    for (int i = 0; i < acProblems.size(); i++){
                        int id = acProblems.get(i).getStat().getQuestion_id();
                        String problemTitle = acProblems.get(i).getStat().getQuestion__title_slug();
                        problemNameList.add(formId(total, id) + "." + problemTitle);
                    }
                }
            }
        }

        return problemNameList;
    }

    public String formId(int total, int id){

        int digitCntTotal = (int)Math.log10(total);
        int digitCntId = (int)Math.log10(id);
        int needDigitCnt = digitCntTotal - digitCntId;

        StringBuilder res = new StringBuilder(digitCntTotal);
        while (needDigitCnt-- != 0){
            res.append(0);
        }
        res.append(id);
        return res.toString();
    }
}
