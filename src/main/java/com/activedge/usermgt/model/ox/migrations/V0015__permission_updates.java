package com.activedge.usermgt.model.ox.migrations;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBRef;
import ox.engine.exception.OxException;
import ox.engine.internal.OxEnvironment;
import ox.engine.structure.Migration;

public class V0015__permission_updates implements Migration {

    @Override
    public void up(OxEnvironment oxEnvironment) throws OxException {
        DB db = oxEnvironment.getMongoDatabase();

        db.getCollection("permissions").insert(BasicDBObjectBuilder
                .start()
                .add("_id", "can_view_nostro_reports")
                .add("action", "can view nostro reports")
                .add("description", "Can view nostro reports")
                .add("module", new DBRef("introspec_modules", "SETTLEMENT"))
                .get());

        db.getCollection("permissions").insert(BasicDBObjectBuilder
                .start()
                .add("_id", "can_view_admin_reports")
                .add("action", "Can view admin reports")
                .add("description", "Can view admin reports")
                .add("module", new DBRef("introspec_modules", "SETTLEMENT"))
                .get());

        db.getCollection("permissions").insert(BasicDBObjectBuilder
                .start()
                .add("_id", "can_view_posting")
                .add("action", "Can view posting")
                .add("description", "Can view posting")
                .add("module", new DBRef("introspec_modules", "SETTLEMENT"))
                .get());

        db.getCollection("permissions").insert(BasicDBObjectBuilder
                .start()
                .add("_id", "can_view_billing_reports")
                .add("action", "Can view billing reports")
                .add("description", "Can view billing reports")
                .add("module", new DBRef("introspec_modules", "SETTLEMENT"))
                .get());
    }

    @Override
    public void down(OxEnvironment oxEnvironment) throws OxException {

    }
}
