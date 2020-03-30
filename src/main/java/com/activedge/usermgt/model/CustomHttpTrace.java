package com.activedge.usermgt.model;

import com.activedge.usermgt.model.enumeration.Severity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.time.Instant;

@Getter
@Setter
@Builder
@ToString
@Document(collection = "http_trace")
public class CustomHttpTrace {
    @Id
    private String id;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Instant timestamp;
    @Enumerated(EnumType.STRING)
    private Severity severity = Severity.NORMAL;
    private String username;
    private String sourceIp;
    private String path;
    private String queryParams;
    private String method;
    private long timetaken;
    private String rawBody;
}