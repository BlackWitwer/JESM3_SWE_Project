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
	
	private HashMap<Integer, MailContainer> messageIdMap;

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

			messageIdMap = new HashMap<Integer, MailContainer>();
			
			for (MailContainer eachMail : backend.getDbManager().getMailContainer()) {
//				messageIdMap.put(eachMail.get, eachMail);
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
		try {
			if (getFolder().getUnreadMessageCount() > 0) {
				getMessagesFromTo(getMessageCount()-getFolder().getUnreadMessageCount(), getMessageCount());
				return true;
			}
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return false;
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
				aListener.mailReceived(getMessagesFromTo(from, to));
			}
		}).start();
	}

	/**
	 * Läd alle Nachrichten aus dem Postfach die sich in dem angegebenen Bereich befinden inklusive der Grenzen. Die Nachrichten
	 * werden in einer Map gespeichert, sollten sie nochmals gebraucht werden. Mit der Ausführung wird auf einen erfolgreichen Login
	 * gewartet.
	 * @param from untere Grenze.
	 * @param to obere Grenze.
	 * @return Liste der Nachrichten innerhalb der Grenzen.
	 */
	public ArrayList<MailContainer> getMessagesFromTo(int from, int to) {
		while (!loggedIn) {
		}
		
		try {
			int temp = to > getFolder().getMessageCount() ? getFolder()
					.getMessageCount() : to;

			ArrayList<MailContainer> theMessageList = new ArrayList<MailContainer>();
			Message theMessage;
			for (int i = from; i <= temp; i++) {
				if (!messageIdMap.containsKey(i)) {
					theMessage  = getFolder().getMessage(i);
					long theUID = idFolder.getUID(theMessage);
					messageIdMap.put(i, new MailContainer(theMessage, theUID));
				}
				theMessageList.add(messageIdMap.get(i));
			}
			Collections.sort(theMessageList, new Comparator<MailContainer>() {

				@Override
				public int compare(MailContainer lhs, MailContainer rhs) {
					return lhs.getOriginalMessage().getMessageNumber() < rhs.getOriginalMessage().getMessageNumber() ? 1 : -1;
				}
			});
			return theMessageList;
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
		return new ArrayList<MailContainer>();
	}

	/**
	 * Gibt die Angegebene Anzahl von Nachrichten zurück. Angefangen bei der Neuesten.
	 * @param anAmount die Anzahl.
	 * @return Liste der Nachrichten.
	 */
	public ArrayList<MailContainer> getLatestMessages(int anAmount) {
		if (anAmount > getMessageCount()) {
			return getMessagesFromTo(1, getMessageCount());
		}
		return getMessagesFromTo(getMessageCount() - anAmount,
				getMessageCount());
	}

	/**
	 * Gibt die Angegebene Anzahl von Nachrichten zurück. Angefangen bei der Neuesten. Wird in einem extra Thread ausgeführt.
	 * Ergebnis wird per Listener zurückgegeben.
	 * @param anAmount die Anzahl.
	 * @return Liste der Nachrichten.
	 */
	public void getLatestMessages(int anAmount, MailListener aListener) {
		while (!loggedIn) {
		}
		
		if (anAmount > getMessageCount()) {
			getMessagesFromTo(1, getMessageCount(), aListener);
		} else {
			getMessagesFromTo(getMessageCount() - anAmount, getMessageCount(),
					aListener);
		}
	}

	public Collection<MailContainer> getCachedMails() {
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
