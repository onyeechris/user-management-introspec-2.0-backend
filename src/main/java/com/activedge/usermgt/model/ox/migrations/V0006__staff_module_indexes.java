package com.activedge.usermgt.model.ox.migrations;

import ox.engine.exception.OxException;
import ox.engine.internal.OxAction;
import ox.engine.internal.OxEnvironment;
import ox.engine.structure.Migration;
import ox.engine.structure.OrderingType;

public class V0006__staff_module_indexes implements Migration {
    @Override
    public void up(OxEnvironment oxEnvironment) throws OxException {
        oxEnvironment.execute(OxAction
                .createIndex("staff_module_id_idx")
                .addAttribute("module", OrderingType.ASC)
                .addAttribute("staff", OrderingType.ASC)
                .unique()
                .setCollection("staff_modules")
                .ifNotExists()
        );

    }

    @Override
    public void down(OxEnvironment oxEnvironment) throws OxException {

    }
}
