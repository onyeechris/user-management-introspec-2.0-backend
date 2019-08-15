package com.activedge.usermgt.model;

import com.activedge.usermgt.model.enumeration.MakerChecker;
import com.activedge.usermgt.model.event.StaffEntityListener;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

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
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

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
    @Column(name = "maker_checker")
    private MakerChecker makerChecker;

    @Column(name = "hire_date")
    @JsonFormat(pattern = "MM/dd/yyyy")
    private LocalDate hireDate;

    @NotNull
    @Column(nullable = false)
    // @ColumnDefault("1")
    private Boolean activated = false;

    @Transient
    private String redisKey;

    @OneToMany(mappedBy = "staff")
    Set<StaffModule> assignments;

    @ManyToMany
    @JoinTable(name = "staff_authority", joinColumns = {
            @JoinColumn(name = "staff_id", referencedColumnName = "id") },
            inverseJoinColumns = {
                    @JoinColumn(name = "module_id", referencedColumnName = "module"),
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
    private Set<Group> groups = new HashSet<>();

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

    public String getRedisKey() {
        return redisKey;
    }

    public void setRedisKey(String redisKey) {
        this.redisKey = redisKey;
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
        return "Staff{" + "id=" + getId() + ", firstName='" + getFirstName() + "'" + ", lastName='" + getLastName()
                + "'" + ", phone='" + getPhone() + "'" + ", email='" + getEmail() + "'" + ", password='" + getPassword()
                + "'" + ", makerChecker='" + getMakerChecker() + "'" + ", Authorities='" + getAuthorities() + "'"
                + "'" + ", RedisKey='" + getRedisKey() + "'" + ", hireDate='" + getHireDate()
                + "'" + "}";
    }

}
