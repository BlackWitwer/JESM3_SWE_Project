package com.jesm3.newDualis.mail;

import com.jesm3.newDualis.is.*;

import java.io.*;
import java.util.*;

import javax.mail.*;


public class MailManager {
	private final String host = "lehre-mail.dhbw-stuttgart.de";
	private final int port = 993;

	private User user;
	private Folder folder;
	private Backend backend;
	private UIDFolder idFolder;
	
	private HashMap<Long, MailContainer> messageIdMap;

	private boolean loggedIn = false;

	public MailManager(Backend aBackend, User aUser) {
		this.user = aUser;
		this.backend = aBackend;
		// Login im Thread um ein hängen zu vermeiden.
		new Thread(new Runnable() {

			@Override
			public void run() {
				init();
				loggedIn = true;
			}
		}).start();
	}

	private void init() {
		messageIdMap = new HashMap<Long, MailContainer>();
		for (MailContainer eachMail : backend.getDbManager().getMailContainer()) {
			messageIdMap.put(eachMail.getUId(), eachMail);
		}
		
		try {
			Properties props = new Properties();
			props.setProperty("mail.imaps.host", getHost());
			props.setProperty("mail.imaps.user", getUsername());
			props.setProperty("mail.imaps.password", getPassword());
			props.setProperty("mail.imaps.auth", "true");
			props.setProperty("mail.imaps.starttls.enable", "true");
			props.setProperty("mail.imaps.socketFactory.port",
					String.valueOf(getPort()));
			props.setProperty("mail.imaps.socketFactory.class",
					"javax.net.ssl.SSLSocketFactory");
			props.setProperty("mail.imaps.socketFactory.fallback", "false");

			Session session = Session.getInstance(props, new PassAuthenticator(
					getUsername(), getPassword()));

			Store store = session.getStore("imaps");
			store.connect();

			folder = store.getDefaultFolder();
			folder = folder.getFolder("INBOX");

			idFolder = (UIDFolder) folder;
			
			try {
				folder.open(Folder.READ_WRITE);
			} catch (MessagingException ex) {
				folder.open(Folder.READ_ONLY);
			}

		} catch (Exception ex) {
			System.out.println("Oops, got exception! " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	/**
	 * Sucht nach ungelesenen Nachrichten. Sind welche vorhanden werden so viele Nachrichten geladen wir ungelesene vorhanden sind.
	 * Damit werden auf jedenfall die neuesten ungelesenen geladen.
	 * @return true, wenn ungelesenen Nachrichten vorhanden sind. Ansonsten false.
	 */
	public boolean sync() {
		long theHighKey = 0;
		for (Map.Entry<Long, MailContainer> eachEntry : messageIdMap.entrySet()) {
			if (eachEntry.getKey() > theHighKey) {
				theHighKey = eachEntry.getKey();
			}
		}
		//HighKey+1 ist die erste Nachricht, die nach der neuesten geladenen Nachricht kommen müsste.
		return getMessagesFromTo(theHighKey, UIDFolder.LASTUID).isEmpty();
	}

	/**
	 * Benutzt die getMessagesFromTo Methode in einem Thread und gibt bei Fertigstellung die Mails per Listener zurück.
	 * @param from untere Grenze.
	 * @param to obere Grenze.
	 * @param aListener Rückgabe Listener.
	 */
	public void getMessagesFromTo(final int from, final int to,
			final MailListener aListener) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				getMessagesFromTo(from, to);
				aListener.mailReceived();
			}
		}).start();
	}

	/**
	 * Läd alle Nachrichten aus dem Postfach die sich in dem angegebenen Bereich befinden inklusive der Grenzen. Die Nachrichten
	 * werden in einer Map gespeichert, sollten sie nochmals gebraucht werden. Mit der Ausführung wird auf einen erfolgreichen Login
	 * gewartet.
	 * @param aFrom untere Grenze.
	 * @param to obere Grenze.
	 * @return Liste der Nachrichten innerhalb der Grenzen.
	 */
	public ArrayList<MailContainer> getMessagesFromTo(long aFrom, long to) {
		while (!loggedIn) {
		}
		
		try {
//			long temp = to > getFolder().getMessageCount() ? getFolder()
//					.getMessageCount() : to;

			ArrayList<MailContainer> theMessageList = new ArrayList<MailContainer>();
			for (Message eachMessage : getIdFolder().getMessagesByUID(aFrom, to)) {
				theMessageList.add(getMessage(eachMessage));
			}
			return theMessageList;
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
		return new ArrayList<MailContainer>();
	}
	
	public boolean refreshCache() {
		try {
			boolean theRefreshFlag = false;
			List<Long> theDelKeyList = new ArrayList<Long>();
			Message[] theMessages;
			for (long eachKey : messageIdMap.keySet()) {
				theMessages = getIdFolder().getMessagesByUID(eachKey, eachKey);
				if (theMessages == null || theMessages.length == 0) {
					theDelKeyList.add(eachKey);
					theRefreshFlag = true;
					
					//Aus der Datenbank löschen falls die Mail gespeichert wurde.
					if (messageIdMap.get(eachKey).getId() > 0) {
						backend.getDbManager().deleteMailContainer(messageIdMap.get(eachKey));
					}
				}
			}
			for (long eachKey : theDelKeyList) {
				messageIdMap.remove(eachKey);
			}
				
			return sync() || theRefreshFlag;
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return false;
	}

	public MailContainer loadOriginalMessage(MailContainer aMail) throws MessagingException {
		if (aMail.getOriginalMessage() == null) {
			aMail.setOriginalMessage(getIdFolder().getMessageByUID(aMail.getUId()));
		}
		return aMail;
	}
	
	public MailContainer getMessage(long aMessageUId) throws MessagingException {
		if (messageIdMap.containsKey(aMessageUId)) {
			return messageIdMap.get(aMessageUId);
		}
		return getMessage(getIdFolder().getMessageByUID(aMessageUId));
	}
	
	public MailContainer getMessage(Message aMessage) {
		while (!loggedIn) {
		}
		
		if (aMessage == null) {
			return null;
		}
		
		try {
			MailContainer theMail = null;
			long theUid = getIdFolder().getUID(aMessage);
			if (!messageIdMap.containsKey(theUid)) {
				theMail = new MailContainer(aMessage, theUid);
				messageIdMap.put(theUid, theMail);
				
				int theMessageCount = getFolder().getMessageCount();
				if (theMail.getMessageNumber() > theMessageCount - 10) {
					backend.getDbManager().insertMailContainer(theMail);
					if (messageIdMap.containsKey(theMessageCount-10)
						&& messageIdMap.get(theMessageCount-10).getId() > 0) {
						backend.getDbManager().deleteMailContainer(messageIdMap.get(theMessageCount-10));
					}
				}
			} else if (messageIdMap.get(theUid).getOriginalMessage() == null) {
				messageIdMap.get(theUid).setOriginalMessage(aMessage);
			}
			return messageIdMap.get(theUid);
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Gibt die Angegebene Anzahl von Nachrichten zurück. Angefangen bei der Neuesten.
	 * @param anAmount die Anzahl.
	 * @return Liste der Nachrichten.
	 */
//	public ArrayList<MailContainer> getLatestMessages(int anAmount) {
//		if (anAmount > getMessageCount()) {
//			return getMessagesFromTo(1, getMessageCount());
//		}
//		return getMessagesFromTo(getMessageCount() - anAmount,
//				getMessageCount());
//	}

	/**
	 * Gibt die Angegebene Anzahl von Nachrichten zurück. Angefangen bei der Neuesten. Wird in einem extra Thread ausgeführt.
	 * Ergebnis wird per Listener zurückgegeben.
	 * @param anAmount die Anzahl.
	 * @return Liste der Nachrichten.
	 * @throws MessagingException Bei Mail Fehler.
	 */
	public void getLatestMessages(int anAmount, MailListener aListener) throws MessagingException {
		while (!loggedIn) {
		}
		
		MailContainer theMail = getMessage(getFolder().getMessage(getFolder().getMessageCount()));
		if (theMail == null) {
			return;
		}
		
		long theId = theMail.getUId();
		MailContainer theOldMail = null;
		for (int i = anAmount; i > 0; i--) {
			theOldMail = null;
			while (theOldMail == null && theId > 0) {
				theOldMail = getMessage(theId--);
			}
		}
		aListener.mailReceived();
	}
	
	public Collection<MailContainer> getCachedMails() {
		while (!loggedIn) {
		}
		return messageIdMap.values();
	}
	
	public int getMessageCount() {
		try {
			return getFolder().getMessageCount();
		} catch (MessagingException ex) {

		}
		return -1;
	}

	private int getPort() {
		return port;
	}

	private String getHost() {
		return host;
	}

	private String getUsername() {
		return user.getUsername();
	}

	private String getPassword() {
		return user.getPassword();
	}

	private Folder getFolder() {
		return folder;
	}
	
	private UIDFolder getIdFolder() {
		return idFolder;
	}

	private class PassAuthenticator extends Authenticator {
		String userName;
		String password;

		public PassAuthenticator(String userName, String password) {
			super();
			this.userName = userName;
			this.password = password;
		}

		@Override
		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(userName, password);
		}
	}
}
