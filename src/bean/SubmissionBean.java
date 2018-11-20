package bean;

import java.util.List;

public class SubmissionBean {
    /**
     * submissions_dump : [{"id":1125365**,"lang":"cpp","time":"1 year, 3 months","timestamp":1501939994,"status_display":"Accepted","runtime":"12 ms","url":"/submissions/detail/1125365**","is_pending":"Not Pending","title":""}]
     * has_next : false
     * last_key :
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
         * id : 1125365**
         * lang : cpp
         * time : 1 year, 3 months
         * timestamp : 1501939994
         * status_display : Accepted
         * runtime : 12 ms
         * url : /submissions/detail/1125365**
         * is_pending : Not Pending
         * title :
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
    }
}
