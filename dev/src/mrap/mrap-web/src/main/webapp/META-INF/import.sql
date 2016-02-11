-- SQL statements which are executed at application startup if hibernate.hbm2ddl.auto is 'create' or 'create-drop'
INSERT INTO `mrap`.`role` (`id`, `description`, `name`) VALUES (1, 'IWM Application Administrator', 'admin');
INSERT INTO `mrap`.`user` (`id`, `email`, `enabled`, `fname`, `lname`, `password`, `uid`) 
							VALUES (1, 'nadia@masterlink.com', 1, 'Administrator', 'Administrator', 'admin', 'admin');
INSERT INTO `mrap`.`user_role` (`user_id`, `role_id`) VALUES (1, 1);