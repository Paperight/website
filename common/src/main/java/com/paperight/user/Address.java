package com.paperight.user;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.paperight.country.Country;
import com.paperight.country.CountryService;

@Entity
@Configurable
public class Address implements Serializable {

	private static final long serialVersionUID = 439249729664012415L;
	private Long id;
	private String addressLine1;
	private String addressLine2;
	private String addressLine3;
	private String addressLine4;
	private String addressLine5;
	private String addressLine6;
	private String postalCode;
	private String countryCode;
	private Country country;
	private String longitude;
	private String latitude;

	@Transient
	@Autowired
	transient private CountryService countryService;

	@Id
	@Column
	@GeneratedValue
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@NotEmpty
	@Size(min = 2, max = 255)
	@Column(nullable = false)
	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	@Size(min = 0, max = 255)
	@Column(nullable = true)
	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	@Size(min = 0, max = 255)
	@Column(nullable = true)
	public String getAddressLine3() {
		return addressLine3;
	}

	public void setAddressLine3(String addressLine3) {
		this.addressLine3 = addressLine3;
	}

	@Size(min = 0, max = 255)
	@Column(nullable = true)
	public String getAddressLine4() {
		return addressLine4;
	}

	public void setAddressLine4(String addressLine4) {
		this.addressLine4 = addressLine4;
	}

	@Size(min = 0, max = 255)
	@Column(nullable = true)
	public String getAddressLine5() {
		return addressLine5;
	}

	public void setAddressLine5(String addressLine5) {
		this.addressLine5 = addressLine5;
	}

	@Size(min = 0, max = 255)
	@Column(nullable = true)
	public String getAddressLine6() {
		return addressLine6;
	}

	public void setAddressLine6(String addressLine6) {
		this.addressLine6 = addressLine6;
	}

	@Size(min = 0, max = 255)
	@Column(nullable = false)
	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	@Column(nullable = false)
	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		country = countryService.findByCode(countryCode);
		this.countryCode = countryCode;
	}

	@Transient
	public Country getCountry() {
		return country;
	}

	@Column(nullable = true)
	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	@Column(nullable = true)
	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	@Override
	public String toString() {
		String result = getAddressLine1();
		if (!StringUtils.isBlank(getAddressLine2())) {
			result = result + ", " + getAddressLine2();
		}
		if (!StringUtils.isBlank(getAddressLine3())) {
			result = result + ", " + getAddressLine3();
		}
		if (!StringUtils.isBlank(getAddressLine4())) {
			result = result + ", " + getAddressLine4();
		}
		if (!StringUtils.isBlank(getPostalCode())) {
			result = result + ", " + getPostalCode();
		}
		if (getCountry() != null) {
			result = result + ", " + getCountry().getName();
		}
		return result;
	}
}