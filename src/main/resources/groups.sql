insert into groups(id, name, description)
values(1, 'INTROSPEC-SYSDEV', 'This group exposes priviledges for the system developers, majorly to add new endpoints.');

insert into groups(id, name, description)
values(2, 'INTROSPEC-SYSADMIN', 'This group exposes ALL priviledges except adding endpoints to Super Admins.');

insert into groups(id, name, description)
values(3, 'INTROSPEC-SYSUSER', 'This group exposes ALL priviledges for new users created from LDAP.');

COMMIT;