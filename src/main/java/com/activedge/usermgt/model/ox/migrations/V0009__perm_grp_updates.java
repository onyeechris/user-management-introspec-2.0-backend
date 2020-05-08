package com.activedge.usermgt.model.ox.migrations;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBRef;
import ox.engine.exception.OxException;
import ox.engine.internal.OxEnvironment;
import ox.engine.structure.Migration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class V0009__perm_grp_updates implements Migration {

    @Override
    public void up(OxEnvironment oxEnvironment) throws OxException {
        DB db = oxEnvironment.getMongoDatabase();

        db.getCollection("permissions").insert(BasicDBObjectBuilder
                .start()
                .add("_id", "can_delete_collections")
                .add("action", "can delete collections")
                .add("description", "Can delete collections")
                .add("module", new DBRef("introspec_modules", "SETTLEMENT"))
                .get());

        db.getCollection("groups").insert(BasicDBObjectBuilder
                .start()
                .add("_id", new BasicDBObject("_id", UUID.randomUUID().toString().replaceAll("-", ""))
                        .append("module", new DBRef("introspec_modules", "SETTLEMENT")))
                .add("name", "collection_admins")
                .add("description", "This group can delete collections.")
                .add("staffs", new ArrayList<>())
                .add("permissions", Arrays.asList(
                        new DBRef("permissions", "can_delete_collections"))
                )
                .add("isDeleted", false)
                .add("module", new DBRef("introspec_modules", "SETTLEMENT"))
                .get());

    }

    @Override
    public void down(OxEnvironment oxEnvironment) throws OxException {

    }
}
