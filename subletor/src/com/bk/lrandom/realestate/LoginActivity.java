package com.bk.lrandom.realestate;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bk.lrandom.realestate.business.Ultils;
import com.bk.lrandom.realestate.business.UserSessionManager;
import com.bk.lrandom.realestate.models.User;
import com.bk.lrandom.realestate.R;

public class LoginActivity extends ActionBarParentActivity {
	ProgressDialog dialogPrg;
	String TAG = "LoginActivity";
	TextView email;
	TextView pwd;
	Button btnLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);
		btnLogin = (Button) findViewById(R.id.btn_login);
		email = (TextView) findViewById(R.id.email);
		pwd = (TextView) findViewById(R.id.pwd);
		dialogPrg = new ProgressDialog(this);
		dialogPrg.setCanceledOnTouchOutside(false);
		btnLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!Ultils.isConnectingToInternet(LoginActivity.this)) {
					Toast ts = Toast.makeText(LoginActivity.this,
							getResources().getString(R.string.open_network),
							5000);
					ts.show();
				} else {
					dialogPrg.setMessage(getResources().getString(
							R.string.loging));
					dialogPrg.show();
					new Login().start();
				}
			}
		});
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	public void showDialogFailedLogin() {
		Ultils.logout(LoginActivity.this);
		AlertDialog.Builder buidler = new AlertDialog.Builder(
				LoginActivity.this);
		buidler.setMessage(getString(R.string.login_failed));
		buidler.setPositiveButton(getResources().getString(R.string.ok_label),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method
						// stub

					}
				});
		AlertDialog dialog = buidler.create();
		dialog.show();
	}

	private class Login extends Thread {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			try {
				String handlerUrl = getResources().getString(
						R.string.users_json_url)
						+ "login";
				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(handlerUrl);
				MultipartEntity reqEntity = new MultipartEntity();
				String emailText = email.getText().toString();
				String pwdText = pwd.getText().toString();
				reqEntity.addPart("email", new StringBody(emailText));
				reqEntity.addPart("pwd", new StringBody(pwdText));
				post.setEntity(reqEntity);
				HttpResponse res = client.execute(post);
				HttpEntity resEntity = res.getEntity();
				final String response_str = EntityUtils.toString(resEntity);
				if (resEntity != null) {
					Log.i(TAG, response_str);
					runOnUiThread(new Runnable() {
						public void run() {
							dialogPrg.dismiss();
							try {
								JSONArray jsonArray = new JSONArray(
										response_str);
								if (jsonArray.length() == 1) {
									JSONObject obj = jsonArray.getJSONObject(0);
									User user = Ultils.parseUser(obj);
									UserSessionManager userSession = new UserSessionManager(
											LoginActivity.this);
									userSession.storeUserSession(user);
								}
								Intent intent = new Intent(LoginActivity.this,
										HomeActivity.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
										| Intent.FLAG_ACTIVITY_NEW_TASK);
								startActivity(intent);
							} catch (Exception e) {
								showDialogFailedLogin();
							}
						}
					});
				}
			} catch (Exception e) {
				// TODO: handle exception
				// showDialogFailedLogin();
			}
		}
	}
}
