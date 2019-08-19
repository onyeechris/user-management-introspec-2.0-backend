package com.activedge.usermgt.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "staffmodule")
@Data
@SequenceGenerator(name = "smGenerator", allocationSize = 50)
public class StaffModule {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "smGenerator")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "module")
    @JsonManagedReference
    Module module;

    @ManyToOne
    @JoinColumn(name = "staff")
    Staff staff;

    LocalDateTime assignAt;

    int grade;

}
