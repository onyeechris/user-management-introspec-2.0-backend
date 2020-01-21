package com.activedge.usermgt.model.ox.migrations;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBRef;
import ox.engine.exception.OxException;
import ox.engine.internal.OxAction;
import ox.engine.internal.OxEnvironment;
import ox.engine.structure.Migration;
import ox.engine.structure.OrderingType;

public class V0002__group_indexes implements Migration {

    @Override
    public void up(OxEnvironment oxEnvironment) throws OxException {
        DB db = oxEnvironment.getMongoDatabase();
        oxEnvironment.execute(OxAction
                .createIndex("group_id_idx")
                .setCollection("groups")
                .addAttribute("_id",OrderingType.ASC)
                .ifNotExists()
        );

        db.getCollection("groups").insert(BasicDBObjectBuilder
                .start()
                .add("_id", 0)
                .add("name", "INTROSPEC-DEFAULT")
                .add("description", "This group exposes priviledges for default users.")
                .add("approved_by", "default")
                .add("approved_date", "2019-01-09")
                .add("is_deleted", false)
                .add("module", new DBRef("introspec_modules", "ADMIN"))
                .get());

    }

    @Override
    public void down(OxEnvironment oxEnvironment) throws OxException {

    }
}
