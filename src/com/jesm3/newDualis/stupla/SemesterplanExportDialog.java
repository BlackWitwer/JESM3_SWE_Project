package com.jesm3.newDualis.stupla;

import java.util.ArrayList;
import java.util.List;

import com.jesm3.newDualis.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

public class SemesterplanExportDialog extends DialogFragment {
	
	/* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface NoticeDialogListener {
        public void onDialogPositiveClick(SemesterplanExportDialog dialog);
        public void onDialogNegativeClick(SemesterplanExportDialog dialog);
    }

	private NoticeDialogListener mListener;
    private List<String> mSelectedItems;
    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		 mSelectedItems = new ArrayList<String>();  // Where we track the selected items
		    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		    // Set the dialog title
		    final int exportArrayId = R.array.export_array;
			builder.setTitle(R.string.semesterplan_export)
		    // Specify the list array, the items to be selected by default (null for none),
		    // and the listener through which to receive callbacks when items are selected
		           .setMultiChoiceItems(exportArrayId, null,
		                      new DialogInterface.OnMultiChoiceClickListener() {
		               @Override
		               public void onClick(DialogInterface dialog, int which,
		                       boolean isChecked) {
		            	   String[] theStringValues = getResources().getStringArray(exportArrayId);
		                   if (isChecked) {
		                       // If the user checked the item, add it to the selected items
		                       mSelectedItems.add(theStringValues[which]);
		                   } else if (mSelectedItems.contains(which)) {
		                       // Else, if the item is already in the array, remove it 
		                       mSelectedItems.remove(theStringValues[which]);
		                   }
		               }
		           })
		    // Set the action buttons
		           .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
		               @Override
		               public void onClick(DialogInterface dialog, int id) {
		                   // User clicked OK, so save the mSelectedItems results somewhere
		                   // or return them to the component that opened the dialog
		            	   mListener.onDialogPositiveClick(SemesterplanExportDialog.this);
		               }
		           })
		           .setNegativeButton(R.string.abbrechen, new DialogInterface.OnClickListener() {
		               @Override
		               public void onClick(DialogInterface dialog, int id) {
		            	   mListener.onDialogNegativeClick(SemesterplanExportDialog.this);
		               }
		           });

		    return builder.create();
	}
	
	public List<String> getmSelectedItems() {
		return mSelectedItems;
	}

}
