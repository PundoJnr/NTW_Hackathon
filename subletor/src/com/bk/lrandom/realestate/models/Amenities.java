package com.bk.lrandom.realestate.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Amenities implements Parcelable {
	int id;
	String name;
	private Boolean selected = false;
	private Boolean checked = false;

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	public Boolean getSelected() {
		return selected;
	}

	public void setSelected(Boolean selected) {
		this.selected = selected;
	}

	public Amenities() {
		// TODO Auto-generated constructor stub
	}

	public Amenities(Parcel in) {
		// TODO Auto-generated constructor stub
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

	public static final Parcelable.Creator<Amenities> CREATOR = new Parcelable.Creator<Amenities>() {
		@Override
		public Amenities createFromParcel(Parcel in) {
			// TODO Auto-generated method stub
			return new Amenities(in);
		}

		@Override
		public Amenities[] newArray(int size) {
			// TODO Auto-generated method stub
			return new Amenities[size];
		}
	};
}
