insert into staff(id, first_name, email, maker_checker, group_id, i_password, created_by, approved_by, created_date, approved_date, activated)
values(1, 'Checker_Acc', 'checker@aet.com', 'CHECKER', 0, '$2a$10$OqVt6M2oTI7cW5FB5D3UBeXN8uO61D/Damd0eBwIrUm83sLAa6H7u', 'default', '2019-01-09', '2019-01-09', '2019-01-09', true);

insert into staff(id, first_name, email, maker_checker, group_id, i_password, created_by, approved_by, created_date, approved_date, activated)
values(2, 'Maker_Acc', 'maker@aet.com', 'MAKER', 0, '$2a$10$S6A.GvxBlFSWXN1K59MbZO5SL6BbDV8l4Ja8cefrWMYv5VPjivkg6', 'default', '2019-01-10', '2019-01-09', '2019-01-09', true);

insert into staff(id, first_name, email, maker_checker, group_id, i_password, created_by, approved_by, created_date, approved_date, activated)
values(3, 'SysDev', 'sysdev@aet.com', 'NONE', 0, '$2a$10$irbom5DMU9YwRdJG1pgpyONG..Vjg4Ru5mb6Ta9ODc.50ztsND.VG', 'default', '2019-01-11', '2019-01-09', '2019-01-09', true);

COMMIT;