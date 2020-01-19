package com.activedge.usermgt.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * Base abstract class for entities which will hold definitions for created, last modified by and created,
 * last modified by date.
 */
@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractAuditingEntity<U> implements Serializable {

    protected static final long serialVersionUID = 1L;

    @CreatedBy
    @Column(name = "approved_by", nullable = false, updatable = false)
    @JsonIgnore
    protected String approvedBy;

    @CreatedDate
    @Column(name = "approved_date", nullable = false, updatable = false)
    @JsonIgnore
    protected Instant approvedDate = Instant.now();

    @LastModifiedBy
    @Column(name = "last_modified_approved_by")
    @JsonIgnore
    protected U lastModifiedApprovedBy;

    @LastModifiedDate
    @Column(name = "last_modified_approved_date")
    @JsonIgnore
    protected Instant lastModifiedApprovedDate = Instant.now();

}
