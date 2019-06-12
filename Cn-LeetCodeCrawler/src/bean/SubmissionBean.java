package bean;

import java.util.List;

public class SubmissionBean {

    /**
     * data : {"submissionList":{"lastKey":"x1dqqdxx","hasNext":false,"submissions":[{"id":"33896xx","statusDisplay":"Accepted","lang":"cpp","runtime":"12 ms","timestamp":"15939994xx","url":"/submissions/detail/33896xx/","isPending":"Not Pending","memory":"N/A"}]}}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * submissionList : {"lastKey":"x1dqqdxx","hasNext":false,"submissions":[{"id":"33896xx","statusDisplay":"Accepted","lang":"cpp","runtime":"12 ms","timestamp":"15939994xx","url":"/submissions/detail/33896xx/","isPending":"Not Pending","memory":"N/A"}]}
         */

        private SubmissionListBean submissionList;

        public SubmissionListBean getSubmissionList() {
            return submissionList;
        }

        public void setSubmissionList(SubmissionListBean submissionList) {
            this.submissionList = submissionList;
        }

        public static class SubmissionListBean {
            /**
             * lastKey : x1dqqdxx
             * hasNext : false
             * submissions : [{"id":"33896xx","statusDisplay":"Accepted","lang":"cpp","runtime":"12 ms","timestamp":"15939994xx","url":"/submissions/detail/33896xx/","isPending":"Not Pending","memory":"N/A"}]
             */

            private String lastKey;
            private boolean hasNext;
            private List<SubmissionsBean> submissions;

            public String getLastKey() {
                return lastKey;
            }

            public void setLastKey(String lastKey) {
                this.lastKey = lastKey;
            }

            public boolean isHasNext() {
                return hasNext;
            }

            public void setHasNext(boolean hasNext) {
                this.hasNext = hasNext;
            }

            public List<SubmissionsBean> getSubmissions() {
                return submissions;
            }

            public void setSubmissions(List<SubmissionsBean> submissions) {
                this.submissions = submissions;
            }

            public static class SubmissionsBean {
                /**
                 * id : 33896xx
                 * statusDisplay : Accepted
                 * lang : cpp
                 * runtime : 12 ms
                 * timestamp : 15939994xx
                 * url : /submissions/detail/33896xx/
                 * isPending : Not Pending
                 * memory : N/A
                 */

                private String id;
                private String statusDisplay;
                private String lang;
                private String runtime;
                private String timestamp;
                private String url;
                private String isPending;
                private String memory;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getStatusDisplay() {
                    return statusDisplay;
                }

                public void setStatusDisplay(String statusDisplay) {
                    this.statusDisplay = statusDisplay;
                }

                public String getLang() {
                    return lang;
                }

                public void setLang(String lang) {
                    this.lang = lang;
                }

                public String getRuntime() {
                    return runtime;
                }

                public void setRuntime(String runtime) {
                    this.runtime = runtime;
                }

                public String getTimestamp() {
                    return timestamp;
                }

                public void setTimestamp(String timestamp) {
                    this.timestamp = timestamp;
                }

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

                public String getIsPending() {
                    return isPending;
                }

                public void setIsPending(String isPending) {
                    this.isPending = isPending;
                }

                public String getMemory() {
                    return memory;
                }

                public void setMemory(String memory) {
                    this.memory = memory;
                }
            }
        }
    }
}
