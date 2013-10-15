package com.jesm3.newDualis.mail;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.TextView;

import com.jesm3.newDualis.R;

public class MailActivity extends Activity
{
	static String indentStr = "                                               ";
	static int level = 0;


	static String protocol = "imap";
	static String host = "lehre-mail.dhbw-stuttgart.de";
	static String user = "it12184@lehre.dhbw-stuttgart.de";
	static String password = "aurumopesest1";
	static String mbox = null;
	static String url = null;
	static int port = 993;
	static boolean verbose = false;
	static boolean debug = false;
	static boolean showStructure = false;
	static boolean showMessage = false;
	static boolean showAlert = false;
	static boolean saveAttachments = false;
	static int attnum = 1;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

		//Nur zu Testzwecken. Unterbindet eine Sicherung die es nicht erlaubt im Interface Thread Netzwerkaktivitäten zu verwenden.
		StrictMode.ThreadPolicy policy = new StrictMode.
			ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		//-----------------------------------
		
		
		TextView text = ((TextView)findViewById(R.id.text));
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
			Properties props = new Properties();
			props.setProperty("mail.imaps.host", posteingang_host);
			props.setProperty("mail.imaps.user", benutzerName);
			props.setProperty("mail.imaps.password", password);
			props.setProperty("mail.imaps.auth", "true");
			props.setProperty("mail.imaps.starttls.enable", "true");
			props.setProperty("mail.imaps.socketFactory.port", String.valueOf(993));
			props.setProperty("mail.imaps.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			props.setProperty("mail.imaps.socketFactory.fallback", "false");
			
			long time = System.currentTimeMillis();
			
//			// Get a Session object
			Session session = Session.getInstance(props, new PassAuthenticator(benutzerName, password));
			session.setDebug(debug);
			
//			// Get a Store object
			Store store = null;
			store = session.getStore("imaps");
			store.connect();

//			// Open the Folder

			Folder folder = store.getDefaultFolder();
			if (folder == null)
			{
				System.out.println("No default folder");
				log("No default folder");
			}

			if (mbox == null)
				mbox = "INBOX";
			folder = folder.getFolder(mbox);
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
			text.append("Total Messages: " + totalMessages);
			text.append("Check");
			if (totalMessages == 0)
			{
				System.out.println("Empty folder");
				log("Empty folder");
				folder.close(false);
				store.close();
			}
			for (Message eachMessage : folder.getMessages()) {
				text.append(eachMessage.getSubject()+"\n");
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
