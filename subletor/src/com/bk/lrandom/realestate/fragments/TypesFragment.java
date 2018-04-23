package com.bk.lrandom.realestate.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.bk.lrandom.realestate.PropertiesActivity;
import com.bk.lrandom.realestate.R;
import com.bk.lrandom.realestate.adapters.TypesAdapter;
import com.bk.lrandom.realestate.business.JSONFetchTask;
import com.bk.lrandom.realestate.business.Ultils;
import com.bk.lrandom.realestate.confs.constants;
import com.bk.lrandom.realestate.models.Types;

public class TypesFragment extends Fragment {
	ArrayList<Types> types_list = new ArrayList<Types>();
	ListView list;
	TypesAdapter adapter;
	String TAG = "TypeFragment";
	ProgressBar loadMorePrg;
	LayoutInflater inflater;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		inflater = (LayoutInflater) activity.getLayoutInflater();
	}

	public static final TypesFragment newInstance() {
		// TODO Auto-generated constructor stub
		TypesFragment categoriesFragment = new TypesFragment();
		return categoriesFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.listview_container_layout, null);
		JSONFetchTask jsonFetchTask = new JSONFetchTask(getResources()
				.getString(R.string.type_json_url) + "type", handler);
		jsonFetchTask.execute();
		list = (ListView) view.findViewById(R.id.list);
		LinearLayout footerView = (LinearLayout) inflater.inflate(
				R.layout.footer_loadmore_layout, null);
		loadMorePrg = (ProgressBar) footerView.findViewById(R.id.prgLoadMore);
		list.addFooterView(footerView);
		list.setAdapter(adapter);
		Ultils.loadAd(this.getActivity(), view);
		return view;
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Bundle data = msg.getData();
			Log.i(TAG, "Handler");
			if (data.containsKey(JSONFetchTask.KEY_RESPONSE)) {
				Log.i(TAG, "Handler");
				String jsonString = data.getString(JSONFetchTask.KEY_RESPONSE);
				try {
					JSONArray jsonArray = new JSONArray(jsonString);
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject categoriesJSON = jsonArray.getJSONObject(i);
						int id = categoriesJSON.getInt(Types.TAG_ID);
						String name = categoriesJSON.getString(Types.TAG_NAME);
						Types categories = new Types();
						categories.setId(id);
						categories.setName(name);
						types_list.add(categories);
					}
					Log.i(TAG, types_list.size() + "");
					adapter = new TypesAdapter(getActivity(),
							R.layout.common_item_layout, types_list);
					list.setAdapter(adapter);
					list.setOnItemClickListener(onItemClickListener);
					loadMorePrg.setVisibility(View.GONE);
				} catch (Exception e) {
					// TODO: handle exception
					loadMorePrg.setVisibility(View.GONE);
					e.printStackTrace();
				}
			}
		};
	};

	private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(getActivity(), PropertiesActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt(constants.TYPE_ID_KEY, types_list.get(arg2)
					.getId());
			intent.putExtras(bundle);
			startActivity(intent);
		}
	};
}
