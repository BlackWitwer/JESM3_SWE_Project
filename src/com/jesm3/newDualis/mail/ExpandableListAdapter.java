package com.jesm3.newDualis.mail;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Environment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.*;

import com.jesm3.newDualis.R;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

	private Context context;
	private ArrayList<Message> messageList;

	//Diese Variablen ändern sich für jede Messgae.
	private boolean textIsHtml = false;
	private ArrayList<Part> attachmentList;

	public ExpandableListAdapter(Context context,
			ArrayList<Message> someMessages) {
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

		final Message child = (Message) getChild(groupPosition, childPosition);

		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_item, null);
		}

		TextView txtListChild = (TextView) convertView.findViewById(R.id.listItem);
		WebView view = (WebView) convertView.findViewById(R.id.webViewItem);

		try {
			attachmentList = new ArrayList<Part>();
			String theText = getText(child);
			if (textIsHtml) {
				view.loadData(theText, "text/html; charset=UTF-8", null);
				view.setVisibility(View.VISIBLE);
				txtListChild.setVisibility(View.GONE);
			} else {
				txtListChild.setText(theText);
				view.setVisibility(View.GONE);
				txtListChild.setVisibility(View.VISIBLE);
			}

			LinearLayout layout = (LinearLayout)convertView.findViewById(R.id.attachmentBar);

			for (Part eachPart : attachmentList) {
				Button theButton = new Button(layout.getContext());
				theButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
				layout.addView(theButton);

				theButton.setText(eachPart.getContentType());
			}

			//TODO ausbauen. Anhang wird bis jetzt nur als test.pdf gespeichert und kann auch nur als pdf geöffnet werden
//			if (attachmentPart != null) {
//				final File attachment = new File(convertView.getContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
//						, "test"
//				+ attachmentPart.getFileName().substring(attachmentPart.getFileName().lastIndexOf(".")));
//				attachment.createNewFile();
//				InputStream is = attachmentPart.getInputStream();
//			    FileOutputStream fos = new FileOutputStream(attachment);
//			    byte[] buf = new byte[4096];
//			    int bytesRead;
//			    while((bytesRead = is.read(buf))!=-1) {
//			        fos.write(buf, 0, bytesRead);
//			    }
//			    fos.close();
//
//			    final View tempConverView = convertView;
//
//			    button.setOnClickListener(new View.OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						Intent intent = new Intent();
//						intent.setAction(android.content.Intent.ACTION_VIEW);
//						intent.setDataAndType(Uri.fromFile(attachment), "application/pdf");
//						tempConverView.getContext().startActivity(intent);
//					}
//				});
//			    button.setText(attachmentPart.getFileName());
//			} else {
//				button.setVisibility(Button.GONE);
//			}
//
		} catch (IOException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
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
		Message theMessage = (Message) getGroup(groupPosition);
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_group, null);
		}

		TextView lblListFrom = (TextView) convertView
				.findViewById(R.id.listFrom);
		TextView lblListSubject = (TextView) convertView
				.findViewById(R.id.listSubject);
		TextView lblListDate = (TextView) convertView
				.findViewById(R.id.listDate);

		try {
			lblListSubject.setTextColor(Color.BLACK);
			if (!theMessage.getFlags().contains(Flags.Flag.SEEN)) {
				lblListSubject.setTextColor(Color.BLUE);
			}
			lblListSubject.setText(theMessage.getSubject());
			lblListFrom.setText(theMessage.getFrom()[0].toString());
			lblListDate.setText(theMessage.getReceivedDate().toLocaleString());
		} catch (MessagingException e) {
			e.printStackTrace();
		}

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

	public void addMessage(Message aMessage) {
		messageList.add(aMessage);
	}
	
	public void addAllMessages (Collection<Message> someMessages) {
		messageList.addAll(someMessages);
	}

	/**
	 * Return the primary text content of the message.
	 */
	private String getText(Part p) throws MessagingException, IOException {
		if (p.isMimeType("text/*")) {
			String s = (String) p.getContent();
			textIsHtml = p.isMimeType("text/html");
			return s;
		}

		if (p.isMimeType("multipart/alternative")) {
			// prefer html text over plain text
			Multipart mp = (Multipart) p.getContent();
			String text = null;
			for (int i = 0; i < mp.getCount(); i++) {
				Part bp = mp.getBodyPart(i);
				if (bp.isMimeType("text/plain")) {
					if (text == null)
						text = getText(bp);
					continue;
				} else if (bp.isMimeType("text/html")) {
					String s = getText(bp);
					if (s != null)
						return s;
				} else {
					return getText(bp);
				}
			}
			return text;
		} else if (p.isMimeType("multipart/*")) {
			Multipart mp = (Multipart) p.getContent();
			for (int i = 0; i < mp.getCount(); i++) {
				String s = getText(mp.getBodyPart(i));
				if (s != null)
					return s;
			}
		}
		return "";
	}
}
