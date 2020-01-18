package com.activedge.usermgt.model.ox.migrations;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBRef;
import ox.engine.exception.OxException;
import ox.engine.internal.OxAction;
import ox.engine.internal.OxEnvironment;
import ox.engine.structure.Migration;
import ox.engine.structure.OrderingType;

public class V0004__authority_indexes implements Migration {

    @Override
    public void up(OxEnvironment oxEnvironment) throws OxException {
        DB db = oxEnvironment.getMongoDatabase();
        oxEnvironment.execute(OxAction
                .createIndex("auth_id_idx")
                .setCollection("introspec_authorities")
                .addAttribute("_id",OrderingType.ASC)
                .ifNotExists()
        );

        db.getCollection("introspec_authorities").insert(BasicDBObjectBuilder
                .start()
                .add("_id", "ROLE_DEV")
                .add("name", "ROLE_DEV")
                .get());

        db.getCollection("introspec_authorities").insert(BasicDBObjectBuilder
                .start()
                .add("_id", "ROLE_ADMIN")
                .add("name", "ROLE_ADMIN")
                .get());

        db.getCollection("introspec_authorities").insert(BasicDBObjectBuilder
                .start()
                .add("_id", "ROLE_USER")
                .add("name", "ROLE_USER")
                .get());

    }

    @Override
    public void down(OxEnvironment oxEnvironment) throws OxException {

    }
}
