package com.paperight;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

privileged aspect ApplicationSetting_Jdbc {
	
	declare @type: ApplicationSetting: @Configurable;
	declare @type: ApplicationSetting: @Entity;
	
	declare @method: public String ApplicationSetting.getName(): @Id;
	
	@PersistenceContext
	transient EntityManager ApplicationSetting.entityManager;
	
	public static final EntityManager ApplicationSetting.entityManager() {
		EntityManager em = new ApplicationSetting().entityManager;
		if (em == null)
			throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}
	
	public static ApplicationSetting ApplicationSetting.find(String name) {
		if (name == null)
			return null;
		return entityManager().find(ApplicationSetting.class, name);
	}
	
	@Transactional
	public ApplicationSetting ApplicationSetting.merge() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		ApplicationSetting merged = this.entityManager.merge(this);
		this.entityManager.flush();
		return merged;
	}

}
