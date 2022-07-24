package com.maybank.sample.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.maybank.sample.entity.Employee;

@Repository
@Transactional
public class EmployeeDAO<T> extends PageDAO<Employee> {

	public List<Employee> getEmployee() {
		Criteria crit = getHibernateSession().createCriteria(Employee.class);

		return crit.list();
	}

	public Employee getEmployeeById(final Long id) {
		Criteria crit = getHibernateSession().createCriteria(Employee.class);
		crit.add(Restrictions.eq("id", id));
		return (Employee) crit.uniqueResult();
	}

}
