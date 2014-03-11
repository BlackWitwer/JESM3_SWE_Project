package com.jesm3.newDualis.mail;

import java.util.List;

import javax.mail.Message;

public abstract class MailListener {
	public abstract void mailReceived(List<Message> someMails);
}
