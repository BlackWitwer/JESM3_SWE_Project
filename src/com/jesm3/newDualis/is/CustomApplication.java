package com.jesm3.newDualis.is;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.jesm3.newDualis.mail.MailManager;
import com.jesm3.newDualis.synchronization.SyncService;

public class CustomApplication extends Application {

	private Backend backend;
	private UserManager userManager;

	private SyncService mBoundSyncService = null;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		// start the SyncService
		Intent intent = new Intent(this, SyncService.class);

		ComponentName cm = startService(intent);
		
		ServiceConnection mConnection = new ServiceConnection() {

			@Override
			public void onServiceDisconnected(ComponentName name) {
				mBoundSyncService = null;
			}

			@Override
			public void onServiceConnected(ComponentName name,
					IBinder service) {
				mBoundSyncService = ((SyncService.LocalBinder) service)
						.getService();
			}
		};
		
		bindService(intent, mConnection, 0);
	}
	
	public Backend getBackend() {
		if (backend == null) {
			backend = new Backend(this);
		}
		return backend;
	}
	
	public UserManager getUserManager() {
		if (userManager == null) {
			userManager = new UserManager(this);
		}
		return userManager;
	}
	
	public SyncService getSyncService() {
		return mBoundSyncService;
	}
	
	public void logout() {
		getUserManager().logout();
		getBackend().getDbManager().logout();
		getBackend().logout();
	}
}
