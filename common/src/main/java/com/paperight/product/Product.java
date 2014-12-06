package com.paperight.product;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.paperight.LicenceFileService;
import com.paperight.credit.PaperightCreditService;
import com.paperight.user.Company;

public class Product implements Serializable {
	
	private static final long serialVersionUID = -5803621429972024589L;
	private Long id;
	private String identifier;
	private String identifierType;
	private DateTime createdDate;
	private DateTime updatedDate;
	private String copyrightStatus;
	private String publisher;
	private String title;
	private String subTitle;
	private String alternativeTitle;
	private String primaryCreators;
	private String secondaryCreators;
	private String edition;
	private String primaryLanguages;
	private String secondaryLanguages;
	private String subjectArea;
	private LocalDateTime publicationDate;
	private LocalDateTime embargoDate;
	private String shortDescription;
	private String longDescription;
	private int oneUpPageExtent;
	private int twoUpPageExtent;
	private int a5PageExtent;
	private String parentIsbn;
	private String alternateIsbn;
	private String audience;
	private BigDecimal licenceFeeInDollars;
	private String oneUpFilename;
	private String twoUpFilename;
	private String a5Filename;
	private String jacketImageFilename;
	private String disallowedCountries;
	private String tags;
	private String url;
	private String urlCallToAction;
	private ProductSource source = ProductSource.PAPERIGHT;
	private boolean supportsAds;
	private ProductAvailabilityStatus availabilityStatus = ProductAvailabilityStatus.ON_SALE;
	private boolean disabled = false;
	private boolean publisherInactive = false;
	private String relatedProducts;
	private boolean premium;
	private boolean canPhotocopy;

	@JsonIgnore
	private Company ownerCompany;

	private String samplePageRange;
	private String licenceStatement;
	
	private Integer publisherEarningPercent = null;
	
	@Transient
	@Autowired
	private transient LicenceFileService licenceFileService;
	
	@Transient
	@Autowired
	private transient PaperightCreditService paperightCreditService;

	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public String getIdentifier() {
		return identifier;
	}
	
	public void setIdentifier(String identifier) {
		this.identifier = identifier;;
	}
	
	public String getIdentifierType() {
		return identifierType;
	}
	
	public void setIdentifierType(String identifierType) {
		this.identifierType = identifierType;
	}

	@Column(nullable=false, updatable=false)
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	public DateTime getCreatedDate() {
		return createdDate;
	}
	
	@SuppressWarnings("unused")
	private void setCreatedDate(DateTime createdDate) {
		this.createdDate = createdDate;
	}

	@Column(nullable=false)
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	public DateTime getUpdatedDate() {
		return updatedDate;
	}

	@SuppressWarnings("unused")
	private void setUpdatedDate(DateTime updatedDate) {
		this.updatedDate = updatedDate;
	}

	@Column(name="sellingPrice", nullable=false)
	public BigDecimal getLicenceFeeInDollars() {
		return licenceFeeInDollars;
	}

	public void setLicenceFeeInDollars(BigDecimal licenceFeeInDollars) {
		this.licenceFeeInDollars = licenceFeeInDollars;
	}

	@Column(nullable=false)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	@Column(nullable=false)
	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
	@DateTimeFormat(iso = ISO.DATE)
	public LocalDateTime getPublicationDate() {
		return publicationDate;
	}

	public void setPublicationDate(LocalDateTime publicationDate) {
		this.publicationDate = publicationDate;
	}

	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
	@DateTimeFormat(iso = ISO.DATE)
	public LocalDateTime getEmbargoDate() {
		return embargoDate;
	}

	public void setEmbargoDate(LocalDateTime embargoDate) {
		this.embargoDate = embargoDate;
	}

