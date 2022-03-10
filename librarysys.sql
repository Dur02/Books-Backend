/*
 Navicat Premium Data Transfer

 Source Server         : mysqldb
 Source Server Type    : MySQL
 Source Server Version : 50623
 Source Host           : localhost:3306
 Source Schema         : librarysys

 Target Server Type    : MySQL
 Target Server Version : 50623
 File Encoding         : 65001

 Date: 10/03/2022 22:25:54
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for book
-- ----------------------------
DROP TABLE IF EXISTS `book`;
CREATE TABLE `book`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `bookname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `author` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `number` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 58 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of book
-- ----------------------------
INSERT INTO `book` VALUES (1, '大雪中的山庄', '东野圭吾', 379);
INSERT INTO `book` VALUES (2, '控方证人', '阿加莎·克里斯蒂', 100);
INSERT INTO `book` VALUES (3, '追寻生命的意义[奥] 维克多·弗兰克', '[奥] 维克多·弗兰克', 152);
INSERT INTO `book` VALUES (52, 'test', 'me', 330);
INSERT INTO `book` VALUES (53, '2', '554', 110);
INSERT INTO `book` VALUES (54, 'test', 'ww', 375);
INSERT INTO `book` VALUES (55, 'ww', 'ww', 1);
INSERT INTO `book` VALUES (56, 'ww', 'wddaw', 12);
INSERT INTO `book` VALUES (57, 'da', '2da', 13);

-- ----------------------------
-- Table structure for history
-- ----------------------------
DROP TABLE IF EXISTS `history`;
CREATE TABLE `history`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userid` int(255) NOT NULL,
  `bookid` int(255) NOT NULL,
  `number` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of history
-- ----------------------------
INSERT INTO `history` VALUES (1, 1, 1, 15);
INSERT INTO `history` VALUES (2, 1, 2, 2);
INSERT INTO `history` VALUES (5, 2, 2, 124);
INSERT INTO `history` VALUES (16, 2, 1, 1);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `role` int(255) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'Dur02', '02468', 1);
INSERT INTO `user` VALUES (2, 'temple', '13579', 0);
INSERT INTO `user` VALUES (3, 'Dude', '123456', 1);

SET FOREIGN_KEY_CHECKS = 1;
