package com.activedge.usermgt.service;

import com.activedge.usermgt.model.CustomConfig;
import com.activedge.usermgt.repository.CustomConfigRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomConfigServiceImpl implements CustomConfigService{
    private final CustomConfigRepo repo;

    public CustomConfigServiceImpl(CustomConfigRepo repo){
        this.repo = repo;
    }

    @Override
    public CustomConfig saveConfig(CustomConfig data){
        CustomConfig builder = CustomConfig.builder()
                .url(data.getUrl())
                .clientId(data.getClientId())
                .clientSecret(data.getClientSecret())
                .build();
        return repo.save(builder);
    }

    @Override
    public List<CustomConfig> findConfig(){
        return repo.findAll();
    }

    @Override
    public CustomConfig updateConfig(String id, CustomConfig data) {
        CustomConfig config = getConfigById(id);
        config.setUrl(data.getUrl());
        config.setClientId(data.getClientId());
        config.setClientSecret(data.getClientSecret());
        return repo.save(config);
    }

    private CustomConfig getConfigById(String id){
        Optional<CustomConfig> configOptional = repo.findById(id);
        try {
            return configOptional.orElseThrow(Exception::new);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void deleteConfig(String id){
        repo.deleteById(id);
    }
}
