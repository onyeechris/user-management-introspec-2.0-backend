package com.activedge.usermgt.controller.util;

import org.springframework.data.domain.Page;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
//@ApiModel(description = "A wrapper Transfer Entity for returning list items")
public class ResponseWrapper {

	private Object payload;
	
	private Page<?> page;

	/**
	 * Metadata.
	 *
	 * @see <a href="http://localhost:9100/auth-service/#overview-pagination">Meta type documentation</a>
	 */
	private MetaFields meta = new MetaFields();

	public ResponseWrapper(Page<?> page) {
		this.payload = page.getContent();
		this.meta.setSize(page.getSize());
		this.meta.setNumber(page.getNumber());
		this.meta.setNumberOfElements(page.getNumberOfElements());
		this.meta.setTotalPages(page.getTotalPages());
		this.meta.setTotalElements(page.getTotalElements());
		this.page = null;
	}

	@Data
	/**
	 * The Meta Transfer Entity for returning summary for list items
	 */
	public class MetaFields {
		private int size;
		private int number;
		private int numberOfElements;
		private int totalPages;
		private long totalElements;
		private int pageNumber;
		private int pageSize;
	}

}
