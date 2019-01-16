package com.activedge.usermgt.model.log;

import com.activedge.usermgt.model.Staff;
import com.activedge.usermgt.model.enumeration.Action;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

import java.util.Date;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.TemporalType.TIMESTAMP;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class StaffLog {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "staff_id", foreignKey = @ForeignKey(name = "FK_staff_history_log"))
    private Staff staff;

    @Type(type = "text")
    @Column(updatable = false)
    private String staffContent;

    @CreatedBy
    private String modifiedBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    private Date modifiedDate;

    @Enumerated(STRING)
    private Action action;

    public StaffLog() {
    }

    public StaffLog(Staff staff, Action action) {
        this.staff = staff;
        this.staffContent = staff.toString();
        this.action = action;
    }
}
