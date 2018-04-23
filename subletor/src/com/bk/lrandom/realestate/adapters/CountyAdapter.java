package com.bk.lrandom.realestate.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bk.lrandom.realestate.R;
import com.bk.lrandom.realestate.models.County;

public class CountyAdapter extends ArrayAdapter<County> {
	private Context context;
	private int itemLayoutResource;

	public CountyAdapter(Context context, int itemLayoutResource,
			ArrayList<County> county) {
		super(context, itemLayoutResource, county);
		this.itemLayoutResource = itemLayoutResource;
		this.context = context;
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
		County province = getItem(position);
		TextView title = (TextView) view.findViewById(R.id.title);
		title.setText(province.getName());
		return view;
	}

}

