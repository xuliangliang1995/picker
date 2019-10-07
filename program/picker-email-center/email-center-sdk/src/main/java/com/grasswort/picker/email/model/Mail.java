package com.grasswort.picker.email.model;

import lombok.Data;
import org.msgpack.annotation.Message;

import java.util.List;

/**
 * @author xuliangliang
 * @Classname Mail
 * @Description 邮件
 * @Date 2019/10/6 19:45
 * @blame Java Team
 */
@Data
@Message
public class Mail {

    private String subject;

    private String content;

    private boolean html = false;

    private List<String> toAddress;

    private List<String> ccAddress;



}
