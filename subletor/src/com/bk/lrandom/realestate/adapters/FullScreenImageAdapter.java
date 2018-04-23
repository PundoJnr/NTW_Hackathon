package com.bk.lrandom.realestate.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.bk.lrandom.realestate.R;
import com.bk.lrandom.realestate.business.TouchImageView;
import com.koushikdutta.ion.Ion;

public class FullScreenImageAdapter extends PagerAdapter {
	private Activity activity;
	private ArrayList<String> paths;
	private LayoutInflater inflater;

	// constructor
	public FullScreenImageAdapter(Activity activity,
			ArrayList<String> imagePaths) {
		this.activity = activity;
		this.paths = imagePaths;
	}

	@Override
	public int getCount() {
		return this.paths.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((RelativeLayout) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		TouchImageView imgDisplay;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View viewLayout = inflater.inflate(R.layout.full_preview_item,
				container, false);

		imgDisplay = (TouchImageView) viewLayout.findViewById(R.id.imgDisplay);
		Ion.with(activity, paths.get(position)).withBitmap()
				.error(R.drawable.no_photo).placeholder(R.drawable.no_photo)
				.intoImageView(imgDisplay);
		(container).addView(viewLayout);
		return viewLayout;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		(container).removeView((RelativeLayout) object);
	}
}
