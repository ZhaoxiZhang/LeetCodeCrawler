package bean;

import java.util.List;

public class SubmissionBean {

    /**
     * submissions_dump : [{"id":83896xx,"lang":"cpp","time":"1 年，10 月","status_display":"Accepted","runtime":"12 ms","url":"/submissions/detail/83896xx/","is_pending":"Not Pending","title":"两数之和","timestamp":01939994xx,"memory":"N/A","code":"class Solution {\npublic:\n    vector<int> twoSum(vector<int>& nums, int target) {\n        vector<int>res;\n        map<int,int>mp;\n        for (unsigned int i = 0;i < nums.size();i++){\n            mp[nums[i]] = i;\n        }\n        for (unsigned int i = 0;i < nums.size();i++){\n            if (mp.find(target - nums[i]) != mp.end() && i != mp[target - nums[i]]){\n                res.push_back(i);\n                res.push_back(mp[target-nums[i]]);\n                return res;\n            }\n        }\n    }\n};","compare_result":"1111111111111111111"}]
     * has_next : false
     * last_key : x1dqoqxx
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
         * id : 83896xx
         * lang : cpp
         * time : 1 年，10 月
         * status_display : Accepted
         * runtime : 12 ms
         * url : /submissions/detail/83896xx/
         * is_pending : Not Pending
         * title : 两数之和
         * timestamp : 01939994xx
         * memory : N/A
         * code : class Solution {
         * public:
         * vector<int> twoSum(vector<int>& nums, int target) {
         * vector<int>res;
         * map<int,int>mp;
         * for (unsigned int i = 0;i < nums.size();i++){
         * mp[nums[i]] = i;
         * }
         * for (unsigned int i = 0;i < nums.size();i++){
         * if (mp.find(target - nums[i]) != mp.end() && i != mp[target - nums[i]]){
         * res.push_back(i);
         * res.push_back(mp[target-nums[i]]);
         * return res;
         * }
         * }
         * }
         * };
         * compare_result : 1111111111111111111
         */

        private int id;
        private String lang;
        private String time;
        private String status_display;
        private String runtime;
        private String url;
        private String is_pending;
        private String title;
        private int timestamp;
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

        public int getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(int timestamp) {
            this.timestamp = timestamp;
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
