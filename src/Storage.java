import bean.ProblemBean;

import java.io.*;
import java.util.List;
import java.util.Map;

import static java.lang.System.out;

public class Storage {
    public static String outputDir;


    public void write2Disk(List<ProblemBean.StatStatusPairsBean>acProblemList) throws IOException {
        for (int i = 0; i < acProblemList.size(); i++){
            Problem problem = Problem.getInstance();
            ProblemBean.StatStatusPairsBean problemStatStatus = acProblemList.get(i);
            int totalProblems = problem.getAllProblems().size();
            int problemId = problemStatStatus.getStat().getQuestion_id();
            String problemTitle = problemStatStatus.getStat().getQuestion__title_slug();
            String problemDirectory = outputDir + "/" + problem.formId(totalProblems, problemId) + "." + problemTitle;
            //创建题目目录
            createDirectory(problemDirectory);

            //写入题目
            writeProblem2Disk(problem, problemTitle, problemDirectory);

            //写入代码
            writeSubmissions2Disk(problem, problemTitle, problemDirectory);
        }
    }

    public void createDirectory(String path){
        File file = new File(path);
        if (!file.exists()){
            file.mkdirs();
        }
    }

    private void writeUtil(String filePath, String fileContent) throws IOException {
        File file = new File(filePath);
        if (!file.exists()){
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(file);
        OutputStreamWriter writer = new OutputStreamWriter(fos);
        BufferedWriter bufferedWriter = new BufferedWriter(writer);
        bufferedWriter.write(fileContent);
        bufferedWriter.close();
    }

    private void writeSubmission2Disk(String problemTitle, String problemDirectory, String type, String code) throws IOException {
        writeUtil(problemDirectory + "/" + problemTitle + "." + type, code);
    }

    public void writeProblem2Disk(Problem problem, String problemTitle, String problemDirectory) throws IOException {
        String problemDescription = problem.getProblemDescription(problemTitle);
        writeUtil(problemDirectory + "/" + problemTitle + ".md", problemDescription);
    }

    public void writeSubmissions2Disk(Problem problem, String problemTitle, String problemDirectory) throws IOException {
        Map<String, String>submissionMap = problem.getSubmissions(problemTitle);
        for (Map.Entry<String, String>entry : submissionMap.entrySet()){
            writeSubmission2Disk(problemTitle, problemDirectory, entry.getKey(), entry.getValue());
        }
    }
}
