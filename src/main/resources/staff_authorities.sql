
insert into staff_authority(staff_id, authority_id, module_id)
values(1, 'ROLE_CHECKER', 'USER');

insert into staff_authority(staff_id, authority_id, module_id)
values(2, 'ROLE_MAKER', 'USER');

insert into staff_authority(staff_id, authority_id, module_id)
values(3, 'ROLE_USER', 'ATM');

insert into staff_authority(staff_id, authority_id, module_id)
values(4, 'ROLE_USER', 'SETTLEMENT');

insert into staff_authority(staff_id, authority_id, module_id)
values(5, 'ROLE_ADMIN', 'ADMIN');

insert into staff_authority(staff_id, authority_id, module_id)
values(5, 'ROLE_USER', 'ATM');

insert into staff_authority(staff_id, authority_id, module_id)
values(5, 'ROLE_USER', 'SETTLEMENT');

insert into staff_authority(staff_id, authority_id, module_id)
values(6, 'ROLE_DEV', 'ADMIN');

COMMIT;
