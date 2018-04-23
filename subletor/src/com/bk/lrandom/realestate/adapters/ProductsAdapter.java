package com.bk.lrandom.realestate.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bk.lrandom.realestate.R;
import com.bk.lrandom.realestate.confs.constants;
import com.bk.lrandom.realestate.models.Properties;
import com.koushikdutta.ion.Ion;

public class ProductsAdapter extends ArrayAdapter<Properties> {
	private Context context;
	private int itemLayoutResource;

	public ProductsAdapter(Context context, int itemLayoutResource,
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

		TextView title = (TextView) view.findViewById(R.id.title);
		title.setText(product.getTitle());

		TextView area = (TextView) view.findViewById(R.id.area);
		area.setText(product.getArea());

		TextView bedroom = (TextView) view.findViewById(R.id.bedroom);
		bedroom.setText(product.getBedroom());

		TextView bathroom = (TextView) view.findViewById(R.id.bathroom);
		bathroom.setText(product.getBathroom());

		TextView address = (TextView) view.findViewById(R.id.address);
		address.setText(product.getAddress());

		String priceText = product.getPrice();
		String currency = product.getCurrency();
		priceText = currency + " " + priceText;
		int timeRate = product.getTime_rate();
		if (timeRate != -1) {
			Resources r = context.getResources();
			if (timeRate == constants.WEEK) {
				priceText += " / " + r.getString(R.string.week);
			}
			if (timeRate == constants.MONTH) {
				priceText += " / " + r.getString(R.string.month);
			}
			if (timeRate == constants.YEAR) {
				priceText += " / " + r.getString(R.string.year);
			}
		}
		TextView price = (TextView) view.findViewById(R.id.price);
		price.setText(" " + priceText);

		ImageView badge = (ImageView) view.findViewById(R.id.badge);
		badge.setImageResource(android.R.color.transparent);

		if (product.getStatus().equals(constants.SOLD)) {
			badge.setImageResource(R.drawable.ic_sold);
		}
		if (product.getStatus().equals(constants.FEATURED)) {
			badge.setImageResource(R.drawable.ic_featured);
		}

		return view;
	}
}