
/*

CREATE TABLE IF NOT EXISTS `url_whitelist` (
  `id`         int(11)        NOT NULL AUTO_INCREMENT,
  `client_id`  varchar(128)       NULL,
  `test_regex` varchar(128)   NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_client_id` (`client_id`),
  KEY `idx_test_regex` (`test_regex`)
);
*/


CREATE TABLE IF NOT EXISTS `url_whitelist` (
  `id`         int(11)        NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `client_id`  varchar(128)       NULL,
  `test_regex` varchar(128)   NOT NULL
);

