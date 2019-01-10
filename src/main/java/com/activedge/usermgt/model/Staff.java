package com.activedge.usermgt.model;

import com.activedge.usermgt.model.enumeration.Action;
import com.activedge.usermgt.model.enumeration.MakerChecker;
import com.activedge.usermgt.model.event.StaffEntityListener;
import com.activedge.usermgt.model.log.StaffLog;
import com.activedge.usermgt.repository.StaffLogRepository;
import com.activedge.usermgt.service.BeanUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import static com.activedge.usermgt.model.enumeration.Action.DELETED;
import static com.activedge.usermgt.model.enumeration.Action.INSERTED;
import static com.activedge.usermgt.model.enumeration.Action.UPDATED;
import static javax.transaction.Transactional.TxType.MANDATORY;

@Entity
@Table(name = "staff")
@EntityListeners(StaffEntityListener.class)
public class Staff extends AbstractAuditingEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone")
    private String phone;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Column(name = "i_password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "maker_checker")
    private MakerChecker makerChecker;

    @Column(name = "hire_date")
    private Instant hireDate;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Groups group;

    @ManyToOne
//    @JsonIgnoreProperties("staff")
    private Groups groups;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public Staff firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Staff lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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
        this.email = email;
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

    public MakerChecker getMakerChecker() {
        return makerChecker;
    }

    public Staff makerChecker(MakerChecker makerChecker) {
        this.makerChecker = makerChecker;
        return this;
    }

    public void setMakerChecker(MakerChecker makerChecker) {
        this.makerChecker = makerChecker;
    }

    public Instant getHireDate() {
        return hireDate;
    }

    public Staff hireDate(Instant hireDate) {
        this.hireDate = hireDate;
        return this;
    }

    public void setHireDate(Instant hireDate) {
        this.hireDate = hireDate;
    }

    public Groups getGroup() {
        return group;
    }

    public Staff group(Groups groups) {
        this.group = groups;
        return this;
    }

    public void setGroup(Groups groups) {
        this.group = groups;
    }

    public Groups getGroups() {
        return groups;
    }

    public Staff groups(Groups groups) {
        this.groups = groups;
        return this;
    }

    public void setGroups(Groups groups) {
        this.groups = groups;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Staff staff = (Staff) o;
        if (staff.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), staff.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Staff{" +
                "id=" + getId() +
                ", firstName='" + getFirstName() + "'" +
                ", lastName='" + getLastName() + "'" +
                ", phone='" + getPhone() + "'" +
                ", email='" + getEmail() + "'" +
                ", password='" + getPassword() + "'" +
                ", makerChecker='" + getMakerChecker() + "'" +
                ", hireDate='" + getHireDate() + "'" +
                "}";
    }

}
