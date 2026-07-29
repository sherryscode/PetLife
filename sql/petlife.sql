/*
 Navicat Premium Dump SQL

 Source Server         : MySQL
 Source Server Type    : MySQL
 Source Server Version : 80043 (8.0.43)
 Source Host           : localhost:3306
 Source Schema         : petlife

 Target Server Type    : MySQL
 Target Server Version : 80043 (8.0.43)
 File Encoding         : 65001

 Date: 01/07/2026 19:33:20
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '管理员ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码(BCrypt加密)',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '管理员' COMMENT '昵称',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '头像',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态 0禁用 1启用',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE,
  INDEX `idx_username`(`username` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '管理员表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES (1, 'admin', '$2a$10$VUB5.dEpbB9bXu23tY9ZoOTuPlNaYjWSGlXbRcoq5n1bVeo6vtbaq', '超级管理员', '', 1, '2026-06-29 16:46:47', '2026-06-30 09:38:55');

-- ----------------------------
-- Table structure for community_comment
-- ----------------------------
DROP TABLE IF EXISTS `community_comment`;
CREATE TABLE `community_comment`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `post_id` bigint NOT NULL COMMENT '帖子ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父评论ID',
  `content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '评论内容',
  `likes` int NULL DEFAULT 0 COMMENT '点赞数',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态 0删除 1正常',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_post_id`(`post_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_parent_id`(`parent_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '社区评论表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of community_comment
-- ----------------------------
INSERT INTO `community_comment` VALUES (1, 1, 1, 0, '111', 0, 1, '2026-07-01 19:06:44', '2026-07-01 19:06:44');
INSERT INTO `community_comment` VALUES (2, 1, 1, 0, '222', 0, 1, '2026-07-01 19:06:44', '2026-07-01 19:06:44');
INSERT INTO `community_comment` VALUES (3, 1, 1, 0, '333', 0, 1, '2026-07-01 19:06:44', '2026-07-01 19:06:44');

-- ----------------------------
-- Table structure for community_post
-- ----------------------------
DROP TABLE IF EXISTS `community_post`;
CREATE TABLE `community_post`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '帖子ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '帖子标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '帖子内容',
  `images` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '图片URL,逗号分隔',
  `likes` int NULL DEFAULT 0 COMMENT '点赞数',
  `comments` int NULL DEFAULT 0 COMMENT '评论数',
  `favorites` int NULL DEFAULT 0 COMMENT '收藏数',
  `audit_status` tinyint NULL DEFAULT 0 COMMENT '审核状态 0待审核 1通过 2驳回',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态 0下架 1正常',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_likes`(`likes` ASC) USING BTREE,
  INDEX `idx_audit_status`(`audit_status` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '社区帖子表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of community_post
-- ----------------------------
INSERT INTO `community_post` VALUES (1, 1, '111', '111', '', 11, 2, 0, 1, 1, '2026-07-01 19:06:44', '2026-07-01 19:06:44');
INSERT INTO `community_post` VALUES (2, 1, '222', '222', '', 1, 0, 0, 1, 1, '2026-07-01 19:06:44', '2026-07-01 19:06:44');
INSERT INTO `community_post` VALUES (3, 1, '333', '333', '', 0, 0, 0, 1, 1, '2026-07-01 19:06:44', '2026-07-01 19:06:44');
INSERT INTO `community_post` VALUES (4, 1, '新几只', '111', '', 0, 0, 0, 1, 1, '2026-07-01 19:06:44', '2026-07-01 19:06:44');
INSERT INTO `community_post` VALUES (6, 1, '帖子', '帖子，汉语词语，拼音tiě zi，指书面通知如请柬，亦指记录生辰八字的纸片、留言便条，现多用于网络论坛发布的主题内容 [1] [4]。\n该词历史文献中衍生多重含义：南北朝作质钱凭证（《南史》），宋代指进献宫廷的节日诗作（苏轼《次韵秦少游元旦立春》）；明清时期扩展至招租启事（《二十年目睹之怪现状》）及社交拜帖（《儒林外史》）。当代网络语境下特指论坛或社交媒体发布的文字信息 [2-5]。\n“贴子”南北朝已出现表示典当，北宋扩展为官府文书；“帖子”唐代指简短纸条，五代为官府公文。现代通用“帖子”指网络内容，《现代汉语词典》仅收录“帖子”词形，日常使用频率占据优势 [3-4]。', '/api/files/20260701/adefae04bc9b4d1490e66e3feb88086c.png', 1, 0, 0, 1, 1, '2026-07-01 19:06:44', '2026-07-01 19:06:44');
INSERT INTO `community_post` VALUES (7, 1, 'sihdoiha', 'sdwahdhsaihdaoffbiafaHihdioahihdoidhsihdiafvafbbafhoihoihfioshfasihfashfasihafhashsahfohsaihioahfoisfhiosafhihfoifhiashfshiofhasihafihfishfiohisafihoiashfoihiahfiohfihfiafaffafafasf', '/api/files/20260701/198e9bc96fe34c2393cd076de9b4c096.webp', 0, 0, 0, 2, 1, '2026-07-01 19:06:44', '2026-07-01 19:06:44');

-- ----------------------------
-- Table structure for goods
-- ----------------------------
DROP TABLE IF EXISTS `goods`;
CREATE TABLE `goods`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `merchant_id` bigint NOT NULL COMMENT '商户ID',
  `goods_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品名称',
  `category` tinyint NOT NULL COMMENT '分类 1粮食 2玩具 3洗护 4药品',
  `price` decimal(10, 2) NOT NULL COMMENT '价格',
  `original_price` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '原价',
  `stock` int NULL DEFAULT 0 COMMENT '库存',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '商品描述',
  `images` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '图片URL,逗号分隔',
  `sales` int NULL DEFAULT 0 COMMENT '销量',
  `is_hot` tinyint NULL DEFAULT 0 COMMENT '是否热销 0否 1是',
  `audit_status` tinyint NULL DEFAULT 0 COMMENT '审核状态 0待审核 1通过 2驳回',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态 0下架 1上架',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_merchant_id`(`merchant_id` ASC) USING BTREE,
  INDEX `idx_category`(`category` ASC) USING BTREE,
  INDEX `idx_is_hot`(`is_hot` ASC) USING BTREE,
  INDEX `idx_audit_status`(`audit_status` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of goods
-- ----------------------------
INSERT INTO `goods` VALUES (5, 1, '猫粮', 1, 100.00, 0.00, 100, '好吃不贵', '/api/files/20260701/3790e2f887064da8aaae614005aec90a.webp', 0, 0, 1, 1, '2026-07-01 19:06:44', '2026-07-01 19:06:44');
INSERT INTO `goods` VALUES (6, 1, '猫条', 1, 10.00, 0.00, 1000, '猫条', '/api/files/20260701/1210df1dc1514b7b81a1eb0a4648359f.jpg', 0, 0, 1, 1, '2026-07-01 19:06:44', '2026-07-01 19:06:44');

-- ----------------------------
-- Table structure for goods_order
-- ----------------------------
DROP TABLE IF EXISTS `goods_order`;
CREATE TABLE `goods_order`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `order_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单编号',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `total_amount` decimal(10, 2) NOT NULL COMMENT '订单金额',
  `pay_status` tinyint NULL DEFAULT 0 COMMENT '支付状态 0未支付 1已支付',
  `ship_status` tinyint NULL DEFAULT 0 COMMENT '发货状态 0待发货 1已发货 2已签收',
  `order_status` tinyint NULL DEFAULT 0 COMMENT '订单状态 0待付款 1待发货 2待收货 3已完成 4已取消 5退款中',
  `receiver_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '收货人',
  `receiver_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '收货电话',
  `receiver_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '收货地址',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '备注',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `order_no`(`order_no` ASC) USING BTREE,
  INDEX `idx_order_no`(`order_no` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_order_status`(`order_status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商城订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of goods_order
-- ----------------------------
INSERT INTO `goods_order` VALUES (1, 'GO2026063017441280208C', 1, 60.00, 1, 2, 3, 'sherry', '18760018159', '111', '', '2026-06-30 00:00:00', '2026-07-01 18:52:23');
INSERT INTO `goods_order` VALUES (2, 'GO202607011722201503B4', 1, 60.00, 1, 2, 3, 'sherry', '18760018159', '111', '', '2026-07-01 00:00:00', '2026-07-01 18:52:23');

-- ----------------------------
-- Table structure for goods_order_item
-- ----------------------------
DROP TABLE IF EXISTS `goods_order_item`;
CREATE TABLE `goods_order_item`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '订单项ID',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `goods_id` bigint NOT NULL COMMENT '商品ID',
  `goods_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品名称',
  `price` decimal(10, 2) NOT NULL COMMENT '单价',
  `quantity` int NOT NULL COMMENT '数量',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order_id`(`order_id` ASC) USING BTREE,
  INDEX `idx_goods_id`(`goods_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商城订单项表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of goods_order_item
-- ----------------------------
INSERT INTO `goods_order_item` VALUES (1, 2, 6, '猫条', 10.00, 5, '2026-07-01 19:06:44', '2026-07-01 19:06:44');

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父菜单ID',
  `menu_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单名称',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '路由路径',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '组件路径',
  `icon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '图标',
  `menu_type` tinyint NULL DEFAULT 0 COMMENT '菜单类型 0目录 1菜单 2按钮',
  `sort_order` int NULL DEFAULT 0 COMMENT '排序',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态 0隐藏 1显示',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_parent_id`(`parent_id` ASC) USING BTREE,
  INDEX `idx_menu_type`(`menu_type` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '菜单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of menu
-- ----------------------------
INSERT INTO `menu` VALUES (1, 0, '系统管理', '/system', '', 'system', 0, 1, 1, '2026-06-29 16:46:47', '2026-06-29 16:46:47');
INSERT INTO `menu` VALUES (2, 0, '内容审核', '/audit', '', 'audit', 0, 2, 1, '2026-06-29 16:46:47', '2026-06-29 16:46:47');
INSERT INTO `menu` VALUES (3, 0, '业务数据', '/business', '', 'business', 0, 3, 1, '2026-06-29 16:46:47', '2026-06-29 16:46:47');
INSERT INTO `menu` VALUES (4, 0, '系统运维', '/ops', '', 'ops', 0, 4, 1, '2026-06-29 16:46:47', '2026-06-29 16:46:47');
INSERT INTO `menu` VALUES (5, 1, '用户管理', '/system/users', 'system/users/index', 'user', 1, 1, 1, '2026-06-29 16:46:47', '2026-06-29 16:46:47');
INSERT INTO `menu` VALUES (6, 1, '商户管理', '/system/merchants', 'system/merchants/index', 'shop', 1, 2, 1, '2026-06-29 16:46:47', '2026-06-29 16:46:47');
INSERT INTO `menu` VALUES (7, 1, '角色管理', '/system/roles', 'system/roles/index', 'role', 1, 3, 1, '2026-06-29 16:46:47', '2026-06-29 16:46:47');
INSERT INTO `menu` VALUES (8, 1, '菜单管理', '/system/menus', 'system/menus/index', 'menu', 1, 4, 1, '2026-06-29 16:46:47', '2026-06-29 16:46:47');
INSERT INTO `menu` VALUES (9, 2, '寻宠启事审核', '/audit/lost', 'audit/lost/index', 'lost', 1, 1, 1, '2026-06-29 16:46:47', '2026-06-29 16:46:47');
INSERT INTO `menu` VALUES (10, 2, '社区帖子审核', '/audit/posts', 'audit/posts/index', 'post', 1, 2, 1, '2026-06-29 16:46:47', '2026-06-29 16:46:47');
INSERT INTO `menu` VALUES (11, 2, '商城商品审核', '/audit/goods', 'audit/goods/index', 'goods', 1, 3, 1, '2026-06-29 16:46:47', '2026-06-29 16:46:47');
INSERT INTO `menu` VALUES (12, 3, '宠物档案', '/business/pets', 'business/pets/index', 'pet', 1, 1, 1, '2026-06-29 16:46:47', '2026-06-29 16:46:47');
INSERT INTO `menu` VALUES (13, 3, '订单管理', '/business/orders', 'business/orders/index', 'order', 1, 2, 1, '2026-06-29 16:46:47', '2026-06-29 16:46:47');
INSERT INTO `menu` VALUES (14, 3, '公告管理', '/business/announcements', 'business/announcements/index', 'announcement', 1, 3, 1, '2026-06-29 16:46:47', '2026-06-29 16:46:47');
INSERT INTO `menu` VALUES (15, 4, '操作日志', '/ops/logs', 'ops/logs/index', 'log', 1, 1, 1, '2026-06-29 16:46:47', '2026-06-29 16:46:47');
INSERT INTO `menu` VALUES (16, 4, '缓存管理', '/ops/cache', 'ops/cache/index', 'cache', 1, 2, 1, '2026-06-29 16:46:47', '2026-06-29 16:46:47');
INSERT INTO `menu` VALUES (17, 4, '数据大屏', '/ops/dashboard', 'ops/dashboard/index', 'dashboard', 1, 3, 1, '2026-06-29 16:46:47', '2026-06-29 16:46:47');

-- ----------------------------
-- Table structure for merchant
-- ----------------------------
DROP TABLE IF EXISTS `merchant`;
CREATE TABLE `merchant`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '商户ID',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '手机号',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码(BCrypt加密)',
  `merchant_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商户名称',
  `store_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '门店名称',
  `store_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '门店地址',
  `store_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '门店电话',
  `business_scope` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '营业范围',
  `business_hours` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '营业时间',
  `rating` decimal(2, 1) NULL DEFAULT 5.0 COMMENT '评分',
  `audit_status` tinyint NULL DEFAULT 0 COMMENT '审核状态 0待审核 1通过 2驳回',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态 0禁用 1启用',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `phone`(`phone` ASC) USING BTREE,
  INDEX `idx_phone`(`phone` ASC) USING BTREE,
  INDEX `idx_audit_status`(`audit_status` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of merchant
-- ----------------------------
INSERT INTO `merchant` VALUES (1, '12121', '$2a$10$eAzZwN4iQwzm7Oop2cbUuOVFy/BUClFY9nTXAdwdZI57BGfRD6kcK', '11', NULL, NULL, NULL, NULL, NULL, 5.0, 1, 1, '2026-07-01 19:06:44', '2026-07-01 19:06:44');

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '消息ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '消息标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '消息内容',
  `message_type` tinyint NULL DEFAULT 0 COMMENT '消息类型 0系统通知 1疫苗提醒 2预约提醒 3订单提醒',
  `read_status` tinyint NULL DEFAULT 0 COMMENT '阅读状态 0未读 1已读',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '跳转链接',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_read_status`(`read_status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '消息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of message
-- ----------------------------

-- ----------------------------
-- Table structure for pet
-- ----------------------------
DROP TABLE IF EXISTS `pet`;
CREATE TABLE `pet`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '宠物ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `pet_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '宠物名称',
  `breed` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '品种',
  `age` int NULL DEFAULT 0 COMMENT '年龄(月)',
  `gender` tinyint NULL DEFAULT 0 COMMENT '性别 0未知 1公 2母',
  `weight` decimal(5, 2) NULL DEFAULT 0.00 COMMENT '体重(kg)',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '照片',
  `adopt_date` date NULL DEFAULT NULL COMMENT '领养日期',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态 0已离世 1正常',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '宠物档案表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pet
-- ----------------------------
INSERT INTO `pet` VALUES (3, 1, '茉莉', '缅因', 1, 2, 7.00, '/api/files/20260701/efb016e715d7430bb375ab3f141b9435.jpg', '2026-06-30', 1, '2026-07-01 19:06:44', '2026-07-01 19:06:44');

-- ----------------------------
-- Table structure for pet_case
-- ----------------------------
DROP TABLE IF EXISTS `pet_case`;
CREATE TABLE `pet_case`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '病历ID',
  `pet_id` bigint NOT NULL COMMENT '宠物ID',
  `hospital_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '医院名称',
  `disease` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '病症',
  `medicine` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '用药',
  `diagnosis` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '诊断结果',
  `visit_time` datetime NOT NULL COMMENT '就诊时间',
  `cost` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '费用',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_pet_id`(`pet_id` ASC) USING BTREE,
  INDEX `idx_visit_time`(`visit_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '病历表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pet_case
-- ----------------------------
INSERT INTO `pet_case` VALUES (1, 2, '宠物医院', '猫藓', '消毒', '11', '2026-06-30 03:39:39', 500.00, '2026-07-01 19:06:44', '2026-07-01 19:06:44');
INSERT INTO `pet_case` VALUES (2, 3, 'xx医院', '猫藓', 'xx药', 'xxxxxx', '2026-07-01 07:07:11', 300.00, '2026-07-01 19:06:44', '2026-07-01 19:06:44');

-- ----------------------------
-- Table structure for pet_checkup
-- ----------------------------
DROP TABLE IF EXISTS `pet_checkup`;
CREATE TABLE `pet_checkup`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `pet_id` bigint NOT NULL COMMENT '宠物ID',
  `hospital_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '医院名称',
  `checkup_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '体检类型',
  `checkup_time` datetime NULL DEFAULT NULL COMMENT '体检时间',
  `cost` decimal(10, 2) NULL DEFAULT NULL COMMENT '费用',
  `result` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '体检结果',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_pet_id`(`pet_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '宠物体检记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pet_checkup
-- ----------------------------
INSERT INTO `pet_checkup` VALUES (1, 2, '宠物医院', '11', '2026-06-30 03:30:36', 1000.00, '111', '111', '2026-07-01 19:06:44', '2026-07-01 19:06:44');
INSERT INTO `pet_checkup` VALUES (2, 3, 'xx医院', 'xx体检', '2026-07-01 07:06:27', 300.00, '猫藓', '须小心处理', '2026-07-01 19:06:44', '2026-07-01 19:06:44');

-- ----------------------------
-- Table structure for pet_lost
-- ----------------------------
DROP TABLE IF EXISTS `pet_lost`;
CREATE TABLE `pet_lost`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '寻宠启事ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `pet_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '宠物名称',
  `breed` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '品种',
  `gender` tinyint NULL DEFAULT 0 COMMENT '性别',
  `age` int NULL DEFAULT 0 COMMENT '年龄',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '照片',
  `lost_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '丢失地点',
  `lost_time` datetime NOT NULL COMMENT '丢失时间',
  `contact_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '联系方式',
  `reward` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '悬赏金额',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '特征描述',
  `is_top` tinyint NULL DEFAULT 0 COMMENT '是否置顶 0否 1是',
  `audit_status` tinyint NULL DEFAULT 0 COMMENT '审核状态 0待审核 1通过 2驳回',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态 0已找到 1寻找中',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_is_top`(`is_top` ASC) USING BTREE,
  INDEX `idx_audit_status`(`audit_status` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '寻宠启事表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pet_lost
-- ----------------------------
INSERT INTO `pet_lost` VALUES (1, 1, '呆呆', '11', 1, 2, '', '11', '2026-06-11 16:00:00', '1111', 1.00, '1111', 0, 1, 1, '2026-07-01 19:06:44', '2026-07-01 19:06:44');
INSERT INTO `pet_lost` VALUES (2, 1, '一一', '狗', 1, 0, '/api/files/20260701/2022307ca3d84c9aaeac97af3a81b965.jpg', '123', '2026-06-30 16:00:00', '12345', 500.00, '111222333烦烦烦方法 ', 0, 1, 1, '2026-07-01 19:06:44', '2026-07-01 19:06:44');
INSERT INTO `pet_lost` VALUES (3, 1, '11', '111', 1, 11, '/api/files/20260701/36d728825e954be4854954b8c2b7ac0f.jpg', '1111', '2026-06-30 16:00:00', '1111111', 1111.00, '11111', 0, 1, 1, '2026-07-01 19:06:44', '2026-07-01 19:06:44');
INSERT INTO `pet_lost` VALUES (4, 1, '茉莉', '缅因', 2, 1, '/api/files/20260701/a3d1468755854b449f79acf9842619b5.jpg', '泉州职业技术大学', '2026-06-30 16:00:00', '18760018159', 1000.00, '法护犊和 好好十大好好的爱混后i啊哈都会我还会地的哈好好ihi哦哇哈哈的hi和i哦啊皇帝哈维i和我啊混蛋hi哦哈啊好玩hi哦对hi哦啊回复哈哈的哈哦ihi活动i阿黄我挥发发黑u发噶哈u分化为符号位回复和', 0, 1, 1, '2026-07-01 19:06:44', '2026-07-01 19:06:44');

-- ----------------------------
-- Table structure for pet_vaccine
-- ----------------------------
DROP TABLE IF EXISTS `pet_vaccine`;
CREATE TABLE `pet_vaccine`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '疫苗记录ID',
  `pet_id` bigint NOT NULL COMMENT '宠物ID',
  `vaccine_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '疫苗名称',
  `vaccine_time` datetime NOT NULL COMMENT '接种时间',
  `valid_until` datetime NULL DEFAULT NULL COMMENT '有效期至',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '备注',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_pet_id`(`pet_id` ASC) USING BTREE,
  INDEX `idx_valid_until`(`valid_until` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '疫苗记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pet_vaccine
-- ----------------------------
INSERT INTO `pet_vaccine` VALUES (1, 2, '111', '2026-06-29 16:00:00', '2026-07-31 16:00:00', '111', '2026-07-01 19:06:44', '2026-07-01 19:06:44');
INSERT INTO `pet_vaccine` VALUES (2, 3, 'xx疫苗', '2026-07-01 07:06:01', '2026-07-01 16:00:00', 'xx疫苗接种', '2026-07-01 19:06:44', '2026-07-01 19:06:44');

-- ----------------------------
-- Table structure for post_like
-- ----------------------------
DROP TABLE IF EXISTS `post_like`;
CREATE TABLE `post_like`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `post_id` bigint NOT NULL,
  `status` tinyint(1) NOT NULL DEFAULT 1,
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_post`(`user_id` ASC, `post_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of post_like
-- ----------------------------
INSERT INTO `post_like` VALUES (1, 1, 1, 0, '2026-07-01 19:06:44', '2026-07-01 19:06:44');
INSERT INTO `post_like` VALUES (2, 1, 2, 0, '2026-07-01 19:06:44', '2026-07-01 19:06:44');
INSERT INTO `post_like` VALUES (3, 1, 5, 0, '2026-07-01 19:06:44', '2026-07-01 19:06:44');
INSERT INTO `post_like` VALUES (4, 1, 6, 1, '2026-07-01 19:06:44', '2026-07-01 19:06:44');

-- ----------------------------
-- Table structure for reserve_order
-- ----------------------------
DROP TABLE IF EXISTS `reserve_order`;
CREATE TABLE `reserve_order`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '预约工单ID',
  `order_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单编号',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `pet_id` bigint NULL DEFAULT NULL COMMENT '宠物ID',
  `store_id` bigint NOT NULL COMMENT '门店ID',
  `service_id` bigint NOT NULL COMMENT '服务套餐ID',
  `reserve_date` date NOT NULL COMMENT '预约日期',
  `reserve_time` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '预约时段',
  `total_amount` decimal(10, 2) NOT NULL COMMENT '订单金额',
  `deposit_amount` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '定金金额',
  `pay_status` tinyint NULL DEFAULT 0 COMMENT '支付状态 0未支付 1已支付',
  `order_status` tinyint NULL DEFAULT 0 COMMENT '订单状态 0待接单 1已接单 2服务中 3已完成 4已取消',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '上门地址',
  `pet_desc` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '宠物情况描述',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '备注',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `order_no`(`order_no` ASC) USING BTREE,
  INDEX `idx_order_no`(`order_no` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_store_id`(`store_id` ASC) USING BTREE,
  INDEX `idx_order_status`(`order_status` ASC) USING BTREE,
  INDEX `idx_reserve_date`(`reserve_date` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '预约工单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of reserve_order
-- ----------------------------
INSERT INTO `reserve_order` VALUES (1, 'RO20260630105910C15E7B', 1, NULL, 1, 1, '2026-06-30', '14:00', 100.00, 30.00, 0, 3, '', '111', '111', '2026-07-01 19:06:44', '2026-07-01 19:06:44');

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名称',
  `role_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色编码',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '角色描述',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态 0禁用 1启用',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `role_name`(`role_name` ASC) USING BTREE,
  UNIQUE INDEX `role_code`(`role_code` ASC) USING BTREE,
  INDEX `idx_role_code`(`role_code` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (1, '普通用户', 'user', '养宠用户角色', 1, '2026-06-29 16:46:47', '2026-06-29 16:46:47');
INSERT INTO `role` VALUES (2, '商户', 'merchant', '宠物门店商户角色', 1, '2026-06-29 16:46:47', '2026-06-29 16:46:47');
INSERT INTO `role` VALUES (3, '管理员', 'admin', '平台管理员角色', 1, '2026-06-29 16:46:47', '2026-06-29 16:46:47');

-- ----------------------------
-- Table structure for role_menu
-- ----------------------------
DROP TABLE IF EXISTS `role_menu`;
CREATE TABLE `role_menu`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_role_menu`(`role_id` ASC, `menu_id` ASC) USING BTREE,
  INDEX `idx_role_id`(`role_id` ASC) USING BTREE,
  INDEX `idx_menu_id`(`menu_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色菜单关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role_menu
-- ----------------------------
INSERT INTO `role_menu` VALUES (1, 3, 1, '2026-06-29 16:46:47');
INSERT INTO `role_menu` VALUES (2, 3, 2, '2026-06-29 16:46:47');
INSERT INTO `role_menu` VALUES (3, 3, 3, '2026-06-29 16:46:47');
INSERT INTO `role_menu` VALUES (4, 3, 4, '2026-06-29 16:46:47');
INSERT INTO `role_menu` VALUES (5, 3, 5, '2026-06-29 16:46:47');
INSERT INTO `role_menu` VALUES (6, 3, 6, '2026-06-29 16:46:47');
INSERT INTO `role_menu` VALUES (7, 3, 7, '2026-06-29 16:46:47');
INSERT INTO `role_menu` VALUES (8, 3, 8, '2026-06-29 16:46:47');
INSERT INTO `role_menu` VALUES (9, 3, 9, '2026-06-29 16:46:47');
INSERT INTO `role_menu` VALUES (10, 3, 10, '2026-06-29 16:46:47');
INSERT INTO `role_menu` VALUES (11, 3, 11, '2026-06-29 16:46:47');
INSERT INTO `role_menu` VALUES (12, 3, 12, '2026-06-29 16:46:47');
INSERT INTO `role_menu` VALUES (13, 3, 13, '2026-06-29 16:46:47');
INSERT INTO `role_menu` VALUES (14, 3, 14, '2026-06-29 16:46:47');
INSERT INTO `role_menu` VALUES (15, 3, 15, '2026-06-29 16:46:47');
INSERT INTO `role_menu` VALUES (16, 3, 16, '2026-06-29 16:46:47');
INSERT INTO `role_menu` VALUES (17, 3, 17, '2026-06-29 16:46:47');

-- ----------------------------
-- Table structure for shop_cart
-- ----------------------------
DROP TABLE IF EXISTS `shop_cart`;
CREATE TABLE `shop_cart`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '购物车ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `goods_id` bigint NOT NULL COMMENT '商品ID',
  `quantity` int NULL DEFAULT 1 COMMENT '数量',
  `selected` tinyint NULL DEFAULT 1 COMMENT '是否选中 0否 1是',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_goods`(`user_id` ASC, `goods_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_goods_id`(`goods_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '购物车表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of shop_cart
-- ----------------------------

-- ----------------------------
-- Table structure for store
-- ----------------------------
DROP TABLE IF EXISTS `store`;
CREATE TABLE `store`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '门店ID',
  `merchant_id` bigint NOT NULL COMMENT '商户ID',
  `store_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '门店名称',
  `store_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '门店地址',
  `store_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '门店电话',
  `business_scope` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '营业范围',
  `business_hours` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '营业时间',
  `rating` decimal(2, 1) NULL DEFAULT 5.0 COMMENT '评分',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态 0关闭 1营业',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_merchant_id`(`merchant_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '门店表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of store
-- ----------------------------
INSERT INTO `store` VALUES (1, 1, '11', '11', '11', '11', '8:00-19:00', 5.0, 1, '2026-07-01 19:06:44', '2026-07-01 19:06:44');

-- ----------------------------
-- Table structure for store_service
-- ----------------------------
DROP TABLE IF EXISTS `store_service`;
CREATE TABLE `store_service`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '服务套餐ID',
  `store_id` bigint NOT NULL COMMENT '门店ID',
  `service_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '服务名称',
  `service_type` tinyint NOT NULL COMMENT '服务类型 1洗护 2寄养 3医疗 4遛狗',
  `price` decimal(10, 2) NOT NULL COMMENT '价格',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '服务描述',
  `duration` int NULL DEFAULT 0 COMMENT '服务时长(分钟)',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态 0下架 1上架',
  `sort_order` int NULL DEFAULT 0 COMMENT '排序',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_store_id`(`store_id` ASC) USING BTREE,
  INDEX `idx_service_type`(`service_type` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '门店服务套餐表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of store_service
-- ----------------------------
INSERT INTO `store_service` VALUES (1, 1, '美容洗护', 1, 100.00, '全身清洁', 20, 1, 0, '2026-07-01 19:06:44', '2026-07-01 19:06:44');

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户ID',
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户名',
  `operation` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作类型',
  `module` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作模块',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作详细描述',
  `url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '请求URL',
  `method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '请求方法',
  `params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '请求参数',
  `result` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '返回结果',
  `ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT 'IP地址',
  `user_agent` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '用户代理',
  `execution_time` bigint NULL DEFAULT 0 COMMENT '执行时长(毫秒)',
  `status` tinyint NULL DEFAULT 0 COMMENT '状态 0失败 1成功',
  `error_msg` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '错误信息',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_operation`(`operation` ASC) USING BTREE,
  INDEX `idx_module`(`module` ASC) USING BTREE,
  INDEX `idx_created_at`(`created_at` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统操作日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_log
-- ----------------------------
INSERT INTO `sys_log` VALUES (1, NULL, 'admin', '登录', '系统管理', '管理员登录', '/admin/login', 'POST', '[{\"username\":\"admin\",\"password\":\"admin\"}]', '{\"code\":200,\"message\":\"success\",\"data\":{\"admin\":{\"id\":1,\"createdAt\":\"2026-06-29T16:46:47\",\"updatedAt\":\"2026-06-30T09:38:55\",\"username\":\"admin\",\"password\":\"$2a$10$VUB5.dEpbB9bXu23tY9ZoOTuPlNaYjWSGlXbRcoq5n1bVeo6vtbaq\",\"nickname\":\"超级管理员\",\"avatar\":\"\",\"status\":1},\"token\":\"eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiYWRtaW4iLCJ1c2VySWQiOjEsImV4cCI6MTc4Mjk4ODg2MH0.KlrQ8pNgVMLv_iQLONMBSw7Z2GCc23LOEUpe1xlaHks\"}}', '26.119.228.39', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/149.0.0.0 Safari/537.36', 83, 1, NULL, '2026-07-01 19:06:44', '2026-07-01 19:06:44');
INSERT INTO `sys_log` VALUES (2, NULL, 'admin', '登录', '系统管理', '管理员登录', '/admin/login', 'POST', '[{\"username\":\"admin\",\"password\":\"admin\"}]', '{\"code\":200,\"message\":\"success\",\"data\":{\"admin\":{\"id\":1,\"createdAt\":\"2026-06-29T16:46:47\",\"updatedAt\":\"2026-06-30T09:38:55\",\"username\":\"admin\",\"password\":\"$2a$10$VUB5.dEpbB9bXu23tY9ZoOTuPlNaYjWSGlXbRcoq5n1bVeo6vtbaq\",\"nickname\":\"超级管理员\",\"avatar\":\"\",\"status\":1},\"token\":\"eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiYWRtaW4iLCJ1c2VySWQiOjEsImV4cCI6MTc4Mjk5MDkxMn0.0-BJMKQJfHu-5mQWXPJC3vEgIHtVYc-DM1_1n4BmqIQ\"}}', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) TraeCN/1.107.1 Chrome/142.0.7444.235 Electron/39.2.7 Safari/537.36', 166, 1, NULL, '2026-07-01 19:15:12', '2026-07-01 19:15:12');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '手机号',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码(BCrypt加密)',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '用户' COMMENT '昵称',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '头像',
  `gender` tinyint NULL DEFAULT 0 COMMENT '性别 0未知 1男 2女',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '地址',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态 0禁用 1启用',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `phone`(`phone` ASC) USING BTREE,
  INDEX `idx_phone`(`phone` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '养宠用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, '18760018159', '$2a$10$dc7zAO0ty93/ypEVB9XyMecpb7aeDdetw5ytvxDiztaq9X390Bzc6', 'sherry', '', 1, '111', 1, '2026-07-01 19:06:44', '2026-07-01 19:06:44');

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_role`(`user_id` ASC, `role_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_role_id`(`role_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户角色关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_role
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
