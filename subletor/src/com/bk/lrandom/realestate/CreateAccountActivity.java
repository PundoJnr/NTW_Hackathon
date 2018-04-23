package com.bk.lrandom.realestate;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bk.lrandom.realestate.business.Ultils;
import com.bk.lrandom.realestate.business.Validator;
import com.bk.lrandom.realestate.confs.constants;

public class CreateAccountActivity extends ActionBarParentActivity {
	String email, userName, fullName, address, phone, websites, pwd, cmfPwd;
	EditText emailText, userNameText, fullNameText, addressText, phoneText,
			pwdText, cmfPwdText;
	Button submit;
	ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_layout);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		emailText = (EditText) findViewById(R.id.email);
		userNameText = (EditText) findViewById(R.id.user_name);
		fullNameText = (EditText) findViewById(R.id.full_name);
		addressText = (EditText) findViewById(R.id.address);
		phoneText = (EditText) findViewById(R.id.phone);
		pwdText = (EditText) findViewById(R.id.pwd);
		cmfPwdText = (EditText) findViewById(R.id.cmf_pwd);

		submit = (Button) findViewById(R.id.btn_submit);
		submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				email = emailText.getText().toString();
				userName = userNameText.getText().toString();
				fullName = fullNameText.getText().toString();
				address = addressText.getText().toString();
				phone = phoneText.getText().toString();
				pwd = pwdText.getText().toString();
				cmfPwd = cmfPwdText.getText().toString();
				// TODO Auto-generated method stub
				Log.i("De", "text" + email);
				if (email.equalsIgnoreCase("") || userName.equalsIgnoreCase("")
						|| fullName.equalsIgnoreCase("")) {
					showDialog(getResources()
							.getString(R.string.vl_required));
					return;
				}

				if (!Validator.validEmail(email)) {
					showDialog(getResources().getString(R.string.invalid_email));
					return;
				}
				

				if (!Validator.validUserName(userName)) {
					showDialog(getResources().getString(
							R.string.invalid_user_name));
					return;
				}

				if (!cmfPwd.equals(pwd)) {
					showDialog(getResources().getString(
							R.string.confirm_pass_alert));
					return;
				}

				if (!Ultils.isConnectingToInternet(CreateAccountActivity.this)) {
					showMsg(getResources().getString(R.string.open_network));
				} else {
					new Upload().execute();
				}

			}
		});
	}

	public void showDialogSendCode() {
		AlertDialog.Builder buidler = new AlertDialog.Builder(
				CreateAccountActivity.this);
		buidler.setMessage(getResources().getString(R.string.verified_msg));
		buidler.setPositiveButton(getResources().getString(R.string.ok_label),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method
						// stub
						Intent intent = new Intent(CreateAccountActivity.this,
								VerifiedAccountActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
								| Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.putExtra(constants.COMMON_KEY, email);
						startActivity(intent);
					}
				});
		AlertDialog dialog = buidler.create();
		dialog.show();
	}

	private class Upload extends AsyncTask<Void, Void, Boolean> {

		public Upload() {
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
			dialog.dismiss();
		}


		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog = new ProgressDialog(CreateAccountActivity.this);
			dialog.setMessage(CreateAccountActivity.this.getResources()
					.getString(R.string.submit));
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String handleInserUrl = getResources().getString(
					R.string.users_json_url)
					+ "check_register_valid";
			try {
				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(handleInserUrl);
				MultipartEntity reqEntity = new MultipartEntity();
				reqEntity.addPart("email", new StringBody(email + ""));
				reqEntity.addPart("user_name", new StringBody(userName));
				reqEntity.addPart("full_name", new StringBody(fullName));
				reqEntity.addPart("address", new StringBody(address));
				reqEntity.addPart("phone", new StringBody(phone));
				reqEntity.addPart("pwd", new StringBody(pwd));
				post.setEntity(reqEntity);
				HttpResponse response = client.execute(post);
				HttpEntity resEntity = response.getEntity();
				final String response_str = EntityUtils.toString(resEntity);
				if (resEntity != null) {
					Log.i("RESPONSE", response_str);
					runOnUiThread(new Runnable() {
						public void run() {
							try {
								dialog.dismiss();
								JSONObject jsonObj = new JSONObject(
										response_str);
								String ok = jsonObj.getString("ok");
								if (ok.equalsIgnoreCase("2")) {
									showDialogSendCode();
								}
								if (ok.equalsIgnoreCase("0")) {
									showDialog(getResources().getString(
											R.string.email_exist));
									return;
								}

								if (ok.equalsIgnoreCase("1")) {
									showDialog(getResources().getString(
											R.string.user_name_exist));
									return;
								}
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
}
