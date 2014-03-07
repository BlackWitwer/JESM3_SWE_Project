package com.jesm3.newDualis.mail;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.util.TimeUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.*;

import com.jesm3.newDualis.R;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

	private Context context;
	private ArrayList<MessageContainer> messageList;

	public ExpandableListAdapter(Context context,
			ArrayList<MessageContainer> someMessages) {
		this.context = context;
		this.messageList = someMessages;
	}

	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		return this.messageList.get(groupPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		final MessageContainer child = (MessageContainer) getChild(groupPosition, childPosition);

		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.mail_list_item, null);
		}

		TextView txtListChild = (TextView) convertView.findViewById(R.id.listItem);
		WebView view = (WebView) convertView.findViewById(R.id.webViewItem);
		String theText = child.getText();
		if (child.isTextIsHtml()) {
			view.loadData(theText, "text/html; charset=UTF-8", null);
			view.setVisibility(View.VISIBLE);
			txtListChild.setVisibility(View.GONE);
		} else {
			txtListChild.setText(theText);
			view.setVisibility(View.GONE);
			txtListChild.setVisibility(View.VISIBLE);
		}

		LinearLayout layout = (LinearLayout)convertView.findViewById(R.id.attachmentBar);
		layout.removeAllViews();

		final View superView = convertView;
		for (final Part eachPart : child.getAttachmentList()) {
			Button theButton = new Button(layout.getContext());
			theButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
			layout.addView(theButton);

			theButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					final File attachment;
					try {
						attachment = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
								, eachPart.getFileName());
						attachment.createNewFile();

						InputStream is = eachPart.getInputStream();
						FileOutputStream fos = new FileOutputStream(attachment);
						byte[] buf = new byte[4096];
						int bytesRead;
						while((bytesRead = is.read(buf))!=-1) {
							fos.write(buf, 0, bytesRead);
						}
						fos.close();

						Intent intent = new Intent();
						intent.setAction(android.content.Intent.ACTION_VIEW);
						intent.setDataAndType(Uri.fromFile(attachment), eachPart.getContentType().split(";")[0]);
						superView.getContext().startActivity(intent);


					} catch (MessagingException e) {
						e.printStackTrace();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
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
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		MessageContainer theMessage = (MessageContainer) getGroup(groupPosition);
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
		if (!theMessage.isSeen()) {
			lblListSubject.setTextColor(Color.BLUE);
		}
		lblListSubject.setText(theMessage.getSubject());
		
		lblListFrom.setText(theMessage.getFrom());
		
		if (theMessage.hasAttachement()) {
			lblListAttach.setCompoundDrawablesWithIntrinsicBounds(R.drawable.logo, 0, 0, 0);				
		}
		lblListDate.setText(theMessage.getDate());
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	public void addMessage(MessageContainer aMessage) {
		messageList.add(aMessage);
	}
	
	public void addAllMessages (Collection<MessageContainer> someMessages) {
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
