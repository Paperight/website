package com.paperight.publisherearning;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.paperight.currency.Currency;
import com.paperight.currency.CurrencyService;

@Configurable
public class PublisherEarningSummary {
	
	@Autowired
	transient JdbcTemplate jdbcTemplate;
	
	@Transient
	@Autowired
	private CurrencyService currencyService;
	
	private Long productId;
	private String productTitle;
	private Long numberOfTransactions;
	private BigDecimal amountInCurrency;
	private String currencyCode;
	private Currency currency;
	
	private static final JdbcTemplate jdbcTemplate() {
		JdbcTemplate jdbcTemplate = new PublisherEarningSummary().jdbcTemplate;
		if (jdbcTemplate == null)
			throw new IllegalStateException("JdbcTemplate has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return jdbcTemplate;
	}

	public static List<PublisherEarningSummary> findByCompanyIdGroupedByProduct(Long companyId) {
		if (companyId == null) {
			return null;
		}
		List<PublisherEarningSummary> publisherEarningSummarys = jdbcTemplate().query("(SELECT COUNT(PublisherEarning.id) AS numberOfTransactions, SUM(amountInCurrency) AS amountInCurrency, PublisherEarning.currencyCode, PRODUCT_ID, Product.title FROM PublisherEarning INNER JOIN Licence ON (PublisherEarning.LICENCE_ID = Licence.Id) INNER JOIN Product ON (Licence.PRODUCT_ID = Product.Id) WHERE PublisherEarning.COMPANY_ID=? GROUP BY PRODUCT_ID, PublisherEarning.currencyCode ORDER BY PublisherEarning.createdDate DESC) UNION (SELECT 0 AS numberOfTransactions, NULL AS amountInCurrency, NULL as currencyCode, id AS PRODUCT_ID, title FROM Product WHERE id NOT IN (SELECT PRODUCT_ID FROM Licence) AND Product.OWNER_COMPANY_ID=?)", new PublisherEarningSummaryRowMapper(), companyId, companyId);
		return publisherEarningSummarys;
	}
	
	public Long getProductId() {
		return productId;
	}
	
	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getProductTitle() {
		return productTitle;
	}

	public void setProductTitle(String productTitle) {
		this.productTitle = productTitle;
	}

	public Long getNumberOfTransactions() {
		return numberOfTransactions;
	}

	public void setNumberOfTransactions(Long numberOfTransactions) {
		this.numberOfTransactions = numberOfTransactions;
	}

	public BigDecimal getAmountInCurrency() {
		return amountInCurrency;
	}

	public void setAmountInCurrency(BigDecimal amountInCurrency) {
		this.amountInCurrency = amountInCurrency;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		if (!StringUtils.isBlank(currencyCode)){
			currency = currencyService.findByCode(currencyCode);
		}
		this.currencyCode = currencyCode;
	}

	@Transient
	public Currency getCurrency() {
		return currency;
	}

}

class PublisherEarningSummaryRowMapper implements RowMapper<PublisherEarningSummary> {
	
	@Override
	public PublisherEarningSummary mapRow(ResultSet rs, int rowNum) throws SQLException {
		PublisherEarningSummary licenceSummary = new PublisherEarningSummary();
		licenceSummary.setNumberOfTransactions(rs.getLong("numberOfTransactions"));
		licenceSummary.setAmountInCurrency(rs.getBigDecimal("amountInCurrency"));
		licenceSummary.setCurrencyCode(rs.getString("currencyCode"));
		licenceSummary.setProductId(rs.getLong("PRODUCT_ID"));
		licenceSummary.setProductTitle(rs.getString("title"));
		return licenceSummary;
	}
	
}
