
CREATE TABLE IF NOT EXISTS `url_whitelist` (
  `id`         int(11)        NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `client_id`  varchar(128)       NULL,
  `test_regex` varchar(128)   NOT NULL
);

