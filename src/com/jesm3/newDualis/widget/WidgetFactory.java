package com.jesm3.newDualis.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import com.jesm3.newDualis.R;
import com.jesm3.newDualis.stupla.Vorlesung;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Black
 * Date: 25.11.13
 * Time: 18:38
 */
public class WidgetFactory implements RemoteViewsService.RemoteViewsFactory {

	private static final int mCount = 10;
	private List<Vorlesung> mWidgetItems = new ArrayList<Vorlesung>();
	private Context context;
	private int widgetId;

	public WidgetFactory (Context context, Intent intent) {
		this.context = context;
		this.widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
	}

	@Override
	public void onCreate() {
		// In onCreate() you setup any connections / cursors to your data
		// source. Heavy lifting,
		// for example downloading or creating content etc, should be deferred
		// to onDataSetChanged()
		// or getViewAt(). Taking more than 20 seconds in this call will result
		// in an ANR.
//		mWidgetItems.add(new Vorlesung(
//				"09:00",
//				"12:30",
//				"Rentschler",
//				"Software Engineering"));
//		mWidgetItems.add(new Vorlesung(
//				"13:00",
//				"14:30",
//				"Kick",
//				"Langsame Mathematic"));
//		mWidgetItems.add(new Vorlesung(
//				"15:00",
//				"18:30",
//				"Schwarze",
//				"Schlafen und anderes Sinnvolles"));
	}

	@Override
	public void onDataSetChanged() {
		// This is triggered when you call AppWidgetManager
		// notifyAppWidgetViewDataChanged
		// on the collection view corresponding to this factory. You can do
		// heaving lifting in
		// here, synchronously. For example, if you need to process an image,
		// fetch something
		// from the network, etc., it is ok to do it here, synchronously. The
		// widget will remain
		// in its current state while work is being done here, so you don't need
		// to worry about
		// locking up the widget.
	}

	@Override
	public void onDestroy() {
		// In onDestroy() you should tear down anything that was setup for your
		// data source,
		// eg. cursors, connections, etc.
		mWidgetItems.clear();
	}

	@Override
	public int getCount() {
		return mWidgetItems.size();
	}

	@Override
	public RemoteViews getViewAt(int position) {
		// position will always range from 0 to getCount() - 1.

		// We construct a remote views item based on our widget item xml file,
		// and set the
		// text based on the position.
		RemoteViews rv = new RemoteViews(context.getPackageName(),
				R.layout.widget_item);
//		if (position % 2 == 0) {
//			rv.setImageViewResource(R.id.widget_item, R.drawable.fire);
//		} else {
//			rv.setImageViewResource(R.id.widget_item, R.drawable.ente);
//		}
		rv.setTextViewText(R.id.widgetZeit, mWidgetItems.get(position).getUhrzeitVon() + " - " + mWidgetItems.get(0).getUhrzeitBis());
		rv.setTextViewText(R.id.widgetName, mWidgetItems.get(position).getName());
		rv.setTextViewText(R.id.widgetDozent, mWidgetItems.get(position).getDozent());
		// rv.setTextViewText(R.id.widget_item,
		// mWidgetItems.get(position).text);

		// Next, we set a fill-intent which will be used to fill-in the pending
		// intent template
		// which is set on the collection view in StackWidgetProvider.
//		Bundle extras = new Bundle();
//		extras.putInt(WidgetProvider.EXTRA_ITEM, position);
//		Intent fillInIntent = new Intent();
//		fillInIntent.putExtras(extras);
//		rv.setOnClickFillInIntent(R.id.widget_item, fillInIntent);

		// You can do heaving lifting in here, synchronously. For example, if
		// you need to
		// process an image, fetch something from the network, etc., it is ok to
		// do it here,
		// synchronously. A loading view will show up in lieu of the actual
		// contents in the
		// interim.
		try {
			System.out.println("Loading view " + position);
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Return the remote views object.
		return rv;
	}

	@Override
	public RemoteViews getLoadingView() {
		// You can create a custom loading view (for instance when getViewAt()
		// is slow.) If you
		// return null here, you will get the default loading view.
		return null;
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}
}
