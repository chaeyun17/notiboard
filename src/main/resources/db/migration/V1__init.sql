CREATE TABLE member
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    nickname    VARCHAR(255)          NULL,
    username    VARCHAR(255)          NULL,
    password    VARCHAR(255)          NULL,
    deleted     BIT(1)                NOT NULL DEFAULT 0,
    created_at  datetime              NULL,
    modified_at datetime              NULL,
    CONSTRAINT pk_member PRIMARY KEY (id)
);

ALTER TABLE member
    ADD CONSTRAINT uc_member_username UNIQUE (username);

CREATE TABLE notice
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    title        VARCHAR(255)          NULL,
    content      LONGTEXT              NULL,
    opening_time datetime              NULL,
    closing_time datetime              NULL,
    created_at   datetime              NULL,
    modified_at  datetime              NULL,
    deleted      BIT(1)                NOT NULL DEFAULT 0,
    created_by   BIGINT                NOT NULL,
    CONSTRAINT pk_notice PRIMARY KEY (id)
);

ALTER TABLE notice
    ADD CONSTRAINT FK_NOTICE_TO_MEMBER FOREIGN KEY (created_by) REFERENCES member (id);

CREATE TABLE attachment
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    notice_id    BIGINT                NULL,
    file_path    VARCHAR(255)          NULL,
    file_name    VARCHAR(255)          NULL,
    storage_type VARCHAR(255)          NULL,
    created_at   datetime              NULL,
    modified_at  datetime              NULL,
    deleted      BIT(1)                NOT NULL DEFAULT 0,
    CONSTRAINT pk_attachment PRIMARY KEY (id)
);

ALTER TABLE attachment
    ADD CONSTRAINT FK_ATTACHMENT_TO_TB_NOTICE FOREIGN KEY (notice_id) REFERENCES notice (id);