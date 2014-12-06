package com.paperight.user;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.transaction.annotation.Transactional;

import com.paperight.authentication.AuthenticationService;
import com.paperight.currency.Currency;
import com.paperight.currency.CurrencyService;
import com.paperight.product.Product;
import com.paperight.utils.StringComparators;

public class Company implements Serializable {

	private static final long serialVersionUID = -858556237480626182L;
	private Long id;
	private DateTime createdDate;
	private DateTime updatedDate;
	@Size(min = 10, max = 20)
	private String phoneNumber;
	private String currencyCode;
	private Currency currency;
	private BigDecimal credits;
	private BigDecimal averagePrintingCost = new BigDecimal(0.00);
	private BigDecimal averageBindingCost = new BigDecimal(15.00);

	@NotEmpty
	private String name;

	private List<AddressContext> addressContexts = new ArrayList<AddressContext>();

	@DateTimeFormat(iso = ISO.DATE)
	private DateTime dateOfEstablishment;

	@Size(min = 0, max = 255)
	private String websiteAddress;

	@Size(min = 0, max = 5000)
	private String description;

	private Set<User> users = new HashSet<User>();
	
	private Set<Product> products = new HashSet<Product>();

	private Company parentCompany = null;
	
	@Transient
	@Autowired
	private CurrencyService currencyService;
	
	private boolean deleted = false;
	private boolean closed = false;
	private boolean disabled = false;
	
	private boolean vatRegistered = false;
	private String vatRegistrationNumber;
	private DateTime vatDateOfLiability;

	private String gpsLocation;
	private MapDisplay mapDisplay = MapDisplay.HIDDEN;
	
	public enum MapDisplay {
		HIDDEN,
		VISIBLE,
		FEATURE;
	}

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
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	public DateTime getUpdatedDate() {
		return updatedDate;
	}

	@SuppressWarnings("unused")
	private void setUpdatedDate(DateTime updatedDate) {
		this.updatedDate = updatedDate;
	}

	@Column(nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(nullable = true)
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	public DateTime getDateOfEstablishment() {
		return dateOfEstablishment;
	}

	public void setDateOfEstablishment(DateTime dateOfEstablishment) {
		this.dateOfEstablishment = dateOfEstablishment;
	}

	@Column(nullable = false)
	public String getWebsiteAddress() {
		return websiteAddress;
	}

	public void setWebsiteAddress(String websiteAddress) {
		this.websiteAddress = websiteAddress;
	}

	@Column(length = 5000, nullable = false)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	public List<AddressContext> getAddressContexts() {
		return addressContexts;
	}

	public void setAddressContexts(List<AddressContext> addressContexts) {
		this.addressContexts = addressContexts;
	}

	public void addAddressContext(AddressContext addressContext) {
		this.getAddressContexts().add(addressContext);
	}

	@Transient
	public Set<AddressContext> getAddressContextsByType(AddressContextType addressContextType) {
		Set<AddressContext> addressContexts = new HashSet<AddressContext>();
		for (AddressContext addressContext : getAddressContexts()) {
			if (addressContext.getType() == addressContextType) {
				addressContexts.add(addressContext);
			}
		}
		return addressContexts;
	}

	@Transient
	public AddressContext getAddressContextByType(AddressContextType addressContextType) {
		Set<AddressContext> addressContexts = getAddressContextsByType(addressContextType);
		for (AddressContext addressContext : addressContexts) {
			return addressContext;
		}
		return null;
	}
	
	@Transient 
	public Address getPrimaryAddress() {
		return getAddressContextByType(AddressContextType.DEFAULT_PRIMARY).getAddress();
	}

	public void updateAddressContext(AddressContext addressContext) {
		int index = getAddressContexts().indexOf(addressContext);
		if (index >= 0) {
			getAddressContexts().remove(index);
		}
		getAddressContexts().add(addressContext);
	}

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "company")
	public Set<User> getUsers() {
		return users;
	}
	
