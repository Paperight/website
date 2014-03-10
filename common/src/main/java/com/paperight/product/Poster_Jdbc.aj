package com.paperight.product;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import com.google.common.collect.Iterables;

privileged aspect Poster_Jdbc {
	
	@Autowired
	transient JdbcTemplate Poster.jdbcTemplate;
	
	private static final JdbcTemplate Poster.jdbcTemplate() {
		JdbcTemplate jdbcTemplate = new Poster().jdbcTemplate;
		if (jdbcTemplate == null)
			throw new IllegalStateException("JdbcTemplate has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return jdbcTemplate;
	}
	
	public static void Poster.updateOrder(final Map<String, Integer> idOrderMap) {
		jdbcTemplate().batchUpdate("UPDATE Poster SET displayOrder = ? WHERE ID = ?", 
				new BatchPreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps, int i)
							throws SQLException {
						int displayOrder = Iterables.get(idOrderMap.values(), i);
						String idString = Iterables.get(idOrderMap.keySet(), i);
						Long id = Long.parseLong(idString);
						ps.setInt(1, displayOrder);
                        ps.setLong(2, id);						
					}

					@Override
					public int getBatchSize() {
						return idOrderMap.size();
					}
			
		});
	}

}
