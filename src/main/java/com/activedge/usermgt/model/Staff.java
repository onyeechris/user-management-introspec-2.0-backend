package com.activedge.usermgt.model;

import com.activedge.usermgt.model.enumeration.Type;
import com.activedge.usermgt.model.event.StaffEntityListener;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@Entity
@Table(name = "staff")
@EntityListeners(StaffEntityListener.class)
@SequenceGenerator(name = "tabGenerator", initialValue = 5, allocationSize = 50)
@SQLDelete(sql="UPDATE staff SET activated = '0' WHERE id = ?")
@Where(clause="activated <> '0'")
@Document(collection = "staff")
public class Staff extends AbstractAuditingEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

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

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @NotNull
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
    private Boolean activated = false;

    @OneToMany(mappedBy = "staff", fetch = FetchType.EAGER)
    @DBRef
    Set<StaffModule> assignments;
    @NotNull
    @Column(nullable = false)
    private Boolean enable2FA = false;
    private Boolean default2FA = false;
    private Boolean enrol = false;
    private String secret;

    @ManyToMany
    @JoinTable(name = "staff_authority", joinColumns = {
            @JoinColumn(name = "staff_id", referencedColumnName = "id") },
            inverseJoinColumns = {
                    @JoinColumn(name = "authority_id", referencedColumnName = "id")
                }
            )
//    @BatchSize(size = 10)
    @DBRef()
    private Set<Authority> authorities = new HashSet<>();

    @ManyToMany(mappedBy = "staffs")
    @DBRef
    private Set<Group> groups = new HashSet<>();

    @PreRemove
    public void deleteGroup() {
        this.activated = false;
    }

    public Boolean isActivated() {
        return activated;
    }
    public Boolean is2FAEnabled(){return enable2FA;}
    public Boolean isDefault(){return default2FA;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Staff)) return false;
        Staff that = (Staff) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


}
