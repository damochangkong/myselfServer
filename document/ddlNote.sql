/*项目表*/
DROP TABLE IF EXISTS t_note;
create table t_note (
  id VARCHAR(255) primary key not null,
  userId VARCHAR(255) not null,
  name VARCHAR(255) not null,
  createAt TIMESTAMP not null COMMENT '创建时间',
  updateAt TIMESTAMP not null COMMENT '修改时间',
  syncTimestamp TIMESTAMP not null COMMENT '版本号',
  modifyFlag TINYINT null COMMENT '修改标志',
  deleteFlag TINYINT null default 0 COMMENT '删除标志',
  deviceId VARCHAR(255) not null,
  childIds TEXT NULL COMMENT '一级节点列表：1,2,3'
);

/*节点,长文表*/
DROP TABLE IF EXISTS t_mind;
create table t_mind (
    id VARCHAR(255) primary key not null,
    noteId VARCHAR(255) not null COMMENT '项目ID',
    mindType TINYINT not null default 0 COMMENT '0:节点,1:长文',
    name VARCHAR(255) NOT NULL COMMENT '最大长度是140个文字',
    childIds TEXT NOT NULL COMMENT '子节点列表：1,2,3',
    sectionIds TEXT NOT NULL COMMENT '当为长文时存放段落列表:1,2,3',
    syncTimestamp TIMESTAMP not null COMMENT '版本号',
    updateAt TIMESTAMP not null COMMENT '修改时间',
    modifyFlag TINYINT null COMMENT '修改标志',
    deleteFlag TINYINT null default 0 COMMENT '删除标志'
);

/*段落表*/
DROP TABLE IF EXISTS t_section;
create table t_section (
    id VARCHAR(255) primary key not null,
    userId VARCHAR(255) not null,
    noteId VARCHAR(255) null COMMENT '项目ID，暂时不用，保留字段',
    undocFlag TINYINT not null default 0 COMMENT '未归档标志',
    sectionType TINYINT not null default 0 COMMENT '0:文本，1：图片集，2：音频',
    audioId VARCHAR(255) null COMMENT 'sectionType为2时的音频ID',
    text TEXT default null COMMENT 'sectionType为0时的文字内容',
    imageIds TEXT default null COMMENT 'sectionType为1时的图片ID集合',
    updateAt TIMESTAMP not null COMMENT '在非归档中的顺序',
    syncTimestamp TIMESTAMP not null COMMENT '版本号',
    modifyFlag TINYINT null COMMENT '修改标志',
    deleteFlag TINYINT null default 0 COMMENT '删除标志'
);


/*图片表*/
DROP TABLE IF EXISTS t_image;
create table t_image (
  id VARCHAR(255) primary key not null,
  imageUrl VARCHAR(255) NOT NULL COMMENT '图片url',
  size FLOAT NULL COMMENT '图片大小',
  imageTagIds TEXT default null COMMENT '图片里面的tagID列表，：1,2,3'
);

/*图片标签表*/
DROP TABLE IF EXISTS t_image_tag;
create table t_image_tag (
    id VARCHAR(255) primary key not null,
    tagType TINYINT not null default 0 COMMENT '0:文本，1：图片，2：音频',
    sourceUrl VARCHAR(255) NOT NULL COMMENT '图片或者音频url',
    directionX VARCHAR(255) NOT NULL COMMENT '横坐标：30%',
    directionY VARCHAR(255) NOT NULL COMMENT '纵坐标：55%',
    text TEXT DEFAULT NULL COMMENT '如果标签是文字，保存在这里',
    duration VARCHAR(255) NOT null COMMENT '音频的长度:00:30/02:55',
    size FLOAT NULL COMMENT '1：图片，2：音频，资源大小',
    isLeft TINYINT NOT NULL DEFAULT 0 COMMENT '0:箭头在右边,1:箭头在左边'
);

/*音频表*/
DROP TABLE IF EXISTS t_audio;
create table t_audio (
  id VARCHAR(255) primary key not null,
  audioUrl VARCHAR(255) NOT NULL COMMENT '音频url',
  duration VARCHAR(255) NOT null COMMENT '时长',
  size FLOAT NULL COMMENT '音频大小',
  audioTagIds TEXT default null COMMENT '下属的tags标签ID列表:1,2,3'
);

/*语音标签表*/
DROP TABLE IF EXISTS t_audio_tag;
create table t_audio_tag (
    id VARCHAR(255) primary key not null,
    tagType TINYINT not null default 0 COMMENT '0:文本，1：图片',
    text TEXT DEFAULT NULL COMMENT '如果标签类型tagType是0文字，保存在这里',
    imageUrl VARCHAR(255) NOT NULL COMMENT '如果标签类型tagType是1图片，图片的url',
    size FLOAT NULL COMMENT '如果标签类型tagType是1图片，图片的大小',
    timePoint VARCHAR(255) NOT NULL COMMENT '音频的时间点'
);

DROP TABLE IF EXISTS t_lock;
create table t_lock (
    id integer primary key auto_increment,
  	userId VARCHAR(255) not null,
    deviceId VARCHAR(255) not null,
  	lockStatus TINYINT null default 0 COMMENT '锁状态',
  	createAt TIMESTAMP not null COMMENT '创建时间'
);

drop table if exists t_user;
create table t_user (
	userId bigint(15) not null primary key auto_increment COMMENT '用户id',
	userName varchar(255) null COMMENT '用户姓名',
	userPhoto varchar(255) null COMMENT '用户头像',
	password varchar(255) not null COMMENT '用户密码',
	mobile varchar(22) null COMMENT '用户手机号',
	email varchar(255) not null COMMENT '用户邮箱',
	status TINYINT null default 0 COMMENT '用戶状态，1,激活，0未激活',
	createAt TIMESTAMP null DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	loginCount integer null COMMENT '登录次数',
	loginTime TIMESTAMP null COMMENT '登录时间',
	lastLoginTime TIMESTAMP null COMMENT '上次登录时间'
)

drop table if exists t_source;
create table t_source (
	id integer primary key auto_increment,
	userId varchar(255) not null COMMENT '用户id',
	soruceId varchar(255) not null COMMENT '资源id',
	size FLOAT NULL COMMENT '大小'
);


drop table if exists t_register;
create table t_register (
	id integer primary key auto_increment,
	userName varchar(255) not null COMMENT '用户id',
	smsCode varchar(20) not null COMMENT '验证码',
	smsTime not null TIMESTAMP COMMENT '验证码的起始时间',
);


