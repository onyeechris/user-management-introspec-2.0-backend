package com.activedge.usermgt.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserStatusResponse implements Serializable {
    private String userId;
    private boolean active;
    private String message;
    private LocalDateTime timestamp;

    public UserStatusResponse(String userId, boolean active, String message, LocalDateTime timestamp) {
        this.userId = userId;
        this.active = active;
        this.message = message;
        this.timestamp = timestamp;
    }
}
