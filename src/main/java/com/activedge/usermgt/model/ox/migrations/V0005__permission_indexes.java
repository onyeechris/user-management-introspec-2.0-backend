package com.activedge.usermgt.model.ox.migrations;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBRef;
import org.bson.types.ObjectId;
import ox.engine.exception.OxException;
import ox.engine.internal.OxAction;
import ox.engine.internal.OxEnvironment;
import ox.engine.structure.Migration;
import ox.engine.structure.OrderingType;

public class V0005__permission_indexes implements Migration {

    @Override
    public void up(OxEnvironment oxEnvironment) throws OxException {
        DB db = oxEnvironment.getMongoDatabase();
        oxEnvironment.execute(OxAction
                .createIndex("permission_id_idx")
                .addAttribute("module", OrderingType.ASC)
                .addAttribute("action", OrderingType.ASC)
                .unique()
                .setCollection("permissions")
                //.addAttribute("_id",OrderingType.ASC)
                .ifNotExists()
        );

        db.getCollection("permissions").insert(BasicDBObjectBuilder
                .start()
                .add("_id", new ObjectId())
                .add("action", "VIEW-JOURNAL")
                .add("description", "creating ATM branch endpoint")
                .add("module", new DBRef("introspec_modules", "ATM"))
                .get());

        db.getCollection("permissions").insert(BasicDBObjectBuilder
                .start()
                .add("_id", new ObjectId())
                .add("action", "EDIT-JOURNAL")
                .add("description", "editing ATM branch endpoint")
                .add("module", new DBRef("introspec_modules", "ATM"))
                .get());

        db.getCollection("permissions").insert(BasicDBObjectBuilder
                .start()
                .add("_id", new ObjectId())
                .add("action", "VIEW-FILES")
                .add("description", "creating SETTLEMENT branch endpoint")
                .add("module", new DBRef("introspec_modules", "SETTLEMENT"))
                .get());

        db.getCollection("permissions").insert(BasicDBObjectBuilder
                .start()
                .add("_id", new ObjectId())
                .add("action", "EDIT-FILES")
                .add("description", "editing SETTLEMENT branch endpoint")
                .add("module", new DBRef("introspec_modules", "SETTLEMENT"))
                .get());

        db.getCollection("permissions").insert(BasicDBObjectBuilder
                .start()
                .add("_id", new ObjectId())
                .add("action", "VIEW-AUDIT")
                .add("description", "creating AUDIT branch endpoint")
                .add("module", new DBRef("introspec_modules", "ADMIN"))
                .get());

        db.getCollection("permissions").insert(BasicDBObjectBuilder
                .start()
                .add("_id", "AUTO_POST")
                .add("action", "AUTO-POST")
                .add("description", "Autopost endpoints")
                .add("module", new DBRef("introspec_modules", "SETTLEMENT"))
                .get());

    }

    @Override
    public void down(OxEnvironment oxEnvironment) throws OxException {

    }
}
