import java.io.IOException;

public class Main {
    public static boolean isDebug = false;

    public static void main(String... args) throws IOException, InterruptedException {
        Config config = Config.getSingleton();
        String usrname = config.getUsername();
        String passwd = config.getPassword();
        Storage.outputDir = config.getOutputDir();

        Login login = new Login(usrname, passwd);
        Problem problem = Problem.getSingleton();
        Storage storage = new Storage();

        if (login.doLogin()){
            storage.write2Disk(problem.getAllAcProblems());
        }

    }
}
