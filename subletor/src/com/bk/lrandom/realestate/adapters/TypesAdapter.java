package com.bk.lrandom.realestate.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bk.lrandom.realestate.R;
import com.bk.lrandom.realestate.models.Types;

public class TypesAdapter extends ArrayAdapter<Types> {
	private Context context;
	private int itemLayoutResource;

	public TypesAdapter(Context context, int itemLayoutResource,
			ArrayList<Types> categories) {
		super(context, itemLayoutResource, categories);
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
		Types categorie = getItem(position);
		TextView title = (TextView) view.findViewById(R.id.title);
		title.setText(categorie.getName());
		return view;
	}

}
