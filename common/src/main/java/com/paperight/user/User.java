package com.paperight.user;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.annotate.JsonBackReference;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;
import org.springframework.transaction.annotation.Transactional;

public class User implements Serializable {

	private static final long serialVersionUID = 2936736450886451500L;
	private Long id;
	private DateTime createdDate;
	private DateTime updatedDate;
	private boolean enabled;
	private boolean verified;
	private String username;

	private String password;
	private Set<Role> roles = new HashSet<Role>();
	private String firstName;
	private String lastName;

	private Company company;
	private String email;
	private boolean subscribed;
	private boolean deleted;
	private UserType type = UserType.STANDARD;
	private Set<Permission> permissions = new HashSet<Permission>();
	private boolean closed = false;

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

	@SuppressWarnings("unused")
	private void setCreatedDate(DateTime createdDate) {
		this.createdDate = createdDate;
	}

	@Column(nullable = false)
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	public DateTime getUpdatedDate() {
		return updatedDate;
	}

	@SuppressWarnings("unused")
	private void setUpdatedDate(DateTime updatedDate) {
		this.updatedDate = updatedDate;
	}

	@Size(min = 0, max = 50)
	@Column(unique = true, nullable = false)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(nullable = false)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(nullable = false)
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Column(nullable = true)
	public boolean getVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	@ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
	@CollectionTable(name = "USER_ROLES", joinColumns = @JoinColumn(name = "USER_ID"))
	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false)
	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public void addRole(Role role) {
		if (!hasRole(role)) {
			roles.add(role);
		}
	}

	public void removeRole(Role role) {
		if (hasRole(role)) {
			roles.remove(role);
		}
	}

	public boolean hasRole(Role role) {
		return roles.contains(role);
	}

	@NotEmpty
	@Size(min = 2, max = 255)
	@Column(nullable = false)
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@NotEmpty
	@Size(min = 2, max = 255)
	@Column(nullable = false)
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@JsonBackReference
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "COMPANY_ID", insertable = true, updatable = false, nullable = false)
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	@NotEmpty
	@Size(min = 2, max = 255)
	@Email
	@Column(nullable = false)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(nullable = false)
	public boolean isSubscribed() {
		return subscribed;
	}

	public void setSubscribed(boolean subscribed) {
		this.subscribed = subscribed;
	}
	
	@Transient
	public String getFullName() {
		String result = "";
		if (!StringUtils.isBlank(getFirstName())) {
			result += getFirstName(); 
		}
		if (!StringUtils.isBlank(getLastName())) {
			result += " " +  getLastName(); 
		}
		return StringUtils.trim(result);
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	@Enumerated(EnumType.STRING)
	public UserType getType() {
		return type;
	}

	public void setType(UserType type) {
		this.type = type;
	}

	@ElementCollection(targetClass = Permission.class, fetch = FetchType.EAGER)
	@CollectionTable(name = "USER_PERMISSIONS", joinColumns = @JoinColumn(name = "USER_ID"))
	@Enumerated(EnumType.STRING)
	@Column(name = "permission", nullable = false)
	public Set<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}
	
	public void addPermission(Permission permission) {
		if (!hasPermission(permission)) {
			permissions.add(permission);
		}
	}

	public void removePermission(Permission permission) {
		if (hasPermission(permission)) {
			permissions.remove(permission);
		}
	}

	public boolean hasPermission(Permission permission) {
		return permissions.contains(permission);
	}

	public boolean isClosed() {
		return closed;
	}

	public void setClosed(boolean closed) {
		this.closed = closed;
	}
	
	@Transactional
	public void delete() {
		setUsername(String.valueOf(getId()) + "_DELETED");
		setEmail(getEmail() + "_DELETED");
		setDeleted(true);
		merge();
	}
	
	@Transactional
	public void close() {
		setClosed(true);
		merge();
	}
	
	@Transactional
	public void reopen() {
		setClosed(false);
		merge();
	}
	
	@Transactional
	public void disable() {
		setEnabled(false);
		merge();
	}
	
	@Transactional
	public void enable() {
		setEnabled(true);
		merge();
	}
	

}
