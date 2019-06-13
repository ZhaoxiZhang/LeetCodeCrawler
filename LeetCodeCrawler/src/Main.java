import java.io.IOException;

public class Main {
    public static boolean isDebug = false;

    public static void main(String... args) throws IOException, InterruptedException {
        Problem problem = Problem.getSingleton();
        Storage storage = Storage.getSingleton();
        Config config = Config.getSingleton();
        String usrname = config.getUsername();
        String passwd = config.getPassword();
        Storage.outputDir = config.getOutputDir();

        Login login = new Login(usrname, passwd);

        if (login.doLogin()) {
            storage.write2Disk(problem.getAllAcProblems());
        }
    }
}
