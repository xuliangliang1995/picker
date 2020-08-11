# `Staxon Json Xml` 互转
![](https://picker-oss.oss-cn-beijing.aliyuncs.com/20200423/695fc67140e415ffafa06e5963688734.png_target)

## `Maven` 依赖
```xml
<dependency>
    <groupId>de.odysseus.staxon</groupId>
    <artifactId>staxon</artifactId>
    <version>1.3</version>
</dependency>
```
## 代码示例
```java
import de.odysseus.staxon.json.JsonXMLConfig;
import de.odysseus.staxon.json.JsonXMLConfigBuilder;
import de.odysseus.staxon.json.JsonXMLInputFactory;
import de.odysseus.staxon.json.JsonXMLOutputFactory;
import de.odysseus.staxon.xml.util.PrettyXMLEventWriter;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * @author xuliangliang
 * @Classname JsonXmlUtil.java
 * @Description
 * @Date 2020/4/5
 * @blame Java Team
 */
public class JsonXmlUtil {

    public static String json2xml(String json) {
        StringReader input = new StringReader(json);
        StringWriter output = new StringWriter();
        JsonXMLConfig config = new JsonXMLConfigBuilder()
                .multiplePI(false)
                .repairingNamespaces(false).build();
        try {
            XMLEventReader reader = new JsonXMLInputFactory(config)
                    .createXMLEventReader(input);
            XMLEventWriter writer = XMLOutputFactory.newInstance()
                    .createXMLEventWriter(output);
            writer = new PrettyXMLEventWriter(writer);
            writer.add(reader);
            reader.close();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            input.close();
            try {
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return output.toString();
    }


    public static String xml2json(String xml) {
        StringReader input = new StringReader(xml);
        StringWriter output = new StringWriter();
        JsonXMLConfig config = new JsonXMLConfigBuilder().autoArray(true)
                .autoPrimitive(true).prettyPrint(true).build();
        try {
            XMLEventReader reader = XMLInputFactory.newInstance()
                    .createXMLEventReader(input);
            XMLEventWriter writer = new JsonXMLOutputFactory(config)
                    .createXMLEventWriter(output);
            writer.add(reader);
            reader.close();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            input.close();
            try {
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return output.toString();
    }
}

```