package com.bk.lrandom.realestate;

import java.util.Iterator;
import java.util.LinkedHashSet;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.bk.lrandom.realestate.business.Ultils;
import com.bk.lrandom.realestate.business.UserSessionManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

@SuppressLint("ShowToast")
public class ActionBarParentActivity extends ActionBarActivity {
	private LinkedHashSet<Integer> enableItems = new LinkedHashSet<Integer>();
	private LinkedHashSet<Integer> disableItems = new LinkedHashSet<Integer>();
	private Iterator<Integer> iter;

	public void setEnableItem(LinkedHashSet<Integer> items) {
		this.enableItems = items;
	}

	public void setDisableItem(LinkedHashSet<Integer> items) {
		this.disableItems = items;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		if (!disableItems.isEmpty()) {
			iter = disableItems.iterator();
			while (iter.hasNext()) {
				MenuItem item = menu.findItem(iter.next());
				item.setVisible(false);
			}
		}

		if (!enableItems.isEmpty()) {
			iter = enableItems.iterator();
			while (iter.hasNext()) {
				MenuItem item = menu.findItem(iter.next());
				item.setVisible(true);
			}
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			// NavUtils.navigateUpTo(this, new Intent(this,
			// HomeActivity.class));
			finish();
			break;

		case R.id.btn_action_upload:
			if (!Ultils.isConnectingToInternet(this)) {
				showMsg(getResources().getString(R.string.open_network));
				return false;
			}
			UserSessionManager userSession = new UserSessionManager(this);
			if (userSession.getUserSession() != null) {
				Intent intent = new Intent(this, SubmitPropertiesActivity.class);
				startActivity(intent);
			} else {
				Intent intent = new Intent(this, AuthenticationActivity.class);
				startActivity(intent);
			}
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void refreshActionBarMenu() {
		this.supportInvalidateOptionsMenu();
	}

	public void changeActionBarTitle(String title) {
		getSupportActionBar().setTitle(title);
	}

	public void showMsg(String msg) {
		Toast ts = Toast.makeText(this, msg, 5000);
		ts.show();
	}

	public void showDialog(String msg) {
		AlertDialog.Builder buidler = new AlertDialog.Builder(
				ActionBarParentActivity.this);
		buidler.setMessage(msg);
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

}
