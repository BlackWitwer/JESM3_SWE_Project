package com.jesm3.newDualis.mail;

import java.io.IOException;
import java.util.ArrayList;

import javax.mail.BodyPart;
import javax.mail.Flags.Flag;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.jesm3.newDualis.R;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

	private Context context;
	private ArrayList<Message> messageList;
	private boolean textIsHtml = false;
	private int expandedGroupPosition = -1;

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

		TextView txtListChild = (TextView) convertView
				.findViewById(R.id.listItem);
		try {
			Multipart multipart = (Multipart) child.getContent();
			for (int i = 0; i < multipart.getCount(); i++) {
				BodyPart body = multipart.getBodyPart(i);
				txtListChild.append(Html.fromHtml(getText(body)+""));
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
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
			if (!theMessage.getFlags().contains(Flag.SEEN)) {
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

		return null;
	}
}
