package com.jesm3.newDualis.is;

import android.app.*;

public class CustomApplication extends Application {

	private Backend backend;
	private User user;

	@Override
	public void onCreate() {
//		startActivity(new Intent(this, LoginActivity.class);
		super.onCreate();
	}
	
	public Backend getBackend() {
		if (backend == null) {
			backend = new Backend();
		}
		return backend;
	}
	
	public User getUser() {
		return user;
	}
}
