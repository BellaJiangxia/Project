/******************************
 * 2017-02-23
******************************/

ALTER TABLE T_CHECK_OTHER_FEE ADD is_required INT NULL

ALTER TABLE T_CHECK_ITEM ADD is_required INT NULL
ALTER TABLE T_CHECK_ITEM ADD check_count INT NULL

/******************************
 * 2017-02-22
******************************/

ALTER TABLE T_RESERVATION ALTER COLUMN patient_idCard NVARCHAR(20)

ALTER TABLE T_RESERVATION ADD docter_mobile NVARCHAR(20) NULL


/******************************
 * 2017-02-13
******************************/

ALTER TABLE T_RESERVE_CONFIG ADD ORG_ID BIGINT NULL;
UPDATE T_RESERVE_CONFIG SET org_id=100039 WHERE provide_code='YT1000';

/******************************
 * 2017-01-18
******************************/

CREATE TABLE T_RESERVE_CONFIG
(
    PROVIDE_CODE NVARCHAR(50) PRIMARY KEY NOT NULL,
    PRODUCT_ID BIGINT NOT NULL,
    NOTE NVARCHAR(100)
)

-- INSERT INTO T_BASE_USER (MOBILE, PWD, TYPE, STATUS, USER_NAME, CREATE_TIME)
-- VALUES (10000000000,'e10adc3949ba59abbe56e057f20f883e',3,2,'微信公众号',getdate());

-- INSERT INTO T_DOCTOR_USER (ID) SELECT ID from T_BASE_USER where MOBILE='10000000000';

-- INSERT INTO T_ORGANIZATION (CREATOR_ID, ORG_NAME, DESCRIPTION, LEVELS, VERIFY_USER_ID, VARIFY_TIME, STATUS, CREATE_TIME,ORG_CODE, VISIBLE, is_public, ORG_PROPERTY)
-- SELECT (SELECT ID from T_BASE_USER where MOBILE='10000000000'),'微信公众号','微信公众号',
--   (SELECT ID from T_DIC_VALUE WHERE DIC_TYPE=3 AND VALUE='其他'),
--   (SELECT ID FROM T_BASE_USER WHERE MOBILE='88888888'),getdate(),1,getdate(),
--   (SELECT VALUE-2 FROM T_DIC_VALUE WHERE DIC_TYPE=12),2,10,11;

-- INSERT INTO T_ORG_USER_MAPPING (ORG_ID, USER_ID, PERMISSION, STATUS, CREATE_TIME)
--     SELECT ID,CREATOR_ID,'201,202,203,204',1,getdate() from T_ORGANIZATION where ORG_NAME='微信公众号';

-- UPDATE T_DIC_VALUE
-- SET VALUE = VALUE + 5
-- WHERE DIC_TYPE=12;

--  2017-01-14-----------------
ALTER TABLE T_RESERVATION ADD complaint NVARCHAR(2000) NULL;
ALTER TABLE T_RESERVATION ADD case_id BIGINT NULL;

BEGIN TRANSACTION
GO
ALTER TABLE [dbo].[T_DIAGNOSIS_V2] ADD
	[product_name] nvarchar(200) NULL,
	[product_charge_type] int NULL,
	[charge_amount] int NULL
GO
COMMIT;
BEGIN TRANSACTION
GO
ALTER TABLE [dbo].[T_DIAGNOSIS_V2] ADD
	[product_unit_price] numeric(20,2) NULL
GO
COMMIT;
BEGIN TRANSACTION
GO
ALTER TABLE [dbo].[T_DIAGNOSIS_V2] ADD
	[total_price] numeric(20,2) NULL
GO
COMMIT;
BEGIN TRANSACTION;
update t1 set t1.product_charge_type = t2.charge_type,t1.product_name = t2.name,t1.product_unit_price = t2.PRICE
from T_DIAGNOSIS_V2 t1,T_ORG_PRODUCT t2
where t1.DIAGNOSIS_PRODUCT_ID = t2.id;

update t1 set t1.charge_amount = (
	case t1.product_charge_type 
		WHEN 10 THEN (select count(id) from t_series where dicom_img_id = t1.dicom_img_id)
		WHEN 20 THEN (select SUM(expose_times) from t_series where dicom_img_id = t1.dicom_img_id)
		WHEN 30 THEN 1
	END
)
from T_DIAGNOSIS_V2 t1;

update T_DIAGNOSIS_V2 set total_price = (product_unit_price * charge_amount);

