package com.activedge.usermgt.controller;

import com.activedge.usermgt.controller.util.HeaderUtil;
import com.activedge.usermgt.model.CustomConfig;
import com.activedge.usermgt.service.CustomConfigService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/custom")
@SecurityRequirement(name = "introspec-cas")
@Slf4j
public class CustomConfigController {

    private final CustomConfigService service;

    public CustomConfigController(CustomConfigService service){
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody CustomConfig data, @RequestHeader(value = "Module", required = false) String module, Errors errors) throws URISyntaxException {
        if (errors.hasErrors()) {
            log.error("Error in creating new customConfig detected...\n{}", errors.getAllErrors());
            throw new ValidationException(errors.getAllErrors().stream()
                    .map(x -> x.getDefaultMessage())
                    .collect(Collectors.joining(", ")));
        }
        CustomConfig config = service.saveConfig(data);
        return ResponseEntity.created(new URI("/custom/" + config.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("customConfig", config.getId().toString()))
                .body(config);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @Valid @RequestBody CustomConfig data, @RequestHeader(value = "Module", required = false) String module,
                                    Errors errors) {
        if (errors.hasErrors()) {
            log.error("Error in updating customConfig detected...\n{}", errors.getAllErrors());
            throw new ValidationException(errors.getAllErrors().stream()
                    .map(x -> x.getDefaultMessage())
                    .collect(Collectors.joining(", ")));
        }
        CustomConfig config = service.updateConfig(id,data);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityCreationAlert("customConfig", config.getId().toString()))
                .body(config);
    }
    @GetMapping
    public ResponseEntity<?> findAll(@RequestHeader(value = "Module", required = false) String module) {
        List<CustomConfig> config = service.findConfig();
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityCreationAlert("customConfig", config.toString()))
                .body(config);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConfig(@PathVariable String id) {
        log.debug("REST request to delete {} : {}", "config", id);
        service.deleteConfig(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("config", id.toString())).build();
    }


}
