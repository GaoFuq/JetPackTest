package com.gfq.jetpacktest;

import java.util.List;

public class News {

    /**
     * code : 200
     * message : 成功!
     * result : [{"path":"https://news.163.com/19/0811/07/EM9IGFVB0001899N.html","image":"http://cms-bucket.ws.126.net/2019/08/11/ae8a97031a0043f3b7cf091e52877f95.png?imageView&thumbnail=140y88&quality=85","title":"外卖哥顶台风送餐 一路手掏排水口双手泡发白","passtime":"2019-08-11 10:00:46"}]
     */

    private int code;
    private String message;
    private List<ResultBean> result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * path : https://news.163.com/19/0811/07/EM9IGFVB0001899N.html
         * image : http://cms-bucket.ws.126.net/2019/08/11/ae8a97031a0043f3b7cf091e52877f95.png?imageView&thumbnail=140y88&quality=85
         * title : 外卖哥顶台风送餐 一路手掏排水口双手泡发白
         * passtime : 2019-08-11 10:00:46
         */

        private String path;
        private String image;
        private String title;
        private String passtime;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPasstime() {
            return passtime;
        }

        public void setPasstime(String passtime) {
            this.passtime = passtime;
        }
    }
}
