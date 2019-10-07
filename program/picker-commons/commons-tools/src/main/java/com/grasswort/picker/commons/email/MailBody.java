package com.grasswort.picker.commons.email;

import lombok.Data;

import javax.mail.Address;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.stream.Collectors;

/**
 * @author xuliangliang
 * @Classname MailBody
 * @Description 邮件相关信息
 * @Date 2019/10/6 9:41
 * @blame Java Team
 */
@Data
public class MailBody {
    /**
     * 发送地址
     */
    private List<String> toAddress = Collections.emptyList();
    /**
     * 抄送地址
     */
    private List<String> ccAddress = Collections.emptyList();
    /**
     * 主题
     */
    private String subject;
    /**
     * 邮件文本内容
     */
    private String content;
    /**
     * 邮件附件的文件名
     */
    private Vector<String> attachFileNames = new Vector<>();
    /**
     * 发送 html 时指定的 contentType
     */
    private String contentType = "text/html;charset=utf-8";
    /**
     * 邮件模板文件名
     */
    private String templateName;
    /**
     * 邮件模板需要替换的数据
     */
    private Map<String, Object> dataMap;

    /**
     * 获取收件人地址
     * @return
     */
    public Address[] getToInternetAddress() {
        List<InternetAddress> addressList = toAddress.stream()
                .map(address -> {
                    try {
                        return new InternetAddress(address);
                    } catch (AddressException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .filter(address -> address != null)
                .collect(Collectors.toList());
        Address[] addresses = new Address[addressList.size()];
        return addressList.toArray(addresses);
    }

    /**
     * 获取抄送地址
     * @return
     */
    public Address[] getCcInternetAddress() {
        List<InternetAddress> addressList = ccAddress.stream()
                .map(address -> {
                    try {
                        return new InternetAddress(address);
                    } catch (AddressException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .filter(address -> address != null)
                .collect(Collectors.toList());

        Address[] addresses = new Address[addressList.size()];
        return addressList.toArray(addresses);
    }
}
