package com.jesm3.newDualis.mail;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ExpandableListView;
import com.jesm3.newDualis.is.*;
import android.app.*;
import com.jesm3.newDualis.*;

public class MailExpandableListView extends ExpandableListView {
	
	private static final int MAX_Y_OVERSCROLL_DISTANCE = 200;

	private MailManager mailManager;
    
    private Context mContext;
	private int mMaxYOverscrollDistance;
	private boolean refreshFlag;
	
	public MailExpandableListView(Context context) {
		super(context);
		mContext = context;
		initBounceListView();
	}
	
	public MailExpandableListView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		initBounceListView();
	}

	public MailExpandableListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initBounceListView();
	}
	
	private void initBounceListView()
	{
		//get the density of the screen and do some maths with it on the max overscroll distance
		//variable so that you get similar behaviors no matter what the screen size
		
		final DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        	final float density = metrics.density;
        
		mMaxYOverscrollDistance = (int) (density * MAX_Y_OVERSCROLL_DISTANCE);
	}
	
	@Override
	protected boolean overScrollBy(int deltaX, int deltaY, int scrollX,
			int scrollY, int scrollRangeX, int scrollRangeY,
			int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
		if (!refreshFlag && scrollY <= -mMaxYOverscrollDistance) {
			getMailManager().refreshCache();
			refreshFlag = true;
			((ExpandableListAdapter) getExpandableListAdapter()).setMessages(getMailManager().getCachedMails());
		} else if (scrollY > -mMaxYOverscrollDistance/5) {
			//Setzt das Flag zurück wenn der Overscroll ein Stück zurück gescrollt wurde.
			refreshFlag = false;
		}
		
		if (scrollY > 0) {
			deltaY = 0;
		}
	
		return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX,
				scrollRangeY, maxOverScrollX, mMaxYOverscrollDistance, isTouchEvent);
	}
	
	private MailManager getMailManager() {
		if (mailManager == null) {
			mailManager = ((CustomApplication) ((Activity) getContext()).getApplication()).getBackend().getMailManager();
		}
		return mailManager;
	}

}
