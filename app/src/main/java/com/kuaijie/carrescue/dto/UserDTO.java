package com.kuaijie.carrescue.dto;

import java.io.Serializable;
import java.util.List;

import org.msgpack.annotation.Index;
import org.msgpack.annotation.Message;

@Message
public class UserDTO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Index(0)
	private Long id;
	@Index(1)
	private String accountName;
	@Index(2)
	private String phone;
	@Index(3)
	private String password;
	@Index(4)
	private String loginTime;
	@Index(5)
	private List<RoleDTO> roles;

	public boolean hasPower(String powerCode) {
		for (RoleDTO roleDTO : roles) {
			for (PowerDTO power : roleDTO.getPowers()) {
				if (power.getCode().equals(powerCode))
					return true;
			}
		}
		return false;
	}
	
	public long getUserId() {
		return id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}

	public List<RoleDTO> getRoles() {
		return roles;
	}

	public void setRoles(List<RoleDTO> roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		return "UserDTO [id=" + id + ", accountName=" + accountName + ", phone=" + phone + ", password=" + password
				+ ", loginTime=" + loginTime + ", roles=" + roles + "]";
	}
	
	
}
