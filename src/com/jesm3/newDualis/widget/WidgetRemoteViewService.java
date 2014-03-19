package com.jesm3.newDualis.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created with IntelliJ IDEA.
 * User: Black
 * Date: 25.11.13
 * Time: 18:35
 */
public class WidgetRemoteViewService extends RemoteViewsService {
	@Override
	public RemoteViewsFactory onGetViewFactory(Intent intent) {
		return new WidgetFactory(getApplicationContext(), intent);
	}
}
