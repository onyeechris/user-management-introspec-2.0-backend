package com.activedge.usermgt.controller.util;

import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
//@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseWrapper {

	private Object content;
	
	private Page<?> page;

	private MetaFields meta = new MetaFields();
	
	public ResponseWrapper(Page<?> page) {
		this.content = page.getContent();
		this.meta.setSize(page.getSize());
		this.meta.setNumber(page.getNumber());
		this.meta.setNumberOfElements(page.getNumberOfElements());
		this.meta.setTotalPages(page.getTotalPages());
		this.meta.setTotalElements(page.getTotalElements());
		this.page = null;
	}

	@Data
	class MetaFields {
		private int size;
		private int number;
		private int numberOfElements;
		private int totalPages;
		private long totalElements;
		private int pageNumber;
		private int pageSize;
	}

}
