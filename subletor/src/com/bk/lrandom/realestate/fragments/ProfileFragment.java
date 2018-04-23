package com.bk.lrandom.realestate.fragments;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bk.lrandom.realestate.ChangePassActivity;
import com.bk.lrandom.realestate.PropertiesActivity;
import com.bk.lrandom.realestate.R;
import com.bk.lrandom.realestate.UpdateProfileActivity;
import com.bk.lrandom.realestate.business.RoundedAvatarDrawable;
import com.bk.lrandom.realestate.business.Ultils;
import com.bk.lrandom.realestate.business.UserSessionManager;
import com.bk.lrandom.realestate.confs.constants;
import com.bk.lrandom.realestate.interfaces.ProfileComunicator;
import com.bk.lrandom.realestate.models.User;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class ProfileFragment extends Fragment {
	Button btnLogout, btnEdit, btnShowMyProducts, btnShowMarkProducts,
			btnChangePass;
	TextView displayName, email, skype, address, phone, userName;
	ImageView avt;
	ProfileComunicator listener;
	ProgressDialog loadingDialog;
	User userProfile;
	public static final String TAG = "ProfileFragment";
	ProgressDialog prgDialog;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		listener = (ProfileComunicator) getActivity();
	}

	public static final ProfileFragment newInstance() {
		// TODO Auto-generated constructor stub
		ProfileFragment fragment = new ProfileFragment();
		return fragment;
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

		UserSessionManager sessionManager = new UserSessionManager(
				getActivity());
		// userProfile = new User();
		userProfile = sessionManager.getUserSession();
		if (userProfile != null) {
			displayName.setText(userProfile.getFullName());
			skype.setText(userProfile.getSkype());
			phone.setText(userProfile.getPhone());
			address.setText(userProfile.getAddress());
			email.setText(userProfile.getEmail());
			userName.setText(userProfile.getUserName());
			if (userProfile.getAvt() != null
					&& !userProfile.getAvt().equalsIgnoreCase("")) {
				String avtString = "";
				if (Ultils.checkFacebookAvt(userProfile.getAvt())) {
					avtString = userProfile.getAvt();
				} else {
					avtString = getResources().getString(R.string.domain_url)
							+ userProfile.getAvt();
				}
				Ion.with(getActivity(), avtString).withBitmap()
						.resize(200, 200).centerCrop()
						.placeholder(R.drawable.ic_avatar)
						.error(R.drawable.ic_avatar).asBitmap()
						.setCallback(new FutureCallback<Bitmap>() {

							@Override
							public void onCompleted(Exception arg0,
									Bitmap bitmap) {
								// TODO Auto-generated method stub
								if (bitmap != null) {
									RoundedAvatarDrawable avtDrawable = new RoundedAvatarDrawable(
											bitmap);
									avt.setImageDrawable(avtDrawable);
								}
							}

						});

			}

			btnEdit.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(getActivity(),
							UpdateProfileActivity.class);
					intent.putExtra(constants.COMMON_KEY, userProfile);
					startActivity(intent);
				}
			});

			btnChangePass.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(getActivity(),
							ChangePassActivity.class);
					intent.putExtra(constants.COMMON_KEY, userProfile);
					startActivity(intent);
				}
			});

			btnLogout.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Ultils.logout(getActivity());
					listener.logout();
				}
			});

			btnShowMyProducts.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(getActivity(),
							PropertiesActivity.class);
					intent.putExtra(constants.USER_ID_KEY, userProfile.getId());
					startActivity(intent);
				}
			});
		}
	}

	private class Upload extends AsyncTask<Void, Void, Boolean> {
		String fb_id = null;
		int user_id = 0;

		public Upload() {
			// TODO Auto-generated constructor stub
		}

		public Upload(String fb_id, int user_id) {
			this.fb_id = fb_id;
			this.user_id = user_id;
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
			prgDialog.dismiss();
		}

		protected void onPostExecute() {
			prgDialog.dismiss();
		};

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			prgDialog = new ProgressDialog(getActivity());
			prgDialog.setMessage(getActivity().getResources().getString(
					R.string.please_wait_msg));
			prgDialog.setCanceledOnTouchOutside(false);
			prgDialog.show();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String handleInserUrl = getResources().getString(
					R.string.estate_json_url)
					+ "update";
			try {
				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(handleInserUrl);
				MultipartEntity reqEntity = new MultipartEntity();
				reqEntity.addPart("user_id", new StringBody(userProfile.getId()
						+ ""));
				post.setEntity(reqEntity);
				HttpResponse response = client.execute(post);
				HttpEntity resEntity = response.getEntity();
				final String response_str = EntityUtils.toString(resEntity);
				if (resEntity != null) {
					Log.i("RESPONSE", response_str);
					getActivity().runOnUiThread(new Runnable() {
						public void run() {
							try {
								prgDialog.dismiss();
								Toast ts = Toast.makeText(
										getActivity(),
										getActivity().getResources().getString(
												R.string.success_action), 5000);
								ts.show();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
				}
			} catch (Exception ex) {
				Log.e("Debug", "error: " + ex.getMessage(), ex);
			}
			return null;
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.profile_fragment_layout, null);
		btnLogout = (Button) view.findViewById(R.id.btn_logout);
		btnEdit = (Button) view.findViewById(R.id.btn_update);
		// btnShowMarkProducts = (Button) view
		// .findViewById(R.id.btn_show_mark_products);
		btnShowMyProducts = (Button) view
				.findViewById(R.id.btn_show_my_products);
		displayName = (TextView) view.findViewById(R.id.display_name);
		email = (TextView) view.findViewById(R.id.email);
		address = (TextView) view.findViewById(R.id.address);
		phone = (TextView) view.findViewById(R.id.phone);
		skype = (TextView) view.findViewById(R.id.skype);
		avt = (ImageView) view.findViewById(R.id.avt);
		userName = (TextView) view.findViewById(R.id.user_name);
		btnChangePass = (Button) view.findViewById(R.id.btn_change_pass);
		return view;
	}
}
