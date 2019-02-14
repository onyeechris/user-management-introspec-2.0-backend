package com.activedge.usermgt.util.facade;

import com.activedge.usermgt.exception.ActivityRequiredException;
import com.activedge.usermgt.model.log.MakerItem;
import com.activedge.usermgt.security.SecurityUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.validation.ValidationException;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
public abstract class Spy {

    private static MakerItem makerItem = null;
    private static ObjectMapper mapper = null;

    @Value("${makerChecker.enabled}")
    private boolean mc_enabled;

    final public void checkModel() throws ActivityRequiredException {
        welcome();

        if(mcEnabled()) {
            if(SecurityUtils.isCurrentUserInRole("ROLE_MAKER")) {
                logRequest();
            }else if(SecurityUtils.isCurrentUserInRole("ROLE_CHECKER")) {
                approveRequest();
            } else {
                throw new ValidationException("Oops! you don't have the ROLE(MAKER) to create a transaction.");
            }
        }

        complete();
    }

    public void welcome() {
        log.info("Check model start...");
    }

    public void complete() { log.info("Check model complete..."); }

    public boolean mcEnabled() {
        return true;
    }

    abstract void logRequest() throws ActivityRequiredException;

    abstract void approveRequest();

    protected static MakerItem getMakerItem() {
        if(makerItem == null) {
            makerItem = new MakerItem();
        }
        return makerItem;
    }

    protected static ObjectMapper getMapper() {
        if(mapper == null) {
            mapper = new ObjectMapper();
        }
        return mapper;
    }

}
