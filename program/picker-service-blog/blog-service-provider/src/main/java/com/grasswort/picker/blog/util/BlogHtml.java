package com.grasswort.picker.blog.util;

import com.grasswort.picker.commons.email.freemarker.FreeMarkerUtil;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

    @Override
    public String toString() {
        Map<String, String> data = new HashMap<>();
        data.put("theme", theme);
        data.put("content", content);
        try {
            return FreeMarkerUtil.getMailTextForTemplate("emailTemplate", "blog.html", data);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
        return null;
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