commit;

BEGIN TRANSACTION
GO
ALTER TABLE [dbo].[T_DICOM_IMG] ADD
	[body_part_ids] nvarchar(2000) NULL,
	[piece_amount] int NULL
GO
COMMIT;
/****************************
2017年1月9日 12:15:06
****************************/
ALTER TABLE T_ORG_AFFIX ADD view_type INT DEFAULT 10 NULL;
UPDATE T_ORG_AFFIX SET view_type=10

/******20161102**********************/
ALTER TABLE [dbo].[T_DIAGNOSIS_V2] ALTER COLUMN [allow_reporter_publish_report] int 
GO
/******20161026**********************/
ALTER TABLE [dbo].[T_DIAGNOSIS_V2]
ADD [urgent_level] int NOT NULL DEFAULT 10 
GO
ALTER TABLE [dbo].[T_REPORT]
ADD [request_urgent_level] int NOT NULL DEFAULT 10 
GO
ALTER TABLE [dbo].[T_ORGANIZATION]
ADD [certification_level] int NOT NULL DEFAULT 1 
GO
/**将待审核机构更新为已审核*/
UPDATE T_ORGANIZATION SET STATUS = 1 where STATUS =11

/******************************
 * 2016年10月15日 12:09:32
******************************/
ALTER TABLE T_ORGANIZATION ADD ORG_PROPERTY INT NOT NULL;
UPDATE T_ORGANIZATION SET ORG_PROPERTY=11;

INSERT INTO T_DIC_VALUE (DIC_TYPE, FATHER_DIC_ID, VALUE, FLAG, DESCRIPTION)
VALUES (23,0,'专家团队',3,'工作团队级别');
INSERT INTO T_DIC_VALUE (DIC_TYPE, FATHER_DIC_ID, VALUE, FLAG, DESCRIPTION)
VALUES (23,0,'个人工作室',3,'工作团队级别');
INSERT INTO T_DIC_VALUE (DIC_TYPE, FATHER_DIC_ID, VALUE, FLAG, DESCRIPTION)
VALUES (23,0,'研究性学会',3,'工作团队级别');

/******************************
 * 2016年3月26日 20:43:08
******************************/
alter table t_doctor_user drop column grade;
alter table t_doctor_user add device_opreator_id numeric null;
alter table t_base_user add grade nvarchar(128) null;
alter table t_user_change add device_opreator_id numeric null;

/******************************
 * 2016-03-19 23:59:37
******************************/
USE [YingTai]
GO

alter table T_ORGANIZATION add is_public numeric null;

alter table T_DIAGNOSIS add view_user_id numeric null;

alter table T_ORG_AFFIX add internet_ip varchar(32) null;

alter table T_ORG_AFFIX add intranet_url varchar(1000) null;

/****** Object:  Table [dbo].[T_MOFIDY_REPORT_REQUEST]    Script Date: 03/21/2016 18:16:12 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[T_MOFIDY_REPORT_REQUEST](
	[id] [numeric](19, 0) IDENTITY(1,1) NOT NULL,
	[diagnosis_id] [numeric](19, 0) NOT NULL,
	[request_user_id] [numeric](18, 0) NOT NULL,
	[response_user_id] [numeric](18, 0) NOT NULL,
	[reason] [nvarchar](1000) NOT NULL,
	[create_time] [datetime] NOT NULL,
	[response_time] [datetime] NULL,
	[status] [numeric](4, 0) NOT NULL,
	[request_org_id] [numeric](19, 0) NOT NULL,
	[response_org_id] [numeric](19, 0) NOT NULL,
	[answer] [nvarchar](1000) NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]

GO

ALTER TABLE [dbo].[T_MOFIDY_REPORT_REQUEST] ADD  DEFAULT ((0)) FOR [request_org_id]
GO

ALTER TABLE [dbo].[T_MOFIDY_REPORT_REQUEST] ADD  DEFAULT ((0)) FOR [response_org_id]
GO

ALTER TABLE [dbo].[T_MOFIDY_REPORT_REQUEST] ADD  DEFAULT ('') FOR [answer]
GO

/*******************20160419********************************/
CREATE TABLE [dbo].[T_USER_CONFIG] (
[user_id] numeric(19) NOT NULL ,
[sms_config] nvarchar(2000) NOT NULL 
)
GO
-- ----------------------------
-- Indexes structure for table T_USER_CONFIG
-- ----------------------------
-- ----------------------------
-- Primary Key structure for table T_USER_CONFIG
-- ----------------------------
ALTER TABLE [dbo].[T_USER_CONFIG] ADD PRIMARY KEY ([user_id])
GO


