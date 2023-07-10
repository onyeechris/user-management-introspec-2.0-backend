package com.activedge.usermgt.model.ox.migrations;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBRef;
import ox.engine.exception.OxException;
import ox.engine.internal.OxEnvironment;
import ox.engine.structure.Migration;

public class V0018__permission_indexes implements Migration {
    @Override
    public void up(OxEnvironment oxEnvironment) throws OxException {
        System.out.println(">>>>>>>>>>>>>>      About to add 2fa collection      <<<<<<<<<<<<");

        DB db = oxEnvironment.getMongoDatabase();

        db.getCollection("permissions").insert(BasicDBObjectBuilder
                .start()
                .add("_id", "can_enable_2fa")
                .add("action", "can enable 2fa")
                .add("description", "can enable 2fa for all users")
                .add("module", new DBRef("introspec_modules", "USER"))
                .get());

    }

    @Override
    public void down(OxEnvironment oxEnvironment) throws OxException {

    }
}
