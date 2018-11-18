import java.io.File;
import java.util.List;

import static java.lang.System.out;

public class Storage {
    public static String baseDirPath;

    public void mkdir(List<String>problemNameList){
        for (int i = 0; i < problemNameList.size(); i++){
            out.println(problemNameList.get(i));
            File file = new File(baseDirPath + "/" + problemNameList.get(i));
            if (!file.exists()){
                file.mkdirs();
            }
        }
    }
}
