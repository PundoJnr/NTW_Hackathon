package com.bk.lrandom.realestate.fragments;

import java.util.ArrayList;

import com.bk.lrandom.realestate.R;
import com.bk.lrandom.realestate.adapters.AmenitiesAdapter;
import com.bk.lrandom.realestate.interfaces.SelectAmenitiesComunicator;
import com.bk.lrandom.realestate.models.Amenities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;

@SuppressLint("ValidFragment")
public class SelectAmenitiesDialog extends DialogFragment {
	ArrayList<Amenities> amenities;
	ArrayList<Amenities> selectedAmenities;
	SelectAmenitiesComunicator listener;

	public SelectAmenitiesDialog() {
		// TODO Auto-generated constructor stub
	}

	public SelectAmenitiesDialog(ArrayList<Amenities> amenities) {
		// TODO Auto-generated constructor stub
		this.amenities = amenities;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		listener = (SelectAmenitiesComunicator) activity;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		selectedAmenities = new ArrayList<Amenities>();
		AmenitiesAdapter amenitiesAdapter = new AmenitiesAdapter(getActivity()
				.getApplicationContext(), R.layout.amenities_item_layout,
				amenities, getActivity().getSupportFragmentManager());
		ListView list = new ListView(getActivity());
		list.setAdapter(amenitiesAdapter);
		list.setItemsCanFocus(false);
		list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				CheckBox cbox = (CheckBox) view.findViewById(R.id.cbox);
				if (cbox.isChecked()) {
					amenities.get(arg2).setChecked(false);
					selectedAmenities.remove(amenities.get(arg2));
				} else {
					amenities.get(arg2).setChecked(true);
					selectedAmenities.add(amenities.get(arg2));
				}
				;
			}
		});

		builder.setTitle(getResources().getString(R.string.select_amenities))
				.setView(list)
				.setPositiveButton(R.string.ok_label, null)
				.setNegativeButton(R.string.cancel_label,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
							}
						});
		AlertDialog dialog = builder.create();
		dialog.show();
		dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(
				new View.OnClickListener() {
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (!selectedAmenities.isEmpty()) {
							listener.getAmenities(selectedAmenities);
							dismiss();
						}
					}
				});
		return dialog;
	}
}
