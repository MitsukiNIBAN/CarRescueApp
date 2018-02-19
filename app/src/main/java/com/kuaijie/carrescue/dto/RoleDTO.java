package com.kuaijie.carrescue.dto;

import java.util.List;

import org.msgpack.annotation.Index;
import org.msgpack.annotation.Message;
@Message
public class RoleDTO {
	@Index(0)
	private long id;
	@Index(1)
	private String position;
	@Index(2)
	private List<PowerDTO> powers;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public List<PowerDTO> getPowers() {
		return powers;
	}
	public void setPowers(List<PowerDTO> powers) {
		this.powers = powers;
	}
	
	
}
