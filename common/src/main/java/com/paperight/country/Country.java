package com.paperight.country;

import java.io.Serializable;

public class Country implements Serializable {
	
	private static final long serialVersionUID = 4008333831192765433L;
	private String name;
	private String code;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
