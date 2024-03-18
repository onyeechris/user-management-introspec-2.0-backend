package com.activedge.usermgt.license;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;
import javax.persistence.Id;
import java.time.LocalDate;


@Document(collection = "license")
@Getter @Setter @ToString
public class License {
    @Id
    private String id;

    @Column(name = "type")
    private String type;

    @Column(name = "no_of_users")
    private String no_of_users;

    @Column(name = "hardware")
    private String hardware;

    @Column(name = "unit_charge")
    private String unit_charge;

    @Column(name = "expiry")
    private LocalDate expiry;

    @Column(name = "total_price")
    private String total_price;

    @Column(name = "grace")
    private int grace;

    @Column(name = "status")
    private String status;

    @Column(name = "partial_access")
    private String partial_access;

    @Column(name = "license")
    private String licence;

    @Column(name = "updated_at")
    private String updated_at;

    public boolean isExpired() {
        LocalDate expiryWithGrace = expiry.plusDays(grace);
        return LocalDate.now().isAfter(expiryWithGrace);
    }

}
