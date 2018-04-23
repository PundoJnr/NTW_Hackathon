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
import com.bk.lrandom.realestate.confs.constants;
import com.bk.lrandom.realestate.R;

public class VerifiedAccountActivity extends ActionBarParentActivity {
	ProgressDialog dialog;
	String email;
	String code;
	EditText codeText;
	Button submit, resend;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.verified_account_layout);
		codeText = (EditText) findViewById(R.id.verified_code);
		email = getIntent().getStringExtra(constants.COMMON_KEY);
		submit = (Button) findViewById(R.id.btn_submit);
		resend = (Button) findViewById(R.id.btn_resend);

		resend.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new ResendVerifiedCode().execute();
			}
		});

		submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				code = codeText.getText().toString();
				if (code.equalsIgnoreCase("") || code == null) {
					showDialog(getResources().getString(R.string.vl_required));
					return;
				}
				if (!Ultils
						.isConnectingToInternet(VerifiedAccountActivity.this)) {
					showMsg(getResources().getString(R.string.open_network));
				} else {
					new Upload().execute();
				}
			}
		});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String handleInserUrl = getResources().getString(
						R.string.users_json_url)
						+ "remove_verified_account";
				try {
					HttpClient client = new DefaultHttpClient();
					HttpPost post = new HttpPost(handleInserUrl);
					MultipartEntity reqEntity = new MultipartEntity();
					reqEntity.addPart("email", new StringBody(email));
					post.setEntity(reqEntity);
					HttpResponse response = client.execute(post);
					HttpEntity resEntity = response.getEntity();
					final String response_str = EntityUtils.toString(resEntity);
					if (resEntity != null) {
						Log.i("RESPONSE", response_str);
						runOnUiThread(new Runnable() {
							public void run() {
								try {
									// dialog.dismiss();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
					}
				} catch (Exception ex) {
					Log.e("Debug", "error: " + ex.getMessage(), ex);
				}
			}
		}).start();
	}

	private class ResendVerifiedCode extends AsyncTask<Void, Void, Boolean> {

		public ResendVerifiedCode() {
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
			dialog = new ProgressDialog(VerifiedAccountActivity.this);
			dialog.setMessage(VerifiedAccountActivity.this.getResources()
					.getString(R.string.submit));
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String handleInserUrl = getResources().getString(
					R.string.users_json_url)
					+ "resend_verified_code";
			try {
				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(handleInserUrl);
				MultipartEntity reqEntity = new MultipartEntity();
				reqEntity.addPart("email", new StringBody(email));
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

		protected void onPostExecute() {
			dialog.dismiss();
		};

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog = new ProgressDialog(VerifiedAccountActivity.this);
			dialog.setMessage(VerifiedAccountActivity.this.getResources()
					.getString(R.string.upload_product_msg));
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String handleInserUrl = getResources().getString(
					R.string.users_json_url)
					+ "register";
			try {
				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(handleInserUrl);
				MultipartEntity reqEntity = new MultipartEntity();
				reqEntity.addPart("code", new StringBody(code + ""));
				reqEntity.addPart("email", new StringBody(email));
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
								if (ok.equalsIgnoreCase("1")) {
									showCorrectDialog(getResources().getString(
											R.string.verified_code_success));

									return;
								}
								if (ok.equalsIgnoreCase("0")) {
									showDialog(getResources().getString(
											R.string.verified_code_failed));
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

	public void showCorrectDialog(String msg) {
		AlertDialog.Builder buidler = new AlertDialog.Builder(
				VerifiedAccountActivity.this);
		buidler.setMessage(msg);
		buidler.setPositiveButton(getResources().getString(R.string.ok_label),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method
						Intent intent = new Intent(
								VerifiedAccountActivity.this,
								HomeActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
								| Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
					}
				});
		AlertDialog dialog = buidler.create();
		dialog.show();
	}
}
