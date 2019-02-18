-- Settlement Endpoints Permissions
insert into permissions(id, action, description) values(1, 'CREATE-ACCOUNT', 'creating account endpoint');
insert into permissions(id, action, description) values(2, 'POST-ACCOUNT', 'endpoint for posting to core banking');

-- ATMRecon Endpoints Permissions
insert into permissions(id, action, description) values(3, 'CREATE-BRANCH', 'creating ATM branch endpoint');
insert into permissions(id, action, description) values(4, 'RECONCILE-TRANSACTION', 'endpoint for reconciling journals to external source');

COMMIT;