	@Column(length=5000)
	public String getLongDescription() {
		return longDescription;
	}

	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}

	public String getCopyrightStatus() {
		return copyrightStatus;
	}

	public void setCopyrightStatus(String copyrightStatus) {
		this.copyrightStatus = copyrightStatus;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public String getAlternativeTitle() {
		return alternativeTitle;
	}

	public void setAlternativeTitle(String alternativeTitle) {
		this.alternativeTitle = alternativeTitle;
	}

	@Column(length=1000)
	public String getPrimaryCreators() {
		return primaryCreators;
	}

	public void setPrimaryCreators(String primaryCreators) {
		this.primaryCreators = primaryCreators;
	}

	@Column(length=1000)
	public String getSecondaryCreators() {
		return secondaryCreators;
	}

	public void setSecondaryCreators(String secondaryCreators) {
		this.secondaryCreators = secondaryCreators;
	}

	public String getEdition() {
		return edition;
	}

	public void setEdition(String edition) {
		this.edition = edition;
	}

	public String getPrimaryLanguages() {
		return primaryLanguages;
	}

	public void setPrimaryLanguages(String primaryLanguages) {
		this.primaryLanguages = primaryLanguages;
	}

	public String getSecondaryLanguages() {
		return secondaryLanguages;
	}

	public void setSecondaryLanguages(String secondaryLanguages) {
		this.secondaryLanguages = secondaryLanguages;
	}

	public String getSubjectArea() {
		return subjectArea;
	}

	public void setSubjectArea(String subjectArea) {
		this.subjectArea = subjectArea;
	}

	@Column(length=5000)
	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getParentIsbn() {
		return parentIsbn;
	}

	public void setParentIsbn(String parentIsbn) {
		this.parentIsbn = parentIsbn;
	}

	public String getAlternateIsbn() {
		return alternateIsbn;
	}

	public void setAlternateIsbn(String alternateIsbn) {
		this.alternateIsbn = alternateIsbn;
	}

	public String getAudience() {
		return audience;
	}

	public void setAudience(String audience) {
		this.audience = audience;
	}

	public String getJacketImageFilename() {
		return jacketImageFilename;
	}

	public void setJacketImageFilename(String jacketImageFilename) {
		this.jacketImageFilename = jacketImageFilename;
	}
	
	@Transient
	public String getJacketImageUrl() {
		String imageName = getJacketImageFilename();
		if (StringUtils.isBlank(imageName)) {
			imageName = "default";
		}
		return "http://images.paperight.com/" + getId() + "/" + imageName;
	}

	@Column(length=5000)
	public String getDisallowedCountries() {
		return disallowedCountries;
	}

	public void setDisallowedCountries(String disallowedCountries) {
		this.disallowedCountries = disallowedCountries;
	}

	@Column(length=5000)
	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrlCallToAction() {
		return urlCallToAction;
	}

	public void setUrlCallToAction(String urlCallToAction) {
		this.urlCallToAction = urlCallToAction;
	}

	
			//2up. 87 pages = 21.75 double sided pages + 0.5 (1 side of a page for the cover) = 22.25 double sided pages = 23 double sided pages
	public int getTwoUpPageExtent(){
		if (twoUpPageExtent == 0 && !StringUtils.isBlank(getTwoUpFilename())) {
			try {
				twoUpPageExtent = licenceFileService.getPageCount(getTwoUpFilename());
				if (getId() != null) {
					merge();
				}
			} catch (Exception e) {
				
			}
		}
		return twoUpPageExtent;
	}
	
	public void setTwoUpPageExtent(int twoUpPageExtent) {
		this.twoUpPageExtent = twoUpPageExtent;
	}

	
			//1up. 87 pages =  43.5 double sided pages + 0.5 (1 side of a page for the cover) = 44 double sided pages.
	public int getOneUpPageExtent(){
		if (oneUpPageExtent == 0 && !StringUtils.isBlank(getOneUpFilename())) {
			try {
				oneUpPageExtent = licenceFileService.getPageCount(getOneUpFilename());
				if (getId() != null) {
					merge();
				}
			} catch (Exception e) {
				
			}
		}
		return oneUpPageExtent;
	}
	
	public void setOneUpPageExtent(int oneUpPageExtent) {
		this.oneUpPageExtent = oneUpPageExtent;
	}

	public int getA5PageExtent(){
		if (a5PageExtent == 0 && !StringUtils.isBlank(getA5Filename())) {
			try {
				a5PageExtent = licenceFileService.getPageCount(getA5Filename());
				if (getId() != null) {
					merge();
				}
			} catch (Exception e) {
						
			}
		}
		return a5PageExtent;
	}
	
	public void setA5PageExtent(int a5PageExtent) {
		this.a5PageExtent = a5PageExtent;
	}

	@Transient
	public ProductSource getSource() {
		return source;
	}

	public void setSource(ProductSource source) {
		this.source = source;
	}

	public boolean isSupportsAds() {
		return supportsAds;
	}

	public void setSupportsAds(boolean supportsAds) {
		this.supportsAds = supportsAds;
	}
	
	@Transient
	public String getDisplayName() {
		String displayName = getTitle();
		if (!StringUtils.isBlank(getPrimaryCreators())) {
			displayName = displayName + " - Creators - " + getPrimaryCreators();
		}
		displayName = displayName.replaceAll("(The |A |[-:,;&()./])", "");
		displayName = displayName.replaceAll(" +", "-");
		displayName = displayName.replaceAll("<b>", "");
		displayName = displayName.replaceAll("</b>", "");
		displayName = displayName.replaceAll("%", "");
		displayName = displayName.replaceAll("/", "");
		displayName = displayName.replaceAll("#", "");
		displayName = displayName.replaceAll("['\"]+", "");
		displayName = StringUtils.replace(displayName, "\\", "");
		return displayName;
	}
	
	@Transient
	@JsonIgnore
	public BigDecimal getOneUpFileSize() {
		String filename = FilenameUtils.concat(licenceFileService.getPdfFileFolder(), getOneUpFilename());
		return licenceFileService.getFileSize(filename);
	}
	
	@Transient
	@JsonIgnore
	public BigDecimal getTwoUpFileSize() {
		String filename = FilenameUtils.concat(licenceFileService.getPdfFileFolder(), getTwoUpFilename());
		return licenceFileService.getFileSize(filename);
	}
	
	@Transient
	@JsonIgnore
	public BigDecimal getA5FileSize() {
		String filename = FilenameUtils.concat(licenceFileService.getPdfFileFolder(), getA5Filename());
		return licenceFileService.getFileSize(filename);
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "OWNER_COMPANY_ID", insertable = true, updatable = true, nullable = true)
	public Company getOwnerCompany() {
		return ownerCompany;
	}

	public void setOwnerCompany(Company ownerCompany) {
		this.ownerCompany = ownerCompany;
	}

	public String getSamplePageRange() {
		return samplePageRange;
	}

	public void setSamplePageRange(String samplePageRange) {
		this.samplePageRange = samplePageRange;
	}
	
	@Column(length=200)
	public String getLicenceStatement() {
		return licenceStatement;
	}

	public void setLicenceStatement(String licenceStatement) {
		this.licenceStatement = licenceStatement;
	}
	
	public String getA5Filename() {
		return a5Filename;
	}

	public void setA5Filename(String a5Filename) {
		this.a5Filename = a5Filename;
	}

	@Enumerated(EnumType.STRING)
	public ProductAvailabilityStatus getAvailabilityStatus() {
		if (availabilityStatus == null) {
			availabilityStatus = ProductAvailabilityStatus.ON_SALE;
		}
		return availabilityStatus;
	}

	public void setAvailabilityStatus(ProductAvailabilityStatus availabilityStatus) {
		this.availabilityStatus = availabilityStatus;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public boolean isPublisherInactive() {
		return publisherInactive;
	}

	public void setPublisherInactive(boolean publisherInactive) {
		this.publisherInactive = publisherInactive;
	}
	
	@Transient
	public boolean isAvailableForSale() {
		boolean result = false;
		if (getAvailabilityStatus() == ProductAvailabilityStatus.ON_SALE && !isDisabled() && !isPublisherInactive()) {
			result = true;
		}
		return result;
	}

	@Column(nullable=true)
	public Integer getPublisherEarningPercent() {
		return publisherEarningPercent;
	}

	public void setPublisherEarningPercent(Integer publisherEarningPercent) {
		this.publisherEarningPercent = publisherEarningPercent;
	}
	
	@Transient
	public BigDecimal getLicenceFeeInCredits() {
		BigDecimal licenceFeeInDollars = getLicenceFeeInDollars();
		return paperightCreditService.paperightCreditsFromBaseCurrency(licenceFeeInDollars);
	}

	@Column(length=5000)
	public String getRelatedProducts() {
		return relatedProducts;
	}

	public void setRelatedProducts(String relatedProducts) {
		this.relatedProducts = relatedProducts;
	}
	
    public boolean isPremium() {
        return premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }
    
    public boolean isCanPhotocopy() {
        return canPhotocopy;
    }

    public void setCanPhotocopy(boolean canPhotocopy) {
        this.canPhotocopy = canPhotocopy;
    }
    
    @Transient
    public boolean isCanPrint() {
        return !StringUtils.isBlank(getA5Filename()) || !StringUtils.isBlank(getOneUpFilename()) || !StringUtils.isBlank(getTwoUpFilename());
    }

}
