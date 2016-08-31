/*≤Â»Î∂Œ¬‰±Ì*/
INSERT INTO t_section VALUES ( 'SECTION1000', 'USER00001', 'NOTE0001', '0', 2, '', 'this is the end222', '', NOW(), NOW(), 0, 0 ); 
INSERT INTO t_section VALUES ( 'SECTION2000', 'USER00001', 'NOTE0001', '0', 2, '', 'this is the end222', '', NOW(), NOW(), 0, 0 );
INSERT INTO t_section VALUES ( 'SECTION3000', 'USER00001', 'NOTE0001', '1', 2, 'audio10002', '', '', NOW(), NOW(), 0, 0 );
INSERT INTO t_section VALUES ( 'SECTION4000', 'USER00001', 'NOTE0001', '2', 2, '', '', 'image10002', NOW(), NOW(), 0, 0 );
INSERT INTO t_section VALUES ( 'SECTION2222', 'USER00001', 'NOTE0001', '2', 2, '', '', 'image10002',NOW(), '1470967168817', 0, 0 );


insert into t_image values('image10002','1234567890','imageTag001,imageTag002,imageTag003');


insert into t_image_tag values ('imageTag001',0,'','30%','30%','this is word','03:30',1);
insert into t_image_tag values ('imageTag002',1,'456456','50%','50%','','',1);
insert into t_image_tag values ('imageTag003',2,'123123','70%','70%','','03:30',1);
