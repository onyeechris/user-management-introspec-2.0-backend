package com.activedge.usermgt.model;

import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import javax.naming.Name;

@Entry(base = "ou=people", objectClasses = { "person", "inetOrgPerson", "top" })
public class LdapUser  {

    @Id
    private Name id;

//    private @Attribute(name = "ou") String organization;
    private @Attribute(name = "cn") String username;
//    private @Attribute(name = "uid") String userid;
    private @Attribute(name = "sn") String userid;
//    private @Attribute(name = "userPassword") String password;

    public LdapUser() {
    }

    public LdapUser(String username, String password) {
        this.username = username;
//        this.password = password;
    }

    public Name getId() {
        return id;
    }

    public void setId(Name id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }

//    public String getOrganization() {
//        return organization;
//    }
//
//    public void setOrganization(String organization) {
//        this.organization = organization;
//    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    @Override
    public String toString() {
        return "Username: " + username + " Password: " + username + " Org: " + username + " Userid: " + userid;
    }

}
