package com.activedge.usermgt.util.facade;

public interface RedisQueue {

    public void add2Queue(String action, Object target);

    public void delete4rmQueue(String id);

}
