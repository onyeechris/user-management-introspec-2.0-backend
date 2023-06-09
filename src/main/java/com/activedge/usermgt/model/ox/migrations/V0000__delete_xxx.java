package com.activedge.usermgt.model.ox.migrations;

import com.mongodb.DB;
import org.springframework.stereotype.Component;
import ox.engine.exception.OxException;
import ox.engine.internal.OxEnvironment;
import ox.engine.structure.Migration;

@Component
public class V0000__delete_xxx implements Migration {

    @Override
    public void up(OxEnvironment oxEnvironment) throws OxException {
        System.out.println(">>>>>>>>>>>>>>      About to drop database      <<<<<<<<<<<<");

        DB db = oxEnvironment.getMongoDatabase();

        db.dropDatabase();

    }

    @Override
    public void down(OxEnvironment oxEnvironment) throws OxException {
        /*
        oxEnvironment.execute(OxAction
            .removeIndex("user_id_idx")
            .setCollection("booking")
            .ifExists());
        */
    }
}
