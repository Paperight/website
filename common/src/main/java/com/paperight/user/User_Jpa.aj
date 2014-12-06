package com.paperight.user;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.apache.commons.lang3.StringUtils;
import org.criteria4jpa.Criteria;
import org.criteria4jpa.CriteriaUtils;
import org.criteria4jpa.criterion.MatchMode;
import org.criteria4jpa.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Configurable;

privileged aspect User_Jpa {
	
	declare @type: User: @Configurable;
	declare @type: User: @Entity;
	
	@PersistenceContext
	transient EntityManager User.entityManager;
	
	public static final EntityManager User.entityManager() {
		EntityManager em = new User().entityManager;
		if (em == null)
			throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}
	
	@PrePersist
	private void User.onCreate() {
		this.setCreatedDate(new DateTime());
		this.setUpdatedDate(this.getCreatedDate());
	}
	
	@PreUpdate
	private void User.onUpdate() {
		this.setUpdatedDate(new DateTime());
	}

	public static long User.count() {
		return entityManager().createQuery("SELECT COUNT(o) FROM User o", Long.class).getSingleResult();
	}

	public static List<User> User.findAll() {
		return entityManager().createQuery("SELECT o FROM User o", User.class).getResultList();
	}

	public static User User.find(Long id) {
		if (id == null)
			return null;
		return entityManager().find(User.class, id);
	}

	public static List<User> User.findEntries(int firstResult, int maxResults) {
		return entityManager()
				.createQuery("SELECT o FROM User o", User.class)
				.setFirstResult(firstResult).setMaxResults(maxResults)
				.getResultList();
	}
	
	public static List<User> User.findByCompany(long companyId){
		return entityManager()
				.createQuery("SELECT o FROM User o WHERE o.company.id = :companyId AND o.deleted = 0", User.class)
				.setParameter("companyId", companyId)
				.getResultList();
	}
	
	public static List<User> User.findByRole(Role role, int firstResult, int maxResults, boolean includeDeleted) {
		return entityManager()
				.createQuery("SELECT u FROM User u JOIN u.roles r WHERE r = :role AND u.deleted = :deleted ", User.class)
				.setParameter("role", role)
				.setParameter("deleted", includeDeleted)
				.setFirstResult(firstResult).setMaxResults(maxResults)
				.getResultList();
	}

	@Transactional
	public void User.persist() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		this.entityManager.persist(this);
	}

	@Transactional
	public void User.remove() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		if (this.entityManager.contains(this)) {
			this.entityManager.remove(this);
		} else {
			User attached = User.find(this.getId());
			this.entityManager.remove(attached);
		}
	}

	@Transactional
	public void User.flush() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		this.entityManager.flush();
	}

	@Transactional
	public void User.clear() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		this.entityManager.clear();
	}

	@Transactional
	public User User.merge() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		User merged = this.entityManager.merge(this);
		this.entityManager.flush();
		return merged;
	}
	
	public static User User.findByUsername(String username, boolean exactMath) {
		if (StringUtils.isBlank(username)) {
			return null;
		}
		Criteria criteria = CriteriaUtils.createCriteria(entityManager(), User.class);
		if (exactMath) {
			criteria.add( Restrictions.eq("username", username) );
		} else {
			criteria.add( Restrictions.like("username", username, MatchMode.ANYWHERE) );
			criteria.add( Restrictions.eq("deleted", false) );
		}
		return (User) criteria.getSingleResultOrNull();
	}
	
	public static User User.findByUsername(String username) {
		return User.findByUsername(username, true);
	}
	
	public static List<User> User.findByEmail(String username) {
		if (StringUtils.isBlank(username)) {
			return null;
		}
		Criteria criteria = CriteriaUtils.createCriteria( entityManager(), User.class);
		criteria.add( Restrictions.like("email", username, MatchMode.ANYWHERE) );
		criteria.add( Restrictions.eq("deleted", false) );
		return criteria.getResultList();
	}
	
}
