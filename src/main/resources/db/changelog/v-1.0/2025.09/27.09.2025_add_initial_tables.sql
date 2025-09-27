CREATE TABLE users (
                       id            UUID PRIMARY KEY,
                       username      VARCHAR(128),
                       email         VARCHAR(256),
                       password_hash VARCHAR(255),
                       full_name     VARCHAR(200),
                       enabled       BOOLEAN,
                       created_at    TIMESTAMPTZ,
                       updated_at    TIMESTAMPTZ
);

CREATE TABLE roles (
                       id   UUID PRIMARY KEY,
                       code VARCHAR(50),
                       name VARCHAR(120)
);

CREATE TABLE user_roles (
                            user_id UUID,
                            role_id UUID,
                            PRIMARY KEY (user_id, role_id),
                            FOREIGN KEY (user_id) REFERENCES users(id),
                            FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE key_store (
                           id         UUID PRIMARY KEY,
                           key_alias  VARCHAR(80),
                           active     BOOLEAN,
                           created_at TIMESTAMPTZ,
                           rotated_at TIMESTAMPTZ
);

CREATE TABLE cards (
                       id                UUID PRIMARY KEY,
                       user_id           UUID,
                       pan_encrypted     BYTEA,
                       pan_hash          CHAR(64),
                       pan_last4         CHAR(4),
                       exp_month         SMALLINT,
                       exp_year          SMALLINT,
                       status            VARCHAR(16),
                       balance           NUMERIC(19,2),
                       currency          CHAR(3),
                       encryption_key_id UUID,
                       owner_name        VARCHAR(100),
                       blocked_at        TIMESTAMPTZ,
                       expired_at        TIMESTAMPTZ,
                       created_at        TIMESTAMPTZ,
                       updated_at        TIMESTAMPTZ,
                       version           INTEGER,
                       FOREIGN KEY (user_id) REFERENCES users(id),
                       FOREIGN KEY (encryption_key_id) REFERENCES key_store(id)
);

CREATE TABLE card_status_history (
                                     id         UUID PRIMARY KEY,
                                     card_id    UUID,
                                     old_status VARCHAR(16),
                                     new_status VARCHAR(16),
                                     reason     VARCHAR(250),
                                     changed_by UUID,
                                     changed_at TIMESTAMPTZ,
                                     FOREIGN KEY (card_id) REFERENCES cards(id),
                                     FOREIGN KEY (changed_by) REFERENCES users(id)
);

CREATE TABLE card_action_requests (
                                      id           UUID PRIMARY KEY,
                                      card_id      UUID,
                                      user_id      UUID,
                                      type         VARCHAR(16),   -- BLOCK (в Java enum)
                                      status       VARCHAR(16),   -- PENDING/APPROVED/REJECTED/EXECUTED
                                      reason       VARCHAR(250),
                                      processed_by UUID,
                                      created_at   TIMESTAMPTZ,
                                      processed_at TIMESTAMPTZ,
                                      FOREIGN KEY (card_id) REFERENCES cards(id),
                                      FOREIGN KEY (user_id) REFERENCES users(id),
                                      FOREIGN KEY (processed_by) REFERENCES users(id)
);

CREATE TABLE transfers (
                           id           UUID PRIMARY KEY,
                           user_id      UUID,
                           from_card_id UUID,
                           to_card_id   UUID,
                           amount       NUMERIC(19,2),
                           currency     CHAR(3),
                           status       VARCHAR(16),   -- PENDING/COMPLETED/FAILED
                           failure_code VARCHAR(80),
                           failure_msg  VARCHAR(250),
                           created_at   TIMESTAMPTZ,
                           completed_at TIMESTAMPTZ,
                           FOREIGN KEY (user_id) REFERENCES users(id),
                           FOREIGN KEY (from_card_id) REFERENCES cards(id),
                           FOREIGN KEY (to_card_id) REFERENCES cards(id)
);

CREATE TABLE transactions (
                              id            UUID PRIMARY KEY,
                              card_id       UUID,
                              transfer_id   UUID,
                              type          VARCHAR(16),  -- DEBIT/CREDIT/ADJUSTMENT
                              amount        NUMERIC(19,2),
                              currency      CHAR(3),
                              balance_after NUMERIC(19,2),
                              description   VARCHAR(250),
                              created_at    TIMESTAMPTZ,
                              FOREIGN KEY (card_id) REFERENCES cards(id),
                              FOREIGN KEY (transfer_id) REFERENCES transfers(id)
);
