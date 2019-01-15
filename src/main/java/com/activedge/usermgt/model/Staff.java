package com.activedge.usermgt.model;

import com.activedge.usermgt.model.enumeration.MakerChecker;
import com.activedge.usermgt.model.event.StaffEntityListener;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

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

    @NotNull
    @Column(nullable = false)
//    @ColumnDefault("1")
    private boolean activated = false;

    @ManyToMany
    @JoinTable(
            name = "staff_authority",
            joinColumns = {@JoinColumn(name = "staff_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "name")})
    @BatchSize(size = 10)
    private Set<Authority> authorities = new HashSet<>();

    @ManyToOne
//    @JsonIgnoreProperties("staff")
    private Group group;

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
        this.email = StringUtils.lowerCase(email, Locale.ENGLISH);;
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

    public Group getGroup() {
        return group;
    }

    public Staff group(Group group) {
        this.group = group;
        return this;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
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
                ", Authorities='" + getAuthorities() + "'" +
                ", hireDate='" + getHireDate() + "'" +
                "}";
    }

}
