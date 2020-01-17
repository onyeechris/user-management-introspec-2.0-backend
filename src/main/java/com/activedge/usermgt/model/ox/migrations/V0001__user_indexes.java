package com.activedge.usermgt.model.ox.migrations;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import org.bson.types.ObjectId;
import ox.engine.exception.OxException;
import ox.engine.internal.OxAction;
import ox.engine.internal.OxEnvironment;
import ox.engine.structure.Migration;
import ox.engine.structure.OrderingType;

public class V0001__user_indexes implements Migration {
    @Override
    public void up(OxEnvironment oxEnvironment) throws OxException {
         DB db = oxEnvironment.getMongoDatabase();
        // do what you want with db.
        oxEnvironment.execute(OxAction
                .createIndex("auth_id_idx")
                .setCollection("staff")
                .addAttribute("authId", OrderingType.ASC)
                .ifNotExists()
        );
        db.getCollection("staff").insert(BasicDBObjectBuilder
                .start()
                // .add("_id", new ObjectId("590f86d92449343841cc2c3f"))
                .add("firstname", "Elvo")
                .add("lastname", "One")
                .add("email", "elvis@example.com")
                .add("authId", 11)
                .get());

        db.getCollection("staff").insert(BasicDBObjectBuilder
                .start()
                // .add("_id", new ObjectId("590f86d92449343841cc2c3f"))
                .add("firstname", "Elvo11")
                .add("lastname", "One1")
                .add("email", "elvis1@example.com")
                .add("authId", 12)
                .get());
        /*
        oxEnvironment.execute(OxAction
                .createIndex("user_id_accommodation_id_idx")
                .setCollection("booking")
                .addAttribute("userId", OrderingType.ASC)
                .addAttribute("accommodationId", OrderingType.ASC)
                .ifNotExists()
        );
        oxEnvironment.execute(OxAction
                .createIndex("user_id_idx")
                .setCollection("booking")
                .addAttribute("userId", OrderingType.ASC));
        oxEnvironment.execute(OxAction
                .createIndex("accommodation_id_idx")
                .setCollection("booking")
                .addAttribute("accommodationId", OrderingType.ASC));
        */
    }

    @Override
    public void down(OxEnvironment oxEnvironment) throws OxException {

    }
}
