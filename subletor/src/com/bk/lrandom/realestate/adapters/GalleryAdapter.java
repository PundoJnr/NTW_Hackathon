package com.bk.lrandom.realestate.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bk.lrandom.realestate.ImagePreviewActivity;
import com.bk.lrandom.realestate.R;
import com.bk.lrandom.realestate.confs.constants;
import com.koushikdutta.ion.Ion;

public class GalleryAdapter extends BaseAdapter {

	private Activity _activity;
	private ArrayList<String> _filePaths = new ArrayList<String>();
	private int imageWidth;

	public GalleryAdapter(Activity activity, ArrayList<String> filePaths,
			int imageWidth) {
		this._activity = activity;
		this._filePaths = filePaths;
		this.imageWidth = imageWidth;
	}

	@Override
	public int getCount() {
		return this._filePaths.size();
	}

	@Override
	public Object getItem(int position) {
		return this._filePaths.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ImageView imageView;
		if (convertView == null) {
			imageView = new ImageView(_activity);
		} else {
			imageView = (ImageView) convertView;
		}

		Ion.with(_activity, _filePaths.get(position)).withBitmap()
				.resize(256, 256).centerCrop().placeholder(R.drawable.no_photo)
				.error(R.drawable.no_photo).intoImageView(imageView);


		imageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			//	v.getTag();
				// TODO Auto-generated method stub
				Intent intent = new Intent(_activity,
						ImagePreviewActivity.class);
				intent.putStringArrayListExtra(
						constants.IMAGES_PATH,_filePaths);
				intent.putExtra(constants.IMAGE_POSITION, position);
				_activity.startActivity(intent);
			}
		});
		return imageView;
	}
}
