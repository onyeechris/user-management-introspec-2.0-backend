insert into introspec_modules(id, name, description, key)
values('ATM', 'ATMrecon', 'Atm Description', UPPER(MD5('ATM')));

insert into introspec_modules(id, name, description, key)
values('MATCH', 'Matching Module', 'Matching desc', UPPER(MD5('MATCH')));

insert into introspec_modules(id, name, description, key)
values('EXCEPTION', 'Exception and Escalation', 'Exception desc', UPPER(MD5('EXCEPTION')));

insert into introspec_modules(id, name, description, key)
values('SETTLEMENT','Settlement application', 'Settlement desc', UPPER(MD5('SETTLEMENT')));

insert into introspec_modules(id, name, description, key)
values('USER','User management', 'User description', UPPER(MD5('USER')));

insert into introspec_modules(id, name, description, key)
values('ADMIN','Application administrator', 'Desc admin', UPPER(MD5('ADMIN')));

COMMIT;