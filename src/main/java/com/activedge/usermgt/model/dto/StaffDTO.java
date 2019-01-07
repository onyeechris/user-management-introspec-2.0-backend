package com.activedge.usermgt.model.dto;

import com.activedge.usermgt.model.enumeration.MakerChecker;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Staff entity.
 */
public class StaffDTO implements Serializable {

    private Long id;

    @NotNull
    private String firstName;

    private String lastName;

    private String phone;

    @NotNull
    private String email;

    @NotNull
    private String password;

    private MakerChecker makerChecker;

    private Instant hireDate;

    private Long groupId;

    private Long groupsId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public MakerChecker getMakerChecker() {
        return makerChecker;
    }

    public void setMakerChecker(MakerChecker makerChecker) {
        this.makerChecker = makerChecker;
    }

    public Instant getHireDate() {
        return hireDate;
    }

    public void setHireDate(Instant hireDate) {
        this.hireDate = hireDate;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupsId) {
        this.groupId = groupsId;
    }

    public Long getGroupsId() {
        return groupsId;
    }

    public void setGroupsId(Long groupsId) {
        this.groupsId = groupsId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StaffDTO staffDTO = (StaffDTO) o;
        if (staffDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), staffDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "StaffDTO{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", phone='" + getPhone() + "'" +
            ", email='" + getEmail() + "'" +
            ", password='" + getPassword() + "'" +
            ", makerChecker='" + getMakerChecker() + "'" +
            ", hireDate='" + getHireDate() + "'" +
            ", group=" + getGroupId() +
            ", groups=" + getGroupsId() +
            "}";
    }
}
