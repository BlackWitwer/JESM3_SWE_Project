package com.jesm3.newDualis.mail;

import android.content.*;
import android.graphics.*;
import android.view.*;
import android.widget.*;
import com.jesm3.newDualis.*;
import java.util.*;
import javax.mail.*;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

	private Context context;
	private ArrayList<Message> messageList;
    private List<String> listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> listDataChild;
 
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
