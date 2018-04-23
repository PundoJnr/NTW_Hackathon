package com.bk.lrandom.realestate;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bk.lrandom.realestate.business.JSONFetchTask;
import com.bk.lrandom.realestate.business.RoundedAvatarDrawable;
import com.bk.lrandom.realestate.business.Ultils;
import com.bk.lrandom.realestate.confs.constants;
import com.bk.lrandom.realestate.models.User;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class ProfileActivity extends ActionBarParentActivity {
	ProgressDialog dialogPrg;
	public static String TAG = "ProfileActivity";
	TextView userName, address, phone, skype;
	ImageView avt;
	Button btnShowItem;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_layout);
		dialogPrg = new ProgressDialog(this);
		dialogPrg.setCanceledOnTouchOutside(false);
		String user_id = getIntent().getStringExtra(constants.COMMON_KEY);
		Log.i(TAG, "user id la" + user_id);
		new JSONFetchTask(getResources().getString(R.string.users_json_url)
				+ "user/id/" + user_id, handler).execute();
		dialogPrg.show();
		userName = (TextView) findViewById(R.id.user_name);
		address = (TextView) findViewById(R.id.address);
		phone = (TextView) findViewById(R.id.phone);
		skype = (TextView) findViewById(R.id.skype);
		avt = (ImageView) findViewById(R.id.avt);
		btnShowItem = (Button) findViewById(R.id.btn_show_my_item);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		changeActionBarTitle(getResources().getString(R.string.profile_label));
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Bundle bundle = msg.getData();
			if (bundle.containsKey(JSONFetchTask.KEY_RESPONSE)) {
				String jsonString = bundle
						.getString(JSONFetchTask.KEY_RESPONSE);
				Log.i(TAG, "skadj");
				try {
					JSONArray jsonArray = new JSONArray(jsonString);
					if (jsonArray.length() == 1) {
						JSONObject obj = jsonArray.getJSONObject(0);
						final User user = Ultils.parseUser(obj);
						if (user != null) {
							userName.setText(user.getUserName());
							address.setText(user.getAddress());
							phone.setText(user.getPhone());
							skype.setText(user.getSkype());
							btnShowItem
									.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											// TODO Auto-generated method stub
											Intent intent = new Intent(
													ProfileActivity.this,
													PropertiesActivity.class);
											intent.putExtra(
													constants.USER_POST_KEY,
													user.getId());
											startActivity(intent);
										}
									});
							if (user.getAvt() != null
									&& !user.getAvt().equalsIgnoreCase("")) {
								Log.i(TAG, "Khac null");
								String avtString = "";
								if (Ultils.checkFacebookAvt(user.getAvt())) {
									avtString = user.getAvt();
								} else {
									avtString = getResources().getString(
											R.string.domain_url)
											+ user.getAvt();
								}
								Ion.with(ProfileActivity.this, avtString)
										.withBitmap()
										.resize(200, 200)
										.centerCrop()
										.placeholder(R.drawable.ic_small_avatar)
										.error(R.drawable.ic_small_avatar)
										.asBitmap()
										.setCallback(
												new FutureCallback<Bitmap>() {

													@Override
													public void onCompleted(
															Exception arg0,
															Bitmap bitmap) {
														// TODO Auto-generated
														// method stub
														if (bitmap != null) {
															RoundedAvatarDrawable avtDrawable = new RoundedAvatarDrawable(
																	bitmap);
															avt.setImageDrawable(avtDrawable);
														}
													}
												});
							}
						}
					}
					dialogPrg.cancel();
				} catch (Exception e) {
					Log.e(TAG, "error parse");
					e.printStackTrace();
				}
			}
		};
	};
}
