import bean.ResultBean;
import com.google.gson.Gson;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Result {
    private static volatile Result instance;
    //从 result.json 中恢复结果
    private List<ResultBean> restoredResultList;
    //记录本次爬取的结果保存到本地
    private List<ResultBean> savedResultList;

    private Result(){
        savedResultList = new ArrayList<>();
    }

    public static Result getSingleton(){
        Result result = instance;
        if (result == null){
            synchronized (Result.class){
                result = instance;
                if (result == null){
                    result = instance = new Result();
                }
            }
        }
        return result;
    }

    //TODO 初始 result.json 文件不存在进行检查
    public List<ResultBean> getRestoredResultList() throws IOException {
        if (restoredResultList == null){
            File file = new File(Storage.outputDir + "/result.json");
            if (!file.exists()){
                file.getParentFile().mkdirs();
                file.createNewFile();
            }

            FileInputStream fis = new FileInputStream(file);
            InputStreamReader reader = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(reader);

            Gson gson = new Gson();
            ResultBean[] res = gson.fromJson(bufferedReader, ResultBean[].class);
            restoredResultList = Arrays.asList(res == null ? new ResultBean[0] : res);
        }

        return restoredResultList;
    }

    public void addElement2SavedResultList(ResultBean resultBean){
        savedResultList.add(resultBean);
    }

    public List<ResultBean> getSavedResultList(){
        return savedResultList;
    }

}
