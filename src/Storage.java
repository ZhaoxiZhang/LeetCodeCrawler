import bean.ProblemBean;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.lang.System.out;

public class Storage {
    public static String outputDir = "./Problems";


    public void write2Disk(List<ProblemBean.StatStatusPairsBean> acProblemList) throws IOException, InterruptedException {
        Problem problem = Problem.getInstance();
        int totalProblems = problem.getAllProblems().size();
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(2 * availableProcessors);
        for (int i = 0; i < acProblemList.size(); i++) {
            int finalI = i;
            fixedThreadPool.execute(() -> {
                ProblemBean.StatStatusPairsBean problemStatStatus = acProblemList.get(finalI);
                int problemId = problemStatStatus.getStat().getQuestion_id();
                String problemTitle = problemStatStatus.getStat().getQuestion__title_slug();
                String problemDirectory = outputDir + "/" + problem.formId(totalProblems, problemId) + "." + problemTitle;

                //创建题目目录
                createDirectory(problemDirectory);

                //写入题目
                try {
                    writeProblem2Disk(problem, problemTitle, problemDirectory);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //写入代码
                try {
                    writeSubmissions2Disk(problem, problemId, problemTitle, problemDirectory);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        }

        fixedThreadPool.shutdown();
        //等待线程池中的所有任务执行结束
        fixedThreadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);

        //写入Markdown
        MarkdownGenerator markdownGenerator = new MarkdownGenerator();
        writeMarkdown2Disk(markdownGenerator.generateMarkdown());
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
        writeUtil(problemDirectory + "/" + problemTitle + "." + type, code);
    }

    public void writeProblem2Disk(Problem problem, String problemTitle, String problemDirectory) throws IOException {
        String problemDescription = problem.getProblemDescription(problemTitle);
        writeUtil(problemDirectory + "/" + problemTitle + ".md", problemDescription);
    }

    public void writeSubmissions2Disk(Problem problem, int problemId, String problemTitle, String problemDirectory) throws IOException{
        Map<String, String> submissionMap = problem.getSubmissions(problemTitle);
        List<String> submissionLanguageList = new ArrayList<>();
        for (Map.Entry<String, String> entry : submissionMap.entrySet()) {
            writeSubmission2Disk(problemTitle, problemDirectory, entry.getKey(), entry.getValue());
            submissionLanguageList.add(entry.getKey());
        }
        problem.getSubmissionLanguage().put(problemId, submissionLanguageList);
    }

    public void writeMarkdown2Disk(String markdownString) throws IOException {
        writeUtil(outputDir + "/README.md", markdownString);
    }
}
