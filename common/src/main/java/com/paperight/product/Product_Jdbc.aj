package com.paperight.product;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

privileged aspect Product_Jdbc {

	@Autowired
	transient JdbcTemplate Product.jdbcTemplate;
	
	private static final JdbcTemplate Product.jdbcTemplate() {
		JdbcTemplate jdbcTemplate = new Product().jdbcTemplate;
		if (jdbcTemplate == null)
			throw new IllegalStateException("JdbcTemplate has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return jdbcTemplate;
	}
	
	public static void Product.updateOwnerCompany(final Long companyId, final List<Long> productIds, final boolean publisherInactive) {
		jdbcTemplate().batchUpdate("UPDATE Product SET OWNER_COMPANY_ID = ?, publisherInactive = ? WHERE ID = ?", 
				new BatchPreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps, int i)
							throws SQLException {
						Long productId = productIds.get(i);
						ps.setLong(1, companyId);
						ps.setBoolean(2, publisherInactive);
                        ps.setLong(3, productId);						
					}

					@Override
					public int getBatchSize() {
						return productIds.size();
					}
			
		});
	}
	
	public static void Product.unassignOwnerCompany(final List<Long> productIds) {
		jdbcTemplate().batchUpdate("UPDATE Product SET OWNER_COMPANY_ID = null WHERE ID = ?", 
				new BatchPreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps, int i)
							throws SQLException {
						Long productId = productIds.get(i);
                        ps.setLong(1, productId);						
					}

					@Override
					public int getBatchSize() {
						return productIds.size();
					}
			
		});
	}

}
