SET @@foreign_key_checks = 0;
TRUNCATE TABLE attachment;
TRUNCATE TABLE post_stats;
TRUNCATE TABLE notice;
TRUNCATE TABLE member;
SET @@foreign_key_checks = 1;