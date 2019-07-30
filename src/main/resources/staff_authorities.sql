-- insert into staff_authority(staff_id, authority_name)
-- values(1, 'ROLE_INTROSPEC-SYSDEV');
--
-- insert into staff_authority(staff_id, authority_name)
-- values(1, 'ROLE_INTROSPEC-SYSUSER');

insert into staff_authority(staff_id, authority_id, module_id)
values(1, 'ROLE_USER', 'ATM');

insert into staff_authority(staff_id, authority_id, module_id)
values(2, 'ROLE_ADMIN', 'SETTLEMENT');
insert into staff_authority(staff_id, authority_id, module_id)
values(2, 'ROLE_USER', 'SETTLEMENT');

insert into staff_authority(staff_id, authority_id, module_id)
values(3, 'ROLE_INTROSPEC-SYSDEV', 'USER');

COMMIT;
