package com.activedge.usermgt.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import javax.naming.Name;

//@Entry(base = "ou=people", objectClasses = { "person", "inetOrgPerson", "top" })
//@Entry(base = "ou=usr", objectClasses = { "top",  "person", "organizationalPerson", "user" })
@Entry(objectClasses = { "top",  "person", "organizationalPerson", "user" })
@Getter
@Setter
public class LdapUser  {

    @Id
    private Name id;
    private @Attribute(name = "SID") String userid;
    private @Attribute(name = "ou") String organization;
    private @Attribute(name = "CN") String fullname;
    private @Attribute(name = "City") String city;
    private @Attribute(name = "Department") String department;
    private @Attribute(name = "Description") String description;
    private @Attribute(name = "DistinguishedName") String distinguishedName;
    private @Attribute(name = "sn") String username;
    private @Attribute(name = "EmailAddress") String mail;
    private @Attribute(name = "EmployeeID") String employeeID;
    private @Attribute(name = "memberOf") String member;
    private @Attribute(name = "MobilePhone") String phone;
    private @Attribute(name = "Title") String title;
    private @Attribute(name = "SamAccountName") String samAccountName;
    private @Attribute(name = "sAMAccountType") String samAccountType;
    private @Attribute(name = "userPrincipalName") String userPrincipalName;
    private @Attribute(name = "userPassword") String password;

    public LdapUser() {
    }

    public LdapUser(String username, String password) {
        this.username = username;
        this.password = password;
    }


}
