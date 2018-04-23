package com.bk.lrandom.realestate.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.bk.lrandom.realestate.R;

public class FanpageFragment extends Fragment {
	public static final FanpageFragment newInstance() {
		// TODO Auto-generated constructor stub
		FanpageFragment fragment = new FanpageFragment();
		return fragment;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fanpage_layout, null);
		WebView content = (WebView) view.findViewById(R.id.content);
		content.getSettings().setJavaScriptEnabled(true);
		content.loadUrl(getResources().getString(R.string.fan_page_link));
		return view;
	}
}
