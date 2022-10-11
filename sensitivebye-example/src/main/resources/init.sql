CREATE TABLE `tb_user` (
   `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
   `username` varchar(20) NOT NULL,
   `mobile` varchar(20) DEFAULT NULL,
   `password` varchar(20) DEFAULT NULL,
   `email` varchar(30) DEFAULT NULL,
   `address` varchar(64) DEFAULT NULL,
   PRIMARY KEY USING BTREE (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4  ;