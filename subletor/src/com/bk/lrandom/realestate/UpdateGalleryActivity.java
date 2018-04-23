package com.bk.lrandom.realestate;

import java.io.File;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;

import com.bk.lrandom.realestate.adapters.UpdateGalleryAdapter;
import com.bk.lrandom.realestate.business.JSONFetchTask;
import com.bk.lrandom.realestate.business.Ultils;
import com.bk.lrandom.realestate.confs.constants;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class UpdateGalleryActivity extends ActionBarParentActivity {
	ImageButton btnImagePicker;
	Button btnSubmit;
	GridView gallery;
	int properties_id;
	int columnWidth;
	public static final String IMAGES_RESPONSE = "images_feed";
	ArrayList<String> paths, thumbPaths;
	ArrayList<Integer> ids;
	UpdateGalleryAdapter adapter;
	private static final int SELECT_PICTURE = 0;
	String photoPath = "";

	private void initGallery() {
		Resources r = getResources();
		float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				constants.GRID_PADDING, r.getDisplayMetrics());

		columnWidth = (int) ((Ultils.getScreenWidth(UpdateGalleryActivity.this) - ((constants.NUM_OF_COLUMNS + 1) * padding)) / constants.NUM_OF_COLUMNS);

		gallery.setNumColumns(constants.NUM_OF_COLUMNS);
		gallery.setColumnWidth(columnWidth);
		gallery.setStretchMode(GridView.NO_STRETCH);
		gallery.setPadding((int) padding, (int) padding, (int) padding,
				(int) padding);
		gallery.setHorizontalSpacing((int) padding);
		gallery.setVerticalSpacing((int) padding);
	}

	@SuppressLint("HandlerLeak")
	Handler handlerImages = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Bundle bundle = msg.getData();
			if (bundle.containsKey(IMAGES_RESPONSE)) {
				String jsonString = bundle.getString(IMAGES_RESPONSE);
				try {
					JSONArray jsonArray = new JSONArray(jsonString);
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObj = jsonArray.getJSONObject(i);
						String path = getResources().getString(
								R.string.domain_url)
								+ jsonObj.getString("path");
						paths.add(path);
						String thumbPath = jsonObj.getString("thumb_path");
						thumbPaths.add(thumbPath);
						int id = jsonObj.getInt("id");
						ids.add(id);
					}
					// gallery adapter
					adapter = new UpdateGalleryAdapter(
							UpdateGalleryActivity.this, properties_id, paths,
							thumbPaths, ids, columnWidth);
					gallery.setAdapter(adapter);
				} catch (Exception e) {
					adapter = new UpdateGalleryAdapter(
							UpdateGalleryActivity.this, properties_id, paths,
							thumbPaths, ids, columnWidth);
					gallery.setAdapter(adapter);
				}
			}
		};
	};

	public void pickPhoto() {
		// TODO: launch the photo picker
		Intent intent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(Intent.createChooser(intent, "Select"),
				SELECT_PICTURE);
	}

	private String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = getContentResolver().query(uri, projection, null, null,
				null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		String filePath = cursor.getString(column_index);
		return filePath;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case SELECT_PICTURE:
				String selectAbpath = getPath(data.getData());
				Ion.with(getApplicationContext(), new File(selectAbpath))
						.withBitmap().resize(200, 200).centerCrop().asBitmap()
						.setCallback(new FutureCallback<Bitmap>() {
							@Override
							public void onCompleted(Exception arg0,
									Bitmap bitmap) {
								btnImagePicker.setImageBitmap(bitmap);
							}
						});

				photoPath = selectAbpath;
				break;
			default:
				break;
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_gallery_layout);
		gallery = (GridView) findViewById(R.id.gallery);
		btnImagePicker = (ImageButton) findViewById(R.id.btnPicker);
		btnSubmit = (Button) findViewById(R.id.btnSubmit);
		properties_id = getIntent().getExtras().getInt(constants.COMMON_KEY);

		btnImagePicker = (ImageButton) findViewById(R.id.btnPicker);
		btnImagePicker.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pickPhoto();
			}
		});

		btnSubmit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!photoPath.equalsIgnoreCase("")) {
					final ProgressDialog prgDialog = new ProgressDialog(
							UpdateGalleryActivity.this);
					prgDialog.show();
					new Thread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							String handleInsertUrl = getResources().getString(
									R.string.images_json_url)
									+ "images";
							try {
								HttpClient client = new DefaultHttpClient();
								HttpPost post = new HttpPost(handleInsertUrl);
								MultipartEntity reqEntity = new MultipartEntity();

								FileBody fileUpload = new FileBody(new File(
										photoPath));
								reqEntity.addPart("photo", fileUpload);
								reqEntity.addPart("id", new StringBody(
										properties_id + ""));
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
												reload();
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
				} else {
					showDialog(getResources().getString(
							R.string.confirm_take_photo));
				}
			}
		});

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		reload();
		Log.i("RESUME", "RESUME");
	}

	private void reload() {
		initGallery();
		paths = new ArrayList<String>();
		thumbPaths = new ArrayList<String>();
		ids = new ArrayList<Integer>();
		new JSONFetchTask(getResources().getString(R.string.images_json_url)
				+ "images/estate_id/" + properties_id, handlerImages,
				IMAGES_RESPONSE).execute();
	}
}
