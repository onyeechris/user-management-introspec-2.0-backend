-- Settlement Endpoints Permissions
insert into permissions
    (id, action, description)
values(1, 'CREATE-ACCOUNT', 'creating account endpoint');
insert into permissions
    (id, action, description)
values(2, 'POST-ACCOUNT', 'endpoint for posting to core banking');

-- ATMRecon Endpoints Permissions
insert into permissions
    (id, action, description)
values(3, 'VIEW-JOURNAL', 'creating ATM branch endpoint');
insert into permissions
    (id, action, description)
values(4, 'VIEW-OFFICE', '');
insert into permissions
    (id, action, description)
values(5, 'CREATE-OFFICE', '');
insert into permissions
    (id, action, description)
values(6, 'DELETE-OFFICE', '');
insert into permissions
    (id, action, description)
values(7, 'VIEW-TERMINAL', '');
insert into permissions
    (id, action, description)
values(8, 'CREATE-TERMINAL', '');
insert into permissions
    (id, action, description)
values(9, 'DELETE-TERMINAL', '');
insert into permissions
    (id, action, description)
values(10, 'UPDATE-TERMINAL', '');
insert into permissions
    (id, action, description)
values(11, 'VIEW-TERMINAL-BRAND', '');
insert into permissions
    (id, action, description)
values(12, 'CREATE-TERMINAL-BRAND', '');
insert into permissions
    (id, action, description)
values(13, 'DELETE-TERMINAL-BRAND', '');
insert into permissions
    (id, action, description)
values(14, 'UPDATE-TERMINAL-BRAND', '');

