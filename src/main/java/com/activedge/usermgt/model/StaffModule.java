package com.activedge.usermgt.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter @Setter
@SequenceGenerator(name = "smGenerator")
@Table(name = "staff_modules", uniqueConstraints = { @UniqueConstraint( columnNames = { "module", "staff" } ) })
@Document(collection = "staff_modules")
public class StaffModule {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    @ManyToOne
    @JoinColumn(name = "module")
    @DBRef
    Module module;

    @ManyToOne
    @JoinColumn(name = "staff")
    @JsonBackReference
    @DBRef
    Staff staff;

    LocalDateTime assignAt;

    int grade;

}
