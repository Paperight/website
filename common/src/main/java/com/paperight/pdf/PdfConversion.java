package com.paperight.pdf;

import java.io.Serializable;

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

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import com.paperight.product.Product;

@Configurable
@Entity
public class PdfConversion implements Serializable {

	@PersistenceContext
	transient EntityManager entityManager;
	
	private static final long serialVersionUID = -5436152617989737768L;
	private Long id;
	private DateTime createdDate;
	private DateTime updatedDate;
	private String originalFilename;
	private String oneUpFilename;
	private String twoUpFilename;
	private String a5Filename;
	private Product product;
	
	public static final EntityManager entityManager() {
		EntityManager em = new PdfConversion().entityManager;
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
	
	public static PdfConversion find(Long id) {
		if (id == null)
			return null;
		return entityManager().find(PdfConversion.class, id);
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
			PdfConversion attached = find(this.getId());
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
	public PdfConversion merge() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		PdfConversion merged = this.entityManager.merge(this);
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

	public String getOneUpFilename() {
		return oneUpFilename;
	}

	public void setOneUpFilename(String oneUpFilename) {
		this.oneUpFilename = oneUpFilename;
	}

	public String getTwoUpFilename() {
		return twoUpFilename;
	}

	public void setTwoUpFilename(String twoUpFilename) {
		this.twoUpFilename = twoUpFilename;
	}

	public String getOriginalFilename() {
		return originalFilename;
	}

	public void setOriginalFilename(String originalFilename) {
		this.originalFilename = originalFilename;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PRODUCT_ID", insertable = true, updatable = true, nullable = true)
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getA5Filename() {
		return a5Filename;
	}

	public void setA5Filename(String a5Filename) {
		this.a5Filename = a5Filename;
	}
	
}
