package com.bk.lrandom.realestate.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bk.lrandom.realestate.R;
import com.bk.lrandom.realestate.confs.constants;

public class CommentFragment extends Fragment {
	int product_id = 0;

	public static final CommentFragment newInstance() {
		CommentFragment fragment = new CommentFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.listview_container_layout,
				container, false);
		if (getArguments() != null) {
			if (getArguments().containsKey(constants.COMMON_KEY)) {
				product_id = getArguments().getInt(constants.COMMON_KEY);
				
			}
		}
		return view;
	}
	
	
}
