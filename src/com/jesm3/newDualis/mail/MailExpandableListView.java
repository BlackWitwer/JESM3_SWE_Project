package com.jesm3.newDualis.mail;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;
import com.jesm3.newDualis.is.*;
import android.app.*;

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
		getMailManager().refreshCache();
		((ExpandableListAdapter) getExpandableListAdapter()).removeAllMessages();
		((ExpandableListAdapter) getExpandableListAdapter()).addAllMessages(getMailManager().getCachedMails());
		((ExpandableListAdapter) getExpandableListAdapter()).notifyDataSetChanged();
	}
	
	private MailManager getMailManager() {
		if (mailManager == null) {
			mailManager = ((CustomApplication) ((Activity) getContext()).getApplication()).getBackend().getMailManager();
		}
		return mailManager;
	}

}
