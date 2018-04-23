package com.bk.lrandom.realestate.business;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import com.bk.lrandom.realestate.confs.constants;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class JSONFetchTask extends AsyncTask<Void, Void, String> {
	private String url;
	private Handler mHandler;
	static InputStream is = null;
	static JSONObject jObj = null;
	static String jsonString = "";
	public static final String KEY_RESPONSE = "KEY_RESPONSE";
	public String TAG = "JSONFetchTask";
	public String keyResponse;

	public JSONFetchTask(String url, Handler mHandler) {
		this.url = url;
		this.mHandler = mHandler;
		this.keyResponse = KEY_RESPONSE;
	}

	public JSONFetchTask(String url, Handler mHandler, String keyResponse) {
		this.url = url;
		this.mHandler = mHandler;
		this.keyResponse = keyResponse;
	}

	@Override
	protected String doInBackground(Void... params) {
		// TODO Auto-generated method stub
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(url);
			HttpResponse httpResponse = httpClient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, constants.STREAM_READER_CHARSET), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			jsonString = sb.toString();
			Log.i("JSON_FETCH_TAG", jsonString);
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}
		return jsonString;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		try {
			Bundle bundle = new Bundle();
			bundle.putString(this.keyResponse, result);
			Message msg = new Message();
			msg.setData(bundle);
			mHandler.sendMessage(msg);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("ERR", e.toString());
		}
	}
}
