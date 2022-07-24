package com.maybank.sample.dto;

import java.util.ArrayList;
import java.util.List;

public class RequestDTO {

	private EmployeeDTO employee;
	private List<EmployeeDTO> employeeList = new ArrayList<>();

	public EmployeeDTO getEmployee() {
		return employee;
	}

	public void setEmployee(EmployeeDTO employee) {
		this.employee = employee;
	}

	public List<EmployeeDTO> getEmployeeList() {
		return employeeList;
	}

	public void setEmployeeList(List<EmployeeDTO> employeeList) {
		this.employeeList = employeeList;
	}

}