	@Transient
	public List<User> getActiveUsers() {
		List<User> users = User.findByCompany(getId());
		List<User> activeUsers = new ArrayList<User>();
		for (User user : users) {
			if (!user.isClosed() && !user.isDeleted() && user.isEnabled()) {
				activeUsers.add(user);
			}
		}
		return activeUsers;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public void addUser(User user) {
		getUsers().add(user);
	}

	@JsonIgnore
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	public Company getParentCompany() {
		return parentCompany;
	}

	public void setParentCompany(Company parentCompany) {
		this.parentCompany = parentCompany;
	}

	@Column(nullable = false)
	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
		currency = currencyService.findByCode(this.currencyCode);
	}
	
	@Transient
	public Currency getCurrency(){
		return currency;
	}
	
	@Column(nullable = false)
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public BigDecimal getCredits() {
		return credits;
	}

	public void setCredits(BigDecimal credits) {
		this.credits = credits;
	}

	@Column(nullable = false)
	public BigDecimal getAveragePrintingCost() {
		return averagePrintingCost;
	}

	public void setAveragePrintingCost(BigDecimal averagePrintingCost) {
		this.averagePrintingCost = averagePrintingCost;
	}

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "ownerCompany")
	public Set<Product> getProducts() {
		return products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	@Transient
	public List<User> getCompanyAdmins() {
		List<User> users = User.findByCompany(getId());
		List<User> companyAdmins = new ArrayList<User>();
		for (User user : users) {
			if (user.hasRole(Role.ROLE_COMPANY_ADMIN)) {
				companyAdmins.add(user);
			}
		}
		return companyAdmins;
	}
	
	@Transient
	public List<User> getActiveCompanyAdmins() {
		List<User> companyAdmins = getCompanyAdmins();
		List<User> activeCompanyAdmins = new ArrayList<User>();
		for (User user : companyAdmins) {
			if (!user.isClosed() && !user.isDeleted() && user.isEnabled()) {
				activeCompanyAdmins.add(user);
			}
		}
		return activeCompanyAdmins;
	}
	
	@Transient
	public List<User> getNormalUsers() {
		List<User> users = User.findByCompany(getId());
		List<User> normalUsers = new ArrayList<User>();
		for (User user : users) {
			if (!user.hasRole(Role.ROLE_COMPANY_ADMIN)) {
				normalUsers.add(user);
			}
		}
		return normalUsers;
	}
	
	@Transient
	public List<User> getActiveNormalUsers() {
		List<User> normalUsers = getNormalUsers();
		List<User> activeNormalUsers = new ArrayList<User>();
		for (User user : normalUsers) {
			if (!user.isClosed() && !user.isDeleted() && user.isEnabled()) {
				activeNormalUsers.add(user);
			}
		}
		return activeNormalUsers;
	}
	
	@Transient
	public List<Company> getChildCompanies() {
		return Company.findByParentCompany(getId());
	}
	
	@Transient
	public List<Company> getActiveChildCompanies() {
		List<Company> childCompanies = Company.findByParentCompany(getId());
		List<Company> activeChildCompanies = new ArrayList<Company>();
		for (Company company : childCompanies) {
			if (!company.isClosed() && !company.isDeleted() && !company.isDisabled()) {
				activeChildCompanies.add(company);
			}
		}
		return activeChildCompanies;
	}

	public boolean isClosed() {
		return closed;
	}

	public void setClosed(boolean closed) {
		this.closed = closed;
	}


	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	
	@Transactional
	public void close() {
		List<Company> children = Company.findByParentCompany(getId());
		for (Company childCompany : children) {
			childCompany.close();
		}
		setClosed(true);
		merge();
		deactivateProducts();
		List<User> users = getActiveUsers();
		for (User user : users) {
			user.close();
		}
	}
	
	@Transactional
	public void reopen() {
		setClosed(false);
		merge();
		activateProducts();
	}
	
	@Transactional
	public void disable() {
		List<Company> children = Company.findByParentCompany(getId());
		for (Company childCompany : children) {
			childCompany.disable();
		}
		setDisabled(true);
		merge();
		deactivateProducts();
		List<User> users = getActiveUsers();
		for (User user : users) {
			user.disable();
		}
	}
	
	@Transactional
	public void enable() {
		setDisabled(false);
		merge();
		activateProducts();
	}
	
	@Transactional
	public void delete() {
		List<Company> children = Company.findByParentCompany(getId());
		for (Company childCompany : children) {
			childCompany.delete();
		}
		setDeleted(true);
		merge();
		deactivateProducts();
		List<User> users = getActiveUsers();
		for (User user : users) {
			user.delete();
		}
	}
	
	@Transactional
	public void deactivateProducts() {
		List<Product> products = Product.findByCompanyId(getId());
		for (Product product : products) {
			product.setPublisherInactive(true);
			product.merge();
		}
	}
	
	@Transactional
	public void activateProducts() {
		List<Product> products = Product.findByCompanyId(getId());
		for (Product product : products) {
			product.setPublisherInactive(false);
			product.merge();
		}
	}
	
	@Transactional
	public void syncUserRoles(User user) {
		List<User> users = User.findByCompany(getId());
		for (User localUser : users) {
			if (!localUser.getId().equals(user.getId())) {
				localUser.removeRole(Role.ROLE_OUTLET);
				localUser.removeRole(Role.ROLE_PUBLISHER);
				
				if (user.getRoles().contains(Role.ROLE_OUTLET)) {
					localUser.addRole(Role.ROLE_OUTLET);
				}
				if (user.getRoles().contains(Role.ROLE_PUBLISHER)) {
					localUser.addRole(Role.ROLE_PUBLISHER);
				}
				localUser.merge();
				AuthenticationService.reloadUser(localUser.getId());
			}
		}
	}

	public String getGpsLocation() {
		return gpsLocation;
	}

	public void setGpsLocation(String gpsLocation) {
		this.gpsLocation = gpsLocation;
	}

	@Enumerated(EnumType.STRING)
	public MapDisplay getMapDisplay() {
		return mapDisplay;
	}

	public void setMapDisplay(MapDisplay mapDisplay) {
		this.mapDisplay = mapDisplay;
	}

	public boolean isVatRegistered() {
		return vatRegistered;
	}

	public void setVatRegistered(boolean vatRegistered) {
		this.vatRegistered = vatRegistered;
	}

	public String getVatRegistrationNumber() {
		return vatRegistrationNumber;
	}

	public void setVatRegistrationNumber(String vatRegistrationNumber) {
		this.vatRegistrationNumber = vatRegistrationNumber;
	}

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	public DateTime getVatDateOfLiability() {
		return vatDateOfLiability;
	}

	public void setVatDateOfLiability(DateTime vatDateOfLiability) {
		this.vatDateOfLiability = vatDateOfLiability;
	}
	
	public static List<Company> findByNameOrUserEmail(String searchString) {
		List<Company> companies = Company.search(searchString);
		if (companies.isEmpty()) {
			companies = Company.findByUserEmail(searchString);
		}
		return companies;
	}
	
	private static List<Company> findByUserEmail(String email) {
		Map<Long, Company> companiesMap = new HashMap<Long, Company>();
		List<User> users = User.findByEmail(email);
		for (User user : users) {
			Company company = user.getCompany();
			companiesMap.put(company.getId(), company);
		}
		return new ArrayList<Company>(companiesMap.values());
	}
	
	@Transient
    public String getEmail() {
        String result = null;
        if (!getActiveCompanyAdmins().isEmpty()) {
            result = getActiveCompanyAdmins().get(0).getEmail();
        } else if (!getNormalUsers().isEmpty()) {
            result = getNormalUsers().get(0).getEmail();
        }
        return result;
    }
	
	public static void sortCompanies(List<Company> companies) {
		Collections.sort(companies, new Comparator<Company>() {

			@Override
			public int compare(Company company, Company otherCompany) {
				return StringComparators.compareNaturalIgnoreCaseAscii(StringUtils.defaultIfBlank(company.getName(), ""), StringUtils.defaultIfBlank(otherCompany.getName(), ""));
			}
			
		});
	}

	@Column(nullable = false)
    public BigDecimal getAverageBindingCost() {
        return averageBindingCost;
    }

    public void setAverageBindingCost(BigDecimal averageBindingCost) {
        this.averageBindingCost = averageBindingCost;
    }
}
