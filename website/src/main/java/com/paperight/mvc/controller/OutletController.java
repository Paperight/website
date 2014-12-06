package com.paperight.mvc.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.paperight.currency.Currency;
import com.paperight.currency.CurrencyService;
import com.paperight.product.Product;
import com.paperight.user.Company;
import com.paperight.user.Company.MapDisplay;
import com.paperight.user.Role;

@Controller
@RequestMapping(value = "/outlets")
public class OutletController {

	private static Logger logger = LoggerFactory.getLogger(OutletController.class);

	@Autowired
	private CurrencyService currencyService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String outlets(Model model) {
		return "outlets";
	}

	@RequestMapping(value = "/companies", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody Object companies(@RequestParam double latitude, @RequestParam double longitude, @RequestParam Long productId) {
	    List<Company> allCompanies = Company.findAllActiveByRole(Role.ROLE_OUTLET);
		List<CompanyDto> companies = new ArrayList<CompanyDto>();
		for (Company company : allCompanies) {
			if (!StringUtils.isBlank(company.getGpsLocation())
					&& !MapDisplay.HIDDEN.equals(company.getMapDisplay())) {
				String[] locations = company.getGpsLocation().split(",");
				try {
					if (locations.length == 2) {
						double distanceFromLocation = getDistanceBetweenTwoLocations(
								latitude, 
								longitude, 
								Double.valueOf(locations[0]), 
								Double.valueOf(locations[1]));

						CompanyDto companyDto = CompanyDto.toDto(company);
						companyDto.setLatitude(locations[0]);
						companyDto.setLongitude(locations[1]);
						companyDto.setDistanceFromLocation(distanceFromLocation);
						companies.add(companyDto);
					}
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
			}
		}
		
		
		if (productId != null) {
		    Product product = Product.find(productId);
			calculatePrintingCost(companies, product);
			if (product.isPremium()) {
			    companies = filterPremiumCompanies(companies);
			}
		}	
		Collections.sort(companies, new DistanceCompare());
		Map<String, Object> response = new HashMap<>();
		response.put("companies", companies);
		return response;
	}
	
	private List<CompanyDto> filterPremiumCompanies(List<CompanyDto> companies) {
	    return Lists.newArrayList(Iterables.filter(companies, new Predicate<CompanyDto>() {

            @Override
            public boolean apply(CompanyDto company) {
                return company.getMapDisplay().equals(MapDisplay.FEATURE);
            }
        }));
	}

	private void calculatePrintingCost(List<CompanyDto> companies, Product product) {
		for (CompanyDto company : companies) {
			String printingCost = calculatePrintingCost(company, product);
			company.setPrintingCost(printingCost);
		}
	}

	private String calculatePrintingCost(CompanyDto company, Product product) {
		int pageCount = (int)Math.ceil(product.getTwoUpPageExtent() / 2);
		if (!StringUtils.isBlank(product.getOneUpFilename())) {
			pageCount = (int)Math.ceil(product.getOneUpPageExtent() / 2);
		} else if (!StringUtils.isBlank(product.getA5Filename())) {
			pageCount = (int)Math.ceil(product.getA5PageExtent() / 4);
		}

		BigDecimal printingCost = company.getAveragePrintingCost().multiply(new BigDecimal(pageCount));
		printingCost = printingCost.add(company.getAverageBindingCost());
		BigDecimal sellingPrice = currencyService.convert(product.getLicenceFeeInDollars(), currencyService.getBaseCurrency(), company.getCurrency());
		BigDecimal totalCost = printingCost.add(sellingPrice);
		return company.getCurrency().getSymbol() + totalCost.setScale(0, RoundingMode.CEILING);
	}

	private double getDistanceBetweenTwoLocations(double lat1, double lon1, double lat2, double lon2) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		dist = dist * 1.609344;
		return dist;
	}

	private double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	private double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}
}

class DistanceCompare implements Comparator<CompanyDto> {

	@Override
	public int compare(CompanyDto o1, CompanyDto o2) {
		if (o1.getDistanceFromLocation() < o2.getDistanceFromLocation()) {
			return -1;
		} else if (o1.getDistanceFromLocation() > o2.getDistanceFromLocation()) {
			return 1;
		} else {
			return 0;
		}
	}
}

class CompanyDto {
	private long id;
	private String name;
	private String latitude;
	private String longitude;
	private MapDisplay mapDisplay;
	private String address;
	private String phoneNumber;
	private String printingCost;
	private BigDecimal averagePrintingCost = new BigDecimal(0.00);
	private BigDecimal averageBindingCost = new BigDecimal(15.00);
	private double distanceFromLocation;
	private Currency currency;
	private String email;

	public static CompanyDto toDto(Company company) {
		CompanyDto companyDto = new CompanyDto();
		companyDto.setId(company.getId());
		companyDto.setName(company.getName());
		companyDto.setMapDisplay(company.getMapDisplay());
		companyDto.setAddress(company.getPrimaryAddress().toString());
		companyDto.setPhoneNumber(company.getPhoneNumber());
		companyDto.setPrintingCost("");
		companyDto.setAveragePrintingCost(company.getAveragePrintingCost());
		companyDto.setAverageBindingCost(company.getAverageBindingCost());
		companyDto.setCurrency(company.getCurrency());
		companyDto.setEmail(company.getEmail());		
		return companyDto;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public MapDisplay getMapDisplay() {
		return mapDisplay;
	}
	public void setMapDisplay(MapDisplay mapDisplay) {
		this.mapDisplay = mapDisplay;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String addressDisplay) {
		this.address = addressDisplay;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getPrintingCost() {
		return printingCost;
	}
	public void setPrintingCost(String printingCost) {
		this.printingCost = printingCost;
	}
	public double getDistanceFromLocation() {
		return distanceFromLocation;
	}
	public void setDistanceFromLocation(double distanceFromLocation) {
		this.distanceFromLocation = distanceFromLocation;
	}
	public BigDecimal getAveragePrintingCost() {
		return averagePrintingCost;
	}
	public void setAveragePrintingCost(BigDecimal averagePrintingCost) {
		this.averagePrintingCost = averagePrintingCost;
	}
	public Currency getCurrency() {
		return currency;
	}
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

    public BigDecimal getAverageBindingCost() {
        return averageBindingCost;
    }
    
    public void setAverageBindingCost(BigDecimal averageBindingCost) {
        this.averageBindingCost = averageBindingCost;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
}