package com.activedge.usermgt.model.ox.migrations;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBRef;
import ox.engine.exception.OxException;
import ox.engine.internal.OxEnvironment;
import ox.engine.structure.Migration;

public class V0010__permission_updates implements Migration {

    @Override
    public void up(OxEnvironment oxEnvironment) throws OxException {
        DB db = oxEnvironment.getMongoDatabase();

        db.getCollection("permissions").insert(BasicDBObjectBuilder
                .start()
                .add("_id", "can_list_directory")
                .add("action", "can list directory")
                .add("description", "Can list directory")
                .add("module", new DBRef("introspec_modules", "SETTLEMENT"))
                .get());

        db.getCollection("permissions").insert(BasicDBObjectBuilder
                .start()
                .add("_id", "can_manage_directory")
                .add("action", "can manage directory")
                .add("description", "Can manage directory")
                .add("module", new DBRef("introspec_modules", "SETTLEMENT"))
                .get());

        /*
        db.getCollection("groups").insert(BasicDBObjectBuilder
                .start()
                .add("_id", new BasicDBObject("_id", UUID.randomUUID().toString().replaceAll("-", ""))
                        .append("module", new DBRef("introspec_modules", "SETTLEMENT")))
                .add("name", "directory_admins")
                .add("description", "This group can handle directories.")
                .add("staffs", new ArrayList<>())
                .add("permissions", Arrays.asList(
                        new DBRef("permissions", "can_list_directory"),
                        new DBRef("permissions", "can_manage_directory"))
                )
                .add("isDeleted", false)
                .add("module", new DBRef("introspec_modules", "SETTLEMENT"))
                .get());
        */

    }

    @Override
    public void down(OxEnvironment oxEnvironment) throws OxException {

    }
}
