public class Util {
    public static String languageName2FileTypeName(String languageName){
        String res;
        switch (languageName){
            case "cpp":
                res = "cpp";
                break;
            case "java":
                res = "java";
                break;
            case "c":
                res = "c";
                break;
            case "csharp":
                res = "cs";
                break;
            case "javascript":
                res = "js";
                break;
            case "python":
                res = "py";
                break;
            case "python3":
                res = "py";
                break;
            case "ruby":
                res = "rb";
                break;
            case "swift":
                res = "swift";
                break;
            case "golang":
                res = "go";
                break;
            case "scala":
                res = "scala";
                break;
            case "kotlin":
                res = "kt";
                break;
            default:
                res = "";
        }
        return res;
    }
}
