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

public class V0007__group_indexes implements Migration {

    @Override
    public void up(OxEnvironment oxEnvironment) throws OxException {
        DB db = oxEnvironment.getMongoDatabase();


        db.getCollection("groups").insert(BasicDBObjectBuilder
                .start()
                .add("_id", new BasicDBObject("_id", UUID.randomUUID().toString().replaceAll("-", ""))
                        .append("module", new DBRef("introspec_modules", "SETTLEMENT")))
                .add("name", "group_users")
                .add("description", "A group-wide user.")
                .add("staffs", new ArrayList<>())
                .add("permissions", Arrays.asList(
                        new DBRef("permissions", "can_view_schemes"),
                        new DBRef("permissions", "can_view_mastercard"),
                        new DBRef("permissions", "can_view_visa"),
                        new DBRef("permissions", "can_view_interaffiliates"),
                        new DBRef("permissions", "can_view_group_details"),
                        new DBRef("permissions", "can_view_reports"),
                        new DBRef("permissions", "can_view_mastercard_reports"),
                        new DBRef("permissions", "can_view_visa_reports"),
                        new DBRef("permissions", "can_view_interaffiliate_reports"),
                        new DBRef("permissions", "can_view_logs"),
                        new DBRef("permissions", "can_view_audit_logs"),
                        new DBRef("permissions", "can_view_processing_logs"),
                        new DBRef("permissions", "can_view_processed_files"),
                        new DBRef("permissions", "can_view_downstream_logs"),
                        new DBRef("permissions", "can_view_advanced_options"),
                        new DBRef("permissions", "can_view_configurations"),
                        new DBRef("permissions", "can_view_forex"),
                        new DBRef("permissions", "can_view_user_assignments"),
                        new DBRef("permissions", "can_view_file_templates"),
                        new DBRef("permissions", "can_view_products"),
                        new DBRef("permissions", "can_view_sres"),
                        new DBRef("permissions", "can_view_regions"),
                        new DBRef("permissions", "can_view_group_affiliates"),
                        new DBRef("permissions", "can_view_bins"))
                )
                .add("isDeleted", false)
                .add("module", new DBRef("introspec_modules", "SETTLEMENT"))
                .get());

        db.getCollection("groups").insert(BasicDBObjectBuilder
                .start()
                .add("_id", new BasicDBObject("_id", UUID.randomUUID().toString().replaceAll("-", ""))
                        .append("module", new DBRef("introspec_modules", "SETTLEMENT")))
                .add("name", "group_admins")
                .add("description", "A group-wide admin that can perform all the activities of a group user and can manage affiliate settings.")
                .add("staffs", new ArrayList<>())
                .add("permissions", Arrays.asList(
                        new DBRef("permissions", "can_view_schemes"),
                        new DBRef("permissions", "can_view_mastercard"),
                        new DBRef("permissions", "can_view_visa"),
                        new DBRef("permissions", "can_view_interaffiliates"),
                        new DBRef("permissions", "can_view_group_details"),
                        new DBRef("permissions", "can_approve_group_entries"),
                        new DBRef("permissions", "can_approve_affiliate_entries"),
                        new DBRef("permissions", "can_view_reports"),
                        new DBRef("permissions", "can_view_mastercard_reports"),
                        new DBRef("permissions", "can_view_visa_reports"),
                        new DBRef("permissions", "can_view_interaffiliate_reports"),
                        new DBRef("permissions", "can_view_logs"),
                        new DBRef("permissions", "can_view_audit_logs"),
                        new DBRef("permissions", "can_view_processing_logs"),
                        new DBRef("permissions", "can_view_processed_files"),
                        new DBRef("permissions", "can_view_downstream_logs"),
                        new DBRef("permissions", "can_view_advanced_options"),
                        new DBRef("permissions", "can_upload_files"),
                        new DBRef("permissions", "can_manage_users"),
                        new DBRef("permissions", "can_view_configurations"),
                        new DBRef("permissions", "can_manage_file_templates"),
                        new DBRef("permissions", "can_manage_products"),
                        new DBRef("permissions", "can_manage_sres"),
                        new DBRef("permissions", "can_view_forex"),
                        new DBRef("permissions", "can_manage_forex"),
                        new DBRef("permissions", "can_manage_regions"),
                        new DBRef("permissions", "can_manage_group_affiliates"),
                        new DBRef("permissions", "can_manage_own_affiliate"),
                        new DBRef("permissions", "can_manage_bins"),
                        new DBRef("permissions", "can_view_user_assignments"),
                        new DBRef("permissions", "can_view_file_templates"),
                        new DBRef("permissions", "can_view_products"),
                        new DBRef("permissions", "can_view_sres"),
                        new DBRef("permissions", "can_view_regions"),
                        new DBRef("permissions", "can_view_group_affiliates"),
                        new DBRef("permissions", "can_view_bins"))
                )
                .add("isDeleted", false)
                .add("module", new DBRef("introspec_modules", "SETTLEMENT"))
                .get());

        db.getCollection("groups").insert(BasicDBObjectBuilder
                .start()
                .add("_id", new BasicDBObject("_id", UUID.randomUUID().toString().replaceAll("-", ""))
                        .append("module", new DBRef("introspec_modules", "SETTLEMENT")))
                .add("name", "auditor")
                .add("description", "Can view reports across all affiliates.")
                .add("staffs", new ArrayList<>())
                .add("permissions", Arrays.asList(
                        new DBRef("permissions", "can_view_schemes"),
                        new DBRef("permissions", "can_view_mastercard"),
                        new DBRef("permissions", "can_view_visa"),
                        new DBRef("permissions", "can_view_interaffiliates"),
                        new DBRef("permissions", "can_view_group_details"),
                        new DBRef("permissions", "can_view_reports"),
                        new DBRef("permissions", "can_view_mastercard_reports"),
                        new DBRef("permissions", "can_view_visa_reports"),
                        new DBRef("permissions", "can_view_interaffiliate_reports"),
                        new DBRef("permissions", "can_view_logs"),
                        new DBRef("permissions", "can_view_audit_logs"),
                        new DBRef("permissions", "can_view_processing_logs"),
                        new DBRef("permissions", "can_view_processed_files"),
                        new DBRef("permissions", "can_view_downstream_logs"),
                        new DBRef("permissions", "can_view_advanced_options"),
                        new DBRef("permissions", "can_view_configurations"),
                        new DBRef("permissions", "can_view_forex"),
                        new DBRef("permissions", "can_view_user_assignments"),
                        new DBRef("permissions", "can_view_file_templates"),
                        new DBRef("permissions", "can_view_products"),
                        new DBRef("permissions", "can_view_sres"),
                        new DBRef("permissions", "can_view_regions"),
                        new DBRef("permissions", "can_view_group_affiliates"),
                        new DBRef("permissions", "can_view_bins"))
                )
                .add("isDeleted", false)
                .add("module", new DBRef("introspec_modules", "SETTLEMENT"))
                .get());

        /*
        db.getCollection("groups").insert(BasicDBObjectBuilder
                .start()
                .add("_id", new BasicDBObject("_id", UUID.randomUUID().toString().replaceAll("-", ""))
                        .append("module", new DBRef("introspec_modules", "SETTLEMENT")))
                .add("name", "super_users")
                .add("description", "This group will have all permissions by default.")
                .add("staffs", new ArrayList<>())
                .add("permissions", Arrays.asList(
                        new DBRef("permissions", "can_view_schemes"),
                        new DBRef("permissions", "can_view_mastercard"),
                        new DBRef("permissions", "can_view_visa"),
                        new DBRef("permissions", "can_view_interaffiliates"),
                        new DBRef("permissions", "can_view_group_details"),
                        new DBRef("permissions", "can_approve_group_entries"),
                        new DBRef("permissions", "can_approve_affiliate_entries"),
                        new DBRef("permissions", "can_view_reports"),
                        new DBRef("permissions", "can_view_mastercard_reports"),
                        new DBRef("permissions", "can_view_visa_reports"),
                        new DBRef("permissions", "can_view_interaffiliate_reports"),
                        new DBRef("permissions", "can_view_logs"),
                        new DBRef("permissions", "can_view_audit_logs"),
                        new DBRef("permissions", "can_view_processing_logs"),
                        new DBRef("permissions", "can_view_processed_files"),
                        new DBRef("permissions", "can_view_downstream_logs"),
                        new DBRef("permissions", "can_view_advanced_options"),
                        new DBRef("permissions", "can_upload_files"),
                        new DBRef("permissions", "can_view_user_assignments"),
                        new DBRef("permissions", "can_manage_users"),
                        new DBRef("permissions", "can_view_configurations"),
                        new DBRef("permissions", "can_view_file_templates"),
                        new DBRef("permissions", "can_manage_file_templates"),
                        new DBRef("permissions", "can_view_products"),
                        new DBRef("permissions", "can_manage_products"),
                        new DBRef("permissions", "can_view_sres"),
                        new DBRef("permissions", "can_manage_sres"),
                        new DBRef("permissions", "can_view_forex"),
                        new DBRef("permissions", "can_manage_forex"),
                        new DBRef("permissions", "can_view_regions"),
                        new DBRef("permissions", "can_manage_regions"),
                        new DBRef("permissions", "can_view_group_affiliates"),
                        new DBRef("permissions", "can_manage_group_affiliates"),
                        new DBRef("permissions", "can_view_own_affiliate"),
                        new DBRef("permissions", "can_manage_own_affiliate"),
                        new DBRef("permissions", "can_view_bins"),
                        new DBRef("permissions", "can_manage_bins")))
                .add("isDeleted", false)
                .add("module", new DBRef("introspec_modules", "SETTLEMENT"))
                .get());

        db.getCollection("groups").insert(BasicDBObjectBuilder
                .start()
                .add("_id", new BasicDBObject("_id", UUID.randomUUID().toString().replaceAll("-", ""))
                        .append("module", new DBRef("introspec_modules", "SETTLEMENT")))
                .add("name", "affiliate_users")
                .add("description", "A regular affiliate user.")
                .add("staffs", new ArrayList<>())
                .add("permissions", Arrays.asList(
                        new DBRef("permissions", "can_view_schemes"),
                        new DBRef("permissions", "can_view_mastercard"),
                        new DBRef("permissions", "can_view_visa"),
                        new DBRef("permissions", "can_view_interaffiliates"),
                        new DBRef("permissions", "can_view_reports"),
                        new DBRef("permissions", "can_view_mastercard_reports"),
                        new DBRef("permissions", "can_view_visa_reports"),
                        new DBRef("permissions", "can_view_interaffiliate_reports"),
                        new DBRef("permissions", "can_view_logs"),
                        new DBRef("permissions", "can_view_processed_files"),
                        new DBRef("permissions", "can_view_forex"),
                        new DBRef("permissions", "can_view_products"),
                        new DBRef("permissions", "can_view_regions"),
                        new DBRef("permissions", "can_view_own_affiliate"))
                )
                .add("isDeleted", false)
                .add("module", new DBRef("introspec_modules", "SETTLEMENT"))
                .get());

        db.getCollection("groups").insert(BasicDBObjectBuilder
                .start()
                .add("_id", new BasicDBObject("_id", UUID.randomUUID().toString().replaceAll("-", ""))
                        .append("module", new DBRef("introspec_modules", "SETTLEMENT")))
                .add("name", "affiliate_admins")
                .add("description", "This group can perform the activities of a regular affiliate user and manage the active affiliate they have been assigned.")
                .add("staffs", new ArrayList<>())
                .add("permissions", Arrays.asList(
                        new DBRef("permissions", "can_approve_affiliate_entries"),
                        new DBRef("permissions", "can_manage_own_affiliate"))
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
