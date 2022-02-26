package com.wuda.bbs.utils.xmlHandler;

import com.wuda.bbs.logic.bean.response.ContentResponse;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MailContentHandler extends DefaultHandler {

    ContentResponse<String> mailContentResponse;
    StringBuilder mailContent;
    String nodeName;

    public MailContentHandler() {
        mailContentResponse = new ContentResponse<>();
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
            mailContentResponse.setContent(beautifyContent(mailContent.toString()));
        }
    }

    public ContentResponse<String> getMailContentResponse() {
        return mailContentResponse;
    }

    private String beautifyContent(String content){
        Pattern pattern = Pattern.compile("(?<=\\\\n\\\\n)(.*?)(?=\\\\n'\\);)");
        Matcher matcher = pattern.matcher(content);

        if (matcher.find()) {
            content = matcher.group();
        }
        content = content.replaceAll("\\\\n", "");
        content = content.replaceAll("\\\\r.*?m", "\n");

        return content.trim();
    }
}
