package com.bk.lrandom.realestate;

import java.util.ArrayList;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.bk.lrandom.realestate.adapters.DrawerMenuAdapter;
import com.bk.lrandom.realestate.business.Ultils;
import com.bk.lrandom.realestate.business.UserSessionManager;
import com.bk.lrandom.realestate.fragments.AboutUsFragment;
import com.bk.lrandom.realestate.fragments.CitiesFragment;
import com.bk.lrandom.realestate.fragments.FanpageFragment;
import com.bk.lrandom.realestate.fragments.FeaturedFragment;
import com.bk.lrandom.realestate.fragments.FilterFragment;
import com.bk.lrandom.realestate.fragments.ProfileFragment;
import com.bk.lrandom.realestate.fragments.PropertiesFragment;
import com.bk.lrandom.realestate.fragments.CountyFragment;
import com.bk.lrandom.realestate.fragments.TypesFragment;
import com.bk.lrandom.realestate.interfaces.ProfileComunicator;
import com.bk.lrandom.realestate.models.DrawerMenuItem;
import com.bk.lrandom.realestate.models.User;

public class HomeActivity extends ActionBarParentActivity implements
		ProfileComunicator {
	DrawerLayout drawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	ListView lst;
	Intent intent;
	ArrayList<DrawerMenuItem> drawerItems;
	DrawerMenuAdapter drawerMenuAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_layout);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		lst = (ListView) findViewById(R.id.list_slidermenu);

		ArrayAdapter<String> drawerMenuTitles = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, getResources()
						.getStringArray(R.array.drawer_menus));

		drawerItems = new ArrayList<DrawerMenuItem>();
		drawerItems.add(new DrawerMenuItem(getResources().getString(
				R.string.login_label), R.drawable.ic_small_avatar));
		drawerItems.add(new DrawerMenuItem(drawerMenuTitles.getItem(0),
				R.drawable.ic_home));
		drawerItems.add(new DrawerMenuItem(drawerMenuTitles.getItem(1),
				R.drawable.ic_cup));
		drawerItems.add(new DrawerMenuItem(drawerMenuTitles.getItem(2),
				R.drawable.ic_list));
		drawerItems.add(new DrawerMenuItem(drawerMenuTitles.getItem(3),
				R.drawable.ic_county));
		drawerItems.add(new DrawerMenuItem(drawerMenuTitles.getItem(4),
				R.drawable.ic_city));
		drawerItems.add(new DrawerMenuItem(drawerMenuTitles.getItem(5),
				R.drawable.ic_filter));
		drawerItems.add(new DrawerMenuItem(drawerMenuTitles.getItem(6),
				R.drawable.ic_nearby));
		drawerItems.add(new DrawerMenuItem(drawerMenuTitles.getItem(7),
				R.drawable.ic_fanpage));
		drawerItems.add(new DrawerMenuItem(drawerMenuTitles.getItem(8),
				R.drawable.ic_info));
		drawerMenuAdapter = new DrawerMenuAdapter(this, drawerItems);

		lst.setAdapter(drawerMenuAdapter);
		lst.setOnItemClickListener(new DrawMenuClickListener());

		mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
				R.drawable.ic_navigation_drawer, R.string.app_name,
				R.string.app_name) {
			@Override
			public void onDrawerClosed(View drawerView) {
				// TODO Auto-generated method stub
				supportInvalidateOptionsMenu();
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				// TODO Auto-generated method stub
				super.onDrawerOpened(drawerView);
				supportInvalidateOptionsMenu();
			}
		};
		drawerLayout.setDrawerListener(mDrawerToggle);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		loadView(1);
	}

	private class DrawMenuClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			// TODO Auto-generated method stub
			loadView(position);
		}
	}

	private void loadView(int position) {
		Fragment fragment = null;
		if (!Ultils.isConnectingToInternet(HomeActivity.this)) {
			showMsg(getResources().getString(R.string.open_network));
		}
		switch (position) {
		case 0:
			UserSessionManager userSessionManager = new UserSessionManager(this);
			if (userSessionManager.getUserSession() != null) {
				fragment = ProfileFragment.newInstance();
			} else {
				Intent intent = new Intent(this, AuthenticationActivity.class);
				startActivity(intent);
			}
			changeActionBarTitle(getResources().getString(
					R.string.profile_label));
			break;

		case 1:
			fragment = PropertiesFragment.newInstance();
			changeActionBarTitle(getResources().getString(R.string.home_label));
			break;

		case 2:
			fragment = FeaturedFragment.newInstance();
			changeActionBarTitle(getResources().getString(R.string.featured));
			break;

		case 3:
			fragment = TypesFragment.newInstance();
			changeActionBarTitle(getResources().getString(R.string.type_label));
			break;

		case 4:
			fragment = CountyFragment.newInstance();
			changeActionBarTitle(getResources()
					.getString(R.string.county_label));
			break;

		case 5:
			fragment = CitiesFragment.newInstance();
			changeActionBarTitle(getResources().getString(R.string.city_label));
			break;

		case 6:
			fragment = FilterFragment.newInstance();
			changeActionBarTitle(getResources()
					.getString(R.string.filter_label));
			break;

		case 7:
			Intent intent = new Intent(HomeActivity.this, NearByActivity.class);
			startActivity(intent);
			break;

		case 8:
			fragment = FanpageFragment.newInstance();
			changeActionBarTitle(getResources().getString(R.string.fan_page));
			break;

		case 9:
			fragment = AboutUsFragment.newInstance();
			changeActionBarTitle(getResources().getString(
					R.string.about_us_label));
			break;
		default:
			break;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.content, fragment)
					.commit();
			lst.setItemChecked(position, true);
			lst.setSelection(position);
			drawerLayout.closeDrawer(lst);
		} else {
			Log.e("HomeActivity", "Error creating fragment");
		}
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.btn_action_upload:

			break;

		default:
			break;
		}

		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		// has login
		final DrawerMenuItem menuItem = new DrawerMenuItem();
		UserSessionManager sessionManager = new UserSessionManager(this);
		User user = sessionManager.getUserSession();
		if (user != null) {
			if (user.getAvt() != null && !user.getAvt().equalsIgnoreCase("")) {
				String avtString = "";
				if (Ultils.checkFacebookAvt(user.getAvt())) {
					avtString = user.getAvt();
				} else {
					avtString = getResources().getString(R.string.domain_url)
							+ user.getAvt();
				}
				menuItem.setAvt(avtString);
			}
			drawerItems.remove(0);
			menuItem.setTitle(user.getFullName());
			drawerItems.add(0, menuItem);
			drawerMenuAdapter.notifyDataSetChanged();
		} else {
			// if (Session.openActiveSession(this, false, callback) == null) {
			// // startLogin Activity
			// }
		}
	}

	@Override
	public void logout() {
		// TODO Auto-generated method stub
		loadView(1);
		drawerItems.remove(0);
		drawerItems.add(
				0,
				new DrawerMenuItem(getResources().getString(
						R.string.login_label), R.drawable.ic_small_avatar));
		drawerMenuAdapter.notifyDataSetChanged();
	}
}
