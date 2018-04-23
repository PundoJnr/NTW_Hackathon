package com.bk.lrandom.realestate.models;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
	public static final String TAG_ID = "id";
	public static final String TAG_FB_ID = "fb_id";
	public static final String TAG_FULL_NAME = "full_name";
	public static final String TAG_USER_NAME = "user_name";
	public static final String TAG_EMAIL = "email";
	public static final String TAG_PHONE = "phone";
	public static final String TAG_ADDRESS = "address";
	public static final String TAG_WEBSITES = "website";
	public static final String TAG_AVT = "avt";
	public static final String TAG_SKYPE = "skype";

	int id;
	String fbId;
	String fullName;
	String email;
	String website;
	String phone;
	String address;
	String userName;
	String avt;
	String skype;

	public String getSkype() {
		return skype;
	}

	public void setSkype(String skype) {
		this.skype = skype;
	}

	public String getAvt() {
		return avt;
	}

	public void setAvt(String avt) {
		this.avt = avt;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFbId() {
		return fbId;
	}

	public void setFbId(String fbId) {
		this.fbId = fbId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeInt(id);
		dest.writeString(fbId);
		dest.writeString(fullName);
		dest.writeString(email);
		dest.writeString(website);
		dest.writeString(phone);
		dest.writeString(address);
		dest.writeString(userName);
		dest.writeString(avt);
		dest.writeString(skype);
	}

	public User(Parcel in) {
		// TODO Auto-generated constructor stub
		id = in.readInt();
		fbId = in.readString();
		fullName = in.readString();
		email = in.readString();
		website = in.readString();
		phone = in.readString();
		address = in.readString();
		userName = in.readString();
		avt = in.readString();
		skype = in.readString();
	}

	public User() {
		// TODO Auto-generated constructor stub
	}

	public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
		@Override
		public User createFromParcel(Parcel in) {
			// TODO Auto-generated method stub
			return new User(in);
		}

		@Override
		public User[] newArray(int size) {
			// TODO Auto-generated method stub
			return new User[size];
		}
	};
}
