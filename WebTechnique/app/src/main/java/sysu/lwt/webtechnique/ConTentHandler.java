package sysu.lwt.webtechnique;


import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by 12136 on 2017/4/2.
 */

public class ConTentHandler extends DefaultHandler {

    private String nodeName;
    private StringBuilder name;

    // 在开始XML解析时调用
    @Override
    public void startDocument() {
        name = new StringBuilder();
    }

    // 在开始解析某个节点时调用
    @Override
    public void startElement(String uri, String localName, String qNmae, Attributes attributes) {
        // 记录当前节点名
        nodeName = localName;
    }

    // 在获取节点中内容时调用
    @Override
    public void characters(char[] ch, int start, int length) {
        // 根据当前节点名判断将内容添加到那个StringBuilder中，这里只有一个StringBuilder
        if ("string".equals(nodeName)) {
            name.append(ch, start, length);
        }
    }

    // 在完成解析某个节点的时候调用
    @Override
    public void endElement(String uri, String localName, String qNmae) {
        // 将StringBuilder清空
        name.setLength(0);
    }

    // 在完成整个XML解析时调用
    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }
}
