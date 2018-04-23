package com.bk.lrandom.realestate.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import android.os.Parcel;
import android.os.Parcelable;

public class Properties implements Parcelable,ClusterItem{
	public static final String TAG_ID = "id";
	public static final String TAG_TITLE = "title";
	public static final String TAG_TYPES_NAME = "type_name";
	public static final String TAG_TYPES_ID = "type_id";
	public static final String TAG_COUNTY_NAME = "county_name";
	public static final String TAG_COUNTY_ID = "county_id";
	public static final String TAG_FULL_NAME = "user_name";
	public static final String TAG_DATE_POST = "updated_at";
	public static final String TAG_IMAGES = "image_path";
	public static final String TAG_PRICE = "price";
	public static final String TAG_CONTENT = "content";
	public static final String TAG_CURRENCY = "currency";
	public static final String TAG_CITIES = "cities_name";
	public static final String TAG_ADDESS = "address";
	public static final String TAG_BATHROOM = "bathrooms";
	public static final String TAG_BEDROOM = "bedrooms";
	public static final String TAG_PURPOSE = "purpose";
	public static final String TAG_AREA = "area";
	public static final String TAG_TIME_RATE = "time_rate";
	public static final String TAG_STATUS = "status";

	int id;
	String title;
	String price;
	String images_path;
	String provinces;
	int county_id;
	String types;
	int types_id;
	int user_id;
	String user_name;
	String date_post;
	String content;
	String currency;
	String cities;
	String address;
	String bathroom;
	String bedroom;
	String area;
	int time_rate;
	String status;
	String marker_path;
	double lat;
	double lng;

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public String getMarker_path() {
		return marker_path;
	}

	public void setMarker_path(String marker_path) {
		this.marker_path = marker_path;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getTime_rate() {
		return time_rate;
	}

	public void setTime_rate(int time_rate) {
		this.time_rate = time_rate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getImages_path() {
		return images_path;
	}

	public void setImages_path(String images_path) {
		this.images_path = images_path;
	}

	public String getProvinces() {
		return provinces;
	}

	public void setProvinces(String provinces) {
		this.provinces = provinces;
	}

	public int getCounty_id() {
		return county_id;
	}

	public void setCounty_id(int county_id) {
		this.county_id = county_id;
	}

	public String getTypes() {
		return types;
	}

	public void setTypes(String types) {
		this.types = types;
	}

	public int getTypes_id() {
		return types_id;
	}

	public void setTypes_id(int types_id) {
		this.types_id = types_id;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getDate_post() {
		return date_post;
	}

	public void setDate_post(String date_post) {
		this.date_post = date_post;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCities() {
		return cities;
	}

	public void setCities(String cities) {
		this.cities = cities;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getBathroom() {
		return bathroom;
	}

	public void setBathroom(String bathroom) {
		this.bathroom = bathroom;
	}

	public String getBedroom() {
		return bedroom;
	}

	public void setBedroom(String bedroom) {
		this.bedroom = bedroom;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public Properties() {
		// TODO Auto-generated constructor stub
	}

	public Properties(Parcel in) {
		id = in.readInt();
		title = in.readString();
		price = in.readString();
		images_path = in.readString();
		provinces = in.readString();
		county_id = in.readInt();
		types = in.readString();
		types_id = in.readInt();
		user_id = in.readInt();
		user_name = in.readString();
		date_post = in.readString();
		content = in.readString();
		currency = in.readString();
		cities = in.readString();
		address = in.readString();
		bathroom = in.readString();
		bedroom = in.readString();
		area = in.readString();
		time_rate = in.readInt();
		status = in.readString();
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
		dest.writeString(title);
		dest.writeString(price);
		dest.writeString(images_path);
		dest.writeString(types);
		dest.writeInt(types_id);
		dest.writeString(provinces);
		dest.writeInt(county_id);
		dest.writeInt(user_id);
		dest.writeString(user_name);
		dest.writeString(date_post);
		dest.writeString(content);
		dest.writeString(currency);
		dest.writeString(cities);
		dest.writeString(address);
		dest.writeString(bathroom);
		dest.writeString(bedroom);
		dest.writeString(area);
		dest.writeInt(time_rate);
		dest.writeString(status);
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

	@Override
	public LatLng getPosition() {
		// TODO Auto-generated method stub
		return new LatLng(lat, lng);
	}
}
