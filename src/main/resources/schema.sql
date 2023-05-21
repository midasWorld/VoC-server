CREATE TABLE company
(
    id                 BIGINT       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    registration_no    VARCHAR(12)  NOT NULL COMMENT '사업자등록번호',
    name               VARCHAR(50)  NOT NULL COMMENT '회사명',
    type               VARCHAR(30)  NOT NULL COMMENT '회사 타입 (고객사 | 운송사)',
    phone              VARCHAR(20)  NOT NULL COMMENT '회사 번호',
    created_at         DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    updated_at         DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP()
);

CREATE TABLE person
(
    id          BIGINT       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name        VARCHAR(50)  NOT NULL COMMENT '이름',
    company_id  BIGINT       NOT NULL COMMENT '회사 FK',
    phone       VARCHAR(20)  NULL     COMMENT '전화 번호',
    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    updated_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    FOREIGN KEY (company_id) REFERENCES company (id)
);

CREATE TABLE voc
(
    id                  BIGINT       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    due_type            VARCHAR(10)  NOT NULL COMMENT '귀책 타입 (고객사 | 운송사)',
    due_target_id       BIGINT       NOT NULL COMMENT '귀책 대상 (사람)',
    due_reason          VARCHAR(255) NOT NULL COMMENT '귀책 이유',
    created_at          DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    updated_at          DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    FOREIGN KEY (due_target_id) REFERENCES person (id)
);

CREATE TABLE penalty
(
    id          BIGINT       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    amount      DECIMAL      NOT NULL DEFAULT 0     COMMENT '패널티 금액',
    voc_id      BIGINT       NOT NULL               COMMENT 'VOC FK',
    confirmed   BOOLEAN      NOT NULL DEFAULT false COMMENT '귀책 인정 승인 여부',
    objected    BOOLEAN      NOT NULL DEFAULT false COMMENT '대상 이의제기 여부',
    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    updated_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    FOREIGN KEY (voc_id) REFERENCES voc (id)
);

CREATE TABLE compensation
(
    id          BIGINT       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    amount      DECIMAL      NOT NULL DEFAULT  0 COMMENT '배상 금액',
    voc_id      BIGINT       NOT NULL            COMMENT 'VOC FK',
    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    updated_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    FOREIGN KEY (voc_id) REFERENCES voc (id)
);