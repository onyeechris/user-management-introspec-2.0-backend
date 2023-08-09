package com.activedge.usermgt.service;

import com.activedge.usermgt.model.CustomConfig;

import java.util.List;

public interface CustomConfigService {
    CustomConfig saveConfig(CustomConfig data);
    List<CustomConfig> findConfig();
    CustomConfig updateConfig(String id, CustomConfig data);
    void deleteConfig(String id);

}
