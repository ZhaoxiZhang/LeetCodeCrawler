package bean;

import java.util.List;

public class ProblemBean {
    /**
     * user_name : xxx
     * num_solved : xxx
     * num_total : xxx
     * ac_easy : xx
     * ac_medium : xx
     * ac_hard : xx
     * stat_status_pairs : [{"stat":{"question_id":981,"question__article__live":true,"question__article__slug":"delete-columns-to-make-sorted","question__title":"Delete Columns to Make Sorted","question__title_slug":"delete-columns-to-make-sorted","question__hide":false,"total_acs":2215,"total_submitted":3025,"frontend_question_id":944,"is_new_question":true},"status":null,"difficulty":{"level":2},"paid_only":false,"is_favor":false,"frequency":0,"progress":0}]
     * frequency_high : 0
     * frequency_mid : 0
     * category_slug : all
     */

    private String user_name;
    private int num_solved;
    private int num_total;
    private int ac_easy;
    private int ac_medium;
    private int ac_hard;
    private int frequency_high;
    private int frequency_mid;
    private String category_slug;
    private List<StatStatusPairsBean> stat_status_pairs;

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int getNum_solved() {
        return num_solved;
    }

    public void setNum_solved(int num_solved) {
        this.num_solved = num_solved;
    }

    public int getNum_total() {
        return num_total;
    }

    public void setNum_total(int num_total) {
        this.num_total = num_total;
    }

    public int getAc_easy() {
        return ac_easy;
    }

    public void setAc_easy(int ac_easy) {
        this.ac_easy = ac_easy;
    }

    public int getAc_medium() {
        return ac_medium;
    }

    public void setAc_medium(int ac_medium) {
        this.ac_medium = ac_medium;
    }

    public int getAc_hard() {
        return ac_hard;
    }

    public void setAc_hard(int ac_hard) {
        this.ac_hard = ac_hard;
    }

    public int getFrequency_high() {
        return frequency_high;
    }

    public void setFrequency_high(int frequency_high) {
        this.frequency_high = frequency_high;
    }

    public int getFrequency_mid() {
        return frequency_mid;
    }

    public void setFrequency_mid(int frequency_mid) {
        this.frequency_mid = frequency_mid;
    }

    public String getCategory_slug() {
        return category_slug;
    }

    public void setCategory_slug(String category_slug) {
        this.category_slug = category_slug;
    }

    public List<StatStatusPairsBean> getStat_status_pairs() {
        return stat_status_pairs;
    }

    public void setStat_status_pairs(List<StatStatusPairsBean> stat_status_pairs) {
        this.stat_status_pairs = stat_status_pairs;
    }

    public static class StatStatusPairsBean {
        /**
         * stat : {"question_id":981,"question__article__live":true,"question__article__slug":"delete-columns-to-make-sorted","question__title":"Delete Columns to Make Sorted","question__title_slug":"delete-columns-to-make-sorted","question__hide":false,"total_acs":2215,"total_submitted":3025,"frontend_question_id":944,"is_new_question":true}
         * status : null
         * difficulty : {"level":2}
         * paid_only : false
         * is_favor : false
         * frequency : 0
         * progress : 0
         */

        private StatBean stat;
        private String status;
        private DifficultyBean difficulty;
        private boolean paid_only;
        private boolean is_favor;
        private int frequency;
        private int progress;

        public StatBean getStat() {
            return stat;
        }

        public void setStat(StatBean stat) {
            this.stat = stat;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public DifficultyBean getDifficulty() {
            return difficulty;
        }

        public void setDifficulty(DifficultyBean difficulty) {
            this.difficulty = difficulty;
        }

        public boolean isPaid_only() {
            return paid_only;
        }

        public void setPaid_only(boolean paid_only) {
            this.paid_only = paid_only;
        }

        public boolean isIs_favor() {
            return is_favor;
        }

        public void setIs_favor(boolean is_favor) {
            this.is_favor = is_favor;
        }

        public int getFrequency() {
            return frequency;
        }

        public void setFrequency(int frequency) {
            this.frequency = frequency;
        }

        public int getProgress() {
            return progress;
        }

        public void setProgress(int progress) {
            this.progress = progress;
        }

        public static class StatBean {
            /**
             * question_id : 981
             * question__article__live : true
             * question__article__slug : delete-columns-to-make-sorted
             * question__title : Delete Columns to Make Sorted
             * question__title_slug : delete-columns-to-make-sorted
             * question__hide : false
             * total_acs : 2215
             * total_submitted : 3025
             * frontend_question_id : 944
             * is_new_question : true
             */

            private int question_id;
            private boolean question__article__live;
            private String question__article__slug;
            private String question__title;
            private String question__title_slug;
            private boolean question__hide;
            private int total_acs;
            private int total_submitted;
            private int frontend_question_id;
            private boolean is_new_question;

            public int getQuestion_id() {
                return question_id;
            }

            public void setQuestion_id(int question_id) {
                this.question_id = question_id;
            }

            public boolean isQuestion__article__live() {
                return question__article__live;
            }

            public void setQuestion__article__live(boolean question__article__live) {
                this.question__article__live = question__article__live;
            }

            public String getQuestion__article__slug() {
                return question__article__slug;
            }

            public void setQuestion__article__slug(String question__article__slug) {
                this.question__article__slug = question__article__slug;
            }

            public String getQuestion__title() {
                return question__title;
            }

            public void setQuestion__title(String question__title) {
                this.question__title = question__title;
            }

            public String getQuestion__title_slug() {
                return question__title_slug;
            }

            public void setQuestion__title_slug(String question__title_slug) {
                this.question__title_slug = question__title_slug;
            }

            public boolean isQuestion__hide() {
                return question__hide;
            }

            public void setQuestion__hide(boolean question__hide) {
                this.question__hide = question__hide;
            }

            public int getTotal_acs() {
                return total_acs;
            }

            public void setTotal_acs(int total_acs) {
                this.total_acs = total_acs;
            }

            public int getTotal_submitted() {
                return total_submitted;
            }

            public void setTotal_submitted(int total_submitted) {
                this.total_submitted = total_submitted;
            }

            public int getFrontend_question_id() {
                return frontend_question_id;
            }

            public void setFrontend_question_id(int frontend_question_id) {
                this.frontend_question_id = frontend_question_id;
            }

            public boolean isIs_new_question() {
                return is_new_question;
            }

            public void setIs_new_question(boolean is_new_question) {
                this.is_new_question = is_new_question;
            }
        }

        public static class DifficultyBean {
            /**
             * level : 2
             */

            private int level;

            public int getLevel() {
                return level;
            }

            public void setLevel(int level) {
                this.level = level;
            }
        }
    }
}
