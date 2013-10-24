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

	// public void sync() {
	// if (getMessageCount() > messages.size()) {
	// messages.addAll(getMessagesFromTo(messages.size() + 1,
	// getMessageCount()));
	// }
	// }

	public void getMessagesFromTo(final int from, final int to,
			final MailListener aListener) {
		new Thread(new Runnable() {

			@Override
			public void run() {
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
					aListener.mailReceived(theMessageList);
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public ArrayList<Message> getMessagesFromTo(int from, int to) {
		try {
			return new ArrayList<Message>(Arrays.asList(getFolder()
					.getMessages(from, to)));
		} catch (MessagingException ex) {

		}
		return new ArrayList<Message>();
	}

	public int getMessageCount() {
		try {
			return getFolder().getMessageCount();
		} catch (MessagingException ex) {

		}
		return -1;
	}

	/**
	 * Gibt die Anzahl der ungelesenen Nachrichten zurück.
	 */
	public int getNewMessageCount() {
		try {
			return getFolder().getNewMessageCount();
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

	class PassAuthenticator extends Authenticator {
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
