package com.bk.lrandom.realestate;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bk.lrandom.realestate.confs.constants;
import com.koushikdutta.ion.Ion;

public class UpdateImagePreviewActivity extends Activity {
	public static final String TAG = "UpdateImagePreviewActivity";
	String paths, thumbPaths;
	Button btnClose, btnSetAsThumbnail, btnDelete;
	ImageView images;
	int properties_id, images_id;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.full_update_preview_activity);
		properties_id = getIntent().getExtras().getInt(constants.COMMON_KEY);
		btnClose = (Button) findViewById(R.id.btn_close);
		btnSetAsThumbnail = (Button) findViewById(R.id.btn_set_thumbnail);
		btnDelete = (Button) findViewById(R.id.btn_delete);
		Intent intent = getIntent();
		paths = intent.getStringExtra(constants.IMAGES_PATH);
		thumbPaths = intent.getStringExtra(constants.THUMB_KEY);
		images_id = intent.getIntExtra(constants.COMMON_KEY, 0);
		properties_id = intent.getIntExtra(constants.PROPERTIES_ID, 0);

		images = (ImageView) findViewById(R.id.images);

		Ion.with(UpdateImagePreviewActivity.this, paths).withBitmap()
				.resize(256, 256).centerCrop().placeholder(R.drawable.no_photo)
				.error(R.drawable.no_photo).intoImageView(images);

		btnClose.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				UpdateImagePreviewActivity.this.finish();
			}
		});

		btnSetAsThumbnail.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				final ProgressDialog prgDialog = new ProgressDialog(
						UpdateImagePreviewActivity.this);
				prgDialog.setMessage(getResources().getString(
						R.string.please_wait_msg));
				prgDialog.show();
				// TODO Auto-generated method stub
				new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						String handleInserUrl = getResources().getString(
								R.string.images_json_url)
								+ "set_thumbnail";
						try {
							HttpClient client = new DefaultHttpClient();
							HttpPost post = new HttpPost(handleInserUrl);
							MultipartEntity reqEntity = new MultipartEntity();
							reqEntity.addPart("estates_id", new StringBody(
									properties_id + ""));
							reqEntity.addPart("thumb_path", new StringBody(
									thumbPaths));
							post.setEntity(reqEntity);
							HttpResponse response = client.execute(post);
							HttpEntity resEntity = response.getEntity();
							final String response_str = EntityUtils
									.toString(resEntity);
							if (resEntity != null) {
								Log.i("RESPONSE", response_str);
								runOnUiThread(new Runnable() {
									public void run() {
										try {
											prgDialog.dismiss();
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
		});

		btnDelete.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(
						UpdateImagePreviewActivity.this);
				builder.setMessage(getResources().getString(
						R.string.confirm_del));
				builder.setPositiveButton(
						getResources().getString(R.string.ok_label),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								final ProgressDialog prgDialog = new ProgressDialog(
										UpdateImagePreviewActivity.this);
								prgDialog.setMessage(getResources().getString(
										R.string.please_wait_msg));
								prgDialog.show();
								// TODO Auto-generated method stub
								new Thread(new Runnable() {
									@Override
									public void run() {
										// TODO Auto-generated method stub
										String handleInserUrl = getResources()
												.getString(
														R.string.images_json_url)
												+ "remove";
										try {
											HttpClient client = new DefaultHttpClient();
											HttpPost post = new HttpPost(
													handleInserUrl);
											MultipartEntity reqEntity = new MultipartEntity();
											reqEntity
													.addPart(
															"estates_id",
															new StringBody(
																	properties_id
																			+ ""));
											reqEntity.addPart("data_id",
													new StringBody(images_id
															+ ""));
											post.setEntity(reqEntity);
											HttpResponse response = client
													.execute(post);
											HttpEntity resEntity = response
													.getEntity();
											final String response_str = EntityUtils
													.toString(resEntity);
											if (resEntity != null) {
												Log.i("RESPONSE", response_str);
												runOnUiThread(new Runnable() {
													public void run() {
														try {
															prgDialog.dismiss();
															UpdateImagePreviewActivity.this
																	.finish();
														} catch (Exception e) {
															e.printStackTrace();
														}
													}
												});
											}
										} catch (Exception ex) {
											Log.e("Debug",
													"error: " + ex.getMessage(),
													ex);
										}
									}
								}).start();
							}
						}).setNegativeButton(
						getResources().getString(R.string.cancel_label),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						});

				Dialog dialog = builder.create();
				dialog.show();
			}
		});
	}
}
