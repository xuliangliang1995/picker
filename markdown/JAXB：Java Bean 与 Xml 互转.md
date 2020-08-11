# `JAXB Java` 对象与 `xml` 互转
![](https://picker-oss.oss-cn-beijing.aliyuncs.com/20200419/5bf9f727a1d3524021f7089493448e53.jpg_target)

本篇主要演示了两种在 `Java` 对象与 `xml` 之间互相转换的工具使用。个人推荐第一种。

## `JAXBUtil`（推荐）
* 不依赖任何 `jar` 包
```java
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xuliangliang
 * @Classname JAXBUtil.java
 * @Description
 * @Date 2020/4/23
 * @blame Java Team
 */
public class JAXBUtil {

    private final static Map<Class, JAXBContext> CONTEXT_SET = new ConcurrentHashMap<>(32);

    /**
     * java 对象转 xml
     * @param object
     * @return
     */
    public static String marshal(Object object) {
        StringWriter stringWriter = new StringWriter();
        try {
            JAXBUtil.createMarshaller(object.getClass()).marshal(object, stringWriter);
            return stringWriter.toString();
        } catch (JAXBException e) {
            e.printStackTrace();
            throw new MarshallerException(e.getMessage());
        }
    }

    /**
     * xml 转 java 对象
     * @param xml
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T unmarshal(String xml, Class<T> clazz) {
        try {
            return (T) JAXBUtil.createUnmarshaller(clazz).unmarshal(new StringReader(xml));
        } catch (JAXBException e) {
            e.printStackTrace();
            throw new UnmarshallerException(e.getMessage());
        }
    }

    /**
     * Reader 转 java 对象
     * @param reader
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T unmarshal(Reader reader, Class<T> clazz) {
        return JAXBUtil.unmarshal(new InputSource(reader), clazz);
    }

    /**
     * InputStream 转 java 对象
     * @param inputStream
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T unmarshal(InputStream inputStream, Class<T> clazz) {
        return JAXBUtil.unmarshal(new InputSource(inputStream), clazz);
    }
    /**
     * inputSource 转 java 对象
     * @param inputSource
     * @param clazz
     * @param <T>
     * @return
     */
    private static <T> T unmarshal(InputSource inputSource, Class<T> clazz) {
        try {
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            saxParserFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            saxParserFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            saxParserFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", false);
            saxParserFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            Source source = new SAXSource(saxParserFactory.newSAXParser().getXMLReader(), inputSource);
            return (T) JAXBUtil.createUnmarshaller(clazz).unmarshal(source);
        } catch (SAXNotRecognizedException e) {
            e.printStackTrace();
            throw new MarshallerException(e.getMessage());
        } catch (SAXNotSupportedException e) {
            e.printStackTrace();
            throw new MarshallerException(e.getMessage());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            throw new MarshallerException(e.getMessage());
        } catch (SAXException e) {
            e.printStackTrace();
            throw new MarshallerException(e.getMessage());
        } catch (JAXBException e) {
            e.printStackTrace();
            throw new MarshallerException(e.getMessage());
        }
    }

    private static Marshaller createMarshaller(Class clazz) throws JAXBException {
        return JAXBUtil.getJAXBContext(clazz).createMarshaller();
    }

    private static Unmarshaller createUnmarshaller(Class clazz) throws JAXBException {
        return JAXBUtil.getJAXBContext(clazz).createUnmarshaller();
    }

    private static JAXBContext getJAXBContext(Class clazz) throws JAXBException {
        if (CONTEXT_SET.containsKey(clazz)) {
            return CONTEXT_SET.get(clazz);
        }
        CONTEXT_SET.put(clazz, JAXBContext.newInstance(clazz));
        return CONTEXT_SET.get(clazz);
    }

    public static class MarshallerException extends RuntimeException{
        public MarshallerException(String message) {
            super(message);
        }
    }
    public static class UnmarshallerException extends RuntimeException {
        public UnmarshallerException(String message) {
            super(message);
        }
    }

}

```
* `Java` 对象举例
```java
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement("xml")
public class OrderQueryRequestBody {
    /**
     * 微信分配的微信公众平台（或微信开放平台）appid
     */
    @NotBlank
    @Length(max = 32)
    private String appid;

    /**
     * 随机字符串（<= 32 位）
     */
    @NotBlank
    @Length(max = 32)
    @XmlElement(name = "nonce_str")
    private String nonceStr;

    /**
     * 微信的订单号，优先使用
     * 和 out_trade_no 二选一
     */
    @Length(max = 32)
    @XmlElement(name = "transaction_id")
    private String transactionId;
    /**
     * 商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*@ ，且在同一个商户号下唯一。
     */
    @Length(max = 32)
    @XmlElement(name = "out_trade_no")
    private String outTradeNo;

}
```

## `XStreamUtil`
* `Maven` 依赖
```xml
<dependency>
    <groupId>com.thoughtworks.xstream</groupId>
    <artifactId>xstream</artifactId>
    <version>1.4.11.1</version>
</dependency>
```
* `XStreamUtil`
```java
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.naming.NameCoder;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.Xpp3DomDriver;

import java.io.Writer;

/**
 * @author xuliangliang
 * @Classname XStreamUtil.java
 * @Description
 * @Date 2020/4/9
 * @blame Java Team
 */
public class XStreamUtil {
    /**
     * 对象转 xml
     * @param object
     * @return
     */
    public static final String toXml(Object object) {
        return XStreamSingletonHolder.XSTREAM.toXML(object);
    }

    /**
     * xml 转对象
     * @param xml
     * @return
     */
    public static final Object fromXml(String xml) {
        return XStreamSingletonHolder.XSTREAM.fromXML(xml);
    }


    private static final class XStreamSingletonHolder {
        static final XStream XSTREAM;
        static {
            // XStream 解析 Java 对象为 xml 时 _ 会被解析成 __ ，加上这个 nameCoder 就可以解决
            final NameCoder nameCoder = new XmlFriendlyNameCoder("_-", "_");
            XSTREAM = new XStream(new Xpp3DomDriver() {
                @Override
                public HierarchicalStreamWriter createWriter(Writer out) {
                    final String CDATA_PREFIX = "<![CDATA[", CDATA_SUFFIX = "]]>";
                    return new PrettyPrintWriter(out, nameCoder) {
                        @Override
                        protected void writeText(QuickWriter writer, String text) {
                            if (text.startsWith(CDATA_PREFIX) && text.endsWith(CDATA_SUFFIX)) {
                                writer.write(text);
                            } else {
                                writer.write(CDATA_PREFIX + text + CDATA_SUFFIX);
                            }
                        }

                    };
                }
            });
            XSTREAM.autodetectAnnotations(true);
            XSTREAM.ignoreUnknownElements();
        }

    }

}
```
* `Java` 对象举例
```java
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Builder;
import lombok.Data;

/**
 * @author xuliangliang
 * @Classname CloseOrderRequestBody.java
 * @Description
 * @Date 2020/4/22
 * @blame Java Team
 */
@Data
@Builder
@XStreamAlias("xml")
public class CloseOrderRequestBody {
    /**
     * 微信公众平台(或微信开放平台) appid
     */
    private String appid;
    /**
     * 随机字符串，<= 32 位
     */
    @XStreamAlias("nonce_str")
    private String nonceStr;
    /**
     * 商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*@ ，且在同一个商户号下唯一。
     */
    @XStreamAlias("out_trade_no")
    private String outTradeNo;
}
```