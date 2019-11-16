package com.grasswort.picker.blog.util;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author xuliangliang
 * @Classname BlogHtmlTest
 * @Description TODO
 * @Date 2019/11/16 17:39
 * @blame Java Team
 */
public class BlogHtmlTest {

    @Test
    public void toHtml() {
        System.out.println(BlogHtml.Builder.aBlogHtml()
                .withTheme("github")
                .withContent("哈哈")
                .build()
                .toHtml());;
    }
}