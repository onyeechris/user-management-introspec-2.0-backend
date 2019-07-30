-- insert into introspec_authority(name)
-- values('ROLE_INTROSPEC-SYSADMIN');
--
-- insert into introspec_authority(name)
-- values('ROLE_INTROSPEC-SYSUSER');

insert into introspec_authority(code, name, module)
values('ROLE_INTROSPEC-SYSDEV', 'ROLE_INTROSPEC-SYSDEV', 'USER');

insert into introspec_authority(code, name, module)
values('ROLE_USER', 'ROLE_USER', 'ATM');

insert into introspec_authority(code, name, module)
values('ROLE_MAKER', 'ROLE_MAKER', 'ATM');

insert into introspec_authority(code, name, module)
values('ROLE_CHECKER', 'ROLE_CHECKER', 'ATM');

insert into introspec_authority(code, name, module)
values('ROLE_ADMIN', 'ROLE_ADMIN', 'SETTLEMENT');

insert into introspec_authority(code, name, module)
values('ROLE_USER', 'ROLE_USER', 'SETTLEMENT');

COMMIT;