-- SettlementApp Endpoints Permission
-- HEAD
insert into permissions(id, action, description) values(15, 'getAllProcessors', '');
insert into permissions(id, action, description) values(16, 'getAllProcessorsByDateRange', '');
insert into permissions(id, action, description) values(17, 'createProcessor', '');
insert into permissions(id, action, description) values(18, 'getProcessor', '');
insert into permissions(id, action, description) values(19, 'updateProcessor', '');
insert into permissions(id, action, description) values(20, 'approveProcessor', '');
insert into permissions(id, action, description) values(21, 'deleteProcessor', '');
insert into permissions(id, action, description) values(22, 'getProcessorSubgroups', '');
insert into permissions(id, action, description) values(23, 'createProcessorSubgroup', '');
insert into permissions(id, action, description) values(24, 'updateProcessorSubgroup', '');
insert into permissions(id, action, description) values(25, 'getProcessorAccounts', '');
insert into permissions(id, action, description) values(26, 'getProcessorExceptionDefinitions', '');
insert into permissions(id, action, description) values(27, 'createProcessorExceptionDefinition', '');
insert into permissions(id, action, description) values(28, 'getExceptionDefinition', '');
insert into permissions(id, action, description) values(29, 'updateExceptionDefinition', '');
insert into permissions(id, action, description) values(30, 'getExceptionDefinitions', '');
insert into permissions(id, action, description) values(31, 'getAllExceptionBatchesByParameters', '');
insert into permissions(id, action, description) values(32, 'getAllExceptionDetailsByParameters', '');
insert into permissions(id, action, description) values(33, 'postExceptionToCustomer', '');
insert into permissions(id, action, description) values(34, 'getProcessorSummaryDefinitions', '');
insert into permissions(id, action, description) values(35, 'createProcessorSummaryDefinition', '');
insert into permissions(id, action, description) values(36, 'getProcessorSummaryDefinition', '');
insert into permissions(id, action, description) values(37, 'editProcessorSummaryDefinition', '');
---insert into permissions(id, action, description) values(38, 'getProcessorSummaryDefinitions', '');
insert into permissions(id, action, description) values(39, 'getSummaryBatchesByParameters', '');
insert into permissions(id, action, description) values(40, 'getSummaryDetailsByParameters', '');
insert into permissions(id, action, description) values(41, 'getAllAccounts', '');
insert into permissions(id, action, description) values(42, 'getAllAccountsByDateRange', '');
insert into permissions(id, action, description) values(43, 'deleteAccount', '');
insert into permissions(id, action, description) values(44, 'getAccount', '');
insert into permissions(id, action, description) values(45, 'getAccountOnly', '');
insert into permissions(id, action, description) values(46, 'createProductAccounts', '');
insert into permissions(id, action, description) values(47, 'updateProductAccount', '');
insert into permissions(id, action, description) values(48, 'approveAccount', '');
insert into permissions(id, action, description) values(49, 'getAccountDefintions', '');
insert into permissions(id, action, description) values(50, 'createAccountDefinition', '');
insert into permissions(id, action, description) values(51, 'updateAccountDefintion', '');
insert into permissions(id, action, description) values(52, 'approveAccountDefintion', '');
insert into permissions(id, action, description) values(53, 'getAccountDefinition', '');
insert into permissions(id, action, description) values(54, 'deleteAccountDefinition', '');
insert into permissions(id, action, description) values(55, 'deleteCurrency', '');
insert into permissions(id, action, description) values(56, 'getCurrency', '');
insert into permissions(id, action, description) values(57, 'getAllCurrencies', '');
insert into permissions(id, action, description) values(58, 'createCurrency', '');
insert into permissions(id, action, description) values(59, 'updateCurrency', '');
insert into permissions(id, action, description) values(60, 'approveCurrency', '');
insert into permissions(id, action, description) values(61, 'getDefintion', '');
insert into permissions(id, action, description) values(62, 'getAllDefinitions', '');
insert into permissions(id, action, description) values(63, 'createDefinition', '');
insert into permissions(id, action, description) values(64, 'updateDefinition', '');
insert into permissions(id, action, description) values(65, 'approveDefintion', '');
insert into permissions(id, action, description) values(66, 'deleteDefintion', '');
insert into permissions(id, action, description) values(67, 'getAllProducts', '');
insert into permissions(id, action, description) values(68, 'getProduct', '');
insert into permissions(id, action, description) values(69, 'createProduct', '');
insert into permissions(id, action, description) values(70, 'updateProduct', '');
insert into permissions(id, action, description) values(71, 'approveProduct', '');
insert into permissions(id, action, description) values(72, 'getProductAccounts', '');
insert into permissions(id, action, description) values(73, 'createSubgroupProduct', '');
insert into permissions(id, action, description) values(74, 'updateSubgroupProduct', '');
insert into permissions(id, action, description) values(75, 'getAccountReportSummaryByParameters', '');
insert into permissions(id, action, description) values(76, 'getAccountChannelSummaryByParameters', '');
insert into permissions(id, action, description) values(77, 'getDailyAccountSummaryByParameters', '');
insert into permissions(id, action, description) values(78, 'getAccountReportSummaryPdf', '');
insert into permissions(id, action, description) values(79, 'getAccountRPReportSummaryPdf', '');
insert into permissions(id, action, description) values(80, 'getChannelReportSummaryPdf', '');
insert into permissions(id, action, description) values(81, 'getDailyReportSummaryPdf', '');
insert into permissions(id, action, description) values(82, 'getAccountReportSummaryCsv', '');
insert into permissions(id, action, description) values(83, 'getAccountRPReportSummaryCsv', '');
insert into permissions(id, action, description) values(84, 'getChannelSummaryReportCsv', '');
insert into permissions(id, action, description) values(85, 'getAccountDailyReportCsv', '');
insert into permissions(id, action, description) values(86, 'topCommissions', '');
insert into permissions(id, action, description) values(87, 'bottomCommissions', '');
insert into permissions(id, action, description) values(88, 'commissionHistory', '');
insert into permissions(id, action, description) values(89, 'getCommissionSummaryByAccount', '');
insert into permissions(id, action, description) values(90, 'getCommissionSummary', '');
insert into permissions(id, action, description) values(91, 'getAllSubgroups', '');
insert into permissions(id, action, description) values(92, 'getAllSubgroupsByDateRange', '');
insert into permissions(id, action, description) values(93, 'updateSubgroup', '');
insert into permissions(id, action, description) values(94, 'approveSubgroup', '');
insert into permissions(id, action, description) values(95, 'deleteSubgroup', '');
insert into permissions(id, action, description) values(96, 'getSubgroupOnly', '');
insert into permissions(id, action, description) values(97, 'getSubgroupById', '');
insert into permissions(id, action, description) values(98, 'getSubgroupProducts', '');
insert into permissions(id, action, description) values(99, 'getAllTodos', '');
insert into permissions(id, action, description) values(100, 'getContributors', '');
insert into permissions(id, action, description) values(101, 'getAccountSummaryByDateRange', '');
insert into permissions(id, action, description) values(102, 'getProcessorSummary', '');
insert into permissions(id, action, description) values(103, 'getProcessorSummaryByProcessor', '');
insert into permissions(id, action, description) values(104, 'getSubgroupSummary', '');
insert into permissions(id, action, description) values(105, 'getAccountsSummaryByProduct', '');
insert into permissions(id, action, description) values(106, 'getAccountsSummaryByProcessor', '');
insert into permissions(id, action, description) values(107, 'postAll', '');
insert into permissions(id, action, description) values(108, 'post', '');
insert into permissions(id, action, description) values(109, 'getAllAccountBatches', '');
insert into permissions(id, action, description) values(110, 'getAllBatchesByProductName', '');
insert into permissions(id, action, description) values(111, 'getAllAccountBatchesByAccount', '');
insert into permissions(id, action, description) values(112, 'getAccountBatch', '');
insert into permissions(id, action, description) values(113, 'createAccountBatch', '');
insert into permissions(id, action, description) values(114, 'updateAccountBatch', '');
insert into permissions(id, action, description) values(115, 'approveAccountBatch', '');
insert into permissions(id, action, description) values(116, 'getAllManualAccountBatches', '');
insert into permissions(id, action, description) values(117, 'approvePost', '');
insert into permissions(id, action, description) values(118, 'approveExceptionPost', '');

COMMIT;