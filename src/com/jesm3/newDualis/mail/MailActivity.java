package com.jesm3.newDualis.mail;

import android.app.*;
import android.os.*;
import android.util.*;
import android.widget.*;
import com.jesm3.newDualis.*;
import com.jesm3.newDualis.is.*;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

import javax.mail.Message;

public class MailActivity extends Activity
{

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main);

		//Nur zu Testzwecken. Unterbindet eine Sicherung die es nicht erlaubt im Interface Thread Netzwerkaktivitäten zu verwenden.
		StrictMode.ThreadPolicy policy = new StrictMode.
			ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		//-----------------------------------
		
		MailManager m = new MailManager(new User("it12126@lehre.dhbw-stuttgart.de", "Yy9m7NRc"));
//		TextView text = ((TextView)findViewById(R.id.text));
//		try {
//			for (Message eachMessage : m.getMessagesFromTo(1, 20)) {
//				text.append(eachMessage.getSubject() + "\n");
//			}
//		} catch (MessagingException ex) {
//			
//		}
//		receive();
	}
	
	public void send() {
//		TextView text = ((TextView)findViewById(R.id.text));
		// Recipient's email ID needs to be mentioned.
			String to = "basti.wahl92@gmail.com";

		// Sender's email ID needs to be mentioned
		String from = "it12126@lehre.dhbw-stuttgart.com";

		String posteingang_host = "lehre-mail.dhbw-stuttgart.de";
		String benutzerName = "it12126@lehre.dhbw-stuttgart.de";
		String password = "";
		int port = 587;

		Properties props = new Properties();
		props.setProperty("mail.smtp.host", posteingang_host);
		props.setProperty("mail.smtp.user", benutzerName);
		props.setProperty("mail.smtp.password", password);
		props.setProperty("mail.smtp.auth", "true");
		props.setProperty("mail.smtp..port", String.valueOf(port));
		
		// Get the default Session object.
		Session session = Session.getDefaultInstance(props, new PassAuthenticator(benutzerName, password));

		try{
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO,
								 new InternetAddress(to));

			// Set Subject: header field
			message.setSubject("This is the Subject Line!");

			// Now set the actual message
//			message.setText("This is actual message");

			// Send message
			Transport.send(message);
//			text.append("Sent message");
			System.out.println("Sent message successfully....");
		}catch (MessagingException mex) {
			mex.printStackTrace();
			log(mex.toString());
		}
	}

	public void receive() {
//		TextView text = ((TextView)findViewById(R.id.text));
		/*
		 * Demo app that exercises the Message interfaces.
		 * Show information about and contents of messages.
		 *
		 * @author John Mani
		 * @author Bill Shannon
		 */

		try
		{
//			// Get a Properties object

			String posteingang_host = "lehre-mail.dhbw-stuttgart.de";
			String benutzerName = "it12126@lehre.dhbw-stuttgart.de";
			String password = "Yy9m7NRc";
			int port = 993;

			Properties props = new Properties();
			props.setProperty("mail.imaps.host", posteingang_host);
			props.setProperty("mail.imaps.user", benutzerName);
			props.setProperty("mail.imaps.password", password);
			props.setProperty("mail.imaps.auth", "true");
			props.setProperty("mail.imaps.starttls.enable", "true");
			props.setProperty("mail.imaps.socketFactory.port", String.valueOf(port));
			props.setProperty("mail.imaps.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			props.setProperty("mail.imaps.socketFactory.fallback", "false");

			long time = System.currentTimeMillis();

//			// Get a Session object
			Session session = Session.getInstance(props, new PassAuthenticator(benutzerName, password));

//			// Get a Store object
			Store store = null;
			store = session.getStore("imaps");
			store.connect();

//			// Open the Folder

			Folder folder = store.getDefaultFolder();
			folder = folder.getFolder("INBOX");
			if (folder == null)
			{
				System.out.println("Invalid folder");
				log("Invalid folder");
			}

//			// try to open read/write and if that fails try read-only
			try
			{
				folder.open(Folder.READ_WRITE);
			}
			catch (MessagingException ex) 
			{
				log(ex.toString());
				folder.open(Folder.READ_ONLY);
			}
			int totalMessages = folder.getMessageCount();
//			text.append("Total Messages: " + totalMessages);
//			text.append("Check");
			if (totalMessages == 0)
			{
				System.out.println("Empty folder");
				log("Empty folder");
				folder.close(false);
				store.close();
			}
			for (Message eachMessage : folder.getMessages()) {
//				text.append(eachMessage.getSubject()+"\n");
			}
			folder.close(false);
			store.close();
		}
		catch (Exception ex)
		{
			System.out.println("Oops, got exception! " + ex.getMessage());
			log(ex.toString());
			ex.printStackTrace();
		}
////		System.exit(0);
	}
	
	public void log(String message) {
		Log.w("MailTest", message);
	}
	
	class PassAuthenticator extends Authenticator  {
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
