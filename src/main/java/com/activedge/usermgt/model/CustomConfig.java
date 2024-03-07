package com.activedge.usermgt.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.annotation.RegEx;
import javax.validation.constraints.Pattern;
//import javax.validation.constraints.Pattern;

@Document
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomConfig extends AbstractAuditingEntity{
    @Id
    private String id;
    @NonNull
    @Pattern(regexp = "^(http(s):\\/\\/.)[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&\\\\=]*\\/[-a-zA-Z0-9@:%_\\+.~#?&\\\\=]*)$", message = "Url must be a valid url")
    private String url;
    @NonNull
    private String clientId;
    @NonNull
    private String clientSecret;
}
