package bean;

import java.util.List;

public class SubmissionBean {
    /**
     * submissions_dump : [{"id":1916359**,"lang":"java","time":"6 months, 1 week","timestamp":15435320**,"status_display":"Accepted","runtime":"4 ms","url":"/submissions/detail/1917359**","is_pending":"Not Pending","title":"Two Sum","memory":"N/A","code":"class Solution {\n    public int[] twoSum(int[] nums, int target) {\n        int[] res = new int[2];\n        Map<Integer,Integer>map = new HashMap<>(nums.length);\n        for (int i = 0; i < nums.length; i++)   map.put(nums[i], i);\n        for (int i = 0; i < nums.length; i++){\n            if (map.get(target - nums[i]) != null && i != map.get(target - nums[i])){\n                res[0] = map.get(target - nums[i]);\n                res[1] = i;\n                break;\n            }\n        }\n        return res;\n    }\n}","compare_result":"11111111111111111111111111111"}]
     * has_next : true
     * last_key : %7D%7D
     */

    private boolean has_next;
    private String last_key;
    private List<SubmissionsDumpBean> submissions_dump;

    public boolean isHas_next() {
        return has_next;
    }

    public void setHas_next(boolean has_next) {
        this.has_next = has_next;
    }

    public String getLast_key() {
        return last_key;
    }

    public void setLast_key(String last_key) {
        this.last_key = last_key;
    }

    public List<SubmissionsDumpBean> getSubmissions_dump() {
        return submissions_dump;
    }

    public void setSubmissions_dump(List<SubmissionsDumpBean> submissions_dump) {
        this.submissions_dump = submissions_dump;
    }

    public static class SubmissionsDumpBean {
        /**
         * id : 1916359**
         * lang : java
         * time : 6 months, 1 week
         * timestamp : 15435320**
         * status_display : Accepted
         * runtime : 4 ms
         * url : /submissions/detail/1917359**
         * is_pending : Not Pending
         * title : Two Sum
         * memory : N/A
         * code : class Solution {
             public int[] twoSum(int[] nums, int target) {
                int[] res = new int[2];
                Map<Integer,Integer>map = new HashMap<>(nums.length);
                for (int i = 0; i < nums.length; i++)   map.put(nums[i], i);
                for (int i = 0; i < nums.length; i++){
                    if (map.get(target - nums[i]) != null && i != map.get(target - nums[i])){
                        res[0] = map.get(target - nums[i]);
                        res[1] = i;
                        break;
                    }
                }
                return res;
             }
         }
         * compare_result : 11111111111111111111111111111
         */

        private int id;
        private String lang;
        private String time;
        private int timestamp;
        private String status_display;
        private String runtime;
        private String url;
        private String is_pending;
        private String title;
        private String memory;
        private String code;
        private String compare_result;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getLang() {
            return lang;
        }

        public void setLang(String lang) {
            this.lang = lang;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public int getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(int timestamp) {
            this.timestamp = timestamp;
        }

        public String getStatus_display() {
            return status_display;
        }

        public void setStatus_display(String status_display) {
            this.status_display = status_display;
        }

        public String getRuntime() {
            return runtime;
        }

        public void setRuntime(String runtime) {
            this.runtime = runtime;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getIs_pending() {
            return is_pending;
        }

        public void setIs_pending(String is_pending) {
            this.is_pending = is_pending;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getMemory() {
            return memory;
        }

        public void setMemory(String memory) {
            this.memory = memory;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getCompare_result() {
            return compare_result;
        }

        public void setCompare_result(String compare_result) {
            this.compare_result = compare_result;
        }
    }
}
