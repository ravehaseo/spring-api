package com.maybank.sample.service;

import java.util.List;

import org.hibernate.HibernateException;

import com.maybank.sample.dto.EmployeeDTO;
import com.maybank.sample.dto.PageResponseDTO;
import com.maybank.sample.dto.ResponseDTO;

public interface EmployeeService {

	PageResponseDTO<EmployeeDTO> getEmployeePage(final Integer page, final Integer perPage)
			throws HibernateException, ClassNotFoundException, Exception;

	ResponseDTO addNewEmployee(final EmployeeDTO reqDTO) throws HibernateException, ClassNotFoundException, Exception;

	ResponseDTO addNewEmployeeList(final List<EmployeeDTO> reqDTO) throws HibernateException, ClassNotFoundException, Exception;

	ResponseDTO modifyEmployee(final EmployeeDTO reqDTO) throws HibernateException, ClassNotFoundException, Exception;

	ResponseDTO retrieveEmployee(final String id) throws HibernateException, ClassNotFoundException, Exception;

	ResponseDTO deleteEmployee(final Long id) throws HibernateException, ClassNotFoundException, Exception;

	ResponseDTO callAPI(final Long id) throws HibernateException, ClassNotFoundException, Exception;

}
