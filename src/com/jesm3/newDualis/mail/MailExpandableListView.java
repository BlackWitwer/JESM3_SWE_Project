package com.jesm3.newDualis.mail;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

public class MailExpandableListView extends ExpandableListView {

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
		// TODO Auto-generated method stub
		System.out.println(scrollX + " " + scrollY + " " + clampedX + " " + clampedY);
		super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
	}

}
