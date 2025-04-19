-- Sequence
DROP SEQUENCE IF EXISTS rosalind_user_seq;
CREATE SEQUENCE rosalind_user_seq INCREMENT BY 1 START WITH 1 NO CYCLE;

-- Table
CREATE TABLE rosalind_user (
    rosalind_user_id bigint PRIMARY KEY,
    service_type varchar(50),
    provider_code varchar(50),
    provider_id varchar(255),
    user_uuid varchar(512),
    user_name varchar(50),
    email varchar(255),
    profile_url varchar(512),
    created_at bigint NOT NULL,
    updated_at bigint NOT NULL
);

-- Indexes
CREATE UNIQUE INDEX rosalind_user_uk01 ON rosalind_user (provider_code, provider_id);
CREATE UNIQUE INDEX rosalind_user_uk02 ON rosalind_user (user_uuid);
