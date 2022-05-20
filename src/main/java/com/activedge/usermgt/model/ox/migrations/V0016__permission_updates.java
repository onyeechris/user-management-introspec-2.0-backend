package com.activedge.usermgt.model.ox.migrations;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBRef;
import ox.engine.exception.OxException;
import ox.engine.internal.OxEnvironment;
import ox.engine.structure.Migration;

public class V0016__permission_updates implements Migration {

    @Override
    public void up(OxEnvironment oxEnvironment) throws OxException {
        DB db = oxEnvironment.getMongoDatabase();

        db.getCollection("permissions").insert(BasicDBObjectBuilder
                .start()
                .add("_id", "can_view_gim")
                .add("action", "can view gim")
                .add("description", "can view gim scheme")
                .add("module", new DBRef("introspec_modules", "SETTLEMENT"))
                .get());

        db.getCollection("permissions").insert(BasicDBObjectBuilder
                .start()
                .add("_id", "can_view_gim_reports")
                .add("action", "can view gim reports")
                .add("description", "can view gim reports")
                .add("module", new DBRef("introspec_modules", "SETTLEMENT"))
                .get());

    }

    @Override
    public void down(OxEnvironment oxEnvironment) throws OxException {

    }
}
