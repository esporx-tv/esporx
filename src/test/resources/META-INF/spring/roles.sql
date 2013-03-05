CREATE TABLE `roles` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role` varchar(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_role_role` (`role`)
);
INSERT INTO roles(id, role) VALUES(1,'ROLE_ADMIN'),(2,'ROLE_USER');