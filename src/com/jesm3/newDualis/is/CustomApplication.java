package com.jesm3.newDualis.is;

import android.app.Application;
import android.content.Intent;

import com.jesm3.newDualis.activities.LoginActivity;
import com.jesm3.newDualis.activities.MainActivity;

public class CustomApplication extends Application {

	private Backend backend;
	private UserManager userManager;

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
}
