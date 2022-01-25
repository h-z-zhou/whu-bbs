package com.wuda.bbs.utils.xmlHandler;

import com.wuda.bbs.bean.MailContentResponse;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class MailContentHandler extends DefaultHandler {

    MailContentResponse mailContentResponse;
    StringBuilder mailContent;
    String nodeName;

    public MailContentHandler() {
        mailContentResponse = new MailContentResponse();
        mailContent = new StringBuilder();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        nodeName = localName;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        if (nodeName.equals("Mail")) {
            mailContent.append(ch, start, length);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if (localName.equals("Mail")) {
            mailContentResponse.setMailContent(beautifyContent(mailContent.toString()));
        }
    }

    public MailContentResponse getMailContentResponse() {
        return mailContentResponse;
    }

    private String beautifyContent(String content){
        // prints('\n\n
        content = content.substring(12, content.length()-3);
        content = content.replaceAll("\\\\n", "");
        content = content.replaceAll("\\\\r.*?m", "\n");
        return content;
//        return content.trim();
    }
}
