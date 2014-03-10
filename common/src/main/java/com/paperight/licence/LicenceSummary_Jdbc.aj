package com.paperight.licence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

privileged aspect LicenceSummary_Jdbc {
	
	declare @type: LicenceSummary: @Configurable;
	
	@Autowired
	transient JdbcTemplate LicenceSummary.jdbcTemplate;
	
	private static final JdbcTemplate LicenceSummary.jdbcTemplate() {
		JdbcTemplate jdbcTemplate = new LicenceSummary().jdbcTemplate;
		if (jdbcTemplate == null)
			throw new IllegalStateException("JdbcTemplate has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return jdbcTemplate;
	}
		
	public static List<LicenceSummary> LicenceSummary.findByCompanyId(Long companyId) {
		if (companyId == null) {
			return null;
		}
		List<LicenceSummary> licenceSummaries = jdbcTemplate().query("SELECT COUNT(id) AS numberOfTransactions, SUM(numberOfCopies) AS numberOfCopies, SUM(costInCredits) AS costInCredits, SUM(costInCurrency) AS costInCurrency, SUM(outletCharge) AS outletCharge, currencyCode, NULL AS product_id, NULL AS title FROM Licence WHERE company_id=? AND status != ? GROUP BY currencyCode", new LicenceSummaryRowMapper(), companyId, LicenceStatus.CANCELLED.toString());
		return licenceSummaries;
	}	
	
	public static List<LicenceSummary> LicenceSummary.findByCompanyIdGroupedByProduct(Long companyId) {
		if (companyId == null) {
			return null;
		}
		List<LicenceSummary> licenceSummaries = jdbcTemplate().query("SELECT COUNT(Licence.id) AS numberOfTransactions, SUM(numberOfCopies) AS numberOfCopies, SUM(costInCredits) AS costInCredits, SUM(costInCurrency) AS costInCurrency, SUM(outletCharge) AS outletCharge, currencyCode, product_id, Product.title FROM Licence INNER JOIN Product ON (Licence.product_id = Product.Id) WHERE company_id=? AND status != ? GROUP BY product_id, currencyCode ORDER BY Licence.createdDate DESC", new LicenceSummaryRowMapper(), companyId, LicenceStatus.CANCELLED.toString());
		return licenceSummaries;
	}
	
	
}

class LicenceSummaryRowMapper implements RowMapper<LicenceSummary> {
	
	@Override
	public LicenceSummary mapRow(ResultSet rs, int rowNum) throws SQLException {
		LicenceSummary licenceSummary = new LicenceSummary();
		licenceSummary.setNumberOfTransactions(rs.getLong("numberOfTransactions"));
		licenceSummary.setNumberOfCopies(rs.getLong("numberOfCopies"));
		licenceSummary.setTotalInCredits(rs.getBigDecimal("costInCredits"));
		licenceSummary.setTotalInCurrency(rs.getBigDecimal("costInCurrency"));
		licenceSummary.setTotalOutletCharges(rs.getBigDecimal("outletCharge"));
		licenceSummary.setCurrencyCode(rs.getString("currencyCode"));
		licenceSummary.setProductId(rs.getLong("product_id"));
		licenceSummary.setProductTitle(rs.getString("title"));
		return licenceSummary;
	}
	
}
