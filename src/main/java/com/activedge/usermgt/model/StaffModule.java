package com.activedge.usermgt.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@SequenceGenerator(name = "smGenerator")
@Table(name = "staffmodule", uniqueConstraints = { @UniqueConstraint( columnNames = { "module", "staff" } ) })
public class StaffModule {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "smGenerator")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "module")
    Module module;

    @ManyToOne
    @JoinColumn(name = "staff")
    @JsonBackReference
    Staff staff;

    LocalDateTime assignAt;

    int grade;

}
