package com.bk.lrandom.realestate.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.bk.lrandom.realestate.R;
import com.bk.lrandom.realestate.models.MyMarker;
import com.koushikdutta.ion.Ion;

public class MarkerAdapter extends BaseAdapter {
	private Context context;
	private int itemLayoutResource;
	private ArrayList<MyMarker> markers;

	public MarkerAdapter(Context context, int itemLayoutResource,
			ArrayList<MyMarker> markers) {
		this.itemLayoutResource = itemLayoutResource;
		this.context = context;
		this.markers = markers;
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
		MyMarker marker = markers.get(position); // getItem(position);
		ImageView imageMarker = (ImageView) view.findViewById(R.id.marker);
		Ion.with(
				context,
				context.getResources().getString(R.string.domain_url)
						+ marker.getPath()).withBitmap().resize(256, 256)
				.centerCrop().placeholder(R.drawable.no_photo)
				.error(R.drawable.no_photo).intoImageView(imageMarker);
		return view;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return markers.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return markers.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}
}