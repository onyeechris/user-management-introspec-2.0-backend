package com.activedge.usermgt.model.log;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import java.time.Instant;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
// https://docs.spring.io/spring-data/redis/docs/current/reference/html/#redis.repositories
/*
This class clears redis object after 24(216000 seconds) hours
 */
@RedisHash(value = "MakerItem", timeToLive = 216000)
public class MakerItem {

    private String id;
    private String action;
    private String payload;
    @Indexed
    private String maker;
    @JsonFormat(pattern = "MM/dd/yyyy HH:mm:ssS")
    private LocalDateTime at;

    public String getMaker() {
        return maker;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }
}
