package com.bk.lrandom.realestate.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import com.bk.lrandom.realestate.R;
import com.bk.lrandom.realestate.models.Amenities;

public class AmenitiesAdapter extends ArrayAdapter<Amenities> implements
		OnItemSelectedListener {
	private Context context;
	private int itemLayoutResource;
	Spinner spinner;
	String[] menus;
	FragmentManager fragmentManager;

	public AmenitiesAdapter(Context context, int itemLayoutResource,
			ArrayList<Amenities> items, FragmentManager fragmentManager) {
		super(context, itemLayoutResource, items);
		this.itemLayoutResource = itemLayoutResource;
		this.context = context;
		this.fragmentManager = fragmentManager;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = convertView;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(this.itemLayoutResource, null);
		}
		Amenities amenities = getItem(position);
		TextView name = (TextView) view.findViewById(R.id.name);
		name.setText(amenities.getName());
		CheckBox cbox = (CheckBox) view.findViewById(R.id.cbox);
		if (cbox != null) {
			if (amenities.getChecked()) {
				cbox.setChecked(true);
			} else {
				cbox.setChecked(false);
			}
		}
		return view;
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}
}
