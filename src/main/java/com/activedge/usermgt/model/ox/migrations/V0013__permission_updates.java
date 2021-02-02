package com.activedge.usermgt.model.ox.migrations;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBRef;
import ox.engine.exception.OxException;
import ox.engine.internal.OxEnvironment;
import ox.engine.structure.Migration;

public class V0013__permission_updates implements Migration {

    @Override
    public void up(OxEnvironment oxEnvironment) throws OxException {
        DB db = oxEnvironment.getMongoDatabase();

        db.getCollection("permissions").insert(BasicDBObjectBuilder
                .start()
                .add("_id", "can_view_ecommerce")
                .add("action", "can view ecommerce")
                .add("description", "Can view ecommerce")
                .add("module", new DBRef("introspec_modules", "SETTLEMENT"))
                .get());

        db.getCollection("permissions").insert(BasicDBObjectBuilder
                .start()
                .add("_id", "can_manage_ecommerce")
                .add("action", "can manage ecommerce")
                .add("description", "Can manage ecommerce")
                .add("module", new DBRef("introspec_modules", "SETTLEMENT"))
                .get());

        db.getCollection("permissions").insert(BasicDBObjectBuilder
                .start()
                .add("_id", "can_view_masterpass")
                .add("action", "can view masterpass")
                .add("description", "Can view masterpass")
                .add("module", new DBRef("introspec_modules", "SETTLEMENT"))
                .get());

        db.getCollection("permissions").insert(BasicDBObjectBuilder
                .start()
                .add("_id", "can_manage_masterpass")
                .add("action", "can manage masterpass")
                .add("description", "Can manage masterpass")
                .add("module", new DBRef("introspec_modules", "SETTLEMENT"))
                .get());

        db.getCollection("permissions").insert(BasicDBObjectBuilder
                .start()
                .add("_id", "can_view_mvisa")
                .add("action", "can view mvisa")
                .add("description", "Can view mvisa")
                .add("module", new DBRef("introspec_modules", "SETTLEMENT"))
                .get());

        db.getCollection("permissions").insert(BasicDBObjectBuilder
                .start()
                .add("_id", "can_manage_mvisa")
                .add("action", "can manage mvisa")
                .add("description", "Can manage mvisa")
                .add("module", new DBRef("introspec_modules", "SETTLEMENT"))
                .get());

        db.getCollection("permissions").insert(BasicDBObjectBuilder
                .start()
                .add("_id", "can_view_xpresscash")
                .add("action", "can view xpresscash")
                .add("description", "Can view xpresscash")
                .add("module", new DBRef("introspec_modules", "SETTLEMENT"))
                .get());

        db.getCollection("permissions").insert(BasicDBObjectBuilder
                .start()
                .add("_id", "can_view_xpresscash_reports")
                .add("action", "can view xpresscash reports")
                .add("description", "Can view xpresscash reports")
                .add("module", new DBRef("introspec_modules", "SETTLEMENT"))
                .get());

        db.getCollection("permissions").insert(BasicDBObjectBuilder
                .start()
                .add("_id", "can_manage_xpresscash")
                .add("action", "can manage xpresscash")
                .add("description", "Can manage xpresscash")
                .add("module", new DBRef("introspec_modules", "SETTLEMENT"))
                .get());

        db.getCollection("permissions").insert(BasicDBObjectBuilder
                .start()
                .add("_id", "can_view_chinapay_summary")
                .add("action", "can view chinapay summary")
                .add("description", "Can view chinapay summary")
                .add("module", new DBRef("introspec_modules", "SETTLEMENT"))
                .get());

        db.getCollection("permissions").insert(BasicDBObjectBuilder
                .start()
                .add("_id", "can_view_chinapay_details")
                .add("action", "can view chinapay details")
                .add("description", "Can manage chinapay details")
                .add("module", new DBRef("introspec_modules", "SETTLEMENT"))
                .get());

    }

    @Override
    public void down(OxEnvironment oxEnvironment) throws OxException {

    }
}
