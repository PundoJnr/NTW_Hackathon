package com.bk.lrandom.realestate;
import android.os.Bundle;

import com.bk.lrandom.realestate.fragments.AuthenticationFragment;
import com.bk.lrandom.realestate.R;
public class AuthenticationActivity extends ActionBarParentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.frame_layout);
		AuthenticationFragment fragment = AuthenticationFragment.newInstance();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.container, fragment).commit();
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setTitle(getResources().getString(R.string.login_label));
	}
}
