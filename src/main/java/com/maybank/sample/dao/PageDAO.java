package com.maybank.sample.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;

import com.maybank.sample.page.GeneralPage;
import com.maybank.sample.page.Pagination;

public class PageDAO<T> extends CommonDAO<T> {

	public GeneralPage<T> getEntityPage(final Class<T> clazz, final Pagination pagination, final List<Criterion> criterionList) {

		Integer perPage = null;
		Integer currentPage = null;
		Integer firstResult = null;

		if (pagination != null) {
			perPage = pagination.getPer_page();
			currentPage = pagination.getCurrent_page();
			firstResult = (currentPage - 1) * perPage;
		}

		Criteria criteria = getHibernateSession().createCriteria(clazz, clazz.getSimpleName());

		if (criterionList != null && !criterionList.isEmpty()) {
			for (Criterion criterion : criterionList) {
				criteria.add(criterion);
			}
		}

		Long total = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();

		criteria.setProjection(null);
		if (pagination != null) {
			criteria.setFirstResult(firstResult);
			criteria.setMaxResults(perPage);
		}
		@SuppressWarnings("unchecked")
		List<T> data = criteria.list();

		GeneralPage<T> dataPage = new GeneralPage<T>();
		dataPage.setData(data);
		if (pagination != null) {
			pagination.setTotal(total);
			pagination.setCurrent_page(currentPage);
			pagination.setPer_page(perPage);
			pagination.setLast_page((int) Math.ceil(total * 1.0 / perPage));
			dataPage.setPagination(pagination);
		}

		return dataPage;
	}
}
