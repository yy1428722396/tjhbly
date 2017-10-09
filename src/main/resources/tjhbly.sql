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

 Date: 09/27/2017 11:37:05 AM
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `t_build_environment`
-- ----------------------------
BEGIN;
INSERT INTO `t_build_environment` VALUES ('1', '01', '01', '1', '0001', '0001', '0001', '0001', '0001', '01', '01', '01', '01', '1', '01', '01', '01', '2017-09-16 15:51:09', '1', '2'), ('2', '01', '02', '3', '0004', '0005', '0006', '0007', '0008', '09', '10', '11', '12', '13', '14', '15', '16', '2017-09-16 23:07:02', '0', '3');
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `t_build_img`
-- ----------------------------
BEGIN;
INSERT INTO `t_build_img` VALUES ('1', '2', '1505824733386.png', null, null), ('2', '2', '1505826151990.png', '1', '0');
COMMIT;

-- ----------------------------
--  Table structure for `t_build_monthreport`
-- ----------------------------
DROP TABLE IF EXISTS `t_build_monthreport`;
CREATE TABLE `t_build_monthreport` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `buildid` int(8) DEFAULT NULL,
  `reportdate` varchar(7) DEFAULT NULL,
  `emptyarea` varchar(10) DEFAULT NULL,
  `incrbusinessnum` varchar(5) DEFAULT NULL,
  `decrbusinessnum` varchar(5) DEFAULT NULL,
  `asset` varchar(20) DEFAULT NULL,
  `income` varchar(20) DEFAULT NULL,
  `profit` varchar(20) DEFAULT NULL,
  `tax` varchar(20) DEFAULT NULL,
  `workmannum` varchar(10) DEFAULT NULL,
  `createtime` varchar(19) DEFAULT NULL,
  `statusvalue` int(3) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `t_build_monthreport`
-- ----------------------------
BEGIN;
INSERT INTO `t_build_monthreport` VALUES ('1', '2', '2019.10', '100', '00100', '00001', '100', '100', '100', '100', '0000000011', '2017-09-16 16:16:44', '1'), ('2', '2', '2017-09', '100', '00001', '00002', '3', '4', '5', '6', '0000000007', '2017-09-16 22:44:03', '0'), ('3', '2', '2017-09', '100', '00001', '00002', '3', '4', '5', '6', '0000000007', '2017-09-16 22:50:40', '0'), ('4', '3', '2017-12', '200', '00011', '00021', '31', '41', '51', '61', '0000000071', '2017-09-16 22:55:59', '0');
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `t_build_unit`
-- ----------------------------
BEGIN;
INSERT INTO `t_build_unit` VALUES ('1', '2', '入驻单位名称', '统一社会信用代码', '10000', '注册地址', '网站地址', '主要产品', '单产权', '是', '是', '000000000001000', '010', '10', 'null', 'null', '12', '满意', '无', 'fafsd', '2017-09-16 11:08:32', 'null', '10000', '1'), ('2', '2', '入驻单位名称', '统一社会信用代码', '10000', '注册地址', '网站地址', '主要产品', '多产权', '否', '否', '000000000001000', '010', '22', '2010-10-10', '2012-10-10', '20', '一般', '有', '说明理由', '2017-09-17 15:27:22', '12000', '128000', '0');
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
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `t_news`
-- ----------------------------
BEGIN;
INSERT INTO `t_news` VALUES ('1', '1', '名称', '副标题', '摘要', '<div align=\"center\">\n	<p>\n		标题\n	</p>\n	<p align=\"left\">\n		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 测试。\n	</p>\n	<p align=\"left\">\n		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容\n	</p>\n	<p align=\"center\">\n		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src=\"/tjhbly/attached/image/20170922/20170922223658_103.png\" alt=\"\" />\n	</p>\n	<p align=\"left\">\n		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容\n	</p>\n</div>', '2017-09-22 22:37:18', 'admin', '1'), ('5', '1', '政策法规', '副标题', '摘要', '<div align=\"center\">\n	<p>\n		发布内容\n	</p>\n	<p align=\"left\">\n		1、测试\n	</p>\n	<p align=\"left\">\n		2、测试\n	</p>\n</div>', '2017-09-16 21:01:08', 'admin', '1'), ('6', '1', '111', '', '', '<img src=\"/tjhbly/attached/image/20170916/20170916211829_452.png\" alt=\"\" />', '2017-09-16 21:19:32', 'admin', '1'), ('7', '2', '测试1', '测试1', '摘要1', '<div align=\"center\">\n	<p>\n		测试\n	</p>\n	<p align=\"left\">\n		1、测试1\n	</p>\n	<p align=\"left\">\n		<img src=\"/tjhbly/attached/image/20170916/20170916220542_902.png\" alt=\"\" /> \n	</p>\n</div>', '2017-09-16 22:17:38', 'admin', '1'), ('8', '1', '测试', '测试', '测试', '1发烧发', '2017-09-16 22:18:18', 'admin', '1'), ('9', '3', '通知测试', 'null', 'null', '通知内容：\n1、afafsda \n2、就发多发', '2017-09-22 22:55:34', 'admin', '1'), ('10', '3', '公告测试', 'null', 'null', '公告内容：\n1、hello\n2、测试', '2017-09-22 22:56:07', 'admin', '1');
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
--  Table structure for `t_unit_monthreport`
-- ----------------------------
DROP TABLE IF EXISTS `t_unit_monthreport`;
CREATE TABLE `t_unit_monthreport` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `unitid` int(8) DEFAULT NULL,
  `reportdate` varchar(7) DEFAULT NULL,
  `asset` varchar(20) DEFAULT NULL,
  `income` varchar(20) DEFAULT NULL,
  `profit` varchar(20) DEFAULT NULL,
  `tax` varchar(20) DEFAULT NULL,
  `createtime` varchar(19) DEFAULT NULL,
  `statusvalue` int(3) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `t_unit_monthreport`
-- ----------------------------
BEGIN;
INSERT INTO `t_unit_monthreport` VALUES ('1', '2', '2019.10', '100', '100', '100', '100', '2017-09-16 16:16:44', '1'), ('2', '2', '2017-09', '3', '4', '5', '6', '2017-09-16 22:44:03', '0'), ('3', '2', '2017-09', '3', '4', '5', '6', '2017-09-16 22:50:40', '0'), ('4', '3', '2017-12', '31', '41', '51', '61', '2017-09-16 22:55:59', '0');
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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `t_user`
-- ----------------------------
BEGIN;
INSERT INTO `t_user` VALUES ('1', 'no0user', 'admin123', '201', '2', '0', null, '0'), ('2', 'test', '11111111', '204', '2', '0', '2017-09-16 16:39:56', null), ('3', 'test1', '11111111', '101', '1', '0', '2017-09-16 16:55:20', null), ('4', 'test3', '11111111', '101', '1', '0', '2017-09-16 16:56:52', null), ('5', 'test4', '11111111', '101', '1', '1', '2017-09-16 17:04:06', null), ('6', 'test', 'test123123', '201', '2', '0', '2017-09-16 17:05:57', null);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
