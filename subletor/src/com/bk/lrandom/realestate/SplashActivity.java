package com.bk.lrandom.realestate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.bk.lrandom.realestate.confs.constants;
import com.bk.lrandom.realestate.R;

public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_layout);
		// @Override
		// protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		try {
//			PackageInfo info = getPackageManager()
//					.getPackageInfo("com.bk.lrandom.droidmarket",
//							PackageManager.GET_SIGNATURES);
//			for (Signature signature : info.signatures) {
//				MessageDigest md = MessageDigest.getInstance("SHA");
//				md.update(signature.toByteArray());
//				Log.i("KeyHash:",
//						Base64.encodeToString(md.digest(), Base64.DEFAULT));
//			}
//		} catch (NameNotFoundException e) {
//			Log.i("LOI CMNR", e.toString());
//		} catch (NoSuchAlgorithmException e) {
//			Log.i("LOI CMNR 2", e.toString());
//		}
//		// }
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SplashActivity.this,
						HomeActivity.class);
				startActivity(intent);
				finish();
			}
		}, constants.SPLASH_TIME_OUT);
	}
}
