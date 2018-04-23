package com.bk.lrandom.realestate.models;

public class DrawerMenuItem {
	private String title;
	private int icon;
	private String avt;

	public String getAvt() {
		return avt;
	}

	public void setAvt(String avt) {
		this.avt = avt;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	public DrawerMenuItem() {

	}

	public DrawerMenuItem(String title, int icon) {
		super();
		this.title = title;
		this.icon = icon;
	}

}
