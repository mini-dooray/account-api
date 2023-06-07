use nhn_academy_17;

DROP TABLE IF EXISTS `account`;

CREATE TABLE `account`
(
    `account_seq` bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `id`          nvarchar(12) NOT NULL,
    `password`    nvarchar(60) NOT NULL,
    `name`        nvarchar(10) NOT NULL
);

DROP TABLE IF EXISTS `additional_info`;

CREATE TABLE `additional_info`
(
    `additional_info_seq` bigint NOT NULL  AUTO_INCREMENT PRIMARY KEY,
    `account_seq`         bigint NOT NULL,
    `email`               nvarchar(30) NOT NULL,
    `phone_number`        nvarchar(13) NOT NULL
);

DROP TABLE IF EXISTS `account_status`;

CREATE TABLE `account_status`
(
    `account_status_seq` bigint NOT NULL  AUTO_INCREMENT PRIMARY KEY,
    `account_seq`        bigint       NOT NULL,
    `last_access_date`   date NULL,
    `status`             tinyint(1) NULL
);

-- account 테이블에 데이터 삽입
INSERT INTO account (account_seq, id, password, name)
VALUES (1, 'user1', 'password1', 'John'),
       (2, 'user2', 'password2', 'Alice'),
       (3, 'user3', 'password3', 'Bob'),
       (4, 'user4', 'password4', 'Jane'),
       (5, 'user5', 'password5', 'Mike');

-- additional_info 테이블에 데이터 삽입
INSERT INTO additional_info (additional_info_seq, account_seq, email, phone_number)
VALUES (1, 1, 'user1@example.com', '1234567890'),
       (2, 2, 'user2@example.com', '2345678901'),
       (3, 3, 'user3@example.com', '3456789012'),
       (4, 4, 'user4@example.com', '4567890123'),
       (5, 5, 'user5@example.com', '5678901234');

-- account_status 테이블에 데이터 삽입
INSERT INTO account_status (account_status_seq, account_seq, last_access_date, status)
VALUES (1, 1, '2023-05-01', 1),
       (2, 2, '2023-05-02', 2),
       (3, 3, '2023-05-03', 1),
       (4, 4, '2023-05-04', 3),
       (5, 5, '2023-05-05', 2);
