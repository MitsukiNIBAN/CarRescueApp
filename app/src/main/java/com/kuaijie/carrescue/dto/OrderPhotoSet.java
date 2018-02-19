package com.kuaijie.carrescue.dto;

import com.kuaijie.carrescue.entity.OrderPhoto;

import java.util.List;

import org.msgpack.annotation.Message;


@Message
public class OrderPhotoSet {
	List<OrderPhoto> photos;

	public List<OrderPhoto> getPhotos() {
		return photos;
	}

	public void setPhotos(List<OrderPhoto> photos) {
		this.photos = photos;
	}

	@Override
	public String toString() {
		return "OrderPhotoSet [photos=" + photos + "]";
	}
}
