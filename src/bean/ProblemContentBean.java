package bean;

public class ProblemContentBean {
    /**
     * data : {"question":{"content":"<p>Given an array of integers, return <strong>indices<\/strong> of the two numbers such that they add up to a specific target.<\/p>\r\n\r\n<p>You may assume that each input would have <strong><em>exactly<\/em><\/strong> one solution, and you may not use the <em>same<\/em> element twice.<\/p>\r\n\r\n<p><strong>Example:<\/strong><\/p>\r\n\r\n<pre>\r\nGiven nums = [2, 7, 11, 15], target = 9,\r\n\r\nBecause nums[<strong>0<\/strong>] + nums[<strong>1<\/strong>] = 2 + 7 = 9,\r\nreturn [<strong>0<\/strong>, <strong>1<\/strong>].\r\n<\/pre>\r\n\r\n<p>&nbsp;<\/p>\r\n"}}
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
         * question : {"content":"<p>Given an array of integers, return <strong>indices<\/strong> of the two numbers such that they add up to a specific target.<\/p>\r\n\r\n<p>You may assume that each input would have <strong><em>exactly<\/em><\/strong> one solution, and you may not use the <em>same<\/em> element twice.<\/p>\r\n\r\n<p><strong>Example:<\/strong><\/p>\r\n\r\n<pre>\r\nGiven nums = [2, 7, 11, 15], target = 9,\r\n\r\nBecause nums[<strong>0<\/strong>] + nums[<strong>1<\/strong>] = 2 + 7 = 9,\r\nreturn [<strong>0<\/strong>, <strong>1<\/strong>].\r\n<\/pre>\r\n\r\n<p>&nbsp;<\/p>\r\n"}
         */

        private QuestionBean question;

        public QuestionBean getQuestion() {
            return question;
        }

        public void setQuestion(QuestionBean question) {
            this.question = question;
        }

        public static class QuestionBean {
            /**
             * content : <p>Given an array of integers, return <strong>indices</strong> of the two numbers such that they add up to a specific target.</p>

             <p>You may assume that each input would have <strong><em>exactly</em></strong> one solution, and you may not use the <em>same</em> element twice.</p>

             <p><strong>Example:</strong></p>

             <pre>
             Given nums = [2, 7, 11, 15], target = 9,

             Because nums[<strong>0</strong>] + nums[<strong>1</strong>] = 2 + 7 = 9,
             return [<strong>0</strong>, <strong>1</strong>].
             </pre>

             <p>&nbsp;</p>

             */

            private String content;

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }
        }
    }
}
