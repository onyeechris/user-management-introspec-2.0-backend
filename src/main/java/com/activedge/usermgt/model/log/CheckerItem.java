package com.activedge.usermgt.model.log;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("CheckerItem")
public class CheckerItem {

    private String id;
    private String payload;

}
