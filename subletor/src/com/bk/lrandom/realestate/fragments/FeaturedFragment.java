package com.bk.lrandom.realestate.fragments;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.bk.lrandom.realestate.DetailActivity;
import com.bk.lrandom.realestate.R;
import com.bk.lrandom.realestate.adapters.ProductsAdapter;
import com.bk.lrandom.realestate.business.JSONFetchTask;
import com.bk.lrandom.realestate.business.Ultils;
import com.bk.lrandom.realestate.confs.constants;
import com.bk.lrandom.realestate.models.Properties;

@SuppressLint("NewApi")
public class FeaturedFragment extends Fragment implements OnScrollListener {
	ArrayList<Properties> properties_list = new ArrayList<Properties>();
	ListView list;
	ProductsAdapter adapter;
	String TAG = "EstateFragment";
	JSONFetchTask jsonFetchTask;
	static InputStream is = null;
	static JSONObject jObj = null;
	static String jsonString = "";
	String query = null, tmpQuery = null;
	int COUNT_ITEM_LOAD_MORE = 5;
	int first = 0;
	ProgressBar loadMorePrg;
	boolean loadingMore = true;
	Button btnAll, btnSell, btnBuy;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	public static final FeaturedFragment newInstance() {
		// TODO Auto-generated constructor stub
		FeaturedFragment fragment = new FeaturedFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(
				R.layout.listview_endless_container_layout, null);

		query = getResources().getString(R.string.estate_json_url)
				+ "estates?x=trick&status=" + constants.FEATURED;
		list = (ListView) view.findViewById(R.id.list);
		adapter = new ProductsAdapter(getActivity(),
				R.layout.properties_item_layout, properties_list);
		LinearLayout footerView = (LinearLayout) inflater.inflate(
				R.layout.footer_loadmore_layout, null);
		loadMorePrg = (ProgressBar) footerView.findViewById(R.id.prgLoadMore);
		list.addFooterView(footerView);
		list.setAdapter(adapter);
		list.setOnScrollListener(this);
		list.setOnItemClickListener(listViewOnClick);

		tmpQuery = query + "&first=" + first + "&offset="
				+ COUNT_ITEM_LOAD_MORE;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			new LoadMoreDataTask()
					.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		} else {
			new LoadMoreDataTask().execute();
		}
		Ultils.loadAd(this.getActivity(), view);
		return view;
	}

	private void parse(JSONObject jsonObj) {
		try {

			int id = jsonObj.getInt(Properties.TAG_ID);
			String name = jsonObj.getString(Properties.TAG_TITLE);
			String price = jsonObj.getString(Properties.TAG_PRICE);
			String type = jsonObj.getString(Properties.TAG_TYPES_NAME);
			String address = jsonObj.getString(Properties.TAG_ADDESS);
			String thumbnail = jsonObj.getString(Properties.TAG_IMAGES);
			String bathroom = jsonObj.getString(Properties.TAG_BATHROOM);
			String bedroom = jsonObj.getString(Properties.TAG_BEDROOM);
			String area = jsonObj.getString(Properties.TAG_AREA);
			int timeRate = jsonObj.getInt(Properties.TAG_TIME_RATE);
			String currency = jsonObj.getString(Properties.TAG_CURRENCY);
			String status = jsonObj.getString(Properties.TAG_STATUS);

			Properties product = new Properties();
			product.setId(id);
			product.setTitle(name);
			product.setPrice(price);
			product.setTypes(type);
			product.setBathroom(bathroom);
			product.setBedroom(bedroom);
			product.setAddress(address);
			product.setImages_path(thumbnail);
			product.setArea(area);
			product.setTime_rate(timeRate);
			product.setCurrency(currency);
			product.setStatus(status);

			properties_list.add(product);
			Log.i(TAG + "SIZE", properties_list.size() + "x");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			loadingMore = false;
		}
	}

	private void parseAndAppend(String jsonString) {
		try {
			JSONArray jsonArray = new JSONArray(jsonString);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObj = jsonArray.getJSONObject(i);
				parse(jsonObj);
			}
			loadingMore = false;
			adapter.notifyDataSetChanged();
			loadMorePrg.setVisibility(View.GONE);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			loadingMore = true;
			loadMorePrg.setVisibility(View.GONE);
		}
	}

	private String feedData(String pullQuery) {
		try {
			HttpGet httpGet = null;
			DefaultHttpClient httpClient = new DefaultHttpClient();
			if (pullQuery != null && !pullQuery.equalsIgnoreCase("")) {
				httpGet = new HttpGet(pullQuery);
			} else {
				httpGet = new HttpGet(tmpQuery);
			}
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
		;
		return jsonString.replaceAll("\\\\'", "'");
	}

	private class LoadMoreDataTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			if (isCancelled()) {
				return null;
			}
			return feedData(null);
		}

		@Override
		protected void onPostExecute(String result) {
			parseAndAppend(jsonString);
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			loadMorePrg.setVisibility(View.VISIBLE);
			loadingMore = true;
		}

		@Override
		protected void onCancelled() {
			loadingMore = false;
			loadMorePrg.setVisibility(View.GONE);
		}
	}

	private OnItemClickListener listViewOnClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(getActivity(), DetailActivity.class);
			intent.putExtra(constants.COMMON_KEY, properties_list.get(arg2)
					.getId());
			startActivity(intent);
		}
	};

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		int lastInScreen = firstVisibleItem + visibleItemCount;
		if ((lastInScreen == totalItemCount) && !loadingMore) {
			first += COUNT_ITEM_LOAD_MORE;
			tmpQuery += "&first=" + first + "&offset=" + COUNT_ITEM_LOAD_MORE;
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				new LoadMoreDataTask()
						.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			} else {
				new LoadMoreDataTask().execute();
			}
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

	}
}
