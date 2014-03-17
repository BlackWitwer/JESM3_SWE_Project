package com.jesm3.newDualis.generatedDAO;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table ABSTRACT_MAIL_CONTAINER.
 */
public class AbstractMailContainer {

    private Long id;
    private String from;
    private String fromComplete;
    private String to;
    private String subject;
    private String text;
    private java.util.Date date;
    private Boolean attachment;
    private Boolean seen;
    private Boolean html;
    private Integer messageNumber;

    public AbstractMailContainer() {
    }

    public AbstractMailContainer(Long id) {
        this.id = id;
    }

    public AbstractMailContainer(Long id, String from, String fromComplete, String to, String subject, String text, java.util.Date date, Boolean attachment, Boolean seen, Boolean html, Integer messageNumber) {
        this.id = id;
        this.from = from;
        this.fromComplete = fromComplete;
        this.to = to;
        this.subject = subject;
        this.text = text;
        this.date = date;
        this.attachment = attachment;
        this.seen = seen;
        this.html = html;
        this.messageNumber = messageNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFromComplete() {
        return fromComplete;
    }

    public void setFromComplete(String fromComplete) {
        this.fromComplete = fromComplete;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public java.util.Date getDate() {
        return date;
    }

    public void setDate(java.util.Date date) {
        this.date = date;
    }

    public Boolean getAttachment() {
        return attachment;
    }

    public void setAttachment(Boolean attachment) {
        this.attachment = attachment;
    }

    public Boolean getSeen() {
        return seen;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }

    public Boolean getHtml() {
        return html;
    }

    public void setHtml(Boolean html) {
        this.html = html;
    }

    public Integer getMessageNumber() {
        return messageNumber;
    }

    public void setMessageNumber(Integer messageNumber) {
        this.messageNumber = messageNumber;
    }

}
