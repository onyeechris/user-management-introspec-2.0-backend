package com.activedge.usermgt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.ACCEPTED, reason = "CHECKER action required!.")
public class ActivityRequiredException extends Exception {

    public ActivityRequiredException(String message) {
        super(message);
    }
}