/**********************20160518***********************/
ALTER TABLE [dbo].[T_ORG_AFFIX]
ADD [remote_server_version] int NOT NULL DEFAULT 1 
GO

/*********************20160519*********************/
ALTER TABLE [dbo].[T_ORG_AFFIX] ALTER COLUMN [AE_CODE] varchar(500) COLLATE Chinese_PRC_CI_AS NULL 
GO

/*********************20160520*******************************/
-- ----------------------------
-- Table structure for T_PATIENT
-- ----------------------------
DROP TABLE [dbo].[T_PATIENT]
GO
CREATE TABLE [dbo].[T_PATIENT] (
[id] bigint NOT NULL IDENTITY(1,1) ,
[name] nvarchar(64) NOT NULL ,
[mobile] nvarchar(15) NULL ,
[identity_id] nvarchar(18) NULL ,
[home_address] nvarchar(256) NULL ,
[gender] smallint NOT NULL ,
[born_address] nvarchar(256) NULL ,
[work] nvarchar(256) NULL ,
[sick_his] nvarchar(1024) NULL ,
[create_time] datetime NOT NULL ,
[status] smallint NOT NULL ,
[source_type] smallint NOT NULL ,
[create_user_id] bigint NULL 
)


GO

-- ----------------------------
-- Indexes structure for table T_PATIENT
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table T_PATIENT
-- ----------------------------
ALTER TABLE [dbo].[T_PATIENT] ADD PRIMARY KEY ([id])
GO
-- -------------------------------------------
CREATE TABLE [dbo].[T_CASE_HISTORY] (
[id] bigint NOT NULL IDENTITY(1,1) ,
[patient_id] bigint NOT NULL ,
[org_id] bigint NOT NULL ,
[pacs_case_num] nvarchar(256) COLLATE Chinese_PRC_CI_AS NULL ,
[his_case_num] nvarchar(256) COLLATE Chinese_PRC_CI_AS NULL ,
[dicom_num] nvarchar(256) COLLATE Chinese_PRC_CI_AS NULL ,
[symptom] nvarchar(1024) COLLATE Chinese_PRC_CI_AS NULL ,
[enter_time] datetime NOT NULL ,
[leave_time] datetime NOT NULL ,
[status] smallint NOT NULL ,
[create_time] datetime NOT NULL ,
[create_user_id] bigint NOT NULL ,
[note] nvarchar(2000) COLLATE Chinese_PRC_CI_AS NULL ,
[hospitalized_num] nvarchar(256) COLLATE Chinese_PRC_CI_AS NULL ,
[reception_section] nvarchar(128) COLLATE Chinese_PRC_CI_AS NULL ,
[reception_doctor] nvarchar(64) COLLATE Chinese_PRC_CI_AS NULL ,
[eaf_list] nvarchar(1000) COLLATE Chinese_PRC_CI_AS NULL ,
[state] smallint NOT NULL ,
[source_type] smallint NOT NULL ,
[type] smallint NOT NULL ,
[modify_time] datetime NULL ,
[modify_user_id] bigint NULL ,
CONSTRAINT [PK__T_CASE_H__3213E83F4460231C] PRIMARY KEY ([id])
)
ON [PRIMARY]
GO

DBCC CHECKIDENT(N'[dbo].[T_CASE_HISTORY]', RESEED, 44)
GO

IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'T_CASE_HISTORY', 
'COLUMN', N'pacs_case_num')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N' pasc系统病人病历号 源病历号'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'T_CASE_HISTORY'
, @level2type = 'COLUMN', @level2name = N'pacs_case_num'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N' pasc系统病人病历号 源病历号'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'T_CASE_HISTORY'
, @level2type = 'COLUMN', @level2name = N'pacs_case_num'
GO

IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'T_CASE_HISTORY', 
'COLUMN', N'his_case_num')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N' his系统病历号'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'T_CASE_HISTORY'
, @level2type = 'COLUMN', @level2name = N'his_case_num'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N' his系统病历号'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'T_CASE_HISTORY'
, @level2type = 'COLUMN', @level2name = N'his_case_num'
GO

IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'T_CASE_HISTORY', 
'COLUMN', N'dicom_num')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'dicom影像号'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'T_CASE_HISTORY'
, @level2type = 'COLUMN', @level2name = N'dicom_num'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'dicom影像号'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'T_CASE_HISTORY'
, @level2type = 'COLUMN', @level2name = N'dicom_num'
GO

IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'T_CASE_HISTORY', 
'COLUMN', N'symptom')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'症状'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'T_CASE_HISTORY'
, @level2type = 'COLUMN', @level2name = N'symptom'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'症状'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'T_CASE_HISTORY'
, @level2type = 'COLUMN', @level2name = N'symptom'
GO

IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'T_CASE_HISTORY', 
'COLUMN', N'enter_time')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'入院时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'T_CASE_HISTORY'
, @level2type = 'COLUMN', @level2name = N'enter_time'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'入院时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'T_CASE_HISTORY'
, @level2type = 'COLUMN', @level2name = N'enter_time'
GO

IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'T_CASE_HISTORY', 
'COLUMN', N'leave_time')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'出院时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'T_CASE_HISTORY'
, @level2type = 'COLUMN', @level2name = N'leave_time'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'出院时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'T_CASE_HISTORY'
, @level2type = 'COLUMN', @level2name = N'leave_time'
GO

IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'T_CASE_HISTORY', 
'COLUMN', N'create_time')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'创建时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'T_CASE_HISTORY'
, @level2type = 'COLUMN', @level2name = N'create_time'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'创建时间'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'T_CASE_HISTORY'
, @level2type = 'COLUMN', @level2name = N'create_time'
GO

IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'T_CASE_HISTORY', 
'COLUMN', N'create_user_id')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'0标识系统检索到的'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'T_CASE_HISTORY'
, @level2type = 'COLUMN', @level2name = N'create_user_id'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'0标识系统检索到的'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'T_CASE_HISTORY'
, @level2type = 'COLUMN', @level2name = N'create_user_id'
GO

IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'T_CASE_HISTORY', 
'COLUMN', N'hospitalized_num')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'住院号'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'T_CASE_HISTORY'
, @level2type = 'COLUMN', @level2name = N'hospitalized_num'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'住院号'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'T_CASE_HISTORY'
, @level2type = 'COLUMN', @level2name = N'hospitalized_num'
GO

IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'T_CASE_HISTORY', 
'COLUMN', N'reception_section')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'接诊科室'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'T_CASE_HISTORY'
, @level2type = 'COLUMN', @level2name = N'reception_section'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'接诊科室'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'T_CASE_HISTORY'
, @level2type = 'COLUMN', @level2name = N'reception_section'
GO

IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'T_CASE_HISTORY', 
'COLUMN', N'reception_doctor')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'接诊医生'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'T_CASE_HISTORY'
, @level2type = 'COLUMN', @level2name = N'reception_doctor'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'接诊医生'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'T_CASE_HISTORY'
, @level2type = 'COLUMN', @level2name = N'reception_doctor'
GO

IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'T_CASE_HISTORY', 
'COLUMN', N'eaf_list')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'电子申请单'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'T_CASE_HISTORY'
, @level2type = 'COLUMN', @level2name = N'eaf_list'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'电子申请单'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'T_CASE_HISTORY'
, @level2type = 'COLUMN', @level2name = N'eaf_list'
GO

IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'T_CASE_HISTORY', 
'COLUMN', N'state')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'是否有效 99标识无效'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'T_CASE_HISTORY'
, @level2type = 'COLUMN', @level2name = N'state'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'是否有效 99标识无效'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'T_CASE_HISTORY'
, @level2type = 'COLUMN', @level2name = N'state'
GO

IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'T_CASE_HISTORY', 
'COLUMN', N'source_type')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'来源类型'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'T_CASE_HISTORY'
, @level2type = 'COLUMN', @level2name = N'source_type'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'来源类型'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'T_CASE_HISTORY'
, @level2type = 'COLUMN', @level2name = N'source_type'
GO

IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'T_CASE_HISTORY', 
'COLUMN', N'type')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'门诊；住院；远诊；其他'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'T_CASE_HISTORY'
, @level2type = 'COLUMN', @level2name = N'type'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'门诊；住院；远诊；其他'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'T_CASE_HISTORY'
, @level2type = 'COLUMN', @level2name = N'type'
GO
CREATE TABLE [dbo].[T_PATIENT_ORG_MAPPER] (
[id] bigint NOT NULL IDENTITY(1,1) ,
[patient_id] bigint NOT NULL ,
[org_id] bigint NOT NULL ,
[card_num] nvarchar(128) COLLATE Chinese_PRC_CI_AS NULL ,
[got_card_time] datetime NULL ,
[create_time] datetime NOT NULL ,
CONSTRAINT [PK__T_PATIEN__3213E83F4924D839] PRIMARY KEY ([id])
)
ON [PRIMARY]
GO

