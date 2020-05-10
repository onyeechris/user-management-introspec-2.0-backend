package com.activedge.usermgt.model.ox.migrations;

import com.mongodb.*;
import org.bson.types.ObjectId;
import ox.engine.exception.OxException;
import ox.engine.internal.OxEnvironment;
import ox.engine.structure.Migration;

import java.time.LocalDateTime;

public class V0011__staff_module_updates implements Migration {

    @Override
    public void up(OxEnvironment oxEnvironment) throws OxException {
        DB db = oxEnvironment.getMongoDatabase();

        DBObject obj = db.getCollection("staff").findOne(BasicDBObjectBuilder
            .start()
            .add("type", "ADMIN" )
            .get());

        db.getCollection("staff_modules").insert(BasicDBObjectBuilder
                .start()
                .add("_id", new ObjectId())
                .add("assignAt", LocalDateTime.now())
                .add("grade", 0)
                .add("module", new DBRef("introspec_modules", "USER"))
                .add("staff", new DBRef("staff", obj.get("_id")))
                .get());

    }

    @Override
    public void down(OxEnvironment oxEnvironment) throws OxException {

    }
}
