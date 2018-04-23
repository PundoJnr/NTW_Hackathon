package com.bk.lrandom.realestate.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Types implements Parcelable {
	public static final String TAG_ID = "id";
	public static final String TAG_NAME = "name";
	int id;
	String name;

	public Types() {
		// TODO Auto-generated constructor stub
	}

	public Types(Parcel in) {
		id = in.readInt();
		name = in.readString();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
		dest.writeString(name);
	}

	public static final Parcelable.Creator<Types> CREATOR = new Parcelable.Creator<Types>() {
		@Override
		public Types createFromParcel(Parcel in) {
			// TODO Auto-generated method stub
			return new Types(in);
		}

		@Override
		public Types[] newArray(int size) {
			// TODO Auto-generated method stub
			return new Types[size];
		}
	};
}
