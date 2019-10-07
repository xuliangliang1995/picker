package com.grasswort.picker.commons.email.freemarker;

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
        Configuration configuration = new Configuration();
        //获取class下面的模板文件
        configuration.setDirectoryForTemplateLoading(new File(FreeMarkerUtil.class.getClass().getResource(
                "/"+templatePath).getPath()));
        Template template = configuration.getTemplate(filename,"utf-8");
        StringWriter out = new StringWriter();
        template.process(datas,out);
        return out.toString();
    }
}
