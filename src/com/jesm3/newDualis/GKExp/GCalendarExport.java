//package com.jesm3.newDualis.GKExp;
//
//import android.content.ContentResolver;
//import android.database.Cursor;
//import android.net.Uri;
//import android.provider.CalendarContract.Calendars;
//
//public class GCalendarExport {
//	// Projection array. Creating indices for this array instead of doing
//	// dynamic lookups improves performance.
//	public static final String[] EVENT_PROJECTION = new String[] {
//	    Calendars._ID,                           // 0
//	    Calendars.ACCOUNT_NAME,                  // 1
//	    Calendars.CALENDAR_DISPLAY_NAME,         // 2
//	    Calendars.OWNER_ACCOUNT                  // 3
//	};
//	  
//	// The indices for the projection array above.
//	private static final int PROJECTION_ID_INDEX = 0;
//	private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
//	private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
//	private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;
//	
//	
//	// Run query
//	Cursor cur = null;
//	//ContentResolver cr = getContentResolver();
//	Uri uri = Calendars.CONTENT_URI;   
//	String selection = "((" + Calendars.ACCOUNT_NAME + " = ?) AND (" 
//	                        + Calendars.ACCOUNT_TYPE + " = ?) AND ("
//	                        + Calendars.OWNER_ACCOUNT + " = ?))";
//	String[] selectionArgs = new String[] {"sampleuser@gmail.com", "com.google",
//	        "sampleuser@gmail.com"}; 
//	// Submit the query and get a Cursor object back. 
//	cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);
//
//	/**
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//
//	}
//
//}
