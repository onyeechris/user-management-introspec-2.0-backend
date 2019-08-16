package com.activedge.usermgt.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;

/**
 * Base abstract class for entities which will hold definitions for created, last modified by and created,
 * last modified by date.
 */
@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractAuditingEntity<U> implements Serializable {

    protected static final long serialVersionUID = 1L;

//    @Column(name = "created_by", nullable = true, length = 50, updatable = false)
//    @JsonIgnore
//    protected String createdBy;

    @CreatedBy
    @Column(name = "approved_by", nullable = false, length = 50, updatable = false)
    @JsonIgnore
    protected String approvedBy;

//    @Column(name = "created_date", nullable = false, updatable = false)
//    @JsonIgnore
//    protected LocalDateTime createdDate = LocalDateTime.now();

    @CreatedDate
    @Column(name = "approved_date", nullable = false, updatable = false)
    @JsonIgnore
//    @Temporal(TemporalType.TIMESTAMP)
    protected Instant approvedDate = Instant.now();

//    @Column(name = "last_modified_by", length = 50)
//    @JsonIgnore
//    protected U lastModifiedBy;

    @LastModifiedBy
    @Column(name = "last_modified_approved_by", length = 50)
    @JsonIgnore
    protected U lastModifiedApprovedBy;

//    @Column(name = "last_modified_date")
//    @JsonIgnore
//    protected LocalDateTime lastModifiedDate = LocalDateTime.now();

    @LastModifiedDate
    @Column(name = "last_modified_approved_date")
    @JsonIgnore
    protected Instant lastModifiedApprovedDate = Instant.now();

}
