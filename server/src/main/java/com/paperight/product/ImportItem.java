package com.paperight.product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.paperight.AbstractEntity;
import com.paperight.Health;
import com.paperight.licence.PageLayout;

@Entity
public class ImportItem extends AbstractEntity {
    
    private static final long serialVersionUID = 1L;
    
    private ImportJob importJob;
    private Status status = Status.NEW;
    private Health health = Health.OK;
    private String error;
    private String rawData;
    
    private String identifier;
    private String identifierType;
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
    private BigDecimal licenceFeeInDollars;
    private String shortDescription;
    private String longDescription;
    private String parentIsbn;
    private String alternateIsbn;
    private String audience;
    private String disallowedCountries;
    private String tags;
    private String url;
    private String urlCallToAction;
    private boolean supportsAds;
    private String samplePageRange;
    private String licenceStatement;
    private String formats;
    private String oneUpFilename;
    private String twoUpFilename;
    private String a5Filename;
    private String jacketImageFilename;
    private Product product;
    private String cssFile;
    
    public enum Status {
        NEW,
        EXTRACTED,
        CONVERTED,
        META_ONLY,
        CANCELLED,
        PROCESSED;
    }

    @ManyToOne
    public ImportJob getImportJob() {
        return importJob;
    }

    public void setImportJob(ImportJob importJob) {
        this.importJob = importJob;
    }
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    
    @Enumerated(EnumType.STRING)
    public Health getHealth() {
        return health;
    }
    
    public void setHealth(Health health) {
        this.health = health;
    }
    
    @Lob
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Lob
    @Column(nullable = false, updatable = false)
    public String getRawData() {
        return rawData;
    }

    public void setRawData(String rawData) {
        this.rawData = rawData;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifierType() {
        return identifierType;
    }

    public void setIdentifierType(String identifierType) {
        this.identifierType = identifierType;
    }

    public String getCopyrightStatus() {
        return copyrightStatus;
    }

    public void setCopyrightStatus(String copyrightStatus) {
        this.copyrightStatus = copyrightStatus;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    @Lob
    public String getPrimaryCreators() {
        return primaryCreators;
    }

    public void setPrimaryCreators(String primaryCreators) {
        this.primaryCreators = primaryCreators;
    }

    @Lob
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

    public BigDecimal getLicenceFeeInDollars() {
        return licenceFeeInDollars;
    }

    public void setLicenceFeeInDollars(BigDecimal licenceFeeInDollars) {
        this.licenceFeeInDollars = licenceFeeInDollars;
    }

    @Lob
    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    @Lob
    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
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

    @Lob
    public String getDisallowedCountries() {
        return disallowedCountries;
    }

    public void setDisallowedCountries(String disallowedCountries) {
        this.disallowedCountries = disallowedCountries;
    }

    @Lob
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

    public boolean isSupportsAds() {
        return supportsAds;
    }

    public void setSupportsAds(boolean supportsAds) {
        this.supportsAds = supportsAds;
    }

    public String getSamplePageRange() {
        return samplePageRange;
    }

    public void setSamplePageRange(String samplePageRange) {
        this.samplePageRange = samplePageRange;
    }

    public String getLicenceStatement() {
        return licenceStatement;
    }

    public void setLicenceStatement(String licenceStatement) {
        this.licenceStatement = licenceStatement;
    }

    public String getFormats() {
        return formats;
    }

    public void setFormats(String formats) {
        this.formats = formats;
    }

    @Transient
    public List<PageLayout> getFormatList() throws Exception {
        List<PageLayout> layouts = new ArrayList<>();
        String[] formatsStrings = StringUtils.split(getFormats(), ";");
        if (formatsStrings != null) {
            for (String format : formatsStrings) {
                PageLayout pageLayout = PageLayout.valueOf(format);
                if (pageLayout == null) {
                    throw new Exception("Invalid format: " + format);
                }
                layouts.add(pageLayout);
            }
        }
        return layouts;
    }
    
    @Transient 
    public boolean canPhotocopy() throws Exception {
        boolean result = false;
        for (PageLayout pageLayout : getFormatList()) {
            if (pageLayout == PageLayout.PHOTOCOPY) {
                result = true;
                break;
            }
        }
        return result;
    }
    
    @Transient
    public boolean isHasPrintFormats() throws Exception {
        boolean result = false;
        List<PageLayout> layouts = getFormatList();
        if ((canPhotocopy() && layouts.size() > 1) || (!canPhotocopy() && layouts.size() > 0) ) {
            result = true;
        }
        return result;
    }
    
    @Transient
    public boolean hasA5Format() throws Exception {
       return hasFormat(PageLayout.A5);
    }
    
    @Transient
    public boolean hasFormat(PageLayout pageLayout) throws Exception {
        boolean result = false;
        for (PageLayout localPageLayout : getFormatList()) {
            if (localPageLayout == pageLayout) {
                result = true;
                break;
            }
        }
        return result;
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

    public String getA5Filename() {
        return a5Filename;
    }

    public void setA5Filename(String a5Filename) {
        this.a5Filename = a5Filename;
    }

    @ManyToOne(cascade=CascadeType.DETACH, fetch=FetchType.LAZY)
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
    
    @Transient
    public boolean isCanApply() {
        boolean result = true;        
        if (getHealth() == Health.ERROR || !(getStatus() == Status.META_ONLY || getStatus() == Status.CONVERTED)) {
            result = false;
        }
        return result;
    }
    
    @Transient
    public boolean isCanCancel() {
        return !isProcessed();
    }
    
    @Transient
    public boolean isCancelled() {
        return getStatus() == Status.CANCELLED;
    }
    
    @Transient
    public boolean isProcessed() {
        return getStatus() == Status.PROCESSED;
    }

    public String getJacketImageFilename() {
        return jacketImageFilename;
    }

    public void setJacketImageFilename(String jacketImageFilename) {
        this.jacketImageFilename = jacketImageFilename;
    }

    public String getCssFile() {
        return cssFile;
    }

    public void setCssFile(String cssFile) {
        this.cssFile = cssFile;
    }

}
