package com.maybank.sample.dto;

import java.util.List;

import com.maybank.sample.page.Pagination;

public class PageResponseDTO<T> {

	private List<T> data;

	private Pagination pagination;

	private ErrorDTO error;

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public Pagination getPagination() {
		return pagination;
	}

	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}

	public ErrorDTO getError() {
		return error;
	}

	public void setError(ErrorDTO error) {
		this.error = error;
	}

}
