package com.grasswort.picker.blog.util;

/**
 * @author xuliangliang
 * @Classname BlogHtmlBuilder
 * @Description 博客页面生成
 * @Date 2019/11/16 15:06
 * @blame Java Team
 */
public class BlogHtml {

    private String theme;

    private String content;

    public String toHtml() {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"utf-8\">\n" +
                "    <title>picker</title>\n" +
                "    <link rel=\"stylesheet\" type=\"text/css\" href=\"https://picker-oss.oss-cn-beijing.aliyuncs.com/meta/css/");
        sb.append(theme);
        sb.append(".min.css\">\n" +
                "</head>\n" +
                "  <style>\n" +
                "    body {\n" +
                "        background-color: #f7f2f2;\n" +
                "    }\n" +
                "    #app {\n" +
                "      font-size: 14px;\n" +
                "      margin: 10px;\n" +
                "    }\n" +
                "    img {" +
                "        width: 90%;" +
                "    }" +
                "  </style>" +
                "<body>\n" +
                "<div id=\"app\">");
        sb.append(content);
        sb.append("</div>\n" +
                "</body>\n" +
                "</html>");
        return sb.toString();
    }

    public static final class Builder {
        public String theme;
        public String content;

        private Builder() {
        }

        public static Builder aBlogHtml() {
            return new Builder();
        }

        public Builder withTheme(String theme) {
            this.theme = theme;
            return this;
        }

        public Builder withContent(String content) {
            this.content = content;
            return this;
        }

        public BlogHtml build() {
            BlogHtml blogHtml = new BlogHtml();
            blogHtml.theme = this.theme;
            blogHtml.content = this.content;
            return blogHtml;
        }
    }
}
