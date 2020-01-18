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
                .createIndex("staff_id_idx")
                .setCollection("staff")
                .addAttribute("_id", OrderingType.ASC)
                .ifNotExists()
        );
        db.getCollection("staff").insert(BasicDBObjectBuilder
                .start()
                 .add("_id", 1L)
                .add("first_name", "ATM_User")
                .add("email", "atmrecon@aet.com")
                .add("type", "USER")
                .add("i_password", "$2a$10$fobhDhagq2vzx/RisgWtiuZ2ybihxIqF2jPl9/zm4aVfI4WEzhOtS")
                .add("approved_by", "default")
                .add("approved_date", "2019-01-09")
                .add("activated", true)
                .get());

        db.getCollection("staff").insert(BasicDBObjectBuilder
                .start()
                 .add("_id", 2L)
                .add("first_name", "Settlement_User")
                .add("email", "settlement@aet.com")
                .add("type", "USER")
                .add("i_password", "$2a$10$ydka6YmJeTVia4fdDHAkXeTEk.HW3220fFDHPZanhdfLyiE/aBIxa")
                .add("approved_by", "default")
                .add("approved_date", "2019-01-12")
                .add("activated", true)
                .get());

        db.getCollection("staff").insert(BasicDBObjectBuilder
                .start()
                 .add("_id", 3L)
                .add("first_name", "Admin_User")
                .add("email", "admin@aet.com")
                .add("type", "ADMIN")
                .add("i_password", "$2a$10$0v503h5I1LCoWFs8XAj3eebmDk6fOR86sMp8gEaVJy/SzvxEliTfC")
                .add("approved_by", "default")
                .add("approved_date", "2019-01-16")
                .add("activated", true)
                .get());

        db.getCollection("staff").insert(BasicDBObjectBuilder
                .start()
                 .add("_id", 4L)
                .add("first_name", "Sys_Dev")
                .add("email", "sysdev@aet.com")
                .add("type", "DEV")
                .add("i_password", "$2a$10$irbom5DMU9YwRdJG1pgpyONG..Vjg4Ru5mb6Ta9ODc.50ztsND.VG")
                .add("approved_by", "default")
                .add("approved_date", "2019-01-17")
                .add("activated", true)
                .get());

    }

    @Override
    public void down(OxEnvironment oxEnvironment) throws OxException {

    }
}
