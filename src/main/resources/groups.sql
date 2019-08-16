insert into groups(id, name, description, approved_by, approved_date, module)
values(0, 'INTROSPEC-DEFAULT', 'This group exposes priviledges for default users.', 'default', '2019-01-09', 'ADMIN');
--
-- insert into groups(id, name, description)
-- values(2, 'INTROSPEC-SYSADMIN', 'This group exposes ALL priviledges except adding endpoints to Super Admins.');
--
-- insert into groups(id, name, description)
-- values(3, 'INTROSPEC-SYSUSER', 'This group exposes ALL priviledges for new users created from LDAP.');

COMMIT