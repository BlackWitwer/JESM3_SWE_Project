package com.jesm3.newDualis.mail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Flags.Flag;
import javax.mail.internet.InternetAddress;

public class MessageContainer {
	
	private String from;
	private String fromComplete;
	private String to;
	private String subject;
	private Date date;
	private String text;
	private boolean attachement;
	private boolean seen;
	
	private boolean textIsHtml = false;
	private ArrayList<Part> attachmentList;
	
	private Message originalMessage;
	
	public MessageContainer(Message anOriginalMessage) throws MessagingException, IOException {
		String thePersonal = null;
		if (anOriginalMessage.getFrom()[0] instanceof InternetAddress) {
			thePersonal = ((InternetAddress) anOriginalMessage.getFrom()[0]).getPersonal();
			setFrom(thePersonal);
		}
		if (thePersonal == null || thePersonal.equals("")) {
			setFrom(anOriginalMessage.getFrom()[0].toString());
		}
		
		setSubject(anOriginalMessage.getSubject());
		setDate(anOriginalMessage.getReceivedDate());
		setAttachmentList(getAttachments(anOriginalMessage));
		setAttachement(!getAttachmentList().isEmpty());
		setSeen(anOriginalMessage.getFlags().contains(Flag.SEEN));
		setFromComplete(anOriginalMessage.getFrom()[0].toString());
		setTo(anOriginalMessage.getAllRecipients()[0].toString());
		
		originalMessage = anOriginalMessage;
	}
	
	/**
	 * TODO SEW nochmal überarbeiten die Anhänge werden nicht korrekt erkannt.
	 * @param aPart
	 * @return
	 * @throws MessagingException
	 * @throws IOException
	 */
	private ArrayList<Part> getAttachments(Part aPart) throws MessagingException, IOException {
		ArrayList<Part> attachments = new ArrayList<Part>();

		if (aPart.isMimeType("multipart/*")) {
			Multipart mp = (Multipart) aPart.getContent();
			for (int i = 0; i < mp.getCount(); i++) {
				attachments.addAll(getAttachments(mp.getBodyPart(i)));
			}
		} else if (aPart.isMimeType("application/*")) {
			attachments.add(aPart);
		}

		return attachments;
	}
	
	public String convertDate(Date aDate) {
		long theTimeDelta = System.currentTimeMillis() - aDate.getTime();
		
		long theMinutes = TimeUnit.MILLISECONDS.toMinutes(theTimeDelta);
		long theHours = TimeUnit.MILLISECONDS.toHours(theTimeDelta);
		long theDays = TimeUnit.MILLISECONDS.toDays(theTimeDelta);
		
		if (theMinutes < 0) {
			return "< 1h";
		} else if (theHours <= 0) {
			return "< 1h";
		} else if (theDays <= 0) {
			return theHours + " h";
		} else if (theDays / 7 <= 0){
			return theDays + " d";
		} else if (theDays / 30 <= 0) {
			//Wochen
			return ((int) (theDays / 7)) + " w";
		} else if (theDays / 365 <= 0) {
			//Monate
			return ((int) (theDays / 30)) + " m";
		} else {
			//Jahre
			return ((int) (theDays / 365)) + " y";
		}
	}
	
	/**
	 * Return the primary text content of the message.
	 */
	private String getText(Part p) throws MessagingException, IOException {
		if (p.isMimeType("text/*")) {
			String s = (String) p.getContent();
			textIsHtml = p.isMimeType("text/html");
			return s;
		}

		if (p.isMimeType("multipart/alternative")) {
			// prefer html text over plain text
			Multipart mp = (Multipart) p.getContent();
			String text = null;
			for (int i = 0; i < mp.getCount(); i++) {
				Part bp = mp.getBodyPart(i);
				if (bp.isMimeType("text/plain")) {
					if (text == null)
						text = getText(bp);
					continue;
				} else if (bp.isMimeType("text/html")) {
					String s = getText(bp);
					if (s != null)
						return s;
				} else {
					return getText(bp);
				}
			}
			return text;
		} else if (p.isMimeType("multipart/*")) {
			Multipart mp = (Multipart) p.getContent();
			for (int i = 0; i < mp.getCount(); i++) {
				String s = getText(mp.getBodyPart(i));
				if (s != null)
					return s;
			}
		}
		return "";
	}
	
	/**
	 * @return the originalMessage
	 */
	public Message getOriginalMessage() {
		return originalMessage;
	}
	
	/**
	 * @return seen
	 */
	public boolean isSeen() {
		return seen;
	}
	
	/**
	 * @param seen the seen to set
	 */
	private void setSeen(boolean seen) {
		this.seen = seen;
	}
	
	/**
	 * @return the from
	 */
	public String getFrom() {
		return from;
	}
	/**
	 * @param name the name to set
	 */
	private void setFrom(String from) {
		this.from = from;
	}
	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}
	/**
	 * @param subject the subject to set
	 */
	private void setSubject(String subject) {
		this.subject = subject;
	}
	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}
	/**
	 * @param date the date to set
	 */
	private void setDate(Date date) {
		this.date = date;
	}
	public String getDeltaTime() {
		return convertDate(getDate());
	}
	/**
	 * @return the attachement
	 */
	public boolean hasAttachement() {
		return attachement;
	}
	/**
	 * @param attachement the attachement to set
	 */
	private void setAttachement(boolean attachement) {
		this.attachement = attachement;
	}

	/**
	 * @return the textIsHtml
	 */
	public boolean isTextIsHtml() {
		return textIsHtml;
	}

	/**
	 * @return the attachmentList
	 */
	public ArrayList<Part> getAttachmentList() {
		return attachmentList;
	}

	/**
	 * @param attachmentList the attachmentList to set
	 */
	private void setAttachmentList(ArrayList<Part> attachmentList) {
		this.attachmentList = attachmentList;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		if (text == null) {
			try {
				text = getText(getOriginalMessage());
			} catch (MessagingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return text;
	}

	/**
	 * @return the attachement
	 */
	public boolean isAttachement() {
		return attachement;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}
	
	public String getFromComplete() {
		return fromComplete;
	}
	
	public String getTo() {
		return to;
	}

	/**
	 * @param fromComplete the fromComplete to set
	 */
	private void setFromComplete(String fromComplete) {
		this.fromComplete = fromComplete;
	}

	/**
	 * @param to the to to set
	 */
	private void setTo(String to) {
		this.to = to;
	}
}
