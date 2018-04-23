package com.bk.lrandom.realestate.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bk.lrandom.realestate.CreateAccountActivity;
import com.bk.lrandom.realestate.LoginActivity;
import com.bk.lrandom.realestate.R;

@SuppressLint("NewApi")
public class AuthenticationFragment extends Fragment {
	String TAG = "AuthenticationFragment";
	Button btnLogin, btnCreateAccount;
	ProgressDialog dialogPrg;
	String userName = null;

	public static final AuthenticationFragment newInstance() {
		// TODO Auto-generated constructor stub
		AuthenticationFragment fragment = new AuthenticationFragment();
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
		View view = inflater.inflate(R.layout.authentication_layout, container,
				false);
		btnLogin = (Button) view.findViewById(R.id.btn_login);
		btnCreateAccount = (Button) view.findViewById(R.id.btn_create_account);
		btnLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), LoginActivity.class);
				startActivity(intent);
			}
		});

		btnCreateAccount.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),
						CreateAccountActivity.class);
				startActivity(intent);
			}
		});

		dialogPrg = new ProgressDialog(getActivity());
		dialogPrg.setCanceledOnTouchOutside(false);
		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
}
