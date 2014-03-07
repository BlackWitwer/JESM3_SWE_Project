package com.jesm3.newDualis.is;

import android.app.Application;
import android.content.Intent;

import com.jesm3.newDualis.activities.LoginActivity;
import com.jesm3.newDualis.activities.MainActivity;
import com.jesm3.newDualis.mail.MailManager;

public class CustomApplication extends Application {

	private Backend backend;
	private UserManager userManager;
	private MailManager mailManager;

	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	public Backend getBackend() {
		if (backend == null) {
			backend = new Backend();
		}
		return backend;
	}
	
	public UserManager getUserManager() {
		if (userManager == null) {
			userManager = new UserManager(this);
		}
		return userManager;
	}
	
	public MailManager getMailManager() {
		if (mailManager == null) {
			mailManager = new MailManager(getUserManager().getUser());
		}
		return mailManager;
	}
}
