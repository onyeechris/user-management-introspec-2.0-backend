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
    private @Attribute(name = "CN") String fullname;
    private @Attribute(name = "Name") String name;
    private @Attribute(name = "GivenName") String givenname;
    private @Attribute(name = "DistinguishedName") String distinguishedName;
    private @Attribute(name = "sn") String surname;
    private @Attribute(name = "EmailAddress") String mail;
    private @Attribute(name = "SamAccountName") String username;
    private @Attribute(name = "sAMAccountType") String samAccountType;
    private @Attribute(name = "UserPrincipalName") String userPrincipalName;
    private @Attribute(name = "userPassword") String password;

    public LdapUser() {
    }

    public LdapUser(String username, String password) {
        this.username = this.username;
        this.password = password;
    }


}
