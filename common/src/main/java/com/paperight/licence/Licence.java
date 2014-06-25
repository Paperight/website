package com.paperight.licence;

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

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.paperight.currency.Currency;
import com.paperight.currency.CurrencyService;
import com.paperight.currency.VatRate;
import com.paperight.product.Product;
import com.paperight.user.Company;
import com.paperight.user.User;

@Configurable
public class Licence implements Serializable {

	private static final long serialVersionUID = -470028514874310262L;

	private Long id;
	private DateTime createdDate;
	private Product product;
	private String customerName;
	private String customerLastName;
	private String customerPhoneNumber;
	private int numberOfCopies = 1;
	private PageLayout pageLayout;
	private BigDecimal costInCredits;
	private BigDecimal costInCurrency;
	private BigDecimal outletCharge;
	private String currencyCode;
	private Currency currency;
	private User user;
	private Company company;
	private String trackingUrl;
	private LicenceStatus status = LicenceStatus.NEW;
	private BigDecimal paperightCreditToBaseCurrencyRate;
	private String paperightCreditBaseCurrencyCode;
	private BigDecimal randsInvoiceValue;
	private BigDecimal dollarsInvoiceValue;
	private InvoiceState invoiceState = InvoiceState.NEW;
	private DateTime firstDownloadedDate;
	private Company ownerCompany;

	public enum InvoiceState {
		NEW("New"),
		DOWNLOADED("Downloaded");
		
		private String displayName;
		
		private InvoiceState(String displayName) {
			setDisplayName(displayName);
		}

		public String getDisplayName() {
			return displayName;
		}

		public void setDisplayName(String displayName) {
			this.displayName = displayName;
		}
	}
	
	@Transient
	@Autowired
	private CurrencyService currencyService;

	@Id
	@Column
	@GeneratedValue
	public Long getId() {
		return id;
	}

	@SuppressWarnings("unused")
	private void setId(Long id) {
		this.id = id;
	}

	@Column(nullable = false)
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	public DateTime getCreatedDate() {
		return createdDate;
	}

	@SuppressWarnings("unused")
	private void setCreatedDate(DateTime createdDate) {
		this.createdDate = createdDate;
	}

	@Column(nullable = false)
	public int getNumberOfCopies() {
		return numberOfCopies;
	}

	public void setNumberOfCopies(int numberOfCopies) {
		this.numberOfCopies = numberOfCopies;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PRODUCT_ID", insertable = true, updatable = false, nullable = false)
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@Column(nullable = false)
	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	@Column(nullable = false)
	public BigDecimal getCostInCurrency() {
		return costInCurrency;
	}

	public void setCostInCurrency(BigDecimal costInCurrency) {
		this.costInCurrency = costInCurrency;
	}

	@Column(nullable = false)
	public BigDecimal getCostInCredits() {
		return costInCredits;
	}

	public void setCostInCredits(BigDecimal costInCredits) {
		this.costInCredits = costInCredits;
	}

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	public PageLayout getPageLayout() {
		return pageLayout;
	}

	public void setPageLayout(PageLayout pdfLayout) {
		this.pageLayout = pdfLayout;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID", insertable = true, updatable = false, nullable = false)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COMPANY_ID", insertable = true, updatable = false, nullable = false)
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public String getTrackingUrl() {
		return trackingUrl;
	}

	public void setTrackingUrl(String trackingUrl) {
		this.trackingUrl = trackingUrl;
	}

	@Transient
	public String getOriginalFileName() {
		switch (getPageLayout()) {
		case ONE_UP:
			return getProduct().getOneUpFilename();
		case TWO_UP:
			return getProduct().getTwoUpFilename();
		case A5:
			return getProduct().getA5Filename();
		default:
			return getProduct().getOneUpFilename();
		}
	}
	
	@Transient
	public int getPageExtent() {
		switch (getPageLayout()) {
		case ONE_UP:
			return getProduct().getOneUpPageExtent();
		case TWO_UP:
			return getProduct().getTwoUpPageExtent();
		case A5:
			return getProduct().getA5PageExtent();
		default:
			return 0;
		}
	}

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	public LicenceStatus getStatus() {
		return status;
	}

	public void setStatus(LicenceStatus status) {
		this.status = status;
	}

	@Column(nullable = false)
	public BigDecimal getOutletCharge() {
		return outletCharge;
	}

	public void setOutletCharge(BigDecimal outletCharge) {
		this.outletCharge = outletCharge;
	}

	@Column(nullable = false)
	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		currency = currencyService.findByCode(currencyCode);
		this.currencyCode = currencyCode;
	}

