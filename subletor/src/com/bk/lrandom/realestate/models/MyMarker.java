package com.bk.lrandom.realestate.models;

import android.os.Parcel;
import android.os.Parcelable;

public class MyMarker implements Parcelable{
	int id;
	String path;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public MyMarker() {
		// TODO Auto-generated constructor stub
	}
	
	public MyMarker(Parcel in) {
		id = in.readInt();
		path = in.readString();
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
		dest.writeString(path);
	}

	public static final Parcelable.Creator<Properties> CREATOR = new Parcelable.Creator<Properties>() {
		@Override
		public Properties createFromParcel(Parcel in) {
			// TODO Auto-generated method stub
			return new Properties(in);
		}

		@Override
		public Properties[] newArray(int size) {
			// TODO Auto-generated method stub
			return new Properties[size];
		}
	};

}
