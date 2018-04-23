package com.bk.lrandom.realestate.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.bk.lrandom.realestate.R;

public class DetailContentFragment extends Fragment {
	public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
	public static final String DETAIL_KEY = "LYRICS_KEY";
	private String content;

	public static final DetailContentFragment newInstance() {
		// TODO Auto-generated constructor stub
		DetailContentFragment fragment = new DetailContentFragment();
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
		View view = inflater.inflate(R.layout.detailcontent_fragment_layout,
				container, false);
		if (getArguments() != null) {
			content = getArguments().getString(DETAIL_KEY);
			if (content == null) {
				content = "";
			}

			String mime = "text/html";
			String encoding = "utf-8";

			WebView web = (WebView) view.findViewById(R.id.detail_content);
			web.loadDataWithBaseURL(null, content, mime, encoding, null);
		}
		return view;
	}

	public void setContent(String content) {
		this.content = content;
		if (getView() != null) {
			WebView web = (WebView) getView().findViewById(R.id.detail_content);
			if (content == null) {
				content = "";
			}
			String mime = "text/html";
			String encoding = "utf-8";
			web.loadDataWithBaseURL(null, content, mime, encoding, null);
		}
	}
}
