package com.bk.lrandom.realestate.business;

import java.io.File;
import java.net.URL;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Shader.TileMode;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.bk.lrandom.realestate.R;
import com.bk.lrandom.realestate.models.User;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class Ultils {
	public static boolean isConnectingToInternet(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
		}
		return false;
	}

	public static File createDirOnSDCard(String dirName) {
		File folder = new File(Environment.getExternalStorageDirectory() + "/"
				+ dirName);
		boolean success = true;
		if (!folder.exists()) {
			success = folder.mkdir();
		}
		return folder;
	}

	public static String getFileName(URL extUrl) {
		String filename = "";
		String path = extUrl.getPath();
		String[] pathContents = path.split("[\\\\/]");
		if (pathContents != null) {
			int pathContentsLength = pathContents.length;
			System.out.println("Path Contents Length: " + pathContentsLength);
			for (int i = 0; i < pathContents.length; i++) {
				System.out.println("Path " + i + ": " + pathContents[i]);
			}
			String lastPart = pathContents[pathContentsLength - 1];
			String[] lastPartContents = lastPart.split("\\.");
			if (lastPartContents != null && lastPartContents.length > 1) {
				int lastPartContentLength = lastPartContents.length;
				System.out
						.println("Last Part Length: " + lastPartContentLength);
				String name = "";
				for (int i = 0; i < lastPartContentLength; i++) {
					System.out.println("Last Part " + i + ": "
							+ lastPartContents[i]);
					if (i < (lastPartContents.length - 1)) {
						name += lastPartContents[i];
						if (i < (lastPartContentLength - 2)) {
							name += ".";
						}
					}
				}
				String extension = lastPartContents[lastPartContentLength - 1];
				filename = name + "." + extension;
				System.out.println("Name: " + name);
				System.out.println("Extension: " + extension);
				System.out.println("Filename: " + filename);
			}
		}
		return filename;
	}

	public static Bitmap getRoundedShape(Bitmap bitmap, int width, int height) {
		Bitmap circleBitmap = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Bitmap.Config.ARGB_8888);

		BitmapShader shader = new BitmapShader(bitmap, TileMode.CLAMP,
				TileMode.CLAMP);
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		paint.setStrokeWidth(1);
		paint.setDither(true);
		paint.setShader(shader);
		Canvas c = new Canvas(circleBitmap);
		c.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
				bitmap.getWidth() / 2, paint);
		return circleBitmap;
	}

	public static void logout(Context context) {
		UserSessionManager sessionManager = new UserSessionManager(context);
		sessionManager.clearAll();
	}

	public static String getFacebookAvt(String fbId, int width, int height) {
		return "http://graph.facebook.com/" + fbId + "/picture?width=" + width
				+ "&height=" + height;
	}

	public static Boolean checkFacebookAvt(String path) {
		if (path.indexOf("http://graph.facebook.com") == -1) {
			return false;
		}
		return true;
	}

	public static User parseUser(JSONObject jsonObj) {
		User user = new User();
		try {
			int id = jsonObj.getInt(User.TAG_ID);
			String fbId = jsonObj.getString(User.TAG_FB_ID);
			String fullName = jsonObj.getString(User.TAG_FULL_NAME);
			String userName = jsonObj.getString(User.TAG_USER_NAME);
			String email = jsonObj.getString(User.TAG_EMAIL);
			String skype = jsonObj.getString(User.TAG_SKYPE);
			String phone = jsonObj.getString(User.TAG_PHONE);
			String address = jsonObj.getString(User.TAG_ADDRESS);
			String avt = jsonObj.getString(User.TAG_AVT);
			user.setId(id);
			user.setFbId(fbId);
			user.setFullName(fullName);
			user.setUserName(userName);
			user.setEmail(email);
			user.setSkype(skype);
			user.setPhone(phone);
			user.setAddress(address);
			user.setAvt(avt);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
		return user;
	}

	@SuppressLint("NewApi")
	public static int getScreenWidth(Context context) {
		int columnWidth;
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();

		final Point point = new Point();
		try {
			display.getSize(point);
		} catch (java.lang.NoSuchMethodError ignore) { // Older device
			point.x = display.getWidth();
			point.y = display.getHeight();
		}
		columnWidth = point.x;
		return columnWidth;
	}

	public static void loadAd(Context context, View view) {
		AdView adView = (AdView) view.findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder()
				.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
				.addTestDevice(
						context.getResources().getString(
								R.string.admob_test_device_ids)).build();
		adView.loadAd(adRequest);
	}
	
	public static void loadAd(Context context, AdView adView) {
		AdRequest adRequest = new AdRequest.Builder()
				.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
				.addTestDevice(
						context.getResources().getString(
								R.string.admob_test_device_ids)).build();
		adView.loadAd(adRequest);
	}
}
