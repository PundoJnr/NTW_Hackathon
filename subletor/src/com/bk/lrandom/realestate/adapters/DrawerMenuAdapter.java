package com.bk.lrandom.realestate.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bk.lrandom.realestate.R;
import com.bk.lrandom.realestate.business.RoundedAvatarDrawable;
import com.bk.lrandom.realestate.models.DrawerMenuItem;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class DrawerMenuAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<DrawerMenuItem> drawerItems;

	public DrawerMenuAdapter(Context context,
			ArrayList<DrawerMenuItem> drawerItems) {
		this.context = context;
		this.drawerItems = drawerItems;
	}

	@Override
	public int getCount() {
		return drawerItems.size();
	}

	@Override
	public Object getItem(int position) {
		return drawerItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater mInflater = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.drawer_menu_item, null);
		}
		final ImageView imgIcon = (ImageView) convertView
				.findViewById(R.id.icon);
		TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
		if (drawerItems.get(position).getAvt() != null
				&& !drawerItems.get(position).getAvt().equals("")) {
			Ion.with(context, drawerItems.get(position).getAvt()).withBitmap()
					.placeholder(R.drawable.ic_small_avatar).resize(64, 64)
					.centerCrop().error(R.drawable.ic_small_avatar).asBitmap()
					.setCallback(new FutureCallback<Bitmap>() {
						@Override
						public void onCompleted(Exception arg0, Bitmap bitmap) {
							// TODO Auto-generated method stub
							if (bitmap != null) {
								RoundedAvatarDrawable avtDrawable = new RoundedAvatarDrawable(
										bitmap);
								imgIcon.setImageDrawable(avtDrawable);
							}
						}
					});

		} else {
			imgIcon.setImageResource(drawerItems.get(position).getIcon());
		}
		txtTitle.setText(drawerItems.get(position).getTitle());
		return convertView;
	}

}