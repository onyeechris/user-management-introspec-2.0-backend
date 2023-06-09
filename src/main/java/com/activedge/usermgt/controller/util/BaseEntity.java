package com.activedge.usermgt.controller.util;

import com.activedge.usermgt.model.Module;
import com.activedge.usermgt.repository.ModuleRepository;
import org.springframework.web.bind.ServletRequestBindingException;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.util.Optional;

public class BaseEntity {

    private ModuleRepository moduleRepository;

    public BaseEntity(ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }

    /*
     * Helper methods
     */
    protected Module getModule(String mdl) throws ServletRequestBindingException {
        Optional<Module> module = this.moduleRepository.findOneWithEagerRelationships(mdl);

        if(!module.isPresent()) {;
            throw new ServletRequestBindingException("Module[" + mdl + "] not found");
        }

        System.out.println("Module gotten is " + module.get());

        return module.get();
    }

}
