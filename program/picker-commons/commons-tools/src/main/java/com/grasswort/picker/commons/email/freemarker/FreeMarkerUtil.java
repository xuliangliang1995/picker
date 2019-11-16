package com.grasswort.picker.commons.email.freemarker;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

/**
 * @author xuliangliang
 * @Classname FreeMarkerUtil
 * @Description TODO
 * @Date 2019/10/6 14:30
 * @blame Java Team
 */
public class FreeMarkerUtil {

    public static String getMailTextForTemplate(String templatePath, String filename, Map datas) throws IOException, TemplateException {
        Configuration configuration = Singleton.configuration;
        //获取class下面的模板文件
        File path = new File(FreeMarkerUtil.class.getClass().getResource(
                "/"+templatePath).getPath());
        System.out.println(path.getPath());
        configuration.setDirectoryForTemplateLoading(path);
        Template template = configuration.getTemplate(filename,"utf-8");
        StringWriter out = new StringWriter();
        template.process(datas,out);
        return out.toString();
    }

    public static String getMailTextFromText(String templateKey, String templateContent, long templateLastModified, Map datas) throws IOException, TemplateException {
        Configuration configuration = Singleton.configuration;
        StringTemplateLoader stringTemplateLoader = Singleton.stringTemplateLoader;
        configuration.setTemplateLoader(stringTemplateLoader);
        stringTemplateLoader.putTemplate(templateKey, templateContent, templateLastModified);
        Template template = configuration.getTemplate(templateKey, "UTF-8");
        StringWriter out = new StringWriter();
        template.process(datas,out);
        return out.toString();
    }


    /**
     * 单例
     */
    private static class Singleton {
        static Configuration configuration = new Configuration();
        static StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
    }

}
