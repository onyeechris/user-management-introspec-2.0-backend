package com.activedge.usermgt.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "staffmodule")
@Getter
@Setter
@SequenceGenerator(name = "smGenerator", allocationSize = 50)
public class StaffModule {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "smGenerator")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "module")
//    @JsonBackReference
    Module module;

    @ManyToOne
    @JoinColumn(name = "staff")
    @JsonBackReference
    Staff staff;

    LocalDateTime assignAt;

    int grade;

}
