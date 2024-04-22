package com.activedge.usermgt.model.dto;

import com.activedge.usermgt.model.enumeration.Type;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A DTO for the Staff entity.
 */
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StaffDTO implements Serializable {


    private String id;

    @NotNull(message = "Staff firstname is required")
    @Size(max = 50)
    private String first_name;

    private String last_name;

    @Size(min = 9, max = 13, message = "phone number length too short or long.")
    private String phone;

    @NotNull(message = "Staff email address is required.")
    @Email(message = "Please enter a correct email address")
    private String email;

    private String username;

    @NotNull(message = "Staff ADMIN or USER type is required.")
    private Type user_type;

    @JsonFormat(pattern = "MM/dd/yyyy")
    private LocalDate hire_date;

    @NotNull(message = "Staff access group is required.")
    private Set<GroupDTO> groups = new HashSet<>();

    private Boolean activated;
    private Boolean enable2FA;
    private Boolean default2FA;
    private Boolean enrol;
    private String enroll;
    private String secret;

    public StaffDTO(@NotNull(message = "Staff firstname is required") @Size(max = 50) String first_name, String last_name, @Size(min = 3, message = "Username length too short.") String username, @NotNull(message = "Staff email address is required.") @Email(message = "Please enter a correct email address") String email, Type user_type) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.username = username;
        this.email = email;
        this.user_type = user_type;
    }

    @Override
    public String toString() {
        return "StaffDTO{" +
                "id=" + id +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", phone='" + phone + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", user_type=" + user_type +
                ", hire_date=" + hire_date +
                ", activated=" + activated +
                ", enable2FA=" + enable2FA +
                ", default2FA=" + default2FA +
                ", enrol=" + enrol +
                '}';
    }



}
