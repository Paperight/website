package com.paperight.authentication;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.PrePersist;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import com.paperight.user.User;

@Entity
@Configurable
public class AuthenticationLog implements Serializable {

	private static final long serialVersionUID = 1L;

	@PersistenceContext
	transient EntityManager entityManager;

	private Long id;
	private User user;
	private DateTime createdDate;
	private AuthenticationLogContext context;
	
	public enum AuthenticationLogContext {
		COOKIE,
		FORM
	}
	
	public static final EntityManager entityManager() {
		EntityManager em = new AuthenticationLog().entityManager;
		if (em == null)
			throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}
	
	@SuppressWarnings("unused")
	@PrePersist
	private void onCreate() {
		this.setCreatedDate(new DateTime());
	}
	
	public static long count() {
		return entityManager().createQuery("SELECT COUNT(o) FROM AuthenticationLog o", Long.class).getSingleResult();
	}

	public static List<AuthenticationLog> findAll() {
		return entityManager().createQuery("SELECT o FROM AuthenticationLog o", AuthenticationLog.class).getResultList();
	}

	public static AuthenticationLog find(Long id) {
		if (id == null)
			return null;
		return entityManager().find(AuthenticationLog.class, id);
	}

	@Transactional
	public void persist() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		this.entityManager.persist(this);
	}

	@Transactional
	public void remove() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		if (this.entityManager.contains(this)) {
			this.entityManager.remove(this);
		} else {
			AuthenticationLog attached = find(this.getId());
			this.entityManager.remove(attached);
		}
	}

	@Transactional
	public void flush() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		this.entityManager.flush();
	}

	@Transactional
	public void clear() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		this.entityManager.clear();
	}

	@Transactional
	public AuthenticationLog merge() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		AuthenticationLog merged = this.entityManager.merge(this);
		this.entityManager.flush();
		return merged;
	}
	
	@Id
	@Column
	@GeneratedValue
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID", insertable = true, updatable = false, nullable = false)
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	@Column(nullable = false)
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	public DateTime getCreatedDate() {
		return createdDate;
	}
	
	public void setCreatedDate(DateTime createdDate) {
		this.createdDate = createdDate;
	}
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	public AuthenticationLogContext getContext() {
		return context;
	}
	
	public void setContext(AuthenticationLogContext context) {
		this.context = context;
	}
	
	
}