/**********************20160524***************************************/
CREATE TABLE [dbo].[T_DIAGNOSIS_V2] (
[ID] numeric(19) NOT NULL IDENTITY(1,1) ,
[CASE_HIS_ID] numeric(19) NOT NULL ,
[REQUEST_USER_ID] numeric(19) NOT NULL ,
[REQUEST_ORG_ID] numeric(19) NOT NULL ,
[DIAGNOSIS_ORG_ID] numeric(19) NOT NULL ,
[DIAGNOSIS_PRODUCT_ID] numeric(19) NOT NULL ,
[REQUEST_TIME] datetime NULL ,
[HANDLE_TIME] datetime NULL ,
[STATUS] numeric(2) NOT NULL ,
[CREATE_TIME] datetime NULL ,
[NOTE] nvarchar(2000) COLLATE Chinese_PRC_CI_AS NULL ,
[DICOM_IMG_DEVICE_TYPE_ID] numeric(19) NOT NULL ,
[DICOM_IMG_PART_TYPE_ID] numeric(19) NOT NULL ,
[ACCEPT_USER_ID] numeric(19) NULL ,
[VERIFY_USER_ID] numeric(19) NULL ,
[PIC_OPINION] nvarchar(2000) COLLATE Chinese_PRC_CI_AS NULL ,
[REPORT_TIME] datetime NULL ,
[PIC_CONCLUSION] nvarchar(2000) COLLATE Chinese_PRC_CI_AS NULL ,
[PUBLISH_REPORT_ORG_ID] numeric(19) NOT NULL ,
[dicom_img_id] numeric(19) NOT NULL ,
[CURR_HANDLE_ID] numeric(19) NULL ,
[F_O_M] numeric(1) NOT NULL DEFAULT ((0)) ,
[PRINT_USER_ID] numeric(19) NULL ,
[PRINT_TIMES] numeric(5) NOT NULL DEFAULT ((0)) ,
[PRINT_TIME] datetime NULL ,
[view_user_id] numeric(18) NULL ,
[case_his_num] nvarchar(256) COLLATE Chinese_PRC_CI_AS NOT NULL ,
[case_symptom] nvarchar(1024) COLLATE Chinese_PRC_CI_AS NULL ,
[case_enter_time] datetime NOT NULL ,
[case_leave_time] datetime NOT NULL ,
[case_hospitalized_num] nvarchar(256) COLLATE Chinese_PRC_CI_AS NULL ,
[case_reception_section] nvarchar(128) COLLATE Chinese_PRC_CI_AS NULL ,
[case_reception_doctor] nvarchar(64) COLLATE Chinese_PRC_CI_AS NULL ,
[case_eaf_list] nvarchar(1000) COLLATE Chinese_PRC_CI_AS NULL ,
[case_source_type] smallint NOT NULL ,
[case_type] smallint NOT NULL ,
[patient_name] nvarchar(64) COLLATE Chinese_PRC_CI_AS NOT NULL ,
[patient_identity_id] nvarchar(18) COLLATE Chinese_PRC_CI_AS NULL ,
[patient_gender] smallint NOT NULL ,
[patient_birthday] datetime NOT NULL ,
[patient_home_address] nvarchar(256) COLLATE Chinese_PRC_CI_AS NULL ,
[patient_born_address] nvarchar(256) COLLATE Chinese_PRC_CI_AS NULL ,
[patient_work] nvarchar(256) COLLATE Chinese_PRC_CI_AS NULL ,
[patient_sick_his] nvarchar(1024) COLLATE Chinese_PRC_CI_AS NULL ,
[patient_source_type] smallint NOT NULL ,
[dicom_img_thumbnail_uid] nvarchar(256) COLLATE Chinese_PRC_CI_AS NOT NULL ,
[dicom_img_mark_char] nvarchar(256) COLLATE Chinese_PRC_CI_AS NULL ,
[dicom_img_checklist_num] nvarchar(128) COLLATE Chinese_PRC_CI_AS NULL ,
[dicom_img_check_time] datetime NOT NULL ,
[dicom_img_check_pro] nvarchar(1000) COLLATE Chinese_PRC_CI_AS NULL ,
CONSTRAINT [PK__T_DIAGNO__3214EC275B438874] PRIMARY KEY ([ID])
)
ON [PRIMARY]
TEXTIMAGE_ON [PRIMARY]
GO

