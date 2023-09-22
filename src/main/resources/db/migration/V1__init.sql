CREATE TABLE notice
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    title        VARCHAR(255)          NULL,
    content      LONGTEXT              NULL,
    opening_time datetime              NULL,
    closing_time datetime              NULL,
    created_at   datetime              NULL,
    modified_at  datetime              NULL,
    deleted      BIT(1)                NOT NULL,
    CONSTRAINT pk_notice PRIMARY KEY (id)
);

CREATE TABLE attachment
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    notice_id    BIGINT                NULL,
    file_path    VARCHAR(255)          NULL,
    file_name    VARCHAR(255)          NULL,
    storage_type VARCHAR(255)          NULL,
    created_at   datetime              NULL,
    modified_at  datetime              NULL,
    deleted      BIT(1)                NOT NULL,
    CONSTRAINT pk_attachment PRIMARY KEY (id)
);

ALTER TABLE attachment
    ADD CONSTRAINT FK_ATTACHMENT_TO_TB_NOTICE FOREIGN KEY (notice_id) REFERENCES notice (id);