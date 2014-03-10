package com.paperight.product;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.criteria4jpa.Criteria;
import org.criteria4jpa.CriteriaUtils;
import org.criteria4jpa.criterion.Restrictions;
import org.criteria4jpa.order.Order;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import com.paperight.theme.PaperightTheme;

@Configurable
@Entity
public class Poster implements Serializable {
	
	@PersistenceContext
	transient EntityManager entityManager;
	
	private static final long serialVersionUID = 5089482376247736862L;
	private Long id;
	private DateTime createdDate;
	private DateTime updatedDate;
	private String title;
	private String imageUrl;
	private String linkUrl;
	private int displayOrder;
	private PaperightTheme theme;

	public static final EntityManager entityManager() {
		EntityManager em = new Poster().entityManager;
		if (em == null)
			throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}
	
	@SuppressWarnings("unused")
	@PrePersist
	private void onCreate() {
		this.setCreatedDate(new DateTime());
		this.setUpdatedDate(this.getCreatedDate());
	}
	
	@SuppressWarnings("unused")
	@PreUpdate
	private void onUpdate() {
		this.setUpdatedDate(new DateTime());
	}
	
	public static long count() {
		return entityManager().createQuery("SELECT COUNT(o) FROM Poster o", Long.class).getSingleResult();
	}

	public static List<Poster> findAll() {
		return entityManager().createQuery("SELECT o FROM Poster o", Poster.class).getResultList();
	}

	public static Poster find(Long id) {
		if (id == null)
			return null;
		return entityManager().find(Poster.class, id);
	}
	
	@SuppressWarnings("unchecked")
	public static List<Poster> findAllOrdered() {
		Criteria criteria = CriteriaUtils.createCriteria(entityManager(), Poster.class);
		criteria.addOrder(Order.asc("displayOrder"));
		return (List<Poster>) criteria.getResultList();
	}

	@SuppressWarnings("unchecked")
	public static List<Poster> findPostersOrderedByThemeId(Long id) {
		Criteria criteria = CriteriaUtils.createCriteria(entityManager(), Poster.class);
		criteria.addOrder(Order.asc("displayOrder"));
		criteria.add(Restrictions.eq("theme.id", id));
		return (List<Poster>) criteria.getResultList();
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
			Poster attached = find(this.getId());
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
	public Poster merge() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		Poster merged = this.entityManager.merge(this);
		this.entityManager.flush();
		return merged;
	}
	
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	@Column(nullable=false, updatable=false)
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
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

	@Column(nullable = false)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(nullable = false)
	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	@Column(nullable = false)
	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	@Column(nullable=true)
	public int getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(int order) {
		this.displayOrder = order;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "THEME_ID", insertable = true, updatable = false, nullable = false)
	public PaperightTheme getTheme() {
		return theme;
	}

	public void setTheme(PaperightTheme theme) {
		this.theme = theme;
	}
}