DBCC CHECKIDENT(N'[dbo].[T_DIAGNOSIS_V2]', RESEED, 155)
GO

/************************20160531********************************************/
CREATE TABLE [dbo].[T_REPORT_MSG] (
[ID] numeric(19) NOT NULL IDENTITY(1,1) ,
[REPORT_ID] numeric(19) NOT NULL ,
[SEND_ORG_ID] numeric(19) NOT NULL ,
[SEND_USER_ID] numeric(19) NOT NULL ,
[RECV_ORG_ID] numeric(19) NOT NULL ,
[RECV_USER_ID] numeric(19) NOT NULL ,
[CONTENT] nvarchar(500) COLLATE Chinese_PRC_CI_AS NOT NULL ,
[SEND_TIME] datetime NOT NULL ,
[RECV_TIME] datetime NULL ,
[STATUS] numeric(2) NOT NULL ,
[TYPE] numeric(2) NOT NULL ,
CONSTRAINT [PK__T_DIAGNO__3214EC2759C55456] PRIMARY KEY ([ID])
)
ON [PRIMARY]
GO

DBCC CHECKIDENT(N'[dbo].[T_REPORT_MSG]', RESEED, 107)
GO
-- -----------------------------------------
CREATE TABLE [dbo].[T_REPORT_SHARE_SPEECH] (
[ID] numeric(19) NOT NULL IDENTITY(1,1) ,
[SHARE_ID] numeric(19) NOT NULL ,
[SPEECH_USER_ID] numeric(19) NOT NULL ,
[SPEECH_TIME] datetime NULL ,
[CONTENT] varchar(2000) COLLATE Chinese_PRC_CI_AS NOT NULL ,
[STATUS] numeric(2) NOT NULL ,
CONSTRAINT [PK__T_MEDICA__3214EC2704AFB25B] PRIMARY KEY ([ID])
)
ON [PRIMARY]
GO

DBCC CHECKIDENT(N'[dbo].[T_REPORT_SHARE_SPEECH]', RESEED, 28)
GO

-- -------------------------------------------
CREATE TABLE [dbo].[T_REPORT_SHARE] (
[ID] numeric(19) NOT NULL IDENTITY(1,1) ,
[DIAGNOSIS_ID] numeric(19) NOT NULL ,
[ORG_ID] numeric(19) NOT NULL ,
[SHARE_USER_ID] numeric(19) NOT NULL ,
[SHARE_TIME] datetime NULL ,
[CLOSE_SHARE_TIME] datetime NULL ,
[STATUS] numeric(2) NOT NULL ,
[CLOSE_SHARE_USER_ID] numeric(19) NULL 
)
ON [PRIMARY]
GO

DBCC CHECKIDENT(N'[dbo].[T_REPORT_SHARE]', RESEED, 24)
GO

/* ************************20160604*************************** */
ALTER TABLE [dbo].[T_CASE_HISTORY] ALTER COLUMN [enter_time] datetime NULL 
GO

ALTER TABLE [dbo].[T_CASE_HISTORY] ALTER COLUMN [leave_time] datetime NULL 
GO

-- 20150915
ALTER TABLE [dbo].[T_PATIENT_ORG_MAPPER]
ADD [source_type] int NOT NULL DEFAULT 10 
GO
ALTER TABLE [dbo].[T_CASE_HISTORY] ALTER COLUMN [source_type] int 
GO
ALTER TABLE [dbo].[T_DICOM_IMG]
ADD [source_type] int NOT NULL DEFAULT 10 
GO
-----------------20160920
ALTER TABLE [dbo].[T_ORG_CONFIGS]
ADD [org_sys_device_name_mapping] nvarchar(MAX) NULL 
GO


-------20161015
ALTER TABLE [dbo].[T_DIAGNOSIS_V2]
ADD [request_class] int NOT NULL DEFAULT 10 
GO
ALTER TABLE [dbo].[T_REPORT]
ADD [request_class] int NULL DEFAULT 10 
GO
ALTER TABLE [dbo].[T_REPORT]
ADD [duty_report_user_id] bigint NOT NULL ,
[duty_verify_user_id] bigint NOT NULL ,
[duty_org_id] bigint NOT NULL 
GO

