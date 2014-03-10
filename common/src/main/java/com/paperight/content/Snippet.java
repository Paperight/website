package com.paperight.content;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.PersistenceContext;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.criteria4jpa.Criteria;
import org.criteria4jpa.CriteriaUtils;
import org.criteria4jpa.criterion.Restrictions;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
public class Snippet implements Serializable {
	
	@PersistenceContext
	transient EntityManager entityManager;

	private static final long serialVersionUID = -590338353378784215L;
	private Long id;
	private DateTime createdDate;
	private DateTime updatedDate;
	private String groupName;
	private String name;
	private String value;
	private String language = "en";
	private boolean multiline = false;
	
	public static final EntityManager entityManager() {
		EntityManager em = new Snippet().entityManager;
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
		return entityManager().createQuery("SELECT COUNT(o) FROM Snippet o", Long.class).getSingleResult();
	}

	public static List<Snippet> findAll() {
		return entityManager().createQuery("SELECT o FROM Snippet o", Snippet.class).getResultList();
	}

	public static Snippet find(Long id) {
		if (id == null)
			return null;
		return entityManager().find(Snippet.class, id);
	}
	
	public static List<Snippet> findByGroupName(String groupName) {
		Criteria criteria = CriteriaUtils.createCriteria(entityManager(), Snippet.class);
		criteria.add( Restrictions.eq("groupName", groupName) );
		return (List<Snippet>) criteria.getResultList();
	}
	
	public static List<String> findGroups() {
		return entityManager().createQuery("SELECT DISTINCT(groupName) FROM Snippet o", String.class).getResultList();
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
			Snippet attached = find(this.getId());
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
	public Snippet merge() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		Snippet merged = this.entityManager.merge(this);
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
	
	@Column(nullable = false)
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	public DateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(DateTime createdDate) {
		this.createdDate = createdDate;
	}

	@Column(nullable = false)
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	public DateTime getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(DateTime updatedDate) {
		this.updatedDate = updatedDate;
	}

	@Column(nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Lob
	@Column(nullable = false)
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Column(nullable = false)
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String group) {
		this.groupName = group;
	}

	@Column(length = 3, nullable = false)
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public boolean isMultiline() {
		return multiline;
	}

	public void setMultiline(boolean multiline) {
		this.multiline = multiline;
	}

}
