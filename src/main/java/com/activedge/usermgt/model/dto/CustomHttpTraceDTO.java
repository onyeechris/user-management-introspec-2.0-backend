package com.activedge.usermgt.model.dto;

import com.activedge.usermgt.model.enumeration.Severity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;
@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CustomHttpTraceDTO {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Date timestamp;
    @Enumerated(EnumType.STRING)
    private Severity severity = Severity.NORMAL;
    private String username;
    private Integer status;
    private String sourceIp;
    private String path;
    private String queryParams;
    private String method;
    private long timeTaken;
    private String payload;
}
