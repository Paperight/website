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

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
public class Article implements Serializable {

	@PersistenceContext
	transient EntityManager entityManager;
	
	private static final long serialVersionUID = -1216751128340901649L;
	private Long id;
	private DateTime createdDate;
	private String name;
	private String title;
	private String content;
	private int revision = 0;
	private String language = "en";
	private boolean published = false;
	
	public static final EntityManager entityManager() {
		EntityManager em = new Article().entityManager;
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
		return entityManager().createQuery("SELECT COUNT(o) FROM Article o", Long.class).getSingleResult();
	}

	public static List<Article> findAll() {
		return entityManager().createQuery("SELECT o FROM Article o", Article.class).getResultList();
	}
	
	public static List<Article> findLatestAll() {
		return entityManager().createQuery("SELECT o FROM Article o WHERE o.id in (SELECT MAX(a.id) FROM Article a GROUP By a.name)", Article.class).getResultList();
	}

	public static Article find(Long id) {
		if (id == null)
			return null;
		return entityManager().find(Article.class, id);
	}
	
	public static Article findLatestByName(String name) {
		List<Article> articles = entityManager().createQuery("SELECT o FROM Article o WHERE o.id IN (SELECT MAX(a.id) FROM Article a WHERE a.name = :name)", Article.class).setParameter("name", name).getResultList();
		return articles.isEmpty() ? null : articles.get(0);
	}
	
	@Transactional
	public void publishArticlesByName(String name, boolean isPublished) {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		this.entityManager.createQuery("UPDATE Article SET published = :published WHERE name = :name)").setParameter("published", isPublished).setParameter("name", name).executeUpdate();
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
			Article attached = find(this.getId());
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
	public Article merge() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		Article merged = this.entityManager.merge(this);
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
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Lob
	@Column(nullable = false)
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(nullable = false)
	public int getRevision() {
		return revision;
	}

	public void setRevision(int revision) {
		this.revision = revision;
	}

	@Column(length = 3, nullable = false)
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	@Column(nullable = false)
	public boolean isPublished() {
		return published;
	}

	public void setPublished(boolean published) {
		this.published = published;
	}

	//@Column(nullable = false)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
}
