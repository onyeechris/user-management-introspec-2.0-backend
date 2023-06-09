package com.activedge.usermgt.model.ox.migrations;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import org.bson.types.ObjectId;
import ox.engine.exception.OxException;
import ox.engine.internal.OxAction;
import ox.engine.internal.OxEnvironment;
import ox.engine.structure.Migration;
import ox.engine.structure.OrderingType;

public class V0003__module_indexes implements Migration {
    @Override
    public void up(OxEnvironment oxEnvironment) throws OxException {
         DB db = oxEnvironment.getMongoDatabase();
        // do what you want with db.
        oxEnvironment.execute(OxAction
                .createIndex("module_id_idx")
                .setCollection("introspec_modules")
                .addAttribute("_id", OrderingType.ASC)
                .ifNotExists()
        );

        db.getCollection("introspec_modules").insert(BasicDBObjectBuilder
                .start()
                .add("_id", "ATM")
                .add("name", "ATMRecon")
                .add("description", "AtmRecon Description")
                .add("key", new ObjectId("590f86d92449343841cc2c3f"))
                .get());

        db.getCollection("introspec_modules").insert(BasicDBObjectBuilder
                .start()
                .add("_id", "MATCH")
                .add("name", "Matching Module")
                .add("description", "Matching desc")
                .add("key", new ObjectId())
                .get());

        db.getCollection("introspec_modules").insert(BasicDBObjectBuilder
                .start()
                .add("_id", "EXCEPTION")
                .add("name", "Exception and Escalation")
                .add("description", "Exception desc")
                .add("key", new ObjectId())
                .get());

        db.getCollection("introspec_modules").insert(BasicDBObjectBuilder
                .start()
                .add("_id", "SETTLEMENT")
                .add("name", "Settlement application")
                .add("description", "Settlement desc")
                .add("key", new ObjectId())
                .get());

        db.getCollection("introspec_modules").insert(BasicDBObjectBuilder
                .start()
                .add("_id", "USER")
                .add("name", "User management")
                .add("description", "User description")
                .add("key", new ObjectId())
                .get());

        db.getCollection("introspec_modules").insert(BasicDBObjectBuilder
                .start()
                .add("_id", "ADMIN")
                .add("name", "Application administrator")
                .add("description", "Admin description")
                .add("key", new ObjectId())
                .get());

    }

    @Override
    public void down(OxEnvironment oxEnvironment) throws OxException {

    }
}
