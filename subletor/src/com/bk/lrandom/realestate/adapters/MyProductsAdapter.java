package com.bk.lrandom.realestate.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bk.lrandom.realestate.R;
import com.bk.lrandom.realestate.models.Properties;
import com.koushikdutta.ion.Ion;

public class MyProductsAdapter extends ArrayAdapter<Properties> {
	private Context context;
	private int itemLayoutResource;

	public MyProductsAdapter(Context context, int itemLayoutResource,
			ArrayList<Properties> products) {
		super(context, itemLayoutResource, products);
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

		Properties product = getItem(position);
		ImageView thumb = (ImageView) view.findViewById(R.id.thumb);
		Ion.with(
				context,
				context.getResources().getString(R.string.domain_url)
						+ product.getImages_path()).withBitmap()
				.resize(200, 200).centerCrop().error(R.drawable.no_photo)
				.placeholder(R.drawable.no_photo).intoImageView(thumb);

		TextView userName = (TextView) view.findViewById(R.id.user_name);
		userName.setText(" " + product.getUser_name());

		TextView title = (TextView) view.findViewById(R.id.title);
		title.setText(" " + product.getTitle());

		TextView price = (TextView) view.findViewById(R.id.price);
		price.setText(" " + product.getPrice());
		
		TextView address = (TextView) view.findViewById(R.id.address);
		address.setText(product.getAddress());

		return view;
	}
}
