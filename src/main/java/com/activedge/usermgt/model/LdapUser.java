package com.activedge.usermgt.model;

import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

@Entry(base = "ou=people", objectClasses = { "person", "inetOrgPerson", "top" })
public class LdapUser  {

    @Id
    private @Attribute(name = "uid") String userid;
    private @Attribute(name = "ou") String organization;
    private @Attribute(name = "cn") String fullname;
    private @Attribute(name = "sn") String username;
    private @Attribute(name = "mail") String mail;
    private @Attribute(name = "description") String description;
    private @Attribute(name = "member") String member;
    private @Attribute(name = "title") String title;
    private @Attribute(name = "userPassword") String password;

    public LdapUser() {
    }

    public LdapUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
