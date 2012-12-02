package com.hackrgt.katanalocate.friendslist;

import android.graphics.Bitmap;

public class Friend {

	private String id, name;
	private boolean isAppUser;
	private Bitmap imgBitmap;

	public Friend(String id, String name) {
		this.id = id;
		this.name = name;
		isAppUser = false;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isAppUser() {
		return isAppUser;
	}

	public void setAppUser(boolean isAppUser) {
		this.isAppUser = isAppUser;
	}

	public Bitmap getImgBitmap() {
		return imgBitmap;
	}

	public void setImgBitmap(Bitmap imgBitmap) {
		this.imgBitmap = imgBitmap;
	}
}
