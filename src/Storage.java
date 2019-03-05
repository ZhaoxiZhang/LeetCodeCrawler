import bean.ProblemBean;
import bean.ResultBean;
import com.google.gson.Gson;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.lang.System.out;

public class Storage {
    public static String outputDir = ".";

    public void write2Disk(List<ProblemBean.StatStatusPairsBean> acProblemList) throws IOException, InterruptedException {
        Problem problem = Problem.getSingleton();
        Result result = Result.getSingleton();
        List<ResultBean>restoredResultList = result.getRestoredResultList();

        //按题目 Id 从小到大排序
        Collections.sort(acProblemList, Comparator.comparingInt(o -> o.getStat().getFrontend_question_id()));
        //按题目 Id 从小到大排序
        Collections.sort(restoredResultList, (Comparator.comparingInt(ResultBean::getId)));

        int totalProblems = problem.getFakeTotalNumProblem();
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(2 * availableProcessors);

        int restoredResultIndex = 0;
        for (int i = 0; i < acProblemList.size(); i++) {
            //查找已经 AC 的题目在之前是否有数据保存在本地
            boolean hasExist = false;
            ResultBean resultBean = null;
            if (restoredResultIndex < restoredResultList.size()){
                int restoredResultIndexId = restoredResultList.get(restoredResultIndex).getId();
                int acProblemListIndexId = acProblemList.get(i).getStat().getFrontend_question_id();

                if (restoredResultIndexId == acProblemListIndexId){
                    hasExist = true;
                    restoredResultIndex++;
                }

                resultBean = hasExist ? restoredResultList.get(restoredResultIndex - 1) : null;
            }

            int finalI = i;
            boolean finalHasExist = hasExist;
            ResultBean finalResultBean = resultBean;
            fixedThreadPool.execute(() -> {
                ProblemBean.StatStatusPairsBean problemStatStatus = acProblemList.get(finalI);
                int problemId = problemStatStatus.getStat().getFrontend_question_id();
                String problemTitle = problemStatStatus.getStat().getQuestion__title_slug();
                String formId = problem.formId(totalProblems, problemId);
                String problemDirectory = outputDir + "/" + formId + "." + problemTitle;

                //之前未保存本地的题目进行写入
                if (finalHasExist == false){
                    //创建题目目录
                    createDirectory(problemDirectory);

                    //写入题目
                    try {
                        writeProblem2Disk(problem, problemTitle, problemDirectory);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                //写入代码
                try {
                    writeSubmissions2Disk(problem, problemId, problemTitle, problemDirectory, finalResultBean);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                out.println(formId + "-[" + problemStatStatus.getStat().getQuestion__title() + "] finished");
            });

        }

        fixedThreadPool.shutdown();
        //等待线程池中的所有任务执行结束
        fixedThreadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);

        //写入Markdown
        MarkdownGenerator markdownGenerator = new MarkdownGenerator();
        writeMarkdown2Disk(markdownGenerator.generateMarkdown());

        //写入 result.json
        Gson gson = new Gson();
        List<ResultBean>savedResultList = result.getSavedResultList();
        String savedResultListString = gson.toJson(savedResultList);
        writeResult2Disk(savedResultListString);

        out.println("Crawl Successfully");
    }

    public void createDirectory(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    private void writeUtil(String filePath, String fileContent) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            file.getParentFile().mkdirs();

            file.createNewFile();
        }

        FileOutputStream fos = new FileOutputStream(file);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        bos.write(fileContent.getBytes());

        /*
        OutputStreamWriter writer = new OutputStreamWriter(fos);
        BufferedWriter bufferedWriter = new BufferedWriter(writer);
        bufferedWriter.write(fileContent);
        bufferedWriter.close();
        */
        bos.close();
    }

    private void writeSubmission2Disk(String problemTitle, String problemDirectory, String type, String code) throws IOException {
        writeUtil(problemDirectory + "/" + problemTitle + "." + Util.languageName2FileTypeName(type), code);
    }

    public void writeProblem2Disk(Problem problem, String problemTitle, String problemDirectory) throws IOException {
        String problemDescription = problem.getProblemDescription(problemTitle);
        writeUtil(problemDirectory + "/" + problemTitle + ".md", problemDescription);
    }

    public void writeSubmissions2Disk(Problem problem, int problemId, String problemTitle, String problemDirectory, ResultBean resultBean) throws IOException{
        Map<String, String> submissionMap = problem.getSubmissions(problemTitle, resultBean);
        List<String> submissionLanguageList;
        if (resultBean != null){
            submissionLanguageList = new ArrayList<>(resultBean.getLanguage());
        }else
        {
            submissionLanguageList = new ArrayList<>();
        }
        for (Map.Entry<String, String> entry : submissionMap.entrySet()) {
            writeSubmission2Disk(problemTitle, problemDirectory, entry.getKey(), entry.getValue());
            submissionLanguageList.add(entry.getKey());
        }
        problem.getSubmissionLanguage().put(problemId, submissionLanguageList);
    }

    public void writeMarkdown2Disk(String markdownString) throws IOException {
        writeUtil(outputDir + "/README.md", markdownString);
    }

    public void writeResult2Disk(String savedResultListString) throws IOException {
        writeUtil(outputDir + "/result.json", savedResultListString);
    }
}
