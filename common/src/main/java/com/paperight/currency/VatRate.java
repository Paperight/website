package com.paperight.currency;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Transient;

import org.criteria4jpa.Criteria;
import org.criteria4jpa.CriteriaUtils;
import org.criteria4jpa.criterion.Restrictions;
import org.criteria4jpa.order.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import com.paperight.country.Country;
import com.paperight.country.CountryService;

@Entity
@Configurable
public class VatRate implements Serializable {
	
	@PersistenceContext
	transient EntityManager entityManager;
	
	private static final long serialVersionUID = 5493261855734971701L;
	private String countryCode;
	private Country country;
	private BigDecimal rate0;
	private BigDecimal rate1;
	private BigDecimal rate2;
	private VatRateType type = VatRateType.VAT;
	
	public enum VatRateType {
		VAT,
		GST;
	}
	
	@Transient
	@Autowired
	private CountryService countryService;
	
	public static final EntityManager entityManager() {
		EntityManager em = new VatRate().entityManager;
		if (em == null)
			throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}
	
	public static List<VatRate> findAll() {
		return entityManager().createQuery("SELECT o FROM VatRate o", VatRate.class).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public static List<VatRate> findAllOrdered() {
		Criteria criteria = CriteriaUtils.createCriteria(entityManager(), VatRate.class);
		criteria.addOrder(Order.asc("countryCode"));
		return (List<VatRate>) criteria.getResultList();
	}
	
	public static VatRate findByCountryCode(String countryCode) {
		if (countryCode == null) {
			return null;
		}
		Criteria criteria = CriteriaUtils.createCriteria(entityManager(), VatRate.class);
		criteria.add( Restrictions.eq("countryCode", countryCode) );
		return (VatRate) criteria.getSingleResultOrNull();
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
			VatRate attached = findByCountryCode(this.getCountryCode());
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
	public VatRate merge() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		VatRate merged = this.entityManager.merge(this);
		this.entityManager.flush();
		return merged;
	}

	@Id
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

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	public VatRateType getType() {
		return type;
	}

	public void setType(VatRateType type) {
		this.type = type;
	}

	public BigDecimal getRate0() {
		return rate0;
	}

	public void setRate0(BigDecimal rate0) {
		this.rate0 = rate0;
	}

	public BigDecimal getRate1() {
		return rate1;
	}

	public void setRate1(BigDecimal rate1) {
		this.rate1 = rate1;
	}

	public BigDecimal getRate2() {
		return rate2;
	}

	public void setRate2(BigDecimal rate2) {
		this.rate2 = rate2;
	}
	
}
