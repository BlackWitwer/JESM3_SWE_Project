package com.jesm3.newDualis.mail;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import javax.mail.MessagingException;
import javax.mail.Part;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.*;

import com.jesm3.newDualis.R;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

	private Context context;
	private ArrayList<MailContainer> messageList;

	public ExpandableListAdapter(Context context,
			ArrayList<MailContainer> someMessages) {
		this.context = context;
		this.messageList = someMessages;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		final MailContainer theMail = (MailContainer) getChild(groupPosition, childPosition);

		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.mail_list_item, null);
		}
		
		TextView theHeaderFrom = (TextView) convertView.findViewById(R.id.listHeaderFrom);
		TextView theHeaderTo = (TextView) convertView.findViewById(R.id.listHeaderTo);
		TextView theHeaderDate = (TextView) convertView.findViewById(R.id.listHeaderDate);
		
		theHeaderFrom.setText("Von: " + theMail.getFromComplete());
		theHeaderTo.setText("An: " + theMail.getTo());
		theHeaderDate.setText("Datum: " + theMail.getDate().toString());
		
		TextView txtListChild = (TextView) convertView.findViewById(R.id.listItem);
		WebView view = (WebView) convertView.findViewById(R.id.webViewItem);
		String theText = theMail.getText();
		if (theMail.getHtml()) {
			view.loadData(theText, "text/html; charset=UTF-8", null);
			view.setVisibility(View.VISIBLE);
			txtListChild.setVisibility(View.GONE);
		} else {
			txtListChild.setText(theText);
			view.setVisibility(View.GONE);
			txtListChild.setVisibility(View.VISIBLE);
		}

		LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.attachmentBar);
		layout.removeAllViews();

		final View superView = convertView;
		for (final Part eachPart : theMail.getAttachmentList()) {
			Button theButton = new Button(layout.getContext());
			theButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
			layout.addView(theButton);

			theButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					downloadAttachment(superView, eachPart);					
				}
			});

			try {
				theButton.setText(eachPart.getFileName() + " " + convertToMinSize(eachPart.getSize()));
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
		return convertView;
	}	
	
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		MailContainer theMessage = (MailContainer) getGroup(groupPosition);
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.mail_list_group, null);
		}

		TextView lblListFrom = (TextView) convertView
				.findViewById(R.id.listFrom);
		TextView lblListSubject = (TextView) convertView
				.findViewById(R.id.listSubject);
		TextView lblListDate = (TextView) convertView
				.findViewById(R.id.listDate);
		TextView lblListAttach = (TextView) convertView
				.findViewById(R.id.listAttach);
				
		//Das Laden der verschiedenen Informationen an dieser Stelle bewirkt das Stockende laden und nachladen
		//der Mail View. Evt in Containerobjekt auslagern in dem die nötigen Informationen bereits drin sind.
		//vermindert zwar nicht die Ladezeit aber macht das Laden evt flüssiger.
		lblListSubject.setTextColor(Color.BLACK);
		if (!theMessage.getSeen()) {
			lblListSubject.setTextColor(Color.BLUE);
			lblListFrom.setTextColor(Color.BLUE);
		}
		lblListSubject.setText(theMessage.getSubject());
		
		lblListFrom.setText(theMessage.getFrom());
		
		if (theMessage.getAttachment()) {
			lblListAttach.setCompoundDrawablesWithIntrinsicBounds(R.drawable.logo, 0, 0, 0);				
		}
		lblListDate.setText(theMessage.getDeltaTime());
		return convertView;
	}
	
	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		return this.messageList.get(groupPosition);
	}
	
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}
	
	public void downloadAttachment(final View aView, final Part aPart) {
		// Start a lengthy operation in a background thread
		new Thread(
		    new Runnable() {
		        @Override
		        public void run() {
		        	final File attachment;
		        	
					final NotificationManager theNotifyManager =
						(NotificationManager) aView.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
					final NotificationCompat.Builder theBuilder = new NotificationCompat.Builder(aView.getContext());
					
					try {
						theBuilder.setContentTitle(aPart.getFileName())
							.setContentText("Download in progress")
							.setSmallIcon(R.drawable.icon);
						
			            attachment = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
								, aPart.getFileName());
	
						if (!attachment.exists()) {
						
							InputStream is = aPart.getInputStream();
							FileOutputStream fos = new FileOutputStream(attachment);
							byte[] buf = new byte[4096];
							int bytesRead;
							int theI = 0;
							int theMaxSize = aPart.getSize();
							while((bytesRead = is.read(buf))!=-1) {
								fos.write(buf, 0, bytesRead);
								// Sets the progress indicator to a max value, the
								// current completion percentage, and "determinate"
								// state
								theBuilder.setProgress(theMaxSize, theI++*buf.length, false);
								// Displays the progress bar for the first time.
								theNotifyManager.notify(0, theBuilder.build());
							}
							fos.close();
							
							attachment.createNewFile();
							
							// When the loop is finished, updates the notification
							theBuilder.setContentText("Download complete")
								// Removes the progress bar
								.setProgress(0,0,false);
						
							Intent intent = new Intent();
							intent.setAction(android.content.Intent.ACTION_VIEW);
							intent.setDataAndType(Uri.fromFile(attachment), aPart.getContentType().split(";")[0]);	
								
							PendingIntent thePendingIntent = PendingIntent.getActivity(	
								aView.getContext(),
								0,
								intent,
								PendingIntent.FLAG_UPDATE_CURRENT);
							
							theBuilder.setContentIntent(thePendingIntent);	
								
							theNotifyManager.notify(0, theBuilder.build());
							
						} else {
							Intent intent = new Intent();
							intent.setAction(android.content.Intent.ACTION_VIEW);
							intent.setDataAndType(Uri.fromFile(attachment), aPart.getContentType().split(";")[0]);
							aView.getContext().startActivity(intent);
						}
		        	} catch (IOException e) {
		        		e.printStackTrace();
		        	} catch (MessagingException e) {
						e.printStackTrace();
					}
		        }
		    }
		).start();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return 1;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return messageList.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return messageList.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	public void addMessage(MailContainer aMessage) {
		messageList.add(aMessage);
	}
	
	public void addAllMessages (Collection<MailContainer> someMessages) {
		messageList.addAll(someMessages);
	}

	public String convertToMinSize (int aSize) {
		int sizeDimension = 0;
		int size = aSize;
		while (size > 1024) {
			size = size / 1024;
			sizeDimension++;
		}
		String sizeDimensionString;
		switch (sizeDimension) {
			case 0:
				sizeDimensionString = "B";
				break;
			case 1:
				sizeDimensionString = "KB";
				break;
			case 2:
				sizeDimensionString = "MB";
				break;
			case 3:
				sizeDimensionString = "GB";
				break;
			case 4:
				sizeDimensionString = "TB";
				break;
			default:
				sizeDimensionString = "Too Damn High";
				break;
		}

		return  size + sizeDimensionString;
	}
}
