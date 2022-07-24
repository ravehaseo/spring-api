package com.maybank.sample.config;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;

import com.google.common.base.CaseFormat;



public class CustomNamingStrategyPhysical extends SpringPhysicalNamingStrategy {

	@Override
	public Identifier toPhysicalTableName(final Identifier name, final JdbcEnvironment context) {
		return new Identifier(tableName(name), name.isQuoted());
	}

	@Override
	public Identifier toPhysicalColumnName(final Identifier name, final JdbcEnvironment context) {
		return new Identifier(propertyToColumnName(name), name.isQuoted());
	}

	public String tableName(final Identifier name) {
		return CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, name.getText());
	}

	public String propertyToColumnName(final Identifier name) {
		return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, name.getText());
	}
}
