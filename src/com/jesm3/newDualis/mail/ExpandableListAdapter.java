package com.jesm3.newDualis.mail;

import java.util.ArrayList;

import javax.mail.Message;
import javax.mail.MessagingException;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.jesm3.newDualis.R;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

	private Context context;
	private ArrayList<Message> messageList;
 
    public ExpandableListAdapter(Context context, ArrayList<Message> someMessages) {
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
 
//        try {
 //TODO Mail Content reinschreiben
			txtListChild.setText("Hallo Manu");
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (MessagingException e) {
//			e.printStackTrace();
//		}
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
 
        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.listHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
		lblListHeader.setTextColor(Color.BLACK);
        try {
			lblListHeader.setText(theMessage.getSubject());
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
}
