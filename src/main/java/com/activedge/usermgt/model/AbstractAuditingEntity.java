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
    @Column(name = "created_by", nullable = false, length = 50, updatable = false)
    @JsonIgnore
    protected String createdBy;

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    @JsonIgnore
//    @Temporal(TemporalType.TIMESTAMP)
    protected Instant createdDate = Instant.now();

    @LastModifiedBy
    @Column(name = "last_modified_by", length = 50)
    @JsonIgnore
    protected U lastModifiedBy;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    @JsonIgnore
    protected Instant lastModifiedDate = Instant.now();

}
