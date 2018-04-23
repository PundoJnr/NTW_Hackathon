package com.bk.lrandom.realestate;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import com.bk.lrandom.realestate.adapters.FullScreenImageAdapter;
import com.bk.lrandom.realestate.confs.constants;

public class ImagePreviewActivity extends Activity {
	public static final String TAG = "ImagePreviewActivity";
	ArrayList<String> paths = null;
	private FullScreenImageAdapter adapter;
	private ViewPager viewPager;
	Button btnClose;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.full_preview_activity);
		btnClose = (Button) findViewById(R.id.btn_close);
		Intent intent = getIntent();
		paths = intent.getExtras().getStringArrayList(constants.IMAGES_PATH);
		adapter = new FullScreenImageAdapter(this, paths);
		viewPager = (ViewPager) findViewById(R.id.pager);
		Intent i = getIntent();
		int position = i.getIntExtra(constants.IMAGE_POSITION, 0);
		viewPager.setAdapter(adapter);
		viewPager.setCurrentItem(position);
		btnClose.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ImagePreviewActivity.this.finish();
			}
		});
	}
}
