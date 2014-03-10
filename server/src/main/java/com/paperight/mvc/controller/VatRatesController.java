package com.paperight.mvc.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.paperight.country.CountryService;
import com.paperight.currency.VatRate;
import com.paperight.utils.StringComparators;

@Controller
@SessionAttributes({"vatRate"})
@PreAuthorize("hasPermission(#user, 'EDIT_VAT_RATES')")
public class VatRatesController {
	
	@Autowired
	private CountryService countryService;

	@RequestMapping(value="vat-rates", method = RequestMethod.GET )
	public String searchPublisherPaymentRequests(@ModelAttribute PublisherPaymentRequestSearch publisherPaymentRequestSearch, Model model) {
		List<VatRate> vatRates = VatRate.findAll();
		Collections.sort(vatRates, new Comparator<VatRate>() {
			
			@Override
			public int compare(VatRate vatRate, VatRate otherVatRate) {
				return StringComparators.compareNaturalIgnoreCaseAscii(vatRate.getCountry().getName(), otherVatRate.getCountry().getName());
			}
			
		});
		model.addAttribute("vatRates", vatRates);
		return "vat-rates";
	}
	
	@RequestMapping(value = "/vat-rate/update/{countryCode}", method = RequestMethod.GET)
	public String update(@PathVariable String countryCode, Model model) {
		VatRate vatRate;
		if (countryCode == null) {
			vatRate = new VatRate();
		} else {
			vatRate = VatRate.findByCountryCode(countryCode);
		}
		model.addAttribute("countries", countryService.getCountries());
		model.addAttribute("vatRate", vatRate);
		return "vat-rate/update";
	}
	
	@RequestMapping(value = "/vat-rate/create", method = RequestMethod.GET)
	public String create(Model model) {
		return update(null, model);
	}
	
	@RequestMapping(value = "/vat-rate/update", method = RequestMethod.POST)
	public String update(@ModelAttribute VatRate vatRate, BindingResult result, Model model, SessionStatus sessionStatus) {
		if (result.hasErrors()) {
			return "vat-rate/update";
		}
		vatRate = vatRate.merge();
		//refreshWebsitePosters();
		sessionStatus.setComplete();
		return "redirect:/vat-rates";
	}
	
	@RequestMapping(value = "/vat-rate/delete/{countryCode}", method = RequestMethod.GET)
	public String delete(@PathVariable String countryCode) {
		VatRate vatRate = VatRate.findByCountryCode(countryCode);
		if (vatRate != null) {
			vatRate.remove();
			//refreshWebsitePosters();
		}
		return "redirect:/vat-rates";
	}
	
}
