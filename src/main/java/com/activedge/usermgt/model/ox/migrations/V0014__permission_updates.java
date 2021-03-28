package com.activedge.usermgt.model.ox.migrations;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBRef;
import ox.engine.exception.OxException;
import ox.engine.internal.OxEnvironment;
import ox.engine.structure.Migration;

public class V0014__permission_updates implements Migration {

    @Override
    public void up(OxEnvironment oxEnvironment) throws OxException {
        DB db = oxEnvironment.getMongoDatabase();

        db.getCollection("permissions").insert(BasicDBObjectBuilder
                .start()
                .add("_id", "can_view_ecommerce_reports")
                .add("action", "can view ecommerce reports")
                .add("description", "Can view ecommerce reports")
                .add("module", new DBRef("introspec_modules", "SETTLEMENT"))
                .get());

        db.getCollection("permissions").insert(BasicDBObjectBuilder
                .start()
                .add("_id", "can_view_mvisa_reports")
                .add("action", "can view mvisa reports")
                .add("description", "Can view mvisa reports")
                .add("module", new DBRef("introspec_modules", "SETTLEMENT"))
                .get());

        db.getCollection("permissions").insert(BasicDBObjectBuilder
                .start()
                .add("_id", "can_view_mvisa_issuer")
                .add("action", "can view mvisa issuer")
                .add("description", "Can view mvisa issuer")
                .add("module", new DBRef("introspec_modules", "SETTLEMENT"))
                .get());

        db.getCollection("permissions").insert(BasicDBObjectBuilder
                .start()
                .add("_id", "can_view_mvisa_acquirer")
                .add("action", "can view mvisa acquirer")
                .add("description", "Can view mvisa acquirer")
                .add("module", new DBRef("introspec_modules", "SETTLEMENT"))
                .get());

        db.getCollection("permissions").insert(BasicDBObjectBuilder
                .start()
                .add("_id", "can_view_mvisa_summary_detail")
                .add("action", "can view mvisa summary detail")
                .add("description", "Can view mvisa summary detail")
                .add("module", new DBRef("introspec_modules", "SETTLEMENT"))
                .get());

        db.getCollection("permissions").insert(BasicDBObjectBuilder
                .start()
                .add("_id", "can_view_xpresscash_issuer")
                .add("action", "can view xpresscash issuer")
                .add("description", "Can view xpresscash issuer")
                .add("module", new DBRef("introspec_modules", "SETTLEMENT"))
                .get());

        db.getCollection("permissions").insert(BasicDBObjectBuilder
                .start()
                .add("_id", "can_view_xpresscash_acquirer")
                .add("action", "can view xpresscash acquirer")
                .add("description", "Can view xpresscash acquirer")
                .add("module", new DBRef("introspec_modules", "SETTLEMENT"))
                .get());

        db.getCollection("permissions").insert(BasicDBObjectBuilder
                .start()
                .add("_id", "can_view_xpresscash_summary_detail")
                .add("action", "can view xpresscash summary detail")
                .add("description", "Can view xpresscash summary detail")
                .add("module", new DBRef("introspec_modules", "SETTLEMENT"))
                .get());

    }

    @Override
    public void down(OxEnvironment oxEnvironment) throws OxException {

    }
}
