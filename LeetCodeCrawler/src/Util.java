public class Util {

    /**
     * 生成格式化的字符串ID
     * 例如total = 100， id = 1， 则result = 001
     * total = 1000， id = 1，则 result = 0001
     *
     * @param total
     * @param id
     * @return 格式化的字符串ID
     */
    public static String formId(int total, int id) {
        int digitCntTotal = (int) Math.log10(total);
        int digitCntId = (int) Math.log10(id);
        int needDigitCnt = digitCntTotal - digitCntId;

        StringBuilder res = new StringBuilder(digitCntTotal);
        while (needDigitCnt-- > 0) {
            res.append(0);
        }
        res.append(id);
        return res.toString();
    }

    public static String languageName2FileTypeName(String languageName) {
        String res;
        switch (languageName) {
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
