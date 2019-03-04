-- Settlement Endpoints Permissions
insert into permissions(id, action, description) values(1, 'CREATE-ACCOUNT', 'creating account endpoint');
insert into permissions(id, action, description) values(2, 'POST-ACCOUNT', 'endpoint for posting to core banking');

-- ATMRecon Endpoints Permissions
insert into permissions(id, action, description) values(3, 'VIEW-JOURNAL', 'creating ATM branch endpoint');
insert into permissions(id, action, description) values(4, 'VIEW-OFFICE', '');
insert into permissions(id, action, description) values(5, 'CREATE-OFFICE', '');
insert into permissions(id, action, description) values(6, 'DELETE-OFFICE', '');
insert into permissions(id, action, description) values(7, 'VIEW-TERMINAL', '');
insert into permissions(id, action, description) values(8, 'CREATE-TERMINAL', '');
insert into permissions(id, action, description) values(9, 'DELETE-TERMINAL', '');
insert into permissions(id, action, description) values(10, 'UPDATE-TERMINAL', '');
insert into permissions(id, action, description) values(11, 'VIEW-TERMINAL-BRAND', '');
insert into permissions(id, action, description) values(12, 'CREATE-TERMINAL-BRAND', '');
insert into permissions(id, action, description) values(13, 'DELETE-TERMINAL-BRAND', '');
insert into permissions(id, action, description) values(14, 'UPDATE-TERMINAL-BRAND', '');

-- SettlementApp Endpoints Permission
insert into permissions(id, action, description) values(15, 'getAllProcessors', '...');
insert into permissions(id, action, description) values(16, 'getAllProcessorsByDateRange', '…');
insert into permissions(id, action, description) values(17, 'createProcessor', '…');
insert into permissions(id, action, description) values(18, 'getProcessor', '…');
insert into permissions(id, action, description) values(19, 'updateProcessor', '…');
insert into permissions(id, action, description) values(20, 'approveProcessor', '…');
insert into permissions(id, action, description) values(21, 'deleteProcessor', '…');
insert into permissions(id, action, description) values(22, 'getProcessorSubgroups', '…');
insert into permissions(id, action, description) values(23, 'createProcessorSubgroup', '…');
insert into permissions(id, action, description) values(24, 'updateProcessorSubgroup', '…');
insert into permissions(id, action, description) values(25, 'getProcessorAccounts', '…');
insert into permissions(id, action, description) values(26, 'getProcessorExceptionDefinitions', '…');
insert into permissions(id, action, description) values(27, 'createProcessorExceptionDefinition', '…');
insert into permissions(id, action, description) values(28, 'getExceptionDefinition', '…');
insert into permissions(id, action, description) values(29, 'updateExceptionDefinition', '…');
insert into permissions(id, action, description) values(30, 'getExceptionDefinitions', '…');
insert into permissions(id, action, description) values(31, 'getAllExceptionBatchesByParameters', '…');
insert into permissions(id, action, description) values(32, 'postExceptionToCustomer', '…');
insert into permissions(id, action, description) values(33, 'getProcessorSummaryDefinitions', '…');
insert into permissions(id, action, description) values(34, 'createProcessorSummaryDefinition', '…');
insert into permissions(id, action, description) values(35, 'getProcessorSummaryDefinition', '…');
insert into permissions(id, action, description) values(36, 'editProcessorSummaryDefinition', '…');
insert into permissions(id, action, description) values(37, 'getSummaryBatchesByParameters', '…');
insert into permissions(id, action, description) values(38, 'getSummaryDetailsByParameters', '…');

COMMIT;