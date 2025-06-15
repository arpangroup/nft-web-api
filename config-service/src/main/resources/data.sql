
--Database connection
INSERT INTO config_properties
(config_key, config_value, application, profile, enum_values, value_type, label, info)
VALUES
('spring.datasource.url', 'jdbc:mysql://localhost:3306/nft', 'nft_app', 'default', NULL, 'STRING', 'Database Connection URL', NULL),
('spring.datasource.driverClassName', 'com.mysql.cj.jdbc.Driver', 'nft_app', 'default', NULL, 'STRING', 'JDBC Driver Class Name', NULL),
('spring.datasource.username', 'root', 'nft_app', 'default', NULL, 'STRING', 'Database Username', NULL),
('spring.datasource.password', 'password', 'nft_app', 'default', NULL, 'STRING', 'Database Password', NULL);

--Hibernate / JPA settings ---
INSERT INTO config_properties
(config_key, config_value, application, profile, enum_values, value_type, label, info)
VALUES
('spring.jpa.hibernate.ddl-auto', 'update', 'nft_app', 'default', 'none, validate, update, create, create-drop', 'STRING', 'DDL Auto Strategy', 'NONE: tells Hibernate not to create/alter schema'),
('spring.jpa.defer-datasource-initialization', 'true', 'nft_app', 'default', NULL, 'BOOLEAN', 'Defer DataSource Initialization', 'Hibernate to create/update schema first, then load data from data.sql'),
('spring.jpa.show-sql', 'false', 'nft_app', 'default', NULL, 'BOOLEAN', 'Show SQL in Logs', NULL);

--SQL init settings (Spring Boot 3.x)
INSERT INTO config_properties
(config_key, config_value, application, profile, enum_values, value_type, label, info)
VALUES
('spring.sql.init.mode', 'NEVER', 'nft_app', 'default', 'ALWAYS, NEVER, EMBEDDED', 'STRING', NULL, 'NEVER: Never run SQL initialization scripts, ALWAYS: Always run SQL initialization scripts, EMBEDDED: Run SQL initialization only if the database is an embedded one (like H2/HSQL/Derby)'),
('spring.sql.init.schema-locations', 'classpath:schema.sql', 'nft_app', 'default', NULL, 'STRING', NULL, NULL),
('spring.sql.init.data-locations', 'classpath:data.sql', 'nft_app', 'default', NULL, 'STRING', NULL, NULL);

--Optional: log SQL execution
INSERT INTO config_properties
(config_key, config_value, application, profile, enum_values, value_type, label, info)
VALUES
('logging.level.org.springframework.jdbc.datasource.init.ScriptUtils', 'DEBUG', 'nft_app', 'default', 'TRACE, DEBUG, INFO, WARN, ERROR, FATAL, OFF', 'STRING', NULL, NULL),
('logging.level.org.springframework.jdbc.datasource.init', 'DEBUG', 'nft_app', 'default', 'TRACE, DEBUG, INFO, WARN, ERROR, FATAL, OFF', 'STRING', NULL, NULL);

--Mail Settings
INSERT INTO config_properties
(config_key, config_value, application, profile, enum_values, value_type, label, info)
VALUES
('mail.host', 'smtp.gmail.com', 'nft_app', 'default', NULL, 'STRING', 'SMTP Server Host', NULL),
('mail.port', '587', 'nft_app', 'default', NULL, 'INT', 'SMTP Server Port', NULL),
('mail.username', 'cloud.arpan@gmail.com', 'nft_app', 'default', NULL, 'STRING', 'SMTP Username', NULL),
('mail.password', 'yrcu swoe gbpm lpwo', 'nft_app', 'default', NULL, 'STRING', 'SMTP Password', NULL),
('mail.smtp.auth', 'true', 'nft_app', 'default', NULL, 'BOOLEAN', 'Enable SMTP Authentication', NULL),
('mail.starttls.enable', 'true', 'nft_app', 'default', NULL, 'BOOLEAN', 'Enable STARTTLS Encryption', NULL);

-- Bonus Settings
INSERT INTO config_properties
(config_key, config_value, application, profile, enum_values, value_type, label, info)
VALUES
('bonus.signup.enable', 'true', 'nft_app', 'default', NULL, 'BOOLEAN', 'Enable Signup Bonus', NULL),
('bonus.signup.calculation-type', 'FLAT', 'nft_app', 'default', 'FLAT, PERCENTAGE', 'STRING', 'Signup Bonus Calculation Type', NULL),
('bonus.signup.flat-amount', '100', 'nft_app', 'default', NULL, 'INT', 'Signup Bonus Flat Amount', NULL),
('bonus.referral.enable', 'true', 'nft_app', 'default', NULL, 'BOOLEAN', 'Enable Referral Bonus', NULL),
('bonus.referral.calculation-type', 'FLAT', 'nft_app', 'default', 'FLAT, PERCENTAGE', 'STRING', 'Referral Bonus Calculation Type', NULL),
('bonus.referral.percentage-rate', '0.5', 'nft_app', 'default', 'FLAT, PERCENTAGE', 'DOUBLE', 'Referral Bonus Percentage Rate', NULL),
('bonus.referral.flat-amount', '300', 'nft_app', 'default', NULL, 'INT', 'Referral Bonus Flat Amount', NULL);