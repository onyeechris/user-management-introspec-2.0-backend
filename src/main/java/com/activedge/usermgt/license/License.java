package com.activedge.usermgt.license;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter @Setter @ToString
public class License {
    private String type;
    private String no_of_users;
    private int hardware;
    private String unit_charge;
    private String expiry;
    private int total_price;
    private String grace;
    private String status;
    private String partial_access;
    private String id;
    private String licence;
    private String updated_at;

}
