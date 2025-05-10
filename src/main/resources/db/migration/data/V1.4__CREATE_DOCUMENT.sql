CREATE TABLE document (
                          id BIGSERIAL PRIMARY KEY,
                          user_id BIGINT NOT NULL,
                          filename VARCHAR(255),
                          content_type VARCHAR(255),
                          description TEXT,
                          data BYTEA,
                          CONSTRAINT fk_document_user FOREIGN KEY (user_id) REFERENCES users(id)
);