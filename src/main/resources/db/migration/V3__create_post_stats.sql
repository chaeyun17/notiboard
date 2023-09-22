CREATE TABLE post_stats
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    view_count BIGINT                NULL,
    deleted    BIT(1)                NULL,
    CONSTRAINT pk_post_stats PRIMARY KEY (id)
);

ALTER TABLE notice
    ADD COLUMN post_stats_id BIGINT NOT NULL;

ALTER TABLE notice
    ADD CONSTRAINT FK_NOTICE_TO_POST_STATS FOREIGN KEY (post_stats_id) REFERENCES post_stats (id);

