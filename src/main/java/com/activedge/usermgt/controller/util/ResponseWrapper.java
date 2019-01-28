package com.activedge.usermgt.controller.util;

import com.activedge.usermgt.model.log.MakerItem;
import com.activedge.usermgt.repository.redis.MakerItemRepository;
import com.activedge.usermgt.security.SecurityUtils;
import com.activedge.usermgt.service.SpringUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "A wrapper Transfer Entity for returning list items")
public class ResponseWrapper {

	private Object payload;
	
	private Page<?> page;

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
	@ApiModel(description = "The Meta Transfer Entity for returning summary for list items")
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
