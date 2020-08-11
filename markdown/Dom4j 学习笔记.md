# `Dom4j` 学习笔记
![u=3401913029,2989789093fm=26gp=0.jpg](https://picker-oss.oss-cn-beijing.aliyuncs.com/20200419/98bd0bcb60a1b3397cf0b13a7c848d04.jpg_target)
参考文档：[【Dom4j】Dom4j完整教程详解](https://blog.csdn.net/qq_41860497/article/details/84339091#1%E3%80%81DOM4J%E7%AE%80%E4%BB%8B)

## `Dom4j` 相关接口类图
![image.png](https://picker-oss.oss-cn-beijing.aliyuncs.com/20200414/d3e2fdd28ea3c79d5b1c9e40153cc6a6.png_target)

## `Maven` 依赖
```xml
   <dependency>
      <groupId>dom4j</groupId>
      <artifactId>dom4j</artifactId>
      <version>1.6.1</version>
   </dependency>
```

## `Demo` 示例
使用比较简单，直接贴上代码：
```java
public class Dom4jTest {

    public static void main(String[] args) {
        //Document document = readXml();
        Document document = parseXmlText();
        Element element = document.getRootElement();

        Element newNode = element.addElement("newNode");
        newNode.add(new DOMCDATA("newNodeValue"));

        List<Element> elements = element.elements();
        elements.stream().forEach(e -> System.out.println(e.getName() + ":" + e.getStringValue()));

        writeToNewXmlFile(document);


    }

    /**
     * 读取 xml 文档
     * @return
     */
    private static Document readXml() {
        SAXReader saxReader = new SAXReader();
        try {
            return saxReader.read(new File("src\\main\\resources\\test.xml"));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void writeToNewXmlFile(Document document)  {
        FileOutputStream fileOutputStream = null;
        XMLWriter xmlWriter = null;
        try {
            String filePath = "src\\main\\resources\\test2.xml";
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            fileOutputStream = new FileOutputStream("src\\main\\resources\\test2.xml");
            xmlWriter = new XMLWriter(new OutputStreamWriter(fileOutputStream, "UTF-8"));
            xmlWriter.write(document);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } finally {
            if (xmlWriter != null) {
                try {
                    xmlWriter.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }
    }

    public static Document parseXmlText() {
        String xml = "<xml>\n" +
                "    <appid><![CDATA[wx2421b1c4370ec43b]]></appid>\n" +
                "    <attach><![CDATA[支付测试]]></attach>\n" +
                "    <bank_type><![CDATA[CFT]]></bank_type>\n" +
                "    <fee_type><![CDATA[CNY]]></fee_type>\n" +
                "    <is_subscribe><![CDATA[Y]]></is_subscribe>\n" +
                "    <mch_id><![CDATA[10000100]]></mch_id>\n" +
                "    <nonce_str><![CDATA[5d2b6c2a8db53831f7eda20af46e531c]]></nonce_str>\n" +
                "    <openid><![CDATA[oUpF8uMEb4qRXf22hE3X68TekukE]]></openid>\n" +
                "    <out_trade_no><![CDATA[1409811653]]></out_trade_no>\n" +
                "    <result_code><![CDATA[SUCCESS]]></result_code>\n" +
                "    <return_code><![CDATA[SUCCESS]]></return_code>\n" +
                "    <sign><![CDATA[B552ED6B279343CB493C5DD0D78AB241]]></sign>\n" +
                "    <time_end><![CDATA[20140903131540]]></time_end>\n" +
                "    <total_fee>1</total_fee>\n" +
                "    <coupon_fee><![CDATA[10]]></coupon_fee>\n" +
                "    <coupon_count><![CDATA[1]]></coupon_count>\n" +
                "    <coupon_type><![CDATA[CASH]]></coupon_type>\n" +
                "    <coupon_id><![CDATA[10000]]></coupon_id>\n" +
                "    <coupon_fee><![CDATA[100]]></coupon_fee>\n" +
                "    <trade_type><![CDATA[JSAPI]]></trade_type>\n" +
                "    <transaction_id><![CDATA[1004400740201409030005092168]]></transaction_id>\n" +
                "</xml>";
        try {
            return DocumentHelper.parseText(xml);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }

}
```
其中使用了一个文件：
* `test.xml`
```xml
<xml>
    <appid><![CDATA[wx2421b1c4370ec43b]]></appid>
    <attach><![CDATA[支付测试]]></attach>
    <bank_type><![CDATA[CFT]]></bank_type>
    <fee_type><![CDATA[CNY]]></fee_type>
    <is_subscribe><![CDATA[Y]]></is_subscribe>
    <mch_id><![CDATA[10000100]]></mch_id>
    <nonce_str><![CDATA[5d2b6c2a8db53831f7eda20af46e531c]]></nonce_str>
    <openid><![CDATA[oUpF8uMEb4qRXf22hE3X68TekukE]]></openid>
    <out_trade_no><![CDATA[1409811653]]></out_trade_no>
    <result_code><![CDATA[SUCCESS]]></result_code>
    <return_code><![CDATA[SUCCESS]]></return_code>
    <sign><![CDATA[B552ED6B279343CB493C5DD0D78AB241]]></sign>
    <time_end><![CDATA[20140903131540]]></time_end>
    <total_fee>1</total_fee>
    <coupon_fee><![CDATA[10]]></coupon_fee>
    <coupon_count><![CDATA[1]]></coupon_count>
    <coupon_type><![CDATA[CASH]]></coupon_type>
    <coupon_id><![CDATA[10000]]></coupon_id>
    <coupon_fee><![CDATA[100]]></coupon_fee>
    <trade_type><![CDATA[JSAPI]]></trade_type>
    <transaction_id><![CDATA[1004400740201409030005092168]]></transaction_id>
</xml>
```