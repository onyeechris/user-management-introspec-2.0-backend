package com.activedge.usermgt.model;

import com.activedge.usermgt.model.enumeration.Type;
import com.activedge.usermgt.model.event.StaffEntityListener;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import static javax.persistence.CascadeType.*;

@Getter
@Setter
@ToString
@Entity
@Table(name = "staff")
@EntityListeners(StaffEntityListener.class)
@SequenceGenerator(name = "tabGenerator", initialValue = 5, allocationSize = 50)
public class Staff extends AbstractAuditingEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tabGenerator")
    private Long id;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String first_name;

    @Column(name = "last_name")
    private String last_name;

    @Column(name = "phone")
    @Size(min = 9, max = 13)
    private String phone;

    @NotNull
    @Column(name = "email", nullable = false, unique = true)
    @Email
    private String email;

    @NotNull
    @Column(name = "i_password", nullable = false)
    @Size(min = 50, max = 100)
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private Type type;

    @Column(name = "hire_date")
    @JsonFormat(pattern = "MM/dd/yyyy")
    private LocalDate hireDate;

    @NotNull
    @Column(nullable = false)
    // @ColumnDefault("1")
    private Boolean activated = false;

    @OneToMany(mappedBy = "staff", fetch = FetchType.EAGER)
    Set<StaffModule> assignments;

//    @ManyToMany(cascade={PERSIST, MERGE, REFRESH, DETACH})
    @ManyToMany
    @JoinTable(name = "staff_authority", joinColumns = {
            @JoinColumn(name = "staff_id", referencedColumnName = "id") },
            inverseJoinColumns = {
//                    @JoinColumn(name = "module_id", referencedColumnName = "module"),
                    @JoinColumn(name = "authority_id", referencedColumnName = "code")
                }
            )
    @BatchSize(size = 10)
    private Set<Authority> authorities = new HashSet<>();

//    @ManyToOne
//    // @JsonIgnoreProperties("staff")
//    private Group group;
    @ManyToMany
    @JoinTable(name = "staff_group", joinColumns = {
            @JoinColumn(name = "staff_id", referencedColumnName = "id") },
            inverseJoinColumns = {
                    @JoinColumn(name = "module", referencedColumnName = "module"),
                    @JoinColumn(name = "group_id", referencedColumnName = "id")
            }
    )
    @BatchSize(size = 10)
    @JsonBackReference
    private Set<Group> groups = new HashSet<>();
/*
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public Staff first_name(String first_name) {
        this.first_name = first_name;
        return this;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public Staff last_name(String last_name) {
        this.last_name = last_name;
        return this;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    public String getPhone() {
        return phone;
    }

    public Staff phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public Staff email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = StringUtils.lowerCase(email, Locale.ENGLISH);
        ;
    }

    public String getPassword() {
        return password;
    }

    public Staff password(String password) {
        this.password = password;
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Type getType() {
        return type;
    }

    public Staff type(Type type) {
        this.type = type;
        return this;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public Staff hireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
        return this;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public Set<Group> getGroups() {
        return groups;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }

    //    public Group getGroup() {
//        return groups;
//    }
//
//    public Staff group(Group group) {
//        this.groups = group;
//        return this;
//    }
//
//    public void setGroup(Group group) {
//        this.group = group;
//    }

    public Boolean isActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public Set<StaffModule> getAssignments() {
        return assignments;
    }

    public void setAssignments(Set<StaffModule> assignments) {
        this.assignments = assignments;
    }

    //    @Override
//    public boolean equals(Object o) {
//        if (this == o) {
//            return true;
//        }
//        if (o == null || getClass() != o.getClass()) {
//            return false;
//        }
//        Staff staff = (Staff) o;
//        if (staff.getId() == null || getId() == null) {
//            return false;
//        }
//        return Objects.equals(getId(), staff.getId());
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hashCode(getId());
//    }

    @Override
    public String toString() {
        return "Staff{" + "id=" + getId() + ", first_name='" + getFirst_name() + "'" + ", last_name='" + getLast_name()
                + "'" + ", phone='" + getPhone() + "'" + ", email='" + getEmail() + "'" + ", password='" + getPassword()
//                + "'" + ", type='" + getType() + "'" + ", Authorities='" + getAuthorities() + "'"
                + "'" + ", hireDate='" + getHireDate()
                + "'" + "}";
    }
*/
    public Boolean isActivated() {
        return activated;
    }

}
