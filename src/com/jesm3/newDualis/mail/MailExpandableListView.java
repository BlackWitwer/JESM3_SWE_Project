package com.jesm3.newDualis.mail;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;
import com.jesm3.newDualis.is.*;
import android.app.*;
import com.jesm3.newDualis.*;

public class MailExpandableListView extends ExpandableListView {

	private MailManager mailManager;
	
	public MailExpandableListView(Context context) {
		super(context);
	}
	
	public MailExpandableListView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	public MailExpandableListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/* (non-Javadoc)
	 * @see android.widget.AbsListView#onOverScrolled(int, int, boolean, boolean)
	 */
	@Override
	protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX,
			boolean clampedY) {
		super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
		final Activity theActivity = (Activity) getContext();
		theActivity.runOnUiThread(new Runnable() {
			public void run() {
				theActivity.findViewById(R.id.mailProgressBar).setVisibility(VISIBLE);
			}
		});
		getMailManager().refreshCache();
//		theActivity.runOnUiThread(new Runnable() {
//			public void run() {
//				theActivity.findViewById(R.id.mailProgressBar).setVisibility(GONE);
//			}
//		});
		((ExpandableListAdapter) getExpandableListAdapter()).setMessages(getMailManager().getCachedMails());
	}
	
	private MailManager getMailManager() {
		if (mailManager == null) {
			mailManager = ((CustomApplication) ((Activity) getContext()).getApplication()).getBackend().getMailManager();
		}
		return mailManager;
	}

}
