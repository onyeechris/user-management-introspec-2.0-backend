package com.activedge.usermgt.model.log;

import com.activedge.usermgt.model.Group;
import com.activedge.usermgt.model.enumeration.Action;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.TemporalType.TIMESTAMP;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Document(collection = "groups_audit")
public class GroupLog {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(
                    name = "group_id",
                    referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_group_history_log")),
            @JoinColumn(
                    name = "module_id",
                    referencedColumnName = "module", foreignKey = @ForeignKey(name = "FK_group_module_history_log")),
    })
    @DBRef
    private Group group;

    @Type(type = "text")
    @Column(updatable = false)
    private String groupContent;

    @CreatedBy
    private String modifiedBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    private Date modifiedDate;

    @Enumerated(STRING)
    private Action action;

    public GroupLog() {
    }

    public GroupLog(Group group, Action action) {
        this.group = group;
        this.groupContent = group.toString();
        this.action = action;
    }

}
