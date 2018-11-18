import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String... args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String usrname = scanner.nextLine();
        String passwd = scanner.nextLine();
        Storage.baseDirPath = "./Problems";
        Login login = new Login(usrname, passwd);
        Problem problem = new Problem();
        Storage storage = new Storage();
        login.doLogin();
        storage.mkdir(problem.getAllAcProblemsName());
    }
}
