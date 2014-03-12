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

import com.jesm3.newDualis.generatedDAO.AbstractMailContainer;

public class MailContainer extends AbstractMailContainer {
	
	private ArrayList<Part> attachmentList;
	
	private Message originalMessage;
	
	public MailContainer(Message anOriginalMessage) throws MessagingException, IOException {
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
		setAttachmntList(getAttachments(anOriginalMessage));
		setAttachment(!getAttachmentList().isEmpty());
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
			setHtml(p.isMimeType("text/html"));
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
	
	public String getDeltaTime() {
		return convertDate(getDate());
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
	private void setAttachmntList(ArrayList<Part> attachmentList) {
		this.attachmentList = attachmentList;
	}

	/**
	 * @return the text
	 */
	@Override
	public String getText() {
		if (super.getText() == null) {
			try {
				setText(getText(getOriginalMessage()));
			} catch (MessagingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			//TODO Update auf der Datenbank ausf�hren, damit der Text persistiert wird.
		}
		return super.getText();
	}
}
