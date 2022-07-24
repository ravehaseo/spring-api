package com.maybank.sample.dto;

import com.maybank.sample.constants.ResponseStatus;
import com.maybank.sample.page.Pagination;

public class ResponseDTO {

	private EmployeeDTO empDTO;

	private Pagination pagination;

	private ErrorDTO error;

	private ResponseStatus status;

	private String message;

	public Pagination getPagination() {
		return pagination;
	}

	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}

	public EmployeeDTO getEmpDTO() {
		return empDTO;
	}

	public void setEmpDTO(EmployeeDTO empDTO) {
		this.empDTO = empDTO;
	}

	public ErrorDTO getError() {
		return error;
	}

	public void setError(ErrorDTO error) {
		this.error = error;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
