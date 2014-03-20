package com.jesm3.newDualis.stupla;

import java.util.List;

import com.jesm3.newDualis.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class WochenplanArrayAdapter extends ArrayAdapter<Wochenplan>{
	
	Context context; 
	int layoutResourceId;    
	List<Wochenplan> wochenplaene = null;

	public WochenplanArrayAdapter(Context context, int resource,
			List<Wochenplan> wochenplaene) {
		super(context, resource, wochenplaene);
		this.context = context;
		this.layoutResourceId = resource;
		this.wochenplaene = wochenplaene;
		
	}

	
	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getDropDownView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		// assign the view we are converting to a local variable
				View v = convertView;

				// first check to see if the view is null. if so, we have to inflate it.
				// to inflate it basically means to render, or show, the view.
				if (v == null) {
					LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					v = inflater.inflate(R.layout.kw_item, null);
				}

				/*
				 * Recall that the variable position is sent in as an argument to this method.
				 * The variable simply refers to the position of the current object in the list. (The ArrayAdapter
				 * iterates through the list we sent it)
				 * 
				 * Therefore, i refers to the current Item object.
				 */
				Wochenplan theWochenplan = wochenplaene.get(position);

				if (theWochenplan != null) {

					// This is how you obtain a reference to the TextViews.
					// These TextViews are created in the XML files we defined.

					TextView tt = (TextView) v.findViewById(R.id.kw_spinner_text);

					// check to see if each individual textview is null.
					// if not, assign some text!
					if (tt != null){
						tt.setText(theWochenplan.getKalenderwoche() + " (" + theWochenplan.getAnfangsDatum() + " - " + theWochenplan.getEndDatum() + ")");
					}
				}

				// the view must be returned to our activity
				return v;
	}


	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// assign the view we are converting to a local variable
		View v = convertView;

		// first check to see if the view is null. if so, we have to inflate it.
		// to inflate it basically means to render, or show, the view.
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.kw_item, null);
		}

		/*
		 * Recall that the variable position is sent in as an argument to this method.
		 * The variable simply refers to the position of the current object in the list. (The ArrayAdapter
		 * iterates through the list we sent it)
		 * 
		 * Therefore, i refers to the current Item object.
		 */
		Wochenplan theWochenplan = wochenplaene.get(position);

		if (theWochenplan != null) {

			// This is how you obtain a reference to the TextViews.
			// These TextViews are created in the XML files we defined.

			TextView tt = (TextView) v.findViewById(R.id.kw_spinner_text);

			// check to see if each individual textview is null.
			// if not, assign some text!
			if (tt != null){
				tt.setText(theWochenplan.getKalenderwoche() + " (" + theWochenplan.getAnfangsDatum() + " - " + theWochenplan.getEndDatum() + ")");
			}
		}

		// the view must be returned to our activity
		return v;
	}

}
