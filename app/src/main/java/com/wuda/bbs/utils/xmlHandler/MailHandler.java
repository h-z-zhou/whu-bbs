package com.wuda.bbs.utils.xmlHandler;

import com.wuda.bbs.logic.bean.Mail;
import com.wuda.bbs.logic.bean.response.ContentResponse;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class MailHandler extends DefaultHandler {
    ContentResponse<List<Mail>> mailListResponse1;
    Mail mail;
    StringBuilder subject;
    String nodeName;

    public MailHandler() {
        mailListResponse1 = new ContentResponse<>();
        mailListResponse1.setContent(new ArrayList<>());
        subject = new StringBuilder();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        nodeName = localName;
        switch (localName) {
            case "Mails":
                int currentPage = Integer.parseInt(attributes.getValue("Page"));
                int totalPage = Integer.parseInt(attributes.getValue("TotalPage"));
                mailListResponse1.setCurrentPage(currentPage);
                mailListResponse1.setTotalPage(totalPage);
                break;
            case "Mail":
                mail = new Mail();
                String num = attributes.getValue("num");
                String sender = attributes.getValue("sender");
                String isNew = attributes.getValue("isnew");
                String time = attributes.getValue("time");
                mail.setNum(num);
                mail.setSender(sender);
                mail.setNew(isNew.equals(""));
                mail.setTime(time);
                break;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        if (nodeName.equals("Mail")) {
            subject.append(ch, start, length);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if (localName.equals("Mail")) {
            mail.setSubject(subject.toString());
            subject.setLength(0);
            mailListResponse1.getContent().add(mail);
        }
    }

    public ContentResponse<List<Mail>> getMailResponse() {
        return mailListResponse1;
    }
}
