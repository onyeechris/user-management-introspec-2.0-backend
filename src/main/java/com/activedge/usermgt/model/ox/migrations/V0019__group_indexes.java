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

public class V0019__group_indexes implements Migration {
    @Override
    public void up(OxEnvironment oxEnvironment) throws OxException {
        DB db = oxEnvironment.getMongoDatabase();

        db.getCollection("groups").insert(BasicDBObjectBuilder
                .start()
                .add("_id", new BasicDBObject("_id", UUID.randomUUID().toString().replaceAll("-", ""))
                        .append("module", new DBRef("introspec_modules", "USER")))
                .add("name", "2FA Admins")
                .add("description", "A group-wide admin that can enable 2FA settings for all users.")
                .add("staffs", new ArrayList<>())
                .add("permissions", Arrays.asList(
                        new DBRef("permissions", "can_enable_2fa")
                ))
                .add("isDeleted", false)
                .add("module", new DBRef("introspec_modules", "USER"))
                .get());
    }

    @Override
    public void down(OxEnvironment oxEnvironment) throws OxException {

    }
}
