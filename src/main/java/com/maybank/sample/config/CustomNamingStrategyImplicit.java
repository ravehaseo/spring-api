package com.maybank.sample.config;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.ImplicitForeignKeyNameSource;
import org.hibernate.boot.model.naming.ImplicitJoinColumnNameSource;
import org.hibernate.boot.model.naming.NamingHelper;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;

public class CustomNamingStrategyImplicit extends SpringImplicitNamingStrategy {

	private static final long serialVersionUID = 1L;

	@Override
	public Identifier determineJoinColumnName(final ImplicitJoinColumnNameSource source) {
		String result;
		if (source.getNature() == ImplicitJoinColumnNameSource.Nature.ELEMENT_COLLECTION || source.getAttributePath() == null) {
			result = transformEntityName(source.getEntityNaming()) + "_fk";
		} else {
			result = transformAttributePath(source.getAttributePath()) + "_fk";
		}
		return toIdentifier(result, source.getBuildingContext());
	}

	@Override
	public Identifier determineForeignKeyName(final ImplicitForeignKeyNameSource source) {
		return toIdentifier(NamingHelper.INSTANCE.generateHashedFkName("FK_", source.getTableName(), source.getReferencedTableName(),
				source.getColumnNames()), source.getBuildingContext());
	}
}
