package com.paperight.theme;


import java.util.List;
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.PrePersist;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.criteria4jpa.Criteria;
import org.criteria4jpa.CriteriaUtils;
import org.criteria4jpa.criterion.Restrictions;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.context.Theme;
import org.springframework.util.Assert;

@Entity
@Configurable
public class PaperightTheme implements Theme {

	@PersistenceContext
	transient EntityManager entityManager;

	private Long id;
	private String name;
	private DateTime createdDate;

	private MessageSource messageSource;

	public static final EntityManager entityManager() {
		EntityManager em = new PaperightTheme().entityManager;
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
		return entityManager().createQuery("SELECT COUNT(o) FROM PaperightTheme o", Long.class).getSingleResult();
	}

	public static List<PaperightTheme> findAll() {
		return entityManager().createQuery("SELECT o FROM PaperightTheme o", PaperightTheme.class).getResultList();
	}

	public static PaperightTheme find(Long id) {
		if (id == null)
			return null;
		return entityManager().find(PaperightTheme.class, id);
	}

	public static PaperightTheme findByName(String name) {
		if (StringUtils.isBlank(name)) {
			return null;
		}
		try {
			Criteria criteria = CriteriaUtils.createCriteria(entityManager(), PaperightTheme.class);
			criteria.add(Restrictions.eq("name", name) );   
			return (PaperightTheme) criteria.getSingleResultOrNull();
		} catch (Exception e) {
			return null;
		}
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
			PaperightTheme attached = find(this.getId());
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
	public PaperightTheme merge() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		PaperightTheme merged = this.entityManager.merge(this);
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

	@Column(nullable = false)
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	public DateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(DateTime createdDate) {
		this.createdDate = createdDate;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PaperightTheme() {

	}

	/**
	 * Create a PaperightTheme.
	 * @param name the name of the theme
	 * @param messageSource the MessageSource that resolves theme messages
	 */
	public PaperightTheme(String name) {
		Assert.notNull(name, "Name must not be null");
		this.name = name;
	}

	@Transient
	@Override
	public MessageSource getMessageSource() {
		if (this.messageSource == null) {
			setMessageSource(new PaperightThemeMessageSource(this));
		}
		return this.messageSource;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@Column(nullable = false)
	@Override
	public String getName() {
		return name;
	}
	
	private class PaperightThemeMessageSource implements MessageSource {

		private PaperightTheme theme;

		public PaperightThemeMessageSource(PaperightTheme theme) {
			setTheme(theme);
		}
		
		public PaperightTheme getTheme() {
			return theme;
		}

		public void setTheme(PaperightTheme theme) {
			this.theme = theme;
		}

		@Override
		public String getMessage(MessageSourceResolvable arg0, Locale arg1)
				throws NoSuchMessageException {
			return null;
		}

		@Override
		public String getMessage(String arg0, Object[] arg1, Locale arg2)
				throws NoSuchMessageException {
			return theme.getName();
		}

		@Override
		public String getMessage(String arg0, Object[] arg1, String arg2,
				Locale arg3) {
			return null;
		}

	}

}
