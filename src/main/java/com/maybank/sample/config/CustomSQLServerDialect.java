package com.maybank.sample.config;

import java.sql.Types;

import org.hibernate.dialect.SQLServer2012Dialect;
import org.hibernate.type.StandardBasicTypes;

public class CustomSQLServerDialect extends SQLServer2012Dialect {

	public CustomSQLServerDialect() {
		super();
		registerColumnType(Types.CHAR, "nchar(1)");
		registerColumnType(Types.VARCHAR, 255, "nvarchar($l)");
		registerColumnType(Types.CLOB, "nvarchar(max)");
		registerColumnType(Types.BLOB, "nvarchar(max)");
		registerHibernateType(Types.NVARCHAR, StandardBasicTypes.STRING.getName());
	}
}
