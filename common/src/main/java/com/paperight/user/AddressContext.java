package com.paperight.user;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Entity
public class AddressContext implements Serializable {

	private static final long serialVersionUID = 1908408072884722100L;
	private Long id;
	private DateTime createdDate;
	private DateTime updatedDate;
	private Address address;
	private AddressContextType type = AddressContextType.DEFAULT;
	
	@SuppressWarnings("unused")
	@PrePersist
	private void onCreate() {
		setCreatedDate(new DateTime());
		setUpdatedDate(getCreatedDate());
	}
	
	@SuppressWarnings("unused")
	@PreUpdate
	private void onUpdate() {
		setUpdatedDate(new DateTime());
	}

	@Id
	@Column
	@GeneratedValue
	public Long getId() {
		return id;
	}

	@SuppressWarnings("unused")
	private void setId(Long id) {
		this.id = id;
	}
	
	@Column(nullable = false)
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	public DateTime getCreatedDate() {
		return createdDate;
	}

	private void setCreatedDate(DateTime createdDate) {
		this.createdDate = createdDate;
	}
	
	@Column(nullable=false)
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	public DateTime getUpdatedDate() {
		return updatedDate;
	}

	private void setUpdatedDate(DateTime updatedDate) {
		this.updatedDate = updatedDate;
	}

	@OneToOne(cascade = CascadeType.ALL, optional=false)
	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	@Enumerated(EnumType.STRING)
	@Column(length=30)
	public AddressContextType getType() {
		return type;
	}

	public void setType(AddressContextType type) {
		this.type = type;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof AddressContext) {
			AddressContext addressContext = (AddressContext)obj;
			if (this.getId().equals(addressContext.getId())) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int h = 0;
		if (getId() != null) {
			h = getId().intValue();
		}
		return h;
	}
	
	
}
