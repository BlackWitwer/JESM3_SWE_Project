package com.jesm3.newDualis.mail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;

import com.jesm3.newDualis.is.User;

public class MailManager {
	private final String host = "lehre-mail.dhbw-stuttgart.de";
	private final int port = 993;

	private User user;
	private Folder folder;
	
	private HashMap<Integer, Message> messageIdMap;

	private boolean loggedIn = false;

	public MailManager(User aUser) {
		this.user = aUser;

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

			try {
				folder.open(Folder.READ_WRITE);
			} catch (MessagingException ex) {
				folder.open(Folder.READ_ONLY);
			}

			messageIdMap = new HashMap<Integer, Message>();
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
	public ArrayList<Message> getMessagesFromTo(int from, int to) {
		while (!loggedIn) {
		}
		
		try {
			int temp = to > getFolder().getMessageCount() ? getFolder()
					.getMessageCount() : to;

			ArrayList<Message> theMessageList = new ArrayList<Message>();
			for (int i = from; i <= temp; i++) {
				if (!messageIdMap.containsKey(i)) {
					messageIdMap.put(i, getFolder().getMessage(i));
				}
				theMessageList.add(messageIdMap.get(i));
			}
			return theMessageList;
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return new ArrayList<Message>();
	}

	/**
	 * Gibt die Angegebene Anzahl von Nachrichten zurück. Angefangen bei der Neuesten.
	 * @param anAmount die Anzahl.
	 * @return Liste der Nachrichten.
	 */
	public ArrayList<Message> getLatestMessages(int anAmount) {
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
		if (anAmount > getMessageCount()) {
			getMessagesFromTo(1, getMessageCount(), aListener);
		} else {
			getMessagesFromTo(getMessageCount() - anAmount, getMessageCount(),
					aListener);
		}
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
