package com.activedge.usermgt.model.log;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.util.Date;

@Setter
@Getter
@ToString
@Entity
@Table(name = "audit_logs")
@Document(collection = "audit_logs")
public class ReqBody {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;
    private String url;
    private String client;
    private String user;
    private String headers;
    private String payload;
    private String dump;
    private Date logtime = new Date();;

}
