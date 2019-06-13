import bean.ProblemBean;
import bean.ResultBean;
import com.google.gson.Gson;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.lang.System.out;

public class Storage {

    public static String outputDir = ".";

    private static volatile Storage instance;
    private Problem problemInstance;
    private Result resultInstance;
    private MarkdownGenerator markdownGeneratorInstance;

    private Storage() {
        problemInstance = Problem.getSingleton();
        resultInstance = Result.getSingleton();
        markdownGeneratorInstance = MarkdownGenerator.getSingleton();
    }

    public static Storage getSingleton() {
        Storage result = instance;
        if (result == null) {
            synchronized (Storage.class) {
                result = instance;
                if (result == null) {
                    result = instance = new Storage();
                }
            }
        }
        return result;
    }

    public void write2Disk(List<ProblemBean.StatStatusPairsBean> acProblemList) throws IOException, InterruptedException {
        List<ResultBean> restoredProblemList = resultInstance.getRestoredResultList();

        //按题目 Id 从小到大排序
        acProblemList.sort(Comparator.comparingInt(o -> o.getStat().getFrontend_question_id()));
        //按题目 Id 从小到大排序
        restoredProblemList.sort((Comparator.comparingInt(ResultBean::getFrontend_id)));

        int fakeTotalNumProblem = problemInstance.getFakeTotalNumProblem();
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(2 * availableProcessors);

        int restoredProblemIndex = 0;
        for (ProblemBean.StatStatusPairsBean statStatusPairsBean : acProblemList) {
            boolean hasExisted = false;
            ResultBean resultBean = null;
            if (restoredProblemIndex < restoredProblemList.size()) {
                int restoredProblemFrontedId = restoredProblemList.get(restoredProblemIndex).getFrontend_id();
                int acProblemFrontedId = statStatusPairsBean.getStat().getFrontend_question_id();

                if (restoredProblemFrontedId == acProblemFrontedId) {
                    hasExisted = true;
                    restoredProblemIndex++;
                }

                resultBean = hasExisted ? restoredProblemList.get(restoredProblemIndex - 1) : null;
            }

            ProblemBean.StatStatusPairsBean.StatBean statBean = statStatusPairsBean.getStat();
            int problemFrontedId = statBean.getFrontend_question_id();
            String problemTitleSlug = statBean.getQuestion__title_slug();
            String formFrontedId = Util.formId(fakeTotalNumProblem, problemFrontedId);
            String problemDirectory = outputDir + "/" + formFrontedId + "." + problemTitleSlug;

            boolean finalHasExisted = hasExisted;
            ResultBean finalResultBean = resultBean;
            fixedThreadPool.execute(() -> {

                if (!finalHasExisted) {
                    createDirectory(problemDirectory);

                    try {
                        writeProblemDescription2Disk(statBean, problemDirectory, problemTitleSlug);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    statBean.setQuestion__translated_title(finalResultBean.getTranslatedTitle());
                    statBean.setQuestion__topics_tags(finalResultBean.getTopicTags());
                    problemInstance.putData2TopicsMap(finalResultBean.getTopicTags(), statBean);
                }

                try {
                    writeSubmissions2Disk(problemDirectory, problemFrontedId, problemTitleSlug, finalResultBean);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                out.println(formFrontedId + "-[" + statBean.getQuestion__title() + "] finished");
            });
        }

        fixedThreadPool.shutdown();
        //等待线程池中的所有任务执行结束
        fixedThreadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);

        //写入Markdown
        writeMarkdown2Disk("Topics", markdownGeneratorInstance.generateTopics());
        writeMarkdown2Disk("README", markdownGeneratorInstance.generateREADME());

        //写入 result.json
        Gson gson = new Gson();
        List<ResultBean> savedResultList = resultInstance.getSavedResultList();
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

    public void writeProblemDescription2Disk(ProblemBean.StatStatusPairsBean.StatBean statBean, String problemDirectory, String problemTitleSlug) throws IOException {
        String problemDescription = problemInstance.getProblemDescription(statBean);
        writeUtil(problemDirectory + "/" + problemTitleSlug + ".md", problemDescription);
    }

    public void writeSubmission2Disk(String problemDirectory, String problemTitleSlug, String code, String type) throws IOException {
        writeUtil(problemDirectory + "/" + problemTitleSlug + "." + Util.languageName2FileTypeName(type), code);
    }

    public void writeSubmissions2Disk(String problemDirectory, int problemFrontedId, String problemTitleSlug, ResultBean resultBean) throws IOException {
        List<String> submissionLanguageList;
        if (resultBean != null) {
            submissionLanguageList = new ArrayList<>(resultBean.getLanguage());
        } else {
            submissionLanguageList = new ArrayList<>();
        }

        Map<String, String> submissionMap = problemInstance.getSubmissions(problemTitleSlug, resultBean);
        for (Map.Entry<String, String> entry : submissionMap.entrySet()) {
            writeSubmission2Disk(problemDirectory, problemTitleSlug, entry.getValue(), entry.getKey());
            submissionLanguageList.add(entry.getKey());
        }

        problemInstance.putData2SubmissionLanguageMap(problemFrontedId, submissionLanguageList);
    }

    public void writeMarkdown2Disk(String fileName, String markdownContent) throws IOException {
        writeUtil(outputDir + "/" + fileName + ".md", markdownContent);
    }

    public void writeResult2Disk(String savedResultListString) throws IOException {
        writeUtil(outputDir + "/result.json", savedResultListString);
    }
}
