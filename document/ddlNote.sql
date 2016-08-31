/*��Ŀ��*/
DROP TABLE IF EXISTS t_note;
create table t_note (
  id VARCHAR(255) primary key not null,
  userId VARCHAR(255) not null,
  name VARCHAR(255) not null,
  createAt TIMESTAMP not null COMMENT '����ʱ��',
  updateAt TIMESTAMP not null COMMENT '�޸�ʱ��',
  syncTimestamp TIMESTAMP not null COMMENT '�汾��',
  modifyFlag TINYINT null COMMENT '�޸ı�־',
  deleteFlag TINYINT null default 0 COMMENT 'ɾ����־',
  deviceId VARCHAR(255) not null,
  childIds TEXT NULL COMMENT 'һ���ڵ��б�1,2,3'
);

/*�ڵ�,���ı�*/
DROP TABLE IF EXISTS t_mind;
create table t_mind (
    id VARCHAR(255) primary key not null,
    noteId VARCHAR(255) not null COMMENT '��ĿID',
    mindType TINYINT not null default 0 COMMENT '0:�ڵ�,1:����',
    name VARCHAR(255) NOT NULL COMMENT '��󳤶���140������',
    childIds TEXT NOT NULL COMMENT '�ӽڵ��б�1,2,3',
    sectionIds TEXT NOT NULL COMMENT '��Ϊ����ʱ��Ŷ����б�:1,2,3',
    syncTimestamp TIMESTAMP not null COMMENT '�汾��',
    updateAt TIMESTAMP not null COMMENT '�޸�ʱ��',
    modifyFlag TINYINT null COMMENT '�޸ı�־',
    deleteFlag TINYINT null default 0 COMMENT 'ɾ����־'
);

/*�����*/
DROP TABLE IF EXISTS t_section;
create table t_section (
    id VARCHAR(255) primary key not null,
    userId VARCHAR(255) not null,
    noteId VARCHAR(255) null COMMENT '��ĿID����ʱ���ã������ֶ�',
    undocFlag TINYINT not null default 0 COMMENT 'δ�鵵��־',
    sectionType TINYINT not null default 0 COMMENT '0:�ı���1��ͼƬ����2����Ƶ',
    audioId VARCHAR(255) null COMMENT 'sectionTypeΪ2ʱ����ƵID',
    text TEXT default null COMMENT 'sectionTypeΪ0ʱ����������',
    imageIds TEXT default null COMMENT 'sectionTypeΪ1ʱ��ͼƬID����',
    updateAt TIMESTAMP not null COMMENT '�ڷǹ鵵�е�˳��',
    syncTimestamp TIMESTAMP not null COMMENT '�汾��',
    modifyFlag TINYINT null COMMENT '�޸ı�־',
    deleteFlag TINYINT null default 0 COMMENT 'ɾ����־'
);


/*ͼƬ��*/
DROP TABLE IF EXISTS t_image;
create table t_image (
  id VARCHAR(255) primary key not null,
  imageUrl VARCHAR(255) NOT NULL COMMENT 'ͼƬurl',
  size FLOAT NULL COMMENT 'ͼƬ��С',
  imageTagIds TEXT default null COMMENT 'ͼƬ�����tagID�б���1,2,3'
);

/*ͼƬ��ǩ��*/
DROP TABLE IF EXISTS t_image_tag;
create table t_image_tag (
    id VARCHAR(255) primary key not null,
    tagType TINYINT not null default 0 COMMENT '0:�ı���1��ͼƬ��2����Ƶ',
    sourceUrl VARCHAR(255) NOT NULL COMMENT 'ͼƬ������Ƶurl',
    directionX VARCHAR(255) NOT NULL COMMENT '�����꣺30%',
    directionY VARCHAR(255) NOT NULL COMMENT '�����꣺55%',
    text TEXT DEFAULT NULL COMMENT '�����ǩ�����֣�����������',
    duration VARCHAR(255) NOT null COMMENT '��Ƶ�ĳ���:00:30/02:55',
    size FLOAT NULL COMMENT '1��ͼƬ��2����Ƶ����Դ��С',
    isLeft TINYINT NOT NULL DEFAULT 0 COMMENT '0:��ͷ���ұ�,1:��ͷ�����'
);

/*��Ƶ��*/
DROP TABLE IF EXISTS t_audio;
create table t_audio (
  id VARCHAR(255) primary key not null,
  audioUrl VARCHAR(255) NOT NULL COMMENT '��Ƶurl',
  duration VARCHAR(255) NOT null COMMENT 'ʱ��',
  size FLOAT NULL COMMENT '��Ƶ��С',
  audioTagIds TEXT default null COMMENT '������tags��ǩID�б�:1,2,3'
);

/*������ǩ��*/
DROP TABLE IF EXISTS t_audio_tag;
create table t_audio_tag (
    id VARCHAR(255) primary key not null,
    tagType TINYINT not null default 0 COMMENT '0:�ı���1��ͼƬ',
    text TEXT DEFAULT NULL COMMENT '�����ǩ����tagType��0���֣�����������',
    imageUrl VARCHAR(255) NOT NULL COMMENT '�����ǩ����tagType��1ͼƬ��ͼƬ��url',
    size FLOAT NULL COMMENT '�����ǩ����tagType��1ͼƬ��ͼƬ�Ĵ�С',
    timePoint VARCHAR(255) NOT NULL COMMENT '��Ƶ��ʱ���'
);

DROP TABLE IF EXISTS t_lock;
create table t_lock (
    id integer primary key auto_increment,
  	userId VARCHAR(255) not null,
    deviceId VARCHAR(255) not null,
  	lockStatus TINYINT null default 0 COMMENT '��״̬',
  	createAt TIMESTAMP not null COMMENT '����ʱ��'
);

drop table if exists t_user;
create table t_user (
	userId bigint(15) not null primary key auto_increment COMMENT '�û�id',
	userName varchar(255) null COMMENT '�û�����',
	userPhoto varchar(255) null COMMENT '�û�ͷ��',
	password varchar(255) not null COMMENT '�û�����',
	mobile varchar(22) null COMMENT '�û��ֻ���',
	email varchar(255) not null COMMENT '�û�����',
	status TINYINT null default 0 COMMENT '�Ñ�״̬��1,���0δ����',
	createAt TIMESTAMP null DEFAULT CURRENT_TIMESTAMP COMMENT '����ʱ��',
	loginCount integer null COMMENT '��¼����',
	loginTime TIMESTAMP null COMMENT '��¼ʱ��',
	lastLoginTime TIMESTAMP null COMMENT '�ϴε�¼ʱ��'
)

drop table if exists t_source;
create table t_source (
	id integer primary key auto_increment,
	userId varchar(255) not null COMMENT '�û�id',
	soruceId varchar(255) not null COMMENT '��Դid',
	size FLOAT NULL COMMENT '��С'
);


drop table if exists t_register;
create table t_register (
	id integer primary key auto_increment,
	userName varchar(255) not null COMMENT '�û�id',
	smsCode varchar(20) not null COMMENT '��֤��',
	smsTime not null TIMESTAMP COMMENT '��֤�����ʼʱ��',
);


