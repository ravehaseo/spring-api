package com.maybank.sample.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Criterion;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.maybank.sample.constants.EmploymentStatus;
import com.maybank.sample.constants.ResponseStatus;
import com.maybank.sample.dao.EmployeeDAO;
import com.maybank.sample.dto.EmployeeDTO;
import com.maybank.sample.dto.ErrorDTO;
import com.maybank.sample.dto.PageResponseDTO;
import com.maybank.sample.dto.ResponseDTO;
import com.maybank.sample.entity.Employee;
import com.maybank.sample.exception.CommonException;
import com.maybank.sample.page.GeneralPage;
import com.maybank.sample.page.Pagination;
import com.maybank.sample.service.EmployeeService;
import com.maybank.sample.util.RestfulUtil;

@Component
@Transactional(readOnly = true)
public class EmployeeServiceImpl implements EmployeeService {

	protected final transient XLogger log = XLoggerFactory.getXLogger(getClass().getName());

	@Autowired
	private EmployeeDAO<Employee> employeeDAO;

	@Autowired
	private RestfulUtil rest;

	@SuppressWarnings("unchecked")
	@Override
	public PageResponseDTO<EmployeeDTO> getEmployeePage(final Integer page, final Integer perPage)
			throws HibernateException, ClassNotFoundException, Exception {

		PageResponseDTO<EmployeeDTO> respDTO = new PageResponseDTO<EmployeeDTO>();
		List<EmployeeDTO> listDTO = new ArrayList<EmployeeDTO>();

		try {
			Pagination pagination = new Pagination();
			pagination.setCurrent_page(page);
			pagination.setPer_page(perPage);

			List<Criterion> criterionList = new ArrayList<Criterion>();

			GeneralPage<Employee> dataPage = employeeDAO.getEntityPage(Employee.class, pagination, criterionList);

			for (Employee model : dataPage.getData()) {
				EmployeeDTO dto = new EmployeeDTO();
				dto.setId(model.getId());
				dto.setName(model.getName());
				dto.setAge(model.getAge());
				dto.setPosition(model.getPosition());
				dto.setEmploymentStatus(model.getEmploymentStatus());
				listDTO.add(dto);
			}
			respDTO.setPagination(dataPage.getPagination());

		} catch (Exception ex) {
			ErrorDTO error = new ErrorDTO();
			error.setMessage(ex.getMessage());
			respDTO.setError(error);
			return respDTO;
		}

		respDTO.setData(listDTO);
		return respDTO;
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = false)
	@Override
	public ResponseDTO addNewEmployee(final EmployeeDTO reqDTO) throws HibernateException, ClassNotFoundException, Exception {

		ResponseDTO respDTO = new ResponseDTO();

		try {
			this.validator(reqDTO);

			Employee newEmployee = new Employee();
			newEmployee.setName(reqDTO.getName());
			newEmployee.setAge(reqDTO.getAge());
			newEmployee.setPosition(reqDTO.getPosition());
			newEmployee.setEmploymentStatus(EmploymentStatus.getStatusById(Integer.parseInt(reqDTO.getEmploymentStatus())).name());

			employeeDAO.create(newEmployee);

		} catch (Exception e) {
			ErrorDTO error = new ErrorDTO();
			error.setMessage(e.getMessage());
			respDTO.setError(error);
			respDTO.setStatus(ResponseStatus.FAILED);
			return respDTO;
		}

		respDTO.setStatus(ResponseStatus.SUCCESS);
		respDTO.setMessage("Employee successfully added. ");
		respDTO.setEmpDTO(reqDTO);
		return respDTO;
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = false)
	@Override
	public ResponseDTO addNewEmployeeList(final List<EmployeeDTO> reqDTO) throws HibernateException, ClassNotFoundException, Exception {

		ResponseDTO respDTO = new ResponseDTO();
		List<Employee> success = new ArrayList<>();

		try {
			if (reqDTO != null && reqDTO.size() > 0) {
				for (EmployeeDTO dto : reqDTO) {
					this.validator(dto);
					Employee newEmployee = new Employee();
					newEmployee.setName(dto.getName());
					newEmployee.setAge(dto.getAge());
					newEmployee.setPosition(dto.getPosition());
					newEmployee.setEmploymentStatus(EmploymentStatus.getStatusById(Integer.parseInt(dto.getEmploymentStatus())).name());
					success.add(newEmployee);

				}

				for (Employee emp : success) {
					employeeDAO.create(emp);
				}

			}

		} catch (Exception e) {
			ErrorDTO error = new ErrorDTO();
			error.setMessage(e.getMessage());
			respDTO.setError(error);
			respDTO.setStatus(ResponseStatus.FAILED);
			return respDTO;
		}

		respDTO.setStatus(ResponseStatus.SUCCESS);
		respDTO.setMessage("Employees successfully added. ");
		return respDTO;
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = false)
	@Override
	public ResponseDTO retrieveEmployee(final String id) throws HibernateException, ClassNotFoundException, Exception {

		ResponseDTO respDTO = new ResponseDTO();

		try {
			if (id == null) {
				throw new CommonException("No ID Present. ");
			}

			Employee employee = employeeDAO.getEmployeeById(Long.parseLong(id));
			if (employee == null) {
				throw new CommonException("Employee Doesn't Exist. ");
			}
			EmployeeDTO dto = new EmployeeDTO();
			dto.setId(employee.getId());
			dto.setName(employee.getName());
			dto.setAge(employee.getAge());
			dto.setPosition(employee.getPosition());
			dto.setEmploymentStatus(employee.getEmploymentStatus());

			respDTO.setEmpDTO(dto);

		} catch (Exception e) {
			ErrorDTO error = new ErrorDTO();
			error.setMessage(e.getMessage());
			respDTO.setError(error);
			respDTO.setStatus(ResponseStatus.FAILED);
			return respDTO;
		}

		respDTO.setStatus(ResponseStatus.SUCCESS);
		respDTO.setMessage("Employee successfully retrieved. ");
		return respDTO;
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = false)
	@Override
	public ResponseDTO modifyEmployee(final EmployeeDTO reqDTO) throws HibernateException, ClassNotFoundException, Exception {

		ResponseDTO respDTO = new ResponseDTO();

		try {
			if (reqDTO.getId() == null || reqDTO.getId() == 0L) {
				throw new CommonException("No ID Present. ");
			}

			Employee employee = employeeDAO.getEmployeeById(reqDTO.getId());

			if (employee == null) {
				throw new CommonException("Employee Doesn't Exist. ");
			}

			if (reqDTO.getName() != null && reqDTO.getName().length() > 0) {
				employee.setName(reqDTO.getName());
			}
			if (reqDTO.getAge() != null && reqDTO.getAge() > 0) {
				employee.setAge(reqDTO.getAge());
			}
			if (reqDTO.getEmploymentStatus() != null && reqDTO.getEmploymentStatus().length() > 0) {
				employee.setEmploymentStatus(EmploymentStatus.getStatusById(Integer.parseInt(reqDTO.getEmploymentStatus())).name());
			}
			if (reqDTO.getPosition() != null && reqDTO.getPosition().length() > 0) {
				employee.setPosition(reqDTO.getPosition());
			}

			employeeDAO.update(employee);

		} catch (Exception e) {
			ErrorDTO error = new ErrorDTO();
			error.setMessage(e.getMessage());
			respDTO.setError(error);
			respDTO.setStatus(ResponseStatus.FAILED);
			return respDTO;
		}

		respDTO.setStatus(ResponseStatus.SUCCESS);
		respDTO.setMessage("Employee successfully updated. ");
		return respDTO;
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = false)
	@Override
	public ResponseDTO deleteEmployee(final Long id) throws HibernateException, ClassNotFoundException, Exception {

		ResponseDTO respDTO = new ResponseDTO();

		try {
			if (id == null || id == 0L) {
				throw new CommonException("No ID Present. ");
			}

			Employee employee = employeeDAO.getEmployeeById(id);
			if (employee == null) {
				throw new CommonException("Employee Doesn't Exist. ");
			}

			employeeDAO.delete(employee);

		} catch (Exception e) {
			ErrorDTO error = new ErrorDTO();
			error.setMessage(e.getMessage());
			respDTO.setError(error);
			respDTO.setStatus(ResponseStatus.FAILED);
			return respDTO;
		}

		respDTO.setStatus(ResponseStatus.SUCCESS);
		respDTO.setMessage("Employee successfully deleted. ");
		return respDTO;
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = false)
	@Override
	public ResponseDTO callAPI(final Long id) throws HibernateException, ClassNotFoundException, Exception {

		ResponseDTO respDTO = new ResponseDTO();

		try {
			if (id == null || id == 0L) {
				throw new CommonException("No ID Present. ");
			}

			String url = appendGetUrlParameter("http://localhost:8080/getEmployee/", id);

			log.info("URL sent: " + url);
			ResponseDTO apiDTO = rest.send(url, null, HttpMethod.GET, null, 10000, ResponseDTO.class, false);
			if (apiDTO.getError() != null) {
				throw new CommonException("API ERROR" + apiDTO.getError().getMessage());
			}
			EmployeeDTO empDTO = apiDTO.getEmpDTO();
			log.info("Response received from API: " + empDTO);
			respDTO.setEmpDTO(empDTO);

		} catch (Exception e) {
			ErrorDTO error = new ErrorDTO();
			error.setMessage(e.getMessage());
			respDTO.setError(error);
			respDTO.setStatus(ResponseStatus.FAILED);
			return respDTO;
		}

		respDTO.setStatus(ResponseStatus.SUCCESS);
		respDTO.setMessage("Employee successfully extracted from another API. ");
		return respDTO;
	}

	private String appendGetUrlParameter(String url, Long id) {

		StringBuilder sb = new StringBuilder();

		sb.append(url);

		if (id != 0L)
			sb.append("" + id);

		return sb.toString();
	}

	private void validator(EmployeeDTO dto) throws CommonException {

		if (dto.getName().equals(null) || StringUtils.isEmpty(dto.getName())) {
			throw new CommonException("Name field is empty.");
		}
		if (dto.getAge().equals(null) || dto.getAge() == 0) {
			throw new CommonException("Age field is invalid.");
		}
		if (dto.getEmploymentStatus().equals(null) || StringUtils.isEmpty(dto.getEmploymentStatus())) {
			throw new CommonException("Employment Status field is empty.");
		}
		if (dto.getPosition().equals(null) || StringUtils.isEmpty(dto.getPosition())) {
			throw new CommonException("Position field is empty.");

		}

	}

}
