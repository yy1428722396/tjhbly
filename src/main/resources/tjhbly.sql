/*
 Navicat Premium Data Transfer

 Source Server         : local
 Source Server Type    : MySQL
 Source Server Version : 50715
 Source Host           : localhost
 Source Database       : tjhbly

 Target Server Type    : MySQL
 Target Server Version : 50715
 File Encoding         : utf-8

 Date: 10/10/2017 15:49:17 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `t_build_basis`
-- ----------------------------
DROP TABLE IF EXISTS `t_build_basis`;
CREATE TABLE `t_build_basis` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `buildname` varchar(100) DEFAULT NULL,
  `prename` varchar(100) DEFAULT NULL,
  `rent` varchar(10) DEFAULT NULL,
  `property` varchar(10) DEFAULT NULL,
  `buildarea` varchar(10) DEFAULT NULL,
  `eartharea` varchar(10) DEFAULT NULL,
  `rentarea` varchar(10) DEFAULT NULL,
  `emptyarea` varchar(10) DEFAULT NULL,
  `buildright` varchar(10) DEFAULT NULL,
  `classvalue` varchar(10) DEFAULT NULL,
  `type` varchar(10) DEFAULT NULL,
  `service` varchar(10) DEFAULT NULL,
  `upfloor` varchar(3) DEFAULT '0',
  `address` varchar(400) DEFAULT NULL,
  `postcode` varchar(6) DEFAULT NULL,
  `buildyear` varchar(4) DEFAULT NULL,
  `devname` varchar(100) DEFAULT NULL,
  `street` varchar(100) DEFAULT NULL,
  `cbd` varchar(100) DEFAULT NULL,
  `propertyname` varchar(100) DEFAULT NULL,
  `propertytel` varchar(100) DEFAULT NULL,
  `createtime` varchar(19) DEFAULT NULL,
  `downfloor` varchar(2) DEFAULT NULL,
  `toucheman` varchar(20) DEFAULT NULL,
  `roomno` varchar(5) DEFAULT NULL,
  `statusvalue` int(3) unsigned zerofill DEFAULT NULL,
  `rununitid` int(8) DEFAULT NULL,
  `vrurl` varchar(400) DEFAULT NULL,
  `carno` varchar(4) DEFAULT NULL,
  `totalfloor` varchar(3) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `t_build_basis`
-- ----------------------------
BEGIN;
INSERT INTO `t_build_basis` VALUES ('1', '楼宇名称1', '楼宇原名称1', '1000001', '100001', '100001', '10001', '10001', '10001', '多产权', '甲级', '城市综合体', '文创', '91', '楼宇详细地址1', '111111', '1901', '楼宇开发商名称1', '所属街道1', '所属商圈1', '物业公司名称1', '111112', '2017-09-28 14:24:54', '11', '联系人1', '1231', '201', '0', 'www.baidu.com', '1001', '101'), ('2', '楼宇名称2', '楼宇原名称', '10000', '10000', '1000', '10000', '10000', '10000', '单产权', '国际甲级', '写字楼', '金融', '100', '楼宇详细地址', '100001', '1988', '楼宇开发商名称', '所属街道', '所属商圈', '物业公司名称', '11123', '2017-09-28 14:46:21', '0', '联系人', '12312', '201', '0', null, '100', '100'), ('3', '楼宇名称3', '', '', '', '', '', '', '', 'null', 'null', 'null', 'null', '', '', '', '', '', '', '', '', '', '2017-09-28 14:46:48', '', '', '', '201', '0', null, '', ''), ('4', '楼宇名称4', '', '', '', '', '', '', '', 'null', 'null', 'null', 'null', '', '', '', '', '', '', '', '', '', '2017-09-28 14:46:57', '', '', '', '201', '0', null, '', ''), ('5', '楼宇名称5', '', '', '', '', '', '', '', 'null', 'null', 'null', 'null', '', '', '', '', '', '', '', '', '', '2017-09-28 14:47:03', '', '', '', '201', '0', null, '', ''), ('6', '楼宇名称6', '楼宇名称', '', '', '', '', '', '', 'null', 'null', 'null', 'null', '', '', '', '', '', '', '', '', '', '2017-10-01 20:19:38', '', '', '', '201', '0', null, '', ''), ('7', '楼宇名称7', '', '', '', '', '', '', '', 'null', 'null', 'null', 'null', '', '', '', '', '', '', '', '', '', '2017-10-01 20:19:45', '', '', '', '201', '0', null, '', ''), ('8', '楼宇名称8', '', '', '', '', '', '', '', 'null', 'null', 'null', 'null', '', '', '', '', '', '', '', '', '', '2017-10-01 20:19:57', '', '', '', '201', '0', null, '', ''), ('9', '楼宇名称9', '', '', '', '', '', '', '', 'null', 'null', 'null', 'null', '', '', '', '', '', '', '', '', '', '2017-10-01 20:20:05', '', '', '', '201', '0', null, '', '');
COMMIT;

-- ----------------------------
--  Table structure for `t_build_basis_count`
-- ----------------------------
DROP TABLE IF EXISTS `t_build_basis_count`;
CREATE TABLE `t_build_basis_count` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `countmonth` varchar(7) NOT NULL,
  `unitnum` int(10) DEFAULT NULL,
  `incrunitnum` varchar(10) DEFAULT NULL,
  `workmannum` int(10) DEFAULT NULL,
  `incrworkmannum` varchar(10) DEFAULT NULL,
  `taxnum` int(16) DEFAULT NULL,
  `taxtotalnum` int(20) DEFAULT NULL,
  `rentarea` varchar(20) DEFAULT NULL,
  `emptyarea` varchar(20) DEFAULT NULL,
  `incrrentarea` varchar(20) DEFAULT NULL,
  `emptyrate` varchar(10) DEFAULT NULL,
  `buildid` int(8) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `t_build_benefit_count`
-- ----------------------------
DROP TABLE IF EXISTS `t_build_benefit_count`;
CREATE TABLE `t_build_benefit_count` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `buildid` int(8) NOT NULL,
  `taxrate` varchar(10) DEFAULT NULL,
  `incrtaxrate` varchar(10) DEFAULT NULL,
  `incomerate` varchar(10) DEFAULT NULL,
  `incrincomerate` varchar(10) DEFAULT NULL,
  `workmanrate` varchar(10) DEFAULT NULL,
  `incrworkmanrate` varchar(10) DEFAULT NULL,
  `areatax` varchar(10) DEFAULT NULL,
  `countdate` varchar(8) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `t_build_environment`
-- ----------------------------
DROP TABLE IF EXISTS `t_build_environment`;
CREATE TABLE `t_build_environment` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `subway` varchar(2) DEFAULT NULL,
  `station` varchar(2) DEFAULT NULL,
  `stopprice` varchar(5) DEFAULT NULL,
  `stopno` varchar(4) DEFAULT NULL,
  `buildstop` varchar(4) DEFAULT NULL,
  `restaurant` varchar(4) DEFAULT NULL,
  `hotel1` varchar(4) DEFAULT NULL,
  `hotel2` varchar(4) DEFAULT NULL,
  `hlift` varchar(2) DEFAULT NULL,
  `mlift` varchar(2) DEFAULT NULL,
  `lift` varchar(2) DEFAULT NULL,
  `workerrestaurant` varchar(2) DEFAULT NULL,
  `afforest` varchar(10) DEFAULT NULL,
  `bankno` varchar(2) DEFAULT NULL,
  `cbd` varchar(2) DEFAULT NULL,
  `cbdarea` varchar(2) DEFAULT NULL,
  `createtime` varchar(19) DEFAULT NULL,
  `statusvalue` int(3) unsigned zerofill DEFAULT NULL,
  `buildid` int(8) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `t_build_environment`
-- ----------------------------
BEGIN;
INSERT INTO `t_build_environment` VALUES ('1', '01', '01', '1', '0001', '0001', '0001', '0001', '0001', '01', '01', '01', '01', '1', '01', '01', '01', '2017-09-16 15:51:09', '1', '2'), ('2', '01', '02', '3', '0004', '0005', '0006', '0007', '0008', '09', '10', '11', '12', '13', '14', '15', '16', '2017-09-16 23:07:02', '0', '3'), ('3', '10', '10', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '2017-09-28 14:29:52', '201', '1'), ('4', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2017-09-28 14:31:20', '201', '1');
COMMIT;

-- ----------------------------
--  Table structure for `t_build_img`
-- ----------------------------
DROP TABLE IF EXISTS `t_build_img`;
CREATE TABLE `t_build_img` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `buildid` int(8) DEFAULT NULL,
  `imgurl` varchar(400) DEFAULT NULL,
  `type` int(1) DEFAULT NULL,
  `statusvalue` int(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `t_build_img`
-- ----------------------------
BEGIN;
INSERT INTO `t_build_img` VALUES ('2', '2', '1506855957652.jpg', '1', '1'), ('3', '1', '1506855710024.jpg', '1', '1'), ('4', '4', '1506855746462.jpg', '1', '1'), ('5', '5', '1506855755919.jpg', '1', '1'), ('6', '3', '1506855734615.jpg', '1', '1'), ('7', '6', '1506860430683.jpg', '1', '1'), ('8', '7', '1506860439813.jpg', '1', '1'), ('9', '8', '1506860454418.jpg', '1', '1'), ('10', '9', '1506860464217.jpg', '1', '1');
COMMIT;

-- ----------------------------
--  Table structure for `t_build_industry_count`
-- ----------------------------
DROP TABLE IF EXISTS `t_build_industry_count`;
CREATE TABLE `t_build_industry_count` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `buildid` int(8) NOT NULL,
  `services` varchar(4) DEFAULT NULL,
  `income` varchar(20) DEFAULT NULL,
  `incrincome` varchar(20) DEFAULT NULL,
  `tax` varchar(20) DEFAULT NULL,
  `incrtax` varchar(20) DEFAULT NULL,
  `workmannum` varchar(20) DEFAULT NULL,
  `incrworkmannum` varchar(20) DEFAULT NULL,
  `countdate` varchar(8) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `t_build_monthreport`
-- ----------------------------
DROP TABLE IF EXISTS `t_build_monthreport`;
CREATE TABLE `t_build_monthreport` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `buildid` int(8) DEFAULT NULL,
  `reportdate` varchar(7) DEFAULT NULL,
  `rentarea` varchar(10) DEFAULT NULL,
  `emptyarea` varchar(10) DEFAULT NULL,
  `incrbusinessnum` varchar(5) DEFAULT NULL,
  `decrbusinessnum` varchar(5) DEFAULT NULL,
  `asset` varchar(20) DEFAULT NULL,
  `income` varchar(20) DEFAULT NULL,
  `incrincome` varchar(20) DEFAULT NULL,
  `profit` varchar(20) DEFAULT NULL,
  `incrprofit` varchar(20) DEFAULT NULL,
  `tax` varchar(20) DEFAULT NULL,
  `incrtax` varchar(20) DEFAULT NULL,
  `workmannum` varchar(10) DEFAULT NULL,
  `incrworkmannum` varchar(10) DEFAULT NULL,
  `createtime` varchar(19) DEFAULT NULL,
  `statusvalue` int(3) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `t_build_monthreport`
-- ----------------------------
BEGIN;
INSERT INTO `t_build_monthreport` VALUES ('1', '2', '2017.09', null, '100', '00100', '00001', '100', '100', null, '100', null, '100', null, '0000000011', null, '2017-09-16 16:16:44', '1'), ('2', '2', '2017.09', null, '100', '00001', '00002', '3', '4', null, '5', null, '6', null, '0000000007', null, '2017-09-16 22:44:03', '1'), ('3', '2', '2017.09', null, '100', '00001', '00002', '3', '4', null, '5', null, '6', null, '0000000007', null, '2017-09-16 22:50:40', '1'), ('4', '3', '2017.09', null, '200', '00011', '00021', '31', '41', null, '51', null, '61', null, '0000000071', null, '2017-09-16 22:55:59', '1'), ('5', '1', '2017.09', null, '20000', '1001', '1001', '1001', '10001', null, '1001', null, '10001', null, '10001', null, '2017-09-28 14:32:29', '201'), ('6', '1', '2017.09', null, '100', '100', '100', '100', '100', null, '100', null, '100', null, '100', null, '2017-09-28 14:52:05', '201'), ('7', '1', '2018.01', null, '10000', '10000', '10000', '10000', '10000', '0.0', '10000', '0.0', '10000', '0.0', '10000', '0', '2017-09-29 17:21:47', '201');
COMMIT;

-- ----------------------------
--  Table structure for `t_build_unit`
-- ----------------------------
DROP TABLE IF EXISTS `t_build_unit`;
CREATE TABLE `t_build_unit` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `buildid` int(8) DEFAULT NULL,
  `unitname` varchar(100) DEFAULT NULL,
  `societycode` varchar(20) DEFAULT NULL,
  `registryasset` varchar(15) DEFAULT NULL,
  `registryaddress` varchar(400) DEFAULT NULL,
  `url` varchar(400) DEFAULT NULL,
  `services` varchar(200) DEFAULT NULL,
  `belongto` varchar(200) DEFAULT NULL,
  `isfirstcompany` varchar(2) DEFAULT NULL,
  `isentity` varchar(4) DEFAULT NULL,
  `workmannum` varchar(15) DEFAULT NULL,
  `floor` varchar(3) DEFAULT NULL,
  `roomno` varchar(6) DEFAULT NULL,
  `entertime` varchar(19) DEFAULT NULL,
  `leavetime` varchar(19) DEFAULT NULL,
  `area` varchar(20) DEFAULT NULL,
  `satisfay` varchar(10) DEFAULT NULL,
  `willleave` varchar(2) DEFAULT NULL,
  `leavereason` varchar(200) DEFAULT NULL,
  `createtime` varchar(19) DEFAULT NULL,
  `telphone` varchar(15) DEFAULT NULL,
  `mobile` varchar(15) DEFAULT NULL,
  `statusvalue` int(3) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `t_build_unit`
-- ----------------------------
BEGIN;
INSERT INTO `t_build_unit` VALUES ('1', '2', '入驻单位名称', '统一社会信用代码', '10000', '注册地址', '网站地址', '主要产品', '单产权', '是', '是', '000000000001000', '010', '10', 'null', 'null', '12', '满意', '无', 'fafsd', '2017-09-16 11:08:32', 'null', '10000', '1'), ('2', '2', '入驻单位名称', '统一社会信用代码', '10000', '注册地址', '网站地址', '主要产品', '多产权', '否', '否', '000000000001000', '010', '22', '2010-10-10', '2012-10-10', '20', '一般', '有', '说明理由', '2017-09-17 15:27:22', '12000', '128000', '1'), ('3', '1', '入驻单位名称1', '统一社会信用代码1', '10001', '注册地址1', '网站地址1', '主要产品（或服务）', '多产权', '否', '否', '1000001', '121', '12331', '1920-11-11', '1930-11-11', '10001', '满意', '有', '说明理由11111', '2017-09-28 14:27:29', '1231231', '1231231', '201');
COMMIT;

-- ----------------------------
--  Table structure for `t_build_user`
-- ----------------------------
DROP TABLE IF EXISTS `t_build_user`;
CREATE TABLE `t_build_user` (
  `id` int(8) NOT NULL,
  `username` varchar(100) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `role` int(1) DEFAULT NULL,
  `status` int(1) DEFAULT NULL,
  `createtime` varchar(19) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `t_count_flag`
-- ----------------------------
DROP TABLE IF EXISTS `t_count_flag`;
CREATE TABLE `t_count_flag` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `countdate` varchar(8) DEFAULT NULL,
  `tjhb_basis` int(1) DEFAULT NULL,
  `build_basis` int(1) DEFAULT NULL,
  `tjhb_benefit` int(1) DEFAULT NULL,
  `build_benefit` int(1) DEFAULT NULL,
  `tjhb_industry` int(1) DEFAULT NULL,
  `build_industry` int(1) DEFAULT NULL,
  `createdate` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `t_government_user`
-- ----------------------------
DROP TABLE IF EXISTS `t_government_user`;
CREATE TABLE `t_government_user` (
  `id` int(8) NOT NULL,
  `username` varchar(100) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `role` int(1) DEFAULT NULL,
  `status` int(1) DEFAULT NULL,
  `createtime` varchar(19) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `t_news`
-- ----------------------------
DROP TABLE IF EXISTS `t_news`;
CREATE TABLE `t_news` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `type` int(1) DEFAULT NULL,
  `title` varchar(200) DEFAULT NULL,
  `subtitle` varchar(200) DEFAULT NULL,
  `summary` varchar(1000) DEFAULT NULL,
  `content` text,
  `createtime` varchar(19) DEFAULT NULL,
  `createuser` varchar(100) DEFAULT NULL,
  `statusvalue` int(3) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `t_news`
-- ----------------------------
BEGIN;
INSERT INTO `t_news` VALUES ('1', '1', '名称', '副标题', '摘要', '<div align=\"center\">\n	<p>\n		标题\n	</p>\n	<p align=\"left\">\n		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 测试。\n	</p>\n	<p align=\"left\">\n		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容\n	</p>\n	<p align=\"center\">\n		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src=\"/tjhbly/attached/image/20170922/20170922223658_103.png\" alt=\"\" />\n	</p>\n	<p align=\"left\">\n		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容\n	</p>\n</div>', '2017-09-22 22:37:18', 'admin', '1'), ('5', '1', '政策法规', '副标题', '摘要', '<div align=\"center\">\n	<p>\n		发布内容\n	</p>\n	<p align=\"left\">\n		1、测试\n	</p>\n	<p align=\"left\">\n		2、测试\n	</p>\n</div>', '2017-09-16 21:01:08', 'admin', '1'), ('6', '1', '111', '', '', '<img src=\"/tjhbly/attached/image/20170916/20170916211829_452.png\" alt=\"\" />', '2017-09-16 21:19:32', 'admin', '1'), ('7', '2', '测试1', '测试1', '摘要1', '<div align=\"center\">\n	<p>\n		测试\n	</p>\n	<p align=\"left\">\n		1、测试1\n	</p>\n	<p align=\"left\">\n		<img src=\"/tjhbly/attached/image/20170916/20170916220542_902.png\" alt=\"\" /> \n	</p>\n</div>', '2017-09-16 22:17:38', 'admin', '1'), ('8', '1', '测试', '测试', '测试', '1发烧发', '2017-09-16 22:18:18', 'admin', '1'), ('9', '3', '通知测试', 'null', 'null', '通知内容：\n1、afafsda \n2、就发多发', '2017-09-22 22:55:34', 'admin', '1'), ('10', '3', '公告测试', 'null', 'null', '公告内容：\n1、hello\n2、测试', '2017-09-22 22:56:07', 'admin', '1'), ('11', '2', '名称', '副标题', '摘要', '<div align=\"left\">\n	<p>\n		发布内容：\n	</p>\n	<p>\n		1、发布内容\n	</p>\n	<p>\n		2、发布内容\n	</p>\n	<p>\n		<img src=\"/tjhbly/attached/image/20170928/20170928143340_486.gif\" alt=\"\" />\n	</p>\n</div>', '2017-09-28 14:33:43', 'no0user', '201'), ('12', '2', '工作动态', '工作动态副标题', '工作动态摘要', '<p>\n	工作动态内容\n</p>\n<p>\n	1、工作动态\n</p>\n<p>\n	2、工作动态\n</p>\n<p>\n	<img src=\"/tjhbly/attached/image/20170928/20170928143448_21.png\" alt=\"\" />\n</p>', '2017-09-28 14:34:49', 'no0user', '201'), ('13', '1', '政策法规', '政策法规副标题', '政策法规摘要', '<p>\n	政策法规内容\n</p>\n<p>\n	1、政策法规\n</p>\n<p>\n	政策法规\n</p>\n<p>\n	2、政策法规\n</p>\n<p>\n	<img src=\"/tjhbly/attached/image/20170928/20170928143523_162.jpg\" alt=\"\" />\n</p>', '2017-09-28 14:35:25', 'no0user', '201'), ('14', '3', '通知公告', 'null', 'null', '通知公告通知公告通知公告通知公告通知公告通知公告通知公告通知公告通知公告通知公告通知公告通知公告通知公告通知公告通知公告通知公告通知公告通知公告通知公告', '2017-09-28 14:35:37', 'no0user', '201'), ('15', '3', '通知公告2', 'null', 'null', '通知公告通知公告通知公告通知公告通知公告通知公告通知公告', '2017-09-28 14:35:45', 'no0user', '201');
COMMIT;

-- ----------------------------
--  Table structure for `t_notice`
-- ----------------------------
DROP TABLE IF EXISTS `t_notice`;
CREATE TABLE `t_notice` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `type` int(1) DEFAULT NULL,
  `title` varchar(200) DEFAULT NULL,
  `subtitle` varchar(200) DEFAULT NULL,
  `summary` varchar(1000) DEFAULT NULL,
  `content` text,
  `createtime` varchar(19) DEFAULT NULL,
  `createuser` varchar(100) DEFAULT NULL,
  `statusvalue` int(3) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `t_notice`
-- ----------------------------
BEGIN;
INSERT INTO `t_notice` VALUES ('1', '1', '名称', '副标题', '摘要', '', '2017-09-16 20:36:21', 'admin', '1'), ('2', '1', '', '', '', '', '2017-09-16 20:56:18', 'admin', '1'), ('3', '1', '', '', '', '', '2017-09-16 20:59:08', 'admin', '1'), ('4', '1', '', '', '', '', '2017-09-16 21:00:15', 'admin', '1'), ('5', '1', '政策法规', '副标题', '摘要', '<div align=\"center\">\n	<p>\n		发布内容\n	</p>\n	<p align=\"left\">\n		1、测试\n	</p>\n	<p align=\"left\">\n		2、测试\n	</p>\n</div>', '2017-09-16 21:01:08', 'admin', '1'), ('6', '1', '111', '', '', '<img src=\"/tjhbly/attached/image/20170916/20170916211829_452.png\" alt=\"\" />', '2017-09-16 21:19:32', 'admin', '1'), ('7', '2', '测试1', '测试1', '摘要1', '<div align=\"center\">\n	<p>\n		测试\n	</p>\n	<p align=\"left\">\n		1、测试1\n	</p>\n	<p align=\"left\">\n		<img src=\"/tjhbly/attached/image/20170916/20170916220542_902.png\" alt=\"\" /> \n	</p>\n</div>', '2017-09-16 22:17:38', 'admin', '0'), ('8', '1', '测试', '测试', '测试', '1发烧发', '2017-09-16 22:18:18', 'admin', '0');
COMMIT;

-- ----------------------------
--  Table structure for `t_rununit`
-- ----------------------------
DROP TABLE IF EXISTS `t_rununit`;
CREATE TABLE `t_rununit` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `unitname` varchar(100) DEFAULT NULL,
  `address` varchar(400) DEFAULT NULL,
  `touchman` varchar(50) DEFAULT NULL,
  `telphone` varchar(20) DEFAULT NULL,
  `statusvalue` int(1) DEFAULT NULL,
  `createtime` varchar(19) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `t_rununit`
-- ----------------------------
BEGIN;
INSERT INTO `t_rununit` VALUES ('1', '单位名称', '单位地址', '联系人', '123123', '1', '2017-09-28 14:37:48');
COMMIT;

-- ----------------------------
--  Table structure for `t_test`
-- ----------------------------
DROP TABLE IF EXISTS `t_test`;
CREATE TABLE `t_test` (
  `id` int(8) NOT NULL,
  `service` varchar(20) DEFAULT NULL,
  `tax` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `t_test`
-- ----------------------------
BEGIN;
INSERT INTO `t_test` VALUES ('1', 'it', '1000000'), ('2', 'it', '2000000'), ('3', '制造业', '1000000'), ('4', '制造业', '1000000'), ('5', '教育业', '100000'), ('6', '教育业', '100000');
COMMIT;

-- ----------------------------
--  Table structure for `t_tjhb_basis_count`
-- ----------------------------
DROP TABLE IF EXISTS `t_tjhb_basis_count`;
CREATE TABLE `t_tjhb_basis_count` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `countmonth` varchar(7) NOT NULL,
  `buildnum` int(10) DEFAULT NULL,
  `unitnum` int(10) DEFAULT NULL,
  `incrunitnum` varchar(10) DEFAULT NULL,
  `workmannum` int(10) DEFAULT NULL,
  `incrworkmannum` varchar(10) DEFAULT NULL,
  `taxnum` int(16) DEFAULT NULL,
  `taxtotalnum` int(20) DEFAULT NULL,
  `emptyarea` varchar(20) DEFAULT NULL,
  `rentarea` varchar(20) DEFAULT NULL,
  `incrrentarea` varchar(20) DEFAULT NULL,
  `emptyrate` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `t_tjhb_benefit_count`
-- ----------------------------
DROP TABLE IF EXISTS `t_tjhb_benefit_count`;
CREATE TABLE `t_tjhb_benefit_count` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `taxrate` varchar(10) DEFAULT NULL,
  `incrtaxrate` varchar(10) DEFAULT NULL,
  `incomerate` varchar(10) DEFAULT NULL,
  `incrincomerate` varchar(10) DEFAULT NULL,
  `workmanrate` varchar(10) DEFAULT NULL,
  `incrworkmanrate` varchar(10) DEFAULT NULL,
  `rank` int(4) DEFAULT NULL,
  `countdate` varchar(8) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `t_tjhb_industry_count`
-- ----------------------------
DROP TABLE IF EXISTS `t_tjhb_industry_count`;
CREATE TABLE `t_tjhb_industry_count` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `services` varchar(4) DEFAULT NULL,
  `income` varchar(20) DEFAULT NULL,
  `incrincome` varchar(20) DEFAULT NULL,
  `tax` varchar(20) DEFAULT NULL,
  `incrtax` varchar(20) DEFAULT NULL,
  `workmannum` varchar(20) DEFAULT NULL,
  `incrworkmannum` varchar(20) DEFAULT NULL,
  `countdate` varchar(8) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `t_tjhb_industry_count`
-- ----------------------------
BEGIN;
INSERT INTO `t_tjhb_industry_count` VALUES ('1', 'it', '100', '100', '100', '100', '100', '100', '2017.01'), ('2', 'it', '200', '200', '200', '200', '200', '200', '2017.02'), ('3', '金融', '100', '100', '100', '200', '100', '100', '2017.01'), ('4', '金融', '200', '200', '200', '300', '200', '200', '2017.02');
COMMIT;

-- ----------------------------
--  Table structure for `t_unit_monthreport`
-- ----------------------------
DROP TABLE IF EXISTS `t_unit_monthreport`;
CREATE TABLE `t_unit_monthreport` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `unitid` int(8) DEFAULT NULL,
  `reportdate` varchar(7) DEFAULT NULL,
  `asset` varchar(20) DEFAULT NULL,
  `income` varchar(20) DEFAULT NULL,
  `incrincome` varchar(20) DEFAULT NULL,
  `profit` varchar(20) DEFAULT NULL,
  `incrprofit` varchar(20) DEFAULT NULL,
  `tax` varchar(20) DEFAULT NULL,
  `incrtax` varchar(20) DEFAULT NULL,
  `createtime` varchar(19) DEFAULT NULL,
  `statusvalue` int(3) DEFAULT NULL,
  `workmannum` varchar(7) DEFAULT NULL,
  `incrworkmannum` varchar(7) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `t_unit_monthreport`
-- ----------------------------
BEGIN;
INSERT INTO `t_unit_monthreport` VALUES ('6', '3', '2017.5', '1000', '1000', null, '1000', null, '1000', null, '2017-09-28 15:09:34', '201', '12222', null), ('7', '1', '2017.10', '1000', '1000', '0.0', '1000', '0.0', '1000', '0.0', '2017-09-29 17:18:14', '201', '1000', '0'), ('9', '1', '2017.11', '2000', '2000', '1000.0', '2000', '1000.0', '2000', '1000.0', '2017-09-29 17:19:42', '201', '2000', '1000');
COMMIT;

-- ----------------------------
--  Table structure for `t_user`
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `username` varchar(100) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `role` int(1) DEFAULT NULL,
  `type` int(3) DEFAULT NULL,
  `statusvalue` int(3) DEFAULT NULL,
  `createtime` varchar(19) DEFAULT NULL,
  `unitid` int(8) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `t_user`
-- ----------------------------
BEGIN;
INSERT INTO `t_user` VALUES ('1', 'no0user', 'admin123', '201', '2', '1', null, '0'), ('7', 'ydld', '11111111', '102', '1', '1', '2017-09-28 14:39:18', '1'), ('8', 'ydgw', '11111111', '101', '1', '1', '2017-09-28 14:39:31', '1'), ('9', 'lyqld', '11111111', '204', '2', '1', '2017-09-28 14:39:43', '0'), ('10', 'lyzr', '11111111', '203', '2', '1', '2017-09-28 14:39:52', '0'), ('11', 'lyks', '11111111', '202', '2', '1', '2017-09-28 14:39:59', '0'), ('12', 'lyzy', '11111111', '201', '2', '1', '2017-09-28 14:40:05', '0'), ('13', 'test', '11111111', '204', '2', '0', '2017-09-28 14:40:09', '0');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
