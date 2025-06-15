CREATE TABLE IF NOT EXISTS config_properties (
  id BIGINT NOT NULL AUTO_INCREMENT,
  application VARCHAR(50) NOT NULL,
  profile VARCHAR(10) NOT NULL,
  config_key VARCHAR(255) NOT NULL,
  config_value VARCHAR(255) DEFAULT NULL,
  enum_values VARCHAR(255) DEFAULT NULL,
  label VARCHAR(255) DEFAULT NULL,
  value_type VARCHAR(20) DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY UK_config_key_app_profile (config_key, application, profile)
);