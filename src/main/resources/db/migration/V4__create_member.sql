CREATE TABLE member
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    nickname    VARCHAR(255)          NULL,
    username    VARCHAR(255)          NULL,
    password    VARCHAR(255)          NULL,
    deleted     BIT(1)                NULL,
    created_at  datetime              NULL,
    modified_at datetime              NULL,
    CONSTRAINT pk_member PRIMARY KEY (id)
);

ALTER TABLE member
    ADD CONSTRAINT uc_member_username UNIQUE (username);