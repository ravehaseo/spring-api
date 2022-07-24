package com.maybank.sample.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Repository;

import com.maybank.sample.config.HibernateConfig;
import com.maybank.sample.entity.BasicTable;

@Repository
@ConditionalOnBean(HibernateConfig.class)
public class CommonDAO<T> {

	private static final String CREATED_DATE = "createdDate";

	@Autowired
	private SessionFactory sessionFactory;

	protected Session getHibernateSession() {
		return sessionFactory.getCurrentSession();
	}

	protected Connection getHibernateConnection() throws SQLException {
		return sessionFactory.getSessionFactoryOptions().getServiceRegistry().getService(ConnectionProvider.class).getConnection();
	}

	public T getObjectByID(final Class<T> clazz, final Serializable id) {
		return clazz.cast(getHibernateSession().get(clazz, id));
	}

	public Long create(final BasicTable item) {
		return (Long) getHibernateSession().save(item);
	}

	public void update(final BasicTable item) {
		getHibernateSession().update(item);
	}

	public void delete(final BasicTable item) {
		getHibernateSession().delete(item);
	}
}