	@Transient
	public Currency getCurrency() {
		return currency;
	}

	public BigDecimal getPaperightCreditToBaseCurrencyRate() {
		return paperightCreditToBaseCurrencyRate;
	}

	public void setPaperightCreditToBaseCurrencyRate(
			BigDecimal paperightCreditToBaseCurrencyRate) {
		this.paperightCreditToBaseCurrencyRate = paperightCreditToBaseCurrencyRate;
	}

	public String getPaperightCreditBaseCurrencyCode() {
		return paperightCreditBaseCurrencyCode;
	}

	public void setPaperightCreditBaseCurrencyCode(
			String paperightCreditBaseCurrencyCode) {
		this.paperightCreditBaseCurrencyCode = paperightCreditBaseCurrencyCode;
	}
	
	@Transient
	public boolean isDownloaded() {
		return getStatus() == LicenceStatus.DOWNLOADED;
	}

	@Column(nullable = true)
	public String getCustomerLastName() {
		return customerLastName;
	}

	public void setCustomerLastName(String customerLastName) {
		this.customerLastName = customerLastName;
	}

	@Column(nullable = true)
	public String getCustomerPhoneNumber() {
		return customerPhoneNumber;
	}

	public void setCustomerPhoneNumber(String customerPhoneNumber) {
		this.customerPhoneNumber = customerPhoneNumber;
	}
	
	@Transient
	public String getCustomerFullName() {
		return StringUtils.trim(getCustomerName() + " " + getCustomerLastName());
	}
	
	@Transient
	public BigDecimal getFileSize() {
		switch (getPageLayout()) {
		case ONE_UP:
			return getProduct().getOneUpFileSize();
		case TWO_UP:
			return getProduct().getTwoUpFileSize();
		case A5:
			return getProduct().getA5FileSize();
		default:
			return getProduct().getOneUpFileSize();
		}
	}

	public BigDecimal getRandsInvoiceValue() {
		return randsInvoiceValue;
	}

	public void setRandsInvoiceValue(BigDecimal randsInvoiceValue) {
		this.randsInvoiceValue = randsInvoiceValue;
	}
	
	@Transient
	public BigDecimal getRandsInvoiceVatValue() {
		BigDecimal result = BigDecimal.ZERO;
		if (currencyService.isLiableForVat(getOwnerCompany(), getTransactionDate())) {
			VatRate vatRate = VatRate.findByCountryCode("ZA");
			result = currencyService.backCalculateVat(getRandsInvoiceValue(), vatRate.getRate0());
		}
		return result;
	}

	public BigDecimal getDollarsInvoiceValue() {
		return dollarsInvoiceValue;
	}

	public void setDollarsInvoiceValue(BigDecimal dollarsInvoiceValue) {
		this.dollarsInvoiceValue = dollarsInvoiceValue;
	}

	@Enumerated(EnumType.STRING)
	public InvoiceState getInvoiceState() {
		return invoiceState;
	}

	public void setInvoiceState(InvoiceState invoiceState) {
		this.invoiceState = invoiceState;
	}

	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	public DateTime getFirstDownloadedDate() {
		return firstDownloadedDate;
	}

	public void setFirstDownloadedDate(DateTime firstDownloadedDate) {
		this.firstDownloadedDate = firstDownloadedDate;
	}
	
	@Transient
	public DateTime getTransactionDate() {
		switch (getStatus()) {
		case DOWNLOADED:
			if (getFirstDownloadedDate() != null) {
				return getFirstDownloadedDate();
			} else {
				return getCreatedDate();
			}
		default:
			return getCreatedDate();
		}
	}

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OWNER_COMPANY_ID", insertable = true, updatable = false, nullable = true)
    public Company getOwnerCompany() {
        return ownerCompany;
    }

    public void setOwnerCompany(Company ownerCompany) {
        this.ownerCompany = ownerCompany;
    }
	
	

}