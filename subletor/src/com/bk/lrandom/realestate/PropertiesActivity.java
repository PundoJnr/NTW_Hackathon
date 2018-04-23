package com.bk.lrandom.realestate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.bk.lrandom.realestate.adapters.ProductsAdapter;
import com.bk.lrandom.realestate.business.JSONFetchTask;
import com.bk.lrandom.realestate.business.Ultils;
import com.bk.lrandom.realestate.confs.constants;
import com.bk.lrandom.realestate.models.Properties;
import com.google.android.gms.ads.AdView;

@SuppressLint("NewApi")
public class PropertiesActivity extends ActionBarParentActivity implements
		OnScrollListener {
	ArrayList<Properties> properties_list = new ArrayList<Properties>();
	ListView list;
	ProductsAdapter adapter;
	String TAG = "ProductsActivity";
	JSONFetchTask jsonFetchTask;
	static InputStream is = null;
	static JSONObject jObj = null;
	static String jsonString = "";
	String query = null, tmpQuery = null;
	int COUNT_ITEM_LOAD_MORE = 5;
	int first = 0;
	int user_id = 0;
	ProgressBar loadMorePrg;
	boolean loadingMore = true;
	Button btnAll, btnSell, btnBuy;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview_endless_container_layout);
		query = getResources().getString(R.string.estate_json_url)
				+ "estates?x=trick";
		list = (ListView) findViewById(R.id.list);
		list.setOnItemClickListener(listViewOnClick);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			if (bundle.containsKey(constants.TITLE_KEY)) {
				String title = bundle.getString(constants.TITLE_KEY);
				if (title != null && title != "") {
					try {
						title = URLEncoder.encode(title, "utf-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					query += "&title=" + title;
				}
			}

			if (bundle.containsKey(constants.TYPE_ID_KEY)) {
				int id = bundle.getInt(constants.TYPE_ID_KEY);
				if (id != 0) {
					query += "&types_id=" + id;
				}
			}
			if (bundle.containsKey(constants.COUNTY_ID_KEY)) {
				int id = bundle.getInt(constants.COUNTY_ID_KEY);
				if (id != 0) {
					query += "&county=" + id;
				}
			}

			if (bundle.containsKey(constants.PURPOSE_KEY)) {
				int id = bundle.getInt(constants.PURPOSE_KEY);
				query += "&purpose=" + id;

			}

			if (bundle.containsKey(constants.CITY_ID_KEY)) {
				int id = bundle.getInt(constants.CITY_ID_KEY);
				if (id != 0) {
					query += "&cities=" + id;
				}
			}

			if (bundle.containsKey(constants.USER_ID_KEY)) {
				int id = bundle.getInt(constants.USER_ID_KEY);
				if (id != 0) {
					query += "&user_id=" + id;
					user_id = id;
				}
			}

			if (bundle.containsKey(constants.USER_POST_KEY)) {
				int id = bundle.getInt(constants.USER_POST_KEY);
				if (id != 0) {
					query += "&user_id=" + id;
				}
			}

			if (bundle.containsKey(constants.AREA_KEY)) {
				String area = bundle.getString(constants.AREA_KEY);
				if (area != null && area != "") {
					try {
						area = URLEncoder.encode(area, "utf-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					query += "&area=" + area;
				}
			}

			if (bundle.containsKey(constants.BEDROOM_KEY)) {
				String bedroom = bundle.getString(constants.BEDROOM_KEY);
				if (bedroom != null && bedroom != "") {
					try {
						bedroom = URLEncoder.encode(bedroom, "utf-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					query += "&bedrooms=" + bedroom;
				}
			}

			if (bundle.containsKey(constants.MIN_PRICE_KEY)) {
				String minPrice = bundle.getString(constants.MIN_PRICE_KEY);
				if (minPrice != null && minPrice != "") {
					try {
						minPrice = URLEncoder.encode(minPrice, "utf-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					query += "&min_price=" + minPrice;
				}
			}

			if (bundle.containsKey(constants.MAX_PRICE_KEY)) {
				String maxPrice = bundle.getString(constants.MAX_PRICE_KEY);
				if (maxPrice != null && maxPrice != "") {
					try {
						maxPrice = URLEncoder.encode(maxPrice, "utf-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					query += "&max_price=" + maxPrice;
				}
			}
			Log.i(TAG, query);

		}

		adapter = new ProductsAdapter(this, R.layout.properties_item_layout,
				properties_list);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout footerView = (LinearLayout) inflater.inflate(
				R.layout.footer_loadmore_layout, null);
		loadMorePrg = (ProgressBar) footerView.findViewById(R.id.prgLoadMore);
		list.addFooterView(footerView);
		list.setAdapter(adapter);
		list.setOnScrollListener(this);

		tmpQuery = query + "&first=" + first + "&offset="
				+ COUNT_ITEM_LOAD_MORE;
		new LoadMoreDataTask().execute();
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setTitle(getResources().getString(R.string.properties_label));
		Ultils.loadAd(this, (AdView) findViewById(R.id.adView));
	}

	private void parse(JSONObject jsonObj) {
		try {
			int id = jsonObj.getInt(Properties.TAG_ID);
			String name = jsonObj.getString(Properties.TAG_TITLE);
			String price = jsonObj.getString(Properties.TAG_PRICE);
			String type = jsonObj.getString(Properties.TAG_TYPES_NAME);
			String address = jsonObj.getString(Properties.TAG_ADDESS);
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
			product.setAddress(address);
			product.setBathroom(bathroom);
			product.setBedroom(bedroom);
			product.setArea(area);
			product.setTime_rate(timeRate);
			product.setCurrency(currency);
			product.setStatus(status);

			properties_list.add(product);

			Log.i(TAG, properties_list.size() + "x");
		} catch (Exception e) {
			// TODO: handle exception
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

	private String feedJson(String pullQuery) {
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
		return jsonString;
	}

	private class LoadMoreDataTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			if (isCancelled()) {
				return null;
			}
			return feedJson(null);
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
			Intent intent;
			if (user_id == 0) {
				intent = new Intent(PropertiesActivity.this,
						DetailActivity.class);
				intent.putExtra(constants.COMMON_KEY, properties_list.get(arg2)
						.getId());
			} else {
				intent = new Intent(PropertiesActivity.this,
						MyDetailActivity.class);
				intent.putExtra(constants.COMMON_KEY, properties_list.get(arg2)
						.getId());
			}

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
