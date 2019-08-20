-- insert into staff(id, first_name, email, type, i_password, created_by, approved_by, created_date, approved_date, activated)
insert into staff(id, first_name, email, type, i_password, approved_by, approved_date, activated)
values(1, 'ATM_User', 'atmrecon@aet.com', 'USER', '$2a$10$fobhDhagq2vzx/RisgWtiuZ2ybihxIqF2jPl9/zm4aVfI4WEzhOtS', 'default', '2019-01-09', true);

insert into staff(id, first_name, email, type, i_password, approved_by, approved_date, activated)
values(2, 'Settlement_User', 'settlement@aet.com', 'USER', '$2a$10$ydka6YmJeTVia4fdDHAkXeTEk.HW3220fFDHPZanhdfLyiE/aBIxa', 'default', '2019-01-12', true);

insert into staff(id, first_name, email, type, i_password, approved_by, approved_date, activated)
values(3, 'Admin_Acc', 'admin@aet.com', 'ADMIN', '$2a$10$0v503h5I1LCoWFs8XAj3eebmDk6fOR86sMp8gEaVJy/SzvxEliTfC', 'default', '2019-01-16', true);

insert into staff(id, first_name, email, type, i_password, approved_by, approved_date, activated)
values(4, 'Sys_Dev', 'sysdev@aet.com', 'DEV', '$2a$10$irbom5DMU9YwRdJG1pgpyONG..Vjg4Ru5mb6Ta9ODc.50ztsND.VG', 'default', '2019-01-17', true);

COMMIT;