package com.jesm3.newDualis.mail;

import android.content.*;
import android.preference.*;
import java.util.*;
import javax.mail.*;
import java.io.*;

public class MailManager
{
	private final String prefKey = "oldKeyCount";
	private final String host = "lehre-mail.dhbw-stuttgart.de";
	private final int port = 993;

	private Context context;
	private String username;
	private String password;
	private Session session;
	private Store store;
	private Folder folder;
	private int oldMailCount;
	
	private ArrayList<Message> messages;
	
	public MailManager(Context aContext, String aUsername, String aPassword)
	{
		this.context = aContext;
		this.username = aUsername;
		this.password = aPassword;
		init();
	}

	private void init()
	{
		try
		{
			Properties props = new Properties();
			props.setProperty("mail.imaps.host", getHost());
			props.setProperty("mail.imaps.user", getUsername());
			props.setProperty("mail.imaps.password", getPassword());
			props.setProperty("mail.imaps.auth", "true");
			props.setProperty("mail.imaps.starttls.enable", "true");
			props.setProperty("mail.imaps.socketFactory.port", String.valueOf(getPort()));
			props.setProperty("mail.imaps.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			props.setProperty("mail.imaps.socketFactory.fallback", "false");

			session = Session.getInstance(props, new PassAuthenticator(getUsername(), getPassword()));

			store = session.getStore("imaps");
			store.connect();

			folder = store.getDefaultFolder();
			folder = folder.getFolder("INBOX");

			SharedPreferences thePrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
			if (thePrefs.contains(prefKey)) {
				oldMailCount = thePrefs.getInt(prefKey, 0);
			}
			
			try {
				folder.open(Folder.READ_WRITE);
			} catch (MessagingException ex) {
				folder.open(Folder.READ_ONLY);
			}
			
			messages = new ArrayList<Message>(Arrays.asList(folder.getMessages()));
		}
		catch (Exception ex)
		{
			System.out.println("Oops, got exception! " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	public void sync() {
		if (getMessageCount() > messages.size()) {
			messages.addAll(getMessagesFromTo(messages.size()+1, getMessageCount()));
		}
	}
	
	public ArrayList<Message> getMessagesFromTo(int from, int to)
	{
		try {
			return new ArrayList<Message>(Arrays.asList(getFolder().getMessages(from, to)));
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
	
	public int getNewMessageCount() {
		try {
			return getFolder().getNewMessageCount();
		} catch (MessagingException ex) {

		}
		return -1;
	}

	public int getPort()
	{
		return port;
	}

	public String getHost()
	{
		return host;
	}

	public String getUsername()
	{
		return username;
	}

	private String getPassword()
	{
		return password;
	}
	
	private Store getStore() {
		return store;
	}
	
	private Session getSession() {
		return session;
	}
	
	private Folder getFolder() {
		return folder;
	}
	
	private Context getContext() {
		return context;
	}

	class PassAuthenticator extends Authenticator
	{
		String userName;
		String password;

		public PassAuthenticator(String userName, String password)
		{
			super();
			this.userName = userName;
			this.password = password;
		}

		@Override
		protected PasswordAuthentication getPasswordAuthentication()
		{
			return new PasswordAuthentication(userName, password);
		}
	}
}
