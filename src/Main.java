import bean.ConfigBean;
import com.google.gson.Gson;

import java.io.IOException;

import static java.lang.System.out;

public class Main {
    public static void main(String... args) throws IOException {
        Config config = Config.getSingleton();
        String usrname = config.getUsername();
        String passwd = config.getPassword();
        Storage.outputDir = config.getOutputDir();

        Login login = new Login(usrname, passwd);
        Problem problem = Problem.getInstance();
        Storage storage = new Storage();
        login.doLogin();
        storage.write2Disk(problem.getAllAcProblems());
    }
}
