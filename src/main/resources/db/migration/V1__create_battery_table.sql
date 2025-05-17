CREATE TABLE battery (
                         id UUID PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         post_code INTEGER NOT NULL,
                         watt_capacity INTEGER NOT NULL
);

CREATE INDEX idx_post_code ON battery (post_code);
CREATE INDEX idx_watt_capacity ON battery (watt_capacity);